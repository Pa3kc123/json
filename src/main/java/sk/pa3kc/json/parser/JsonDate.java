package sk.pa3kc.json.parser;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import sk.pa3kc.json.JsonException;
import sk.pa3kc.json.JsonTokener;
import sk.pa3kc.json.ann.JsonValueFormat;

public class JsonDate extends JsonParser {
    @Override
    public @Nullable Object decode(@NotNull JsonTokener tokener, @NotNull Type cls, @Nullable Object extras) throws IOException, JsonException {
        final char c = tokener.nextClearChar();

        if (c == '"') {
            tokener.stepBack();
            final String str = tokener.readString();

            if (str == null) {
                return null;
            }

            final JsonValueFormat format = (JsonValueFormat)extras;

            final ParsePosition pos = new ParsePosition(0);
            final Date result = new SimpleDateFormat(format != null ? format.value() : JsonValueFormat.DEFAULT_FORMAT).parse(str, pos);

            if (pos.getIndex() == 0) {
                throw new JsonException("Invalid date format", tokener.getOffset());
            }

            return result;
        } else if (c == '-' || Character.isDigit(c)) {
            tokener.stepBack();
            final String num = tokener.readNumber();
            return num != null ? new Date(Long.parseLong(num)) : null;
        } else {
            if (c == 'n') {
                if (tokener.readNull()) {
                    return null;
                }
            }
            throw new JsonException("Invalid date format", tokener.getOffset());
        }
    }

    @Override
    public void encode(@NotNull Object value, @NotNull StringBuilder output, @Nullable Object extras) throws JsonException {
        final String format = extras instanceof JsonValueFormat ? ((JsonValueFormat)extras).value() : JsonValueFormat.DEFAULT_FORMAT;
        output.append('"').append(new SimpleDateFormat(format).format(value)).append('"');
    }
}
