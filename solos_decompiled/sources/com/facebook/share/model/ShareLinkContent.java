package com.facebook.share.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.facebook.share.model.ShareContent;

/* JADX INFO: loaded from: classes41.dex */
public final class ShareLinkContent extends ShareContent<ShareLinkContent, Builder> {
    public static final Parcelable.Creator<ShareLinkContent> CREATOR = new Parcelable.Creator<ShareLinkContent>() { // from class: com.facebook.share.model.ShareLinkContent.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ShareLinkContent createFromParcel(Parcel in) {
            return new ShareLinkContent(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ShareLinkContent[] newArray(int size) {
            return new ShareLinkContent[size];
        }
    };
    private final String contentDescription;
    private final String contentTitle;
    private final Uri imageUrl;
    private final String quote;

    private ShareLinkContent(Builder builder) {
        super(builder);
        this.contentDescription = builder.contentDescription;
        this.contentTitle = builder.contentTitle;
        this.imageUrl = builder.imageUrl;
        this.quote = builder.quote;
    }

    ShareLinkContent(Parcel in) {
        super(in);
        this.contentDescription = in.readString();
        this.contentTitle = in.readString();
        this.imageUrl = (Uri) in.readParcelable(Uri.class.getClassLoader());
        this.quote = in.readString();
    }

    public String getContentDescription() {
        return this.contentDescription;
    }

    @Nullable
    public String getContentTitle() {
        return this.contentTitle;
    }

    @Nullable
    public Uri getImageUrl() {
        return this.imageUrl;
    }

    @Nullable
    public String getQuote() {
        return this.quote;
    }

    @Override // com.facebook.share.model.ShareContent, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.facebook.share.model.ShareContent, android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeString(this.contentDescription);
        out.writeString(this.contentTitle);
        out.writeParcelable(this.imageUrl, 0);
        out.writeString(this.quote);
    }

    public static final class Builder extends ShareContent.Builder<ShareLinkContent, Builder> {
        private String contentDescription;
        private String contentTitle;
        private Uri imageUrl;
        private String quote;

        public Builder setContentDescription(@Nullable String contentDescription) {
            this.contentDescription = contentDescription;
            return this;
        }

        public Builder setContentTitle(@Nullable String contentTitle) {
            this.contentTitle = contentTitle;
            return this;
        }

        public Builder setImageUrl(@Nullable Uri imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder setQuote(@Nullable String quote) {
            this.quote = quote;
            return this;
        }

        @Override // com.facebook.share.ShareBuilder
        public ShareLinkContent build() {
            return new ShareLinkContent(this);
        }

        @Override // com.facebook.share.model.ShareContent.Builder, com.facebook.share.model.ShareModelBuilder
        public Builder readFrom(ShareLinkContent model) {
            return model == null ? this : ((Builder) super.readFrom(model)).setContentDescription(model.getContentDescription()).setImageUrl(model.getImageUrl()).setContentTitle(model.getContentTitle()).setQuote(model.getQuote());
        }
    }
}
