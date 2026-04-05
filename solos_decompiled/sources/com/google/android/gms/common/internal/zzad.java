package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes3.dex */
public final class zzad implements Handler.Callback {
    private final Handler mHandler;
    private final zzae zzaHG;
    private final ArrayList<GoogleApiClient.ConnectionCallbacks> zzaHH = new ArrayList<>();
    private ArrayList<GoogleApiClient.ConnectionCallbacks> zzaHI = new ArrayList<>();
    private final ArrayList<GoogleApiClient.OnConnectionFailedListener> zzaHJ = new ArrayList<>();
    private volatile boolean zzaHK = false;
    private final AtomicInteger zzaHL = new AtomicInteger(0);
    private boolean zzaHM = false;
    private final Object mLock = new Object();

    public zzad(Looper looper, zzae zzaeVar) {
        this.zzaHG = zzaeVar;
        this.mHandler = new Handler(looper, this);
    }

    @Override // android.os.Handler.Callback
    public final boolean handleMessage(Message message) {
        if (message.what != 1) {
            Log.wtf("GmsClientEvents", new StringBuilder(45).append("Don't know how to handle message: ").append(message.what).toString(), new Exception());
            return false;
        }
        GoogleApiClient.ConnectionCallbacks connectionCallbacks = (GoogleApiClient.ConnectionCallbacks) message.obj;
        synchronized (this.mLock) {
            if (this.zzaHK && this.zzaHG.isConnected() && this.zzaHH.contains(connectionCallbacks)) {
                connectionCallbacks.onConnected(this.zzaHG.zzoA());
            }
        }
        return true;
    }

    public final boolean isConnectionCallbacksRegistered(GoogleApiClient.ConnectionCallbacks connectionCallbacks) {
        boolean zContains;
        zzbr.zzu(connectionCallbacks);
        synchronized (this.mLock) {
            zContains = this.zzaHH.contains(connectionCallbacks);
        }
        return zContains;
    }

    public final boolean isConnectionFailedListenerRegistered(GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        boolean zContains;
        zzbr.zzu(onConnectionFailedListener);
        synchronized (this.mLock) {
            zContains = this.zzaHJ.contains(onConnectionFailedListener);
        }
        return zContains;
    }

    public final void registerConnectionCallbacks(GoogleApiClient.ConnectionCallbacks connectionCallbacks) {
        zzbr.zzu(connectionCallbacks);
        synchronized (this.mLock) {
            if (this.zzaHH.contains(connectionCallbacks)) {
                String strValueOf = String.valueOf(connectionCallbacks);
                Log.w("GmsClientEvents", new StringBuilder(String.valueOf(strValueOf).length() + 62).append("registerConnectionCallbacks(): listener ").append(strValueOf).append(" is already registered").toString());
            } else {
                this.zzaHH.add(connectionCallbacks);
            }
        }
        if (this.zzaHG.isConnected()) {
            this.mHandler.sendMessage(this.mHandler.obtainMessage(1, connectionCallbacks));
        }
    }

    public final void registerConnectionFailedListener(GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        zzbr.zzu(onConnectionFailedListener);
        synchronized (this.mLock) {
            if (this.zzaHJ.contains(onConnectionFailedListener)) {
                String strValueOf = String.valueOf(onConnectionFailedListener);
                Log.w("GmsClientEvents", new StringBuilder(String.valueOf(strValueOf).length() + 67).append("registerConnectionFailedListener(): listener ").append(strValueOf).append(" is already registered").toString());
            } else {
                this.zzaHJ.add(onConnectionFailedListener);
            }
        }
    }

    public final void unregisterConnectionCallbacks(GoogleApiClient.ConnectionCallbacks connectionCallbacks) {
        zzbr.zzu(connectionCallbacks);
        synchronized (this.mLock) {
            if (!this.zzaHH.remove(connectionCallbacks)) {
                String strValueOf = String.valueOf(connectionCallbacks);
                Log.w("GmsClientEvents", new StringBuilder(String.valueOf(strValueOf).length() + 52).append("unregisterConnectionCallbacks(): listener ").append(strValueOf).append(" not found").toString());
            } else if (this.zzaHM) {
                this.zzaHI.add(connectionCallbacks);
            }
        }
    }

