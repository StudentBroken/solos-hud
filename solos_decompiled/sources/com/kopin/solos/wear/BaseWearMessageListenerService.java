package com.kopin.solos.wear;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.WearableListenerService;
import com.kopin.solos.wear.WearMessenger;
import java.util.List;

/* JADX INFO: loaded from: classes59.dex */
public class BaseWearMessageListenerService extends WearableListenerService {
    private static final String TAG = "BaseWearMsgLstnrService";
    private static final int WATCH_SEARCH_TIMEOUT = 3800;
    protected static WatchMessageCallback watchMessageCallback;
    private static final Handler watchStateHandler = new Handler();
    private static boolean testingWatchState = false;

    public static void setCallback(WatchMessageCallback callback) {
        watchMessageCallback = callback;
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        WearMessenger.init(this);
        return 1;
    }

    @Override // android.app.Service
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartService = new Intent(getApplicationContext(), getClass());
        restartService.setPackage(getPackageName());
        PendingIntent restartServicePI = PendingIntent.getService(getApplicationContext(), 1, restartService, 1073741824);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(NotificationCompat.CATEGORY_ALARM);
        alarmService.set(3, SystemClock.elapsedRealtime() + 1000, restartServicePI);
    }

    @Override // com.google.android.gms.wearable.WearableListenerService, com.google.android.gms.wearable.MessageApi.MessageListener
    public void onMessageReceived(MessageEvent messageEvent) {
        MessageType messageType = getMessageType(messageEvent);
        switch (messageType) {
            case IS_WATCH_APP_INSTALLED:
                sendCommand(MessageType.WATCH_APP_IS_INSTALLED);
                break;
            case WATCH_APP_IS_INSTALLED:
                Log.i(TAG, "got app is installed answer");
                if (testingWatchState) {
                    Log.i(TAG, "ready to transfer");
                    if (watchMessageCallback != null) {
                        watchMessageCallback.onGetWatchState(WatchTransferState.READY_TO_TRANSFER);
                    }
                    watchStateHandler.removeCallbacksAndMessages(null);
                    testingWatchState = false;
                }
                break;
            default:
                super.onMessageReceived(messageEvent);
                break;
        }
    }

    public MessageType getMessageType(MessageEvent messageEvent) {
        return MessageType.fromString(messageEvent.getPath());
    }

    protected WearData getData(MessageEvent messageEvent) {
        if (messageEvent != null && messageEvent.getData() != null) {
            try {
                return WearData.fromBytes(messageEvent.getData());
            } catch (Exception ioe) {
                Log.e(TAG, "onMessageReceived conversion problem " + ioe.getMessage());
            }
        }
        return null;
    }

    protected void launchAppIfNotInForeground(String packageName, boolean inForeground) {
        if (!inForeground) {
            Log.i(TAG, "* launch app: Solos not in foreground/visible");
            PackageManager pm = getApplicationContext().getPackageManager();
            Intent launchIntent = pm.getLaunchIntentForPackage(packageName);
            launchIntent.addFlags(335544320);
            launchIntent.putExtra("hide_splash", true);
            getApplicationContext().startActivity(launchIntent);
            return;
        }
        Log.i(TAG, "* launch app: Solos already in foreground");
    }

    protected void sendCommand(MessageType messageType) {
        sendCommand(messageType, new ResultCallback() { // from class: com.kopin.solos.wear.BaseWearMessageListenerService.1
            @Override // com.google.android.gms.common.api.ResultCallback
            public void onResult(Result sendMessageResult) {
                if (sendMessageResult != null && sendMessageResult.getStatus().isSuccess()) {
                    Log.i(BaseWearMessageListenerService.TAG, "Sent command successfully...");
                } else {
                    Log.i(BaseWearMessageListenerService.TAG, "command send FAIL");
                }
            }
        });
    }

    protected void sendCommand(MessageType messageType, ResultCallback resultCallback) {
        Log.i(TAG, "sendCommand " + messageType.name());
        WearMessenger.sendAsyncMessage(messageType, null, resultCallback);
    }

    public static void getWatchState() {
        Log.i(TAG, "get watch state");
        testingWatchState = true;
        WearMessenger.connect();
        watchStateHandler.postDelayed(new Runnable() { // from class: com.kopin.solos.wear.BaseWearMessageListenerService.2
            @Override // java.lang.Runnable
            public void run() {
                if (BaseWearMessageListenerService.testingWatchState) {
                    Log.i(BaseWearMessageListenerService.TAG, "timeout trying to get to watch app");
                    if (BaseWearMessageListenerService.watchMessageCallback != null) {
                        BaseWearMessageListenerService.watchMessageCallback.onGetWatchState(WatchTransferState.WATCH_SOLOS_NOT_INSTALLED);
                    }
                    boolean unused = BaseWearMessageListenerService.testingWatchState = false;
                }
            }
        }, 3800L);
        Log.i(TAG, "get connected nodes");
        WearMessenger.getConnectedNodes(new WearMessenger.DeviceCallback() { // from class: com.kopin.solos.wear.BaseWearMessageListenerService.3
            @Override // com.kopin.solos.wear.WearMessenger.DeviceCallback
            public void getConnectedWatches(List<Node> nodes) {
                Log.i(BaseWearMessageListenerService.TAG, "got connected nodes");
                if (BaseWearMessageListenerService.testingWatchState) {
                    if (nodes == null || nodes.size() == 0) {
                        Log.i(BaseWearMessageListenerService.TAG, "NOnE connected");
                        if (BaseWearMessageListenerService.watchMessageCallback != null) {
                            BaseWearMessageListenerService.watchMessageCallback.onGetWatchState(WatchTransferState.WATCH_NOT_AVAILABLE);
                        }
                        BaseWearMessageListenerService.watchStateHandler.removeCallbacksAndMessages(null);
                        boolean unused = BaseWearMessageListenerService.testingWatchState = false;
                        return;
                    }
                    WearMessenger.sendCommand(MessageType.START_APP, nodes);
                    Log.i(BaseWearMessageListenerService.TAG, "found a watch, send app installed question");
                    WearMessenger.sendCommand(MessageType.IS_WATCH_APP_INSTALLED, nodes);
                }
            }
        });
    }
}
