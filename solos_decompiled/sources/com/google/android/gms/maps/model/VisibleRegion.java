package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.zzbh;
import java.util.Arrays;

/* JADX INFO: loaded from: classes10.dex */
public final class VisibleRegion extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<VisibleRegion> CREATOR = new zzu();
    public final LatLng farLeft;
    public final LatLng farRight;
    public final LatLngBounds latLngBounds;
    public final LatLng nearLeft;
    public final LatLng nearRight;

    public VisibleRegion(LatLng latLng, LatLng latLng2, LatLng latLng3, LatLng latLng4, LatLngBounds latLngBounds) {
        this.nearLeft = latLng;
        this.nearRight = latLng2;
        this.farLeft = latLng3;
        this.farRight = latLng4;
        this.latLngBounds = latLngBounds;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof VisibleRegion)) {
            return false;
        }
        VisibleRegion visibleRegion = (VisibleRegion) obj;
        return this.nearLeft.equals(visibleRegion.nearLeft) && this.nearRight.equals(visibleRegion.nearRight) && this.farLeft.equals(visibleRegion.farLeft) && this.farRight.equals(visibleRegion.farRight) && this.latLngBounds.equals(visibleRegion.latLngBounds);
    }

    public final int hashCode() {
        return Arrays.hashCode(new Object[]{this.nearLeft, this.nearRight, this.farLeft, this.farRight, this.latLngBounds});
    }

    public final String toString() {
        return zzbh.zzt(this).zzg("nearLeft", this.nearLeft).zzg("nearRight", this.nearRight).zzg("farLeft", this.farLeft).zzg("farRight", this.farRight).zzg("latLngBounds", this.latLngBounds).toString();
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, (Parcelable) this.nearLeft, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, (Parcelable) this.nearRight, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 4, (Parcelable) this.farLeft, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 5, (Parcelable) this.farRight, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 6, (Parcelable) this.latLngBounds, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
