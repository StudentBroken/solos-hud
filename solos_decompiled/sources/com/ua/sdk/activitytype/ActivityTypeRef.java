package com.ua.sdk.activitytype;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.EntityRef;
import com.ua.sdk.internal.BaseReferenceBuilder;
import com.ua.sdk.internal.LinkEntityRef;
import com.ua.sdk.internal.Precondition;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityTypeRef extends LinkEntityRef<ActivityType> implements EntityRef<ActivityType> {
    public static Parcelable.Creator<ActivityTypeRef> CREATOR = new Parcelable.Creator<ActivityTypeRef>() { // from class: com.ua.sdk.activitytype.ActivityTypeRef.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityTypeRef createFromParcel(Parcel source) {
            return new ActivityTypeRef(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityTypeRef[] newArray(int size) {
            return new ActivityTypeRef[size];
        }
    };

    public static Builder getBuilder() {
        return new Builder();
    }

    private ActivityTypeRef(Builder builder) {
        super(builder.id, builder.localId, builder.getHref());
    }

    @Override // com.ua.sdk.internal.LinkEntityRef, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.LinkEntityRef, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
    }

    private ActivityTypeRef(Parcel source) {
        super(source);
    }

    public static class Builder extends BaseReferenceBuilder {
        private String id;
        private long localId;

        protected Builder() {
            super("/v7.0/activity_type/{id}/");
            this.localId = -1L;
        }

        public Builder setActivityTypeId(String id) {
            setParam("id", id);
            this.id = id;
            return this;
        }

        public Builder setLocalId(long localId) {
            this.localId = localId;
            return this;
        }

        public ActivityTypeRef build() {
            Precondition.isNotNull(this.id, "id");
            return new ActivityTypeRef(this);
        }
    }
}
