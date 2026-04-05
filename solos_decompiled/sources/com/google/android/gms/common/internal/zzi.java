package com.google.android.gms.common.internal;

import android.util.Log;

/* JADX INFO: loaded from: classes67.dex */
public abstract class zzi<TListener> {
    private TListener mListener;
    private /* synthetic */ zzd zzaHg;
    private boolean zzaHh = false;

    public zzi(zzd zzdVar, TListener tlistener) {
        this.zzaHg = zzdVar;
        this.mListener = tlistener;
    }

    public final void removeListener() {
        synchronized (this) {
            this.mListener = null;
        }
    }

    public final void unregister() {
        removeListener();
        synchronized (this.zzaHg.zzaGU) {
            this.zzaHg.zzaGU.remove(this);
        }
    }

    public final void zzri() {
        TListener tlistener;
        synchronized (this) {
            tlistener = this.mListener;
            if (this.zzaHh) {
                String strValueOf = String.valueOf(this);
                Log.w("GmsClient", new StringBuilder(String.valueOf(strValueOf).length() + 47).append("Callback proxy ").append(strValueOf).append(" being reused. This is not safe.").toString());
            }
        }
        if (tlistener != null) {
            try {
                zzs(tlistener);
            } catch (RuntimeException e) {
                throw e;
            }
        }
        synchronized (this) {
            this.zzaHh = true;
        }
        unregister();
    }

    protected abstract void zzs(TListener tlistener);
}
