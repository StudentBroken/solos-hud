package com.ua.sdk.friendship;

import android.os.Parcelable;
import com.ua.sdk.Entity;
import com.ua.sdk.EntityRef;
import com.ua.sdk.user.User;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public interface Friendship extends Entity, Parcelable {
    Date getCreatedDateTime();

    FriendshipStatus getFriendshipStatus();

    EntityRef<User> getFromUserEntityRef();

    String getMessage();

    EntityRef<User> getToUserEntityRef();
}
