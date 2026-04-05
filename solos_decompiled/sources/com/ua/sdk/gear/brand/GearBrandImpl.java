package com.ua.sdk.gear.brand;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.EntityRef;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.LinkEntityRef;

/* JADX INFO: loaded from: classes65.dex */
public class GearBrandImpl extends ApiTransferObject implements GearBrand {
    public static final Parcelable.Creator<GearBrandImpl> CREATOR = new Parcelable.Creator<GearBrandImpl>() { // from class: com.ua.sdk.gear.brand.GearBrandImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GearBrandImpl createFromParcel(Parcel source) {
            return new GearBrandImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GearBrandImpl[] newArray(int size) {
            return new GearBrandImpl[size];
        }
    };
    public static final String REF_SELF = "self";

    @SerializedName("brand")
    String brandName;

    @SerializedName("gear_type_id")
    String gearTypeId;

    @SerializedName("popular")
    Boolean isPopular;

    public GearBrandImpl() {
    }

    @Override // com.ua.sdk.gear.brand.GearBrand
    public String getGearTypeId() {
        return this.gearTypeId;
    }

    @Override // com.ua.sdk.gear.brand.GearBrand
    public Boolean isPopular() {
        return this.isPopular;
    }

    @Override // com.ua.sdk.gear.brand.GearBrand
    public String getBrandName() {
        return this.brandName;
    }

    @Override // com.ua.sdk.Resource
    public EntityRef getRef() {
        Link link = getLink("self");
        if (link == null) {
            return null;
        }
        return new LinkEntityRef(link.getId(), link.getHref());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.gearTypeId);
        dest.writeValue(this.isPopular);
        dest.writeString(this.brandName);
    }

    private GearBrandImpl(Parcel in) {
        super(in);
        this.gearTypeId = in.readString();
        this.isPopular = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.brandName = in.readString();
    }
}
