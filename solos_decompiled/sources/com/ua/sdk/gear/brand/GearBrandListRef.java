package com.ua.sdk.gear.brand;

import android.os.Parcel;
import android.os.Parcelable;
import com.facebook.share.internal.ShareConstants;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.gear.GearType;
import com.ua.sdk.internal.BaseReferenceBuilder;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.internal.net.v7.UrlBuilderImpl;

/* JADX INFO: loaded from: classes65.dex */
public class GearBrandListRef implements EntityListRef<GearBrand> {
    public static final Parcelable.Creator<GearBrandListRef> CREATOR = new Parcelable.Creator<GearBrandListRef>() { // from class: com.ua.sdk.gear.brand.GearBrandListRef.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GearBrandListRef createFromParcel(Parcel source) {
            return new GearBrandListRef(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GearBrandListRef[] newArray(int size) {
            return new GearBrandListRef[size];
        }
    };
    private String href;

    private GearBrandListRef(Builder init) {
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
        GearType type;

        private Builder() {
            super(UrlBuilderImpl.GET_GEAR_BRAND_URL);
        }

        public Builder setType(GearType type) {
            this.type = type;
            setParam(ShareConstants.MEDIA_TYPE, type.getValue());
            return this;
        }

        public GearBrandListRef build() {
            Precondition.isNotNull(this.type);
            return new GearBrandListRef(this);
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

    private GearBrandListRef(Parcel in) {
        this.href = in.readString();
    }
}
