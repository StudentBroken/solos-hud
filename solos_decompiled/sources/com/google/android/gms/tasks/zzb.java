package com.google.android.gms.tasks;

/* JADX INFO: loaded from: classes56.dex */
final class zzb implements Runnable {
    private /* synthetic */ Task zzbLV;
    private /* synthetic */ zza zzbLW;

    zzb(zza zzaVar, Task task) {
        this.zzbLW = zzaVar;
        this.zzbLV = task;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            this.zzbLW.zzbLU.setResult(this.zzbLW.zzbLT.then(this.zzbLV));
        } catch (RuntimeExecutionException e) {
            if (e.getCause() instanceof Exception) {
                this.zzbLW.zzbLU.setException((Exception) e.getCause());
            } else {
                this.zzbLW.zzbLU.setException(e);
            }
        } catch (Exception e2) {
            this.zzbLW.zzbLU.setException(e2);
        }
    }
}
