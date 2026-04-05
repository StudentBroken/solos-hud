package com.ua.sdk.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.EntityListRef;

/* JADX INFO: loaded from: classes65.dex */
public class LinkListRef<E> implements EntityListRef<E> {
    public static final Parcelable.Creator<LinkListRef> CREATOR = new Parcelable.Creator<LinkListRef>() { // from class: com.ua.sdk.internal.LinkListRef.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LinkListRef createFromParcel(Parcel in) {
            return new LinkListRef(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LinkListRef[] newArray(int size) {
            return new LinkListRef[size];
        }
    };
    private final String href;
    private final long localId;

    public LinkListRef(String href) {
        this.href = href;
        this.localId = -1L;
    }

    public LinkListRef(long localId, String href) {
        this.href = href;
        this.localId = localId;
    }

    @Override // com.ua.sdk.Reference
    public String getHref() {
        return this.href;
    }

    public long getLocalId() {
        return this.localId;
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
        dest.writeLong(this.localId);
        dest.writeString(this.href);
    }

    protected LinkListRef(Parcel in) {
        this.localId = in.readLong();
        this.href = in.readString();
    }

    @Override // com.ua.sdk.Reference
    public String getId() {
        return getHref();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LinkListRef that = (LinkListRef) o;
        if (this.localId != that.localId) {
            return false;
        }
        if (this.href != null) {
            if (this.href.equals(that.href)) {
                return true;
            }
        } else if (that.href == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = this.href != null ? this.href.hashCode() : 0;
        return (result * 31) + ((int) (this.localId ^ (this.localId >>> 32)));
    }
}
