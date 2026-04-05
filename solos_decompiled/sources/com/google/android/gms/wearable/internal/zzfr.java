package com.google.android.gms.wearable.internal;

import com.google.android.gms.internal.zzbcl;
import com.google.android.gms.wearable.DataApi;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.FutureTask;

/* JADX INFO: loaded from: classes6.dex */
final class zzfr extends zzfc<DataApi.DataItemResult> {
    private final List<FutureTask<Boolean>> zzJR;

    zzfr(zzbcl<DataApi.DataItemResult> zzbclVar, List<FutureTask<Boolean>> list) {
        super(zzbclVar);
        this.zzJR = list;
    }

    @Override // com.google.android.gms.wearable.internal.zza, com.google.android.gms.wearable.internal.zzdi
    public final void zza(zzem zzemVar) {
        zzR(new zzbs(zzev.zzaY(zzemVar.statusCode), zzemVar.zzbSR));
        if (zzemVar.statusCode != 0) {
            Iterator<FutureTask<Boolean>> it = this.zzJR.iterator();
            while (it.hasNext()) {
                it.next().cancel(true);
            }
        }
    }
}
