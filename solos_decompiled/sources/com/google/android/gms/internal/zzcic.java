package com.google.android.gms.internal;

import android.os.Binder;
import android.support.annotation.BinderThread;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.google.android.gms.common.internal.zzbr;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

/* JADX INFO: loaded from: classes36.dex */
public final class zzcic extends zzcgq {
    private final zzchx zzboi;
    private Boolean zzbtg;

    @Nullable
    private String zzbth;

    public zzcic(zzchx zzchxVar) {
        this(zzchxVar, null);
    }

    private zzcic(zzchx zzchxVar, @Nullable String str) {
        zzbr.zzu(zzchxVar);
        this.zzboi = zzchxVar;
        this.zzbth = null;
    }

    @BinderThread
    private final void zzb(zzcft zzcftVar, boolean z) {
        zzbr.zzu(zzcftVar);
        zzh(zzcftVar.packageName, false);
        this.zzboi.zzwA().zzew(zzcftVar.zzboU);
    }

    @BinderThread
    private final void zzh(String str, boolean z) {
        if (TextUtils.isEmpty(str)) {
            this.zzboi.zzwE().zzyv().log("Measurement Service called without app package");
            throw new SecurityException("Measurement Service called without app package");
        }
        if (z) {
            try {
                if (this.zzbtg == null) {
                    this.zzbtg = Boolean.valueOf("com.google.android.gms".equals(this.zzbth) || com.google.android.gms.common.util.zzy.zzf(this.zzboi.getContext(), Binder.getCallingUid()) || com.google.android.gms.common.zzp.zzax(this.zzboi.getContext()).zza(this.zzboi.getContext().getPackageManager(), Binder.getCallingUid()));
                }
                if (this.zzbtg.booleanValue()) {
                    return;
                }
            } catch (SecurityException e) {
                this.zzboi.zzwE().zzyv().zzj("Measurement Service called with invalid calling package. appId", zzcgx.zzea(str));
                throw e;
            }
        }
        if (this.zzbth == null && com.google.android.gms.common.zzo.zzb(this.zzboi.getContext(), Binder.getCallingUid(), str)) {
            this.zzbth = str;
        }
        if (str.equals(this.zzbth)) {
        } else {
            throw new SecurityException(String.format("Unknown calling package name '%s'.", str));
        }
    }

    @Override // com.google.android.gms.internal.zzcgp
    @BinderThread
    public final List<zzcku> zza(zzcft zzcftVar, boolean z) {
        zzb(zzcftVar, false);
        try {
            List<zzckw> list = (List) this.zzboi.zzwD().zze(new zzcir(this, zzcftVar)).get();
            ArrayList arrayList = new ArrayList(list.size());
            for (zzckw zzckwVar : list) {
                if (z || !zzckx.zzey(zzckwVar.mName)) {
                    arrayList.add(new zzcku(zzckwVar));
                }
            }
            return arrayList;
        } catch (InterruptedException | ExecutionException e) {
            this.zzboi.zzwE().zzyv().zze("Failed to get user attributes. appId", zzcgx.zzea(zzcftVar.packageName), e);
            return null;
        }
    }

    @Override // com.google.android.gms.internal.zzcgp
    @BinderThread
    public final List<zzcfw> zza(String str, String str2, zzcft zzcftVar) {
        zzb(zzcftVar, false);
        try {
            return (List) this.zzboi.zzwD().zze(new zzcik(this, zzcftVar, str, str2)).get();
        } catch (InterruptedException | ExecutionException e) {
            this.zzboi.zzwE().zzyv().zzj("Failed to get conditional user properties", e);
            return Collections.emptyList();
        }
    }

    @Override // com.google.android.gms.internal.zzcgp
    @BinderThread
    public final List<zzcku> zza(String str, String str2, String str3, boolean z) {
        zzh(str, true);
        try {
            List<zzckw> list = (List) this.zzboi.zzwD().zze(new zzcij(this, str, str2, str3)).get();
            ArrayList arrayList = new ArrayList(list.size());
            for (zzckw zzckwVar : list) {
                if (z || !zzckx.zzey(zzckwVar.mName)) {
                    arrayList.add(new zzcku(zzckwVar));
                }
            }
            return arrayList;
        } catch (InterruptedException | ExecutionException e) {
            this.zzboi.zzwE().zzyv().zze("Failed to get user attributes. appId", zzcgx.zzea(str), e);
            return Collections.emptyList();
        }
    }

    @Override // com.google.android.gms.internal.zzcgp
    @BinderThread
    public final List<zzcku> zza(String str, String str2, boolean z, zzcft zzcftVar) {
        zzb(zzcftVar, false);
        try {
            List<zzckw> list = (List) this.zzboi.zzwD().zze(new zzcii(this, zzcftVar, str, str2)).get();
            ArrayList arrayList = new ArrayList(list.size());
            for (zzckw zzckwVar : list) {
                if (z || !zzckx.zzey(zzckwVar.mName)) {
                    arrayList.add(new zzcku(zzckwVar));
                }
            }
            return arrayList;
        } catch (InterruptedException | ExecutionException e) {
            this.zzboi.zzwE().zzyv().zze("Failed to get user attributes. appId", zzcgx.zzea(zzcftVar.packageName), e);
            return Collections.emptyList();
        }
    }

    @Override // com.google.android.gms.internal.zzcgp
    @BinderThread
    public final void zza(long j, String str, String str2, String str3) {
        this.zzboi.zzwD().zzj(new zzcit(this, str2, str3, str, j));
    }

