package com.google.android.gms.internal;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.support.annotation.WorkerThread;
import android.support.v4.util.ArrayMap;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.measurement.AppMeasurement;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.io.IOException;
import java.util.Map;

/* JADX INFO: loaded from: classes36.dex */
public final class zzchr extends zzciv {
    private final Map<String, Map<String, String>> zzbrU;
    private final Map<String, Map<String, Boolean>> zzbrV;
    private final Map<String, Map<String, Boolean>> zzbrW;
    private final Map<String, zzclf> zzbrX;
    private final Map<String, String> zzbrY;

    zzchr(zzchx zzchxVar) {
        super(zzchxVar);
        this.zzbrU = new ArrayMap();
        this.zzbrV = new ArrayMap();
        this.zzbrW = new ArrayMap();
        this.zzbrX = new ArrayMap();
        this.zzbrY = new ArrayMap();
    }

    private static Map<String, String> zza(zzclf zzclfVar) {
        ArrayMap arrayMap = new ArrayMap();
        if (zzclfVar != null && zzclfVar.zzbvr != null) {
            for (zzclg zzclgVar : zzclfVar.zzbvr) {
                if (zzclgVar != null) {
                    arrayMap.put(zzclgVar.key, zzclgVar.value);
                }
            }
        }
        return arrayMap;
    }

    private final void zza(String str, zzclf zzclfVar) {
        ArrayMap arrayMap = new ArrayMap();
        ArrayMap arrayMap2 = new ArrayMap();
        if (zzclfVar != null && zzclfVar.zzbvs != null) {
            for (zzcle zzcleVar : zzclfVar.zzbvs) {
                if (zzcleVar != null) {
                    String strZzdG = AppMeasurement.Event.zzdG(zzcleVar.name);
                    if (strZzdG != null) {
                        zzcleVar.name = strZzdG;
                    }
                    arrayMap.put(zzcleVar.name, zzcleVar.zzbvn);
                    arrayMap2.put(zzcleVar.name, zzcleVar.zzbvo);
                }
            }
        }
        this.zzbrV.put(str, arrayMap);
        this.zzbrW.put(str, arrayMap2);
    }

    @WorkerThread
    private final zzclf zzc(String str, byte[] bArr) {
        if (bArr == null) {
            return new zzclf();
        }
        ahw ahwVarZzb = ahw.zzb(bArr, 0, bArr.length);
        zzclf zzclfVar = new zzclf();
        try {
            zzclfVar.zza(ahwVarZzb);
            super.zzwE().zzyB().zze("Parsed config. version, gmp_app_id", zzclfVar.zzbvp, zzclfVar.zzboU);
            return zzclfVar;
        } catch (IOException e) {
            super.zzwE().zzyx().zze("Unable to merge remote config. appId", zzcgx.zzea(str), e);
            return new zzclf();
        }
    }

