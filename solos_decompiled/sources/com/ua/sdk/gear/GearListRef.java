package com.ua.sdk.gear;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.internal.BaseReferenceBuilder;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.internal.net.v7.UrlBuilderImpl;

/* JADX INFO: loaded from: classes65.dex */
public class GearListRef implements EntityListRef<Gear> {
    public static final Parcelable.Creator<GearListRef> CREATOR = new Parcelable.Creator<GearListRef>() { // from class: com.ua.sdk.gear.GearListRef.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GearListRef createFromParcel(Parcel source) {
            return new GearListRef(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GearListRef[] newArray(int size) {
            return new GearListRef[size];
        }
    };
    private String href;

    private GearListRef(Builder init) {
        this.href = init.getHref();
    }

    @Override // com.ua.sdk.Reference
    public String getId() {
        return null;
    }

    @Override // com.ua.sdk.Reference
    public String getHref() {
        return this.href;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder extends BaseReferenceBuilder {
        String brand;

        private Builder() {
            super(UrlBuilderImpl.GET_GEAR_URL);
        }

        public Builder setBrand(String brand) {
            this.brand = brand;
            setParam("brand", brand);
            return this;
        }

        public GearListRef build() {
            Precondition.isNotNull(this.brand);
            return new GearListRef(this);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.href);
    }

    private GearListRef(Parcel in) {
        this.href = in.readString();
    }
}
