package com.ua.sdk.user;

import android.content.SharedPreferences;
import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.NetworkError;
import com.ua.sdk.Reference;
import com.ua.sdk.Request;
import com.ua.sdk.SaveCallback;
import com.ua.sdk.UaException;
import com.ua.sdk.UaLog;
import com.ua.sdk.authentication.AuthenticationManager;
import com.ua.sdk.cache.Cache;
import com.ua.sdk.cache.CachePolicy;
import com.ua.sdk.cache.CacheSettings;
import com.ua.sdk.cache.DiskCache;
import com.ua.sdk.internal.AbstractManager;
import com.ua.sdk.internal.EntityService;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.LinkEntityRef;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.user.profilephoto.UserProfilePhotoImpl;
import com.ua.sdk.user.profilephoto.UserProfilePhotoManager;
import com.ua.sdk.user.profilephoto.UserProfilePhotoRef;
import java.util.List;
import java.util.concurrent.ExecutorService;

/* JADX INFO: loaded from: classes65.dex */
public class UserManagerImpl extends AbstractManager<User> implements UserManager {
    protected static final String PREF_CURRENT_USER_HREF = "mmdk_user_href";
    protected static final String PREF_CURRENT_USER_ID = "mmdk_user_id";
    protected static final long THREAD_WAIT_DELAY = 1000;
    protected AuthenticationManager mAuthManager;
    protected User mCurrentUser;
    protected EntityRef<User> mCurrentUserRef;
    protected final SharedPreferences mSharedPrefs;
    protected UserProfilePhotoManager mUserProfilePhotoManager;
    protected UserService mUserService;

    public UserManagerImpl(CacheSettings cacheSettings, Cache memCache, DiskCache<User> diskCache, EntityService<User> service, ExecutorService executor, AuthenticationManager authManager, UserProfilePhotoManager userProfilePhotoManager, SharedPreferences sharedPreferences) {
        super(cacheSettings, memCache, diskCache, service, executor);
        this.mSharedPrefs = (SharedPreferences) Precondition.isNotNull(sharedPreferences);
        this.mAuthManager = (AuthenticationManager) Precondition.isNotNull(authManager);
        this.mUserProfilePhotoManager = (UserProfilePhotoManager) Precondition.isNotNull(userProfilePhotoManager);
        this.mUserService = (UserService) service;
        if (getCurrentUserRef() != null) {
            try {
                this.mCurrentUser = fetch(getCurrentUserRef(), CachePolicy.CACHE_ONLY_IGNORE_MAX_AGE);
            } catch (UaException e) {
                UaLog.error("Failed to get current user from cache.", (Throwable) e);
            }
        }
    }

    @Override // com.ua.sdk.user.UserManager
    public EntityRef<User> getCurrentUserRef() {
        if (this.mCurrentUserRef != null) {
            return this.mCurrentUserRef;
        }
        String currentUserId = this.mSharedPrefs.getString(PREF_CURRENT_USER_ID, null);
        String currentUserHref = this.mSharedPrefs.getString(PREF_CURRENT_USER_HREF, null);
        if (currentUserId != null) {
            this.mCurrentUserRef = new LinkEntityRef(currentUserId, currentUserHref);
        }
        return this.mCurrentUserRef;
    }

    private void setCurrentUser(User user) {
        if (user != null) {
            this.mCurrentUser = user;
            this.mCurrentUserRef = user.getRef();
            String id = this.mCurrentUserRef.getId();
            String href = this.mCurrentUserRef.getHref();
            if (id == null || href == null) {
                onLogout();
                return;
            } else {
                this.mSharedPrefs.edit().putString(PREF_CURRENT_USER_ID, id).putString(PREF_CURRENT_USER_HREF, href).commit();
                return;
            }
        }
        onLogout();
    }

    @Override // com.ua.sdk.user.UserManager
    public User getCurrentUser() {
        return this.mCurrentUser;
    }

    @Override // com.ua.sdk.user.UserManager
    public User updateUser(User user) throws UaException {
        return update(user);
    }

