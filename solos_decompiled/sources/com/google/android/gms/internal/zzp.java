package com.google.android.gms.internal;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.google.android.gms.internal.zzab;
import java.util.Collections;
import java.util.Map;

/* JADX INFO: loaded from: classes67.dex */
public abstract class zzp<T> implements Comparable<zzp<T>> {
    private final zzab.zza zzB;
    private final int zzC;
    private final String zzD;
    private final int zzE;
    private final zzu zzF;
    private Integer zzG;
    private zzs zzH;
    private boolean zzI;
    private boolean zzJ;
    private boolean zzK;
    private boolean zzL;
    private zzx zzM;
    private zzc zzN;

    public zzp(int i, String str, zzu zzuVar) {
        Uri uri;
        String host;
        this.zzB = zzab.zza.zzai ? new zzab.zza() : null;
        this.zzI = true;
        this.zzJ = false;
        this.zzK = false;
        this.zzL = false;
        this.zzN = null;
        this.zzC = i;
        this.zzD = str;
        this.zzF = zzuVar;
        this.zzM = new zzg();
        this.zzE = (TextUtils.isEmpty(str) || (uri = Uri.parse(str)) == null || (host = uri.getHost()) == null) ? 0 : host.hashCode();
    }

    public static String zzf() {
        String strValueOf = String.valueOf("UTF-8");
        return strValueOf.length() != 0 ? "application/x-www-form-urlencoded; charset=".concat(strValueOf) : new String("application/x-www-form-urlencoded; charset=");
    }

    @Override // java.lang.Comparable
    public /* synthetic */ int compareTo(Object obj) {
        zzp zzpVar = (zzp) obj;
        zzr zzrVar = zzr.NORMAL;
        zzr zzrVar2 = zzr.NORMAL;
        return zzrVar == zzrVar2 ? this.zzG.intValue() - zzpVar.zzG.intValue() : zzrVar2.ordinal() - zzrVar.ordinal();
    }

    public Map<String, String> getHeaders() throws zza {
        return Collections.emptyMap();
    }

    public final int getMethod() {
        return this.zzC;
    }

    public final String getUrl() {
        return this.zzD;
    }

    public String toString() {
        String strValueOf = String.valueOf(Integer.toHexString(this.zzE));
        String strConcat = strValueOf.length() != 0 ? "0x".concat(strValueOf) : new String("0x");
        String strValueOf2 = String.valueOf(this.zzD);
        String strValueOf3 = String.valueOf(zzr.NORMAL);
        String strValueOf4 = String.valueOf(this.zzG);
        return new StringBuilder(String.valueOf("[ ] ").length() + 3 + String.valueOf(strValueOf2).length() + String.valueOf(strConcat).length() + String.valueOf(strValueOf3).length() + String.valueOf(strValueOf4).length()).append("[ ] ").append(strValueOf2).append(" ").append(strConcat).append(" ").append(strValueOf3).append(" ").append(strValueOf4).toString();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final zzp<?> zza(int i) {
        this.zzG = Integer.valueOf(i);
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final zzp<?> zza(zzc zzcVar) {
        this.zzN = zzcVar;
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final zzp<?> zza(zzs zzsVar) {
        this.zzH = zzsVar;
        return this;
    }

    protected abstract zzt<T> zza(zzn zznVar);

    protected abstract void zza(T t);

    public final void zzb(zzaa zzaaVar) {
        if (this.zzF != null) {
            this.zzF.zzd(zzaaVar);
        }
    }

    public final void zzb(String str) {
        if (zzab.zza.zzai) {
            this.zzB.zza(str, Thread.currentThread().getId());
        }
    }

    public final int zzc() {
        return this.zzE;
    }

    final void zzc(String str) {
        if (this.zzH != null) {
            this.zzH.zzd(this);
        }
        if (zzab.zza.zzai) {
            long id = Thread.currentThread().getId();
            if (Looper.myLooper() != Looper.getMainLooper()) {
                new Handler(Looper.getMainLooper()).post(new zzq(this, str, id));
            } else {
                this.zzB.zza(str, id);
                this.zzB.zzc(toString());
            }
        }
    }

    public final String zzd() {
        return this.zzD;
    }

    public final zzc zze() {
        return this.zzN;
    }

    public byte[] zzg() throws zza {
        return null;
    }

    public final boolean zzh() {
        return this.zzI;
    }

    public final int zzi() {
        return this.zzM.zza();
    }

    public final zzx zzj() {
        return this.zzM;
    }

    public final void zzk() {
        this.zzK = true;
    }

    public final boolean zzl() {
        return this.zzK;
    }
}
