package com.google.android.gms.common.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.google.android.gms.internal.zzbim;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* JADX INFO: loaded from: classes67.dex */
public final class zza {
    public static byte[] zzA(Context context, String str) throws PackageManager.NameNotFoundException {
        MessageDigest messageDigestZzbE;
        PackageInfo packageInfo = zzbim.zzaP(context).getPackageInfo(str, 64);
        if (packageInfo.signatures == null || packageInfo.signatures.length <= 0 || (messageDigestZzbE = zzbE("SHA1")) == null) {
            return null;
        }
        return messageDigestZzbE.digest(packageInfo.signatures[0].toByteArray());
    }

    private static MessageDigest zzbE(String str) {
        MessageDigest messageDigest;
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= 2) {
                return null;
            }
            try {
                messageDigest = MessageDigest.getInstance(str);
            } catch (NoSuchAlgorithmException e) {
            }
            if (messageDigest != null) {
                return messageDigest;
            }
            i = i2 + 1;
        }
    }
}
