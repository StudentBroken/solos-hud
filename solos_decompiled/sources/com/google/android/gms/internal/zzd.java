package com.google.android.gms.internal;

import android.os.Process;
import java.util.concurrent.BlockingQueue;

/* JADX INFO: loaded from: classes67.dex */
public final class zzd extends Thread {
    private static final boolean DEBUG = zzab.DEBUG;
    private final BlockingQueue<zzp<?>> zzg;
    private final BlockingQueue<zzp<?>> zzh;
    private final zzb zzi;
    private final zzw zzj;
    private volatile boolean zzk = false;

    public zzd(BlockingQueue<zzp<?>> blockingQueue, BlockingQueue<zzp<?>> blockingQueue2, zzb zzbVar, zzw zzwVar) {
        this.zzg = blockingQueue;
        this.zzh = blockingQueue2;
        this.zzi = zzbVar;
        this.zzj = zzwVar;
    }

    public final void quit() {
        this.zzk = true;
        interrupt();
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public final void run() {
        if (DEBUG) {
            zzab.zza("start new dispatcher", new Object[0]);
        }
        Process.setThreadPriority(10);
        this.zzi.initialize();
        while (true) {
            try {
                zzp<?> zzpVarTake = this.zzg.take();
                zzpVarTake.zzb("cache-queue-take");
                zzc zzcVarZza = this.zzi.zza(zzpVarTake.getUrl());
                if (zzcVarZza == null) {
                    zzpVarTake.zzb("cache-miss");
                    this.zzh.put(zzpVarTake);
                } else {
                    if (zzcVarZza.zzd < System.currentTimeMillis()) {
                        zzpVarTake.zzb("cache-hit-expired");
                        zzpVarTake.zza(zzcVarZza);
                        this.zzh.put(zzpVarTake);
                    } else {
                        zzpVarTake.zzb("cache-hit");
                        zzt<?> zztVarZza = zzpVarTake.zza(new zzn(zzcVarZza.data, zzcVarZza.zzf));
                        zzpVarTake.zzb("cache-hit-parsed");
                        if (zzcVarZza.zze < System.currentTimeMillis()) {
                            zzpVarTake.zzb("cache-hit-refresh-needed");
                            zzpVarTake.zza(zzcVarZza);
                            zztVarZza.zzag = true;
                            this.zzj.zza(zzpVarTake, zztVarZza, new zze(this, zzpVarTake));
                        } else {
                            this.zzj.zza(zzpVarTake, zztVarZza);
                        }
                    }
                }
            } catch (InterruptedException e) {
                if (this.zzk) {
                    return;
                }
            }
        }
    }
}
