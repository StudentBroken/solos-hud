package com.ua.sdk.user;

/* JADX INFO: loaded from: classes65.dex */
public enum UserSource {
    FACEBOOK("facebook"),
    MMF("mmf");

    private final String name;

    UserSource(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
