package com.kopin.peloton;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/* JADX INFO: loaded from: classes61.dex */
public class PelotonPrefs {
    private static final String key_peloton_access_token = "peloton.access_token";
    private static final String key_peloton_refresh_token = "peloton.refresh_token";
    private static final String key_peloton_user_email = "peloton.user_email";
    private static String mEmail = "";
    private static String mToken = null;
    private static String mRefreshToken = "";

    public static void init(Context context) {
        mEmail = PreferenceManager.getDefaultSharedPreferences(context).getString(key_peloton_user_email, "");
        mToken = PreferenceManager.getDefaultSharedPreferences(context).getString(key_peloton_access_token, null);
        mRefreshToken = PreferenceManager.getDefaultSharedPreferences(context).getString(key_peloton_refresh_token, "");
    }

    public static void clearSession(Context context) {
        setToken(context, null);
        setRefreshToken(context, null);
        setEmail(context, null);
    }

    public static String getToken() {
        return mToken;
    }

    public static void setToken(Context context, String token) {
        mToken = token;
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(key_peloton_access_token, token).apply();
    }

    public static String getRefreshToken() {
        return mRefreshToken;
    }

    public static void setRefreshToken(Context context, String token) {
        mRefreshToken = token;
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(key_peloton_refresh_token, token).apply();
    }

    public static String getEmail() {
        return mEmail;
    }

    public static void setEmail(Context context, String email) {
        mEmail = email;
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(key_peloton_user_email, email).apply();
    }
}
