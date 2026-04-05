package com.ua.sdk.user.permission;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.Resource;

/* JADX INFO: loaded from: classes65.dex */
public class UserPermissionListRef implements EntityListRef<UserPermission> {
    public static Parcelable.Creator<UserPermissionListRef> CREATOR = new Parcelable.Creator<UserPermissionListRef>() { // from class: com.ua.sdk.user.permission.UserPermissionListRef.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserPermissionListRef createFromParcel(Parcel source) {
            return new UserPermissionListRef(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserPermissionListRef[] newArray(int size) {
            return new UserPermissionListRef[size];
        }
    };
    private final String href;
    private final String id;

    private UserPermissionListRef(Builder init) {
        this.id = init.id;
        this.href = init.href;
    }

    private UserPermissionListRef(Parcel in) {
        this.id = in.readString();
        this.href = in.readString();
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

    public static class Builder {
        private String href;
        private final String id;

        public Builder(Resource resource) {
            this.id = resource.getRef().getId();
            this.href = resource.getRef().getHref();
        }

        public Builder(String id) {
            this.id = id;
        }

        public Builder href(String href) {
            this.href = href;
            return this;
        }

        public UserPermissionListRef build() {
            return new UserPermissionListRef(this);
        }
    }
}
