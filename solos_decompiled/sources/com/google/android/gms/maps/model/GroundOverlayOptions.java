package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.dynamic.IObjectWrapper;

/* JADX INFO: loaded from: classes10.dex */
public final class GroundOverlayOptions extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<GroundOverlayOptions> CREATOR = new zzd();
    public static final float NO_DIMENSION = -1.0f;
    private LatLngBounds zzblu;
    private float zzbnj;
    private float zzbno;
    private boolean zzbnp;
    private boolean zzbnq;

    @NonNull
    private BitmapDescriptor zzbnt;
    private LatLng zzbnu;
    private float zzbnv;
    private float zzbnw;
    private float zzbnx;
    private float zzbny;
    private float zzbnz;

    public GroundOverlayOptions() {
        this.zzbnp = true;
        this.zzbnx = 0.0f;
        this.zzbny = 0.5f;
        this.zzbnz = 0.5f;
        this.zzbnq = false;
    }

    GroundOverlayOptions(IBinder iBinder, LatLng latLng, float f, float f2, LatLngBounds latLngBounds, float f3, float f4, boolean z, float f5, float f6, float f7, boolean z2) {
        this.zzbnp = true;
        this.zzbnx = 0.0f;
        this.zzbny = 0.5f;
        this.zzbnz = 0.5f;
        this.zzbnq = false;
        this.zzbnt = new BitmapDescriptor(IObjectWrapper.zza.zzM(iBinder));
        this.zzbnu = latLng;
        this.zzbnv = f;
        this.zzbnw = f2;
        this.zzblu = latLngBounds;
        this.zzbnj = f3;
        this.zzbno = f4;
        this.zzbnp = z;
        this.zzbnx = f5;
        this.zzbny = f6;
        this.zzbnz = f7;
        this.zzbnq = z2;
    }

    private final GroundOverlayOptions zza(LatLng latLng, float f, float f2) {
        this.zzbnu = latLng;
        this.zzbnv = f;
        this.zzbnw = f2;
        return this;
    }

    public final GroundOverlayOptions anchor(float f, float f2) {
        this.zzbny = f;
        this.zzbnz = f2;
        return this;
    }

    public final GroundOverlayOptions bearing(float f) {
        this.zzbnj = ((f % 360.0f) + 360.0f) % 360.0f;
        return this;
    }

    public final GroundOverlayOptions clickable(boolean z) {
        this.zzbnq = z;
        return this;
    }

    public final float getAnchorU() {
        return this.zzbny;
    }

    public final float getAnchorV() {
        return this.zzbnz;
    }

    public final float getBearing() {
        return this.zzbnj;
    }

    public final LatLngBounds getBounds() {
        return this.zzblu;
    }

    public final float getHeight() {
        return this.zzbnw;
    }

    public final BitmapDescriptor getImage() {
        return this.zzbnt;
    }

    public final LatLng getLocation() {
        return this.zzbnu;
    }

    public final float getTransparency() {
        return this.zzbnx;
    }

    public final float getWidth() {
        return this.zzbnv;
    }

    public final float getZIndex() {
        return this.zzbno;
    }

    public final GroundOverlayOptions image(@NonNull BitmapDescriptor bitmapDescriptor) {
        zzbr.zzb(bitmapDescriptor, "imageDescriptor must not be null");
        this.zzbnt = bitmapDescriptor;
        return this;
    }

    public final boolean isClickable() {
        return this.zzbnq;
    }

    public final boolean isVisible() {
        return this.zzbnp;
    }

    public final GroundOverlayOptions position(LatLng latLng, float f) {
        zzbr.zza(this.zzblu == null, "Position has already been set using positionFromBounds");
        zzbr.zzb(latLng != null, "Location must be specified");
        zzbr.zzb(f >= 0.0f, "Width must be non-negative");
        return zza(latLng, f, -1.0f);
    }

    public final GroundOverlayOptions position(LatLng latLng, float f, float f2) {
        zzbr.zza(this.zzblu == null, "Position has already been set using positionFromBounds");
        zzbr.zzb(latLng != null, "Location must be specified");
        zzbr.zzb(f >= 0.0f, "Width must be non-negative");
        zzbr.zzb(f2 >= 0.0f, "Height must be non-negative");
        return zza(latLng, f, f2);
    }

    public final GroundOverlayOptions positionFromBounds(LatLngBounds latLngBounds) {
        boolean z = this.zzbnu == null;
        String strValueOf = String.valueOf(this.zzbnu);
        zzbr.zza(z, new StringBuilder(String.valueOf(strValueOf).length() + 46).append("Position has already been set using position: ").append(strValueOf).toString());
        this.zzblu = latLngBounds;
        return this;
    }

    public final GroundOverlayOptions transparency(float f) {
        zzbr.zzb(f >= 0.0f && f <= 1.0f, "Transparency must be in the range [0..1]");
        this.zzbnx = f;
        return this;
    }

    public final GroundOverlayOptions visible(boolean z) {
        this.zzbnp = z;
        return this;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, this.zzbnt.zzwd().asBinder(), false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, (Parcelable) getLocation(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 4, getWidth());
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 5, getHeight());
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 6, (Parcelable) getBounds(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 7, getBearing());
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 8, getZIndex());
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 9, isVisible());
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 10, getTransparency());
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 11, getAnchorU());
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 12, getAnchorV());
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 13, isClickable());
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }

    public final GroundOverlayOptions zIndex(float f) {
        this.zzbno = f;
        return this;
    }
}
