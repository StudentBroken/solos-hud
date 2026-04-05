package com.facebook.share.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.SharePhoto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes41.dex */
public final class SharePhotoContent extends ShareContent<SharePhotoContent, Builder> {
    public static final Parcelable.Creator<SharePhotoContent> CREATOR = new Parcelable.Creator<SharePhotoContent>() { // from class: com.facebook.share.model.SharePhotoContent.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SharePhotoContent createFromParcel(Parcel in) {
            return new SharePhotoContent(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SharePhotoContent[] newArray(int size) {
            return new SharePhotoContent[size];
        }
    };
    private final List<SharePhoto> photos;

    private SharePhotoContent(Builder builder) {
        super(builder);
        this.photos = Collections.unmodifiableList(builder.photos);
    }

    SharePhotoContent(Parcel in) {
        super(in);
        this.photos = Collections.unmodifiableList(SharePhoto.Builder.readPhotoListFrom(in));
    }

    @Nullable
    public List<SharePhoto> getPhotos() {
        return this.photos;
    }

    @Override // com.facebook.share.model.ShareContent, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.facebook.share.model.ShareContent, android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        SharePhoto.Builder.writePhotoListTo(out, flags, this.photos);
    }

    public static class Builder extends ShareContent.Builder<SharePhotoContent, Builder> {
        private final List<SharePhoto> photos = new ArrayList();

        public Builder addPhoto(@Nullable SharePhoto photo) {
            if (photo != null) {
                this.photos.add(new SharePhoto.Builder().readFrom(photo).build());
            }
            return this;
        }

        public Builder addPhotos(@Nullable List<SharePhoto> photos) {
            if (photos != null) {
                for (SharePhoto photo : photos) {
                    addPhoto(photo);
                }
            }
            return this;
        }

        @Override // com.facebook.share.ShareBuilder
        public SharePhotoContent build() {
            return new SharePhotoContent(this);
        }

        @Override // com.facebook.share.model.ShareContent.Builder, com.facebook.share.model.ShareModelBuilder
        public Builder readFrom(SharePhotoContent model) {
            return model == null ? this : ((Builder) super.readFrom(model)).addPhotos(model.getPhotos());
        }

        public Builder setPhotos(@Nullable List<SharePhoto> photos) {
            this.photos.clear();
            addPhotos(photos);
            return this;
        }
    }
}
