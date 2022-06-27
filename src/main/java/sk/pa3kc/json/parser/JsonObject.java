package sk.pa3kc.json.parser;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import sk.pa3kc.json.JsonException;
import sk.pa3kc.json.JsonParsers;
import sk.pa3kc.json.JsonTokener;
import sk.pa3kc.json.ReflectUtils;
import sk.pa3kc.json.ann.JsonOptions;

public class JsonObject extends JsonParser {
    @Override
    public @NotNull Object decode(@NotNull JsonTokener tokener, @NotNull Type cls) throws IOException, JsonException {
        final Class<?> rawType = ReflectUtils.getClassFromType(cls);

        final JsonOptions options = rawType.getAnnotation(JsonOptions.class);

        final Map<String, Field> fields = ReflectUtils.getFields(rawType, options);

        final Object instance = ReflectUtils.createInstance(rawType);

        char c = tokener.nextClearChar();

        if (c != '{') {
            throw new JsonException("Not an object");
        }

        do {
            final String key = tokener.readString(false);

            if (tokener.nextClearChar() != ':') {
                throw new JsonException("Invalid character");
            }

            final Field field = fields.get(key);

            if (field == null) {
                if (options != null && options.strict()) {
                    throw new JsonException("No key");
                } else {
                    tokener.skip();

                    c = tokener.nextClearChar();

                    continue;
                }
            }

            final Type fieldType = field.getGenericType();
            final Class<?> fieldCls = ReflectUtils.getClassFromType(fieldType);

            final Method setter = ReflectUtils.getSetter(rawType, field);

            try {
                setter.invoke(
                    instance,
                    JsonParsers
                        .get(fieldCls)
                        .decode(tokener, fieldType)
                );
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new JsonException("Error while invoking " + rawType.getCanonicalName() + '#' + setter.getName(), e);
            }
            c = tokener.nextClearChar();
        } while (c != '}');

        return instance;
    }

    @Override
    public void encode(@NotNull Object value, @NotNull StringBuilder output) {
        final Class<?> cls = value.getClass();

        final JsonOptions options = cls.getAnnotation(JsonOptions.class);

        final Map<String, Field> fields = ReflectUtils.getFields(cls, options);

        output.append('{');

        final Iterator<String> keys = fields.keySet().iterator();
        while (keys.hasNext()) {
            final String key = keys.next();

            JsonParsers.get(String.class).encode(key, output);

            final Field val = fields.get(key);

            final Method getter = ReflectUtils.getGetter(cls, val);

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

            JsonParsers.get(val.getType()).encode(getterValue, output);

            if (keys.hasNext()) {
                output.append(',');
            }
        }

        output.append('}');
    }
}
