package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import java.util.List;

/* JADX INFO: loaded from: classes10.dex */
public final class CircleOptions extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<CircleOptions> CREATOR = new zzc();
    private int mFillColor;
    private int mStrokeColor;
    private float mStrokeWidth;
    private LatLng zzbnm;
    private double zzbnn;
    private float zzbno;
    private boolean zzbnp;
    private boolean zzbnq;

    @Nullable
    private List<PatternItem> zzbnr;

    public CircleOptions() {
        this.zzbnm = null;
        this.zzbnn = 0.0d;
        this.mStrokeWidth = 10.0f;
        this.mStrokeColor = ViewCompat.MEASURED_STATE_MASK;
        this.mFillColor = 0;
        this.zzbno = 0.0f;
        this.zzbnp = true;
        this.zzbnq = false;
        this.zzbnr = null;
    }

    CircleOptions(LatLng latLng, double d, float f, int i, int i2, float f2, boolean z, boolean z2, @Nullable List<PatternItem> list) {
        this.zzbnm = null;
        this.zzbnn = 0.0d;
        this.mStrokeWidth = 10.0f;
        this.mStrokeColor = ViewCompat.MEASURED_STATE_MASK;
        this.mFillColor = 0;
        this.zzbno = 0.0f;
        this.zzbnp = true;
        this.zzbnq = false;
        this.zzbnr = null;
        this.zzbnm = latLng;
        this.zzbnn = d;
        this.mStrokeWidth = f;
        this.mStrokeColor = i;
        this.mFillColor = i2;
        this.zzbno = f2;
        this.zzbnp = z;
        this.zzbnq = z2;
        this.zzbnr = list;
    }

    public final CircleOptions center(LatLng latLng) {
        this.zzbnm = latLng;
        return this;
    }

    public final CircleOptions clickable(boolean z) {
        this.zzbnq = z;
        return this;
    }

    public final CircleOptions fillColor(int i) {
        this.mFillColor = i;
        return this;
    }

    public final LatLng getCenter() {
        return this.zzbnm;
    }

    public final int getFillColor() {
        return this.mFillColor;
    }

    public final double getRadius() {
        return this.zzbnn;
    }

    public final int getStrokeColor() {
        return this.mStrokeColor;
    }

    @Nullable
    public final List<PatternItem> getStrokePattern() {
        return this.zzbnr;
    }

    public final float getStrokeWidth() {
        return this.mStrokeWidth;
    }

    public final float getZIndex() {
        return this.zzbno;
    }

    public final boolean isClickable() {
        return this.zzbnq;
    }

    public final boolean isVisible() {
        return this.zzbnp;
    }

    public final CircleOptions radius(double d) {
        this.zzbnn = d;
        return this;
    }

    public final CircleOptions strokeColor(int i) {
        this.mStrokeColor = i;
        return this;
    }

    public final CircleOptions strokePattern(@Nullable List<PatternItem> list) {
        this.zzbnr = list;
        return this;
    }

    public final CircleOptions strokeWidth(float f) {
        this.mStrokeWidth = f;
        return this;
    }

    public final CircleOptions visible(boolean z) {
        this.zzbnp = z;
        return this;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, (Parcelable) getCenter(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, getRadius());
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 4, getStrokeWidth());
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 5, getStrokeColor());
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 6, getFillColor());
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 7, getZIndex());
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 8, isVisible());
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 9, isClickable());
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 10, getStrokePattern(), false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }

    public final CircleOptions zIndex(float f) {
        this.zzbno = f;
        return this;
    }
}
