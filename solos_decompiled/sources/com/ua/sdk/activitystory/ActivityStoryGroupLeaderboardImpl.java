package com.ua.sdk.activitystory;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.annotations.SerializedName;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityStoryGroupLeaderboardImpl implements ActivityStoryGroupLeaderboard {
    public static Parcelable.Creator<ActivityStoryGroupLeaderboardImpl> CREATOR = new Parcelable.Creator<ActivityStoryGroupLeaderboardImpl>() { // from class: com.ua.sdk.activitystory.ActivityStoryGroupLeaderboardImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryGroupLeaderboardImpl createFromParcel(Parcel source) {
            return new ActivityStoryGroupLeaderboardImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryGroupLeaderboardImpl[] newArray(int size) {
            return new ActivityStoryGroupLeaderboardImpl[size];
        }
    };

    @SerializedName("rank")
    int rank;

    @SerializedName("user_id")
    long userId;

    @SerializedName(FirebaseAnalytics.Param.VALUE)
    Object value;

    public ActivityStoryGroupLeaderboardImpl() {
    }

    private ActivityStoryGroupLeaderboardImpl(Parcel in) {
        this.value = in.readValue(Object.class.getClassLoader());
        this.userId = in.readLong();
        this.rank = in.readInt();
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupLeaderboard
    public Double getValueDouble() {
        if (this.value == null) {
            return null;
        }
        if (this.value instanceof Double) {
            return (Double) this.value;
        }
        if (this.value instanceof Long) {
            return Double.valueOf(((Long) this.value).doubleValue());
        }
        return null;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupLeaderboard
    public Long getValueLong() {
        if (this.value == null) {
            return null;
        }
        if (this.value instanceof Long) {
            return (Long) this.value;
        }
        if (this.value instanceof Double) {
            return Long.valueOf(((Double) this.value).longValue());
        }
        return null;
    }

    public void addValue(Object value) {
        this.value = value;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupLeaderboard
    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupLeaderboard
    public int getRank() {
        return this.rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.value);
        dest.writeLong(this.userId);
        dest.writeInt(this.rank);
    }
}
