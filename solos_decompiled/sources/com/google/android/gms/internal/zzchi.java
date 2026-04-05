package com.google.android.gms.internal;

import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import android.util.Pair;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.kopin.pupil.aria.app.TimedAppState;
import io.fabric.sdk.android.services.common.CommonUtils;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Locale;

/* JADX INFO: loaded from: classes36.dex */
final class zzchi extends zzciv {
    static final Pair<String, Long> zzbrm = new Pair<>("", 0L);
    private SharedPreferences zzaiz;
    private final Object zzbrA;
    public final zzchl zzbrB;
    public final zzchl zzbrC;
    public final zzchk zzbrD;
    public final zzchl zzbrE;
    public final zzchl zzbrF;
    public boolean zzbrG;
    public final zzchm zzbrn;
    public final zzchl zzbro;
    public final zzchl zzbrp;
    public final zzchl zzbrq;
    public final zzchl zzbrr;
    public final zzchl zzbrs;
    public final zzchl zzbrt;
    public final zzchn zzbru;
    private String zzbrv;
    private boolean zzbrw;
    private long zzbrx;
    private String zzbry;
    private long zzbrz;

    zzchi(zzchx zzchxVar) {
        super(zzchxVar);
        this.zzbrn = new zzchm(this, "health_monitor", zzcfy.zzxJ());
        this.zzbro = new zzchl(this, "last_upload", 0L);
        this.zzbrp = new zzchl(this, "last_upload_attempt", 0L);
        this.zzbrq = new zzchl(this, "backoff", 0L);
        this.zzbrr = new zzchl(this, "last_delete_stale", 0L);
        this.zzbrB = new zzchl(this, "time_before_start", TimedAppState.DEFAULT_CONFIRM_TIMEOUT);
        this.zzbrC = new zzchl(this, "session_timeout", 1800000L);
        this.zzbrD = new zzchk(this, "start_new_session", true);
        this.zzbrE = new zzchl(this, "last_pause_time", 0L);
        this.zzbrF = new zzchl(this, "time_active", 0L);
        this.zzbrs = new zzchl(this, "midnight_offset", 0L);
        this.zzbrt = new zzchl(this, "first_open_time", 0L);
        this.zzbru = new zzchn(this, "app_instance_id", null);
        this.zzbrA = new Object();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @WorkerThread
    public final SharedPreferences zzyD() {
        zzjB();
        zzkC();
        return this.zzaiz;
    }

    @WorkerThread
    final void setMeasurementEnabled(boolean z) {
        zzjB();
        zzwE().zzyB().zzj("Setting measurementEnabled", Boolean.valueOf(z));
        SharedPreferences.Editor editorEdit = zzyD().edit();
        editorEdit.putBoolean("measurement_enabled", z);
        editorEdit.apply();
    }

    @WorkerThread
    final void zzak(boolean z) {
        zzjB();
        zzwE().zzyB().zzj("Setting useService", Boolean.valueOf(z));
        SharedPreferences.Editor editorEdit = zzyD().edit();
        editorEdit.putBoolean("use_service", z);
        editorEdit.apply();
    }

    @WorkerThread
    final boolean zzal(boolean z) {
        zzjB();
        return zzyD().getBoolean("measurement_enabled", z);
    }

    @WorkerThread
    @NonNull
    final Pair<String, Boolean> zzec(String str) {
        zzjB();
        long jElapsedRealtime = zzkp().elapsedRealtime();
        if (this.zzbrv != null && jElapsedRealtime < this.zzbrx) {
            return new Pair<>(this.zzbrv, Boolean.valueOf(this.zzbrw));
        }
        this.zzbrx = jElapsedRealtime + zzwG().zza(str, zzcgn.zzbqa);
        AdvertisingIdClient.setShouldSkipGmsCoreVersionCheck(true);
        try {
            AdvertisingIdClient.Info advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(getContext());
            if (advertisingIdInfo != null) {
                this.zzbrv = advertisingIdInfo.getId();
                this.zzbrw = advertisingIdInfo.isLimitAdTrackingEnabled();
            }
            if (this.zzbrv == null) {
                this.zzbrv = "";
            }
        } catch (Throwable th) {
            zzwE().zzyA().zzj("Unable to get advertising id", th);
            this.zzbrv = "";
        }
        AdvertisingIdClient.setShouldSkipGmsCoreVersionCheck(false);
        return new Pair<>(this.zzbrv, Boolean.valueOf(this.zzbrw));
    }

    @WorkerThread
    final String zzed(String str) {
        zzjB();
        String str2 = (String) zzec(str).first;
        MessageDigest messageDigestZzbE = zzckx.zzbE(CommonUtils.MD5_INSTANCE);
        if (messageDigestZzbE == null) {
            return null;
        }
        return String.format(Locale.US, "%032X", new BigInteger(1, messageDigestZzbE.digest(str2.getBytes())));
    }

    @WorkerThread
    final void zzee(String str) {
        zzjB();
        SharedPreferences.Editor editorEdit = zzyD().edit();
        editorEdit.putString("gmp_app_id", str);
        editorEdit.apply();
    }

    final void zzef(String str) {
        synchronized (this.zzbrA) {
            this.zzbry = str;
            this.zzbrz = zzkp().elapsedRealtime();
        }
    }

    @Override // com.google.android.gms.internal.zzciv
    protected final void zzjC() {
        this.zzaiz = getContext().getSharedPreferences("com.google.android.gms.measurement.prefs", 0);
        this.zzbrG = this.zzaiz.getBoolean("has_been_opened", false);
        if (this.zzbrG) {
            return;
        }
        SharedPreferences.Editor editorEdit = this.zzaiz.edit();
        editorEdit.putBoolean("has_been_opened", true);
        editorEdit.apply();
    }

    @WorkerThread
    final String zzyE() {
        zzjB();
        return zzyD().getString("gmp_app_id", null);
    }

    final String zzyF() {
        String str;
        synchronized (this.zzbrA) {
            str = Math.abs(zzkp().elapsedRealtime() - this.zzbrz) < 1000 ? this.zzbry : null;
        }
        return str;
    }

    @WorkerThread
    final Boolean zzyG() {
        zzjB();
        if (zzyD().contains("use_service")) {
            return Boolean.valueOf(zzyD().getBoolean("use_service", false));
        }
        return null;
    }

    @WorkerThread
    final void zzyH() {
        zzjB();
        zzwE().zzyB().log("Clearing collection preferences.");
        boolean zContains = zzyD().contains("measurement_enabled");
        boolean zZzal = zContains ? zzal(true) : true;
        SharedPreferences.Editor editorEdit = zzyD().edit();
        editorEdit.clear();
        editorEdit.apply();
        if (zContains) {
            setMeasurementEnabled(zZzal);
        }
    }

    @WorkerThread
    protected final String zzyI() {
        zzjB();
        String string = zzyD().getString("previous_os_version", null);
        zzwu().zzkC();
        String str = Build.VERSION.RELEASE;
        if (!TextUtils.isEmpty(str) && !str.equals(string)) {
            SharedPreferences.Editor editorEdit = zzyD().edit();
            editorEdit.putString("previous_os_version", str);
            editorEdit.apply();
        }
        return string;
    }
}