    @Override // com.ua.sdk.user.UserManager
    public Request updateUser(User user, SaveCallback<User> callback) {
        return update(user, callback);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.internal.AbstractManager
    public User onPostServiceSave(User entity) throws UaException {
        UserImpl user = (UserImpl) entity;
        fetchUserProfilePhoto(user);
        if (isCurrentUserRef(entity.getRef())) {
            setCurrentUser(user);
        }
        return user;
    }

    @Override // com.ua.sdk.user.UserManager
    public User fetchUser(EntityRef<User> ref) throws UaException {
        return fetch(ref);
    }

    @Override // com.ua.sdk.user.UserManager
    public Request fetchUser(EntityRef<User> ref, FetchCallback<User> callback) {
        return fetch(ref, callback);
    }

    @Override // com.ua.sdk.user.UserManager
    public Request fetchUser(EntityRef<User> ref, CachePolicy cachePolicy, FetchCallback<User> callback) {
        return fetch(ref, cachePolicy, callback);
    }

    private boolean isCurrentUserRef(Reference ref) {
        if (ref == null || ref.getId() == null) {
            return false;
        }
        if ((ref instanceof CurrentUserRef) || ref.getId().equalsIgnoreCase("self")) {
            return true;
        }
        Reference currentUserRef = getCurrentUserRef();
        return currentUserRef != null && ref.getId().equals(currentUserRef.getId());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.internal.AbstractManager
    public User onPostServiceFetch(Reference ref, User user) throws UaException {
        if (isCurrentUserRef(ref)) {
            setCurrentUser(user);
        }
        UserImpl userImpl = (UserImpl) user;
        fetchUserProfilePhoto(userImpl);
        return user;
    }

    private void fetchUserProfilePhoto(UserImpl userImpl) throws UaException {
        List<Link> profilePictureLink = userImpl.getLinks("image");
        if (profilePictureLink != null) {
            UserProfilePhotoRef ref = UserProfilePhotoRef.getBuilder().setId(userImpl.getId()).build();
            UserProfilePhotoImpl userProfilePhoto = (UserProfilePhotoImpl) this.mUserProfilePhotoManager.fetchCurrentProfilePhoto(ref);
            userImpl.setUserProfilePhoto(userProfilePhoto.toImageUrl());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.internal.AbstractManager
    public User onPostServiceCreate(User createdEntity) throws UaException {
        UserImpl userImpl = (UserImpl) createdEntity;
        this.mAuthManager.setOAuth2Credentials(userImpl.getOauth2Credentials());
        setCurrentUser(userImpl);
        fetchUserProfilePhoto(userImpl);
        return userImpl;
    }

    @Override // com.ua.sdk.user.UserManager
    public EntityList<User> fetchUserList(EntityListRef<User> ref) throws UaException {
        return fetchPage(ref);
    }

    @Override // com.ua.sdk.user.UserManager
    public Request fetchUserList(EntityListRef<User> ref, FetchCallback<EntityList<User>> callback) {
        return fetchPage(ref, callback);
    }

    @Override // com.ua.sdk.user.UserManager
    public Request fetchUserList(EntityListRef<User> ref, CachePolicy cachePolicy, FetchCallback<EntityList<User>> callback) {
        return fetchPage(ref, cachePolicy, callback);
    }

    @Override // com.ua.sdk.internal.AbstractManager
    protected EntityList<User> onPostServiceFetchPage(Reference ref, EntityList<User> list) throws UaException {
        UserListImpl users = (UserListImpl) list;
        for (User user : users.getElements()) {
            fetchUserProfilePhoto((UserImpl) user);
        }
        return list;
    }

    @Override // com.ua.sdk.user.UserManager
    public User newUser() {
        return new UserImpl();
    }

    @Override // com.ua.sdk.user.UserManager
    public void onLogout() {
        this.mCurrentUser = null;
        this.mCurrentUserRef = null;
        this.mSharedPrefs.edit().remove(PREF_CURRENT_USER_ID).remove(PREF_CURRENT_USER_HREF).commit();
    }

    @Override // com.ua.sdk.internal.AbstractManager
    protected EntityList<User> fetchPage(EntityListRef<User> ref, CachePolicy cachePolicy, boolean includeMemCache) throws UaException {
        return fetchPage(ref, cachePolicy, includeMemCache, 0);
    }

    protected EntityList<User> fetchPage(EntityListRef<User> ref, CachePolicy cachePolicy, boolean includeMemCache, int recursiveCounter) throws UaException {
        try {
            return super.fetchPage(ref, cachePolicy, includeMemCache);
        } catch (NetworkError e) {
            if (e.getResponseCode() == 202 && recursiveCounter < 5) {
                try {
                    Thread.sleep(1000L);
                    return fetchPage(ref, cachePolicy, false, recursiveCounter + 1);
                } catch (InterruptedException e1) {
                    throw new UaException(e1);
                }
            }
            if (cachePolicy.checkCache()) {
                return fetchPage(ref, CachePolicy.CACHE_ONLY, includeMemCache);
            }
            throw e;
        }
    }
}
