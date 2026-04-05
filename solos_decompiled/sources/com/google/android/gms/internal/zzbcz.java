package com.google.android.gms.internal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.common.ConnectionResult;

/* JADX INFO: loaded from: classes3.dex */
final class zzbcz implements zzbfc {
    private /* synthetic */ zzbcw zzaCz;

    private zzbcz(zzbcw zzbcwVar) {
        this.zzaCz = zzbcwVar;
    }

    /* synthetic */ zzbcz(zzbcw zzbcwVar, zzbcx zzbcxVar) {
        this(zzbcwVar);
    }

    @Override // com.google.android.gms.internal.zzbfc
    public final void zzc(@NonNull ConnectionResult connectionResult) {
        this.zzaCz.zzaCx.lock();
        try {
            this.zzaCz.zzaCv = connectionResult;
            this.zzaCz.zzpD();
        } finally {
            this.zzaCz.zzaCx.unlock();
        }
    }

    @Override // com.google.android.gms.internal.zzbfc
    public final void zze(int i, boolean z) {
        this.zzaCz.zzaCx.lock();
        try {
            if (this.zzaCz.zzaCw) {
                this.zzaCz.zzaCw = false;
                this.zzaCz.zzd(i, z);
            } else {
                this.zzaCz.zzaCw = true;
                this.zzaCz.zzaCo.onConnectionSuspended(i);
            }
        } finally {
            this.zzaCz.zzaCx.unlock();
        }
    }

    @Override // com.google.android.gms.internal.zzbfc
    public final void zzm(@Nullable Bundle bundle) {
        this.zzaCz.zzaCx.lock();
        try {
            this.zzaCz.zzaCv = ConnectionResult.zzazZ;
            this.zzaCz.zzpD();
        } finally {
            this.zzaCz.zzaCx.unlock();
        }
    }
}
