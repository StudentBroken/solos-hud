package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.zzbh;
import java.util.Arrays;

/* JADX INFO: loaded from: classes10.dex */
public class StreetViewPanoramaLocation extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<StreetViewPanoramaLocation> CREATOR = new zzo();
    public final StreetViewPanoramaLink[] links;
    public final String panoId;
    public final LatLng position;

    public StreetViewPanoramaLocation(StreetViewPanoramaLink[] streetViewPanoramaLinkArr, LatLng latLng, String str) {
        this.links = streetViewPanoramaLinkArr;
        this.position = latLng;
        this.panoId = str;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StreetViewPanoramaLocation)) {
            return false;
        }
        StreetViewPanoramaLocation streetViewPanoramaLocation = (StreetViewPanoramaLocation) obj;
        return this.panoId.equals(streetViewPanoramaLocation.panoId) && this.position.equals(streetViewPanoramaLocation.position);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.position, this.panoId});
    }

    public String toString() {
        return zzbh.zzt(this).zzg("panoId", this.panoId).zzg("position", this.position.toString()).toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, (Parcelable[]) this.links, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, (Parcelable) this.position, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 4, this.panoId, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
