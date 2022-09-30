package sk.pa3kc.json.parser;

import java.io.IOException;
import java.lang.reflect.Type;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import sk.pa3kc.json.JsonException;
import sk.pa3kc.json.JsonTokener;

public class JsonCharacter extends JsonParser {
    @Override
    public @Nullable Object decode(@NotNull JsonTokener tokener, @NotNull Type cls, @Nullable Object extras) throws IOException, JsonException {
        char c = tokener.nextClearChar();

        if (c == '"') {
            tokener.stepBack();
            final String res = tokener.readString();
            if (res.length() != 1) {
                throw new JsonException("Invalid symbol", tokener.getOffset());
            }
            return res.charAt(0);
        } else if (c == '-' || Character.isDigit(c)) {
            tokener.stepBack();
            final String res = tokener.readNumber();
            try {
                return (char)Integer.parseInt(res, 10);
            } catch (NumberFormatException e) {
                throw new JsonException("Invalid symbol", tokener.getOffset());
            }
        } else {
            throw new JsonException("Invalid symbol", tokener.getOffset());
        }
    }

    @Override
    public void encode(@NotNull Object value, @NotNull StringBuilder output, @Nullable Object extras) throws JsonException {
        output.append('"').append(value).append('"');
    }
}
