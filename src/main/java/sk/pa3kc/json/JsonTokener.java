package sk.pa3kc.json;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class JsonTokener {
    private final Reader reader;
    private char lastChar = '\0';

    private boolean wentBack = false;
    private char charBefore = '\0';

    private int pos = 0;

    public JsonTokener(String src) {
        this(new StringReader(src));
    }
    public JsonTokener(Reader reader) {
        this.reader = reader;
    }

    public char nextChar() throws IOException {
        if (this.wentBack) {
            this.wentBack = false;

            char tmp = this.lastChar;
            this.lastChar = this.charBefore;
            this.charBefore = tmp;
        } else {
            this.charBefore = this.lastChar > (char)32 ? this.lastChar : this.charBefore;
            this.lastChar = (char)this.reader.read();
            this.pos++;
        }
        return this.lastChar;
    }

    public char nextClearChar() throws IOException {
        for (char c = nextChar(); true; c = nextChar()) {
            if (c == '\0' || c > (char)32) {
                this.lastChar = c;
                return this.lastChar;
            }
        }
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
}
