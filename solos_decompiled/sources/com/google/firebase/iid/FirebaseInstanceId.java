package com.google.firebase.iid;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.support.v4.util.ArrayMap;
import android.util.Base64;
import android.util.Log;
import com.google.firebase.FirebaseApp;
import java.io.IOException;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/* JADX INFO: loaded from: classes35.dex */
public class FirebaseInstanceId {
    private static Map<String, FirebaseInstanceId> zzbgU = new ArrayMap();
    private static zzk zzcnp;
    private final FirebaseApp zzcnq;
    private final zzj zzcnr;
    private final String zzcns;

    private FirebaseInstanceId(FirebaseApp firebaseApp, zzj zzjVar) {
        this.zzcnq = firebaseApp;
        this.zzcnr = zzjVar;
        String gcmSenderId = this.zzcnq.getOptions().getGcmSenderId();
        if (gcmSenderId == null) {
            gcmSenderId = this.zzcnq.getOptions().getApplicationId();
            if (gcmSenderId.startsWith("1:")) {
                String[] strArrSplit = gcmSenderId.split(":");
                if (strArrSplit.length < 2) {
                    gcmSenderId = null;
                } else {
                    gcmSenderId = strArrSplit[1];
                    if (gcmSenderId.isEmpty()) {
                        gcmSenderId = null;
                    }
                }
            }
        }
        this.zzcns = gcmSenderId;
        if (this.zzcns == null) {
            throw new IllegalStateException("IID failing to initialize, FirebaseApp is missing project ID");
        }
        FirebaseInstanceIdService.zza(this.zzcnq.getApplicationContext(), this);
    }

    public static FirebaseInstanceId getInstance() {
        return getInstance(FirebaseApp.getInstance());
    }

    @Keep
    public static synchronized FirebaseInstanceId getInstance(@NonNull FirebaseApp firebaseApp) {
        FirebaseInstanceId firebaseInstanceId;
        firebaseInstanceId = zzbgU.get(firebaseApp.getOptions().getApplicationId());
        if (firebaseInstanceId == null) {
            zzj zzjVarZzb = zzj.zzb(firebaseApp.getApplicationContext(), null);
            if (zzcnp == null) {
                zzcnp = new zzk(zzj.zzKi());
            }
            firebaseInstanceId = new FirebaseInstanceId(firebaseApp, zzjVarZzb);
            zzbgU.put(firebaseApp.getOptions().getApplicationId(), firebaseInstanceId);
        }
        return firebaseInstanceId;
    }

    private final void zzF(Bundle bundle) {
        bundle.putString("gmp_app_id", this.zzcnq.getOptions().getApplicationId());
    }

    static zzk zzKh() {
        return zzcnp;
    }

    static int zzP(Context context, String str) {
        try {
            return context.getPackageManager().getPackageInfo(str, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            String strValueOf = String.valueOf(e);
            Log.w("FirebaseInstanceId", new StringBuilder(String.valueOf(strValueOf).length() + 23).append("Failed to find package ").append(strValueOf).toString());
            return 0;
        }
    }

    static String zza(KeyPair keyPair) {
        try {
            byte[] bArrDigest = MessageDigest.getInstance("SHA1").digest(keyPair.getPublic().getEncoded());
            bArrDigest[0] = (byte) ((bArrDigest[0] & 15) + 112);
            return Base64.encodeToString(bArrDigest, 0, 8, 11);
        } catch (NoSuchAlgorithmException e) {
            Log.w("FirebaseInstanceId", "Unexpected error, device missing required alghorithms");
            return null;
        }
    }

    static void zza(Context context, zzr zzrVar) {
        zzrVar.zzvO();
        Intent intent = new Intent();
        intent.putExtra("CMD", "RST");
        zzq.zzKm().zze(context, intent);
    }

    static int zzbF(Context context) {
        return zzP(context, context.getPackageName());
    }

    static void zzbG(Context context) {
        Intent intent = new Intent();
        intent.putExtra("CMD", "SYNC");
        zzq.zzKm().zze(context, intent);
    }

    static String zzbb(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            String strValueOf = String.valueOf(e);
            Log.w("FirebaseInstanceId", new StringBuilder(String.valueOf(strValueOf).length() + 38).append("Never happens: can't find own package ").append(strValueOf).toString());
            return null;
        }
    }

