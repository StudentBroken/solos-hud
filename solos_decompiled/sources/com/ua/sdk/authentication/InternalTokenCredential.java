package com.ua.sdk.authentication;

import android.os.Parcelable;

/* JADX INFO: loaded from: classes65.dex */
public interface InternalTokenCredential extends Parcelable {
    String getSecret();

    String getToken();

    String getTokenType();

    void setSecret(String str);

    void setToken(String str);

    void setTokenType(String str);
}
