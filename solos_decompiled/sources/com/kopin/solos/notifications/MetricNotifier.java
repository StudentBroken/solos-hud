package com.kopin.solos.notifications;

import android.os.Handler;
import android.util.Log;
import com.kopin.solos.AppService;
import com.kopin.solos.pages.PageNav;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.MetricType;
import com.kopin.solos.util.TTSHelper;
import com.kopin.solos.virtualworkout.VirtualCoach;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes37.dex */
public class MetricNotifier {
    private static MetricNotifier self;
    private final AppService mApp;
    private int mTTSInterval;
    private List<String> mPages = new ArrayList();
    private Runnable mInfoTTS = new Runnable() { // from class: com.kopin.solos.notifications.MetricNotifier.1
        @Override // java.lang.Runnable
        public void run() {
            if (!Prefs.isReady()) {
                MetricNotifier.this.mPages.clear();
                return;
            }
            boolean isSingleMetrics = Prefs.isSingleMetrics();
            if (MetricNotifier.this.mPages.isEmpty()) {
                if (LiveRide.isFunctionalThresholdPowerMode()) {
                    PageNav.refreshPages(PageNav.PageMode.FUNCTIONAL_THRESHOLD_POWER);
                    MetricNotifier.this.mPages.addAll(PageNav.getAllMetricResources());
                } else if (LiveRide.isVirtualWorkout()) {
                    for (MetricType metricType : VirtualCoach.getAllMetrics()) {
                        MetricNotifier.this.mPages.add(metricType.getResource());
                    }
                } else if (!isSingleMetrics) {
                    MetricNotifier.this.mPages.addAll(PageNav.getAllMetricResources());
                } else {
                    List<MetricType> selected = LiveRide.isGhostWorkout() ? Prefs.getSingleMetricGhostChoices() : Prefs.getSingleMetricChoices();
                    if (!selected.contains(MetricType.TIME.getResource())) {
                        MetricNotifier.this.mPages.add(MetricType.TIME.getResource());
                    }
                    for (MetricType metricType2 : selected) {
                        MetricNotifier.this.mPages.add(metricType2.getResource());
                    }
                }
            }
            if (!MetricNotifier.this.mPages.isEmpty()) {
                String page = (String) MetricNotifier.this.mPages.remove(0);
                String tts = TTSHelper.getMetricTTS(MetricNotifier.this.mApp.getHardwareReceiverService(), page);
                if (tts == null || tts.trim().isEmpty()) {
                    MetricNotifier.this.mTTSHandler.postDelayed(this, MetricNotifier.this.mPages.isEmpty() ? MetricNotifier.this.mTTSInterval : 0L);
                    return;
                }
                if (LiveRide.isActiveRide() && LiveRide.isStartedAndRunning()) {
                    boolean ttsPlayed = MetricNotifier.this.mApp.speakTextIfQuiet(MetricNotifier.this.mPages.isEmpty() ? AppService.UPDATE_TTS : AppService.MULTI_TTS, tts);
                    if (!ttsPlayed) {
                        MetricNotifier.this.mPages.add(0, page);
                        MetricNotifier.this.mTTSHandler.postDelayed(this, 1000L);
                    }
                }
            }
        }
    };
    private final Handler mTTSHandler = new Handler();

    private MetricNotifier(AppService appService) {
        this.mApp = appService;
    }

    public static void init(AppService appService) {
        self = new MetricNotifier(appService);
    }

    public static void start(int interval) {
        self.mTTSInterval = interval;
        self.queueUpdate();
    }

    public static void stop(boolean andClear) {
        self.mTTSHandler.removeCallbacksAndMessages(null);
        if (andClear) {
            self.mPages.clear();
        }
    }

    public static void restart() {
        self.queueUpdate();
    }

    public static void next() {
        self.immediateUpdate();
    }

    public static void update(String metric) {
        self.playUpdateFor(metric);
    }

    private void queueUpdate() {
        Log.v("TTS", "Next update in " + (this.mTTSInterval / 1000) + " seconds");
        this.mTTSHandler.postDelayed(this.mInfoTTS, this.mTTSInterval);
    }

    private void immediateUpdate() {
        Log.v("TTS", "Next metric");
        this.mTTSHandler.postDelayed(this.mInfoTTS, 100L);
    }

    private void playUpdateFor(String metric) {
        Log.v("TTS", "Update for: " + metric);
        String tts = TTSHelper.getMetricTTS(this.mApp.getHardwareReceiverService(), metric);
        if (tts != null && !tts.trim().isEmpty() && LiveRide.isActiveRide() && LiveRide.isStartedAndRunning()) {
            this.mApp.speakText(AppService.NOTIFICATION_TTS, tts);
        }
    }
}
