package com.ua.sdk.suggestedfriends;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.internal.BaseReferenceBuilder;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.internal.net.v7.UrlBuilderImpl;
import com.ua.sdk.user.User;

/* JADX INFO: loaded from: classes65.dex */
public class SuggestedFriendsListRef implements EntityListRef<SuggestedFriends> {
    public static Parcelable.Creator<SuggestedFriendsListRef> CREATOR = new Parcelable.Creator<SuggestedFriendsListRef>() { // from class: com.ua.sdk.suggestedfriends.SuggestedFriendsListRef.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SuggestedFriendsListRef createFromParcel(Parcel source) {
            return new SuggestedFriendsListRef(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SuggestedFriendsListRef[] newArray(int size) {
            return new SuggestedFriendsListRef[size];
        }
    };
    private final String href;

    private SuggestedFriendsListRef(Builder init) {
        this.href = init.getHref();
    }

    @Override // com.ua.sdk.Reference
    public String getHref() {
        return this.href;
    }

    @Override // com.ua.sdk.Reference
    public String getId() {
        return null;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder extends BaseReferenceBuilder {
        EntityRef<User> user;

        private Builder() {
            super(UrlBuilderImpl.GET_SUGGESTED_FRIENDS_URL);
        }

        public Builder setUser(EntityRef<User> user) {
            this.user = user;
            setParam("user", user.getId());
            return this;
        }

        public SuggestedFriendsListRef build() {
            Precondition.isNotNull(this.user);
            return new SuggestedFriendsListRef(this);
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

    private SuggestedFriendsListRef(Parcel in) {
        this.href = in.readString();
    }
}
