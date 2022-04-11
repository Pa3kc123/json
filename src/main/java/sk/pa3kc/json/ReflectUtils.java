package sk.pa3kc.json;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class ReflectUtils {
    private ReflectUtils() { }

    public static Method getGetter(Class<?> cls, String fieldName) throws NoSuchFieldException, NoSuchMethodException {
        final Field f = cls.getDeclaredField(fieldName);
        return getGetter(cls, f);
    }
    public static Method getGetter(Class<?> cls, Field field) throws JsonException {
        final String getterName = (field.getType() == boolean.class ? "is" : "get") + Utils.capitalize(field.getName());
        try {
            return cls.getMethod(getterName);
        } catch (NoSuchMethodException e) {
            throw new JsonException(field.getType().getCanonicalName() + " is missing getter called " + getterName, e);
        }
    }

    public static Method getSetter(Class<?> cls, String fieldName) throws NoSuchFieldException, NoSuchMethodException {
        final Field f = cls.getDeclaredField(fieldName);
        return getSetter(cls, f);
    }
    public static Method getSetter(Class<?> cls, Field field) throws JsonException {
        final String setterName = "set" + Utils.capitalize(field.getName());
        try {
            return cls.getMethod(setterName, field.getType());
        } catch (NoSuchMethodException e) {
            throw new JsonException(field.getType().getCanonicalName() + " is missing setter called " + setterName, e);
        }
    }
}
