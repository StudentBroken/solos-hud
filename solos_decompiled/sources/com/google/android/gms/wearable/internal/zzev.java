package com.google.android.gms.wearable.internal;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.WearableStatusCodes;

/* JADX INFO: loaded from: classes6.dex */
public final class zzev {
    public static Status zzaY(int i) {
        return new Status(i, WearableStatusCodes.getStatusCodeString(i));
    }
}
