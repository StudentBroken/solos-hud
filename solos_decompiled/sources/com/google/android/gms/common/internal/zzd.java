package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteException;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Scope;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes67.dex */
public abstract class zzd<T extends IInterface> {
    private static String[] zzaHe = {"service_esmobile", "service_googleme"};
    private final Context mContext;
    final Handler mHandler;
    private final Object mLock;
    private final com.google.android.gms.common.zze zzaCH;
    private int zzaGJ;
    private long zzaGK;
    private long zzaGL;
    private int zzaGM;
    private long zzaGN;
    private zzal zzaGO;
    private final zzaf zzaGP;
    private final Object zzaGQ;
    private zzay zzaGR;
    protected zzj zzaGS;
    private T zzaGT;
    private final ArrayList<zzi<?>> zzaGU;
    private zzl zzaGV;
    private int zzaGW;
    private final zzf zzaGX;
    private final zzg zzaGY;
    private final int zzaGZ;
    private final String zzaHa;
    private ConnectionResult zzaHb;
    private boolean zzaHc;
    protected AtomicInteger zzaHd;
    private final Looper zzrP;

    protected zzd(Context context, Looper looper, int i, zzf zzfVar, zzg zzgVar, String str) {
        this(context, looper, zzaf.zzaC(context), com.google.android.gms.common.zze.zzoU(), i, (zzf) zzbr.zzu(zzfVar), (zzg) zzbr.zzu(zzgVar), null);
    }

