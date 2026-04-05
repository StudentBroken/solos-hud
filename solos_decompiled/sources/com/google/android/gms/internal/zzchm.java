package com.google.android.gms.internal;

import android.content.SharedPreferences;
import android.support.annotation.WorkerThread;
import android.util.Pair;
import com.google.android.gms.common.internal.zzbr;

/* JADX INFO: loaded from: classes36.dex */
public final class zzchm {
    private final long zzaiD;
    private /* synthetic */ zzchi zzbrJ;
    private String zzbrL;
    private final String zzbrM;
    private final String zzbrN;

    private zzchm(zzchi zzchiVar, String str, long j) {
        this.zzbrJ = zzchiVar;
        zzbr.zzcF(str);
        zzbr.zzaf(j > 0);
        this.zzbrL = String.valueOf(str).concat(":start");
        this.zzbrM = String.valueOf(str).concat(":count");
        this.zzbrN = String.valueOf(str).concat(":value");
        this.zzaiD = j;
    }

    @WorkerThread
    private final void zzlY() {
        this.zzbrJ.zzjB();
        long jCurrentTimeMillis = this.zzbrJ.zzkp().currentTimeMillis();
        SharedPreferences.Editor editorEdit = this.zzbrJ.zzaiz.edit();
        editorEdit.remove(this.zzbrM);
        editorEdit.remove(this.zzbrN);
        editorEdit.putLong(this.zzbrL, jCurrentTimeMillis);
        editorEdit.apply();
    }

    @WorkerThread
    private final long zzma() {
        return this.zzbrJ.zzyD().getLong(this.zzbrL, 0L);
    }

    @WorkerThread
    public final void zzf(String str, long j) {
        this.zzbrJ.zzjB();
        if (zzma() == 0) {
            zzlY();
        }
        if (str == null) {
            str = "";
        }
        long j2 = this.zzbrJ.zzaiz.getLong(this.zzbrM, 0L);
        if (j2 <= 0) {
            SharedPreferences.Editor editorEdit = this.zzbrJ.zzaiz.edit();
            editorEdit.putString(this.zzbrN, str);
            editorEdit.putLong(this.zzbrM, 1L);
            editorEdit.apply();
            return;
        }
        boolean z = (this.zzbrJ.zzwA().zzzr().nextLong() & Long.MAX_VALUE) < Long.MAX_VALUE / (j2 + 1);
        SharedPreferences.Editor editorEdit2 = this.zzbrJ.zzaiz.edit();
        if (z) {
            editorEdit2.putString(this.zzbrN, str);
        }
        editorEdit2.putLong(this.zzbrM, j2 + 1);
        editorEdit2.apply();
    }

    @WorkerThread
    public final Pair<String, Long> zzlZ() {
        long jAbs;
        this.zzbrJ.zzjB();
        this.zzbrJ.zzjB();
        long jZzma = zzma();
        if (jZzma == 0) {
            zzlY();
            jAbs = 0;
        } else {
            jAbs = Math.abs(jZzma - this.zzbrJ.zzkp().currentTimeMillis());
        }
        if (jAbs < this.zzaiD) {
            return null;
        }
        if (jAbs > (this.zzaiD << 1)) {
            zzlY();
            return null;
        }
        String string = this.zzbrJ.zzyD().getString(this.zzbrN, null);
        long j = this.zzbrJ.zzyD().getLong(this.zzbrM, 0L);
        zzlY();
        return (string == null || j <= 0) ? zzchi.zzbrm : new Pair<>(string, Long.valueOf(j));
    }
}
