package com.ua.sdk.bodymass;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.EntityRef;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.LinkEntityRef;
import com.ua.sdk.user.User;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public class BodyMassImpl extends ApiTransferObject implements BodyMass {
    public static Parcelable.Creator<BodyMassImpl> CREATOR = new Parcelable.Creator<BodyMassImpl>() { // from class: com.ua.sdk.bodymass.BodyMassImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BodyMassImpl createFromParcel(Parcel source) {
            return new BodyMassImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BodyMassImpl[] newArray(int size) {
            return new BodyMassImpl[size];
        }
    };
    private static final String LINK_USER = "user";

    @SerializedName("bmi")
    String bmi;

    @SerializedName("created_datetime")
    Date createdDateTime;

    @SerializedName("datetime_timezone")
    String dateTimeTimezone;

    @SerializedName("datetime_utc")
    Date dateTimeUtc;

    @SerializedName("fat_mass")
    String fatMass;

    @SerializedName("fat_percent")
    String fatPercent;

    @SerializedName("lean_mass")
    String leanMass;

    @SerializedName("mass")
    String mass;

    @SerializedName("recorder_type_key")
    String recorderType;

    @SerializedName("reference_key")
    String referenceKey;

    @SerializedName("updated_datetime")
    Date updatedDateTime;
    private transient LinkEntityRef<User> userRef;

    public BodyMassImpl() {
    }

    private BodyMassImpl(Parcel in) {
        super(in);
        this.dateTimeUtc = (Date) in.readValue(Date.class.getClassLoader());
        this.dateTimeTimezone = in.readString();
        this.createdDateTime = (Date) in.readValue(Date.class.getClassLoader());
        this.updatedDateTime = (Date) in.readValue(Date.class.getClassLoader());
        this.recorderType = in.readString();
        this.referenceKey = in.readString();
        this.mass = in.readString();
        this.bmi = in.readString();
        this.fatPercent = in.readString();
        this.leanMass = in.readString();
        this.fatMass = in.readString();
    }

    @Override // com.ua.sdk.bodymass.BodyMass
    public Date getDateTimeUtc() {
        return this.dateTimeUtc;
    }

    @Override // com.ua.sdk.bodymass.BodyMass
    public void setDateTimeUtc(Date dateTimeUtc) {
        this.dateTimeUtc = dateTimeUtc;
    }

    @Override // com.ua.sdk.bodymass.BodyMass
    public String getDateTimeTimezone() {
        return this.dateTimeTimezone;
    }

    @Override // com.ua.sdk.bodymass.BodyMass
    public void setDateTimeTimezone(String dateTimeTimezone) {
        this.dateTimeTimezone = dateTimeTimezone;
    }

    @Override // com.ua.sdk.bodymass.BodyMass
    public Date getCreatedDateTime() {
        return this.createdDateTime;
    }

    @Override // com.ua.sdk.bodymass.BodyMass
    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    @Override // com.ua.sdk.bodymass.BodyMass
    public Date getUpdatedDateTime() {
        return this.updatedDateTime;
    }

    @Override // com.ua.sdk.bodymass.BodyMass
    public void setUpdatedDateTime(Date updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    @Override // com.ua.sdk.bodymass.BodyMass
    public String getRecorderType() {
        return this.recorderType;
    }

    @Override // com.ua.sdk.bodymass.BodyMass
    public void setRecorderType(String recorderType) {
        this.recorderType = recorderType;
    }

    @Override // com.ua.sdk.bodymass.BodyMass
    public String getReferenceKey() {
        return this.referenceKey;
    }

    @Override // com.ua.sdk.bodymass.BodyMass
    public void setReferenceKey(String referenceKey) {
        this.referenceKey = referenceKey;
    }

    @Override // com.ua.sdk.bodymass.BodyMass
    public String getMass() {
        return this.mass;
    }

    @Override // com.ua.sdk.bodymass.BodyMass
    public void setMass(String mass) {
        this.mass = mass;
    }

    @Override // com.ua.sdk.bodymass.BodyMass
    public String getBmi() {
        return this.bmi;
    }

    @Override // com.ua.sdk.bodymass.BodyMass
    public void setBmi(String bmi) {
        this.bmi = bmi;
    }

    @Override // com.ua.sdk.bodymass.BodyMass
    public String getFatPercent() {
        return this.fatPercent;
    }

    @Override // com.ua.sdk.bodymass.BodyMass
    public void setFatPercent(String fatPercent) {
        this.fatPercent = fatPercent;
    }

    @Override // com.ua.sdk.bodymass.BodyMass
    public String getLeanMass() {
        return this.leanMass;
    }

    @Override // com.ua.sdk.bodymass.BodyMass
    public void setLeanMass(String leanMass) {
        this.leanMass = leanMass;
    }

    @Override // com.ua.sdk.bodymass.BodyMass
    public String getFatMass() {
        return this.fatMass;
    }

    @Override // com.ua.sdk.bodymass.BodyMass
    public void setFatMass(String fatMass) {
        this.fatMass = fatMass;
    }

    @Override // com.ua.sdk.bodymass.BodyMass
    public EntityRef<User> getUserRef() {
        Link ref;
        if (this.userRef == null && (ref = getLink("user")) != null) {
            this.userRef = new LinkEntityRef<>(ref.getId(), ref.getHref());
        }
        return this.userRef;
    }

    @Override // com.ua.sdk.Resource
    public EntityRef<BodyMass> getRef() {
        Link self = getLink("self");
        if (self == null) {
            return null;
        }
        return new LinkEntityRef(self.getId(), self.getHref());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.dateTimeUtc);
        dest.writeString(this.dateTimeTimezone);
        dest.writeValue(this.createdDateTime);
        dest.writeValue(this.updatedDateTime);
        dest.writeString(this.recorderType);
        dest.writeString(this.referenceKey);
        dest.writeString(this.mass);
        dest.writeString(this.bmi);
        dest.writeString(this.fatPercent);
        dest.writeString(this.leanMass);
        dest.writeString(this.fatMass);
    }
}
