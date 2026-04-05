package com.ua.sdk.activitystory;

import android.os.Parcel;
import android.os.Parcelable;
import com.facebook.share.internal.ShareConstants;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.ImageUrl;
import com.ua.sdk.activitystory.Attachment;
import com.ua.sdk.activitystory.VideoAttachment;
import com.ua.sdk.internal.BaseReferenceBuilder;
import com.ua.sdk.internal.ImageUrlImpl;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public class VideoAttachmentImpl implements VideoAttachment {
    public static final Parcelable.Creator<VideoAttachmentImpl> CREATOR = new Parcelable.Creator<VideoAttachmentImpl>() { // from class: com.ua.sdk.activitystory.VideoAttachmentImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VideoAttachmentImpl createFromParcel(Parcel source) {
            return new VideoAttachmentImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VideoAttachmentImpl[] newArray(int size) {
            return new VideoAttachmentImpl[size];
        }
    };

    @SerializedName("object")
    Data data;
    transient ImageUrl imageUrl;
    transient VideoAttachment.Provider provider;

    public VideoAttachmentImpl() {
    }

    public VideoAttachmentImpl(Attachment.Type type) {
        this.data = new Data();
        this.data.type = type;
    }

    @Override // com.ua.sdk.activitystory.Attachment
    public String getUri() {
        return this.data.uri;
    }

    @Override // com.ua.sdk.activitystory.VideoAttachment
    public String getProviderId() {
        return this.data.providerId;
    }

    @Override // com.ua.sdk.activitystory.VideoAttachment
    public ImageUrl getThumbnailUrl() {
        if (this.imageUrl == null) {
            this.imageUrl = ImageUrlImpl.getBuilder().setUri(this.data.thumbnailUri).setTemplate(this.data.thumbnailUriTemplate).build();
        }
        return this.imageUrl;
    }

    @Override // com.ua.sdk.activitystory.VideoAttachment
    public VideoAttachment.Provider getProvider() {
        if (this.provider == null) {
            if (this.data.providerId != null && this.data.providerId.equalsIgnoreCase(VideoAttachment.Provider.OOYALA.name())) {
                this.provider = VideoAttachment.Provider.OOYALA;
            } else {
                this.provider = VideoAttachment.Provider.UNKNOWN;
            }
        }
        return this.provider;
    }

    @Override // com.ua.sdk.activitystory.VideoAttachment
    public String getProviderString() {
        return this.data.provider;
    }

    @Override // com.ua.sdk.activitystory.VideoAttachment, com.ua.sdk.activitystory.Attachment
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

    private VideoAttachmentImpl(Parcel in) {
        this.data = (Data) in.readParcelable(Data.class.getClassLoader());
    }

    public static class Data implements Parcelable {
        public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() { // from class: com.ua.sdk.activitystory.VideoAttachmentImpl.Data.1
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

        @SerializedName("provider")
        String provider;

        @SerializedName("provider_id")
        String providerId;

        @SerializedName("published")
        Date published;

        @SerializedName("status")
        Attachment.Status status;

        @SerializedName("thumbnail_uri")
        String thumbnailUri;

        @SerializedName("thumbnail_uri_template")
        String thumbnailUriTemplate;

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
            dest.writeInt(this.type == null ? -1 : this.type.ordinal());
            dest.writeLong(this.published != null ? this.published.getTime() : -1L);
            dest.writeString(this.provider);
            dest.writeString(this.providerId);
            dest.writeString(this.thumbnailUri);
            dest.writeString(this.thumbnailUriTemplate);
            dest.writeInt(this.status != null ? this.status.ordinal() : -1);
        }

        public Data() {
        }

        private Data(Parcel in) {
            this.uri = in.readString();
            int tmpType = in.readInt();
            this.type = tmpType == -1 ? null : Attachment.Type.values()[tmpType];
            long tmpPublished = in.readLong();
            this.published = tmpPublished == -1 ? null : new Date(tmpPublished);
            this.provider = in.readString();
            this.providerId = in.readString();
            this.thumbnailUri = in.readString();
            this.thumbnailUriTemplate = in.readString();
            int tmpStatus = in.readInt();
            this.status = tmpStatus != -1 ? Attachment.Status.values()[tmpStatus] : null;
        }
    }

    static class ImageUriBuilder extends BaseReferenceBuilder {
        public ImageUriBuilder(String hrefTemplate) {
            super(hrefTemplate);
        }

        public ImageUriBuilder setWidth(int width) {
            setParam("width_px", width);
            return this;
        }

        public ImageUriBuilder setHeight(int height) {
            setParam("height_px", height);
            return this;
        }
    }
}
