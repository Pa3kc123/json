package sk.pa3kc.json.parser;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import sk.pa3kc.json.JsonException;
import sk.pa3kc.json.JsonTokener;

public class JsonLocalDate extends JsonParser {
    @Override
    public @Nullable Object decode(@NotNull JsonTokener tokener, @NotNull Type cls) throws IOException, JsonException {
        final String date = tokener.readString();
        return LocalDate.parse(date);
    }

    @Override
    public void encode(@NotNull Object value, @NotNull StringBuilder output) {
        output.append(value);
    }
}
