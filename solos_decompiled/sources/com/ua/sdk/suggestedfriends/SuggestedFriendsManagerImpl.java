package com.ua.sdk.suggestedfriends;

import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Reference;
import com.ua.sdk.Request;
import com.ua.sdk.UaException;
import com.ua.sdk.cache.Cache;
import com.ua.sdk.cache.CacheSettings;
import com.ua.sdk.cache.DiskCache;
import com.ua.sdk.internal.AbstractManager;
import com.ua.sdk.internal.EntityService;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.LinkEntityRef;
import com.ua.sdk.user.profilephoto.UserProfilePhoto;
import com.ua.sdk.user.profilephoto.UserProfilePhotoImpl;
import com.ua.sdk.user.profilephoto.UserProfilePhotoManager;
import java.util.List;
import java.util.concurrent.ExecutorService;

/* JADX INFO: loaded from: classes65.dex */
public class SuggestedFriendsManagerImpl extends AbstractManager<SuggestedFriends> implements SuggestedFriendsManager {
    private UserProfilePhotoManager userProfilePhotoManager;

    public SuggestedFriendsManagerImpl(CacheSettings cacheSettings, Cache memCache, DiskCache<SuggestedFriends> diskCache, EntityService<SuggestedFriends> service, ExecutorService executor, UserProfilePhotoManager userProfilePhotoManager) {
        super(cacheSettings, memCache, diskCache, service, executor);
        this.userProfilePhotoManager = userProfilePhotoManager;
    }

    private void fetchUserProfilePhoto(SuggestedFriendsImpl suggestedFriends) throws UaException {
        List<Link> profilePictureLink = suggestedFriends.getLinks(SuggestedFriendsImpl.REF_PROFILE_PIC);
        if (profilePictureLink != null) {
            LinkEntityRef<UserProfilePhoto> link = new LinkEntityRef<>(profilePictureLink.get(0).getId(), profilePictureLink.get(0).getHref());
            UserProfilePhotoImpl userProfilePhoto = (UserProfilePhotoImpl) this.userProfilePhotoManager.fetchCurrentProfilePhoto(link);
            suggestedFriends.setSuggestedFriendsProfilePicture(userProfilePhoto.toImageUrl());
        }
    }

    @Override // com.ua.sdk.suggestedfriends.SuggestedFriendsManager
    public EntityList<SuggestedFriends> fetchSuggestedFriendList(EntityListRef<SuggestedFriends> listRef) throws UaException {
        return fetchPage(listRef);
    }

    @Override // com.ua.sdk.suggestedfriends.SuggestedFriendsManager
    public Request fetchSuggestedFriendList(EntityListRef<SuggestedFriends> listRef, FetchCallback<EntityList<SuggestedFriends>> callback) {
        return fetchPage(listRef, callback);
    }

    @Override // com.ua.sdk.internal.AbstractManager
    protected EntityList<SuggestedFriends> onPostServiceFetchPage(Reference ref, EntityList<SuggestedFriends> list) throws UaException {
        SuggestedFriendsListImpl suggestedFriends = (SuggestedFriendsListImpl) list;
        for (SuggestedFriends friend : suggestedFriends.getElements()) {
            fetchUserProfilePhoto((SuggestedFriendsImpl) friend);
        }
        return list;
    }
}
