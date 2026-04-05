package com.ua.sdk.gear;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.EntityRef;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.LinkEntityRef;

/* JADX INFO: loaded from: classes65.dex */
public class GearImpl extends ApiTransferObject implements Gear {
    public static final Parcelable.Creator<GearImpl> CREATOR = new Parcelable.Creator<GearImpl>() { // from class: com.ua.sdk.gear.GearImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GearImpl createFromParcel(Parcel source) {
            return new GearImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GearImpl[] newArray(int size) {
            return new GearImpl[size];
        }
    };
    public static final String REF_SELF = "self";

    @SerializedName("age_group")
    String ageGroup;

    @SerializedName("available")
    Boolean available;

    @SerializedName("brand")
    String brand;

    @SerializedName("category")
    String category;

    @SerializedName("color")
    String color;

    @SerializedName("department")
    String department;

    @SerializedName("description")
    String description;

    @SerializedName("detail_photo_url")
    String detailPhotoUrl;

    @SerializedName("gender")
    String gender;

    @SerializedName("keywords")
    String keywords;

    @SerializedName("mid_level_product_type")
    String midLevelProductType;

    @SerializedName("model")
    String model;

    @SerializedName("msrp")
    String msrp;

    @SerializedName("photo_url")
    String photoUrl;

    @SerializedName(FirebaseAnalytics.Param.PRICE)
    String price;

    @SerializedName("product_type")
    String productType;

    @SerializedName("product_url")
    String productUrl;

    @SerializedName("purchase_url")
    String purchaseUrl;

    @SerializedName("size")
    String size;

    @SerializedName("sku")
    String sku;

    @SerializedName("source")
    String source;

    @SerializedName("styleid")
    String styleId;

    @SerializedName("style_number")
    String styleNumber;

    @SerializedName("thumbnail_url")
    String thumbnailUrl;

    @SerializedName("upc")
    Long upc;

    public GearImpl() {
    }

    @Override // com.ua.sdk.gear.Gear
    public String getStyleNumber() {
        return this.styleNumber;
    }

    @Override // com.ua.sdk.gear.Gear
    public String getColor() {
        return this.color;
    }

    @Override // com.ua.sdk.gear.Gear
    public String getProductUrl() {
        return this.productUrl;
    }

    @Override // com.ua.sdk.gear.Gear
    public String getKeywords() {
        return this.keywords;
    }

    @Override // com.ua.sdk.gear.Gear
    public String getAgeGroup() {
        return this.ageGroup;
    }

    @Override // com.ua.sdk.gear.Gear
    public String getSize() {
        return this.size;
    }

    @Override // com.ua.sdk.gear.Gear
    public String getSku() {
        return this.sku;
    }

    @Override // com.ua.sdk.gear.Gear
    public String getSource() {
        return this.source;
    }

    @Override // com.ua.sdk.gear.Gear
    public String getDepartment() {
        return this.department;
    }

    @Override // com.ua.sdk.gear.Gear
    public String getBrand() {
        return this.brand;
    }

    @Override // com.ua.sdk.gear.Gear
    public Boolean getAvailable() {
        return this.available;
    }

    @Override // com.ua.sdk.gear.Gear
    public String getCategory() {
        return this.category;
    }

    @Override // com.ua.sdk.gear.Gear
    public String getDescription() {
        return this.description;
    }

    @Override // com.ua.sdk.gear.Gear
    public String getPrice() {
        return this.price;
    }

    @Override // com.ua.sdk.gear.Gear
    public String getPurchaseUrl() {
        return this.purchaseUrl;
    }

    @Override // com.ua.sdk.gear.Gear
    public String getMidLevelProductType() {
        return this.midLevelProductType;
    }

    @Override // com.ua.sdk.gear.Gear
    public String getPhotoUrl() {
        return this.photoUrl;
    }

    @Override // com.ua.sdk.gear.Gear
    public String getDetailPhotoUrl() {
        return this.detailPhotoUrl;
    }

    @Override // com.ua.sdk.gear.Gear
    public String getProductType() {
        return this.productType;
    }

    @Override // com.ua.sdk.gear.Gear
    public String getGender() {
        return this.gender;
    }

    @Override // com.ua.sdk.gear.Gear
    public Long getUpc() {
        return this.upc;
    }

    @Override // com.ua.sdk.gear.Gear
    public String getThumbnailUrl() {
        return this.thumbnailUrl;
    }

    @Override // com.ua.sdk.gear.Gear
    public String getStyleId() {
        return this.styleId;
    }

    @Override // com.ua.sdk.gear.Gear
    public String getModel() {
        return this.model;
    }

    @Override // com.ua.sdk.gear.Gear
    public String getMsrp() {
        return this.msrp;
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
        dest.writeString(this.styleNumber);
        dest.writeString(this.color);
        dest.writeString(this.productUrl);
        dest.writeString(this.keywords);
        dest.writeString(this.ageGroup);
        dest.writeString(this.size);
        dest.writeString(this.sku);
        dest.writeString(this.source);
        dest.writeString(this.department);
        dest.writeString(this.brand);
        dest.writeValue(this.available);
        dest.writeString(this.category);
        dest.writeString(this.description);
        dest.writeString(this.price);
        dest.writeString(this.purchaseUrl);
        dest.writeString(this.midLevelProductType);
        dest.writeString(this.photoUrl);
        dest.writeString(this.detailPhotoUrl);
        dest.writeString(this.productType);
        dest.writeString(this.gender);
        dest.writeValue(this.upc);
        dest.writeString(this.thumbnailUrl);
        dest.writeString(this.styleId);
        dest.writeString(this.model);
        dest.writeString(this.msrp);
    }

    private GearImpl(Parcel in) {
        super(in);
        this.styleNumber = in.readString();
        this.color = in.readString();
        this.productUrl = in.readString();
        this.keywords = in.readString();
        this.ageGroup = in.readString();
        this.size = in.readString();
        this.sku = in.readString();
        this.source = in.readString();
        this.department = in.readString();
        this.brand = in.readString();
        this.available = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.category = in.readString();
        this.description = in.readString();
        this.price = in.readString();
        this.purchaseUrl = in.readString();
        this.midLevelProductType = in.readString();
        this.photoUrl = in.readString();
        this.detailPhotoUrl = in.readString();
        this.productType = in.readString();
        this.gender = in.readString();
        this.upc = (Long) in.readValue(Long.class.getClassLoader());
        this.thumbnailUrl = in.readString();
        this.styleId = in.readString();
        this.model = in.readString();
        this.msrp = in.readString();
    }
}
