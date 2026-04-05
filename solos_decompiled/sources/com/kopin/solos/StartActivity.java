package com.kopin.solos;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.IntentCompat;
import android.util.Log;
import android.widget.TextView;
import com.digits.sdk.vcard.VCardConfig;
import com.facebook.FacebookSdk;
import com.facebook.internal.ServerProtocol;
import com.kopin.peloton.Peloton;
import com.kopin.peloton.PelotonPrefs;
import com.kopin.peloton.PelotonResponse;
import com.kopin.solos.common.BaseActivity;
import com.kopin.solos.common.Device;
import com.kopin.solos.config.ConfigActivity;
import com.kopin.solos.share.Config;
import com.kopin.solos.share.Platforms;
import com.kopin.solos.share.ShareHelper;
import com.kopin.solos.share.Sync;
import com.kopin.solos.share.peloton.PelotonActivity;
import com.kopin.solos.share.peloton.PelotonReauthenticate;
import com.kopin.solos.storage.FTPHelper;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.RecoveryUtil;
import java.io.File;
import java.util.Calendar;
import java.util.Date;

/* JADX INFO: loaded from: classes24.dex */
public class StartActivity extends BaseActivity {
    private Intent mLaunchIntent;
    public static String KEY_CLOUD_INIT = "cloudInit";
    private static String TAG = "StartAct";
    private static String CONFIG_FILE = "solos_config.txt";
    private static long SPLASH_SHOW_TIME = 1000;
    private boolean mBound = false;
    private final Handler mHandler = new Handler();
    private boolean mWaitOrFinish = false;
    private final ServiceConnection mConnection = new ServiceConnection() { // from class: com.kopin.solos.StartActivity.2
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName className, IBinder service) {
            StartActivity.this.mBound = true;
            StartActivity.this.mHandler.postDelayed(new Runnable() { // from class: com.kopin.solos.StartActivity.2.1
                @Override // java.lang.Runnable
                public void run() {
                    if (StartActivity.this.mWaitOrFinish) {
                        StartActivity.this.startActivityForResult(StartActivity.this.mLaunchIntent, 77);
                    } else {
                        StartActivity.this.startActivity(StartActivity.this.mLaunchIntent);
                        StartActivity.this.finish();
                    }
                }
            }, StartActivity.SPLASH_SHOW_TIME);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName arg0) {
            StartActivity.this.mBound = false;
        }
    };

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Date buildDate = com.kopin.solos.core.BuildConfig.BUILD_DATE != null ? com.kopin.solos.core.BuildConfig.BUILD_DATE : new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(buildDate);
        String line = getString(R.string.text_copyright_splash, new Object[]{String.valueOf(cal.get(1))});
        ((TextView) findViewById(R.id.txtCopyright)).setText(line);
        boolean allowConfig = getIntent().getBooleanExtra("config", true);
        if (Config.DEBUG_ALLOW_CONFIG_OVERRIDE && allowConfig) {
            try {
                File file = new File(Environment.getExternalStorageDirectory(), CONFIG_FILE);
                if (file.exists()) {
                    Intent intent = new Intent(this, (Class<?>) ConfigActivity.class);
                    intent.setFlags(VCardConfig.FLAG_APPEND_TYPE_PARAM);
                    intent.putExtra("startup", true);
                    startActivity(intent);
                    finish();
                    return;
                }
            } catch (Exception e) {
            }
        }
        boolean cloudInit = getIntent().getBooleanExtra(KEY_CLOUD_INIT, true);
        if (cloudInit) {
            Log.d(TAG, "onCreate sync init");
            Sync.init(getApplicationContext(), Device.DeviceType.MOBILE, new Peloton.AuthenticationListener() { // from class: com.kopin.solos.StartActivity.1
                @Override // com.kopin.peloton.Peloton.AuthenticationListener
                public void onFailed(PelotonResponse pelotonResponse) {
                    Intent intent2 = new Intent(StartActivity.this, (Class<?>) StartActivity.class);
                    intent2.putExtra(Sync.KEY_REAUTHENTICATE, true);
                    Intent mainIntent = IntentCompat.makeRestartActivityTask(intent2.getComponent());
                    mainIntent.addFlags(1946157056);
                    mainIntent.putExtra(Sync.KEY_REAUTHENTICATE, true);
                    StartActivity.this.getApplicationContext().startActivity(mainIntent);
                }
            });
        }
        FacebookSdk.sdkInitialize(getApplicationContext());
        String version = ServerProtocol.getAPIVersion();
        Log.i("FB version", version);
        if (Config.SYNC_PROVIDER.isLoggedIn(this)) {
            SQLHelper.setCurrentUsername(PelotonPrefs.getEmail());
            if (!RecoveryUtil.hasInCompleteRide()) {
                Sync.sync(cloudInit);
            }
            Prefs.setCloudLoggedInUser();
            this.mLaunchIntent = new Intent(this, (Class<?>) SetupActivity.class);
            this.mLaunchIntent.putExtra(SetupActivity.SETUP_INTENT_EXTRA_KEY, 1);
            this.mWaitOrFinish = false;
            return;
        }
        if (Config.SYNC_PROVIDER == Platforms.Peloton) {
            boolean reAuthenticate = getIntent().getBooleanExtra(Sync.KEY_REAUTHENTICATE, false);
            this.mLaunchIntent = new Intent(this, (Class<?>) (reAuthenticate ? PelotonReauthenticate.class : PelotonActivity.class));
            this.mLaunchIntent.setFlags(VCardConfig.FLAG_APPEND_TYPE_PARAM);
            this.mWaitOrFinish = true;
            Sync.cancelTasks();
        }
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, (Class<?>) HardwareReceiverService.class);
        bindService(intent, this.mConnection, 1);
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    protected void onStop() {
        if (this.mBound) {
            unbindService(this.mConnection);
            this.mBound = false;
        }
        super.onStop();
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 77) {
            Log.i(TAG, "user registered/logged in " + PelotonPrefs.getEmail());
            ShareHelper.logoutAll(getApplicationContext(), Platforms.Peloton);
            FTPHelper.init();
            Sync.setUsername(PelotonPrefs.getEmail());
            Sync.setEnabled(true);
            SQLHelper.setCurrentUsername(PelotonPrefs.getEmail());
            boolean newUser = data == null;
            if (!newUser) {
                Log.i(TAG, "sync data for logged in user");
                Sync.sync(true);
                Prefs.setCloudLoggedInUser();
            } else {
                com.kopin.pupil.aria.Config.setProvisioned(this, false);
                Prefs.setSetupComplete(false);
                Prefs.setCloudLoggedInUser(false);
            }
            Prefs.initUnitSystem();
            Intent intent = new Intent(this, (Class<?>) SetupActivity.class);
            intent.setFlags(VCardConfig.FLAG_APPEND_TYPE_PARAM);
            if (!newUser) {
                intent.putExtra(SetupActivity.SETUP_INTENT_EXTRA_KEY, 4);
            }
            startActivity(intent);
        }
        finish();
    }

    public static Intent getRestartIntent(Context context) {
        Intent intent = new Intent(context, (Class<?>) StartActivity.class);
        intent.addFlags(67141632);
        return intent;
    }
}
