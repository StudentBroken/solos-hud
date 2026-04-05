package com.ua.sdk.user;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.EntityRef;
import com.ua.sdk.internal.BaseReferenceBuilder;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.internal.net.v7.UrlBuilderImpl;

/* JADX INFO: loaded from: classes65.dex */
public class UserRef implements EntityRef<User> {
    public static Parcelable.Creator<UserRef> CREATOR = new Parcelable.Creator<UserRef>() { // from class: com.ua.sdk.user.UserRef.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserRef createFromParcel(Parcel source) {
            return new UserRef(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserRef[] newArray(int size) {
            return new UserRef[size];
        }
    };
    private final String id;

    private UserRef(Builder init) {
        this.id = init.id;
    }

    @Override // com.ua.sdk.Reference
    public String getId() {
        if (this.id == null || this.id.length() <= 0) {
            return null;
        }
        return this.id;
    }

    @Override // com.ua.sdk.Reference
    public String getHref() {
        if (this.id == null || this.id.isEmpty()) {
            return null;
        }
        return String.format(UrlBuilderImpl.GET_USER_URL, this.id);
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static final class Builder extends BaseReferenceBuilder {
        private String id;

        private Builder() {
            super(UrlBuilderImpl.GET_USER_URL);
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public UserRef build() {
            Precondition.isNotNull(this.id, "User Id");
            return new UserRef(this);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
    }

    private UserRef(Parcel in) {
        this.id = in.readString();
    }
}
