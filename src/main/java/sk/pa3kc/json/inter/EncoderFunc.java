package sk.pa3kc.json.inter;

import sk.pa3kc.json.JsonException;

public interface EncoderFunc<T> {
    void encode(T value, StringBuilder b) throws JsonException;
}
