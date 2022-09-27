package sk.pa3kc.json;

import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import sk.pa3kc.json.parser.JsonArray;
import sk.pa3kc.json.parser.JsonBigDecimal;
import sk.pa3kc.json.parser.JsonBigInteger;
import sk.pa3kc.json.parser.JsonBoolean;
import sk.pa3kc.json.parser.JsonByte;
import sk.pa3kc.json.parser.JsonCharacter;
import sk.pa3kc.json.parser.JsonDouble;
import sk.pa3kc.json.parser.JsonFloat;
import sk.pa3kc.json.parser.JsonInteger;
import sk.pa3kc.json.parser.JsonIterable;
import sk.pa3kc.json.parser.JsonLocalDate;
import sk.pa3kc.json.parser.JsonLong;
import sk.pa3kc.json.parser.JsonMap;
import sk.pa3kc.json.parser.JsonNumber;
import sk.pa3kc.json.parser.JsonObject;
import sk.pa3kc.json.parser.JsonParser;
import sk.pa3kc.json.parser.JsonPrimitive;
import sk.pa3kc.json.parser.JsonSet;
import sk.pa3kc.json.parser.JsonShort;
import sk.pa3kc.json.parser.JsonString;

@SuppressWarnings("unchecked")
public class JsonParsers {
    private static final Map<Class<?>, SoftReference<? extends JsonParser>> instances = new LinkedHashMap<>();

    static final ParserRegister parsers = new ParserRegister(
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
            super.put(Iterable.class, JsonIterable.class);
            super.put(Map.class, JsonMap.class);
            super.put(LocalDate.class, JsonLocalDate.class);
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

    static class ParserRegister {
        private Class<?>[] keys;
        private Class<? extends JsonParser>[] values;

        private int index = 0;

        ParserRegister(Map<Class<?>, Class<? extends JsonParser>> values) {
            final int length = (int)Math.ceil((values.size() + 1) / 2.0) * 2;

            this.keys = new Class[length];
            this.values = new Class[length];

            final Iterator<Class<?>> keys = values.keySet().iterator();
            for (int i = 0; i < values.size(); i++) {
                this.keys[i] = keys.next();
                this.values[i] = values.get(this.keys[i]);
            }
            this.index = values.size();
        }

        @Nullable
        <T> Class<? extends JsonParser> get(@NotNull Class<T> key) {
            if (key.isArray()) {
                return JsonArray.class;
            }

            for (int i = 0; i < this.index; i++) {
                final Class<?> cls = this.keys[i];

                if (cls.equals(key)) {
                    return this.values[i];
                }
            }

            for (int i = 0; i < this.index; i++) {
                Class<?> superCls = this.keys[i].getSuperclass();
                while (superCls != null && indexOfKey(superCls) != -1)
                    superCls = superCls.getSuperclass();

                if (!Object.class.equals(superCls)) {
                    return this.values[i];
                }
            }

            return null;
        }

        @Nullable
        <T> Class<? extends JsonParser> put(@NotNull Class<T> key, @NotNull Class<? extends JsonParser> value) {
            final int index = indexOfKey(key);

            if (index == -1) {
                ensureCapacity();
                this.keys[this.index] = key;
                this.values[this.index] = value;
                this.index++;
                return null;
            } else {
                final Class<? extends JsonParser> old = this.values[index];
                this.values[index] = value;
                return old;
            }
        }

        @Nullable
        <T> Class<? extends JsonParser> remove(@NotNull Class<T> key) {
            for (int i = 0; i < this.keys.length; i++) {
                if (this.keys[i].equals(key)) {
                    final Class<? extends JsonParser> x = this.values[i];
                    for (i++; i < this.keys.length - 1; i++) {
                        this.keys[i] = this.keys[i+1];
                        this.values[i] = this.values[i+1];
                    }
                    return x;
                }
            }

            return null;
        }

        int indexOfKey(@NotNull Class<?> key) {
            for (int i = 0; i < this.index; i++) {
                if (this.keys[i].equals(key)) {
                    return i;
                }
            }
            return -1;
        }

        private void ensureCapacity() {
            if (index + 1 == this.keys.length) {
                final int length = this.keys.length;
                final int newLength = length * 2;
                System.arraycopy(this.keys, 0, (this.keys = new Class[newLength]), 0, length);
                System.arraycopy(this.values, 0, (this.values = new Class[newLength]), 0, length);
            }
        }
    }
}
