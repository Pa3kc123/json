package sk.pa3kc.json;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import sk.pa3kc.json.impl.Decoder;
import sk.pa3kc.json.impl.Encoder;

public final class Json {
    private Json() { }

    @NotNull
    public static <T> T objFromJson(String json, Class<T> cls) {
        if (CharSequence.class.isAssignableFrom(cls) || cls.isPrimitive() || Number.class.isAssignableFrom(cls) || cls == Boolean.class) {
            throw new JsonException("Json#objFromJson is for parsing into custom objects");
        }

        try {
            final JsonTokener tokener = new JsonTokener(json.trim());
            tokener.nextChar();
            return Decoder.decodeObject(tokener, cls);
        } catch (IOException|ReflectiveOperationException e) {
            throw new JsonException(e);
        }
    }
    @NotNull
    public static <T> T[] arrFromJson(String json, Class<T> cls) {
        if (CharSequence.class.isAssignableFrom(cls) || cls.isPrimitive() || Number.class.isAssignableFrom(cls) || cls == Boolean.class) {
            throw new JsonException("Json#arrFromJson is for parsing into array of objects");
        }

        try {
            final JsonTokener tokener = new JsonTokener(json.trim());
            tokener.nextChar();
            return Decoder.decodeArray(tokener, cls);
        } catch (IOException|ReflectiveOperationException e) {
            throw new JsonException(e);
        }
    }
    @NotNull
    public static <T> List<T> listFromJson(String json, Class<? extends List<T>> collCls, Class<T> cls) {
        if (CharSequence.class.isAssignableFrom(cls) || cls.isPrimitive() || Number.class.isAssignableFrom(cls) || cls == Boolean.class) {
            throw new JsonException("Json#listFromJson is for parsing into list of objects");
        }

        try {
            final JsonTokener tokener = new JsonTokener(json.trim());
            tokener.nextChar();
            return Decoder.decodeCollection(tokener, collCls, cls);
        } catch (IOException|ReflectiveOperationException e) {
            throw new JsonException(e);
        }
    }
    @NotNull
    public static <T> Map<String, T> mapFromJson(String json, Class<? extends Map<String, T>> mapCls, Class<T> cls) {
        if (CharSequence.class.isAssignableFrom(cls) || cls.isPrimitive() || Number.class.isAssignableFrom(cls) || cls == Boolean.class) {
            throw new JsonException("Json#listFromJson is for parsing into list of objects");
        }

        try {
            final JsonTokener tokener = new JsonTokener(json.trim());
            tokener.nextChar();
            return Decoder.decodeMap(tokener, mapCls, cls);
        } catch (IOException|ReflectiveOperationException e) {
            throw new JsonException(e);
        }
    }

    @NotNull
    public static String createJson(@Nullable Object obj, boolean ignoreNulls) {
        if (obj == null) {
            return "null";
        }

        final StringBuilder builder = new StringBuilder();

        Encoder.appendValue(obj, builder, true);

        return builder.toString();
    }
}
