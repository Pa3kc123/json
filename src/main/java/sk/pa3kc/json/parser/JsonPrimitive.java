package sk.pa3kc.json.parser;

import java.io.IOException;
import java.lang.reflect.Type;

import org.jetbrains.annotations.NotNull;

import sk.pa3kc.json.JsonException;
import sk.pa3kc.json.JsonTokener;
import sk.pa3kc.json.ReflectUtils;

public class JsonPrimitive extends JsonParser {
    @Override
    public @NotNull Object decode(@NotNull JsonTokener tokener, @NotNull Type cls) throws IOException, JsonException {
        final Class<?> rawType = ReflectUtils.getClassFromType(cls);

        if (rawType == boolean.class) {
            return tokener.readBoolOrNull();
        } else if (rawType == char.class) {
            return tokener.nextClearChar();
        } else {
            final String num = tokener.readNumber();
            tokener.goBack();

            if (rawType == byte.class) {
                return Byte.parseByte(num);
            } else if (rawType == short.class) {
                return Short.parseShort(num);
            } else if (rawType == int.class) {
                return Integer.parseInt(num);
            } else if (rawType == long.class) {
                return Long.parseLong(num);
            } else if (rawType == float.class) {
                return Float.parseFloat(num);
            } else if (rawType == double.class) {
                return Double.parseDouble(num);
            } else {
                throw new JsonException("Unsupported type " + cls.getTypeName() + "(" + cls.getClass().getCanonicalName() + ")");
            }
        }
    }

    @Override
    public void encode(@NotNull Object value, @NotNull StringBuilder output) {

    }
}
