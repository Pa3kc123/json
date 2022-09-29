package sk.pa3kc;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import sk.pa3kc.data2.IssueResponse;
import sk.pa3kc.json.Json;

public class Test {
    private static final List LEAVES = Arrays.asList(
            Boolean.class, Character.class, Byte.class, Short.class,
            Integer.class, Long.class, Float.class, Double.class, Void.class,
            String.class);

    public static String toStringRecursive(Object o) throws Exception {

        if (o == null)
            return "null";

        if (LEAVES.contains(o.getClass()))
            return o.toString();

        StringBuilder sb = new StringBuilder();
        sb.append(o.getClass().getSimpleName()).append(": [");
        final Field[] fields = o.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            final Field f = fields[i];
            if (Modifier.isStatic(f.getModifiers()))
                continue;
            f.setAccessible(true);
            sb.append(f.getName()).append(": ");
            sb.append(toStringRecursive(f.get(o)));
            if (i+1 < fields.length) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        final String name = "issues_response.json";

        final String json = loadFile(name);

        if (json == null) {
            return;
        }

        final IssueResponse response = Json.fromJson(json, IssueResponse.class);

        try {
            System.out.println(toStringRecursive(response));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String loadFile(String name) {
        final InputStream is = Test.class.getClassLoader().getResourceAsStream(name);

        if (is == null) {
            return null;
        }

        final char[] buffer = new char[4096];
        final StringBuilder builder = new StringBuilder();

        try (final Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            do {
                final int read = reader.read(buffer);

                if (read == -1) {
                    break;
                }

                builder.append(buffer, 0, read);
            } while (true);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return builder.toString();
    }
}
