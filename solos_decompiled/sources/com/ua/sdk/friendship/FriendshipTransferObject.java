package com.ua.sdk.friendship;

import com.google.gson.annotations.SerializedName;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.Precondition;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes65.dex */
public class FriendshipTransferObject extends ApiTransferObject {

    @SerializedName("created_datetime")
    Date dateTimeCreated;

    @SerializedName(FriendshipPageTransferObject.KEY_FRIENDSHIPS)
    List<Friendship> friendships;

    @SerializedName("message")
    String message;

    @SerializedName("status")
    String status;

    public static FriendshipTransferObject fromFriendship(Friendship friendship) {
        Precondition.isNotNull(friendship, "friendship");
        Precondition.isNotNull(friendship.getFriendshipStatus());
        FriendshipTransferObject friendshipTransferObject = new FriendshipTransferObject();
        friendshipTransferObject.status = friendship.getFriendshipStatus().getValue();
        friendshipTransferObject.dateTimeCreated = friendship.getCreatedDateTime();
        friendshipTransferObject.message = friendship.getMessage();
        if (friendship instanceof FriendshipImpl) {
            Map<String, ArrayList<Link>> linkMap = ((FriendshipImpl) friendship).getLinkMap();
            if (linkMap != null) {
                friendshipTransferObject.setLinkMap(linkMap);
            }
            if (!((FriendshipImpl) friendship).getFriendships().isEmpty()) {
                friendshipTransferObject.friendships = ((FriendshipImpl) friendship).getFriendships();
            }
        }
        return friendshipTransferObject;
    }

    public static FriendshipImpl toFriendship(FriendshipTransferObject to) {
        Precondition.isNotNull(to);
        FriendshipImpl friendship = new FriendshipImpl();
        friendship.setFriendshipStatus(FriendshipStatus.getStatusFromString(to.status));
        friendship.setCreatedDateTime(to.dateTimeCreated);
        friendship.setMessage(to.message);
        for (String key : to.getLinkKeys()) {
            friendship.setLinksForRelation(key, to.getLinks(key));
        }
        return friendship;
    }
}
