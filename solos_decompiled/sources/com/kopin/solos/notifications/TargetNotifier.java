package com.kopin.solos.notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import com.kopin.pupil.ui.VCNotification;
import com.kopin.solos.AppService;
import com.kopin.solos.HardwareReceiverService;
import com.kopin.solos.core.R;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.DataHolder;
import com.kopin.solos.storage.util.MetricType;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.util.PaceUtil;
import com.kopin.solos.util.TTSHelper;
import com.kopin.solos.virtualworkout.Target;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes37.dex */
public class TargetNotifier {
    NotificationProvider mNotificationProvider;
    private AppService mVCApp;
    private final ArrayList<TargetToCheck> mTargetsToCheck = new ArrayList<>();
    private final long OFF_TARGET_ALLOWANCE = 5000;
    private Runnable mTimerTask = new Runnable() { // from class: com.kopin.solos.notifications.TargetNotifier.1
        @Override // java.lang.Runnable
        public void run() {
            HardwareReceiverService service = TargetNotifier.this.mVCApp.getHardwareReceiverService();
            if (service != null) {
                for (int i = 0; i < TargetNotifier.this.mTargetsToCheck.size(); i++) {
                    TargetNotifier.this.tryToNotify((TargetToCheck) TargetNotifier.this.mTargetsToCheck.get(i));
                }
                TargetNotifier.this.mTargetAveNotificationHandler.postDelayed(TargetNotifier.this.mTimerTask, 1000L);
            }
        }
    };
    private final SharedPreferences.OnSharedPreferenceChangeListener targetPrefChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() { // from class: com.kopin.solos.notifications.TargetNotifier.2
        @Override // android.content.SharedPreferences.OnSharedPreferenceChangeListener
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.contentEquals(Prefs.getTargetVerbosityKey())) {
                TargetNotifier.this.onPreferenceChanged();
            }
        }
    };
    private Handler mTargetAveNotificationHandler = new Handler();

    public interface NotificationProvider {
        boolean canNotify();

        double getLiveData(MetricType metricType);

        TargetNotification getNotification();

        Target getTarget(MetricType metricType);
    }

    class TargetToCheck {
        long mAllowanceTimeStamp;
        final MetricType mMetricType;
        long mVerbosityTimestamp;

        TargetToCheck(MetricType metricType) {
            this.mMetricType = metricType;
        }
    }

    public TargetNotifier(AppService vcapp) {
        this.mVCApp = vcapp;
    }

    public void start(NotificationProvider provider, List<MetricType> metrics) {
        this.mNotificationProvider = provider;
        this.mTargetsToCheck.clear();
        for (MetricType metricType : metrics) {
            this.mTargetsToCheck.add(new TargetToCheck(metricType));
        }
        PreferenceManager.getDefaultSharedPreferences(this.mVCApp).registerOnSharedPreferenceChangeListener(this.targetPrefChangeListener);
        startVerbosityTimer();
    }

    public void stop() {
        cancelVerbosityTimer();
        PreferenceManager.getDefaultSharedPreferences(this.mVCApp).unregisterOnSharedPreferenceChangeListener(this.targetPrefChangeListener);
    }

    public void pause() {
        cancelVerbosityTimer();
    }

    public void resume() {
        startVerbosityTimer();
    }

    private void startVerbosityTimer() {
        if (!Prefs.getTargetAveVerbosity().isVerbosityOff()) {
            for (TargetToCheck targetToCheck : this.mTargetsToCheck) {
                targetToCheck.mVerbosityTimestamp = Utility.getTimeMilliseconds();
                targetToCheck.mAllowanceTimeStamp = 0L;
            }
            this.mTargetAveNotificationHandler.postDelayed(this.mTimerTask, 1000L);
        }
    }

    private void cancelVerbosityTimer() {
        this.mTargetAveNotificationHandler.removeCallbacksAndMessages(null);
    }

    public void notifyNext() {
    }

    public void clearQueue() {
        startVerbosityTimer();
    }

    public void resetVerbosityTimer() {
        cancelVerbosityTimer();
        startVerbosityTimer();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPreferenceChanged() {
        resetVerbosityTimer();
    }

    private void sendDropDownNotificationWithoutTTS(String icon, int duration, String msg) {
        this.mVCApp.sendDropDownNotification(icon, duration, false, (Runnable) null, VCNotification.Priority.NORMAL, new VCNotification.TextPart(msg));
    }

    public void updateNow(MetricType metricType) {
        Target target = getTarget(metricType);
        if (target != null) {
            double value = this.mNotificationProvider.getLiveData(metricType);
            if (DataHolder.isValid(value)) {
                if (target.isOutOfRange(value)) {
                    this.mVCApp.speakText(AppService.TARGET_TTS, getVoiceNotificationText(metricType, value));
                } else {
                    this.mVCApp.speakText(AppService.TARGET_TTS, this.mVCApp.getString(R.string.tts_notification_on_target));
                }
            }
        }
    }

    private boolean isOffTarget(MetricType metricType, double value) {
        Target target = getTarget(metricType);
        if (target == null || !DataHolder.isValid(value)) {
            return false;
        }
        return target.isOutOfRange(value);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean tryToNotify(TargetToCheck targetToCheck) {
        double value = this.mNotificationProvider.getLiveData(targetToCheck.mMetricType);
        if (isOffTarget(targetToCheck.mMetricType, value)) {
            long timeNow = Utility.getTimeMilliseconds();
            if (targetToCheck.mAllowanceTimeStamp <= 0) {
                targetToCheck.mAllowanceTimeStamp = timeNow;
                return false;
            }
            long verbosity = Prefs.getTargetAveVerbosity().getVerbosityInSeconds() * 1000;
            long verbosityDelta = timeNow - targetToCheck.mVerbosityTimestamp;
            if (verbosityDelta >= verbosity) {
                long allowedDelta = timeNow - targetToCheck.mAllowanceTimeStamp;
                if (allowedDelta >= 5000) {
                    targetToCheck.mAllowanceTimeStamp = 0L;
                    targetToCheck.mVerbosityTimestamp = timeNow;
                    if (this.mNotificationProvider.canNotify()) {
                        sendNotification(targetToCheck.mMetricType, value);
                        return false;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        targetToCheck.mAllowanceTimeStamp = 0L;
        return false;
    }

    private void sendNotification(MetricType metricType, double value) {
        if (this.mNotificationProvider.getNotification().resVisual > 0) {
            sendDropDownNotificationWithoutTTS(AppService.PUPIL_IMG, 3000, getVisualNotificationText(metricType, value));
        }
        if (Prefs.isTTSNotificationsEnabled()) {
            this.mVCApp.speakText(AppService.TARGET_TTS, getVoiceNotificationText(metricType, value));
        }
    }

    private String getVisualNotificationText(MetricType metricType, double value) {
        Target target = getTarget(metricType);
        double diff = target.getDifference(Double.valueOf(value));
        String metricName = "";
        String unit = "";
        String diffText = null;
        String textCompare = null;
        switch (metricType.getBaseMetricType()) {
            case SPEED:
                metricName = this.mVCApp.getString(R.string.speed);
                unit = this.mVCApp.getString(Prefs.isMetric() ? R.string.unit_speed_metric_short : R.string.unit_speed_imperial_short);
                diffText = new DecimalFormat("0.#").format(diff) + " " + unit;
                break;
            case PACE:
                metricName = this.mVCApp.getString(R.string.pace);
                unit = this.mVCApp.getString(Prefs.isMetric() ? R.string.unit_pace_slash_metric_short : R.string.unit_pace_slash_imperial_short);
                diffText = String.format("%s %s", PaceUtil.formatPace(diff), unit);
                textCompare = !target.isAbove(Double.valueOf(value), true) ? this.mVCApp.getString(R.string.target_notification_slower) : this.mVCApp.getString(R.string.target_notification_faster);
                break;
            case HEARTRATE:
                metricName = this.mVCApp.getString(R.string.heartrate);
                unit = this.mVCApp.getString(R.string.heart_unit);
                break;
            case POWER:
            case KICK:
                metricName = this.mVCApp.getString(R.string.power);
                unit = this.mVCApp.getString(R.string.power_unit);
                break;
            case CADENCE:
                metricName = this.mVCApp.getString(R.string.cadence);
                unit = this.mVCApp.getString(R.string.cadence_unit);
                break;
            case STEP:
                metricName = this.mVCApp.getString(R.string.cadence);
                unit = this.mVCApp.getString(R.string.unit_cadence_run_short);
                break;
        }
        if (diffText == null) {
            diffText = diff < 1.0d ? String.format("%.1f %s", Double.valueOf(diff), unit) : String.format("%d %s", Long.valueOf((long) diff), unit);
        }
        if (textCompare == null) {
            textCompare = target.isAbove(Double.valueOf(value), false) ? this.mVCApp.getString(R.string.target_notification_above) : this.mVCApp.getString(R.string.target_notification_below);
        }
        String textRange = target.hasRange ? this.mVCApp.getString(R.string.notification_range) : this.mVCApp.getString(R.string.notification_target);
        return String.format(getVisualFormat(), diffText, textCompare, metricName, textRange);
    }

    private String getVoiceNotificationText(MetricType metricType, double value) {
        Target target = getTarget(metricType);
        double diff = target.getDifference(Double.valueOf(value));
        String metricName = "";
        String unit = "";
        String diffText = null;
        String textCompare = null;
        switch (metricType.getBaseMetricType()) {
            case SPEED:
                metricName = this.mVCApp.getString(R.string.speed);
                unit = this.mVCApp.getString(Prefs.isMetric() ? R.string.unit_speed_metric : R.string.unit_speed_imperial);
                diffText = new DecimalFormat("0.#").format(diff) + " " + unit;
                break;
            case PACE:
                metricName = this.mVCApp.getString(R.string.pace);
                unit = this.mVCApp.getString(Prefs.isMetric() ? R.string.unit_pace_metric : R.string.unit_pace_imperial);
                diffText = TTSHelper.addTimeDiff(diff, this.mVCApp);
                textCompare = !target.isAbove(Double.valueOf(value), true) ? this.mVCApp.getString(R.string.target_notification_slower) : this.mVCApp.getString(R.string.target_notification_faster);
                break;
            case HEARTRATE:
                metricName = this.mVCApp.getString(R.string.heartrate);
                unit = this.mVCApp.getString(R.string.unit_heart_rate);
                break;
            case POWER:
            case KICK:
                metricName = this.mVCApp.getString(R.string.power);
                unit = this.mVCApp.getString(R.string.power_unit);
                break;
            case CADENCE:
                metricName = this.mVCApp.getString(R.string.cadence);
                unit = this.mVCApp.getString(R.string.unit_cadence);
                break;
            case STEP:
                metricName = this.mVCApp.getString(R.string.cadence);
                unit = this.mVCApp.getString(R.string.unit_cadence_run);
                break;
            case DISTANCE:
                metricName = this.mVCApp.getString(R.string.distance);
                unit = this.mVCApp.getString(Prefs.isMetric() ? R.string.unit_distance_metric : R.string.unit_distance_imperial);
                break;
        }
        if (diffText == null) {
            diffText = diff < 1.0d ? String.format("%.1f %s", Double.valueOf(diff), unit) : String.format("%d %s", Long.valueOf((long) diff), unit);
        }
        if (textCompare == null) {
            textCompare = target.isAbove(Double.valueOf(value), false) ? this.mVCApp.getString(R.string.target_notification_above) : this.mVCApp.getString(R.string.target_notification_below);
        }
        String textRange = target.hasRange ? this.mVCApp.getString(R.string.notification_range) : this.mVCApp.getString(R.string.notification_target);
        return String.format(getVoiceFormat(), diffText, textCompare, metricName, textRange);
    }

    public static String getUnitForTTS(Context context, MetricType metricType) {
        switch (metricType.getBaseMetricType()) {
            case SPEED:
                return context.getString(Prefs.isMetric() ? R.string.unit_speed_metric : R.string.unit_speed_imperial);
            case PACE:
                return context.getString(Prefs.isMetric() ? R.string.unit_pace_metric : R.string.unit_pace_imperial);
            case HEARTRATE:
                return context.getString(R.string.unit_heart_rate);
            case POWER:
            case KICK:
                return context.getString(R.string.power_unit);
            case CADENCE:
                return context.getString(R.string.unit_cadence);
            case STEP:
                return context.getString(R.string.unit_cadence_run);
            default:
                return null;
        }
    }

    private Target getTarget(MetricType metricType) {
        return this.mNotificationProvider.getTarget(metricType);
    }

    private String getVisualFormat() {
        return this.mVCApp.getString(this.mNotificationProvider.getNotification().resVisual);
    }

    private String getVoiceFormat() {
        return this.mVCApp.getString(this.mNotificationProvider.getNotification().resVoice);
    }
}
