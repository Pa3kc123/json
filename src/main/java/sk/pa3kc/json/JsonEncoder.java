package sk.pa3kc.json;

import java.util.Map;

import sk.pa3kc.json.impl.Encoder;

final class JsonEncoder {
    static {
        JsonEncoders.setEncoder(CharSequence.class, JsonEncoder::encodeText);
        JsonEncoders.setEncoder(Map.class, Encoder::encodeMap);
        JsonEncoders.setEncoder(Iterable.class, Encoder::encodeIterable);
        JsonEncoders.setEncoder(Number.class, JsonEncoder::encodePrimitive);
        JsonEncoders.setEncoder(Character.class, JsonEncoder::encodeText);
        JsonEncoders.setEncoder(Boolean.class, JsonEncoder::encodePrimitive);
        JsonEncoders.setEncoder(Object.class, (val, builder) -> {
            if (val.getClass().isArray()) {
                Encoder.encodeArray(val, builder);
            } else {
                Encoder.encodeObject(val, builder);
            }
        });
    }

    private JsonEncoder() { }

    private static void encodePrimitive(Object val, StringBuilder builder) {
        builder.append(val);
    }
    private static void encodeText(Object val, StringBuilder builder) {
        builder.append('"').append(val).append('"');
    }
}
