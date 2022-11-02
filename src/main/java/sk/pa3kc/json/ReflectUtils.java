package sk.pa3kc.json;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import sk.pa3kc.json.ann.JsonKey;
import sk.pa3kc.json.ann.JsonOptions;
import sk.pa3kc.json.ann.JsonSuperclass;

public final class ReflectUtils {
    private ReflectUtils() { }

    public static boolean isValidSuperclass(@Nullable Class<?> cls, @NotNull Class<? extends Annotation> superCls) {
        return cls != null && cls.getAnnotation(superCls) != null;
    }

    public static Field[] getInheritedFields(Class<?> cls) {
        final List<Field> list = Arrays.asList(cls.getDeclaredFields());

        Class<?> innerCls = cls.getSuperclass();
        while (isValidSuperclass(innerCls, JsonSuperclass.class)) {
            list.addAll(Arrays.asList(innerCls.getDeclaredFields()));
            innerCls = innerCls.getSuperclass();
        }

        return list.toArray(new Field[0]);
    }

    @NotNull
    public static <T> T createInstance(@NotNull Class<T> cls) {
        try {
            return cls.getConstructor().newInstance();
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
    public static Map<String, Field> getFields(@NotNull Class<?> cls, @Nullable JsonOptions options) {
        final Map<String, Field> map = new HashMap<>();

        Class<?> x = cls;
        do {
            for (Field f : x.getDeclaredFields()) {
                final JsonKey key = f.getAnnotation(JsonKey.class);

                if (key == null && options != null && !options.useFieldNameAsKey()) {
                    throw new JsonException("No json key defined for " + x.getCanonicalName() + "#" + f.getName());
                }

                map.put(key == null ? f.getName() : key.value(), f);
            }

            x = x.getSuperclass();

            final JsonSuperclass superclassAnn = x.getAnnotation(JsonSuperclass.class);
            if (superclassAnn == null) {
                break;
            }
        } while (x != null);

        return map;
    }

    @NotNull
    public static Method getGetter(Class<?> cls, String fieldName, Class<?> fieldType) throws NoSuchFieldException, NoSuchMethodException {
        if (fieldName.startsWith("is")) {
            try {
                return cls.getMethod(fieldName);
            } catch (NoSuchMethodException ignored) { }
        }

        final String getterName = "get" + Utils.capitalize(fieldName);
        try {
            return cls.getMethod(getterName);
        } catch (NoSuchMethodException e) {
            throw new JsonException(fieldType.getCanonicalName() + " is missing getter called " + getterName, e);
        }
    }
    @NotNull
    public static Method getGetter(Class<?> cls, Field field) throws JsonException {
        if (field.getName().startsWith("is")) {
            try {
                return cls.getMethod(field.getName());
            } catch (NoSuchMethodException ignored) { }
        }

        final String getterName = "get" + Utils.capitalize(field.getName());
        try {
            return cls.getMethod(getterName);
        } catch (NoSuchMethodException e) {
            throw new JsonException(field.getType().getCanonicalName() + " is missing getter called " + getterName, e);
        }
    }

    @NotNull
    public static Method getSetter(Class<?> cls, String fieldName, Class<?> paramCls) throws NoSuchFieldException, NoSuchMethodException {
        final String setterName;
        if (fieldName.startsWith("is")) {
            setterName = "set" + fieldName.substring(2);
        } else {
            setterName = "set" + Utils.capitalize(fieldName);
        }

        try {
            return cls.getMethod(setterName, paramCls);
        } catch (NoSuchMethodException e) {
            throw new JsonException(cls.getCanonicalName() + " is missing setter called " + setterName, e);
        }
    }
    @NotNull
    public static Method getSetter(Class<?> cls, Field field) throws JsonException {
        final String setterName;
        if (field.getName().startsWith("is")) {
            setterName = "set" + field.getName().substring(2);
        } else {
            setterName = "set" + Utils.capitalize(field.getName());
        }

        try {
            return cls.getMethod(setterName, field.getType());
        } catch (NoSuchMethodException e) {
            throw new JsonException(cls.getCanonicalName() + " is missing setter called " + setterName, e);
        }
    }

    @NotNull
    public static Class<?> getClassFromType(Type type) {
        if (type instanceof Class<?>) {
            return (Class<?>)type;
        } else if (type instanceof ParameterizedType) {
            return (Class<?>)((ParameterizedType)type).getRawType();
        } else if (type instanceof GenericArrayType) {
            final GenericArrayType gat = (GenericArrayType)type;
            return Array.newInstance(getClassFromType(gat.getGenericComponentType()),0).getClass();
        } else if (type instanceof WildcardType) {
            // TODO: Add at least some support for wildcard
            // final WildcardType wt = (WildcardType)type;
            return null;
        } else {
            throw new IllegalArgumentException("Type " + type.getTypeName() + " has unsupported type (" + type.getClass().getCanonicalName() + ")");
        }
    }
    @Nullable
    public static Type[] getGenericTypesFromType(Type type) {
        if (type instanceof Class<?>) {
            return null;
        } else if (type instanceof ParameterizedType) {
            return ((ParameterizedType)type).getActualTypeArguments();
        } else if (type instanceof GenericArrayType) {
            return new Type[] { ((GenericArrayType)type).getGenericComponentType() };
        } else {
            throw new IllegalArgumentException("Type " + type.getTypeName() + " has unsupported generic type (" + type.getClass().getCanonicalName() + ")");
        }
    }
}
