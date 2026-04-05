package com.google.android.gms.internal;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

/* JADX INFO: loaded from: classes67.dex */
public interface zzbff {
    void startActivityForResult(Intent intent, int i);

    <T extends zzbfe> T zza(String str, Class<T> cls);

    void zza(String str, @NonNull zzbfe zzbfeVar);

    Activity zzqD();
}
