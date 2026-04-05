package com.ua.sdk.remoteconnection;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.internal.AbstractEntityList;

/* JADX INFO: loaded from: classes65.dex */
public class RemoteConnectionPageImpl extends AbstractEntityList<RemoteConnection> {
    public static Parcelable.Creator<RemoteConnectionPageImpl> CREATOR = new Parcelable.Creator<RemoteConnectionPageImpl>() { // from class: com.ua.sdk.remoteconnection.RemoteConnectionPageImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RemoteConnectionPageImpl createFromParcel(Parcel source) {
            return new RemoteConnectionPageImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RemoteConnectionPageImpl[] newArray(int size) {
            return new RemoteConnectionPageImpl[size];
        }
    };
    private static final String LIST_KEY = "remote_connections";

    @Override // com.ua.sdk.internal.AbstractEntityList
    protected String getListKey() {
        return LIST_KEY;
    }

    public RemoteConnectionPageImpl() {
    }

    private RemoteConnectionPageImpl(Parcel in) {
        super(in);
    }
}
