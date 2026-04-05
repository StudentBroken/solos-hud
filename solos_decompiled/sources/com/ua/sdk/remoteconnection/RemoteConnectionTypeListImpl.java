package com.ua.sdk.remoteconnection;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.internal.AbstractEntityList;

/* JADX INFO: loaded from: classes65.dex */
public class RemoteConnectionTypeListImpl extends AbstractEntityList<RemoteConnectionType> {
    public static Parcelable.Creator<RemoteConnectionTypeListImpl> CREATOR = new Parcelable.Creator<RemoteConnectionTypeListImpl>() { // from class: com.ua.sdk.remoteconnection.RemoteConnectionTypeListImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RemoteConnectionTypeListImpl createFromParcel(Parcel parcel) {
            return new RemoteConnectionTypeListImpl(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RemoteConnectionTypeListImpl[] newArray(int i) {
            return new RemoteConnectionTypeListImpl[i];
        }
    };
    public static final String LIST_KEY = "remoteConnectionTypes";

    @Override // com.ua.sdk.internal.AbstractEntityList
    protected String getListKey() {
        return LIST_KEY;
    }

    public RemoteConnectionTypeListImpl() {
    }

    private RemoteConnectionTypeListImpl(Parcel in) {
        super(in);
    }
}
