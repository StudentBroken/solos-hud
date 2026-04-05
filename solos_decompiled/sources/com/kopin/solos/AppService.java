package com.kopin.solos;

import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import com.kopin.accessory.packets.ActionType;
import com.kopin.accessory.packets.ButtonType;
import com.kopin.accessory.packets.StatusType;
import com.kopin.accessory.utility.CallHelper;
import com.kopin.groupcom.GroupCom;
import com.kopin.peloton.groupcom.ChatGroup;
import com.kopin.pupil.ConnectionManager;
import com.kopin.pupil.PupilDevice;
import com.kopin.pupil.SolosDevice;
import com.kopin.pupil.VCApplication;
import com.kopin.pupil.aria.AriaController;
import com.kopin.pupil.aria.AriaHost;
import com.kopin.pupil.aria.tts.AriaTTS;
import com.kopin.pupil.ui.VCNotification;
import com.kopin.pupil.update.util.FirmwareFlash;
import com.kopin.pupil.util.Action;
import com.kopin.pupil.util.VolumeHelper;
import com.kopin.solos.HardwareReceiverService;
import com.kopin.solos.RideControl;
import com.kopin.solos.cabledfu.CableFlash;
import com.kopin.solos.common.SportType;
import com.kopin.solos.common.config.Config;
import com.kopin.solos.config.Features;
import com.kopin.solos.debug.EventLog;
import com.kopin.solos.metrics.MetricData;
import com.kopin.solos.metrics.MetricResource;
import com.kopin.solos.metrics.TemplateManager;
import com.kopin.solos.notifications.MetricNotifier;
import com.kopin.solos.notifications.NotificationService;
import com.kopin.solos.pages.PageHelper;
import com.kopin.solos.pages.PageNav;
import com.kopin.solos.phone.CallsAndMessages;
import com.kopin.solos.sensors.Sensor;
import com.kopin.solos.sensors.SensorList;
import com.kopin.solos.storage.DataListener;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.storage.Workout;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.HeartRateZones;
import com.kopin.solos.storage.util.MetricType;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.update.FwFlash;
import com.kopin.solos.util.BatteryStatus;
import com.kopin.solos.util.TTSHelper;
import com.kopin.solos.virtualworkout.VirtualCoach;
import com.kopin.solos.vocon.SolosAriaApp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes37.dex */
public class AppService extends VCApplication implements HardwareReceiverService.InfoListener, RideControl.RideObserver {
    public static final String BATTERY_LEVEL_ICON = "battery_level";
    private static int COLOR_NOTIFICATION_BACKGROUND = 0;
    private static int COLOR_NOTIFICATION_BORDER = 0;
    public static final String HOME = "home";
    private static final long MIN_UPDATE_TIME = 350;
    public static final int MULTI_TTS = 263;
    public static final int NOTIFICATION_TTS = 264;
    public static final String NO_REFRESH = "no_refresh";
    public static final String PAUSED_PAGE = "paused";
    public static final String PUPIL_IMG = "pupil_img";
    private static final String TAG = "AppService";
    public static final int TARGET_TTS = 265;
    private static final long TTS_TIMEOUT = 15000;
    public static final String TWO_VIEW = "two_view";
    public static final int UPDATE_TTS = 262;
    private AriaController mAria;
    private BatteryStatus mBattery;
    private boolean mBound;
    private volatile boolean mChangedPage;
    private final Runnable mClockUpdateRunnable;
    private ServiceConnection mConnection;
    private DataListener mDataListener;
    private volatile boolean mHasTTS;
    private long mHeadsetSleepTimestamp;
    private volatile boolean mIsOpened;
    private long mLastManualScreenChange;
    private String mLastPage;
    private long mLastSilenceTimestamp;
    private int mLastStatusRequest;
    private long mLastTTSTime;
    private Handler mMainHandler;
    private final FirmwareFlash.MaintenanceModeListener mMaintenanceModeListener;
    private volatile String mNextPage;
    private final Runnable mNextScreenChanger;
    private boolean mOnHome;
    private Handler mPageHandler;
    private Map<MetricType, Bitmap> mPageImages;
    private String mPageShownBeforePause;
    private CallsAndMessages mPhoneActionListener;
    private HardwareReceiverService mService;
    private VCApplication.StatusListener mStatusListener;
    private int mTTSId;
    private int mTTSInterval;
    private volatile boolean mTTSOnPage;
    private int mTTSPri;
    private TemplateManager mTemplateManager;
    private final NotificationService.NewNotificationCallback onNotice;
    private static final int[] batteryIcons = {com.kopin.solos.core.R.drawable.battery_zone_20, com.kopin.solos.core.R.drawable.battery_zone_40, com.kopin.solos.core.R.drawable.battery_zone_60, com.kopin.solos.core.R.drawable.battery_zone_80, com.kopin.solos.core.R.drawable.battery_zone_100, com.kopin.solos.core.R.drawable.battery_charging};
    private static long CLOCK_UPDATE_DELAY = 1000;

    static /* synthetic */ int access$1408(AppService x0) {
        int i = x0.mLastStatusRequest;
        x0.mLastStatusRequest = i + 1;
        return i;
    }

