package com.ua.sdk.concurrent;

import com.ua.sdk.ResetPasswordCallback;
import com.ua.sdk.UaException;

/* JADX INFO: loaded from: classes65.dex */
public class ResetPasswordRequest extends AsyncRequest<Void> {
    private final ResetPasswordCallback callback;

    public ResetPasswordRequest(ResetPasswordCallback callback) {
        this.callback = callback;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.ua.sdk.concurrent.AsyncRequest
    public void onDone(Void ignore, UaException e) {
        EntityEventHandler.callOnResetPassword(e, this.callback);
    }
}
