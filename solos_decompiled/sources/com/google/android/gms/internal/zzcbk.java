package com.google.android.gms.internal;

import android.os.RemoteException;

/* JADX INFO: loaded from: classes67.dex */
public final class zzcbk extends zzcbg<Long> {
    public zzcbk(int i, String str, Long l) {
        super(0, str, l);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Override // com.google.android.gms.internal.zzcbg
    /* JADX INFO: renamed from: zzd, reason: merged with bridge method [inline-methods] */
    public final Long zza(zzcbo zzcboVar) {
        try {
            return Long.valueOf(zzcboVar.getLongFlagValue(getKey(), zzdH().longValue(), getSource()));
        } catch (RemoteException e) {
            return zzdH();
        }
    }
}
