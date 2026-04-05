package com.ua.sdk.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.EntityRef;

/* JADX INFO: loaded from: classes65.dex */
public class LinkEntityRef<T> implements EntityRef<T> {
    public static final Parcelable.Creator<LinkEntityRef> CREATOR = new Parcelable.Creator<LinkEntityRef>() { // from class: com.ua.sdk.internal.LinkEntityRef.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LinkEntityRef createFromParcel(Parcel in) {
            return new LinkEntityRef(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LinkEntityRef[] newArray(int size) {
            return new LinkEntityRef[size];
        }
    };
    protected final String href;
    protected final String id;
    protected final long localId;
    protected final int options;

    public LinkEntityRef(String href) {
        this(null, href);
    }

    public LinkEntityRef(String id, String href) {
        this.id = id;
        this.href = href;
        this.localId = -1L;
        this.options = 0;
    }

    public LinkEntityRef(String id, long localId, String href) {
        this.id = id;
        this.href = href;
        this.localId = localId;
        this.options = 0;
    }

    public LinkEntityRef(String id, long localId, String href, int options) {
        this.id = id;
        this.href = href;
        this.localId = localId;
        this.options = options;
    }

    @Override // com.ua.sdk.Reference
    public String getId() {
        return this.id;
    }

    @Override // com.ua.sdk.Reference
    public String getHref() {
        return this.href;
    }

    public long getLocalId() {
        return this.localId;
    }

    public int getOptions() {
        return this.options;
    }

    public boolean checkCache() {
        return true;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeLong(this.localId);
        dest.writeString(this.href);
        dest.writeInt(this.options);
    }

    protected LinkEntityRef(Parcel in) {
        this.id = in.readString();
        this.localId = in.readLong();
        this.href = in.readString();
        this.options = in.readInt();
    }
}
