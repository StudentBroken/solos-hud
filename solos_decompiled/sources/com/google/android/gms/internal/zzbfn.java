package com.google.android.gms.internal;

import android.app.Activity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.concurrent.CancellationException;

/* JADX INFO: loaded from: classes3.dex */
public class zzbfn extends zzbcm {
    private TaskCompletionSource<Void> zzalG;

    private zzbfn(zzbff zzbffVar) {
        super(zzbffVar);
        this.zzalG = new TaskCompletionSource<>();
        this.zzaEI.zza("GmsAvailabilityHelper", this);
    }

    public static zzbfn zzp(Activity activity) {
        zzbff zzbffVarZzn = zzn(activity);
        zzbfn zzbfnVar = (zzbfn) zzbffVarZzn.zza("GmsAvailabilityHelper", zzbfn.class);
        if (zzbfnVar == null) {
            return new zzbfn(zzbffVarZzn);
        }
        if (!zzbfnVar.zzalG.getTask().isComplete()) {
            return zzbfnVar;
        }
        zzbfnVar.zzalG = new TaskCompletionSource<>();
        return zzbfnVar;
    }

    public final Task<Void> getTask() {
        return this.zzalG.getTask();
    }

    @Override // com.google.android.gms.internal.zzbfe
    public final void onDestroy() {
        super.onDestroy();
        this.zzalG.setException(new CancellationException("Host activity was destroyed before Google Play services could be made available."));
    }

    @Override // com.google.android.gms.internal.zzbcm
    protected final void zza(ConnectionResult connectionResult, int i) {
        this.zzalG.setException(com.google.android.gms.common.internal.zzb.zzx(new Status(connectionResult.getErrorCode(), connectionResult.getErrorMessage(), connectionResult.getResolution())));
    }

    @Override // com.google.android.gms.internal.zzbcm
    protected final void zzpq() {
        int iIsGooglePlayServicesAvailable = this.zzaBf.isGooglePlayServicesAvailable(this.zzaEI.zzqD());
        if (iIsGooglePlayServicesAvailable == 0) {
            this.zzalG.setResult(null);
        } else {
            if (this.zzalG.getTask().isComplete()) {
                return;
            }
            zzb(new ConnectionResult(iIsGooglePlayServicesAvailable, null), 0);
        }
    }
}
