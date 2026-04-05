package com.ua.sdk.user;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.ImageUrl;
import com.ua.sdk.LocalDate;
import com.ua.sdk.MeasurementSystem;
import com.ua.sdk.UaLog;
import com.ua.sdk.authentication.OAuth2Credentials;
import com.ua.sdk.authentication.OAuth2CredentialsImpl;
import com.ua.sdk.friendship.FriendshipListRef;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.ImageUrlImpl;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.LinkEntityRef;
import com.ua.sdk.location.Location;
import com.ua.sdk.location.LocationImpl;
import com.ua.sdk.page.follow.PageFollow;
import com.ua.sdk.page.follow.PageFollowListRef;
import com.ua.sdk.privacy.Privacy;
import com.ua.sdk.privacy.PrivacyHelper;
import com.ua.sdk.user.stats.UserStatsRef;
import java.util.Date;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class UserImpl extends ApiTransferObject implements User, Parcelable {
    public static Parcelable.Creator<UserImpl> CREATOR = new Parcelable.Creator<UserImpl>() { // from class: com.ua.sdk.user.UserImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserImpl createFromParcel(Parcel source) {
            return new UserImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserImpl[] newArray(int size) {
            return new UserImpl[size];
        }
    };
    protected static final String NAME_ACTIVITY_FEED = "activity_feed";
    protected static final String NAME_BODY_MASS = "bodymass";
    protected static final String NAME_EMAIL_SEARCH = "email_search";
    protected static final String NAME_FOOD_LOG = "food_log";
    protected static final String NAME_PROFILE = "profile";
    protected static final String NAME_ROUTE = "route";
    protected static final String NAME_SLEEP = "sleep";
    protected static final String NAME_WORKOUT = "workout";
    protected static final String REF_DEACTIVATION = "deactivation";
    protected static final String REF_DOCUMENTATION = "documentation";
    protected static final String REF_FRIENDSHIPS = "friendships";
    protected static final String REF_IMAGE = "image";
    protected static final String REF_PRIVACY = "privacy";
    protected static final String REF_STATS = "stats";
    protected static final String REF_WORKOUTS = "workouts";
    private transient Privacy activityFeedPrivacy;
    private LocalDate birthdate;
    private transient Privacy bodyMassPrivacy;
    private UserCommunication communication;
    private Date dateJoined;
    private MeasurementSystem displayMeasurementSystem;
    private String displayName;
    private String email;
    private transient Privacy emailSearchPrivacy;
    private String firstName;
    private transient EntityListRef<PageFollow> followingRef;
    private transient Privacy foodLogPrivacy;
    private transient FriendshipListRef friendships;
    private Gender gender;
    private String goalStatement;
    private Double height;
    private String hobbies;
    private String id;
    private String introduction;
    private String lastInitial;
    private Date lastLogin;
    private String lastName;
    private Location location;
    private UserObjectState myState;
    private OAuth2Credentials oAuth2Credentials;
    private transient String password;
    private transient Privacy profilePrivacy;
    private String profileStatement;
    private transient Privacy routePrivacy;
    private UserSharing sharing;
    private transient Privacy sleepPrivacy;
    private String timeZone;
    private ImageUrlImpl userProfilePhoto;
    private String username;
    private Double weight;
    private transient Privacy workoutPrivacy;

    public UserImpl() {
        this.oAuth2Credentials = new OAuth2CredentialsImpl();
        this.goalStatement = "";
        this.communication = new UserCommunicationImpl();
        this.sharing = new UserSharingImpl();
        this.location = new LocationImpl();
        this.userProfilePhoto = new ImageUrlImpl();
        this.myState = UserObjectState.FULL;
    }

    @Override // com.ua.sdk.user.User
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override // com.ua.sdk.user.User
    public String getUsername() {
        return this.username;
    }

    @Override // com.ua.sdk.user.User
    public void setUsername(String username) {
        this.username = username;
    }

    @Override // com.ua.sdk.user.User
    public String getEmail() {
        return this.email;
    }

    @Override // com.ua.sdk.user.User
    public void setEmail(String email) {
        this.email = email;
    }

    public OAuth2Credentials getOauth2Credentials() {
        return this.oAuth2Credentials;
    }

    public void setOauth2Credentials(OAuth2Credentials oAuth2Credentials) {
        this.oAuth2Credentials = oAuth2Credentials;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override // com.ua.sdk.user.User
    public String getFirstName() {
        return this.firstName;
    }

    @Override // com.ua.sdk.user.User
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override // com.ua.sdk.user.User
    public String getLastName() {
        return this.lastName;
    }

    @Override // com.ua.sdk.user.User
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override // com.ua.sdk.user.User
    public String getLastInitial() {
        return this.lastInitial;
    }

    @Override // com.ua.sdk.user.User
    public void setLastInitial(String lastInitial) {
        this.lastInitial = lastInitial;
    }

    @Override // com.ua.sdk.user.User
    public String getDisplayName() {
        return this.displayName;
    }

    @Override // com.ua.sdk.user.User
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override // com.ua.sdk.user.User
    public LocalDate getBirthdate() {
        return this.birthdate;
    }

    @Override // com.ua.sdk.user.User
    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    @Override // com.ua.sdk.user.User
    public Gender getGender() {
        return this.gender;
    }

    @Override // com.ua.sdk.user.User
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override // com.ua.sdk.user.User
    public Double getHeight() {
        return this.height;
    }

    @Override // com.ua.sdk.user.User
    public void setHeight(Double height) {
        this.height = height;
    }

    @Override // com.ua.sdk.user.User
    public Double getWeight() {
        return this.weight;
    }

    @Override // com.ua.sdk.user.User
    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @Override // com.ua.sdk.user.User
    public String getTimeZone() {
        return this.timeZone;
    }

    @Override // com.ua.sdk.user.User
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    @Override // com.ua.sdk.user.User
    public Date getDateJoined() {
        return this.dateJoined;
    }

    @Override // com.ua.sdk.user.User
    public void setDateJoined(Date dateJoined) {
        this.dateJoined = dateJoined;
    }

    @Override // com.ua.sdk.user.User
    public Date getLastLogin() {
        return this.lastLogin;
    }

    @Override // com.ua.sdk.user.User
    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override // com.ua.sdk.user.User
    public MeasurementSystem getDisplayMeasurementSystem() {
        return this.displayMeasurementSystem;
    }

    @Override // com.ua.sdk.user.User
    public void setDisplayMeasurementSystem(MeasurementSystem displayMeasurementSystem) {
        this.displayMeasurementSystem = displayMeasurementSystem;
    }

    @Override // com.ua.sdk.user.User
    public UserCommunication getCommunication() {
        return this.communication;
    }

    @Override // com.ua.sdk.user.User
    public void setCommunication(UserCommunication communication) {
        this.communication = communication;
    }

    @Override // com.ua.sdk.user.User
    public UserSharing getSharing() {
        return this.sharing;
    }

    @Override // com.ua.sdk.user.User
    public void setSharing(UserSharing sharing) {
        this.sharing = sharing;
    }

    @Override // com.ua.sdk.user.User
    public Location getLocation() {
        return this.location;
    }

    @Override // com.ua.sdk.user.User
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override // com.ua.sdk.user.User
    public void setProfilePrivacy(Privacy.Level profilePrivacy) {
        this.profilePrivacy = PrivacyHelper.getPrivacy(profilePrivacy);
        updatePrivacyLink("profile", profilePrivacy);
    }

    @Override // com.ua.sdk.user.User
    public void setWorkoutPrivacy(Privacy.Level workoutPrivacy) {
        this.workoutPrivacy = PrivacyHelper.getPrivacy(workoutPrivacy);
        updatePrivacyLink("workout", workoutPrivacy);
    }

    @Override // com.ua.sdk.user.User
    public void setActivityFeedPrivacy(Privacy.Level activityFeedPrivacy) {
        this.activityFeedPrivacy = PrivacyHelper.getPrivacy(activityFeedPrivacy);
        updatePrivacyLink(NAME_ACTIVITY_FEED, activityFeedPrivacy);
    }

    @Override // com.ua.sdk.user.User
    public void setFoodLogPrivacy(Privacy.Level foodLogPrivacy) {
        this.foodLogPrivacy = PrivacyHelper.getPrivacy(foodLogPrivacy);
        updatePrivacyLink(NAME_FOOD_LOG, foodLogPrivacy);
    }

    @Override // com.ua.sdk.user.User
    public void setEmailSearchPrivacy(Privacy.Level emailSearchPrivacy) {
        this.emailSearchPrivacy = PrivacyHelper.getPrivacy(emailSearchPrivacy);
        updatePrivacyLink(NAME_EMAIL_SEARCH, emailSearchPrivacy);
    }

    @Override // com.ua.sdk.user.User
    public void setRoutePrivacy(Privacy.Level routePrivacy) {
        this.routePrivacy = PrivacyHelper.getPrivacy(routePrivacy);
        updatePrivacyLink(NAME_ROUTE, routePrivacy);
    }

    @Override // com.ua.sdk.user.User
    public void setSleepPrivacy(Privacy.Level sleepPrivacy) {
        this.sleepPrivacy = PrivacyHelper.getPrivacy(sleepPrivacy);
        updatePrivacyLink(NAME_SLEEP, sleepPrivacy);
    }

    @Override // com.ua.sdk.user.User
    public void setBodyMassPrivacy(Privacy.Level bodyMassPrivacy) {
        this.bodyMassPrivacy = PrivacyHelper.getPrivacy(bodyMassPrivacy);
        updatePrivacyLink(NAME_BODY_MASS, bodyMassPrivacy);
    }

    @Override // com.ua.sdk.user.User
    public String getIntroduction() {
        return this.introduction;
    }

    @Override // com.ua.sdk.user.User
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Override // com.ua.sdk.user.User
    public String getHobbies() {
        return this.hobbies;
    }

    @Override // com.ua.sdk.user.User
    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    @Override // com.ua.sdk.user.User
    public String getGoalStatement() {
        return this.goalStatement;
    }

    @Override // com.ua.sdk.user.User
    public void setGoalStatement(String goalStatement) {
        this.goalStatement = goalStatement;
    }

    @Override // com.ua.sdk.user.User
    public String getProfileStatement() {
        return this.profileStatement;
    }

    @Override // com.ua.sdk.user.User
    public void setProfileStatement(String profileStatement) {
        this.profileStatement = profileStatement;
    }

    @Override // com.ua.sdk.Resource
    public EntityRef getRef() {
        return new LinkEntityRef(this.id, this.mLocalId, getHref());
    }

    @Override // com.ua.sdk.user.User
    public ImageUrlImpl getUserProfilePhoto() {
        return this.userProfilePhoto;
    }

    @Override // com.ua.sdk.user.User
    public void setUserProfilePhoto(ImageUrl userProfilePhoto) {
        this.userProfilePhoto = (ImageUrlImpl) userProfilePhoto;
    }

    @Override // com.ua.sdk.user.User
    public FriendshipListRef getFriendships() {
        return FriendshipListRef.getBuilder().setHref(getLink("friendships").getHref()).build();
    }

    @Override // com.ua.sdk.user.User
    public UserStatsRef getStatsByDay() {
        return UserStatsRef.getBuilder().setUser(getRef()).setAggregatePeriodUserStats(UserStatsRef.AggregatePeriodUserStats.DAY).build();
    }

    @Override // com.ua.sdk.user.User
    public UserStatsRef getStatsByWeek() {
        return UserStatsRef.getBuilder().setUser(getRef()).setAggregatePeriodUserStats(UserStatsRef.AggregatePeriodUserStats.WEEK).build();
    }

    @Override // com.ua.sdk.user.User
    public UserStatsRef getStatsByMonth() {
        return UserStatsRef.getBuilder().setUser(getRef()).setAggregatePeriodUserStats(UserStatsRef.AggregatePeriodUserStats.MONTH).build();
    }

    @Override // com.ua.sdk.user.User
    public UserStatsRef getStatsByYear() {
        return UserStatsRef.getBuilder().setUser(getRef()).setAggregatePeriodUserStats(UserStatsRef.AggregatePeriodUserStats.YEAR).build();
    }

    @Override // com.ua.sdk.user.User
    public UserStatsRef getStatsByLifetime() {
        return UserStatsRef.getBuilder().setUser(getRef()).setAggregatePeriodUserStats(UserStatsRef.AggregatePeriodUserStats.DAY).build();
    }

    private Privacy getPrivacy(String name) {
        Link link = getLink("privacy", name);
        if (link == null) {
            return null;
        }
        try {
            int id = Integer.parseInt(link.getId());
            return PrivacyHelper.getPrivacyFromId(id);
        } catch (NumberFormatException e) {
            UaLog.error("Unable to get privacy.", (Throwable) e);
            return null;
        }
    }

    private void updatePrivacyLink(String name, Privacy.Level level) {
        List<Link> links = getLinks("privacy");
        if (links != null) {
            Link updatedPrivacy = PrivacyHelper.toLink(level, name);
            boolean addLink = true;
            for (int i = 0; i < links.size(); i++) {
                if (links.get(i).getName().equals(name)) {
                    links.set(i, updatedPrivacy);
                    addLink = false;
                }
            }
            if (addLink) {
                links.add(updatedPrivacy);
            }
        }
    }

    @Override // com.ua.sdk.user.User
    public Privacy getProfilePrivacy() {
        if (this.profilePrivacy == null) {
            this.profilePrivacy = getPrivacy("profile");
        }
        return this.profilePrivacy;
    }

    @Override // com.ua.sdk.user.User
    public Privacy getWorkoutPrivacy() {
        if (this.workoutPrivacy == null) {
            this.workoutPrivacy = getPrivacy("workout");
        }
        return this.workoutPrivacy;
    }

    @Override // com.ua.sdk.user.User
    public Privacy getActivityFeedPrivacy() {
        if (this.activityFeedPrivacy == null) {
            this.activityFeedPrivacy = getPrivacy(NAME_ACTIVITY_FEED);
        }
        return this.activityFeedPrivacy;
    }

    @Override // com.ua.sdk.user.User
    public Privacy getFoodLogPrivacy() {
        if (this.foodLogPrivacy == null) {
            this.foodLogPrivacy = getPrivacy(NAME_FOOD_LOG);
        }
        return this.foodLogPrivacy;
    }

    @Override // com.ua.sdk.user.User
    public Privacy getEmailSearchPrivacy() {
        if (this.emailSearchPrivacy == null) {
            this.emailSearchPrivacy = getPrivacy(NAME_EMAIL_SEARCH);
        }
        return this.emailSearchPrivacy;
    }

    @Override // com.ua.sdk.user.User
    public Privacy getRoutePrivacy() {
        if (this.routePrivacy == null) {
            this.routePrivacy = getPrivacy(NAME_ROUTE);
        }
        return this.routePrivacy;
    }

    @Override // com.ua.sdk.user.User
    public Privacy getBodyMassPrivacy() {
        if (this.bodyMassPrivacy == null) {
            this.bodyMassPrivacy = getPrivacy(NAME_BODY_MASS);
        }
        return this.bodyMassPrivacy;
    }

    @Override // com.ua.sdk.user.User
    public Privacy getSleepPrivacy() {
        if (this.sleepPrivacy == null) {
            this.sleepPrivacy = getPrivacy(NAME_SLEEP);
        }
        return this.sleepPrivacy;
    }

    @Override // com.ua.sdk.user.User
    public EntityListRef<PageFollow> getFollowingRef() {
        if (this.followingRef == null) {
            this.followingRef = PageFollowListRef.getBuilder().setUserId(this.id).build();
        }
        return this.followingRef;
    }

    public UserObjectState getObjectState() {
        return this.myState;
    }

    public void setObjectState(UserObjectState state) {
        this.myState = state;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.id);
        dest.writeString(this.username);
        dest.writeString(this.email);
        dest.writeParcelable(this.oAuth2Credentials, 0);
        dest.writeString(this.password);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.lastInitial);
        dest.writeString(this.displayName);
        dest.writeString(this.introduction);
        dest.writeString(this.hobbies);
        dest.writeString(this.goalStatement);
        dest.writeString(this.profileStatement);
        dest.writeParcelable(this.birthdate, 0);
        dest.writeInt(this.gender == null ? -1 : this.gender.ordinal());
        dest.writeValue(this.height);
        dest.writeValue(this.weight);
        dest.writeString(this.timeZone);
        dest.writeLong(this.dateJoined != null ? this.dateJoined.getTime() : -1L);
        dest.writeLong(this.lastLogin != null ? this.lastLogin.getTime() : -1L);
        dest.writeInt(this.displayMeasurementSystem == null ? -1 : this.displayMeasurementSystem.ordinal());
        dest.writeParcelable(this.communication, 0);
        dest.writeParcelable(this.sharing, 0);
        dest.writeParcelable(this.location, 0);
        dest.writeParcelable(this.userProfilePhoto, 0);
        dest.writeParcelable(this.profilePrivacy, flags);
        dest.writeParcelable(this.workoutPrivacy, flags);
        dest.writeParcelable(this.activityFeedPrivacy, flags);
        dest.writeParcelable(this.foodLogPrivacy, flags);
        dest.writeParcelable(this.emailSearchPrivacy, flags);
        dest.writeParcelable(this.routePrivacy, flags);
        dest.writeParcelable(this.sleepPrivacy, flags);
        dest.writeParcelable(this.bodyMassPrivacy, flags);
        dest.writeParcelable(this.friendships, 0);
        dest.writeInt(this.myState != null ? this.myState.ordinal() : -1);
    }

    private UserImpl(Parcel in) {
        super(in);
        this.oAuth2Credentials = new OAuth2CredentialsImpl();
        this.goalStatement = "";
        this.communication = new UserCommunicationImpl();
        this.sharing = new UserSharingImpl();
        this.location = new LocationImpl();
        this.userProfilePhoto = new ImageUrlImpl();
        this.myState = UserObjectState.FULL;
        this.id = in.readString();
        this.username = in.readString();
        this.email = in.readString();
        this.oAuth2Credentials = (OAuth2Credentials) in.readParcelable(OAuth2Credentials.class.getClassLoader());
        this.password = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.lastInitial = in.readString();
        this.displayName = in.readString();
        this.introduction = in.readString();
        this.hobbies = in.readString();
        this.goalStatement = in.readString();
        this.profileStatement = in.readString();
        this.birthdate = (LocalDate) in.readParcelable(LocalDate.class.getClassLoader());
        int tmpGender = in.readInt();
        this.gender = tmpGender == -1 ? null : Gender.values()[tmpGender];
        this.height = (Double) in.readValue(Double.class.getClassLoader());
        this.weight = (Double) in.readValue(Double.class.getClassLoader());
        this.timeZone = in.readString();
        long tmpDateJoined = in.readLong();
        this.dateJoined = tmpDateJoined == -1 ? null : new Date(tmpDateJoined);
        long tmpLastLogin = in.readLong();
        this.lastLogin = tmpLastLogin == -1 ? null : new Date(tmpLastLogin);
        int tmpDisplayMeasurementSystem = in.readInt();
        this.displayMeasurementSystem = tmpDisplayMeasurementSystem == -1 ? null : MeasurementSystem.values()[tmpDisplayMeasurementSystem];
        this.communication = (UserCommunication) in.readParcelable(UserCommunication.class.getClassLoader());
        this.sharing = (UserSharing) in.readParcelable(UserSharing.class.getClassLoader());
        this.location = (Location) in.readParcelable(Location.class.getClassLoader());
        this.userProfilePhoto = (ImageUrlImpl) in.readParcelable(ImageUrl.class.getClassLoader());
        this.profilePrivacy = (Privacy) in.readParcelable(Privacy.class.getClassLoader());
        this.workoutPrivacy = (Privacy) in.readParcelable(Privacy.class.getClassLoader());
        this.activityFeedPrivacy = (Privacy) in.readParcelable(Privacy.class.getClassLoader());
        this.foodLogPrivacy = (Privacy) in.readParcelable(Privacy.class.getClassLoader());
        this.emailSearchPrivacy = (Privacy) in.readParcelable(Privacy.class.getClassLoader());
        this.routePrivacy = (Privacy) in.readParcelable(Privacy.class.getClassLoader());
        this.sleepPrivacy = (Privacy) in.readParcelable(Privacy.class.getClassLoader());
        this.bodyMassPrivacy = (Privacy) in.readParcelable(Privacy.class.getClassLoader());
        this.friendships = (FriendshipListRef) in.readParcelable(FriendshipListRef.class.getClassLoader());
        int tmpMyState = in.readInt();
        this.myState = tmpMyState != -1 ? UserObjectState.values()[tmpMyState] : null;
    }
}
