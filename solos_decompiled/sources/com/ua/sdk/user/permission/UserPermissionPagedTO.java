package com.ua.sdk.user.permission;

import com.google.gson.annotations.SerializedName;
import com.ua.sdk.cache.EntityDatabase;
import com.ua.sdk.internal.ApiTransferObject;
import java.util.ArrayList;
import java.util.Map;

/* JADX INFO: loaded from: classes65.dex */
public class UserPermissionPagedTO extends ApiTransferObject {
    private static final String KEY_PERMISSIONS = "user_permissions";

    @SerializedName(EntityDatabase.LIST.COLS.TOTAL_COUNT)
    public Integer totalUserCount;

    @SerializedName("_embedded")
    public Map<String, ArrayList<UserPermissionTO>> userPermissions;

    private ArrayList<UserPermissionTO> getUserPermissionList() {
        if (this.userPermissions == null) {
            return null;
        }
        return this.userPermissions.get(KEY_PERMISSIONS);
    }

    public static UserPermissionList toPage(UserPermissionPagedTO to) {
        UserPermissionList list = new UserPermissionList();
        ArrayList<UserPermissionTO> userPermissionTOs = to.getUserPermissionList();
        for (UserPermissionTO userTransferObject : userPermissionTOs) {
            UserPermission userPermission = UserPermissionTO.fromTransferObject(userTransferObject);
            list.add(userPermission);
        }
        list.setLinkMap(to.getLinkMap());
        list.setTotalCount(to.totalUserCount.intValue());
        return list;
    }
}
