package com.ua.sdk.activitystory;

import android.os.Parcel;
import android.os.Parcelable;
import com.facebook.share.internal.ShareConstants;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.ImageUrl;
import com.ua.sdk.activitystory.Attachment;
import com.ua.sdk.internal.ImageUrlImpl;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public class PhotoAttachmentImpl implements PhotoAttachment {
    public static final Parcelable.Creator<PhotoAttachmentImpl> CREATOR = new Parcelable.Creator<PhotoAttachmentImpl>() { // from class: com.ua.sdk.activitystory.PhotoAttachmentImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PhotoAttachmentImpl createFromParcel(Parcel source) {
            return new PhotoAttachmentImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PhotoAttachmentImpl[] newArray(int size) {
            return new PhotoAttachmentImpl[size];
        }
    };

    @SerializedName("object")
    Data data;

    public PhotoAttachmentImpl() {
    }

    public PhotoAttachmentImpl(Attachment.Type type) {
        this.data = new Data();
        this.data.type = type;
    }

    @Override // com.ua.sdk.activitystory.Attachment
    public String getUri() {
        return this.data.uri;
    }

    @Override // com.ua.sdk.activitystory.PhotoAttachment
    public ImageUrl getImageUrl() {
        if (this.data.template == null && this.data.uri == null) {
            return null;
        }
        return ImageUrlImpl.getBuilder().setUri(this.data.uri).setTemplate(this.data.template).build();
    }

    @Override // com.ua.sdk.activitystory.PhotoAttachment, com.ua.sdk.activitystory.Attachment
    public Attachment.Type getType() {
        return this.data.type;
    }

    @Override // com.ua.sdk.activitystory.Attachment
    public Attachment.Status getStatus() {
        return this.data.status;
    }

    @Override // com.ua.sdk.activitystory.Attachment
    public Date getPublished() {
        return this.data.published;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.data, flags);
    }

    private PhotoAttachmentImpl(Parcel in) {
        this.data = (Data) in.readParcelable(Data.class.getClassLoader());
    }

    public static class Data implements Parcelable {
        public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() { // from class: com.ua.sdk.activitystory.PhotoAttachmentImpl.Data.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Data createFromParcel(Parcel source) {
                return new Data(source);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Data[] newArray(int size) {
                return new Data[size];
            }
        };

        @SerializedName("published")
        Date published;

        @SerializedName("status")
        Attachment.Status status;

        @SerializedName("template")
        String template;

        @SerializedName(ShareConstants.MEDIA_TYPE)
        Attachment.Type type;

        @SerializedName(ShareConstants.MEDIA_URI)
        String uri;

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.uri);
            dest.writeString(this.template);
            dest.writeInt(this.type == null ? -1 : this.type.ordinal());
            dest.writeLong(this.published != null ? this.published.getTime() : -1L);
            dest.writeInt(this.status != null ? this.status.ordinal() : -1);
        }

        public Data() {
        }

        private Data(Parcel in) {
            this.uri = in.readString();
            this.template = in.readString();
            int tmpType = in.readInt();
            this.type = tmpType == -1 ? null : Attachment.Type.values()[tmpType];
            long tmpPublished = in.readLong();
            this.published = tmpPublished == -1 ? null : new Date(tmpPublished);
            int tmpStatus = in.readInt();
            this.status = tmpStatus != -1 ? Attachment.Status.values()[tmpStatus] : null;
        }
    }
}
