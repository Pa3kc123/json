package sk.pa3kc.json.parser;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigInteger;

import org.jetbrains.annotations.NotNull;

import sk.pa3kc.json.JsonException;
import sk.pa3kc.json.JsonTokener;

public class JsonBigInteger extends JsonParser {
    @Override
    public @NotNull Object decode(@NotNull JsonTokener tokener, @NotNull Type cls) throws IOException, JsonException {
        return new BigInteger(tokener.readNumber());
    }

    @Override
    public void encode(@NotNull Object value, @NotNull StringBuilder output) {
        if (!(value instanceof BigInteger)) {
            throw new JsonException("Not a big integer");
        }

        output.append(value);
    }
}
