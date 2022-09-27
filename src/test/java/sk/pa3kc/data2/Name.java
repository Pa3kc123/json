package sk.pa3kc.data2;

import java.io.IOException;

public enum Name {
    DEPLOYED_DATE, ROZDIEL;

    public String toValue() {
        switch (this) {
            case DEPLOYED_DATE: return "Deployed date";
            case ROZDIEL: return "ROZDIEL";
        }
        return null;
    }

    public static Name forValue(String value) throws IOException {
        if (value.equals("Deployed date")) return DEPLOYED_DATE;
        if (value.equals("ROZDIEL")) return ROZDIEL;
        throw new IOException("Cannot deserialize Name");
    }
}
