package com.google.android.gms.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.RemoteException;
import android.support.annotation.WorkerThread;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.measurement.AppMeasurement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: loaded from: classes36.dex */
public final class zzcjp extends zzciv {
    private final zzckc zzbtX;
    private zzcgp zzbtY;
    private Boolean zzbtZ;
    private final zzcgd zzbua;
    private final zzckr zzbub;
    private final List<Runnable> zzbuc;
    private final zzcgd zzbud;

    protected zzcjp(zzchx zzchxVar) {
        super(zzchxVar);
        this.zzbuc = new ArrayList();
        this.zzbub = new zzckr(zzchxVar.zzkp());
        this.zzbtX = new zzckc(this);
        this.zzbua = new zzcjq(this, zzchxVar);
        this.zzbud = new zzcju(this, zzchxVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @WorkerThread
    public final void onServiceDisconnected(ComponentName componentName) {
        super.zzjB();
        if (this.zzbtY != null) {
            this.zzbtY = null;
            super.zzwE().zzyB().zzj("Disconnected from device MeasurementService", componentName);
            super.zzjB();
            zzkZ();
        }
    }

    static /* synthetic */ zzcgp zza(zzcjp zzcjpVar, zzcgp zzcgpVar) {
        zzcjpVar.zzbtY = null;
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @WorkerThread
    public final void zzkO() {
        super.zzjB();
        this.zzbub.start();
        this.zzbua.zzs(zzcfy.zzxA());
    }

    /* JADX INFO: Access modifiers changed from: private */
    @WorkerThread
    public final void zzkP() {
        super.zzjB();
        if (isConnected()) {
            super.zzwE().zzyB().log("Inactivity, disconnecting from the service");
            disconnect();
        }
    }

    @WorkerThread
    private final void zzm(Runnable runnable) throws IllegalStateException {
        super.zzjB();
        if (isConnected()) {
            runnable.run();
        } else {
            if (this.zzbuc.size() >= zzcfy.zzxI()) {
                super.zzwE().zzyv().log("Discarding data. Max runnable queue size reached");
                return;
            }
            this.zzbuc.add(runnable);
            this.zzbud.zzs(60000L);
            zzkZ();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @WorkerThread
    public final void zzzj() {
        super.zzjB();
        super.zzwE().zzyB().zzj("Processing queued up service tasks", Integer.valueOf(this.zzbuc.size()));
        Iterator<Runnable> it = this.zzbuc.iterator();
        while (it.hasNext()) {
            try {
                it.next().run();
            } catch (Throwable th) {
                super.zzwE().zzyv().zzj("Task exception while flushing queue", th);
            }
        }
        this.zzbuc.clear();
        this.zzbud.cancel();
    }

    @WorkerThread
    public final void disconnect() {
        super.zzjB();
        zzkC();
        try {
            com.google.android.gms.common.stats.zza.zzrT();
            super.getContext().unbindService(this.zzbtX);
        } catch (IllegalArgumentException e) {
        } catch (IllegalStateException e2) {
        }
        this.zzbtY = null;
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    @WorkerThread
    public final boolean isConnected() {
        super.zzjB();
        zzkC();
        return this.zzbtY != null;
    }

    @WorkerThread
    protected final void zza(zzcgp zzcgpVar) {
        super.zzjB();
        zzbr.zzu(zzcgpVar);
        this.zzbtY = zzcgpVar;
        zzkO();
        zzzj();
    }

    @WorkerThread
    final void zza(zzcgp zzcgpVar, com.google.android.gms.common.internal.safeparcel.zza zzaVar) throws Throwable {
        super.zzjB();
        super.zzwo();
        zzkC();
        zzcfy.zzxD();
        ArrayList arrayList = new ArrayList();
        zzcfy.zzxM();
        int size = 100;
        for (int i = 0; i < 1001 && size == 100; i++) {
            List<com.google.android.gms.common.internal.safeparcel.zza> listZzbo = super.zzwx().zzbo(100);
            if (listZzbo != null) {
                arrayList.addAll(listZzbo);
                size = listZzbo.size();
            } else {
                size = 0;
            }
            if (zzaVar != null && size < 100) {
                arrayList.add(zzaVar);
            }
            ArrayList arrayList2 = arrayList;
            int size2 = arrayList2.size();
            int i2 = 0;
            while (i2 < size2) {
                Object obj = arrayList2.get(i2);
                i2++;
                com.google.android.gms.common.internal.safeparcel.zza zzaVar2 = (com.google.android.gms.common.internal.safeparcel.zza) obj;
                if (zzaVar2 instanceof zzcgl) {
                    try {
                        zzcgpVar.zza((zzcgl) zzaVar2, super.zzwt().zzdW(super.zzwE().zzyC()));
                    } catch (RemoteException e) {
                        super.zzwE().zzyv().zzj("Failed to send event to the service", e);
                    }
                } else if (zzaVar2 instanceof zzcku) {
                    try {
                        zzcgpVar.zza((zzcku) zzaVar2, super.zzwt().zzdW(super.zzwE().zzyC()));
                    } catch (RemoteException e2) {
                        super.zzwE().zzyv().zzj("Failed to send attribute to the service", e2);
                    }
                } else if (zzaVar2 instanceof zzcfw) {
                    try {
                        zzcgpVar.zza((zzcfw) zzaVar2, super.zzwt().zzdW(super.zzwE().zzyC()));
                    } catch (RemoteException e3) {
                        super.zzwE().zzyv().zzj("Failed to send conditional property to the service", e3);
                    }
                } else {
                    super.zzwE().zzyv().log("Discarding data. Unrecognized parcel type.");
                }
            }
        }
    }

    @WorkerThread
    protected final void zza(AppMeasurement.zzb zzbVar) {
        super.zzjB();
        zzkC();
        zzm(new zzcjt(this, zzbVar));
    }

    @WorkerThread
    public final void zza(AtomicReference<String> atomicReference) {
        super.zzjB();
        zzkC();
        zzm(new zzcjr(this, atomicReference));
    }

    @WorkerThread
    protected final void zza(AtomicReference<List<zzcfw>> atomicReference, String str, String str2, String str3) {
        super.zzjB();
        zzkC();
        zzm(new zzcjy(this, atomicReference, str, str2, str3));
    }

    @WorkerThread
    protected final void zza(AtomicReference<List<zzcku>> atomicReference, String str, String str2, String str3, boolean z) {
        super.zzjB();
        zzkC();
        zzm(new zzcjz(this, atomicReference, str, str2, str3, z));
    }

    @WorkerThread
    protected final void zza(AtomicReference<List<zzcku>> atomicReference, boolean z) {
        super.zzjB();
        zzkC();
        zzm(new zzckb(this, atomicReference, z));
    }

    @WorkerThread
    protected final void zzb(zzcku zzckuVar) {
        super.zzjB();
        zzkC();
        zzcfy.zzxD();
        zzm(new zzcka(this, super.zzwx().zza(zzckuVar), zzckuVar));
    }

    @WorkerThread
    protected final void zzc(zzcgl zzcglVar, String str) {
        zzbr.zzu(zzcglVar);
        super.zzjB();
        zzkC();
        zzcfy.zzxD();
        zzm(new zzcjw(this, true, super.zzwx().zza(zzcglVar), zzcglVar, str));
    }

    @WorkerThread
    protected final void zzf(zzcfw zzcfwVar) {
        zzbr.zzu(zzcfwVar);
        super.zzjB();
        zzkC();
        zzcfy.zzxD();
        zzm(new zzcjx(this, true, super.zzwx().zzc(zzcfwVar), new zzcfw(zzcfwVar), zzcfwVar));
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ void zzjB() {
        super.zzjB();
    }

    @Override // com.google.android.gms.internal.zzciv
    protected final void zzjC() {
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @WorkerThread
    final void zzkZ() {
        boolean z;
        super.zzjB();
        zzkC();
        if (isConnected()) {
            return;
        }
        if (this.zzbtZ == null) {
            this.zzbtZ = super.zzwF().zzyG();
            if (this.zzbtZ == null) {
                super.zzwE().zzyB().log("State of service unknown");
                super.zzjB();
                zzkC();
                zzcfy.zzxD();
                super.zzwE().zzyB().log("Checking service availability");
                switch (com.google.android.gms.common.zze.zzoU().isGooglePlayServicesAvailable(super.getContext())) {
                    case 0:
                        super.zzwE().zzyB().log("Service available");
                        z = true;
                        break;
                    case 1:
                        super.zzwE().zzyB().log("Service missing");
                        z = false;
                        break;
                    case 2:
                        super.zzwE().zzyA().log("Service container out of date");
                        z = true;
                        break;
                    case 3:
                        super.zzwE().zzyx().log("Service disabled");
                        z = false;
                        break;
                    case 9:
                        super.zzwE().zzyx().log("Service invalid");
                        z = false;
                        break;
                    case 18:
                        super.zzwE().zzyx().log("Service updating");
                        z = true;
                        break;
                    default:
                        z = false;
                        break;
                }
                this.zzbtZ = Boolean.valueOf(z);
                super.zzwF().zzak(this.zzbtZ.booleanValue());
            }
        }
        if (this.zzbtZ.booleanValue()) {
            super.zzwE().zzyB().log("Using measurement service");
            this.zzbtX.zzzk();
            return;
        }
        zzcfy.zzxD();
        List<ResolveInfo> listQueryIntentServices = super.getContext().getPackageManager().queryIntentServices(new Intent().setClassName(super.getContext(), "com.google.android.gms.measurement.AppMeasurementService"), 65536);
        if (!(listQueryIntentServices != null && listQueryIntentServices.size() > 0)) {
            super.zzwE().zzyv().log("Unable to use remote or local measurement implementation. Please register the AppMeasurementService service in the app manifest");
            return;
        }
        super.zzwE().zzyB().log("Using local app measurement service");
        Intent intent = new Intent("com.google.android.gms.measurement.START");
        Context context = super.getContext();
        zzcfy.zzxD();
        intent.setComponent(new ComponentName(context, "com.google.android.gms.measurement.AppMeasurementService"));
        this.zzbtX.zzk(intent);
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ com.google.android.gms.common.util.zzf zzkp() {
        return super.zzkp();
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

    @WorkerThread
    protected final void zzzh() {
        super.zzjB();
        zzkC();
        zzm(new zzcjv(this));
    }

    @WorkerThread
    protected final void zzzi() {
        super.zzjB();
        zzkC();
        zzm(new zzcjs(this));
    }
}
