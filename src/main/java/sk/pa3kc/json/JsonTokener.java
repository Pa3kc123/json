package sk.pa3kc.json;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class JsonTokener {
    private final Reader reader;
    private boolean steppedBack = false;
    private char currChar = '\0';
    private int offset = 0;

    public JsonTokener(String src) {
        this.reader = new StringReader(src);
    }

    public JsonTokener(Reader reader) {
        this.reader = reader;
    }

    public int getOffset() {
        return this.offset;
    }

    public char nextChar() throws IOException {
        if (this.steppedBack) {
            this.steppedBack = false;
        } else {
            this.currChar = (char) this.reader.read();
            this.offset++;
        }

        return this.currChar;
    }

    public char nextClearChar() throws IOException {
        char c;
        do {
            c = nextChar();
        } while (Character.isWhitespace(c));
        return c;
    }

    public boolean readNull() throws IOException {
        return this.currChar == 'n' && this.nextChar() == 'u' && this.nextChar() == 'l' && this.nextChar() == 'l';
    }

    public String readString() throws IOException {
        char c = this.nextClearChar();
        if (c != '"') {
            if (this.readNull()) {
                return null;
            }

            throw new JsonException("Not a string", this.offset);
        }

        final StringBuilder builder = new StringBuilder();

        do {
            c = this.nextChar();

            if (c == '"') {
                final int length = builder.length();
                final int lastIndex = length - 1;
                final int secondLastIndex = lastIndex - 1;
                if (length == 0 || (length > 1 && (builder.charAt(lastIndex) != '\\' || (builder.charAt(lastIndex) == '\\' && builder.charAt(secondLastIndex) == '\\')))) {
                    break;
                }
            }

            if (c == '\\') {
                char codePoint = nextChar();

                switch (codePoint) {
                    case '"': builder.append('\"'); break;
                    case '\\': builder.append('\\'); break;
                    case '/': builder.append('/'); break;
                    case 'b': builder.append('\b'); break;
                    case 'f': builder.append('\f'); break;
                    case 'n': builder.append('\n'); break;
                    case 'r': builder.append('\r'); break;
                    case 't': builder.append('\t'); break;

                    case 'u': {
                        final char[] num = { nextChar(), nextChar(), nextChar(), nextChar() };
                        builder.append(Integer.parseInt(new String(num), 16));
                    }
                    break;

                    default:
                        throw new JsonException("Invalid code point '\\" + codePoint + "'", this.offset);
                }

                continue;
            }

            builder.append(c);
        } while (true);

        return builder.toString();
    }

    public String readNumber() throws IOException {
        char c = this.nextClearChar();

        if (!Character.isDigit(c)) {
            if (this.readNull()) {
                return null;
            }

            throw new JsonException("Not a number", this.offset);
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

        if (c == 'e' || c == 'E') {
            while (Character.isDigit((c = this.nextChar()))) {
                builder.append(c);
            }
        }

        this.steppedBack = true;
        return builder.toString();
    }

    public String readBoolean() throws IOException {
        char c = nextClearChar();

        if (c != 't' && c != 'f') {
            if (this.readNull()) {
                return null;
            }
            throw new JsonException("Not a boolean", this.offset);
        }

        final char[] arr = new char[c == 't' ? 4 : 5];
        arr[0] = c;
        for (int i = 1; i < arr.length; i++) {
            arr[i] = this.nextChar();
        }

        return new String(arr, 0, arr.length);
    }

    //region Skippers
    private void skipObject() throws IOException {
        char c;
        do {
            c = nextClearChar();

            if (c != '"') {
                throw new JsonException("Invalid symbol", this.offset);
            }

            skipString();

            c = nextClearChar();
            if (c != ':') {
                throw new JsonException("Invalid symbol", this.offset);
            }

            skip();

            c = nextClearChar();

            if (c != ',' && c != '}') {
                throw new JsonException("Invalid symbol", this.offset);
            }
        } while (c != '}');
    }
    private void skipArray() throws IOException {
        char c;
        do {
            skip();
            c = nextClearChar();

            if (c != ',' && c != ']') {
                throw new JsonException("Invalid symbol", this.offset);
            }
        } while (c != ']');
    }
    private void skipString() throws IOException {
        final char[] buf = new char[3];
        int length = 0;
        int index = 0;
        char c;
        do {
            c = nextChar();

            if (c == '"') {
                final int lastIndex = (length - 1) % 3;
                final int secondLastIndex = (length - 2) % 3;

                if (length == 0 || (length > 1 && (buf[lastIndex] != '\\' || (buf[lastIndex] == '\\' && buf[secondLastIndex] == '\\')))) {
                    break;
                }
            }

            if (c == '\\') {
                final char codePoint = nextChar();
                switch (codePoint) {
                    case '"':
                    case '\\':
                    case '/':
                    case 'b':
                    case 'f':
                    case 'n':
                    case 'r':
                    case 't':
                        break;

                    case 'u':
                        for (int i = 0; i < 4; i++) nextChar();
                        break;

                    default:
                        throw new JsonException("Invalid code point '\\" + codePoint + "'", this.offset);
                }
            }

            buf[(index++) % 3] = c;
            length++;
        } while (true);
    }
    private void skipNumber() throws IOException {
        char c;
        do c = nextChar(); while (Character.isDigit(c));

        if (c == '.') {
            do c = nextChar(); while (Character.isDigit(c));
        }

        if (c == 'e' || c == 'E') {
            do c = nextChar(); while (Character.isDigit(c));
        }

        this.steppedBack = true;
    }
    private void skipBoolean() throws IOException {
        switch (this.currChar) {
            case 't':
                if (nextChar() != 'r' || nextChar() != 'u' || nextChar() != 'e') {
                    throw new JsonException("Invalid symbol", this.offset);
                }
                break;

            case 'f':
                if (nextChar() != 'a' || nextChar() != 'l' || nextChar() != 's' || nextChar() != 'e') {
                    throw new JsonException("Invalid symbol", this.offset);
                }
                break;

            default:
                throw new JsonException("Invalid symbol (This shouldn't have happened)", this.offset);
        }
    }
    private void skipNull() throws IOException {
        if (nextChar() != 'u' || nextChar() != 'l' || nextChar() != 'l') {
            throw new JsonException("Invalid symbol", this.offset);
        }
    }

    public void skip() throws IOException {
        char c = this.nextClearChar();

        switch (c) {
            case '{': skipObject(); break;
            case '[': skipArray(); break;
            case '"': skipString(); break;
            default:
                if (c == 't' || c == 'f') {
                    skipBoolean();
                } else if (c == 'n') {
                    skipNull();
                } else if (Character.isDigit(c)) {
                    this.steppedBack = true;
                    skipNumber();
                } else {
                    throw new JsonException("Invalid symbol", this.offset);
                }
        }
    }
    //endregion
}
