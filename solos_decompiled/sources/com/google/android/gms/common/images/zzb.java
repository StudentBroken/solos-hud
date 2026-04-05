package com.google.android.gms.common.images;

import android.net.Uri;
import com.google.android.gms.common.internal.zzbh;
import java.util.Arrays;

/* JADX INFO: loaded from: classes3.dex */
final class zzb {
    public final Uri uri;

    public zzb(Uri uri) {
        this.uri = uri;
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof zzb)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        return zzbh.equal(((zzb) obj).uri, this.uri);
    }

    public final int hashCode() {
        return Arrays.hashCode(new Object[]{this.uri});
    }
}
