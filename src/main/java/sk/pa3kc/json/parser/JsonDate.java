package sk.pa3kc.json.parser;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.jetbrains.annotations.NotNull;

import sk.pa3kc.json.JsonException;
import sk.pa3kc.json.JsonTokener;

public class JsonDate extends JsonParser {
    private final SimpleDateFormat ISO_FORMAT = new SimpleDateFormat("", Locale.ENGLISH);

    @Override
    public @NotNull Object decode(@NotNull JsonTokener tokener, @NotNull Type cls) throws IOException, JsonException {
        final String str = tokener.readString(false);
        final ParsePosition pos = new ParsePosition(0);
        final Date result = ISO_FORMAT.parse(str, pos);
        if (pos.getIndex() == 0) {
            throw new JsonException("Unparseable date: \"" + str + "\" (At index: " + pos.getErrorIndex() + ")");
        }
        return result;
    }

    @Override
    public void encode(@NotNull Object value, @NotNull StringBuilder output) {
        output.append(((Date)value).toString());
    }
}
