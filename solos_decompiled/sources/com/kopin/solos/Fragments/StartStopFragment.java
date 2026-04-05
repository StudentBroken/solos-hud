package com.kopin.solos.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.digits.sdk.vcard.VCardConfig;
import com.kopin.groupcom.GroupCom;
import com.kopin.groupcom.GroupComListActivity;
import com.kopin.peloton.groupcom.ChatGroup;
import com.kopin.pupil.PupilDevice;
import com.kopin.pupil.update.util.FirmwareFlash;
import com.kopin.solos.FTP;
import com.kopin.solos.FinishRideActivity;
import com.kopin.solos.HardwareReceiverService;
import com.kopin.solos.MainActivity;
import com.kopin.solos.R;
import com.kopin.solos.RideControl;
import com.kopin.solos.SensorWarnings;
import com.kopin.solos.ThemeActivity;
import com.kopin.solos.ThemeUtil;
import com.kopin.solos.common.DialogUtils;
import com.kopin.solos.common.SafeFragment;
import com.kopin.solos.common.SportType;
import com.kopin.solos.common.permission.Permission;
import com.kopin.solos.common.permission.PermissionUtil;
import com.kopin.solos.config.Features;
import com.kopin.solos.customisableridemetrics.CustomiseableRideMetrics;
import com.kopin.solos.menu.CustomActionProvider;
import com.kopin.solos.menu.CustomMenuItem;
import com.kopin.solos.menu.NavigationMenuAdapter;
import com.kopin.solos.menu.TextMenuAdapter;
import com.kopin.solos.navigation.Navigator;
import com.kopin.solos.pages.PageNav;
import com.kopin.solos.sensors.Sensor;
import com.kopin.solos.share.Config;
import com.kopin.solos.share.Sync;
import com.kopin.solos.storage.Bike;
import com.kopin.solos.storage.Bikes;
import com.kopin.solos.storage.DataListener;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.Ride;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.storage.Workout;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.settings.UserProfile;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.RecoveryUtil;
import com.kopin.solos.storage.util.SplitHelper;
import com.kopin.solos.storage.util.TimeHelper;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.update.FlashActivity;
import com.kopin.solos.util.PaceUtil;
import com.kopin.solos.util.RunType;
import com.kopin.solos.view.PagerContainer;
import com.kopin.solos.virtualworkout.Trainer;
import com.kopin.solos.virtualworkout.VirtualCoach;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/* JADX INFO: loaded from: classes24.dex */
public class StartStopFragment extends SafeFragment implements HardwareReceiverService.InfoListener, AdapterView.OnItemSelectedListener, HardwareReceiverService.HeadsetStateListener, RideControl.RideObserver, GroupCom.GroupComListener {
    private static final int MENU_ITEM_ID_ADDRESS = 1;
    private static final int MENU_ITEM_ID_CANCEL = 0;
    private static final int MENU_ITEM_ID_GHOST = 4;
    private static final int MENU_ITEM_ID_RIDE = 3;
    private static final int MENU_ITEM_ID_ROUTE = 2;
    private static final int MENU_ITEM_ID_TARGET = 5;
    private static final int MENU_ITEM_ID_TRAINING = 6;
    private static final String TAG = "StartStopFrag";
    private static HeadsetState mHeadsetState = HeadsetState.UNKNOWN;
    private static boolean sPromptFirmwareUpdate = true;
    private CustomiseableRideMetrics customiseableRideMetrics;
    private ImageView imgBackground;
    private View layoutFTPMode;
    private View layoutNormalMode;
    private View layoutTimerFTP;
    private View layoutTimerNormal;
    private CustomActionProvider.DefaultActionView mActionViewRideMode;
    private CountdownDialog mAutoStartDialog;
    private Button mBikeButton;
    private long mBikeId;
    private ViewGroup mButtonBar;
    private ViewGroup mButtonBar1;
    private ClockTask mClockUpdater;
    private CountdownDialog mCountdownDialog;
    private MenuItem mMenuItemGroupChat;
    private Button mPauseButton;
    private Button mRecordButton;
    private CustomActionProvider<NavigationMenuAdapter.NavigationMenuItem> mRideModeProvider;
    private ProgressDialog mRideRecoveryDialog;
    private ImageButton mScreenLockButton;
    private HardwareReceiverService mService;
    private PopupMenu mSportSelectionMenu;
    private int mSportType;
    private Button mStopButton;
    private TextView mTimerText;
    private View mView;
    private AlertDialog stopFtpDialog;
    private TextView textGroupComStatus;
    private TextView txtFtpAvePowerValue;
    private TextView txtFtpDurationValue;
    private TextView txtFtpPowerValue;
    private TextView txtHeadsetState;
    private boolean menuShowing = false;
    private boolean mBound = false;
    private TimeHelper mTimeHelper = new TimeHelper();
    private Handler handlerHeadsetState = new Handler();
    private Timer mClock = new Timer();
    private Handler mButtonTimeHandler = new Handler();
    private boolean ftpMode = false;
    private boolean mScreenLocked = true;
    private DialogInterface.OnCancelListener mAutoStartCancel = new DialogInterface.OnCancelListener() { // from class: com.kopin.solos.Fragments.StartStopFragment.33
        @Override // android.content.DialogInterface.OnCancelListener
        public void onCancel(DialogInterface dialog) {
            RideControl.stop(true);
        }
    };
    private DialogInterface.OnCancelListener mCountdownCancel = new DialogInterface.OnCancelListener() { // from class: com.kopin.solos.Fragments.StartStopFragment.34
        @Override // android.content.DialogInterface.OnCancelListener
        public void onCancel(DialogInterface dialog) {
            RideControl.cancelCountdown();
        }
    };
    private DataListener mDataListener = new DataListener() { // from class: com.kopin.solos.Fragments.StartStopFragment.35
        @Override // com.kopin.solos.storage.DataListener
        public void onButtonPress(Sensor.ButtonAction action) {
            if (action.equals(Sensor.ButtonAction.MENU)) {
                if (StartStopFragment.this.mTimeHelper.isPaused()) {
                    RideControl.requestResume();
                } else {
                    RideControl.pause();
                }
            }
        }
    };
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private long mThreadId = Looper.getMainLooper().getThread().getId();

    private enum HeadsetState {
        UNKNOWN,
        CONNECTED,
        CONNECTING,
        DISCONNECTED
    }

    public interface ServiceActivity {
        void getService(ServiceCallback serviceCallback);
    }

    public interface ServiceCallback {
        void onService(HardwareReceiverService hardwareReceiverService);
    }

    public static StartStopFragment newInstance(boolean startFTP) {
        StartStopFragment self = new StartStopFragment();
        self.ftpMode = startFTP;
        return self;
    }

