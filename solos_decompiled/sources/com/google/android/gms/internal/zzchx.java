package com.google.android.gms.internal;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.support.annotation.WorkerThread;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Pair;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.measurement.AppMeasurement;
import com.google.firebase.analytics.FirebaseAnalytics;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: loaded from: classes36.dex */
public class zzchx {
    private static volatile zzchx zzbsq;
    private final Context mContext;
    private final boolean zzafM;
    private final zzcgv zzbsA;
    private final zzcfz zzbsB;
    private final zzcgt zzbsC;
    private final zzchb zzbsD;
    private final zzcjl zzbsE;
    private final zzcjp zzbsF;
    private final zzcgf zzbsG;
    private final zzcix zzbsH;
    private final zzcgs zzbsI;
    private final zzchg zzbsJ;
    private final zzcks zzbsK;
    private final zzcfv zzbsL;
    private final zzcfo zzbsM;
    private boolean zzbsN;
    private Boolean zzbsO;
    private long zzbsP;
    private FileLock zzbsQ;
    private FileChannel zzbsR;
    private List<Long> zzbsS;
    private List<Runnable> zzbsT;
    private int zzbsU;
    private int zzbsV;
    private long zzbsW;
    private long zzbsX;
    private boolean zzbsY;
    private boolean zzbsZ;
    private final zzcfy zzbsr;
    private final zzchi zzbss;
    private final zzcgx zzbst;
    private final zzchs zzbsu;
    private final zzckm zzbsv;
    private final zzchr zzbsw;
    private final AppMeasurement zzbsx;
    private final FirebaseAnalytics zzbsy;
    private final zzckx zzbsz;
    private boolean zzbta;
    private final long zzbtb;
    private final com.google.android.gms.common.util.zzf zzvz;

    class zza implements zzcgb {
        zzcll zzbtd;
        List<Long> zzbte;
        private long zzbtf;
        List<zzcli> zztK;

        private zza() {
        }

        /* synthetic */ zza(zzchx zzchxVar, zzchy zzchyVar) {
            this();
        }

        private static long zza(zzcli zzcliVar) {
            return ((zzcliVar.zzbvB.longValue() / 1000) / 60) / 60;
        }

        @Override // com.google.android.gms.internal.zzcgb
        public final boolean zza(long j, zzcli zzcliVar) {
            zzbr.zzu(zzcliVar);
            if (this.zztK == null) {
                this.zztK = new ArrayList();
            }
            if (this.zzbte == null) {
                this.zzbte = new ArrayList();
            }
            if (this.zztK.size() > 0 && zza(this.zztK.get(0)) != zza(zzcliVar)) {
                return false;
            }
            long jZzMl = this.zzbtf + ((long) zzcliVar.zzMl());
            if (jZzMl >= zzcfy.zzxK()) {
                return false;
            }
            this.zzbtf = jZzMl;
            this.zztK.add(zzcliVar);
            this.zzbte.add(Long.valueOf(j));
            return this.zztK.size() < zzcfy.zzxL();
        }

        @Override // com.google.android.gms.internal.zzcgb
        public final void zzb(zzcll zzcllVar) {
            zzbr.zzu(zzcllVar);
            this.zzbtd = zzcllVar;
        }
    }

    private zzchx(zzciw zzciwVar) {
        zzcgz zzcgzVarZzyz;
        String strConcat;
        zzbr.zzu(zzciwVar);
        this.mContext = zzciwVar.mContext;
        this.zzbsW = -1L;
        this.zzvz = com.google.android.gms.common.util.zzj.zzrX();
        this.zzbtb = this.zzvz.currentTimeMillis();
        this.zzbsr = new zzcfy(this);
        zzchi zzchiVar = new zzchi(this);
        zzchiVar.initialize();
        this.zzbss = zzchiVar;
        zzcgx zzcgxVar = new zzcgx(this);
        zzcgxVar.initialize();
        this.zzbst = zzcgxVar;
        zzwE().zzyz().zzj("App measurement is starting up, version", Long.valueOf(zzcfy.zzwO()));
        zzcfy.zzxD();
        zzwE().zzyz().log("To enable debug logging run: adb shell setprop log.tag.FA VERBOSE");
        zzckx zzckxVar = new zzckx(this);
        zzckxVar.initialize();
        this.zzbsz = zzckxVar;
        zzcgv zzcgvVar = new zzcgv(this);
        zzcgvVar.initialize();
        this.zzbsA = zzcgvVar;
        zzcgf zzcgfVar = new zzcgf(this);
        zzcgfVar.initialize();
        this.zzbsG = zzcgfVar;
        zzcgs zzcgsVar = new zzcgs(this);
        zzcgsVar.initialize();
        this.zzbsI = zzcgsVar;
        zzcfy.zzxD();
        String strZzhk = zzcgsVar.zzhk();
        if (zzwA().zzez(strZzhk)) {
            zzcgzVarZzyz = zzwE().zzyz();
            strConcat = "Faster debug mode event logging enabled. To disable, run:\n  adb shell setprop debug.firebase.analytics.app .none.";
        } else {
            zzcgzVarZzyz = zzwE().zzyz();
            String strValueOf = String.valueOf(strZzhk);
            strConcat = strValueOf.length() != 0 ? "To enable faster debug mode event logging run:\n  adb shell setprop debug.firebase.analytics.app ".concat(strValueOf) : new String("To enable faster debug mode event logging run:\n  adb shell setprop debug.firebase.analytics.app ");
        }
        zzcgzVarZzyz.log(strConcat);
        zzwE().zzyA().log("Debug-level message logging enabled");
        zzcfz zzcfzVar = new zzcfz(this);
        zzcfzVar.initialize();
        this.zzbsB = zzcfzVar;
        zzcgt zzcgtVar = new zzcgt(this);
        zzcgtVar.initialize();
        this.zzbsC = zzcgtVar;
        zzcfv zzcfvVar = new zzcfv(this);
        zzcfvVar.initialize();
        this.zzbsL = zzcfvVar;
        this.zzbsM = new zzcfo(this);
        zzchb zzchbVar = new zzchb(this);
        zzchbVar.initialize();
        this.zzbsD = zzchbVar;
        zzcjl zzcjlVar = new zzcjl(this);
        zzcjlVar.initialize();
        this.zzbsE = zzcjlVar;
        zzcjp zzcjpVar = new zzcjp(this);
        zzcjpVar.initialize();
        this.zzbsF = zzcjpVar;
        zzcix zzcixVar = new zzcix(this);
        zzcixVar.initialize();
        this.zzbsH = zzcixVar;
        zzcks zzcksVar = new zzcks(this);
        zzcksVar.initialize();
        this.zzbsK = zzcksVar;
        this.zzbsJ = new zzchg(this);
        this.zzbsx = new AppMeasurement(this);
        this.zzbsy = new FirebaseAnalytics(this);
        zzckm zzckmVar = new zzckm(this);
        zzckmVar.initialize();
        this.zzbsv = zzckmVar;
        zzchr zzchrVar = new zzchr(this);
        zzchrVar.initialize();
        this.zzbsw = zzchrVar;
        zzchs zzchsVar = new zzchs(this);
        zzchsVar.initialize();
        this.zzbsu = zzchsVar;
        if (this.zzbsU != this.zzbsV) {
            zzwE().zzyv().zze("Not all components initialized", Integer.valueOf(this.zzbsU), Integer.valueOf(this.zzbsV));
        }
        this.zzafM = true;
        zzcfy.zzxD();
        if (this.mContext.getApplicationContext() instanceof Application) {
            zzcix zzcixVarZzws = zzws();
            if (zzcixVarZzws.getContext().getApplicationContext() instanceof Application) {
                Application application = (Application) zzcixVarZzws.getContext().getApplicationContext();
                if (zzcixVarZzws.zzbts == null) {
                    zzcixVarZzws.zzbts = new zzcjk(zzcixVarZzws, null);
                }
                application.unregisterActivityLifecycleCallbacks(zzcixVarZzws.zzbts);
                application.registerActivityLifecycleCallbacks(zzcixVarZzws.zzbts);
                zzcixVarZzws.zzwE().zzyB().log("Registered activity lifecycle callback");
            }
        } else {
            zzwE().zzyx().log("Application context is not an Application");
        }
        this.zzbsu.zzj(new zzchy(this));
    }

    @WorkerThread
    private final int zza(FileChannel fileChannel) {
        int i = 0;
        zzwD().zzjB();
        if (fileChannel == null || !fileChannel.isOpen()) {
            zzwE().zzyv().log("Bad chanel to read from");
        } else {
            ByteBuffer byteBufferAllocate = ByteBuffer.allocate(4);
            try {
                fileChannel.position(0L);
                int i2 = fileChannel.read(byteBufferAllocate);
                if (i2 == 4) {
                    byteBufferAllocate.flip();
                    i = byteBufferAllocate.getInt();
                } else if (i2 != -1) {
                    zzwE().zzyx().zzj("Unexpected data length. Bytes read", Integer.valueOf(i2));
                }
            } catch (IOException e) {
                zzwE().zzyv().zzj("Failed to read from channel", e);
            }
        }
        return i;
    }

