package com.ua.sdk.concurrent;

import com.ua.sdk.Ua;
import com.ua.sdk.UaException;
import com.ua.sdk.user.User;

/* JADX INFO: loaded from: classes65.dex */
public class LoginRequest extends AsyncRequest<User> {
    private final Ua.LoginCallback callback;

    public LoginRequest(Ua.LoginCallback callback) {
        this.callback = callback;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.ua.sdk.concurrent.AsyncRequest
    public void onDone(User user, UaException e) {
        EntityEventHandler.callOnLogin(user, e, this.callback);
    }
}
