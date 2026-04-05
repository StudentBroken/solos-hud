package com.ua.sdk.page.follow;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.internal.BaseReferenceBuilder;
import com.ua.sdk.user.User;

/* JADX INFO: loaded from: classes65.dex */
public class PageFollowListRef implements EntityListRef<PageFollow> {
    public static Parcelable.Creator<PageFollowListRef> CREATOR = new Parcelable.Creator<PageFollowListRef>() { // from class: com.ua.sdk.page.follow.PageFollowListRef.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PageFollowListRef createFromParcel(Parcel source) {
            return new PageFollowListRef(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PageFollowListRef[] newArray(int size) {
            return new PageFollowListRef[size];
        }
    };
    private String href;

    private PageFollowListRef(Builder init) {
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

    public static class Builder extends BaseReferenceBuilder {
        private String id;
        private EntityRef<User> user;

        private Builder() {
            super("/v7.0/page_follow/");
        }

        public Builder setUserId(String id) {
            this.id = id;
            return this;
        }

        public Builder setUser(EntityRef<User> user) {
            this.user = user;
            return this;
        }

        public PageFollowListRef build() {
            if (this.id == null && this.user == null) {
                throw new NullPointerException("user id is null.");
            }
            if (this.user == null) {
                setParam("user_id", this.id);
            } else if (this.id == null) {
                setParam("user_id", this.user.getId());
            } else {
                throw new IllegalStateException("cannot specify a user and an id");
            }
            return new PageFollowListRef(this);
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

    private PageFollowListRef(Parcel in) {
        this.href = in.readString();
    }
}
