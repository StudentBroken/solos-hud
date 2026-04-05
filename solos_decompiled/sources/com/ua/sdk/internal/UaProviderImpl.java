package com.ua.sdk.internal;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.ua.sdk.EntityList;
import com.ua.sdk.IntensityCalculator;
import com.ua.sdk.MetabolicEnergyCalculator;
import com.ua.sdk.actigraphy.Actigraphy;
import com.ua.sdk.actigraphy.ActigraphyJsonParser;
import com.ua.sdk.actigraphy.ActigraphyManager;
import com.ua.sdk.actigraphy.ActigraphyManagerImpl;
import com.ua.sdk.actigraphy.ActigraphyService;
import com.ua.sdk.actigraphysettings.ActigraphySettingsJsonParser;
import com.ua.sdk.actigraphysettings.ActigraphySettingsManager;
import com.ua.sdk.actigraphysettings.ActigraphySettingsManagerImpl;
import com.ua.sdk.actigraphysettings.ActigraphySettingsRequestWriter;
import com.ua.sdk.actigraphysettings.ActigraphySettingsService;
import com.ua.sdk.activitystory.ActivityStory;
import com.ua.sdk.activitystory.ActivityStoryJsonParser;
import com.ua.sdk.activitystory.ActivityStoryJsonWriter;
import com.ua.sdk.activitystory.ActivityStoryListJsonParser;
import com.ua.sdk.activitystory.ActivityStoryManager;
import com.ua.sdk.activitystory.ActivityStoryManagerImpl;
import com.ua.sdk.activitystory.ActivityStoryService;
import com.ua.sdk.activitytimeseries.ActivityTimeSeriesJsonParser;
import com.ua.sdk.activitytimeseries.ActivityTimeSeriesJsonWriter;
import com.ua.sdk.activitytimeseries.ActivityTimeSeriesManager;
import com.ua.sdk.activitytimeseries.ActivityTimeSeriesManagerImpl;
import com.ua.sdk.activitytimeseries.ActivityTimeSeriesService;
import com.ua.sdk.activitytype.ActivityTypeDatabase;
import com.ua.sdk.activitytype.ActivityTypeListJsonParser;
import com.ua.sdk.activitytype.ActivityTypeManager;
import com.ua.sdk.activitytype.ActivityTypeManagerImpl;
import com.ua.sdk.activitytype.ActivityTypeParser;
import com.ua.sdk.activitytype.ActivityTypeService;
import com.ua.sdk.aggregate.AggregateJsonParser;
import com.ua.sdk.aggregate.AggregateListJsonParser;
import com.ua.sdk.aggregate.AggregateManager;
import com.ua.sdk.aggregate.AggregateManagerImpl;
import com.ua.sdk.aggregate.AggregateService;
import com.ua.sdk.authentication.AuthenticationManager;
import com.ua.sdk.authentication.AuthenticationManagerImpl;
import com.ua.sdk.authentication.AuthenticationService;
import com.ua.sdk.authentication.AuthenticationServiceImpl;
import com.ua.sdk.authentication.FilemobileCredentialJsonParser;
import com.ua.sdk.authentication.FilemobileCredentialManager;
import com.ua.sdk.authentication.FilemobileCredentialManagerImpl;
import com.ua.sdk.authentication.FilemobileCredentialService;
import com.ua.sdk.authentication.OAuth2Credentials;
import com.ua.sdk.authentication.OAuth2CredentialsParser;
import com.ua.sdk.bodymass.BodyMassJsonParser;
import com.ua.sdk.bodymass.BodyMassJsonWriter;
import com.ua.sdk.bodymass.BodyMassListJsonParser;
import com.ua.sdk.bodymass.BodyMassManager;
import com.ua.sdk.bodymass.BodyMassManagerImpl;
import com.ua.sdk.bodymass.BodyMassService;
import com.ua.sdk.cache.CachePolicy;
import com.ua.sdk.cache.CacheSettings;
import com.ua.sdk.cache.DiskCache;
import com.ua.sdk.cache.NullDiskCache;
import com.ua.sdk.cache.memory.MemoryCache;
import com.ua.sdk.friendship.Friendship;
import com.ua.sdk.friendship.FriendshipJsonParser;
import com.ua.sdk.friendship.FriendshipJsonWriter;
import com.ua.sdk.friendship.FriendshipManager;
import com.ua.sdk.friendship.FriendshipManagerImpl;
import com.ua.sdk.friendship.FriendshipPageJsonParser;
import com.ua.sdk.friendship.FriendshipService;
import com.ua.sdk.gear.GearListJsonParser;
import com.ua.sdk.gear.GearManager;
import com.ua.sdk.gear.GearManagerImpl;
import com.ua.sdk.gear.GearService;
import com.ua.sdk.gear.brand.GearBrandListJsonParser;
import com.ua.sdk.gear.brand.GearBrandManager;
import com.ua.sdk.gear.brand.GearBrandManagerImpl;
import com.ua.sdk.gear.brand.GearBrandService;
import com.ua.sdk.gear.user.UserGearJsonParser;
import com.ua.sdk.gear.user.UserGearJsonWriter;
import com.ua.sdk.gear.user.UserGearListJsonParser;
import com.ua.sdk.gear.user.UserGearManager;
import com.ua.sdk.gear.user.UserGearManagerImpl;
import com.ua.sdk.gear.user.UserGearService;
import com.ua.sdk.group.GroupJsonParser;
import com.ua.sdk.group.GroupJsonWriter;
import com.ua.sdk.group.GroupListJsonParser;
import com.ua.sdk.group.GroupManager;
import com.ua.sdk.group.GroupManagerImpl;
import com.ua.sdk.group.GroupService;
import com.ua.sdk.group.invite.GroupInviteJsonParser;
import com.ua.sdk.group.invite.GroupInviteJsonWriter;
import com.ua.sdk.group.invite.GroupInviteListJsonParser;
import com.ua.sdk.group.invite.GroupInviteManager;
import com.ua.sdk.group.invite.GroupInviteManagerImpl;
import com.ua.sdk.group.invite.GroupInviteService;
import com.ua.sdk.group.leaderboard.GroupLeaderboardJsonParser;
import com.ua.sdk.group.leaderboard.GroupLeaderboardListJsonParser;
import com.ua.sdk.group.leaderboard.GroupLeaderboardManager;
import com.ua.sdk.group.leaderboard.GroupLeaderboardManagerImpl;
import com.ua.sdk.group.leaderboard.GroupLeaderboardService;
import com.ua.sdk.group.objective.GroupObjectiveJsonParser;
import com.ua.sdk.group.objective.GroupObjectiveJsonWriter;
import com.ua.sdk.group.objective.GroupObjectiveListJsonParser;
import com.ua.sdk.group.objective.GroupObjectiveManager;
import com.ua.sdk.group.objective.GroupObjectiveManagerImpl;
import com.ua.sdk.group.objective.GroupObjectiveService;
import com.ua.sdk.group.purpose.GroupPurposeJsonParser;
import com.ua.sdk.group.purpose.GroupPurposeListJsonParser;
import com.ua.sdk.group.purpose.GroupPurposeManager;
import com.ua.sdk.group.purpose.GroupPurposeManagerImpl;
import com.ua.sdk.group.purpose.GroupPurposeService;
import com.ua.sdk.group.user.GroupUserJsonParser;
import com.ua.sdk.group.user.GroupUserJsonWriter;
import com.ua.sdk.group.user.GroupUserListJsonParser;
import com.ua.sdk.group.user.GroupUserManager;
import com.ua.sdk.group.user.GroupUserManagerImpl;
import com.ua.sdk.group.user.GroupUserService;
import com.ua.sdk.heartrate.HeartRateZonesJsonParser;
import com.ua.sdk.heartrate.HeartRateZonesJsonWriter;
import com.ua.sdk.heartrate.HeartRateZonesListJsonParser;
import com.ua.sdk.heartrate.HeartRateZonesManager;
import com.ua.sdk.heartrate.HeartRateZonesManagerImpl;
import com.ua.sdk.heartrate.HeartRateZonesService;
import com.ua.sdk.internal.net.UrlBuilder;
import com.ua.sdk.internal.net.v7.UrlBuilderImpl;
import com.ua.sdk.moderation.ModerationActionManager;
import com.ua.sdk.moderation.ModerationActionParser;
import com.ua.sdk.moderation.ModerationActionService;
import com.ua.sdk.moderation.ModerationActionWriter;
import com.ua.sdk.moderation.ModerationManager;
import com.ua.sdk.moderation.ModerationManagerImpl;
import com.ua.sdk.net.json.GsonFactory;
import com.ua.sdk.page.Page;
import com.ua.sdk.page.PageJsonParser;
import com.ua.sdk.page.PageListJsonParser;
import com.ua.sdk.page.PageManager;
import com.ua.sdk.page.PageManagerImpl;
import com.ua.sdk.page.PageService;
import com.ua.sdk.page.PageSuperManager;
import com.ua.sdk.page.association.PageAssociationJsonParser;
import com.ua.sdk.page.association.PageAssociationListJsonParser;
import com.ua.sdk.page.association.PageAssociationManager;
import com.ua.sdk.page.association.PageAssociationManagerImpl;
import com.ua.sdk.page.association.PageAssociationRequestJsonWriter;
import com.ua.sdk.page.association.PageAssociationService;
import com.ua.sdk.page.follow.PageFollow;
import com.ua.sdk.page.follow.PageFollowJsonParser;
import com.ua.sdk.page.follow.PageFollowManager;
import com.ua.sdk.page.follow.PageFollowPageJsonParser;
import com.ua.sdk.page.follow.PageFollowRequestJsonWriter;
import com.ua.sdk.page.follow.PageFollowService;
import com.ua.sdk.recorder.RecorderManager;
import com.ua.sdk.recorder.RecorderManagerImpl;
import com.ua.sdk.recorder.persistence.RecorderConfigurationDatabase;
import com.ua.sdk.remoteconnection.RemoteConnection;
import com.ua.sdk.remoteconnection.RemoteConnectionJsonParser;
import com.ua.sdk.remoteconnection.RemoteConnectionManager;
import com.ua.sdk.remoteconnection.RemoteConnectionManagerImpl;
import com.ua.sdk.remoteconnection.RemoteConnectionPageJsonParser;
import com.ua.sdk.remoteconnection.RemoteConnectionService;
import com.ua.sdk.remoteconnection.RemoteConnectionType;
import com.ua.sdk.remoteconnection.RemoteConnectionTypeJsonParser;
import com.ua.sdk.remoteconnection.RemoteConnectionTypeManager;
import com.ua.sdk.remoteconnection.RemoteConnectionTypeManagerImpl;
import com.ua.sdk.remoteconnection.RemoteConnectionTypePageJsonParser;
import com.ua.sdk.remoteconnection.RemoteConnectionTypeService;
import com.ua.sdk.role.RoleJsonParser;
import com.ua.sdk.role.RoleListJsonParser;
import com.ua.sdk.role.RoleManager;
import com.ua.sdk.role.RoleManagerImpl;
import com.ua.sdk.role.RoleService;
import com.ua.sdk.role.RoleSuperManager;
import com.ua.sdk.role.RoleSuperManagerImpl;
import com.ua.sdk.route.Route;
import com.ua.sdk.route.RouteBookmarkManager;
import com.ua.sdk.route.RouteJsonParser;
import com.ua.sdk.route.RouteJsonWriter;
import com.ua.sdk.route.RouteManager;
import com.ua.sdk.route.RouteManagerImpl;
import com.ua.sdk.route.RoutePageJsonParser;
import com.ua.sdk.route.RouteService;
import com.ua.sdk.route.bookmark.RouteBookmarkJsonParser;
import com.ua.sdk.route.bookmark.RouteBookmarkJsonWriter;
import com.ua.sdk.route.bookmark.RouteBookmarkListJsonParser;
import com.ua.sdk.route.bookmark.RouteBookmarkManagerImpl;
import com.ua.sdk.route.bookmark.RouteBookmarkService;
import com.ua.sdk.sleep.SleepManager;
import com.ua.sdk.sleep.SleepManagerImpl;
import com.ua.sdk.sleep.SleepMetricJsonParser;
import com.ua.sdk.sleep.SleepMetricJsonWriter;
import com.ua.sdk.sleep.SleepMetricListJsonParser;
import com.ua.sdk.sleep.SleepService;
import com.ua.sdk.suggestedfriends.SuggestedFriendsJsonParser;
import com.ua.sdk.suggestedfriends.SuggestedFriendsListJsonParser;
import com.ua.sdk.suggestedfriends.SuggestedFriendsManager;
import com.ua.sdk.suggestedfriends.SuggestedFriendsManagerImpl;
import com.ua.sdk.suggestedfriends.SuggestedFriendsService;
import com.ua.sdk.user.User;
import com.ua.sdk.user.UserDatabase;
import com.ua.sdk.user.UserJsonParser;
import com.ua.sdk.user.UserJsonWriter;
import com.ua.sdk.user.UserManager;
import com.ua.sdk.user.UserManagerImpl;
import com.ua.sdk.user.UserPageParser;
import com.ua.sdk.user.UserService;
import com.ua.sdk.user.permission.UserPermissionListJsonParser;
import com.ua.sdk.user.permission.UserPermissionManager;
import com.ua.sdk.user.permission.UserPermissionManagerImpl;
import com.ua.sdk.user.permission.UserPermissionService;
import com.ua.sdk.user.profilephoto.UserProfilePhoto;
import com.ua.sdk.user.profilephoto.UserProfilePhotoDatabase;
import com.ua.sdk.user.profilephoto.UserProfilePhotoJsonParser;
import com.ua.sdk.user.profilephoto.UserProfilePhotoManager;
import com.ua.sdk.user.profilephoto.UserProfilePhotoManagerImpl;
import com.ua.sdk.user.profilephoto.UserProfilePhotoService;
import com.ua.sdk.user.role.UserRoleJsonParser;
import com.ua.sdk.user.role.UserRoleJsonWriter;
import com.ua.sdk.user.role.UserRoleManager;
import com.ua.sdk.user.role.UserRoleManagerImpl;
import com.ua.sdk.user.role.UserRoleService;
import com.ua.sdk.user.stats.UserStatsJsonParser;
import com.ua.sdk.user.stats.UserStatsManager;
import com.ua.sdk.user.stats.UserStatsManagerImpl;
import com.ua.sdk.user.stats.UserStatsService;
import com.ua.sdk.util.SystemTimeSource;
import com.ua.sdk.util.TimeSource;
import com.ua.sdk.workout.Workout;
import com.ua.sdk.workout.WorkoutDatabase;
import com.ua.sdk.workout.WorkoutJsonParser;
import com.ua.sdk.workout.WorkoutJsonWriter;
import com.ua.sdk.workout.WorkoutListParser;
import com.ua.sdk.workout.WorkoutManager;
import com.ua.sdk.workout.WorkoutManagerImpl;
import com.ua.sdk.workout.WorkoutService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes65.dex */
public class UaProviderImpl implements UaProvider {
    private static final String SHARED_PREFS = "mmdk_shared_prefs";
    private JsonParser<EntityList<Actigraphy>> actigraphyParser;
    private ActigraphySettingsJsonParser actigraphySettingsJsonParser;
    private ActigraphySettingsManager actigraphySettingsManager;
    private ActigraphySettingsRequestWriter actigraphySettingsRequestWriter;
    private ActivityStoryManager activityStoryManager;
    private ActivityTimeSeriesManager activityTimeSeriesManager;
    private ActivityTypeManager activityTypeManager;
    private AggregateManager aggregateManager;
    private BodyMassManager bodyMassManager;
    private final String clientId;
    private final String clientSecret;
    private ConnectionFactory connectionFactory;
    private final Context context;
    private boolean debug;
    private JsonParser<EntityList<Friendship>> friendshipPageParser;
    private JsonParser<Friendship> friendshipParser;
    private JsonWriter<Friendship> friendshipWriter;
    private GearBrandManager gearBrandManager;
    private GearManager gearManager;
    private GroupInviteManager groupInviteManager;
    private GroupLeaderboardManager groupLeaderboardManager;
    private GroupManager groupManager;
    private GroupObjectiveManager groupObjectiveManager;
    private GroupPurposeManager groupPurposeManager;
    private GroupUserManager groupUserManager;
    private Gson gson;
    private HeartRateZonesManager heartRateZonesManager;
    private ModerationManager moderationManager;
    private PageAssociationManager pageAssociationManager;
    private JsonParser<EntityList<PageFollow>> pageFollowPageParser;
    private JsonParser<PageFollow> pageFollowParser;
    private JsonWriter<PageFollow> pageFollowRequestWriter;
    private JsonParser<Page> pageJsonParser;
    private PageManager pageManager;
    private JsonParser<EntityList<Page>> pagePageJsonParser;
    private RecorderConfigurationDatabase recorderConfigurationCache;
    private RecorderManager recorderManager;
    private RemoteConnectionJsonParser remoteConnectionJsonParser;
    private RemoteConnectionManager remoteConnectionManager;
    private RemoteConnectionPageJsonParser remoteConnectionPageJsonParser;
    private RemoteConnectionTypeJsonParser remoteConnectionTypeJsonParser;
    private RemoteConnectionTypeManager remoteConnectionTypeManager;
    private RemoteConnectionTypePageJsonParser remoteConnectionTypePageJsonParser;
    private RoleManager roleManager;
    private RoleSuperManager roleSuperManager;
    private RouteBookmarkManager routeBookmarkManager;
    private JsonWriter<Route> routeJsonWriter;
    private RouteManager routeManager;
    private JsonParser<EntityList<Route>> routePageParser;
    private JsonParser<Route> routeParser;
    private SleepManager sleepManager;
    private SuggestedFriendsManager suggestedFriendsManager;
    private UrlBuilder urlBuilder;
    private UserGearManager userGearManager;
    private UserPermissionManager userPermissionManager;
    private UserRoleManager userRoleManager;
    private UserStatsManager userStatsManager;
    private CacheSettings workoutCacheSettings;
    private WorkoutDatabase workoutDatabase;
    private WorkoutManager workoutManager;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = (CPU_COUNT * 2) + 1;
    private Gson gsonSuggestedFriends = null;
    protected MemoryCache memCache = new MemoryCache();
    private ExecutorService executorService = null;
    private UserManager userManager = null;
    private ActigraphyManager actigraphyManager = null;
    private AuthenticationService authService = null;
    protected AuthenticationManager authManager = null;
    private FilemobileCredentialManager filemobileCredentialManager = null;
    private JsonParser<User> userParser = null;
    private JsonWriter<User> userWriter = null;
    private JsonParser<EntityList<User>> userPageParser = null;
    private JsonParser<OAuth2Credentials> oauth2Parser = null;
    private UserDatabase userDiskCache = null;
    private SharedPreferences sharedPrefs = null;
    private UserProfilePhotoManager profilePhotoManager = null;
    private JsonParser<UserProfilePhoto> profilePhotoParser = null;
    private UserProfilePhotoDatabase profilePhotoDiskCache = null;
    private CacheSettings userCacheSettings = null;
    private CacheSettings actigraphyCacheSettings = null;
    private FriendshipManager friendshipManager = null;

