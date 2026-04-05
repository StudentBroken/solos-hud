package org.webrtc;

/* JADX INFO: loaded from: classes57.dex */
public interface SdpObserver {
    void onCreateFailure(String str);

    void onCreateSuccess(SessionDescription sessionDescription);

    void onSetFailure(String str);

    void onSetSuccess();
}
