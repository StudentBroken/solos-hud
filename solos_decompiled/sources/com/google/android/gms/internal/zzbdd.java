package com.google.android.gms.internal;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.util.Collections;
import java.util.Iterator;

/* JADX INFO: loaded from: classes3.dex */
final class zzbdd implements OnCompleteListener<Void> {
    private /* synthetic */ zzbdb zzaCR;

    private zzbdd(zzbdb zzbdbVar) {
        this.zzaCR = zzbdbVar;
    }

    @Override // com.google.android.gms.tasks.OnCompleteListener
    public final void onComplete(@NonNull Task<Void> task) {
        this.zzaCR.zzaCx.lock();
        try {
            if (this.zzaCR.zzaCM) {
                if (task.isSuccessful()) {
                    this.zzaCR.zzaCN = new ArrayMap(this.zzaCR.zzaCD.size());
                    Iterator it = this.zzaCR.zzaCD.values().iterator();
                    while (it.hasNext()) {
                        this.zzaCR.zzaCN.put(((zzbda) it.next()).zzpf(), ConnectionResult.zzazZ);
                    }
                } else if (task.getException() instanceof com.google.android.gms.common.api.zza) {
                    com.google.android.gms.common.api.zza zzaVar = (com.google.android.gms.common.api.zza) task.getException();
                    if (this.zzaCR.zzaCK) {
                        this.zzaCR.zzaCN = new ArrayMap(this.zzaCR.zzaCD.size());
                        for (zzbda zzbdaVar : this.zzaCR.zzaCD.values()) {
                            Object objZzpf = zzbdaVar.zzpf();
                            ConnectionResult connectionResultZza = zzaVar.zza(zzbdaVar);
                            if (this.zzaCR.zza((zzbda<?>) zzbdaVar, connectionResultZza)) {
                                this.zzaCR.zzaCN.put(objZzpf, new ConnectionResult(16));
                            } else {
                                this.zzaCR.zzaCN.put(objZzpf, connectionResultZza);
                            }
                        }
                    } else {
                        this.zzaCR.zzaCN = zzaVar.zzpd();
                    }
                    this.zzaCR.zzaCQ = this.zzaCR.zzpL();
                } else {
                    Log.e("ConnectionlessGAC", "Unexpected availability exception", task.getException());
                    this.zzaCR.zzaCN = Collections.emptyMap();
                    this.zzaCR.zzaCQ = new ConnectionResult(8);
                }
                if (this.zzaCR.zzaCO != null) {
                    this.zzaCR.zzaCN.putAll(this.zzaCR.zzaCO);
                    this.zzaCR.zzaCQ = this.zzaCR.zzpL();
                }
                if (this.zzaCR.zzaCQ == null) {
                    this.zzaCR.zzpJ();
                    this.zzaCR.zzpK();
                } else {
                    zzbdb.zza(this.zzaCR, false);
                    this.zzaCR.zzaCG.zzc(this.zzaCR.zzaCQ);
                }
                this.zzaCR.zzaCI.signalAll();
            }
        } finally {
            this.zzaCR.zzaCx.unlock();
        }
    }
}
