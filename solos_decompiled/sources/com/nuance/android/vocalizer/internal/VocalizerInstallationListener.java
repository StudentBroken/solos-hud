package com.nuance.android.vocalizer.internal;

/* JADX INFO: loaded from: classes16.dex */
public interface VocalizerInstallationListener {
    public static final int INSTALL_ADDED = 1;
    public static final int INSTALL_CHANGED = 3;
    public static final int INSTALL_REMOVED = 2;

    void onInstallationEvent(int i, String str);
}
