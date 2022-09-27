package sk.pa3kc.data2;

import java.io.IOException;

public enum FieldFormat {
    DATE, EASY_COMPUTED_TOKEN;

    public String toValue() {
        switch (this) {
            case DATE: return "date";
            case EASY_COMPUTED_TOKEN: return "easy_computed_token";
        }
        return null;
    }

    public static FieldFormat forValue(String value) throws IOException {
        if (value.equals("date")) return DATE;
        if (value.equals("easy_computed_token")) return EASY_COMPUTED_TOKEN;
        throw new IOException("Cannot deserialize FieldFormat");
    }
}
