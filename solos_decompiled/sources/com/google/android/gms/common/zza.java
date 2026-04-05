package com.google.android.gms.common;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.google.android.gms.common.internal.zzbr;
import com.kopin.pupil.aria.app.TimedAppState;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* JADX INFO: loaded from: classes67.dex */
public final class zza implements ServiceConnection {
    private boolean zzazX = false;
    private final BlockingQueue<IBinder> zzazY = new LinkedBlockingQueue();

    @Override // android.content.ServiceConnection
    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        this.zzazY.add(iBinder);
    }

    @Override // android.content.ServiceConnection
    public final void onServiceDisconnected(ComponentName componentName) {
    }

    public final IBinder zza(long j, TimeUnit timeUnit) throws InterruptedException, TimeoutException {
        zzbr.zzcG("BlockingServiceConnection.getServiceWithTimeout() called on main thread");
        if (this.zzazX) {
            throw new IllegalStateException("Cannot call get on this connection more than once");
        }
        this.zzazX = true;
        IBinder iBinderPoll = this.zzazY.poll(TimedAppState.DEFAULT_CONFIRM_TIMEOUT, timeUnit);
        if (iBinderPoll == null) {
            throw new TimeoutException("Timed out waiting for the service connection");
        }
        return iBinderPoll;
    }

    public final IBinder zzoT() throws InterruptedException {
        zzbr.zzcG("BlockingServiceConnection.getService() called on main thread");
        if (this.zzazX) {
            throw new IllegalStateException("Cannot call get on this connection more than once");
        }
        this.zzazX = true;
        return this.zzazY.take();
    }
}
