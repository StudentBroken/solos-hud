package com.digits.sdk.android;

/* JADX INFO: loaded from: classes18.dex */
public interface AuthCallback {
    void failure(DigitsException digitsException);

    void success(DigitsSession digitsSession, String str);
}
