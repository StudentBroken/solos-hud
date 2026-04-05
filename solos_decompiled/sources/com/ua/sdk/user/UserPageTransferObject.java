package com.ua.sdk.user;

import com.google.gson.annotations.SerializedName;
import com.ua.sdk.UaException;
import com.ua.sdk.cache.EntityDatabase;
import com.ua.sdk.internal.ApiTransferObject;
import java.util.ArrayList;
import java.util.Map;

/* JADX INFO: loaded from: classes65.dex */
public class UserPageTransferObject extends ApiTransferObject {
    public static final String KEY_USERS = "user";

    @SerializedName(EntityDatabase.LIST.COLS.TOTAL_COUNT)
    public Integer totalUserCount;

    @SerializedName("_embedded")
    public Map<String, ArrayList<UserTO>> users;

    private ArrayList<UserTO> getUserList() {
        if (this.users == null) {
            return null;
        }
        return this.users.get("user");
    }

    public static UserListImpl toPage(UserPageTransferObject to) throws UaException {
        UserListImpl list = new UserListImpl();
        ArrayList<UserTO> userPageTransferObjects = to.getUserList();
        for (UserTO userTransferObject : userPageTransferObjects) {
            UserImpl user = UserTO.fromTransferObject(userTransferObject);
            list.add(user);
        }
        list.setLinkMap(to.getLinkMap());
        list.setTotalCount(to.totalUserCount.intValue());
        return list;
    }
}
