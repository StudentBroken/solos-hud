package com.kopin.solos.util;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import com.goldeni.audio.GIAudioNative;
import com.kopin.solos.HardwareReceiverService;
import com.kopin.solos.common.SportType;
import com.kopin.solos.core.R;
import com.kopin.solos.metrics.IntensityFactor;
import com.kopin.solos.sensors.Sensor;
import com.kopin.solos.sensors.SensorList;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.DataHolder;
import com.kopin.solos.storage.util.HeartRateZones;
import com.kopin.solos.storage.util.MetricType;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.view.graphics.Bar;
import com.kopin.solos.virtualworkout.GhostWorkout;
import com.kopin.solos.virtualworkout.VirtualCoach;
import com.ua.sdk.datapoint.BaseDataTypes;

/* JADX INFO: loaded from: classes37.dex */
public class TTSHelper {
    private static final long TTS_DELAY_OFFSET_IN_SECONDS = 2;

    public static String getMetricTTS(HardwareReceiverService service, String metric) {
        StringBuilder sb = new StringBuilder();
        if (LiveRide.isGhostWorkout() && (VirtualCoach.getVirtualWorkout() instanceof GhostWorkout) && ((GhostWorkout) VirtualCoach.getVirtualWorkout()).hasData()) {
            getGhostMetricTTS(service, metric, sb);
        } else {
            getMetricTTS(service, metric, sb);
        }
        return sb.toString();
    }

    public static String getMetricsTTS(HardwareReceiverService service, String metric1, String metric2) {
        StringBuilder sb = new StringBuilder();
        getMetricTTS(service, metric1, sb);
        getMetricTTS(service, metric2, sb);
        return sb.toString();
    }

    private static void getMetricTTS(HardwareReceiverService service, String metric, StringBuilder sb) {
        if (metric != null && !metric.isEmpty()) {
            MetricType mt = MetricType.getMetricType(metric);
            switch (AnonymousClass1.$SwitchMap$com$kopin$solos$storage$util$MetricType[mt.ordinal()]) {
                case 1:
                case 2:
                    addHeartRate(service, service.getLastHeartRate(), sb);
                    break;
                case 3:
                case 4:
                    addCadence(service, (int) service.getLastCadence(), sb);
                    break;
                case 5:
                case 6:
                case 7:
                    addPower(service, (int) service.getLastPower(), sb);
                    break;
                case 8:
                case 9:
                    addSpeed(service, service.getLastSpeed(), sb);
                    break;
                case 10:
                    addTime(service, service.getTimeHelper().getTime() / 1000, sb);
                    break;
                case 11:
                case 12:
                    addElevation(service, (int) service.getLastElevationForLocale(), sb);
                    break;
                case 13:
                    addElevationChange(service, (int) service.getElevationChangeForLocale(), sb);
                    break;
                case 14:
                    addDistance(service, service.getLastDistanceForLocale(), sb);
                    break;
                case 15:
                    addSplitElevation(service, service.getSplitElevation(), sb);
                    break;
                case 16:
                case 17:
                    addAvgSpeed(service, service.getAverageSpeedForLocale(), sb);
                    break;
                case 18:
                    double distance = service.getSplitHelper().getSplitDistance();
                    long time = service.getSplitHelper().getSplitTime();
                    int speedMPS = (int) Utility.calculateSpeedMetresPerSecond(distance, time);
                    addSplitSpeed(service, speedMPS, sb);
                    break;
                case 19:
                    addCalories(service, LiveRide.getCurrentCalories(), sb);
                    break;
                case 20:
                    addNormalisedPower(service, sb);
                    break;
                case 21:
                    addTargetTime(service, sb);
                    break;
                case 22:
                    addTargetDistance(service, sb);
                    break;
                case 23:
                    addTargetAverageSpeed(service, sb);
                    break;
                case 24:
                    addTargetAverageHeartRate(service, sb);
                    break;
                case 25:
                    addTargetAveragePower(service, sb);
                    break;
                case 26:
                    addTargetAverageCadence(service, sb);
                    break;
                case 27:
                    addHeartRateZone(service, sb);
                    break;
                case 28:
                    addOverallClimb(service, sb);
                    break;
                case GIAudioNative.AUDIO_CURRENT_CONFIGURATION /* 29 */:
                    addIntensityFactor(service, sb);
                    break;
                case 30:
                    addStride(service, service.getLastStride(), sb);
                    break;
                case 31:
                case 32:
                    addPace(service, service.getLastPaceForLocale(), sb);
                    break;
                case MotionEventCompat.AXIS_GENERIC_2 /* 33 */:
                case 34:
                    addAvgPace(service, service.getAveragePaceForLocale(), sb);
                    break;
                case MotionEventCompat.AXIS_GENERIC_4 /* 35 */:
                    addTargetAveragePace(service, sb);
                    break;
                case MotionEventCompat.AXIS_GENERIC_5 /* 36 */:
                case MotionEventCompat.AXIS_GENERIC_6 /* 37 */:
                    addStep(service, (int) service.getLastCadence(), sb);
                    break;
                case MotionEventCompat.AXIS_GENERIC_7 /* 38 */:
                    addTargetAverageStep(service, sb);
                    break;
                case MotionEventCompat.AXIS_GENERIC_8 /* 39 */:
                case 40:
                case MotionEventCompat.AXIS_GENERIC_10 /* 41 */:
                    addPower(service, (int) service.getLastPower(), sb);
                    break;
                case MotionEventCompat.AXIS_GENERIC_11 /* 42 */:
                    addTargetAverageKick(service, sb);
                    break;
                case MotionEventCompat.AXIS_GENERIC_12 /* 43 */:
                    switch (LiveRide.getCurrentSport()) {
                        case RIDE:
                            getMetricTTS(service, BaseDataTypes.ID_SPEED, sb);
                            sb.append(service.getString(R.string.tts_builder_comma));
                            getMetricTTS(service, "cadence", sb);
                            sb.append(service.getString(R.string.tts_builder_comma));
                            getMetricTTS(service, "power", sb);
                            sb.append(service.getString(R.string.tts_builder_comma));
                            getMetricTTS(service, "heartrate", sb);
                            break;
                        case RUN:
                            getMetricTTS(service, "pace", sb);
                            sb.append(service.getString(R.string.tts_builder_comma));
                            getMetricTTS(service, "step", sb);
                            sb.append(service.getString(R.string.tts_builder_comma));
                            if (SensorList.isSensorConnected(Sensor.DataType.KICK, true)) {
                                getMetricTTS(service, "kick", sb);
                            } else {
                                getMetricTTS(service, "stride", sb);
                            }
                            sb.append(service.getString(R.string.tts_builder_comma));
                            getMetricTTS(service, "heartrate", sb);
                            break;
                    }
                    break;
            }
        }
    }

