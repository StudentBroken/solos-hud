package com.google.android.gms.location.places;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzd;
import com.google.android.gms.common.internal.zzbh;
import com.google.android.gms.common.internal.zzbj;
import com.google.android.gms.common.internal.zzbr;
import java.util.Arrays;

/* JADX INFO: loaded from: classes67.dex */
public class PlaceReport extends zza implements ReflectedParcelable {
    public static final Parcelable.Creator<PlaceReport> CREATOR = new zzl();
    private final String mTag;
    private final String zzaeM;
    private int zzakw;
    private final String zzbjM;

    PlaceReport(int i, String str, String str2, String str3) {
        this.zzakw = i;
        this.zzbjM = str;
        this.mTag = str2;
        this.zzaeM = str3;
    }

    public static PlaceReport create(String str, String str2) {
        boolean z = false;
        zzbr.zzu(str);
        zzbr.zzcF(str2);
        zzbr.zzcF("unknown");
        byte b = -1;
        switch ("unknown".hashCode()) {
            case -1436706272:
                if ("unknown".equals("inferredGeofencing")) {
                    b = 2;
                }
                break;
            case -1194968642:
                if ("unknown".equals("userReported")) {
                    b = 1;
                }
                break;
            case -284840886:
                if ("unknown".equals("unknown")) {
                    b = 0;
                }
                break;
            case -262743844:
                if ("unknown".equals("inferredReverseGeocoding")) {
                    b = 4;
                }
                break;
            case 1164924125:
                if ("unknown".equals("inferredSnappedToRoad")) {
                    b = 5;
                }
                break;
            case 1287171955:
                if ("unknown".equals("inferredRadioSignals")) {
                    b = 3;
                }
                break;
        }
        switch (b) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                z = true;
                break;
        }
        zzbr.zzb(z, "Invalid source");
        return new PlaceReport(1, str, str2, "unknown");
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof PlaceReport)) {
            return false;
        }
        PlaceReport placeReport = (PlaceReport) obj;
        return zzbh.equal(this.zzbjM, placeReport.zzbjM) && zzbh.equal(this.mTag, placeReport.mTag) && zzbh.equal(this.zzaeM, placeReport.zzaeM);
    }

    public String getPlaceId() {
        return this.zzbjM;
    }

    public String getTag() {
        return this.mTag;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.zzbjM, this.mTag, this.zzaeM});
    }

    public String toString() {
        zzbj zzbjVarZzt = zzbh.zzt(this);
        zzbjVarZzt.zzg("placeId", this.zzbjM);
        zzbjVarZzt.zzg("tag", this.mTag);
        if (!"unknown".equals(this.zzaeM)) {
            zzbjVarZzt.zzg("source", this.zzaeM);
        }
        return zzbjVarZzt.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        int iZze = zzd.zze(parcel);
        zzd.zzc(parcel, 1, this.zzakw);
        zzd.zza(parcel, 2, getPlaceId(), false);
        zzd.zza(parcel, 3, getTag(), false);
        zzd.zza(parcel, 4, this.zzaeM, false);
        zzd.zzI(parcel, iZze);
    }
}
