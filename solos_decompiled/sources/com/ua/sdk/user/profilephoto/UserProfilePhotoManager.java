package com.ua.sdk.user.profilephoto;

import android.net.Uri;
import com.ua.sdk.EntityRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.SaveCallback;
import com.ua.sdk.UaException;

/* JADX INFO: loaded from: classes65.dex */
public interface UserProfilePhotoManager {
    Request fetchCurrentProfilePhoto(EntityRef<UserProfilePhoto> entityRef, FetchCallback<UserProfilePhoto> fetchCallback);

    UserProfilePhoto fetchCurrentProfilePhoto(EntityRef<UserProfilePhoto> entityRef) throws UaException;

    Request updateUserProfilePhoto(Uri uri, UserProfilePhoto userProfilePhoto, SaveCallback<UserProfilePhoto> saveCallback);

    UserProfilePhoto updateUserProfilePhoto(Uri uri, UserProfilePhoto userProfilePhoto) throws UaException;
}
