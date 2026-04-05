package com.digits.sdk.android;

/* JADX INFO: loaded from: classes18.dex */
class CouldNotAuthenticateException extends DigitsException {
    public CouldNotAuthenticateException(String message) {
        super(message);
    }

    public CouldNotAuthenticateException(String message, int error, AuthConfig config) {
        super(message, error, config);
    }
}
