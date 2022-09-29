package sk.pa3kc.json.parser;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import sk.pa3kc.json.JsonException;
import sk.pa3kc.json.JsonParsers;
import sk.pa3kc.json.JsonTokener;
import sk.pa3kc.json.ReflectUtils;

public final class JsonArray extends JsonParser {
    @Override
    public @Nullable Object decode(@NotNull JsonTokener tokener, @NotNull Type cls) throws IOException, JsonException {
        final Class<?> rawType = ReflectUtils.getClassFromType(cls);

        if (!rawType.isArray()) {
            throw new JsonException("Invalid type", tokener.getOffset());
        }

        return null;
    }

    @Override
    public void encode(@NotNull Object value, @NotNull StringBuilder output) {
        if (!value.getClass().isArray()) {
            throw new JsonException("Not an array");
        }

        output.append('[');

        final int length = Array.getLength(value);
        for (int i = 0; i < length; i++) {
            final Object val = Array.get(value, i);
            final Class<?> valClass = val.getClass();

            JsonParsers.get(valClass).encode(val, output);

            if (i+1 < length) {
                output.append(',');
            }
        }

        output.append(']');
    }
}
