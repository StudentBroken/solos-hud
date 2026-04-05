package com.ua.sdk.friendship;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.EntityRef;
import com.ua.sdk.internal.BaseReferenceBuilder;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.internal.net.v7.UrlBuilderImpl;
import com.ua.sdk.user.User;

/* JADX INFO: loaded from: classes65.dex */
public class FriendshipRef implements EntityRef<Friendship> {
    public static Parcelable.Creator<FriendshipRef> CREATOR = new Parcelable.Creator<FriendshipRef>() { // from class: com.ua.sdk.friendship.FriendshipRef.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FriendshipRef createFromParcel(Parcel source) {
            return new FriendshipRef(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FriendshipRef[] newArray(int size) {
            return new FriendshipRef[size];
        }
    };
    private final String fromUserId;
    private final String href;
    private final String id;
    private final String toUserId;

    private FriendshipRef(Builder init) {
        this.href = init.href;
        this.id = init.id;
        this.toUserId = init.toUserId;
        this.fromUserId = init.fromUserId;
    }

    @Override // com.ua.sdk.Reference
    public String getId() {
        return this.id;
    }

    @Override // com.ua.sdk.Reference
    public String getHref() {
        return this.href;
    }

    public String getToUserId() {
        return this.toUserId;
    }

    public String getFromUserId() {
        return this.fromUserId;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static final class Builder extends BaseReferenceBuilder {
        String fromUserId;
        String href;
        String id;
        String toUserId;

        private Builder() {
            super(UrlBuilderImpl.CHANGE_FRIENDSHIP_URL);
        }

        public Builder setHref(String href) {
            this.href = href;
            return this;
        }

        public Builder setToUser(EntityRef<User> toUser) {
            this.toUserId = toUser.getId();
            return this;
        }

        public Builder setFromUser(EntityRef<User> fromUser) {
            this.fromUserId = fromUser.getId();
            return this;
        }

        public Builder setToUser(String id) {
            this.toUserId = id;
            return this;
        }

        public Builder setFromUser(String id) {
            this.fromUserId = id;
            return this;
        }

        public FriendshipRef build() {
            Precondition.isNotNull(this.fromUserId, "fromUser must be defined");
            Precondition.isNotNull(this.toUserId, "toUser must be defined");
            this.id = this.fromUserId + '_' + this.toUserId;
            setHref(String.format(UrlBuilderImpl.CHANGE_FRIENDSHIP_URL, this.id));
            return new FriendshipRef(this);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.href);
        dest.writeString(this.toUserId);
        dest.writeString(this.fromUserId);
    }

    private FriendshipRef(Parcel in) {
        this.id = in.readString();
        this.href = in.readString();
        this.toUserId = in.readString();
        this.fromUserId = in.readString();
    }
}
