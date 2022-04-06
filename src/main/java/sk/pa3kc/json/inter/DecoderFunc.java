package sk.pa3kc.json.inter;

import sk.pa3kc.json.JsonException;

public interface DecoderFunc<T> {
    T decode(String json) throws JsonException;
}
