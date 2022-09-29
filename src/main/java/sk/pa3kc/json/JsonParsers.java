package sk.pa3kc.json;

import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.math.BigInteger;
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
import sk.pa3kc.json.parser.JsonBoolean;
import sk.pa3kc.json.parser.JsonByte;
import sk.pa3kc.json.parser.JsonCharacter;
import sk.pa3kc.json.parser.JsonDate;
import sk.pa3kc.json.parser.JsonDouble;
import sk.pa3kc.json.parser.JsonFloat;
import sk.pa3kc.json.parser.JsonInteger;
import sk.pa3kc.json.parser.JsonList;
import sk.pa3kc.json.parser.JsonLong;
import sk.pa3kc.json.parser.JsonMap;
import sk.pa3kc.json.parser.JsonNumber;
import sk.pa3kc.json.parser.JsonObject;
import sk.pa3kc.json.parser.JsonParser;
import sk.pa3kc.json.parser.JsonPrimitive;
import sk.pa3kc.json.parser.JsonQueue;
import sk.pa3kc.json.parser.JsonSet;
import sk.pa3kc.json.parser.JsonShort;
import sk.pa3kc.json.parser.JsonString;

@SuppressWarnings("unchecked")
public class JsonParsers {
    private static final Map<Class<?>, SoftReference<? extends JsonParser>> instances = new LinkedHashMap<>();

    static final HashMap<Class<?>, Class<? extends JsonParser>> parsers = new HashMap<>(
        new LinkedHashMap<Class<?>, Class<? extends JsonParser>>() {{
            super.put(Character.class, JsonCharacter.class);
            super.put(Boolean.class, JsonBoolean.class);
            super.put(Byte.class, JsonByte.class);
            super.put(Short.class, JsonShort.class);
            super.put(Integer.class, JsonInteger.class);
            super.put(Long.class, JsonLong.class);
            super.put(Float.class, JsonFloat.class);
            super.put(Double.class, JsonDouble.class);
            super.put(String.class, JsonString.class);
            super.put(BigDecimal.class, JsonBigDecimal.class);
            super.put(BigInteger.class, JsonBigInteger.class);
            super.put(Number.class, JsonNumber.class);
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
        Class<? extends JsonParser> parserCls = parsers.get(cls);

        if (parserCls == null) {
            if (cls.isArray()) {
                parserCls = JsonArray.class;
            } else if (cls.isPrimitive()) {
                parserCls = JsonPrimitive.class;
            } else {
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

    public static JsonObject getObjectParser() throws JsonException {
        return new JsonObject();
    }

    public static JsonArray getArrayParser() throws JsonException {
        return new JsonArray();
    }
}
