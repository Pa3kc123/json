package sk.pa3kc.json;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import sk.pa3kc.json.ann.JsonOptions;

@SuppressWarnings({"unchecked", "rawtypes"})
final class JsonDecoder {
    static {
        JsonDecoders.addDecoder(CharSequence.class, json -> {
            if (json.charAt(0) != '"' || json.charAt(json.length() - 1) != '"') {
                throw new JsonException("Invalid json string");
            }

            return json.substring(1, json.length() - 1);
        });
        JsonDecoders.addDecoder(Iterable.class, json -> null);

        //TODO
        JsonDecoders.addDecoder(Object.class, json -> null);
    }

    private JsonDecoder() { }

    @NotNull
    static <T> T decodeObject(
        JsonTokener tokener,
        Class<T> cls
    ) throws IOException, ReflectiveOperationException {
        if (tokener.lastChar() != '{') {
            throw new JsonException("Invalid start of object");
        }

        final Map<String, Field> fieldMap = Utils.getFields(cls, null);
        for (Class<?> mCls = cls.getSuperclass(); Utils.isValidSuperclass(mCls); mCls = mCls.getSuperclass()) {
            fieldMap.putAll(Utils.getFields(mCls, null));
        }

        final JsonOptions options = cls.getAnnotation(JsonOptions.class);
        final T inst = Utils.createInstance(cls);

        if (fieldMap.isEmpty()) {
            return inst;
        }

        char c;
        do {
            c = tokener.nextClearChar();
            if (c != '"') {
                throw new JsonException("Missing key declaration");
            }

            final String key = JsonDecoder.decodeString(tokener);
            final Field field = fieldMap.get(key);

            c = tokener.nextClearChar();

            if (c != ':') {
                throw new JsonException("Missing semicolon between key and value");
            }

            tokener.nextClearChar();

            if (field == null) {
                if (options == null || !options.ignoreMissing()) {
                    throw new JsonException(cls.getCanonicalName() + " is missing field for json key '" + key + "'");
                }

                JsonDecoder.ignoreValue(tokener);

                c = tokener.nextClearChar();
                if (",}".indexOf(c) == -1) {
                    throw new JsonException("Missing object divider/end (',' or '}')");
                }

                continue;
            }

            final Method setter;
            try {
                setter = cls.getMethod("set" + Utils.capitalize(field.getName()), field.getType());
            } catch (NoSuchMethodException e) {
                throw new JsonException(cls.getCanonicalName() + " is missing setter for");
            }

            final boolean isMap = Map.class.isAssignableFrom(field.getType());
            final boolean isColl = Collection.class.isAssignableFrom(field.getType());

            if (isMap || isColl) {
                final ParameterizedType genType = (ParameterizedType)field.getGenericType();
                final Type[] genTypes = genType.getActualTypeArguments();

                String typeName = genTypes[isMap ? 1 : 0].getTypeName();

                int counter = 0;
                while (typeName.endsWith("[]")) {
                    typeName = typeName.substring(0, typeName.length() - 2);
                    counter++;
                }

                Class<?> genClass;
                try {
                    genClass = Class.forName(typeName);
                } catch (ClassNotFoundException e) {
                    throw new JsonException("Unable to find field class " + genTypes[isMap ? 1 : 0].getTypeName());
                }

                for (int i = 0; i < counter; i++) {
                    genClass = Array.newInstance(genClass, 0).getClass();
                }

                setter.invoke(inst, JsonDecoder.decodeValue(tokener, field.getType(), genClass));
            } else {
                setter.invoke(inst, JsonDecoder.decodeValue(tokener, field.getType(), null));
            }

            c = tokener.nextClearChar();
            if (",}".indexOf(c) == -1) {
                throw new JsonException("Missing object divider/end (',' or '}')");
            }
        } while (c != '}');

        return inst;
    }

