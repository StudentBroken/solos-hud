package com.google.android.gms.internal;

import android.content.SharedPreferences;
import android.support.annotation.WorkerThread;
import com.google.android.gms.common.internal.zzbr;

/* JADX INFO: loaded from: classes36.dex */
public final class zzchk {
    private final String zzBQ;
    private boolean zzaAK;
    private final boolean zzbrH;
    private boolean zzbrI;
    private /* synthetic */ zzchi zzbrJ;

    public zzchk(zzchi zzchiVar, String str, boolean z) {
        this.zzbrJ = zzchiVar;
        zzbr.zzcF(str);
        this.zzBQ = str;
        this.zzbrH = true;
    }

    @WorkerThread
    public final boolean get() {
        if (!this.zzbrI) {
            this.zzbrI = true;
            this.zzaAK = this.zzbrJ.zzaiz.getBoolean(this.zzBQ, this.zzbrH);
        }
        return this.zzaAK;
    }

    @WorkerThread
    public final void set(boolean z) {
        SharedPreferences.Editor editorEdit = this.zzbrJ.zzaiz.edit();
        editorEdit.putBoolean(this.zzBQ, z);
        editorEdit.apply();
        this.zzaAK = z;
    }
}
