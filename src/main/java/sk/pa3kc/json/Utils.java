package sk.pa3kc.json;

import org.jetbrains.annotations.NotNull;

public final class Utils {
    private Utils() {}

    @NotNull
    public static String capitalize(@NotNull String src) {
        if (src.length() == 0) {
            return src;
        }

        return Character.toUpperCase(src.charAt(0)) + src.substring(1);
    }
}
