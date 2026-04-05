package com.ua.sdk.user.profilephoto;

import com.ua.sdk.internal.ApiTransferObject;

/* JADX INFO: loaded from: classes65.dex */
public class UserProfilePhotoTransferObject extends ApiTransferObject {
    public static UserProfilePhotoImpl toUserProfilePhotoImpl(UserProfilePhotoTransferObject obj) {
        UserProfilePhotoImpl profilePhoto = new UserProfilePhotoImpl();
        for (String key : obj.getLinkKeys()) {
            profilePhoto.setLinksForRelation(key, obj.getLinks(key));
        }
        return profilePhoto;
    }
}
