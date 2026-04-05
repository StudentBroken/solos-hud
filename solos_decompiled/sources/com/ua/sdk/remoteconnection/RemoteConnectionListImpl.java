package com.ua.sdk.remoteconnection;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.internal.AbstractEntityList;

/* JADX INFO: loaded from: classes65.dex */
public class RemoteConnectionListImpl extends AbstractEntityList<RemoteConnection> {
    public static Parcelable.Creator<RemoteConnectionListImpl> CREATOR = new Parcelable.Creator<RemoteConnectionListImpl>() { // from class: com.ua.sdk.remoteconnection.RemoteConnectionListImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RemoteConnectionListImpl createFromParcel(Parcel source) {
            return new RemoteConnectionListImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RemoteConnectionListImpl[] newArray(int size) {
            return new RemoteConnectionListImpl[size];
        }
    };
    public static final String LIST_KEY = "remoteConnectionPages";

    @Override // com.ua.sdk.internal.AbstractEntityList
    protected String getListKey() {
        return LIST_KEY;
    }

    public RemoteConnectionListImpl() {
    }

    private RemoteConnectionListImpl(Parcel in) {
        super(in);
    }
}
