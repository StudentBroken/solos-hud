package com.google.android.gms.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import com.facebook.appevents.AppEventsConstants;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.measurement.AppMeasurement;
import com.google.firebase.analytics.FirebaseAnalytics;
import io.fabric.sdk.android.services.events.EventsFilesManager;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.security.auth.x500.X500Principal;

/* JADX INFO: loaded from: classes36.dex */
public final class zzckx extends zzciv {
    private static String[] zzbuH = {"firebase_"};
    private SecureRandom zzbuI;
    private final AtomicLong zzbuJ;
    private int zzbuK;

    zzckx(zzchx zzchxVar) {
        super(zzchxVar);
        this.zzbuJ = new AtomicLong(0L);
    }

    public static Bundle[] zzC(Object obj) {
        if (obj instanceof Bundle) {
            return new Bundle[]{(Bundle) obj};
        }
        if (obj instanceof Parcelable[]) {
            return (Bundle[]) Arrays.copyOf((Parcelable[]) obj, ((Parcelable[]) obj).length, Bundle[].class);
        }
        if (!(obj instanceof ArrayList)) {
            return null;
        }
        ArrayList arrayList = (ArrayList) obj;
        return (Bundle[]) arrayList.toArray(new Bundle[arrayList.size()]);
    }

    public static Object zzD(Object obj) throws Throwable {
        ObjectInputStream objectInputStream;
        ObjectOutputStream objectOutputStream;
        try {
            if (obj == null) {
                return null;
            }
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                try {
                    objectOutputStream.writeObject(obj);
                    objectOutputStream.flush();
                    objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
                } catch (Throwable th) {
                    th = th;
                    objectInputStream = null;
                }
                try {
                    Object object = objectInputStream.readObject();
                    objectOutputStream.close();
                    objectInputStream.close();
                    return object;
                } catch (Throwable th2) {
                    th = th2;
                    if (objectOutputStream != null) {
                        objectOutputStream.close();
                    }
                    if (objectInputStream != null) {
                        objectInputStream.close();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                objectInputStream = null;
                objectOutputStream = null;
            }
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e2) {
            return null;
        }
    }

    private final boolean zzK(Context context, String str) {
        X500Principal x500Principal = new X500Principal("CN=Android Debug,O=Android,C=US");
        try {
            PackageInfo packageInfo = zzbim.zzaP(context).getPackageInfo(str, 64);
            if (packageInfo != null && packageInfo.signatures != null && packageInfo.signatures.length > 0) {
                return ((X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(packageInfo.signatures[0].toByteArray()))).getSubjectX500Principal().equals(x500Principal);
            }
        } catch (PackageManager.NameNotFoundException e) {
            super.zzwE().zzyv().zzj("Package name not found", e);
        } catch (CertificateException e2) {
            super.zzwE().zzyv().zzj("Error obtaining certificate", e2);
        }
        return true;
    }

    private final boolean zzP(String str, String str2) {
        if (str2 == null) {
            super.zzwE().zzyv().zzj("Name is required and can't be null. Type", str);
            return false;
        }
        if (str2.length() == 0) {
            super.zzwE().zzyv().zzj("Name is required and can't be empty. Type", str);
            return false;
        }
        int iCodePointAt = str2.codePointAt(0);
        if (!Character.isLetter(iCodePointAt)) {
            super.zzwE().zzyv().zze("Name must start with a letter. Type, name", str, str2);
            return false;
        }
        int length = str2.length();
        int iCharCount = Character.charCount(iCodePointAt);
        while (iCharCount < length) {
            int iCodePointAt2 = str2.codePointAt(iCharCount);
            if (iCodePointAt2 != 95 && !Character.isLetterOrDigit(iCodePointAt2)) {
                super.zzwE().zzyv().zze("Name must consist of letters, digits or _ (underscores). Type, name", str, str2);
                return false;
            }
            iCharCount += Character.charCount(iCodePointAt2);
        }
        return true;
    }

    private final boolean zzQ(String str, String str2) {
        if (str2 == null) {
            super.zzwE().zzyv().zzj("Name is required and can't be null. Type", str);
            return false;
        }
        if (str2.length() == 0) {
            super.zzwE().zzyv().zzj("Name is required and can't be empty. Type", str);
            return false;
        }
        int iCodePointAt = str2.codePointAt(0);
        if (!Character.isLetter(iCodePointAt) && iCodePointAt != 95) {
            super.zzwE().zzyv().zze("Name must start with a letter or _ (underscore). Type, name", str, str2);
            return false;
        }
        int length = str2.length();
        int iCharCount = Character.charCount(iCodePointAt);
        while (iCharCount < length) {
            int iCodePointAt2 = str2.codePointAt(iCharCount);
            if (iCodePointAt2 != 95 && !Character.isLetterOrDigit(iCodePointAt2)) {
                super.zzwE().zzyv().zze("Name must consist of letters, digits or _ (underscores). Type, name", str, str2);
                return false;
            }
            iCharCount += Character.charCount(iCodePointAt2);
        }
        return true;
    }

    public static boolean zzR(String str, String str2) {
        if (str == null && str2 == null) {
            return true;
        }
        if (str == null) {
            return false;
        }
        return str.equals(str2);
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0029 A[ORIG_RETURN, RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final int zza(java.lang.String r8, java.lang.Object r9, boolean r10) {
        /*
            r7 = this;
            r1 = 1
            r6 = 0
            if (r10 == 0) goto L3c
            java.lang.String r2 = "param"
            com.google.android.gms.internal.zzcfy.zzxl()
            boolean r0 = r9 instanceof android.os.Parcelable[]
            if (r0 == 0) goto L2c
            r0 = r9
            android.os.Parcelable[] r0 = (android.os.Parcelable[]) r0
            int r0 = r0.length
        L11:
            r3 = 1000(0x3e8, float:1.401E-42)
            if (r0 <= r3) goto L3a
            com.google.android.gms.internal.zzcgx r1 = super.zzwE()
            com.google.android.gms.internal.zzcgz r1 = r1.zzyx()
            java.lang.String r3 = "Parameter array is too long; discarded. Value kind, name, array length"
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            r1.zzd(r3, r2, r8, r0)
            r0 = r6
        L27:
            if (r0 != 0) goto L3c
            r0 = 17
        L2b:
            return r0
        L2c:
            boolean r0 = r9 instanceof java.util.ArrayList
            if (r0 == 0) goto L38
            r0 = r9
            java.util.ArrayList r0 = (java.util.ArrayList) r0
            int r0 = r0.size()
            goto L11
        L38:
            r0 = r1
            goto L27
        L3a:
            r0 = r1
            goto L27
        L3c:
            boolean r0 = zzey(r8)
            if (r0 == 0) goto L54
            java.lang.String r1 = "param"
            int r3 = com.google.android.gms.internal.zzcfy.zzxk()
            r0 = r7
            r2 = r8
            r4 = r9
            r5 = r10
            boolean r0 = r0.zza(r1, r2, r3, r4, r5)
        L50:
            if (r0 == 0) goto L63
            r0 = r6
            goto L2b
        L54:
            java.lang.String r1 = "param"
            int r3 = com.google.android.gms.internal.zzcfy.zzxj()
            r0 = r7
            r2 = r8
            r4 = r9
            r5 = r10
            boolean r0 = r0.zza(r1, r2, r3, r4, r5)
            goto L50
        L63:
            r0 = 4
            goto L2b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzckx.zza(java.lang.String, java.lang.Object, boolean):int");
    }

    private static Object zza(int i, Object obj, boolean z) {
        if (obj == null) {
            return null;
        }
        if ((obj instanceof Long) || (obj instanceof Double)) {
            return obj;
        }
        if (obj instanceof Integer) {
            return Long.valueOf(((Integer) obj).intValue());
        }
        if (obj instanceof Byte) {
            return Long.valueOf(((Byte) obj).byteValue());
        }
        if (obj instanceof Short) {
            return Long.valueOf(((Short) obj).shortValue());
        }
        if (obj instanceof Boolean) {
            return Long.valueOf(((Boolean) obj).booleanValue() ? 1L : 0L);
        }
        if (obj instanceof Float) {
            return Double.valueOf(((Float) obj).doubleValue());
        }
        if ((obj instanceof String) || (obj instanceof Character) || (obj instanceof CharSequence)) {
            return zza(String.valueOf(obj), i, z);
        }
        return null;
    }

    public static String zza(String str, int i, boolean z) {
        if (str.codePointCount(0, str.length()) <= i) {
            return str;
        }
        if (z) {
            return String.valueOf(str.substring(0, str.offsetByCodePoints(0, i))).concat("...");
        }
        return null;
    }

    @Nullable
    public static String zza(String str, String[] strArr, String[] strArr2) {
        zzbr.zzu(strArr);
        zzbr.zzu(strArr2);
        int iMin = Math.min(strArr.length, strArr2.length);
        for (int i = 0; i < iMin; i++) {
            if (zzR(str, strArr[i])) {
                return strArr2[i];
            }
        }
        return null;
    }

    public static boolean zza(Context context, String str, boolean z) {
        ActivityInfo receiverInfo;
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager == null || (receiverInfo = packageManager.getReceiverInfo(new ComponentName(context, str), 2)) == null) {
                return false;
            }
            return receiverInfo.enabled;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private final boolean zza(String str, String str2, int i, Object obj, boolean z) {
        if (obj == null || (obj instanceof Long) || (obj instanceof Float) || (obj instanceof Integer) || (obj instanceof Byte) || (obj instanceof Short) || (obj instanceof Boolean) || (obj instanceof Double)) {
            return true;
        }
        if ((obj instanceof String) || (obj instanceof Character) || (obj instanceof CharSequence)) {
            String strValueOf = String.valueOf(obj);
            if (strValueOf.codePointCount(0, strValueOf.length()) <= i) {
                return true;
            }
            super.zzwE().zzyx().zzd("Value is too long; discarded. Value kind, name, value length", str, str2, Integer.valueOf(strValueOf.length()));
            return false;
        }
        if ((obj instanceof Bundle) && z) {
            return true;
        }
        if ((obj instanceof Parcelable[]) && z) {
            for (Parcelable parcelable : (Parcelable[]) obj) {
                if (!(parcelable instanceof Bundle)) {
                    super.zzwE().zzyx().zze("All Parcelable[] elements must be of type Bundle. Value type, name", parcelable.getClass(), str2);
                    return false;
                }
            }
            return true;
        }
        if (!(obj instanceof ArrayList) || !z) {
            return false;
        }
        ArrayList arrayList = (ArrayList) obj;
        int size = arrayList.size();
        int i2 = 0;
        while (i2 < size) {
            Object obj2 = arrayList.get(i2);
            i2++;
            if (!(obj2 instanceof Bundle)) {
                super.zzwE().zzyx().zze("All ArrayList elements must be of type Bundle. Value type, name", obj2.getClass(), str2);
                return false;
            }
        }
        return true;
    }

    private final boolean zza(String str, String[] strArr, String str2) {
        boolean z;
        boolean z2;
        if (str2 == null) {
            super.zzwE().zzyv().zzj("Name is required and can't be null. Type", str);
            return false;
        }
        zzbr.zzu(str2);
        int i = 0;
        while (true) {
            if (i >= zzbuH.length) {
                z = false;
                break;
            }
            if (str2.startsWith(zzbuH[i])) {
                z = true;
                break;
            }
            i++;
        }
        if (z) {
            super.zzwE().zzyv().zze("Name starts with reserved prefix. Type, name", str, str2);
            return false;
        }
        if (strArr != null) {
            zzbr.zzu(strArr);
            int i2 = 0;
            while (true) {
                if (i2 >= strArr.length) {
                    z2 = false;
                    break;
                }
                if (zzR(str2, strArr[i2])) {
                    z2 = true;
                    break;
                }
                i2++;
            }
            if (z2) {
                super.zzwE().zzyv().zze("Name is reserved. Type, name", str, str2);
                return false;
            }
        }
        return true;
    }

    public static boolean zza(long[] jArr, int i) {
        return i < (jArr.length << 6) && (jArr[i / 64] & (1 << (i % 64))) != 0;
    }

    static byte[] zza(Parcelable parcelable) {
        if (parcelable == null) {
            return null;
        }
        Parcel parcelObtain = Parcel.obtain();
        try {
            parcelable.writeToParcel(parcelObtain, 0);
            return parcelObtain.marshall();
        } finally {
            parcelObtain.recycle();
        }
    }

    public static long[] zza(BitSet bitSet) {
        int length = (bitSet.length() + 63) / 64;
        long[] jArr = new long[length];
        for (int i = 0; i < length; i++) {
            jArr[i] = 0;
            for (int i2 = 0; i2 < 64 && (i << 6) + i2 < bitSet.length(); i2++) {
                if (bitSet.get((i << 6) + i2)) {
                    jArr[i] = jArr[i] | (1 << i2);
                }
            }
        }
        return jArr;
    }

    private static void zzb(Bundle bundle, Object obj) {
        zzbr.zzu(bundle);
        if (obj != null) {
            if ((obj instanceof String) || (obj instanceof CharSequence)) {
                bundle.putLong("_el", String.valueOf(obj).length());
            }
        }
    }

    private final boolean zzb(String str, int i, String str2) {
        if (str2 == null) {
            super.zzwE().zzyv().zzj("Name is required and can't be null. Type", str);
            return false;
        }
        if (str2.codePointCount(0, str2.length()) <= i) {
            return true;
        }
        super.zzwE().zzyv().zzd("Name is too long. Type, maximum supported length, name", str, Integer.valueOf(i), str2);
        return false;
    }

    static MessageDigest zzbE(String str) {
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

    private static boolean zzd(Bundle bundle, int i) {
        if (bundle.getLong("_err") != 0) {
            return false;
        }
        bundle.putLong("_err", i);
        return true;
    }

    @WorkerThread
    static boolean zzd(zzcgl zzcglVar, zzcft zzcftVar) {
        zzbr.zzu(zzcglVar);
        zzbr.zzu(zzcftVar);
        if (!TextUtils.isEmpty(zzcftVar.zzboU)) {
            return true;
        }
        zzcfy.zzxD();
        return false;
    }

    static boolean zzeA(String str) {
        return str != null && str.matches("(\\+|-)?([0-9]+\\.?[0-9]*|[0-9]*\\.?[0-9]+)") && str.length() <= 310;
    }

    @WorkerThread
    static boolean zzeD(String str) {
        zzbr.zzcF(str);
        switch (str) {
            case "_in":
            case "_ui":
            case "_ug":
                return true;
            default:
                return false;
        }
    }

    static boolean zzep(String str) {
        zzbr.zzcF(str);
        return str.charAt(0) != '_' || str.equals("_ep");
    }

    private final int zzeu(String str) {
        if (!zzP("event param", str)) {
            return 3;
        }
        if (zza("event param", (String[]) null, str)) {
            return zzb("event param", zzcfy.zzxi(), str) ? 0 : 3;
        }
        return 14;
    }

    private final int zzev(String str) {
        if (!zzQ("event param", str)) {
            return 3;
        }
        if (zza("event param", (String[]) null, str)) {
            return zzb("event param", zzcfy.zzxi(), str) ? 0 : 3;
        }
        return 14;
    }

    private static int zzex(String str) {
        return "_ldl".equals(str) ? zzcfy.zzxn() : zzcfy.zzxm();
    }

    public static boolean zzey(String str) {
        return !TextUtils.isEmpty(str) && str.startsWith(EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR);
    }

    public static boolean zzl(Intent intent) {
        String stringExtra = intent.getStringExtra("android.intent.extra.REFERRER_NAME");
        return "android-app://com.google.android.googlequicksearchbox/https/www.google.com".equals(stringExtra) || "https://www.google.com".equals(stringExtra) || "android-app://com.google.appcrawler".equals(stringExtra);
    }

    static long zzo(byte[] bArr) {
        int i = 0;
        zzbr.zzu(bArr);
        zzbr.zzae(bArr.length > 0);
        long j = 0;
        for (int length = bArr.length - 1; length >= 0 && length >= bArr.length - 8; length--) {
            j += (((long) bArr[length]) & 255) << i;
            i += 8;
        }
        return j;
    }

    public static boolean zzw(Context context, String str) {
        ServiceInfo serviceInfo;
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager == null || (serviceInfo = packageManager.getServiceInfo(new ComponentName(context, str), 4)) == null) {
                return false;
            }
            return serviceInfo.enabled;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    final Bundle zzB(Bundle bundle) {
        Bundle bundle2 = new Bundle();
        if (bundle != null) {
            for (String str : bundle.keySet()) {
                Object objZzk = zzk(str, bundle.get(str));
                if (objZzk == null) {
                    super.zzwE().zzyx().zzj("Param value can't be null", super.zzwz().zzdY(str));
                } else {
                    zza(bundle2, str, objZzk);
                }
            }
        }
        return bundle2;
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0078  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:17:0x006b -> B:18:0x0078). Please report as a decompilation issue!!! */
    @android.support.annotation.WorkerThread
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    final long zzJ(android.content.Context r9, java.lang.String r10) {
        /*
            r8 = this;
            r0 = -1
            super.zzjB()
            com.google.android.gms.common.internal.zzbr.zzu(r9)
            com.google.android.gms.common.internal.zzbr.zzcF(r10)
            r2 = 0
            android.content.pm.PackageManager r4 = r9.getPackageManager()
            java.lang.String r5 = "MD5"
            java.security.MessageDigest r5 = zzbE(r5)
            if (r5 != 0) goto L27
            com.google.android.gms.internal.zzcgx r2 = super.zzwE()
            com.google.android.gms.internal.zzcgz r2 = r2.zzyv()
            java.lang.String r3 = "Could not get MD5 instance"
            r2.log(r3)
        L26:
            return r0
        L27:
            if (r4 == 0) goto L78
            boolean r4 = r8.zzK(r9, r10)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L6a
            if (r4 != 0) goto L78
            com.google.android.gms.internal.zzbil r4 = com.google.android.gms.internal.zzbim.zzaP(r9)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L6a
            android.content.Context r6 = super.getContext()     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L6a
            java.lang.String r6 = r6.getPackageName()     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L6a
            r7 = 64
            android.content.pm.PackageInfo r4 = r4.getPackageInfo(r6, r7)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L6a
            android.content.pm.Signature[] r6 = r4.signatures     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L6a
            if (r6 == 0) goto L5c
            android.content.pm.Signature[] r6 = r4.signatures     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L6a
            int r6 = r6.length     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L6a
            if (r6 <= 0) goto L5c
            android.content.pm.Signature[] r0 = r4.signatures     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L6a
            r1 = 0
            r0 = r0[r1]     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L6a
            byte[] r0 = r0.toByteArray()     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L6a
            byte[] r0 = r5.digest(r0)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L6a
            long r0 = zzo(r0)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L6a
            goto L26
        L5c:
            com.google.android.gms.internal.zzcgx r4 = super.zzwE()     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L6a
            com.google.android.gms.internal.zzcgz r4 = r4.zzyx()     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L6a
            java.lang.String r5 = "Could not get signatures"
            r4.log(r5)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L6a
            goto L26
        L6a:
            r0 = move-exception
            com.google.android.gms.internal.zzcgx r1 = super.zzwE()
            com.google.android.gms.internal.zzcgz r1 = r1.zzyv()
            java.lang.String r4 = "Package name not found"
            r1.zzj(r4, r0)
        L78:
            r0 = r2
            goto L26
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzckx.zzJ(android.content.Context, java.lang.String):long");
    }

    public final Bundle zza(String str, Bundle bundle, @Nullable List<String> list, boolean z, boolean z2) {
        int iZzeu;
        if (bundle == null) {
            return null;
        }
        Bundle bundle2 = new Bundle(bundle);
        zzcfy.zzxf();
        int i = 0;
        for (String str2 : bundle.keySet()) {
            if (list == null || !list.contains(str2)) {
                iZzeu = z ? zzeu(str2) : 0;
                if (iZzeu == 0) {
                    iZzeu = zzev(str2);
                }
            } else {
                iZzeu = 0;
            }
            if (iZzeu != 0) {
                if (zzd(bundle2, iZzeu)) {
                    bundle2.putString("_ev", zza(str2, zzcfy.zzxi(), true));
                    if (iZzeu == 3) {
                        zzb(bundle2, str2);
                    }
                }
                bundle2.remove(str2);
            } else {
                int iZza = zza(str2, bundle.get(str2), z2);
                if (iZza != 0 && !"_ev".equals(str2)) {
                    if (zzd(bundle2, iZza)) {
                        bundle2.putString("_ev", zza(str2, zzcfy.zzxi(), true));
                        zzb(bundle2, bundle.get(str2));
                    }
                    bundle2.remove(str2);
                } else if (!zzep(str2) || (i = i + 1) <= 25) {
                    i = i;
                } else {
                    super.zzwE().zzyv().zze(new StringBuilder(48).append("Event can't contain more then 25 params").toString(), super.zzwz().zzdX(str), super.zzwz().zzA(bundle));
                    zzd(bundle2, 5);
                    bundle2.remove(str2);
                }
            }
        }
        return bundle2;
    }

    final zzcgl zza(String str, Bundle bundle, String str2, long j, boolean z, boolean z2) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (zzer(str) != 0) {
            super.zzwE().zzyv().zzj("Invalid conditional property event name", super.zzwz().zzdZ(str));
            throw new IllegalArgumentException();
        }
        Bundle bundle2 = bundle != null ? new Bundle(bundle) : new Bundle();
        bundle2.putString("_o", str2);
        return new zzcgl(str, new zzcgi(zzB(zza(str, bundle2, Collections.singletonList("_o"), false, false))), str2, j);
    }

    public final void zza(int i, String str, String str2, int i2) {
        zza((String) null, i, str, str2, i2);
    }

    public final void zza(Bundle bundle, String str, Object obj) {
        if (bundle == null) {
            return;
        }
        if (obj instanceof Long) {
            bundle.putLong(str, ((Long) obj).longValue());
            return;
        }
        if (obj instanceof String) {
            bundle.putString(str, String.valueOf(obj));
        } else if (obj instanceof Double) {
            bundle.putDouble(str, ((Double) obj).doubleValue());
        } else if (str != null) {
            super.zzwE().zzyy().zze("Not putting event parameter. Invalid value type. name, type", super.zzwz().zzdY(str), obj != null ? obj.getClass().getSimpleName() : null);
        }
    }

    public final void zza(zzclj zzcljVar, Object obj) {
        zzbr.zzu(obj);
        zzcljVar.zzaIH = null;
        zzcljVar.zzbvE = null;
        zzcljVar.zzbuF = null;
        if (obj instanceof String) {
            zzcljVar.zzaIH = (String) obj;
            return;
        }
        if (obj instanceof Long) {
            zzcljVar.zzbvE = (Long) obj;
        } else if (obj instanceof Double) {
            zzcljVar.zzbuF = (Double) obj;
        } else {
            super.zzwE().zzyv().zzj("Ignoring invalid (type) event param value", obj);
        }
    }

    public final void zza(zzcln zzclnVar, Object obj) {
        zzbr.zzu(obj);
        zzclnVar.zzaIH = null;
        zzclnVar.zzbvE = null;
        zzclnVar.zzbuF = null;
        if (obj instanceof String) {
            zzclnVar.zzaIH = (String) obj;
            return;
        }
        if (obj instanceof Long) {
            zzclnVar.zzbvE = (Long) obj;
        } else if (obj instanceof Double) {
            zzclnVar.zzbuF = (Double) obj;
        } else {
            super.zzwE().zzyv().zzj("Ignoring invalid (type) user attribute value", obj);
        }
    }

    public final void zza(String str, int i, String str2, String str3, int i2) {
        Bundle bundle = new Bundle();
        zzd(bundle, i);
        if (!TextUtils.isEmpty(str2)) {
            bundle.putString(str2, str3);
        }
        if (i == 6 || i == 7 || i == 2) {
            bundle.putLong("_el", i2);
        }
        zzcfy.zzxD();
        this.zzboi.zzws().zzd("auto", "_err", bundle);
    }

    final <T extends Parcelable> T zzb(byte[] bArr, Parcelable.Creator<T> creator) {
        if (bArr == null) {
            return null;
        }
        Parcel parcelObtain = Parcel.obtain();
        try {
            parcelObtain.unmarshall(bArr, 0, bArr.length);
            parcelObtain.setDataPosition(0);
            return creator.createFromParcel(parcelObtain);
        } catch (com.google.android.gms.common.internal.safeparcel.zzc e) {
            super.zzwE().zzyv().log("Failed to load parcelable from buffer");
            return null;
        } finally {
            parcelObtain.recycle();
        }
    }

    public final byte[] zzb(zzclk zzclkVar) {
        try {
            byte[] bArr = new byte[zzclkVar.zzMl()];
            ahx ahxVarZzc = ahx.zzc(bArr, 0, bArr.length);
            zzclkVar.zza(ahxVarZzc);
            ahxVarZzc.zzMc();
            return bArr;
        } catch (IOException e) {
            super.zzwE().zzyv().zzj("Data loss. Failed to serialize batch", e);
            return null;
        }
    }

    @WorkerThread
    public final boolean zzbv(String str) {
        super.zzjB();
        if (zzbim.zzaP(super.getContext()).checkCallingOrSelfPermission(str) == 0) {
            return true;
        }
        super.zzwE().zzyA().zzj("Permission not granted", str);
        return false;
    }

    final boolean zzeB(String str) {
        return AppEventsConstants.EVENT_PARAM_VALUE_YES.equals(super.zzwB().zzM(str, "measurement.upload.blacklist_internal"));
    }

    final boolean zzeC(String str) {
        return AppEventsConstants.EVENT_PARAM_VALUE_YES.equals(super.zzwB().zzM(str, "measurement.upload.blacklist_public"));
    }

    public final int zzeq(String str) {
        if (!zzP("event", str)) {
            return 2;
        }
        if (zza("event", AppMeasurement.Event.zzboj, str)) {
            return zzb("event", zzcfy.zzxg(), str) ? 0 : 2;
        }
        return 13;
    }

    public final int zzer(String str) {
        if (!zzQ("event", str)) {
            return 2;
        }
        if (zza("event", AppMeasurement.Event.zzboj, str)) {
            return zzb("event", zzcfy.zzxg(), str) ? 0 : 2;
        }
        return 13;
    }

    public final int zzes(String str) {
        if (!zzP("user property", str)) {
            return 6;
        }
        if (zza("user property", AppMeasurement.UserProperty.zzboq, str)) {
            return zzb("user property", zzcfy.zzxh(), str) ? 0 : 6;
        }
        return 15;
    }

    public final int zzet(String str) {
        if (!zzQ("user property", str)) {
            return 6;
        }
        if (zza("user property", AppMeasurement.UserProperty.zzboq, str)) {
            return zzb("user property", zzcfy.zzxh(), str) ? 0 : 6;
        }
        return 15;
    }

    public final boolean zzew(String str) {
        if (TextUtils.isEmpty(str)) {
            super.zzwE().zzyv().log("Missing google_app_id. Firebase Analytics disabled. See https://goo.gl/NAOOOI");
            return false;
        }
        zzbr.zzu(str);
        if (str.matches("^1:\\d+:android:[a-f0-9]+$")) {
            return true;
        }
        super.zzwE().zzyv().zzj("Invalid google_app_id. Firebase Analytics disabled. See https://goo.gl/NAOOOI. provided id", str);
        return false;
    }

    public final boolean zzez(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        String strZzxZ = super.zzwG().zzxZ();
        zzcfy.zzxD();
        return strZzxZ.equals(str);
    }

    public final boolean zzf(long j, long j2) {
        return j == 0 || j2 <= 0 || Math.abs(super.zzkp().currentTimeMillis() - j) > j2;
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ void zzjB() {
        super.zzjB();
    }

    @Override // com.google.android.gms.internal.zzciv
    protected final void zzjC() {
        SecureRandom secureRandom = new SecureRandom();
        long jNextLong = secureRandom.nextLong();
        if (jNextLong == 0) {
            jNextLong = secureRandom.nextLong();
            if (jNextLong == 0) {
                super.zzwE().zzyx().log("Utils falling back to Random for random id");
            }
        }
        this.zzbuJ.set(jNextLong);
    }

    public final Object zzk(String str, Object obj) {
        if ("_ev".equals(str)) {
            return zza(zzcfy.zzxk(), obj, true);
        }
        return zza(zzey(str) ? zzcfy.zzxk() : zzcfy.zzxj(), obj, false);
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ com.google.android.gms.common.util.zzf zzkp() {
        return super.zzkp();
    }

    public final int zzl(String str, Object obj) {
        return "_ldl".equals(str) ? zza("user property referrer", str, zzex(str), obj, false) : zza("user property", str, zzex(str), obj, false) ? 0 : 7;
    }

    public final Object zzm(String str, Object obj) {
        return "_ldl".equals(str) ? zza(zzex(str), obj, true) : zza(zzex(str), obj, false);
    }

    public final byte[] zzm(byte[] bArr) throws IOException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gZIPOutputStream.write(bArr);
            gZIPOutputStream.close();
            byteArrayOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            super.zzwE().zzyv().zzj("Failed to gzip content", e);
            throw e;
        }
    }

    public final byte[] zzn(byte[] bArr) throws IOException {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
            GZIPInputStream gZIPInputStream = new GZIPInputStream(byteArrayInputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr2 = new byte[1024];
            while (true) {
                int i = gZIPInputStream.read(bArr2);
                if (i <= 0) {
                    gZIPInputStream.close();
                    byteArrayInputStream.close();
                    return byteArrayOutputStream.toByteArray();
                }
                byteArrayOutputStream.write(bArr2, 0, i);
            }
        } catch (IOException e) {
            super.zzwE().zzyv().zzj("Failed to ungzip content", e);
            throw e;
        }
    }

    public final Bundle zzq(@NonNull Uri uri) {
        String queryParameter;
        String queryParameter2;
        String queryParameter3;
        String queryParameter4;
        Bundle bundle = null;
        if (uri != null) {
            try {
                if (uri.isHierarchical()) {
                    queryParameter4 = uri.getQueryParameter("utm_campaign");
                    queryParameter3 = uri.getQueryParameter("utm_source");
                    queryParameter2 = uri.getQueryParameter("utm_medium");
                    queryParameter = uri.getQueryParameter("gclid");
                } else {
                    queryParameter = null;
                    queryParameter2 = null;
                    queryParameter3 = null;
                    queryParameter4 = null;
                }
                if (!TextUtils.isEmpty(queryParameter4) || !TextUtils.isEmpty(queryParameter3) || !TextUtils.isEmpty(queryParameter2) || !TextUtils.isEmpty(queryParameter)) {
                    bundle = new Bundle();
                    if (!TextUtils.isEmpty(queryParameter4)) {
                        bundle.putString(FirebaseAnalytics.Param.CAMPAIGN, queryParameter4);
                    }
                    if (!TextUtils.isEmpty(queryParameter3)) {
                        bundle.putString("source", queryParameter3);
                    }
                    if (!TextUtils.isEmpty(queryParameter2)) {
                        bundle.putString("medium", queryParameter2);
                    }
                    if (!TextUtils.isEmpty(queryParameter)) {
                        bundle.putString("gclid", queryParameter);
                    }
                    String queryParameter5 = uri.getQueryParameter("utm_term");
                    if (!TextUtils.isEmpty(queryParameter5)) {
                        bundle.putString(FirebaseAnalytics.Param.TERM, queryParameter5);
                    }
                    String queryParameter6 = uri.getQueryParameter("utm_content");
                    if (!TextUtils.isEmpty(queryParameter6)) {
                        bundle.putString("content", queryParameter6);
                    }
                    String queryParameter7 = uri.getQueryParameter(FirebaseAnalytics.Param.ACLID);
                    if (!TextUtils.isEmpty(queryParameter7)) {
                        bundle.putString(FirebaseAnalytics.Param.ACLID, queryParameter7);
                    }
                    String queryParameter8 = uri.getQueryParameter(FirebaseAnalytics.Param.CP1);
                    if (!TextUtils.isEmpty(queryParameter8)) {
                        bundle.putString(FirebaseAnalytics.Param.CP1, queryParameter8);
                    }
                    String queryParameter9 = uri.getQueryParameter("anid");
                    if (!TextUtils.isEmpty(queryParameter9)) {
                        bundle.putString("anid", queryParameter9);
                    }
                }
            } catch (UnsupportedOperationException e) {
                super.zzwE().zzyx().zzj("Install referrer url isn't a hierarchical URI", e);
            }
        }
        return bundle;
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

    public final long zzzq() {
        long andIncrement;
        if (this.zzbuJ.get() == 0) {
            synchronized (this.zzbuJ) {
                long jNextLong = new Random(System.nanoTime() ^ super.zzkp().currentTimeMillis()).nextLong();
                int i = this.zzbuK + 1;
                this.zzbuK = i;
                andIncrement = jNextLong + ((long) i);
            }
        } else {
            synchronized (this.zzbuJ) {
                this.zzbuJ.compareAndSet(-1L, 1L);
                andIncrement = this.zzbuJ.getAndIncrement();
            }
        }
        return andIncrement;
    }

    @WorkerThread
    final SecureRandom zzzr() {
        super.zzjB();
        if (this.zzbuI == null) {
            this.zzbuI = new SecureRandom();
        }
        return this.zzbuI;
    }
}
