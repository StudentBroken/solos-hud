package com.ua.sdk.page;

import com.ua.sdk.Entity;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.ImageUrl;
import com.ua.sdk.actigraphy.Actigraphy;
import com.ua.sdk.activitystory.ActivityStory;
import com.ua.sdk.location.Location;
import com.ua.sdk.page.association.PageAssociation;
import com.ua.sdk.page.follow.PageFollow;
import com.ua.sdk.user.User;
import com.ua.sdk.workout.Workout;
import java.net.URI;

/* JADX INFO: loaded from: classes65.dex */
public interface Page extends Entity {
    EntityListRef<Actigraphy> getActigraphyRef();

    EntityListRef<ActivityStory> getActivityFeedRef();

    String getAlias();

    ImageUrl getCoverPhoto();

    String getDescription();

    EntityListRef<ActivityStory> getFeaturedFeedRef();

    Integer getFollowerCount();

    EntityListRef<PageFollow> getFollowersRef();

    Integer getFollowingCount();

    EntityListRef<PageFollow> getFollowingRef();

    Integer getFromPageAssociationCount();

    EntityListRef<PageAssociation> getFromPageAssociationsRef();

    String getHeadline();

    Location getLocation();

    EntityRef<PageFollow> getPageFollowRef();

    PageSetting getPageSetting();

    PageTypeEnum getPageType();

    EntityRef<PageType> getPageTypeRef();

    EntityRef<PageFollow> getPageUnfollowRef();

    ImageUrl getProfilePhoto();

    @Override // com.ua.sdk.Entity, com.ua.sdk.Resource
    EntityRef<Page> getRef();

    String getTitle();

    Integer getToPageAssociationCount();

    EntityListRef<PageAssociation> getToPageAssociationsRef();

    URI getUrl();

    EntityRef<User> getUserRef();

    URI getWebsite();

    EntityListRef<Workout> getWorkoutsRef();
}
