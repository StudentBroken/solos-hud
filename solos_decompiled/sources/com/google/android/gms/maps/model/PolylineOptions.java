package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import com.google.android.gms.common.internal.zzbr;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes10.dex */
public final class PolylineOptions extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<PolylineOptions> CREATOR = new zzl();
    private int mColor;
    private final List<LatLng> zzbnR;
    private boolean zzbnT;

    @NonNull
    private Cap zzbnW;

    @NonNull
    private Cap zzbnX;
    private int zzbnY;

    @Nullable
    private List<PatternItem> zzbnZ;
    private float zzbno;
    private boolean zzbnp;
    private boolean zzbnq;
    private float zzbnv;

    public PolylineOptions() {
        this.zzbnv = 10.0f;
        this.mColor = ViewCompat.MEASURED_STATE_MASK;
        this.zzbno = 0.0f;
        this.zzbnp = true;
        this.zzbnT = false;
        this.zzbnq = false;
        this.zzbnW = new ButtCap();
        this.zzbnX = new ButtCap();
        this.zzbnY = 0;
        this.zzbnZ = null;
        this.zzbnR = new ArrayList();
    }

    PolylineOptions(List list, float f, int i, float f2, boolean z, boolean z2, boolean z3, @Nullable Cap cap, @Nullable Cap cap2, int i2, @Nullable List<PatternItem> list2) {
        this.zzbnv = 10.0f;
        this.mColor = ViewCompat.MEASURED_STATE_MASK;
        this.zzbno = 0.0f;
        this.zzbnp = true;
        this.zzbnT = false;
        this.zzbnq = false;
        this.zzbnW = new ButtCap();
        this.zzbnX = new ButtCap();
        this.zzbnY = 0;
        this.zzbnZ = null;
        this.zzbnR = list;
        this.zzbnv = f;
        this.mColor = i;
        this.zzbno = f2;
        this.zzbnp = z;
        this.zzbnT = z2;
        this.zzbnq = z3;
        if (cap != null) {
            this.zzbnW = cap;
        }
        if (cap2 != null) {
            this.zzbnX = cap2;
        }
        this.zzbnY = i2;
        this.zzbnZ = list2;
    }

    public final PolylineOptions add(LatLng latLng) {
        this.zzbnR.add(latLng);
        return this;
    }

    public final PolylineOptions add(LatLng... latLngArr) {
        this.zzbnR.addAll(Arrays.asList(latLngArr));
        return this;
    }

    public final PolylineOptions addAll(Iterable<LatLng> iterable) {
        Iterator<LatLng> it = iterable.iterator();
        while (it.hasNext()) {
            this.zzbnR.add(it.next());
        }
        return this;
    }

    public final PolylineOptions clickable(boolean z) {
        this.zzbnq = z;
        return this;
    }

    public final PolylineOptions color(int i) {
        this.mColor = i;
        return this;
    }

    public final PolylineOptions endCap(@NonNull Cap cap) {
        this.zzbnX = (Cap) zzbr.zzb(cap, "endCap must not be null");
        return this;
    }

    public final PolylineOptions geodesic(boolean z) {
        this.zzbnT = z;
        return this;
    }

    public final int getColor() {
        return this.mColor;
    }

    @NonNull
    public final Cap getEndCap() {
        return this.zzbnX;
    }

    public final int getJointType() {
        return this.zzbnY;
    }

    @Nullable
    public final List<PatternItem> getPattern() {
        return this.zzbnZ;
    }

    public final List<LatLng> getPoints() {
        return this.zzbnR;
    }

    @NonNull
    public final Cap getStartCap() {
        return this.zzbnW;
    }

    public final float getWidth() {
        return this.zzbnv;
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

    public final PolylineOptions jointType(int i) {
        this.zzbnY = i;
        return this;
    }

    public final PolylineOptions pattern(@Nullable List<PatternItem> list) {
        this.zzbnZ = list;
        return this;
    }

    public final PolylineOptions startCap(@NonNull Cap cap) {
        this.zzbnW = (Cap) zzbr.zzb(cap, "startCap must not be null");
        return this;
    }

    public final PolylineOptions visible(boolean z) {
        this.zzbnp = z;
        return this;
    }

    public final PolylineOptions width(float f) {
        this.zzbnv = f;
        return this;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 2, getPoints(), false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, getWidth());
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 4, getColor());
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 5, getZIndex());
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 6, isVisible());
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 7, isGeodesic());
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 8, isClickable());
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 9, (Parcelable) getStartCap(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 10, (Parcelable) getEndCap(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 11, getJointType());
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 12, getPattern(), false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }

    public final PolylineOptions zIndex(float f) {
        this.zzbno = f;
        return this;
    }
}
