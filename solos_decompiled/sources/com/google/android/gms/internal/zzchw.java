package com.google.android.gms.internal;

import com.google.android.gms.common.internal.zzbr;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.FutureTask;

/* JADX INFO: loaded from: classes36.dex */
final class zzchw extends Thread {
    private /* synthetic */ zzchs zzbsl;
    private final Object zzbso;
    private final BlockingQueue<FutureTask<?>> zzbsp;

    public zzchw(zzchs zzchsVar, String str, BlockingQueue<FutureTask<?>> blockingQueue) {
        this.zzbsl = zzchsVar;
        zzbr.zzu(str);
        zzbr.zzu(blockingQueue);
        this.zzbso = new Object();
        this.zzbsp = blockingQueue;
        setName(str);
    }

    private final void zza(InterruptedException interruptedException) {
        this.zzbsl.zzwE().zzyx().zzj(String.valueOf(getName()).concat(" was interrupted"), interruptedException);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public final void run() {
        boolean z = false;
        while (!z) {
            try {
                this.zzbsl.zzbsh.acquire();
                z = true;
            } catch (InterruptedException e) {
                zza(e);
            }
        }
        while (true) {
            try {
                FutureTask<?> futureTaskPoll = this.zzbsp.poll();
                if (futureTaskPoll == null) {
                    synchronized (this.zzbso) {
                        if (this.zzbsp.peek() == null && !this.zzbsl.zzbsi) {
                            try {
                                this.zzbso.wait(30000L);
                            } catch (InterruptedException e2) {
                                zza(e2);
                            }
                        }
                    }
                    synchronized (this.zzbsl.zzbsg) {
                        if (this.zzbsp.peek() == null) {
                            break;
                        }
                    }
                } else {
                    futureTaskPoll.run();
                }
            } catch (Throwable th) {
                synchronized (this.zzbsl.zzbsg) {
                    this.zzbsl.zzbsh.release();
                    this.zzbsl.zzbsg.notifyAll();
                    if (this == this.zzbsl.zzbsa) {
                        zzchs.zza(this.zzbsl, null);
                    } else if (this == this.zzbsl.zzbsb) {
                        zzchs.zzb(this.zzbsl, null);
                    } else {
                        this.zzbsl.zzwE().zzyv().log("Current scheduler thread is neither worker nor network");
                    }
                    throw th;
                }
            }
        }
        synchronized (this.zzbsl.zzbsg) {
            this.zzbsl.zzbsh.release();
            this.zzbsl.zzbsg.notifyAll();
            if (this == this.zzbsl.zzbsa) {
                zzchs.zza(this.zzbsl, null);
            } else if (this == this.zzbsl.zzbsb) {
                zzchs.zzb(this.zzbsl, null);
            } else {
                this.zzbsl.zzwE().zzyv().log("Current scheduler thread is neither worker nor network");
            }
        }
    }

    public final void zzfE() {
        synchronized (this.zzbso) {
            this.zzbso.notifyAll();
        }
    }
}
