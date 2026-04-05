package com.google.android.gms.maps;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.zzbh;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;

/* JADX INFO: loaded from: classes10.dex */
public final class StreetViewPanoramaOptions extends com.google.android.gms.common.internal.safeparcel.zza implements ReflectedParcelable {
    public static final Parcelable.Creator<StreetViewPanoramaOptions> CREATOR = new zzah();
    private StreetViewPanoramaCamera zzbmP;
    private String zzbmQ;
    private LatLng zzbmR;
    private Integer zzbmS;
    private Boolean zzbmT;
    private Boolean zzbmU;
    private Boolean zzbmV;
    private Boolean zzbme;
    private Boolean zzbmk;

    public StreetViewPanoramaOptions() {
        this.zzbmT = true;
        this.zzbmk = true;
        this.zzbmU = true;
        this.zzbmV = true;
    }

    StreetViewPanoramaOptions(StreetViewPanoramaCamera streetViewPanoramaCamera, String str, LatLng latLng, Integer num, byte b, byte b2, byte b3, byte b4, byte b5) {
        this.zzbmT = true;
        this.zzbmk = true;
        this.zzbmU = true;
        this.zzbmV = true;
        this.zzbmP = streetViewPanoramaCamera;
        this.zzbmR = latLng;
        this.zzbmS = num;
        this.zzbmQ = str;
        this.zzbmT = com.google.android.gms.maps.internal.zza.zza(b);
        this.zzbmk = com.google.android.gms.maps.internal.zza.zza(b2);
        this.zzbmU = com.google.android.gms.maps.internal.zza.zza(b3);
        this.zzbmV = com.google.android.gms.maps.internal.zza.zza(b4);
        this.zzbme = com.google.android.gms.maps.internal.zza.zza(b5);
    }

    public final Boolean getPanningGesturesEnabled() {
        return this.zzbmU;
    }

    public final String getPanoramaId() {
        return this.zzbmQ;
    }

    public final LatLng getPosition() {
        return this.zzbmR;
    }

    public final Integer getRadius() {
        return this.zzbmS;
    }

    public final Boolean getStreetNamesEnabled() {
        return this.zzbmV;
    }

    public final StreetViewPanoramaCamera getStreetViewPanoramaCamera() {
        return this.zzbmP;
    }

    public final Boolean getUseViewLifecycleInFragment() {
        return this.zzbme;
    }

    public final Boolean getUserNavigationEnabled() {
        return this.zzbmT;
    }

    public final Boolean getZoomGesturesEnabled() {
        return this.zzbmk;
    }

    public final StreetViewPanoramaOptions panningGesturesEnabled(boolean z) {
        this.zzbmU = Boolean.valueOf(z);
        return this;
    }

    public final StreetViewPanoramaOptions panoramaCamera(StreetViewPanoramaCamera streetViewPanoramaCamera) {
        this.zzbmP = streetViewPanoramaCamera;
        return this;
    }

    public final StreetViewPanoramaOptions panoramaId(String str) {
        this.zzbmQ = str;
        return this;
    }

    public final StreetViewPanoramaOptions position(LatLng latLng) {
        this.zzbmR = latLng;
        return this;
    }

    public final StreetViewPanoramaOptions position(LatLng latLng, Integer num) {
        this.zzbmR = latLng;
        this.zzbmS = num;
        return this;
    }

    public final StreetViewPanoramaOptions streetNamesEnabled(boolean z) {
        this.zzbmV = Boolean.valueOf(z);
        return this;
    }

    public final String toString() {
        return zzbh.zzt(this).zzg("PanoramaId", this.zzbmQ).zzg("Position", this.zzbmR).zzg("Radius", this.zzbmS).zzg("StreetViewPanoramaCamera", this.zzbmP).zzg("UserNavigationEnabled", this.zzbmT).zzg("ZoomGesturesEnabled", this.zzbmk).zzg("PanningGesturesEnabled", this.zzbmU).zzg("StreetNamesEnabled", this.zzbmV).zzg("UseViewLifecycleInFragment", this.zzbme).toString();
    }

    public final StreetViewPanoramaOptions useViewLifecycleInFragment(boolean z) {
        this.zzbme = Boolean.valueOf(z);
        return this;
    }

    public final StreetViewPanoramaOptions userNavigationEnabled(boolean z) {
        this.zzbmT = Boolean.valueOf(z);
        return this;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, (Parcelable) getStreetViewPanoramaCamera(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, getPanoramaId(), false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 4, (Parcelable) getPosition(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 5, getRadius(), false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 6, com.google.android.gms.maps.internal.zza.zzb(this.zzbmT));
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 7, com.google.android.gms.maps.internal.zza.zzb(this.zzbmk));
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 8, com.google.android.gms.maps.internal.zza.zzb(this.zzbmU));
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 9, com.google.android.gms.maps.internal.zza.zzb(this.zzbmV));
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 10, com.google.android.gms.maps.internal.zza.zzb(this.zzbme));
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }

    public final StreetViewPanoramaOptions zoomGesturesEnabled(boolean z) {
        this.zzbmk = Boolean.valueOf(z);
        return this;
    }
}
