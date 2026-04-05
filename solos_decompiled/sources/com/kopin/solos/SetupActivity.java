package com.kopin.solos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import com.digits.sdk.vcard.VCardConfig;
import com.kopin.pupil.PupilDevice;
import com.kopin.pupil.aria.Config;
import com.kopin.solos.Fragments.BaseServiceFragment;
import com.kopin.solos.HardwareReceiverService;
import com.kopin.solos.common.BaseFragment;
import com.kopin.solos.common.DialogUtils;
import com.kopin.solos.common.SportType;
import com.kopin.solos.common.permission.Permission;
import com.kopin.solos.config.Features;
import com.kopin.solos.sensors.SensorsConnector;
import com.kopin.solos.setup.BikeProfileFragment;
import com.kopin.solos.setup.HeadsetConnectFragment;
import com.kopin.solos.setup.HeadsetListFragment;
import com.kopin.solos.setup.LoginShareFragment;
import com.kopin.solos.setup.PhysicalProfileFragment;
import com.kopin.solos.setup.SensorListFragment;
import com.kopin.solos.setup.SetupBeginFragment;
import com.kopin.solos.setup.SetupCompleteFragment;
import com.kopin.solos.setup.SetupFacebookFragment;
import com.kopin.solos.setup.SportSelectionFragment;
import com.kopin.solos.setup.WatchFragment;
import com.kopin.solos.share.Platforms;
import com.kopin.solos.share.Sync;
import com.kopin.solos.storage.Bike;
import com.kopin.solos.storage.FTPHelper;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.settings.ConfigMetrics;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.settings.UserProfile;
import com.kopin.solos.storage.util.Utility;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;

/* JADX INFO: loaded from: classes24.dex */
public class SetupActivity extends ThemeActivity {
    public static final String BACK_STACK_KEY = "back_stack";
    public static final String BT_DEVICE_KEY = "bt_device";
    public static final int FRAGMENT_FACEBOOK = 2;
    public static final int FRAGMENT_HEADSET_CONNECT = 5;
    public static final int FRAGMENT_HEADSET_LIST = 4;
    public static final String FRAGMENT_NAME_KEY = "fragment_name";
    public static final int FRAGMENT_NONE = 0;
    public static final int FRAGMENT_PROFILE_BIKE = 7;
    public static final int FRAGMENT_PROFILE_PHYSICAL = 3;
    public static final int FRAGMENT_SENSOR_LIST = 8;
    public static final int FRAGMENT_SETUP_BEGIN = 1;
    public static final int FRAGMENT_SETUP_COMPLETE = 11;
    public static final int FRAGMENT_SHARE_LOGIN = 9;
    public static final int FRAGMENT_SPORT_SELECTION = 6;
    public static final int FRAGMENT_WATCH = 10;
    public static final String INTENT_KEY_PROFILE = "from_profile";
    public static final String SETUP_INTENT_EXTRA_KEY = "setup_page";
    private static final String TAG = "SetupAct";
    private static final String TWITTER_KEY = "oHueI1XZFhmPqIwuUWuH8QO18";
    private static final String TWITTER_SECRET = "FrgQPFy4TQ1Di98VseyJIKqObR2lcLEhlIdQSqpuI8EvWZAAXK";
    private Bike defaultBike;
    private ImageView mBackground;
    private BaseServiceFragment mCurrentFragment;
    private FragmentManager mFragManager;
    protected HardwareReceiverService mHardwareReceiverService;
    private boolean mInitialSetup;
    private PhysicalProfileFragment mPhysicalProfileFragment;
    private SetupFacebookFragment mSetupFacebookFragment;
    private boolean newUser = false;
    protected boolean mBound = false;
    private int currentFragTag = 0;
    private int[] fragmentsRequiringBluetooth = {5};
    private ServiceConnection mConnection = new ServiceConnection() { // from class: com.kopin.solos.SetupActivity.2
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName className, IBinder service) {
            HardwareReceiverService.HardwareBinder binder = (HardwareReceiverService.HardwareBinder) service;
            SetupActivity.this.mHardwareReceiverService = binder.getService();
            SetupActivity.this.mBound = true;
            if (SetupActivity.this.mCurrentFragment != null && (SetupActivity.this.mCurrentFragment instanceof BaseServiceFragment)) {
                SetupActivity.this.mCurrentFragment.onServiceConnected(SetupActivity.this.mHardwareReceiverService);
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName arg0) {
            SetupActivity.this.mBound = false;
            if (SetupActivity.this.mCurrentFragment != null && (SetupActivity.this.mCurrentFragment instanceof BaseServiceFragment)) {
                SetupActivity.this.mCurrentFragment.onServiceDisconnected();
            }
        }
    };

