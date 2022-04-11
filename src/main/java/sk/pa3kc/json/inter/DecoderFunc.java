package sk.pa3kc.json.inter;

import sk.pa3kc.json.JsonException;
import sk.pa3kc.json.JsonTokener;

public interface DecoderFunc<T> {
    T decode(JsonTokener json, Class<T> rCls, Class<?> gCls) throws JsonException;
}