    @Override // com.kopin.solos.common.SafeFragment, android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isDetached() && this.mService != null) {
            this.mService.addDataListener(this.mDataListener);
            this.mService.setHeadsetCallback(this);
        }
        SensorWarnings.init(getActivity());
        this.mActionViewRideMode = new CustomActionProvider.DefaultActionView(getActivity());
        this.mActionViewRideMode.setInactiveColor(getResources().getColor(R.color.unfocused_grey));
        this.mRideModeProvider = new CustomActionProvider<>(getActivity());
        setHasOptionsMenu(!this.ftpMode);
        this.handlerHeadsetState.postDelayed(new Runnable() { // from class: com.kopin.solos.Fragments.StartStopFragment.1
            @Override // java.lang.Runnable
            public void run() {
                StartStopFragment.this.checkHeadsetConnection();
                StartStopFragment.this.updateHeadsetNotification();
                StartStopFragment.this.startFTP();
            }
        }, 400L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startFTP() {
        if (this.ftpMode && !isFinishing()) {
            getActivity().getWindow().addFlags(128);
            FTP.start();
        }
    }

    @Override // com.kopin.solos.common.SafeFragment, android.app.Fragment
    public void onStart() {
        super.onStart();
        if (LiveRide.getMode() != Workout.RideMode.TRAINING) {
            this.customiseableRideMetrics.resetSelections();
            this.customiseableRideMetrics.setLongPressEnabled(true);
        } else {
            this.customiseableRideMetrics.setLongPressEnabled(false);
        }
    }

    @Override // com.kopin.solos.common.SafeFragment, android.app.Fragment
    public void onResume() {
        super.onResume();
        this.mClockUpdater = new ClockTask();
        this.mClock.schedule(this.mClockUpdater, 1000L, 1000L);
        if (this.mTimeHelper != null) {
            this.mTimerText.setText(this.mTimeHelper.isStarted() ? this.mTimeHelper.getTimeAsString() : TimeHelper.ZERO);
        }
        showFTPView();
        checkUI();
        checkWarnings();
        checkNavigation();
        if (this.ftpMode) {
            this.mButtonBar.setVisibility(8);
            this.mRecordButton.setText(R.string.ready);
            getActivity().getActionBar().setNavigationMode(0);
            getActivity().getActionBar().setTitle(R.string.functional_threshold_power);
            getActivity().getActionBar().setDisplayShowTitleEnabled(true);
            getActivity().getActionBar().setHomeButtonEnabled(false);
        }
        if (this.mBound) {
            this.mService.getAppService().refreshUnitSystem(Prefs.getUnitSystem());
            this.mService.prepareLocation();
            this.mService.getAppService().refreshVocon((this.ftpMode || GroupCom.isInSession() || !com.kopin.pupil.aria.Prefs.isVoconAlwaysOn()) ? false : true);
            this.mService.getAppService().sendWakeUp();
            if (!LiveRide.isActiveRide()) {
                this.mService.getAppService().gotoPage("home", "home", null);
            }
        }
        RideControl.registerObserver(this);
        GroupCom.addListener(this);
        updateGroupComStatus(GroupCom.getCurrentChatGroup());
        updateLockButton();
        getActivity().invalidateOptionsMenu();
        Sync.setProfileUIListener(new Sync.UIListener() { // from class: com.kopin.solos.Fragments.StartStopFragment.2
            @Override // com.kopin.solos.share.Sync.UIListener
            public void onStart() {
            }

            @Override // com.kopin.solos.share.Sync.UIListener
            public void onComplete() {
                if (!StartStopFragment.this.isFinishing()) {
                    StartStopFragment.this.getActivity().runOnUiThread(new Runnable() { // from class: com.kopin.solos.Fragments.StartStopFragment.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Log.i(StartStopFragment.TAG, "sync completed so update the bike menu");
                            StartStopFragment.this.selectBike(Prefs.getChosenBikeId());
                            if (!Features.IS_SPORT_CHANGE_ENABLED) {
                                StartStopFragment.this.createBikeMenu();
                            }
                        }
                    });
                }
            }

            @Override // com.kopin.solos.share.Sync.ConnectionListener
            public void onConnectionChange(boolean connected) {
            }
        });
        showFirmwareUpdatePromptDialog();
    }

    @Override // com.kopin.solos.common.SafeFragment, android.app.Fragment
    public void onPause() {
        super.onPause();
        if (isFinishing()) {
            cancelRideRecovery();
        }
        RideControl.unregisterObserver(this);
        GroupCom.removeListener(this);
        this.mClockUpdater.cancel();
        this.mClockUpdater = null;
        if (this.mBound && !Prefs.isReady()) {
            this.mService.stopLocation();
        }
        if (this.mBound && !LiveRide.isActiveRide() && LiveRide.lastRide() == null && !this.mService.getAppService().isInCall()) {
            this.mService.getAppService().stopVocon();
        }
        if (this.stopFtpDialog != null) {
            this.stopFtpDialog.dismiss();
        }
        Sync.setProfileUIListener(null);
    }

    @Override // android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_stop, container, false);
        this.mView = view;
        this.mBikeButton = (Button) view.findViewById(R.id.btnBikeType);
        this.mSportType = Prefs.getDefaultRideType();
        this.mTimerText = (TextView) view.findViewById(R.id.start_timer);
        this.imgBackground = (ImageView) view.findViewById(R.id.imgBackground);
        this.imgBackground.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.Fragments.StartStopFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                StartStopFragment.this.customiseableRideMetrics.setEnabled(false);
            }
        });
        this.mPauseButton = (Button) view.findViewById(R.id.btnPause);
        this.mRecordButton = (Button) view.findViewById(R.id.btnRecord);
        this.mStopButton = (Button) view.findViewById(R.id.btnStop);
        this.mButtonBar = (ViewGroup) view.findViewById(R.id.btnBar);
        this.mButtonBar1 = (LinearLayout) view.findViewById(R.id.btnBar1);
        this.mCountdownDialog = new CountdownDialog(getActivity());
        this.mAutoStartDialog = new CountdownDialog(getActivity());
        this.txtHeadsetState = (TextView) view.findViewById(R.id.txtHeadsetState);
        this.textGroupComStatus = (TextView) view.findViewById(R.id.textGroupcomStatus);
        final Animation shakeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        this.mScreenLockButton = (ImageButton) view.findViewById(R.id.btnLock);
        final Vibrator vibrator = (Vibrator) getActivity().getSystemService("vibrator");
        this.mScreenLockButton.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.kopin.solos.Fragments.StartStopFragment.4
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View v) {
                vibrator.vibrate(100L);
                StartStopFragment.this.lockRideControls(!StartStopFragment.this.mScreenLocked);
                return true;
            }
        });
        this.mRecordButton.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.Fragments.StartStopFragment.5
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                final Runnable actuallyStart = new Runnable() { // from class: com.kopin.solos.Fragments.StartStopFragment.5.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (!Prefs.isGPSEnabled() || !PermissionUtil.askPermission(StartStopFragment.this.getActivity(), Permission.ACCESS_FINE_LOCATION)) {
                            if (LiveRide.isNavigtionRideMode() && StartStopFragment.this.mBound && !StartStopFragment.this.mService.hasRecentLocation()) {
                                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(StartStopFragment.this.getActivity()).setPositiveButton(android.R.string.ok, (DialogInterface.OnClickListener) null);
                                if (Prefs.isGPSEnabled() && StartStopFragment.this.mService.isGPSEnabled()) {
                                    if (!StartStopFragment.this.mService.hasRecentLocation()) {
                                        alertBuilder.setMessage(R.string.dialog_no_gps);
                                    } else {
                                        alertBuilder.setMessage(R.string.dialog_no_gps);
                                    }
                                } else {
                                    alertBuilder.setMessage(R.string.dialog_gps_disabled);
                                }
                                alertBuilder.create().show();
                                return;
                            }
                            RideControl.ready(true);
                        }
                    }
                };
                if (Prefs.isGPSEnabled() && !Prefs.isGPSAllowed()) {
                    new AlertDialog.Builder(StartStopFragment.this.getActivity()).setTitle(R.string.dialog_gps_get_consent).setMessage(R.string.dialog_gps_explain_consent).setNegativeButton(R.string.dialog_gps_disallow, new DialogInterface.OnClickListener() { // from class: com.kopin.solos.Fragments.StartStopFragment.5.3
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialog, int which) {
                            Prefs.setGPSEnabled(false);
                            StartStopFragment.this.runOnMainThread(actuallyStart);
                        }
                    }).setPositiveButton(R.string.dialog_gps_allow, new DialogInterface.OnClickListener() { // from class: com.kopin.solos.Fragments.StartStopFragment.5.2
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialog, int which) {
                            Prefs.setGPSAllowed(true);
                            StartStopFragment.this.runOnMainThread(actuallyStart);
                        }
                    }).create().show();
                } else {
                    actuallyStart.run();
                }
            }
        });
        this.mStopButton.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.Fragments.StartStopFragment.6
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (StartStopFragment.this.mScreenLocked) {
                    StartStopFragment.this.mScreenLockButton.startAnimation(shakeAnimation);
                } else {
                    StartStopFragment.this.stopCheck();
                }
            }
        });
        view.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.Fragments.StartStopFragment.7
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                StartStopFragment.this.stopCheck();
            }
        });
        this.mPauseButton.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.Fragments.StartStopFragment.8
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (StartStopFragment.this.mScreenLocked) {
                    StartStopFragment.this.mScreenLockButton.startAnimation(shakeAnimation);
                } else if (StartStopFragment.this.mBound) {
                    if (StartStopFragment.this.mTimeHelper.isPaused()) {
                        RideControl.requestResume();
                    } else {
                        RideControl.pause();
                    }
                }
            }
        });
        this.txtFtpDurationValue = (TextView) view.findViewById(R.id.txtDurationValue);
        this.txtFtpPowerValue = (TextView) view.findViewById(R.id.txtPowerValue);
        this.txtFtpAvePowerValue = (TextView) view.findViewById(R.id.txtAvePowerValue);
        this.layoutTimerNormal = view.findViewById(R.id.layoutTimerNormal);
        this.layoutNormalMode = view.findViewById(R.id.layoutNormalMode);
        this.layoutTimerFTP = view.findViewById(R.id.layoutTimerFTP);
        this.layoutFTPMode = view.findViewById(R.id.layoutFTPMode);
        showFTPView();
        initRideScreenMetricPagers(this.mView);
        if (Features.IS_SPORT_CHANGE_ENABLED) {
            createSportSelectionMenu();
        } else {
            createBikeMenu();
        }
        this.mSportType = Prefs.getDefaultRideType();
        selectBike(Prefs.getChosenBikeId());
        checkUI();
        setData();
        checkForIncompleteRide();
        return view;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void lockRideControls(boolean lock) {
        if (lock) {
            this.mScreenLocked = true;
            this.mPauseButton.setAlpha(0.6f);
            this.mStopButton.setAlpha(0.6f);
            this.mScreenLockButton.setColorFilter(getResources().getColor(R.color.solos_orange));
            return;
        }
        this.mScreenLocked = false;
        this.mPauseButton.setAlpha(1.0f);
        this.mStopButton.setAlpha(1.0f);
        this.mScreenLockButton.clearColorFilter();
    }

    private void updateLockButton() {
        runOnMainThread(new Runnable() { // from class: com.kopin.solos.Fragments.StartStopFragment.9
            @Override // java.lang.Runnable
            public void run() {
                boolean ridePaused = StartStopFragment.this.mTimeHelper.isPaused();
                if (ridePaused) {
                    StartStopFragment.this.mScreenLockButton.setAlpha(0.6f);
                    StartStopFragment.this.mScreenLockButton.setEnabled(false);
                    StartStopFragment.this.lockRideControls(false);
                } else {
                    StartStopFragment.this.mScreenLockButton.setAlpha(1.0f);
                    StartStopFragment.this.mScreenLockButton.setEnabled(true);
                    StartStopFragment.this.lockRideControls(Prefs.isAutoLockEnabled());
                }
            }
        });
    }

    public void onBackPressed() {
        if (!this.mScreenLocked) {
            stopCheck();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showStopFTPDialog() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() { // from class: com.kopin.solos.Fragments.StartStopFragment.10
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case -1:
                        if (!StartStopFragment.this.isFinishing()) {
                            FTP.stop(true);
                            if (StartStopFragment.this.getFragmentManager() != null) {
                                StartStopFragment.this.getFragmentManager().popBackStack();
                            }
                        }
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_ftp_end_message).setPositiveButton(R.string.dialog_button_yes, dialogClickListener).setNegativeButton(R.string.dialog_button_no, dialogClickListener);
        this.stopFtpDialog = builder.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopCheck() {
        if (this.ftpMode && LiveRide.getTargetTime() > 0) {
            showStopFTPDialog();
        } else {
            RideControl.stop(false);
        }
    }

    private void showFTPView() {
        this.layoutTimerNormal.setVisibility(this.ftpMode ? 8 : 0);
        this.layoutNormalMode.setVisibility(this.ftpMode ? 8 : 0);
        this.layoutTimerFTP.setVisibility(this.ftpMode ? 0 : 8);
        this.layoutFTPMode.setVisibility(this.ftpMode ? 0 : 8);
    }

    @Override // com.kopin.solos.RideControl.RideObserver
    public void onRideConfig(Workout.RideMode mode, long rideOrRouteId) {
    }

    @Override // com.kopin.solos.RideControl.RideObserver
    public void onRideIdle() {
    }

    @Override // com.kopin.solos.RideControl.RideObserver
    public void onRideReady(RideControl.StartMode startMode) {
        switch (startMode) {
            case AUTO:
                runOnMainThread(new Runnable() { // from class: com.kopin.solos.Fragments.StartStopFragment.11
                    @Override // java.lang.Runnable
                    public void run() {
                        StartStopFragment.this.showAutoStartOverlay(true);
                    }
                });
                break;
        }
    }

    @Override // com.kopin.solos.RideControl.RideObserver
    public void onRideStarted(Workout.RideMode mode) {
        runOnMainThread(new Runnable() { // from class: com.kopin.solos.Fragments.StartStopFragment.12
            @Override // java.lang.Runnable
            public void run() {
                StartStopFragment.this.showAutoStartOverlay(false);
                StartStopFragment.this.customiseableRideMetrics.setLongPressEnabled(LiveRide.getMode() != Workout.RideMode.TRAINING);
            }
        });
        checkUI();
    }

    @Override // com.kopin.solos.RideControl.RideObserver
    public boolean okToStop() {
        long countdown = LiveRide.getCountdownTime();
        Log.d("Target", "" + countdown);
        if (this.ftpMode && countdown > 0) {
            runOnMainThread(new Runnable() { // from class: com.kopin.solos.Fragments.StartStopFragment.13
                @Override // java.lang.Runnable
                public void run() {
                    StartStopFragment.this.showStopFTPDialog();
                }
            });
            return false;
        }
        if (this.stopFtpDialog != null && countdown < 0) {
            this.stopFtpDialog.dismiss();
        }
        return true;
    }

    @Override // com.kopin.solos.RideControl.RideObserver
    public void onRideStopped(SavedWorkout ride) {
        runOnMainThread(new Runnable() { // from class: com.kopin.solos.Fragments.StartStopFragment.14
            @Override // java.lang.Runnable
            public void run() {
                StartStopFragment.this.checkUI();
                Navigator.cancel();
                StartStopFragment.this.selectBike(Prefs.getChosenBikeId());
                new Handler().postDelayed(new Runnable() { // from class: com.kopin.solos.Fragments.StartStopFragment.14.1
                    @Override // java.lang.Runnable
                    public void run() {
                        StartStopFragment.this.customiseableRideMetrics.resetSelections();
                        StartStopFragment.this.customiseableRideMetrics.resetMetricValues();
                        StartStopFragment.this.customiseableRideMetrics.setLongPressEnabled(true);
                    }
                }, 300L);
            }
        });
    }

    @Override // com.kopin.solos.RideControl.RideObserver
    public void onRidePaused(boolean userOrAuto) {
        updateLockButton();
    }

    @Override // com.kopin.solos.RideControl.RideObserver
    public void onRideResumed(boolean userOrAuto) {
        updateLockButton();
    }

    @Override // com.kopin.solos.RideControl.RideObserver
    public void onNewLap(double lastDistance, long lastTime) {
        if (LiveRide.getMode() == Workout.RideMode.TRAINING) {
            getActivity().runOnUiThread(new Runnable() { // from class: com.kopin.solos.Fragments.StartStopFragment.15
                @Override // java.lang.Runnable
                public void run() {
                    StartStopFragment.this.setTrainingMetrics();
                }
            });
        }
    }

    @Override // com.kopin.solos.RideControl.RideObserver
    public void onCountdownStarted() {
        runOnMainThread(new Runnable() { // from class: com.kopin.solos.Fragments.StartStopFragment.16
            @Override // java.lang.Runnable
            public void run() {
                StartStopFragment.this.mCountdownDialog.show(ThemeUtil.getResourceId(StartStopFragment.this.getActivity(), StartStopFragment.this.mTimeHelper.isStarted() ? R.attr.countdownMsgResume : R.attr.strCountdownMsgStart), String.valueOf(3), StartStopFragment.this.mCountdownCancel);
            }
        });
    }

    @Override // com.kopin.solos.RideControl.RideObserver
    public void onCountdown(int counter) {
        if (isVisible()) {
            this.mCountdownDialog.setText(String.valueOf(counter));
        }
    }

    @Override // com.kopin.solos.RideControl.RideObserver
    public void onCountdownComplete(boolean wasCancelled) {
        if (isAdded()) {
            this.mCountdownDialog.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initRideScreenMetricPagers(View view) {
        this.customiseableRideMetrics = new CustomiseableRideMetrics(getActivity());
        this.customiseableRideMetrics.init((PagerContainer) view.findViewById(R.id.pager_container1), (PagerContainer) view.findViewById(R.id.pager_container2), (PagerContainer) view.findViewById(R.id.pager_container3), LiveRide.getCurrentSport().name(), getDefaultMetrics());
        if (LiveRide.getMode() == Workout.RideMode.TRAINING) {
            setTrainingMetrics();
        } else {
            this.customiseableRideMetrics.resetSelections();
        }
        this.customiseableRideMetrics.setEnabled(false);
        new Handler().postDelayed(new Runnable() { // from class: com.kopin.solos.Fragments.StartStopFragment.17
            @Override // java.lang.Runnable
            public void run() {
                if (LiveRide.isStartedAndRunning()) {
                    StartStopFragment.this.setData();
                }
            }
        }, 40L);
    }

    private List<CustomiseableRideMetrics.RideScreenMetric> getDefaultMetrics() {
        List<CustomiseableRideMetrics.RideScreenMetric> defaultSet = new ArrayList<>();
        switch (LiveRide.getCurrentSport()) {
            case RUN:
                defaultSet.add(CustomiseableRideMetrics.RideScreenMetric.DISTANCE);
                defaultSet.add(CustomiseableRideMetrics.RideScreenMetric.PACE);
                defaultSet.add(CustomiseableRideMetrics.RideScreenMetric.HEART_RATE);
                return defaultSet;
            default:
                defaultSet.add(CustomiseableRideMetrics.RideScreenMetric.DISTANCE);
                defaultSet.add(CustomiseableRideMetrics.RideScreenMetric.AVG_SPEED);
                defaultSet.add(CustomiseableRideMetrics.RideScreenMetric.ELEVATION_CHANGE);
                return defaultSet;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setTrainingMetrics() {
        this.customiseableRideMetrics.setLongPressEnabled(false);
        List<CustomiseableRideMetrics.RideScreenMetric> defaultSet = new ArrayList<>();
        Trainer trainer = (Trainer) VirtualCoach.getVirtualWorkout();
        switch (trainer.getPrimaryMetric()) {
            case AVERAGE_TARGET_SPEED:
                defaultSet.add(CustomiseableRideMetrics.RideScreenMetric.SPEED);
                defaultSet.add(CustomiseableRideMetrics.RideScreenMetric.AVG_SPEED);
                break;
            case AVERAGE_TARGET_PACE:
                defaultSet.add(CustomiseableRideMetrics.RideScreenMetric.PACE);
                defaultSet.add(CustomiseableRideMetrics.RideScreenMetric.AVG_PACE);
                break;
            case AVERAGE_TARGET_HEARTRATE:
                defaultSet.add(CustomiseableRideMetrics.RideScreenMetric.HEART_RATE);
                defaultSet.add(CustomiseableRideMetrics.RideScreenMetric.AVG_HEART_RATE);
                break;
            case AVERAGE_TARGET_POWER:
                defaultSet.add(CustomiseableRideMetrics.RideScreenMetric.POWER);
                defaultSet.add(CustomiseableRideMetrics.RideScreenMetric.AVG_POWER);
                break;
            case AVERAGE_TARGET_KICK:
                defaultSet.add(CustomiseableRideMetrics.RideScreenMetric.KICK);
                defaultSet.add(CustomiseableRideMetrics.RideScreenMetric.AVG_KICK);
                break;
            default:
                defaultSet.add(CustomiseableRideMetrics.RideScreenMetric.AVG_SPEED);
                defaultSet.add(CustomiseableRideMetrics.RideScreenMetric.ELEVATION_CHANGE);
                break;
        }
        if (trainer.stepHasSecondaryTarget()) {
            defaultSet.add(LiveRide.getCurrentSport() == SportType.RUN ? CustomiseableRideMetrics.RideScreenMetric.STEP : CustomiseableRideMetrics.RideScreenMetric.CADENCE);
        } else {
            defaultSet.add(CustomiseableRideMetrics.RideScreenMetric.DISTANCE);
        }
        for (int i = 0; i < defaultSet.size(); i++) {
            this.customiseableRideMetrics.overrideSelection(defaultSet.get(i), i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createBikeMenu() {
        updateBikeMenuButton();
        final List<Bike> bikes = Bikes.getActiveBikes();
        if (bikes != null && bikes.size() > 0 && bikes.size() > 1) {
            this.mBikeButton.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.Fragments.StartStopFragment.18
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (!StartStopFragment.this.menuShowing) {
                        StartStopFragment.this.menuShowing = true;
                        PopupMenu menu = new PopupMenu(StartStopFragment.this.getActivity(), StartStopFragment.this.mBikeButton);
                        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // from class: com.kopin.solos.Fragments.StartStopFragment.18.1
                            @Override // android.widget.PopupMenu.OnMenuItemClickListener
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                StartStopFragment.this.selectBike(menuItem.getItemId());
                                StartStopFragment.this.updateBikeMenuButton();
                                StartStopFragment.this.menuShowing = false;
                                return false;
                            }
                        });
                        menu.setOnDismissListener(new PopupMenu.OnDismissListener() { // from class: com.kopin.solos.Fragments.StartStopFragment.18.2
                            @Override // android.widget.PopupMenu.OnDismissListener
                            public void onDismiss(PopupMenu menu2) {
                                StartStopFragment.this.menuShowing = false;
                            }
                        });
                        menu.getMenu().clear();
                        for (Bike bike : bikes) {
                            menu.getMenu().add(0, (int) bike.getId(), 0, bike.getName());
                        }
                        menu.show();
                        return;
                    }
                    StartStopFragment.this.menuShowing = false;
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBikeMenuButton() {
        Bike currentDefaultBike = SQLHelper.getBike(this.mBikeId);
        if (currentDefaultBike != null) {
            this.mBikeButton.setText(currentDefaultBike.getName());
        } else {
            this.mBikeButton.setText(R.string.finish_ride_bike_type_msg);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void selectBike(long bikeId) {
        Bike bike = SQLHelper.getActiveBike(bikeId);
        if (bike != null) {
            this.mBikeId = bikeId;
        } else {
            List<Bike> bikes = Bikes.getActiveBikes();
            if (bikes.size() > 0) {
                this.mBikeId = bikes.get(0).getId();
            } else {
                return;
            }
        }
        Prefs.setChosenBikeId(this.mBikeId);
        if (bike != null) {
            float wheelSizeInMtr = SQLHelper.getBike(this.mBikeId).getWheelSize() / 1000.0f;
            Prefs.setWheelSize(wheelSizeInMtr);
        }
    }

    private void createSportSelectionMenu() {
        updateSportMenuButton();
        this.mSportSelectionMenu = new PopupMenu(getActivity(), this.mBikeButton);
        this.mSportSelectionMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // from class: com.kopin.solos.Fragments.StartStopFragment.19
            @Override // android.widget.PopupMenu.OnMenuItemClickListener
            public boolean onMenuItemClick(MenuItem menuItem) {
                SportType sportType = SportType.values()[menuItem.getItemId()];
                ((MainActivity) StartStopFragment.this.getActivity()).changeSport(sportType);
                StartStopFragment.this.updateSportMenuButton();
                StartStopFragment.this.initRideScreenMetricPagers(StartStopFragment.this.mView);
                StartStopFragment.this.menuShowing = false;
                return false;
            }
        });
        this.mSportSelectionMenu.setOnDismissListener(new PopupMenu.OnDismissListener() { // from class: com.kopin.solos.Fragments.StartStopFragment.20
            @Override // android.widget.PopupMenu.OnDismissListener
            public void onDismiss(PopupMenu menu) {
                StartStopFragment.this.menuShowing = false;
            }
        });
        for (SportType sportType : SportType.values()) {
            MenuItem item = this.mSportSelectionMenu.getMenu().add(0, sportType.ordinal(), 0, ThemeUtil.getSportNameRes(sportType));
            item.setIcon(ThemeUtil.getSportIconRes(sportType));
        }
        this.mBikeButton.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.Fragments.StartStopFragment.21
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (!StartStopFragment.this.menuShowing) {
                    StartStopFragment.this.menuShowing = true;
                    StartStopFragment.this.mSportSelectionMenu.show();
                } else {
                    StartStopFragment.this.menuShowing = false;
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSportMenuButton() {
        SportType sportType = LiveRide.getCurrentSport();
        this.mBikeButton.setText(ThemeUtil.getSportNameRes(sportType));
        this.mBikeButton.setCompoundDrawablesWithIntrinsicBounds(0, ThemeUtil.getSportIconRes(sportType), 0, 0);
    }

    private int indexOf(String[] array, String element) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].contentEquals(element)) {
                return i;
            }
        }
        return -1;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ServiceActivity) {
            ((ServiceActivity) activity).getService(new ServiceCallback() { // from class: com.kopin.solos.Fragments.StartStopFragment.22
                @Override // com.kopin.solos.Fragments.StartStopFragment.ServiceCallback
                public void onService(HardwareReceiverService service) {
                    StartStopFragment.this.mService = service;
                    StartStopFragment.this.mService.addCallback(StartStopFragment.this);
                    StartStopFragment.this.mService.setHeadsetCallback(StartStopFragment.this);
                    StartStopFragment.this.mTimeHelper = StartStopFragment.this.mService.getTimeHelper();
                    StartStopFragment.this.mBound = true;
                    StartStopFragment.this.mService.prepareLocation();
                    StartStopFragment.this.checkUI();
                    StartStopFragment.this.setData();
                    StartStopFragment.this.checkWarnings();
                    StartStopFragment.this.checkNavigation();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetWorkoutMode() {
        RideControl.setWorkoutMode(Workout.RideMode.NORMAL, -1L);
        this.customiseableRideMetrics.resetSelections();
        this.customiseableRideMetrics.setLongPressEnabled(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkNavigation() {
        if (!this.ftpMode) {
            ((MainActivity) getActivity()).showSpinner();
            getActivity().getActionBar().setDisplayShowTitleEnabled(false);
        }
        getActivity().getActionBar().setDisplayShowHomeEnabled(true);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
        getActivity().getActionBar().setHomeButtonEnabled(false);
        ((MainActivity) getActivity()).unhideActionBar();
    }

    @Override // com.kopin.solos.common.SafeFragment, android.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        if (this.mService != null) {
            this.mService.removeDataListener(this.mDataListener);
            this.mService.setHeadsetCallback(null);
        }
    }

    @Override // android.app.Fragment
    public void onDetach() {
        super.onDetach();
        if (this.mBound) {
            this.mBound = false;
            this.mService.setHeadsetCallback(null);
            this.mService.removeCallback(this);
            if (!Prefs.isReady()) {
                this.mService.stopLocation();
            }
        }
    }

    @Override // com.kopin.solos.HardwareReceiverService.InfoListener
    public void onListChanged() {
        checkUI();
        Fragment fragment = getFragmentManager().findFragmentByTag(MainActivity.KEY_START_STOP_FRAGMENT);
        if (fragment != null && fragment.isVisible()) {
            checkWarnings();
            checkNavigation();
        }
    }

    @Override // com.kopin.solos.HardwareReceiverService.InfoListener
    public void onNotification(HardwareReceiverService.NotificationType type) {
        switch (type) {
            case START_STOP:
            case PAUSE_RESUME:
            case CONNECTION:
            case PREPARE:
                checkUI();
                break;
        }
    }

    @Override // android.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        CustomActionProvider warningsProvider = SensorWarnings.getWarningsProvider();
        warningsProvider.setMenuAdapter(new TextMenuAdapter(getActivity()));
        warningsProvider.setOnPrepareListener(new CustomActionProvider.OnPrepareListener<TextMenuAdapter.TextMenuItem>() { // from class: com.kopin.solos.Fragments.StartStopFragment.23
            @Override // com.kopin.solos.menu.CustomActionProvider.OnPrepareListener
            public void onPrepare(CustomActionProvider<TextMenuAdapter.TextMenuItem> actionProvider) {
                StartStopFragment.this.checkWarnings();
            }
        });
        Workout.RideMode mode = LiveRide.getMode();
        this.mActionViewRideMode.setActiveColor(getResources().getColor(mode == Workout.RideMode.NORMAL ? R.color.unfocused_grey : R.color.app_actionbar_divider));
        this.mActionViewRideMode.setActive(true);
        this.mRideModeProvider.setMenuAdapter(new NavigationMenuAdapter(getActivity()));
        this.mRideModeProvider.setActionView(this.mActionViewRideMode);
        this.mRideModeProvider.setOnPrepareListener(new CustomActionProvider.OnPrepareListener<NavigationMenuAdapter.NavigationMenuItem>() { // from class: com.kopin.solos.Fragments.StartStopFragment.24
            @Override // com.kopin.solos.menu.CustomActionProvider.OnPrepareListener
            public void onPrepare(CustomActionProvider<NavigationMenuAdapter.NavigationMenuItem> provider) {
                StartStopFragment.this.mRideModeProvider.clear();
                StartStopFragment.this.checkNavigation();
                if (!LiveRide.isActiveRide()) {
                    if (LiveRide.getMode() != Workout.RideMode.NORMAL) {
                        StartStopFragment.this.addRideModeMenuItem(0, R.string.cancel, 0);
                        return;
                    }
                    StartStopFragment.this.addRideModeMenuItem(1, R.string.navigation_menu_item_address, R.drawable.ic_nav_icon);
                    StartStopFragment.this.addRideModeMenuItem(2, R.string.navigation_menu_item_route, R.drawable.ic_nav_icon);
                    StartStopFragment.this.addRideModeMenuItem(3, ThemeUtil.getResourceId(StartStopFragment.this.getActivity(), R.attr.strNavigationWorkout), R.drawable.ic_nav_icon);
                    StartStopFragment.this.addRideModeMenuItem(4, R.string.menu_ghost_ride, R.drawable.ic_ghost_rider_icon);
                    if (Features.IS_TRAINING_ENABLED) {
                        StartStopFragment.this.addRideModeMenuItem(6, R.string.menu_training_mode, R.drawable.ic_workouts_small);
                    }
                }
            }
        });
        this.mRideModeProvider.setOnItemClickListener(new CustomActionProvider.OnItemClickListener() { // from class: com.kopin.solos.Fragments.StartStopFragment.25
            @Override // com.kopin.solos.menu.CustomActionProvider.OnItemClickListener
            public void onItemClick(int position, CustomMenuItem menuItem) {
                switch (menuItem.getId()) {
                    case 0:
                        if (VirtualCoach.isActive()) {
                            StartStopFragment.this.resetWorkoutMode();
                        } else {
                            Navigator.cancel();
                            StartStopFragment.this.checkNavigation();
                        }
                        StartStopFragment.this.getActivity().invalidateOptionsMenu();
                        break;
                    case 1:
                        ((MainActivity) StartStopFragment.this.getActivity()).showDirectionFragment();
                        break;
                    case 2:
                        ((MainActivity) StartStopFragment.this.getActivity()).showRouteNavigationFragment();
                        break;
                    case 3:
                        ((MainActivity) StartStopFragment.this.getActivity()).selectNavigateFromRide();
                        break;
                    case 4:
                        ((MainActivity) StartStopFragment.this.getActivity()).selectGhostRide();
                        break;
                    case 5:
                        RideControl.setWorkoutMode(Workout.RideMode.TARGETS, -1L);
                        StartStopFragment.this.getActivity().invalidateOptionsMenu();
                        break;
                    case 6:
                        ((MainActivity) StartStopFragment.this.getActivity()).showTrainingWorkouts();
                        break;
                }
            }
        });
        warningsProvider.setOnPrepareListener(new CustomActionProvider.OnPrepareListener<TextMenuAdapter.TextMenuItem>() { // from class: com.kopin.solos.Fragments.StartStopFragment.26
            @Override // com.kopin.solos.menu.CustomActionProvider.OnPrepareListener
            public void onPrepare(CustomActionProvider<TextMenuAdapter.TextMenuItem> actionProvider) {
                StartStopFragment.this.checkWarnings();
            }
        });
        inflater.inflate(R.menu.record_menu, menu);
        menu.findItem(R.id.menu_warnings).setActionProvider(warningsProvider);
        MenuItem menuItemRideMode = menu.findItem(R.id.menu_ride_mode);
        menuItemRideMode.setActionProvider(this.mRideModeProvider);
        menuItemRideMode.setIcon(getWorkoutRes(mode));
        menu.findItem(R.id.menu_show_sensors).setVisible(true);
        this.mMenuItemGroupChat = menu.findItem(R.id.menu_group_com);
        if (Features.IS_GROUPCHAT_ENABLED) {
            ChatGroup group = GroupCom.getCurrentChatGroup();
            updateGroupChatMenuItem(group != null ? group.sessionState : ChatGroup.SessionState.NONE);
        } else {
            this.mMenuItemGroupChat.setVisible(false);
        }
    }

    private int getWorkoutRes(Workout.RideMode mode) {
        switch (mode) {
            case GHOST_RIDE:
            case GHOST_LAP:
                return R.drawable.ic_ghost_rider;
            case ROUTE:
                return R.drawable.ic_nav;
            case TARGETS:
                return R.drawable.ic_nav_targets;
            case TRAINING:
                return R.drawable.ic_nav_workouts;
            default:
                return R.drawable.ic_options;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addRideModeMenuItem(int itemId, int titleRes, int drawableRes) {
        NavigationMenuAdapter.NavigationMenuItem item = new NavigationMenuAdapter.NavigationMenuItem(getString(titleRes), drawableRes, itemId);
        item.setDismissOnTap(true);
        this.mRideModeProvider.addMenuItem(item);
    }

    private void updateGroupChatMenuItem(ChatGroup.SessionState state) {
        if (this.mMenuItemGroupChat != null) {
            this.mMenuItemGroupChat.setEnabled(true);
            switch (state) {
                case CONNECTED:
                    this.mMenuItemGroupChat.getIcon().setColorFilter(getResources().getColor(R.color.sessionConnected), PorterDuff.Mode.SRC_ATOP);
                    break;
                case CONNECTING:
                    this.mMenuItemGroupChat.getIcon().setColorFilter(getResources().getColor(R.color.sessionConnecting), PorterDuff.Mode.SRC_ATOP);
                    break;
                default:
                    this.mMenuItemGroupChat.getIcon().setColorFilter(getResources().getColor(R.color.unfocused_grey), PorterDuff.Mode.SRC_ATOP);
                    break;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRideModeMenuItem() {
        boolean isRideStarted = this.mTimeHelper.isStarted();
        this.mActionViewRideMode.setActive(!isRideStarted);
        this.mActionViewRideMode.setAlpha(isRideStarted ? 0.3f : 1.0f);
    }

    @Override // com.kopin.solos.common.SafeFragment, android.app.Fragment
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.menu_group_com) {
            return super.onOptionsItemSelected(item);
        }
        Intent intent = new Intent(getActivity(), (Class<?>) GroupComListActivity.class);
        intent.putExtra(GroupComListActivity.INTENT_EXTRA_USER_NAME, UserProfile.getName());
        startActivity(intent);
        return true;
    }

    private void checkForIncompleteRide() {
        if (!LiveRide.isActiveRide()) {
            recoverIncompleteRide();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkUI() {
        runOnMainThread(new Runnable() { // from class: com.kopin.solos.Fragments.StartStopFragment.27
            @Override // java.lang.Runnable
            public void run() {
                int drawable = R.drawable.ic_pause;
                if (StartStopFragment.this.mBound && StartStopFragment.this.mPauseButton != null) {
                    boolean isAutoPauseEnabled = Prefs.isAutoPauseEnabled();
                    boolean isPaused = StartStopFragment.this.mTimeHelper.isPaused();
                    if (RideControl.hasCountdown() || (isAutoPauseEnabled && isPaused)) {
                        StartStopFragment.this.mCountdownDialog.show();
                    } else {
                        StartStopFragment.this.mCountdownDialog.dismiss();
                    }
                    StartStopFragment.this.mPauseButton.setEnabled(StartStopFragment.this.mTimeHelper.isStarted());
                    StartStopFragment.this.mStopButton.setEnabled(StartStopFragment.this.mTimeHelper.isStarted());
                    StartStopFragment.this.checkHeadsetConnection();
                    StartStopFragment.this.showStartButton(!StartStopFragment.this.mTimeHelper.isStarted());
                    if (!Prefs.isAutoStartPossible()) {
                        StartStopFragment.this.showAutoStartOverlay(false);
                        StartStopFragment.this.mRecordButton.setText(ThemeUtil.getString(StartStopFragment.this.getActivity(), R.attr.strStartWorkout));
                        StartStopFragment.this.mRecordButton.setTextColor(-1);
                        StartStopFragment.this.mRecordButton.setBackgroundResource(R.drawable.background_record_btn);
                    } else {
                        boolean rideInactive = !StartStopFragment.this.mTimeHelper.isStarted();
                        StartStopFragment.this.mRecordButton.setText(rideInactive ? R.string.ready : R.string.cancel);
                        StartStopFragment.this.mRecordButton.setBackgroundResource(rideInactive ? R.drawable.background_ready_btn : R.drawable.background_cancel_btn);
                        StartStopFragment.this.mRecordButton.setTextColor(rideInactive ? StartStopFragment.this.getResources().getColor(R.color.ready_btn_text) : -1);
                    }
                    if (!StartStopFragment.this.mTimeHelper.isStarted()) {
                        StartStopFragment.this.showPauseAlertOverlay(false, isAutoPauseEnabled);
                        StartStopFragment.this.mPauseButton.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_pause, 0, 0);
                        StartStopFragment.this.mPauseButton.setText(R.string.caption_pause);
                    } else {
                        if (isPaused) {
                            drawable = R.drawable.ic_resume;
                        }
                        StartStopFragment.this.showPauseAlertOverlay(isPaused, isAutoPauseEnabled);
                        StartStopFragment.this.mPauseButton.setCompoundDrawablesRelativeWithIntrinsicBounds(0, drawable, 0, 0);
                        StartStopFragment.this.mPauseButton.setText(isPaused ? R.string.caption_resume : R.string.caption_pause);
                    }
                    StartStopFragment.this.updateRideModeMenuItem();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showStartButton(boolean show) {
        if (!show) {
            if (this.mButtonBar1.getVisibility() == 0) {
                if (this.ftpMode) {
                    this.mButtonBar1.setVisibility(8);
                    this.mRecordButton.setText(R.string.ready);
                    return;
                }
                this.mService.getAppService().sendWakeUp();
                TranslateAnimation animate = new TranslateAnimation(0.0f, 0.0f, 0.0f, this.mButtonBar1.getHeight());
                animate.setDuration(500L);
                this.mButtonBar1.startAnimation(animate);
                this.mButtonBar1.setVisibility(8);
                this.mButtonTimeHandler.postDelayed(new Runnable() { // from class: com.kopin.solos.Fragments.StartStopFragment.28
                    @Override // java.lang.Runnable
                    public void run() {
                        StartStopFragment.this.mRecordButton.setText(R.string.ready);
                    }
                }, 600L);
                return;
            }
            return;
        }
        this.mButtonBar1.setVisibility(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkWarnings() {
        if (this.mBound) {
            SensorWarnings.checkWarnings(PageNav.getMetricChoices(), this.mBound, this.mService);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkHeadsetConnection() {
        Log.d("StarStop", "" + Features.IS_GROUPCHAT_ENABLED);
        if (this.mMenuItemGroupChat != null) {
            this.mMenuItemGroupChat.setVisible(Features.IS_GROUPCHAT_ENABLED);
        }
        if (SensorWarnings.hasHeadsetWarning()) {
            Log.e(TAG, "checkHeadsetConnection disconn");
            updateHeadsetBar(HeadsetState.DISCONNECTED);
        } else {
            SensorWarnings.checkHeadsetConnection(this.mBound && !this.mService.isConnectedToVC());
            updateHeadsetNotification();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateHeadsetNotification() {
        boolean update;
        boolean fade = false;
        if (this.mBound && this.mService.isReconnecting()) {
            update = updateHeadsetBar(HeadsetState.CONNECTING);
        } else if (this.mBound && this.mService.isConnectedToVC()) {
            update = updateHeadsetBar(HeadsetState.CONNECTED);
            fade = true;
        } else {
            update = updateHeadsetBar(HeadsetState.DISCONNECTED);
        }
        if (update) {
            if (fade) {
                Animation fadeInAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_slow);
                this.txtHeadsetState.startAnimation(fadeInAnimation);
                this.txtHeadsetState.setVisibility(8);
                return;
            }
            this.handlerHeadsetState.postDelayed(new Runnable() { // from class: com.kopin.solos.Fragments.StartStopFragment.29
                @Override // java.lang.Runnable
                public void run() {
                    StartStopFragment.this.updateHeadsetNotification();
                }
            }, 2500L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean updateHeadsetBar(HeadsetState state) {
        Animation groupComAnim = this.textGroupComStatus.getAnimation();
        if (this.textGroupComStatus.getVisibility() == 0 || (groupComAnim != null && !groupComAnim.hasEnded())) {
            this.txtHeadsetState.setVisibility(4);
            return true;
        }
        if (mHeadsetState == state && mHeadsetState != HeadsetState.UNKNOWN && state != HeadsetState.DISCONNECTED) {
            return false;
        }
        mHeadsetState = state;
        switch (state) {
            case CONNECTED:
                this.txtHeadsetState.setBackgroundResource(R.color.green_button_fill);
                this.txtHeadsetState.setText(R.string.notification_headset_connected);
                break;
            case CONNECTING:
                this.txtHeadsetState.setBackgroundResource(R.color.app_actionbar_divider);
                this.txtHeadsetState.setText(R.string.notification_headset_connecting);
                this.txtHeadsetState.setVisibility(0);
                break;
            case DISCONNECTED:
                this.txtHeadsetState.setBackgroundResource(R.color.red_button_stroke);
                this.txtHeadsetState.setText(R.string.notification_headset_disconnected);
                this.txtHeadsetState.setVisibility(0);
                break;
        }
        this.txtHeadsetState.invalidate();
        return true;
    }

    private void showFirmwareUpdatePromptDialog() {
        if (!isFinishing() && isVisible()) {
            boolean showPrompt = Config.SHOW_FIRMWARE_UPDATE_PROMPT_DIALOG && sPromptFirmwareUpdate && PupilDevice.isConnected() && FirmwareFlash.updateAvailable() && FirmwareFlash.isUpdateHigherVersion(PupilDevice.currentDeviceInfo().mVersion);
            if (showPrompt) {
                String updateVersion = FirmwareFlash.updateVersion();
                String message = String.format(getString(R.string.headset_version_upgrade_dialog_text), updateVersion);
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() { // from class: com.kopin.solos.Fragments.StartStopFragment.30
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case -1:
                                Intent intent = new Intent(StartStopFragment.this.getActivity(), (Class<?>) FlashActivity.class);
                                StartStopFragment.this.startActivity(intent);
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.headset_version_upgrade_dialog_title).setMessage(message).setPositiveButton(R.string.headset_version_upgrade_dialog_btn_now, dialogClickListener).setNegativeButton(R.string.headset_version_upgrade_dialog_btn_later, dialogClickListener);
                Dialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                DialogUtils.setDialogTitleDivider(dialog);
                sPromptFirmwareUpdate = false;
            }
        }
    }

    private void cancelRideRecovery() {
        if (this.mRideRecoveryDialog != null) {
            RecoveryUtil.cancelRecovery();
            this.mRideRecoveryDialog.dismiss();
            this.mRideRecoveryDialog = null;
        }
    }

    private void recoverIncompleteRide() {
        final long id = RecoveryUtil.getLastIncompleteWorkoutId();
        if (id != -1 && !isFinishing()) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() { // from class: com.kopin.solos.Fragments.StartStopFragment.31
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    if (StartStopFragment.this.isStillRequired()) {
                        switch (which) {
                            case -2:
                                RecoveryUtil.discardIncompleteWorkout(id);
                                break;
                            case -1:
                                StartStopFragment.this.openRide(LiveRide.getCurrentSport(), id);
                                break;
                        }
                    }
                }
            };
            this.mRideRecoveryDialog = new ProgressDialog(getActivity());
            this.mRideRecoveryDialog.setProgressStyle(0);
            this.mRideRecoveryDialog.setButton(-1, getString(R.string.show_me), dialogClickListener);
            this.mRideRecoveryDialog.setButton(-2, getString(R.string.discard_ride), dialogClickListener);
            this.mRideRecoveryDialog.setTitle(R.string.recover_ride_title);
            this.mRideRecoveryDialog.setMessage(getString(R.string.recovering_workout));
            this.mRideRecoveryDialog.setCancelable(false);
            this.mRideRecoveryDialog.setCanceledOnTouchOutside(false);
            this.mRideRecoveryDialog.show();
            DialogUtils.setDialogTitleDivider(this.mRideRecoveryDialog);
            this.mRideRecoveryDialog.getButton(-1).setEnabled(false);
            this.mRideRecoveryDialog.getButton(-2).setEnabled(false);
            int defaultType = -1;
            switch (LiveRide.getCurrentSport()) {
                case RUN:
                    defaultType = RunType.DEFAULT_TYPE_ID;
                    break;
                case RIDE:
                    defaultType = Prefs.getDefaultRideType();
                    break;
            }
            RecoveryUtil.recoverRide(SplitHelper.getSplitMode(getActivity()), id, defaultType, new RecoveryUtil.RideRecoveryComplete() { // from class: com.kopin.solos.Fragments.StartStopFragment.32
                @Override // com.kopin.solos.storage.util.RecoveryUtil.RideRecoveryComplete
                public void onRecoveryComplete() {
                    if (StartStopFragment.this.mRideRecoveryDialog.isShowing()) {
                        ProgressBar prog = (ProgressBar) StartStopFragment.this.mRideRecoveryDialog.findViewById(android.R.id.progress);
                        prog.setVisibility(8);
                        StartStopFragment.this.mRideRecoveryDialog.setMessage(StartStopFragment.this.getString(R.string.recover_ride_message));
                        StartStopFragment.this.mRideRecoveryDialog.getButton(-1).setEnabled(true);
                        StartStopFragment.this.mRideRecoveryDialog.getButton(-2).setEnabled(true);
                    }
                    Sync.sync();
                }
            });
        }
    }

    public void runOnMainThread(Runnable runnable) {
        if (Thread.currentThread().getId() != this.mThreadId) {
            this.mHandler.post(runnable);
        } else {
            runnable.run();
        }
    }

    public void openRide(SportType type, long rideId) {
        Log.d(TAG, "openRide: " + rideId);
        Intent intent = new Intent(getActivity(), (Class<?>) FinishRideActivity.class);
        intent.putExtra("ride_id", rideId);
        intent.putExtra(ThemeActivity.EXTRA_WORKOUT_TYPE, type.name());
        intent.putExtra(ThemeActivity.EXTRA_NEW_RIDE, true);
        intent.putExtra(ThemeActivity.EXTRA_RIDE_ACTIVE, LiveRide.isActiveRide());
        intent.setFlags(VCardConfig.FLAG_APPEND_TYPE_PARAM);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_right, R.anim.slide_left);
    }

    public void setData() {
        if (this.mBound && this.mTimeHelper.isStarted() && this.mTimerText != null) {
            this.mTimerText.setText(this.mTimeHelper.getTimeAsString());
            for (CustomiseableRideMetrics.RideScreenMetric rideScreenMetric : CustomiseableRideMetrics.RideScreenMetric.values()) {
                this.customiseableRideMetrics.setMetricValue(formattedValueForMetric(rideScreenMetric), rideScreenMetric);
            }
            this.txtFtpDurationValue.setText(Utility.formatTime(LiveRide.getCountdownTime()));
            TextView textView = this.txtFtpPowerValue;
            Object[] objArr = new Object[1];
            objArr[0] = Integer.valueOf(Ride.hasData(this.mService.getLastPower()) ? (int) this.mService.getLastPower() : 0);
            textView.setText(String.format("%d", objArr));
            TextView textView2 = this.txtFtpAvePowerValue;
            Object[] objArr2 = new Object[1];
            objArr2[0] = Integer.valueOf(Ride.hasData(LiveRide.getAveragePower()) ? (int) LiveRide.getAveragePower() : 0);
            textView2.setText(String.format("%d", objArr2));
            return;
        }
        if (this.mTimerText != null) {
            this.mTimerText.setText(TimeHelper.ZERO);
            this.txtFtpDurationValue.setText(TimeHelper.ZERO);
            this.customiseableRideMetrics.resetMetricValues();
            this.txtFtpPowerValue.setText("0");
            this.txtFtpAvePowerValue.setText("0");
        }
    }

    private String formattedValueForMetric(CustomiseableRideMetrics.RideScreenMetric rideScreenMetric) {
        switch (rideScreenMetric) {
            case DISTANCE:
                return formatMetricDecimal(this.mService.getLastDistanceForLocale());
            case AVG_SPEED:
                return formatMetricDecimal(this.mService.getAverageSpeedForLocale());
            case SPEED:
                return formatMetricDecimal(this.mService.getLastSpeedForLocale());
            case OVERALL_CLIMB:
                return formatMetricInt(LiveRide.getOverallClimbForLocale());
            case CADENCE:
                return formatMetricInt(this.mService.getLastCadence());
            case AVG_CADENCE:
                return formatMetricInt(LiveRide.getAverageCadence());
            case CALORIES:
                return formatMetricInt(LiveRide.getCurrentCalories());
            case HEART_RATE:
                return formatMetricInt(this.mService.getLastHeartRate());
            case AVG_HEART_RATE:
                return formatMetricInt(LiveRide.getAverageHeartRate());
            case OXYGEN:
                return formatMetricInt(this.mService.getLastOxygen());
            case POWER:
                return formatMetricInt(this.mService.getLastPower());
            case AVG_POWER:
                return formatMetricInt(LiveRide.getAveragePower());
            case STEP:
                return formatMetricInt(this.mService.getLastCadence());
            case AVG_STEP:
                return formatMetricInt(LiveRide.getAverageCadence());
            case STRIDE:
                return formatMetricDecimal(Conversion.strideForLocale(this.mService.getLastStride()));
            case PACE:
                return PaceUtil.formatPace(Conversion.paceForLocale(this.mService.getLastPace()));
            case AVG_PACE:
                return PaceUtil.formatPace(LiveRide.getAveragePaceLocale());
            case KICK:
                return formatMetricInt(this.mService.getLastPower());
            case AVG_KICK:
                return formatMetricInt(LiveRide.getAveragePower());
            case ELEVATION_CHANGE:
                float elevLocale = this.mService.getElevationChangeForLocale();
                int elevation = elevLocale != -2.14748365E9f ? (int) elevLocale : 0;
                return formatMetricInt(elevation);
            default:
                return null;
        }
    }

    private String formatMetricInt(double value) {
        return String.format(Locale.US, "%d", Integer.valueOf(Math.max((int) value, 0)));
    }

    private String formatMetricDecimal(double value) {
        return String.format(Locale.US, "%.2f", Double.valueOf(Math.max(value, 0.0d)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPauseAlertOverlay(boolean isVisible, boolean isAutoPause) {
        if (isAutoPause && isVisible()) {
            if (isVisible) {
                this.mCountdownDialog.show(isAutoPause ? R.string.auto_pause_message : R.string.manual_pause_message, isAutoPause ? R.string.pedal_faster : R.string.press_resume, R.drawable.ic_auto_pause, null);
            } else {
                this.mCountdownDialog.dismiss();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showAutoStartOverlay(boolean show) {
        if (isVisible()) {
            if (show && !this.mAutoStartDialog.isShowing()) {
                this.mAutoStartDialog.show(R.string.auto_start_over_title, ThemeUtil.getResourceId(getActivity(), R.attr.strAutoStartMsg), ThemeUtil.getResourceId(getActivity(), R.attr.drawableAutoStart), this.mAutoStartCancel);
            } else {
                this.mAutoStartDialog.dismiss();
            }
        }
    }

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
    }

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private class ClockTask extends TimerTask {
        private ClockTask() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            StartStopFragment.this.runOnMainThread(new Runnable() { // from class: com.kopin.solos.Fragments.StartStopFragment.ClockTask.1
                @Override // java.lang.Runnable
                public void run() {
                    if (!StartStopFragment.this.mTimeHelper.isPaused()) {
                        StartStopFragment.this.setData();
                    }
                }
            });
        }
    }

    @Override // com.kopin.solos.HardwareReceiverService.HeadsetStateListener
    public void onHeadsetConnected() {
        runOnMainThread(new Runnable() { // from class: com.kopin.solos.Fragments.StartStopFragment.36
            @Override // java.lang.Runnable
            public void run() {
                if (StartStopFragment.this.isStillRequired() && StartStopFragment.this.isAdded()) {
                    if (SensorWarnings.hasHeadsetWarning()) {
                        SensorWarnings.removeHeadsetWarning();
                    }
                    StartStopFragment.this.updateHeadsetNotification();
                }
            }
        });
    }

    @Override // com.kopin.solos.HardwareReceiverService.HeadsetStateListener
    public void onHeadsetDisconnected() {
        runOnMainThread(new Runnable() { // from class: com.kopin.solos.Fragments.StartStopFragment.37
            @Override // java.lang.Runnable
            public void run() {
                if (StartStopFragment.this.isStillRequired() && StartStopFragment.this.isAdded()) {
                    StartStopFragment.this.updateHeadsetBar(HeadsetState.DISCONNECTED);
                    StartStopFragment.this.checkHeadsetConnection();
                }
            }
        });
    }

    @Override // com.kopin.solos.HardwareReceiverService.HeadsetStateListener
    public void onHeadsetReady() {
        runOnMainThread(new Runnable() { // from class: com.kopin.solos.Fragments.StartStopFragment.38
            @Override // java.lang.Runnable
            public void run() {
                if (StartStopFragment.this.isStillRequired() && StartStopFragment.this.isAdded()) {
                    StartStopFragment.this.checkHeadsetConnection();
                }
            }
        });
    }

    static class CountdownDialog extends Dialog {
        private TextView mCounterText;
        private ImageView mIcon;
        private TextView mMessageText;
        private TextView mTitleText;

        public CountdownDialog(Context context) {
            super(context, android.R.style.Theme.DeviceDefault.Dialog.NoActionBar);
            setContentView(R.layout.layout_overlay);
            getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            this.mMessageText = (TextView) findViewById(R.id.txtCountdownMsg);
            this.mCounterText = (TextView) findViewById(R.id.txtCounter);
            this.mTitleText = (TextView) findViewById(R.id.txtTitle);
            this.mIcon = (ImageView) findViewById(R.id.imageOverlay);
        }

        void show(int msgId, int textId, DialogInterface.OnCancelListener cb) {
            this.mMessageText.setText(msgId);
            this.mCounterText.setText(textId);
            this.mCounterText.setTextSize(1, 18.0f);
            setOnCancelListener(cb);
            setCancelable(true);
            show();
        }

        void show(int titleId, int msgId, int iconResId, DialogInterface.OnCancelListener cb) {
            this.mTitleText.setText(titleId);
            this.mIcon.setImageResource(iconResId);
            this.mMessageText.setText(msgId);
            this.mIcon.setVisibility(0);
            this.mCounterText.setVisibility(8);
            this.mTitleText.setVisibility(0);
            setOnCancelListener(cb);
            setCancelable(true);
            show();
        }

        void show(int msgId, String text, DialogInterface.OnCancelListener cb) {
            this.mMessageText.setText(msgId);
            this.mCounterText.setText(text);
            this.mTitleText.setVisibility(8);
            this.mCounterText.setVisibility(0);
            this.mIcon.setVisibility(8);
            setOnCancelListener(cb);
            setCancelable(true);
            show();
        }

        void setText(String text) {
            this.mCounterText.setText(text);
        }
    }

    @Override // com.kopin.groupcom.GroupCom.GroupComListener
    public void onGroupComError(GroupCom.GroupComError error) {
    }

    @Override // com.kopin.groupcom.GroupCom.GroupComListener
    public void onSessionStateChange(ChatGroup chatGroup) {
        updateGroupChatMenuItem(chatGroup.sessionState);
        updateGroupComStatus(chatGroup);
    }

    @Override // com.kopin.groupcom.GroupCom.GroupComListener
    public void onSessionTimerTick(ChatGroup chatGroup) {
        updateGroupComStatus(chatGroup);
    }

    @Override // com.kopin.groupcom.GroupCom.GroupComListener
    public void onSubscriberConnected(String userName, int count) {
    }

    @Override // com.kopin.groupcom.GroupCom.GroupComListener
    public void onSubscriberDropped(String userName, int count) {
    }

    @Override // com.kopin.groupcom.GroupCom.GroupComListener
    public void onChatGroupsChanged(List<ChatGroup> chatGroups) {
    }

    private void updateGroupComStatus(final ChatGroup group) {
        runOnMainThread(new Runnable() { // from class: com.kopin.solos.Fragments.StartStopFragment.39
            @Override // java.lang.Runnable
            public void run() {
                String formattedGroupName;
                if (group == null || group.sessionState == ChatGroup.SessionState.NONE) {
                    if (StartStopFragment.this.textGroupComStatus.getAnimation() == null || StartStopFragment.this.textGroupComStatus.getAnimation().hasEnded()) {
                        StartStopFragment.this.textGroupComStatus.setVisibility(8);
                        return;
                    }
                    return;
                }
                StringBuilder builder = new StringBuilder("" + group.sessionState);
                builder.append("  |  ");
                if (group.GroupName.length() > 20) {
                    String formattedGroupName2 = group.GroupName.substring(0, 17);
                    formattedGroupName = formattedGroupName2 + "…";
                } else {
                    formattedGroupName = group.GroupName;
                }
                builder.append(formattedGroupName);
                switch (AnonymousClass40.$SwitchMap$com$kopin$peloton$groupcom$ChatGroup$SessionState[group.sessionState.ordinal()]) {
                    case 1:
                        StartStopFragment.this.textGroupComStatus.setBackgroundResource(R.color.sessionConnected);
                        StartStopFragment.this.showGroupStatusBar(0, false);
                        builder.append("  |  ");
                        builder.append(Utility.formatTime(group.sessionTime));
                        break;
                    case 2:
                        StartStopFragment.this.showGroupStatusBar(0, false);
                        StartStopFragment.this.textGroupComStatus.setBackgroundResource(R.color.sessionConnecting);
                        break;
                    case 3:
                        StartStopFragment.this.textGroupComStatus.setBackgroundResource(R.color.sessionDisconnected);
                        StartStopFragment.this.showGroupStatusBar(8, true);
                        StartStopFragment.this.mService.getAppService().refreshVocon(com.kopin.pupil.aria.Prefs.isVoconAlwaysOn());
                        break;
                    case 4:
                        StartStopFragment.this.textGroupComStatus.setBackgroundResource(R.color.sessionConnecting);
                        boolean networkAvailable = Utility.isNetworkAvailable(StartStopFragment.this.getActivity());
                        if (networkAvailable) {
                            StartStopFragment.this.showGroupStatusBar(0, false);
                        } else {
                            StartStopFragment.this.showGroupStatusBar(8, true);
                        }
                        break;
                }
                StartStopFragment.this.textGroupComStatus.setText(builder.toString());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showGroupStatusBar(int visibility, boolean animateToHide) {
        if (animateToHide) {
            Animation fadeInAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_slow);
            this.textGroupComStatus.startAnimation(fadeInAnimation);
        }
        this.textGroupComStatus.setVisibility(visibility);
    }
}
