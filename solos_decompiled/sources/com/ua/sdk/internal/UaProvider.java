package com.ua.sdk.internal;

import android.content.SharedPreferences;
import com.ua.sdk.IntensityCalculator;
import com.ua.sdk.MetabolicEnergyCalculator;
import com.ua.sdk.actigraphy.ActigraphyManager;
import com.ua.sdk.activitystory.ActivityStoryManager;
import com.ua.sdk.activitytimeseries.ActivityTimeSeriesManager;
import com.ua.sdk.activitytype.ActivityTypeManager;
import com.ua.sdk.aggregate.AggregateManager;
import com.ua.sdk.authentication.AuthenticationManager;
import com.ua.sdk.bodymass.BodyMassManager;
import com.ua.sdk.cache.CacheSettings;
import com.ua.sdk.cache.DiskCache;
import com.ua.sdk.friendship.FriendshipManager;
import com.ua.sdk.gear.GearManager;
import com.ua.sdk.gear.brand.GearBrandManager;
import com.ua.sdk.gear.user.UserGearManager;
import com.ua.sdk.group.GroupManager;
import com.ua.sdk.group.invite.GroupInviteManager;
import com.ua.sdk.group.leaderboard.GroupLeaderboardManager;
import com.ua.sdk.group.objective.GroupObjectiveManager;
import com.ua.sdk.group.purpose.GroupPurposeManager;
import com.ua.sdk.group.user.GroupUserManager;
import com.ua.sdk.heartrate.HeartRateZonesManager;
import com.ua.sdk.moderation.ModerationManager;
import com.ua.sdk.page.PageManager;
import com.ua.sdk.page.association.PageAssociationManager;
import com.ua.sdk.recorder.RecorderManager;
import com.ua.sdk.recorder.persistence.RecorderConfigurationDatabase;
import com.ua.sdk.remoteconnection.RemoteConnection;
import com.ua.sdk.remoteconnection.RemoteConnectionManager;
import com.ua.sdk.remoteconnection.RemoteConnectionType;
import com.ua.sdk.remoteconnection.RemoteConnectionTypeManager;
import com.ua.sdk.role.RoleSuperManager;
import com.ua.sdk.route.RouteManager;
import com.ua.sdk.sleep.SleepManager;
import com.ua.sdk.user.User;
import com.ua.sdk.user.UserManager;
import com.ua.sdk.user.profilephoto.UserProfilePhoto;
import com.ua.sdk.user.profilephoto.UserProfilePhotoManager;
import com.ua.sdk.user.stats.UserStatsManager;
import com.ua.sdk.util.TimeSource;
import com.ua.sdk.workout.Workout;
import com.ua.sdk.workout.WorkoutManager;
import java.util.concurrent.ExecutorService;

/* JADX INFO: loaded from: classes65.dex */
public interface UaProvider {
    CacheSettings getActigraphyCacheSettings();

    ActigraphyManager getActigraphyManager();

    ActivityStoryManager getActivityStoryManager();

    ActivityTimeSeriesManager getActivityTimeSeriesManager();

    ActivityTypeManager getActivityTypeManager();

    AggregateManager getAggregateManager();

    AuthenticationManager getAuthenticationManager();

    BodyMassManager getBodyMassManager();

    ExecutorService getExecutionService();

    FriendshipManager getFriendshipManager();

    GearBrandManager getGearBrandManager();

    GearManager getGearManager();

    GroupInviteManager getGroupInviteManager();

    GroupLeaderboardManager getGroupLeaderboardManager();

    GroupManager getGroupManager();

    GroupObjectiveManager getGroupObjectiveManager();

    GroupPurposeManager getGroupPurposeManager();

    GroupUserManager getGroupUserManager();

    HeartRateZonesManager getHeartRateZonesManager();

    IntensityCalculator getIntensityCalculator();

    MetabolicEnergyCalculator getMetabolicEnergyCalculator();

    ModerationManager getModerationManager();

    PageAssociationManager getPageAssociationManager();

    PageManager getPageSuperManager();

    RecorderConfigurationDatabase getRecorderConfigurationCache();

    RecorderManager getRecorderManager();

    DiskCache<RemoteConnection> getRemoteConnectionDiskCache();

    RemoteConnectionManager getRemoteConnectionManager();

    DiskCache<RemoteConnectionType> getRemoteConnectionTypeDiskCache();

    RemoteConnectionTypeManager getRemoteConnectionTypeManager();

    RoleSuperManager getRolesManager();

    RouteManager getRouteManager();

    SharedPreferences getSharedPreferences();

    SleepManager getSleepManager();

    TimeSource getTimeSource();

    CacheSettings getUserCacheSettings();

    DiskCache<User> getUserDiskCache();

    UserGearManager getUserGearManager();

    UserManager getUserManager();

    DiskCache<UserProfilePhoto> getUserProfilePhotoDiskCache();

    UserProfilePhotoManager getUserProfilePhotoManager();

    UserStatsManager getUserStatsManager();

    CacheSettings getWorkoutCacheSettings();

    DiskCache<Workout> getWorkoutDiskCache();

    WorkoutManager getWorkoutManager();
}
