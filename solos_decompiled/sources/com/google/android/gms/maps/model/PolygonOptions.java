package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes10.dex */
public final class PolygonOptions extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<PolygonOptions> CREATOR = new zzk();
    private int mFillColor;
    private int mStrokeColor;
    private float mStrokeWidth;
    private final List<LatLng> zzbnR;
    private final List<List<LatLng>> zzbnS;
    private boolean zzbnT;
    private int zzbnU;
    private float zzbno;
    private boolean zzbnp;
    private boolean zzbnq;

    @Nullable
    private List<PatternItem> zzbnr;

    public PolygonOptions() {
        this.mStrokeWidth = 10.0f;
        this.mStrokeColor = ViewCompat.MEASURED_STATE_MASK;
        this.mFillColor = 0;
        this.zzbno = 0.0f;
        this.zzbnp = true;
        this.zzbnT = false;
        this.zzbnq = false;
        this.zzbnU = 0;
        this.zzbnr = null;
        this.zzbnR = new ArrayList();
        this.zzbnS = new ArrayList();
    }

    PolygonOptions(List<LatLng> list, List list2, float f, int i, int i2, float f2, boolean z, boolean z2, boolean z3, int i3, @Nullable List<PatternItem> list3) {
        this.mStrokeWidth = 10.0f;
        this.mStrokeColor = ViewCompat.MEASURED_STATE_MASK;
        this.mFillColor = 0;
        this.zzbno = 0.0f;
        this.zzbnp = true;
        this.zzbnT = false;
        this.zzbnq = false;
        this.zzbnU = 0;
        this.zzbnr = null;
        this.zzbnR = list;
        this.zzbnS = list2;
        this.mStrokeWidth = f;
        this.mStrokeColor = i;
        this.mFillColor = i2;
        this.zzbno = f2;
        this.zzbnp = z;
        this.zzbnT = z2;
        this.zzbnq = z3;
        this.zzbnU = i3;
        this.zzbnr = list3;
    }

    public final PolygonOptions add(LatLng latLng) {
        this.zzbnR.add(latLng);
        return this;
    }

    public final PolygonOptions add(LatLng... latLngArr) {
        this.zzbnR.addAll(Arrays.asList(latLngArr));
        return this;
    }

    public final PolygonOptions addAll(Iterable<LatLng> iterable) {
        Iterator<LatLng> it = iterable.iterator();
        while (it.hasNext()) {
            this.zzbnR.add(it.next());
        }
        return this;
    }

    public final PolygonOptions addHole(Iterable<LatLng> iterable) {
        ArrayList arrayList = new ArrayList();
        Iterator<LatLng> it = iterable.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next());
        }
        this.zzbnS.add(arrayList);
        return this;
    }

    public final PolygonOptions clickable(boolean z) {
        this.zzbnq = z;
        return this;
    }

    public final PolygonOptions fillColor(int i) {
        this.mFillColor = i;
        return this;
    }

    public final PolygonOptions geodesic(boolean z) {
        this.zzbnT = z;
        return this;
    }

    public final int getFillColor() {
        return this.mFillColor;
    }

    public final List<List<LatLng>> getHoles() {
        return this.zzbnS;
    }

    public final List<LatLng> getPoints() {
        return this.zzbnR;
    }

    public final int getStrokeColor() {
        return this.mStrokeColor;
    }

    public final int getStrokeJointType() {
        return this.zzbnU;
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

    public final boolean isGeodesic() {
        return this.zzbnT;
    }

    public final boolean isVisible() {
        return this.zzbnp;
    }

    public final PolygonOptions strokeColor(int i) {
        this.mStrokeColor = i;
        return this;
    }

    public final PolygonOptions strokeJointType(int i) {
        this.zzbnU = i;
        return this;
    }

    public final PolygonOptions strokePattern(@Nullable List<PatternItem> list) {
        this.zzbnr = list;
        return this;
    }

    public final PolygonOptions strokeWidth(float f) {
        this.mStrokeWidth = f;
        return this;
    }

    public final PolygonOptions visible(boolean z) {
        this.zzbnp = z;
        return this;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 2, getPoints(), false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzd(parcel, 3, this.zzbnS, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 4, getStrokeWidth());
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 5, getStrokeColor());
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 6, getFillColor());
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 7, getZIndex());
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 8, isVisible());
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 9, isGeodesic());
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 10, isClickable());
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 11, getStrokeJointType());
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 12, getStrokePattern(), false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }

    public final PolygonOptions zIndex(float f) {
        this.zzbno = f;
        return this;
    }
}
