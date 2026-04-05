package com.ua.sdk.friendship;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.internal.AbstractEntityList;

/* JADX INFO: loaded from: classes65.dex */
public class FriendshipListImpl extends AbstractEntityList<Friendship> {
    public static Parcelable.Creator<FriendshipListImpl> CREATOR = new Parcelable.Creator<FriendshipListImpl>() { // from class: com.ua.sdk.friendship.FriendshipListImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FriendshipListImpl createFromParcel(Parcel source) {
            return new FriendshipListImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FriendshipListImpl[] newArray(int size) {
            return new FriendshipListImpl[size];
        }
    };

    public FriendshipListImpl() {
    }

    @Override // com.ua.sdk.internal.AbstractEntityList
    protected String getListKey() {
        return FriendshipPageTransferObject.KEY_FRIENDSHIPS;
    }

    private FriendshipListImpl(Parcel in) {
        super(in);
    }
}
