package com.google.android.gms.tasks;

/* JADX INFO: loaded from: classes56.dex */
final class zzd implements Runnable {
    private /* synthetic */ Task zzbLV;
    private /* synthetic */ zzc zzbLX;

    zzd(zzc zzcVar, Task task) {
        this.zzbLX = zzcVar;
        this.zzbLV = task;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            Task task = (Task) this.zzbLX.zzbLT.then(this.zzbLV);
            if (task == null) {
                this.zzbLX.onFailure(new NullPointerException("Continuation returned null"));
            } else {
                task.addOnSuccessListener(TaskExecutors.zzbMh, this.zzbLX);
                task.addOnFailureListener(TaskExecutors.zzbMh, this.zzbLX);
            }
        } catch (RuntimeExecutionException e) {
            if (e.getCause() instanceof Exception) {
                this.zzbLX.zzbLU.setException((Exception) e.getCause());
            } else {
                this.zzbLX.zzbLU.setException(e);
            }
        } catch (Exception e2) {
            this.zzbLX.zzbLU.setException(e2);
        }
    }
}
