package com.google.android.gms.internal;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import android.text.TextUtils;
import com.facebook.appevents.AppEventsConstants;
import com.google.android.gms.common.internal.zzbr;
import java.lang.reflect.InvocationTargetException;

/* JADX INFO: loaded from: classes36.dex */
public final class zzcfy extends zzciu {
    private static String zzbpq = String.valueOf(com.google.android.gms.common.zze.GOOGLE_PLAY_SERVICES_VERSION_CODE / 1000).replaceAll("(\\d+)(\\d)(\\d\\d)", "$1.$2.$3");
    private Boolean zzagW;

    zzcfy(zzchx zzchxVar) {
        super(zzchxVar);
    }

    public static boolean zzqz() {
        return zzbey.zzqz();
    }

    public static long zzwO() {
        return 11010L;
    }

    static long zzxA() {
        return zzcgn.zzbqF.get().longValue();
    }

    public static String zzxB() {
        return "google_app_measurement.db";
    }

    static String zzxC() {
        return "google_app_measurement_local.db";
    }

    public static boolean zzxD() {
        return false;
    }

    public static long zzxF() {
        return zzcgn.zzbqC.get().longValue();
    }

    public static long zzxG() {
        return zzcgn.zzbqx.get().longValue();
    }

    public static long zzxH() {
        return zzcgn.zzbqy.get().longValue();
    }

    public static long zzxI() {
        return 1000L;
    }

    public static long zzxJ() {
        return Math.max(0L, zzcgn.zzbqb.get().longValue());
    }

    public static int zzxK() {
        return Math.max(0, zzcgn.zzbqh.get().intValue());
    }

    public static int zzxL() {
        return Math.max(1, zzcgn.zzbqi.get().intValue());
    }

    public static int zzxM() {
        return 100000;
    }

    public static String zzxN() {
        return zzcgn.zzbqp.get();
    }

    public static long zzxO() {
        return zzcgn.zzbqc.get().longValue();
    }

    public static long zzxP() {
        return Math.max(0L, zzcgn.zzbqq.get().longValue());
    }

    public static long zzxQ() {
        return Math.max(0L, zzcgn.zzbqs.get().longValue());
    }

    public static long zzxR() {
        return Math.max(0L, zzcgn.zzbqt.get().longValue());
    }

    public static long zzxS() {
        return Math.max(0L, zzcgn.zzbqu.get().longValue());
    }

    public static long zzxT() {
        return Math.max(0L, zzcgn.zzbqv.get().longValue());
    }

    public static long zzxU() {
        return Math.max(0L, zzcgn.zzbqw.get().longValue());
    }

    public static long zzxV() {
        return zzcgn.zzbqr.get().longValue();
    }

    public static long zzxW() {
        return Math.max(0L, zzcgn.zzbqz.get().longValue());
    }

    public static long zzxX() {
        return Math.max(0L, zzcgn.zzbqA.get().longValue());
    }

    public static int zzxY() {
        return Math.min(20, Math.max(0, zzcgn.zzbqB.get().intValue()));
    }

    static String zzxe() {
        return zzcgn.zzbpZ.get();
    }

    public static int zzxf() {
        return 25;
    }

    public static int zzxg() {
        return 40;
    }

    public static int zzxh() {
        return 24;
    }

    static int zzxi() {
        return 40;
    }

    static int zzxj() {
        return 100;
    }

    static int zzxk() {
        return 256;
    }

    static int zzxl() {
        return 1000;
    }

    public static int zzxm() {
        return 36;
    }

    public static int zzxn() {
        return 2048;
    }

    static int zzxo() {
        return 500;
    }

    public static long zzxp() {
        return zzcgn.zzbqj.get().intValue();
    }

    public static long zzxq() {
        return zzcgn.zzbql.get().intValue();
    }

    static int zzxr() {
        return 25;
    }

    static int zzxs() {
        return 1000;
    }

    static int zzxt() {
        return 25;
    }

    static int zzxu() {
        return 1000;
    }

    static long zzxv() {
        return 15552000000L;
    }

    static long zzxw() {
        return 15552000000L;
    }

    static long zzxx() {
        return 3600000L;
    }

    static long zzxy() {
        return 60000L;
    }

    static long zzxz() {
        return 61000L;
    }