    @WorkerThread
    private final void zzeh(String str) throws Throwable {
        zzkC();
        super.zzjB();
        zzbr.zzcF(str);
        if (this.zzbrX.get(str) == null) {
            byte[] bArrZzdT = super.zzwy().zzdT(str);
            if (bArrZzdT == null) {
                this.zzbrU.put(str, null);
                this.zzbrV.put(str, null);
                this.zzbrW.put(str, null);
                this.zzbrX.put(str, null);
                this.zzbrY.put(str, null);
                return;
            }
            zzclf zzclfVarZzc = zzc(str, bArrZzdT);
            this.zzbrU.put(str, zza(zzclfVarZzc));
            zza(str, zzclfVarZzc);
            this.zzbrX.put(str, zzclfVarZzc);
            this.zzbrY.put(str, null);
        }
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    @WorkerThread
    final String zzM(String str, String str2) {
        super.zzjB();
        zzeh(str);
        Map<String, String> map = this.zzbrU.get(str);
        if (map != null) {
            return map.get(str2);
        }
        return null;
    }

    @WorkerThread
    final boolean zzN(String str, String str2) throws Throwable {
        Boolean bool;
        super.zzjB();
        zzeh(str);
        if (super.zzwA().zzeB(str) && zzckx.zzey(str2)) {
            return true;
        }
        if (super.zzwA().zzeC(str) && zzckx.zzep(str2)) {
            return true;
        }
        Map<String, Boolean> map = this.zzbrV.get(str);
        if (map != null && (bool = map.get(str2)) != null) {
            return bool.booleanValue();
        }
        return false;
    }

    @WorkerThread
    final boolean zzO(String str, String str2) {
        Boolean bool;
        super.zzjB();
        zzeh(str);
        if (FirebaseAnalytics.Event.ECOMMERCE_PURCHASE.equals(str2)) {
            return true;
        }
        Map<String, Boolean> map = this.zzbrW.get(str);
        if (map != null && (bool = map.get(str2)) != null) {
            return bool.booleanValue();
        }
        return false;
    }

    @WorkerThread
    protected final boolean zzb(String str, byte[] bArr, String str2) {
        zzkC();
        super.zzjB();
        zzbr.zzcF(str);
        zzclf zzclfVarZzc = zzc(str, bArr);
        if (zzclfVarZzc == null) {
            return false;
        }
        zza(str, zzclfVarZzc);
        this.zzbrX.put(str, zzclfVarZzc);
        this.zzbrY.put(str, str2);
        this.zzbrU.put(str, zza(zzclfVarZzc));
        zzcfv zzcfvVarZzwr = super.zzwr();
        zzcky[] zzckyVarArr = zzclfVarZzc.zzbvt;
        zzbr.zzu(zzckyVarArr);
        for (zzcky zzckyVar : zzckyVarArr) {
            for (zzckz zzckzVar : zzckyVar.zzbuO) {
                String strZzdG = AppMeasurement.Event.zzdG(zzckzVar.zzbuR);
                if (strZzdG != null) {
                    zzckzVar.zzbuR = strZzdG;
                }
                zzcla[] zzclaVarArr = zzckzVar.zzbuS;
                for (zzcla zzclaVar : zzclaVarArr) {
                    String strZzdG2 = AppMeasurement.Param.zzdG(zzclaVar.zzbuZ);
                    if (strZzdG2 != null) {
                        zzclaVar.zzbuZ = strZzdG2;
                    }
                }
            }
            for (zzclc zzclcVar : zzckyVar.zzbuN) {
                String strZzdG3 = AppMeasurement.UserProperty.zzdG(zzclcVar.zzbvg);
                if (strZzdG3 != null) {
                    zzclcVar.zzbvg = strZzdG3;
                }
            }
        }
        zzcfvVarZzwr.zzwy().zza(str, zzckyVarArr);
        try {
            zzclfVarZzc.zzbvt = null;
            byte[] bArr2 = new byte[zzclfVarZzc.zzMl()];
            zzclfVarZzc.zza(ahx.zzc(bArr2, 0, bArr2.length));
            bArr = bArr2;
        } catch (IOException e) {
            super.zzwE().zzyx().zze("Unable to serialize reduced-size config. Storing full config instead. appId", zzcgx.zzea(str), e);
        }
        zzcfz zzcfzVarZzwy = super.zzwy();
        zzbr.zzcF(str);
        zzcfzVarZzwy.zzjB();
        zzcfzVarZzwy.zzkC();
        new ContentValues().put("remote_config", bArr);
        try {
            if (zzcfzVarZzwy.getWritableDatabase().update("apps", r2, "app_id = ?", new String[]{str}) == 0) {
                zzcfzVarZzwy.zzwE().zzyv().zzj("Failed to update remote config (got 0). appId", zzcgx.zzea(str));
            }
        } catch (SQLiteException e2) {
            zzcfzVarZzwy.zzwE().zzyv().zze("Error storing remote config. appId", zzcgx.zzea(str), e2);
        }
        return true;
    }

    @WorkerThread
    protected final zzclf zzei(String str) {
        zzkC();
        super.zzjB();
        zzbr.zzcF(str);
        zzeh(str);
        return this.zzbrX.get(str);
    }

    @WorkerThread
    protected final String zzej(String str) {
        super.zzjB();
        return this.zzbrY.get(str);
    }

    @WorkerThread
    protected final void zzek(String str) {
        super.zzjB();
        this.zzbrY.put(str, null);
    }

    @WorkerThread
    final void zzel(String str) {
        super.zzjB();
        this.zzbrX.remove(str);
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ void zzjB() {
        super.zzjB();
    }

    @Override // com.google.android.gms.internal.zzciv
    protected final void zzjC() {
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
}
