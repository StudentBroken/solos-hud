package com.google.android.gms.maps.model;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import java.io.IOException;

/* JADX INFO: loaded from: classes10.dex */
public final class MapStyleOptions extends com.google.android.gms.common.internal.safeparcel.zza {
    private String zzbnG;
    private static final String TAG = MapStyleOptions.class.getSimpleName();
    public static final Parcelable.Creator<MapStyleOptions> CREATOR = new zzg();

    public MapStyleOptions(String str) {
        this.zzbnG = str;
    }

    public static MapStyleOptions loadRawResourceStyle(Context context, int i) throws Resources.NotFoundException {
        try {
            return new MapStyleOptions(new String(com.google.android.gms.common.util.zzp.zza(context.getResources().openRawResource(i), true), "UTF-8"));
        } catch (IOException e) {
            String strValueOf = String.valueOf(e);
            throw new Resources.NotFoundException(new StringBuilder(String.valueOf(strValueOf).length() + 37).append("Failed to read resource ").append(i).append(": ").append(strValueOf).toString());
        }
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, this.zzbnG, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
