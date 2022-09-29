package sk.pa3kc.json.parser;

import java.io.IOException;
import java.lang.reflect.Type;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import sk.pa3kc.json.JsonException;
import sk.pa3kc.json.JsonTokener;

public class JsonDouble extends JsonParser {
    @Override
    public @Nullable Object decode(@NotNull JsonTokener tokener, @NotNull Type cls) throws IOException, JsonException {
        return Double.parseDouble(tokener.readNumber());
    }

    @Override
    public void encode(@NotNull Object value, @NotNull StringBuilder output) {
        if (!(value instanceof Double)) {
            throw new JsonException("Not a double");
        }

        output.append(value);
    }
}
