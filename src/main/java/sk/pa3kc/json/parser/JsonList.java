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

public class JsonList extends JsonParser {
    @Override
    public @Nullable Object decode(@NotNull JsonTokener tokener, @NotNull Type cls) throws IOException, JsonException {
        final Class<?> rawType = ReflectUtils.getClassFromType(cls);
        final Type[] genTypes = ReflectUtils.getGenericTypesFromType(cls);

        final List list;
        if (Modifier.isAbstract(rawType.getModifiers())) {
            list = new ArrayList<>();
        } else if (Modifier.isInterface(rawType.getModifiers())) {
            list = new ArrayList<>();
        } else {
            list = (List)ReflectUtils.createInstance(rawType);
        }

        char c = tokener.nextClearChar();

        if (c != '[') {
            throw new JsonException("Not an array", tokener.getOffset());
        }

        do {
            list.add(
                JsonParsers
                    .get(ReflectUtils.getClassFromType(genTypes[0]))
                    .decode(tokener, genTypes[0])
            );
            c = tokener.nextClearChar();

            if (",]".indexOf(c) == -1) {
                throw new JsonException("Invalid char", tokener.getOffset());
            }
        } while (c != ']');

        return list;
    }

    @Override
    public void encode(@NotNull Object value, @NotNull StringBuilder output) {
        if (!(value instanceof List)) {
            throw new JsonException("Not a list");
        }

        output.append('[');

        final Iterator<?> vals = ((List)value).iterator();
        while (vals.hasNext()) {
            final Object val = vals.next();
            final Class<?> valClass = val.getClass();

            JsonParsers.get(valClass).encode(val, output);

            if (vals.hasNext()) {
                output.append(',');
            }
        }

        output.append(']');
    }
}
