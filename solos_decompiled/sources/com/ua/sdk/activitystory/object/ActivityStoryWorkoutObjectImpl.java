package com.ua.sdk.activitystory.object;

import android.os.Parcel;
import android.os.Parcelable;
import com.facebook.share.internal.ShareConstants;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.EntityRef;
import com.ua.sdk.activitystory.ActivityStoryHighlight;
import com.ua.sdk.activitystory.ActivityStoryObject;
import com.ua.sdk.activitystory.ActivityStoryWorkoutObject;
import com.ua.sdk.activitytype.ActivityType;
import com.ua.sdk.datapoint.BaseDataTypes;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.Link;
import com.ua.sdk.privacy.Privacy;
import com.ua.sdk.workout.WorkoutRef;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityStoryWorkoutObjectImpl extends ApiTransferObject implements ActivityStoryWorkoutObject {
    public static Parcelable.Creator<ActivityStoryWorkoutObjectImpl> CREATOR = new Parcelable.Creator<ActivityStoryWorkoutObjectImpl>() { // from class: com.ua.sdk.activitystory.object.ActivityStoryWorkoutObjectImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryWorkoutObjectImpl createFromParcel(Parcel source) {
            return new ActivityStoryWorkoutObjectImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryWorkoutObjectImpl[] newArray(int size) {
            return new ActivityStoryWorkoutObjectImpl[size];
        }
    };

    @SerializedName("avg_pace")
    Double mAveragePace;

    @SerializedName("avg_speed")
    Double mAverageSpeed;

    @SerializedName(BaseDataTypes.ID_DISTANCE)
    Double mDistance;

    @SerializedName("duration")
    Long mDuration;

    @SerializedName("energy_burned")
    Integer mEnergyBurned;

    @SerializedName("highlights")
    List<ActivityStoryHighlight> mHighlights;

    @SerializedName("notes")
    String mNotes;

    @SerializedName(ShareConstants.WEB_DIALOG_PARAM_PRIVACY)
    Privacy mPrivacy;

    @SerializedName(BaseDataTypes.ID_STEPS)
    Integer mSteps;

    @SerializedName("title")
    String mTitle;

    @Override // com.ua.sdk.activitystory.ActivityStoryWorkoutObject
    public Double getDistance() {
        return this.mDistance;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryWorkoutObject
    public Double getAveragePace() {
        return this.mAveragePace;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryWorkoutObject
    public String getTitle() {
        return this.mTitle;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryWorkoutObject
    public List<ActivityStoryHighlight> getHighlights() {
        return this.mHighlights == null ? Collections.emptyList() : this.mHighlights;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryWorkoutObject
    public String getNotes() {
        return this.mNotes;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryWorkoutObject
    public Privacy getPrivacy() {
        return this.mPrivacy;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryWorkoutObject
    public Integer getSteps() {
        return this.mSteps;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryWorkoutObject
    public Double getAverageSpeed() {
        return this.mAverageSpeed;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryWorkoutObject
    public long getDuration() {
        if (this.mDuration == null) {
            return 0L;
        }
        return this.mDuration.longValue();
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryWorkoutObject
    public Integer getEnergyBurned() {
        return this.mEnergyBurned;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryWorkoutObject
    public EntityRef<ActivityType> getActivityTypeRef() {
        return null;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryWorkoutObject
    public WorkoutRef getWorkoutRef() {
        Link link = getLink("self");
        if (link == null) {
            return null;
        }
        return new WorkoutRef(link.getId(), link.getHref());
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryWorkoutObject, com.ua.sdk.activitystory.ActivityStoryObject
    public ActivityStoryObject.Type getType() {
        return ActivityStoryObject.Type.WORKOUT;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.mDistance);
        dest.writeValue(this.mAveragePace);
        dest.writeString(this.mTitle);
        dest.writeString(this.mNotes);
        dest.writeParcelable(this.mPrivacy, flags);
        dest.writeValue(this.mSteps);
        dest.writeValue(this.mAverageSpeed);
        dest.writeValue(this.mEnergyBurned);
        dest.writeValue(this.mDuration);
        dest.writeList(this.mHighlights);
    }

    public ActivityStoryWorkoutObjectImpl() {
    }

    public ActivityStoryWorkoutObjectImpl(String notes) {
        this.mNotes = notes;
    }

    private ActivityStoryWorkoutObjectImpl(Parcel in) {
        super(in);
        this.mDistance = (Double) in.readValue(Double.class.getClassLoader());
        this.mAveragePace = (Double) in.readValue(Double.class.getClassLoader());
        this.mTitle = in.readString();
        this.mNotes = in.readString();
        this.mPrivacy = (Privacy) in.readParcelable(Privacy.class.getClassLoader());
        this.mSteps = (Integer) in.readValue(Integer.class.getClassLoader());
        this.mAverageSpeed = (Double) in.readValue(Double.class.getClassLoader());
        this.mEnergyBurned = (Integer) in.readValue(Integer.class.getClassLoader());
        this.mDuration = (Long) in.readValue(Long.class.getClassLoader());
        this.mHighlights = new ArrayList();
        in.readList(this.mHighlights, ActivityStoryHighlightImpl.class.getClassLoader());
        if (this.mHighlights.isEmpty()) {
            this.mHighlights = null;
        }
    }
}
