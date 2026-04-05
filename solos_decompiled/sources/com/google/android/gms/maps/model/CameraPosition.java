package com.google.android.gms.maps.model;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import com.google.android.gms.R;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.zzbh;
import com.google.android.gms.common.internal.zzbr;
import com.ua.sdk.datapoint.BaseDataTypes;
import java.util.Arrays;

/* JADX INFO: loaded from: classes10.dex */
public final class CameraPosition extends com.google.android.gms.common.internal.safeparcel.zza implements ReflectedParcelable {
    public static final Parcelable.Creator<CameraPosition> CREATOR = new zza();
    public final float bearing;
    public final LatLng target;
    public final float tilt;
    public final float zoom;

    public static final class Builder {
        private LatLng zzbng;
        private float zzbnh;
        private float zzbni;
        private float zzbnj;

        public Builder() {
        }

        public Builder(CameraPosition cameraPosition) {
            this.zzbng = cameraPosition.target;
            this.zzbnh = cameraPosition.zoom;
            this.zzbni = cameraPosition.tilt;
            this.zzbnj = cameraPosition.bearing;
        }

        public final Builder bearing(float f) {
            this.zzbnj = f;
            return this;
        }

        public final CameraPosition build() {
            return new CameraPosition(this.zzbng, this.zzbnh, this.zzbni, this.zzbnj);
        }

        public final Builder target(LatLng latLng) {
            this.zzbng = latLng;
            return this;
        }

        public final Builder tilt(float f) {
            this.zzbni = f;
            return this;
        }

        public final Builder zoom(float f) {
            this.zzbnh = f;
            return this;
        }
    }

    public CameraPosition(LatLng latLng, float f, float f2, float f3) {
        zzbr.zzb(latLng, "null camera target");
        zzbr.zzb(0.0f <= f2 && f2 <= 90.0f, "Tilt needs to be between 0 and 90 inclusive: %s", Float.valueOf(f2));
        this.target = latLng;
        this.zoom = f;
        this.tilt = f2 + 0.0f;
        this.bearing = (((double) f3) <= 0.0d ? (f3 % 360.0f) + 360.0f : f3) % 360.0f;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(CameraPosition cameraPosition) {
        return new Builder(cameraPosition);
    }

    public static CameraPosition createFromAttributes(Context context, AttributeSet attributeSet) {
        if (attributeSet == null) {
            return null;
        }
        TypedArray typedArrayObtainAttributes = context.getResources().obtainAttributes(attributeSet, R.styleable.MapAttrs);
        LatLng latLng = new LatLng(typedArrayObtainAttributes.hasValue(R.styleable.MapAttrs_cameraTargetLat) ? typedArrayObtainAttributes.getFloat(R.styleable.MapAttrs_cameraTargetLat, 0.0f) : 0.0f, typedArrayObtainAttributes.hasValue(R.styleable.MapAttrs_cameraTargetLng) ? typedArrayObtainAttributes.getFloat(R.styleable.MapAttrs_cameraTargetLng, 0.0f) : 0.0f);
        Builder builder = builder();
        builder.target(latLng);
        if (typedArrayObtainAttributes.hasValue(R.styleable.MapAttrs_cameraZoom)) {
            builder.zoom(typedArrayObtainAttributes.getFloat(R.styleable.MapAttrs_cameraZoom, 0.0f));
        }
        if (typedArrayObtainAttributes.hasValue(R.styleable.MapAttrs_cameraBearing)) {
            builder.bearing(typedArrayObtainAttributes.getFloat(R.styleable.MapAttrs_cameraBearing, 0.0f));
        }
        if (typedArrayObtainAttributes.hasValue(R.styleable.MapAttrs_cameraTilt)) {
            builder.tilt(typedArrayObtainAttributes.getFloat(R.styleable.MapAttrs_cameraTilt, 0.0f));
        }
        return builder.build();
    }

    public static final CameraPosition fromLatLngZoom(LatLng latLng, float f) {
        return new CameraPosition(latLng, f, 0.0f, 0.0f);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CameraPosition)) {
            return false;
        }
        CameraPosition cameraPosition = (CameraPosition) obj;
        return this.target.equals(cameraPosition.target) && Float.floatToIntBits(this.zoom) == Float.floatToIntBits(cameraPosition.zoom) && Float.floatToIntBits(this.tilt) == Float.floatToIntBits(cameraPosition.tilt) && Float.floatToIntBits(this.bearing) == Float.floatToIntBits(cameraPosition.bearing);
    }

    public final int hashCode() {
        return Arrays.hashCode(new Object[]{this.target, Float.valueOf(this.zoom), Float.valueOf(this.tilt), Float.valueOf(this.bearing)});
    }

    public final String toString() {
        return zzbh.zzt(this).zzg("target", this.target).zzg("zoom", Float.valueOf(this.zoom)).zzg("tilt", Float.valueOf(this.tilt)).zzg(BaseDataTypes.ID_BEARING, Float.valueOf(this.bearing)).toString();
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, (Parcelable) this.target, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, this.zoom);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 4, this.tilt);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 5, this.bearing);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
