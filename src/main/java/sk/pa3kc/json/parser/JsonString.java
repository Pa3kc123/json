package sk.pa3kc.json.parser;

import java.io.IOException;
import java.lang.reflect.Type;

import org.jetbrains.annotations.NotNull;

import sk.pa3kc.json.JsonException;
import sk.pa3kc.json.JsonTokener;

public class JsonString extends JsonParser {
    @Override
    public @NotNull Object decode(@NotNull JsonTokener tokener, @NotNull Type cls) throws IOException, JsonException {
        return tokener.readString(false);
    }

    @Override
    public void encode(@NotNull Object value, @NotNull StringBuilder output) {
        if (!(value instanceof String)) {
            throw new JsonException("Not a string");
        }

        output.append('"').append(value).append('"');
    }
}
