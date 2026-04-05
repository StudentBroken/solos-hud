package com.kopin.solos;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.digits.sdk.vcard.VCardConfig;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.kopin.pupil.PupilDevice;
import com.kopin.pupil.aria.contacts.ContactsCache;
import com.kopin.pupil.aria.debug.WaveFileRecorder;
import com.kopin.pupil.diags.Diagnostics;
import com.kopin.solos.Fragments.BikeFragment;
import com.kopin.solos.Fragments.PeakHRSettingsFragment;
import com.kopin.solos.Fragments.ProfileEditFragment;
import com.kopin.solos.Fragments.ProfileFragment;
import com.kopin.solos.Fragments.RideHistoryStravaFragment;
import com.kopin.solos.Fragments.StartStopFragment;
import com.kopin.solos.Fragments.StatsFragment;
import com.kopin.solos.Fragments.WatchModeFragment;
import com.kopin.solos.Fragments.WorkoutHistoryFragment;
import com.kopin.solos.HardwareReceiverService;
import com.kopin.solos.analytics.Analytics;
import com.kopin.solos.common.SportType;
import com.kopin.solos.common.permission.Permission;
import com.kopin.solos.common.permission.PermissionUtil;
import com.kopin.solos.config.ConfigActivity;
import com.kopin.solos.config.Features;
import com.kopin.solos.debug.DebugFragment;
import com.kopin.solos.functionalthresholdpower.FTPSettingsFragment;
import com.kopin.solos.menu.CustomActionProvider;
import com.kopin.solos.menu.CustomMenuItem;
import com.kopin.solos.menu.SensorMenuAdapter;
import com.kopin.solos.navigation.DirectionFragment;
import com.kopin.solos.navigation.RouteManagementFragment;
import com.kopin.solos.sensors.SensorsConnector;
import com.kopin.solos.settings.QuickSettingsActivity;
import com.kopin.solos.settings.SettingsFragment;
import com.kopin.solos.setup.BikeProfileFragment;
import com.kopin.solos.share.Config;
import com.kopin.solos.share.strava.StravaActivity;
import com.kopin.solos.share.strava.StravaHelper;
import com.kopin.solos.share.trainingpeaks.TPActivity;
import com.kopin.solos.storage.FTPHelper;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.Workout;
import com.kopin.solos.storage.settings.ConfigMetrics;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.wear.WatchModeActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes24.dex */
public class MainActivity extends SolosBaseFragmentActivity implements StartStopFragment.ServiceActivity {
    public static final String INTENT_EXTRA_WORKOUT_MODE_KEY = "workout_mode";
    private static final String KEY_ACTIVITY_FRAGMENT = "activity";
    public static final String KEY_FTP_FRAGMENT = "ftp";
    public static final String KEY_HRZ_FRAGMENT = "peakhr";
    public static final String KEY_START_STOP_FRAGMENT = "startStop";
    public static boolean SHOW_DEBUG_MENUS = false;
    private static final Permission[] START_PERMISSIONS = {Permission.ACCESS_FINE_LOCATION, Permission.READ_CONTACTS};
    private static final String TAG = "MainAct";
    ArrayAdapter<String> adapter;
    private View mButtonContainer;
    private FirebaseAnalytics mFirebaseAnalytics;
    private long mLastClickTime;
    private SensorMenuAdapter.SensorMenuItem mMenuItem;
    private ProfileEditFragment mProfileEditFragment;
    private ProfileFragment mProfileFragment;
    private CustomActionProvider.DefaultActionView mSensorIcon;
    private HardwareReceiverService mService;
    private StartStopFragment.ServiceCallback mServiceCallback;
    private StartStopFragment mStartStopFragment;
    private boolean mBound = false;
    private boolean inStravaList = false;
    private boolean loggingOut = false;
    ArrayList<String> pages = new ArrayList<>();
    private final ActionBar.OnNavigationListener mNavListener = new ActionBar.OnNavigationListener() { // from class: com.kopin.solos.MainActivity.1
        @Override // android.app.ActionBar.OnNavigationListener
        public boolean onNavigationItemSelected(int itemPosition, long itemId) {
            if (MainActivity.this.isFinishing()) {
                return false;
            }
            MainActivity.this.prepNavChange();
            boolean watchMode = Prefs.isWatchMode();
            switch (itemPosition) {
                case 0:
                    if (!watchMode) {
                        MainActivity.this.rideState(true);
                        return true;
                    }
                    MainActivity.this.showWatchModeFragment();
                    return true;
                case 1:
                    MainActivity.this.showActivitiesFragment();
                    return true;
                case 2:
                    MainActivity.this.mProfileFragment = new ProfileFragment();
                    Bundle args = new Bundle();
                    if (MainActivity.this.mService != null) {
                        args.putBoolean("rideActive", MainActivity.this.mService.isActiveRide());
                    }
                    MainActivity.this.mProfileFragment.setArguments(args);
                    MainActivity.this.fragmentReplace(R.id.main_content, MainActivity.this.mProfileFragment);
                    if (MainActivity.this.getIntent() == null || !MainActivity.this.getIntent().getBooleanExtra("ftp", false)) {
                        return true;
                    }
                    MainActivity.this.getIntent().removeExtra("ftp");
                    MainActivity.this.fragmentReplaceBackStack(R.id.main_content, FTPSettingsFragment.newInstance(), "ftp");
                    return true;
                case 3:
                    if (!watchMode) {
                        MainActivity.this.fragmentReplace(R.id.main_content, new SettingsFragment());
                        return true;
                    }
                    break;
            }
            String s = MainActivity.this.pages.get(itemPosition);
            if (s.equals("DEBUG")) {
                MainActivity.this.fragmentReplace(R.id.main_content, new DebugFragment());
                return true;
            }
            if (!s.equals("CONFIG")) {
                return false;
            }
            Intent intent = new Intent(MainActivity.this, (Class<?>) ConfigActivity.class);
            intent.setFlags(VCardConfig.FLAG_APPEND_TYPE_PARAM);
            MainActivity.this.startActivity(intent);
            return true;
        }
    };
    private HardwareReceiverService.InfoListener mInfoListener = new HardwareReceiverService.InfoListener() { // from class: com.kopin.solos.MainActivity.5
        @Override // com.kopin.solos.HardwareReceiverService.InfoListener
        public void onListChanged() {
            MainActivity.this.runOnUiThread(new Runnable() { // from class: com.kopin.solos.MainActivity.5.1
                @Override // java.lang.Runnable
                public void run() {
                    MainActivity.this.mSensorIcon.setActive(ActiveSensors.checkForActiveSensors());
                    Fragment fragment = MainActivity.this.getFragmentManager().findFragmentByTag(MainActivity.KEY_START_STOP_FRAGMENT);
                    if (fragment != null && fragment.isVisible()) {
                        MainActivity.this.invalidateOptionsMenu();
                    }
                }
            });
        }

        @Override // com.kopin.solos.HardwareReceiverService.InfoListener
        public void onNotification(HardwareReceiverService.NotificationType type) {
        }
    };
    private ServiceConnection mConnection = new ServiceConnection() { // from class: com.kopin.solos.MainActivity.6
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName className, IBinder service) {
            HardwareReceiverService.HardwareBinder binder = (HardwareReceiverService.HardwareBinder) service;
            MainActivity.this.mService = binder.getService();
            MainActivity.this.mBound = true;
            if (MainActivity.this.mServiceCallback != null) {
                MainActivity.this.mServiceCallback.onService(MainActivity.this.mService);
                MainActivity.this.mServiceCallback = null;
            }
            MainActivity.this.mService.addCallback(MainActivity.this.mInfoListener);
            ActiveSensors.onServiceConnected(MainActivity.this.mService);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName arg0) {
            MainActivity.this.mBound = false;
            MainActivity.this.mService.removeCallback(MainActivity.this.mInfoListener);
            ActiveSensors.onServiceDisconnected(MainActivity.this.mService);
        }
    };

    @Override // com.kopin.solos.SolosBaseFragmentActivity, com.kopin.solos.common.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        int ordinal;
        super.onCreate(savedInstanceState);
        List<Permission> startPermissions = new ArrayList<>(Arrays.asList(START_PERMISSIONS));
        if (!Prefs.isGPSEnabled()) {
            startPermissions.remove(Permission.ACCESS_FINE_LOCATION);
        }
        initPermissions(R.id.main_content, startPermissions);
        if (Features.IS_SPORT_CHANGE_ENABLED) {
            Intent intent = getIntent();
            SportType sportType = Prefs.getSportType();
            boolean resetWorkout = true;
            if (intent != null && savedInstanceState == null && (ordinal = intent.getIntExtra("workout_mode", sportType.ordinal())) != sportType.ordinal()) {
                sportType = SportType.values()[ordinal];
                resetWorkout = false;
            }
            refreshForSport(sportType, resetWorkout);
        } else {
            refreshForSport(SportType.RIDE, true);
        }
        setContentView(R.layout.activity_main);
        this.mButtonContainer = findViewById(R.id.main_buttons);
        SensorWarnings.init(this);
        ActiveSensors.init(this);
        this.mSensorIcon = new CustomActionProvider.DefaultActionView(this);
        this.mSensorIcon.setActiveColor(getResources().getColor(R.color.app_actionbar_divider));
        this.mSensorIcon.setInactiveColor(getResources().getColor(R.color.unfocused_grey));
        this.mSensorIcon.setActive(ActiveSensors.checkForActiveSensors());
        Prefs.initSingleMetrics();
        Prefs.initMultiMetrics();
        bindService(new Intent(this, (Class<?>) HardwareReceiverService.class), this.mConnection, 1);
        Intent received = getIntent();
        boolean showFTP = false;
        if (received != null) {
            showFTP = received.getBooleanExtra("ftp", false);
        }
        initActionBar(getActionBar());
        if (LiveRide.isActiveFtp() || showFTP) {
            getActionBar().setSelectedNavigationItem(2);
        }
        StravaHelper.refreshAuthResponse(this, null);
        this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    @Override // com.kopin.solos.common.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        if (this.mBound) {
            unbindService(this.mConnection);
            this.mBound = false;
        }
    }

    private void refreshForSport(SportType sport, boolean resetMode) {
        SportType currentSport = LiveRide.getCurrentSport();
        setSport(sport);
        if (currentSport != sport && resetMode) {
            RideControl.setWorkoutMode(Workout.RideMode.NORMAL, -1L);
            StartStopFragment fragment = (StartStopFragment) getFragmentManager().findFragmentByTag(KEY_START_STOP_FRAGMENT);
            if (fragment != null) {
                getFragmentManager().beginTransaction().remove(fragment).commit();
            }
            recreate();
        }
    }

    private void setSport(SportType sport) {
        LiveRide.setSport(sport);
        SensorsConnector.setForSport(sport);
        FTPHelper.init();
        setTheme(ThemeUtil.getTheme(sport));
        getApplication().setTheme(ThemeUtil.getTheme(sport));
    }

    public void changeSport(SportType sportType) {
        Bundle params = new Bundle();
        params.putString(Analytics.Param.WORKOUT_TYPE, sportType.name());
        this.mFirebaseAnalytics.logEvent(Analytics.Events.WORKOUT_CHANGE, params);
        if (!Prefs.getSetupComplete(sportType)) {
            SetupActivity.showSetupDialog(this, sportType);
            return;
        }
        LiveRide.resetMode();
        refreshForSport(sportType, true);
        ConfigMetrics.init();
    }

    public void showSpinner() {
        if (getActionBar() != null) {
            getActionBar().setNavigationMode(1);
        }
    }

    private void initActionBar(ActionBar actionBar) {
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            this.pages.clear();
            this.pages.add(getStringFromTheme(R.attr.strWorkout).toString());
            for (String p : getResources().getStringArray(Prefs.isWatchMode() ? R.array.main_activity_fragments_watch : R.array.main_activity_fragments)) {
                this.pages.add(p);
            }
            if (Config.DEBUG) {
                this.pages.add("DEBUG");
            }
            if (Config.DEBUG_ALLOW_CONFIG_OVERRIDE) {
                this.pages.add("CONFIG");
            }
            this.adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, this.pages);
            actionBar.setNavigationMode(1);
            actionBar.setListNavigationCallbacks(this.adapter, this.mNavListener);
        }
    }

    public void resetActionBar() {
        if (getActionBar() != null) {
            getActionBar().setHomeButtonEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(false);
            showSpinner();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void prepNavChange() {
        if (!isFinishing()) {
            getActionBar().setDisplayHomeAsUpEnabled(false);
            getActionBar().setHomeButtonEnabled(false);
            clearBackStack();
            this.mButtonContainer.setVisibility(8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void rideState(boolean fromNavChange) {
        if (!isFinishing()) {
            if (!fromNavChange && getActionBar().getNavigationMode() == 1) {
                getActionBar().setSelectedNavigationItem(0);
                return;
            }
            prepNavChange();
            this.mStartStopFragment = StartStopFragment.newInstance(false);
            fragmentReplace(R.id.main_content, this.mStartStopFragment, KEY_START_STOP_FRAGMENT);
            System.gc();
        }
    }

    @Override // com.kopin.solos.common.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
    }

    public void setActionBarFragmentBackEnabled(boolean enabled) {
        getActionBar().setDisplayHomeAsUpEnabled(enabled);
        getActionBar().setHomeButtonEnabled(enabled);
        if (enabled) {
            getActionBar().setDisplayShowHomeEnabled(enabled);
            getActionBar().setDisplayUseLogoEnabled(enabled);
            getActionBar().setIcon(R.drawable.ic_launcher);
        }
    }

    public void showWatchModeFragment() {
        if (!isFinishing()) {
            getActionBar().setDisplayShowTitleEnabled(false);
            getActionBar().setDisplayShowHomeEnabled(false);
            getActionBar().setDisplayUseLogoEnabled(false);
            getActionBar().setDisplayHomeAsUpEnabled(false);
            fragmentReplace(R.id.main_content, new WatchModeFragment());
        }
    }

    public void onSwitchToPhoneClick(View view) {
        Intent intent = new Intent(this, (Class<?>) WatchModeActivity.class);
        intent.putExtra(WatchModeActivity.MODE_PHONE, true);
        startActivity(intent);
        finish();
    }

    public void showActivitiesFragment() {
        if (!isFinishing()) {
            clearBackStack();
            WorkoutHistoryFragment fragment = WorkoutHistoryFragment.createFragmentForCurrentSport();
            Bundle args = new Bundle();
            args.putBoolean("rideActive", LiveRide.isActiveRide());
            fragment.setArguments(args);
            fragmentReplace(R.id.main_content, fragment, "activity");
            invalidateOptionsMenu();
        }
    }

    private void showActivitiesFragmentForSelection(boolean forNavigation) {
        getActionBar().setNavigationMode(0);
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setTitle(getStringFromTheme(R.attr.strSelectWorkout));
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setDisplayUseLogoEnabled(true);
        getActionBar().setIcon(R.drawable.ic_launcher);
        WorkoutHistoryFragment fragment = WorkoutHistoryFragment.createFragmentForCurrentSport();
        Bundle args = new Bundle();
        if (forNavigation) {
            args.putBoolean(ThemeActivity.EXTRA_ROUTE_PREVIEW, true);
        } else {
            args.putBoolean(WorkoutHistoryFragment.EXTRA_GHOST_RIDE_SELECTION, true);
        }
        fragment.setArguments(args);
        fragmentReplaceBackStack(R.id.main_content, fragment);
    }

    public void selectNavigateFromRide() {
        showActivitiesFragmentForSelection(true);
    }

    public void selectGhostRide() {
        showActivitiesFragmentForSelection(false);
    }

    public void popFromAddressSelector() {
        clearBackStack();
    }

    public Location getLocation() {
        return this.mService.getLocation();
    }

    public void showStatsFragment() {
        if (!isFinishing()) {
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setDisplayHomeAsUpEnabled(true);
            fragmentReplaceBackStack(R.id.main_content, new StatsFragment());
        }
    }

    public void showDirectionFragment() {
        if (!isFinishing()) {
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setDisplayShowHomeEnabled(true);
            getActionBar().setDisplayUseLogoEnabled(true);
            getActionBar().setIcon(R.drawable.ic_launcher);
            fragmentReplaceBackStack(R.id.main_content, new DirectionFragment());
        }
    }

    public void showRouteNavigationFragment() {
        if (!isFinishing()) {
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setDisplayShowHomeEnabled(true);
            getActionBar().setDisplayUseLogoEnabled(true);
            getActionBar().setIcon(R.drawable.ic_launcher);
            fragmentReplaceBackStack(R.id.main_content, new RouteManagementFragment());
        }
    }

    public void showStravaFragment() {
        if (!isFinishing()) {
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setDisplayShowHomeEnabled(true);
            getActionBar().setDisplayUseLogoEnabled(true);
            getActionBar().setIcon(R.drawable.ic_launcher);
            fragmentReplaceBackStack(R.id.main_content, new RideHistoryStravaFragment());
        }
    }

    public void showTrainingWorkouts() {
        if (!isFinishing()) {
            startActivity(new Intent(this, (Class<?>) TrainingListActivity.class));
        }
    }

    public void showBikesFragment() {
        if (!isFinishing()) {
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setDisplayShowHomeEnabled(true);
            getActionBar().setDisplayUseLogoEnabled(true);
            getActionBar().setIcon(R.drawable.ic_launcher);
            fragmentReplaceBackStack(R.id.main_content, new BikeFragment());
        }
    }

    public void showAddBikeFragment() {
        if (!isFinishing()) {
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setDisplayShowHomeEnabled(true);
            getActionBar().setDisplayUseLogoEnabled(true);
            getActionBar().setIcon(R.drawable.ic_launcher);
            fragmentReplaceBackStack(R.id.main_content, new BikeProfileFragment());
        }
    }

    public void showEditProfileFragment() {
        if (!isFinishing()) {
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setDisplayShowHomeEnabled(true);
            getActionBar().setDisplayUseLogoEnabled(true);
            getActionBar().setIcon(R.drawable.ic_launcher);
            this.mProfileEditFragment = new ProfileEditFragment();
            fragmentReplaceBackStack(R.id.main_content, this.mProfileEditFragment);
        }
    }

    public void showQuickSettings(View view) {
        startActivity(new Intent(this, (Class<?>) QuickSettingsActivity.class));
    }

    public void showFunctionalThresholdPowerSettings(boolean backNav) {
        if (backNav) {
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setDisplayShowHomeEnabled(true);
            getActionBar().setDisplayUseLogoEnabled(true);
            getActionBar().setIcon(R.drawable.ic_launcher);
        }
        fragmentReplaceBackStack(R.id.main_content, FTPSettingsFragment.newInstance(), "ftp");
    }

    public void showFunctionalThresholdPower() {
        if (!isFinishing()) {
            getActionBar().setNavigationMode(0);
            getActionBar().setTitle(R.string.functional_threshold_power);
            getActionBar().setDisplayShowTitleEnabled(true);
            getActionBar().setDisplayShowHomeEnabled(false);
            getActionBar().setDisplayUseLogoEnabled(false);
            getActionBar().setDisplayHomeAsUpEnabled(false);
            this.mButtonContainer.setVisibility(8);
            this.mStartStopFragment = (StartStopFragment) findFragment(com.kopin.solos.storage.FTP.NAME);
            if (this.mStartStopFragment != null) {
                popToFragment(com.kopin.solos.storage.FTP.NAME);
            } else {
                this.mStartStopFragment = StartStopFragment.newInstance(true);
                fragmentAddBackStack(R.id.main_content, this.mStartStopFragment, com.kopin.solos.storage.FTP.NAME);
            }
        }
    }

    public void showPeakHeartRateSettings(boolean backNav) {
        if (backNav) {
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setDisplayShowHomeEnabled(true);
            getActionBar().setDisplayUseLogoEnabled(true);
            getActionBar().setIcon(R.drawable.ic_launcher);
        }
        fragmentReplaceBackStack(R.id.main_content, PeakHRSettingsFragment.newInstance(), KEY_HRZ_FRAGMENT);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean dispatchTouchEvent(MotionEvent ev) {
        ActionBar actionBar = getActionBar();
        if (actionBar != null && actionBar.getSelectedNavigationIndex() == 3 && ev.getAction() == 0) {
            long elapsedTime = SystemClock.elapsedRealtime();
            if (elapsedTime - this.mLastClickTime < 500) {
                return false;
            }
            this.mLastClickTime = elapsedTime;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean z = false;
        CustomActionProvider<SensorMenuAdapter.SensorMenuItem> actionProvider = ActiveSensors.getActionProvider();
        actionProvider.setActionView(this.mSensorIcon);
        actionProvider.setOnPrepareListener(new CustomActionProvider.OnPrepareListener<SensorMenuAdapter.SensorMenuItem>() { // from class: com.kopin.solos.MainActivity.2
            @Override // com.kopin.solos.menu.CustomActionProvider.OnPrepareListener
            public void onPrepare(CustomActionProvider<SensorMenuAdapter.SensorMenuItem> actionProvider2) {
                ActiveSensors.onPrepareActionProvider(actionProvider2);
                actionProvider2.addMenuItem(MainActivity.this.mMenuItem);
            }
        });
        actionProvider.setOnItemClickListener(new CustomActionProvider.OnItemClickListener() { // from class: com.kopin.solos.MainActivity.3
            @Override // com.kopin.solos.menu.CustomActionProvider.OnItemClickListener
            public void onItemClick(int position, CustomMenuItem menuItem) {
                ActiveSensors.onMenuItemSelected(menuItem);
            }
        });
        this.mMenuItem = ActiveSensors.createMenuItem(getString(R.string.option_view_sensors));
        this.mMenuItem.setType(SensorMenuAdapter.SensorMenuItemType.OPTION);
        this.mMenuItem.setDismissOnTap(true);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.action_log).setVisible(Config.DEBUG && SHOW_DEBUG_MENUS);
        menu.findItem(R.id.action_ant).setVisible(false);
        menu.findItem(R.id.action_strava).setVisible(Config.DEBUG && SHOW_DEBUG_MENUS);
        MenuItem menuItemFindItem = menu.findItem(R.id.action_tp);
        if (Config.DEBUG && SHOW_DEBUG_MENUS) {
            z = true;
        }
        menuItemFindItem.setVisible(z);
        menu.findItem(R.id.menu_show_sensors).setActionProvider(actionProvider);
        if (SHOW_DEBUG_MENUS) {
            inflater.inflate(R.menu.menu_debug, menu);
        }
        return true;
    }

    @Override // android.app.Activity
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (Config.DEBUG && SHOW_DEBUG_MENUS) {
            menu.findItem(R.id.action_low_power).setTitle(PupilDevice.isLowColorMode() ? "Full colour mode" : "8 colour mode").setVisible(Config.DEBUG);
            menu.findItem(R.id.action_battery_log).setTitle(Diagnostics.BatteryLog.isActive() ? "Stop battery log" : "Start battery log").setVisible(Config.DEBUG);
            menu.findItem(R.id.action_voice_rec).setTitle(WaveFileRecorder.isActive() ? R.string.menu_command_voice_stop : R.string.menu_command_voice_record).setVisible(Config.DEBUG);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override // com.kopin.solos.common.BaseFragmentActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        if (super.onOptionsItemSelected(item) || this.loggingOut) {
            return true;
        }
        if (item.getItemId() != R.id.action_import_strava && item.getItemId() != 16908332) {
            this.inStravaList = false;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                new Handler().post(new Runnable() { // from class: com.kopin.solos.MainActivity.4
                    @Override // java.lang.Runnable
                    public void run() {
                        if (!MainActivity.this.isFinishing()) {
                            MainActivity.this.getFragmentManager().popBackStackImmediate();
                            MainActivity.this.getActionBar().setDisplayHomeAsUpEnabled(false);
                            if (MainActivity.this.inStravaList) {
                                MainActivity.this.inStravaList = false;
                                MainActivity.this.showActivitiesFragment();
                                MainActivity.this.setActionBarFragmentBackEnabled(false);
                                MainActivity.this.showSpinner();
                                MainActivity.this.getActionBar().setDisplayShowTitleEnabled(false);
                                MainActivity.this.invalidateOptionsMenu();
                            }
                        }
                    }
                });
                break;
            case R.id.action_training /* 2131952534 */:
                showTrainingWorkouts();
                break;
            case R.id.action_import_strava /* 2131952535 */:
                this.inStravaList = true;
                showStravaFragment();
                break;
            case R.id.action_manage_routes /* 2131952536 */:
                this.inStravaList = true;
                showRouteNavigationFragment();
                break;
            case R.id.action_strava /* 2131952542 */:
                Intent intent = new Intent(this, (Class<?>) StravaActivity.class);
                startActivity(intent);
                break;
            case R.id.action_tp /* 2131952543 */:
                Intent intent2 = new Intent(this, (Class<?>) TPActivity.class);
                startActivity(intent2);
                break;
            case R.id.action_low_power /* 2131952544 */:
                PupilDevice.setLowColorMode(PupilDevice.isLowColorMode() ? false : true);
                break;
            case R.id.action_battery_log /* 2131952545 */:
                if (Diagnostics.BatteryLog.isActive()) {
                    String log = Diagnostics.BatteryLog.stop();
                    Toast.makeText(getApplication(), "Battery log saved to " + log, 1).show();
                } else {
                    Diagnostics.BatteryLog.start(getApplicationContext());
                    Toast.makeText(getApplication(), "Battery log started", 0).show();
                }
                break;
            case R.id.action_voice_rec /* 2131952546 */:
                if (WaveFileRecorder.isActive()) {
                    String log2 = WaveFileRecorder.stop();
                    Toast.makeText(getApplication(), "Audio recording saved to " + log2, 1).show();
                } else {
                    WaveFileRecorder.record(false, this);
                    Toast.makeText(getApplication(), "Audio recording started", 0).show();
                }
                break;
        }
        return true;
    }

    @Override // com.kopin.solos.common.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        if (!isFinishing() && !this.loggingOut) {
            if (LiveRide.isActiveFtp() && this.mStartStopFragment != null) {
                this.mStartStopFragment.onBackPressed();
            } else {
                performBack();
            }
        }
    }

    private void performBack() {
        if (!isFinishing() && !getFragmentManager().popBackStackImmediate()) {
            if (getActionBar().getNavigationMode() == 1 && getActionBar().getSelectedNavigationIndex() != 0) {
                getActionBar().setSelectedNavigationItem(0);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override // com.kopin.solos.Fragments.StartStopFragment.ServiceActivity
    public void getService(StartStopFragment.ServiceCallback serviceCallback) {
        if (serviceCallback == null) {
            throw new NullPointerException("null ServiceCallback");
        }
        if (this.mBound) {
            serviceCallback.onService(this.mService);
        } else {
            this.mServiceCallback = serviceCallback;
        }
    }

    public void onStartRideClick(View view) {
        rideState(false);
    }

    @Override // com.kopin.solos.common.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.mProfileEditFragment != null && resultCode == -1 && (requestCode == FacebookBaseFragment.REQUEST_IMAGE_CAPTURE || requestCode == FacebookBaseFragment.RESULT_LOAD_IMG)) {
            this.mProfileEditFragment.onActivityResult(requestCode, resultCode, data);
        } else if (this.mProfileFragment != null) {
            this.mProfileFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void unhideActionBar() {
        getActionBar().show();
    }

    public void setLogoutMode(boolean logout) {
        this.loggingOut = logout;
        if (logout) {
            getActionBar().setNavigationMode(0);
        }
    }

    @Override // com.kopin.solos.common.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.app.Activity, android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (PermissionUtil.granted(Permission.READ_CONTACTS, permissions, grantResults)) {
            Log.i(TAG, "READ_CONTACTS permission granted, refresh contacts cache");
            ContactsCache.reload();
        }
        PermissionUtil.GrantResult result = PermissionUtil.grantedState(Permission.ACCESS_FINE_LOCATION, permissions, grantResults);
        switch (result) {
            case GRANTED:
                if (this.mService != null) {
                    Log.i(TAG, "gps permission granted prepare location");
                    this.mService.prepareLocation();
                }
                break;
            case DENIED:
                Log.i(TAG, "gps permission denied");
                Prefs.setGPSEnabled(false);
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
