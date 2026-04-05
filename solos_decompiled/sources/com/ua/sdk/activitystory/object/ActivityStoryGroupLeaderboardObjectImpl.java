package com.ua.sdk.activitystory.object;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.activitystory.ActivityStoryGroupLeaderboard;
import com.ua.sdk.activitystory.ActivityStoryGroupLeaderboardObject;
import com.ua.sdk.activitystory.ActivityStoryObject;
import com.ua.sdk.internal.ApiTransferObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityStoryGroupLeaderboardObjectImpl extends ApiTransferObject implements ActivityStoryGroupLeaderboardObject {
    public static Parcelable.Creator<ActivityStoryGroupLeaderboardObjectImpl> CREATOR = new Parcelable.Creator<ActivityStoryGroupLeaderboardObjectImpl>() { // from class: com.ua.sdk.activitystory.object.ActivityStoryGroupLeaderboardObjectImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryGroupLeaderboardObjectImpl createFromParcel(Parcel source) {
            return new ActivityStoryGroupLeaderboardObjectImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryGroupLeaderboardObjectImpl[] newArray(int size) {
            return new ActivityStoryGroupLeaderboardObjectImpl[size];
        }
    };

    @SerializedName("end_time")
    private Date endTime;

    @SerializedName("leaderboard")
    private List<ActivityStoryGroupLeaderboard> leaderboard;

    @SerializedName("result")
    private ActivityStoryGroupLeaderboard result;

    @SerializedName("start_time")
    private Date startTime;

    public ActivityStoryGroupLeaderboardObjectImpl() {
        this.leaderboard = new ArrayList(10);
    }

    private ActivityStoryGroupLeaderboardObjectImpl(Parcel in) {
        super(in);
        this.leaderboard = new ArrayList(10);
        this.startTime = (Date) in.readValue(Date.class.getClassLoader());
        this.endTime = (Date) in.readValue(Date.class.getClassLoader());
        in.readList(this.leaderboard, ActivityStoryGroupLeaderboard.class.getClassLoader());
        this.result = (ActivityStoryGroupLeaderboard) in.readValue(ActivityStoryGroupLeaderboard.class.getClassLoader());
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupLeaderboardObject
    public Date getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupLeaderboardObject
    public Date getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupLeaderboardObject
    public List<ActivityStoryGroupLeaderboard> getLeaderboard() {
        return this.leaderboard;
    }

    public void setLeaderboard(List<ActivityStoryGroupLeaderboard> leaderboard) {
        this.leaderboard.clear();
        this.leaderboard.addAll(leaderboard);
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupLeaderboardObject
    public ActivityStoryGroupLeaderboard getResult() {
        return this.result;
    }

    public void setResult(ActivityStoryGroupLeaderboard result) {
        this.result = result;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryObject
    public ActivityStoryObject.Type getType() {
        return ActivityStoryObject.Type.GROUP_LEADERBOARD;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.startTime);
        dest.writeValue(this.endTime);
        dest.writeList(this.leaderboard);
        dest.writeValue(this.result);
    }
}
