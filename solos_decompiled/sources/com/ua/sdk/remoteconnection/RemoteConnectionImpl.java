package com.ua.sdk.remoteconnection;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.EntityRef;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.LinkEntityRef;
import com.ua.sdk.internal.Precondition;
import java.util.Date;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class RemoteConnectionImpl extends ApiTransferObject implements RemoteConnection, Parcelable {
    public static Parcelable.Creator<RemoteConnectionImpl> CREATOR = new Parcelable.Creator<RemoteConnectionImpl>() { // from class: com.ua.sdk.remoteconnection.RemoteConnectionImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RemoteConnectionImpl createFromParcel(Parcel source) {
            return new RemoteConnectionImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RemoteConnectionImpl[] newArray(int size) {
            return new RemoteConnectionImpl[size];
        }
    };
    private Date mCreatedDateTime;
    private Date mLastSyncTime;
    private String mType;

    protected RemoteConnectionImpl() {
    }

    protected RemoteConnectionImpl(RemoteConnection remoteConnection) {
        Precondition.isNotNull(remoteConnection, "remoteConnection");
        this.mCreatedDateTime = remoteConnection.getCreatedDateTime();
        this.mLastSyncTime = remoteConnection.getLastSyncTime();
        this.mType = remoteConnection.getType();
        if (remoteConnection instanceof RemoteConnectionImpl) {
            copyLinkMap(((RemoteConnectionImpl) remoteConnection).getLinkMap());
        }
    }

    @Override // com.ua.sdk.Resource
    public EntityRef<RemoteConnection> getRef() {
        List<Link> selfLinks = getLinks("self");
        if (selfLinks == null || selfLinks.isEmpty()) {
            return null;
        }
        return new LinkEntityRef(selfLinks.get(0).getId(), selfLinks.get(0).getHref());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnection
    public Date getCreatedDateTime() {
        return this.mCreatedDateTime;
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnection
    public Date getLastSyncTime() {
        return this.mLastSyncTime;
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnection
    public String getType() {
        return this.mType;
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnection
    public void setCreatedDateTime(Date createdDateTime) {
        this.mCreatedDateTime = createdDateTime;
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnection
    public void setLastSyncTime(Date lastSyncTime) {
        this.mLastSyncTime = lastSyncTime;
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnection
    public void setType(String type) {
        this.mType = type;
    }

    @Override // com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(this.mCreatedDateTime.getTime());
        dest.writeLong(this.mLastSyncTime.getTime());
        dest.writeString(this.mType);
    }

    private RemoteConnectionImpl(Parcel in) {
        super(in);
        this.mCreatedDateTime = new Date(in.readLong());
        this.mLastSyncTime = new Date(in.readLong());
        this.mType = in.readString();
    }
}
