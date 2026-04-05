package com.google.android.gms.ads.identifier;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import com.facebook.appevents.AppEventsConstants;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.annotation.KeepForSdkWithMembers;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.common.zze;
import com.google.android.gms.common.zzo;
import com.google.android.gms.internal.zzfd;
import com.google.android.gms.internal.zzfe;
import com.kopin.pupil.aria.app.TimedAppState;
import io.fabric.sdk.android.services.common.AdvertisingInfoServiceStrategy;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes67.dex */
@KeepForSdkWithMembers
public class AdvertisingIdClient {
    private final Context mContext;

    @Nullable
    private com.google.android.gms.common.zza zzsA;

    @Nullable
    private zzfd zzsB;
    private boolean zzsC;
    private Object zzsD;

    @Nullable
    private zza zzsE;
    private long zzsF;

    public static final class Info {
        private final String zzsL;
        private final boolean zzsM;

        public Info(String str, boolean z) {
            this.zzsL = str;
            this.zzsM = z;
        }

        public final String getId() {
            return this.zzsL;
        }

        public final boolean isLimitAdTrackingEnabled() {
            return this.zzsM;
        }

        public final String toString() {
            String str = this.zzsL;
            return new StringBuilder(String.valueOf(str).length() + 7).append("{").append(str).append("}").append(this.zzsM).toString();
        }
    }

    static class zza extends Thread {
        private WeakReference<AdvertisingIdClient> zzsH;
        private long zzsI;
        CountDownLatch zzsJ = new CountDownLatch(1);
        boolean zzsK = false;

        public zza(AdvertisingIdClient advertisingIdClient, long j) {
            this.zzsH = new WeakReference<>(advertisingIdClient);
            this.zzsI = j;
            start();
        }

        private final void disconnect() {
            AdvertisingIdClient advertisingIdClient = this.zzsH.get();
            if (advertisingIdClient != null) {
                advertisingIdClient.finish();
                this.zzsK = true;
            }
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public final void run() {
            try {
                if (this.zzsJ.await(this.zzsI, TimeUnit.MILLISECONDS)) {
                    return;
                }
                disconnect();
            } catch (InterruptedException e) {
                disconnect();
            }
        }
    }

    public AdvertisingIdClient(Context context) {
        this(context, 30000L, false);
    }

    public AdvertisingIdClient(Context context, long j, boolean z) {
        this.zzsD = new Object();
        zzbr.zzu(context);
        if (z) {
            Context applicationContext = context.getApplicationContext();
            this.mContext = applicationContext != null ? applicationContext : context;
        } else {
            this.mContext = context;
        }
        this.zzsC = false;
        this.zzsF = j;
    }

    @Nullable
    public static Info getAdvertisingIdInfo(Context context) throws GooglePlayServicesRepairableException, IllegalStateException, GooglePlayServicesNotAvailableException, IOException {
        float f = 0.0f;
        boolean z = false;
        try {
            Context remoteContext = zzo.getRemoteContext(context);
            if (remoteContext != null) {
                SharedPreferences sharedPreferences = remoteContext.getSharedPreferences("google_ads_flags", 0);
                z = sharedPreferences.getBoolean("gads:ad_id_app_context:enabled", false);
                f = sharedPreferences.getFloat("gads:ad_id_app_context:ping_ratio", 0.0f);
            }
        } catch (Exception e) {
            Log.w("AdvertisingIdClient", "Error while reading from SharedPreferences ", e);
        }
        AdvertisingIdClient advertisingIdClient = new AdvertisingIdClient(context, -1L, z);
        try {
            advertisingIdClient.start(false);
            Info info = advertisingIdClient.getInfo();
            advertisingIdClient.zza(info, z, f, null);
            return info;
        } catch (Throwable th) {
            advertisingIdClient.zza(null, z, f, th);
            return null;
        } finally {
            advertisingIdClient.finish();
        }
    }

    public static void setShouldSkipGmsCoreVersionCheck(boolean z) {
    }

    private final void start(boolean z) throws GooglePlayServicesRepairableException, IllegalStateException, GooglePlayServicesNotAvailableException, IOException {
        zzbr.zzcG("Calling this from your main thread can lead to deadlock");
        synchronized (this) {
            if (this.zzsC) {
                finish();
            }
            this.zzsA = zzd(this.mContext);
            this.zzsB = zza(this.mContext, this.zzsA);
            this.zzsC = true;
            if (z) {
                zzai();
            }
        }
    }

    private static zzfd zza(Context context, com.google.android.gms.common.zza zzaVar) throws IOException {
        try {
            return zzfe.zzc(zzaVar.zza(TimedAppState.DEFAULT_CONFIRM_TIMEOUT, TimeUnit.MILLISECONDS));
        } catch (InterruptedException e) {
            throw new IOException("Interrupted exception");
        } catch (Throwable th) {
            throw new IOException(th);
        }
    }

