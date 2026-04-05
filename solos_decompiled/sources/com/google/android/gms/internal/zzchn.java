package com.google.android.gms.internal;

import android.content.SharedPreferences;
import android.support.annotation.WorkerThread;
import com.google.android.gms.common.internal.zzbr;

/* JADX INFO: loaded from: classes36.dex */
public final class zzchn {
    private String mValue;
    private final String zzBQ;
    private boolean zzbrI;
    private /* synthetic */ zzchi zzbrJ;
    private final String zzbrO;

    public zzchn(zzchi zzchiVar, String str, String str2) {
        this.zzbrJ = zzchiVar;
        zzbr.zzcF(str);
        this.zzBQ = str;
        this.zzbrO = null;
    }

    @WorkerThread
    public final void zzeg(String str) {
        if (zzckx.zzR(str, this.mValue)) {
            return;
        }
        SharedPreferences.Editor editorEdit = this.zzbrJ.zzaiz.edit();
        editorEdit.putString(this.zzBQ, str);
        editorEdit.apply();
        this.mValue = str;
    }

    @WorkerThread
    public final String zzyJ() {
        if (!this.zzbrI) {
            this.zzbrI = true;
            this.mValue = this.zzbrJ.zzaiz.getString(this.zzBQ, null);
        }
        return this.mValue;
    }
}
