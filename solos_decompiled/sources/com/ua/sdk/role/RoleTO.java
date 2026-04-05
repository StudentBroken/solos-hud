package com.ua.sdk.role;

import com.facebook.internal.NativeProtocol;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.role.Role;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class RoleTO extends ApiTransferObject {

    @SerializedName("description")
    String description;

    @SerializedName("name")
    Role.Type name;

    @SerializedName(NativeProtocol.RESULT_ARGS_PERMISSIONS)
    List<Role.Permission> permissions;

    public static RoleTO toTransferObject(RoleImpl role) {
        RoleTO to = new RoleTO();
        to.permissions = role.getPermissions();
        to.description = role.getDescription();
        to.name = role.getName();
        return to;
    }

    public static RoleImpl fromTransferObject(RoleTO to) {
        RoleImpl answer = new RoleImpl();
        answer.setDescription(to.description);
        answer.setPermissions(to.permissions);
        answer.setName(to.name);
        return answer;
    }
}
