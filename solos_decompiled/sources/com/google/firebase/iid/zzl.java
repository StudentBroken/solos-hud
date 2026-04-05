package com.google.firebase.iid;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.Process;
import android.os.SystemClock;
import android.support.v4.util.SimpleArrayMap;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.iid.MessengerCompat;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.util.Iterator;
import java.util.Random;

/* JADX INFO: loaded from: classes35.dex */
public final class zzl {
    private static PendingIntent zzbfT;
    private static String zzbhd = null;
    private static boolean zzbhe = false;
    private static int zzbhf = 0;
    private static int zzbhg = 0;
    private static int zzbhh = 0;
    private static BroadcastReceiver zzbhi = null;
    private Messenger zzbfX;
    private Messenger zzbhk;
    private MessengerCompat zzbhl;
    private long zzbhm;
    private long zzbhn;
    private int zzbho;
    private int zzbhp;
    private long zzbhq;
    private final SimpleArrayMap<String, zzp> zzcnC = new SimpleArrayMap<>();
    private Context zzqG;

    public zzl(Context context) {
        this.zzqG = context;
    }

    private static String zza(KeyPair keyPair, String... strArr) {
        try {
            byte[] bytes = TextUtils.join("\n", strArr).getBytes("UTF-8");
            try {
                PrivateKey privateKey = keyPair.getPrivate();
                Signature signature = Signature.getInstance(privateKey instanceof RSAPrivateKey ? "SHA256withRSA" : "SHA256withECDSA");
                signature.initSign(privateKey);
                signature.update(bytes);
                return FirebaseInstanceId.zzk(signature.sign());
            } catch (GeneralSecurityException e) {
                Log.e("InstanceID/Rpc", "Unable to sign registration request", e);
                return null;
            }
        } catch (UnsupportedEncodingException e2) {
            Log.e("InstanceID/Rpc", "Unable to encode string", e2);
            return null;
        }
    }

    private static boolean zza(PackageManager packageManager) {
        Iterator<ResolveInfo> it = packageManager.queryIntentServices(new Intent("com.google.android.c2dm.intent.REGISTER"), 0).iterator();
        while (it.hasNext()) {
            if (zza(packageManager, it.next().serviceInfo.packageName, "com.google.android.c2dm.intent.REGISTER")) {
                zzbhe = false;
                return true;
            }
        }
        return false;
    }

    private static boolean zza(PackageManager packageManager, String str, String str2) {
        if (packageManager.checkPermission("com.google.android.c2dm.permission.SEND", str) == 0) {
            return zzb(packageManager, str);
        }
        Log.w("InstanceID/Rpc", new StringBuilder(String.valueOf(str).length() + 56 + String.valueOf(str2).length()).append("Possible malicious package ").append(str).append(" declares ").append(str2).append(" without permission").toString());
        return false;
    }

