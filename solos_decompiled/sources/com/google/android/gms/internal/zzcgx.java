package com.google.android.gms.internal;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.measurement.AppMeasurement;

/* JADX INFO: loaded from: classes36.dex */
public final class zzcgx extends zzciv {
    private final String zzaId;
    private final long zzboG;
    private final char zzbqP;
    private final zzcgz zzbqQ;
    private final zzcgz zzbqR;
    private final zzcgz zzbqS;
    private final zzcgz zzbqT;
    private final zzcgz zzbqU;
    private final zzcgz zzbqV;
    private final zzcgz zzbqW;
    private final zzcgz zzbqX;
    private final zzcgz zzbqY;

    zzcgx(zzchx zzchxVar) {
        super(zzchxVar);
        this.zzaId = zzcfy.zzxe();
        this.zzboG = zzcfy.zzwO();
        if (super.zzwG().zzlm()) {
            zzcfy.zzxD();
            this.zzbqP = 'C';
        } else {
            zzcfy.zzxD();
            this.zzbqP = 'c';
        }
        this.zzbqQ = new zzcgz(this, 6, false, false);
        this.zzbqR = new zzcgz(this, 6, true, false);
        this.zzbqS = new zzcgz(this, 6, false, true);
        this.zzbqT = new zzcgz(this, 5, false, false);
        this.zzbqU = new zzcgz(this, 5, true, false);
        this.zzbqV = new zzcgz(this, 5, false, true);
        this.zzbqW = new zzcgz(this, 4, false, false);
        this.zzbqX = new zzcgz(this, 3, false, false);
        this.zzbqY = new zzcgz(this, 2, false, false);
    }

    private static String zza(boolean z, String str, Object obj, Object obj2, Object obj3) {
        if (str == null) {
            str = "";
        }
        String strZzc = zzc(z, obj);
        String strZzc2 = zzc(z, obj2);
        String strZzc3 = zzc(z, obj3);
        StringBuilder sb = new StringBuilder();
        String str2 = "";
        if (!TextUtils.isEmpty(str)) {
            sb.append(str);
            str2 = ": ";
        }
        if (!TextUtils.isEmpty(strZzc)) {
            sb.append(str2);
            sb.append(strZzc);
            str2 = ", ";
        }
        if (!TextUtils.isEmpty(strZzc2)) {
            sb.append(str2);
            sb.append(strZzc2);
            str2 = ", ";
        }
        if (!TextUtils.isEmpty(strZzc3)) {
            sb.append(str2);
            sb.append(strZzc3);
        }
        return sb.toString();
    }

    private static String zzc(boolean z, Object obj) {
        String className;
        if (obj == null) {
            return "";
        }
        Object objValueOf = obj instanceof Integer ? Long.valueOf(((Integer) obj).intValue()) : obj;
        if (objValueOf instanceof Long) {
            if (z && Math.abs(((Long) objValueOf).longValue()) >= 100) {
                String str = String.valueOf(objValueOf).charAt(0) == '-' ? "-" : "";
                String strValueOf = String.valueOf(Math.abs(((Long) objValueOf).longValue()));
                return new StringBuilder(String.valueOf(str).length() + 43 + String.valueOf(str).length()).append(str).append(Math.round(Math.pow(10.0d, strValueOf.length() - 1))).append("...").append(str).append(Math.round(Math.pow(10.0d, strValueOf.length()) - 1.0d)).toString();
            }
            return String.valueOf(objValueOf);
        }
        if (objValueOf instanceof Boolean) {
            return String.valueOf(objValueOf);
        }
        if (!(objValueOf instanceof Throwable)) {
            return objValueOf instanceof zzcha ? ((zzcha) objValueOf).zzbrd : z ? "-" : String.valueOf(objValueOf);
        }
        Throwable th = (Throwable) objValueOf;
        StringBuilder sb = new StringBuilder(z ? th.getClass().getName() : th.toString());
        String strZzeb = zzeb(AppMeasurement.class.getCanonicalName());
        String strZzeb2 = zzeb(zzchx.class.getCanonicalName());
        for (StackTraceElement stackTraceElement : th.getStackTrace()) {
            if (!stackTraceElement.isNativeMethod() && (className = stackTraceElement.getClassName()) != null) {
                String strZzeb3 = zzeb(className);
                if (strZzeb3.equals(strZzeb) || strZzeb3.equals(strZzeb2)) {
                    sb.append(": ");
                    sb.append(stackTraceElement);
                    break;
                }
            }
        }
        return sb.toString();
    }

