package com.digits.sdk.android;

import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes18.dex */
class WeakAuthCallback implements AuthCallback {
    private final WeakReference<AuthCallback> callbackWeakReference;
    private final DigitsScribeService scribeService;

    public WeakAuthCallback(AuthCallback callback) {
        this(callback, new AuthScribeService(Digits.getInstance().getScribeClient()));
    }

    WeakAuthCallback(AuthCallback callback, DigitsScribeService scribeService) {
        this.callbackWeakReference = new WeakReference<>(callback);
        this.scribeService = scribeService;
    }

    @Override // com.digits.sdk.android.AuthCallback
    public void success(DigitsSession session, String phoneNumber) {
        AuthCallback callback = this.callbackWeakReference.get();
        if (callback != null) {
            this.scribeService.success();
            callback.success(session, phoneNumber);
        }
    }

    @Override // com.digits.sdk.android.AuthCallback
    public void failure(DigitsException error) {
        AuthCallback callback = this.callbackWeakReference.get();
        if (callback != null) {
            this.scribeService.failure();
            callback.failure(error);
        }
    }

    public AuthCallback getCallback() {
        return this.callbackWeakReference.get();
    }
}
