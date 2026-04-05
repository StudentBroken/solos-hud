package com.ua.sdk.user;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.ua.sdk.LocalDate;
import com.ua.sdk.MeasurementSystem;
import com.ua.sdk.cache.database.LegacyEntityDatabase;
import com.ua.sdk.cache.database.definition.BooleanColumnDefinition;
import com.ua.sdk.cache.database.definition.ColumnDefinition;
import com.ua.sdk.cache.database.definition.DateColumnDefinition;
import com.ua.sdk.cache.database.definition.DoubleColumnDefinition;
import com.ua.sdk.cache.database.definition.EnumColumnDefinition;
import com.ua.sdk.cache.database.definition.LocalDateColumnDefinition;
import com.ua.sdk.cache.database.definition.LocalIdColumnDefinition;
import com.ua.sdk.cache.database.definition.StringColumnDefinition;
import com.ua.sdk.internal.ImageUrlImpl;
import com.ua.sdk.location.Location;
import com.ua.sdk.location.LocationImpl;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public class UserDatabase extends LegacyEntityDatabase<User> {
    private static final String USER_DATABASE_NAME = "mmdk_user";
    private static final int USER_DATABASE_VERSION = 4;
    private static final String USER_TABLE = "user";
    private static UserDatabase mInstance = null;
    public static final ColumnDefinition<Long> LOCAL_ID = new LocalIdColumnDefinition(0, "_id");
    public static final ColumnDefinition<String> ID = new StringColumnDefinition(1, "id");
    public static final ColumnDefinition<String> USERNAME = new StringColumnDefinition(2, "username");
    public static final ColumnDefinition<String> EMAIL = new StringColumnDefinition(3, "email");
    public static final ColumnDefinition<String> FIRST_NAME = new StringColumnDefinition(4, "first_name");
    public static final ColumnDefinition<String> LAST_NAME = new StringColumnDefinition(5, "last_name");
    public static final ColumnDefinition<String> LAST_INITIAL = new StringColumnDefinition(6, "last_initial");
    public static final ColumnDefinition<String> DISPLAY_NAME = new StringColumnDefinition(7, "display_name");
    public static final ColumnDefinition<String> INTRODUCTION = new StringColumnDefinition(8, "introduction");
    public static final ColumnDefinition<String> HOBBIES = new StringColumnDefinition(9, "hobbies");
    public static final ColumnDefinition<String> GOAL_STATEMENT = new StringColumnDefinition(10, "goal_statement");
    public static final ColumnDefinition<String> PROFILE_STATEMENT = new StringColumnDefinition(11, "profile_statement");
    public static final ColumnDefinition<LocalDate> BIRTHDATE = new LocalDateColumnDefinition(12, "birthdate");
    public static final ColumnDefinition<Gender> GENDER = new EnumColumnDefinition(13, "gender", Gender.class);
    public static final ColumnDefinition<Double> HEIGHT = new DoubleColumnDefinition(14, "height");
    public static final ColumnDefinition<Double> WEIGHT = new DoubleColumnDefinition(15, "weight");
    public static final ColumnDefinition<String> TIMEZONE = new StringColumnDefinition(16, "timezone");
    public static final ColumnDefinition<Date> DATE_JOINED = new DateColumnDefinition(17, "date_joined");
    public static final ColumnDefinition<Date> LAST_LOGIN = new DateColumnDefinition(18, "last_login");
    public static final ColumnDefinition<MeasurementSystem> DISPLAY_MEASUREMENT_SYSTEM = new EnumColumnDefinition(19, "display_measurement_system", MeasurementSystem.class);
    public static final ColumnDefinition<Boolean> COMMUNICATION_PROMOTIONS = new BooleanColumnDefinition(20, "communication_promotions");
    public static final ColumnDefinition<Boolean> COMMUNICATION_NEWSLETTER = new BooleanColumnDefinition(22, "communication_newsletter");
    public static final ColumnDefinition<Boolean> COMMUNICATION_SYSTEM_MESSAGES = new BooleanColumnDefinition(22, "communication_system_messages");
    public static final ColumnDefinition<Boolean> SHARING_TWITTER = new BooleanColumnDefinition(23, "sharing_twitter");
    public static final ColumnDefinition<Boolean> SHARING_FACEBOOK = new BooleanColumnDefinition(24, "sharing_facebook");
    public static final ColumnDefinition<String> LOCATION_COUNTRY = new StringColumnDefinition(25, "location_country");
    public static final ColumnDefinition<String> LOCATION_REGION = new StringColumnDefinition(26, "location_region");
    public static final ColumnDefinition<String> LOCATION_LOCALITY = new StringColumnDefinition(27, "location_locality");
    public static final ColumnDefinition<String> LOCATION_ADDRESS = new StringColumnDefinition(28, "location_address");
    public static final ColumnDefinition<String> PROFILE_IMAGE_SMALL = new StringColumnDefinition(29, "profile_image_small");
    public static final ColumnDefinition<String> PROFILE_IMAGE_MEDIUM = new StringColumnDefinition(30, "profile_image_medium");
    public static final ColumnDefinition<String> PROFILE_IMAGE_LARGE = new StringColumnDefinition(31, "profile_image_large");
    private static final ColumnDefinition[] ALL_COLUMNS = {LOCAL_ID, ID, USERNAME, EMAIL, FIRST_NAME, LAST_NAME, LAST_INITIAL, DISPLAY_NAME, INTRODUCTION, HOBBIES, GOAL_STATEMENT, PROFILE_STATEMENT, BIRTHDATE, GENDER, HEIGHT, WEIGHT, TIMEZONE, DATE_JOINED, LAST_LOGIN, DISPLAY_MEASUREMENT_SYSTEM, COMMUNICATION_PROMOTIONS, COMMUNICATION_NEWSLETTER, COMMUNICATION_SYSTEM_MESSAGES, SHARING_TWITTER, SHARING_FACEBOOK, LOCATION_COUNTRY, LOCATION_REGION, LOCATION_LOCALITY, LOCATION_ADDRESS, PROFILE_IMAGE_SMALL, PROFILE_IMAGE_MEDIUM, PROFILE_IMAGE_LARGE};

    public static UserDatabase getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new UserDatabase(context.getApplicationContext());
        }
        return mInstance;
    }

    protected UserDatabase(Context context) {
        super(context, USER_DATABASE_NAME, "user", buildColumnNames(ALL_COLUMNS), ID.getColumnName(), 4);
    }

    @Override // com.ua.sdk.cache.database.LegacyEntityDatabase
    public void createEntityTable(SQLiteDatabase db) {
        String createStatement = buildCreateStatement("user", ALL_COLUMNS);
        db.execSQL(createStatement);
    }

    @Override // com.ua.sdk.cache.database.LegacyEntityDatabase
    public void onEntityUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        createEntityTable(db);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.cache.database.LegacyEntityDatabase
    public ContentValues getContentValuesFromEntity(User user) {
        if (user instanceof UserImpl) {
            switch (((UserImpl) user).getObjectState()) {
            }
            return getFullContentValues(user);
        }
        return getFullContentValues(user);
    }

    private ContentValues getFullContentValues(User user) {
        ContentValues values = new ContentValues();
        ID.write(user.getId(), values);
        USERNAME.write(user.getUsername(), values);
        EMAIL.write(user.getEmail(), values);
        FIRST_NAME.write(user.getFirstName(), values);
        LAST_NAME.write(user.getLastName(), values);
        LAST_INITIAL.write(user.getLastInitial(), values);
        DISPLAY_NAME.write(user.getDisplayName(), values);
        INTRODUCTION.write(user.getIntroduction(), values);
        HOBBIES.write(user.getHobbies(), values);
        GOAL_STATEMENT.write(user.getGoalStatement(), values);
        PROFILE_STATEMENT.write(user.getProfileStatement(), values);
        BIRTHDATE.write(user.getBirthdate(), values);
        GENDER.write(user.getGender(), values);
        HEIGHT.write(user.getHeight(), values);
        WEIGHT.write(user.getWeight(), values);
        TIMEZONE.write(user.getTimeZone(), values);
        DATE_JOINED.write(user.getDateJoined(), values);
        LAST_LOGIN.write(user.getLastLogin(), values);
        DISPLAY_MEASUREMENT_SYSTEM.write(user.getDisplayMeasurementSystem(), values);
        COMMUNICATION_PROMOTIONS.write(user.getCommunication() != null ? user.getCommunication().getPromotions() : null, values);
        COMMUNICATION_NEWSLETTER.write(user.getCommunication() != null ? user.getCommunication().getNewsletter() : null, values);
        COMMUNICATION_SYSTEM_MESSAGES.write(user.getCommunication() != null ? user.getCommunication().getSystemMessages() : null, values);
        SHARING_TWITTER.write(user.getSharing() != null ? user.getSharing().getTwitter() : null, values);
        SHARING_FACEBOOK.write(user.getSharing() != null ? user.getSharing().getFacebook() : null, values);
        LOCATION_COUNTRY.write(user.getLocation() != null ? user.getLocation().getCountry() : null, values);
        LOCATION_REGION.write(user.getLocation() != null ? user.getLocation().getRegion() : null, values);
        LOCATION_LOCALITY.write(user.getLocation() != null ? user.getLocation().getLocality() : null, values);
        LOCATION_ADDRESS.write(user.getLocation() != null ? user.getLocation().getAddress() : null, values);
        PROFILE_IMAGE_SMALL.write(user.getUserProfilePhoto() != null ? user.getUserProfilePhoto().getSmall() : null, values);
        PROFILE_IMAGE_MEDIUM.write(user.getUserProfilePhoto() != null ? user.getUserProfilePhoto().getMedium() : null, values);
        PROFILE_IMAGE_LARGE.write(user.getUserProfilePhoto() != null ? user.getUserProfilePhoto().getLarge() : null, values);
        return values;
    }

    private ContentValues getFriendsWithContent(User user) {
        ContentValues values = new ContentValues();
        ID.write(user.getId(), values);
        USERNAME.write(user.getUsername(), values);
        FIRST_NAME.write(user.getFirstName(), values);
        LAST_INITIAL.write(user.getLastInitial(), values);
        return values;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.cache.database.LegacyEntityDatabase
    public UserImpl getEntityFromCursor(Cursor c) {
        UserImpl user = new UserImpl();
        user.setLocalId(LOCAL_ID.read(c).longValue());
        user.setId(ID.read(c));
        user.setUsername(USERNAME.read(c));
        user.setEmail(EMAIL.read(c));
        user.setFirstName(FIRST_NAME.read(c));
        user.setLastName(LAST_NAME.read(c));
        user.setLastInitial(LAST_INITIAL.read(c));
        user.setDisplayName(DISPLAY_NAME.read(c));
        user.setIntroduction(INTRODUCTION.read(c));
        user.setHobbies(HOBBIES.read(c));
        user.setGoalStatement(GOAL_STATEMENT.read(c));
        user.setProfileStatement(PROFILE_STATEMENT.read(c));
        user.setBirthdate(BIRTHDATE.read(c));
        user.setGender(GENDER.read(c));
        user.setHeight(HEIGHT.read(c));
        user.setWeight(WEIGHT.read(c));
        user.setTimeZone(TIMEZONE.read(c));
        user.setDateJoined(DATE_JOINED.read(c));
        user.setLastLogin(LAST_LOGIN.read(c));
        user.setDisplayMeasurementSystem(DISPLAY_MEASUREMENT_SYSTEM.read(c));
        user.setCommunication(UserCommunicationImpl.getBuilder().setPromotions(COMMUNICATION_PROMOTIONS.read(c)).setNewletters(COMMUNICATION_NEWSLETTER.read(c)).setSystemMessages(COMMUNICATION_SYSTEM_MESSAGES.read(c)).build());
        user.setSharing(UserSharingImpl.getBuilder().setTwitter(SHARING_TWITTER.read(c)).setFacebook(SHARING_FACEBOOK.read(c)).build());
        Location location = new LocationImpl();
        location.setCountry(LOCATION_COUNTRY.read(c));
        location.setRegion(LOCATION_REGION.read(c));
        location.setLocality(LOCATION_LOCALITY.read(c));
        location.setAddress(LOCATION_ADDRESS.read(c));
        user.setLocation(location);
        user.setUserProfilePhoto(ImageUrlImpl.getBuilder().setSmall(PROFILE_IMAGE_SMALL.read(c)).setMedium(PROFILE_IMAGE_MEDIUM.read(c)).setLarge(PROFILE_IMAGE_LARGE.read(c)).build());
        return user;
    }
}