    protected static Object zzea(String str) {
        if (str == null) {
            return null;
        }
        return new zzcha(str);
    }

    private static String zzeb(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        int iLastIndexOf = str.lastIndexOf(46);
        return iLastIndexOf != -1 ? str.substring(0, iLastIndexOf) : str;
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    protected final void zza(int i, boolean z, boolean z2, String str, Object obj, Object obj2, Object obj3) {
        if (!z && zzz(i)) {
            zzk(i, zza(false, str, obj, obj2, obj3));
        }
        if (z2 || i < 5) {
            return;
        }
        zzbr.zzu(str);
        zzchs zzchsVarZzyP = this.zzboi.zzyP();
        if (zzchsVarZzyP == null) {
            zzk(6, "Scheduler not set. Not logging error/warn");
            return;
        }
        if (!zzchsVarZzyP.isInitialized()) {
            zzk(6, "Scheduler not initialized. Not logging error/warn");
            return;
        }
        int i2 = i < 0 ? 0 : i;
        if (i2 >= 9) {
            i2 = 8;
        }
        String strValueOf = String.valueOf("2");
        char cCharAt = "01VDIWEA?".charAt(i2);
        char c = this.zzbqP;
        long j = this.zzboG;
        String strValueOf2 = String.valueOf(zza(true, str, obj, obj2, obj3));
        String string = new StringBuilder(String.valueOf(strValueOf).length() + 23 + String.valueOf(strValueOf2).length()).append(strValueOf).append(cCharAt).append(c).append(j).append(":").append(strValueOf2).toString();
        if (string.length() > 1024) {
            string = str.substring(0, 1024);
        }
        zzchsVarZzyP.zzj(new zzcgy(this, string));
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ void zzjB() {
        super.zzjB();
    }

    @Override // com.google.android.gms.internal.zzciv
    protected final void zzjC() {
    }

    protected final void zzk(int i, String str) {
        Log.println(i, this.zzaId, str);
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ com.google.android.gms.common.util.zzf zzkp() {
        return super.zzkp();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzckx zzwA() {
        return super.zzwA();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzchr zzwB() {
        return super.zzwB();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzckm zzwC() {
        return super.zzwC();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzchs zzwD() {
        return super.zzwD();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcgx zzwE() {
        return super.zzwE();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzchi zzwF() {
        return super.zzwF();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcfy zzwG() {
        return super.zzwG();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ void zzwn() {
        super.zzwn();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ void zzwo() {
        super.zzwo();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ void zzwp() {
        super.zzwp();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcfo zzwq() {
        return super.zzwq();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcfv zzwr() {
        return super.zzwr();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcix zzws() {
        return super.zzws();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcgs zzwt() {
        return super.zzwt();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcgf zzwu() {
        return super.zzwu();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcjp zzwv() {
        return super.zzwv();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcjl zzww() {
        return super.zzww();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcgt zzwx() {
        return super.zzwx();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcfz zzwy() {
        return super.zzwy();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcgv zzwz() {
        return super.zzwz();
    }

    public final zzcgz zzyA() {
        return this.zzbqX;
    }

    public final zzcgz zzyB() {
        return this.zzbqY;
    }

    public final String zzyC() {
        Pair<String, Long> pairZzlZ = super.zzwF().zzbrn.zzlZ();
        if (pairZzlZ == null || pairZzlZ == zzchi.zzbrm) {
            return null;
        }
        String strValueOf = String.valueOf(String.valueOf(pairZzlZ.second));
        String str = (String) pairZzlZ.first;
        return new StringBuilder(String.valueOf(strValueOf).length() + 1 + String.valueOf(str).length()).append(strValueOf).append(":").append(str).toString();
    }

    public final zzcgz zzyv() {
        return this.zzbqQ;
    }

    public final zzcgz zzyw() {
        return this.zzbqR;
    }

    public final zzcgz zzyx() {
        return this.zzbqT;
    }

    public final zzcgz zzyy() {
        return this.zzbqV;
    }

    public final zzcgz zzyz() {
        return this.zzbqW;
    }

    protected final boolean zzz(int i) {
        return Log.isLoggable(this.zzaId, i);
    }
}
