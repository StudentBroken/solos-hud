package com.google.android.gms.internal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: loaded from: classes42.dex */
public final class aes {
    private static final AtomicReference<aes> zzbXg = new AtomicReference<>();

    private aes(Context context) {
    }

    @Nullable
    public static aes zzKo() {
        return zzbXg.get();
    }

    public static Set<String> zzKp() {
        return Collections.emptySet();
    }

    public static aes zzbL(Context context) {
        zzbXg.compareAndSet(null, new aes(context));
        return zzbXg.get();
    }

    public static void zze(@NonNull FirebaseApp firebaseApp) {
    }

    public static FirebaseOptions zzhP(@NonNull String str) {
        return null;
    }
}
