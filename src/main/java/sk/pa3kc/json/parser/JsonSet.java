package sk.pa3kc.json.parser;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import sk.pa3kc.json.JsonException;
import sk.pa3kc.json.JsonParsers;
import sk.pa3kc.json.JsonTokener;
import sk.pa3kc.json.ReflectUtils;

public class JsonSet extends JsonParser {
    @Override
    public @Nullable Object decode(@NotNull JsonTokener tokener, @NotNull Type cls, @Nullable Object extras) throws IOException, JsonException {
        final Class<?> rawType = ReflectUtils.getClassFromType(cls);
        final Type[] genTypes = ReflectUtils.getGenericTypesFromType(cls);

        final Set<? super Object> list = (rawType.getModifiers() & (Modifier.ABSTRACT|Modifier.INTERFACE)) == 0 ? (Set<? super Object>)ReflectUtils.createInstance(rawType) : new HashSet<>();
        char c = tokener.nextClearChar();

        if (c != '[') {
            throw new JsonException("Not a query", tokener.getOffset());
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

        final Iterator<?> i = ((Set<?>)value).iterator();
        while (i.hasNext()) {
            final Object val = i.next();
            final Class<?> valClass = val.getClass();

            JsonParsers.get(valClass).encode(val, output, null);

            if (i.hasNext()) {
                output.append(',');
            }
        }

        output.append(']');
    }
}
