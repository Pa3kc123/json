package sk.pa3kc.json;

final class JsonDecoder {
    static {
        JsonDecoders.setDecoder(CharSequence.class, json -> {
            if (json.charAt(0) != '"' || json.charAt(json.length() - 1) != '"') {
                throw new JsonException("Invalid json string");
            }

            return json.substring(1, json.length() - 1);
        });
        JsonDecoders.setDecoder(Iterable.class, json -> null);

        //TODO
        JsonDecoders.setDecoder(Object.class, json -> null);
    }

    private JsonDecoder() { }
}
