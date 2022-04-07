package sk.pa3kc.json.impl;

import java.io.IOException;

import sk.pa3kc.json.JsonException;
import sk.pa3kc.json.JsonTokener;

public class Ignorer {
    private Ignorer() { }

    public static void ignoreValue(JsonTokener token) throws IOException {
        switch (token.lastChar()) {
            case '"':
                ignoreString(token);
                return;

            case 't':
            case 'f':
            case 'n':
                ignoreBoolOrNull(token);
                return;

            case '{':
                ignoreMap(token);
                return;

            case '[':
                ignoreArray(token);
                return;

            default:
                if (token.lastChar() == '-' || Character.isDigit(token.lastChar())) {
                    ignoreNumber(token);
                } else {
                    throw new JsonException("Invalid char '" + token.lastChar() + "'");
                }
        }
    }

    public static void ignoreString(JsonTokener token) throws IOException {
        ignore(token, '"', '"');
    }
    public static void ignoreBoolOrNull(JsonTokener token) throws IOException {
        for (int i = token.lastChar() == 'f' ? 5 : 4; i > 0; i--){
            token.nextChar();
        }
    }
    public static void ignoreNumber(JsonTokener token) throws IOException {
        if (token.lastChar() == '-') {
            token.nextChar();
        }

        do {
            token.nextChar();
        } while (token.lastChar() >= '0' && token.lastChar() <= '9');

        if (".eE".indexOf(token.lastChar()) == -1) {
            token.goBack();
            return;
        }

        final boolean isDecimal = token.lastChar() == '.';

        if (isDecimal) {
            token.nextChar();

            while (token.lastChar() >= '0' && token.lastChar() <= '9') {
                token.nextChar();
            }
        }

        if ("eE".indexOf(token.lastChar()) != -1) {
            if ("+-".indexOf(token.nextChar()) != -1) {
                token.nextChar();
            } else {
                throw new JsonException("Invalid number");
            }

            while (token.lastChar() >= '0' && token.lastChar() <= '9') {
                token.nextClearChar();
            }
        }

        token.goBack();
    }
    public static void ignoreMap(JsonTokener token) throws IOException {
        ignore(token, '{', '}');
    }
    public static void ignoreArray(JsonTokener token) throws IOException {
        ignore(token, '[', ']');
    }
    public static void ignore(JsonTokener token, char b1, char b2) throws IOException {
        if (b1 != b2) {
            int i = 1;
            do {
                char c = token.nextClearChar();
                i += c == b1 ? 1 : c == b2 ? -1 : 0;
            } while (i != 0);
        } else {
            char c;
            do {
                c = token.nextClearChar();
            } while (c != b1);
        }
    }
}
