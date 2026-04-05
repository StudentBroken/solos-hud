package io.fabric.sdk.android.services.common;

/* JADX INFO: loaded from: classes66.dex */
public class SystemCurrentTimeProvider implements CurrentTimeProvider {
    @Override // io.fabric.sdk.android.services.common.CurrentTimeProvider
    public long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }
}
