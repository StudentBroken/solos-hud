package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.zzbh;
import com.ua.sdk.datapoint.BaseDataTypes;
import java.util.Arrays;

/* JADX INFO: loaded from: classes10.dex */
public class StreetViewPanoramaLink extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<StreetViewPanoramaLink> CREATOR = new zzn();
    public final float bearing;
    public final String panoId;

    public StreetViewPanoramaLink(String str, float f) {
        this.panoId = str;
        this.bearing = (((double) f) <= 0.0d ? (f % 360.0f) + 360.0f : f) % 360.0f;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StreetViewPanoramaLink)) {
            return false;
        }
        StreetViewPanoramaLink streetViewPanoramaLink = (StreetViewPanoramaLink) obj;
        return this.panoId.equals(streetViewPanoramaLink.panoId) && Float.floatToIntBits(this.bearing) == Float.floatToIntBits(streetViewPanoramaLink.bearing);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.panoId, Float.valueOf(this.bearing)});
    }

    public String toString() {
        return zzbh.zzt(this).zzg("panoId", this.panoId).zzg(BaseDataTypes.ID_BEARING, Float.valueOf(this.bearing)).toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, this.panoId, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, this.bearing);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
