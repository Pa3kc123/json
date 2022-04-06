package sk.pa3kc.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import sk.pa3kc.json.inter.DecoderFunc;

@SuppressWarnings("rawtypes")
public class JsonDecoders implements Map<Class<?>, DecoderFunc<?>> {
    final static JsonDecoders INST;

    static {
        INST = new JsonDecoders();
    }

    private Class[] keys = new Class[8];
    private DecoderFunc[] values = new DecoderFunc[8];

    private int index = 0;

    @Override
    public int size() {
        return this.index;
    }

    @Override
    public boolean isEmpty() {
        return this.index == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Class)) {
            return false;
        }

        for (int i = 0; i < this.index; i++) {
            if (key.equals(this.keys[i])) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof DecoderFunc<?>)) {
            return false;
        }

        for (int i = 0; i < this.index; i++) {
            if (value.equals(this.values[i])) {
                return true;
            }
        }

        return false;
    }

    @Override
    public DecoderFunc<?> get(Object key) {
        if (!(key instanceof Class)) {
            return null;
        }

        Class<?> cls;
        for (int i = 0; i < this.index; i++) {
            cls = this.keys[i];
            while (cls != Object.class) {
                if (key.equals(cls)) {
                    return this.values[i];
                }

                cls = cls.getSuperclass();
            }
        }

        return null;
    }

    @Nullable
    @Override
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

    @Override
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

    @Override
    public void putAll(@NotNull Map<? extends Class<?>, ? extends DecoderFunc<?>> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.index; i++) {
            this.keys[i] = null;
            this.values[i] = null;
        }
        this.index = 0;
    }

    @NotNull
    @Override
    public Set<Class<?>> keySet() {
        return new LinkedHashSet<>(Arrays.<Class<?>>asList(this.keys));
    }

    @NotNull
    @Override
    public Collection<DecoderFunc<?>> values() {
        return new ArrayList<>(Arrays.<DecoderFunc<?>>asList(this.values));
    }

    @NotNull
    @Override
    public Set<Entry<Class<?>, DecoderFunc<?>>> entrySet() {
        final Set<Entry<Class<?>, DecoderFunc<?>>> result = new LinkedHashSet<>();

        class Tmp implements Entry<Class<?>, DecoderFunc<?>> {
            private final Class<?> key;
            private DecoderFunc<?> value;

            private Tmp(Class<?> key, DecoderFunc<?> value) {
                this.key = key;
                this.value = value;
            }

            @Override
            public Class<?> getKey() {
                return this.key;
            }

            @Override
            public DecoderFunc<?> getValue() {
                return this.value;
            }

            @Override
            public DecoderFunc<?> setValue(DecoderFunc<?> value) {
                final DecoderFunc<?> prev = this.value;
                this.value = value;
                return prev;
            }
        }

        for (int i = 0; i < index; i++) {
            result.add(new Tmp(this.keys[i], this.values[i]));
        }

        return result;
    }

    private void ensureCapacity(int count) {
        if (this.index + count > this.keys.length) {
            int newSize = this.keys.length * 2;
            while (newSize < this.index + count) {
                newSize *= 2;
            }

            final Class[] keys = new Class[newSize];
            System.arraycopy(this.keys, 0, keys, 0, this.keys.length);
            this.keys = keys;

            final DecoderFunc[] values = new DecoderFunc[newSize];
            System.arraycopy(this.values, 0, values, 0, this.values.length);
            this.values = values;
        }
    }

    public static <T> boolean addDecoder(Class<T> cls, DecoderFunc<T> func) {
        if (JsonDecoders.INST.containsKey(cls)) {
            return false;
        }

        JsonDecoders.INST.put(cls, func);

        return true;
    }
}