    @Override // com.kopin.solos.ThemeActivity, com.kopin.solos.common.BaseActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPermissions(R.id.fragmentContainer, new Permission[0]);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        Utility.setUnitSystem(Prefs.getUnitSystem());
        setContentView(R.layout.activity_setup);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);
        boolean alreadySetup = Prefs.getSetupComplete();
        this.mBackground = (ImageView) findViewById(R.id.imgBackground);
        this.currentFragTag = getIntent().getIntExtra(SETUP_INTENT_EXTRA_KEY, 1);
        boolean fromProfile = getIntent().getBooleanExtra(INTENT_KEY_PROFILE, false);
        this.mInitialSetup = this.currentFragTag == 1;
        if (alreadySetup && this.mInitialSetup) {
            startMainActivity();
            return;
        }
        if (fromProfile) {
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setDisplayShowHomeEnabled(true);
            getActionBar().setDisplayUseLogoEnabled(true);
            getActionBar().setIcon(R.drawable.ic_launcher);
        }
        if (alreadySetup) {
            this.newUser = false;
        } else if (Prefs.isCloudLoggedInUser()) {
            this.mInitialSetup = true;
            this.currentFragTag = 4;
            Log.i(TAG, "setup logged in user");
            this.newUser = false;
        } else if (this.mInitialSetup) {
            Log.i(TAG, "setup brand new user");
            this.newUser = true;
            Sync.setProfile(Platforms.Peloton.getSharedKey(), UserProfile.createRider(), System.currentTimeMillis());
        }
        Intent intent = new Intent(this, (Class<?>) HardwareReceiverService.class);
        bindService(intent, this.mConnection, 1);
        this.mFragManager = getFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_NAME_KEY, this.currentFragTag);
        bundle.putBoolean(BACK_STACK_KEY, false);
        showFragment(bundle);
        this.defaultBike = Bike.getDefaultBike(this);
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        if (this.mBound) {
            unbindService(this.mConnection);
            this.mBound = false;
        }
    }

    private boolean fragmentsRequiringBluetooth() {
        for (int fragmentIndex : this.fragmentsRequiringBluetooth) {
            if (fragmentIndex == this.currentFragTag) {
                return true;
            }
        }
        return false;
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
        this.mCurrentFragment = getCurrentFragment();
        setBackground(this.mCurrentFragment.getArguments());
        if (this.mSetupFacebookFragment != null && this.mSetupFacebookFragment == this.mCurrentFragment && FacebookBaseFragment.isLoggedInFacebook()) {
            onBackPressed();
        }
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        if (this.mInitialSetup) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.setup_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) throws Throwable {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.mPhysicalProfileFragment != null && resultCode == -1 && (requestCode == FacebookBaseFragment.REQUEST_IMAGE_CAPTURE || requestCode == FacebookBaseFragment.RESULT_LOAD_IMG)) {
            this.mPhysicalProfileFragment.onActivityResult(requestCode, resultCode, data);
        } else if (this.mSetupFacebookFragment != null) {
            this.mSetupFacebookFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public static void checkBluetoothConnectivity(final Activity activity) {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() { // from class: com.kopin.solos.SetupActivity.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case -1:
                            SetupActivity.startBTSettingsActivity(activity);
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage(R.string.bt_enable).setTitle(R.string.bt_not_available);
            builder.setPositiveButton(R.string.title_activity_settings, dialogClickListener);
            builder.setNegativeButton(android.R.string.cancel, dialogClickListener);
            AlertDialog dialog = builder.create();
            dialog.show();
            DialogUtils.setDialogTitleDivider(dialog);
        }
    }

    public static void startBTSettingsActivity(Activity activity) {
        Intent intentOpenBluetoothSettings = new Intent();
        intentOpenBluetoothSettings.setAction("android.settings.BLUETOOTH_SETTINGS");
        activity.startActivity(intentOpenBluetoothSettings);
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, (Class<?>) MainActivity.class);
        intent.addFlags(VCardConfig.FLAG_APPEND_TYPE_PARAM);
        startActivity(intent);
        finish();
    }

    public void onSkipClick(MenuItem item) {
        int fragmentId = this.mCurrentFragment.getArguments().getInt(FRAGMENT_NAME_KEY, 0);
        Bundle bundle = new Bundle();
        switch (fragmentId) {
            case 2:
                bundle.putInt(FRAGMENT_NAME_KEY, 3);
                showFragment(bundle);
                break;
            case 3:
                bundle.putInt(FRAGMENT_NAME_KEY, 4);
                showFragment(bundle);
                break;
            case 4:
            case 5:
                bundle.putInt(FRAGMENT_NAME_KEY, Features.IS_SPORT_CHANGE_ENABLED ? 6 : 7);
                showFragment(bundle);
                break;
            case 6:
                bundle.putInt(FRAGMENT_NAME_KEY, Prefs.getSportType() != SportType.RIDE ? 8 : 7);
                showFragment(bundle);
                break;
            case 7:
                bundle.putInt(FRAGMENT_NAME_KEY, 8);
                showFragment(bundle);
                break;
            case 8:
                bundle.putInt(FRAGMENT_NAME_KEY, 9);
                showFragment(bundle);
                break;
            case 9:
                bundle.putInt(FRAGMENT_NAME_KEY, 10);
                showFragment(bundle);
                break;
            case 10:
                bundle.putInt(FRAGMENT_NAME_KEY, 11);
                showFragment(bundle);
                break;
            default:
                onSetupComplete();
                startMainActivity();
                break;
        }
    }

    public void skipFacebook() {
        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_NAME_KEY, 3);
        showFragment(bundle);
    }

    public void onStepBtnClick(View view) {
        int currentFragmentId = this.mCurrentFragment.getArguments().getInt(FRAGMENT_NAME_KEY, 0);
        switch (currentFragmentId) {
            case 5:
            case 7:
            case 8:
                if (!this.mInitialSetup) {
                    finish();
                    return;
                }
                break;
            case 11:
                onSetupComplete();
                startMainActivity();
                return;
        }
        Object tag = view.getTag();
        Bundle bundle = new Bundle();
        if (tag instanceof Bundle) {
            bundle = (Bundle) tag;
        }
        bundle.putInt(FRAGMENT_NAME_KEY, incrementFragment(currentFragmentId));
        showFragment(bundle);
    }

    private int incrementFragment(int id) {
        int id2 = id + 1;
        if (id2 == 2 && FacebookBaseFragment.isLoggedInFacebook()) {
            return id2 + 1;
        }
        return id2;
    }

    private void showFragment(Bundle bundle) {
        this.currentFragTag = bundle.getInt(FRAGMENT_NAME_KEY, 0);
        if (fragmentsRequiringBluetooth()) {
            checkBluetoothConnectivity(this);
        }
        switch (this.currentFragTag) {
            case 1:
                showFragment(new SetupBeginFragment(), bundle);
                break;
            case 2:
                if (this.mSetupFacebookFragment == null) {
                    this.mSetupFacebookFragment = new SetupFacebookFragment();
                }
                showFragment(this.mSetupFacebookFragment, bundle);
                break;
            case 3:
                this.mPhysicalProfileFragment = new PhysicalProfileFragment();
                showFragment(this.mPhysicalProfileFragment, bundle);
                break;
            case 4:
                if (this.mInitialSetup && PupilDevice.isConnected()) {
                    if (Features.IS_SPORT_CHANGE_ENABLED && Prefs.getSetupCompleteSet().isEmpty()) {
                        bundle.putInt(FRAGMENT_NAME_KEY, 6);
                        showFragment(bundle);
                    } else if (Features.IS_SPORT_CHANGE_ENABLED && Prefs.getSportType() == SportType.RIDE) {
                        bundle.putInt(FRAGMENT_NAME_KEY, 7);
                        showFragment(bundle);
                    } else {
                        bundle.putInt(FRAGMENT_NAME_KEY, 8);
                        showFragment(bundle);
                    }
                } else {
                    showFragment(new HeadsetListFragment(), bundle);
                }
                break;
            case 5:
                showFragment(new HeadsetConnectFragment(), bundle);
                break;
            case 6:
                if (!Features.IS_SPORT_CHANGE_ENABLED) {
                    bundle.putInt(FRAGMENT_NAME_KEY, 7);
                    showFragment(bundle);
                } else if (!Prefs.getSetupCompleteSet().isEmpty()) {
                    bundle.putInt(FRAGMENT_NAME_KEY, Prefs.getSportType() != SportType.RIDE ? 8 : 7);
                    showFragment(bundle);
                } else {
                    showFragment(new SportSelectionFragment(), bundle);
                }
                break;
            case 7:
                if (!Features.IS_SPORT_CHANGE_ENABLED) {
                    setSport(SportType.RIDE);
                }
                if (Prefs.getSportType() == SportType.RUN || (this.mInitialSetup && !SQLHelper.getBikes().isEmpty())) {
                    bundle.putInt(FRAGMENT_NAME_KEY, 8);
                    showFragment(bundle);
                } else {
                    showFragment(BikeProfileFragment.getInstance(this.defaultBike, this.mInitialSetup), bundle);
                }
                break;
            case 8:
                showFragment(new SensorListFragment(), bundle);
                break;
            case 9:
                showFragment(new LoginShareFragment(), bundle);
                break;
            case 10:
                if (Features.IS_WEARAPP_ENABLED) {
                    showFragment(new WatchFragment(), bundle);
                } else {
                    bundle.putInt(FRAGMENT_NAME_KEY, 11);
                    showFragment(bundle);
                }
                break;
            case 11:
                showFragment(new SetupCompleteFragment(), bundle);
                break;
        }
    }

    private BaseServiceFragment getCurrentFragment() {
        return (BaseServiceFragment) this.mFragManager.findFragmentById(R.id.fragmentContainer);
    }

    private void showFragment(BaseFragment fragment, Bundle bundle) {
        setBackground(bundle);
        fragment.setArguments(bundle);
        this.mCurrentFragment = (BaseServiceFragment) fragment;
        FragmentTransaction fragmentTransaction = this.mFragManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        boolean addToBackStack = bundle.getBoolean(BACK_STACK_KEY, true);
        if (addToBackStack) {
            String backStackTag = fragment.getClass().getSimpleName();
            fragmentTransaction.addToBackStack(backStackTag);
        }
        fragmentTransaction.commit();
        if (this.mBound && (this.mCurrentFragment instanceof BaseServiceFragment)) {
            this.mCurrentFragment.onServiceConnected(this.mHardwareReceiverService);
        }
    }

    private void onSetupComplete() {
        if (Prefs.getSportType() == SportType.RIDE && SQLHelper.getBikes().isEmpty()) {
            Sync.addBike(this.defaultBike);
        }
        Config.setProvisioned(this, true);
        Prefs.setSetupComplete(true);
        Sync.uploadAll(true);
    }

    public void setDefaultBike(Bike bike) {
        this.defaultBike = bike;
    }

    public void onRunClick(View view) {
        setSport(SportType.RUN);
        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_NAME_KEY, 8);
        showFragment(bundle);
    }

    public void onRideClick(View view) {
        setSport(SportType.RIDE);
        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_NAME_KEY, 7);
        showFragment(bundle);
    }

    private void setSport(SportType sportType) {
        LiveRide.setSport(sportType);
        SensorsConnector.setForSport(sportType);
        FTPHelper.init();
        ConfigMetrics.init();
        changeSportTheme(sportType);
    }

    private void changeSportTheme(SportType sportType) {
        super.setTheme(ThemeUtil.getTheme(sportType));
    }

    private void setBackground(Bundle fragmentBundle) {
        this.mBackground.setImageResource((Prefs.getSetupComplete() || fragmentBundle.getInt(FRAGMENT_NAME_KEY, 0) > 5) ? getResourceIdFromTheme(R.attr.drawableAppBg) : R.drawable.solos_back);
    }

    public static void showSetupDialog(final Activity activity, final SportType sportType) {
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() { // from class: com.kopin.solos.SetupActivity.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case -1:
                        if (!activity.isFinishing()) {
                            LiveRide.resetMode();
                            LiveRide.setSport(sportType);
                            SensorsConnector.setForSport(sportType);
                            activity.getApplication().setTheme(ThemeUtil.getTheme(sportType));
                            ConfigMetrics.init();
                            FTPHelper.init();
                            activity.startActivity(new Intent(activity, (Class<?>) StartActivity.class).putExtra(StartActivity.KEY_CLOUD_INIT, false).setFlags(268468224));
                            activity.finish();
                        }
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.setup_dialog_title).setMessage(ThemeUtil.getResourceId(activity, R.attr.strSetupWorkoutMsg)).setPositiveButton(R.string.setup, listener).setNegativeButton(R.string.dialog_btn_cancel, listener);
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        DialogUtils.setDialogTitleDivider(dialog);
    }
}
