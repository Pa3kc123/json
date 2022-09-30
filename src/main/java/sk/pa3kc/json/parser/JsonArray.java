package sk.pa3kc.json.parser;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import sk.pa3kc.json.JsonException;
import sk.pa3kc.json.JsonParsers;
import sk.pa3kc.json.JsonTokener;
import sk.pa3kc.json.ReflectUtils;

public final class JsonArray extends JsonParser {
    @Override
    public @Nullable Object decode(@NotNull JsonTokener tokener, @NotNull Type cls, @Nullable Object extras) throws IOException, JsonException {
        final Class<?> rawType = ReflectUtils.getClassFromType(cls);

        if (!rawType.isArray()) {
            throw new JsonException("Invalid type", tokener.getOffset());
        }

        final List<? super Object> list = new ArrayList<>();

        final Class<?> compType = rawType.getComponentType();

        char c = tokener.nextClearChar();

        if (c != '[') {
            if (c == 'n') {
                tokener.stepBack();
                if (tokener.readNull()) {
                    return null;
                }
            }

            throw new JsonException("Not an array", tokener.getOffset());
        }

        do {
            list.add(
                JsonParsers
                    .get(ReflectUtils.getClassFromType(compType))
                    .decode(tokener, compType, null)
            );
            c = tokener.nextClearChar();

            if (",]".indexOf(c) == -1) {
                throw new JsonException("Invalid char", tokener.getOffset());
            }
        } while (c != ']');

        return list.toArray();
    }

    @Override
    public void encode(@NotNull Object value, @NotNull StringBuilder output, @Nullable Object extras) throws JsonException {
        output.append('[');

        final int length = Array.getLength(value);
        for (int i = 0; i < length; i++) {
            final Object val = Array.get(value, i);
            final Class<?> valClass = val.getClass();

            JsonParsers.get(valClass).encode(val, output, null);

            if (i+1 < length) {
                output.append(',');
            }
        }

        output.append(']');
    }
}
