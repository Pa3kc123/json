package sk.pa3kc.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import sk.pa3kc.json.inter.EncoderFunc;

@SuppressWarnings("rawtypes")
public class JsonEncoders implements Map<Class<?>, EncoderFunc<?>> {
    final static JsonEncoders INST;

    static {
        INST = new JsonEncoders();
    }

    private Class[] keys = new Class[8];
    private EncoderFunc[] values = new EncoderFunc[8];

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
        if (!(value instanceof EncoderFunc<?>)) {
            return false;
        }

        for (int i = 0; i < this.index; i++) {
            if (value.equals(this.values[i])) {
                return true;
            }
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public EncoderFunc<?> get(Object key) {
        if (!(key instanceof Class)) {
            return null;
        }

        for (int i = 0; i < this.index; i++) {
            if (this.keys[i].isAssignableFrom((Class<?>)key)) {
                return this.values[i];
            }
        }

        return null;
    }

    @Nullable
    @Override
    public EncoderFunc<?> put(Class<?> key, EncoderFunc<?> value) {
        EncoderFunc<?> previous = null;
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
    public EncoderFunc<?> remove(Object key) {
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

        final EncoderFunc<?> func = this.values[removeIndex];

        for (int i = removeIndex; i < this.index - 1; i++) {
            this.keys[i] = this.keys[i + 1];
            this.values[i] = this.values[i + 1];
        }

        return func;
    }

    @Override
    public void putAll(@NotNull Map<? extends Class<?>, ? extends EncoderFunc<?>> m) {
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
    public Collection<EncoderFunc<?>> values() {
        return new ArrayList<>(Arrays.<EncoderFunc<?>>asList(this.values));
    }

    @NotNull
    @Override
    public Set<Entry<Class<?>, EncoderFunc<?>>> entrySet() {
        final Set<Entry<Class<?>, EncoderFunc<?>>> result = new LinkedHashSet<>();

        class Tmp implements Entry<Class<?>, EncoderFunc<?>> {
            private final Class<?> key;
            private EncoderFunc<?> value;

            private Tmp(Class<?> key, EncoderFunc<?> value) {
                this.key = key;
                this.value = value;
            }

            @Override
            public Class<?> getKey() {
                return this.key;
            }

            @Override
            public EncoderFunc<?> getValue() {
                return this.value;
            }

            @Override
            public EncoderFunc<?> setValue(EncoderFunc<?> value) {
                final EncoderFunc<?> prev = this.value;
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

            final EncoderFunc[] values = new EncoderFunc[newSize];
            System.arraycopy(this.values, 0, values, 0, this.values.length);
            this.values = values;
        }
    }

    public static <T> boolean addEncoder(Class<T> cls, EncoderFunc<T> func) {
        if (JsonEncoders.INST.containsKey(cls)) {
            return false;
        }

        JsonEncoders.INST.put(cls, func);

        return true;
    }
}
