package com.facebook.share.model;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes41.dex */
public class ShareHashtag implements ShareModel {
    public static final Parcelable.Creator<ShareHashtag> CREATOR = new Parcelable.Creator<ShareHashtag>() { // from class: com.facebook.share.model.ShareHashtag.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ShareHashtag createFromParcel(Parcel in) {
            return new ShareHashtag(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ShareHashtag[] newArray(int size) {
            return new ShareHashtag[size];
        }
    };
    private final String hashtag;

    private ShareHashtag(Builder builder) {
        this.hashtag = builder.hashtag;
    }

    ShareHashtag(Parcel in) {
        this.hashtag = in.readString();
    }

    public String getHashtag() {
        return this.hashtag;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.hashtag);
    }

    public static class Builder implements ShareModelBuilder<ShareHashtag, Builder> {
        private String hashtag;

        public Builder setHashtag(String hashtag) {
            this.hashtag = hashtag;
            return this;
        }

        public String getHashtag() {
            return this.hashtag;
        }

        @Override // com.facebook.share.model.ShareModelBuilder
        public Builder readFrom(ShareHashtag model) {
            return model == null ? this : setHashtag(model.getHashtag());
        }

        Builder readFrom(Parcel parcel) {
            return readFrom((ShareHashtag) parcel.readParcelable(ShareHashtag.class.getClassLoader()));
        }

        @Override // com.facebook.share.ShareBuilder
        public ShareHashtag build() {
            return new ShareHashtag(this);
        }
    }
}
