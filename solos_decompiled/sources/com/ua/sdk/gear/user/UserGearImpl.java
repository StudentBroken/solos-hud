package com.ua.sdk.gear.user;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.kopin.solos.storage.settings.Prefs;
import com.ua.sdk.EntityRef;
import com.ua.sdk.LocalDate;
import com.ua.sdk.activitytype.ActivityType;
import com.ua.sdk.gear.Gear;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.LinkEntityRef;
import com.ua.sdk.user.User;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class UserGearImpl extends ApiTransferObject implements UserGear {
    public static final Parcelable.Creator<UserGearImpl> CREATOR = new Parcelable.Creator<UserGearImpl>() { // from class: com.ua.sdk.gear.user.UserGearImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserGearImpl createFromParcel(Parcel source) {
            return new UserGearImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserGearImpl[] newArray(int size) {
            return new UserGearImpl[size];
        }
    };
    private static final String REF_DEFAULT_ACTIVITIES = "default_activities";
    private static final String REF_SELF = "self";
    private static final String REF_USER = "user";

    @SerializedName("current_distance")
    Double currentDistance;

    @SerializedName("gear")
    Gear gear;

    @SerializedName("initial_distance")
    Double initialDistance;

    @SerializedName("retired")
    Boolean isRetired;

    @SerializedName("name")
    String name;

    @SerializedName("purchase_date")
    LocalDate purchaseDate;

    @SerializedName(Prefs.TARGET_DISTANCE)
    Double targetDistance;

    public UserGearImpl() {
    }

    @Override // com.ua.sdk.gear.user.UserGear
    public Gear getGear() {
        return this.gear;
    }

    @Override // com.ua.sdk.gear.user.UserGear
    public String getName() {
        return this.name;
    }

    @Override // com.ua.sdk.gear.user.UserGear
    public Double getInitialDistance() {
        return this.initialDistance;
    }

    @Override // com.ua.sdk.gear.user.UserGear
    public Double getTargetDistance() {
        return this.targetDistance;
    }

    @Override // com.ua.sdk.gear.user.UserGear
    public LocalDate getPurchaseDate() {
        return this.purchaseDate;
    }

    @Override // com.ua.sdk.gear.user.UserGear
    public Double getCurrentDistance() {
        return this.currentDistance;
    }

    @Override // com.ua.sdk.gear.user.UserGear
    public Boolean isRetired() {
        return this.isRetired;
    }

    @Override // com.ua.sdk.gear.user.UserGear
    public EntityRef<User> getUser() {
        Link link = getLink("user");
        if (link == null) {
            return null;
        }
        return new LinkEntityRef(link.getId(), link.getHref());
    }

    @Override // com.ua.sdk.gear.user.UserGear
    public List<EntityRef<ActivityType>> getDefaultActivities() {
        List<Link> links = getLinks(REF_DEFAULT_ACTIVITIES);
        if (links == null) {
            return null;
        }
        List<EntityRef<ActivityType>> activityTypes = new ArrayList<>(links.size());
        for (Link link : links) {
            activityTypes.add(new LinkEntityRef<>(link.getId(), link.getHref()));
        }
        return activityTypes;
    }

    @Override // com.ua.sdk.gear.user.UserGear
    public void setGear(Gear gear) {
        this.gear = gear;
    }

    @Override // com.ua.sdk.gear.user.UserGear
    public void setName(String name) {
        this.name = name;
    }

    @Override // com.ua.sdk.gear.user.UserGear
    public void setPurchaseDate(LocalDate date) {
        this.purchaseDate = date;
    }

    @Override // com.ua.sdk.gear.user.UserGear
    public void setInitialDistance(Double distance) {
        this.initialDistance = distance;
    }

    @Override // com.ua.sdk.gear.user.UserGear
    public void setTargetDistance(Double distance) {
        this.targetDistance = distance;
    }

    @Override // com.ua.sdk.gear.user.UserGear
    public void setDefaultActivities(List<EntityRef<ActivityType>> activities) {
        for (EntityRef<ActivityType> entityRef : activities) {
            Link link = new Link(entityRef.getHref(), entityRef.getId());
            addLink(REF_DEFAULT_ACTIVITIES, link);
        }
    }

    @Override // com.ua.sdk.gear.user.UserGear
    public void setRetired(Boolean isRetired) {
        this.isRetired = isRetired;
    }

    @Override // com.ua.sdk.Resource
    public UserGearRef getRef() {
        Link link = getLink("self");
        if (link == null) {
            return null;
        }
        return new UserGearRef(link.getId(), link.getHref());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.gear, 0);
        dest.writeString(this.name);
        dest.writeValue(this.initialDistance);
        dest.writeValue(this.targetDistance);
        dest.writeParcelable(this.purchaseDate, 0);
        dest.writeValue(this.currentDistance);
        dest.writeValue(this.isRetired);
    }

    private UserGearImpl(Parcel in) {
        super(in);
        this.gear = (Gear) in.readParcelable(Gear.class.getClassLoader());
        this.name = in.readString();
        this.initialDistance = (Double) in.readValue(Double.class.getClassLoader());
        this.targetDistance = (Double) in.readValue(Double.class.getClassLoader());
        this.purchaseDate = (LocalDate) in.readParcelable(LocalDate.class.getClassLoader());
        this.currentDistance = (Double) in.readValue(Double.class.getClassLoader());
        this.isRetired = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }
}
