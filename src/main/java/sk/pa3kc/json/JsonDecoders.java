package sk.pa3kc.json;

import java.io.IOError;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import sk.pa3kc.json.impl.Decoder;
import sk.pa3kc.json.inter.DecoderFunc;

@SuppressWarnings("rawtypes")
public class JsonDecoders {
    private static JsonDecoders INST;

    public static JsonDecoders getInstance() {
        if (INST == null) {
            INST = new JsonDecoders();

            INST.put(Object.class, (json, rCls, gCls) -> {
                if (rCls.isArray()) {
                    try {
                        return Decoder.decodeArr(json, rCls);
                    } catch (IOException e) {
                        throw new JsonException(e);
                    }
                } else {
                    try {
                        return Decoder.decodeObj(json, rCls);
                    } catch (IOException e) {
                        throw new JsonException(e);
                    }
                }
            });
            INST.put(Map.class, (json, rCls, gCls) -> {
                return new HashMap<>();
            });
            INST.put(Iterable.class, (json, rCls, gCls) -> {
                try {
                    return Decoder.decodeIter(json, rCls, gCls);
                } catch (IOException e) {
                    throw new JsonException(e);
                }
            });
            INST.put(CharSequence.class, (json, rCls, gCls) -> {
                return "";
            });
            INST.put(Number.class, (json, rCls, gCls) -> {
                return 0L;
            });
            INST.put(Boolean.class, (json, rCls, gCls) -> {
                return false;
            });

            INST.put(BigInteger.class, (json, rCls, gCls) -> {
                //TODO: Build number as string
                final Number n = JsonDecoders.decode(json, Number.class, null);
                return new BigInteger(n.toString());
            });
            INST.put(BigDecimal.class, (json, rCls, gCls) -> {
                //TODO: Build number as string
                final Number n = JsonDecoders.decode(json, Number.class, null);
                return new BigDecimal(n.toString());
            });
        }

        return INST;
    }

    private Class[] keys = new Class[8];
    private DecoderFunc<?>[] values = new DecoderFunc<?>[8];

    private int index = 0;

    public boolean containsKey(Class<?> key) {
        for (int i = this.index - 1; i >= 0; i--) {
            if (this.keys[i].equals(key)) {
                return true;
            }
        }

        for (int i = this.index - 1; i >= 0; i--) {
            if (this.keys[i].isAssignableFrom(key)) {
                return true;
            }
        }

        return false;
    }

    public <T> DecoderFunc<T> get(Class<T> key) {
        if (!(key instanceof Class)) {
            return null;
        }

        for (int i = 0; i < this.index; i++) {
            if (this.keys[i].equals(key)) {
                return (DecoderFunc<T>)this.values[i];
            }
        }

        for (int i = 0; i < this.index; i++) {
            if (this.keys[i].isAssignableFrom(key)) {
                return (DecoderFunc<T>)this.values[i];
            }
        }

        return null;
    }

    @Nullable
    public DecoderFunc<?> put(Class<?> key, DecoderFunc<?> value) {
        DecoderFunc<?> previous = null;
        int index = 0;
        for (int i = 0; i < this.index; i++, index++) {
            if (key.equals(this.keys[i])) {
                previous = this.values[i];
                index = i;
                break;
            }
        }

        if (previous == null) {
            ensureCapacity(1);

            this.keys[this.index] = key;
            this.values[this.index] = value;
            this.index++;
        } else {
            this.keys[index] = key;
            this.values[index] = value;
        }

        return previous;
    }

    public DecoderFunc<?> remove(Object key) {
        if (!(key instanceof Class)) {
            return null;
        }

        int removeIndex = -1;
        for (int i = 0; i < this.index; i++) {
            if (key.equals(this.keys[i])) {
                removeIndex = i;
                break;
            }
        }

        if (removeIndex == -1) {
            return null;
        }

        final DecoderFunc<?> func = this.values[removeIndex];

        for (int i = removeIndex; i < this.index - 1; i++) {
            this.keys[i] = this.keys[i + 1];
            this.values[i] = this.values[i + 1];
        }

        return func;
    }

    private void ensureCapacity(int count) {
        if (this.index + count < this.keys.length) {
            return;
        }

        int newSize = this.keys.length * 2;
        while (newSize < this.index + count) {
            newSize *= 2;
        }

        final Class[] keys = new Class[newSize];
        System.arraycopy(this.keys, 0, keys, 0, this.keys.length);
        this.keys = keys;

        final DecoderFunc<?>[] values = new DecoderFunc<?>[newSize];
        System.arraycopy(this.values, 0, values, 0, this.values.length);
        this.values = values;
    }

    public static <T> T decode(JsonTokener json, Class<T> cls, Class<?> gCls) throws JsonException {
        if (!JsonDecoders.getInstance().containsKey(cls)) {
            throw new JsonException("No decoder for " + cls.getCanonicalName());
        }

        return JsonDecoders.getInstance().get(cls).decode(json, cls, gCls);
    }
    public static <T> boolean setDecoder(Class<T> cls, DecoderFunc<T> func) {
        if (JsonDecoders.getInstance().containsKey(cls)) {
            return false;
        }

        JsonDecoders.getInstance().put(cls, func);

        return true;
    }
}
