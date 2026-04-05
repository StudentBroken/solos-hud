package com.ua.sdk.remoteconnection;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.EntityRef;
import com.ua.sdk.internal.BaseReferenceBuilder;

/* JADX INFO: loaded from: classes65.dex */
public class RemoteConnectionTypeRef implements EntityRef<RemoteConnectionType>, Parcelable {
    public static final Parcelable.Creator<RemoteConnectionTypeRef> CREATOR = new Parcelable.Creator<RemoteConnectionTypeRef>() { // from class: com.ua.sdk.remoteconnection.RemoteConnectionTypeRef.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RemoteConnectionTypeRef createFromParcel(Parcel source) {
            return new RemoteConnectionTypeRef(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RemoteConnectionTypeRef[] newArray(int size) {
            return new RemoteConnectionTypeRef[size];
        }
    };
    private final String href;
    private final String id;
    private final String recorderTypeKey;

    private RemoteConnectionTypeRef(Builder init) {
        this.id = init.id;
        this.href = init.getHref();
        this.recorderTypeKey = init.recorderTypeKey;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder extends BaseReferenceBuilder {
        String id;
        String recorderTypeKey;

        private Builder() {
            super("/v7.0/remoteconnectiontype/{id}/");
        }

        public Builder setId(String id) {
            this.id = id;
            setParam("id", id);
            return this;
        }

        public Builder setRecorderTypeKey(String recorderTypeKey) {
            this.recorderTypeKey = recorderTypeKey;
            return this;
        }

        public RemoteConnectionTypeRef build() {
            RemoteConnectionTypeRef remoteConnectionTypeRef;
            synchronized (RemoteConnectionTypeRef.class) {
                remoteConnectionTypeRef = new RemoteConnectionTypeRef(this);
            }
            return remoteConnectionTypeRef;
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

    public String getRecorderTypeKey() {
        return this.recorderTypeKey;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.href);
        dest.writeString(this.recorderTypeKey);
    }

    private RemoteConnectionTypeRef(Parcel in) {
        this.id = in.readString();
        this.href = in.readString();
        this.recorderTypeKey = in.readString();
    }
}
