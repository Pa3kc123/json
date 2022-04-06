package sk.pa3kc.json;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import sk.pa3kc.json.ann.JsonKey;
import sk.pa3kc.json.ann.JsonOptions;
import sk.pa3kc.json.ann.JsonSuperclass;

final class Utils {
    private Utils() {}

    @NotNull
    static String capitalize(@NotNull String src) {
        if (src.length() == 0) {
            return src;
        }

        return src.substring(0, 1).toUpperCase().concat(src.substring(1));
    }

    static boolean isValidSuperclass(@Nullable Class<?> cls) {
        return cls != null && cls.getAnnotation(JsonSuperclass.class) != null;
    }

    static Field[] getInheritedFields(Class<?> cls) {
        final List<Field> list = new ArrayList<>(Arrays.asList(cls.getDeclaredFields()));

        for (
            Class<?> innerCls = cls.getSuperclass();
            isValidSuperclass(innerCls);
            innerCls = innerCls.getSuperclass()
        ) {
            list.addAll(Arrays.asList(innerCls.getDeclaredFields()));
        }

        return list.toArray(new Field[0]);
    }

    @NotNull
    static <T> T createInstance(@NotNull Class<T> cls) {
        try {
            final T inst = cls.getConstructor().newInstance();
            return inst;
        } catch (NoSuchMethodException e) {
            throw new JsonException(cls.getCanonicalName() + " doesn't have default constructor", e);
        } catch (InvocationTargetException e) {
            throw new JsonException(cls.getCanonicalName() + " threw an exception", e);
        } catch (InstantiationException e) {
            if (Modifier.isInterface(cls.getModifiers())) {
                throw new JsonException(cls.getCanonicalName() + " is interface", e);
            }
            if (Modifier.isAbstract(cls.getModifiers())) {
                throw new JsonException(cls.getCanonicalName() + " is abstract class", e);
            }
            if (cls.isArray()) {
                throw new JsonException(cls.getCanonicalName() + " is array class", e);
            }
            if (cls.isPrimitive()) {
                throw new JsonException(cls.getCanonicalName() + " is primitive type", e);
            }

            throw new JsonException("Unable to create instance of " + cls.getCanonicalName(), e);
        } catch (IllegalAccessException e) {
            throw new JsonException("Unable to access default constructor of " + cls.getCanonicalName(), e);
        }
    }
    @NotNull
    static Map<String, Field> getFields(@NotNull Class<?> cls, @Nullable JsonOptions options) {
        final Map<String, Field> map = new HashMap<>();

        for (Field f : cls.getDeclaredFields()) {
            final JsonKey key = f.getAnnotation(JsonKey.class);

            if (key == null && options != null && !options.useFieldNameAsKey()) {
                throw new JsonException("No json key defined for " + cls.getCanonicalName() + "#" + f.getName());
            }

            map.put(key == null ? f.getName() : key.value(), f);
        }

        return map;
    }
}
