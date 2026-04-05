package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.gms.common.internal.zzbh;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.dynamic.IObjectWrapper;
import java.util.Arrays;

/* JADX INFO: loaded from: classes10.dex */
public class Cap extends com.google.android.gms.common.internal.safeparcel.zza {

    @Nullable
    private final BitmapDescriptor bitmapDescriptor;
    private final int type;

    @Nullable
    private final Float zzbnk;
    private static final String TAG = Cap.class.getSimpleName();
    public static final Parcelable.Creator<Cap> CREATOR = new zzb();

    protected Cap(int i) {
        this(i, (BitmapDescriptor) null, (Float) null);
    }

    Cap(int i, @Nullable IBinder iBinder, @Nullable Float f) {
        this(i, iBinder == null ? null : new BitmapDescriptor(IObjectWrapper.zza.zzM(iBinder)), f);
    }

    private Cap(int i, @Nullable BitmapDescriptor bitmapDescriptor, @Nullable Float f) {
        boolean z = false;
        boolean z2 = f != null && f.floatValue() > 0.0f;
        if (i != 3 || (bitmapDescriptor != null && z2)) {
            z = true;
        }
        String strValueOf = String.valueOf(bitmapDescriptor);
        String strValueOf2 = String.valueOf(f);
        zzbr.zzb(z, new StringBuilder(String.valueOf(strValueOf).length() + 63 + String.valueOf(strValueOf2).length()).append("Invalid Cap: type=").append(i).append(" bitmapDescriptor=").append(strValueOf).append(" bitmapRefWidth=").append(strValueOf2).toString());
        this.type = i;
        this.bitmapDescriptor = bitmapDescriptor;
        this.zzbnk = f;
    }

    protected Cap(@NonNull BitmapDescriptor bitmapDescriptor, float f) {
        this(3, bitmapDescriptor, Float.valueOf(f));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Cap)) {
            return false;
        }
        Cap cap = (Cap) obj;
        return this.type == cap.type && zzbh.equal(this.bitmapDescriptor, cap.bitmapDescriptor) && zzbh.equal(this.zzbnk, cap.zzbnk);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Integer.valueOf(this.type), this.bitmapDescriptor, this.zzbnk});
    }

    public String toString() {
        return new StringBuilder(23).append("[Cap: type=").append(this.type).append("]").toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 2, this.type);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, this.bitmapDescriptor == null ? null : this.bitmapDescriptor.zzwd().asBinder(), false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 4, this.zzbnk, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }

    final Cap zzwj() {
        switch (this.type) {
            case 0:
                return new ButtCap();
            case 1:
                return new SquareCap();
            case 2:
                return new RoundCap();
            case 3:
                return new CustomCap(this.bitmapDescriptor, this.zzbnk.floatValue());
            default:
                Log.w(TAG, new StringBuilder(29).append("Unknown Cap type: ").append(this.type).toString());
                return this;
        }
    }
}