    private final void zzai(String str, String str2) {
        synchronized (this.zzcnC) {
            if (str == null) {
                for (int i = 0; i < this.zzcnC.size(); i++) {
                    this.zzcnC.valueAt(i).onError(str2);
                }
                this.zzcnC.clear();
            } else {
                zzp zzpVarRemove = this.zzcnC.remove(str);
                if (zzpVarRemove == null) {
                    String strValueOf = String.valueOf(str);
                    Log.w("InstanceID/Rpc", strValueOf.length() != 0 ? "Missing callback for ".concat(strValueOf) : new String("Missing callback for "));
                    return;
                }
                zzpVarRemove.onError(str2);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:60:0x0210  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x021c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final android.content.Intent zzb(android.os.Bundle r12, java.security.KeyPair r13) throws java.io.IOException {
        /*
            Method dump skipped, instruction units count: 563
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.iid.zzl.zzb(android.os.Bundle, java.security.KeyPair):android.content.Intent");
    }

    private final void zzb(String str, Intent intent) {
        synchronized (this.zzcnC) {
            zzp zzpVarRemove = this.zzcnC.remove(str);
            if (zzpVarRemove != null) {
                zzpVarRemove.zzq(intent);
            } else {
                String strValueOf = String.valueOf(str);
                Log.w("InstanceID/Rpc", strValueOf.length() != 0 ? "Missing callback for ".concat(strValueOf) : new String("Missing callback for "));
            }
        }
    }

    private static boolean zzb(PackageManager packageManager, String str) {
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(str, 0);
            zzbhd = applicationInfo.packageName;
            zzbhg = applicationInfo.uid;
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static String zzbd(Context context) {
        boolean z;
        if (zzbhd != null) {
            return zzbhd;
        }
        zzbhf = Process.myUid();
        PackageManager packageManager = context.getPackageManager();
        Iterator<ResolveInfo> it = packageManager.queryBroadcastReceivers(new Intent("com.google.iid.TOKEN_REQUEST"), 0).iterator();
        while (true) {
            if (!it.hasNext()) {
                z = false;
                break;
            }
            if (zza(packageManager, it.next().activityInfo.packageName, "com.google.iid.TOKEN_REQUEST")) {
                zzbhe = true;
                z = true;
                break;
            }
        }
        if (z) {
            return zzbhd;
        }
        if (!com.google.android.gms.common.util.zzs.isAtLeastO() && zza(packageManager)) {
            return zzbhd;
        }
        Log.w("InstanceID/Rpc", "Failed to resolve IID implementation package, falling back");
        if (zzb(packageManager, "com.google.android.gms")) {
            zzbhe = com.google.android.gms.common.util.zzs.isAtLeastO();
            return zzbhd;
        }
        if (com.google.android.gms.common.util.zzs.zzsd() || !zzb(packageManager, "com.google.android.gsf")) {
            Log.w("InstanceID/Rpc", "Google Play services is missing, unable to get tokens");
            return null;
        }
        zzbhe = false;
        return zzbhd;
    }

    public static synchronized void zzd(Context context, Intent intent) {
        if (zzbfT == null) {
            Intent intent2 = new Intent();
            intent2.setPackage("com.google.example.invalidpackage");
            zzbfT = PendingIntent.getBroadcast(context, 0, intent2, 0);
        }
        intent.putExtra(SettingsJsonConstants.APP_KEY, zzbfT);
    }

    private final void zzvM() {
        if (this.zzbfX != null) {
            return;
        }
        zzbd(this.zzqG);
        this.zzbfX = new Messenger(new zzm(this, Looper.getMainLooper()));
    }

    public static synchronized String zzvN() {
        int i;
        i = zzbhh;
        zzbhh = i + 1;
        return Integer.toString(i);
    }

    final Intent zza(Bundle bundle, KeyPair keyPair) throws IOException {
        Intent intentZzb = zzb(bundle, keyPair);
        if (intentZzb == null || !intentZzb.hasExtra("google.messenger")) {
            return intentZzb;
        }
        Intent intentZzb2 = zzb(bundle, keyPair);
        if (intentZzb2 == null || !intentZzb2.hasExtra("google.messenger")) {
            return intentZzb2;
        }
        return null;
    }

    final void zzc(Message message) {
        if (message == null) {
            return;
        }
        if (!(message.obj instanceof Intent)) {
            Log.w("InstanceID/Rpc", "Dropping invalid message");
            return;
        }
        Intent intent = (Intent) message.obj;
        intent.setExtrasClassLoader(MessengerCompat.class.getClassLoader());
        if (intent.hasExtra("google.messenger")) {
            Parcelable parcelableExtra = intent.getParcelableExtra("google.messenger");
            if (parcelableExtra instanceof MessengerCompat) {
                this.zzbhl = (MessengerCompat) parcelableExtra;
            }
            if (parcelableExtra instanceof Messenger) {
                this.zzbhk = (Messenger) parcelableExtra;
            }
        }
        zzi((Intent) message.obj);
    }

    final void zzi(Intent intent) {
        String str;
        String strSubstring;
        String str2 = null;
        if (intent == null) {
            if (Log.isLoggable("InstanceID/Rpc", 3)) {
                Log.d("InstanceID/Rpc", "Unexpected response: null");
                return;
            }
            return;
        }
        if (!"com.google.android.c2dm.intent.REGISTRATION".equals(intent.getAction())) {
            if (Log.isLoggable("InstanceID/Rpc", 3)) {
                String strValueOf = String.valueOf(intent.getAction());
                Log.d("InstanceID/Rpc", strValueOf.length() != 0 ? "Unexpected response ".concat(strValueOf) : new String("Unexpected response "));
                return;
            }
            return;
        }
        String stringExtra = intent.getStringExtra("registration_id");
        if (stringExtra == null) {
            stringExtra = intent.getStringExtra("unregistered");
        }
        if (stringExtra != null) {
            this.zzbhm = SystemClock.elapsedRealtime();
            this.zzbhq = 0L;
            this.zzbho = 0;
            this.zzbhp = 0;
            if (stringExtra.startsWith("|")) {
                String[] strArrSplit = stringExtra.split("\\|");
                if (!"ID".equals(strArrSplit[1])) {
                    String strValueOf2 = String.valueOf(stringExtra);
                    Log.w("InstanceID/Rpc", strValueOf2.length() != 0 ? "Unexpected structured response ".concat(strValueOf2) : new String("Unexpected structured response "));
                }
                String str3 = strArrSplit[2];
                if (strArrSplit.length > 4) {
                    if ("SYNC".equals(strArrSplit[3])) {
                        FirebaseInstanceId.zzbG(this.zzqG);
                    } else if ("RST".equals(strArrSplit[3])) {
                        Context context = this.zzqG;
                        zzj.zzb(this.zzqG, null);
                        FirebaseInstanceId.zza(context, zzj.zzKi());
                        intent.removeExtra("registration_id");
                        zzb(str3, intent);
                        return;
                    }
                }
                String strSubstring2 = strArrSplit[strArrSplit.length - 1];
                if (strSubstring2.startsWith(":")) {
                    strSubstring2 = strSubstring2.substring(1);
                }
                intent.putExtra("registration_id", strSubstring2);
                str2 = str3;
            }
            if (str2 != null) {
                zzb(str2, intent);
                return;
            } else {
                if (Log.isLoggable("InstanceID/Rpc", 3)) {
                    Log.d("InstanceID/Rpc", "Ignoring response without a request ID");
                    return;
                }
                return;
            }
        }
        String stringExtra2 = intent.getStringExtra("error");
        if (stringExtra2 == null) {
            String strValueOf3 = String.valueOf(intent.getExtras());
            Log.w("InstanceID/Rpc", new StringBuilder(String.valueOf(strValueOf3).length() + 49).append("Unexpected response, no error or registration id ").append(strValueOf3).toString());
            return;
        }
        if (Log.isLoggable("InstanceID/Rpc", 3)) {
            String strValueOf4 = String.valueOf(stringExtra2);
            Log.d("InstanceID/Rpc", strValueOf4.length() != 0 ? "Received InstanceID error ".concat(strValueOf4) : new String("Received InstanceID error "));
        }
        if (stringExtra2.startsWith("|")) {
            String[] strArrSplit2 = stringExtra2.split("\\|");
            if (!"ID".equals(strArrSplit2[1])) {
                String strValueOf5 = String.valueOf(stringExtra2);
                Log.w("InstanceID/Rpc", strValueOf5.length() != 0 ? "Unexpected structured response ".concat(strValueOf5) : new String("Unexpected structured response "));
            }
            if (strArrSplit2.length > 2) {
                str = strArrSplit2[2];
                strSubstring = strArrSplit2[3];
                if (strSubstring.startsWith(":")) {
                    strSubstring = strSubstring.substring(1);
                }
            } else {
                strSubstring = "UNKNOWN";
                str = null;
            }
            intent.putExtra("error", strSubstring);
        } else {
            str = null;
            strSubstring = stringExtra2;
        }
        zzai(str, strSubstring);
        long longExtra = intent.getLongExtra("Retry-After", 0L);
        if (longExtra > 0) {
            this.zzbhn = SystemClock.elapsedRealtime();
            this.zzbhp = ((int) longExtra) * 1000;
            this.zzbhq = SystemClock.elapsedRealtime() + ((long) this.zzbhp);
            Log.w("InstanceID/Rpc", new StringBuilder(52).append("Explicit request from server to backoff: ").append(this.zzbhp).toString());
            return;
        }
        if (("SERVICE_NOT_AVAILABLE".equals(strSubstring) || "AUTHENTICATION_FAILED".equals(strSubstring)) && "com.google.android.gsf".equals(zzbhd)) {
            this.zzbho++;
            if (this.zzbho >= 3) {
                if (this.zzbho == 3) {
                    this.zzbhp = new Random().nextInt(1000) + 1000;
                }
                this.zzbhp <<= 1;
                this.zzbhq = SystemClock.elapsedRealtime() + ((long) this.zzbhp);
                Log.w("InstanceID/Rpc", new StringBuilder(String.valueOf(strSubstring).length() + 31).append("Backoff due to ").append(strSubstring).append(" for ").append(this.zzbhp).toString());
            }
        }
    }
}
