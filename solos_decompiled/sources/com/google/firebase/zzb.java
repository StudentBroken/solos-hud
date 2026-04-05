package com.google.firebase;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.zzbfy;

/* JADX INFO: loaded from: classes67.dex */
public final class zzb implements zzbfy {
    @Override // com.google.android.gms.internal.zzbfy
    public final Exception zzq(Status status) {
        return status.getStatusCode() == 8 ? new FirebaseException(status.zzpo()) : new FirebaseApiNotAvailableException(status.zzpo());
    }
}