    public final void unregisterConnectionFailedListener(GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        zzbr.zzu(onConnectionFailedListener);
        synchronized (this.mLock) {
            if (!this.zzaHJ.remove(onConnectionFailedListener)) {
                String strValueOf = String.valueOf(onConnectionFailedListener);
                Log.w("GmsClientEvents", new StringBuilder(String.valueOf(strValueOf).length() + 57).append("unregisterConnectionFailedListener(): listener ").append(strValueOf).append(" not found").toString());
            }
        }
    }

    public final void zzaA(int i) {
        int i2 = 0;
        zzbr.zza(Looper.myLooper() == this.mHandler.getLooper(), "onUnintentionalDisconnection must only be called on the Handler thread");
        this.mHandler.removeMessages(1);
        synchronized (this.mLock) {
            this.zzaHM = true;
            ArrayList arrayList = new ArrayList(this.zzaHH);
            int i3 = this.zzaHL.get();
            ArrayList arrayList2 = arrayList;
            int size = arrayList2.size();
            while (i2 < size) {
                Object obj = arrayList2.get(i2);
                i2++;
                GoogleApiClient.ConnectionCallbacks connectionCallbacks = (GoogleApiClient.ConnectionCallbacks) obj;
                if (!this.zzaHK || this.zzaHL.get() != i3) {
                    break;
                } else if (this.zzaHH.contains(connectionCallbacks)) {
                    connectionCallbacks.onConnectionSuspended(i);
                }
            }
            this.zzaHI.clear();
            this.zzaHM = false;
        }
    }

    public final void zzk(ConnectionResult connectionResult) {
        int i = 0;
        zzbr.zza(Looper.myLooper() == this.mHandler.getLooper(), "onConnectionFailure must only be called on the Handler thread");
        this.mHandler.removeMessages(1);
        synchronized (this.mLock) {
            ArrayList arrayList = new ArrayList(this.zzaHJ);
            int i2 = this.zzaHL.get();
            ArrayList arrayList2 = arrayList;
            int size = arrayList2.size();
            while (i < size) {
                Object obj = arrayList2.get(i);
                i++;
                GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener = (GoogleApiClient.OnConnectionFailedListener) obj;
                if (!this.zzaHK || this.zzaHL.get() != i2) {
                    return;
                }
                if (this.zzaHJ.contains(onConnectionFailedListener)) {
                    onConnectionFailedListener.onConnectionFailed(connectionResult);
                }
            }
        }
    }

    public final void zzn(Bundle bundle) {
        int i = 0;
        zzbr.zza(Looper.myLooper() == this.mHandler.getLooper(), "onConnectionSuccess must only be called on the Handler thread");
        synchronized (this.mLock) {
            zzbr.zzae(!this.zzaHM);
            this.mHandler.removeMessages(1);
            this.zzaHM = true;
            zzbr.zzae(this.zzaHI.size() == 0);
            ArrayList arrayList = new ArrayList(this.zzaHH);
            int i2 = this.zzaHL.get();
            ArrayList arrayList2 = arrayList;
            int size = arrayList2.size();
            while (i < size) {
                Object obj = arrayList2.get(i);
                i++;
                GoogleApiClient.ConnectionCallbacks connectionCallbacks = (GoogleApiClient.ConnectionCallbacks) obj;
                if (!this.zzaHK || !this.zzaHG.isConnected() || this.zzaHL.get() != i2) {
                    break;
                } else if (!this.zzaHI.contains(connectionCallbacks)) {
                    connectionCallbacks.onConnected(bundle);
                }
            }
            this.zzaHI.clear();
            this.zzaHM = false;
        }
    }

    public final void zzry() {
        this.zzaHK = false;
        this.zzaHL.incrementAndGet();
    }

    public final void zzrz() {
        this.zzaHK = true;
    }
}
