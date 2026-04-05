package com.ua.sdk.user.profilephoto;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.EntityRef;
import com.ua.sdk.internal.BaseReferenceBuilder;

/* JADX INFO: loaded from: classes65.dex */
public class UserProfilePhotoRef implements EntityRef<UserProfilePhoto>, Parcelable {
    public static final Parcelable.Creator<UserProfilePhotoRef> CREATOR = new Parcelable.Creator<UserProfilePhotoRef>() { // from class: com.ua.sdk.user.profilephoto.UserProfilePhotoRef.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserProfilePhotoRef createFromParcel(Parcel source) {
            return new UserProfilePhotoRef(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserProfilePhotoRef[] newArray(int size) {
            return new UserProfilePhotoRef[size];
        }
    };
    private final String href;
    private final String id;

    private UserProfilePhotoRef(Builder init) {
        this.id = init.id;
        this.href = init.getHref();
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder extends BaseReferenceBuilder {
        private String id;

        private Builder() {
            super("/v7.0/user_profile_photo/{id}/");
        }

        public Builder setId(String id) {
            this.id = id;
            setParam("id", id);
            return this;
        }

        public UserProfilePhotoRef build() {
            return new UserProfilePhotoRef(this);
        }
    }

    @Override // com.ua.sdk.Reference
    public String getId() {
        return this.id;
    }

    @Override // com.ua.sdk.Reference
    public String getHref() {
        return this.href;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.href);
    }

    private UserProfilePhotoRef(Parcel in) {
        this.id = in.readString();
        this.href = in.readString();
    }
}
