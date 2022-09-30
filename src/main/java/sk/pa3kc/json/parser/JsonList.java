package sk.pa3kc.json.parser;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import sk.pa3kc.json.JsonException;
import sk.pa3kc.json.JsonParsers;
import sk.pa3kc.json.JsonTokener;
import sk.pa3kc.json.ReflectUtils;

@SuppressWarnings("unchecked")
public class JsonList extends JsonParser {
    @Override
    public @Nullable Object decode(@NotNull JsonTokener tokener, @NotNull Type cls, @Nullable Object extras) throws IOException, JsonException {
        final Class<?> rawType = ReflectUtils.getClassFromType(cls);
        final Type[] genTypes = ReflectUtils.getGenericTypesFromType(cls);

        final List<? super Object> list = (rawType.getModifiers() & (Modifier.ABSTRACT|Modifier.INTERFACE)) == 0 ? (List<? super Object>)ReflectUtils.createInstance(rawType) : new ArrayList<>();

        char c = tokener.nextClearChar();

        if (c != '[') {
            if (c == 'n') {
                tokener.stepBack();
                if (tokener.readNull()) {
                    return null;
                }
            }

            throw new JsonException("Not an array", tokener.getOffset());
        }

        do {
            list.add(
                JsonParsers
                    .get(ReflectUtils.getClassFromType(genTypes[0]))
                    .decode(tokener, genTypes[0], null)
            );
            c = tokener.nextClearChar();

            if (",]".indexOf(c) == -1) {
                throw new JsonException("Invalid char", tokener.getOffset());
            }
        } while (c != ']');

        return list;
    }

    @Override
    public void encode(@NotNull Object value, @NotNull StringBuilder output, @Nullable Object extras) throws JsonException {
        output.append('[');

        final Iterator<?> vals = ((List<?>)value).iterator();
        while (vals.hasNext()) {
            final Object val = vals.next();
            final Class<?> valClass = val.getClass();

            JsonParsers.get(valClass).encode(val, output, null);

            if (vals.hasNext()) {
                output.append(',');
            }
        }

        output.append(']');
    }
}
