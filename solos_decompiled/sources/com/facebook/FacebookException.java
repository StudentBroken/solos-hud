package com.facebook;

/* JADX INFO: loaded from: classes41.dex */
public class FacebookException extends RuntimeException {
    static final long serialVersionUID = 1;

    public FacebookException() {
    }

    public FacebookException(String message) {
        super(message);
    }

    public FacebookException(String format, Object... args) {
        this(String.format(format, args));
    }

    public FacebookException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public FacebookException(Throwable throwable) {
        super(throwable);
    }

    @Override // java.lang.Throwable
    public String toString() {
        return getMessage();
    }
}
