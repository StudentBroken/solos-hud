package com.twitter.sdk.android.core;

/* JADX INFO: loaded from: classes62.dex */
public class TwitterException extends RuntimeException {
    public TwitterException(String detailMessage) {
        super(detailMessage);
    }

    public TwitterException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
