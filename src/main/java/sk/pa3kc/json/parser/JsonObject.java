package sk.pa3kc.json.parser;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import sk.pa3kc.json.JsonException;
import sk.pa3kc.json.JsonParsers;
import sk.pa3kc.json.JsonTokener;
import sk.pa3kc.json.ReflectUtils;
import sk.pa3kc.json.ann.JsonOptions;
import sk.pa3kc.json.ann.JsonValueFormat;

public class JsonObject extends JsonParser {
    @Override
    public @Nullable Object decode(@NotNull JsonTokener tokener, @NotNull Type cls, @Nullable Object extras) throws IOException, JsonException {
        final Class<?> rawType = ReflectUtils.getClassFromType(cls);

        if (Object.class.equals(rawType)) {
            tokener.skip();
            return null;
        }

        final JsonOptions options = rawType.getAnnotation(JsonOptions.class);

        if (rawType.isEnum()) {
            final String str = tokener.readString();

            if (str == null) {
                return null;
            }

            try {
                final Method decoder = rawType.getMethod("forValue", String.class);
                return decoder.invoke(null, str);
            } catch (ReflectiveOperationException e) {
                throw new JsonException("Unable to decode " + rawType.getCanonicalName(), e);
            }
        }

        final Map<String, Field> fields = ReflectUtils.getFields(rawType, options);

        final Object instance = ReflectUtils.createInstance(rawType);

        char c = tokener.nextClearChar();

        if (c != '{') {
            throw new JsonException("Not an object", tokener.getOffset());
        }

        do {
            final String key = tokener.readString();

            if (tokener.nextClearChar() != ':') {
                throw new JsonException("Invalid character", tokener.getOffset());
            }

            final Field field = fields.get(key);

            if (field == null) {
                if (options != null && options.strict()) {
                    throw new JsonException("No key", tokener.getOffset());
                } else {
                    tokener.skip();
                }
            } else {
                final JsonValueFormat extra = field.getAnnotation(JsonValueFormat.class);

                final Type fieldType = field.getGenericType();
                final Class<?> fieldCls = ReflectUtils.getClassFromType(fieldType);

                final Method setter = ReflectUtils.getSetter(rawType, field);

                try {
                    setter.invoke(
                        instance,
                        JsonParsers
                            .get(fieldCls)
                            .decode(tokener, fieldType, extra)
                    );
                } catch (IllegalAccessException e) {
                    throw new JsonException("Invalid access to " + fieldCls.getCanonicalName() + "#" + setter.getName());
                } catch (IllegalArgumentException e) {
                    throw new JsonException("Getter " + fieldCls.getCanonicalName() + "#" + setter.getName() + " must have no arguments");
                } catch (InvocationTargetException e) {
                    throw new JsonException("Getter " + fieldCls.getCanonicalName() + "#" + setter.getName() + " threw and exception");
                }
            }

            c = tokener.nextClearChar();
            if (c != ',' && c != '}') {
                throw new JsonException("Invalid symbol", tokener.getOffset());
            }
        } while (c != '}');

        return instance;
    }

    @Override
    public void encode(@NotNull Object value, @NotNull StringBuilder output, @Nullable Object extras) throws JsonException {
        final Class<?> cls = value.getClass();

        final JsonOptions options = cls.getAnnotation(JsonOptions.class);

        if (cls.isEnum()) {
            try {
                final Method encoder = cls.getMethod("toValue");
                output.append('"').append(encoder.invoke(value)).append('"');
                return;
            } catch (ReflectiveOperationException e) {
                throw new JsonException("Unable to encode " + cls.getCanonicalName(), e);
            }
        }

        final Map<String, Field> fields = ReflectUtils.getFields(cls, options);

        output.append('{');

        final Iterator<String> keys = fields.keySet().iterator();
        while (keys.hasNext()) {
            final String key = keys.next();
            final Field field = fields.get(key);
            final JsonValueFormat jvf = field.getAnnotation(JsonValueFormat.class);

            final Method getter = ReflectUtils.getGetter(cls, field);

            Object getterValue;
            try {
                getterValue = getter.invoke(value);
            } catch (IllegalAccessException e) {
                throw new JsonException("Invalid access to " + cls.getCanonicalName() + "#" + getter.getName());
            } catch (IllegalArgumentException e) {
                throw new JsonException("Getter " + cls.getCanonicalName() + "#" + getter.getName() + " must have no arguments");
            } catch (InvocationTargetException e) {
                throw new JsonException("Getter " + cls.getCanonicalName() + "#" + getter.getName() + " threw and exception");
            }

            if (getterValue == null) {
                continue;
            }

            JsonParsers.get(String.class).encode(key, output, null);
            output.append(':');
            JsonParsers.get(field.getType()).encode(getterValue, output, jvf);

            if (keys.hasNext()) {
                output.append(',');
            }
        }

        output.append('}');
    }
}
