package com.kopin.solos.wear;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import com.kopin.pupil.PupilDevice;
import com.kopin.solos.HardwareReceiverService;
import com.kopin.solos.MainActivity;
import com.kopin.solos.WearMessageListenerService;
import com.kopin.solos.common.BaseActivity;
import com.kopin.solos.share.Sync;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.util.ModeSwitch;

/* JADX INFO: loaded from: classes24.dex */
public class WatchModeActivity extends BaseActivity implements WatchMessageCallback {
    private static final int HEADSET_CHECK_TIMES = 4500;
    public static final String MODE_PHONE = "phone";
    private static final String TAG = "WatchModeAct";
    static final boolean TIME_OUT_ON = true;
    private static final int TRANSFER_BACK_TIMEOUT = 4500;
    private static final int TRANSFER_WATCH_TIMEOUT = 22000;
    ImageView mImageView;
    HardwareReceiverService mService;
    TextView mTextHeader;
    TextView mTextMessage;
    int numOfHeadsetChecks;
    private MessageType state;
    private boolean phoneMode = true;
    private boolean transferring = false;
    boolean mBound = false;
    final Handler watchTransferHandler = new Handler();
    final Handler phoneTransferHandler = new Handler();
    private ServiceConnection mConnection = new ServiceConnection() { // from class: com.kopin.solos.wear.WatchModeActivity.1
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName className, IBinder service) {
            HardwareReceiverService.HardwareBinder binder = (HardwareReceiverService.HardwareBinder) service;
            WatchModeActivity.this.mService = binder.getService();
            WatchModeActivity.this.mBound = true;
            WearMessageListenerService.setHardwareReceiverService(WatchModeActivity.this.mService);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName arg0) {
            WatchModeActivity.this.mBound = false;
        }
    };

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.kopin.solos.R.layout.activity_watch_mode);
        this.phoneMode = getIntent().getBooleanExtra(MODE_PHONE, false);
        this.mImageView = (ImageView) findViewById(com.kopin.solos.R.id.imageView);
        this.mTextHeader = (TextView) findViewById(com.kopin.solos.R.id.textHeader);
        this.mTextMessage = (TextView) findViewById(com.kopin.solos.R.id.textMessage);
        if (this.phoneMode) {
            this.mImageView.setImageResource(com.kopin.solos.R.drawable.ic_phone_spin);
            this.mTextHeader.setText(com.kopin.solos.R.string.switching_to_phone_mode);
        } else {
            this.mImageView.setImageResource(com.kopin.solos.R.drawable.ic_watch_spin);
            this.mTextHeader.setText(com.kopin.solos.R.string.switching_to_watch_mode);
        }
        this.mImageView.setDrawingCacheEnabled(true);
        RotateAnimation rotateAnim = new RotateAnimation(0.0f, 359.0f, 1, 0.5f, 1, 0.5f);
        rotateAnim.setInterpolator(new LinearInterpolator());
        rotateAnim.setDuration(1500L);
        rotateAnim.setRepeatCount(-1);
        rotateAnim.setFillAfter(true);
        this.mImageView.startAnimation(rotateAnim);
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    protected void onStart() {
        super.onStart();
        WearMessageListenerService.setCallback(this);
        Intent intent = new Intent(this, (Class<?>) HardwareReceiverService.class);
        bindService(intent, this.mConnection, 1);
        this.transferring = false;
        WearMessenger.connect();
        checkWatch();
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    protected void onStop() {
        super.onStop();
        WearMessageListenerService.setCallback(null);
        if (this.mBound) {
            unbindService(this.mConnection);
            this.mBound = false;
        }
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    public void onBackPressed() {
    }

    private void checkWatch() {
        Log.e(TAG, "check watch, get watch state");
        WearMessageListenerService.getWatchState();
    }

    @Override // com.kopin.solos.wear.WatchMessageCallback
    public void onGetWatchState(WatchTransferState watchTransferState) {
        Log.e(TAG, "onGetWatchState " + watchTransferState);
        if (this.phoneMode) {
            Log.e(TAG, "go to phone mode" + watchTransferState.name());
            startTransfer();
        } else if (!PupilDevice.isConnected() || PupilDevice.getDevice() == null || PupilDevice.getDevice().getAddress() == null) {
            showError(com.kopin.solos.R.string.err_no_headset);
        } else if (watchTransferState == WatchTransferState.READY_TO_TRANSFER) {
            Log.e(TAG, "go to watch mode");
            startTransfer();
        } else {
            Log.e(TAG, "watch not in correct state to transfer over " + watchTransferState.name());
        }
    }

    private void startTransfer() {
        Log.e(TAG, "start transfer");
        WearMessenger.sendAsyncCommand(MessageType.START_APP);
        if (this.phoneMode) {
            Log.e(TAG, "initiate transfer back");
            this.state = MessageType.DISCONNECT_SENSORS;
            WearMessenger.sendAsyncCommand(MessageType.DISCONNECT_SENSORS);
            WearMessageListenerService.setTransferringToPhone(true);
            this.phoneTransferHandler.postDelayed(new Runnable() { // from class: com.kopin.solos.wear.WatchModeActivity.2
                @Override // java.lang.Runnable
                public void run() {
                    WatchModeActivity.this.phoneTransferHandler.removeCallbacksAndMessages(null);
                    if (WearMessageListenerService.isTransferringToPhone()) {
                        Log.e(WatchModeActivity.TAG, "phone mode: time out waiting for disconnect sensors on watch, FORCE NOW");
                        WearMessenger.sendAsyncCommand(MessageType.DISCONNECT_SENSORS_FORCE);
                        WatchModeActivity.this.transferBack();
                    }
                }
            }, 4500L);
            return;
        }
        Log.e(TAG, "initiate transfer");
        this.state = MessageType.TRANSFER_INIT;
        WearMessenger.sendAsyncCommand(MessageType.TRANSFER_INIT);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void transferBack() {
        Log.e(TAG, "transfer back " + this.state.name());
        this.state = MessageType.DISCONNECT_CONFIRMED;
        Prefs.setWatchMode(false);
        Log.e(TAG, "do transfer back " + this.state.name());
        ModeSwitch.enableDevices(this.mService);
        WearMessageListenerService.setTransferringToPhone(false);
        Sync.sync(true);
        this.numOfHeadsetChecks = 3;
        delayedFinish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void transferTimeOut() {
        this.watchTransferHandler.postDelayed(new Runnable() { // from class: com.kopin.solos.wear.WatchModeActivity.3
            @Override // java.lang.Runnable
            public void run() {
                Log.i(WatchModeActivity.TAG, "Transfer TIME OUT");
                if (WearMessageListenerService.isTransferringToWatch()) {
                    Log.i(WatchModeActivity.TAG, "Transfer TIME OUT - now enable devices for the phone ready");
                    Prefs.setWatchMode(false);
                    ModeSwitch.enableDevices(WatchModeActivity.this.mService);
                    WearMessenger.sendAsyncCommand(MessageType.DISCONNECT_SENSORS_FORCE);
                    WatchModeActivity.this.showError(com.kopin.solos.R.string.transfer_timed_out);
                }
            }
        }, 22000L);
    }

    @Override // com.kopin.solos.wear.WatchMessageCallback
    public void performAction(final MessageType messageType) {
        runOnUiThread(new Runnable() { // from class: com.kopin.solos.wear.WatchModeActivity.4
            @Override // java.lang.Runnable
            public void run() {
                Log.e(WatchModeActivity.TAG, "preform action " + messageType.name());
                switch (AnonymousClass7.$SwitchMap$com$kopin$solos$wear$MessageType[messageType.ordinal()]) {
                    case 1:
                        Log.e(WatchModeActivity.TAG, "transferring headset/sensors to watch, start the process now");
                        WatchModeActivity.this.transferTimeOut();
                        break;
                    case 2:
                        WatchModeActivity.this.transferring = false;
                        Log.e(WatchModeActivity.TAG, "TRANSFER_IN_RIDE");
                        WatchModeActivity.this.showError(com.kopin.solos.R.string.watch_ride_in_progress);
                        break;
                    case 3:
                        Log.e(WatchModeActivity.TAG, "transferred sent");
                        break;
                    case 4:
                        Log.e(WatchModeActivity.TAG, "watch confirmed disconnected");
                        Sync.sync(true);
                        WatchModeActivity.this.numOfHeadsetChecks = 3;
                        WatchModeActivity.this.delayedFinish();
                        break;
                    case 5:
                        WatchModeActivity.this.watchTransferHandler.removeCallbacksAndMessages(null);
                        WatchModeActivity.this.phoneTransferHandler.removeCallbacksAndMessages(null);
                        Log.e(WatchModeActivity.TAG, "watch connections all done");
                        new Handler().postDelayed(new Runnable() { // from class: com.kopin.solos.wear.WatchModeActivity.4.1
                            @Override // java.lang.Runnable
                            public void run() {
                                WatchModeActivity.this.finish();
                            }
                        }, 1500L);
                        break;
                }
            }
        });
    }

    /* JADX INFO: renamed from: com.kopin.solos.wear.WatchModeActivity$7, reason: invalid class name */
    static /* synthetic */ class AnonymousClass7 {
        static final /* synthetic */ int[] $SwitchMap$com$kopin$solos$wear$MessageType = new int[MessageType.values().length];

        static {
            try {
                $SwitchMap$com$kopin$solos$wear$MessageType[MessageType.TRANSFER.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$kopin$solos$wear$MessageType[MessageType.TRANSFER_IN_RIDE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$kopin$solos$wear$MessageType[MessageType.TRANSFER_SENT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$kopin$solos$wear$MessageType[MessageType.DISCONNECT_CONFIRMED.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$kopin$solos$wear$MessageType[MessageType.WATCH_CONNECTIONS_DONE.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showError(final int errorRes) {
        runOnUiThread(new Runnable() { // from class: com.kopin.solos.wear.WatchModeActivity.5
            @Override // java.lang.Runnable
            public void run() {
                WatchModeActivity.this.mTextHeader.setCompoundDrawablesWithIntrinsicBounds(com.kopin.solos.R.drawable.ic_error_login, 0, 0, 0);
                WatchModeActivity.this.mTextHeader.setText(com.kopin.solos.R.string.menu_command_log_error);
                WatchModeActivity.this.mTextMessage.setText(errorRes);
                WatchModeActivity.this.findViewById(com.kopin.solos.R.id.btnOK).setVisibility(0);
            }
        });
    }

    public void onOkClick(View view) {
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void delayedFinish() {
        new Handler().postDelayed(new Runnable() { // from class: com.kopin.solos.wear.WatchModeActivity.6
            @Override // java.lang.Runnable
            public void run() {
                WatchModeActivity watchModeActivity = WatchModeActivity.this;
                int i = watchModeActivity.numOfHeadsetChecks - 1;
                watchModeActivity.numOfHeadsetChecks = i;
                if (i > 0 && (!PupilDevice.isConnected() || PupilDevice.getDevice() == null || PupilDevice.getDevice().getAddress() == null)) {
                    WatchModeActivity.this.delayedFinish();
                } else {
                    WatchModeActivity.this.finish();
                }
            }
        }, 4500L);
    }

    @Override // android.app.Activity
    public void finish() {
        Intent intent = new Intent(this, (Class<?>) MainActivity.class);
        intent.setFlags(268468224);
        startActivity(intent);
        super.finish();
    }
}
