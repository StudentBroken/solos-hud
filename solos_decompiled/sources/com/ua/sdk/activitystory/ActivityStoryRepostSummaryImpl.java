package com.ua.sdk.activitystory;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityStoryRepostSummaryImpl implements ActivityStoryRepostSummary {
    public static Parcelable.Creator<ActivityStoryRepostSummaryImpl> CREATOR = new Parcelable.Creator<ActivityStoryRepostSummaryImpl>() { // from class: com.ua.sdk.activitystory.ActivityStoryRepostSummaryImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryRepostSummaryImpl createFromParcel(Parcel source) {
            return new ActivityStoryRepostSummaryImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryRepostSummaryImpl[] newArray(int size) {
            return new ActivityStoryRepostSummaryImpl[size];
        }
    };

    @SerializedName("items")
    ArrayList<ActivityStory> mItems;

    @SerializedName("reposted")
    Boolean mReposted;

    @SerializedName("count")
    Integer mTotalCount;

    @Override // com.ua.sdk.activitystory.ActivityStoryRepostSummary
    public int getTotalCount() {
        if (this.mTotalCount != null) {
            return this.mTotalCount.intValue();
        }
        return 0;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryRepostSummary
    public boolean isReposted() {
        if (this.mReposted != null) {
            return this.mReposted.booleanValue();
        }
        return false;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryRepostSummary
    public List<ActivityStory> getItems() {
        return this.mItems == null ? Collections.emptyList() : Collections.unmodifiableList(this.mItems);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.mTotalCount);
        dest.writeValue(this.mReposted);
        dest.writeList(this.mItems);
    }

    public ActivityStoryRepostSummaryImpl() {
    }

    private ActivityStoryRepostSummaryImpl(Parcel in) {
        this.mTotalCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.mReposted = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.mItems = new ArrayList<>(5);
        in.readList(this.mItems, ActivityStory.class.getClassLoader());
        if (this.mItems.isEmpty()) {
            this.mItems = null;
        }
    }
}
