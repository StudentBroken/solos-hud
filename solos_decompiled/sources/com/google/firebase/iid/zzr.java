package com.google.firebase.iid;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;
import com.google.android.gms.common.util.zzw;
import java.io.File;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/* JADX INFO: loaded from: classes35.dex */
final class zzr {
    SharedPreferences zzbhs;
    private Context zzqG;

    public zzr(Context context) {
        this(context, "com.google.android.gms.appid");
    }

    private zzr(Context context, String str) {
        this.zzqG = context;
        this.zzbhs = context.getSharedPreferences(str, 0);
        String strValueOf = String.valueOf(str);
        String strValueOf2 = String.valueOf("-no-backup");
        File file = new File(zzw.getNoBackupFilesDir(this.zzqG), strValueOf2.length() != 0 ? strValueOf.concat(strValueOf2) : new String(strValueOf));
        if (file.exists()) {
            return;
        }
        try {
            if (!file.createNewFile() || isEmpty()) {
                return;
            }
            Log.i("InstanceID/Store", "App restored, clearing state");
            FirebaseInstanceId.zza(this.zzqG, this);
        } catch (IOException e) {
            if (Log.isLoggable("InstanceID/Store", 3)) {
                String strValueOf3 = String.valueOf(e.getMessage());
                Log.d("InstanceID/Store", strValueOf3.length() != 0 ? "Error creating file in no backup dir: ".concat(strValueOf3) : new String("Error creating file in no backup dir: "));
            }
        }
    }

    private static String zzaj(String str, String str2) {
        String strValueOf = String.valueOf("|S|");
        return new StringBuilder(String.valueOf(str).length() + String.valueOf(strValueOf).length() + String.valueOf(str2).length()).append(str).append(strValueOf).append(str2).toString();
    }

    private final void zzdr(String str) {
        SharedPreferences.Editor editorEdit = this.zzbhs.edit();
        for (String str2 : this.zzbhs.getAll().keySet()) {
            if (str2.startsWith(str)) {
                editorEdit.remove(str2);
            }
        }
        editorEdit.commit();
    }

    private static String zzo(String str, String str2, String str3) {
        String strValueOf = String.valueOf("|T|");
        return new StringBuilder(String.valueOf(str).length() + 1 + String.valueOf(strValueOf).length() + String.valueOf(str2).length() + String.valueOf(str3).length()).append(str).append(strValueOf).append(str2).append("|").append(str3).toString();
    }

    public final synchronized boolean isEmpty() {
        return this.zzbhs.getAll().isEmpty();
    }

    public final synchronized void zza(String str, String str2, String str3, String str4, String str5) {
        String strZzc = zzs.zzc(str4, str5, System.currentTimeMillis());
        if (strZzc != null) {
            SharedPreferences.Editor editorEdit = this.zzbhs.edit();
            editorEdit.putString(zzo(str, str2, str3), strZzc);
            editorEdit.commit();
        }
    }

    public final synchronized void zzds(String str) {
        zzdr(String.valueOf(str).concat("|T|"));
    }

    public final synchronized void zzg(String str, String str2, String str3) {
        String strZzo = zzo(str, str2, str3);
        SharedPreferences.Editor editorEdit = this.zzbhs.edit();
        editorEdit.remove(strZzo);
        editorEdit.commit();
    }

    public final synchronized long zzhJ(String str) {
        long j;
        String string = this.zzbhs.getString(zzaj(str, "cre"), null);
        if (string != null) {
            try {
                j = Long.parseLong(string);
            } catch (NumberFormatException e) {
                j = 0;
            }
        } else {
            j = 0;
        }
        return j;
    }

    final synchronized KeyPair zzhK(String str) {
        KeyPair keyPairZzvI;
        keyPairZzvI = zza.zzvI();
        long jCurrentTimeMillis = System.currentTimeMillis();
        SharedPreferences.Editor editorEdit = this.zzbhs.edit();
        editorEdit.putString(zzaj(str, "|P|"), FirebaseInstanceId.zzk(keyPairZzvI.getPublic().getEncoded()));
        editorEdit.putString(zzaj(str, "|K|"), FirebaseInstanceId.zzk(keyPairZzvI.getPrivate().getEncoded()));
        editorEdit.putString(zzaj(str, "cre"), Long.toString(jCurrentTimeMillis));
        editorEdit.commit();
        return keyPairZzvI;
    }

    final synchronized void zzhL(String str) {
        zzdr(String.valueOf(str).concat("|"));
    }

    public final synchronized KeyPair zzhM(String str) {
        KeyPair keyPair;
        String string = this.zzbhs.getString(zzaj(str, "|P|"), null);
        String string2 = this.zzbhs.getString(zzaj(str, "|K|"), null);
        if (string == null || string2 == null) {
            keyPair = null;
        } else {
            try {
                byte[] bArrDecode = Base64.decode(string, 8);
                byte[] bArrDecode2 = Base64.decode(string2, 8);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                keyPair = new KeyPair(keyFactory.generatePublic(new X509EncodedKeySpec(bArrDecode)), keyFactory.generatePrivate(new PKCS8EncodedKeySpec(bArrDecode2)));
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                String strValueOf = String.valueOf(e);
                Log.w("InstanceID/Store", new StringBuilder(String.valueOf(strValueOf).length() + 19).append("Invalid key stored ").append(strValueOf).toString());
                FirebaseInstanceId.zza(this.zzqG, this);
                keyPair = null;
            }
        }
        return keyPair;
    }

    public final synchronized zzs zzp(String str, String str2, String str3) {
        return zzs.zzhN(this.zzbhs.getString(zzo(str, str2, str3), null));
    }

    public final synchronized void zzvO() {
        this.zzbhs.edit().clear().commit();
    }
}