    /* JADX INFO: renamed from: com.kopin.solos.util.TTSHelper$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$kopin$solos$storage$util$MetricType = new int[MetricType.values().length];

        static {
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.HEARTRATE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.HEARTRATE_GRAPH.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.CADENCE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.CADENCE_GRAPH.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.POWER.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.POWER_BAR.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.POWER_GRAPH.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.SPEED.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.SPEED_GRAPH.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.TIME.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.ELEVATION.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.ELEVATION_GRAPH.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.ELEVATION_CHANGE.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.DISTANCE.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.ELEVATION_LAP_GRAPH.ordinal()] = 15;
            } catch (NoSuchFieldError e15) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_SPEED.ordinal()] = 16;
            } catch (NoSuchFieldError e16) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_SPEED_GRAPH.ordinal()] = 17;
            } catch (NoSuchFieldError e17) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.LAP_GRAPH.ordinal()] = 18;
            } catch (NoSuchFieldError e18) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.CALORIES.ordinal()] = 19;
            } catch (NoSuchFieldError e19) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.POWER_NORMALISED.ordinal()] = 20;
            } catch (NoSuchFieldError e20) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.TARGET_TIME.ordinal()] = 21;
            } catch (NoSuchFieldError e21) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.TARGET_DISTANCE.ordinal()] = 22;
            } catch (NoSuchFieldError e22) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_TARGET_SPEED.ordinal()] = 23;
            } catch (NoSuchFieldError e23) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_TARGET_HEARTRATE.ordinal()] = 24;
            } catch (NoSuchFieldError e24) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_TARGET_POWER.ordinal()] = 25;
            } catch (NoSuchFieldError e25) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_TARGET_CADENCE.ordinal()] = 26;
            } catch (NoSuchFieldError e26) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.HEARTRATE_ZONES.ordinal()] = 27;
            } catch (NoSuchFieldError e27) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.OVERALL_CLIMB.ordinal()] = 28;
            } catch (NoSuchFieldError e28) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.INTENSITY_FACTOR.ordinal()] = 29;
            } catch (NoSuchFieldError e29) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.STRIDE.ordinal()] = 30;
            } catch (NoSuchFieldError e30) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.PACE.ordinal()] = 31;
            } catch (NoSuchFieldError e31) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.PACE_GRAPH.ordinal()] = 32;
            } catch (NoSuchFieldError e32) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_PACE.ordinal()] = 33;
            } catch (NoSuchFieldError e33) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_PACE_GRAPH.ordinal()] = 34;
            } catch (NoSuchFieldError e34) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_TARGET_PACE.ordinal()] = 35;
            } catch (NoSuchFieldError e35) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.STEP.ordinal()] = 36;
            } catch (NoSuchFieldError e36) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.STEP_GRAPH.ordinal()] = 37;
            } catch (NoSuchFieldError e37) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_TARGET_STEP.ordinal()] = 38;
            } catch (NoSuchFieldError e38) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.KICK.ordinal()] = 39;
            } catch (NoSuchFieldError e39) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.KICK_BAR.ordinal()] = 40;
            } catch (NoSuchFieldError e40) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.KICK_GRAPH.ordinal()] = 41;
            } catch (NoSuchFieldError e41) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.AVERAGE_TARGET_KICK.ordinal()] = 42;
            } catch (NoSuchFieldError e42) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.METRIC_OVERVIEW.ordinal()] = 43;
            } catch (NoSuchFieldError e43) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.GHOST_AVERAGE_CADENCE.ordinal()] = 44;
            } catch (NoSuchFieldError e44) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.GHOST_AVERAGE_HEARTRATE.ordinal()] = 45;
            } catch (NoSuchFieldError e45) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.GHOST_AVERAGE_POWER.ordinal()] = 46;
            } catch (NoSuchFieldError e46) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.GHOST_AVERAGE_KICK.ordinal()] = 47;
            } catch (NoSuchFieldError e47) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.GHOST_AVERAGE_SPEED.ordinal()] = 48;
            } catch (NoSuchFieldError e48) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.GHOST_AVERAGE_PACE.ordinal()] = 49;
            } catch (NoSuchFieldError e49) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$util$MetricType[MetricType.GHOST_AVERAGE_STEP.ordinal()] = 50;
            } catch (NoSuchFieldError e50) {
            }
            $SwitchMap$com$kopin$solos$common$SportType = new int[SportType.values().length];
            try {
                $SwitchMap$com$kopin$solos$common$SportType[SportType.RIDE.ordinal()] = 1;
            } catch (NoSuchFieldError e51) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$SportType[SportType.RUN.ordinal()] = 2;
            } catch (NoSuchFieldError e52) {
            }
        }
    }

    private static boolean addTime(Context ctx, long time, StringBuilder sb) {
        if (time <= 1) {
            return false;
        }
        sb.append(ctx.getResources().getString(R.string.tts_builder_time_passed));
        sb.append(timeAsString(ctx, time + 2));
        return true;
    }

    public static String timeAsString(Context ctx, long time) {
        StringBuilder sb = new StringBuilder();
        long sec = time % 60;
        long time2 = time / 60;
        long min = time2 % 60;
        long hour = time2 / 60;
        if (hour > 0) {
            sb.append(" ").append(hour).append(ctx.getResources().getQuantityString(R.plurals.hours, (int) hour));
        }
        if (min > 0) {
            sb.append(" ").append(min).append(ctx.getResources().getQuantityString(R.plurals.minutes, (int) min));
        }
        if (sec > 1) {
            if (min > 0) {
                sb.append(" ").append(ctx.getResources().getString(R.string.tts_builder_and));
            }
            sb.append(" ").append(sec).append(ctx.getResources().getQuantityString(R.plurals.seconds, (int) sec));
        }
        return sb.toString();
    }

    private static boolean addSpeed(Context context, double speed, StringBuilder sb) {
        if (speed < 0.0d) {
            return false;
        }
        String ttsDistance = Conversion.getUnitOfDistanceTTS(context);
        sb.append(context.getString(R.string.tts_speed, Integer.valueOf((int) Math.round(Conversion.speedForLocale(speed))), ttsDistance)).append(" ");
        return true;
    }

    private static boolean addPace(Context context, double pace, StringBuilder sb) {
        if (pace < 0.0d) {
            return false;
        }
        sb.append(context.getString(R.string.tts_pace));
        sb.append(addTimeDiff((long) pace, context));
        sb.append(" ");
        sb.append(Conversion.getUnitOfPaceTTS(context));
        sb.append(" ");
        return true;
    }

    private static boolean addStride(Context context, double stride, StringBuilder sb) {
        if (stride < 0.0d) {
            return false;
        }
        String ttsUnit = Conversion.getUnitOfStrideLong(context);
        sb.append(context.getString(R.string.tts_stride, Integer.valueOf((int) Conversion.strideForLocale(stride)), ttsUnit)).append(" ");
        return true;
    }

    private static boolean addCadence(Context context, int cadence, StringBuilder sb) {
        return addItem(context, sb, cadence >= 0, R.string.tts_cadence, cadence);
    }

    private static boolean addStep(Context context, int cadence, StringBuilder sb) {
        return addItem(context, sb, cadence >= 0, R.string.tts_cadence_run, cadence);
    }

    private static boolean addPower(Context context, int power, StringBuilder sb) {
        return addItem(context, sb, power >= 0, R.string.tts_power, power);
    }

    private static boolean addHeartRate(Context context, int heartRate, StringBuilder sb) {
        return addItem(context, sb, heartRate > 0, R.string.tts_heartrate, heartRate);
    }

    private static boolean addCalories(Context context, int calories, StringBuilder sb) {
        return addItem(context, sb, calories > 0, R.string.tts_calories, calories);
    }

    private static boolean addItem(Context context, StringBuilder sb, boolean include, int resource, int amount) {
        if (include) {
            sb.append(context.getString(resource, Integer.valueOf(amount))).append(" ");
        }
        return include;
    }

    private static boolean addElevation(Context context, int elevation, StringBuilder sb) {
        if (elevation <= -2.14748365E9f) {
            return false;
        }
        String ttsLength = Utility.getUserDefinedUnit(context, 0);
        sb.append(context.getString(R.string.tts_elevation, Integer.valueOf(elevation), ttsLength)).append(" ");
        return true;
    }

    private static boolean addElevationChange(Context context, int elevation, StringBuilder sb) {
        if (elevation <= -2.14748365E9f) {
            return false;
        }
        String ttsLength = Utility.getUserDefinedUnit(context, 0);
        sb.append(context.getString(R.string.tts_elevation_change, Integer.valueOf(elevation), ttsLength)).append(" ");
        return true;
    }

    private static boolean addDistance(Context context, double distance, StringBuilder sb) {
        if (distance < 0.0d) {
            return false;
        }
        String unit = Utility.getUserDefinedUnit(context, 1);
        sb.append(context.getString(R.string.tts_distance3, Double.valueOf(distance), unit)).append(" ");
        return true;
    }

    private static boolean addAvgSpeed(Context context, double avgSpeed, StringBuilder sb) {
        if (avgSpeed < 0.0d) {
            return false;
        }
        String ttsDistance = Utility.getUserDefinedUnit(context, 1);
        sb.append(context.getString(R.string.tts_avg_speed, Double.valueOf(avgSpeed), ttsDistance)).append(" ");
        return true;
    }

    private static boolean addAvgPace(Context context, double avgPace, StringBuilder sb) {
        if (avgPace < 0.0d) {
            return false;
        }
        sb.append(context.getString(R.string.tts_avg_pace));
        sb.append(addTimeDiff((long) avgPace, context));
        sb.append(" ");
        sb.append(context.getString(R.string.per)).append(" " + Conversion.getUnitOfDistanceTTS(context));
        sb.append(" ");
        return true;
    }

    private static boolean addSplitElevation(Context context, int elevation, StringBuilder sb) {
        if (elevation <= -2.14748365E9f) {
            return false;
        }
        String ttsLength = Conversion.getUnitOfLengthTTS(context);
        String ttsLengthFeet = context.getString(R.string.unit_length_imperial);
        if (ttsLength.equals(ttsLengthFeet)) {
            elevation = (int) Utility.metersToFeet(elevation);
        }
        sb.append(context.getString(R.string.tts_lap_elevation, Integer.valueOf(elevation), ttsLength)).append(" ");
        return true;
    }

    private static boolean addSplitSpeed(Context context, int speed, StringBuilder sb) {
        if (speed < 0) {
            return false;
        }
        String ttsDistance = Conversion.getUnitOfDistanceTTS(context);
        sb.append(context.getString(R.string.tts_lap_speed, Integer.valueOf((int) Conversion.speedForLocale(speed)), ttsDistance)).append(" ");
        return true;
    }

    private static boolean addNormalisedPower(Context context, StringBuilder sb) {
        double normalisedPower = LiveRide.getPowerNormalised();
        if (normalisedPower > 0.0d) {
            sb.append(context.getString(R.string.tts_normalised_power, Integer.valueOf((int) normalisedPower))).append(" ");
        }
        return true;
    }

    private static boolean addTargetTime(HardwareReceiverService service, StringBuilder sb) {
        boolean hasTargetTime = Prefs.hasTargetTime();
        if (!hasTargetTime) {
            return false;
        }
        long targetTime = LiveRide.getTargetTime();
        long time = ((targetTime - service.getTimeHelper().getTime()) / 1000) - 2;
        if (time > 0) {
            sb.append(service.getString(R.string.tts_target_time)).append(" ");
            sb.append(timeAsString(service, time));
        } else {
            sb.append(service.getString(R.string.tts_target_time_passed));
        }
        return true;
    }

    private static boolean addTargetDistance(HardwareReceiverService service, StringBuilder sb) {
        boolean hasTargetDistance = Prefs.hasTargetDistance();
        if (!hasTargetDistance) {
            return false;
        }
        double targetDistance = Prefs.getTargetDistance();
        double distance = service.getLastDistanceForLocale();
        double remainingDistance = targetDistance - distance;
        String unit = Utility.getUserDefinedUnit(service, 1);
        String distStr = String.format("%.2f", Double.valueOf(remainingDistance));
        if (remainingDistance > 0.0d) {
            sb.append(service.getString(R.string.tts_target_distance, new Object[]{distStr, unit})).append(" ");
        } else {
            sb.append(service.getString(R.string.tts_target_distance_reached));
        }
        return true;
    }

    private static boolean addTargetAverageSpeed(HardwareReceiverService service, StringBuilder sb) {
        if (!Prefs.hasTargetAverageSpeed()) {
            return false;
        }
        double targetAvg = Utility.roundToLong(Conversion.speedForLocale(Prefs.getTargetAverageSpeedValue()));
        double average = service.getAverageSpeedForLocale();
        double avgDifference = average - targetAvg;
        String ttsUnit = Utility.getUserDefinedUnit(service, 2);
        String speedStr = String.format("%.1f", Double.valueOf(Math.abs(avgDifference)));
        sb.append(service.getString(R.string.tts_target_avg_speed, new Object[]{speedStr, ttsUnit}));
        sb.append("\n");
        sb.append(service.getString(average >= targetAvg ? R.string.tts_above_target : R.string.tts_below_target)).append(" ");
        return true;
    }

    private static boolean addTargetAveragePace(HardwareReceiverService service, StringBuilder sb) {
        if (!Prefs.hasTargetAveragePace()) {
            return false;
        }
        long targetAvg = Utility.round(Conversion.paceForLocale(Prefs.getTargetAveragePaceValue()), 5);
        double average = LiveRide.getAveragePaceLocale();
        double avgDifference = average - targetAvg;
        double avgDifference2 = Math.abs(avgDifference);
        sb.append(service.getString(R.string.tts_target_avg_pace));
        sb.append(addTimeDiff(avgDifference2, service));
        sb.append("\n");
        sb.append(service.getString(average <= ((double) targetAvg) ? R.string.tts_above_target : R.string.tts_below_target)).append(" ");
        return true;
    }

    public static String addTimeDiff(double timeSeconds, Context context) {
        StringBuilder builder = new StringBuilder();
        int secondsDiff = (int) Math.round(timeSeconds);
        int minsDiff = secondsDiff / 60;
        if (minsDiff > 0) {
            builder.append(minsDiff).append(" ");
            builder.append(context.getResources().getQuantityString(R.plurals.minutes, minsDiff));
            secondsDiff %= 60;
        }
        if (secondsDiff > 0) {
            builder.append(" ").append(secondsDiff).append(" ");
            builder.append(context.getResources().getQuantityString(R.plurals.seconds, secondsDiff));
        }
        return builder.toString();
    }

    private static boolean addTargetAverageStep(HardwareReceiverService service, StringBuilder sb) {
        if (!Prefs.hasTargetAverageStep() || LiveRide.getAverageCadence() == -2.147483648E9d) {
            return false;
        }
        return addTargetAverage(service, sb, Prefs.getTargetAverageStep(), LiveRide.getAverageCadence(), R.string.tts_target_avg_cadence, R.string.unit_cadence_run);
    }

    private static boolean addTargetAverageHeartRate(HardwareReceiverService service, StringBuilder sb) {
        if (!Prefs.hasTargetAverageHeartRate() || LiveRide.getAverageHeartRate() == -2.147483648E9d) {
            return false;
        }
        return addTargetAverage(service, sb, Prefs.getTargetAverageHeartrate(), LiveRide.getAverageHeartRate(), R.string.tts_target_avg_heart_rate, R.string.unit_heart_rate);
    }

    private static boolean addTargetAveragePower(HardwareReceiverService service, StringBuilder sb) {
        if (!Prefs.hasTargetAveragePower() || LiveRide.getAveragePower() == -2.147483648E9d) {
            return false;
        }
        return addTargetAverage(service, sb, Prefs.getTargetAveragePower(), LiveRide.getAveragePower(), R.string.tts_target_avg_power, R.string.power_unit);
    }

    private static boolean addTargetAverageKick(HardwareReceiverService service, StringBuilder sb) {
        if (!Prefs.hasTargetAverageKick() || LiveRide.getAveragePower() == -2.147483648E9d) {
            return false;
        }
        return addTargetAverage(service, sb, Prefs.getTargetAverageKick(), LiveRide.getAveragePower(), R.string.tts_target_avg_power, R.string.power_unit);
    }

    private static boolean addTargetAverageCadence(HardwareReceiverService service, StringBuilder sb) {
        if (!Prefs.hasTargetAverageCadence() || LiveRide.getAverageCadence() == -2.147483648E9d) {
            return false;
        }
        return addTargetAverage(service, sb, Prefs.getTargetAverageCadence(), LiveRide.getAverageCadence(), R.string.tts_target_avg_cadence, R.string.unit_cadence);
    }

    private static boolean addTargetAverage(HardwareReceiverService service, StringBuilder sb, double target, double value, int resTarget, int resUnit) {
        int avgDifference = (int) Math.abs(value - target);
        sb.append(service.getString(resTarget, new Object[]{Integer.valueOf(avgDifference), service.getString(resUnit)}));
        sb.append("\n");
        sb.append(service.getString(value >= target ? R.string.tts_above_target : R.string.tts_below_target)).append(" ");
        return true;
    }

    private static boolean addHeartRateZone(HardwareReceiverService service, StringBuilder sb) {
        int heartRate = service.getLastHeartRate();
        if (heartRate <= 0) {
            return false;
        }
        int heartRateZone = HeartRateZones.computeHeartRateZone(heartRate);
        String heartRateZoneName = HeartRateZones.getHeartRateZoneName(heartRateZone);
        sb.append(service.getString(R.string.tts_heartrate_zone, new Object[]{heartRateZoneName})).append(" ");
        return true;
    }

    private static boolean addIntensityFactor(HardwareReceiverService service, StringBuilder sb) {
        if (!IntensityFactor.hasIntensityFactor()) {
            return false;
        }
        String ifZone = IntensityFactor.intensityFactorZone(service);
        sb.append(service.getString(R.string.tts_intensity_factor, new Object[]{ifZone})).append(" ");
        return true;
    }

    private static boolean addOverallClimb(HardwareReceiverService service, StringBuilder sb) {
        float overAllClimb = LiveRide.getOverallClimbForLocale();
        if (overAllClimb < 0.0f) {
            return false;
        }
        sb.append(service.getString(R.string.tts_overall_climb, new Object[]{Integer.valueOf((int) overAllClimb), Utility.getUserDefinedUnit(service, 0)})).append(" ");
        return true;
    }

    private static boolean addGhost(HardwareReceiverService service, StringBuilder sb, double ghostValue, double currentValue, int resTarget, int resUnit) {
        if (!DataHolder.isValid(ghostValue) || !DataHolder.isValid(currentValue)) {
            return false;
        }
        int avgDifference = (int) Math.abs(currentValue - ghostValue);
        sb.append(service.getString(resTarget)).append(": ");
        if (avgDifference == 0) {
            sb.append(service.getString(R.string.tts_level_ghost));
            return true;
        }
        sb.append(avgDifference).append(" ");
        sb.append(service.getString(resUnit)).append(" .").append("\n");
        sb.append(service.getString(currentValue >= ghostValue ? R.string.tts_above_ghost : R.string.tts_below_ghost)).append(" ");
        return true;
    }

    private static boolean addGhostDistance(HardwareReceiverService service, StringBuilder sb) {
        double ghostDistance = ((GhostWorkout) VirtualCoach.getVirtualWorkout()).getDistanceForLocale();
        double distance = service.getLastDistanceForLocale();
        if (!DataHolder.isValid(ghostDistance) || !DataHolder.isValid(distance)) {
            return false;
        }
        double diff = Math.abs(ghostDistance - distance);
        Prefs.UnitSystem unitSystem = Prefs.getUnitSystem();
        int unitRes = unitSystem == Prefs.UnitSystem.METRIC ? R.string.unit_distance_metric : R.string.unit_distance_imperial;
        String distStr = String.format("%.2f", Double.valueOf(diff));
        if (diff < 1.0d) {
            if (unitSystem == Prefs.UnitSystem.METRIC) {
                unitRes = R.string.unit_distance_metric_small;
                diff *= 1000.0d;
            } else {
                unitRes = R.string.unit_distance_yards;
                diff *= 1093.61d;
            }
            distStr = String.format("%d", Integer.valueOf((int) diff));
        }
        sb.append(service.getString(R.string.distance)).append(": ");
        if (diff < 1.0d) {
            sb.append(service.getString(R.string.tts_level_ghost));
            return true;
        }
        String unit = service.getString(unitRes);
        sb.append(distStr).append(" ");
        sb.append(unit).append(" .").append("\n");
        sb.append(service.getString(distance >= ghostDistance ? R.string.tts_above_ghost : R.string.tts_below_ghost)).append(" ");
        return true;
    }

    private static boolean addGhostSpeed(HardwareReceiverService service, StringBuilder sb) {
        double ghostSpeed = ((GhostWorkout) VirtualCoach.getVirtualWorkout()).getSpeedForLocale();
        double currentSpeed = service.getLastSpeedForLocale();
        if (!DataHolder.isValid(ghostSpeed) || !DataHolder.isValid(currentSpeed)) {
            return false;
        }
        double diff = Math.abs(ghostSpeed - currentSpeed);
        sb.append(service.getString(R.string.speed)).append(": ");
        if (diff < 1.0d) {
            sb.append(service.getString(R.string.tts_level_ghost));
            return true;
        }
        String unit = Utility.getUserDefinedUnit(service, 2);
        sb.append((int) diff).append(" ");
        sb.append(unit).append(" ").append("\n");
        sb.append(service.getString(currentSpeed >= ghostSpeed ? R.string.tts_above_ghost : R.string.tts_below_ghost)).append(" ");
        return true;
    }

    private static boolean addGhostAvgSpeed(HardwareReceiverService service, StringBuilder sb) {
        double ghostSpeed = ((GhostWorkout) VirtualCoach.getVirtualWorkout()).getAverageSpeedForLocale();
        double currentSpeed = service.getAverageSpeedForLocale();
        if (!DataHolder.isValid(ghostSpeed) || !DataHolder.isValid(currentSpeed)) {
            return false;
        }
        double diff = Math.abs(ghostSpeed - currentSpeed);
        sb.append(service.getString(R.string.vc_title_average_speed));
        sb.append(": ");
        if (diff < 1.0d) {
            sb.append(service.getString(R.string.tts_level_ghost));
            return true;
        }
        String unit = Utility.getUserDefinedUnit(service, 2);
        sb.append((int) diff);
        sb.append(" ");
        sb.append(unit);
        sb.append(" ");
        sb.append("\n");
        sb.append(service.getString(currentSpeed >= ghostSpeed ? R.string.tts_above_ghost : R.string.tts_below_ghost)).append(" ");
        return true;
    }

    private static boolean addGhostPace(HardwareReceiverService service, StringBuilder sb) {
        double ghostPace = ((GhostWorkout) VirtualCoach.getVirtualWorkout()).getPaceForLocale();
        double currentPace = service.getLastPaceForLocale();
        if (!DataHolder.isValid(ghostPace) || !DataHolder.isValid(currentPace)) {
            return false;
        }
        double diff = Math.abs(ghostPace - currentPace);
        sb.append(service.getString(R.string.tts_pace));
        if (diff < 0.1d) {
            sb.append(service.getString(R.string.tts_level_ghost));
            return true;
        }
        String unit = Conversion.getUnitOfPaceTTS(service);
        sb.append(addTimeDiff((long) diff, service)).append(" ");
        sb.append(unit).append(" .").append("\n");
        sb.append(service.getString(ghostPace >= currentPace ? R.string.tts_above_ghost : R.string.tts_below_ghost)).append(" ");
        return true;
    }

    private static boolean addGhostAvgPace(HardwareReceiverService service, StringBuilder sb) {
        double ghost = ((GhostWorkout) VirtualCoach.getVirtualWorkout()).getAveragePaceForLocale();
        double current = LiveRide.getAveragePaceLocale();
        if (!DataHolder.isValid(ghost) || !DataHolder.isValid(current)) {
            return false;
        }
        double diff = Math.abs(ghost - current);
        sb.append(service.getString(R.string.tts_avg_pace));
        if (diff < 0.1d) {
            sb.append(service.getString(R.string.tts_level_ghost));
            return true;
        }
        String unit = Conversion.getUnitOfPaceTTS(service);
        sb.append(addTimeDiff((long) diff, service)).append(" ");
        sb.append(unit).append(" .").append("\n");
        sb.append(service.getString(ghost >= current ? R.string.tts_above_ghost : R.string.tts_below_ghost)).append(" ");
        return true;
    }

    private static boolean addGhostStride(HardwareReceiverService service, StringBuilder sb) {
        double ghost = ((GhostWorkout) VirtualCoach.getVirtualWorkout()).getStrideForLocale();
        double current = Conversion.strideForLocale(service.getLastStride());
        if (!DataHolder.isValid(ghost) || !DataHolder.isValid(current)) {
            return false;
        }
        double diff = Math.abs(ghost - current);
        sb.append(service.getString(R.string.vc_title_stride)).append(": ");
        if (diff < 1.0d) {
            sb.append(service.getString(R.string.tts_level_ghost));
            return true;
        }
        String unit = Conversion.getUnitOfStrideLong(service);
        sb.append((int) diff).append(" ");
        sb.append(unit).append(" .").append("\n");
        sb.append(service.getString(current >= ghost ? R.string.tts_above_ghost : R.string.tts_below_ghost)).append(" ");
        return true;
    }

    private static void getGhostMetricTTS(HardwareReceiverService service, String metric, StringBuilder sb) {
        if (metric != null && !metric.isEmpty()) {
            MetricType mt = MetricType.getMetricType(metric);
            switch (AnonymousClass1.$SwitchMap$com$kopin$solos$storage$util$MetricType[mt.ordinal()]) {
                case 1:
                    addGhost(service, sb, ((GhostWorkout) VirtualCoach.getVirtualWorkout()).getHeartRate(), service.getLastHeartRate(), R.string.heart, R.string.unit_heart_rate);
                    break;
                case 3:
                    addGhost(service, sb, ((GhostWorkout) VirtualCoach.getVirtualWorkout()).getCadence(), service.getLastCadence(), R.string.cadence, R.string.unit_cadence);
                    break;
                case 5:
                case MotionEventCompat.AXIS_GENERIC_8 /* 39 */:
                    addGhost(service, sb, ((GhostWorkout) VirtualCoach.getVirtualWorkout()).getPower(), service.getLastPower(), R.string.vc_title_power, R.string.power_unit);
                    break;
                case 8:
                    addGhostSpeed(service, sb);
                    break;
                case 10:
                    addTime(service, service.getTimeHelper().getTime() / 1000, sb);
                    break;
                case 14:
                    addGhostDistance(service, sb);
                    break;
                case 30:
                    addGhostStride(service, sb);
                    break;
                case 31:
                    addGhostPace(service, sb);
                    break;
                case MotionEventCompat.AXIS_GENERIC_5 /* 36 */:
                    addGhost(service, sb, ((GhostWorkout) VirtualCoach.getVirtualWorkout()).getCadence(), service.getLastCadence(), R.string.cadence, R.string.unit_cadence_run);
                    break;
                case MotionEventCompat.AXIS_GENERIC_12 /* 43 */:
                    switch (LiveRide.getCurrentSport()) {
                        case RIDE:
                            getGhostMetricTTS(service, BaseDataTypes.ID_SPEED, sb);
                            sb.append(service.getString(R.string.tts_builder_comma));
                            getGhostMetricTTS(service, "cadence", sb);
                            sb.append(service.getString(R.string.tts_builder_comma));
                            getGhostMetricTTS(service, "power", sb);
                            sb.append(service.getString(R.string.tts_builder_comma));
                            getGhostMetricTTS(service, "heartrate", sb);
                            break;
                        case RUN:
                            getGhostMetricTTS(service, "pace", sb);
                            sb.append(service.getString(R.string.tts_builder_comma));
                            getGhostMetricTTS(service, "step", sb);
                            sb.append(service.getString(R.string.tts_builder_comma));
                            if (SensorList.isSensorConnected(Sensor.DataType.KICK, true)) {
                                getGhostMetricTTS(service, "kick", sb);
                            } else {
                                getGhostMetricTTS(service, "stride", sb);
                            }
                            sb.append(service.getString(R.string.tts_builder_comma));
                            getGhostMetricTTS(service, "heartrate", sb);
                            break;
                    }
                    break;
                case 44:
                    addGhost(service, sb, ((GhostWorkout) VirtualCoach.getVirtualWorkout()).getAverageCadence(), LiveRide.getAverageCadence(), R.string.tts_avg_cadence, R.string.unit_cadence);
                    break;
                case MotionEventCompat.AXIS_GENERIC_14 /* 45 */:
                    addGhost(service, sb, ((GhostWorkout) VirtualCoach.getVirtualWorkout()).getAverageHeartRate(), LiveRide.getAverageHeartRate(), R.string.tts_avg_hr, R.string.unit_heart_rate);
                    break;
                case MotionEventCompat.AXIS_GENERIC_15 /* 46 */:
                case MotionEventCompat.AXIS_GENERIC_16 /* 47 */:
                    addGhost(service, sb, ((GhostWorkout) VirtualCoach.getVirtualWorkout()).getAveragePower(), LiveRide.getAveragePower(), R.string.tts_avg_power, R.string.power_unit);
                    break;
                case Bar.DEFAULT_HEIGHT /* 48 */:
                    addGhostAvgSpeed(service, sb);
                    break;
                case 49:
                    addGhostAvgPace(service, sb);
                    break;
                case 50:
                    addGhost(service, sb, ((GhostWorkout) VirtualCoach.getVirtualWorkout()).getAverageCadence(), LiveRide.getAverageCadence(), R.string.tts_avg_cadence, R.string.unit_cadence_run);
                    break;
            }
        }
    }
}
