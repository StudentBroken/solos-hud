package com.ua.sdk.role;

import com.google.gson.annotations.SerializedName;
import com.ua.sdk.cache.EntityDatabase;
import com.ua.sdk.internal.ApiTransferObject;
import java.util.ArrayList;
import java.util.Map;

/* JADX INFO: loaded from: classes65.dex */
public class RolePagedTO extends ApiTransferObject {
    private static final String KEY_ROLES = "roles";

    @SerializedName("_embedded")
    public Map<String, ArrayList<RoleTO>> roles;

    @SerializedName(EntityDatabase.LIST.COLS.TOTAL_COUNT)
    public Integer totalUserCount;

    private ArrayList<RoleTO> getRolesList() {
        if (this.roles == null) {
            return null;
        }
        return this.roles.get(KEY_ROLES);
    }

    public static RoleList toPage(RolePagedTO to) {
        RoleList list = new RoleList();
        ArrayList<RoleTO> roleTOs = to.getRolesList();
        for (RoleTO roleTO : roleTOs) {
            list.add(RoleTO.fromTransferObject(roleTO));
        }
        list.setLinkMap(to.getLinkMap());
        list.setTotalCount(to.totalUserCount.intValue());
        return list;
    }
}
