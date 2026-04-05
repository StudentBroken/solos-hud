package com.ua.sdk;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.internal.BaseReferenceBuilder;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.user.User;

/* JADX INFO: loaded from: classes65.dex */
@Deprecated
public class UserSearchByNameListRef implements EntityListRef<User> {
    public static Parcelable.Creator<UserSearchByNameListRef> CREATOR = new Parcelable.Creator<UserSearchByNameListRef>() { // from class: com.ua.sdk.UserSearchByNameListRef.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserSearchByNameListRef createFromParcel(Parcel source) {
            return new UserSearchByNameListRef(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserSearchByNameListRef[] newArray(int size) {
            return new UserSearchByNameListRef[size];
        }
    };
    private final String href;

    private UserSearchByNameListRef(Builder init) {
        this.href = init.getHref();
    }

    @Override // com.ua.sdk.Reference
    public String getId() {
        return null;
    }

    @Override // com.ua.sdk.Reference
    public String getHref() {
        return this.href;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static final class Builder extends BaseReferenceBuilder {
        private String searchName;

        private Builder() {
            super("/v7.0/user/");
        }

        public Builder setSearchName(String searchName) {
            this.searchName = searchName;
            setParam("q", searchName);
            return this;
        }

        public UserSearchByNameListRef build() {
            Precondition.isNotNull(this.searchName);
            return new UserSearchByNameListRef(this);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.href);
    }

    private UserSearchByNameListRef(Parcel in) {
        this.href = in.readString();
    }
}
