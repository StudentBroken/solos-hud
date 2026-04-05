package com.google.android.gms.internal;

import android.os.RemoteException;

/* JADX INFO: loaded from: classes67.dex */
public final class zzcbj extends zzcbg<Integer> {
    public zzcbj(int i, String str, Integer num) {
        super(0, str, num);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Override // com.google.android.gms.internal.zzcbg
    /* JADX INFO: renamed from: zzc, reason: merged with bridge method [inline-methods] */
    public final Integer zza(zzcbo zzcboVar) {
        try {
            return Integer.valueOf(zzcboVar.getIntFlagValue(getKey(), zzdH().intValue(), getSource()));
        } catch (RemoteException e) {
            return zzdH();
        }
    }
}
