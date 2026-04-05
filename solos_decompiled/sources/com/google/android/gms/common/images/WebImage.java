package com.google.android.gms.common.images;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.zzbh;
import java.util.Arrays;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes3.dex */
public final class WebImage extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<WebImage> CREATOR = new zze();
    private int zzakw;
    private final Uri zzauS;
    private final int zzrZ;
    private final int zzsa;

    WebImage(int i, Uri uri, int i2, int i3) {
        this.zzakw = i;
        this.zzauS = uri;
        this.zzrZ = i2;
        this.zzsa = i3;
    }

    public WebImage(Uri uri) throws IllegalArgumentException {
        this(uri, 0, 0);
    }

    public WebImage(Uri uri, int i, int i2) throws IllegalArgumentException {
        this(1, uri, i, i2);
        if (uri == null) {
            throw new IllegalArgumentException("url cannot be null");
        }
        if (i < 0 || i2 < 0) {
            throw new IllegalArgumentException("width and height must not be negative");
        }
    }

    public WebImage(JSONObject jSONObject) throws IllegalArgumentException {
        this(zzp(jSONObject), jSONObject.optInt("width", 0), jSONObject.optInt("height", 0));
    }

    private static Uri zzp(JSONObject jSONObject) {
        if (!jSONObject.has("url")) {
            return null;
        }
        try {
            return Uri.parse(jSONObject.getString("url"));
        } catch (JSONException e) {
            return null;
        }
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof WebImage)) {
            return false;
        }
        WebImage webImage = (WebImage) obj;
        return zzbh.equal(this.zzauS, webImage.zzauS) && this.zzrZ == webImage.zzrZ && this.zzsa == webImage.zzsa;
    }

    public final int getHeight() {
        return this.zzsa;
    }

    public final Uri getUrl() {
        return this.zzauS;
    }

    public final int getWidth() {
        return this.zzrZ;
    }

    public final int hashCode() {
        return Arrays.hashCode(new Object[]{this.zzauS, Integer.valueOf(this.zzrZ), Integer.valueOf(this.zzsa)});
    }

    public final JSONObject toJson() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("url", this.zzauS.toString());
            jSONObject.put("width", this.zzrZ);
            jSONObject.put("height", this.zzsa);
        } catch (JSONException e) {
        }
        return jSONObject;
    }

    public final String toString() {
        return String.format(Locale.US, "Image %dx%d %s", Integer.valueOf(this.zzrZ), Integer.valueOf(this.zzsa), this.zzauS.toString());
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 1, this.zzakw);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, (Parcelable) getUrl(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 3, getWidth());
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 4, getHeight());
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
