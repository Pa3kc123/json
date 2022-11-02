package sk.pa3kc.json.parser;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import sk.pa3kc.json.JsonException;
import sk.pa3kc.json.JsonParsers;
import sk.pa3kc.json.JsonTokener;
import sk.pa3kc.json.ReflectUtils;

public class JsonMap extends JsonParser {
    @SuppressWarnings("unchecked")
    @Override
    public @Nullable Object decode(@NotNull JsonTokener tokener, @NotNull Type cls, @Nullable Object extras) throws IOException, JsonException {
        if (!(cls instanceof ParameterizedType)) {
            throw new JsonException("Invalid type " + cls.getTypeName());
        }

        final ParameterizedType pt = (ParameterizedType)cls;
        final Class<?> rawType = (Class<?>)pt.getRawType();
        final Type[] genTypes = pt.getActualTypeArguments();

        if (genTypes.length != 2) {
            throw new JsonException("Missing value type", tokener.getOffset());
        }

        final Type valueType = genTypes[1];
        final Class<?> valueCls = ReflectUtils.getClassFromType(valueType);

        final Map<String, ? super Object> res;

        if (Modifier.isInterface(rawType.getModifiers()) && Map.class.equals(rawType)) {
            res = new LinkedHashMap<String, Object>();
        } else if (Modifier.isAbstract(rawType.getModifiers()) && AbstractMap.class.equals(rawType)) {
            res = new LinkedHashMap<String, Object>();
        } else {
            res = (Map<String, ? super Object>)ReflectUtils.createInstance(rawType);
        }

        char c = tokener.nextClearChar();

        if (c != '{') {
            throw new JsonException("Not a map", tokener.getOffset());
        }

        do {
            final String key = tokener.readString();

            if (tokener.nextClearChar() != ':') {
                throw new JsonException("Missing colon", tokener.getOffset());
            }

            res.put(
                key,
                JsonParsers
                    .get(valueCls)
                    .decode(tokener, valueType, null)
            );

            c = tokener.nextClearChar();
        } while (c != '}');

        return res;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void encode(@NotNull Object value, @NotNull StringBuilder output, @Nullable Object extras) throws JsonException {
        output.append('{');

        final Map<String, ?> map = (Map<String, ?>)value;
        final Iterator<String> keys = map.keySet().iterator();
        while (keys.hasNext()) {
            final String key = keys.next();

            output.append('"').append(key).append("\":");

            final Object val = map.get(key);
            final Class<?> valClass = val.getClass();

            JsonParsers.get(valClass).encode(value, output, null);

            if (keys.hasNext()) {
                output.append(',');
            }
        }

        output.append('}');
    }
}
