package com.kopin.solos.share;

import android.content.Context;
import android.util.Log;
import com.kopin.peloton.PelotonPrefs;

/* JADX INFO: loaded from: classes4.dex */
public class SyncNewUser {
    private static final String TAG = "SyncNewUser";
    private String mEmail;
    private String mRefreshToken;
    private String mToken;
    private boolean validNewUser;

    public SyncNewUser() {
        this.validNewUser = false;
    }

    public SyncNewUser(String token, String refreshToken, String email) {
        this.validNewUser = false;
        Log.i(TAG, TAG);
        this.validNewUser = false;
        this.mToken = token;
        this.mRefreshToken = refreshToken;
        this.mEmail = email;
        if (validItem(token) && validItem(refreshToken) && validItem(email) && !token.equals(PelotonPrefs.getToken())) {
            Log.i(TAG, "new user token");
            this.validNewUser = true;
        }
    }

    private static boolean validItem(String item) {
        return (item == null || item.isEmpty()) ? false : true;
    }

    public boolean isValidNewUser() {
        Log.i(TAG, "isValidNewUser " + this.validNewUser);
        return this.validNewUser;
    }

    public boolean setTokens(Context context) {
        if (!this.validNewUser || !validItem(this.mToken) || !validItem(this.mRefreshToken) || !validItem(this.mEmail)) {
            return false;
        }
        PelotonPrefs.setToken(context, this.mToken);
        PelotonPrefs.setRefreshToken(context, this.mRefreshToken);
        PelotonPrefs.setEmail(context, this.mEmail);
        Log.i(TAG, "set tokens true, for " + this.mEmail);
        return true;
    }

    public static boolean isValidCurrentUser() {
        return validItem(PelotonPrefs.getToken()) && validItem(PelotonPrefs.getRefreshToken()) && validItem(PelotonPrefs.getEmail());
    }
}
