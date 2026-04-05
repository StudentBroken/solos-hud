package com.ua.sdk.activitystory;

import com.ua.sdk.EntityRef;
import com.ua.sdk.ImageUrl;
import com.ua.sdk.friendship.FriendshipStatus;
import com.ua.sdk.user.Gender;
import com.ua.sdk.user.User;

/* JADX INFO: loaded from: classes65.dex */
public interface ActivityStoryUserActor extends ActivityStoryActor {
    String getFirstName();

    FriendshipStatus getFriendshipStatus();

    Gender getGender();

    String getLastName();

    ImageUrl getProfilePhoto();

    String getTitle();

    EntityRef<User> getUserRef();

    Boolean isMvp();
}
