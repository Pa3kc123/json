package sk.pa3kc.json.parser;

import java.io.IOException;
import java.lang.reflect.Type;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import sk.pa3kc.json.JsonException;
import sk.pa3kc.json.JsonTokener;

public class JsonNumber extends JsonParser {
    @Override
    public @Nullable Object decode(@NotNull JsonTokener tokener, @NotNull Type cls) throws IOException, JsonException {
        final String num = tokener.readNumber();

        if (num.indexOf('.') != -1) {
            return Double.parseDouble(num);
        } else {
            return Long.parseLong(num);
        }
    }

    @Override
    public void encode(@NotNull Object value, @NotNull StringBuilder output) {
        if (!(value instanceof Number)) {
            throw new JsonException("Not a number");
        }

        output.append(value);
    }
}
