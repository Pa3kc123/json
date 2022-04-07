package sk.pa3kc.json.impl;

import java.io.IOException;

import sk.pa3kc.json.JsonException;
import sk.pa3kc.json.JsonTokener;

public class Ignorer {
    private Ignorer() { }

    public static void ignoreValue(JsonTokener tokener) throws IOException {
        switch (tokener.lastChar()) {
            case '"':
                ignoreString(tokener);
                return;

            case 't':
            case 'f':
            case 'n':
                ignoreBoolOrNull(tokener);
                return;

            case '{':
                ignoreMap(tokener);
                return;

            case '[':
                ignoreArray(tokener);
                return;

            default:
                if (tokener.lastChar() == '-' || Character.isDigit(tokener.lastChar())) {
                    ignoreNumber(tokener);
                } else {
                    throw new JsonException("Invalid char '" + tokener.lastChar() + "'");
                }
        }
    }

    public static void ignoreString(JsonTokener tokener) throws IOException {
        ignore(tokener, '"', '"');
    }
    public static void ignoreBoolOrNull(JsonTokener tokener) throws IOException {
        for (int i = tokener.lastChar() == 'f' ? 5 : 4; i > 0; i--){
            tokener.nextChar();
        }
    }
    public static void ignoreNumber(JsonTokener tokener) throws IOException {
        if (tokener.lastChar() == '-') {
            tokener.nextChar();
        }

        do {
            tokener.nextChar();
        } while (tokener.lastChar() >= '0' && tokener.lastChar() <= '9');

        if (".eE".indexOf(tokener.lastChar()) == -1) {
            tokener.goBack();
            return;
        }

        final boolean isDecimal = tokener.lastChar() == '.';

        if (isDecimal) {
            tokener.nextChar();

            while (tokener.lastChar() >= '0' && tokener.lastChar() <= '9') {
                tokener.nextChar();
            }
        }

        if ("eE".indexOf(tokener.lastChar()) != -1) {
            if ("+-".indexOf(tokener.nextChar()) != -1) {
                tokener.nextChar();
            } else {
                throw new JsonException("Invalid number");
            }

            while (tokener.lastChar() >= '0' && tokener.lastChar() <= '9') {
                tokener.nextClearChar();
            }
        }

        tokener.goBack();
    }
    public static void ignoreMap(JsonTokener tokener) throws IOException {
        ignore(tokener, '{', '}');
    }
    public static void ignoreArray(JsonTokener tokener) throws IOException {
        ignore(tokener, '[', ']');
    }
    public static void ignore(JsonTokener tokener, char b1, char b2) throws IOException {
        if (b1 != b2) {
            int i = 1;
            do {
                char c = tokener.nextClearChar();
                i += c == b1 ? 1 : c == b2 ? -1 : 0;
            } while (i != 0);
        } else {
            char c;
            do {
                c = tokener.nextClearChar();
            } while (c != b1);
        }
    }
}