    @Override // com.google.android.gms.internal.zzcgp
    @BinderThread
    public final void zza(zzcft zzcftVar) {
        zzb(zzcftVar, false);
        zzcis zzcisVar = new zzcis(this, zzcftVar);
        if (this.zzboi.zzwD().zzyK()) {
            zzcisVar.run();
        } else {
            this.zzboi.zzwD().zzj(zzcisVar);
        }
    }

    @Override // com.google.android.gms.internal.zzcgp
    @BinderThread
    public final void zza(zzcfw zzcfwVar, zzcft zzcftVar) {
        zzbr.zzu(zzcfwVar);
        zzbr.zzu(zzcfwVar.zzbph);
        zzb(zzcftVar, false);
        zzcfw zzcfwVar2 = new zzcfw(zzcfwVar);
        zzcfwVar2.packageName = zzcftVar.packageName;
        if (zzcfwVar.zzbph.getValue() == null) {
            this.zzboi.zzwD().zzj(new zzcie(this, zzcfwVar2, zzcftVar));
        } else {
            this.zzboi.zzwD().zzj(new zzcif(this, zzcfwVar2, zzcftVar));
        }
    }

    @Override // com.google.android.gms.internal.zzcgp
    @BinderThread
    public final void zza(zzcgl zzcglVar, zzcft zzcftVar) {
        zzbr.zzu(zzcglVar);
        zzb(zzcftVar, false);
        this.zzboi.zzwD().zzj(new zzcim(this, zzcglVar, zzcftVar));
    }

    @Override // com.google.android.gms.internal.zzcgp
    @BinderThread
    public final void zza(zzcgl zzcglVar, String str, String str2) {
        zzbr.zzu(zzcglVar);
        zzbr.zzcF(str);
        zzh(str, true);
        this.zzboi.zzwD().zzj(new zzcin(this, zzcglVar, str));
    }

    @Override // com.google.android.gms.internal.zzcgp
    @BinderThread
    public final void zza(zzcku zzckuVar, zzcft zzcftVar) {
        zzbr.zzu(zzckuVar);
        zzb(zzcftVar, false);
        if (zzckuVar.getValue() == null) {
            this.zzboi.zzwD().zzj(new zzcip(this, zzckuVar, zzcftVar));
        } else {
            this.zzboi.zzwD().zzj(new zzciq(this, zzckuVar, zzcftVar));
        }
    }

    @Override // com.google.android.gms.internal.zzcgp
    @BinderThread
    public final byte[] zza(zzcgl zzcglVar, String str) {
        zzbr.zzcF(str);
        zzbr.zzu(zzcglVar);
        zzh(str, true);
        this.zzboi.zzwE().zzyA().zzj("Log and bundle. event", this.zzboi.zzwz().zzdX(zzcglVar.name));
        long jNanoTime = this.zzboi.zzkp().nanoTime() / 1000000;
        try {
            byte[] bArr = (byte[]) this.zzboi.zzwD().zzf(new zzcio(this, zzcglVar, str)).get();
            if (bArr == null) {
                this.zzboi.zzwE().zzyv().zzj("Log and bundle returned null. appId", zzcgx.zzea(str));
                bArr = new byte[0];
            }
            this.zzboi.zzwE().zzyA().zzd("Log and bundle processed. event, size, time_ms", this.zzboi.zzwz().zzdX(zzcglVar.name), Integer.valueOf(bArr.length), Long.valueOf((this.zzboi.zzkp().nanoTime() / 1000000) - jNanoTime));
            return bArr;
        } catch (InterruptedException | ExecutionException e) {
            this.zzboi.zzwE().zzyv().zzd("Failed to log and bundle. appId, event, error", zzcgx.zzea(str), this.zzboi.zzwz().zzdX(zzcglVar.name), e);
            return null;
        }
    }

    @Override // com.google.android.gms.internal.zzcgp
    @BinderThread
    public final void zzb(zzcft zzcftVar) {
        zzb(zzcftVar, false);
        this.zzboi.zzwD().zzj(new zzcid(this, zzcftVar));
    }

    @Override // com.google.android.gms.internal.zzcgp
    @BinderThread
    public final void zzb(zzcfw zzcfwVar) {
        zzbr.zzu(zzcfwVar);
        zzbr.zzu(zzcfwVar.zzbph);
        zzh(zzcfwVar.packageName, true);
        zzcfw zzcfwVar2 = new zzcfw(zzcfwVar);
        if (zzcfwVar.zzbph.getValue() == null) {
            this.zzboi.zzwD().zzj(new zzcig(this, zzcfwVar2));
        } else {
            this.zzboi.zzwD().zzj(new zzcih(this, zzcfwVar2));
        }
    }

    @Override // com.google.android.gms.internal.zzcgp
    @BinderThread
    public final String zzc(zzcft zzcftVar) {
        zzb(zzcftVar, false);
        return this.zzboi.zzen(zzcftVar.packageName);
    }

    @Override // com.google.android.gms.internal.zzcgp
    @BinderThread
    public final List<zzcfw> zzk(String str, String str2, String str3) {
        zzh(str, true);
        try {
            return (List) this.zzboi.zzwD().zze(new zzcil(this, str, str2, str3)).get();
        } catch (InterruptedException | ExecutionException e) {
            this.zzboi.zzwE().zzyv().zzj("Failed to get conditional user properties", e);
            return Collections.emptyList();
        }
    }
}
