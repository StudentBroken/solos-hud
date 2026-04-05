package com.google.android.gms.internal;

import com.google.android.gms.common.internal.zzbr;

/* JADX INFO: loaded from: classes36.dex */
final class zzcgh {
    final String mAppId;
    final String mName;
    final long zzbpK;
    final long zzbpL;
    final long zzbpM;

    zzcgh(String str, String str2, long j, long j2, long j3) {
        zzbr.zzcF(str);
        zzbr.zzcF(str2);
        zzbr.zzaf(j >= 0);
        zzbr.zzaf(j2 >= 0);
        this.mAppId = str;
        this.mName = str2;
        this.zzbpK = j;
        this.zzbpL = j2;
        this.zzbpM = j3;
    }

    final zzcgh zzab(long j) {
        return new zzcgh(this.mAppId, this.mName, this.zzbpK, this.zzbpL, j);
    }

    final zzcgh zzyq() {
        return new zzcgh(this.mAppId, this.mName, this.zzbpK + 1, this.zzbpL + 1, this.zzbpM);
    }
}
