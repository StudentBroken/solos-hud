package com.kopin.solos;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import com.kopin.peloton.PelotonPrefs;
import com.kopin.solos.common.config.MetricDataType;
import com.kopin.solos.notifications.LapNotifier;
import com.kopin.solos.share.Platforms;
import com.kopin.solos.share.ShareHelper;
import com.kopin.solos.share.Sync;
import com.kopin.solos.storage.FTPHelper;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.Ride;
import com.kopin.solos.storage.Rider;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.SavedRides;
import com.kopin.solos.storage.SavedTraining;
import com.kopin.solos.storage.SavedTrainingWorkouts;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.storage.ShareMap;
import com.kopin.solos.storage.Workout;
import com.kopin.solos.storage.settings.ConfigMetrics;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.SplitHelper;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.util.CorrectedElevationHelper;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes37.dex */
public class RideControl {
    public static final int COUNTER_DEFAULT = 3;
    private static final int COUNTER_NONE = 0;
    private static final String INTENT_ACTION_FTP_SUMMARY = "com.kopin.solos.FTP_SUMMARY";
    private static final String INTENT_ACTION_IDLE = "com.kopin.solos.IDLE";
    private static final String INTENT_ACTION_WORKOUT_PREVIEW = "com.kopin.solos.PREVIEW";
    private static final String INTENT_ACTION_WORKOUT_SUMMARY = "com.kopin.solos.END_WORKOUT";
    private static final String TAG = "RideControl";
    private static AppService mAppService;
    private static Handler mCountdownHandler;
    private static Handler mDispatchHandler;
    private static Handler mSpeedCheckerHandler;
    private static ArrayList<RideObserver> mObservers = new ArrayList<>();
    private static int mCounter = 0;
    private static final SplitHelper.SplitListener mSplitListener = new SplitHelper.SplitListener() { // from class: com.kopin.solos.RideControl.3
        @Override // com.kopin.solos.storage.util.SplitHelper.SplitListener
        public boolean isLastLap() {
            return false;
        }

        @Override // com.kopin.solos.storage.util.SplitHelper.SplitListener
        public void onSplit(long startTime, double splitDistance, long splitTime, boolean isEnd) {
            if (!isEnd) {
                LapNotifier.notify(RideControl.mAppService, LiveRide.getCurrentSport(), LiveRide.getMode(), splitDistance, splitTime);
                RideControl.mDispatch.onNewLap(splitDistance, splitTime);
            }
        }
    };
    private static final Runnable mAutoPauseChecker = new Runnable() { // from class: com.kopin.solos.RideControl.4
        @Override // java.lang.Runnable
        public void run() {
            RideControl.checkSpeed(RideControl.mAppService.getHardwareReceiverService().getLastSpeed());
            RideControl.mSpeedCheckerHandler.postDelayed(this, 2000L);
        }
    };
    private static final Runnable mCountdownTimer = new Runnable() { // from class: com.kopin.solos.RideControl.5
        @Override // java.lang.Runnable
        public void run() {
            if (RideControl.mCounter <= 0) {
                RideControl.endCountdown();
                return;
            }
            RideControl.mDispatch.onCountdown(RideControl.mCounter);
            RideControl.access$410();
            RideControl.mCountdownHandler.postDelayed(this, 1000L);
        }
    };
    private static final RideObserver mDispatch = new RideObserver() { // from class: com.kopin.solos.RideControl.6
        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRideConfig(final Workout.RideMode mode, final long rideOrRouteId) {
            RideControl.mDispatchHandler.post(new Runnable() { // from class: com.kopin.solos.RideControl.6.1
                @Override // java.lang.Runnable
                public void run() {
                    synchronized (RideControl.mObservers) {
                        for (RideObserver o : RideControl.mObservers) {
                            o.onRideConfig(mode, rideOrRouteId);
                        }
                    }
                }
            });
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRideIdle() {
            RideControl.mDispatchHandler.post(new Runnable() { // from class: com.kopin.solos.RideControl.6.2
                @Override // java.lang.Runnable
                public void run() {
                    synchronized (RideControl.mObservers) {
                        for (RideObserver o : RideControl.mObservers) {
                            o.onRideIdle();
                        }
                    }
                }
            });
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRideReady(final StartMode startMode) {
            RideControl.mDispatchHandler.post(new Runnable() { // from class: com.kopin.solos.RideControl.6.3
                @Override // java.lang.Runnable
                public void run() {
                    synchronized (RideControl.mObservers) {
                        for (RideObserver o : RideControl.mObservers) {
                            o.onRideReady(startMode);
                        }
                    }
                }
            });
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRideStarted(final Workout.RideMode mode) {
            RideControl.mDispatchHandler.post(new Runnable() { // from class: com.kopin.solos.RideControl.6.4
                @Override // java.lang.Runnable
                public void run() {
                    synchronized (RideControl.mObservers) {
                        for (RideObserver o : RideControl.mObservers) {
                            o.onRideStarted(mode);
                        }
                    }
                }
            });
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public boolean okToStop() {
            synchronized (RideControl.mObservers) {
                for (RideObserver o : RideControl.mObservers) {
                    if (!o.okToStop()) {
                        return false;
                    }
                }
                return true;
            }
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRideStopped(final SavedWorkout ride) {
            RideControl.mDispatchHandler.post(new Runnable() { // from class: com.kopin.solos.RideControl.6.5
                @Override // java.lang.Runnable
                public void run() {
                    synchronized (RideControl.mObservers) {
                        for (RideObserver o : RideControl.mObservers) {
                            o.onRideStopped(ride);
                        }
                    }
                }
            });
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRidePaused(final boolean userOrAuto) {
            RideControl.mDispatchHandler.post(new Runnable() { // from class: com.kopin.solos.RideControl.6.6
                @Override // java.lang.Runnable
                public void run() {
                    synchronized (RideControl.mObservers) {
                        for (RideObserver o : RideControl.mObservers) {
                            o.onRidePaused(userOrAuto);
                        }
                    }
                }
            });
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRideResumed(final boolean userOrAuto) {
            RideControl.mDispatchHandler.post(new Runnable() { // from class: com.kopin.solos.RideControl.6.7
                @Override // java.lang.Runnable
                public void run() {
                    synchronized (RideControl.mObservers) {
                        for (RideObserver o : RideControl.mObservers) {
                            o.onRideResumed(userOrAuto);
                        }
                    }
                }
            });
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onNewLap(final double lastDistance, final long lastTime) {
            RideControl.mDispatchHandler.post(new Runnable() { // from class: com.kopin.solos.RideControl.6.8
                @Override // java.lang.Runnable
                public void run() {
                    synchronized (RideControl.mObservers) {
                        for (RideObserver o : RideControl.mObservers) {
                            o.onNewLap(lastDistance, lastTime);
                        }
                    }
                }
            });
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onCountdownStarted() {
            RideControl.mDispatchHandler.post(new Runnable() { // from class: com.kopin.solos.RideControl.6.9
                @Override // java.lang.Runnable
                public void run() {
                    synchronized (RideControl.mObservers) {
                        for (RideObserver o : RideControl.mObservers) {
                            o.onCountdownStarted();
                        }
                    }
                }
            });
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onCountdown(final int counter) {
            RideControl.mDispatchHandler.post(new Runnable() { // from class: com.kopin.solos.RideControl.6.10
                @Override // java.lang.Runnable
                public void run() {
                    synchronized (RideControl.mObservers) {
                        for (RideObserver o : RideControl.mObservers) {
                            o.onCountdown(counter);
                        }
                    }
                }
            });
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onCountdownComplete(final boolean wasCancelled) {
            RideControl.mDispatchHandler.post(new Runnable() { // from class: com.kopin.solos.RideControl.6.11
                @Override // java.lang.Runnable
                public void run() {
                    synchronized (RideControl.mObservers) {
                        for (RideObserver o : RideControl.mObservers) {
                            o.onCountdownComplete(wasCancelled);
                        }
                    }
                }
            });
        }
    };

    public interface RideObserver {
        boolean okToStop();

        void onCountdown(int i);

        void onCountdownComplete(boolean z);

        void onCountdownStarted();

        void onNewLap(double d, long j);

        void onRideConfig(Workout.RideMode rideMode, long j);

        void onRideIdle();

        void onRidePaused(boolean z);

        void onRideReady(StartMode startMode);

        void onRideResumed(boolean z);

        void onRideStarted(Workout.RideMode rideMode);

        void onRideStopped(SavedWorkout savedWorkout);
    }

    public enum StartMode {
        MANUAL,
        AUTO,
        COUNTDOWN
    }

    static /* synthetic */ int access$410() {
        int i = mCounter;
        mCounter = i - 1;
        return i;
    }

    public static void init(AppService appService, SplitHelper splitHelper) {
        mAppService = appService;
        mDispatchHandler = new Handler();
        mSpeedCheckerHandler = new Handler();
        mCountdownHandler = new Handler();
        splitHelper.addSplitListener(mSplitListener);
    }

    public static void registerObserver(RideObserver obs) {
        synchronized (mObservers) {
            mObservers.add(obs);
        }
    }

    public static void unregisterObserver(RideObserver obs) {
        synchronized (mObservers) {
            mObservers.remove(obs);
        }
    }

    public static boolean isGPSAvailable() {
        return mAppService.getHardwareReceiverService().isGPSEnabled();
    }

    public static void setWorkoutMode(Workout.RideMode mode, long virtualCoachId) {
        LiveRide.setMode(mode, virtualCoachId);
        mDispatch.onRideConfig(mode, virtualCoachId);
    }

    public static void setNavigationRouteId(long routeId) {
        if (routeId != -1) {
            LiveRide.setMode(Workout.RideMode.ROUTE, routeId);
            mDispatch.onRideConfig(Workout.RideMode.ROUTE, routeId);
        } else {
            LiveRide.resetMode();
            mDispatch.onRideConfig(Workout.RideMode.NORMAL, routeId);
        }
    }

    public static void reset() {
        LiveRide.reset();
        mDispatch.onRideIdle();
    }

    public static void ready(boolean autoStart) {
        StartMode mode;
        if (LiveRide.getMode() == Workout.RideMode.NORMAL && Prefs.isAnyTargetEnabled()) {
            setWorkoutMode(Workout.RideMode.TARGETS, -1L);
        }
        if (LiveRide.isActiveRide()) {
            Log.e(TAG, "ready called, but ride is already active!");
            return;
        }
        if (autoStart) {
            autoStart = Prefs.isAutoStartPossible();
        }
        boolean countdown = Prefs.isCountdownActive();
        if (autoStart) {
            mode = StartMode.AUTO;
        } else {
            mode = countdown ? StartMode.COUNTDOWN : StartMode.MANUAL;
        }
        Utility.markReady(mAppService, true, HardwareReceiverService.class);
        mDispatch.onRideReady(mode);
    }

    public static void requestStart() {
        beginCountdown();
    }

    private static void start(Workout.RideMode mode) {
        if (LiveRide.isActiveRide()) {
            Log.e(TAG, "start called, but ride is already active!");
            return;
        }
        mSpeedCheckerHandler.postDelayed(mAutoPauseChecker, 100L);
        LiveRide.start(mode);
        mDispatch.onRideStarted(mode);
    }

    public static void stop(boolean discard) {
        if (discard || mDispatch.okToStop()) {
            boolean isFTP = LiveRide.isActiveFtp();
            SavedWorkout ride = LiveRide.stop();
            if (discard && ride != null) {
                SavedRides.deleteWorkout(ride);
                SQLHelper.removeIncompleteWorkouts();
                ride = null;
            }
            Utility.markReady(mAppService, false, HardwareReceiverService.class);
            mDispatch.onRideStopped(ride);
            if (ride != null) {
                ride.setBikeId(Prefs.getChosenBikeId());
                ride.setActivity(Prefs.getDefaultRideType());
                Rider rider = SQLHelper.getLatestRider();
                ride.setRiderId(rider != null ? rider.getId() : 0L);
                if (isFTP) {
                    sendOpenFTPResultBroadcast(ride);
                } else {
                    sendOpenWorkoutBroadcast(ride);
                }
            }
        }
    }

    public static void stop(boolean discard, RideObserver toBeRemovedObserver) {
        stop(discard);
        unregisterObserver(toBeRemovedObserver);
    }

    private static void sendOpenFTPResultBroadcast(SavedWorkout workout) {
        reset();
        if (Ride.hasData(workout.getAveragePower())) {
            float ftp = (float) FTPHelper.calculateFunctionalThresholdPower(workout.getAveragePower());
            Sync.addFTP(FTPHelper.setFtp(System.currentTimeMillis(), ftp));
            Intent intent = new Intent(INTENT_ACTION_FTP_SUMMARY);
            intent.putExtra("ride_id", workout.getId());
            intent.setFlags(335544320);
            mAppService.getHardwareReceiverService().startActivity(intent);
            return;
        }
        SavedRides.deleteWorkout(workout);
        Toast.makeText(mAppService.getHardwareReceiverService(), com.kopin.solos.core.R.string.profile_detail_ftp_result_error, 0).show();
    }

    private static void sendOpenWorkoutBroadcast(SavedWorkout workout) {
        Log.d(TAG, "openRide: " + workout.getId());
        Intent intent = new Intent(INTENT_ACTION_WORKOUT_SUMMARY);
        intent.putExtra("ride_id", workout.getId());
        intent.putExtra(ThemeActivity.EXTRA_WORKOUT_TYPE, workout.getSportType().name());
        intent.putExtra(ThemeActivity.EXTRA_NEW_RIDE, true);
        intent.putExtra(ThemeActivity.EXTRA_RIDE_ACTIVE, LiveRide.isActiveRide());
        intent.setFlags(335544320);
        mAppService.getHardwareReceiverService().startActivity(intent);
    }

    public static void saveWorkout(boolean syncToCloud) {
        SavedWorkout ride = LiveRide.lastRide();
        ride.setTitle(ride.getOrGenerateTitle());
        SavedRides.updateWorkout(ride);
        if (syncToCloud) {
            syncWorkout(ride, true, null);
        }
        sendWorkoutSummaryBroadcast(ride);
    }

    public static void discardWorkout() {
        SavedWorkout lastRide = LiveRide.lastRide();
        if (lastRide != null) {
            SavedRides.deleteWorkout(lastRide.getSportType(), lastRide.getId());
        }
        SQLHelper.removeIncompleteWorkouts();
        sendWorkoutInactive();
    }

    private static void sendWorkoutSummaryBroadcast(SavedWorkout workout) {
        Intent intent = new Intent(INTENT_ACTION_WORKOUT_PREVIEW);
        intent.putExtra("ride_id", workout.getId());
        intent.putExtra(ThemeActivity.EXTRA_NEW_RIDE, false);
        intent.setFlags(335544320);
        mAppService.getHardwareReceiverService().startActivity(intent);
    }

    private static void sendWorkoutInactive() {
        Intent intent = new Intent(INTENT_ACTION_IDLE);
        intent.setFlags(335544320);
        mAppService.getHardwareReceiverService().startActivity(intent);
    }

    public static void saveWorkout(boolean syncToCloud, SavedWorkout ride, CorrectedElevationHelper.CorrectedElevationCallback elevationCallback) {
        SavedRides.updateWorkout(ride);
        if (syncToCloud) {
            syncWorkout(ride, true, elevationCallback);
        }
    }

    public static void updateWorkout(SavedWorkout workout, CorrectedElevationHelper.CorrectedElevationCallback elevationCallback) {
        syncWorkout(workout, false, elevationCallback);
    }

    private static void syncWorkout(final SavedWorkout workout, final boolean newWorkout, final CorrectedElevationHelper.CorrectedElevationCallback elevationCallback) {
        if (!Utility.isNetworkAvailable(mAppService)) {
            Log.d(TAG, "Network not available to share workout");
            return;
        }
        boolean needCorrectedElevation = ConfigMetrics.isMetricEnabled(MetricDataType.ELEVATION) && Ride.hasData(workout.getGainedAltitude()) && workout.hasRoute() && !workout.hasCorrectedElevation();
        if (needCorrectedElevation) {
            CorrectedElevationHelper.getCorrectedElevation(workout, new CorrectedElevationHelper.CorrectedElevationCallback() { // from class: com.kopin.solos.RideControl.1
                @Override // com.kopin.solos.util.CorrectedElevationHelper.CorrectedElevationCallback
                public void onComplete(boolean wasSuccess) {
                    RideControl.syncWorkout(workout, newWorkout);
                    if (elevationCallback != null) {
                        elevationCallback.onComplete(wasSuccess);
                    }
                }
            });
        } else if (newWorkout) {
            syncWorkout(workout, newWorkout);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void syncWorkout(SavedWorkout workout, boolean newWorkout) {
        if (newWorkout) {
            Sync.addRide(workout);
            shareRide(workout);
        } else {
            Sync.updateRide(workout);
        }
    }

    private static void shareRide(SavedWorkout workout) {
        if (Platforms.Strava.hasAutoShare(mAppService) && !Platforms.Strava.isShared(workout.getId())) {
            shareRide(workout, Platforms.Strava);
        }
        boolean isTrainingWorkout = workout.getWorkoutMode() == Workout.RideMode.TRAINING;
        if ((Platforms.TrainingPeaks.hasAutoShare(mAppService) || isTrainingWorkout) && !Platforms.TrainingPeaks.isShared(workout.getId())) {
            shareRide(workout, Platforms.TrainingPeaks);
        }
        if (Platforms.UnderArmour.hasAutoShare(mAppService) && !Platforms.UnderArmour.isShared(workout.getId())) {
            shareRide(workout, Platforms.UnderArmour);
        }
        if (isTrainingWorkout) {
            SavedTraining training = SavedTrainingWorkouts.get(workout.getVirtualWorkoutId());
            ShareMap.addTraining(training, PelotonPrefs.getEmail(), Platforms.TrainingPeaks.getSharedKey());
        }
    }

    private static void shareRide(final SavedWorkout workout, final Platforms platform) {
        if (platform.isLoggedIn(mAppService)) {
            new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.kopin.solos.RideControl.2
                @Override // java.lang.Runnable
                public void run() {
                    ShareHelper.upload(RideControl.mAppService, workout, new ShareHelper.UploadListener() { // from class: com.kopin.solos.RideControl.2.1
                        @Override // com.kopin.solos.share.ShareHelper.UploadListener, com.kopin.solos.share.ShareHelper.ShareProgressListener
                        public void onProgress(Platforms which, ShareHelper.ShareProgress progress) {
                            Log.d(RideControl.TAG, which.name() + " Share Workout: " + progress);
                        }
                    }, platform);
                }
            });
        }
    }

    public static void pause() {
        pause(true);
    }

    private static void pause(boolean userOrAuto) {
        LiveRide.pause();
        mDispatch.onRidePaused(userOrAuto);
    }

    public static void requestResume() {
        beginCountdown();
    }

    private static void resume() {
        LiveRide.resume();
        mDispatch.onRideResumed(true);
    }

    static void checkCadence(double value) {
        if (!LiveRide.isStarted() && value > 0.0d && Prefs.isReady() && Prefs.isAutoStartPossible()) {
            start(Workout.RideMode.NORMAL);
        }
    }

    static void checkSpeed(double valueMPS) {
        if (!LiveRide.isStarted()) {
            if (valueMPS > 0.0d && Prefs.isReady() && Prefs.isAutoStartPossible()) {
                start(Workout.RideMode.NORMAL);
                return;
            }
            return;
        }
        if (!Prefs.isAutoPauseEnabled() || LiveRide.isFunctionalThresholdPowerMode()) {
            return;
        }
        if ((valueMPS < Prefs.getAutoPauseSpeedMPS() || valueMPS == -2.147483648E9d) && !LiveRide.isPaused()) {
            pause(false);
        } else if (valueMPS >= Prefs.getAutoPauseSpeedMPS() && LiveRide.isPaused() && valueMPS != -2.147483648E9d) {
            resume();
        }
    }

    private static void beginCountdown() {
        if (!Prefs.isCountdownActive()) {
            mDispatch.onCountdownComplete(false);
            if (LiveRide.isActiveRide()) {
                resume();
                return;
            } else {
                start(Workout.RideMode.NORMAL);
                return;
            }
        }
        mCounter = 3;
        mCountdownHandler.postDelayed(mCountdownTimer, 100L);
        mDispatch.onCountdownStarted();
    }

    public static void cancelCountdown() {
        mCounter = 0;
        mCountdownHandler.removeCallbacksAndMessages(null);
        if (!LiveRide.isActiveRide()) {
            Utility.markReady(mAppService, false, HardwareReceiverService.class);
        }
        mDispatch.onCountdownComplete(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void endCountdown() {
        mDispatch.onCountdownComplete(false);
        if (LiveRide.isActiveRide()) {
            resume();
        } else {
            start(Workout.RideMode.NORMAL);
        }
    }

    public static boolean hasCountdown() {
        return mCounter != 0;
    }
}
