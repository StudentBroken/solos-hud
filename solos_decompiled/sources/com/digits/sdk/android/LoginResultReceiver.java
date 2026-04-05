package com.digits.sdk.android;

import android.os.Bundle;
import android.os.ResultReceiver;
import com.twitter.sdk.android.core.SessionManager;

/* JADX INFO: loaded from: classes18.dex */
class LoginResultReceiver extends ResultReceiver {
    static final String KEY_ERROR = "login_error";
    static final int RESULT_ERROR = 400;
    static final int RESULT_OK = 200;
    final WeakAuthCallback callback;
    final SessionManager<DigitsSession> sessionManager;

    LoginResultReceiver(AuthCallback callback, SessionManager<DigitsSession> sessionManager) {
        super(null);
        this.callback = new WeakAuthCallback(callback);
        this.sessionManager = sessionManager;
    }

    LoginResultReceiver(WeakAuthCallback callback, SessionManager<DigitsSession> sessionManager) {
        super(null);
        this.callback = callback;
        this.sessionManager = sessionManager;
    }

    @Override // android.os.ResultReceiver
    public void onReceiveResult(int resultCode, Bundle resultData) {
        if (this.callback != null) {
            if (resultCode == 200) {
                this.callback.success((DigitsSession) this.sessionManager.getActiveSession(), resultData.getString(DigitsClient.EXTRA_PHONE));
            } else if (resultCode == RESULT_ERROR) {
                this.callback.failure(new DigitsException(resultData.getString(KEY_ERROR)));
            }
        }
    }
}
