package com.ua.sdk.gear.user;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.internal.BaseReferenceBuilder;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.user.User;

/* JADX INFO: loaded from: classes65.dex */
public class UserGearListRef implements EntityListRef<UserGear> {
    public static final Parcelable.Creator<UserGearListRef> CREATOR = new Parcelable.Creator<UserGearListRef>() { // from class: com.ua.sdk.gear.user.UserGearListRef.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserGearListRef createFromParcel(Parcel source) {
            return new UserGearListRef(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserGearListRef[] newArray(int size) {
            return new UserGearListRef[size];
        }
    };
    private String href;

    private UserGearListRef(Builder init) {
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
        private EntityRef<User> user;

        private Builder() {
            super("/api/0.1/usergear/");
        }

        public Builder setUser(EntityRef<User> user) {
            this.user = user;
            setParam("user_id", user.getId());
            return this;
        }

        public UserGearListRef build() {
            Precondition.isNotNull(this.user);
            return new UserGearListRef(this);
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

    private UserGearListRef(Parcel in) {
        this.href = in.readString();
    }
}
