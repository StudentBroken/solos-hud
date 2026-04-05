package com.google.tagmanager;

/* JADX INFO: loaded from: classes49.dex */
interface HitStore {
    void close();

    void dispatch();

    Dispatcher getDispatcher();

    void putHit(long j, String str);
}
