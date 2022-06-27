package sk.pa3kc.json.parser;

import java.io.IOException;
import java.lang.reflect.Type;

import org.jetbrains.annotations.NotNull;

import sk.pa3kc.json.JsonException;
import sk.pa3kc.json.JsonTokener;

public abstract class JsonParser {
    public abstract @NotNull Object decode(@NotNull JsonTokener tokener, @NotNull Type cls) throws IOException, JsonException;
    public abstract void encode(@NotNull Object value, @NotNull StringBuilder output);
}
