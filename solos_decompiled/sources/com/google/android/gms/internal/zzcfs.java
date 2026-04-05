package com.google.android.gms.internal;

import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import com.google.android.gms.common.internal.zzbr;

/* JADX INFO: loaded from: classes36.dex */
final class zzcfs {
    private final String mAppId;
    private String zzXD;
    private String zzaKI;
    private String zzaeK;
    private String zzboA;
    private long zzboB;
    private long zzboC;
    private long zzboD;
    private long zzboE;
    private String zzboF;
    private long zzboG;
    private long zzboH;
    private boolean zzboI;
    private long zzboJ;
    private long zzboK;
    private long zzboL;
    private long zzboM;
    private long zzboN;
    private long zzboO;
    private long zzboP;
    private String zzboQ;
    private boolean zzboR;
    private long zzboS;
    private long zzboT;
    private final zzchx zzboi;
    private String zzboz;

    @WorkerThread
    zzcfs(zzchx zzchxVar, String str) {
        zzbr.zzu(zzchxVar);
        zzbr.zzcF(str);
        this.zzboi = zzchxVar;
        this.mAppId = str;
        this.zzboi.zzwD().zzjB();
    }

    @WorkerThread
    public final String getAppInstanceId() {
        this.zzboi.zzwD().zzjB();
        return this.zzaKI;
    }

    @WorkerThread
    public final String getGmpAppId() {
        this.zzboi.zzwD().zzjB();
        return this.zzXD;
    }

    @WorkerThread
    public final void setAppVersion(String str) {
        this.zzboi.zzwD().zzjB();
        this.zzboR = (!zzckx.zzR(this.zzaeK, str)) | this.zzboR;
        this.zzaeK = str;
    }

    @WorkerThread
    public final void setMeasurementEnabled(boolean z) {
        this.zzboi.zzwD().zzjB();
        this.zzboR = (this.zzboI != z) | this.zzboR;
        this.zzboI = z;
    }

    @WorkerThread
    public final void zzL(long j) {
        this.zzboi.zzwD().zzjB();
        this.zzboR = (this.zzboC != j) | this.zzboR;
        this.zzboC = j;
    }

    @WorkerThread
    public final void zzM(long j) {
        this.zzboi.zzwD().zzjB();
        this.zzboR = (this.zzboD != j) | this.zzboR;
        this.zzboD = j;
    }

    @WorkerThread
    public final void zzN(long j) {
        this.zzboi.zzwD().zzjB();
        this.zzboR = (this.zzboE != j) | this.zzboR;
        this.zzboE = j;
    }

    @WorkerThread
    public final void zzO(long j) {
        this.zzboi.zzwD().zzjB();
        this.zzboR = (this.zzboG != j) | this.zzboR;
        this.zzboG = j;
    }

    @WorkerThread
    public final void zzP(long j) {
        this.zzboi.zzwD().zzjB();
        this.zzboR = (this.zzboH != j) | this.zzboR;
        this.zzboH = j;
    }

    @WorkerThread
    public final void zzQ(long j) {
        zzbr.zzaf(j >= 0);
        this.zzboi.zzwD().zzjB();
        this.zzboR |= this.zzboB != j;
        this.zzboB = j;
    }

    @WorkerThread
    public final void zzR(long j) {
        this.zzboi.zzwD().zzjB();
        this.zzboR = (this.zzboS != j) | this.zzboR;
        this.zzboS = j;
    }

    @WorkerThread
    public final void zzS(long j) {
        this.zzboi.zzwD().zzjB();
        this.zzboR = (this.zzboT != j) | this.zzboR;
        this.zzboT = j;
    }

    @WorkerThread
    public final void zzT(long j) {
        this.zzboi.zzwD().zzjB();
        this.zzboR = (this.zzboK != j) | this.zzboR;
        this.zzboK = j;
    }

    @WorkerThread
    public final void zzU(long j) {
        this.zzboi.zzwD().zzjB();
        this.zzboR = (this.zzboL != j) | this.zzboR;
        this.zzboL = j;
    }

    @WorkerThread
    public final void zzV(long j) {
        this.zzboi.zzwD().zzjB();
        this.zzboR = (this.zzboM != j) | this.zzboR;
        this.zzboM = j;
    }

    @WorkerThread
    public final void zzW(long j) {
        this.zzboi.zzwD().zzjB();
        this.zzboR = (this.zzboN != j) | this.zzboR;
        this.zzboN = j;
    }

    @WorkerThread
    public final void zzX(long j) {
        this.zzboi.zzwD().zzjB();
        this.zzboR = (this.zzboP != j) | this.zzboR;
        this.zzboP = j;
    }

    @WorkerThread
    public final void zzY(long j) {
        this.zzboi.zzwD().zzjB();
        this.zzboR = (this.zzboO != j) | this.zzboR;
        this.zzboO = j;
    }

    @WorkerThread
    public final void zzZ(long j) {
        this.zzboi.zzwD().zzjB();
        this.zzboR = (this.zzboJ != j) | this.zzboR;
        this.zzboJ = j;
    }

