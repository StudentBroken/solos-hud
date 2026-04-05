package com.google.android.gms.maps;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.zzbh;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLngBounds;

/* JADX INFO: loaded from: classes10.dex */
public final class GoogleMapOptions extends com.google.android.gms.common.internal.safeparcel.zza implements ReflectedParcelable {
    public static final Parcelable.Creator<GoogleMapOptions> CREATOR = new zzz();
    private Boolean zzbmd;
    private Boolean zzbme;
    private int zzbmf;
    private CameraPosition zzbmg;
    private Boolean zzbmh;
    private Boolean zzbmi;
    private Boolean zzbmj;
    private Boolean zzbmk;
    private Boolean zzbml;
    private Boolean zzbmm;
    private Boolean zzbmn;
    private Boolean zzbmo;
    private Boolean zzbmp;
    private Float zzbmq;
    private Float zzbmr;
    private LatLngBounds zzbms;

    public GoogleMapOptions() {
        this.zzbmf = -1;
        this.zzbmq = null;
        this.zzbmr = null;
        this.zzbms = null;
    }

    GoogleMapOptions(byte b, byte b2, int i, CameraPosition cameraPosition, byte b3, byte b4, byte b5, byte b6, byte b7, byte b8, byte b9, byte b10, byte b11, Float f, Float f2, LatLngBounds latLngBounds) {
        this.zzbmf = -1;
        this.zzbmq = null;
        this.zzbmr = null;
        this.zzbms = null;
        this.zzbmd = com.google.android.gms.maps.internal.zza.zza(b);
        this.zzbme = com.google.android.gms.maps.internal.zza.zza(b2);
        this.zzbmf = i;
        this.zzbmg = cameraPosition;
        this.zzbmh = com.google.android.gms.maps.internal.zza.zza(b3);
        this.zzbmi = com.google.android.gms.maps.internal.zza.zza(b4);
        this.zzbmj = com.google.android.gms.maps.internal.zza.zza(b5);
        this.zzbmk = com.google.android.gms.maps.internal.zza.zza(b6);
        this.zzbml = com.google.android.gms.maps.internal.zza.zza(b7);
        this.zzbmm = com.google.android.gms.maps.internal.zza.zza(b8);
        this.zzbmn = com.google.android.gms.maps.internal.zza.zza(b9);
        this.zzbmo = com.google.android.gms.maps.internal.zza.zza(b10);
        this.zzbmp = com.google.android.gms.maps.internal.zza.zza(b11);
        this.zzbmq = f;
        this.zzbmr = f2;
        this.zzbms = latLngBounds;
    }