    public AppService(ConnectionManager connectionManager, HardwareReceiverService service) {
        super(connectionManager, service);
        this.mHasTTS = false;
        this.mNextPage = null;
        this.mIsOpened = false;
        this.mTTSOnPage = false;
        this.mChangedPage = false;
        this.mBound = false;
        this.mTTSInterval = 0;
        this.mLastManualScreenChange = 0L;
        this.mOnHome = false;
        this.mLastPage = "";
        this.mPageImages = new HashMap();
        this.mLastStatusRequest = -1;
        this.mHeadsetSleepTimestamp = 0L;
        this.mBattery = null;
        this.mLastSilenceTimestamp = 0L;
        this.mMaintenanceModeListener = new FirmwareFlash.MaintenanceModeListener() { // from class: com.kopin.solos.AppService.1
            @Override // com.kopin.pupil.update.util.FirmwareFlash.MaintenanceModeListener
            public void onUnexpectedMaintenanceMode() {
                Log.d(AppService.TAG, "Device has connected and is in maintenance mode, when no update is running...");
                PageNav.refreshPages(PageNav.PageMode.FWFLASH);
                PageNav.showPage(FwFlash.PAGE_ID);
            }
        };
        this.mNextScreenChanger = new Runnable() { // from class: com.kopin.solos.AppService.4
            @Override // java.lang.Runnable
            public void run() {
                String nextPage;
                while (true) {
                    if (PageNav.hasAutoChange() && LiveRide.isActiveRide()) {
                        nextPage = PageNav.getNextPageId();
                        AppService.this.mChangedPage = !AppService.this.mLastPage.equals(nextPage);
                    } else {
                        nextPage = PageNav.getCurrentPageId();
                        AppService.this.mChangedPage = false;
                    }
                    try {
                        AppService.this.gotoPage(PageHelper.convertPage(nextPage), nextPage, null);
                        return;
                    } catch (Exception ex) {
                        Log.e(AppService.TAG, "Error going to next page " + nextPage + ", " + ex.getMessage());
                    }
                }
            }
        };
        this.mDataListener = new DataListener() { // from class: com.kopin.solos.AppService.11
            @Override // com.kopin.solos.storage.DataListener
            public void onButtonPress(Sensor.ButtonAction action) {
                switch (AnonymousClass16.$SwitchMap$com$kopin$solos$sensors$Sensor$ButtonAction[action.ordinal()]) {
                    case 1:
                        AppService.this.onPauseButtonPressed();
                        break;
                    case 2:
                        AppService.this.onLapButtonPressed();
                        break;
                }
            }
        };
        this.mConnection = new ServiceConnection() { // from class: com.kopin.solos.AppService.12
            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName className, IBinder service2) {
                AppService.this.mBound = true;
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName arg0) {
                AppService.this.mBound = false;
            }
        };
        this.mStatusListener = new VCApplication.StatusListener() { // from class: com.kopin.solos.AppService.13
            @Override // com.kopin.pupil.VCApplication.StatusListener
            public void onStatus(PupilDevice.DeviceStatus deviceStatus) {
                byte newVal;
                if (AppService.this.mBattery != null) {
                    AppService.this.mBattery.updateStatus(deviceStatus);
                }
                if (deviceStatus.hasLux()) {
                    int lux = deviceStatus.getLux();
                    Log.d(AppService.TAG, "ALS: " + lux);
                    if (Prefs.hasHeadsetAutoBrightness()) {
                        PupilDevice.DeviceStatus status = new PupilDevice.DeviceStatus();
                        if (lux == 0) {
                            newVal = 8;
                        } else if (lux < 8) {
                            newVal = 24;
                        } else if (lux < 30) {
                            newVal = 48;
                        } else if (lux < 100) {
                            newVal = CallHelper.CallState.FLAG_CALL_ACTIVE;
                        } else if (lux < 1000) {
                            newVal = -80;
                        } else {
                            newVal = -1;
                        }
                        status.setBrightness(newVal);
                        PupilDevice.sendDeviceStatus(status);
                    }
                }
            }
        };
        this.mClockUpdateRunnable = new Runnable() { // from class: com.kopin.solos.AppService.14
            @Override // java.lang.Runnable
            public void run() {
                Log.d(AppService.TAG, "Clock Update running: " + System.currentTimeMillis());
                AppService.this.mTemplateManager.updateTime(LiveRide.getTime());
                AppService.this.mService.getSplitHelper().onTime(LiveRide.getTime());
                PageNav.checkCurrentPage();
                if (AppService.this.mIsOpened) {
                    if (AppService.this.mPhoneActionListener.isOnCall()) {
                        AppService.this.mPhoneActionListener.refresh();
                    }
                    if (AppService.this.mLastStatusRequest < 30 && AppService.this.mLastStatusRequest != -1) {
                        if (AppService.this.mLastStatusRequest % 2 == 1 && AppService.this.isHeadsetAwake() && Prefs.hasHeadsetAutoBrightness()) {
                            PupilDevice.requestStatus(StatusType.AMBIENT_LIGHT);
                        }
                    } else {
                        PupilDevice.requestStatus(StatusType.BATTERY_LEVEL, StatusType.POWER_AVAILABLE);
                        AppService.this.mLastStatusRequest = -1;
                    }
                    AppService.access$1408(AppService.this);
                    long now = System.currentTimeMillis();
                    if (AppService.this.mHasTTS && now - AppService.this.mLastTTSTime >= AppService.TTS_TIMEOUT) {
                        AppService.this.onTTSComplete(AppService.this.mTTSId);
                    }
                    int headsetSleepTime = Prefs.getHeadsetSleepTime() * 1000;
                    if (headsetSleepTime >= 0 && AppService.this.isHeadsetAwake() && !AppService.this.mService.isActiveRide() && !AppService.this.mPhoneActionListener.isOnCall() && now - AppService.this.mHeadsetSleepTimestamp >= headsetSleepTime) {
                        AppService.this.sendGotoSleep();
                    }
                }
                AppService.this.mMainHandler.postDelayed(this, AppService.CLOCK_UPDATE_DELAY);
            }
        };
        this.onNotice = new NotificationService.NewNotificationCallback() { // from class: com.kopin.solos.AppService.15
            @Override // com.kopin.solos.notifications.NotificationService.NewNotificationCallback
            public void onNotification(String pkg, int iconId, String appName, String title, String text) {
                Bitmap icon = null;
                try {
                    Resources res = AppService.this.getPackageManager().getResourcesForApplication(pkg);
                    if (res != null) {
                        icon = BitmapFactory.decodeResource(res, iconId);
                    }
                } catch (PackageManager.NameNotFoundException e) {
                }
                String info = "";
                if (title != null && !title.isEmpty() && !title.equals("null")) {
                    info = "" + title;
                }
                if (text != null && !text.isEmpty() && !text.equals("null")) {
                    if (info.length() > 0) {
                        info = info + " - ";
                    }
                    info = info + text;
                }
                AppService.this.sendDropDownNotification(icon, icon == null ? AppService.PUPIL_IMG : "", 5000, info, appName, Prefs.isTTSNotificationsEnabled());
            }
        };
        SolosDevice.Product model = SolosDevice.getLastHeadsetModel();
        FirmwareFlash.init(service, model.toString(), this.mMaintenanceModeListener);
        Features.updateForProduct(model, PupilDevice.isConnected(), Prefs.isWatchMode());
        CableFlash.init(service, SolosDevice.getCableProductName(), null, Config.IS_RELEASE_FIRMWARE_SERVER ? false : true);
        this.mTemplateManager = new TemplateManager(this);
        PageNav.init(getApplicationContext(), this, this.mTemplateManager);
        this.mPageHandler = new Handler();
        this.mMainHandler = new Handler(Looper.getMainLooper());
        MetricNotifier.init(this);
        this.mPhoneActionListener = new CallsAndMessages(this);
        onServiceConnected(service);
        SQLHelper.init(getApplicationContext());
        refreshDoublePages();
        COLOR_NOTIFICATION_BACKGROUND = getResources().getColor(com.kopin.solos.core.R.color.about_background);
        COLOR_NOTIFICATION_BORDER = getResources().getColor(com.kopin.solos.core.R.color.app_actionbar_divider);
        NotificationService.setCallback(this.onNotice);
        RideControl.init(this, getHardwareReceiverService().getSplitHelper());
        RideControl.registerObserver(this);
        VirtualCoach.init(this);
        HeartRateZones.init(this);
        this.mAria = new AriaHost(getApplicationContext(), SolosAriaApp.mFactory);
        this.mBattery = new BatteryStatus(this);
        this.mMainHandler.postDelayed(this.mClockUpdateRunnable, CLOCK_UPDATE_DELAY);
    }

    public void refreshVocon(boolean startIfEnabled) {
        this.mAria.setEnabled(com.kopin.pupil.aria.Prefs.isVoconEnabled());
        if (startIfEnabled) {
            startVocon();
        }
    }

    public void startVocon() {
        if (this.mAria.isEnabled() && !this.mAria.isListening() && !this.mPhoneActionListener.isOnCall() && !FirmwareFlash.isActive() && !LiveRide.isActiveFtp()) {
            this.mAria.start();
        }
    }

    public void stopVocon() {
        this.mAria.stop();
    }

    public boolean isVoconActive() {
        return this.mAria.isListening();
    }

    public boolean isInCall() {
        String status = this.mAria.getStatus();
        return status.equalsIgnoreCase("incall");
    }

    private boolean isBluetoothEnabled() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }

    public void onCreate() {
        Intent intent = new Intent(this, (Class<?>) HardwareReceiverService.class);
        bindService(intent, this.mConnection, 1);
    }

    public void onDestroy() {
        NotificationService.setCallback(null);
        if (this.mBound) {
            unbindService(this.mConnection);
            this.mBound = false;
        }
    }

    public void addPages(List<String> pages, List<Integer> pageIds) {
        if (pages == null || pageIds == null || pages.isEmpty() || pageIds.isEmpty() || pages.size() != pageIds.size()) {
            Log.e(TAG, "Inconsistent page list!");
            return;
        }
        for (int i = 0; i < pages.size(); i++) {
            addPage(pages.get(i), pageIds.get(i).intValue());
        }
    }

    @Override // com.kopin.pupil.VCApplication
    public void onConnected() {
        addPage(PageNav.loadPages());
        addBitmap("time_img", com.kopin.solos.core.R.drawable.stop_watch_icon);
        addBitmap("swervearrow", com.kopin.solos.core.R.drawable.ic_straight);
        addBitmap("distance_flag", com.kopin.solos.core.R.drawable.ic_flags_headset);
        addBitmap("goal_flag", com.kopin.solos.core.R.drawable.ic_finished);
        addBitmap("distance_compass", com.kopin.solos.core.R.drawable.ic_compass_headset);
        addBitmap("time_icon", com.kopin.solos.core.R.drawable.time_icon);
        addBitmap("time_elapsed", com.kopin.solos.core.R.drawable.ic_elapsed_time);
        addBitmap("time", com.kopin.solos.core.R.drawable.time_icon);
        addBitmap("stats_icon", com.kopin.solos.core.R.drawable.ic_trophy);
        addBitmap(MetricResource.CADENCE, Utility.getBitmapForDensity(this, MetricResource.CADENCE.getResourceImage(), 240));
        addBitmap(MetricResource.CALORIES, Utility.getBitmapForDensity(this, MetricResource.CALORIES.getResourceImage(), 240));
        addBitmap(MetricResource.DISTANCE, Utility.getBitmapForDensity(this, MetricResource.DISTANCE.getResourceImage(), 240));
        addBitmap(MetricResource.ELEVATION, Utility.getBitmapForDensity(this, MetricResource.ELEVATION.getResourceImage(), 240));
        addBitmap(MetricResource.HEARTRATE, Utility.getBitmapForDensity(this, MetricResource.HEARTRATE.getResourceImage(), 240));
        addBitmap(MetricResource.OXYGENATION, Utility.getBitmapForDensity(this, MetricResource.OXYGENATION.getResourceImage(), 240));
        addBitmap(MetricResource.SPEED, Utility.getBitmapForDensity(this, MetricResource.SPEED.getResourceImage(), 240));
        addBitmap(MetricResource.POWER, Utility.getBitmapForDensity(this, MetricResource.POWER.getResourceImage(), 240));
        addBitmap(MetricResource.INTENSITY_FACTOR, Utility.getBitmapForDensity(this, MetricResource.INTENSITY_FACTOR.getResourceImage(), 240));
        addBitmap(MetricResource.STRIDE, Utility.getBitmapForDensity(this, MetricResource.STRIDE.getResourceImage(), 240));
        addBitmap(MetricResource.PACE, Utility.getBitmapForDensity(this, MetricResource.PACE.getResourceImage(), 240));
        addBitmap(MetricResource.STEP, Utility.getBitmapForDensity(this, MetricResource.STEP.getResourceImage(), 240));
        addBitmap(MetricResource.KICK, Utility.getBitmapForDensity(this, MetricResource.KICK.getResourceImage(), 240));
        addBitmap(MetricResource.DERIVED_POWER, Utility.getBitmapForDensity(this, MetricResource.DERIVED_POWER.getResourceImage(), 240));
        addBitmap("target_distance_icon", Utility.getBitmapForDensity(this, com.kopin.solos.core.R.drawable.ic_target_icon, 240));
        addBitmap("pause_icon", Utility.getBitmapForDensity(this, com.kopin.solos.core.R.drawable.ic_pause, 320));
        addBitmap("bike_img", com.kopin.solos.core.R.drawable.bike_icon);
        addBitmap(PUPIL_IMG, com.kopin.solos.core.R.drawable.ic_vc_icon);
        addBitmap("vc_img", com.kopin.solos.core.R.drawable.ic_vc_icon);
        addBitmap("ic_notification", com.kopin.solos.core.R.drawable.ic_in_chat);
        addBitmap("bike_img2", com.kopin.solos.core.R.drawable.ic_start_ride_icon);
        addBitmap("vc_logo", com.kopin.solos.core.R.drawable.ic_vc_logo_2);
        addBitmap(BATTERY_LEVEL_ICON, com.kopin.solos.core.R.drawable.battery_zone_20);
        addBitmap("countdown_circle", com.kopin.solos.core.R.drawable.countdown_circle);
        addBitmap(CallsAndMessages.CALL_INCOMING_ICON, com.kopin.solos.core.R.drawable.ic_incoming_phone);
        addBitmap(CallsAndMessages.CALL_OUTGOING_ICON, com.kopin.solos.core.R.drawable.ic_outgoing_phone);
        addBitmap(CallsAndMessages.CALL_TERMINATE_ICON, com.kopin.solos.core.R.drawable.ic_terminate_phone);
        addBitmap(CallsAndMessages.MESSAGE_ICON, com.kopin.solos.core.R.drawable.ic_message);
        addBitmap("fwflash_update", com.kopin.solos.core.R.drawable.ic_maintenance_mode);
        addBitmap("chevron_up", com.kopin.solos.core.R.drawable.chevron_up);
        addBitmap("chevron_down", com.kopin.solos.core.R.drawable.chevron_down);
        addBitmap("button_press", com.kopin.solos.core.R.drawable.ic_headset_button);
        this.mPageImages.put(MetricType.CALORIES, Utility.getBitmapForDensity(this, MetricResource.CALORIES.getResourceImage(), 160));
        this.mPageImages.put(MetricType.CADENCE, Utility.getBitmapForDensity(this, MetricResource.CADENCE.getResourceImage(), 160));
        this.mPageImages.put(MetricType.HEARTRATE, Utility.getBitmapForDensity(this, MetricResource.HEARTRATE.getResourceImage(), 160));
        this.mPageImages.put(MetricType.OXYGENATION, Utility.getBitmapForDensity(this, MetricResource.OXYGENATION.getResourceImage(), 160));
        this.mPageImages.put(MetricType.POWER, Utility.getBitmapForDensity(this, MetricResource.POWER.getResourceImage(), 160));
        this.mPageImages.put(MetricType.AVERAGE_POWER, Utility.getBitmapForDensity(this, MetricResource.POWER.getResourceImage(), 160));
        this.mPageImages.put(MetricType.SPEED, Utility.getBitmapForDensity(this, MetricResource.SPEED.getResourceImage(), 160));
        this.mPageImages.put(MetricType.ELEVATION, Utility.getBitmapForDensity(this, MetricResource.ELEVATION.getResourceImage(), 160));
        this.mPageImages.put(MetricType.DISTANCE, Utility.getBitmapForDensity(this, MetricResource.DISTANCE.getResourceImage(), 160));
        this.mPageImages.put(MetricType.TARGET_DISTANCE, Utility.getBitmapForDensity(this, com.kopin.solos.core.R.drawable.ic_target_icon, 160));
        this.mPageImages.put(MetricType.TIME, Utility.getBitmapForDensity(this, com.kopin.solos.core.R.drawable.ic_elapsed_time2, 160));
        this.mPageImages.put(MetricType.TARGET_TIME, Utility.getBitmapForDensity(this, com.kopin.solos.core.R.drawable.ic_target_time_icon, 160));
        this.mPageImages.put(MetricType.STRIDE, Utility.getBitmapForDensity(this, MetricResource.STRIDE.getResourceImage(), 160));
        this.mPageImages.put(MetricType.PACE, Utility.getBitmapForDensity(this, MetricResource.PACE.getResourceImage(), 160));
        this.mPageImages.put(MetricType.STEP, Utility.getBitmapForDensity(this, MetricResource.STEP.getResourceImage(), 160));
        this.mPageImages.put(MetricType.KICK, Utility.getBitmapForDensity(this, MetricResource.KICK.getResourceImage(), 160));
        this.mPageImages.put(MetricType.NONE, Utility.getBitmapForDensity(this, com.kopin.solos.core.R.drawable.ic_none, 160));
        this.mBattery.reset();
        setConnectionListener(new VCApplication.ConnectionListener() { // from class: com.kopin.solos.AppService.2
            @Override // com.kopin.pupil.VCApplication.ConnectionListener
            public void onStateChanged(VCApplication.ConnectionListener.ConnectionState connectionState) {
                AppService.this.mService.sendNotification(HardwareReceiverService.NotificationType.CONNECTION);
            }
        });
        this.mPhoneActionListener.onConnected();
        this.mService.addDataListener(this.mDataListener);
        Log.i(TAG, "onConnected() headset");
        this.mService.onHeadsetConnected();
        PupilDevice.enableVAD(true);
        setVolume(Prefs.getHeadsetVolume());
        if (!Prefs.hasHeadsetAutoBrightness()) {
            setBrightness(Prefs.getHeadsetBrightness());
        }
    }

    @Override // com.kopin.pupil.VCApplication
    protected void onInfoChanged(PupilDevice.DeviceInfo deviceInfo) {
        super.onInfoChanged(deviceInfo);
        this.mService.onHeadsetReady(deviceInfo.mAntBridgeId);
        SolosDevice.Product model = SolosDevice.getHeadsetModel();
        FirmwareFlash.setProduct(model.toString(), !Config.IS_RELEASE_FIRMWARE_SERVER);
        Features.updateForProduct(model, true, Prefs.isWatchMode());
        CableFlash.setProduct(SolosDevice.getCableProductName(), Config.IS_RELEASE_FIRMWARE_SERVER ? false : true);
    }

    @Override // com.kopin.pupil.VCApplication
    public void onDisconnected() {
        Log.i(TAG, "onDisConnected() headset");
        SolosDevice.Product model = SolosDevice.getLastHeadsetModel();
        Features.updateForProduct(model, false, Prefs.isWatchMode());
        this.mService.onHeadsetDisconnected();
        this.mService.removeDataListener(this.mDataListener);
        GroupCom.disconnectSession();
        if (this.mPhoneActionListener != null) {
            this.mPhoneActionListener.onDisconnected();
        }
    }

    @Override // com.kopin.pupil.VCApplication
    public void onEnter() {
        this.mIsOpened = true;
        this.mHasTTS = false;
        sendWakeUp();
        checkTTSSettings();
        PageNav.refreshPages(FirmwareFlash.isActive() ? PageNav.PageMode.FWFLASH : PageNav.PageMode.NORMAL);
        this.mMainHandler.postDelayed(new Runnable() { // from class: com.kopin.solos.AppService.3
            @Override // java.lang.Runnable
            public void run() {
                AppService.this.gotoTimePage();
            }
        }, 250L);
        setStatusListener(this.mStatusListener);
    }

    @Override // com.kopin.pupil.VCApplication
    public void onExit() {
        this.mIsOpened = false;
        this.mPageHandler.removeCallbacksAndMessages(null);
        MetricNotifier.stop(true);
        removeStatusListener(this.mStatusListener);
        this.mHasTTS = false;
    }

    @Override // com.kopin.pupil.VCApplication
    protected void onAction(Action action) {
        if (action.getType() == Action.Type.CHANGING_PAGE) {
            String pageId = action.getPageID();
            if ("home".equals(pageId)) {
                actionPageHome();
                return;
            }
            if (MetricType.COUNTDOWN.equals(action.getPageID())) {
                String countdownMsg = getStringFromTheme(LiveRide.isStarted() ? com.kopin.solos.core.R.attr.countdownMsgResume : com.kopin.solos.core.R.attr.strCountdownMsgStart);
                updateElement(action.getTemplateID(), pageId, "countdown_msg", "content", countdownMsg);
                return;
            }
            if (action.getPageID().startsWith(TWO_VIEW)) {
                actionPageTwoView(action);
                return;
            }
            updateMetric(action.getPageID());
            if (this.mTTSOnPage && doRefresh(action)) {
                String tts = TTSHelper.getMetricTTS(this.mService, action.getPageID());
                if (!tts.trim().isEmpty()) {
                    speakUpdate(tts);
                    return;
                }
                return;
            }
            return;
        }
        if (action.getType() == Action.Type.CHANGED_PAGE) {
            this.mLastPage = action.getPageID();
            this.mOnHome = "home".equals(action.getPageID());
            if (MetricType.TIME.equals(this.mLastPage) && !Prefs.isReady()) {
                gotoPage(action.getTemplateID(), PageNav.getNextPageId(), null);
            }
            if (doRefresh(action)) {
                this.mPageHandler.removeCallbacksAndMessages(null);
                if (Prefs.hasAutoScreenChange()) {
                    this.mPageHandler.postDelayed(this.mNextScreenChanger, Prefs.getScreenTimer());
                }
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // com.kopin.pupil.VCApplication
    protected boolean onButtonPress(ButtonType button, boolean longPress) {
        boolean handled = SolosDevice.getHeadsetModel() == SolosDevice.Product.SOLOS2 ? this.mPhoneActionListener.onButtonPress(button, longPress) : false;
        if (!handled) {
            switch (button) {
                case MAIN:
                    if (longPress) {
                        if (!onPauseButtonPressed()) {
                            clearNotification();
                            refresh(true);
                        }
                    } else {
                        sendWakeUp();
                        onMainButtonPressed();
                    }
                    break;
                case FRONT:
                    onScreenChangePressed(true);
                    break;
                case BACK:
                    onScreenChangePressed(false);
                    break;
            }
        }
        return true;
    }

    @Override // com.kopin.pupil.VCApplication
    protected void onTapEvent() {
        if (Features.IS_GROUPCHAT_ENABLED && GroupCom.isInSession()) {
            if (GroupCom.isSpeaking()) {
                PupilDevice.sendAction(ActionType.PLAY_PROCESSING_CHIME);
                PupilDevice.enableVAD(false);
                GroupCom.stopSpeaking();
                return;
            } else {
                this.mLastSilenceTimestamp = System.currentTimeMillis();
                PupilDevice.sendAction(ActionType.PLAY_LISTENING_CHIME);
                PupilDevice.enableVAD(true);
                GroupCom.startSpeaking();
                return;
            }
        }
        if (com.kopin.pupil.aria.Prefs.isDoubleTapEnabled()) {
            startVocon();
        }
    }

    @Override // com.kopin.pupil.VCApplication
    protected void onVoiceDetected() {
        Log.d(TAG, "onVoiceDetected");
        this.mLastSilenceTimestamp = System.currentTimeMillis();
    }

    @Override // com.kopin.pupil.VCApplication
    protected void onSilenceDetected() {
        Log.d(TAG, "onSilenceDetected");
        this.mLastSilenceTimestamp = System.currentTimeMillis();
    }

    public void addBitmapFromResource(String id, int resource) {
        addBitmap(id, resource);
    }

    private void actionPageHome() {
        updateElement("home", "home", "time", "content", Utility.formatTimeForPage(), true);
        updateGroupComStatus("home");
        addBitmap(BATTERY_LEVEL_ICON, Utility.getBitmapForDensity(getApplicationContext(), getBatteryDrawable(), 160));
        if (Prefs.isReady()) {
            String nextPage = PageNav.getTimePageId();
            if (this.mLastPage == null || !this.mLastPage.contentEquals(nextPage)) {
                gotoPage(PageHelper.convertPage(nextPage), nextPage, null);
                return;
            }
            return;
        }
        updateHomePage(Prefs.isAuto(), Prefs.isAutoStartPossible());
    }

    private void updateHomePage(boolean isAuto, boolean isAutoStartPossible) {
        updateHomePage(isAuto, isAutoStartPossible, false);
    }

    public void updateHomePage(boolean isAuto, boolean isAutoStartPossible, boolean started) {
        int res;
        if (LiveRide.getCurrentSport() == SportType.RUN) {
            updateHomePageForRun(isAuto, isAutoStartPossible, started);
            return;
        }
        if (!isAuto) {
            res = com.kopin.solos.core.R.string.outside_mode_message_manual;
        } else if (isAutoStartPossible) {
            res = started ? com.kopin.solos.core.R.string.outside_mode_message : com.kopin.solos.core.R.string.outside_mode_message_auto_ready;
        } else {
            res = com.kopin.solos.core.R.string.no_bike_sensor_message;
            updateElement("home", "home", "title", "content", getString(com.kopin.solos.core.R.string.no_bike_sensor));
        }
        updateElement("home", "home", "message", "content", getString(res), true);
    }

    private void updateHomePageForRun(boolean isAuto, boolean isAutoStartPossible, boolean started) {
        int res;
        if (!isAuto) {
            res = com.kopin.solos.core.R.string.outside_mode_message_manual_run;
        } else if (isAutoStartPossible) {
            res = started ? com.kopin.solos.core.R.string.outside_mode_message_run : com.kopin.solos.core.R.string.outside_mode_message_auto_ready_run;
        } else {
            res = com.kopin.solos.core.R.string.no_bike_sensor_message;
            updateElement("home", "home", "title", "content", getString(com.kopin.solos.core.R.string.no_bike_sensor));
        }
        updateElement("home", "home", "message", "content", getString(res), true);
    }

    private void actionPageTwoView(Action action) {
        MetricData data = PageNav.getMetricData(action.getPageID());
        updateElement(null, action.getPageID(), "title", "content", data.getTitle());
        updateMetricImage("img1", data.getMetricType1());
        updateMetricImage("img2", data.getMetricType2());
        updateMetric(data.getMetricType1());
        updateMetric(data.getMetricType2());
        updateElement(null, action.getPageID(), "target1", "content", data.getTarget1());
        updateElement(null, action.getPageID(), "target2", "content", data.getTarget2());
        int resName1 = data.getMetricType1().getStringResourceName();
        int resName2 = data.getMetricType2().getStringResourceName();
        String title1 = resName1 > 0 ? getResources().getString(resName1) : data.getMetricType1().name();
        String title2 = resName2 > 0 ? getResources().getString(resName2) : data.getMetricType2().name();
        if (data.getMetricType1() == MetricType.TARGET_TIME || data.getMetricType2() == MetricType.TARGET_TIME) {
            long targetTime = LiveRide.getTargetTime();
            long time = (targetTime - this.mService.getTimeHelper().getTime()) / 1000;
            if (time <= 0) {
                if (data.getMetricType1() == MetricType.TARGET_TIME) {
                    title1 = getResources().getString(com.kopin.solos.core.R.string.target_time_passed);
                }
                if (data.getMetricType2() == MetricType.TARGET_TIME) {
                    title2 = getResources().getString(com.kopin.solos.core.R.string.target_time_passed);
                }
            }
        }
        if (data.getMetricType1() == MetricType.TARGET_DISTANCE || data.getMetricType2() == MetricType.TARGET_DISTANCE) {
            double targetDistance = Prefs.getTargetDistance();
            double distance = this.mService.getLastDistanceForLocale();
            if (distance >= targetDistance) {
                if (data.getMetricType1() == MetricType.TARGET_DISTANCE) {
                    title1 = getResources().getString(com.kopin.solos.core.R.string.target_distance_passed);
                }
                if (data.getMetricType2() == MetricType.TARGET_DISTANCE) {
                    title2 = getResources().getString(com.kopin.solos.core.R.string.target_distance_passed);
                }
            }
        }
        updateElement(action.getTemplateID(), action.getPageID(), "target_metric1", "content", title1.toUpperCase());
        updateElement(action.getTemplateID(), action.getPageID(), "target_metric2", "content", title2.toUpperCase(), true);
        if (this.mTTSOnPage && doRefresh(action)) {
            String tts = TTSHelper.getMetricsTTS(this.mService, data.getMetricType1().toString().toLowerCase(), data.getMetricType2().toString().toLowerCase());
            if (!tts.trim().isEmpty()) {
                speakUpdate(tts);
            }
        }
    }

    public int getBatteryDrawable() {
        if (PupilDevice.currentDeviceStatus().isOnCharge()) {
            return batteryIcons[5];
        }
        int iBatteryLevel = PupilDevice.currentDeviceStatus().getBattery();
        if (iBatteryLevel <= 20) {
            return batteryIcons[0];
        }
        if (iBatteryLevel <= 40) {
            return batteryIcons[1];
        }
        if (iBatteryLevel <= 60) {
            return batteryIcons[2];
        }
        if (iBatteryLevel <= 80) {
            return batteryIcons[3];
        }
        return batteryIcons[4];
    }

    private boolean doRefresh(Action action) {
        Bundle extra = action.getExtra();
        return extra == null || !extra.containsKey(NO_REFRESH);
    }

    private void updateMetricImage(String image, MetricType metricType) {
        Bitmap bitmap;
        if (this.mIsOpened && (bitmap = this.mPageImages.get(metricType.getBaseMetricType())) != null) {
            addBitmap(image, bitmap);
        }
    }

    private void updateMetric(String metricId) {
        this.mTemplateManager.updateMetric(metricId, true);
    }

    private void updateMetric(MetricType metricId) {
        this.mTemplateManager.updateMetric(metricId.getResource(), true);
    }

    @Override // com.kopin.pupil.VCApplication
    protected void onTTSComplete(int id) {
        Log.d("TTS", "onComplete: " + id + " (pri: " + this.mTTSPri + ")");
        this.mHasTTS = false;
        this.mTTSId = 0;
        if (this.mNextPage != null) {
            gotoPage(PageHelper.convertPage(this.mNextPage), this.mNextPage, null);
            this.mNextPage = null;
        }
        switch (this.mTTSPri) {
            case UPDATE_TTS /* 262 */:
                if (!this.mTTSOnPage && Prefs.hasTTS() && !this.mPhoneActionListener.isOnCall() && !LiveRide.isVirtualWorkout()) {
                    MetricNotifier.restart();
                }
                break;
            case MULTI_TTS /* 263 */:
                if (!this.mPhoneActionListener.isOnCall()) {
                    MetricNotifier.next();
                }
                break;
            case NOTIFICATION_TTS /* 264 */:
            case TARGET_TTS /* 265 */:
                VirtualCoach.notifyNext(this.mTTSPri);
                break;
        }
        this.mTTSPri = 0;
        this.mAria.onAudioEnd(id);
    }

    @Override // com.kopin.pupil.VCApplication
    protected void onAudioReceived(byte[] data) {
        if (this.mAria != null) {
            this.mAria.onAudioReceived(data);
        }
    }

    @Override // com.kopin.pupil.VCApplication
    public void gotoPage(String template, String pageID, Bundle extra) {
        Log.d(TAG, "gotoPage: " + template + " - " + pageID);
        if (this.mHasTTS && this.mTTSOnPage) {
            if (this.mNextPage != null) {
                this.mNextPage = null;
                this.mTemplateManager.graphAdjust();
                super.gotoPage(template, pageID, extra);
                return;
            } else {
                this.mNextPage = pageID;
                this.mPageHandler.removeCallbacksAndMessages(null);
                this.mPageHandler.postDelayed(this.mNextScreenChanger, Prefs.getScreenTimer());
                return;
            }
        }
        this.mNextPage = null;
        this.mTemplateManager.graphAdjust();
        super.gotoPage(template, pageID, extra);
    }

    public void updateGroupComStatus(String pageId) {
        ChatGroup currentChatGroup = GroupCom.getCurrentChatGroup();
        int resColor = com.kopin.solos.core.R.color.transparent;
        if (currentChatGroup != null) {
            switch (currentChatGroup.sessionState) {
                case CONNECTED:
                    resColor = com.kopin.solos.core.R.color.white;
                    break;
                case CONNECTING:
                    resColor = com.kopin.solos.core.R.color.sessionConnecting;
                    break;
            }
        }
        if (GroupCom.isSpeaking()) {
            resColor = com.kopin.solos.core.R.color.sessionConnected;
        }
        updateElement(pageId, "ic_notification", "color", Integer.valueOf(getResources().getColor(resColor)));
    }

    @Override // com.kopin.pupil.VCApplication
    public void stopTTS() {
        super.stopTTS();
        Log.d("TTS", "Stop");
        MetricNotifier.stop(false);
        this.mHasTTS = false;
    }

    public boolean speakTextIfQuiet(int id, String text) {
        Log.d("TTS", "speakIfQuiet: " + id + ": " + text);
        if (this.mPhoneActionListener.isOnCall()) {
            Log.d("TTS", " speak ignored, user is on a call");
            return false;
        }
        if (this.mHasTTS && id != 264 && id != 265) {
            return false;
        }
        if (!TTSAllowed(id)) {
            Log.d("TTS", "  Not allowed due to current Aria state (not in ride)");
            return false;
        }
        speakText(id, text);
        return true;
    }

    @Override // com.kopin.pupil.VCApplication
    public void speakText(int id, String text) {
        Log.d("TTS", "speak: " + id + ": " + text);
        if (!this.mPhoneActionListener.isOnCall()) {
            if (!this.mHasTTS || id == 264 || id == 265) {
                if (!TTSAllowed(id)) {
                    Log.d("TTS", "  Not allowed due to current Aria state (not in ride)");
                    return;
                }
                if (this.mHasTTS && this.mTTSId != 0) {
                    onTTSComplete(this.mTTSId);
                }
                this.mHasTTS = true;
                this.mTTSPri = id;
                if (isConnected()) {
                    Log.d("TTS", "  speaking");
                    this.mTTSId = AriaTTS.sayText(AriaTTS.SayPriority.VERBOSE, "Solos:" + id, text, null);
                    this.mLastTTSTime = System.currentTimeMillis();
                    return;
                }
                onTTSComplete(id);
            }
        }
    }

    private boolean TTSAllowed(int ttsId) {
        if (!this.mAria.isEnabled()) {
            return true;
        }
        if (ttsId != 262 && ttsId != 265 && ttsId != 263) {
            return true;
        }
        String status = this.mAria.getStatus();
        return status.equalsIgnoreCase("InRide");
    }

    @Override // com.kopin.pupil.VCApplication
    public void addBitmap(String id, Bitmap bitmap) {
        super.addBitmap(id, bitmap);
    }

    public void addBitmap(MetricResource metricResource, Bitmap bitmap) {
        super.addBitmap(metricResource.name(), bitmap);
    }

    public List<VCNotification.TextPart> getSplitNotificationText(VCNotification.TextPart fullText) {
        return getBrakedMessage(fullText);
    }

    public void sendDropDownNotification(String icon, int duration, String text) {
        sendDropDownNotification((Bitmap) null, icon, duration, text, (String) null, Prefs.isTTSNotificationsEnabled());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendDropDownNotification(final Bitmap icon, final String iconStr, final int duration, final String text, final String appName, final boolean andSpeak) {
        if (Thread.currentThread().getId() != this.mMainHandler.getLooper().getThread().getId()) {
            this.mMainHandler.post(new Runnable() { // from class: com.kopin.solos.AppService.5
                @Override // java.lang.Runnable
                public void run() {
                    AppService.this.sendDropDownNotification(icon, iconStr, duration, text, appName, andSpeak);
                }
            });
            return;
        }
        List<VCNotification.TextPart> textParts = getBrakedMessage(new VCNotification.TextPart(text));
        VCNotification.TextPart[] textPartsArr = (VCNotification.TextPart[]) textParts.toArray(new VCNotification.TextPart[textParts.size()]);
        super.sendDropDownNotification(icon, iconStr, PUPIL_IMG, COLOR_NOTIFICATION_BACKGROUND, COLOR_NOTIFICATION_BORDER, duration, andSpeak, null, appName, VCNotification.Priority.NORMAL, textPartsArr);
    }

    public void sendDropDownNotification(final String icon, final int duration, final boolean andSpeak, final Runnable action, final VCNotification.Priority priority, final VCNotification.TextPart... parts) {
        if (Thread.currentThread().getId() != this.mMainHandler.getLooper().getThread().getId()) {
            this.mMainHandler.post(new Runnable() { // from class: com.kopin.solos.AppService.6
                @Override // java.lang.Runnable
                public void run() {
                    AppService.this.sendDropDownNotification(icon, duration, andSpeak, action, priority, parts);
                }
            });
        } else {
            super.sendDropDownNotification(null, icon, PUPIL_IMG, COLOR_NOTIFICATION_BACKGROUND, COLOR_NOTIFICATION_BORDER, duration, andSpeak, action, null, priority, parts);
        }
    }

    public void sendDropDownNotification(String icon, int duration, Runnable action, VCNotification.Priority priority, VCNotification.TextPart... parts) {
        sendDropDownNotification(icon, duration, Prefs.isTTSNotificationsEnabled(), action, priority, parts);
    }

    @Override // com.kopin.pupil.VCApplication
    public void updateElements(String pageID) {
        Log.d(TAG, "updateElements: " + pageID);
        super.updateElements(pageID);
    }

    @Override // com.kopin.pupil.VCApplication
    public void updateElement(String templateID, String pageID, String elementID, String attribute, Object value, boolean refresh) {
        Log.d(TAG, "updateElement: " + templateID + "/" + pageID + "." + elementID + "." + attribute + " = " + value + " : refresh - " + refresh);
        super.updateElement(templateID, pageID, elementID, attribute, value, refresh);
    }

    private void speakUpdate(String update) {
        if (this.mChangedPage) {
            speakText(UPDATE_TTS, update);
        }
    }

    @Override // com.kopin.solos.HardwareReceiverService.InfoListener
    public void onListChanged() {
        if (this.mOnHome) {
            String nextPage = PageNav.getNextPageId();
            gotoPage(PageHelper.convertPage(nextPage), nextPage, null);
        }
    }

    @Override // com.kopin.solos.HardwareReceiverService.InfoListener
    public void onNotification(HardwareReceiverService.NotificationType type) {
        PageNav.checkCurrentPage();
    }

    public HardwareReceiverService getHardwareReceiverService() {
        return this.mService;
    }

    public void refreshPagesAvailability() {
        this.mMainHandler.post(new Runnable() { // from class: com.kopin.solos.AppService.7
            @Override // java.lang.Runnable
            public void run() {
                PageNav.refreshPages(PageNav.PageMode.NORMAL);
            }
        });
    }

    public void refreshDoublePages() {
        this.mMainHandler.post(new Runnable() { // from class: com.kopin.solos.AppService.8
            @Override // java.lang.Runnable
            public void run() {
                PageNav.refreshPages(PageNav.PageMode.NORMAL);
                PageNav.checkCurrentPage();
            }
        });
    }

    public void refreshSplitSettings() {
        this.mMainHandler.post(new Runnable() { // from class: com.kopin.solos.AppService.9
            @Override // java.lang.Runnable
            public void run() {
                LiveRide.refreshSplitType();
            }
        });
    }

    public void refreshUnitSystem(Prefs.UnitSystem unitSystem) {
        this.mTemplateManager.updateUnitSystem(unitSystem);
    }

    public void refreshOnStartModeChange(boolean isAuto) {
        boolean isAutoStartPossible = true;
        if (!this.mBound || this.mService == null) {
            isAutoStartPossible = isAuto;
        } else if (!isAuto || (!SensorList.isSensorConnected(Sensor.DataType.CADENCE, true) && !SensorList.isSensorConnected(Sensor.DataType.STEP, true))) {
            isAutoStartPossible = false;
        }
        if (this.mOnHome) {
            updateHomePage(isAuto, isAutoStartPossible);
        }
    }

    public void checkTTSSettings() {
        this.mMainHandler.post(new Runnable() { // from class: com.kopin.solos.AppService.10
            @Override // java.lang.Runnable
            public void run() {
                MetricNotifier.stop(true);
                if (!Prefs.hasTTS() || LiveRide.isVirtualWorkout()) {
                    AppService.this.mTTSOnPage = false;
                    return;
                }
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(AppService.this);
                String timerString = sharedPref.getString(Prefs.TTS_TIMER_KEY, "60");
                AppService.this.mTTSInterval = Utility.intFromString(timerString, 60) * 1000;
                if (AppService.this.mTTSInterval > 0) {
                    MetricNotifier.start(AppService.this.mTTSInterval);
                    AppService.this.mTTSOnPage = false;
                } else {
                    AppService.this.mTTSOnPage = true;
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void gotoTimePage() {
        this.mPageHandler.removeCallbacksAndMessages(null);
        if (FirmwareFlash.isActive()) {
            PageNav.showPage(FwFlash.PAGE_ID);
            return;
        }
        if (Prefs.isReady()) {
            String nextPage = PageNav.getTimePageId();
            gotoPage(PageHelper.convertPage(nextPage), nextPage, null);
        } else {
            if (LiveRide.lastRide() != null) {
                if (LiveRide.lastRide().getWorkoutMode() != Workout.RideMode.FTP) {
                    PageNav.showPage(MetricType.RIDE_STATS.getResource());
                    return;
                } else {
                    gotoPage("home", "home", null);
                    return;
                }
            }
            gotoPage("home", "home", null);
        }
    }

    private void gotoNextPage() {
        this.mPageHandler.removeCallbacksAndMessages(null);
        String nextPage = PageNav.getNextPageId();
        gotoPage(PageHelper.convertPage(nextPage), nextPage, null);
    }

    @Override // com.kopin.solos.RideControl.RideObserver
    public void onRideConfig(Workout.RideMode mode, long rideOrRouteId) {
    }

    @Override // com.kopin.solos.RideControl.RideObserver
    public void onRideIdle() {
        EventLog.clear();
        PageNav.showPage("home");
    }

    @Override // com.kopin.solos.RideControl.RideObserver
    public void onCountdownStarted() {
        this.mPageHandler.removeCallbacksAndMessages(null);
        this.mTemplateManager.updateCountdown(3);
        PageNav.showCountdown(true);
    }

    @Override // com.kopin.solos.RideControl.RideObserver
    public void onCountdown(int counter) {
        this.mTemplateManager.updateCountdown(counter);
        PageNav.showCountdown(true);
    }

    @Override // com.kopin.solos.RideControl.RideObserver
    public void onCountdownComplete(boolean wasCancelled) {
        PageNav.showCountdown(false);
    }

    @Override // com.kopin.solos.RideControl.RideObserver
    public void onRideReady(RideControl.StartMode startMode) {
        EventLog.start(this.mService);
        stopTTS();
        sendWakeUp();
        if (startMode != RideControl.StartMode.AUTO) {
            RideControl.requestStart();
        }
    }

    @Override // com.kopin.solos.RideControl.RideObserver
    public void onRideStarted(Workout.RideMode mode) {
        updateHomePage(Prefs.isAuto(), Prefs.isAutoStartPossible(), true);
        if (LiveRide.isFunctionalThresholdPowerMode()) {
            addPage(com.kopin.solos.core.R.xml.two_view_metric_metric);
            PageNav.refreshPages(PageNav.PageMode.FUNCTIONAL_THRESHOLD_POWER);
        } else if (LiveRide.isNavigtionRideMode()) {
            PageNav.refreshPages(PageNav.PageMode.NAVIGATION);
        } else if (VirtualCoach.isActive()) {
            addPage(VirtualCoach.getHeadsetPages());
            PageNav.refreshPages(VirtualCoach.getHeadsetPageMode());
        } else {
            addPage(com.kopin.solos.core.R.xml.single_metric);
            addPage(com.kopin.solos.core.R.xml.time);
            addPage(com.kopin.solos.core.R.xml.quad_metric);
            PageNav.refreshPages(PageNav.PageMode.NORMAL);
        }
        prepare();
        if (this.mIsOpened) {
            addBitmapFromResource("time_icon", com.kopin.solos.core.R.drawable.time_icon);
            gotoTimePage();
        }
        this.mTemplateManager.start();
        checkTTSSettings();
    }

    public void prepare() {
        this.mService.resetInitialElevation();
        this.mTemplateManager.prepare();
    }

    @Override // com.kopin.solos.RideControl.RideObserver
    public void onRideResumed(boolean userOrAuto) {
        PageNav.refreshPages(PageNav.PageMode.NORMAL);
        if (this.mPageShownBeforePause != null) {
            PageNav.showPage(this.mPageShownBeforePause);
        } else {
            gotoNextPage();
        }
        checkTTSSettings();
    }

    @Override // com.kopin.solos.RideControl.RideObserver
    public void onRidePaused(boolean userOrAuto) {
        this.mPageShownBeforePause = PageNav.getCurrentPageId();
        PageNav.refreshPages(PageNav.PageMode.PAUSED);
        gotoNextPage();
        if (this.mIsOpened && userOrAuto && Prefs.isAutoPauseEnabled()) {
            Prefs.changeAutoPause(false);
            sendDropDownNotification(PUPIL_IMG, 3000, getString(com.kopin.solos.core.R.string.auto_pause_disabled));
        }
    }

    @Override // com.kopin.solos.RideControl.RideObserver
    public boolean okToStop() {
        return true;
    }

    @Override // com.kopin.solos.RideControl.RideObserver
    public void onRideStopped(SavedWorkout ride) {
        EventLog.stop();
        PageNav.refreshPages(PageNav.PageMode.NORMAL);
        gotoTimePage();
        this.mTemplateManager.stop();
        MetricNotifier.stop(true);
        this.mHeadsetSleepTimestamp = System.currentTimeMillis();
    }

    @Override // com.kopin.solos.RideControl.RideObserver
    public void onNewLap(double lastDistance, long lastTime) {
    }

    public boolean isOpened() {
        return this.mIsOpened;
    }

    private void onMainButtonPressed() {
        if (!onAcceptNotification()) {
            if (!this.mAria.isEnabled() || this.mAria.isListening()) {
                onLapButtonPressed();
            } else if (SolosDevice.getLastHeadsetModel() == SolosDevice.Product.SOLOS && com.kopin.pupil.aria.Prefs.isDoubleTapEnabled()) {
                startVocon();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean onPauseButtonPressed() {
        if (LiveRide.isActiveRide() && !LiveRide.isFunctionalThresholdPowerMode()) {
            if (LiveRide.isPaused()) {
                RideControl.requestResume();
            } else {
                RideControl.pause();
            }
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean onLapButtonPressed() {
        if (!LiveRide.isActiveRide() || LiveRide.isFunctionalThresholdPowerMode()) {
            return false;
        }
        LiveRide.split();
        return true;
    }

    public boolean tryGotoPage(String templateId, String pageId, Bundle extra) {
        try {
            gotoPage(templateId, pageId, extra);
            this.mChangedPage = true;
            return true;
        } catch (Exception ex) {
            Log.e(TAG, "tryGotoPage not moved to " + pageId + ", " + ex.getMessage());
            return false;
        }
    }

    private void onScreenChangePressed(boolean forward) {
        if (Prefs.hasManualScreenChange() && Utility.getTimeMilliseconds() - this.mLastManualScreenChange >= MIN_UPDATE_TIME) {
            this.mLastManualScreenChange = Utility.getTimeMilliseconds();
            this.mPageHandler.removeCallbacksAndMessages(null);
            this.mNextPage = null;
            if (this.mHasTTS) {
                stopTTS();
            }
            boolean found = false;
            while (!found) {
                String nextPage = forward ? PageNav.getNextPageId() : PageNav.getPrevPageId();
                found = tryGotoPage(PageHelper.convertPage(nextPage), nextPage, null);
            }
        }
    }

    private void onServiceConnected(HardwareReceiverService service) {
        this.mService = service;
        this.mTemplateManager.setService(service);
        this.mTemplateManager.updateAll();
        String nextPage = PageNav.getNextPageId();
        gotoPage(PageHelper.convertPage(nextPage), nextPage, null);
    }

    /* JADX INFO: renamed from: com.kopin.solos.AppService$16, reason: invalid class name */
    static /* synthetic */ class AnonymousClass16 {
        static final /* synthetic */ int[] $SwitchMap$com$kopin$solos$sensors$Sensor$ButtonAction = new int[Sensor.ButtonAction.values().length];

        static {
            try {
                $SwitchMap$com$kopin$solos$sensors$Sensor$ButtonAction[Sensor.ButtonAction.BLUE_THINGY.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$kopin$solos$sensors$Sensor$ButtonAction[Sensor.ButtonAction.LAP.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            $SwitchMap$com$kopin$peloton$groupcom$ChatGroup$SessionState = new int[ChatGroup.SessionState.values().length];
            try {
                $SwitchMap$com$kopin$peloton$groupcom$ChatGroup$SessionState[ChatGroup.SessionState.CONNECTED.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$kopin$peloton$groupcom$ChatGroup$SessionState[ChatGroup.SessionState.CONNECTING.ordinal()] = 2;
            } catch (NoSuchFieldError e4) {
            }
            $SwitchMap$com$kopin$accessory$packets$ButtonType = new int[ButtonType.values().length];
            try {
                $SwitchMap$com$kopin$accessory$packets$ButtonType[ButtonType.MAIN.ordinal()] = 1;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$kopin$accessory$packets$ButtonType[ButtonType.FRONT.ordinal()] = 2;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$kopin$accessory$packets$ButtonType[ButtonType.BACK.ordinal()] = 3;
            } catch (NoSuchFieldError e7) {
            }
        }
    }

    public void changeVolume(boolean downOrUp) {
        int curVol;
        int curVol2 = Prefs.getHeadsetVolume();
        if (downOrUp) {
            curVol = curVol2 == 0 ? 0 : curVol2 - 1;
        } else {
            curVol = curVol2 == 10 ? 10 : curVol2 + 1;
        }
        Prefs.setHeadsetVolume(curVol);
        setVolume(curVol);
    }

    private void setVolume(int level) {
        PupilDevice.DeviceStatus deviceStatus = new PupilDevice.DeviceStatus();
        byte actual = VolumeHelper.friendlyToHeadset((byte) level);
        deviceStatus.setVolume(actual);
        PupilDevice.sendDeviceStatus(deviceStatus);
        PupilDevice.currentDeviceStatus().setVolume(actual);
    }

    private void setBrightness(int val) {
        PupilDevice.DeviceStatus deviceStatus = new PupilDevice.DeviceStatus();
        deviceStatus.setBrightness((byte) val);
        PupilDevice.currentDeviceStatus().setBrightness((byte) val);
        PupilDevice.sendDeviceStatus(deviceStatus);
    }

    public void sendWakeUp() {
        PupilDevice.wakeUp();
        this.mHeadsetSleepTimestamp = System.currentTimeMillis();
    }

    public void sendGotoSleep() {
        PupilDevice.gotoSleep();
        this.mHeadsetSleepTimestamp = -1L;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isHeadsetAwake() {
        return this.mHeadsetSleepTimestamp > -1;
    }

    public String getStringFromTheme(int attr) {
        TypedValue typedValue = new TypedValue();
        if (getApplicationContext().getTheme().resolveAttribute(attr, typedValue, true)) {
            return typedValue.string.toString();
        }
        return null;
    }
}
