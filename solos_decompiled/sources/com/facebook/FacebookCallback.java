package com.facebook;

/* JADX INFO: loaded from: classes41.dex */
public interface FacebookCallback<RESULT> {
    void onCancel();

    void onError(FacebookException facebookException);

    void onSuccess(RESULT result);
}
