package com.facebook.share.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.facebook.share.model.ShareMedia;

/* JADX INFO: loaded from: classes41.dex */
public final class ShareVideo extends ShareMedia {
    public static final Parcelable.Creator<ShareVideo> CREATOR = new Parcelable.Creator<ShareVideo>() { // from class: com.facebook.share.model.ShareVideo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ShareVideo createFromParcel(Parcel source) {
            return new ShareVideo(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ShareVideo[] newArray(int size) {
            return new ShareVideo[size];
        }
    };
    private final Uri localUrl;

    private ShareVideo(Builder builder) {
        super(builder);
        this.localUrl = builder.localUrl;
    }

    ShareVideo(Parcel in) {
        super(in);
        this.localUrl = (Uri) in.readParcelable(Uri.class.getClassLoader());
    }

    @Nullable
    public Uri getLocalUrl() {
        return this.localUrl;
    }

    @Override // com.facebook.share.model.ShareMedia, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.facebook.share.model.ShareMedia, android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeParcelable(this.localUrl, 0);
    }

    @Override // com.facebook.share.model.ShareMedia
    public ShareMedia.Type getMediaType() {
        return ShareMedia.Type.VIDEO;
    }

    public static final class Builder extends ShareMedia.Builder<ShareVideo, Builder> {
        private Uri localUrl;

        public Builder setLocalUrl(@Nullable Uri localUrl) {
            this.localUrl = localUrl;
            return this;
        }

        @Override // com.facebook.share.ShareBuilder
        public ShareVideo build() {
            return new ShareVideo(this);
        }

        @Override // com.facebook.share.model.ShareMedia.Builder, com.facebook.share.model.ShareModelBuilder
        public Builder readFrom(ShareVideo model) {
            return model == null ? this : ((Builder) super.readFrom(model)).setLocalUrl(model.getLocalUrl());
        }

        Builder readFrom(Parcel parcel) {
            return readFrom((ShareVideo) parcel.readParcelable(ShareVideo.class.getClassLoader()));
        }
    }
}
