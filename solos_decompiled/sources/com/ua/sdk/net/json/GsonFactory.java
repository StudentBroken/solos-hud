package com.ua.sdk.net.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ua.sdk.LocalDate;
import com.ua.sdk.MeasurementSystem;
import com.ua.sdk.Source;
import com.ua.sdk.activitystory.ActivityStory;
import com.ua.sdk.activitystory.ActivityStoryActor;
import com.ua.sdk.activitystory.ActivityStoryActorAdapter;
import com.ua.sdk.activitystory.ActivityStoryAdapter;
import com.ua.sdk.activitystory.ActivityStoryGroupLeaderboard;
import com.ua.sdk.activitystory.ActivityStoryGroupLeaderboardAdapter;
import com.ua.sdk.activitystory.ActivityStoryHighlight;
import com.ua.sdk.activitystory.ActivityStoryHighlightAdapter;
import com.ua.sdk.activitystory.ActivityStoryObject;
import com.ua.sdk.activitystory.ActivityStoryObjectAdapter;
import com.ua.sdk.activitystory.ActivityStoryTarget;
import com.ua.sdk.activitystory.ActivityStoryTargetAdapter;
import com.ua.sdk.activitystory.ActivityStoryTemplate;
import com.ua.sdk.activitystory.ActivityStoryTemplateAdapter;
import com.ua.sdk.activitystory.ActivityStoryVerb;
import com.ua.sdk.activitystory.ActivityStoryVerbAdapter;
import com.ua.sdk.activitystory.Attachment;
import com.ua.sdk.activitystory.AttachmentAdapter;
import com.ua.sdk.activitystory.SourceAdapter;
import com.ua.sdk.activitytimeseries.ActivityTimeSeriesImpl;
import com.ua.sdk.activitytimeseries.ActivityTimeSeriesTypeAdapter;
import com.ua.sdk.activitytype.ActivityType;
import com.ua.sdk.activitytype.ActivityTypeAdapter;
import com.ua.sdk.aggregate.Aggregate;
import com.ua.sdk.aggregate.AggregateAdapter;
import com.ua.sdk.aggregate.AggregateSummary;
import com.ua.sdk.aggregate.AggregateSummaryAdapter;
import com.ua.sdk.authentication.FilemobileCredential;
import com.ua.sdk.authentication.FilemobileCredentialAdapter;
import com.ua.sdk.bodymass.BodyMass;
import com.ua.sdk.bodymass.BodyMassAdapter;
import com.ua.sdk.datasourceidentifier.DataSourceIdentifier;
import com.ua.sdk.datasourceidentifier.DataSourceIdentifierAdapter;
import com.ua.sdk.device.Device;
import com.ua.sdk.device.DeviceAdapter;
import com.ua.sdk.friendship.FriendshipStatus;
import com.ua.sdk.friendship.FriendshipStatusAdapter;
import com.ua.sdk.gear.Gear;
import com.ua.sdk.gear.GearAdapter;
import com.ua.sdk.gear.brand.GearBrand;
import com.ua.sdk.gear.brand.GearBrandAdapter;
import com.ua.sdk.gear.user.UserGear;
import com.ua.sdk.gear.user.UserGearAdapter;
import com.ua.sdk.group.Group;
import com.ua.sdk.group.GroupAdapter;
import com.ua.sdk.group.invite.GroupInvite;
import com.ua.sdk.group.invite.GroupInviteAdapter;
import com.ua.sdk.group.leaderboard.GroupLeaderboard;
import com.ua.sdk.group.leaderboard.GroupLeaderboardAdapter;
import com.ua.sdk.group.objective.Criteria;
import com.ua.sdk.group.objective.CriteriaGsonAdapter;
import com.ua.sdk.group.objective.GroupObjective;
import com.ua.sdk.group.objective.GroupObjectiveAdapter;
import com.ua.sdk.group.purpose.GroupPurpose;
import com.ua.sdk.group.purpose.GroupPurposeAdapter;
import com.ua.sdk.group.user.GroupUser;
import com.ua.sdk.group.user.GroupUserAdapter;
import com.ua.sdk.heartrate.HeartRateZones;
import com.ua.sdk.heartrate.HeartRateZonesGsonAdapter;
import com.ua.sdk.internal.Period;
import com.ua.sdk.location.Location;
import com.ua.sdk.location.LocationAdapter;
import com.ua.sdk.moderation.ModerationAction;
import com.ua.sdk.moderation.ModerationActionAdapter;
import com.ua.sdk.page.association.PageAssociation;
import com.ua.sdk.page.association.PageAssociationAdapter;
import com.ua.sdk.privacy.Privacy;
import com.ua.sdk.privacy.PrivacyAdapter;
import com.ua.sdk.recorder.BluetoothSensorDataSourceConfiguration;
import com.ua.sdk.recorder.DerivedDataSourceConfiguration;
import com.ua.sdk.recorder.LocationSensorDataSourceConfiguration;
import com.ua.sdk.recorder.datasource.derived.DerivedDataSourceConfigurationAdapter;
import com.ua.sdk.recorder.datasource.sensor.bluetooth.BluetoothSensorDataSourceAdapter;
import com.ua.sdk.recorder.datasource.sensor.location.LocationSensorDataSourceAdapter;
import com.ua.sdk.sleep.SleepMetric;
import com.ua.sdk.sleep.SleepMetricAdapter;
import com.ua.sdk.sleep.SleepMetricImpl;
import com.ua.sdk.sleep.SleepTimeSeriesTypeAdapter;
import com.ua.sdk.suggestedfriends.SuggestedFriends;
import com.ua.sdk.suggestedfriends.SuggestedFriendsAdapter;
import com.ua.sdk.user.Gender;
import com.ua.sdk.user.stats.AggregatePeriod;
import com.ua.sdk.user.stats.AggregatePeriodAdapter;
import com.ua.sdk.user.stats.HeartRateTimesAggregate;
import com.ua.sdk.user.stats.HeartRateTimesAggregatesAdapter;
import com.ua.sdk.user.stats.Stats;
import com.ua.sdk.user.stats.StatsAdapter;
import com.ua.sdk.user.stats.UserStats;
import com.ua.sdk.user.stats.UserStatsAdapter;
import com.ua.sdk.workout.TimeSeriesData;
import com.ua.sdk.workout.Workout;
import com.ua.sdk.workout.WorkoutAdapter;
import com.ua.sdk.workout.WorkoutAggregates;
import com.ua.sdk.workout.WorkoutAggregatesAdapter;
import com.ua.sdk.workout.WorkoutTimeSeriesDataAdapter;
import java.util.Date;
import java.util.TimeZone;

