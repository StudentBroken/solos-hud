package com.facebook.share.model;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes41.dex */
public final class AppGroupCreationContent implements ShareModel {
    public static final Parcelable.Creator<AppGroupCreationContent> CREATOR = new Parcelable.Creator<AppGroupCreationContent>() { // from class: com.facebook.share.model.AppGroupCreationContent.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AppGroupCreationContent createFromParcel(Parcel in) {
            return new AppGroupCreationContent(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AppGroupCreationContent[] newArray(int size) {
            return new AppGroupCreationContent[size];
        }
    };
    private final String description;
    private final String name;
    private AppGroupPrivacy privacy;

    public enum AppGroupPrivacy {
        Open,
        Closed
    }

    private AppGroupCreationContent(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.privacy = builder.privacy;
    }

    AppGroupCreationContent(Parcel in) {
        this.name = in.readString();
        this.description = in.readString();
        this.privacy = (AppGroupPrivacy) in.readSerializable();
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public AppGroupPrivacy getAppGroupPrivacy() {
        return this.privacy;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.name);
        out.writeString(this.description);
        out.writeSerializable(this.privacy);
    }

    public static class Builder implements ShareModelBuilder<AppGroupCreationContent, Builder> {
        private String description;
        private String name;
        private AppGroupPrivacy privacy;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setAppGroupPrivacy(AppGroupPrivacy privacy) {
            this.privacy = privacy;
            return this;
        }

        @Override // com.facebook.share.ShareBuilder
        public AppGroupCreationContent build() {
            return new AppGroupCreationContent(this);
        }

        @Override // com.facebook.share.model.ShareModelBuilder
        public Builder readFrom(AppGroupCreationContent content) {
            return content == null ? this : setName(content.getName()).setDescription(content.getDescription()).setAppGroupPrivacy(content.getAppGroupPrivacy());
        }
    }
}
