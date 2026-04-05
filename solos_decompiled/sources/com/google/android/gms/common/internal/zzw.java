package com.google.android.gms.common.internal;

import android.content.Intent;
import com.google.android.gms.internal.zzbff;

/* JADX INFO: loaded from: classes3.dex */
final class zzw extends zzt {
    private /* synthetic */ Intent val$intent;
    private /* synthetic */ int val$requestCode;
    private /* synthetic */ zzbff zzaHr;

    zzw(Intent intent, zzbff zzbffVar, int i) {
        this.val$intent = intent;
        this.zzaHr = zzbffVar;
        this.val$requestCode = i;
    }

    @Override // com.google.android.gms.common.internal.zzt
    public final void zzrt() {
        if (this.val$intent != null) {
            this.zzaHr.startActivityForResult(this.val$intent, this.val$requestCode);
        }
    }
}