/* JADX INFO: loaded from: classes65.dex */
public class GsonFactory {
    public static Gson newInstance() {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(8, 128);
        builder.registerTypeAdapter(Date.class, new DateAdapter());
        builder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        builder.registerTypeAdapter(Gender.class, new Gender.GenderAdapter());
        builder.registerTypeAdapter(FriendshipStatus.class, new FriendshipStatusAdapter());
        builder.registerTypeAdapter(MeasurementSystem.class, new MeasurementSystem.MeasurementSystemAdapter());
        builder.registerTypeAdapter(Attachment.class, new AttachmentAdapter());
        Gson gson = builder.create();
        return gson;
    }

    public static Gson newFilemobileCredentialInstance() {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(8, 128);
        builder.registerTypeAdapter(Date.class, new DateAdapter());
        builder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        builder.registerTypeAdapter(FilemobileCredential.class, new FilemobileCredentialAdapter());
        Gson gson = builder.create();
        return gson;
    }

    public static Gson newActivityStoryInstance() {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(8, 128);
        builder.registerTypeAdapter(Date.class, new DateAdapter());
        builder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        builder.registerTypeAdapter(Gender.class, new Gender.GenderAdapter());
        builder.registerTypeAdapter(FriendshipStatus.class, new FriendshipStatusAdapter());
        builder.registerTypeAdapter(MeasurementSystem.class, new MeasurementSystem.MeasurementSystemAdapter());
        builder.registerTypeAdapter(ActivityStory.class, new ActivityStoryAdapter());
        builder.registerTypeAdapter(ActivityStoryTarget.class, new ActivityStoryTargetAdapter());
        builder.registerTypeAdapter(ActivityStoryActor.class, new ActivityStoryActorAdapter());
        builder.registerTypeAdapter(ActivityStoryVerb.class, new ActivityStoryVerbAdapter());
        builder.registerTypeAdapter(ActivityStoryObject.class, new ActivityStoryObjectAdapter());
        builder.registerTypeAdapter(ActivityStoryHighlight.class, new ActivityStoryHighlightAdapter());
        builder.registerTypeAdapter(ActivityStoryTemplate.class, new ActivityStoryTemplateAdapter());
        builder.registerTypeAdapter(Privacy.class, new PrivacyAdapter());
        builder.registerTypeAdapter(Location.class, new LocationAdapter());
        builder.registerTypeAdapter(Attachment.class, new AttachmentAdapter());
        builder.registerTypeAdapter(Source.class, new SourceAdapter());
        builder.registerTypeAdapter(GroupLeaderboard.class, new GroupLeaderboardAdapter());
        builder.registerTypeAdapter(Period.class, new Period.PeriodAdapter());
        builder.registerTypeAdapter(ActivityStoryGroupLeaderboard.class, new ActivityStoryGroupLeaderboardAdapter());
        builder.registerTypeAdapter(Criteria.class, new CriteriaGsonAdapter());
        Gson gson = builder.create();
        return gson;
    }

    public static Gson newSuggestedFriendsInstance() {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(8, 128);
        builder.registerTypeAdapter(Date.class, new DateAdapter());
        builder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        builder.registerTypeAdapter(SuggestedFriends.class, new SuggestedFriendsAdapter());
        Gson gson = builder.create();
        return gson;
    }

    public static Gson newPageAssociationInstance() {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(8, 128);
        builder.registerTypeAdapter(PageAssociation.class, new PageAssociationAdapter());
        Gson gson = builder.create();
        return gson;
    }

