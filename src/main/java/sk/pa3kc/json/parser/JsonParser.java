package sk.pa3kc.json.parser;

import java.io.IOException;
import java.lang.reflect.Type;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import sk.pa3kc.json.JsonException;
import sk.pa3kc.json.JsonTokener;

public abstract class JsonParser {
    public abstract @Nullable Object decode(@NotNull JsonTokener tokener, @NotNull Type cls, @Nullable Object extras) throws IOException, JsonException;
    public abstract void encode(@NotNull Object value, @NotNull StringBuilder output, @Nullable Object extras) throws JsonException;
}