    protected zzd(Context context, Looper looper, zzaf zzafVar, com.google.android.gms.common.zze zzeVar, int i, zzf zzfVar, zzg zzgVar, String str) {
        this.mLock = new Object();
        this.zzaGQ = new Object();
        this.zzaGU = new ArrayList<>();
        this.zzaGW = 1;
        this.zzaHb = null;
        this.zzaHc = false;
        this.zzaHd = new AtomicInteger(0);
        this.mContext = (Context) zzbr.zzb(context, "Context must not be null");
        this.zzrP = (Looper) zzbr.zzb(looper, "Looper must not be null");
        this.zzaGP = (zzaf) zzbr.zzb(zzafVar, "Supervisor must not be null");
        this.zzaCH = (com.google.android.gms.common.zze) zzbr.zzb(zzeVar, "API availability must not be null");
        this.mHandler = new zzh(this, looper);
        this.zzaGZ = i;
        this.zzaGX = zzfVar;
        this.zzaGY = zzgVar;
        this.zzaHa = str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zza(int i, T t) {
        zzbr.zzaf((i == 4) == (t != null));
        synchronized (this.mLock) {
            this.zzaGW = i;
            this.zzaGT = t;
            switch (i) {
                case 1:
                    if (this.zzaGV != null) {
                        this.zzaGP.zza(zzda(), zzqX(), this.zzaGV, zzqY());
                        this.zzaGV = null;
                    }
                    break;
                case 2:
                case 3:
                    if (this.zzaGV != null && this.zzaGO != null) {
                        String strValueOf = String.valueOf(this.zzaGO.zzrD());
                        String strValueOf2 = String.valueOf(this.zzaGO.getPackageName());
                        Log.e("GmsClient", new StringBuilder(String.valueOf(strValueOf).length() + 70 + String.valueOf(strValueOf2).length()).append("Calling connect() while still connected, missing disconnect() for ").append(strValueOf).append(" on ").append(strValueOf2).toString());
                        this.zzaGP.zza(this.zzaGO.zzrD(), this.zzaGO.getPackageName(), this.zzaGV, zzqY());
                        this.zzaHd.incrementAndGet();
                    }
                    this.zzaGV = new zzl(this, this.zzaHd.get());
                    this.zzaGO = new zzal(zzqX(), zzda(), false);
                    if (!this.zzaGP.zza(new zzag(this.zzaGO.zzrD(), this.zzaGO.getPackageName()), this.zzaGV, zzqY())) {
                        String strValueOf3 = String.valueOf(this.zzaGO.zzrD());
                        String strValueOf4 = String.valueOf(this.zzaGO.getPackageName());
                        Log.e("GmsClient", new StringBuilder(String.valueOf(strValueOf3).length() + 34 + String.valueOf(strValueOf4).length()).append("unable to connect to service: ").append(strValueOf3).append(" on ").append(strValueOf4).toString());
                        zza(16, (Bundle) null, this.zzaHd.get());
                    }
                    break;
                case 4:
                    zza(t);
                    break;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean zza(int i, int i2, T t) {
        boolean z;
        synchronized (this.mLock) {
            if (this.zzaGW != i) {
                z = false;
            } else {
                zza(i2, t);
                z = true;
            }
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zzaz(int i) {
        int i2;
        if (zzra()) {
            i2 = 5;
            this.zzaHc = true;
        } else {
            i2 = 4;
        }
        this.mHandler.sendMessage(this.mHandler.obtainMessage(i2, this.zzaHd.get(), 16));
    }

    @Nullable
    private final String zzqY() {
        return this.zzaHa == null ? this.mContext.getClass().getName() : this.zzaHa;
    }

    private final boolean zzra() {
        boolean z;
        synchronized (this.mLock) {
            z = this.zzaGW == 3;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean zzrg() {
        if (this.zzaHc || TextUtils.isEmpty(zzdb()) || TextUtils.isEmpty(null)) {
            return false;
        }
        try {
            Class.forName(zzdb());
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public void disconnect() {
        this.zzaHd.incrementAndGet();
        synchronized (this.zzaGU) {
            int size = this.zzaGU.size();
            for (int i = 0; i < size; i++) {
                this.zzaGU.get(i).removeListener();
            }
            this.zzaGU.clear();
        }
        synchronized (this.zzaGQ) {
            this.zzaGR = null;
        }
        zza(1, (IInterface) null);
    }

    public final void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        int i;
        T t;
        zzay zzayVar;
        synchronized (this.mLock) {
            i = this.zzaGW;
            t = this.zzaGT;
        }
        synchronized (this.zzaGQ) {
            zzayVar = this.zzaGR;
        }
        printWriter.append((CharSequence) str).append("mConnectState=");
        switch (i) {
            case 1:
                printWriter.print("DISCONNECTED");
                break;
            case 2:
                printWriter.print("REMOTE_CONNECTING");
                break;
            case 3:
                printWriter.print("LOCAL_CONNECTING");
                break;
            case 4:
                printWriter.print("CONNECTED");
                break;
            case 5:
                printWriter.print("DISCONNECTING");
                break;
            default:
                printWriter.print("UNKNOWN");
                break;
        }
        printWriter.append(" mService=");
        if (t == null) {
            printWriter.append("null");
        } else {
            printWriter.append((CharSequence) zzdb()).append("@").append((CharSequence) Integer.toHexString(System.identityHashCode(t.asBinder())));
        }
        printWriter.append(" mServiceBroker=");
        if (zzayVar == null) {
            printWriter.println("null");
        } else {
            printWriter.append("IGmsServiceBroker@").println(Integer.toHexString(System.identityHashCode(zzayVar.asBinder())));
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
        if (this.zzaGL > 0) {
            PrintWriter printWriterAppend = printWriter.append((CharSequence) str).append("lastConnectedTime=");
            long j = this.zzaGL;
            String strValueOf = String.valueOf(simpleDateFormat.format(new Date(this.zzaGL)));
            printWriterAppend.println(new StringBuilder(String.valueOf(strValueOf).length() + 21).append(j).append(" ").append(strValueOf).toString());
        }
        if (this.zzaGK > 0) {
            printWriter.append((CharSequence) str).append("lastSuspendedCause=");
            switch (this.zzaGJ) {
                case 1:
                    printWriter.append("CAUSE_SERVICE_DISCONNECTED");
                    break;
                case 2:
                    printWriter.append("CAUSE_NETWORK_LOST");
                    break;
                default:
                    printWriter.append((CharSequence) String.valueOf(this.zzaGJ));
                    break;
            }
            PrintWriter printWriterAppend2 = printWriter.append(" lastSuspendedTime=");
            long j2 = this.zzaGK;
            String strValueOf2 = String.valueOf(simpleDateFormat.format(new Date(this.zzaGK)));
            printWriterAppend2.println(new StringBuilder(String.valueOf(strValueOf2).length() + 21).append(j2).append(" ").append(strValueOf2).toString());
        }
        if (this.zzaGN > 0) {
            printWriter.append((CharSequence) str).append("lastFailedStatus=").append((CharSequence) CommonStatusCodes.getStatusCodeString(this.zzaGM));
            PrintWriter printWriterAppend3 = printWriter.append(" lastFailedTime=");
            long j3 = this.zzaGN;
            String strValueOf3 = String.valueOf(simpleDateFormat.format(new Date(this.zzaGN)));
            printWriterAppend3.println(new StringBuilder(String.valueOf(strValueOf3).length() + 21).append(j3).append(" ").append(strValueOf3).toString());
        }
    }

    public Account getAccount() {
        return null;
    }

    public final Context getContext() {
        return this.mContext;
    }

    public final Looper getLooper() {
        return this.zzrP;
    }

    public final boolean isConnected() {
        boolean z;
        synchronized (this.mLock) {
            z = this.zzaGW == 4;
        }
        return z;
    }

    public final boolean isConnecting() {
        boolean z;
        synchronized (this.mLock) {
            z = this.zzaGW == 2 || this.zzaGW == 3;
        }
        return z;
    }

    @CallSuper
    protected void onConnectionFailed(ConnectionResult connectionResult) {
        this.zzaGM = connectionResult.getErrorCode();
        this.zzaGN = System.currentTimeMillis();
    }

    @CallSuper
    protected final void onConnectionSuspended(int i) {
        this.zzaGJ = i;
        this.zzaGK = System.currentTimeMillis();
    }

    protected final void zza(int i, @Nullable Bundle bundle, int i2) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(7, i2, -1, new zzo(this, i, null)));
    }

    protected void zza(int i, IBinder iBinder, Bundle bundle, int i2) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(1, i2, -1, new zzn(this, i, iBinder, bundle)));
    }

    @CallSuper
    protected void zza(@NonNull T t) {
        this.zzaGL = System.currentTimeMillis();
    }

    @WorkerThread
    public final void zza(zzam zzamVar, Set<Scope> set) {
        Bundle bundleZzmm = zzmm();
        zzy zzyVar = new zzy(this.zzaGZ);
        zzyVar.zzaHy = this.mContext.getPackageName();
        zzyVar.zzaHB = bundleZzmm;
        if (set != null) {
            zzyVar.zzaHA = (Scope[]) set.toArray(new Scope[set.size()]);
        }
        if (zzmt()) {
            zzyVar.zzaHC = getAccount() != null ? getAccount() : new Account("<<default account>>", "com.google");
            if (zzamVar != null) {
                zzyVar.zzaHz = zzamVar.asBinder();
            }
        } else if (zzre()) {
            zzyVar.zzaHC = getAccount();
        }
        zzyVar.zzaHD = zzrb();
        try {
            synchronized (this.zzaGQ) {
                if (this.zzaGR != null) {
                    this.zzaGR.zza(new zzk(this, this.zzaHd.get()), zzyVar);
                } else {
                    Log.w("GmsClient", "mServiceBroker is null, client disconnected");
                }
            }
        } catch (DeadObjectException e) {
            Log.w("GmsClient", "IGmsServiceBroker.getService failed", e);
            zzay(1);
        } catch (RemoteException e2) {
            e = e2;
            Log.w("GmsClient", "IGmsServiceBroker.getService failed", e);
            zza(8, (IBinder) null, (Bundle) null, this.zzaHd.get());
        } catch (SecurityException e3) {
            throw e3;
        } catch (RuntimeException e4) {
            e = e4;
            Log.w("GmsClient", "IGmsServiceBroker.getService failed", e);
            zza(8, (IBinder) null, (Bundle) null, this.zzaHd.get());
        }
    }

    public void zza(@NonNull zzj zzjVar) {
        this.zzaGS = (zzj) zzbr.zzb(zzjVar, "Connection progress callbacks cannot be null.");
        zza(2, (IInterface) null);
    }

    protected final void zza(@NonNull zzj zzjVar, int i, @Nullable PendingIntent pendingIntent) {
        this.zzaGS = (zzj) zzbr.zzb(zzjVar, "Connection progress callbacks cannot be null.");
        this.mHandler.sendMessage(this.mHandler.obtainMessage(3, this.zzaHd.get(), i, pendingIntent));
    }

    public final void zzay(int i) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(6, this.zzaHd.get(), i));
    }

    @Nullable
    protected abstract T zzd(IBinder iBinder);

    @NonNull
    protected abstract String zzda();

    @NonNull
    protected abstract String zzdb();

    public boolean zzmE() {
        return false;
    }

    public Intent zzmF() {
        throw new UnsupportedOperationException("Not a sign in API");
    }

    protected Bundle zzmm() {
        return new Bundle();
    }

    public boolean zzmt() {
        return false;
    }

    public Bundle zzoA() {
        return null;
    }

    public boolean zzpc() {
        return true;
    }

    protected String zzqX() {
        return "com.google.android.gms";
    }

    public final void zzqZ() {
        int iIsGooglePlayServicesAvailable = this.zzaCH.isGooglePlayServicesAvailable(this.mContext);
        if (iIsGooglePlayServicesAvailable == 0) {
            zza(new zzm(this));
        } else {
            zza(1, (IInterface) null);
            zza(new zzm(this), iIsGooglePlayServicesAvailable, (PendingIntent) null);
        }
    }

    public com.google.android.gms.common.zzc[] zzrb() {
        return new com.google.android.gms.common.zzc[0];
    }

    protected final void zzrc() {
        if (!isConnected()) {
            throw new IllegalStateException("Not connected. Call connect() and wait for onConnected() to be called.");
        }
    }

    public final T zzrd() throws DeadObjectException {
        T t;
        synchronized (this.mLock) {
            if (this.zzaGW == 5) {
                throw new DeadObjectException();
            }
            zzrc();
            zzbr.zza(this.zzaGT != null, "Client is connected but service is null");
            t = this.zzaGT;
        }
        return t;
    }

    public boolean zzre() {
        return false;
    }

    protected Set<Scope> zzrf() {
        return Collections.EMPTY_SET;
    }
}
