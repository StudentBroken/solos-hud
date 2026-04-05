package com.ua.sdk.internal.net;

import com.ua.sdk.EntityListRef;
import com.ua.sdk.Reference;
import com.ua.sdk.actigraphy.ActigraphyListRef;
import com.ua.sdk.activitystory.ActivityStory;
import com.ua.sdk.suggestedfriends.SuggestedFriends;
import java.net.URL;

/* JADX INFO: loaded from: classes65.dex */
public interface UrlBuilder {
    URL buildApproveFriendship(Reference reference);

    URL buildCreateActivityStoryUrl();

    URL buildCreateActivityTimeSeriesUrl();

    URL buildCreateFilemobileTokenCredentialUrl();

    URL buildCreateGroupInviteUrl();

    URL buildCreateGroupObjectiveUrl();

    URL buildCreateGroupUrl();

    URL buildCreateGroupUserUrl();

    URL buildCreateHeartRateZonesUrl();

    URL buildCreateInternalTokenCredentialUrl();

    URL buildCreateModerationActionUrl();

    URL buildCreateNotificationRegistrationUrl();

    URL buildCreatePageAssociationUrl();

    URL buildCreatePageFollowUrl();

    URL buildCreateRouteBookmarkUrl();

    URL buildCreateRouteUrl();

    URL buildCreateUserGearUrl();

    URL buildCreateUserRoleUrl();

    URL buildCreateUserUrl();

    URL buildCreateWorkoutUrl();

    URL buildDeleteFriendshipUrl(Reference reference);

    URL buildDeleteGroupInviteUrl(Reference reference);

    URL buildDeleteGroupObjectiveUrl(Reference reference);

    URL buildDeleteGroupUrl(Reference reference);

    URL buildDeleteGroupUserUrl(Reference reference);

    URL buildDeleteNotificationRegistrationUrl(Reference reference);

    URL buildDeletePageAssociationUrl(Reference reference);

    URL buildDeletePageFollowUrl(Reference reference);

    URL buildDeleteRemoteConnectionUrl(Reference reference);

    URL buildDeleteRouteBookmarkUrl(Reference reference);

    URL buildDeleteRouteUrl(Reference reference);

    URL buildDeleteUserGearUrl(Reference reference);

    URL buildDeleteWorkoutUrl(Reference reference);

    URL buildFetchAggregateListUrl(Reference reference);

    URL buildFetchBodyMassListUrl(Reference reference);

    URL buildFetchBodyMassUrl(Reference reference);

    URL buildFetchGroupLeaderboardListUrl(Reference reference);

    URL buildFetchGroupListUrl(Reference reference);

    URL buildFetchGroupUrl(Reference reference);

    URL buildFetchHeartRateZonesListUrl(Reference reference);

    URL buildFetchHeartRateZonesUrl(Reference reference);

    URL buildFetchRouteBookmarkListUrl(EntityListRef entityListRef);

    URL buildFetchRouteBookmarkUrl(Reference reference);

    URL buildGetActigraphyRecorderPriorityUrl();

    URL buildGetActigraphySettingsUrl(Reference reference);

    URL buildGetActigraphyUrl(ActigraphyListRef actigraphyListRef);

    URL buildGetActivityFeedUrl(EntityListRef<ActivityStory> entityListRef);

    URL buildGetActivityStoryUrl(Reference reference);

    URL buildGetActivityTypeListUrl(Reference reference);

    URL buildGetActivityTypeUrl(Reference reference);

    URL buildGetAuthenticationToken();

    URL buildGetCurrentUserUrl();

    URL buildGetFriendsUrl(EntityListRef entityListRef);

    URL buildGetGearBrandUrl(EntityListRef entityListRef);

    URL buildGetGearUrl(EntityListRef entityListRef);

    URL buildGetGroupInviteUrl(Reference reference);

    URL buildGetGroupObjectiveListUrl(Reference reference);

    URL buildGetGroupObjectiveUrl(Reference reference);

    URL buildGetGroupPurposesUrl(Reference reference);

    URL buildGetGroupUserUrl(Reference reference);

    URL buildGetNotificationRegistrationPageUrl(EntityListRef entityListRef);

    URL buildGetNotificationRegistrationUrl(Reference reference);

    URL buildGetNotificationSubscriptionPageUrl(Reference reference);

    URL buildGetNotificationSubscriptionUrl(Reference reference);

    URL buildGetPageAssociationUrl(Reference reference);

    URL buildGetPageAssociationsUrl(EntityListRef entityListRef);

    URL buildGetPageFollowPageUrl(EntityListRef entityListRef);

    URL buildGetPageFollowUrl(Reference reference);

    URL buildGetPageUrl(Reference reference);

    URL buildGetPagesUrl(Reference reference);

    URL buildGetRemoteConnectionTypeUrl(Reference reference);

    URL buildGetRemoteConnectionUrl(Reference reference);

    URL buildGetRoleUrl(Reference reference);

    URL buildGetRolesUrl(EntityListRef entityListRef);

    URL buildGetRouteUrl(Reference reference);

    URL buildGetSleepListUrl(Reference reference);

    URL buildGetSleepUrl(Reference reference);

    URL buildGetSuggestedFriendsUrl(EntityListRef<SuggestedFriends> entityListRef);

    URL buildGetUserGearUrl(EntityListRef entityListRef);

    URL buildGetUserPageUrl(EntityListRef entityListRef);

    URL buildGetUserPermissionUrl(Reference reference);

    URL buildGetUserPermissionsUrl(EntityListRef entityListRef);

    URL buildGetUserProfilePhotoUrl(Reference reference);

    URL buildGetUserRoleUrl(Reference reference);

    URL buildGetUserStatsUrl(Reference reference);

    URL buildGetUserUrl(Reference reference);

    URL buildGetWorkoutByIdUrl(Reference reference);

    URL buildGetWorkoutsListUrl(Reference reference);

    URL buildOAuth2AuthorizationUrl(String str, String str2);

    URL buildPatchFriendshipRequest(Reference reference);

    URL buildPatchGroupInviteUrl(Reference reference);

    URL buildPatchGroupUrl(Reference reference);

    URL buildPatchNotificationRegistrationUrl(Reference reference);

    URL buildPatchNotificationSubscriptionUrl(Reference reference);

    URL buildPatchPageFollowUrl(Reference reference);

    URL buildPatchUserGearUrl(Reference reference);

    URL buildPostImageUrl();

    URL buildPostVideoUrl();

    URL buildResetPasswordUrl();

    URL buildRpcPatchActivityStoryUrl(Reference reference);

    URL buildSaveBodyMassUrl(Reference reference);

    URL buildSaveGroupObjectiveUrl(Reference reference);

    URL buildSaveGroupUrl(Reference reference);

    URL buildSaveSleepUrl(Reference reference);

    URL buildSaveUserUrl(Reference reference);

    URL buildSaveWorkoutUrl(Reference reference);

    URL buildSendFriendshipRequest();

    URL buildUpdateRouteUrl(Reference reference);
}
