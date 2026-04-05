package com.ua.sdk.activitystory.object;

import android.os.Parcel;
import android.os.Parcelable;
import com.facebook.share.internal.ShareConstants;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.activitystory.ActivityStoryActigraphyObject;
import com.ua.sdk.activitystory.ActivityStoryHighlight;
import com.ua.sdk.activitystory.ActivityStoryObject;
import com.ua.sdk.datapoint.BaseDataTypes;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.privacy.Privacy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityStoryActigraphyObjectImpl extends ApiTransferObject implements ActivityStoryActigraphyObject {
    public static Parcelable.Creator<ActivityStoryActigraphyObjectImpl> CREATOR = new Parcelable.Creator<ActivityStoryActigraphyObjectImpl>() { // from class: com.ua.sdk.activitystory.object.ActivityStoryActigraphyObjectImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryActigraphyObjectImpl createFromParcel(Parcel source) {
            return new ActivityStoryActigraphyObjectImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryActigraphyObjectImpl[] newArray(int size) {
            return new ActivityStoryActigraphyObjectImpl[size];
        }
    };

    @SerializedName("end_time")
    Date mEndTime;

    @SerializedName("highlights")
    List<ActivityStoryHighlight> mHighlights;

    @SerializedName(ShareConstants.WEB_DIALOG_PARAM_PRIVACY)
    Privacy mPrivacy;

    @SerializedName("published")
    Date mPublishedTime;

    @SerializedName("start_time")
    Date mStartTime;

    @SerializedName(BaseDataTypes.ID_STEPS)
    Integer mSteps;

    @Override // com.ua.sdk.activitystory.ActivityStoryActigraphyObject
    public Privacy getPrivacy() {
        return this.mPrivacy;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryActigraphyObject
    public List<ActivityStoryHighlight> getHighlights() {
        return this.mHighlights == null ? Collections.emptyList() : this.mHighlights;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryActigraphyObject
    public Date getStartTime() {
        return this.mStartTime;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryActigraphyObject
    public Date getEndTime() {
        return this.mEndTime;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryActigraphyObject
    public Integer getSteps() {
        return this.mSteps;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryActigraphyObject
    public Date getPublishedTime() {
        return this.mPublishedTime;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryActigraphyObject, com.ua.sdk.activitystory.ActivityStoryObject
    public ActivityStoryObject.Type getType() {
        return ActivityStoryObject.Type.ACTIGRAPHY;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.mPrivacy, flags);
        dest.writeLong(this.mStartTime != null ? this.mStartTime.getTime() : -1L);
        dest.writeLong(this.mEndTime != null ? this.mEndTime.getTime() : -1L);
        dest.writeLong(this.mPublishedTime != null ? this.mPublishedTime.getTime() : -1L);
        dest.writeValue(this.mSteps);
        dest.writeList(this.mHighlights);
    }

    public ActivityStoryActigraphyObjectImpl() {
    }

    private ActivityStoryActigraphyObjectImpl(Parcel in) {
        super(in);
        this.mPrivacy = (Privacy) in.readParcelable(Privacy.class.getClassLoader());
        long tmpMStartTime = in.readLong();
        this.mStartTime = tmpMStartTime == -1 ? null : new Date(tmpMStartTime);
        long tmpMEndTime = in.readLong();
        this.mEndTime = tmpMEndTime == -1 ? null : new Date(tmpMEndTime);
        long tmpMPublishedTime = in.readLong();
        this.mPublishedTime = tmpMPublishedTime == -1 ? null : new Date(tmpMPublishedTime);
        this.mSteps = (Integer) in.readValue(Integer.class.getClassLoader());
        this.mHighlights = new ArrayList();
        in.readList(this.mHighlights, ActivityStoryHighlightImpl.class.getClassLoader());
        if (this.mHighlights.isEmpty()) {
            this.mHighlights = null;
        }
    }
}
