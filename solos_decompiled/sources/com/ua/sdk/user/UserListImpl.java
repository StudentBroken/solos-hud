package com.ua.sdk.user;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.Reference;
import com.ua.sdk.internal.AbstractEntityList;

/* JADX INFO: loaded from: classes65.dex */
public class UserListImpl extends AbstractEntityList<User> {
    public static Parcelable.Creator<UserListImpl> CREATOR = new Parcelable.Creator<UserListImpl>() { // from class: com.ua.sdk.user.UserListImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserListImpl createFromParcel(Parcel source) {
            return new UserListImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserListImpl[] newArray(int size) {
            return new UserListImpl[size];
        }
    };

    public UserListImpl() {
    }

    @Override // com.ua.sdk.internal.AbstractEntityList
    protected String getListKey() {
        return "user";
    }

    @Override // com.ua.sdk.internal.AbstractEntityList, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    private UserListImpl(Parcel in) {
        super(in);
    }

    @Override // com.ua.sdk.internal.AbstractEntityList
    public boolean preparePartials(Reference ref) {
        if (ref == null || !ref.getHref().contains("friends_with")) {
            return false;
        }
        for (User user : getAll()) {
            if (user instanceof UserImpl) {
                ((UserImpl) user).setObjectState(UserObjectState.FRIENDS_WITH);
            }
        }
        return true;
    }
}
