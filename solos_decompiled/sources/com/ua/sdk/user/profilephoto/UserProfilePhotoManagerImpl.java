package com.ua.sdk.user.profilephoto;

import android.content.SharedPreferences;
import android.net.Uri;
import com.ua.sdk.EntityRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.SaveCallback;
import com.ua.sdk.UaException;
import com.ua.sdk.UaLog;
import com.ua.sdk.cache.Cache;
import com.ua.sdk.cache.CacheSettings;
import com.ua.sdk.cache.DiskCache;
import com.ua.sdk.concurrent.SaveRequest;
import com.ua.sdk.internal.AbstractManager;
import com.ua.sdk.internal.EntityService;
import com.ua.sdk.internal.MediaService;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.internal.net.v7.UrlBuilderImpl;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/* JADX INFO: loaded from: classes65.dex */
public class UserProfilePhotoManagerImpl extends AbstractManager<UserProfilePhoto> implements UserProfilePhotoManager {
    protected static final String PREF_CURRENT_PICTURE_PROFILE_LAST_SAVED = "mmdk_user_last_saved";
    private final MediaService<UserProfilePhoto> mediaService;
    private final SharedPreferences sharedPreferences;

    public UserProfilePhotoManagerImpl(MediaService<UserProfilePhoto> mediaService, CacheSettings cacheSettings, Cache memCache, DiskCache<UserProfilePhoto> diskCache, EntityService<UserProfilePhoto> service, ExecutorService executor, SharedPreferences sharedPreferences) {
        super(cacheSettings, memCache, diskCache, service, executor);
        this.mediaService = (MediaService) Precondition.isNotNull(mediaService, "mediaService");
        this.sharedPreferences = (SharedPreferences) Precondition.isNotNull(sharedPreferences);
    }

    @Override // com.ua.sdk.user.profilephoto.UserProfilePhotoManager
    public UserProfilePhoto fetchCurrentProfilePhoto(EntityRef<UserProfilePhoto> ref) throws UaException {
        if (ref == null) {
            throw new UaException("ref can't be null");
        }
        String timestamp = this.sharedPreferences.getString(PREF_CURRENT_PICTURE_PROFILE_LAST_SAVED, "");
        UserProfilePhotoImpl userProfilePhoto = new UserProfilePhotoImpl();
        userProfilePhoto.setRef(ref);
        userProfilePhoto.setLargeImageURL(String.format(Locale.US, UrlBuilderImpl.GET_USER_PROFILE_PICTURE_DIRECT_URL, ref.getId(), "Large", timestamp));
        userProfilePhoto.setMediumImageURL(String.format(Locale.US, UrlBuilderImpl.GET_USER_PROFILE_PICTURE_DIRECT_URL, ref.getId(), "Medium", timestamp));
        userProfilePhoto.setSmallImageURL(String.format(Locale.US, UrlBuilderImpl.GET_USER_PROFILE_PICTURE_DIRECT_URL, ref.getId(), "Small", timestamp));
        return userProfilePhoto;
    }

    @Override // com.ua.sdk.user.profilephoto.UserProfilePhotoManager
    public Request fetchCurrentProfilePhoto(EntityRef<UserProfilePhoto> ref, FetchCallback<UserProfilePhoto> callback) {
        return fetch(ref, callback);
    }

    @Override // com.ua.sdk.user.profilephoto.UserProfilePhotoManager
    public UserProfilePhoto updateUserProfilePhoto(Uri image, UserProfilePhoto entity) throws UaException {
        if (image == null) {
            throw new UaException("Uri image cannot be NULL!");
        }
        if (entity == null) {
            throw new UaException("User profile picture entity cannot be NULL!");
        }
        return (UserProfilePhoto) this.mediaService.uploadUserProfileImage(image, entity);
    }

    @Override // com.ua.sdk.user.profilephoto.UserProfilePhotoManager
    public Request updateUserProfilePhoto(final Uri image, final UserProfilePhoto entity, SaveCallback<UserProfilePhoto> callback) {
        final SaveRequest<UserProfilePhoto> request = new SaveRequest<>(callback);
        Future<?> future = this.executor.submit(new Runnable() { // from class: com.ua.sdk.user.profilephoto.UserProfilePhotoManagerImpl.1
            @Override // java.lang.Runnable
            public void run() {
                UaException error = null;
                UserProfilePhoto savedUserProfilePhoto = null;
                try {
                    savedUserProfilePhoto = UserProfilePhotoManagerImpl.this.updateUserProfilePhoto(image, entity);
                } catch (UaException e) {
                    UaLog.error("Failed to save user profile picture!", (Throwable) e);
                    error = e;
                }
                request.done(savedUserProfilePhoto, error);
            }
        });
        request.setFuture(future);
        return request;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.internal.AbstractManager
    public UserProfilePhoto onPostServiceSave(UserProfilePhoto entity) throws UaException {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(PREF_CURRENT_PICTURE_PROFILE_LAST_SAVED, String.valueOf(System.currentTimeMillis())).apply();
        return (UserProfilePhoto) super.onPostServiceSave(entity);
    }
}