    public static boolean zzya() {
        return zzcgn.zzbpY.get().booleanValue();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public final long zza(String str, zzcgo<Long> zzcgoVar) {
        if (str == null) {
            return zzcgoVar.get().longValue();
        }
        String strZzM = super.zzwB().zzM(str, zzcgoVar.getKey());
        if (TextUtils.isEmpty(strZzM)) {
            return zzcgoVar.get().longValue();
        }
        try {
            return zzcgoVar.get(Long.valueOf(Long.valueOf(strZzM).longValue())).longValue();
        } catch (NumberFormatException e) {
            return zzcgoVar.get().longValue();
        }
    }

    public final int zzb(String str, zzcgo<Integer> zzcgoVar) {
        if (str == null) {
            return zzcgoVar.get().intValue();
        }
        String strZzM = super.zzwB().zzM(str, zzcgoVar.getKey());
        if (TextUtils.isEmpty(strZzM)) {
            return zzcgoVar.get().intValue();
        }
        try {
            return zzcgoVar.get(Integer.valueOf(Integer.valueOf(strZzM).intValue())).intValue();
        } catch (NumberFormatException e) {
            return zzcgoVar.get().intValue();
        }
    }

    public final int zzdN(@Size(min = 1) String str) {
        return zzb(str, zzcgn.zzbqn);
    }

    @Nullable
    final Boolean zzdO(@Size(min = 1) String str) {
        Boolean boolValueOf = null;
        zzbr.zzcF(str);
        try {
            if (super.getContext().getPackageManager() == null) {
                super.zzwE().zzyv().log("Failed to load metadata: PackageManager is null");
            } else {
                ApplicationInfo applicationInfo = zzbim.zzaP(super.getContext()).getApplicationInfo(super.getContext().getPackageName(), 128);
                if (applicationInfo == null) {
                    super.zzwE().zzyv().log("Failed to load metadata: ApplicationInfo is null");
                } else if (applicationInfo.metaData == null) {
                    super.zzwE().zzyv().log("Failed to load metadata: Metadata bundle is null");
                } else if (applicationInfo.metaData.containsKey(str)) {
                    boolValueOf = Boolean.valueOf(applicationInfo.metaData.getBoolean(str));
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            super.zzwE().zzyv().zzj("Failed to load metadata: Package name not found", e);
        }
        return boolValueOf;
    }

    public final boolean zzdP(String str) {
        return AppEventsConstants.EVENT_PARAM_VALUE_YES.equals(super.zzwB().zzM(str, "gaia_collection_enabled"));
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ void zzjB() {
        super.zzjB();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ com.google.android.gms.common.util.zzf zzkp() {
        return super.zzkp();
    }

    public final boolean zzlm() {
        if (this.zzagW == null) {
            synchronized (this) {
                if (this.zzagW == null) {
                    ApplicationInfo applicationInfo = super.getContext().getApplicationInfo();
                    String strZzse = com.google.android.gms.common.util.zzt.zzse();
                    if (applicationInfo != null) {
                        String str = applicationInfo.processName;
                        this.zzagW = Boolean.valueOf(str != null && str.equals(strZzse));
                    }
                    if (this.zzagW == null) {
                        this.zzagW = Boolean.TRUE;
                        super.zzwE().zzyv().log("My process not in the list of running processes");
                    }
                }
            }
        }
        return this.zzagW.booleanValue();
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

    public final boolean zzxE() {
        Boolean boolZzdO = zzdO("firebase_analytics_collection_deactivated");
        return boolZzdO != null && boolZzdO.booleanValue();
    }

    public final String zzxZ() {
        try {
            return (String) Class.forName("android.os.SystemProperties").getMethod("get", String.class, String.class).invoke(null, "debug.firebase.analytics.app", "");
        } catch (ClassNotFoundException e) {
            super.zzwE().zzyv().zzj("Could not find SystemProperties class", e);
            return "";
        } catch (IllegalAccessException e2) {
            super.zzwE().zzyv().zzj("Could not access SystemProperties.get()", e2);
            return "";
        } catch (NoSuchMethodException e3) {
            super.zzwE().zzyv().zzj("Could not find SystemProperties.get() method", e3);
            return "";
        } catch (InvocationTargetException e4) {
            super.zzwE().zzyv().zzj("SystemProperties.get() threw an exception", e4);
            return "";
        }
    }
}