    public UaProviderImpl(String clientId, String clientSecret, Context context, boolean debug) {
        this.context = (Context) Precondition.isNotNull(context, "context");
        this.clientId = (String) Precondition.isNotNull(clientId, "clientId");
        this.clientSecret = (String) Precondition.isNotNull(clientSecret, "clientSecret");
        this.debug = debug;
    }

    public UrlBuilder getUrlBuilder() {
        if (this.urlBuilder == null) {
            this.urlBuilder = new UrlBuilderImpl();
        }
        return this.urlBuilder;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public DiskCache<User> getUserDiskCache() {
        if (this.userDiskCache == null) {
            this.userDiskCache = UserDatabase.getInstance(this.context);
        }
        return this.userDiskCache;
    }

    protected Gson getGson() {
        if (this.gson == null) {
            this.gson = GsonFactory.newInstance();
        }
        return this.gson;
    }

    private Gson getGsonSuggestedFriends() {
        if (this.gsonSuggestedFriends == null) {
            this.gsonSuggestedFriends = GsonFactory.newSuggestedFriendsInstance();
        }
        return this.gsonSuggestedFriends;
    }

    protected JsonParser<User> getUserParser() {
        if (this.userParser == null) {
            this.userParser = getParser(new UserJsonParser(getGson()));
        }
        return this.userParser;
    }

    protected JsonWriter<User> getUserWriter() {
        if (this.userWriter == null) {
            this.userWriter = getWriter(new UserJsonWriter(getGson()));
        }
        return this.userWriter;
    }

    protected JsonParser<EntityList<User>> getUserPageParser() {
        if (this.userPageParser == null) {
            this.userPageParser = getParser(new UserPageParser(getGson()));
        }
        return this.userPageParser;
    }

    private JsonParser<UserProfilePhoto> getProfilePhotoParser() {
        if (this.profilePhotoParser == null) {
            this.profilePhotoParser = getParser(new UserProfilePhotoJsonParser(getGson()));
        }
        return this.profilePhotoParser;
    }

    private JsonParser<EntityList<Friendship>> getFriendshipPageParser() {
        if (this.friendshipPageParser == null) {
            this.friendshipPageParser = getParser(new FriendshipPageJsonParser(getGson()));
        }
        return this.friendshipPageParser;
    }

    private JsonParser<Friendship> getFriendshipParser() {
        if (this.friendshipParser == null) {
            this.friendshipParser = getParser(new FriendshipJsonParser(getGson()));
        }
        return this.friendshipParser;
    }

    private JsonWriter<Friendship> getFriendshipWriter() {
        if (this.friendshipWriter == null) {
            this.friendshipWriter = getWriter(new FriendshipJsonWriter(getGson()));
        }
        return this.friendshipWriter;
    }

    private JsonParser<EntityList<Actigraphy>> getActigraphyParser() {
        if (this.actigraphyParser == null) {
            this.actigraphyParser = getParser(new ActigraphyJsonParser(getGson()));
        }
        return this.actigraphyParser;
    }

    protected <T> JsonParser<T> getParser(JsonParser<T> parser) {
        if (this.debug) {
            return new DebugJsonParser<>(parser);
        }
        return parser;
    }

    protected <T> JsonWriter<T> getWriter(JsonWriter<T> writer) {
        if (this.debug) {
            return new DebugJsonWriter<>(writer);
        }
        return writer;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public UserManager getUserManager() {
        if (this.userManager == null) {
            UserService userService = new UserService(getConnectionFactory(), getAuthenticationManager(), getUrlBuilder(), getUserParser(), getUserWriter(), getUserPageParser());
            this.userManager = new UserManagerImpl(getUserCacheSettings(), this.memCache, getUserDiskCache(), userService, getExecutionService(), getAuthenticationManager(), getUserProfilePhotoManager(), getSharedPreferences());
        }
        return this.userManager;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public ActigraphyManager getActigraphyManager() {
        if (this.actigraphyManager == null) {
            ActigraphyService actigraphyService = new ActigraphyService(getConnectionFactory(), getAuthenticationManager(), getUrlBuilder(), getActigraphyParser());
            this.actigraphyManager = new ActigraphyManagerImpl(getActigraphyCacheSettings(), this.memCache, null, actigraphyService, getExecutionService());
        }
        return this.actigraphyManager;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public FriendshipManager getFriendshipManager() {
        if (this.friendshipManager == null) {
            FriendshipService friendshipService = new FriendshipService(getConnectionFactory(), getAuthenticationManager(), getUrlBuilder(), getFriendshipPageParser(), getFriendshipParser(), getFriendshipWriter());
            this.friendshipManager = new FriendshipManagerImpl(getUserManager(), getSuggestedFriendsManager(), new CacheSettings(CachePolicy.NETWORK_ONLY, -1L), null, null, friendshipService, getExecutionService());
        }
        return this.friendshipManager;
    }

    public SuggestedFriendsManager getSuggestedFriendsManager() {
        if (this.suggestedFriendsManager == null) {
            SuggestedFriendsService suggestedFriendsService = new SuggestedFriendsService(getConnectionFactory(), getAuthenticationManager(), getUrlBuilder(), getParser(new SuggestedFriendsListJsonParser(getGsonSuggestedFriends())), getParser(new SuggestedFriendsJsonParser(getGsonSuggestedFriends())));
            this.suggestedFriendsManager = new SuggestedFriendsManagerImpl(new CacheSettings(CachePolicy.NETWORK_ONLY, -1L), this.memCache, null, suggestedFriendsService, getExecutionService(), getUserProfilePhotoManager());
        }
        return this.suggestedFriendsManager;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public CacheSettings getUserCacheSettings() {
        if (this.userCacheSettings == null) {
            this.userCacheSettings = new CacheSettings(CachePolicy.CACHE_ELSE_NETWORK, TimeUnit.DAYS.toMillis(1L));
        }
        return this.userCacheSettings;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public CacheSettings getActigraphyCacheSettings() {
        if (this.actigraphyCacheSettings == null) {
            this.actigraphyCacheSettings = new CacheSettings(CachePolicy.CACHE_ELSE_NETWORK, TimeUnit.DAYS.toMillis(1L));
        }
        return this.actigraphyCacheSettings;
    }

    public AuthenticationService getAuthenticationService() {
        if (this.authService == null) {
            this.authService = new AuthenticationServiceImpl(this.clientId, this.clientSecret, getConnectionFactory(), getUrlBuilder(), getOAuth2Parser(), this.context);
        }
        return this.authService;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public ExecutorService getExecutionService() {
        if (this.executorService == null) {
            this.executorService = new ScheduledThreadPoolExecutor(CORE_POOL_SIZE);
        }
        return this.executorService;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public SharedPreferences getSharedPreferences() {
        if (this.sharedPrefs == null) {
            this.sharedPrefs = this.context.getSharedPreferences(SHARED_PREFS, 0);
        }
        return this.sharedPrefs;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public AuthenticationManager getAuthenticationManager() {
        if (this.authManager == null) {
            AuthenticationManagerImpl authenticationManagerImpl = createAuthenticationManagerImpl();
            this.authManager = authenticationManagerImpl;
            authenticationManagerImpl.init(getAuthenticationService(), getExecutionService(), getSharedPreferences());
        }
        return this.authManager;
    }

    protected AuthenticationManagerImpl createAuthenticationManagerImpl() {
        return new AuthenticationManagerImpl();
    }

    private FilemobileCredentialManager getFilemobileCredentialManager() {
        if (this.filemobileCredentialManager == null) {
            FilemobileCredentialJsonParser jsonParser = new FilemobileCredentialJsonParser();
            FilemobileCredentialService service = new FilemobileCredentialService();
            FilemobileCredentialManagerImpl filemobileCredentialsManagerImpl = new FilemobileCredentialManagerImpl(service);
            this.filemobileCredentialManager = filemobileCredentialsManagerImpl;
            service.init(getConnectionFactory(), getUrlBuilder(), getParser(jsonParser), getAuthenticationManager());
        }
        return this.filemobileCredentialManager;
    }

    protected ConnectionFactory getConnectionFactory() {
        if (this.connectionFactory == null) {
            this.connectionFactory = new ConnectionFactory(this.context, this.clientId);
        }
        return this.connectionFactory;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public DiskCache<UserProfilePhoto> getUserProfilePhotoDiskCache() {
        if (this.profilePhotoDiskCache == null) {
            this.profilePhotoDiskCache = new UserProfilePhotoDatabase(this.context);
        }
        return this.profilePhotoDiskCache;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public UserProfilePhotoManager getUserProfilePhotoManager() {
        MediaService<UserProfilePhoto> mediaService = new MediaService<>(this.context, getConnectionFactory(), getUrlBuilder(), getParser(new UserProfilePhotoJsonParser(this.gson)), getAuthenticationManager());
        if (this.profilePhotoManager == null) {
            UserProfilePhotoService profilePhotoService = new UserProfilePhotoService(getConnectionFactory(), getAuthenticationManager(), getUrlBuilder(), getProfilePhotoParser());
            this.profilePhotoManager = new UserProfilePhotoManagerImpl(mediaService, new CacheSettings(CachePolicy.NETWORK_ONLY, 0L), this.memCache, null, profilePhotoService, getExecutionService(), getSharedPreferences());
        }
        return this.profilePhotoManager;
    }

    private RemoteConnectionTypeJsonParser getRemoteConnectionTypeParser() {
        if (this.remoteConnectionTypeJsonParser == null) {
            this.remoteConnectionTypeJsonParser = new RemoteConnectionTypeJsonParser(getGson());
        }
        return this.remoteConnectionTypeJsonParser;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public DiskCache<RemoteConnection> getRemoteConnectionDiskCache() {
        return new NullDiskCache();
    }

    @Override // com.ua.sdk.internal.UaProvider
    public RouteManager getRouteManager() {
        if (this.routeParser == null) {
            this.routeParser = getParser(new RouteJsonParser(getGson()));
        }
        if (this.routePageParser == null) {
            this.routePageParser = getParser(new RoutePageJsonParser(getGson()));
        }
        if (this.routeJsonWriter == null) {
            this.routeJsonWriter = getWriter(new RouteJsonWriter(getGson()));
        }
        if (this.routeManager == null) {
            RouteService routeService = new RouteService(getConnectionFactory(), getUrlBuilder(), getAuthenticationManager(), this.routeParser, this.routeJsonWriter, this.routePageParser);
            this.routeManager = new RouteManagerImpl(getUserCacheSettings(), null, null, routeService, getExecutionService(), getRouteBookmarkManager(), getUserManager());
        }
        return this.routeManager;
    }

    private RouteBookmarkManager getRouteBookmarkManager() {
        if (this.routeBookmarkManager == null) {
            RouteBookmarkService service = new RouteBookmarkService(getConnectionFactory(), getAuthenticationManager(), getUrlBuilder(), getParser(new RouteBookmarkJsonParser(getGson())), getWriter(new RouteBookmarkJsonWriter(getGson())), getParser(new RouteBookmarkListJsonParser(getGson())));
            this.routeBookmarkManager = new RouteBookmarkManagerImpl(new CacheSettings(CachePolicy.NETWORK_ONLY, -1L), null, null, service, getExecutionService());
        }
        return this.routeBookmarkManager;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public DiskCache<RemoteConnectionType> getRemoteConnectionTypeDiskCache() {
        return new NullDiskCache();
    }

    @Override // com.ua.sdk.internal.UaProvider
    public RemoteConnectionTypeManager getRemoteConnectionTypeManager() {
        if (this.remoteConnectionTypePageJsonParser == null) {
            this.remoteConnectionTypePageJsonParser = new RemoteConnectionTypePageJsonParser(getGson());
        }
        if (this.remoteConnectionTypeManager == null) {
            RemoteConnectionTypeService service = new RemoteConnectionTypeService(getConnectionFactory(), getUrlBuilder(), getAuthenticationManager(), getRemoteConnectionTypeParser(), this.remoteConnectionTypePageJsonParser);
            this.remoteConnectionTypeManager = new RemoteConnectionTypeManagerImpl(getActigraphySettingsManager(), getUserCacheSettings(), this.memCache, getRemoteConnectionTypeDiskCache(), service, getExecutionService());
        }
        return this.remoteConnectionTypeManager;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public RemoteConnectionManager getRemoteConnectionManager() {
        if (this.remoteConnectionJsonParser == null) {
            this.remoteConnectionJsonParser = new RemoteConnectionJsonParser(getGson());
        }
        if (this.remoteConnectionPageJsonParser == null) {
            this.remoteConnectionPageJsonParser = new RemoteConnectionPageJsonParser(getGson());
        }
        if (this.remoteConnectionManager == null) {
            RemoteConnectionService service = new RemoteConnectionService(getConnectionFactory(), getAuthenticationManager(), getUrlBuilder(), this.remoteConnectionJsonParser, this.remoteConnectionPageJsonParser);
            this.remoteConnectionManager = new RemoteConnectionManagerImpl(getUserCacheSettings(), this.memCache, getRemoteConnectionDiskCache(), service, getExecutionService());
        }
        return this.remoteConnectionManager;
    }

    private ActigraphySettingsManager getActigraphySettingsManager() {
        if (this.actigraphySettingsJsonParser == null) {
            this.actigraphySettingsJsonParser = new ActigraphySettingsJsonParser(getGson());
        }
        if (this.actigraphySettingsRequestWriter == null) {
            this.actigraphySettingsRequestWriter = new ActigraphySettingsRequestWriter();
        }
        if (this.actigraphySettingsManager == null) {
            ActigraphySettingsService service = new ActigraphySettingsService(getConnectionFactory(), getAuthenticationManager(), getUrlBuilder(), this.actigraphySettingsJsonParser, this.actigraphySettingsRequestWriter);
            this.actigraphySettingsManager = new ActigraphySettingsManagerImpl(getUserManager(), getUserCacheSettings(), null, null, service, getExecutionService());
            this.actigraphySettingsManager.setRemoteConnectionTypeManager(getRemoteConnectionTypeManager());
        }
        return this.actigraphySettingsManager;
    }

    private JsonParser<Page> getPageParser() {
        if (this.pageJsonParser == null) {
            this.pageJsonParser = new PageJsonParser(getGson());
        }
        return this.pageJsonParser;
    }

    private JsonParser<EntityList<Page>> getPagePageParser() {
        if (this.pagePageJsonParser == null) {
            this.pagePageJsonParser = new PageListJsonParser(getGson());
        }
        return this.pagePageJsonParser;
    }

    private DiskCache<Page> getPageDiskCache() {
        return new NullDiskCache();
    }

    private JsonParser<PageFollow> getPageFollowParser() {
        if (this.pageFollowParser == null) {
            this.pageFollowParser = new PageFollowJsonParser(getGson());
        }
        return this.pageFollowParser;
    }

    private JsonParser<EntityList<PageFollow>> getPageFollowPageParser() {
        if (this.pageFollowPageParser == null) {
            this.pageFollowPageParser = new PageFollowPageJsonParser(getGson());
        }
        return this.pageFollowPageParser;
    }

    private JsonWriter<PageFollow> getPageFollowRequestWriter() {
        if (this.pageFollowRequestWriter == null) {
            this.pageFollowRequestWriter = new PageFollowRequestJsonWriter(getGson());
        }
        return this.pageFollowRequestWriter;
    }

    private DiskCache<PageFollow> getPageFollowDiskCache() {
        return new NullDiskCache();
    }

    @Override // com.ua.sdk.internal.UaProvider
    public PageManager getPageSuperManager() {
        if (this.pageManager == null) {
            PageService pageService = new PageService(getConnectionFactory(), getAuthenticationManager(), getUrlBuilder(), getParser(getPageParser()), getParser(getPagePageParser()));
            PageManagerImpl pmi = new PageManagerImpl(getUserCacheSettings(), this.memCache, getPageDiskCache(), pageService, getExecutionService());
            PageFollowService pageFollowService = new PageFollowService(getConnectionFactory(), getAuthenticationManager(), getUrlBuilder(), getParser(getPageFollowParser()), getParser(getPageFollowPageParser()), getWriter(getPageFollowRequestWriter()));
            PageFollowManager pfm = new PageFollowManager(getUserCacheSettings(), this.memCache, getPageFollowDiskCache(), pageFollowService, getExecutionService());
            this.pageManager = new PageSuperManager(pmi, pfm);
        }
        return this.pageManager;
    }

    protected JsonParser<OAuth2Credentials> getOAuth2Parser() {
        if (this.oauth2Parser == null) {
            this.oauth2Parser = getParser(new OAuth2CredentialsParser(getGson()));
        }
        return this.oauth2Parser;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public ActivityStoryManager getActivityStoryManager() {
        if (this.activityStoryManager == null) {
            ActivityStoryService activityStoryService = new ActivityStoryService(getConnectionFactory(), getAuthenticationManager(), getUrlBuilder(), getParser(new ActivityStoryListJsonParser()), getParser(new ActivityStoryJsonParser()), getWriter(new ActivityStoryJsonWriter()));
            ActivityStoryJsonParser jsonParser = new ActivityStoryJsonParser();
            MediaService<ActivityStory> mediaService = new MediaService<>(this.context, getConnectionFactory(), getUrlBuilder(), getParser(jsonParser), getAuthenticationManager(), getFilemobileCredentialManager());
            this.activityStoryManager = new ActivityStoryManagerImpl(getUserManager(), getUserProfilePhotoManager(), mediaService, new CacheSettings(CachePolicy.NETWORK_ONLY, -1L), null, null, activityStoryService, getExecutionService());
        }
        return this.activityStoryManager;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public PageAssociationManager getPageAssociationManager() {
        if (this.pageAssociationManager == null) {
            PageAssociationService pageAssociationService = new PageAssociationService(getConnectionFactory(), getAuthenticationManager(), getUrlBuilder(), getParser(new PageAssociationJsonParser()), getParser(new PageAssociationListJsonParser()), getWriter(new PageAssociationRequestJsonWriter()));
            this.pageAssociationManager = new PageAssociationManagerImpl(new CacheSettings(CachePolicy.NETWORK_ONLY, -1L), null, null, pageAssociationService, getExecutionService());
        }
        return this.pageAssociationManager;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public ActivityTimeSeriesManager getActivityTimeSeriesManager() {
        if (this.activityTimeSeriesManager == null) {
            ActivityTimeSeriesService service = new ActivityTimeSeriesService(getConnectionFactory(), getAuthenticationManager(), getUrlBuilder(), getWriter(new ActivityTimeSeriesJsonWriter()), getParser(new ActivityTimeSeriesJsonParser()));
            this.activityTimeSeriesManager = new ActivityTimeSeriesManagerImpl(new CacheSettings(CachePolicy.NETWORK_ONLY, -1L), null, null, service, getExecutionService());
        }
        return this.activityTimeSeriesManager;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public UserStatsManager getUserStatsManager() {
        if (this.userStatsManager == null) {
            UserStatsService service = new UserStatsService(getConnectionFactory(), getAuthenticationManager(), getUrlBuilder(), getParser(new UserStatsJsonParser()));
            this.userStatsManager = new UserStatsManagerImpl(new CacheSettings(CachePolicy.NETWORK_ONLY, -1L), null, null, service, getExecutionService());
        }
        return this.userStatsManager;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public GearManager getGearManager() {
        if (this.gearManager == null) {
            GearService service = new GearService(getConnectionFactory(), getAuthenticationManager(), getUrlBuilder(), getParser(new GearListJsonParser()));
            this.gearManager = new GearManagerImpl(new CacheSettings(CachePolicy.NETWORK_ONLY, -1L), this.memCache, null, service, getExecutionService(), getGearBrandManager(), getUserGearManager());
        }
        return this.gearManager;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public GearBrandManager getGearBrandManager() {
        if (this.gearBrandManager == null) {
            GearBrandService service = new GearBrandService(getConnectionFactory(), getAuthenticationManager(), getUrlBuilder(), getParser(new GearBrandListJsonParser()));
            this.gearBrandManager = new GearBrandManagerImpl(new CacheSettings(CachePolicy.NETWORK_ONLY, -1L), this.memCache, null, service, getExecutionService());
        }
        return this.gearBrandManager;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public SleepManager getSleepManager() {
        if (this.sleepManager == null) {
            SleepService service = new SleepService(getConnectionFactory(), getAuthenticationManager(), getUrlBuilder(), getParser(new SleepMetricJsonParser()), getWriter(new SleepMetricJsonWriter()), getParser(new SleepMetricListJsonParser()));
            this.sleepManager = new SleepManagerImpl(new CacheSettings(CachePolicy.NETWORK_ONLY, -1L), null, null, service, getExecutionService());
        }
        return this.sleepManager;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public ActivityTypeManager getActivityTypeManager() {
        if (this.activityTypeManager == null) {
            ActivityTypeService service = new ActivityTypeService(getConnectionFactory(), getAuthenticationManager(), getUrlBuilder(), getParser(new ActivityTypeParser(getGson())), null, new ActivityTypeListJsonParser());
            this.activityTypeManager = new ActivityTypeManagerImpl(new CacheSettings(CachePolicy.CACHE_ELSE_NETWORK, 604800000L), this.memCache, ActivityTypeDatabase.getInstance(this.context), service, getExecutionService());
        }
        return this.activityTypeManager;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public UserGearManager getUserGearManager() {
        if (this.userGearManager == null) {
            UserGearService service = new UserGearService(getConnectionFactory(), getAuthenticationManager(), getUrlBuilder(), getParser(new UserGearListJsonParser()), getParser(new UserGearJsonParser()), getWriter(new UserGearJsonWriter()));
            this.userGearManager = new UserGearManagerImpl(new CacheSettings(CachePolicy.NETWORK_ONLY, -1L), this.memCache, null, service, getExecutionService());
        }
        return this.userGearManager;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public RoleSuperManager getRolesManager() {
        if (this.roleSuperManager == null) {
            this.roleSuperManager = new RoleSuperManagerImpl(getRoleManager(), getUserPermissionManager(), getUserRoleManager());
        }
        return this.roleSuperManager;
    }

    private RoleManager getRoleManager() {
        if (this.roleManager == null) {
            RoleService service = new RoleService(getConnectionFactory(), getAuthenticationManager(), getUrlBuilder(), getParser(new RoleJsonParser(getGson())), getParser(new RoleListJsonParser(getGson())));
            this.roleManager = new RoleManagerImpl(new CacheSettings(CachePolicy.NETWORK_ONLY, -1L), null, null, service, getExecutionService());
        }
        return this.roleManager;
    }

    private UserPermissionManager getUserPermissionManager() {
        if (this.userPermissionManager == null) {
            UserPermissionService service = new UserPermissionService(getConnectionFactory(), getAuthenticationManager(), getUrlBuilder(), getParser(new UserPermissionListJsonParser(getGson())));
            this.userPermissionManager = new UserPermissionManagerImpl(new CacheSettings(CachePolicy.NETWORK_ONLY, -1L), null, null, service, getExecutionService());
        }
        return this.userPermissionManager;
    }

    private UserRoleManager getUserRoleManager() {
        if (this.userRoleManager == null) {
            UserRoleService service = new UserRoleService(getConnectionFactory(), getAuthenticationManager(), getUrlBuilder(), getParser(new UserRoleJsonParser(getGson())), getWriter(new UserRoleJsonWriter(getGson())));
            this.userRoleManager = new UserRoleManagerImpl(new CacheSettings(CachePolicy.NETWORK_ONLY, -1L), null, null, service, getExecutionService());
        }
        return this.userRoleManager;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public CacheSettings getWorkoutCacheSettings() {
        if (this.workoutCacheSettings == null) {
            this.workoutCacheSettings = new CacheSettings(CachePolicy.CACHE_ELSE_NETWORK, TimeUnit.MINUTES.toMillis(4));
        }
        return this.workoutCacheSettings;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public WorkoutDatabase getWorkoutDiskCache() {
        if (this.workoutDatabase == null) {
            this.workoutDatabase = WorkoutDatabase.getInstance(this.context);
        }
        return this.workoutDatabase;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public WorkoutManager getWorkoutManager() {
        if (this.workoutManager == null) {
            WorkoutService service = new WorkoutService(getConnectionFactory(), getAuthenticationManager(), getUrlBuilder(), getParser(new WorkoutJsonParser()), getWriter(new WorkoutJsonWriter()), getParser(new WorkoutListParser()));
            MediaService<Workout> mediaService = new MediaService<>(this.context, getConnectionFactory(), getUrlBuilder(), getParser(new WorkoutJsonParser()), getAuthenticationManager(), getFilemobileCredentialManager());
            this.workoutManager = new WorkoutManagerImpl(getUserManager(), getWorkoutCacheSettings(), null, getWorkoutDiskCache(), service, getExecutionService(), mediaService);
        }
        return this.workoutManager;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public ModerationManager getModerationManager() {
        if (this.moderationManager == null) {
            ModerationActionService actionService = new ModerationActionService(getConnectionFactory(), getAuthenticationManager(), getUrlBuilder(), getParser(new ModerationActionParser()), getWriter(new ModerationActionWriter()));
            this.moderationManager = new ModerationManagerImpl(new ModerationActionManager(new CacheSettings(CachePolicy.NETWORK_ONLY, -1L), null, null, actionService, getExecutionService()));
        }
        return this.moderationManager;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public GroupManager getGroupManager() {
        if (this.groupManager == null) {
            GroupService service = new GroupService(getConnectionFactory(), getAuthenticationManager(), getUrlBuilder(), getParser(new GroupJsonParser()), getWriter(new GroupJsonWriter()), getParser(new GroupListJsonParser()));
            this.groupManager = new GroupManagerImpl(new CacheSettings(CachePolicy.NETWORK_ONLY, -1L), null, null, service, getExecutionService());
        }
        return this.groupManager;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public GroupInviteManager getGroupInviteManager() {
        if (this.groupInviteManager == null) {
            GroupInviteService service = new GroupInviteService(getConnectionFactory(), getAuthenticationManager(), getUrlBuilder(), getParser(new GroupInviteJsonParser()), getWriter(new GroupInviteJsonWriter()), getParser(new GroupInviteListJsonParser()));
            this.groupInviteManager = new GroupInviteManagerImpl(new CacheSettings(CachePolicy.NETWORK_ONLY, -1L), null, null, service, getExecutionService());
        }
        return this.groupInviteManager;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public GroupUserManager getGroupUserManager() {
        if (this.groupUserManager == null) {
            GroupUserService service = new GroupUserService(getConnectionFactory(), getAuthenticationManager(), getUrlBuilder(), getParser(new GroupUserJsonParser()), getWriter(new GroupUserJsonWriter()), getParser(new GroupUserListJsonParser()));
            this.groupUserManager = new GroupUserManagerImpl(new CacheSettings(CachePolicy.NETWORK_ONLY, -1L), null, null, service, getExecutionService());
        }
        return this.groupUserManager;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public GroupPurposeManager getGroupPurposeManager() {
        if (this.groupPurposeManager == null) {
            GroupPurposeService service = new GroupPurposeService(getConnectionFactory(), getAuthenticationManager(), getUrlBuilder(), getParser(new GroupPurposeJsonParser()), null, getParser(new GroupPurposeListJsonParser()));
            this.groupPurposeManager = new GroupPurposeManagerImpl(new CacheSettings(CachePolicy.NETWORK_ONLY, -1L), null, null, service, getExecutionService());
        }
        return this.groupPurposeManager;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public GroupObjectiveManager getGroupObjectiveManager() {
        if (this.groupObjectiveManager == null) {
            GroupObjectiveService service = new GroupObjectiveService(getConnectionFactory(), getAuthenticationManager(), getUrlBuilder(), getParser(new GroupObjectiveJsonParser()), getWriter(new GroupObjectiveJsonWriter()), getParser(new GroupObjectiveListJsonParser()));
            this.groupObjectiveManager = new GroupObjectiveManagerImpl(new CacheSettings(CachePolicy.NETWORK_ONLY, -1L), null, null, service, getExecutionService());
        }
        return this.groupObjectiveManager;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public GroupLeaderboardManager getGroupLeaderboardManager() {
        if (this.groupLeaderboardManager == null) {
            GroupLeaderboardService service = new GroupLeaderboardService(getConnectionFactory(), getAuthenticationManager(), getUrlBuilder(), getParser(new GroupLeaderboardJsonParser()), null, getParser(new GroupLeaderboardListJsonParser()));
            this.groupLeaderboardManager = new GroupLeaderboardManagerImpl(new CacheSettings(CachePolicy.NETWORK_ONLY, -1L), null, null, service, getExecutionService());
        }
        return this.groupLeaderboardManager;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public BodyMassManager getBodyMassManager() {
        if (this.bodyMassManager == null) {
            BodyMassService service = new BodyMassService(getConnectionFactory(), getAuthenticationManager(), getUrlBuilder(), getParser(new BodyMassJsonParser()), getWriter(new BodyMassJsonWriter()), getParser(new BodyMassListJsonParser()));
            this.bodyMassManager = new BodyMassManagerImpl(new CacheSettings(CachePolicy.NETWORK_ONLY, -1L), this.memCache, new NullDiskCache(), service, getExecutionService());
        }
        return this.bodyMassManager;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public AggregateManager getAggregateManager() {
        if (this.aggregateManager == null) {
            AggregateService service = new AggregateService(getConnectionFactory(), getAuthenticationManager(), getUrlBuilder(), getParser(new AggregateJsonParser()), null, getParser(new AggregateListJsonParser()));
            this.aggregateManager = new AggregateManagerImpl(new CacheSettings(CachePolicy.NETWORK_ONLY, -1L), this.memCache, new NullDiskCache(), service, getExecutionService());
        }
        return this.aggregateManager;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public MetabolicEnergyCalculator getMetabolicEnergyCalculator() {
        return new MetabolicEnergyCalculatorImpl();
    }

    @Override // com.ua.sdk.internal.UaProvider
    public HeartRateZonesManager getHeartRateZonesManager() {
        if (this.heartRateZonesManager == null) {
            HeartRateZonesService service = new HeartRateZonesService(getConnectionFactory(), getAuthenticationManager(), getUrlBuilder(), getParser(new HeartRateZonesJsonParser()), getWriter(new HeartRateZonesJsonWriter()), getParser(new HeartRateZonesListJsonParser()));
            this.heartRateZonesManager = new HeartRateZonesManagerImpl(new CacheSettings(CachePolicy.NETWORK_ONLY, -1L), this.memCache, new NullDiskCache(), service, getExecutionService());
        }
        return this.heartRateZonesManager;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public RecorderConfigurationDatabase getRecorderConfigurationCache() {
        if (this.recorderConfigurationCache == null) {
            this.recorderConfigurationCache = RecorderConfigurationDatabase.getInstance(this.context);
        }
        return this.recorderConfigurationCache;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public RecorderManager getRecorderManager() {
        if (this.recorderManager == null) {
            this.recorderManager = new RecorderManagerImpl(this.context, getExecutionService(), getUserManager(), getActivityTypeManager(), getHeartRateZonesManager(), getMetabolicEnergyCalculator(), getIntensityCalculator(), getRecorderConfigurationCache());
        }
        return this.recorderManager;
    }

    @Override // com.ua.sdk.internal.UaProvider
    public IntensityCalculator getIntensityCalculator() {
        return new IntensityCalculatorImpl();
    }

    @Override // com.ua.sdk.internal.UaProvider
    public TimeSource getTimeSource() {
        return new SystemTimeSource();
    }
}
