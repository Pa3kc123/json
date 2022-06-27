package sk.pa3kc.json.parser;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;

import org.jetbrains.annotations.NotNull;

import sk.pa3kc.json.JsonException;
import sk.pa3kc.json.JsonTokener;

public class JsonBigDecimal extends JsonParser {
    @Override
    public @NotNull Object decode(@NotNull JsonTokener tokener, @NotNull Type cls) throws IOException, JsonException {
        return new BigDecimal(tokener.readNumber());
    }

    @Override
    public void encode(@NotNull Object value, @NotNull StringBuilder output) {
        if (!(value instanceof BigDecimal)) {
            throw new JsonException("Not a big decimal");
        }

        output.append(value);
    }
}
