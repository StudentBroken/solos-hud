package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: loaded from: classes3.dex */
public abstract class zzbcm extends zzbfe implements DialogInterface.OnCancelListener {
    protected volatile boolean mStarted;
    protected final AtomicReference<zzbcn> zzaBP;
    private final Handler zzaBQ;
    protected final GoogleApiAvailability zzaBf;

    protected zzbcm(zzbff zzbffVar) {
        this(zzbffVar, GoogleApiAvailability.getInstance());
    }

    private zzbcm(zzbff zzbffVar, GoogleApiAvailability googleApiAvailability) {
        super(zzbffVar);
        this.zzaBP = new AtomicReference<>(null);
        this.zzaBQ = new Handler(Looper.getMainLooper());
        this.zzaBf = googleApiAvailability;
    }

    private static int zza(@Nullable zzbcn zzbcnVar) {
        if (zzbcnVar == null) {
            return -1;
        }
        return zzbcnVar.zzpw();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:23:0x005a  */
    /* JADX WARN: Removed duplicated region for block: B:4:0x0011  */
    /* JADX WARN: Removed duplicated region for block: B:6:0x0014  */
    @Override // com.google.android.gms.internal.zzbfe
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onActivityResult(int r7, int r8, android.content.Intent r9) {
        /*
            r6 = this;
            r5 = 18
            r1 = 13
            r2 = 1
            r3 = 0
            java.util.concurrent.atomic.AtomicReference<com.google.android.gms.internal.zzbcn> r0 = r6.zzaBP
            java.lang.Object r0 = r0.get()
            com.google.android.gms.internal.zzbcn r0 = (com.google.android.gms.internal.zzbcn) r0
            switch(r7) {
                case 1: goto L34;
                case 2: goto L18;
                default: goto L11;
            }
        L11:
            r1 = r3
        L12:
            if (r1 == 0) goto L5a
            r6.zzpv()
        L17:
            return
        L18:
            com.google.android.gms.common.GoogleApiAvailability r1 = r6.zzaBf
            android.app.Activity r4 = r6.getActivity()
            int r4 = r1.isGooglePlayServicesAvailable(r4)
            if (r4 != 0) goto L68
            r1 = r2
        L25:
            if (r0 == 0) goto L17
            com.google.android.gms.common.ConnectionResult r2 = r0.zzpx()
            int r2 = r2.getErrorCode()
            if (r2 != r5) goto L12
            if (r4 != r5) goto L12
            goto L17
        L34:
            r4 = -1
            if (r8 != r4) goto L39
            r1 = r2
            goto L12
        L39:
            if (r8 != 0) goto L11
            if (r9 == 0) goto L43
            java.lang.String r2 = "<<ResolutionFailureErrorDetail>>"
            int r1 = r9.getIntExtra(r2, r1)
        L43:
            com.google.android.gms.internal.zzbcn r2 = new com.google.android.gms.internal.zzbcn
            com.google.android.gms.common.ConnectionResult r4 = new com.google.android.gms.common.ConnectionResult
            r5 = 0
            r4.<init>(r1, r5)
            int r0 = zza(r0)
            r2.<init>(r4, r0)
            java.util.concurrent.atomic.AtomicReference<com.google.android.gms.internal.zzbcn> r0 = r6.zzaBP
            r0.set(r2)
            r0 = r2
            r1 = r3
            goto L12
        L5a:
            if (r0 == 0) goto L17
            com.google.android.gms.common.ConnectionResult r1 = r0.zzpx()
            int r0 = r0.zzpw()
            r6.zza(r1, r0)
            goto L17
        L68:
            r1 = r3
            goto L25
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzbcm.onActivityResult(int, int, android.content.Intent):void");
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialogInterface) {
        zza(new ConnectionResult(13, null), zza(this.zzaBP.get()));
        zzpv();
    }

    @Override // com.google.android.gms.internal.zzbfe
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            this.zzaBP.set(bundle.getBoolean("resolving_error", false) ? new zzbcn(new ConnectionResult(bundle.getInt("failed_status"), (PendingIntent) bundle.getParcelable("failed_resolution")), bundle.getInt("failed_client_id", -1)) : null);
        }
    }

    @Override // com.google.android.gms.internal.zzbfe
    public final void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        zzbcn zzbcnVar = this.zzaBP.get();
        if (zzbcnVar != null) {
            bundle.putBoolean("resolving_error", true);
            bundle.putInt("failed_client_id", zzbcnVar.zzpw());
            bundle.putInt("failed_status", zzbcnVar.zzpx().getErrorCode());
            bundle.putParcelable("failed_resolution", zzbcnVar.zzpx().getResolution());
        }
    }

    @Override // com.google.android.gms.internal.zzbfe
    public void onStart() {
        super.onStart();
        this.mStarted = true;
    }

    @Override // com.google.android.gms.internal.zzbfe
    public void onStop() {
        super.onStop();
        this.mStarted = false;
    }

    protected abstract void zza(ConnectionResult connectionResult, int i);

    public final void zzb(ConnectionResult connectionResult, int i) {
        zzbcn zzbcnVar = new zzbcn(connectionResult, i);
        if (this.zzaBP.compareAndSet(null, zzbcnVar)) {
            this.zzaBQ.post(new zzbco(this, zzbcnVar));
        }
    }

    protected abstract void zzpq();

    protected final void zzpv() {
        this.zzaBP.set(null);
        zzpq();
    }
}
