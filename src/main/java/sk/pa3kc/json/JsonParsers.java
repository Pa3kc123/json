package sk.pa3kc.json;

import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import sk.pa3kc.json.parser.JsonArray;
import sk.pa3kc.json.parser.JsonBigDecimal;
import sk.pa3kc.json.parser.JsonBigInteger;
import sk.pa3kc.json.parser.JsonCharacter;
import sk.pa3kc.json.parser.JsonDate;
import sk.pa3kc.json.parser.JsonList;
import sk.pa3kc.json.parser.JsonMap;
import sk.pa3kc.json.parser.JsonNumber;
import sk.pa3kc.json.parser.JsonObject;
import sk.pa3kc.json.parser.JsonParser;
import sk.pa3kc.json.parser.JsonPrimitive;
import sk.pa3kc.json.parser.JsonQueue;
import sk.pa3kc.json.parser.JsonSet;
import sk.pa3kc.json.parser.JsonString;

public class JsonParsers {
    private static final Map<Class<?>, SoftReference<? extends JsonParser>> instances = new LinkedHashMap<>();

    private static final Collection<Class<?>> wrapperTypes = Collections.unmodifiableCollection(Arrays.asList(
        Boolean.class,
        Byte.class,
        Short.class,
        Integer.class,
        Long.class,
        Float.class,
        Double.class
    ));

    static final Map<Class<?>, Class<? extends JsonParser>> parsers = new HashMap<>(
        new LinkedHashMap<Class<?>, Class<? extends JsonParser>>() {{
            super.put(char.class, JsonCharacter.class);
            super.put(Character.class, JsonCharacter.class);
            super.put(CharSequence.class, JsonString.class);
            super.put(String.class, JsonString.class);

            super.put(Number.class, JsonNumber.class);
            super.put(BigDecimal.class, JsonBigDecimal.class);
            super.put(BigInteger.class, JsonBigInteger.class);

            super.put(Set.class, JsonSet.class);
            super.put(List.class, JsonList.class);
            super.put(Queue.class, JsonQueue.class);
            super.put(Map.class, JsonMap.class);

            super.put(Date.class, JsonDate.class);
        }}
    );

    public static <T> Class<? extends JsonParser> register(Class<T> cls, Class<? extends JsonParser> parserCls) {
        return parsers.put(cls, parserCls);
    }

    public static <T> Class<? extends JsonParser> unregister(Class<T> cls) {
        return parsers.remove(cls);
    }

    @NotNull
    public static <T> JsonParser get(Class<T> cls) throws JsonException {
        Class<? extends JsonParser> parserCls;
        if (cls.isPrimitive() || wrapperTypes.contains(cls)) {
            parserCls = JsonPrimitive.class;
        } else if (cls.isArray()) {
            parserCls = JsonArray.class;
        } else {
            parserCls = parsers.get(cls);
            if (parserCls == null) {
                parserCls = JsonObject.class;
            }
        }

        final SoftReference<? extends JsonParser> ref = instances.get(parserCls);

        if (ref != null && !ref.isEnqueued()) {
            final JsonParser parser = ref.get();
            if (parser != null) {
                return parser;
            }
        }

        final JsonParser inst = ReflectUtils.createInstance(parserCls);
        instances.put(cls, new SoftReference<>(inst));
        return inst;
    }
}
