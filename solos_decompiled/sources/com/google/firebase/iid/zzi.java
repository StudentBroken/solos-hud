package com.google.firebase.iid;

import android.support.annotation.Nullable;

/* JADX INFO: loaded from: classes35.dex */
@Deprecated
public final class zzi {
    private final FirebaseInstanceId zzcno;

    private zzi(FirebaseInstanceId firebaseInstanceId) {
        this.zzcno = firebaseInstanceId;
    }

    public static zzi zzKe() {
        return new zzi(FirebaseInstanceId.getInstance());
    }

    public final String getId() {
        return this.zzcno.getId();
    }

    @Nullable
    public final String getToken() {
        return this.zzcno.getToken();
    }
}
