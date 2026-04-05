package com.ua.sdk.friendship;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.internal.BaseReferenceBuilder;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.user.User;

/* JADX INFO: loaded from: classes65.dex */
public class FriendshipListRef implements EntityListRef<Friendship> {
    public static Parcelable.Creator<FriendshipListRef> CREATOR = new Parcelable.Creator<FriendshipListRef>() { // from class: com.ua.sdk.friendship.FriendshipListRef.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FriendshipListRef createFromParcel(Parcel source) {
            return new FriendshipListRef(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FriendshipListRef[] newArray(int size) {
            return new FriendshipListRef[size];
        }
    };
    private final String href;

    private FriendshipListRef(Builder init) {
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
        EntityRef<User> fromUser;
        String href;
        FriendshipStatus status;
        EntityRef<User> toUser;

        private Builder() {
            super("/v7.0/friendship/");
        }

        public Builder setHref(String href) {
            this.href = href;
            return this;
        }

        public Builder setToUser(EntityRef<User> toUser) {
            this.toUser = toUser;
            return this;
        }

        public Builder setFromUser(EntityRef<User> fromUser) {
            this.fromUser = fromUser;
            return this;
        }

        public Builder setFriendshipStatus(FriendshipStatus status) {
            this.status = status;
            return this;
        }

        public FriendshipListRef build() {
            if (this.status == FriendshipStatus.NONE) {
                Precondition.check(false, "none is not a valid status type.");
            }
            if (this.fromUser == null && this.toUser == null) {
                Precondition.check(false, "a from_user and/or a to_user filter must be defined.");
            }
            if (this.fromUser != null) {
                setParam(FriendshipImpl.ARG_FROM_USER, this.fromUser.getId());
            }
            if (this.toUser != null) {
                setParam(FriendshipImpl.ARG_TO_USER, this.toUser.getId());
            }
            if (this.status != null) {
                if (this.fromUser == null || this.toUser == null) {
                    setParam("status", this.status.getValue());
                } else {
                    Precondition.check(false, "a status cannot be applied when a to_user and from_user filter are applied");
                }
            }
            return new FriendshipListRef(this);
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

    private FriendshipListRef(Parcel in) {
        this.href = in.readString();
    }
}
