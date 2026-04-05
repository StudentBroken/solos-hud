package com.facebook.login;

/* JADX INFO: loaded from: classes41.dex */
public enum LoginBehavior {
    NATIVE_WITH_FALLBACK(true, true, false, true, true),
    NATIVE_ONLY(true, false, false, false, true),
    WEB_ONLY(false, true, false, true, false),
    WEB_VIEW_ONLY(false, true, false, false, false),
    DEVICE_AUTH(false, false, true, false, false);

    private final boolean allowsCustomTabAuth;
    private final boolean allowsDeviceAuth;
    private final boolean allowsFacebookLiteAuth;
    private final boolean allowsKatanaAuth;
    private final boolean allowsWebViewAuth;

    LoginBehavior(boolean allowsKatanaAuth, boolean allowsWebViewAuth, boolean allowsDeviceAuth, boolean allowsCustomTabAuth, boolean allowsFacebookLiteAuth) {
        this.allowsKatanaAuth = allowsKatanaAuth;
        this.allowsWebViewAuth = allowsWebViewAuth;
        this.allowsDeviceAuth = allowsDeviceAuth;
        this.allowsCustomTabAuth = allowsCustomTabAuth;
        this.allowsFacebookLiteAuth = allowsFacebookLiteAuth;
    }

    boolean allowsKatanaAuth() {
        return this.allowsKatanaAuth;
    }

    boolean allowsWebViewAuth() {
        return this.allowsWebViewAuth;
    }

    boolean allowsDeviceAuth() {
        return this.allowsDeviceAuth;
    }

    boolean allowsCustomTabAuth() {
        return this.allowsCustomTabAuth;
    }

    boolean allowsFacebookLiteAuth() {
        return this.allowsFacebookLiteAuth;
    }
}
