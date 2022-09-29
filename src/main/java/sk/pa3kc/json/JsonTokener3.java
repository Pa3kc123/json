package sk.pa3kc.json;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class JsonTokener3 {
    private final Reader reader;
    private char lastChar = '\0';

    private boolean wentBack = false;
    private char charBefore = '\0';

    public JsonTokener3(String src) {
        this(new StringReader(src));
    }
    public JsonTokener3(Reader reader) {
        this.reader = reader;
    }

    public char nextChar() throws IOException {
        if (this.wentBack) {
            this.wentBack = false;

            final char tmp = this.lastChar;
            this.lastChar = this.charBefore;
            this.charBefore = tmp;
        } else {
            this.charBefore = Character.isWhitespace(this.lastChar) ? this.lastChar : this.charBefore;
            this.lastChar = (char)this.reader.read();
        }
        return this.lastChar;
    }

    public char nextClearChar() throws IOException {
        char c;
        do c = nextChar(); while (Character.isWhitespace(c));
        this.lastChar = c;
        return this.lastChar;
    }

    public char lastChar() {
        return this.lastChar;
    }

    public void goBack() {
        if (this.wentBack) {
            throw new IllegalStateException("Already went back once");
        }

        this.wentBack = true;

        char tmp = this.lastChar;
        this.lastChar = this.charBefore;
        this.charBefore = tmp;
    }

    public String readNumber() throws IOException {
        char c = nextClearChar();

        if (!Character.isDigit(c)) {
            throw new JsonException("Not a number");
        }

        final StringBuilder builder = new StringBuilder().append(c);
        while (Character.isDigit(c = this.nextChar())) {
            builder.append(c);
        }

        if (c == '.') {
            builder.append(c);
            while (Character.isDigit((c = this.nextChar()))) {
                builder.append(c);
            }
        }

        if ("eE".indexOf(c) != -1) {
            while (Character.isDigit((c = this.nextChar()))) {
                builder.append(c);
            }
        }

        return builder.toString();
    }

    public String readString(boolean includeLastChar) throws IOException {
        char c = includeLastChar ? this.lastChar : nextClearChar();

        if (c != '"') {
            throw new JsonException("Not a string");
        }

        final StringBuilder builder = new StringBuilder();

        while (true) {
            c = nextChar();

            if (c == '"' && this.lastChar != '\\') {
                break;
            }

            builder.append(c);
        }

        return builder.toString();
    }

    public Boolean readBoolOrNull() throws IOException {
        char c = nextClearChar();

        if ("tfn".indexOf(c) == -1) {
            throw new JsonException("Invalid type");
        }

        final int length = c == 'f' ? 5 : 4;
        final char[] buf = new char[length];
        buf[0] = c;
        for (int i = 1; i < length; i++) {
            buf[i] = this.nextChar();
        }

        final String res = new String(buf, 0, length);
        return "null".equals(res) ? null : Boolean.parseBoolean(res);
    }

    public void skip() throws IOException {
        char c = nextClearChar();

        switch (c) {
            case '"':
                do {
                    c = nextChar();
                } while (c == '"' && this.lastChar != '\\');
                break;

            case '{': {
                int counter = 1;
                boolean isInString = false;
                do {
                    c = nextChar();

                    if (c == '"' && this.lastChar != '\\') {
                        isInString = !isInString;
                    }

                    if (!isInString) {
                        if (c == '{') counter++;
                        if (c == '}') counter--;
                    }
                } while (counter != 0);
                break;
            }

            case '[': {
                int counter = 1;
                boolean isInString = false;
                do {
                    c = nextChar();

                    if (c == '"' && this.lastChar != '\\') {
                        isInString = !isInString;
                    }

                    if (!isInString) {
                        if (c == '[') counter++;
                        if (c == ']') counter--;
                    }
                } while (counter != 0);
                break;
            }

            case 't':
            case 'f':
            case 'n':
                for (int i = 0; i < (c == 'f' ? 4 : 3); i++) {
                    nextChar();
                }
                break;

            default:
                if (c != '+' && c != '-' && !Character.isDigit(c)) {
                    throw new JsonException("Invalid char");
                }

                while (c == '+' || c == '-' || Character.isDigit(c) || c == '.' || c == 'e' || c == 'E') {
                    c = nextChar();
                }
                break;
        }
    }
}
