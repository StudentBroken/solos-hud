package com.ua.sdk.remoteconnection;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.EntityRef;
import com.ua.sdk.internal.BaseReferenceBuilder;

/* JADX INFO: loaded from: classes65.dex */
public class RemoteConnectionRef implements EntityRef<RemoteConnection> {
    public static final Parcelable.Creator<RemoteConnectionRef> CREATOR = new Parcelable.Creator<RemoteConnectionRef>() { // from class: com.ua.sdk.remoteconnection.RemoteConnectionRef.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RemoteConnectionRef createFromParcel(Parcel source) {
            return new RemoteConnectionRef(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RemoteConnectionRef[] newArray(int size) {
            return new RemoteConnectionRef[size];
        }
    };
    private final String href;
    private final String id;

    private RemoteConnectionRef(Builder init) {
        this.id = init.id;
        this.href = init.getHref();
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder extends BaseReferenceBuilder {
        private String id;

        private Builder() {
            super("/v7.0/remoteconnection/{id}/");
        }

        public Builder setId(String id) {
            this.id = id;
            setParam("id", id);
            return this;
        }

        public RemoteConnectionRef build() {
            RemoteConnectionRef remoteConnectionRef;
            synchronized (RemoteConnectionRef.class) {
                remoteConnectionRef = new RemoteConnectionRef(this);
            }
            return remoteConnectionRef;
        }
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

    private RemoteConnectionRef(Parcel in) {
        this.id = in.readString();
        this.href = in.readString();
    }
}
