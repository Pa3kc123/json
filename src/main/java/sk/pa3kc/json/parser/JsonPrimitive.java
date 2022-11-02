package sk.pa3kc.json.parser;

import java.io.IOException;
import java.lang.reflect.Type;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import sk.pa3kc.json.JsonException;
import sk.pa3kc.json.JsonTokener;
import sk.pa3kc.json.ReflectUtils;

public class JsonPrimitive extends JsonParser {
    @Override
    public @Nullable Object decode(@NotNull JsonTokener tokener, @NotNull Type cls, @Nullable Object extras) throws IOException, JsonException {
        final Class<?> rawType = ReflectUtils.getClassFromType(cls);

        if (rawType == boolean.class || rawType == Boolean.class) {
            final String bool = tokener.readBoolean();
            return bool != null ? Boolean.parseBoolean(bool) : bool;
        } else {
            final String num = tokener.readNumber();

            if (num == null) {
                return null;
            }

            if (rawType == byte.class || rawType == Byte.class) {
                return Byte.parseByte(num);
            } else if (rawType == short.class || rawType == Short.class) {
                return Short.parseShort(num);
            } else if (rawType == int.class || rawType == Integer.class) {
                return Integer.parseInt(num);
            } else if (rawType == long.class || rawType == Long.class) {
                return Long.parseLong(num);
            } else if (rawType == float.class || rawType == Float.class) {
                return Float.parseFloat(num);
            } else if (rawType == double.class || rawType == Double.class) {
                return Double.parseDouble(num);
            } else {
                throw new JsonException("Unsupported type " + cls.getTypeName() + "(" + cls.getClass().getCanonicalName() + ")");
            }
        }
    }

    @Override
    public void encode(@NotNull Object value, @NotNull StringBuilder output, @Nullable Object extras) throws JsonException {
        final Class<?> cls = value.getClass();

        if (cls == char.class || cls == Character.class) {
            output.append('"').append(value).append('"');
        } else {
            output.append(value);
        }
    }
}
