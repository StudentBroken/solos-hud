package com.google.android.gms.internal;

import android.os.Bundle;
import android.text.TextUtils;
import com.google.android.gms.common.internal.zzbr;
import java.util.Iterator;

/* JADX INFO: loaded from: classes36.dex */
public final class zzcgg {
    final String mAppId;
    final String mName;
    private String mOrigin;
    final long zzayU;
    final long zzbpI;
    final zzcgi zzbpJ;

    zzcgg(zzchx zzchxVar, String str, String str2, String str3, long j, long j2, Bundle bundle) {
        zzbr.zzcF(str2);
        zzbr.zzcF(str3);
        this.mAppId = str2;
        this.mName = str3;
        this.mOrigin = TextUtils.isEmpty(str) ? null : str;
        this.zzayU = j;
        this.zzbpI = j2;
        if (this.zzbpI != 0 && this.zzbpI > this.zzayU) {
            zzchxVar.zzwE().zzyx().zzj("Event created with reverse previous/current timestamps. appId", zzcgx.zzea(str2));
        }
        this.zzbpJ = zza(zzchxVar, bundle);
    }

    private zzcgg(zzchx zzchxVar, String str, String str2, String str3, long j, long j2, zzcgi zzcgiVar) {
        zzbr.zzcF(str2);
        zzbr.zzcF(str3);
        zzbr.zzu(zzcgiVar);
        this.mAppId = str2;
        this.mName = str3;
        this.mOrigin = TextUtils.isEmpty(str) ? null : str;
        this.zzayU = j;
        this.zzbpI = j2;
        if (this.zzbpI != 0 && this.zzbpI > this.zzayU) {
            zzchxVar.zzwE().zzyx().zzj("Event created with reverse previous/current timestamps. appId", zzcgx.zzea(str2));
        }
        this.zzbpJ = zzcgiVar;
    }

    private static zzcgi zza(zzchx zzchxVar, Bundle bundle) {
        if (bundle == null || bundle.isEmpty()) {
            return new zzcgi(new Bundle());
        }
        Bundle bundle2 = new Bundle(bundle);
        Iterator<String> it = bundle2.keySet().iterator();
        while (it.hasNext()) {
            String next = it.next();
            if (next == null) {
                zzchxVar.zzwE().zzyv().log("Param name can't be null");
                it.remove();
            } else {
                Object objZzk = zzchxVar.zzwA().zzk(next, bundle2.get(next));
                if (objZzk == null) {
                    zzchxVar.zzwE().zzyx().zzj("Param value can't be null", zzchxVar.zzwz().zzdY(next));
                    it.remove();
                } else {
                    zzchxVar.zzwA().zza(bundle2, next, objZzk);
                }
            }
        }
        return new zzcgi(bundle2);
    }

    public final String toString() {
        String str = this.mAppId;
        String str2 = this.mName;
        String strValueOf = String.valueOf(this.zzbpJ);
        return new StringBuilder(String.valueOf(str).length() + 33 + String.valueOf(str2).length() + String.valueOf(strValueOf).length()).append("Event{appId='").append(str).append("', name='").append(str2).append("', params=").append(strValueOf).append("}").toString();
    }

    final zzcgg zza(zzchx zzchxVar, long j) {
        return new zzcgg(zzchxVar, this.mOrigin, this.mAppId, this.mName, this.zzayU, j, this.zzbpJ);
    }
}
