package com.ua.sdk.recorder;

/* JADX INFO: loaded from: classes65.dex */
public interface RecorderManagerObserver {
    void onRecorderCreated(String str);

    void onRecorderDestroyed(String str);

    void onRecorderRecovered(String str);
}
