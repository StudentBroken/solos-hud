package com.google.android.gms.internal;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import com.facebook.internal.AnalyticsEvents;
import com.google.android.gms.common.api.Status;
import com.google.firebase.iid.FirebaseInstanceId;
import java.math.BigInteger;
import java.util.Locale;

/* JADX INFO: loaded from: classes36.dex */
public final class zzcgs extends zzciv {
    private String mAppId;
    private String zzXD;
    private String zzaeJ;
    private String zzaeK;
    private String zzboF;
    private long zzboJ;
    private int zzbqG;
    private long zzbqH;
    private int zzbqI;

    zzcgs(zzchx zzchxVar) {
        super(zzchxVar);
    }

    @WorkerThread
    private final String zzwJ() {
        super.zzjB();
        try {
            return FirebaseInstanceId.getInstance().getId();
        } catch (IllegalStateException e) {
            super.zzwE().zzyx().log("Failed to retrieve Firebase Instance Id");
            return null;
        }
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    final String getGmpAppId() {
        zzkC();
        return this.zzXD;
    }

    @WorkerThread
    final zzcft zzdW(String str) {
        super.zzjB();
        String strZzhk = zzhk();
        String gmpAppId = getGmpAppId();
        zzkC();
        String str2 = this.zzaeK;
        long jZzyt = zzyt();
        zzkC();
        String str3 = this.zzboF;
        long jZzwO = zzcfy.zzwO();
        zzkC();
        super.zzjB();
        if (this.zzbqH == 0) {
            this.zzbqH = this.zzboi.zzwA().zzJ(super.getContext(), super.getContext().getPackageName());
        }
        long j = this.zzbqH;
        boolean zIsEnabled = this.zzboi.isEnabled();
        boolean z = !super.zzwF().zzbrG;
        String strZzwJ = zzwJ();
        zzkC();
        long jZzyW = this.zzboi.zzyW();
        zzkC();
        return new zzcft(strZzhk, gmpAppId, str2, jZzyt, str3, jZzwO, j, str, zIsEnabled, z, strZzwJ, 0L, jZzyW, this.zzbqI);
    }

    final String zzhk() {
        zzkC();
        return this.mAppId;
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ void zzjB() {
        super.zzjB();
    }

    @Override // com.google.android.gms.internal.zzciv
    protected final void zzjC() {
        boolean z;
        String installerPackageName = "unknown";
        String str = AnalyticsEvents.PARAMETER_DIALOG_OUTCOME_VALUE_UNKNOWN;
        int i = Integer.MIN_VALUE;
        String string = AnalyticsEvents.PARAMETER_DIALOG_OUTCOME_VALUE_UNKNOWN;
        String packageName = super.getContext().getPackageName();
        PackageManager packageManager = super.getContext().getPackageManager();
        if (packageManager == null) {
            super.zzwE().zzyv().zzj("PackageManager is null, app identity information might be inaccurate. appId", zzcgx.zzea(packageName));
        } else {
            try {
                installerPackageName = packageManager.getInstallerPackageName(packageName);
            } catch (IllegalArgumentException e) {
                super.zzwE().zzyv().zzj("Error retrieving app installer package name. appId", zzcgx.zzea(packageName));
            }
            if (installerPackageName == null) {
                installerPackageName = "manual_install";
            } else if ("com.android.vending".equals(installerPackageName)) {
                installerPackageName = "";
            }
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(super.getContext().getPackageName(), 0);
                if (packageInfo != null) {
                    CharSequence applicationLabel = packageManager.getApplicationLabel(packageInfo.applicationInfo);
                    if (!TextUtils.isEmpty(applicationLabel)) {
                        string = applicationLabel.toString();
                    }
                    str = packageInfo.versionName;
                    i = packageInfo.versionCode;
                }
            } catch (PackageManager.NameNotFoundException e2) {
                super.zzwE().zzyv().zze("Error retrieving package info. appId, appName", zzcgx.zzea(packageName), string);
            }
        }
        this.mAppId = packageName;
        this.zzboF = installerPackageName;
        this.zzaeK = str;
        this.zzbqG = i;
        this.zzaeJ = string;
        this.zzbqH = 0L;
        zzcfy.zzxD();
        Status statusZzaz = zzbey.zzaz(super.getContext());
        boolean z2 = statusZzaz != null && statusZzaz.isSuccess();
        if (!z2) {
            if (statusZzaz == null) {
                super.zzwE().zzyv().log("GoogleService failed to initialize (no status)");
            } else {
                super.zzwE().zzyv().zze("GoogleService failed to initialize, status", Integer.valueOf(statusZzaz.getStatusCode()), statusZzaz.getStatusMessage());
            }
        }
        if (z2) {
            Boolean boolZzdO = super.zzwG().zzdO("firebase_analytics_collection_enabled");
            if (super.zzwG().zzxE()) {
                super.zzwE().zzyz().log("Collection disabled with firebase_analytics_collection_deactivated=1");
                z = false;
            } else if (boolZzdO != null && !boolZzdO.booleanValue()) {
                super.zzwE().zzyz().log("Collection disabled with firebase_analytics_collection_enabled=0");
                z = false;
            } else if (boolZzdO == null && zzcfy.zzqz()) {
                super.zzwE().zzyz().log("Collection disabled with google_app_measurement_enable=0");
                z = false;
            } else {
                super.zzwE().zzyB().log("Collection enabled");
                z = true;
            }
        } else {
            z = false;
        }
        this.zzXD = "";
        this.zzboJ = 0L;
        zzcfy.zzxD();
        try {
            String strZzqy = zzbey.zzqy();
            if (TextUtils.isEmpty(strZzqy)) {
                strZzqy = "";
            }
            this.zzXD = strZzqy;
            if (z) {
                super.zzwE().zzyB().zze("App package, google app id", this.mAppId, this.zzXD);
            }
        } catch (IllegalStateException e3) {
            super.zzwE().zzyv().zze("getGoogleAppId or isMeasurementEnabled failed with exception. appId", zzcgx.zzea(packageName), e3);
        }
        if (Build.VERSION.SDK_INT >= 16) {
            this.zzbqI = zzbik.zzaN(super.getContext()) ? 1 : 0;
        } else {
            this.zzbqI = 0;
        }
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

    @WorkerThread
    final String zzys() {
        byte[] bArr = new byte[16];
        super.zzwA().zzzr().nextBytes(bArr);
        return String.format(Locale.US, "%032x", new BigInteger(1, bArr));
    }

    final int zzyt() {
        zzkC();
        return this.zzbqG;
    }
}
