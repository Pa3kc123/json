package sk.pa3kc.json.parser;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import sk.pa3kc.json.JsonException;
import sk.pa3kc.json.JsonParsers;
import sk.pa3kc.json.JsonTokener;

public class JsonSet extends JsonParser {
    @Override
    public @NotNull Object decode(@NotNull JsonTokener tokener, @NotNull Type cls) throws IOException, JsonException {
        return null;
    }

    @Override
    public void encode(@NotNull Object value, @NotNull StringBuilder output) {
        if (!(value instanceof Set)) {
            throw new JsonException("Not a set");
        }

        output.append('[');

        final Iterator<?> i = ((Set<?>)value).iterator();
        while (i.hasNext()) {
            final Object val = i.next();
            final Class<?> valClass = val.getClass();

            JsonParsers.get(valClass).encode(val, output);

            if (i.hasNext()) {
                output.append(',');
            }
        }

        output.append(']');
    }
}
