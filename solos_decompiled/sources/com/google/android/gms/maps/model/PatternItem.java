package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.gms.common.internal.zzbh;
import com.google.android.gms.common.internal.zzbr;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes10.dex */
public class PatternItem extends com.google.android.gms.common.internal.safeparcel.zza {
    private final int type;

    @Nullable
    private final Float zzbnP;
    private static final String TAG = PatternItem.class.getSimpleName();
    public static final Parcelable.Creator<PatternItem> CREATOR = new zzi();

    public PatternItem(int i, @Nullable Float f) {
        boolean z = true;
        if (i != 1 && (f == null || f.floatValue() < 0.0f)) {
            z = false;
        }
        String strValueOf = String.valueOf(f);
        zzbr.zzb(z, new StringBuilder(String.valueOf(strValueOf).length() + 45).append("Invalid PatternItem: type=").append(i).append(" length=").append(strValueOf).toString());
        this.type = i;
        this.zzbnP = f;
    }

    @Nullable
    static List<PatternItem> zzF(@Nullable List<PatternItem> list) {
        if (list == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList(list.size());
        for (PatternItem gap : list) {
            if (gap != null) {
                switch (gap.type) {
                    case 0:
                        gap = new Dash(gap.zzbnP.floatValue());
                        break;
                    case 1:
                        gap = new Dot();
                        break;
                    case 2:
                        gap = new Gap(gap.zzbnP.floatValue());
                        break;
                    default:
                        Log.w(TAG, new StringBuilder(37).append("Unknown PatternItem type: ").append(gap.type).toString());
                        break;
                }
            } else {
                gap = null;
            }
            arrayList.add(gap);
        }
        return arrayList;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PatternItem)) {
            return false;
        }
        PatternItem patternItem = (PatternItem) obj;
        return this.type == patternItem.type && zzbh.equal(this.zzbnP, patternItem.zzbnP);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Integer.valueOf(this.type), this.zzbnP});
    }

    public String toString() {
        int i = this.type;
        String strValueOf = String.valueOf(this.zzbnP);
        return new StringBuilder(String.valueOf(strValueOf).length() + 39).append("[PatternItem: type=").append(i).append(" length=").append(strValueOf).append("]").toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 2, this.type);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, this.zzbnP, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
