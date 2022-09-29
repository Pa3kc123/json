package sk.pa3kc.json.parser;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import sk.pa3kc.json.JsonException;
import sk.pa3kc.json.JsonTokener;

public class JsonDate extends JsonParser {
    private final SimpleDateFormat ISO_DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.ENGLISH);
    private final SimpleDateFormat ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private final SimpleDateFormat ISO_TIME_FORMAT = new SimpleDateFormat("HH:mm:ssXXX", Locale.ENGLISH);

    @Override
    public @Nullable Object decode(@NotNull JsonTokener tokener, @NotNull Type cls) throws IOException, JsonException {
        final String str = tokener.readString();

        if (str == null) {
            return null;
        }

        final ParsePosition pos = new ParsePosition(0);
        Date result;

        result = ISO_DATETIME_FORMAT.parse(str, pos);
        if (pos.getIndex() != 0) {
            return result;
        }

        pos.setIndex(0);
        pos.setErrorIndex(-1);
        result = ISO_DATE_FORMAT.parse(str, pos);
        if (pos.getIndex() != 0) {
            return result;
        }

        pos.setIndex(0);
        pos.setErrorIndex(-1);
        result = ISO_TIME_FORMAT.parse(str, pos);
        if (pos.getIndex() != 0) {
            return result;
        }

        throw new JsonException("Unparseable date: \"" + str + '\"', tokener.getOffset());
    }

    @Override
    public void encode(@NotNull Object value, @NotNull StringBuilder output) {
        output.append(value);
    }
}
