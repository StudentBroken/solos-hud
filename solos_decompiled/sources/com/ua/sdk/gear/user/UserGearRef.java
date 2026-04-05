package com.ua.sdk.gear.user;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.internal.BaseReferenceBuilder;
import com.ua.sdk.internal.LinkEntityRef;

/* JADX INFO: loaded from: classes65.dex */
public class UserGearRef extends LinkEntityRef<UserGear> {
    public static final Parcelable.Creator<UserGearRef> CREATOR = new Parcelable.Creator<UserGearRef>() { // from class: com.ua.sdk.gear.user.UserGearRef.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserGearRef createFromParcel(Parcel source) {
            return new UserGearRef(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserGearRef[] newArray(int size) {
            return new UserGearRef[size];
        }
    };

    public UserGearRef(String href) {
        super(href);
    }

    public UserGearRef(String id, String href) {
        super(id, href);
    }

    public UserGearRef(String id, long localId, String href) {
        super(id, localId, href);
    }

    private UserGearRef(Builder init) {
        super(init.id, init.getHref());
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder extends BaseReferenceBuilder {
        private String id;

        private Builder() {
            super("/api/0.1/usergear/{id}/");
        }

        public Builder setGearId(String id) {
            this.id = id;
            setParam("id", id);
            return this;
        }

        public UserGearRef build() {
            if (this.id == null) {
                throw new IllegalArgumentException("An id must be specified in the builder.");
            }
            return new UserGearRef(this);
        }
    }

    @Override // com.ua.sdk.internal.LinkEntityRef, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.LinkEntityRef, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    private UserGearRef(Parcel in) {
        super(in);
    }
}
