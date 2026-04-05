package com.google.android.gms.wearable;

import android.net.Uri;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.zzbh;
import java.util.Arrays;

/* JADX INFO: loaded from: classes6.dex */
public class Asset extends com.google.android.gms.common.internal.safeparcel.zza implements ReflectedParcelable {
    public static final Parcelable.Creator<Asset> CREATOR = new zze();
    private Uri uri;
    private String zzbQZ;
    private ParcelFileDescriptor zzbRa;
    private byte[] zzbec;

    Asset(byte[] bArr, String str, ParcelFileDescriptor parcelFileDescriptor, Uri uri) {
        this.zzbec = bArr;
        this.zzbQZ = str;
        this.zzbRa = parcelFileDescriptor;
        this.uri = uri;
    }

    public static Asset createFromBytes(byte[] bArr) {
        if (bArr == null) {
            throw new IllegalArgumentException("Asset data cannot be null");
        }
        return new Asset(bArr, null, null, null);
    }

    public static Asset createFromFd(ParcelFileDescriptor parcelFileDescriptor) {
        if (parcelFileDescriptor == null) {
            throw new IllegalArgumentException("Asset fd cannot be null");
        }
        return new Asset(null, null, parcelFileDescriptor, null);
    }

    public static Asset createFromRef(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Asset digest cannot be null");
        }
        return new Asset(null, str, null, null);
    }

    public static Asset createFromUri(Uri uri) {
        if (uri == null) {
            throw new IllegalArgumentException("Asset uri cannot be null");
        }
        return new Asset(null, null, null, uri);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Asset)) {
            return false;
        }
        Asset asset = (Asset) obj;
        return Arrays.equals(this.zzbec, asset.zzbec) && zzbh.equal(this.zzbQZ, asset.zzbQZ) && zzbh.equal(this.zzbRa, asset.zzbRa) && zzbh.equal(this.uri, asset.uri);
    }

    public final byte[] getData() {
        return this.zzbec;
    }

    public String getDigest() {
        return this.zzbQZ;
    }

    public ParcelFileDescriptor getFd() {
        return this.zzbRa;
    }

    public Uri getUri() {
        return this.uri;
    }

    public int hashCode() {
        return Arrays.deepHashCode(new Object[]{this.zzbec, this.zzbQZ, this.zzbRa, this.uri});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Asset[@");
        sb.append(Integer.toHexString(hashCode()));
        if (this.zzbQZ == null) {
            sb.append(", nodigest");
        } else {
            sb.append(", ");
            sb.append(this.zzbQZ);
        }
        if (this.zzbec != null) {
            sb.append(", size=");
            sb.append(this.zzbec.length);
        }
        if (this.zzbRa != null) {
            sb.append(", fd=");
            sb.append(this.zzbRa);
        }
        if (this.uri != null) {
            sb.append(", uri=");
            sb.append(this.uri);
        }
        sb.append("]");
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        int i2 = i | 1;
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, this.zzbec, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, getDigest(), false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 4, (Parcelable) this.zzbRa, i2, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 5, (Parcelable) this.uri, i2, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
