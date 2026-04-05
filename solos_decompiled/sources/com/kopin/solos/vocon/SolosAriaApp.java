package com.kopin.solos.vocon;

import android.content.Context;
import android.os.Handler;
import com.kopin.pupil.PupilSpeechRecognizer;
import com.kopin.pupil.aria.AriaController;
import com.kopin.pupil.aria.Home;
import com.kopin.pupil.aria.VoConPolicy;
import com.kopin.pupil.aria.app.BaseAriaApp;
import com.kopin.pupil.aria.app.HeadCommand;
import com.kopin.pupil.aria.tts.AriaTTS;
import com.kopin.solos.AppService;
import com.kopin.solos.RideControl;
import com.kopin.solos.common.SportType;
import com.kopin.solos.core.R;
import com.kopin.solos.navigation.Navigator;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.storage.Workout;

/* JADX INFO: loaded from: classes37.dex */
public class SolosAriaApp extends Home implements RideControl.RideObserver {
    private static final String APP_ID = "Ride";
    public static final String APP_NAME = "ride";
    public static final AriaController.AriaAppFactory mFactory = new AriaController.AriaAppFactory() { // from class: com.kopin.solos.vocon.SolosAriaApp.1
        @Override // com.kopin.pupil.aria.AriaController.AriaAppFactory
        public BaseAriaApp createApp(Context context, BaseAriaApp.AriaAppHost appHost, PupilSpeechRecognizer.SpeechConfig speechConfig, VoConPolicy policy) {
            String unused = SolosAriaApp.sportRide = context.getString(R.string.ride).toLowerCase();
            String unused2 = SolosAriaApp.sportRun = context.getString(R.string.run).toLowerCase();
            return new SolosAriaApp(context, appHost, speechConfig, policy);
        }
    };
    private static String sportRide;
    private static String sportRun;
    private final HeadCommand HEAD_COMMAND;
    private final String[] RIDE_KEYWORDS;
    private final String[] RIDE_READY;
    private final String[] RIDE_START;
    private final String WARN_GPS_DISABLED;
    private final String WARN_GPS_NOFIX;
    private final Handler mDelayHandler;

    public SolosAriaApp(Context context, BaseAriaApp.AriaAppHost appHost, PupilSpeechRecognizer.SpeechConfig speechHost, VoConPolicy policy) {
        super(context, context.getResources().getStringArray(R.array.ride_grammar), appHost, speechHost, policy, null);
        this.HEAD_COMMAND = new HeadCommand(context.getString(R.string.ride_head));
        this.RIDE_KEYWORDS = context.getResources().getStringArray(R.array.ride_keywords);
        this.RIDE_START = context.getResources().getStringArray(R.array.ride_start_grammar);
        this.RIDE_READY = context.getResources().getStringArray(R.array.ride_ready_grammar);
        RideControl.registerObserver(this);
        addState("inride", new InRide(context.getResources()));
        addState(AppService.PAUSED_PAGE, new PausedRide(context.getResources()));
        addState("endride", new EndRide(context, context.getResources()));
        addState("saveride", new SaveRide(context, context.getResources()));
        this.mDelayHandler = new Handler();
        this.WARN_GPS_NOFIX = context.getString(R.string.dialog_no_gps);
        this.WARN_GPS_DISABLED = context.getString(R.string.dialog_gps_disabled);
    }

    @Override // com.kopin.pupil.aria.Home, com.kopin.pupil.aria.app.AppState
    protected void onEnter() {
        super.onEnter();
        addDefinition(SportType.KEY, new String[]{getSportName()});
    }

    @Override // com.kopin.pupil.aria.Home, com.kopin.pupil.aria.app.BaseAriaApp
    public void onStart() {
        if (LiveRide.isActiveRide()) {
            setAppState(LiveRide.isPaused() ? AppService.PAUSED_PAGE : "inride");
        } else {
            super.onStart();
        }
    }

    @Override // com.kopin.pupil.aria.Home, com.kopin.pupil.aria.app.BaseAriaApp, com.kopin.pupil.aria.app.AppState
    public boolean holdIdle() {
        String curState = getCurrentAppState();
        if (curState.contentEquals("idle")) {
            return super.holdIdle();
        }
        if (curState.contentEquals("EndRide")) {
            return false;
        }
        return super.holdIdle();
    }

