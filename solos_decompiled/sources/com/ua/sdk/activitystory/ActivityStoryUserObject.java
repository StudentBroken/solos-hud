package com.ua.sdk.activitystory;

import com.ua.sdk.EntityRef;
import com.ua.sdk.activitystory.ActivityStoryObject;
import com.ua.sdk.friendship.FriendshipStatus;
import com.ua.sdk.location.Location;
import com.ua.sdk.privacy.Privacy;
import com.ua.sdk.user.Gender;
import com.ua.sdk.user.User;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public interface ActivityStoryUserObject extends ActivityStoryObject {
    String getFirstName();

    FriendshipStatus getFriendshipStatus();

    Gender getGender();

    String getId();

    Date getJoinedDate();

    String getLastName();

    Location getLocation();

    Privacy getPrivacy();

    String getTitle();

    @Override // com.ua.sdk.activitystory.ActivityStoryObject
    ActivityStoryObject.Type getType();

    EntityRef<User> getUserRef();

    Boolean isMvp();
}
