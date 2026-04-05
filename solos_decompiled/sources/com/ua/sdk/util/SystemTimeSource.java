package com.ua.sdk.util;

/* JADX INFO: loaded from: classes65.dex */
public class SystemTimeSource implements TimeSource {
    @Override // com.ua.sdk.util.TimeSource
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
