package com.google.tagmanager;

/* JADX INFO: loaded from: classes49.dex */
interface HitSendingThread {
    void queueToThread(Runnable runnable);

    void sendHit(String str);
}