    private final void zza(zzcgg zzcggVar, zzcft zzcftVar) {
        boolean z;
        zzwD().zzjB();
        zzkC();
        zzbr.zzu(zzcggVar);
        zzbr.zzu(zzcftVar);
        zzbr.zzcF(zzcggVar.mAppId);
        zzbr.zzaf(zzcggVar.mAppId.equals(zzcftVar.packageName));
        zzcll zzcllVar = new zzcll();
        zzcllVar.zzbvH = 1;
        zzcllVar.zzbvP = "android";
        zzcllVar.zzaK = zzcftVar.packageName;
        zzcllVar.zzboV = zzcftVar.zzboV;
        zzcllVar.zzbha = zzcftVar.zzbha;
        zzcllVar.zzbwc = zzcftVar.zzbpb == -2147483648L ? null : Integer.valueOf((int) zzcftVar.zzbpb);
        zzcllVar.zzbvT = Long.valueOf(zzcftVar.zzboW);
        zzcllVar.zzboU = zzcftVar.zzboU;
        zzcllVar.zzbvY = zzcftVar.zzboX == 0 ? null : Long.valueOf(zzcftVar.zzboX);
        Pair<String, Boolean> pairZzec = zzwF().zzec(zzcftVar.packageName);
        if (pairZzec != null && !TextUtils.isEmpty((CharSequence) pairZzec.first)) {
            zzcllVar.zzbvV = (String) pairZzec.first;
            zzcllVar.zzbvW = (Boolean) pairZzec.second;
        }
        zzwu().zzkC();
        zzcllVar.zzbvQ = Build.MODEL;
        zzwu().zzkC();
        zzcllVar.zzbb = Build.VERSION.RELEASE;
        zzcllVar.zzbvS = Integer.valueOf((int) zzwu().zzyo());
        zzcllVar.zzbvR = zzwu().zzyp();
        zzcllVar.zzbvU = null;
        zzcllVar.zzbvK = null;
        zzcllVar.zzbvL = null;
        zzcllVar.zzbvM = null;
        zzcllVar.zzbwg = Long.valueOf(zzcftVar.zzbpd);
        if (isEnabled() && zzcfy.zzya()) {
            zzwt();
            zzcllVar.zzbwh = null;
        }
        zzcfs zzcfsVarZzdR = zzwy().zzdR(zzcftVar.packageName);
        if (zzcfsVarZzdR == null) {
            zzcfsVarZzdR = new zzcfs(this, zzcftVar.packageName);
            zzcfsVarZzdR.zzdH(zzwt().zzys());
            zzcfsVarZzdR.zzdK(zzcftVar.zzbpc);
            zzcfsVarZzdR.zzdI(zzcftVar.zzboU);
            zzcfsVarZzdR.zzdJ(zzwF().zzed(zzcftVar.packageName));
            zzcfsVarZzdR.zzQ(0L);
            zzcfsVarZzdR.zzL(0L);
            zzcfsVarZzdR.zzM(0L);
            zzcfsVarZzdR.setAppVersion(zzcftVar.zzbha);
            zzcfsVarZzdR.zzN(zzcftVar.zzbpb);
            zzcfsVarZzdR.zzdL(zzcftVar.zzboV);
            zzcfsVarZzdR.zzO(zzcftVar.zzboW);
            zzcfsVarZzdR.zzP(zzcftVar.zzboX);
            zzcfsVarZzdR.setMeasurementEnabled(zzcftVar.zzboZ);
            zzcfsVarZzdR.zzZ(zzcftVar.zzbpd);
            zzwy().zza(zzcfsVarZzdR);
        }
        zzcllVar.zzbvX = zzcfsVarZzdR.getAppInstanceId();
        zzcllVar.zzbpc = zzcfsVarZzdR.zzwJ();
        List<zzckw> listZzdQ = zzwy().zzdQ(zzcftVar.packageName);
        zzcllVar.zzbvJ = new zzcln[listZzdQ.size()];
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= listZzdQ.size()) {
                try {
                    break;
                } catch (IOException e) {
                    zzwE().zzyv().zze("Data loss. Failed to insert raw event metadata. appId", zzcgx.zzea(zzcllVar.zzaK), e);
                    return;
                }
            } else {
                zzcln zzclnVar = new zzcln();
                zzcllVar.zzbvJ[i2] = zzclnVar;
                zzclnVar.name = listZzdQ.get(i2).mName;
                zzclnVar.zzbwl = Long.valueOf(listZzdQ.get(i2).zzbuG);
                zzwA().zza(zzclnVar, listZzdQ.get(i2).mValue);
                i = i2 + 1;
            }
        }
        long jZza = zzwy().zza(zzcllVar);
        zzcfz zzcfzVarZzwy = zzwy();
        if (zzcggVar.zzbpJ != null) {
            Iterator<String> it = zzcggVar.zzbpJ.iterator();
            while (true) {
                if (!it.hasNext()) {
                    boolean zZzO = zzwB().zzO(zzcggVar.mAppId, zzcggVar.mName);
                    zzcga zzcgaVarZza = zzwy().zza(zzyX(), zzcggVar.mAppId, false, false, false, false, false);
                    if (zZzO && zzcgaVarZza.zzbpC < this.zzbsr.zzdN(zzcggVar.mAppId)) {
                        z = true;
                    }
                } else if ("_r".equals(it.next())) {
                    z = true;
                    break;
                }
            }
        } else {
            z = false;
        }
        if (zzcfzVarZzwy.zza(zzcggVar, jZza, z)) {
            this.zzbsX = 0L;
        }
    }

    private static void zza(zzciu zzciuVar) {
        if (zzciuVar == null) {
            throw new IllegalStateException("Component not created");
        }
    }

    private static void zza(zzciv zzcivVar) {
        if (zzcivVar == null) {
            throw new IllegalStateException("Component not created");
        }
        if (!zzcivVar.isInitialized()) {
            throw new IllegalStateException("Component not initialized");
        }
    }

    @WorkerThread
    private final boolean zza(int i, FileChannel fileChannel) {
        zzwD().zzjB();
        if (fileChannel == null || !fileChannel.isOpen()) {
            zzwE().zzyv().log("Bad chanel to read from");
            return false;
        }
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(4);
        byteBufferAllocate.putInt(i);
        byteBufferAllocate.flip();
        try {
            fileChannel.truncate(0L);
            fileChannel.write(byteBufferAllocate);
            fileChannel.force(true);
            if (fileChannel.size() == 4) {
                return true;
            }
            zzwE().zzyv().zzj("Error writing to channel. Bytes written", Long.valueOf(fileChannel.size()));
            return true;
        } catch (IOException e) {
            zzwE().zzyv().zzj("Failed to write to channel", e);
            return false;
        }
    }

    private final zzclh[] zza(String str, zzcln[] zzclnVarArr, zzcli[] zzcliVarArr) {
        zzbr.zzcF(str);
        return zzwr().zza(str, zzcliVarArr, zzclnVarArr);
    }

    @WorkerThread
    private final void zzb(zzcfs zzcfsVar) {
        ArrayMap arrayMap = null;
        zzwD().zzjB();
        if (TextUtils.isEmpty(zzcfsVar.getGmpAppId())) {
            zzb(zzcfsVar.zzhk(), 204, null, null, null);
            return;
        }
        String gmpAppId = zzcfsVar.getGmpAppId();
        String appInstanceId = zzcfsVar.getAppInstanceId();
        Uri.Builder builder = new Uri.Builder();
        Uri.Builder builderEncodedAuthority = builder.scheme(zzcgn.zzbqd.get()).encodedAuthority(zzcgn.zzbqe.get());
        String strValueOf = String.valueOf(gmpAppId);
        builderEncodedAuthority.path(strValueOf.length() != 0 ? "config/app/".concat(strValueOf) : new String("config/app/")).appendQueryParameter("app_instance_id", appInstanceId).appendQueryParameter("platform", "android").appendQueryParameter("gmp_version", "11010");
        String string = builder.build().toString();
        try {
            URL url = new URL(string);
            zzwE().zzyB().zzj("Fetching remote configuration", zzcfsVar.zzhk());
            zzclf zzclfVarZzei = zzwB().zzei(zzcfsVar.zzhk());
            String strZzej = zzwB().zzej(zzcfsVar.zzhk());
            if (zzclfVarZzei != null && !TextUtils.isEmpty(strZzej)) {
                arrayMap = new ArrayMap();
                arrayMap.put("If-Modified-Since", strZzej);
            }
            this.zzbsY = true;
            zzyS().zza(zzcfsVar.zzhk(), url, arrayMap, new zzcib(this));
        } catch (MalformedURLException e) {
            zzwE().zzyv().zze("Failed to parse config URL. Not fetching. appId", zzcgx.zzea(zzcfsVar.zzhk()), string);
        }
    }

    public static zzchx zzbj(Context context) {
        zzbr.zzu(context);
        zzbr.zzu(context.getApplicationContext());
        if (zzbsq == null) {
            synchronized (zzchx.class) {
                if (zzbsq == null) {
                    zzbsq = new zzchx(new zzciw(context));
                }
            }
        }
        return zzbsq;
    }

    @WorkerThread
    private final void zzc(zzcgl zzcglVar, zzcft zzcftVar) {
        long jRound;
        zzckw zzckwVar;
        zzcgh zzcghVarZzab;
        zzcfs zzcfsVarZzdR;
        zzbr.zzu(zzcftVar);
        zzbr.zzcF(zzcftVar.packageName);
        long jNanoTime = System.nanoTime();
        zzwD().zzjB();
        zzkC();
        String str = zzcftVar.packageName;
        zzwA();
        if (zzckx.zzd(zzcglVar, zzcftVar)) {
            if (!zzcftVar.zzboZ) {
                zzf(zzcftVar);
                return;
            }
            if (zzwB().zzN(str, zzcglVar.name)) {
                zzwE().zzyx().zze("Dropping blacklisted event. appId", zzcgx.zzea(str), zzwz().zzdX(zzcglVar.name));
                boolean z = zzwA().zzeB(str) || zzwA().zzeC(str);
                if (!z && !"_err".equals(zzcglVar.name)) {
                    zzwA().zza(str, 11, "_ev", zzcglVar.name, 0);
                }
                if (!z || (zzcfsVarZzdR = zzwy().zzdR(str)) == null || Math.abs(this.zzvz.currentTimeMillis() - Math.max(zzcfsVarZzdR.zzwT(), zzcfsVarZzdR.zzwS())) <= zzcfy.zzxH()) {
                    return;
                }
                zzwE().zzyA().log("Fetching config for blacklisted app");
                zzb(zzcfsVarZzdR);
                return;
            }
            if (zzwE().zzz(2)) {
                zzwE().zzyB().zzj("Logging event", zzwz().zzb(zzcglVar));
            }
            zzwy().beginTransaction();
            try {
                Bundle bundleZzyr = zzcglVar.zzbpQ.zzyr();
                zzf(zzcftVar);
                if ("_iap".equals(zzcglVar.name) || FirebaseAnalytics.Event.ECOMMERCE_PURCHASE.equals(zzcglVar.name)) {
                    String string = bundleZzyr.getString(FirebaseAnalytics.Param.CURRENCY);
                    if (FirebaseAnalytics.Event.ECOMMERCE_PURCHASE.equals(zzcglVar.name)) {
                        double d = bundleZzyr.getDouble(FirebaseAnalytics.Param.VALUE) * 1000000.0d;
                        if (d == 0.0d) {
                            d = bundleZzyr.getLong(FirebaseAnalytics.Param.VALUE) * 1000000.0d;
                        }
                        if (d > 9.223372036854776E18d || d < -9.223372036854776E18d) {
                            zzwE().zzyx().zze("Data lost. Currency value is too big. appId", zzcgx.zzea(str), Double.valueOf(d));
                            zzwy().setTransactionSuccessful();
                            return;
                        }
                        jRound = Math.round(d);
                    } else {
                        jRound = bundleZzyr.getLong(FirebaseAnalytics.Param.VALUE);
                    }
                    if (!TextUtils.isEmpty(string)) {
                        String upperCase = string.toUpperCase(Locale.US);
                        if (upperCase.matches("[A-Z]{3}")) {
                            String strValueOf = String.valueOf("_ltv_");
                            String strValueOf2 = String.valueOf(upperCase);
                            String strConcat = strValueOf2.length() != 0 ? strValueOf.concat(strValueOf2) : new String(strValueOf);
                            zzckw zzckwVarZzG = zzwy().zzG(str, strConcat);
                            if (zzckwVarZzG == null || !(zzckwVarZzG.mValue instanceof Long)) {
                                zzcfz zzcfzVarZzwy = zzwy();
                                int iZzb = this.zzbsr.zzb(str, zzcgn.zzbqD) - 1;
                                zzbr.zzcF(str);
                                zzcfzVarZzwy.zzjB();
                                zzcfzVarZzwy.zzkC();
                                try {
                                    zzcfzVarZzwy.getWritableDatabase().execSQL("delete from user_attributes where app_id=? and name in (select name from user_attributes where app_id=? and name like '_ltv_%' order by set_timestamp desc limit ?,10);", new String[]{str, str, String.valueOf(iZzb)});
                                } catch (SQLiteException e) {
                                    zzcfzVarZzwy.zzwE().zzyv().zze("Error pruning currencies. appId", zzcgx.zzea(str), e);
                                }
                                zzckwVar = new zzckw(str, zzcglVar.zzbpg, strConcat, this.zzvz.currentTimeMillis(), Long.valueOf(jRound));
                            } else {
                                zzckwVar = new zzckw(str, zzcglVar.zzbpg, strConcat, this.zzvz.currentTimeMillis(), Long.valueOf(jRound + ((Long) zzckwVarZzG.mValue).longValue()));
                            }
                            if (!zzwy().zza(zzckwVar)) {
                                zzwE().zzyv().zzd("Too many unique user properties are set. Ignoring user property. appId", zzcgx.zzea(str), zzwz().zzdZ(zzckwVar.mName), zzckwVar.mValue);
                                zzwA().zza(str, 9, (String) null, (String) null, 0);
                            }
                        }
                    }
                }
                boolean zZzep = zzckx.zzep(zzcglVar.name);
                boolean zEquals = "_err".equals(zzcglVar.name);
                zzcga zzcgaVarZza = zzwy().zza(zzyX(), str, true, zZzep, false, zEquals, false);
                long jZzxp = zzcgaVarZza.zzbpz - zzcfy.zzxp();
                if (jZzxp > 0) {
                    if (jZzxp % 1000 == 1) {
                        zzwE().zzyv().zze("Data loss. Too many events logged. appId, count", zzcgx.zzea(str), Long.valueOf(zzcgaVarZza.zzbpz));
                    }
                    zzwy().setTransactionSuccessful();
                    return;
                }
                if (zZzep) {
                    long jZzxq = zzcgaVarZza.zzbpy - zzcfy.zzxq();
                    if (jZzxq > 0) {
                        if (jZzxq % 1000 == 1) {
                            zzwE().zzyv().zze("Data loss. Too many public events logged. appId, count", zzcgx.zzea(str), Long.valueOf(zzcgaVarZza.zzbpy));
                        }
                        zzwA().zza(str, 16, "_ev", zzcglVar.name, 0);
                        zzwy().setTransactionSuccessful();
                        return;
                    }
                }
                if (zEquals) {
                    long jMax = zzcgaVarZza.zzbpB - ((long) Math.max(0, Math.min(1000000, this.zzbsr.zzb(zzcftVar.packageName, zzcgn.zzbqk))));
                    if (jMax > 0) {
                        if (jMax == 1) {
                            zzwE().zzyv().zze("Too many error events logged. appId, count", zzcgx.zzea(str), Long.valueOf(zzcgaVarZza.zzbpB));
                        }
                        zzwy().setTransactionSuccessful();
                        return;
                    }
                }
                zzwA().zza(bundleZzyr, "_o", zzcglVar.zzbpg);
                if (zzwA().zzez(str)) {
                    zzwA().zza(bundleZzyr, "_dbg", (Object) 1L);
                    zzwA().zza(bundleZzyr, "_r", (Object) 1L);
                }
                long jZzdS = zzwy().zzdS(str);
                if (jZzdS > 0) {
                    zzwE().zzyx().zze("Data lost. Too many events stored on disk, deleted. appId", zzcgx.zzea(str), Long.valueOf(jZzdS));
                }
                zzcgg zzcggVar = new zzcgg(this, zzcglVar.zzbpg, str, zzcglVar.name, zzcglVar.zzbpR, 0L, bundleZzyr);
                zzcgh zzcghVarZzE = zzwy().zzE(str, zzcggVar.mName);
                if (zzcghVarZzE == null) {
                    long jZzdV = zzwy().zzdV(str);
                    zzcfy.zzxo();
                    if (jZzdV >= 500) {
                        zzwE().zzyv().zzd("Too many event names used, ignoring event. appId, name, supported count", zzcgx.zzea(str), zzwz().zzdX(zzcggVar.mName), Integer.valueOf(zzcfy.zzxo()));
                        zzwA().zza(str, 8, (String) null, (String) null, 0);
                        return;
                    }
                    zzcghVarZzab = new zzcgh(str, zzcggVar.mName, 0L, 0L, zzcggVar.zzayU);
                } else {
                    zzcggVar = zzcggVar.zza(this, zzcghVarZzE.zzbpM);
                    zzcghVarZzab = zzcghVarZzE.zzab(zzcggVar.zzayU);
                }
                zzwy().zza(zzcghVarZzab);
                zza(zzcggVar, zzcftVar);
                zzwy().setTransactionSuccessful();
                if (zzwE().zzz(2)) {
                    zzwE().zzyB().zzj("Event recorded", zzwz().zza(zzcggVar));
                }
                zzwy().endTransaction();
                zzza();
                zzwE().zzyB().zzj("Background event processing time, ms", Long.valueOf(((System.nanoTime() - jNanoTime) + 500000) / 1000000));
            } finally {
                zzwy().endTransaction();
            }
        }
    }

    @WorkerThread
    private final zzcft zzem(String str) {
        zzcfs zzcfsVarZzdR = zzwy().zzdR(str);
        if (zzcfsVarZzdR == null || TextUtils.isEmpty(zzcfsVarZzdR.zzjG())) {
            zzwE().zzyA().zzj("No app data available; dropping", str);
            return null;
        }
        try {
            String str2 = zzbim.zzaP(this.mContext).getPackageInfo(str, 0).versionName;
            if (zzcfsVarZzdR.zzjG() != null && !zzcfsVarZzdR.zzjG().equals(str2)) {
                zzwE().zzyx().zzj("App version does not match; dropping. appId", zzcgx.zzea(str));
                return null;
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
        return new zzcft(str, zzcfsVarZzdR.getGmpAppId(), zzcfsVarZzdR.zzjG(), zzcfsVarZzdR.zzwM(), zzcfsVarZzdR.zzwN(), zzcfsVarZzdR.zzwO(), zzcfsVarZzdR.zzwP(), (String) null, zzcfsVarZzdR.zzwQ(), false, zzcfsVarZzdR.zzwJ(), zzcfsVarZzdR.zzxd(), 0L, 0);
    }

    @WorkerThread
    private final void zzf(zzcft zzcftVar) {
        boolean z = true;
        zzwD().zzjB();
        zzkC();
        zzbr.zzu(zzcftVar);
        zzbr.zzcF(zzcftVar.packageName);
        zzcfs zzcfsVarZzdR = zzwy().zzdR(zzcftVar.packageName);
        String strZzed = zzwF().zzed(zzcftVar.packageName);
        boolean z2 = false;
        if (zzcfsVarZzdR == null) {
            zzcfs zzcfsVar = new zzcfs(this, zzcftVar.packageName);
            zzcfsVar.zzdH(zzwt().zzys());
            zzcfsVar.zzdJ(strZzed);
            zzcfsVarZzdR = zzcfsVar;
            z2 = true;
        } else if (!strZzed.equals(zzcfsVarZzdR.zzwI())) {
            zzcfsVarZzdR.zzdJ(strZzed);
            zzcfsVarZzdR.zzdH(zzwt().zzys());
            z2 = true;
        }
        if (!TextUtils.isEmpty(zzcftVar.zzboU) && !zzcftVar.zzboU.equals(zzcfsVarZzdR.getGmpAppId())) {
            zzcfsVarZzdR.zzdI(zzcftVar.zzboU);
            z2 = true;
        }
        if (!TextUtils.isEmpty(zzcftVar.zzbpc) && !zzcftVar.zzbpc.equals(zzcfsVarZzdR.zzwJ())) {
            zzcfsVarZzdR.zzdK(zzcftVar.zzbpc);
            z2 = true;
        }
        if (zzcftVar.zzboW != 0 && zzcftVar.zzboW != zzcfsVarZzdR.zzwO()) {
            zzcfsVarZzdR.zzO(zzcftVar.zzboW);
            z2 = true;
        }
        if (!TextUtils.isEmpty(zzcftVar.zzbha) && !zzcftVar.zzbha.equals(zzcfsVarZzdR.zzjG())) {
            zzcfsVarZzdR.setAppVersion(zzcftVar.zzbha);
            z2 = true;
        }
        if (zzcftVar.zzbpb != zzcfsVarZzdR.zzwM()) {
            zzcfsVarZzdR.zzN(zzcftVar.zzbpb);
            z2 = true;
        }
        if (zzcftVar.zzboV != null && !zzcftVar.zzboV.equals(zzcfsVarZzdR.zzwN())) {
            zzcfsVarZzdR.zzdL(zzcftVar.zzboV);
            z2 = true;
        }
        if (zzcftVar.zzboX != zzcfsVarZzdR.zzwP()) {
            zzcfsVarZzdR.zzP(zzcftVar.zzboX);
            z2 = true;
        }
        if (zzcftVar.zzboZ != zzcfsVarZzdR.zzwQ()) {
            zzcfsVarZzdR.setMeasurementEnabled(zzcftVar.zzboZ);
            z2 = true;
        }
        if (!TextUtils.isEmpty(zzcftVar.zzboY) && !zzcftVar.zzboY.equals(zzcfsVarZzdR.zzxb())) {
            zzcfsVarZzdR.zzdM(zzcftVar.zzboY);
            z2 = true;
        }
        if (zzcftVar.zzbpd != zzcfsVarZzdR.zzxd()) {
            zzcfsVarZzdR.zzZ(zzcftVar.zzbpd);
        } else {
            z = z2;
        }
        if (z) {
            zzwy().zza(zzcfsVarZzdR);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:124:0x0366 A[Catch: all -> 0x019d, TryCatch #6 {all -> 0x019d, blocks: (B:3:0x0007, B:16:0x007e, B:17:0x0081, B:19:0x0085, B:23:0x0090, B:24:0x00a4, B:26:0x00ac, B:28:0x00c4, B:30:0x00f9, B:34:0x010a, B:36:0x011c, B:120:0x033b, B:122:0x0353, B:179:0x058e, B:124:0x0366, B:126:0x0374, B:127:0x0381, B:129:0x0390, B:131:0x039c, B:132:0x03a7, B:133:0x03ac, B:135:0x03b6, B:138:0x03c4, B:140:0x0422, B:141:0x047e, B:143:0x04a7, B:144:0x04b0, B:146:0x04b5, B:148:0x04c3, B:150:0x04cc, B:151:0x04d3, B:153:0x04d6, B:154:0x04df, B:166:0x0556, B:155:0x04e1, B:158:0x04f3, B:160:0x051d, B:162:0x0543, B:165:0x0550, B:167:0x055a, B:172:0x056f, B:174:0x057e, B:176:0x0582, B:177:0x0586, B:178:0x058b, B:181:0x05a4, B:182:0x05b2, B:184:0x05c9, B:186:0x05d1, B:187:0x05df, B:188:0x060d, B:190:0x0614, B:192:0x062c, B:193:0x0632, B:195:0x0644, B:196:0x064a, B:197:0x064d, B:199:0x065b, B:200:0x0670, B:202:0x0677, B:204:0x0688, B:231:0x0762, B:209:0x06a0, B:206:0x068c, B:208:0x0696, B:230:0x074b, B:210:0x06a9, B:211:0x06ba, B:212:0x06c8, B:233:0x076b, B:217:0x06df, B:219:0x06e6, B:221:0x06f0, B:222:0x06f4, B:226:0x0708, B:227:0x070c, B:235:0x0781, B:48:0x0198, B:84:0x0293, B:106:0x030a, B:113:0x0329, B:97:0x02d3, B:89:0x02ac, B:116:0x0331, B:117:0x0334, B:63:0x01f2, B:72:0x0220), top: B:257:0x0007, inners: #1, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:183:0x05c7 A[PHI: r13
      0x05c7: PHI (r13v4 boolean) = (r13v3 boolean), (r13v3 boolean), (r13v3 boolean), (r13v3 boolean), (r13v1 boolean) binds: [B:156:0x04ef, B:157:0x04f1, B:159:0x051b, B:182:0x05b2, B:123:0x0364] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0185 A[Catch: SQLiteException -> 0x02bd, all -> 0x0792, TRY_LEAVE, TryCatch #9 {SQLiteException -> 0x02bd, all -> 0x0792, blocks: (B:44:0x0160, B:46:0x0185, B:71:0x0211, B:72:0x0220, B:73:0x0223, B:75:0x0229, B:76:0x023a, B:78:0x0246, B:79:0x0258, B:91:0x02b1, B:87:0x0299), top: B:258:0x0160 }] */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0210  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final boolean zzg(java.lang.String r21, long r22) {
        /*
            Method dump skipped, instruction units count: 1969
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzchx.zzg(java.lang.String, long):boolean");
    }

    static void zzwn() {
        zzcfy.zzxD();
        throw new IllegalStateException("Unexpected call on client side");
    }

    private final zzchg zzyT() {
        if (this.zzbsJ == null) {
            throw new IllegalStateException("Network broadcast receiver not created");
        }
        return this.zzbsJ;
    }

    private final zzcks zzyU() {
        zza((zzciv) this.zzbsK);
        return this.zzbsK;
    }

    @WorkerThread
    private final boolean zzyV() {
        zzwD().zzjB();
        try {
            this.zzbsR = new RandomAccessFile(new File(this.mContext.getFilesDir(), zzcfy.zzxB()), "rw").getChannel();
            this.zzbsQ = this.zzbsR.tryLock();
        } catch (FileNotFoundException e) {
            zzwE().zzyv().zzj("Failed to acquire storage lock", e);
        } catch (IOException e2) {
            zzwE().zzyv().zzj("Failed to access storage lock file", e2);
        }
        if (this.zzbsQ != null) {
            zzwE().zzyB().log("Storage concurrent access okay");
            return true;
        }
        zzwE().zzyv().log("Storage concurrent data access panic");
        return false;
    }

    private final long zzyX() {
        long jCurrentTimeMillis = this.zzvz.currentTimeMillis();
        zzchi zzchiVarZzwF = zzwF();
        zzchiVarZzwF.zzkC();
        zzchiVarZzwF.zzjB();
        long jNextInt = zzchiVarZzwF.zzbrs.get();
        if (jNextInt == 0) {
            jNextInt = zzchiVarZzwF.zzwA().zzzr().nextInt(86400000) + 1;
            zzchiVarZzwF.zzbrs.set(jNextInt);
        }
        return ((((jNextInt + jCurrentTimeMillis) / 1000) / 60) / 60) / 24;
    }

    private final boolean zzyZ() {
        zzwD().zzjB();
        zzkC();
        return zzwy().zzyg() || !TextUtils.isEmpty(zzwy().zzyb());
    }

    @WorkerThread
    private final void zzza() {
        long jZzxQ;
        long jMax;
        zzwD().zzjB();
        zzkC();
        if (zzzd()) {
            if (this.zzbsX > 0) {
                long jAbs = 3600000 - Math.abs(this.zzvz.elapsedRealtime() - this.zzbsX);
                if (jAbs > 0) {
                    zzwE().zzyB().zzj("Upload has been suspended. Will update scheduling later in approximately ms", Long.valueOf(jAbs));
                    zzyT().unregister();
                    zzyU().cancel();
                    return;
                }
                this.zzbsX = 0L;
            }
            if (!zzyN() || !zzyZ()) {
                zzwE().zzyB().log("Nothing to upload or uploading impossible");
                zzyT().unregister();
                zzyU().cancel();
                return;
            }
            long jCurrentTimeMillis = this.zzvz.currentTimeMillis();
            long jZzxW = zzcfy.zzxW();
            boolean z = zzwy().zzyh() || zzwy().zzyc();
            if (z) {
                String strZzxZ = this.zzbsr.zzxZ();
                jZzxQ = (TextUtils.isEmpty(strZzxZ) || ".none.".equals(strZzxZ)) ? zzcfy.zzxR() : zzcfy.zzxS();
            } else {
                jZzxQ = zzcfy.zzxQ();
            }
            long j = zzwF().zzbro.get();
            long j2 = zzwF().zzbrp.get();
            long jMax2 = Math.max(zzwy().zzye(), zzwy().zzyf());
            if (jMax2 != 0) {
                long jAbs2 = jCurrentTimeMillis - Math.abs(jMax2 - jCurrentTimeMillis);
                long jAbs3 = jCurrentTimeMillis - Math.abs(j - jCurrentTimeMillis);
                long jAbs4 = jCurrentTimeMillis - Math.abs(j2 - jCurrentTimeMillis);
                long jMax3 = Math.max(jAbs3, jAbs4);
                long jZzxX = jAbs2 + jZzxW;
                if (z && jMax3 > 0) {
                    jZzxX = Math.min(jAbs2, jMax3) + jZzxQ;
                }
                if (!zzwA().zzf(jMax3, jZzxQ)) {
                    jZzxX = jMax3 + jZzxQ;
                }
                if (jAbs4 != 0 && jAbs4 >= jAbs2) {
                    int i = 0;
                    while (true) {
                        if (i >= zzcfy.zzxY()) {
                            jMax = 0;
                            break;
                        }
                        jZzxX += ((long) (1 << i)) * zzcfy.zzxX();
                        if (jZzxX > jAbs4) {
                            jMax = jZzxX;
                            break;
                        }
                        i++;
                    }
                } else {
                    jMax = jZzxX;
                }
            } else {
                jMax = 0;
            }
            if (jMax == 0) {
                zzwE().zzyB().log("Next upload time is 0");
                zzyT().unregister();
                zzyU().cancel();
                return;
            }
            if (!zzyS().zzlP()) {
                zzwE().zzyB().log("No network");
                zzyT().zzlM();
                zzyU().cancel();
                return;
            }
            long j3 = zzwF().zzbrq.get();
            long jZzxP = zzcfy.zzxP();
            if (!zzwA().zzf(j3, jZzxP)) {
                jMax = Math.max(jMax, j3 + jZzxP);
            }
            zzyT().unregister();
            long jCurrentTimeMillis2 = jMax - this.zzvz.currentTimeMillis();
            if (jCurrentTimeMillis2 <= 0) {
                jCurrentTimeMillis2 = zzcfy.zzxT();
                zzwF().zzbro.set(this.zzvz.currentTimeMillis());
            }
            zzwE().zzyB().zzj("Upload scheduled in approximately ms", Long.valueOf(jCurrentTimeMillis2));
            zzyU().zzs(jCurrentTimeMillis2);
        }
    }

    @WorkerThread
    private final boolean zzzd() {
        zzwD().zzjB();
        zzkC();
        return this.zzbsN;
    }

    @WorkerThread
    private final void zzze() {
        zzwD().zzjB();
        if (this.zzbsY || this.zzbsZ || this.zzbta) {
            zzwE().zzyB().zzd("Not stopping services. fetch, network, upload", Boolean.valueOf(this.zzbsY), Boolean.valueOf(this.zzbsZ), Boolean.valueOf(this.zzbta));
            return;
        }
        zzwE().zzyB().log("Stopping uploading service(s)");
        if (this.zzbsT != null) {
            Iterator<Runnable> it = this.zzbsT.iterator();
            while (it.hasNext()) {
                it.next().run();
            }
            this.zzbsT.clear();
        }
    }

    public final Context getContext() {
        return this.mContext;
    }

    @WorkerThread
    public final boolean isEnabled() {
        boolean zBooleanValue = false;
        zzwD().zzjB();
        zzkC();
        if (this.zzbsr.zzxE()) {
            return false;
        }
        Boolean boolZzdO = this.zzbsr.zzdO("firebase_analytics_collection_enabled");
        if (boolZzdO != null) {
            zBooleanValue = boolZzdO.booleanValue();
        } else if (!zzcfy.zzqz()) {
            zBooleanValue = true;
        }
        return zzwF().zzal(zBooleanValue);
    }

    @WorkerThread
    protected final void start() {
        zzwD().zzjB();
        zzwy().zzyd();
        if (zzwF().zzbro.get() == 0) {
            zzwF().zzbro.set(this.zzvz.currentTimeMillis());
        }
        if (Long.valueOf(zzwF().zzbrt.get()).longValue() == 0) {
            zzwE().zzyB().zzj("Persisting first open", Long.valueOf(this.zzbtb));
            zzwF().zzbrt.set(this.zzbtb);
        }
        if (zzyN()) {
            zzcfy.zzxD();
            if (!TextUtils.isEmpty(zzwt().getGmpAppId())) {
                String strZzyE = zzwF().zzyE();
                if (strZzyE == null) {
                    zzwF().zzee(zzwt().getGmpAppId());
                } else if (!strZzyE.equals(zzwt().getGmpAppId())) {
                    zzwE().zzyz().log("Rechecking which service to use due to a GMP App Id change");
                    zzwF().zzyH();
                    this.zzbsF.disconnect();
                    this.zzbsF.zzkZ();
                    zzwF().zzee(zzwt().getGmpAppId());
                    zzwF().zzbrt.set(this.zzbtb);
                    zzwF().zzbru.zzeg(null);
                }
            }
            zzws().zzef(zzwF().zzbru.zzyJ());
            zzcfy.zzxD();
            if (!TextUtils.isEmpty(zzwt().getGmpAppId())) {
                zzcix zzcixVarZzws = zzws();
                zzcixVarZzws.zzjB();
                zzcixVarZzws.zzwo();
                zzcixVarZzws.zzkC();
                if (zzcixVarZzws.zzboi.zzyN()) {
                    zzcixVarZzws.zzwv().zzzi();
                    String strZzyI = zzcixVarZzws.zzwF().zzyI();
                    if (!TextUtils.isEmpty(strZzyI)) {
                        zzcixVarZzws.zzwu().zzkC();
                        if (!strZzyI.equals(Build.VERSION.RELEASE)) {
                            Bundle bundle = new Bundle();
                            bundle.putString("_po", strZzyI);
                            zzcixVarZzws.zzd("auto", "_ou", bundle);
                        }
                    }
                }
                zzwv().zza(new AtomicReference<>());
            }
        } else if (isEnabled()) {
            if (!zzwA().zzbv("android.permission.INTERNET")) {
                zzwE().zzyv().log("App is missing INTERNET permission");
            }
            if (!zzwA().zzbv("android.permission.ACCESS_NETWORK_STATE")) {
                zzwE().zzyv().log("App is missing ACCESS_NETWORK_STATE permission");
            }
            zzcfy.zzxD();
            if (!zzbim.zzaP(this.mContext).zzsk()) {
                if (!zzcho.zzj(this.mContext, false)) {
                    zzwE().zzyv().log("AppMeasurementReceiver not registered/enabled");
                }
                if (!zzcki.zzk(this.mContext, false)) {
                    zzwE().zzyv().log("AppMeasurementService not registered/enabled");
                }
            }
            zzwE().zzyv().log("Uploading is not possible. App measurement disabled");
        }
        zzza();
    }

    @WorkerThread
    protected final void zza(int i, Throwable th, byte[] bArr) {
        zzwD().zzjB();
        zzkC();
        if (bArr == null) {
            try {
                bArr = new byte[0];
            } finally {
                this.zzbsZ = false;
                zzze();
            }
        }
        List<Long> list = this.zzbsS;
        this.zzbsS = null;
        if ((i == 200 || i == 204) && th == null) {
            try {
                zzwF().zzbro.set(this.zzvz.currentTimeMillis());
                zzwF().zzbrp.set(0L);
                zzza();
                zzwE().zzyB().zze("Successful upload. Got network response. code, size", Integer.valueOf(i), Integer.valueOf(bArr.length));
                zzwy().beginTransaction();
                try {
                    for (Long l : list) {
                        zzcfz zzcfzVarZzwy = zzwy();
                        long jLongValue = l.longValue();
                        zzcfzVarZzwy.zzjB();
                        zzcfzVarZzwy.zzkC();
                        try {
                            if (zzcfzVarZzwy.getWritableDatabase().delete("queue", "rowid=?", new String[]{String.valueOf(jLongValue)}) != 1) {
                                throw new SQLiteException("Deleted fewer rows from queue than expected");
                            }
                        } catch (SQLiteException e) {
                            zzcfzVarZzwy.zzwE().zzyv().zzj("Failed to delete a bundle in a queue table", e);
                            throw e;
                        }
                    }
                    zzwy().setTransactionSuccessful();
                    zzwy().endTransaction();
                    if (zzyS().zzlP() && zzyZ()) {
                        zzyY();
                    } else {
                        this.zzbsW = -1L;
                        zzza();
                    }
                    this.zzbsX = 0L;
                } catch (Throwable th2) {
                    zzwy().endTransaction();
                    throw th2;
                }
            } catch (SQLiteException e2) {
                zzwE().zzyv().zzj("Database error while trying to delete uploaded bundles", e2);
                this.zzbsX = this.zzvz.elapsedRealtime();
                zzwE().zzyB().zzj("Disable upload, time", Long.valueOf(this.zzbsX));
            }
        } else {
            zzwE().zzyB().zze("Network upload failed. Will retry later. code, error", Integer.valueOf(i), th);
            zzwF().zzbrp.set(this.zzvz.currentTimeMillis());
            if (i == 503 || i == 429) {
                zzwF().zzbrq.set(this.zzvz.currentTimeMillis());
            }
            zzza();
        }
    }

    @WorkerThread
    public final byte[] zza(@NonNull zzcgl zzcglVar, @Size(min = 1) String str) {
        long j;
        zzkC();
        zzwD().zzjB();
        zzwn();
        zzbr.zzu(zzcglVar);
        zzbr.zzcF(str);
        zzclk zzclkVar = new zzclk();
        zzwy().beginTransaction();
        try {
            zzcfs zzcfsVarZzdR = zzwy().zzdR(str);
            if (zzcfsVarZzdR == null) {
                zzwE().zzyA().zzj("Log and bundle not available. package_name", str);
                return new byte[0];
            }
            if (!zzcfsVarZzdR.zzwQ()) {
                zzwE().zzyA().zzj("Log and bundle disabled. package_name", str);
                return new byte[0];
            }
            zzcll zzcllVar = new zzcll();
            zzclkVar.zzbvF = new zzcll[]{zzcllVar};
            zzcllVar.zzbvH = 1;
            zzcllVar.zzbvP = "android";
            zzcllVar.zzaK = zzcfsVarZzdR.zzhk();
            zzcllVar.zzboV = zzcfsVarZzdR.zzwN();
            zzcllVar.zzbha = zzcfsVarZzdR.zzjG();
            long jZzwM = zzcfsVarZzdR.zzwM();
            zzcllVar.zzbwc = jZzwM == -2147483648L ? null : Integer.valueOf((int) jZzwM);
            zzcllVar.zzbvT = Long.valueOf(zzcfsVarZzdR.zzwO());
            zzcllVar.zzboU = zzcfsVarZzdR.getGmpAppId();
            zzcllVar.zzbvY = Long.valueOf(zzcfsVarZzdR.zzwP());
            if (isEnabled() && zzcfy.zzya() && this.zzbsr.zzdP(zzcllVar.zzaK)) {
                zzwt();
                zzcllVar.zzbwh = null;
            }
            Pair<String, Boolean> pairZzec = zzwF().zzec(zzcfsVarZzdR.zzhk());
            if (pairZzec != null && !TextUtils.isEmpty((CharSequence) pairZzec.first)) {
                zzcllVar.zzbvV = (String) pairZzec.first;
                zzcllVar.zzbvW = (Boolean) pairZzec.second;
            }
            zzwu().zzkC();
            zzcllVar.zzbvQ = Build.MODEL;
            zzwu().zzkC();
            zzcllVar.zzbb = Build.VERSION.RELEASE;
            zzcllVar.zzbvS = Integer.valueOf((int) zzwu().zzyo());
            zzcllVar.zzbvR = zzwu().zzyp();
            zzcllVar.zzbvX = zzcfsVarZzdR.getAppInstanceId();
            zzcllVar.zzbpc = zzcfsVarZzdR.zzwJ();
            List<zzckw> listZzdQ = zzwy().zzdQ(zzcfsVarZzdR.zzhk());
            zzcllVar.zzbvJ = new zzcln[listZzdQ.size()];
            for (int i = 0; i < listZzdQ.size(); i++) {
                zzcln zzclnVar = new zzcln();
                zzcllVar.zzbvJ[i] = zzclnVar;
                zzclnVar.name = listZzdQ.get(i).mName;
                zzclnVar.zzbwl = Long.valueOf(listZzdQ.get(i).zzbuG);
                zzwA().zza(zzclnVar, listZzdQ.get(i).mValue);
            }
            Bundle bundleZzyr = zzcglVar.zzbpQ.zzyr();
            if ("_iap".equals(zzcglVar.name)) {
                bundleZzyr.putLong("_c", 1L);
                zzwE().zzyA().log("Marking in-app purchase as real-time");
                bundleZzyr.putLong("_r", 1L);
            }
            bundleZzyr.putString("_o", zzcglVar.zzbpg);
            if (zzwA().zzez(zzcllVar.zzaK)) {
                zzwA().zza(bundleZzyr, "_dbg", (Object) 1L);
                zzwA().zza(bundleZzyr, "_r", (Object) 1L);
            }
            zzcgh zzcghVarZzE = zzwy().zzE(str, zzcglVar.name);
            if (zzcghVarZzE == null) {
                zzwy().zza(new zzcgh(str, zzcglVar.name, 1L, 0L, zzcglVar.zzbpR));
                j = 0;
            } else {
                j = zzcghVarZzE.zzbpM;
                zzwy().zza(zzcghVarZzE.zzab(zzcglVar.zzbpR).zzyq());
            }
            zzcgg zzcggVar = new zzcgg(this, zzcglVar.zzbpg, str, zzcglVar.name, zzcglVar.zzbpR, j, bundleZzyr);
            zzcli zzcliVar = new zzcli();
            zzcllVar.zzbvI = new zzcli[]{zzcliVar};
            zzcliVar.zzbvB = Long.valueOf(zzcggVar.zzayU);
            zzcliVar.name = zzcggVar.mName;
            zzcliVar.zzbvC = Long.valueOf(zzcggVar.zzbpI);
            zzcliVar.zzbvA = new zzclj[zzcggVar.zzbpJ.size()];
            int i2 = 0;
            for (String str2 : zzcggVar.zzbpJ) {
                zzclj zzcljVar = new zzclj();
                zzcliVar.zzbvA[i2] = zzcljVar;
                zzcljVar.name = str2;
                zzwA().zza(zzcljVar, zzcggVar.zzbpJ.get(str2));
                i2++;
            }
            zzcllVar.zzbwb = zza(zzcfsVarZzdR.zzhk(), zzcllVar.zzbvJ, zzcllVar.zzbvI);
            zzcllVar.zzbvL = zzcliVar.zzbvB;
            zzcllVar.zzbvM = zzcliVar.zzbvB;
            long jZzwL = zzcfsVarZzdR.zzwL();
            zzcllVar.zzbvO = jZzwL != 0 ? Long.valueOf(jZzwL) : null;
            long jZzwK = zzcfsVarZzdR.zzwK();
            if (jZzwK != 0) {
                jZzwL = jZzwK;
            }
            zzcllVar.zzbvN = jZzwL != 0 ? Long.valueOf(jZzwL) : null;
            zzcfsVarZzdR.zzwU();
            zzcllVar.zzbvZ = Integer.valueOf((int) zzcfsVarZzdR.zzwR());
            zzcllVar.zzbvU = Long.valueOf(zzcfy.zzwO());
            zzcllVar.zzbvK = Long.valueOf(this.zzvz.currentTimeMillis());
            zzcllVar.zzbwa = Boolean.TRUE;
            zzcfsVarZzdR.zzL(zzcllVar.zzbvL.longValue());
            zzcfsVarZzdR.zzM(zzcllVar.zzbvM.longValue());
            zzwy().zza(zzcfsVarZzdR);
            zzwy().setTransactionSuccessful();
            try {
                byte[] bArr = new byte[zzclkVar.zzMl()];
                ahx ahxVarZzc = ahx.zzc(bArr, 0, bArr.length);
                zzclkVar.zza(ahxVarZzc);
                ahxVarZzc.zzMc();
                return zzwA().zzm(bArr);
            } catch (IOException e) {
                zzwE().zzyv().zze("Data loss. Failed to bundle and serialize. appId", zzcgx.zzea(str), e);
                return null;
            }
        } finally {
            zzwy().endTransaction();
        }
    }

    public final void zzam(boolean z) {
        zzza();
    }

    @WorkerThread
    final void zzb(zzcfw zzcfwVar, zzcft zzcftVar) {
        boolean z = true;
        zzbr.zzu(zzcfwVar);
        zzbr.zzcF(zzcfwVar.packageName);
        zzbr.zzu(zzcfwVar.zzbpg);
        zzbr.zzu(zzcfwVar.zzbph);
        zzbr.zzcF(zzcfwVar.zzbph.name);
        zzwD().zzjB();
        zzkC();
        if (TextUtils.isEmpty(zzcftVar.zzboU)) {
            return;
        }
        if (!zzcftVar.zzboZ) {
            zzf(zzcftVar);
            return;
        }
        zzcfw zzcfwVar2 = new zzcfw(zzcfwVar);
        zzcfwVar2.zzbpj = false;
        zzwy().beginTransaction();
        try {
            zzcfw zzcfwVarZzH = zzwy().zzH(zzcfwVar2.packageName, zzcfwVar2.zzbph.name);
            if (zzcfwVarZzH != null && !zzcfwVarZzH.zzbpg.equals(zzcfwVar2.zzbpg)) {
                zzwE().zzyx().zzd("Updating a conditional user property with different origin. name, origin, origin (from DB)", zzwz().zzdZ(zzcfwVar2.zzbph.name), zzcfwVar2.zzbpg, zzcfwVarZzH.zzbpg);
            }
            if (zzcfwVarZzH != null && zzcfwVarZzH.zzbpj) {
                zzcfwVar2.zzbpg = zzcfwVarZzH.zzbpg;
                zzcfwVar2.zzbpi = zzcfwVarZzH.zzbpi;
                zzcfwVar2.zzbpm = zzcfwVarZzH.zzbpm;
                zzcfwVar2.zzbpk = zzcfwVarZzH.zzbpk;
                zzcfwVar2.zzbpn = zzcfwVarZzH.zzbpn;
                zzcfwVar2.zzbpj = zzcfwVarZzH.zzbpj;
                zzcfwVar2.zzbph = new zzcku(zzcfwVar2.zzbph.name, zzcfwVarZzH.zzbph.zzbuC, zzcfwVar2.zzbph.getValue(), zzcfwVarZzH.zzbph.zzbpg);
                z = false;
            } else if (TextUtils.isEmpty(zzcfwVar2.zzbpk)) {
                zzcfwVar2.zzbph = new zzcku(zzcfwVar2.zzbph.name, zzcfwVar2.zzbpi, zzcfwVar2.zzbph.getValue(), zzcfwVar2.zzbph.zzbpg);
                zzcfwVar2.zzbpj = true;
            } else {
                z = false;
            }
            if (zzcfwVar2.zzbpj) {
                zzcku zzckuVar = zzcfwVar2.zzbph;
                zzckw zzckwVar = new zzckw(zzcfwVar2.packageName, zzcfwVar2.zzbpg, zzckuVar.name, zzckuVar.zzbuC, zzckuVar.getValue());
                if (zzwy().zza(zzckwVar)) {
                    zzwE().zzyA().zzd("User property updated immediately", zzcfwVar2.packageName, zzwz().zzdZ(zzckwVar.mName), zzckwVar.mValue);
                } else {
                    zzwE().zzyv().zzd("(2)Too many active user properties, ignoring", zzcgx.zzea(zzcfwVar2.packageName), zzwz().zzdZ(zzckwVar.mName), zzckwVar.mValue);
                }
                if (z && zzcfwVar2.zzbpn != null) {
                    zzc(new zzcgl(zzcfwVar2.zzbpn, zzcfwVar2.zzbpi), zzcftVar);
                }
            }
            if (zzwy().zza(zzcfwVar2)) {
                zzwE().zzyA().zzd("Conditional property added", zzcfwVar2.packageName, zzwz().zzdZ(zzcfwVar2.zzbph.name), zzcfwVar2.zzbph.getValue());
            } else {
                zzwE().zzyv().zzd("Too many conditional properties, ignoring", zzcgx.zzea(zzcfwVar2.packageName), zzwz().zzdZ(zzcfwVar2.zzbph.name), zzcfwVar2.zzbph.getValue());
            }
            zzwy().setTransactionSuccessful();
        } finally {
            zzwy().endTransaction();
        }
    }

    @WorkerThread
    final void zzb(zzcgl zzcglVar, zzcft zzcftVar) {
        List<zzcfw> listZzc;
        List<zzcfw> listZzc2;
        List<zzcfw> listZzc3;
        zzbr.zzu(zzcftVar);
        zzbr.zzcF(zzcftVar.packageName);
        zzwD().zzjB();
        zzkC();
        String str = zzcftVar.packageName;
        long j = zzcglVar.zzbpR;
        zzwA();
        if (zzckx.zzd(zzcglVar, zzcftVar)) {
            if (!zzcftVar.zzboZ) {
                zzf(zzcftVar);
                return;
            }
            zzwy().beginTransaction();
            try {
                zzcfz zzcfzVarZzwy = zzwy();
                zzbr.zzcF(str);
                zzcfzVarZzwy.zzjB();
                zzcfzVarZzwy.zzkC();
                if (j < 0) {
                    zzcfzVarZzwy.zzwE().zzyx().zze("Invalid time querying timed out conditional properties", zzcgx.zzea(str), Long.valueOf(j));
                    listZzc = Collections.emptyList();
                } else {
                    listZzc = zzcfzVarZzwy.zzc("active=0 and app_id=? and abs(? - creation_timestamp) > trigger_timeout", new String[]{str, String.valueOf(j)});
                }
                for (zzcfw zzcfwVar : listZzc) {
                    if (zzcfwVar != null) {
                        zzwE().zzyA().zzd("User property timed out", zzcfwVar.packageName, zzwz().zzdZ(zzcfwVar.zzbph.name), zzcfwVar.zzbph.getValue());
                        if (zzcfwVar.zzbpl != null) {
                            zzc(new zzcgl(zzcfwVar.zzbpl, j), zzcftVar);
                        }
                        zzwy().zzI(str, zzcfwVar.zzbph.name);
                    }
                }
                zzcfz zzcfzVarZzwy2 = zzwy();
                zzbr.zzcF(str);
                zzcfzVarZzwy2.zzjB();
                zzcfzVarZzwy2.zzkC();
                if (j < 0) {
                    zzcfzVarZzwy2.zzwE().zzyx().zze("Invalid time querying expired conditional properties", zzcgx.zzea(str), Long.valueOf(j));
                    listZzc2 = Collections.emptyList();
                } else {
                    listZzc2 = zzcfzVarZzwy2.zzc("active<>0 and app_id=? and abs(? - triggered_timestamp) > time_to_live", new String[]{str, String.valueOf(j)});
                }
                ArrayList arrayList = new ArrayList(listZzc2.size());
                for (zzcfw zzcfwVar2 : listZzc2) {
                    if (zzcfwVar2 != null) {
                        zzwE().zzyA().zzd("User property expired", zzcfwVar2.packageName, zzwz().zzdZ(zzcfwVar2.zzbph.name), zzcfwVar2.zzbph.getValue());
                        zzwy().zzF(str, zzcfwVar2.zzbph.name);
                        if (zzcfwVar2.zzbpp != null) {
                            arrayList.add(zzcfwVar2.zzbpp);
                        }
                        zzwy().zzI(str, zzcfwVar2.zzbph.name);
                    }
                }
                ArrayList arrayList2 = arrayList;
                int size = arrayList2.size();
                int i = 0;
                while (i < size) {
                    Object obj = arrayList2.get(i);
                    i++;
                    zzc(new zzcgl((zzcgl) obj, j), zzcftVar);
                }
                zzcfz zzcfzVarZzwy3 = zzwy();
                String str2 = zzcglVar.name;
                zzbr.zzcF(str);
                zzbr.zzcF(str2);
                zzcfzVarZzwy3.zzjB();
                zzcfzVarZzwy3.zzkC();
                if (j < 0) {
                    zzcfzVarZzwy3.zzwE().zzyx().zzd("Invalid time querying triggered conditional properties", zzcgx.zzea(str), zzcfzVarZzwy3.zzwz().zzdX(str2), Long.valueOf(j));
                    listZzc3 = Collections.emptyList();
                } else {
                    listZzc3 = zzcfzVarZzwy3.zzc("active=0 and app_id=? and trigger_event_name=? and abs(? - creation_timestamp) <= trigger_timeout", new String[]{str, str2, String.valueOf(j)});
                }
                ArrayList arrayList3 = new ArrayList(listZzc3.size());
                for (zzcfw zzcfwVar3 : listZzc3) {
                    if (zzcfwVar3 != null) {
                        zzcku zzckuVar = zzcfwVar3.zzbph;
                        zzckw zzckwVar = new zzckw(zzcfwVar3.packageName, zzcfwVar3.zzbpg, zzckuVar.name, j, zzckuVar.getValue());
                        if (zzwy().zza(zzckwVar)) {
                            zzwE().zzyA().zzd("User property triggered", zzcfwVar3.packageName, zzwz().zzdZ(zzckwVar.mName), zzckwVar.mValue);
                        } else {
                            zzwE().zzyv().zzd("Too many active user properties, ignoring", zzcgx.zzea(zzcfwVar3.packageName), zzwz().zzdZ(zzckwVar.mName), zzckwVar.mValue);
                        }
                        if (zzcfwVar3.zzbpn != null) {
                            arrayList3.add(zzcfwVar3.zzbpn);
                        }
                        zzcfwVar3.zzbph = new zzcku(zzckwVar);
                        zzcfwVar3.zzbpj = true;
                        zzwy().zza(zzcfwVar3);
                    }
                }
                zzc(zzcglVar, zzcftVar);
                ArrayList arrayList4 = arrayList3;
                int size2 = arrayList4.size();
                int i2 = 0;
                while (i2 < size2) {
                    Object obj2 = arrayList4.get(i2);
                    i2++;
                    zzc(new zzcgl((zzcgl) obj2, j), zzcftVar);
                }
                zzwy().setTransactionSuccessful();
            } finally {
                zzwy().endTransaction();
            }
        }
    }

    @WorkerThread
    final void zzb(zzcgl zzcglVar, String str) {
        zzcfs zzcfsVarZzdR = zzwy().zzdR(str);
        if (zzcfsVarZzdR == null || TextUtils.isEmpty(zzcfsVarZzdR.zzjG())) {
            zzwE().zzyA().zzj("No app data available; dropping event", str);
            return;
        }
        try {
            String str2 = zzbim.zzaP(this.mContext).getPackageInfo(str, 0).versionName;
            if (zzcfsVarZzdR.zzjG() != null && !zzcfsVarZzdR.zzjG().equals(str2)) {
                zzwE().zzyx().zzj("App version does not match; dropping event. appId", zzcgx.zzea(str));
                return;
            }
        } catch (PackageManager.NameNotFoundException e) {
            if (!"_ui".equals(zzcglVar.name)) {
                zzwE().zzyx().zzj("Could not find package. appId", zzcgx.zzea(str));
            }
        }
        zzb(zzcglVar, new zzcft(str, zzcfsVarZzdR.getGmpAppId(), zzcfsVarZzdR.zzjG(), zzcfsVarZzdR.zzwM(), zzcfsVarZzdR.zzwN(), zzcfsVarZzdR.zzwO(), zzcfsVarZzdR.zzwP(), (String) null, zzcfsVarZzdR.zzwQ(), false, zzcfsVarZzdR.zzwJ(), zzcfsVarZzdR.zzxd(), 0L, 0));
    }

    final void zzb(zzciv zzcivVar) {
        this.zzbsU++;
    }

    @WorkerThread
    final void zzb(zzcku zzckuVar, zzcft zzcftVar) {
        zzwD().zzjB();
        zzkC();
        if (TextUtils.isEmpty(zzcftVar.zzboU)) {
            return;
        }
        if (!zzcftVar.zzboZ) {
            zzf(zzcftVar);
            return;
        }
        int iZzet = zzwA().zzet(zzckuVar.name);
        if (iZzet != 0) {
            zzwA();
            zzwA().zza(zzcftVar.packageName, iZzet, "_ev", zzckx.zza(zzckuVar.name, zzcfy.zzxh(), true), zzckuVar.name != null ? zzckuVar.name.length() : 0);
            return;
        }
        int iZzl = zzwA().zzl(zzckuVar.name, zzckuVar.getValue());
        if (iZzl != 0) {
            zzwA();
            String strZza = zzckx.zza(zzckuVar.name, zzcfy.zzxh(), true);
            Object value = zzckuVar.getValue();
            if (value != null && ((value instanceof String) || (value instanceof CharSequence))) {
                length = String.valueOf(value).length();
            }
            zzwA().zza(zzcftVar.packageName, iZzl, "_ev", strZza, length);
            return;
        }
        Object objZzm = zzwA().zzm(zzckuVar.name, zzckuVar.getValue());
        if (objZzm != null) {
            zzckw zzckwVar = new zzckw(zzcftVar.packageName, zzckuVar.zzbpg, zzckuVar.name, zzckuVar.zzbuC, objZzm);
            zzwE().zzyA().zze("Setting user property", zzwz().zzdZ(zzckwVar.mName), objZzm);
            zzwy().beginTransaction();
            try {
                zzf(zzcftVar);
                boolean zZza = zzwy().zza(zzckwVar);
                zzwy().setTransactionSuccessful();
                if (zZza) {
                    zzwE().zzyA().zze("User property set", zzwz().zzdZ(zzckwVar.mName), zzckwVar.mValue);
                } else {
                    zzwE().zzyv().zze("Too many unique user properties are set. Ignoring user property", zzwz().zzdZ(zzckwVar.mName), zzckwVar.mValue);
                    zzwA().zza(zzcftVar.packageName, 9, (String) null, (String) null, 0);
                }
            } finally {
                zzwy().endTransaction();
            }
        }
    }

    @WorkerThread
    final void zzb(String str, int i, Throwable th, byte[] bArr, Map<String, List<String>> map) {
        boolean z = true;
        zzwD().zzjB();
        zzkC();
        zzbr.zzcF(str);
        if (bArr == null) {
            try {
                bArr = new byte[0];
            } finally {
                this.zzbsY = false;
                zzze();
            }
        }
        zzwE().zzyB().zzj("onConfigFetched. Response size", Integer.valueOf(bArr.length));
        zzwy().beginTransaction();
        try {
            zzcfs zzcfsVarZzdR = zzwy().zzdR(str);
            boolean z2 = (i == 200 || i == 204 || i == 304) && th == null;
            if (zzcfsVarZzdR == null) {
                zzwE().zzyx().zzj("App does not exist in onConfigFetched. appId", zzcgx.zzea(str));
            } else if (z2 || i == 404) {
                List<String> list = map != null ? map.get(HttpRequest.HEADER_LAST_MODIFIED) : null;
                String str2 = (list == null || list.size() <= 0) ? null : list.get(0);
                if (i == 404 || i == 304) {
                    if (zzwB().zzei(str) == null && !zzwB().zzb(str, null, null)) {
                        return;
                    }
                } else if (!zzwB().zzb(str, bArr, str2)) {
                    return;
                }
                zzcfsVarZzdR.zzR(this.zzvz.currentTimeMillis());
                zzwy().zza(zzcfsVarZzdR);
                if (i == 404) {
                    zzwE().zzyy().zzj("Config not found. Using empty config. appId", str);
                } else {
                    zzwE().zzyB().zze("Successfully fetched config. Got network response. code, size", Integer.valueOf(i), Integer.valueOf(bArr.length));
                }
                if (zzyS().zzlP() && zzyZ()) {
                    zzyY();
                } else {
                    zzza();
                }
            } else {
                zzcfsVarZzdR.zzS(this.zzvz.currentTimeMillis());
                zzwy().zza(zzcfsVarZzdR);
                zzwE().zzyB().zze("Fetching config failed. code, error", Integer.valueOf(i), th);
                zzwB().zzek(str);
                zzwF().zzbrp.set(this.zzvz.currentTimeMillis());
                if (i != 503 && i != 429) {
                    z = false;
                }
                if (z) {
                    zzwF().zzbrq.set(this.zzvz.currentTimeMillis());
                }
                zzza();
            }
            zzwy().setTransactionSuccessful();
        } finally {
            zzwy().endTransaction();
        }
    }

    @WorkerThread
    final void zzc(zzcfw zzcfwVar, zzcft zzcftVar) {
        zzbr.zzu(zzcfwVar);
        zzbr.zzcF(zzcfwVar.packageName);
        zzbr.zzu(zzcfwVar.zzbph);
        zzbr.zzcF(zzcfwVar.zzbph.name);
        zzwD().zzjB();
        zzkC();
        if (TextUtils.isEmpty(zzcftVar.zzboU)) {
            return;
        }
        if (!zzcftVar.zzboZ) {
            zzf(zzcftVar);
            return;
        }
        zzwy().beginTransaction();
        try {
            zzf(zzcftVar);
            zzcfw zzcfwVarZzH = zzwy().zzH(zzcfwVar.packageName, zzcfwVar.zzbph.name);
            if (zzcfwVarZzH != null) {
                zzwE().zzyA().zze("Removing conditional user property", zzcfwVar.packageName, zzwz().zzdZ(zzcfwVar.zzbph.name));
                zzwy().zzI(zzcfwVar.packageName, zzcfwVar.zzbph.name);
                if (zzcfwVarZzH.zzbpj) {
                    zzwy().zzF(zzcfwVar.packageName, zzcfwVar.zzbph.name);
                }
                if (zzcfwVar.zzbpp != null) {
                    zzc(zzwA().zza(zzcfwVar.zzbpp.name, zzcfwVar.zzbpp.zzbpQ != null ? zzcfwVar.zzbpp.zzbpQ.zzyr() : null, zzcfwVarZzH.zzbpg, zzcfwVar.zzbpp.zzbpR, true, false), zzcftVar);
                }
            } else {
                zzwE().zzyx().zze("Conditional user property doesn't exist", zzcgx.zzea(zzcfwVar.packageName), zzwz().zzdZ(zzcfwVar.zzbph.name));
            }
            zzwy().setTransactionSuccessful();
        } finally {
            zzwy().endTransaction();
        }
    }

    @WorkerThread
    final void zzc(zzcku zzckuVar, zzcft zzcftVar) {
        zzwD().zzjB();
        zzkC();
        if (TextUtils.isEmpty(zzcftVar.zzboU)) {
            return;
        }
        if (!zzcftVar.zzboZ) {
            zzf(zzcftVar);
            return;
        }
        zzwE().zzyA().zzj("Removing user property", zzwz().zzdZ(zzckuVar.name));
        zzwy().beginTransaction();
        try {
            zzf(zzcftVar);
            zzwy().zzF(zzcftVar.packageName, zzckuVar.name);
            zzwy().setTransactionSuccessful();
            zzwE().zzyA().zzj("User property removed", zzwz().zzdZ(zzckuVar.name));
        } finally {
            zzwy().endTransaction();
        }
    }

    final void zzd(zzcft zzcftVar) {
        zzwD().zzjB();
        zzkC();
        zzbr.zzcF(zzcftVar.packageName);
        zzf(zzcftVar);
    }

    @WorkerThread
    final void zzd(zzcfw zzcfwVar) {
        zzcft zzcftVarZzem = zzem(zzcfwVar.packageName);
        if (zzcftVarZzem != null) {
            zzb(zzcfwVar, zzcftVarZzem);
        }
    }

    @WorkerThread
    public final void zze(zzcft zzcftVar) throws PackageManager.NameNotFoundException {
        int i;
        ApplicationInfo applicationInfo;
        zzwD().zzjB();
        zzkC();
        zzbr.zzu(zzcftVar);
        zzbr.zzcF(zzcftVar.packageName);
        if (TextUtils.isEmpty(zzcftVar.zzboU)) {
            return;
        }
        zzcfs zzcfsVarZzdR = zzwy().zzdR(zzcftVar.packageName);
        if (zzcfsVarZzdR != null && TextUtils.isEmpty(zzcfsVarZzdR.getGmpAppId()) && !TextUtils.isEmpty(zzcftVar.zzboU)) {
            zzcfsVarZzdR.zzR(0L);
            zzwy().zza(zzcfsVarZzdR);
            zzwB().zzel(zzcftVar.packageName);
        }
        if (!zzcftVar.zzboZ) {
            zzf(zzcftVar);
            return;
        }
        long jCurrentTimeMillis = zzcftVar.zzbpe;
        if (jCurrentTimeMillis == 0) {
            jCurrentTimeMillis = this.zzvz.currentTimeMillis();
        }
        int i2 = zzcftVar.zzbpf;
        if (i2 == 0 || i2 == 1) {
            i = i2;
        } else {
            zzwE().zzyx().zze("Incorrect app type, assuming installed app. appId, appType", zzcgx.zzea(zzcftVar.packageName), Integer.valueOf(i2));
            i = 0;
        }
        zzwy().beginTransaction();
        try {
            zzcfs zzcfsVarZzdR2 = zzwy().zzdR(zzcftVar.packageName);
            if (zzcfsVarZzdR2 != null && zzcfsVarZzdR2.getGmpAppId() != null && !zzcfsVarZzdR2.getGmpAppId().equals(zzcftVar.zzboU)) {
                zzwE().zzyx().zzj("New GMP App Id passed in. Removing cached database data. appId", zzcgx.zzea(zzcfsVarZzdR2.zzhk()));
                zzcfz zzcfzVarZzwy = zzwy();
                String strZzhk = zzcfsVarZzdR2.zzhk();
                zzcfzVarZzwy.zzkC();
                zzcfzVarZzwy.zzjB();
                zzbr.zzcF(strZzhk);
                try {
                    SQLiteDatabase writableDatabase = zzcfzVarZzwy.getWritableDatabase();
                    String[] strArr = {strZzhk};
                    int iDelete = writableDatabase.delete("audience_filter_values", "app_id=?", strArr) + writableDatabase.delete("events", "app_id=?", strArr) + 0 + writableDatabase.delete("user_attributes", "app_id=?", strArr) + writableDatabase.delete("conditional_properties", "app_id=?", strArr) + writableDatabase.delete("apps", "app_id=?", strArr) + writableDatabase.delete("raw_events", "app_id=?", strArr) + writableDatabase.delete("raw_events_metadata", "app_id=?", strArr) + writableDatabase.delete("event_filters", "app_id=?", strArr) + writableDatabase.delete("property_filters", "app_id=?", strArr);
                    if (iDelete > 0) {
                        zzcfzVarZzwy.zzwE().zzyB().zze("Deleted application data. app, records", strZzhk, Integer.valueOf(iDelete));
                    }
                } catch (SQLiteException e) {
                    zzcfzVarZzwy.zzwE().zzyv().zze("Error deleting application data. appId, error", zzcgx.zzea(strZzhk), e);
                }
                zzcfsVarZzdR2 = null;
            }
            if (zzcfsVarZzdR2 != null && zzcfsVarZzdR2.zzjG() != null && !zzcfsVarZzdR2.zzjG().equals(zzcftVar.zzbha)) {
                Bundle bundle = new Bundle();
                bundle.putString("_pv", zzcfsVarZzdR2.zzjG());
                zzb(new zzcgl("_au", new zzcgi(bundle), "auto", jCurrentTimeMillis), zzcftVar);
            }
            zzf(zzcftVar);
            zzcgh zzcghVarZzE = null;
            if (i == 0) {
                zzcghVarZzE = zzwy().zzE(zzcftVar.packageName, "_f");
            } else if (i == 1) {
                zzcghVarZzE = zzwy().zzE(zzcftVar.packageName, "_v");
            }
            if (zzcghVarZzE == null) {
                long j = (1 + (jCurrentTimeMillis / 3600000)) * 3600000;
                if (i == 0) {
                    zzb(new zzcku("_fot", jCurrentTimeMillis, Long.valueOf(j), "auto"), zzcftVar);
                    zzwD().zzjB();
                    zzkC();
                    Bundle bundle2 = new Bundle();
                    bundle2.putLong("_c", 1L);
                    bundle2.putLong("_r", 1L);
                    bundle2.putLong("_uwa", 0L);
                    bundle2.putLong("_pfo", 0L);
                    bundle2.putLong("_sys", 0L);
                    bundle2.putLong("_sysu", 0L);
                    if (this.mContext.getPackageManager() == null) {
                        zzwE().zzyv().zzj("PackageManager is null, first open report might be inaccurate. appId", zzcgx.zzea(zzcftVar.packageName));
                    } else {
                        PackageInfo packageInfo = null;
                        try {
                            packageInfo = zzbim.zzaP(this.mContext).getPackageInfo(zzcftVar.packageName, 0);
                        } catch (PackageManager.NameNotFoundException e2) {
                            zzwE().zzyv().zze("Package info is null, first open report might be inaccurate. appId", zzcgx.zzea(zzcftVar.packageName), e2);
                        }
                        if (packageInfo != null && packageInfo.firstInstallTime != 0) {
                            boolean z = false;
                            if (packageInfo.firstInstallTime != packageInfo.lastUpdateTime) {
                                bundle2.putLong("_uwa", 1L);
                            } else {
                                z = true;
                            }
                            zzb(new zzcku("_fi", jCurrentTimeMillis, Long.valueOf(z ? 1L : 0L), "auto"), zzcftVar);
                        }
                        try {
                            applicationInfo = zzbim.zzaP(this.mContext).getApplicationInfo(zzcftVar.packageName, 0);
                        } catch (PackageManager.NameNotFoundException e3) {
                            zzwE().zzyv().zze("Application info is null, first open report might be inaccurate. appId", zzcgx.zzea(zzcftVar.packageName), e3);
                            applicationInfo = null;
                        }
                        if (applicationInfo != null) {
                            if ((applicationInfo.flags & 1) != 0) {
                                bundle2.putLong("_sys", 1L);
                            }
                            if ((applicationInfo.flags & 128) != 0) {
                                bundle2.putLong("_sysu", 1L);
                            }
                        }
                    }
                    zzcfz zzcfzVarZzwy2 = zzwy();
                    String str = zzcftVar.packageName;
                    zzbr.zzcF(str);
                    zzcfzVarZzwy2.zzjB();
                    zzcfzVarZzwy2.zzkC();
                    long jZzL = zzcfzVarZzwy2.zzL(str, "first_open_count");
                    if (jZzL >= 0) {
                        bundle2.putLong("_pfo", jZzL);
                    }
                    zzb(new zzcgl("_f", new zzcgi(bundle2), "auto", jCurrentTimeMillis), zzcftVar);
                } else if (i == 1) {
                    zzb(new zzcku("_fvt", jCurrentTimeMillis, Long.valueOf(j), "auto"), zzcftVar);
                    zzwD().zzjB();
                    zzkC();
                    Bundle bundle3 = new Bundle();
                    bundle3.putLong("_c", 1L);
                    bundle3.putLong("_r", 1L);
                    zzb(new zzcgl("_v", new zzcgi(bundle3), "auto", jCurrentTimeMillis), zzcftVar);
                }
                Bundle bundle4 = new Bundle();
                bundle4.putLong("_et", 1L);
                zzb(new zzcgl("_e", new zzcgi(bundle4), "auto", jCurrentTimeMillis), zzcftVar);
            } else if (zzcftVar.zzbpa) {
                zzb(new zzcgl("_cd", new zzcgi(new Bundle()), "auto", jCurrentTimeMillis), zzcftVar);
            }
            zzwy().setTransactionSuccessful();
        } finally {
            zzwy().endTransaction();
        }
    }

    @WorkerThread
    final void zze(zzcfw zzcfwVar) {
        zzcft zzcftVarZzem = zzem(zzcfwVar.packageName);
        if (zzcftVarZzem != null) {
            zzc(zzcfwVar, zzcftVarZzem);
        }
    }

    public final String zzen(String str) {
        try {
            return (String) zzwD().zze(new zzchz(this, str)).get(30000L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            zzwE().zzyv().zze("Failed to get app instance id. appId", zzcgx.zzea(str), e);
            return null;
        }
    }

    final void zzkC() {
        if (!this.zzafM) {
            throw new IllegalStateException("AppMeasurement is not initialized");
        }
    }

    public final com.google.android.gms.common.util.zzf zzkp() {
        return this.zzvz;
    }

    @WorkerThread
    final void zzl(Runnable runnable) {
        zzwD().zzjB();
        if (this.zzbsT == null) {
            this.zzbsT = new ArrayList();
        }
        this.zzbsT.add(runnable);
    }

    public final zzckx zzwA() {
        zza((zzciu) this.zzbsz);
        return this.zzbsz;
    }

    public final zzchr zzwB() {
        zza((zzciv) this.zzbsw);
        return this.zzbsw;
    }

    public final zzckm zzwC() {
        zza((zzciv) this.zzbsv);
        return this.zzbsv;
    }

    public final zzchs zzwD() {
        zza((zzciv) this.zzbsu);
        return this.zzbsu;
    }

    public final zzcgx zzwE() {
        zza((zzciv) this.zzbst);
        return this.zzbst;
    }

    public final zzchi zzwF() {
        zza((zzciu) this.zzbss);
        return this.zzbss;
    }

    public final zzcfy zzwG() {
        return this.zzbsr;
    }

    public final zzcfo zzwq() {
        zza(this.zzbsM);
        return this.zzbsM;
    }

    public final zzcfv zzwr() {
        zza((zzciv) this.zzbsL);
        return this.zzbsL;
    }

    public final zzcix zzws() {
        zza((zzciv) this.zzbsH);
        return this.zzbsH;
    }

    public final zzcgs zzwt() {
        zza((zzciv) this.zzbsI);
        return this.zzbsI;
    }

    public final zzcgf zzwu() {
        zza((zzciv) this.zzbsG);
        return this.zzbsG;
    }

    public final zzcjp zzwv() {
        zza((zzciv) this.zzbsF);
        return this.zzbsF;
    }

    public final zzcjl zzww() {
        zza((zzciv) this.zzbsE);
        return this.zzbsE;
    }

    public final zzcgt zzwx() {
        zza((zzciv) this.zzbsC);
        return this.zzbsC;
    }

    public final zzcfz zzwy() {
        zza((zzciv) this.zzbsB);
        return this.zzbsB;
    }

    public final zzcgv zzwz() {
        zza((zzciu) this.zzbsA);
        return this.zzbsA;
    }

    @WorkerThread
    protected final boolean zzyN() {
        boolean z = false;
        zzkC();
        zzwD().zzjB();
        if (this.zzbsO == null || this.zzbsP == 0 || (this.zzbsO != null && !this.zzbsO.booleanValue() && Math.abs(this.zzvz.elapsedRealtime() - this.zzbsP) > 1000)) {
            this.zzbsP = this.zzvz.elapsedRealtime();
            zzcfy.zzxD();
            if (zzwA().zzbv("android.permission.INTERNET") && zzwA().zzbv("android.permission.ACCESS_NETWORK_STATE") && (zzbim.zzaP(this.mContext).zzsk() || (zzcho.zzj(this.mContext, false) && zzcki.zzk(this.mContext, false)))) {
                z = true;
            }
            this.zzbsO = Boolean.valueOf(z);
            if (this.zzbsO.booleanValue()) {
                this.zzbsO = Boolean.valueOf(zzwA().zzew(zzwt().getGmpAppId()));
            }
        }
        return this.zzbsO.booleanValue();
    }

    public final zzcgx zzyO() {
        if (this.zzbst == null || !this.zzbst.isInitialized()) {
            return null;
        }
        return this.zzbst;
    }

    final zzchs zzyP() {
        return this.zzbsu;
    }

    public final AppMeasurement zzyQ() {
        return this.zzbsx;
    }

    public final FirebaseAnalytics zzyR() {
        return this.zzbsy;
    }

    public final zzchb zzyS() {
        zza((zzciv) this.zzbsD);
        return this.zzbsD;
    }

    final long zzyW() {
        Long lValueOf = Long.valueOf(zzwF().zzbrt.get());
        return lValueOf.longValue() == 0 ? this.zzbtb : Math.min(this.zzbtb, lValueOf.longValue());
    }

    /* JADX WARN: Removed duplicated region for block: B:60:0x0187 A[Catch: all -> 0x0278, TryCatch #1 {all -> 0x0278, blocks: (B:3:0x0013, B:5:0x0020, B:8:0x0033, B:10:0x0039, B:12:0x004c, B:14:0x0052, B:16:0x005b, B:20:0x0069, B:23:0x007e, B:25:0x0088, B:27:0x009e, B:29:0x00bc, B:30:0x00d3, B:32:0x00e1, B:34:0x00e7, B:35:0x00f1, B:37:0x0114, B:38:0x0118, B:40:0x011e, B:42:0x0130, B:45:0x0136, B:47:0x013c, B:49:0x014e, B:51:0x0156, B:52:0x015c, B:54:0x0178, B:58:0x0182, B:60:0x0187, B:62:0x01ca, B:63:0x01d1, B:66:0x01dc, B:68:0x01e7, B:69:0x01f0, B:70:0x01fc, B:72:0x0207, B:74:0x020e, B:75:0x021b, B:77:0x022b, B:78:0x0232, B:81:0x025d, B:84:0x0266, B:64:0x01d5, B:90:0x0281, B:92:0x0297, B:94:0x02a1), top: B:101:0x0013, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x01da  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x01e7 A[Catch: all -> 0x0278, TryCatch #1 {all -> 0x0278, blocks: (B:3:0x0013, B:5:0x0020, B:8:0x0033, B:10:0x0039, B:12:0x004c, B:14:0x0052, B:16:0x005b, B:20:0x0069, B:23:0x007e, B:25:0x0088, B:27:0x009e, B:29:0x00bc, B:30:0x00d3, B:32:0x00e1, B:34:0x00e7, B:35:0x00f1, B:37:0x0114, B:38:0x0118, B:40:0x011e, B:42:0x0130, B:45:0x0136, B:47:0x013c, B:49:0x014e, B:51:0x0156, B:52:0x015c, B:54:0x0178, B:58:0x0182, B:60:0x0187, B:62:0x01ca, B:63:0x01d1, B:66:0x01dc, B:68:0x01e7, B:69:0x01f0, B:70:0x01fc, B:72:0x0207, B:74:0x020e, B:75:0x021b, B:77:0x022b, B:78:0x0232, B:81:0x025d, B:84:0x0266, B:64:0x01d5, B:90:0x0281, B:92:0x0297, B:94:0x02a1), top: B:101:0x0013, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:74:0x020e A[Catch: MalformedURLException -> 0x0265, all -> 0x0278, TryCatch #0 {MalformedURLException -> 0x0265, blocks: (B:70:0x01fc, B:72:0x0207, B:74:0x020e, B:75:0x021b, B:77:0x022b, B:78:0x0232, B:81:0x025d), top: B:99:0x01fc, outer: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:77:0x022b A[Catch: MalformedURLException -> 0x0265, all -> 0x0278, TryCatch #0 {MalformedURLException -> 0x0265, blocks: (B:70:0x01fc, B:72:0x0207, B:74:0x020e, B:75:0x021b, B:77:0x022b, B:78:0x0232, B:81:0x025d), top: B:99:0x01fc, outer: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:80:0x025b  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x025d A[Catch: MalformedURLException -> 0x0265, all -> 0x0278, TRY_ENTER, TRY_LEAVE, TryCatch #0 {MalformedURLException -> 0x0265, blocks: (B:70:0x01fc, B:72:0x0207, B:74:0x020e, B:75:0x021b, B:77:0x022b, B:78:0x0232, B:81:0x025d), top: B:99:0x01fc, outer: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:96:0x02a5  */
    @android.support.annotation.WorkerThread
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void zzyY() {
        /*
            Method dump skipped, instruction units count: 686
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzchx.zzyY():void");
    }

    final void zzzb() {
        this.zzbsV++;
    }

    @WorkerThread
    final void zzzc() {
        zzwD().zzjB();
        zzkC();
        if (this.zzbsN) {
            return;
        }
        zzwE().zzyz().log("This instance being marked as an uploader");
        zzwD().zzjB();
        zzkC();
        if (zzzd() && zzyV()) {
            int iZza = zza(this.zzbsR);
            int iZzyt = zzwt().zzyt();
            zzwD().zzjB();
            if (iZza > iZzyt) {
                zzwE().zzyv().zze("Panic: can't downgrade version. Previous, current version", Integer.valueOf(iZza), Integer.valueOf(iZzyt));
            } else if (iZza < iZzyt) {
                if (zza(iZzyt, this.zzbsR)) {
                    zzwE().zzyB().zze("Storage version upgraded. Previous, current version", Integer.valueOf(iZza), Integer.valueOf(iZzyt));
                } else {
                    zzwE().zzyv().zze("Storage version upgrade failed. Previous, current version", Integer.valueOf(iZza), Integer.valueOf(iZzyt));
                }
            }
        }
        this.zzbsN = true;
        zzza();
    }
}
