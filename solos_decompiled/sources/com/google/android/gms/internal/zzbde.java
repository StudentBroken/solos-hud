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
final class zzbde implements OnCompleteListener<Void> {
    private /* synthetic */ zzbdb zzaCR;
    private zzbfu zzaCS;

    zzbde(zzbdb zzbdbVar, zzbfu zzbfuVar) {
        this.zzaCR = zzbdbVar;
        this.zzaCS = zzbfuVar;
    }

    final void cancel() {
        this.zzaCS.zzmD();
    }

    @Override // com.google.android.gms.tasks.OnCompleteListener
    public final void onComplete(@NonNull Task<Void> task) {
        this.zzaCR.zzaCx.lock();
        try {
            if (!this.zzaCR.zzaCM) {
                this.zzaCS.zzmD();
                return;
            }
            if (task.isSuccessful()) {
                this.zzaCR.zzaCO = new ArrayMap(this.zzaCR.zzaCE.size());
                Iterator it = this.zzaCR.zzaCE.values().iterator();
                while (it.hasNext()) {
                    this.zzaCR.zzaCO.put(((zzbda) it.next()).zzpf(), ConnectionResult.zzazZ);
                }
            } else if (task.getException() instanceof com.google.android.gms.common.api.zza) {
                com.google.android.gms.common.api.zza zzaVar = (com.google.android.gms.common.api.zza) task.getException();
                if (this.zzaCR.zzaCK) {
                    this.zzaCR.zzaCO = new ArrayMap(this.zzaCR.zzaCE.size());
                    for (zzbda zzbdaVar : this.zzaCR.zzaCE.values()) {
                        Object objZzpf = zzbdaVar.zzpf();
                        ConnectionResult connectionResultZza = zzaVar.zza(zzbdaVar);
                        if (this.zzaCR.zza((zzbda<?>) zzbdaVar, connectionResultZza)) {
                            this.zzaCR.zzaCO.put(objZzpf, new ConnectionResult(16));
                        } else {
                            this.zzaCR.zzaCO.put(objZzpf, connectionResultZza);
                        }
                    }
                } else {
                    this.zzaCR.zzaCO = zzaVar.zzpd();
                }
            } else {
                Log.e("ConnectionlessGAC", "Unexpected availability exception", task.getException());
                this.zzaCR.zzaCO = Collections.emptyMap();
            }
            if (this.zzaCR.isConnected()) {
                this.zzaCR.zzaCN.putAll(this.zzaCR.zzaCO);
                if (this.zzaCR.zzpL() == null) {
                    this.zzaCR.zzpJ();
                    this.zzaCR.zzpK();
                    this.zzaCR.zzaCI.signalAll();
                }
            }
            this.zzaCS.zzmD();
        } finally {
            this.zzaCR.zzaCx.unlock();
        }
    }
}
