package com.kopin.solos.wear;

/* JADX INFO: loaded from: classes59.dex */
public interface WatchMessageCallback {
    void onGetWatchState(WatchTransferState watchTransferState);

    void performAction(MessageType messageType);
}
