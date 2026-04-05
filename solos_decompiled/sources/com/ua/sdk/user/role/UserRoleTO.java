package com.ua.sdk.user.role;

import com.google.gson.annotations.SerializedName;
import com.ua.sdk.internal.ApiTransferObject;

/* JADX INFO: loaded from: classes65.dex */
public class UserRoleTO extends ApiTransferObject {
    private static final String RESOURCE = "resource";
    private static final String ROLE = "role";
    private static final String USER = "user";

    @SerializedName(RESOURCE)
    String resource;

    @SerializedName(ROLE)
    String role;

    @SerializedName("user")
    String user;

    public static UserRoleTO toTransferObject(UserRole userRole) {
        UserRoleTO to = new UserRoleTO();
        to.user = userRole.getUser().getId();
        to.role = userRole.getRole().getId();
        to.resource = userRole.getResource().getHref();
        return to;
    }

    public static UserRole fromTransferObject(UserRoleTO to) {
        UserRoleImpl answer = new UserRoleImpl();
        answer.setUser(to.getLink("user"));
        answer.setRole(to.getLink(ROLE));
        answer.setResource(to.getLink(RESOURCE));
        return answer;
    }
}
