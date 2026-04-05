package com.kopin.solos.util;

import java.io.IOException;
import java.io.Writer;

/* JADX INFO: loaded from: classes4.dex */
public class StringPrintWriter extends Writer {
    private StringBuilder sb = new StringBuilder();

    @Override // java.io.Writer, java.lang.Appendable
    public Writer append(char c) throws IOException {
        this.sb.append(c);
        return this;
    }

    @Override // java.io.Writer, java.lang.Appendable
    public Writer append(CharSequence csq) throws IOException {
        this.sb.append(csq);
        return this;
    }

    @Override // java.io.Writer, java.lang.Appendable
    public Writer append(CharSequence csq, int start, int end) throws IOException {
        this.sb.append(csq, start, end);
        return this;
    }

    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
    }

    @Override // java.io.Writer, java.io.Flushable
    public void flush() throws IOException {
    }

    @Override // java.io.Writer
    public void write(char[] buf) throws IOException {
        this.sb.append(buf);
    }

    @Override // java.io.Writer
    public void write(int oneChar) throws IOException {
        this.sb.append((char) oneChar);
    }

    @Override // java.io.Writer
    public void write(String str) throws IOException {
        this.sb.append(str);
    }

    @Override // java.io.Writer
    public void write(String str, int offset, int count) throws IOException {
        this.sb.append(str);
    }

    @Override // java.io.Writer
    public void write(char[] buf, int offset, int count) throws IOException {
        this.sb.append(buf, offset, count);
    }

    public String getResult() {
        return this.sb.toString();
    }
}
