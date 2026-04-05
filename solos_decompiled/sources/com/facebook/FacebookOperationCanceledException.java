package com.facebook;

/* JADX INFO: loaded from: classes41.dex */
public class FacebookOperationCanceledException extends FacebookException {
    static final long serialVersionUID = 1;

    public FacebookOperationCanceledException() {
    }

    public FacebookOperationCanceledException(String message) {
        super(message);
    }

    public FacebookOperationCanceledException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public FacebookOperationCanceledException(Throwable throwable) {
        super(throwable);
    }
}
