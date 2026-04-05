package com.ua.sdk.friendship;

import com.google.gson.annotations.SerializedName;
import com.ua.sdk.cache.EntityDatabase;
import com.ua.sdk.internal.ApiTransferObject;
import java.util.ArrayList;
import java.util.Map;

/* JADX INFO: loaded from: classes65.dex */
public class FriendshipPageTransferObject extends ApiTransferObject {
    public static final String KEY_FRIENDSHIPS = "friendships";

    @SerializedName("_embedded")
    public Map<String, ArrayList<FriendshipTransferObject>> friendships;

    @SerializedName(EntityDatabase.LIST.COLS.TOTAL_COUNT)
    public Integer totalFriendsCount;

    private ArrayList<FriendshipTransferObject> getFriendshipList() {
        if (this.friendships == null) {
            return null;
        }
        return this.friendships.get(KEY_FRIENDSHIPS);
    }

    public static FriendshipPageTransferObject toTransferObject(FriendshipListImpl friendshipPage) {
        if (friendshipPage == null) {
            return null;
        }
        FriendshipPageTransferObject friendshipPageTransferObject = new FriendshipPageTransferObject();
        for (Friendship friendship : friendshipPage.getElements()) {
            friendshipPageTransferObject.friendships.get(KEY_FRIENDSHIPS).add(FriendshipTransferObject.fromFriendship((FriendshipImpl) friendship));
        }
        friendshipPageTransferObject.setLinkMap(friendshipPage.getLinkMap());
        friendshipPageTransferObject.totalFriendsCount = Integer.valueOf(friendshipPage.getTotalCount());
        return friendshipPageTransferObject;
    }

    public static FriendshipListImpl fromTransferObject(FriendshipPageTransferObject to) {
        FriendshipListImpl page = new FriendshipListImpl();
        ArrayList<FriendshipTransferObject> friendshipTransferObjects = to.getFriendshipList();
        for (FriendshipTransferObject friendshipTransferObject : friendshipTransferObjects) {
            FriendshipImpl friendship = FriendshipTransferObject.toFriendship(friendshipTransferObject);
            page.add(friendship);
        }
        page.setLinkMap(to.getLinkMap());
        page.setTotalCount(to.totalFriendsCount.intValue());
        return page;
    }
}
