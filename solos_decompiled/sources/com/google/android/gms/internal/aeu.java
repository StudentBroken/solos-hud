package com.google.android.gms.internal;

import android.support.annotation.Nullable;
import com.google.android.gms.common.internal.zzbh;
import java.util.Arrays;

/* JADX INFO: loaded from: classes42.dex */
public final class aeu {
    private String zzakx;

    public aeu(@Nullable String str) {
        this.zzakx = str;
    }

    public final boolean equals(Object obj) {
        if (obj instanceof aeu) {
            return zzbh.equal(this.zzakx, ((aeu) obj).zzakx);
        }
        return false;
    }

    @Nullable
    public final String getToken() {
        return this.zzakx;
    }

    public final int hashCode() {
        return Arrays.hashCode(new Object[]{this.zzakx});
    }

    public final String toString() {
        return zzbh.zzt(this).zzg("token", this.zzakx).toString();
    }
}
