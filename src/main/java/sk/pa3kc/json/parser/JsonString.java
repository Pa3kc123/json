package sk.pa3kc.json.parser;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import sk.pa3kc.json.JsonException;
import sk.pa3kc.json.JsonTokener;

public class JsonString extends JsonParser {
    @Override
    public @Nullable Object decode(@NotNull JsonTokener tokener, @NotNull Type cls, @Nullable Object extras) throws IOException, JsonException {
        return tokener.readString();
    }

    @Override
    public void encode(@NotNull Object value, @NotNull StringBuilder output, @Nullable Object extras) throws JsonException {
        if (value instanceof String) {
            final StringReader reader = new StringReader(value.toString());
            final StringBuilder builder = new StringBuilder();

            try {
                int i;
                while ((i = reader.read()) != -1) {
                    char c = (char)i;
                    switch (c) {
                        case '"':
                        case '\\':
                        case '/':
                            builder.append('\\').append(c);
                            break;

                        case '\b': builder.append("\\b");
                            break;
                        case '\f': builder.append("\\f");
                            break;
                        case '\n': builder.append("\\n");
                            break;
                        case '\r': builder.append("\\r");
                            break;
                        case '\t': builder.append("\\t");
                            break;

                        default:
                            builder.append(c);
                            break;
                    }
                }
            } catch (IOException e) {
                System.err.println("This should never happen");
            } finally {
                reader.close();
            }

            output.append('"').append(builder).append('"');
        } else if (value instanceof Integer) {
            output.append("\\u").append(Integer.toString((int)value, 16));
        } else if (value instanceof Long) {
            output.append("\\u").append(Long.toString((long)value, 16));
        } else {
            throw new JsonException("Value type '" + value.getClass().getCanonicalName() + "' cannot be encoded into string");
        }
    }
}
