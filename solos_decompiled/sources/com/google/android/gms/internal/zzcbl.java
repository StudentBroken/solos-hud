package com.google.android.gms.internal;

import android.os.RemoteException;

/* JADX INFO: loaded from: classes67.dex */
public final class zzcbl extends zzcbg<String> {
    public zzcbl(int i, String str, String str2) {
        super(0, str, str2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Override // com.google.android.gms.internal.zzcbg
    /* JADX INFO: renamed from: zze, reason: merged with bridge method [inline-methods] */
    public final String zza(zzcbo zzcboVar) {
        try {
            return zzcboVar.getStringFlagValue(getKey(), zzdH(), getSource());
        } catch (RemoteException e) {
            return zzdH();
        }
    }
}
