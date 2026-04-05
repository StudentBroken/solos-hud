package com.ua.sdk.activitystory;

import android.os.Parcel;
import android.os.Parcelable;
import com.facebook.share.internal.ShareConstants;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.internal.Link;
import com.ua.sdk.privacy.Privacy;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityStoryRpcPostObject extends ActivityStoryImpl implements Parcelable {
    public static Parcelable.Creator<ActivityStoryRpcPostObject> CREATOR = new Parcelable.Creator<ActivityStoryRpcPostObject>() { // from class: com.ua.sdk.activitystory.ActivityStoryRpcPostObject.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryRpcPostObject createFromParcel(Parcel source) {
            return new ActivityStoryRpcPostObject(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryRpcPostObject[] newArray(int size) {
            return new ActivityStoryRpcPostObject[size];
        }
    };

    @SerializedName(ShareConstants.WEB_DIALOG_PARAM_PRIVACY)
    Privacy privacy;

    public ActivityStoryRpcPostObject() {
    }

    private ActivityStoryRpcPostObject(Parcel in) {
        this.privacy = (Privacy) in.readParcelable(Privacy.class.getClassLoader());
    }

    private ActivityStoryRpcPostObject(Builder init) {
        this.privacy = init.privacy;
        setLink("self", init.link);
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryImpl, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryImpl, com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.privacy, flags);
    }

    public static class Builder {
        Link link;
        final Privacy privacy;

        public Builder(Privacy privacy) {
            this.privacy = privacy;
        }

        public Builder setLink(String href) {
            this.link = new Link(href);
            return this;
        }

        public ActivityStoryRpcPostObject build() {
            return new ActivityStoryRpcPostObject(this);
        }
    }
}
