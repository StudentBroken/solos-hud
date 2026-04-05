package com.ua.sdk.user;

import com.ua.sdk.Entity;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.ImageUrl;
import com.ua.sdk.LocalDate;
import com.ua.sdk.MeasurementSystem;
import com.ua.sdk.friendship.Friendship;
import com.ua.sdk.location.Location;
import com.ua.sdk.page.follow.PageFollow;
import com.ua.sdk.privacy.Privacy;
import com.ua.sdk.user.stats.UserStats;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public interface User extends Entity<EntityRef> {
    Privacy getActivityFeedPrivacy();

    LocalDate getBirthdate();

    Privacy getBodyMassPrivacy();

    UserCommunication getCommunication();

    Date getDateJoined();

    MeasurementSystem getDisplayMeasurementSystem();

    String getDisplayName();

    String getEmail();

    Privacy getEmailSearchPrivacy();

    String getFirstName();

    EntityListRef<PageFollow> getFollowingRef();

    Privacy getFoodLogPrivacy();

    EntityListRef<Friendship> getFriendships();

    Gender getGender();

    String getGoalStatement();

    Double getHeight();

    String getHobbies();

    String getId();

    String getIntroduction();

    String getLastInitial();

    Date getLastLogin();

    String getLastName();

    Location getLocation();

    Privacy getProfilePrivacy();

    String getProfileStatement();

    @Override // com.ua.sdk.Entity, com.ua.sdk.Resource
    EntityRef<User> getRef();

    Privacy getRoutePrivacy();

    UserSharing getSharing();

    Privacy getSleepPrivacy();

    EntityRef<UserStats> getStatsByDay();

    EntityRef<UserStats> getStatsByLifetime();

    EntityRef<UserStats> getStatsByMonth();

    EntityRef<UserStats> getStatsByWeek();

    EntityRef<UserStats> getStatsByYear();

    String getTimeZone();

    ImageUrl getUserProfilePhoto();

    String getUsername();

    Double getWeight();

    Privacy getWorkoutPrivacy();

    void setActivityFeedPrivacy(Privacy.Level level);

    void setBirthdate(LocalDate localDate);

    void setBodyMassPrivacy(Privacy.Level level);

    void setCommunication(UserCommunication userCommunication);

    void setDateJoined(Date date);

    void setDisplayMeasurementSystem(MeasurementSystem measurementSystem);

    void setDisplayName(String str);

    void setEmail(String str);

    void setEmailSearchPrivacy(Privacy.Level level);

    void setFirstName(String str);

    void setFoodLogPrivacy(Privacy.Level level);

    void setGender(Gender gender);

    void setGoalStatement(String str);

    void setHeight(Double d);

    void setHobbies(String str);

    void setIntroduction(String str);

    void setLastInitial(String str);

    void setLastLogin(Date date);

    void setLastName(String str);

    void setLocation(Location location);

    void setProfilePrivacy(Privacy.Level level);

    void setProfileStatement(String str);

    void setRoutePrivacy(Privacy.Level level);

    void setSharing(UserSharing userSharing);

    void setSleepPrivacy(Privacy.Level level);

    void setTimeZone(String str);

    void setUserProfilePhoto(ImageUrl imageUrl);

    void setUsername(String str);

    void setWeight(Double d);

    void setWorkoutPrivacy(Privacy.Level level);
}
