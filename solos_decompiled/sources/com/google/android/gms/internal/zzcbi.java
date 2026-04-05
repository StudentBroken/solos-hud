package com.google.android.gms.internal;

import android.os.RemoteException;

/* JADX INFO: loaded from: classes67.dex */
public final class zzcbi extends zzcbg<Boolean> {
    public zzcbi(int i, String str, Boolean bool) {
        super(0, str, bool);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Override // com.google.android.gms.internal.zzcbg
    /* JADX INFO: renamed from: zzb, reason: merged with bridge method [inline-methods] */
    public final Boolean zza(zzcbo zzcboVar) {
        try {
            return Boolean.valueOf(zzcboVar.getBooleanFlagValue(getKey(), zzdH().booleanValue(), getSource()));
        } catch (RemoteException e) {
            return zzdH();
        }
    }
}
