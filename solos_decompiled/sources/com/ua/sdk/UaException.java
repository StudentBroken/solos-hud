package com.ua.sdk;

/* JADX INFO: loaded from: classes65.dex */
public class UaException extends Exception {
    private final Code code;

    public enum Code {
        TIMEOUT,
        NETWORK,
        PERMISSION,
        CANCELED,
        NOT_FOUND,
        BAD_FORMAT,
        UNKNOWN,
        NOT_AUTHENTICATED,
        NOT_CONNECTED,
        NETWORK_ON_MAIN_THREAD
    }

    public UaException(String message) {
        super(message);
        this.code = Code.UNKNOWN;
    }

    public UaException(String message, Throwable e) {
        super(message, e);
        this.code = Code.UNKNOWN;
    }

    public UaException(Code code, String message, Throwable e) {
        super(message, e);
        this.code = code;
    }

    public UaException() {
        super(Code.UNKNOWN.toString());
        this.code = Code.UNKNOWN;
    }

    public UaException(Throwable cause) {
        super(Code.UNKNOWN.toString(), cause);
        this.code = Code.UNKNOWN;
    }

    public UaException(Code code) {
        super(code.toString());
        this.code = code;
    }

    public UaException(Code code, Throwable cause) {
        super(code.toString(), cause);
        this.code = code;
    }

    public Code getCode() {
        return this.code;
    }
}