    public static Gson newActivityTimeSeriesInstance() {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(8, 128);
        builder.registerTypeAdapter(ActivityTimeSeriesImpl.TimeSeries.class, new ActivityTimeSeriesTypeAdapter());
        Gson gson = builder.create();
        return gson;
    }

    public static Gson newUserStatsInstance() {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(8, 128);
        builder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        builder.registerTypeAdapter(UserStats.class, new UserStatsAdapter());
        builder.registerTypeAdapter(AggregatePeriod.class, new AggregatePeriodAdapter());
        builder.registerTypeAdapter(HeartRateTimesAggregate.class, new HeartRateTimesAggregatesAdapter());
        builder.registerTypeAdapter(Stats.class, new StatsAdapter());
        return builder.create();
    }

    public static Gson newGearInstance() {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(8, 128);
        builder.registerTypeAdapter(Gear.class, new GearAdapter());
        builder.registerTypeAdapter(GearBrand.class, new GearBrandAdapter());
        builder.registerTypeAdapter(UserGear.class, new UserGearAdapter());
        builder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        return builder.create();
    }

    public static Gson newSleepInstance() {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(8, 128);
        builder.registerTypeAdapter(Date.class, new DateAdapter());
        builder.registerTypeAdapter(SleepMetricImpl.TimeSeries.class, new SleepTimeSeriesTypeAdapter());
        builder.registerTypeAdapter(SleepMetric.class, new SleepMetricAdapter());
        builder.registerTypeAdapter(TimeZone.class, new TimeZoneTypeAdapter());
        return builder.create();
    }

    public static Gson newActivityTypeInstance() {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(8, 128);
        builder.registerTypeAdapter(ActivityType.class, new ActivityTypeAdapter());
        return builder.create();
    }

    public static Gson newWorkoutInstance() {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(8, 128);
        builder.registerTypeAdapter(TimeZone.class, new TimeZoneTypeAdapter());
        builder.registerTypeAdapter(Date.class, new DateAdapter());
        builder.registerTypeAdapter(Privacy.class, new PrivacyAdapter());
        builder.registerTypeAdapter(Workout.class, new WorkoutAdapter());
        builder.registerTypeAdapter(TimeSeriesData.class, new WorkoutTimeSeriesDataAdapter());
        builder.registerTypeAdapter(WorkoutAggregates.class, new WorkoutAggregatesAdapter());
        builder.registerTypeAdapter(Attachment.class, new AttachmentAdapter());
        return builder.create();
    }

    public static Gson newModerationInstance() {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(8, 128);
        builder.registerTypeAdapter(ModerationAction.class, new ModerationActionAdapter());
        return builder.create();
    }

    public static Gson newGroupInstance() {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(8, 128);
        builder.registerTypeAdapter(Aggregate.class, new AggregateAdapter());
        builder.registerTypeAdapter(AggregateSummary.class, new AggregateSummaryAdapter());
        builder.registerTypeAdapter(Date.class, new DateAdapter());
        builder.registerTypeAdapter(Period.class, new Period.PeriodAdapter());
        builder.registerTypeAdapter(Group.class, new GroupAdapter());
        builder.registerTypeAdapter(GroupInvite.class, new GroupInviteAdapter());
        builder.registerTypeAdapter(GroupUser.class, new GroupUserAdapter());
        builder.registerTypeAdapter(GroupPurpose.class, new GroupPurposeAdapter());
        builder.registerTypeAdapter(GroupObjective.class, new GroupObjectiveAdapter());
        builder.registerTypeAdapter(GroupLeaderboard.class, new GroupLeaderboardAdapter());
        builder.registerTypeAdapter(Criteria.class, new CriteriaGsonAdapter());
        return builder.create();
    }

    public static Gson newAggregateInstance() {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(8, 128);
        builder.registerTypeAdapter(Date.class, new DateAdapter());
        builder.registerTypeAdapter(Period.class, new Period.PeriodAdapter());
        builder.registerTypeAdapter(Aggregate.class, new AggregateAdapter());
        builder.registerTypeAdapter(AggregateSummary.class, new AggregateSummaryAdapter());
        return builder.create();
    }

    public static Gson newBodyMassInstance() {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(8, 128);
        builder.registerTypeAdapter(Date.class, new DateAdapter());
        builder.registerTypeAdapter(BodyMass.class, new BodyMassAdapter());
        return builder.create();
    }

    public static Gson newRecorderConfigurationInstance() {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(8, 128);
        builder.registerTypeAdapter(BluetoothSensorDataSourceConfiguration.class, new BluetoothSensorDataSourceAdapter());
        builder.registerTypeAdapter(LocationSensorDataSourceConfiguration.class, new LocationSensorDataSourceAdapter());
        builder.registerTypeAdapter(DerivedDataSourceConfiguration.class, new DerivedDataSourceConfigurationAdapter());
        builder.registerTypeAdapter(DataSourceIdentifier.class, new DataSourceIdentifierAdapter());
        builder.registerTypeAdapter(Device.class, new DeviceAdapter());
        return builder.create();
    }

    public static Gson newHeartRateInstance() {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(8, 128);
        builder.registerTypeAdapter(HeartRateZones.class, new HeartRateZonesGsonAdapter());
        return builder.create();
    }
}
