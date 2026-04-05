package com.ua.sdk.suggestedfriends;

import android.os.Parcelable;
import com.ua.sdk.Entity;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.ImageUrl;
import com.ua.sdk.user.User;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public interface SuggestedFriends extends Entity, Parcelable {
    Integer getMutualFriendCount();

    EntityListRef<User> getMutualFriendsRef();

    List<SuggestedFriendsReason> getReasons();

    String getSuggestFriendDisplayName();

    EntityRef<User> getSuggestedFriendRef();

    ImageUrl getSuggestedFriendsProfilePicture();
}