    @WorkerThread
    public final void zzdH(String str) {
        this.zzboi.zzwD().zzjB();
        this.zzboR = (!zzckx.zzR(this.zzaKI, str)) | this.zzboR;
        this.zzaKI = str;
    }

    @WorkerThread
    public final void zzdI(String str) {
        this.zzboi.zzwD().zzjB();
        if (TextUtils.isEmpty(str)) {
            str = null;
        }
        this.zzboR = (!zzckx.zzR(this.zzXD, str)) | this.zzboR;
        this.zzXD = str;
    }

    @WorkerThread
    public final void zzdJ(String str) {
        this.zzboi.zzwD().zzjB();
        this.zzboR = (!zzckx.zzR(this.zzboz, str)) | this.zzboR;
        this.zzboz = str;
    }

    @WorkerThread
    public final void zzdK(String str) {
        this.zzboi.zzwD().zzjB();
        this.zzboR = (!zzckx.zzR(this.zzboA, str)) | this.zzboR;
        this.zzboA = str;
    }

    @WorkerThread
    public final void zzdL(String str) {
        this.zzboi.zzwD().zzjB();
        this.zzboR = (!zzckx.zzR(this.zzboF, str)) | this.zzboR;
        this.zzboF = str;
    }

    @WorkerThread
    public final void zzdM(String str) {
        this.zzboi.zzwD().zzjB();
        this.zzboR = (!zzckx.zzR(this.zzboQ, str)) | this.zzboR;
        this.zzboQ = str;
    }

    @WorkerThread
    public final String zzhk() {
        this.zzboi.zzwD().zzjB();
        return this.mAppId;
    }

    @WorkerThread
    public final String zzjG() {
        this.zzboi.zzwD().zzjB();
        return this.zzaeK;
    }

    @WorkerThread
    public final void zzwH() {
        this.zzboi.zzwD().zzjB();
        this.zzboR = false;
    }

    @WorkerThread
    public final String zzwI() {
        this.zzboi.zzwD().zzjB();
        return this.zzboz;
    }

    @WorkerThread
    public final String zzwJ() {
        this.zzboi.zzwD().zzjB();
        return this.zzboA;
    }

    @WorkerThread
    public final long zzwK() {
        this.zzboi.zzwD().zzjB();
        return this.zzboC;
    }

    @WorkerThread
    public final long zzwL() {
        this.zzboi.zzwD().zzjB();
        return this.zzboD;
    }

    @WorkerThread
    public final long zzwM() {
        this.zzboi.zzwD().zzjB();
        return this.zzboE;
    }

    @WorkerThread
    public final String zzwN() {
        this.zzboi.zzwD().zzjB();
        return this.zzboF;
    }

    @WorkerThread
    public final long zzwO() {
        this.zzboi.zzwD().zzjB();
        return this.zzboG;
    }

    @WorkerThread
    public final long zzwP() {
        this.zzboi.zzwD().zzjB();
        return this.zzboH;
    }

    @WorkerThread
    public final boolean zzwQ() {
        this.zzboi.zzwD().zzjB();
        return this.zzboI;
    }

    @WorkerThread
    public final long zzwR() {
        this.zzboi.zzwD().zzjB();
        return this.zzboB;
    }

    @WorkerThread
    public final long zzwS() {
        this.zzboi.zzwD().zzjB();
        return this.zzboS;
    }

    @WorkerThread
    public final long zzwT() {
        this.zzboi.zzwD().zzjB();
        return this.zzboT;
    }

    @WorkerThread
    public final void zzwU() {
        this.zzboi.zzwD().zzjB();
        long j = this.zzboB + 1;
        if (j > 2147483647L) {
            this.zzboi.zzwE().zzyx().zzj("Bundle index overflow. appId", zzcgx.zzea(this.mAppId));
            j = 0;
        }
        this.zzboR = true;
        this.zzboB = j;
    }

    @WorkerThread
    public final long zzwV() {
        this.zzboi.zzwD().zzjB();
        return this.zzboK;
    }

    @WorkerThread
    public final long zzwW() {
        this.zzboi.zzwD().zzjB();
        return this.zzboL;
    }

    @WorkerThread
    public final long zzwX() {
        this.zzboi.zzwD().zzjB();
        return this.zzboM;
    }

    @WorkerThread
    public final long zzwY() {
        this.zzboi.zzwD().zzjB();
        return this.zzboN;
    }

    @WorkerThread
    public final long zzwZ() {
        this.zzboi.zzwD().zzjB();
        return this.zzboP;
    }

    @WorkerThread
    public final long zzxa() {
        this.zzboi.zzwD().zzjB();
        return this.zzboO;
    }

    @WorkerThread
    public final String zzxb() {
        this.zzboi.zzwD().zzjB();
        return this.zzboQ;
    }

    @WorkerThread
    public final String zzxc() {
        this.zzboi.zzwD().zzjB();
        String str = this.zzboQ;
        zzdM(null);
        return str;
    }

    @WorkerThread
    public final long zzxd() {
        this.zzboi.zzwD().zzjB();
        return this.zzboJ;
    }
}
