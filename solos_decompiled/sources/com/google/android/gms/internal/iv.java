package com.google.android.gms.internal;

import android.database.ContentObserver;
import android.os.Handler;

/* JADX INFO: loaded from: classes67.dex */
final class iv extends ContentObserver {
    iv(Handler handler) {
        super(null);
    }

    @Override // android.database.ContentObserver
    public final void onChange(boolean z) {
        iu.zzbUf.set(true);
    }
}
