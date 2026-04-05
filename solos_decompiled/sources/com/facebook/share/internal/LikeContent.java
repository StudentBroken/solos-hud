package com.facebook.share.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.facebook.share.model.ShareModel;
import com.facebook.share.model.ShareModelBuilder;

/* JADX INFO: loaded from: classes41.dex */
public class LikeContent implements ShareModel {
    public static final Parcelable.Creator<LikeContent> CREATOR = new Parcelable.Creator<LikeContent>() { // from class: com.facebook.share.internal.LikeContent.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LikeContent createFromParcel(Parcel in) {
            return new LikeContent(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LikeContent[] newArray(int size) {
            return new LikeContent[size];
        }
    };
    private final String objectId;
    private final String objectType;

    private LikeContent(Builder builder) {
        this.objectId = builder.objectId;
        this.objectType = builder.objectType;
    }

    LikeContent(Parcel in) {
        this.objectId = in.readString();
        this.objectType = in.readString();
    }

    public String getObjectId() {
        return this.objectId;
    }

    public String getObjectType() {
        return this.objectType;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.objectId);
        out.writeString(this.objectType);
    }

    public static class Builder implements ShareModelBuilder<LikeContent, Builder> {
        private String objectId;
        private String objectType;

        public Builder setObjectId(String objectId) {
            this.objectId = objectId;
            return this;
        }

        public Builder setObjectType(String objectType) {
            this.objectType = objectType;
            return this;
        }

        @Override // com.facebook.share.ShareBuilder
        public LikeContent build() {
            return new LikeContent(this);
        }

        @Override // com.facebook.share.model.ShareModelBuilder
        public Builder readFrom(LikeContent content) {
            return content == null ? this : setObjectId(content.getObjectId()).setObjectType(content.getObjectType());
        }
    }
}
