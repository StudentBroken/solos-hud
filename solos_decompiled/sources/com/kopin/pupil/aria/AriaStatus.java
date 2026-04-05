package com.kopin.pupil.aria;

/* JADX INFO: loaded from: classes43.dex */
public interface AriaStatus {
    void onAppStart(String str);

    void onAppStatus(String str, String str2);

    void onAppStatusChanged();

    void onAudioStatusChanged();

    void onCommandRecognised(String str, String str2);

    void onHeadsetStatusChanged(boolean z, boolean z2);

    void onHome();
}