    static String zzk(byte[] bArr) {
        return Base64.encodeToString(bArr, 11);
    }

    public void deleteInstanceId() throws IOException {
        this.zzcnr.zzb("*", "*", null);
        this.zzcnr.zzvK();
    }

    @WorkerThread
    public void deleteToken(String str, String str2) throws IOException {
        Bundle bundle = new Bundle();
        zzF(bundle);
        this.zzcnr.zzb(str, str2, bundle);
    }

    public long getCreationTime() {
        return this.zzcnr.getCreationTime();
    }

    public String getId() {
        return zza(this.zzcnr.zzvJ());
    }

    @Nullable
    public String getToken() {
        zzs zzsVarZzKf = zzKf();
        if (zzsVarZzKf == null || zzsVarZzKf.zzhO(zzj.zzbha)) {
            FirebaseInstanceIdService.zzbI(this.zzcnq.getApplicationContext());
        }
        if (zzsVarZzKf != null) {
            return zzsVarZzKf.zzbPL;
        }
        return null;
    }

    @WorkerThread
    public String getToken(String str, String str2) throws IOException {
        Bundle bundle = new Bundle();
        zzF(bundle);
        return this.zzcnr.getToken(str, str2, bundle);
    }

    @Nullable
    final zzs zzKf() {
        return zzj.zzKi().zzp("", this.zzcns, "*");
    }

    final String zzKg() throws IOException {
        return getToken(this.zzcns, "*");
    }

    public final void zzhE(String str) {
        zzcnp.zzhE(str);
        FirebaseInstanceIdService.zzbI(this.zzcnq.getApplicationContext());
    }

    final void zzhF(String str) throws IOException {
        zzs zzsVarZzKf = zzKf();
        if (zzsVarZzKf == null || zzsVarZzKf.zzhO(zzj.zzbha)) {
            throw new IOException("token not available");
        }
        Bundle bundle = new Bundle();
        String strValueOf = String.valueOf("/topics/");
        String strValueOf2 = String.valueOf(str);
        bundle.putString("gcm.topic", strValueOf2.length() != 0 ? strValueOf.concat(strValueOf2) : new String(strValueOf));
        String str2 = zzsVarZzKf.zzbPL;
        String strValueOf3 = String.valueOf("/topics/");
        String strValueOf4 = String.valueOf(str);
        String strConcat = strValueOf4.length() != 0 ? strValueOf3.concat(strValueOf4) : new String(strValueOf3);
        zzF(bundle);
        this.zzcnr.zzc(str2, strConcat, bundle);
    }

    final void zzhG(String str) throws IOException {
        zzs zzsVarZzKf = zzKf();
        if (zzsVarZzKf == null || zzsVarZzKf.zzhO(zzj.zzbha)) {
            throw new IOException("token not available");
        }
        Bundle bundle = new Bundle();
        String strValueOf = String.valueOf("/topics/");
        String strValueOf2 = String.valueOf(str);
        bundle.putString("gcm.topic", strValueOf2.length() != 0 ? strValueOf.concat(strValueOf2) : new String(strValueOf));
        zzj zzjVar = this.zzcnr;
        String str2 = zzsVarZzKf.zzbPL;
        String strValueOf3 = String.valueOf("/topics/");
        String strValueOf4 = String.valueOf(str);
        zzjVar.zzb(str2, strValueOf4.length() != 0 ? strValueOf3.concat(strValueOf4) : new String(strValueOf3), bundle);
    }
}
