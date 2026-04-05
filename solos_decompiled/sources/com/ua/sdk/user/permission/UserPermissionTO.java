package com.ua.sdk.user.permission;

import android.os.Parcel;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.Reference;
import com.ua.sdk.Resource;

/* JADX INFO: loaded from: classes65.dex */
public class UserPermissionTO implements Resource {

    @SerializedName("permission")
    String permission;

    @SerializedName("resource")
    String resource;

    public static UserPermissionTO toTransferObject(UserPermissionImpl userPermission) {
        UserPermissionTO to = new UserPermissionTO();
        to.resource = userPermission.getResource();
        to.permission = userPermission.getPermission();
        return to;
    }

    public static UserPermission fromTransferObject(UserPermissionTO to) {
        UserPermissionImpl answer = new UserPermissionImpl();
        answer.setResource(to.resource);
        answer.setPermission(to.permission);
        return answer;
    }

    @Override // com.ua.sdk.Resource
    public Reference getRef() {
        return null;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
    }
}
