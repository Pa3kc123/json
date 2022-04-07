package sk.pa3kc.json.impl;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

import sk.pa3kc.json.JsonEncoders;
import sk.pa3kc.json.JsonException;
import sk.pa3kc.json.Utils;

public class Encoder {
    private Encoder() { }

    public static void encodeArray(Object arr, StringBuilder builder) {
        final Class<?> compCls = arr.getClass().getComponentType();

        builder.append('[');

        final int arrLength = Array.getLength(arr);

        if (compCls.isPrimitive()) {
            for (int i = 0; i < arrLength; i++) {
                switch (compCls.getCanonicalName()) {
                    case "boolean":
                        builder.append(Array.getBoolean(arr, i));
                        break;

                    case "byte":
                        builder.append(Array.getByte(arr, i));
                        break;

                    case "char":
                        builder.append('"').append(Array.getChar(arr, i)).append('"');
                        break;

                    case "short":
                        builder.append(Array.getShort(arr, i));
                        break;

                    case "int":
                        builder.append(Array.getInt(arr, i));
                        break;

                    case "long":
                        builder.append(Array.getLong(arr, i));
                        break;

                    case "float":
                        builder.append(Array.getFloat(arr, i));
                        break;

                    case "double":
                        builder.append(Array.getDouble(arr, i));
                        break;

                    default:
                        throw new JsonException("Invalid primitive type '" + compCls.getCanonicalName() + "'");
                }
            }
        } else {
            for (int i = 0; i < arrLength; i++) {
                appendValue(Array.get(arr, i), builder);

                if (i+1 < arrLength) {
                    builder.append(',');
                }
            }
        }

        builder.append(']');
    }
    public static void encodeIterable(Iterable<?> iter, StringBuilder builder) {
        builder.append('[');

        final Iterator<?> iterator = iter.iterator();
        while (iterator.hasNext()) {
            appendValue(iterator.next(), builder);

            if (iterator.hasNext()) {
                builder.append(',');
            }
        }

        builder.append(']');
    }
    public static void encodeMap(Map<?, ?> map, StringBuilder builder) {
        builder.append('{');

        final Iterator<?> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            final Object key = iterator.next();
            final Object value = map.get(key);

            appendValue(key, builder);

            builder.append(':');

            appendValue(value, builder);

            if (iterator.hasNext()) {
                builder.append(',');
            }
        }

        builder.append('}');
    }
    public static void encodeObject(Object obj, StringBuilder builder) {
        builder.append('{');

        final Class<?> cls = obj.getClass();
        final Field[] fields = Utils.getInheritedFields(cls);

        for (int i = 0; i < fields.length; i++) {
            final Field field = fields[i];

            final Object value;
            final String getterName = (field.getType() == boolean.class ? "is" : "get") + Utils.capitalize(field.getName());
            final Method getter;
            try {
                getter = cls.getMethod(getterName);
                value = getter.invoke(obj);
            } catch (NoSuchMethodException e) {
                throw new JsonException(getterName + " was not found (" + cls.getCanonicalName() + ')', e);
            } catch (IllegalAccessException e) {
                throw new JsonException(getterName + " is not public (" + cls.getCanonicalName() + ')', e);
            } catch (InvocationTargetException e) {
                throw new JsonException(getterName + " threw an exception (" + cls.getCanonicalName() + ')', e);
            }

            builder.append('"').append(field.getName()).append('"').append(':');
            appendValue(value, builder);

            if (i+1 < fields.length) {
                builder.append(',');
            }
        }

        builder.append('}');
    }

    public static void appendValue(Object value, StringBuilder builder) {
        if (value == null) {
            builder.append("null");
            return;
        }

        final Class<?> cls = value.getClass();

        if (cls.isPrimitive()) {
            switch (cls.getCanonicalName()) {
                case "boolean":
                case "byte":
                case "short":
                case "int":
                case "long":
                case "float":
                case "double":
                    builder.append(value);
                    break;

                case "char":
                    builder.append("'").append(value).append('"');
                    break;

                default:
                    throw new JsonException("Invalid primitive type '" + cls.getCanonicalName() + "'");
            }
        } else {
            JsonEncoders.encode(value, builder);
        }
    }
}
