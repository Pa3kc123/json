package sk.pa3kc.json.parser;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import sk.pa3kc.json.JsonException;
import sk.pa3kc.json.JsonTokener;

public class JsonBigDecimal extends JsonParser {
    @Override
    public @Nullable Object decode(@NotNull JsonTokener tokener, @NotNull Type cls, @Nullable Object extras) throws IOException, JsonException {
        final String val = tokener.readNumber();
        return val != null ? new BigDecimal(val) : null;
    }

    @Override
    public void encode(@NotNull Object value, @NotNull StringBuilder output, @Nullable Object extras) throws JsonException {
        output.append(value);
    }
}
