package sk.pa3kc;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import sk.pa3kc.data.File;
import sk.pa3kc.data.FilePaginator;
import sk.pa3kc.json.Json;

public class Test {
    public static void main(String[] args) throws Exception {
        final String name = "gdrive.files.list.json";

        final String json = loadFile(name);

        if (json == null) {
            return;
        }

        final FilePaginator fp = Json.objFromJson(json, FilePaginator.class);
        System.out.println(Json.createJson(fp, true));
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
