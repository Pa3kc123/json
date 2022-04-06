package sk.pa3kc.json;

import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"unused"})
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
            return JsonDecoder.decodeObject(tokener, cls);
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
            return JsonDecoder.decodeArray(tokener, cls);
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
            return JsonDecoder.decodeCollection(tokener, collCls, cls);
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
            return JsonDecoder.decodeMap(tokener, mapCls, cls);
        } catch (IOException|ReflectiveOperationException e) {
            throw new JsonException(e);
        }
    }

    @NotNull
    public static String createJson(@Nullable Object obj) {
        if (obj == null) {
            return "null";
        }

        final StringBuilder builder = new StringBuilder();

        JsonEncoder.appendValue(obj, builder);

        return builder.toString();
    }
}
