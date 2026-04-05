package com.ua.sdk.user;

import com.facebook.AccessToken;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.LocalDate;
import com.ua.sdk.MeasurementSystem;
import com.ua.sdk.authentication.OAuth2Credentials;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.location.LocationImpl;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public class UserTO extends ApiTransferObject {

    @SerializedName("access_token")
    String accessToken;

    @SerializedName("birthdate")
    LocalDate birthdate;

    @SerializedName("communication")
    UserCommunicationImpl communication;

    @SerializedName("date_joined")
    Date dateJoined;

    @SerializedName("display_measurement_system")
    MeasurementSystem displayMeasurementSystem;

    @SerializedName("display_name")
    String displayName;

    @SerializedName("email")
    String email;

    @SerializedName(AccessToken.EXPIRES_IN_KEY)
    Long expiresIn;

    @SerializedName("first_name")
    String firstName;

    @SerializedName("gender")
    Gender gender;

    @SerializedName("goal_statement")
    String goalStatement;

    @SerializedName("height")
    Double height;

    @SerializedName("hobbies")
    String hobbies;

    @SerializedName("id")
    String id;

    @SerializedName("introduction")
    String introduction;

    @SerializedName("last_initial")
    String lastInitial;

    @SerializedName("last_login")
    Date lastLogin;

    @SerializedName("last_name")
    String lastName;

    @SerializedName("location")
    LocationImpl location;

    @SerializedName("password")
    String password;

    @SerializedName("profile_statement")
    String profileStatment;

    @SerializedName("refresh_token")
    String refreshToken;

    @SerializedName("sharing")
    UserSharingImpl sharing;

    @SerializedName("time_zone")
    String timeZone;

    @SerializedName("username")
    String username;

    @SerializedName("weight")
    Double weight;

    public static UserTO toTransferObject(User user) {
        if (user == null) {
            return null;
        }
        UserImpl userImpl = (UserImpl) user;
        UserTO to = new UserTO();
        to.id = user.getId();
        to.username = user.getUsername();
        to.email = user.getEmail();
        to.password = userImpl.getPassword();
        to.firstName = user.getFirstName();
        to.lastName = user.getLastName();
        to.lastInitial = user.getLastInitial();
        to.displayName = user.getDisplayName();
        to.introduction = user.getIntroduction();
        to.hobbies = user.getHobbies();
        to.goalStatement = user.getGoalStatement();
        to.profileStatment = user.getProfileStatement();
        to.birthdate = user.getBirthdate();
        to.gender = user.getGender();
        to.height = user.getHeight();
        to.weight = user.getWeight();
        to.timeZone = user.getTimeZone();
        to.dateJoined = user.getDateJoined();
        to.lastLogin = user.getLastLogin();
        to.displayMeasurementSystem = user.getDisplayMeasurementSystem();
        to.communication = (UserCommunicationImpl) userImpl.getCommunication();
        to.sharing = (UserSharingImpl) userImpl.getSharing();
        to.location = (LocationImpl) userImpl.getLocation();
        to.setLinkMap(userImpl.getLinkMap());
        return to;
    }

    public static UserImpl fromTransferObject(UserTO from) {
        if (from == null) {
            return null;
        }
        UserImpl answer = new UserImpl();
        answer.setId(from.id);
        answer.setUsername(from.username);
        answer.setEmail(from.email);
        OAuth2Credentials oAuth2Token = answer.getOauth2Credentials();
        oAuth2Token.setAccessToken(from.accessToken);
        oAuth2Token.setExpiresAt(from.expiresIn != null ? Long.valueOf(System.currentTimeMillis() + (from.expiresIn.longValue() * 1000)) : null);
        oAuth2Token.setRefreshToken(from.refreshToken);
        answer.setFirstName(from.firstName);
        answer.setLastName(from.lastName);
        answer.setLastInitial(from.lastInitial);
        answer.setDisplayName(from.displayName);
        answer.setIntroduction(from.introduction);
        answer.setHobbies(from.hobbies);
        answer.setGoalStatement(from.goalStatement);
        answer.setProfileStatement(from.profileStatment);
        answer.setBirthdate(from.birthdate);
        answer.setGender(from.gender);
        answer.setHeight(from.height);
        answer.setWeight(from.weight);
        answer.setTimeZone(from.timeZone);
        answer.setDateJoined(from.dateJoined);
        answer.setLastLogin(from.lastLogin);
        answer.setDisplayMeasurementSystem(from.displayMeasurementSystem);
        answer.setCommunication(from.communication);
        answer.setSharing(from.sharing);
        answer.setLocation(from.location);
        for (String key : from.getLinkKeys()) {
            answer.setLinksForRelation(key, from.getLinks(key));
        }
        return answer;
    }
}
