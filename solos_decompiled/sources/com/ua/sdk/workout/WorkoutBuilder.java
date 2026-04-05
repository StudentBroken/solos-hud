package com.ua.sdk.workout;

import android.os.Parcelable;
import com.ua.sdk.activitystory.Attachment;
import com.ua.sdk.activitystory.SocialSettings;
import com.ua.sdk.activitytype.ActivityTypeRef;
import com.ua.sdk.gear.user.UserGearRef;
import com.ua.sdk.privacy.Privacy;
import com.ua.sdk.route.RouteRef;
import java.util.Date;
import java.util.TimeZone;

/* JADX INFO: loaded from: classes65.dex */
public interface WorkoutBuilder extends Parcelable {
    WorkoutBuilder addAttachment(Attachment.Type type);

    WorkoutBuilder addCadenceEvent(double d, int i);

    WorkoutBuilder addDistanceEvent(double d, double d2);

    WorkoutBuilder addHeartRateEvent(double d, int i);

    WorkoutBuilder addPositionEvent(double d, Double d2, Double d3, Double d4);

    WorkoutBuilder addPowerEvent(double d, double d2);

    WorkoutBuilder addSpeedEvent(double d, double d2);

    WorkoutBuilder addStepsEvent(double d, int i);

    WorkoutBuilder addTimerStopEvent(double d, double d2);

    WorkoutBuilder addTorqueEvent(double d, double d2);

    Workout build();

    WorkoutBuilder setActivityType(ActivityTypeRef activityTypeRef);

    WorkoutBuilder setCadenceAggregates(Integer num, Integer num2, Integer num3);

    WorkoutBuilder setCreateTime(Date date);

    WorkoutBuilder setHasTimeSeries(Boolean bool);

    WorkoutBuilder setHeartRateAggregates(Integer num, Integer num2, Integer num3);

    WorkoutBuilder setLocalId(Long l);

    WorkoutBuilder setMetabolicEnergyTotal(Double d);

    WorkoutBuilder setName(String str);

    WorkoutBuilder setNotes(String str);

    WorkoutBuilder setPowerAggregates(Double d, Double d2, Double d3);

    WorkoutBuilder setPrivacy(Privacy.Level level);

    WorkoutBuilder setReferenceKey(String str);

    WorkoutBuilder setRouteRef(RouteRef routeRef);

    WorkoutBuilder setSocialSettings(SocialSettings socialSettings);

    WorkoutBuilder setSource(String str);

    WorkoutBuilder setSpeedAggregates(Double d, Double d2, Double d3);

    WorkoutBuilder setStartTime(Date date);

    WorkoutBuilder setStepsTotal(Integer num);

    WorkoutBuilder setTimeZone(TimeZone timeZone);

    WorkoutBuilder setTorqueAggregates(Double d, Double d2, Double d3);

    WorkoutBuilder setTotalDistance(Double d);

    WorkoutBuilder setTotalTime(Double d, Double d2);

    WorkoutBuilder setUpdateTime(Date date);

    WorkoutBuilder setUserGear(UserGearRef userGearRef);

    WorkoutBuilder setWillPower(Double d);
}
