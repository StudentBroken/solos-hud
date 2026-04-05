package com.google.android.gms.maps.model;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import com.google.android.gms.R;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.zzbh;
import com.google.android.gms.common.internal.zzbr;
import java.util.Arrays;

/* JADX INFO: loaded from: classes10.dex */
public final class LatLngBounds extends com.google.android.gms.common.internal.safeparcel.zza implements ReflectedParcelable {
    public static final Parcelable.Creator<LatLngBounds> CREATOR = new zze();
    public final LatLng northeast;
    public final LatLng southwest;

    public static final class Builder {
        private double zzbnC = Double.POSITIVE_INFINITY;
        private double zzbnD = Double.NEGATIVE_INFINITY;
        private double zzbnE = Double.NaN;
        private double zzbnF = Double.NaN;

        public final LatLngBounds build() {
            zzbr.zza(!Double.isNaN(this.zzbnE), "no included points");
            return new LatLngBounds(new LatLng(this.zzbnC, this.zzbnE), new LatLng(this.zzbnD, this.zzbnF));
        }

        public final Builder include(LatLng latLng) {
            boolean z = true;
            this.zzbnC = Math.min(this.zzbnC, latLng.latitude);
            this.zzbnD = Math.max(this.zzbnD, latLng.latitude);
            double d = latLng.longitude;
            if (!Double.isNaN(this.zzbnE)) {
                if (this.zzbnE <= this.zzbnF) {
                    if (this.zzbnE > d || d > this.zzbnF) {
                        z = false;
                    }
                } else if (this.zzbnE > d && d > this.zzbnF) {
                    z = false;
                }
                if (!z) {
                    if (LatLngBounds.zza(this.zzbnE, d) < LatLngBounds.zzb(this.zzbnF, d)) {
                        this.zzbnE = d;
                    }
                }
                return this;
            }
            this.zzbnE = d;
            this.zzbnF = d;
            return this;
        }
    }

    public LatLngBounds(LatLng latLng, LatLng latLng2) {
        zzbr.zzb(latLng, "null southwest");
        zzbr.zzb(latLng2, "null northeast");
        zzbr.zzb(latLng2.latitude >= latLng.latitude, "southern latitude exceeds northern latitude (%s > %s)", Double.valueOf(latLng.latitude), Double.valueOf(latLng2.latitude));
        this.southwest = latLng;
        this.northeast = latLng2;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static LatLngBounds createFromAttributes(Context context, AttributeSet attributeSet) {
        if (context == null || attributeSet == null) {
            return null;
        }
        TypedArray typedArrayObtainAttributes = context.getResources().obtainAttributes(attributeSet, R.styleable.MapAttrs);
        Float fValueOf = typedArrayObtainAttributes.hasValue(R.styleable.MapAttrs_latLngBoundsSouthWestLatitude) ? Float.valueOf(typedArrayObtainAttributes.getFloat(R.styleable.MapAttrs_latLngBoundsSouthWestLatitude, 0.0f)) : null;
        Float fValueOf2 = typedArrayObtainAttributes.hasValue(R.styleable.MapAttrs_latLngBoundsSouthWestLongitude) ? Float.valueOf(typedArrayObtainAttributes.getFloat(R.styleable.MapAttrs_latLngBoundsSouthWestLongitude, 0.0f)) : null;
        Float fValueOf3 = typedArrayObtainAttributes.hasValue(R.styleable.MapAttrs_latLngBoundsNorthEastLatitude) ? Float.valueOf(typedArrayObtainAttributes.getFloat(R.styleable.MapAttrs_latLngBoundsNorthEastLatitude, 0.0f)) : null;
        Float fValueOf4 = typedArrayObtainAttributes.hasValue(R.styleable.MapAttrs_latLngBoundsNorthEastLongitude) ? Float.valueOf(typedArrayObtainAttributes.getFloat(R.styleable.MapAttrs_latLngBoundsNorthEastLongitude, 0.0f)) : null;
        if (fValueOf == null || fValueOf2 == null || fValueOf3 == null || fValueOf4 == null) {
            return null;
        }
        return new LatLngBounds(new LatLng(fValueOf.floatValue(), fValueOf2.floatValue()), new LatLng(fValueOf3.floatValue(), fValueOf4.floatValue()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static double zza(double d, double d2) {
        return ((d - d2) + 360.0d) % 360.0d;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static double zzb(double d, double d2) {
        return ((d2 - d) + 360.0d) % 360.0d;
    }

    private final boolean zzg(double d) {
        return this.southwest.longitude <= this.northeast.longitude ? this.southwest.longitude <= d && d <= this.northeast.longitude : this.southwest.longitude <= d || d <= this.northeast.longitude;
    }

    public final boolean contains(LatLng latLng) {
        double d = latLng.latitude;
        return ((this.southwest.latitude > d ? 1 : (this.southwest.latitude == d ? 0 : -1)) <= 0 && (d > this.northeast.latitude ? 1 : (d == this.northeast.latitude ? 0 : -1)) <= 0) && zzg(latLng.longitude);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LatLngBounds)) {
            return false;
        }
        LatLngBounds latLngBounds = (LatLngBounds) obj;
        return this.southwest.equals(latLngBounds.southwest) && this.northeast.equals(latLngBounds.northeast);
    }

    public final LatLng getCenter() {
        double d = (this.southwest.latitude + this.northeast.latitude) / 2.0d;
        double d2 = this.northeast.longitude;
        double d3 = this.southwest.longitude;
        return new LatLng(d, d3 <= d2 ? (d2 + d3) / 2.0d : ((d2 + 360.0d) + d3) / 2.0d);
    }

    public final int hashCode() {
        return Arrays.hashCode(new Object[]{this.southwest, this.northeast});
    }

    public final LatLngBounds including(LatLng latLng) {
        double d;
        double dMin = Math.min(this.southwest.latitude, latLng.latitude);
        double dMax = Math.max(this.northeast.latitude, latLng.latitude);
        double d2 = this.northeast.longitude;
        double d3 = this.southwest.longitude;
        double d4 = latLng.longitude;
        if (zzg(d4)) {
            d4 = d3;
            d = d2;
        } else if (zza(d3, d4) < zzb(d2, d4)) {
            d = d2;
        } else {
            d = d4;
            d4 = d3;
        }
        return new LatLngBounds(new LatLng(dMin, d4), new LatLng(dMax, d));
    }

    public final String toString() {
        return zzbh.zzt(this).zzg("southwest", this.southwest).zzg("northeast", this.northeast).toString();
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, (Parcelable) this.southwest, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, (Parcelable) this.northeast, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
