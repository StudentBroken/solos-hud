package com.ua.sdk.suggestedfriends;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

/* JADX INFO: loaded from: classes65.dex */
public class SuggestedFriendsReasonImpl implements SuggestedFriendsReason {
    public static Parcelable.Creator<SuggestedFriendsReasonImpl> CREATOR = new Parcelable.Creator<SuggestedFriendsReasonImpl>() { // from class: com.ua.sdk.suggestedfriends.SuggestedFriendsReasonImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SuggestedFriendsReasonImpl createFromParcel(Parcel source) {
            return new SuggestedFriendsReasonImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SuggestedFriendsReasonImpl[] newArray(int size) {
            return new SuggestedFriendsReasonImpl[size];
        }
    };

    @SerializedName("source")
    String source;

    @SerializedName("weight")
    Double weight;

    public SuggestedFriendsReasonImpl() {
    }

    protected SuggestedFriendsReasonImpl(String source, Double weight) {
        this.source = source;
        this.weight = weight;
    }

    @Override // com.ua.sdk.suggestedfriends.SuggestedFriendsReason
    public String getSource() {
        return this.source;
    }

    @Override // com.ua.sdk.suggestedfriends.SuggestedFriendsReason
    public Double getWeight() {
        return this.weight;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.source);
        dest.writeValue(this.weight);
    }

    private SuggestedFriendsReasonImpl(Parcel in) {
        this.source = in.readString();
        this.weight = (Double) in.readValue(Double.class.getClassLoader());
    }
}
