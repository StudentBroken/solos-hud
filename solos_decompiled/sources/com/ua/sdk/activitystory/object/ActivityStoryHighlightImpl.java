package com.ua.sdk.activitystory.object;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.activitystory.ActivityStoryHighlight;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityStoryHighlightImpl implements ActivityStoryHighlight {
    public static Parcelable.Creator<ActivityStoryHighlightImpl> CREATOR = new Parcelable.Creator<ActivityStoryHighlightImpl>() { // from class: com.ua.sdk.activitystory.object.ActivityStoryHighlightImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryHighlightImpl createFromParcel(Parcel source) {
            return new ActivityStoryHighlightImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryHighlightImpl[] newArray(int size) {
            return new ActivityStoryHighlightImpl[size];
        }
    };

    @SerializedName("key")
    String mKey;

    @SerializedName("percentile")
    Double mPercentile;

    @SerializedName("thumbnail_url")
    String mThumbnailUrl;
    transient Number mValue;

    public ActivityStoryHighlightImpl() {
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryHighlight
    public String getKey() {
        return this.mKey;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryHighlight
    public Number getValue() {
        return this.mValue;
    }

    public void setValue(Number val) {
        this.mValue = val;
    }

    public void setKey(String mKey) {
        this.mKey = mKey;
    }

    public void setPercentile(Double mPercentile) {
        this.mPercentile = mPercentile;
    }

    public void setThumbnailUrl(String mThumbnailUrl) {
        this.mThumbnailUrl = mThumbnailUrl;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryHighlight
    public Double getPercentile() {
        return this.mPercentile;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryHighlight
    public String getThumbnailUrl() {
        return this.mThumbnailUrl;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mKey);
        dest.writeValue(this.mPercentile);
        dest.writeString(this.mThumbnailUrl);
    }

    private ActivityStoryHighlightImpl(Parcel in) {
        this.mKey = in.readString();
        this.mPercentile = (Double) in.readValue(Double.class.getClassLoader());
        this.mThumbnailUrl = in.readString();
    }
}
