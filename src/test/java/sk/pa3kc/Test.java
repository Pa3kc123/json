package sk.pa3kc;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import sk.pa3kc.data2.IssueResponse;
import sk.pa3kc.json.Json;

public class Test {
    public static void main(String[] args) {
        final String name = "issues_response.json";

        final String json = loadFile(name);

        if (json == null) {
            return;
        }

        final IssueResponse response = Json.fromJson(json, IssueResponse.class);

        System.out.println("Done");
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
