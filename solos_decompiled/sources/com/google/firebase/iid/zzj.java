package com.google.firebase.iid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.share.internal.ShareConstants;
import java.io.IOException;
import java.security.KeyPair;
import java.util.Map;

/* JADX INFO: loaded from: classes35.dex */
public final class zzj {
    private static Map<String, zzj> zzbgU = new ArrayMap();
    static String zzbha;
    private static zzl zzcnA;
    private static zzr zzcnz;
    private Context mContext;
    private KeyPair zzbgX;
    private String zzbgY;

    private zzj(Context context, String str, Bundle bundle) {
        this.zzbgY = "";
        this.mContext = context.getApplicationContext();
        this.zzbgY = str;
    }

    public static zzr zzKi() {
        return zzcnz;
    }

    public static zzl zzKj() {
        return zzcnA;
    }

    public static synchronized zzj zzb(Context context, Bundle bundle) {
        zzj zzjVar;
        String string = bundle == null ? "" : bundle.getString("subtype");
        String str = string == null ? "" : string;
        Context applicationContext = context.getApplicationContext();
        if (zzcnz == null) {
            zzcnz = new zzr(applicationContext);
            zzcnA = new zzl(applicationContext);
        }
        zzbha = Integer.toString(FirebaseInstanceId.zzbF(applicationContext));
        zzjVar = zzbgU.get(str);
        if (zzjVar == null) {
            zzjVar = new zzj(applicationContext, str, bundle);
            zzbgU.put(str, zzjVar);
        }
        return zzjVar;
    }

    public final long getCreationTime() {
        return zzcnz.zzhJ(this.zzbgY);
    }

    public final String getToken(String str, String str2, Bundle bundle) throws IOException {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            throw new IOException("MAIN_THREAD");
        }
        boolean z = true;
        if (bundle.getString("ttl") != null || "jwt".equals(bundle.getString(ShareConstants.MEDIA_TYPE))) {
            z = false;
        } else {
            zzs zzsVarZzp = zzcnz.zzp(this.zzbgY, str, str2);
            if (zzsVarZzp != null && !zzsVarZzp.zzhO(zzbha)) {
                return zzsVarZzp.zzbPL;
            }
        }
        String strZzc = zzc(str, str2, bundle);
        if (strZzc == null || !z) {
            return strZzc;
        }
        zzcnz.zza(this.zzbgY, str, str2, strZzc, zzbha);
        return strZzc;
    }

    public final void zzb(String str, String str2, Bundle bundle) throws IOException {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            throw new IOException("MAIN_THREAD");
        }
        zzcnz.zzg(this.zzbgY, str, str2);
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putString("delete", AppEventsConstants.EVENT_PARAM_VALUE_YES);
        zzc(str, str2, bundle);
    }

    public final String zzc(String str, String str2, Bundle bundle) throws IOException {
        if (str2 != null) {
            bundle.putString("scope", str2);
        }
        bundle.putString("sender", str);
        if (!"".equals(this.zzbgY)) {
            str = this.zzbgY;
        }
        bundle.putString("subtype", str);
        bundle.putString("X-subtype", str);
        Intent intentZza = zzcnA.zza(bundle, zzvJ());
        if (intentZza == null) {
            throw new IOException("SERVICE_NOT_AVAILABLE");
        }
        String stringExtra = intentZza.getStringExtra("registration_id");
        if (stringExtra == null) {
            stringExtra = intentZza.getStringExtra("unregistered");
        }
        if (stringExtra != null) {
            return stringExtra;
        }
        String stringExtra2 = intentZza.getStringExtra("error");
        if (stringExtra2 != null) {
            throw new IOException(stringExtra2);
        }
        String strValueOf = String.valueOf(intentZza.getExtras());
        Log.w("InstanceID/Rpc", new StringBuilder(String.valueOf(strValueOf).length() + 29).append("Unexpected response from GCM ").append(strValueOf).toString(), new Throwable());
        throw new IOException("SERVICE_NOT_AVAILABLE");
    }

    final KeyPair zzvJ() {
        if (this.zzbgX == null) {
            this.zzbgX = zzcnz.zzhM(this.zzbgY);
        }
        if (this.zzbgX == null) {
            this.zzbgX = zzcnz.zzhK(this.zzbgY);
        }
        return this.zzbgX;
    }

    public final void zzvK() {
        zzcnz.zzhL(this.zzbgY);
        this.zzbgX = null;
    }
}
