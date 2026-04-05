package com.google.android.gms.internal;

import android.net.TrafficStats;
import android.os.Process;
import android.os.SystemClock;
import java.util.concurrent.BlockingQueue;

/* JADX INFO: loaded from: classes67.dex */
public final class zzl extends Thread {
    private final zzb zzi;
    private final zzw zzj;
    private volatile boolean zzk = false;
    private final BlockingQueue<zzp<?>> zzw;
    private final zzk zzx;

    public zzl(BlockingQueue<zzp<?>> blockingQueue, zzk zzkVar, zzb zzbVar, zzw zzwVar) {
        this.zzw = blockingQueue;
        this.zzx = zzkVar;
        this.zzi = zzbVar;
        this.zzj = zzwVar;
    }

    public final void quit() {
        this.zzk = true;
        interrupt();
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public final void run() {
        zzp<?> zzpVarTake;
        zzn zznVarZza;
        Process.setThreadPriority(10);
        while (true) {
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            try {
                zzpVarTake = this.zzw.take();
                try {
                    zzpVarTake.zzb("network-queue-take");
                    TrafficStats.setThreadStatsTag(zzpVarTake.zzc());
                    zznVarZza = this.zzx.zza(zzpVarTake);
                    zzpVarTake.zzb("network-http-complete");
                } catch (zzaa e) {
                    e.zza(SystemClock.elapsedRealtime() - jElapsedRealtime);
                    this.zzj.zza(zzpVarTake, e);
                } catch (Exception e2) {
                    zzab.zza(e2, "Unhandled exception %s", e2.toString());
                    zzaa zzaaVar = new zzaa(e2);
                    zzaaVar.zza(SystemClock.elapsedRealtime() - jElapsedRealtime);
                    this.zzj.zza(zzpVarTake, zzaaVar);
                }
            } catch (InterruptedException e3) {
                if (this.zzk) {
                    return;
                }
            }
            if (zznVarZza.zzz && zzpVarTake.zzl()) {
                zzpVarTake.zzc("not-modified");
            } else {
                zzt<?> zztVarZza = zzpVarTake.zza(zznVarZza);
                zzpVarTake.zzb("network-parse-complete");
                if (zzpVarTake.zzh() && zztVarZza.zzae != null) {
                    this.zzi.zza(zzpVarTake.getUrl(), zztVarZza.zzae);
                    zzpVarTake.zzb("network-cache-written");
                }
                zzpVarTake.zzk();
                this.zzj.zza(zzpVarTake, zztVarZza);
            }
        }
    }
}
