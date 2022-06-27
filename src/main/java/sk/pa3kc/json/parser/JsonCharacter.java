package sk.pa3kc.json.parser;

import java.io.IOException;
import java.lang.reflect.Type;

import org.jetbrains.annotations.NotNull;

import sk.pa3kc.json.JsonException;
import sk.pa3kc.json.JsonTokener;

public class JsonCharacter extends JsonParser {
    @Override
    public @NotNull Object decode(@NotNull JsonTokener tokener, @NotNull Type cls) throws IOException, JsonException {
        final String str = tokener.readString(false);

        if (str.length() != 1) {
            throw new JsonException("Not a char");
        }

        return str.charAt(0);
    }

    @Override
    public void encode(@NotNull Object value, @NotNull StringBuilder output) {
        output.append('"').append(value).append('"');
    }
}
