package com.google.android.gms.common.internal;

import android.app.Activity;
import android.content.Intent;

/* JADX INFO: loaded from: classes3.dex */
final class zzu extends zzt {
    private /* synthetic */ Activity val$activity;
    private /* synthetic */ Intent val$intent;
    private /* synthetic */ int val$requestCode;

    zzu(Intent intent, Activity activity, int i) {
        this.val$intent = intent;
        this.val$activity = activity;
        this.val$requestCode = i;
    }

    @Override // com.google.android.gms.common.internal.zzt
    public final void zzrt() {
        if (this.val$intent != null) {
            this.val$activity.startActivityForResult(this.val$intent, this.val$requestCode);
        }
    }
}
