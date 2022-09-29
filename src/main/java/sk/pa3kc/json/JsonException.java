package sk.pa3kc.json;

public class JsonException extends RuntimeException {
    private static final long serialVersionUID = -799736193997878226L;

    public JsonException() {
        super();
    }

    public JsonException(String message) {
        super(message);
    }

    public JsonException(String message, int offset) {
        super(message + " (At offset: " + offset + ")");
    }

    public JsonException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonException(Throwable cause) {
        super(cause);
    }
}
