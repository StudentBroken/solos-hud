package com.google.android.gms.internal;

import android.content.SharedPreferences;
import android.support.annotation.WorkerThread;
import com.google.android.gms.common.internal.zzbr;

/* JADX INFO: loaded from: classes36.dex */
public final class zzchl {
    private final String zzBQ;
    private long zzafb;
    private boolean zzbrI;
    private /* synthetic */ zzchi zzbrJ;
    private final long zzbrK;

    public zzchl(zzchi zzchiVar, String str, long j) {
        this.zzbrJ = zzchiVar;
        zzbr.zzcF(str);
        this.zzBQ = str;
        this.zzbrK = j;
    }

    @WorkerThread
    public final long get() {
        if (!this.zzbrI) {
            this.zzbrI = true;
            this.zzafb = this.zzbrJ.zzaiz.getLong(this.zzBQ, this.zzbrK);
        }
        return this.zzafb;
    }

    @WorkerThread
    public final void set(long j) {
        SharedPreferences.Editor editorEdit = this.zzbrJ.zzaiz.edit();
        editorEdit.putLong(this.zzBQ, j);
        editorEdit.apply();
        this.zzafb = j;
    }
}