    private final void zza(Info info, boolean z, float f, Throwable th) {
        if (Math.random() > f) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("app_context", z ? AppEventsConstants.EVENT_PARAM_VALUE_YES : "0");
        if (info != null) {
            bundle.putString("limit_ad_tracking", info.isLimitAdTrackingEnabled() ? AppEventsConstants.EVENT_PARAM_VALUE_YES : "0");
        }
        if (info != null && info.getId() != null) {
            bundle.putString("ad_id_size", Integer.toString(info.getId().length()));
        }
        if (th != null) {
            bundle.putString("error", th.getClass().getName());
        }
        Uri.Builder builderBuildUpon = Uri.parse("https://pagead2.googlesyndication.com/pagead/gen_204?id=gmob-apps").buildUpon();
        for (String str : bundle.keySet()) {
            builderBuildUpon.appendQueryParameter(str, bundle.getString(str));
        }
        new com.google.android.gms.ads.identifier.zza(this, builderBuildUpon.build().toString()).start();
    }

    private final void zzai() {
        synchronized (this.zzsD) {
            if (this.zzsE != null) {
                this.zzsE.zzsJ.countDown();
                try {
                    this.zzsE.join();
                } catch (InterruptedException e) {
                }
            }
            if (this.zzsF > 0) {
                this.zzsE = new zza(this, this.zzsF);
            }
        }
    }

    private static com.google.android.gms.common.zza zzd(Context context) throws GooglePlayServicesRepairableException, GooglePlayServicesNotAvailableException, IOException {
        try {
            context.getPackageManager().getPackageInfo("com.android.vending", 0);
            switch (zze.zzoU().isGooglePlayServicesAvailable(context)) {
                case 0:
                case 2:
                    com.google.android.gms.common.zza zzaVar = new com.google.android.gms.common.zza();
                    Intent intent = new Intent(AdvertisingInfoServiceStrategy.GOOGLE_PLAY_SERVICES_INTENT);
                    intent.setPackage("com.google.android.gms");
                    try {
                        if (com.google.android.gms.common.stats.zza.zzrT().zza(context, intent, zzaVar, 1)) {
                            return zzaVar;
                        }
                        throw new IOException("Connection failure");
                    } catch (Throwable th) {
                        throw new IOException(th);
                    }
                case 1:
                default:
                    throw new IOException("Google Play services not available");
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new GooglePlayServicesNotAvailableException(9);
        }
    }

    protected void finalize() throws Throwable {
        finish();
        super.finalize();
    }

    public void finish() {
        zzbr.zzcG("Calling this from your main thread can lead to deadlock");
        synchronized (this) {
            if (this.mContext == null || this.zzsA == null) {
                return;
            }
            try {
                try {
                    if (this.zzsC) {
                        com.google.android.gms.common.stats.zza.zzrT();
                        this.mContext.unbindService(this.zzsA);
                    }
                } catch (Throwable th) {
                    Log.i("AdvertisingIdClient", "AdvertisingIdClient unbindService failed.", th);
                }
            } catch (IllegalArgumentException e) {
                Log.i("AdvertisingIdClient", "AdvertisingIdClient unbindService failed.", e);
            }
            this.zzsC = false;
            this.zzsB = null;
            this.zzsA = null;
        }
    }

    public Info getInfo() throws IOException {
        Info info;
        zzbr.zzcG("Calling this from your main thread can lead to deadlock");
        synchronized (this) {
            if (this.zzsC) {
                zzbr.zzu(this.zzsA);
                zzbr.zzu(this.zzsB);
                info = new Info(this.zzsB.getId(), this.zzsB.zzb(true));
            } else {
                synchronized (this.zzsD) {
                    if (this.zzsE == null || !this.zzsE.zzsK) {
                        throw new IOException("AdvertisingIdClient is not connected.");
                    }
                }
                try {
                    start(false);
                    if (!this.zzsC) {
                        throw new IOException("AdvertisingIdClient cannot reconnect.");
                    }
                    zzbr.zzu(this.zzsA);
                    zzbr.zzu(this.zzsB);
                    try {
                        info = new Info(this.zzsB.getId(), this.zzsB.zzb(true));
                    } catch (RemoteException e) {
                        Log.i("AdvertisingIdClient", "GMS remote exception ", e);
                        throw new IOException("Remote exception");
                    }
                } catch (Exception e2) {
                    throw new IOException("AdvertisingIdClient cannot reconnect.", e2);
                }
            }
        }
        zzai();
        return info;
    }

    public void start() throws GooglePlayServicesRepairableException, IllegalStateException, GooglePlayServicesNotAvailableException, IOException {
        start(true);
    }
}
