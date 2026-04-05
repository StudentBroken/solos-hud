package io.fabric.sdk.android.services.events;

import java.io.IOException;

/* JADX INFO: loaded from: classes66.dex */
public interface FileRollOverManager {
    void cancelTimeBasedFileRollOver();

    boolean rollFileOver() throws IOException;

    void scheduleTimeBasedRollOverIfNeeded();
}
