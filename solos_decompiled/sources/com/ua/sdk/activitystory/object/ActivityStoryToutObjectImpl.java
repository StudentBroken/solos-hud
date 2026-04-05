package com.ua.sdk.activitystory.object;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.activitystory.ActivityStoryObject;
import com.ua.sdk.activitystory.ActivityStoryToutObject;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityStoryToutObjectImpl implements ActivityStoryToutObject {
    public static Parcelable.Creator<ActivityStoryToutObjectImpl> CREATOR = new Parcelable.Creator<ActivityStoryToutObjectImpl>() { // from class: com.ua.sdk.activitystory.object.ActivityStoryToutObjectImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryToutObjectImpl createFromParcel(Parcel source) {
            return new ActivityStoryToutObjectImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryToutObjectImpl[] newArray(int size) {
            return new ActivityStoryToutObjectImpl[size];
        }
    };
    private final ActivityStoryToutObject.Subtype mSubtype;

    public ActivityStoryToutObjectImpl(ActivityStoryToutObject.Subtype subtype) {
        this.mSubtype = subtype;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryToutObject
    public ActivityStoryToutObject.Subtype getSubtype() {
        return this.mSubtype;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryToutObject, com.ua.sdk.activitystory.ActivityStoryObject
    public ActivityStoryObject.Type getType() {
        return ActivityStoryObject.Type.TOUT;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mSubtype == null ? -1 : this.mSubtype.ordinal());
    }

    private ActivityStoryToutObjectImpl(Parcel in) {
        int tmpMSubtype = in.readInt();
        this.mSubtype = tmpMSubtype == -1 ? null : ActivityStoryToutObject.Subtype.values()[tmpMSubtype];
    }
}
