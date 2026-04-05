package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.maps.model.internal.zzaa;
import com.google.android.gms.maps.model.internal.zzz;

/* JADX INFO: loaded from: classes10.dex */
public final class TileOverlayOptions extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<TileOverlayOptions> CREATOR = new zzt();
    private float zzbno;
    private boolean zzbnp;
    private float zzbnx;
    private zzz zzboc;
    private TileProvider zzbod;
    private boolean zzboe;

    public TileOverlayOptions() {
        this.zzbnp = true;
        this.zzboe = true;
        this.zzbnx = 0.0f;
    }

    TileOverlayOptions(IBinder iBinder, boolean z, float f, boolean z2, float f2) {
        this.zzbnp = true;
        this.zzboe = true;
        this.zzbnx = 0.0f;
        this.zzboc = zzaa.zzaj(iBinder);
        this.zzbod = this.zzboc == null ? null : new zzr(this);
        this.zzbnp = z;
        this.zzbno = f;
        this.zzboe = z2;
        this.zzbnx = f2;
    }

    public final TileOverlayOptions fadeIn(boolean z) {
        this.zzboe = z;
        return this;
    }

    public final boolean getFadeIn() {
        return this.zzboe;
    }

    public final TileProvider getTileProvider() {
        return this.zzbod;
    }

    public final float getTransparency() {
        return this.zzbnx;
    }

    public final float getZIndex() {
        return this.zzbno;
    }

    public final boolean isVisible() {
        return this.zzbnp;
    }

    public final TileOverlayOptions tileProvider(TileProvider tileProvider) {
        this.zzbod = tileProvider;
        this.zzboc = this.zzbod == null ? null : new zzs(this, tileProvider);
        return this;
    }

    public final TileOverlayOptions transparency(float f) {
        zzbr.zzb(f >= 0.0f && f <= 1.0f, "Transparency must be in the range [0..1]");
        this.zzbnx = f;
        return this;
    }

    public final TileOverlayOptions visible(boolean z) {
        this.zzbnp = z;
        return this;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, this.zzboc.asBinder(), false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, isVisible());
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 4, getZIndex());
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 5, getFadeIn());
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 6, getTransparency());
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }

    public final TileOverlayOptions zIndex(float f) {
        this.zzbno = f;
        return this;
    }
}