    public static GoogleMapOptions createFromAttributes(Context context, AttributeSet attributeSet) {
        if (attributeSet == null) {
            return null;
        }
        TypedArray typedArrayObtainAttributes = context.getResources().obtainAttributes(attributeSet, com.google.android.gms.R.styleable.MapAttrs);
        GoogleMapOptions googleMapOptions = new GoogleMapOptions();
        if (typedArrayObtainAttributes.hasValue(com.google.android.gms.R.styleable.MapAttrs_mapType)) {
            googleMapOptions.mapType(typedArrayObtainAttributes.getInt(com.google.android.gms.R.styleable.MapAttrs_mapType, -1));
        }
        if (typedArrayObtainAttributes.hasValue(com.google.android.gms.R.styleable.MapAttrs_zOrderOnTop)) {
            googleMapOptions.zOrderOnTop(typedArrayObtainAttributes.getBoolean(com.google.android.gms.R.styleable.MapAttrs_zOrderOnTop, false));
        }
        if (typedArrayObtainAttributes.hasValue(com.google.android.gms.R.styleable.MapAttrs_useViewLifecycle)) {
            googleMapOptions.useViewLifecycleInFragment(typedArrayObtainAttributes.getBoolean(com.google.android.gms.R.styleable.MapAttrs_useViewLifecycle, false));
        }
        if (typedArrayObtainAttributes.hasValue(com.google.android.gms.R.styleable.MapAttrs_uiCompass)) {
            googleMapOptions.compassEnabled(typedArrayObtainAttributes.getBoolean(com.google.android.gms.R.styleable.MapAttrs_uiCompass, true));
        }
        if (typedArrayObtainAttributes.hasValue(com.google.android.gms.R.styleable.MapAttrs_uiRotateGestures)) {
            googleMapOptions.rotateGesturesEnabled(typedArrayObtainAttributes.getBoolean(com.google.android.gms.R.styleable.MapAttrs_uiRotateGestures, true));
        }
        if (typedArrayObtainAttributes.hasValue(com.google.android.gms.R.styleable.MapAttrs_uiScrollGestures)) {
            googleMapOptions.scrollGesturesEnabled(typedArrayObtainAttributes.getBoolean(com.google.android.gms.R.styleable.MapAttrs_uiScrollGestures, true));
        }
        if (typedArrayObtainAttributes.hasValue(com.google.android.gms.R.styleable.MapAttrs_uiTiltGestures)) {
            googleMapOptions.tiltGesturesEnabled(typedArrayObtainAttributes.getBoolean(com.google.android.gms.R.styleable.MapAttrs_uiTiltGestures, true));
        }
        if (typedArrayObtainAttributes.hasValue(com.google.android.gms.R.styleable.MapAttrs_uiZoomGestures)) {
            googleMapOptions.zoomGesturesEnabled(typedArrayObtainAttributes.getBoolean(com.google.android.gms.R.styleable.MapAttrs_uiZoomGestures, true));
        }
        if (typedArrayObtainAttributes.hasValue(com.google.android.gms.R.styleable.MapAttrs_uiZoomControls)) {
            googleMapOptions.zoomControlsEnabled(typedArrayObtainAttributes.getBoolean(com.google.android.gms.R.styleable.MapAttrs_uiZoomControls, true));
        }
        if (typedArrayObtainAttributes.hasValue(com.google.android.gms.R.styleable.MapAttrs_liteMode)) {
            googleMapOptions.liteMode(typedArrayObtainAttributes.getBoolean(com.google.android.gms.R.styleable.MapAttrs_liteMode, false));
        }
        if (typedArrayObtainAttributes.hasValue(com.google.android.gms.R.styleable.MapAttrs_uiMapToolbar)) {
            googleMapOptions.mapToolbarEnabled(typedArrayObtainAttributes.getBoolean(com.google.android.gms.R.styleable.MapAttrs_uiMapToolbar, true));
        }
        if (typedArrayObtainAttributes.hasValue(com.google.android.gms.R.styleable.MapAttrs_ambientEnabled)) {
            googleMapOptions.ambientEnabled(typedArrayObtainAttributes.getBoolean(com.google.android.gms.R.styleable.MapAttrs_ambientEnabled, false));
        }
        if (typedArrayObtainAttributes.hasValue(com.google.android.gms.R.styleable.MapAttrs_cameraMinZoomPreference)) {
            googleMapOptions.minZoomPreference(typedArrayObtainAttributes.getFloat(com.google.android.gms.R.styleable.MapAttrs_cameraMinZoomPreference, Float.NEGATIVE_INFINITY));
        }
        if (typedArrayObtainAttributes.hasValue(com.google.android.gms.R.styleable.MapAttrs_cameraMinZoomPreference)) {
            googleMapOptions.maxZoomPreference(typedArrayObtainAttributes.getFloat(com.google.android.gms.R.styleable.MapAttrs_cameraMaxZoomPreference, Float.POSITIVE_INFINITY));
        }
        googleMapOptions.latLngBoundsForCameraTarget(LatLngBounds.createFromAttributes(context, attributeSet));
        googleMapOptions.camera(CameraPosition.createFromAttributes(context, attributeSet));
        typedArrayObtainAttributes.recycle();
        return googleMapOptions;
    }

    public final GoogleMapOptions ambientEnabled(boolean z) {
        this.zzbmp = Boolean.valueOf(z);
        return this;
    }

    public final GoogleMapOptions camera(CameraPosition cameraPosition) {
        this.zzbmg = cameraPosition;
        return this;
    }

    public final GoogleMapOptions compassEnabled(boolean z) {
        this.zzbmi = Boolean.valueOf(z);
        return this;
    }

    public final Boolean getAmbientEnabled() {
        return this.zzbmp;
    }

    public final CameraPosition getCamera() {
        return this.zzbmg;
    }

    public final Boolean getCompassEnabled() {
        return this.zzbmi;
    }

    public final LatLngBounds getLatLngBoundsForCameraTarget() {
        return this.zzbms;
    }

    public final Boolean getLiteMode() {
        return this.zzbmn;
    }

    public final Boolean getMapToolbarEnabled() {
        return this.zzbmo;
    }

    public final int getMapType() {
        return this.zzbmf;
    }

    public final Float getMaxZoomPreference() {
        return this.zzbmr;
    }

    public final Float getMinZoomPreference() {
        return this.zzbmq;
    }

    public final Boolean getRotateGesturesEnabled() {
        return this.zzbmm;
    }

