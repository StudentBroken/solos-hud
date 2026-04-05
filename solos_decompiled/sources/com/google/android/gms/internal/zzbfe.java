package com.google.android.gms.internal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
import java.io.FileDescriptor;
import java.io.PrintWriter;

/* JADX INFO: loaded from: classes67.dex */
public class zzbfe {
    protected final zzbff zzaEI;

    protected zzbfe(zzbff zzbffVar) {
        this.zzaEI = zzbffVar;
    }

    protected static zzbff zzb(zzbfd zzbfdVar) {
        return zzbfdVar.zzqA() ? zzbga.zza(zzbfdVar.zzqC()) : zzbfg.zzo(zzbfdVar.zzqB());
    }

    public static zzbff zzn(Activity activity) {
        return zzb(new zzbfd(activity));
    }

    @MainThread
    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
    }

    public final Activity getActivity() {
        return this.zzaEI.zzqD();
    }

    @MainThread
    public void onActivityResult(int i, int i2, Intent intent) {
    }

    @MainThread
    public void onCreate(Bundle bundle) {
    }

    @MainThread
    public void onDestroy() {
    }

    @MainThread
    public void onResume() {
    }

    @MainThread
    public void onSaveInstanceState(Bundle bundle) {
    }

    @MainThread
    public void onStart() {
    }

    @MainThread
    public void onStop() {
    }
}
