package com.facebook.share.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.facebook.share.model.ShareMedia;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes41.dex */
public final class SharePhoto extends ShareMedia {
    public static final Parcelable.Creator<SharePhoto> CREATOR = new Parcelable.Creator<SharePhoto>() { // from class: com.facebook.share.model.SharePhoto.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SharePhoto createFromParcel(Parcel source) {
            return new SharePhoto(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SharePhoto[] newArray(int size) {
            return new SharePhoto[size];
        }
    };
    private final Bitmap bitmap;
    private final String caption;
    private final Uri imageUrl;
    private final boolean userGenerated;

    private SharePhoto(Builder builder) {
        super(builder);
        this.bitmap = builder.bitmap;
        this.imageUrl = builder.imageUrl;
        this.userGenerated = builder.userGenerated;
        this.caption = builder.caption;
    }

    SharePhoto(Parcel in) {
        super(in);
        this.bitmap = (Bitmap) in.readParcelable(Bitmap.class.getClassLoader());
        this.imageUrl = (Uri) in.readParcelable(Uri.class.getClassLoader());
        this.userGenerated = in.readByte() != 0;
        this.caption = in.readString();
    }

    @Nullable
    public Bitmap getBitmap() {
        return this.bitmap;
    }

    @Nullable
    public Uri getImageUrl() {
        return this.imageUrl;
    }

    public boolean getUserGenerated() {
        return this.userGenerated;
    }

    public String getCaption() {
        return this.caption;
    }

    @Override // com.facebook.share.model.ShareMedia, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.facebook.share.model.ShareMedia, android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeParcelable(this.bitmap, 0);
        out.writeParcelable(this.imageUrl, 0);
        out.writeByte((byte) (this.userGenerated ? 1 : 0));
        out.writeString(this.caption);
    }

    @Override // com.facebook.share.model.ShareMedia
    public ShareMedia.Type getMediaType() {
        return ShareMedia.Type.PHOTO;
    }

    public static final class Builder extends ShareMedia.Builder<SharePhoto, Builder> {
        private Bitmap bitmap;
        private String caption;
        private Uri imageUrl;
        private boolean userGenerated;

        public Builder setBitmap(@Nullable Bitmap bitmap) {
            this.bitmap = bitmap;
            return this;
        }

        public Builder setImageUrl(@Nullable Uri imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder setUserGenerated(boolean userGenerated) {
            this.userGenerated = userGenerated;
            return this;
        }

        public Builder setCaption(@Nullable String caption) {
            this.caption = caption;
            return this;
        }

        Uri getImageUrl() {
            return this.imageUrl;
        }

        Bitmap getBitmap() {
            return this.bitmap;
        }

        @Override // com.facebook.share.ShareBuilder
        public SharePhoto build() {
            return new SharePhoto(this);
        }

        @Override // com.facebook.share.model.ShareMedia.Builder, com.facebook.share.model.ShareModelBuilder
        public Builder readFrom(SharePhoto model) {
            return model == null ? this : ((Builder) super.readFrom(model)).setBitmap(model.getBitmap()).setImageUrl(model.getImageUrl()).setUserGenerated(model.getUserGenerated()).setCaption(model.getCaption());
        }

        Builder readFrom(Parcel parcel) {
            return readFrom((SharePhoto) parcel.readParcelable(SharePhoto.class.getClassLoader()));
        }

        static void writePhotoListTo(Parcel out, int parcelFlags, List<SharePhoto> photos) {
            ShareMedia[] array = new ShareMedia[photos.size()];
            for (int i = 0; i < photos.size(); i++) {
                array[i] = photos.get(i);
            }
            out.writeParcelableArray(array, parcelFlags);
        }

        static List<SharePhoto> readPhotoListFrom(Parcel in) {
            List<ShareMedia> media = readListFrom(in);
            List<SharePhoto> photos = new ArrayList<>();
            for (ShareMedia medium : media) {
                if (medium instanceof SharePhoto) {
                    photos.add((SharePhoto) medium);
                }
            }
            return photos;
        }
    }
}