    public final Boolean getScrollGesturesEnabled() {
        return this.zzbmj;
    }

    public final Boolean getTiltGesturesEnabled() {
        return this.zzbml;
    }

    public final Boolean getUseViewLifecycleInFragment() {
        return this.zzbme;
    }

    public final Boolean getZOrderOnTop() {
        return this.zzbmd;
    }

    public final Boolean getZoomControlsEnabled() {
        return this.zzbmh;
    }

    public final Boolean getZoomGesturesEnabled() {
        return this.zzbmk;
    }

    public final GoogleMapOptions latLngBoundsForCameraTarget(LatLngBounds latLngBounds) {
        this.zzbms = latLngBounds;
        return this;
    }

    public final GoogleMapOptions liteMode(boolean z) {
        this.zzbmn = Boolean.valueOf(z);
        return this;
    }

    public final GoogleMapOptions mapToolbarEnabled(boolean z) {
        this.zzbmo = Boolean.valueOf(z);
        return this;
    }

    public final GoogleMapOptions mapType(int i) {
        this.zzbmf = i;
        return this;
    }

    public final GoogleMapOptions maxZoomPreference(float f) {
        this.zzbmr = Float.valueOf(f);
        return this;
    }

    public final GoogleMapOptions minZoomPreference(float f) {
        this.zzbmq = Float.valueOf(f);
        return this;
    }

    public final GoogleMapOptions rotateGesturesEnabled(boolean z) {
        this.zzbmm = Boolean.valueOf(z);
        return this;
    }

    public final GoogleMapOptions scrollGesturesEnabled(boolean z) {
        this.zzbmj = Boolean.valueOf(z);
        return this;
    }

    public final GoogleMapOptions tiltGesturesEnabled(boolean z) {
        this.zzbml = Boolean.valueOf(z);
        return this;
    }

    public final String toString() {
        return zzbh.zzt(this).zzg("MapType", Integer.valueOf(this.zzbmf)).zzg("LiteMode", this.zzbmn).zzg("Camera", this.zzbmg).zzg("CompassEnabled", this.zzbmi).zzg("ZoomControlsEnabled", this.zzbmh).zzg("ScrollGesturesEnabled", this.zzbmj).zzg("ZoomGesturesEnabled", this.zzbmk).zzg("TiltGesturesEnabled", this.zzbml).zzg("RotateGesturesEnabled", this.zzbmm).zzg("MapToolbarEnabled", this.zzbmo).zzg("AmbientEnabled", this.zzbmp).zzg("MinZoomPreference", this.zzbmq).zzg("MaxZoomPreference", this.zzbmr).zzg("LatLngBoundsForCameraTarget", this.zzbms).zzg("ZOrderOnTop", this.zzbmd).zzg("UseViewLifecycleInFragment", this.zzbme).toString();
    }

    public final GoogleMapOptions useViewLifecycleInFragment(boolean z) {
        this.zzbme = Boolean.valueOf(z);
        return this;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, com.google.android.gms.maps.internal.zza.zzb(this.zzbmd));
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, com.google.android.gms.maps.internal.zza.zzb(this.zzbme));
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 4, getMapType());
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 5, (Parcelable) getCamera(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 6, com.google.android.gms.maps.internal.zza.zzb(this.zzbmh));
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 7, com.google.android.gms.maps.internal.zza.zzb(this.zzbmi));
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 8, com.google.android.gms.maps.internal.zza.zzb(this.zzbmj));
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 9, com.google.android.gms.maps.internal.zza.zzb(this.zzbmk));
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 10, com.google.android.gms.maps.internal.zza.zzb(this.zzbml));
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 11, com.google.android.gms.maps.internal.zza.zzb(this.zzbmm));
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 12, com.google.android.gms.maps.internal.zza.zzb(this.zzbmn));
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 14, com.google.android.gms.maps.internal.zza.zzb(this.zzbmo));
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 15, com.google.android.gms.maps.internal.zza.zzb(this.zzbmp));
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 16, getMinZoomPreference(), false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 17, getMaxZoomPreference(), false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 18, (Parcelable) getLatLngBoundsForCameraTarget(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }

    public final GoogleMapOptions zOrderOnTop(boolean z) {
        this.zzbmd = Boolean.valueOf(z);
        return this;
    }

    public final GoogleMapOptions zoomControlsEnabled(boolean z) {
        this.zzbmh = Boolean.valueOf(z);
        return this;
    }

    public final GoogleMapOptions zoomGesturesEnabled(boolean z) {
        this.zzbmk = Boolean.valueOf(z);
        return this;
    }
}