    static <T extends Map> T decodeMap(
        JsonTokener token,
        Class<T> mapCls,
        Class<?> valueCls
    ) throws IOException, ReflectiveOperationException {
        if (token.lastChar() != '{') {
            throw new JsonException("Invalid start of object");
        }

        if (Modifier.isInterface(mapCls.getModifiers())) {
            mapCls = (Class<T>) HashMap.class;
        }

        if (Modifier.isAbstract(mapCls.getModifiers())) {
            throw new JsonException("Unable to instantiate abstract class");
        }

        final T map = Utils.createInstance(mapCls);

        char c;
        do {
            c = token.nextClearChar();

            if (c != '"') {
                throw new JsonException("Missing key declaration");
            }

            final String key = JsonDecoder.decodeString(token);

            c = token.nextClearChar();

            if (c != ':') {
                throw new JsonException("Missing semicolon between key and value");
            }

            token.nextClearChar();

            map.put(key, JsonDecoder.decodeValue(token, valueCls, null));

            c = token.nextClearChar();
            if (",}".indexOf(c) == -1) {
                throw new JsonException("Missing object divider/end (',' or '}')");
            }
        } while (c != '}');

        return map;
    }

    static <T> T[] decodeArray(
        JsonTokener tokener,
        Class<T> cls
    ) throws IOException, ReflectiveOperationException {
        if (tokener.lastChar() != '[') {
            throw new JsonException("Invalid start of array");
        }

        final Collection<T> result = new ArrayList<>();

        char c;
        do {
            tokener.nextClearChar();
            final T value = (T) JsonDecoder.decodeValue(tokener, cls, null);
            result.add(value);

            c = tokener.nextClearChar();
            if (",]".indexOf(c) == -1) {
                throw new JsonException("Missing array divider/end (',' or ']')");
            }
        } while (c != ']');

        return result.toArray((T[])Array.newInstance(cls, 0));
    }

    static <T extends Collection> T decodeCollection(
        JsonTokener tokener,
        Class<T> collCls,
        Class<?> valueCls
    ) throws IOException, ReflectiveOperationException {
        if (tokener.lastChar() != '[') {
            throw new JsonException("Invalid start of array");
        }

        if (Modifier.isInterface(collCls.getModifiers())) {
            collCls = (Class<T>) ArrayList.class;
        }

        if (Modifier.isAbstract(collCls.getModifiers())) {
            throw new JsonException("Unable to instantiate abstract class");
        }

        final T result = Utils.createInstance(collCls);

        char c;
        do {
            tokener.nextClearChar();
            final Object value = JsonDecoder.decodeValue(tokener, valueCls, null);
            result.add(value);

            c = tokener.nextClearChar();
            if (",]".indexOf(c) == -1) {
                throw new JsonException("Missing array divider/end (',' or ']')");
            }
        } while (c != ']');


        return result;
    }

    static Object decodeValue(
        @NotNull JsonTokener token,
        @NotNull Class<?> fieldClass,
        @Nullable Class<?> valueClass
    ) throws ReflectiveOperationException, IOException {
        char c = token.lastChar();

        switch (c) {
            case '"':
                return decodeString(token);

            case 't':
            case 'f':
            case 'n':
                return decodeBoolOrNull(token);

            case '{':
                if (Map.class.isAssignableFrom(fieldClass)) {
                    return decodeMap(token, (Class<? extends Map>) fieldClass, valueClass);
                } else {
                    return decodeObject(token, fieldClass);
                }

            case '[':
                if (Collection.class.isAssignableFrom(fieldClass)) {
                    return decodeCollection(token, (Class<? extends Collection>) fieldClass, valueClass);
                } else {
                    return decodeArray(token, fieldClass.getComponentType());
                }

            default:
                if (token.lastChar() == '-' || Character.isDigit(c)) {
                    return decodeNumber(token);
                } else {
                    throw new JsonException("Invalid char '" + c + "'");
                }
        }
    }

    @NotNull
    static String decodeString(JsonTokener token) throws IOException {
        final StringBuilder builder = new StringBuilder();

        for (char c = token.nextChar(); c != '"'; c = token.nextChar()) {
            builder.append(c);
        }

        return builder.toString();
    }

