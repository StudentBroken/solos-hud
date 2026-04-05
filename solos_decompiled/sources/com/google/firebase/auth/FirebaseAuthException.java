package com.google.firebase.auth;

import android.support.annotation.NonNull;
import com.google.android.gms.common.internal.zzbr;
import com.google.firebase.FirebaseException;

/* JADX INFO: loaded from: classes42.dex */
public class FirebaseAuthException extends FirebaseException {
    private final String zzbYj;

    public FirebaseAuthException(@NonNull String str, @NonNull String str2) {
        super(str2);
        this.zzbYj = zzbr.zzcF(str);
    }

    @NonNull
    public String getErrorCode() {
        return this.zzbYj;
    }
}
