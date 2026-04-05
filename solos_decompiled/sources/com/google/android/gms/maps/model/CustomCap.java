package com.google.android.gms.maps.model;

import android.support.annotation.NonNull;
import com.google.android.gms.common.internal.zzbr;

/* JADX INFO: loaded from: classes10.dex */
public final class CustomCap extends Cap {
    public final BitmapDescriptor bitmapDescriptor;
    public final float refWidth;

    public CustomCap(@NonNull BitmapDescriptor bitmapDescriptor) {
        this(bitmapDescriptor, 10.0f);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public CustomCap(@NonNull BitmapDescriptor bitmapDescriptor, float f) {
        BitmapDescriptor bitmapDescriptor2 = (BitmapDescriptor) zzbr.zzb(bitmapDescriptor, "bitmapDescriptor must not be null");
        if (f <= 0.0f) {
            throw new IllegalArgumentException("refWidth must be positive");
        }
        super(bitmapDescriptor2, f);
        this.bitmapDescriptor = bitmapDescriptor;
        this.refWidth = f;
    }

    @Override // com.google.android.gms.maps.model.Cap
    public final String toString() {
        String strValueOf = String.valueOf(this.bitmapDescriptor);
        return new StringBuilder(String.valueOf(strValueOf).length() + 55).append("[CustomCap: bitmapDescriptor=").append(strValueOf).append(" refWidth=").append(this.refWidth).append("]").toString();
    }
}
