package com.ua.sdk.suggestedfriends;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.internal.AbstractEntityList;

/* JADX INFO: loaded from: classes65.dex */
public class SuggestedFriendsListImpl extends AbstractEntityList<SuggestedFriends> {
    public static Parcelable.Creator<SuggestedFriendsListImpl> CREATOR = new Parcelable.Creator<SuggestedFriendsListImpl>() { // from class: com.ua.sdk.suggestedfriends.SuggestedFriendsListImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SuggestedFriendsListImpl createFromParcel(Parcel source) {
            return new SuggestedFriendsListImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SuggestedFriendsListImpl[] newArray(int size) {
            return new SuggestedFriendsListImpl[size];
        }
    };
    private static final String LIST_KEY = "suggestions";

    @Override // com.ua.sdk.internal.AbstractEntityList
    protected String getListKey() {
        return "suggestions";
    }

    public SuggestedFriendsListImpl() {
    }

    private SuggestedFriendsListImpl(Parcel in) {
        super(in);
    }
}
