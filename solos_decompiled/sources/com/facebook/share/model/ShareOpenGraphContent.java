package com.facebook.share.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareOpenGraphAction;

/* JADX INFO: loaded from: classes41.dex */
public final class ShareOpenGraphContent extends ShareContent<ShareOpenGraphContent, Builder> {
    public static final Parcelable.Creator<ShareOpenGraphContent> CREATOR = new Parcelable.Creator<ShareOpenGraphContent>() { // from class: com.facebook.share.model.ShareOpenGraphContent.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ShareOpenGraphContent createFromParcel(Parcel in) {
            return new ShareOpenGraphContent(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ShareOpenGraphContent[] newArray(int size) {
            return new ShareOpenGraphContent[size];
        }
    };
    private final ShareOpenGraphAction action;
    private final String previewPropertyName;

    private ShareOpenGraphContent(Builder builder) {
        super(builder);
        this.action = builder.action;
        this.previewPropertyName = builder.previewPropertyName;
    }

    ShareOpenGraphContent(Parcel in) {
        super(in);
        this.action = new ShareOpenGraphAction.Builder().readFrom(in).build();
        this.previewPropertyName = in.readString();
    }

    @Nullable
    public ShareOpenGraphAction getAction() {
        return this.action;
    }

    @Nullable
    public String getPreviewPropertyName() {
        return this.previewPropertyName;
    }

    @Override // com.facebook.share.model.ShareContent, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.facebook.share.model.ShareContent, android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeParcelable(this.action, 0);
        out.writeString(this.previewPropertyName);
    }

    public static final class Builder extends ShareContent.Builder<ShareOpenGraphContent, Builder> {
        private ShareOpenGraphAction action;
        private String previewPropertyName;

        public Builder setAction(@Nullable ShareOpenGraphAction action) {
            this.action = action == null ? null : new ShareOpenGraphAction.Builder().readFrom(action).build();
            return this;
        }

        public Builder setPreviewPropertyName(@Nullable String previewPropertyName) {
            this.previewPropertyName = previewPropertyName;
            return this;
        }

        @Override // com.facebook.share.ShareBuilder
        public ShareOpenGraphContent build() {
            return new ShareOpenGraphContent(this);
        }

        @Override // com.facebook.share.model.ShareContent.Builder, com.facebook.share.model.ShareModelBuilder
        public Builder readFrom(ShareOpenGraphContent model) {
            return model == null ? this : ((Builder) super.readFrom(model)).setAction(model.getAction()).setPreviewPropertyName(model.getPreviewPropertyName());
        }
    }
}
