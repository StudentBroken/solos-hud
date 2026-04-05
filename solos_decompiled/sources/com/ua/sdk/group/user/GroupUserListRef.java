package com.ua.sdk.group.user;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.internal.BaseReferenceBuilder;

/* JADX INFO: loaded from: classes65.dex */
public class GroupUserListRef implements EntityListRef<GroupUser> {
    public static Parcelable.Creator<GroupUserListRef> CREATOR = new Parcelable.Creator<GroupUserListRef>() { // from class: com.ua.sdk.group.user.GroupUserListRef.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GroupUserListRef createFromParcel(Parcel source) {
            return new GroupUserListRef(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GroupUserListRef[] newArray(int size) {
            return new GroupUserListRef[size];
        }
    };
    private final String href;

    public enum Type {
        GROUP,
        USER
    }

    private GroupUserListRef(Parcel in) {
        this.href = in.readString();
    }

    private GroupUserListRef(Builder init) {
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

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.href);
    }

    public static class Builder extends BaseReferenceBuilder {
        private static final String GROUP_ID_KEY = "group_id";
        private static final String USER_ID_KEY = "user_id";
        private String id;
        private Type type;

        public Builder() {
            super("/v7.0/group_user/");
        }

        public Builder setType(Type type) {
            this.type = type;
            return this;
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public GroupUserListRef build() {
            if (this.type == null) {
                throw new IllegalArgumentException("Builder type must be initialized!");
            }
            if (this.id == null) {
                throw new IllegalArgumentException("ID must be initialized!");
            }
            if (this.type == Type.GROUP) {
                setParam("group_id", this.id);
            } else {
                setParam("user_id", this.id);
            }
            return new GroupUserListRef(this);
        }
    }
}
