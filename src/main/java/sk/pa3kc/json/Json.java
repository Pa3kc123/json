package sk.pa3kc.json;

import java.io.IOException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Json {
    private Json() { }

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> T fromJson(String json, Class<T> cls) {
        try {
            final JsonTokener tokener = new JsonTokener(json.trim());
            return (T)JsonParsers
                    .get(cls)
                    .decode(tokener, cls, null);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }
    @NotNull
    public static String toJson(Object value) {
        final StringBuilder builder = new StringBuilder();
        try {
            JsonParsers.get(value.getClass()).encode(value, builder, null);
        } catch (JsonException e) {
            throw new JsonException(e);
        }
        return builder.toString();
    }
}
