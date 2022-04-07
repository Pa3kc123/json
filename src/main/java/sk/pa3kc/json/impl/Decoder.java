package sk.pa3kc.json.impl;

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
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import sk.pa3kc.json.JsonException;
import sk.pa3kc.json.JsonTokener;
import sk.pa3kc.json.Utils;
import sk.pa3kc.json.ann.JsonOptions;

public class Decoder {
    private Decoder() { }

    @NotNull
    public static <T> T decodeObject(
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

            final String key = decodeString(tokener);
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

                Ignorer.ignoreValue(tokener);

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
                final Type compType = genType.getActualTypeArguments()[isMap ? 1 : 0];

                final Class<?> genClass;
                if (compType instanceof Class<?>) {
                    genClass = (Class<?>)compType;
                } else {
                    String typeName = compType.getTypeName();

                    int counter = 0;
                    while (typeName.endsWith("[]")) {
                        typeName = typeName.substring(0, typeName.length() - 2);
                        counter++;
                    }

                    Class<?> result;
                    try {
                        result = Class.forName(typeName);
                    } catch (ClassNotFoundException e) {
                        throw new JsonException("Unable to find field class " + typeName);
                    }

                    for (int i = 0; i < counter; i++) {
                        result = Array.newInstance(result, 0).getClass();
                    }

                    genClass = result;
                }

                setter.invoke(inst, decodeValue(tokener, field.getType(), genClass));
            } else {
                setter.invoke(inst, decodeValue(tokener, field.getType(), null));
            }

            c = tokener.nextClearChar();
            if (",}".indexOf(c) == -1) {
                throw new JsonException("Missing object divider/end (',' or '}')");
            }
        } while (c != '}');

        return inst;
    }

    public static <T extends Map> T decodeMap(
        JsonTokener tokener,
        Class<T> mapCls,
        Class<?> valueCls
    ) throws IOException, ReflectiveOperationException {
        if (tokener.lastChar() != '{') {
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
            c = tokener.nextClearChar();

            if (c != '"') {
                throw new JsonException("Missing key declaration");
            }

            final String key = decodeString(tokener);

            c = tokener.nextClearChar();

            if (c != ':') {
                throw new JsonException("Missing semicolon between key and value");
            }

            tokener.nextClearChar();

            map.put(key, decodeValue(tokener, valueCls, null));

            c = tokener.nextClearChar();
            if (",}".indexOf(c) == -1) {
                throw new JsonException("Missing object divider/end (',' or '}')");
            }
        } while (c != '}');

        return map;
    }

    public static <T> T[] decodeArray(
        JsonTokener tokener,
        Class<T> cls
    ) throws IOException, ReflectiveOperationException {
        if (tokener.lastChar() != '[') {
            throw new JsonException("Invalid start of array");
        }

        final List<T> result = new ArrayList<>();

        char c;
        do {
            tokener.nextClearChar();
            final T value = (T) decodeValue(tokener, cls, null);
            result.add(value);

            c = tokener.nextClearChar();
            if (",]".indexOf(c) == -1) {
                throw new JsonException("Missing array divider/end (',' or ']')");
            }
        } while (c != ']');

        return result.toArray((T[])Array.newInstance(cls, 0));
    }

    public static <T extends Collection> T decodeCollection(
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
            final Object value = decodeValue(tokener, valueCls, null);
            result.add(value);

            c = tokener.nextClearChar();
            if (",]".indexOf(c) == -1) {
                throw new JsonException("Missing array divider/end (',' or ']')");
            }
        } while (c != ']');


        return result;
    }

    static Object decodeValue(
        @NotNull JsonTokener tokener,
        @NotNull Class<?> fieldClass,
        @Nullable Class<?> valueClass
    ) throws ReflectiveOperationException, IOException {
        char c = tokener.lastChar();

        switch (c) {
            case '"':
                return decodeString(tokener);

            case 't':
            case 'f':
            case 'n':
                return decodeBoolOrNull(tokener);

            case '{':
                if (Map.class.isAssignableFrom(fieldClass)) {
                    return decodeMap(tokener, (Class<? extends Map>) fieldClass, valueClass);
                } else {
                    return decodeObject(tokener, fieldClass);
                }

            case '[':
                if (Collection.class.isAssignableFrom(fieldClass)) {
                    return decodeCollection(tokener, (Class<? extends Collection>) fieldClass, valueClass);
                } else {
                    return decodeArray(tokener, fieldClass.getComponentType());
                }

            default:
                if (tokener.lastChar() == '-' || Character.isDigit(c)) {
                    return decodeNumber(tokener);
                } else {
                    throw new JsonException("Invalid char '" + c + "'");
                }
        }
    }

    @NotNull
    static String decodeString(JsonTokener tokener) throws IOException {
        final StringBuilder builder = new StringBuilder();

        for (char c = tokener.nextChar(); c != '"' && tokener.lastChar() != '\\' ; c = tokener.nextChar()) {
            builder.append(c);
        }

        return builder.toString();
    }

    @Nullable
    static Boolean decodeBoolOrNull(JsonTokener tokener) throws IOException {
        final StringBuilder builder = new StringBuilder(4).append(tokener.lastChar());

        final int length = tokener.lastChar() == 'f' ? 5 : 4;

        for (int i = 1; i < length; i++) {
            builder.append(tokener.nextChar());
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
    static Number decodeNumber(JsonTokener tokener) throws IOException {
        final StringBuilder builder = new StringBuilder();

        char c = tokener.lastChar();

        if (c == '-') {
            builder.append(c);
            c = tokener.nextChar();
        }

        do {
            builder.append(c);
            c = tokener.nextChar();
        } while (c >= '0' && c <= '9');

        if (".eE".indexOf(c) == -1) {
            tokener.goBack();
            return Long.parseLong(builder.toString());
        }

        final boolean isDecimal = c == '.';

        if (isDecimal) {
            builder.append(c);
            c = tokener.nextChar();

            while (c >= '0' && c <= '9') {
                builder.append(c);
                c = tokener.nextChar();
            }
        }

        if (c == 'e' || c == 'E') {
            builder.append(c);
            c = tokener.nextChar();

            if (c == '+' || c == '-') {
                if (c == '-') {
                    builder.append(c);
                }
                c = tokener.nextChar();
            } else {
                throw new JsonException("Invalid number");
            }

            while (c >= '0' && c <= '9') {
                builder.append(c);
                c = tokener.nextClearChar();
            }
        }

        tokener.goBack();
        return isDecimal ? Double.parseDouble(builder.toString()) : Long.parseLong(builder.toString());
    }
}
