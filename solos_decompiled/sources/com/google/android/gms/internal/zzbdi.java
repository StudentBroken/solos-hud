package com.google.android.gms.internal;

import android.app.Activity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.zzbr;

/* JADX INFO: loaded from: classes3.dex */
public class zzbdi extends zzbcm {
    private zzben zzaAP;
    private final com.google.android.gms.common.util.zzb<zzbcf<?>> zzaCY;

    private zzbdi(zzbff zzbffVar) {
        super(zzbffVar);
        this.zzaCY = new com.google.android.gms.common.util.zzb<>();
        this.zzaEI.zza("ConnectionlessLifecycleHelper", this);
    }

    public static void zza(Activity activity, zzben zzbenVar, zzbcf<?> zzbcfVar) {
        zzn(activity);
        zzbff zzbffVarZzn = zzn(activity);
        zzbdi zzbdiVar = (zzbdi) zzbffVarZzn.zza("ConnectionlessLifecycleHelper", zzbdi.class);
        if (zzbdiVar == null) {
            zzbdiVar = new zzbdi(zzbffVarZzn);
        }
        zzbdiVar.zzaAP = zzbenVar;
        zzbr.zzb(zzbcfVar, "ApiKey cannot be null");
        zzbdiVar.zzaCY.add(zzbcfVar);
        zzbenVar.zza(zzbdiVar);
    }

    private final void zzpQ() {
        if (this.zzaCY.isEmpty()) {
            return;
        }
        this.zzaAP.zza(this);
    }

    @Override // com.google.android.gms.internal.zzbfe
    public final void onResume() {
        super.onResume();
        zzpQ();
    }

    @Override // com.google.android.gms.internal.zzbcm, com.google.android.gms.internal.zzbfe
    public final void onStart() {
        super.onStart();
        zzpQ();
    }

    @Override // com.google.android.gms.internal.zzbcm, com.google.android.gms.internal.zzbfe
    public final void onStop() {
        super.onStop();
        this.zzaAP.zzb(this);
    }

    @Override // com.google.android.gms.internal.zzbcm
    protected final void zza(ConnectionResult connectionResult, int i) {
        this.zzaAP.zza(connectionResult, i);
    }

    final com.google.android.gms.common.util.zzb<zzbcf<?>> zzpP() {
        return this.zzaCY;
    }

    @Override // com.google.android.gms.internal.zzbcm
    protected final void zzpq() {
        this.zzaAP.zzpq();
    }
}
