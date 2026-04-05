package com.google.android.gms.internal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.ArrayMap;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

/* JADX INFO: loaded from: classes67.dex */
public final class zzbga extends Fragment implements zzbff {
    private static WeakHashMap<FragmentActivity, WeakReference<zzbga>> zzaEJ = new WeakHashMap<>();
    private Bundle zzaEL;
    private Map<String, zzbfe> zzaEK = new ArrayMap();
    private int zzLj = 0;

    public static zzbga zza(FragmentActivity fragmentActivity) {
        zzbga zzbgaVar;
        WeakReference<zzbga> weakReference = zzaEJ.get(fragmentActivity);
        if (weakReference == null || (zzbgaVar = weakReference.get()) == null) {
            try {
                zzbgaVar = (zzbga) fragmentActivity.getSupportFragmentManager().findFragmentByTag("SupportLifecycleFragmentImpl");
                if (zzbgaVar == null || zzbgaVar.isRemoving()) {
                    zzbgaVar = new zzbga();
                    fragmentActivity.getSupportFragmentManager().beginTransaction().add(zzbgaVar, "SupportLifecycleFragmentImpl").commitAllowingStateLoss();
                }
                zzaEJ.put(fragmentActivity, new WeakReference<>(zzbgaVar));
            } catch (ClassCastException e) {
                throw new IllegalStateException("Fragment with tag SupportLifecycleFragmentImpl is not a SupportLifecycleFragmentImpl", e);
            }
        }
        return zzbgaVar;
    }

    @Override // android.support.v4.app.Fragment
    public final void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        super.dump(str, fileDescriptor, printWriter, strArr);
        Iterator<zzbfe> it = this.zzaEK.values().iterator();
        while (it.hasNext()) {
            it.next().dump(str, fileDescriptor, printWriter, strArr);
        }
    }

    @Override // android.support.v4.app.Fragment
    public final void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        Iterator<zzbfe> it = this.zzaEK.values().iterator();
        while (it.hasNext()) {
            it.next().onActivityResult(i, i2, intent);
        }
    }

    @Override // android.support.v4.app.Fragment
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.zzLj = 1;
        this.zzaEL = bundle;
        for (Map.Entry<String, zzbfe> entry : this.zzaEK.entrySet()) {
            entry.getValue().onCreate(bundle != null ? bundle.getBundle(entry.getKey()) : null);
        }
    }

    @Override // android.support.v4.app.Fragment
    public final void onDestroy() {
        super.onDestroy();
        this.zzLj = 5;
        Iterator<zzbfe> it = this.zzaEK.values().iterator();
        while (it.hasNext()) {
            it.next().onDestroy();
        }
    }

    @Override // android.support.v4.app.Fragment
    public final void onResume() {
        super.onResume();
        this.zzLj = 3;
        Iterator<zzbfe> it = this.zzaEK.values().iterator();
        while (it.hasNext()) {
            it.next().onResume();
        }
    }

    @Override // android.support.v4.app.Fragment
    public final void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (bundle == null) {
            return;
        }
        for (Map.Entry<String, zzbfe> entry : this.zzaEK.entrySet()) {
            Bundle bundle2 = new Bundle();
            entry.getValue().onSaveInstanceState(bundle2);
            bundle.putBundle(entry.getKey(), bundle2);
        }
    }

    @Override // android.support.v4.app.Fragment
    public final void onStart() {
        super.onStart();
        this.zzLj = 2;
        Iterator<zzbfe> it = this.zzaEK.values().iterator();
        while (it.hasNext()) {
            it.next().onStart();
        }
    }

    @Override // android.support.v4.app.Fragment
    public final void onStop() {
        super.onStop();
        this.zzLj = 4;
        Iterator<zzbfe> it = this.zzaEK.values().iterator();
        while (it.hasNext()) {
            it.next().onStop();
        }
    }

    @Override // com.google.android.gms.internal.zzbff
    public final <T extends zzbfe> T zza(String str, Class<T> cls) {
        return cls.cast(this.zzaEK.get(str));
    }

    @Override // com.google.android.gms.internal.zzbff
    public final void zza(String str, @NonNull zzbfe zzbfeVar) {
        if (this.zzaEK.containsKey(str)) {
            throw new IllegalArgumentException(new StringBuilder(String.valueOf(str).length() + 59).append("LifecycleCallback with tag ").append(str).append(" already added to this fragment.").toString());
        }
        this.zzaEK.put(str, zzbfeVar);
        if (this.zzLj > 0) {
            new Handler(Looper.getMainLooper()).post(new zzbgb(this, zzbfeVar, str));
        }
    }

    @Override // com.google.android.gms.internal.zzbff
    public final /* synthetic */ Activity zzqD() {
        return getActivity();
    }
}