    @Override // com.kopin.pupil.aria.Home, com.kopin.pupil.aria.app.BaseAriaApp
    public void onIdle() {
        if (LiveRide.isActiveRide()) {
            requestState(LiveRide.isPaused() ? AppService.PAUSED_PAGE : "inride");
        } else {
            super.onIdle();
        }
    }

    @Override // com.kopin.pupil.aria.Home
    protected boolean shouldIdle() {
        return !LiveRide.isActiveRide();
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp, com.kopin.pupil.aria.app.AppState
    public boolean onCommandResult(String cmd) {
        for (String t : this.RIDE_START) {
            if (cmd.contentEquals(t)) {
                if (LiveRide.isNavigtionRideMode() && !Navigator.hasRecentLocation()) {
                    sayText(AriaTTS.SayPriority.VERBOSE, Navigator.isLocationEnabled() ? this.WARN_GPS_NOFIX : this.WARN_GPS_DISABLED);
                    return false;
                }
                RideControl.ready(false);
                return true;
            }
        }
        for (String t2 : this.RIDE_READY) {
            if (cmd.contentEquals(t2)) {
                RideControl.ready(true);
                return true;
            }
        }
        return super.onCommandResult(cmd);
    }

    @Override // com.kopin.pupil.aria.Home, com.kopin.pupil.aria.app.BaseAriaApp, com.kopin.pupil.aria.app.AppState
    public boolean onASRWarn(PupilSpeechRecognizer.ASRWarnings code) {
        if (getCurrentAppState() == "inride") {
            return false;
        }
        return super.onASRWarn(code);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isIdle() {
        return getStatus().contentEquals("idle");
    }

    @Override // com.kopin.solos.RideControl.RideObserver
    public void onRideConfig(Workout.RideMode mode, long rideOrRouteId) {
    }

    @Override // com.kopin.solos.RideControl.RideObserver
    public void onRideIdle() {
        setAppState(null);
        grammarChanged(true);
    }

    @Override // com.kopin.solos.RideControl.RideObserver
    public void onRideReady(RideControl.StartMode countdownOrAuto) {
        if (countdownOrAuto == RideControl.StartMode.AUTO) {
            setAppState(AppService.PAUSED_PAGE);
            grammarChanged(false);
        }
    }

    @Override // com.kopin.solos.RideControl.RideObserver
    public void onRideStarted(Workout.RideMode mode) {
        if (!LiveRide.isActiveFtp()) {
            requestState("inride");
        }
    }

    @Override // com.kopin.solos.RideControl.RideObserver
    public boolean okToStop() {
        return true;
    }

    @Override // com.kopin.solos.RideControl.RideObserver
    public void onRideStopped(SavedWorkout ride) {
        if (!isIdle()) {
            if (ride == null) {
                setAppState(null);
            } else {
                this.mDelayHandler.postDelayed(new Runnable() { // from class: com.kopin.solos.vocon.SolosAriaApp.2
                    @Override // java.lang.Runnable
                    public void run() {
                        if (!SolosAriaApp.this.isIdle()) {
                            SolosAriaApp.this.setAppState("saveride");
                            SolosAriaApp.this.grammarChanged(false);
                        }
                    }
                }, 1000L);
            }
        }
    }

    @Override // com.kopin.solos.RideControl.RideObserver
    public void onRidePaused(boolean userOrAuto) {
        setAppState(AppService.PAUSED_PAGE);
        grammarChanged(false);
    }

    @Override // com.kopin.solos.RideControl.RideObserver
    public void onRideResumed(boolean userOrAuto) {
        setAppState("inride");
        grammarChanged(false);
    }

    @Override // com.kopin.solos.RideControl.RideObserver
    public void onNewLap(double lastDistance, long lastTime) {
    }

    @Override // com.kopin.solos.RideControl.RideObserver
    public void onCountdownStarted() {
    }

    @Override // com.kopin.solos.RideControl.RideObserver
    public void onCountdown(int counter) {
    }

    @Override // com.kopin.solos.RideControl.RideObserver
    public void onCountdownComplete(boolean wasCancelled) {
    }

    public static String getSportName() {
        switch (LiveRide.getCurrentSport()) {
            case RIDE:
                return sportRide;
            case RUN:
                return sportRun;
            default:
                return null;
        }
    }
}
