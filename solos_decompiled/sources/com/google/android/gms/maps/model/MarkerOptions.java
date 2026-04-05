package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.dynamic.IObjectWrapper;

/* JADX INFO: loaded from: classes10.dex */
public final class MarkerOptions extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<MarkerOptions> CREATOR = new zzh();
    private float mAlpha;
    private String zzaoA;
    private LatLng zzbmR;
    private String zzbnI;
    private BitmapDescriptor zzbnJ;
    private boolean zzbnK;
    private boolean zzbnL;
    private float zzbnM;
    private float zzbnN;
    private float zzbnO;
    private float zzbno;
    private boolean zzbnp;
    private float zzbny;
    private float zzbnz;

    public MarkerOptions() {
        this.zzbny = 0.5f;
        this.zzbnz = 1.0f;
        this.zzbnp = true;
        this.zzbnL = false;
        this.zzbnM = 0.0f;
        this.zzbnN = 0.5f;
        this.zzbnO = 0.0f;
        this.mAlpha = 1.0f;
    }

    MarkerOptions(LatLng latLng, String str, String str2, IBinder iBinder, float f, float f2, boolean z, boolean z2, boolean z3, float f3, float f4, float f5, float f6, float f7) {
        this.zzbny = 0.5f;
        this.zzbnz = 1.0f;
        this.zzbnp = true;
        this.zzbnL = false;
        this.zzbnM = 0.0f;
        this.zzbnN = 0.5f;
        this.zzbnO = 0.0f;
        this.mAlpha = 1.0f;
        this.zzbmR = latLng;
        this.zzaoA = str;
        this.zzbnI = str2;
        if (iBinder == null) {
            this.zzbnJ = null;
        } else {
            this.zzbnJ = new BitmapDescriptor(IObjectWrapper.zza.zzM(iBinder));
        }
        this.zzbny = f;
        this.zzbnz = f2;
        this.zzbnK = z;
        this.zzbnp = z2;
        this.zzbnL = z3;
        this.zzbnM = f3;
        this.zzbnN = f4;
        this.zzbnO = f5;
        this.mAlpha = f6;
        this.zzbno = f7;
    }

    public final MarkerOptions alpha(float f) {
        this.mAlpha = f;
        return this;
    }

    public final MarkerOptions anchor(float f, float f2) {
        this.zzbny = f;
        this.zzbnz = f2;
        return this;
    }

    public final MarkerOptions draggable(boolean z) {
        this.zzbnK = z;
        return this;
    }

    public final MarkerOptions flat(boolean z) {
        this.zzbnL = z;
        return this;
    }

    public final float getAlpha() {
        return this.mAlpha;
    }

    public final float getAnchorU() {
        return this.zzbny;
    }

    public final float getAnchorV() {
        return this.zzbnz;
    }

    public final BitmapDescriptor getIcon() {
        return this.zzbnJ;
    }

    public final float getInfoWindowAnchorU() {
        return this.zzbnN;
    }

    public final float getInfoWindowAnchorV() {
        return this.zzbnO;
    }

    public final LatLng getPosition() {
        return this.zzbmR;
    }

    public final float getRotation() {
        return this.zzbnM;
    }

    public final String getSnippet() {
        return this.zzbnI;
    }

    public final String getTitle() {
        return this.zzaoA;
    }

    public final float getZIndex() {
        return this.zzbno;
    }

    public final MarkerOptions icon(@Nullable BitmapDescriptor bitmapDescriptor) {
        this.zzbnJ = bitmapDescriptor;
        return this;
    }

    public final MarkerOptions infoWindowAnchor(float f, float f2) {
        this.zzbnN = f;
        this.zzbnO = f2;
        return this;
    }

    public final boolean isDraggable() {
        return this.zzbnK;
    }

    public final boolean isFlat() {
        return this.zzbnL;
    }

    public final boolean isVisible() {
        return this.zzbnp;
    }

    public final MarkerOptions position(@NonNull LatLng latLng) {
        if (latLng == null) {
            throw new IllegalArgumentException("latlng cannot be null - a position is required.");
        }
        this.zzbmR = latLng;
        return this;
    }

    public final MarkerOptions rotation(float f) {
        this.zzbnM = f;
        return this;
    }

    public final MarkerOptions snippet(@Nullable String str) {
        this.zzbnI = str;
        return this;
    }

    public final MarkerOptions title(@Nullable String str) {
        this.zzaoA = str;
        return this;
    }

    public final MarkerOptions visible(boolean z) {
        this.zzbnp = z;
        return this;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, (Parcelable) getPosition(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, getTitle(), false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 4, getSnippet(), false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 5, this.zzbnJ == null ? null : this.zzbnJ.zzwd().asBinder(), false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 6, getAnchorU());
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 7, getAnchorV());
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 8, isDraggable());
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 9, isVisible());
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 10, isFlat());
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 11, getRotation());
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 12, getInfoWindowAnchorU());
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 13, getInfoWindowAnchorV());
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 14, getAlpha());
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 15, getZIndex());
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }

    public final MarkerOptions zIndex(float f) {
        this.zzbno = f;
        return this;
    }
}
