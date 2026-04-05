package com.kopin.solos.notifications;

import com.kopin.pupil.ui.VCNotification;
import com.kopin.solos.AppService;
import com.kopin.solos.common.SportType;
import com.kopin.solos.core.R;
import com.kopin.solos.storage.Workout;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.SplitHelper;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.util.PaceUtil;
import com.kopin.solos.util.TTSHelper;

/* JADX INFO: loaded from: classes37.dex */
public class LapNotifier {
    private static String fmtNotif = " %s%s.";
    private static String fmtTTSNotif = " %s: %s %s .";

    public static void notify(AppService service, SportType sportType, Workout.RideMode workoutMode, double lastDistance, long lastTime) {
        if (workoutMode != Workout.RideMode.FTP && workoutMode != Workout.RideMode.TRAINING) {
            long lastTimeInSeconds = (500 + lastTime) / 1000;
            sendDropDownNotification(service, getVisualNotificationText(service, sportType, lastDistance, lastTimeInSeconds));
            if (Prefs.isTTSNotificationsEnabled()) {
                service.speakText(AppService.NOTIFICATION_TTS, getTTSNotificationText(service, sportType, lastDistance, lastTimeInSeconds));
            }
        }
    }

    private static void sendDropDownNotification(AppService service, String msg) {
        service.sendDropDownNotification("lap_icon", 4000, false, (Runnable) null, VCNotification.Priority.NORMAL, new VCNotification.TextPart(msg));
    }

    private static String getVisualNotificationText(AppService service, SportType sportType, double lapDistance, long lapTime) {
        SplitHelper.SplitType splitType = SplitHelper.getSplitMode(service);
        StringBuilder builder = new StringBuilder(service.getString(R.string.laps_single));
        builder.append(".");
        if (splitType == SplitHelper.SplitType.DISTANCE) {
            appendTime(service, builder, lapTime);
        } else if (splitType == SplitHelper.SplitType.TIME) {
            appendDistance(service, builder, lapDistance);
        } else {
            appendTime(service, builder, lapTime);
            appendDistance(service, builder, lapDistance);
        }
        if (sportType == SportType.RIDE) {
            appendAvgSpeed(service, builder, lapDistance, lapTime);
        } else {
            appendAvgPace(service, builder, lapDistance, lapTime);
        }
        return builder.toString();
    }

    private static String getTTSNotificationText(AppService service, SportType sportType, double lapDistance, long lapTime) {
        SplitHelper.SplitType splitType = SplitHelper.getSplitMode(service);
        StringBuilder builder = new StringBuilder(service.getString(R.string.notification_new_lap));
        builder.append(" ");
        if (splitType == SplitHelper.SplitType.DISTANCE) {
            appendTimeTTS(service, builder, lapTime);
        } else if (splitType == SplitHelper.SplitType.TIME) {
            appendDistanceTTS(service, builder, lapDistance);
        } else {
            appendTimeTTS(service, builder, lapTime);
            appendDistanceTTS(service, builder, lapDistance);
        }
        if (sportType == SportType.RIDE) {
            appendAvgSpeedTTS(service, builder, lapDistance, lapTime);
        } else {
            appendAvgPaceTTS(service, builder, lapDistance, lapTime);
        }
        return builder.toString();
    }

    private static void appendTime(AppService service, StringBuilder sb, long lapTime) {
        long sec = lapTime % 60;
        long timeInMinutes = lapTime / 60;
        long min = timeInMinutes % 60;
        long hour = timeInMinutes / 60;
        appendValue(sb, String.format("%d:%02d:%02d", Long.valueOf(hour), Long.valueOf(min), Long.valueOf(sec)), "");
    }

    private static void appendTimeTTS(AppService service, StringBuilder sb, long lapTime) {
        sb.append(service.getString(R.string.notification_time)).append(": ").append(TTSHelper.timeAsString(service, lapTime));
    }

    private static void appendDistance(AppService service, StringBuilder sb, double distance) {
        appendValue(sb, formatDouble(Utility.convertToUserUnits(1, distance / 1000.0d)), Conversion.getUnitOfDistance(service));
    }

    private static void appendDistanceTTS(AppService service, StringBuilder sb, double distance) {
        appendTTSValue(sb, service.getString(R.string.notification_distance), formatDouble(Utility.convertToUserUnits(1, distance / 1000.0d)), Conversion.getUnitOfDistance(service));
    }

    private static void appendAvgSpeed(AppService service, StringBuilder sb, double distance, long lapTime) {
        double avgSpeed = Conversion.speedForLocale(distance / lapTime);
        appendValue(sb, formatDouble(avgSpeed), Conversion.getUnitOfSpeed(service));
    }

    private static void appendAvgSpeedTTS(AppService service, StringBuilder sb, double distance, long lapTime) {
        double avgSpeed = Conversion.speedForLocale(distance / lapTime);
        appendTTSValue(sb, service.getString(R.string.notification_avg_speed), formatDouble(avgSpeed), Conversion.getUnitOfSpeed(service));
    }

    private static void appendAvgPace(AppService service, StringBuilder sb, double distance, long lapTime) {
        double avgPace = Conversion.speedToPaceForLocale(distance / lapTime);
        appendValue(sb, PaceUtil.formatPace(avgPace), Conversion.getUnitOfPace(service));
    }

    private static void appendAvgPaceTTS(AppService service, StringBuilder sb, double distance, long lapTime) {
        double avgPace = Conversion.speedToPaceForLocale(distance / lapTime);
        appendTTSValue(sb, service.getString(R.string.notification_avg_pace), TTSHelper.addTimeDiff(avgPace, service), Conversion.getUnitOfPaceTTS(service));
    }

    private static String formatDouble(double value) {
        return String.format(value < 1.0d ? "%.2f" : "%.1f", Double.valueOf(value));
    }

    private static void appendValue(StringBuilder sb, String value1, String value2) {
        sb.append(String.format(fmtNotif, value1, value2));
    }

    private static void appendTTSValue(StringBuilder sb, String value1, String value2, String value3) {
        sb.append(String.format(fmtTTSNotif, value1, value2, value3));
    }
}
