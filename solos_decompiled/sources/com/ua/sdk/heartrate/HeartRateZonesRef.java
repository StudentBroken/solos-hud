package com.ua.sdk.heartrate;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.EntityRef;
import com.ua.sdk.internal.BaseReferenceBuilder;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.internal.net.v7.UrlBuilderImpl;

/* JADX INFO: loaded from: classes65.dex */
public class HeartRateZonesRef implements EntityRef<HeartRateZones> {
    public static Parcelable.Creator<HeartRateZonesRef> CREATOR = new Parcelable.Creator<HeartRateZonesRef>() { // from class: com.ua.sdk.heartrate.HeartRateZonesRef.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public HeartRateZonesRef createFromParcel(Parcel source) {
            return new HeartRateZonesRef(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public HeartRateZonesRef[] newArray(int size) {
            return new HeartRateZonesRef[size];
        }
    };
    private final String id;

    private HeartRateZonesRef(Builder init) {
        this.id = init.id;
    }

    @Override // com.ua.sdk.Reference
    public String getId() {
        return this.id.length() > 0 ? this.id : "";
    }

    @Override // com.ua.sdk.Reference
    public String getHref() {
        return !this.id.isEmpty() ? String.format(UrlBuilderImpl.GET_HEART_RATE_ZONES, this.id) : "";
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static final class Builder extends BaseReferenceBuilder {
        private String id;

        private Builder() {
            super(UrlBuilderImpl.GET_HEART_RATE_ZONES);
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public HeartRateZonesRef build() {
            Precondition.isNotNull(this.id, "HeartRateZones Id");
            return new HeartRateZonesRef(this);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
    }

    private HeartRateZonesRef(Parcel in) {
        this.id = in.readString();
    }
}
