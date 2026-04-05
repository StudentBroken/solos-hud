package com.ua.sdk.activitystory.object;

import android.os.Parcel;
import android.os.Parcelable;
import com.facebook.share.internal.ShareConstants;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.activitystory.ActivityStoryHighlight;
import com.ua.sdk.activitystory.ActivityStoryObject;
import com.ua.sdk.activitystory.ActivityStoryRouteObject;
import com.ua.sdk.datapoint.BaseDataTypes;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.Link;
import com.ua.sdk.location.Location;
import com.ua.sdk.privacy.Privacy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityStoryRouteObjectImpl extends ApiTransferObject implements ActivityStoryRouteObject, Parcelable {
    public static Parcelable.Creator<ActivityStoryRouteObjectImpl> CREATOR = new Parcelable.Creator<ActivityStoryRouteObjectImpl>() { // from class: com.ua.sdk.activitystory.object.ActivityStoryRouteObjectImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryRouteObjectImpl createFromParcel(Parcel source) {
            return new ActivityStoryRouteObjectImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryRouteObjectImpl[] newArray(int size) {
            return new ActivityStoryRouteObjectImpl[size];
        }
    };

    @SerializedName(BaseDataTypes.ID_DISTANCE)
    private Double mDistance;

    @SerializedName("highlights")
    private List<ActivityStoryHighlight> mHighlights;

    @SerializedName("location")
    private Location mLocation;

    @SerializedName(ShareConstants.WEB_DIALOG_PARAM_PRIVACY)
    private Privacy mPrivacy;

    @SerializedName("title")
    private String mTitle;

    public ActivityStoryRouteObjectImpl() {
    }

    public void setDistance(Double mDistance) {
        this.mDistance = mDistance;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setPrivacy(Privacy mPrivacy) {
        this.mPrivacy = mPrivacy;
    }

    public void setHighlights(List<ActivityStoryHighlight> mHighlights) {
        this.mHighlights = mHighlights;
    }

    public void setLocation(Location mLocation) {
        this.mLocation = mLocation;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryRouteObject
    public Double getDistance() {
        return this.mDistance;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryRouteObject
    public String getTitle() {
        return this.mTitle;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryRouteObject
    public Privacy getPrivacy() {
        return this.mPrivacy;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryRouteObject
    public List<ActivityStoryHighlight> getHighlights() {
        return this.mHighlights == null ? Collections.emptyList() : this.mHighlights;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryRouteObject
    public Location getLocation() {
        return this.mLocation;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryRouteObject, com.ua.sdk.activitystory.ActivityStoryObject
    public ActivityStoryObject.Type getType() {
        return ActivityStoryObject.Type.ROUTE;
    }

    public String getRouteId() {
        Link link = getLink("self");
        if (link == null) {
            return null;
        }
        return link.getId();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.mDistance);
        dest.writeString(this.mTitle);
        dest.writeParcelable(this.mPrivacy, flags);
        dest.writeList(this.mHighlights);
        dest.writeParcelable(this.mLocation, 0);
    }

    private ActivityStoryRouteObjectImpl(Parcel in) {
        super(in);
        this.mDistance = (Double) in.readValue(Double.class.getClassLoader());
        this.mTitle = in.readString();
        this.mPrivacy = (Privacy) in.readParcelable(Privacy.class.getClassLoader());
        this.mHighlights = new ArrayList();
        in.readList(this.mHighlights, ActivityStoryHighlightImpl.class.getClassLoader());
        if (this.mHighlights.isEmpty()) {
            this.mHighlights = null;
        }
        this.mLocation = (Location) in.readParcelable(Location.class.getClassLoader());
    }
}