    @Nullable
    static Boolean decodeBoolOrNull(JsonTokener token) throws IOException {
        final StringBuilder builder = new StringBuilder(4).append(token.lastChar());

        final int length = token.lastChar() == 'f' ? 5 : 4;

        for (int i = 1; i < length; i++) {
            builder.append(token.nextChar());
        }

        final String result = builder.toString();

        switch (result) {
            case "null":
                return null;

            case "true":
                return true;

            case "false":
                return false;

            default:
                throw new JsonException("Invalid value '" + result + "'");
        }
    }

    @NotNull
    static Number decodeNumber(JsonTokener token) throws IOException {
        final StringBuilder builder = new StringBuilder();

        char c = token.lastChar();

        if (c == '-') {
            builder.append(c);
            c = token.nextChar();
        }

        do {
            builder.append(c);
            c = token.nextChar();
        } while (c >= '0' && c <= '9');

        if (".eE".indexOf(c) == -1) {
            token.goBack();
            return Long.parseLong(builder.toString());
        }

        final boolean isDecimal = c == '.';

        if (isDecimal) {
            builder.append(c);
            c = token.nextChar();

            while (c >= '0' && c <= '9') {
                builder.append(c);
                c = token.nextChar();
            }
        }

        if (c == 'e' || c == 'E') {
            builder.append(c);
            c = token.nextChar();

            if (c == '+' || c == '-') {
                if (c == '-') {
                    builder.append(c);
                }
                c = token.nextChar();
            } else {
                throw new JsonException("Invalid number");
            }

            while (c >= '0' && c <= '9') {
                builder.append(c);
                c = token.nextClearChar();
            }
        }

        token.goBack();
        return isDecimal ? Double.parseDouble(builder.toString()) : Long.parseLong(builder.toString());
    }

    //region Helper functions
    static void ignoreValue(JsonTokener token) throws IOException {
        switch (token.lastChar()) {
            case '"':
                ignoreString(token);
                return;

            case 't':
            case 'f':
            case 'n':
                ignoreBoolOrNull(token);
                return;

            case '{':
                ignoreMap(token);
                return;

            case '[':
                ignoreArray(token);
                return;

            default:
                if (token.lastChar() == '-' || Character.isDigit(token.lastChar())) {
                    ignoreNumber(token);
                } else {
                    throw new JsonException("Invalid char '" + token.lastChar() + "'");
                }
        }
    }

    static void ignoreString(JsonTokener token) throws IOException {
        ignore(token, '"', '"');
    }
    static void ignoreBoolOrNull(JsonTokener token) throws IOException {
        for (int i = token.lastChar() == 'f' ? 5 : 4; i > 0; i--){
            token.nextChar();
        }
    }
    static void ignoreNumber(JsonTokener token) throws IOException {
        if (token.lastChar() == '-') {
            token.nextChar();
        }

        do {
            token.nextChar();
        } while (token.lastChar() >= '0' && token.lastChar() <= '9');

        if (".eE".indexOf(token.lastChar()) == -1) {
            token.goBack();
            return;
        }

        final boolean isDecimal = token.lastChar() == '.';

        if (isDecimal) {
            token.nextChar();

            while (token.lastChar() >= '0' && token.lastChar() <= '9') {
                token.nextChar();
            }
        }

        if ("eE".indexOf(token.lastChar()) != -1) {
            if ("+-".indexOf(token.nextChar()) != -1) {
                token.nextChar();
            } else {
                throw new JsonException("Invalid number");
            }

            while (token.lastChar() >= '0' && token.lastChar() <= '9') {
                token.nextClearChar();
            }
        }

        token.goBack();
    }
    static void ignoreMap(JsonTokener token) throws IOException {
        ignore(token, '{', '}');
    }
    static void ignoreArray(JsonTokener token) throws IOException {
        ignore(token, '[', ']');
    }
    static void ignore(JsonTokener token, char b1, char b2) throws IOException {
        if (b1 != b2) {
            int i = 1;
            do {
                char c = token.nextClearChar();
                i += c == b1 ? 1 : c == b2 ? -1 : 0;
            } while (i != 0);
        } else {
            char c;
            do {
                c = token.nextClearChar();
            } while (c != b1);
        }
    }
    //endregion
}
