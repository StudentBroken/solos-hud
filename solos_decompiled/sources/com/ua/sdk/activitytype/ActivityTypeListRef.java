package com.ua.sdk.activitytype;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.internal.BaseReferenceBuilder;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityTypeListRef implements EntityListRef<ActivityType> {
    public static Parcelable.Creator<ActivityTypeListRef> CREATOR = new Parcelable.Creator<ActivityTypeListRef>() { // from class: com.ua.sdk.activitytype.ActivityTypeListRef.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityTypeListRef createFromParcel(Parcel source) {
            return new ActivityTypeListRef(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityTypeListRef[] newArray(int size) {
            return new ActivityTypeListRef[size];
        }
    };
    private final String href;

    public ActivityTypeListRef(Builder builder) {
        this.href = builder.getHref();
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    @Override // com.ua.sdk.Reference
    public String getId() {
        return null;
    }

    @Override // com.ua.sdk.Reference
    public String getHref() {
        return this.href;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.href);
    }

    private ActivityTypeListRef(Parcel source) {
        this.href = source.readString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ActivityTypeListRef that = (ActivityTypeListRef) o;
        return this.href.equals(that.href);
    }

    public int hashCode() {
        return this.href.hashCode();
    }

    public static class Builder extends BaseReferenceBuilder {
        protected Builder() {
            super("/v7.0/activity_type/");
        }

        public ActivityTypeListRef build() {
            return new ActivityTypeListRef(this);
        }
    }
}
