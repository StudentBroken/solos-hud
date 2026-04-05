package com.google.android.gms.internal;

import android.app.Dialog;

/* JADX INFO: loaded from: classes3.dex */
final class zzbcp extends zzbex {
    private /* synthetic */ Dialog zzaBV;
    private /* synthetic */ zzbco zzaBW;

    zzbcp(zzbco zzbcoVar, Dialog dialog) {
        this.zzaBW = zzbcoVar;
        this.zzaBV = dialog;
    }

    @Override // com.google.android.gms.internal.zzbex
    public final void zzpy() {
        this.zzaBW.zzaBU.zzpv();
        if (this.zzaBV.isShowing()) {
            this.zzaBV.dismiss();
        }
    }
}
