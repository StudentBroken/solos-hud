package com.kopin.solos;

import android.os.AsyncTask;
import android.util.Log;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.kopin.peloton.PelotonPrefs;
import com.kopin.solos.share.Platforms;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.Shared;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.util.ModeSwitch;
import com.kopin.solos.wear.BaseWearMessageListenerService;
import com.kopin.solos.wear.MessageType;
import com.kopin.solos.wear.WatchModeTransfer;
import com.kopin.solos.wear.WearData;
import com.kopin.solos.wear.WearMessenger;
import java.io.IOException;

/* JADX INFO: loaded from: classes24.dex */
public class WearMessageListenerService extends BaseWearMessageListenerService {
    private static final String TAG = "WearMessageListrService";
    private static HardwareReceiverService mHardwareReceiverService;
    private static boolean transferringToWatch = false;
    private static boolean transferringToPhone = false;

    @Override // com.kopin.solos.wear.BaseWearMessageListenerService, com.google.android.gms.wearable.WearableListenerService, com.google.android.gms.wearable.MessageApi.MessageListener
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.e(TAG, "onMessageReceived " + messageEvent.getPath());
        super.onMessageReceived(messageEvent);
        MessageType messageType = getMessageType(messageEvent);
        switch (messageType) {
            case START_APP:
            case WATCH_IN_WATCH_MODE:
            case TRANSFER_IN_RIDE:
            case DISCONNECT_CONFIRMED:
            case WATCH_CONNECTIONS_DONE:
                new AsyncMessageReceived().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, messageEvent);
                break;
            case TRANSFER_COMMAND_RECEIVED:
                new AsyncMessageReceived().execute(messageEvent);
                break;
        }
    }

    private class AsyncMessageReceived extends AsyncTask<MessageEvent, Void, Void> {
        private AsyncMessageReceived() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Void doInBackground(MessageEvent... messageEvent) {
            MessageType messageType = WearMessageListenerService.this.getMessageType(messageEvent[0]);
            switch (messageType) {
                case WATCH_IN_WATCH_MODE:
                    WearMessageListenerService.this.checkMode();
                    break;
                case TRANSFER_IN_RIDE:
                    WearMessageListenerService.this.inRide();
                    break;
                case DISCONNECT_CONFIRMED:
                    WearMessageListenerService.this.transferBack();
                    break;
                case WATCH_CONNECTIONS_DONE:
                    WearMessageListenerService.this.transferDone();
                    break;
                case TRANSFER_COMMAND_RECEIVED:
                    WearMessageListenerService.this.doTransfer();
                    break;
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkMode() {
        if (!Prefs.isWatchMode()) {
            if (!transferringToPhone && !transferringToWatch) {
                Log.w(TAG, "watch and phone in different modes, force disconnect on watch");
                WearMessenger.sendAsyncCommand(MessageType.DISCONNECT_SENSORS_FORCE);
            } else if (transferringToWatch) {
                Log.w(TAG, "watch reporting already in watch mode, so go to done");
                transferDone();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doTransfer() {
        Log.e(TAG, "doTranfer, got init received msg");
        if (watchMessageCallback != null) {
            Log.e(TAG, "doTranfer, got init received msg CAllback");
            String externalId = SQLHelper.getExternalId(Prefs.getChosenBikeId(), Platforms.Peloton.getSharedKey(), PelotonPrefs.getEmail(), Shared.ShareType.BIKE);
            Prefs.setChosenBikeExternalId(externalId);
            WearData wearData = new WearData();
            WatchModeTransfer.prepareData(wearData, this);
            try {
                ModeSwitch.disableDevices(mHardwareReceiverService);
                WearMessenger.sendAsyncMessage(MessageType.TRANSFER, wearData.toBytes(), new ResultCallback<MessageApi.SendMessageResult>() { // from class: com.kopin.solos.WearMessageListenerService.1
                    @Override // com.google.android.gms.common.api.ResultCallback
                    public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                    }
                });
                transferringToWatch = true;
                Prefs.setWatchMode(true);
                watchMessageCallback.performAction(MessageType.TRANSFER);
            } catch (IOException ioe) {
                Log.e(TAG, "Send error with data " + ioe.getMessage());
            }
        }
    }

    public static void setHardwareReceiverService(HardwareReceiverService hardwareReceiverService) {
        mHardwareReceiverService = hardwareReceiverService;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void transferBack() {
        transferringToPhone = false;
        transferringToWatch = false;
        Prefs.setWatchMode(false);
        ModeSwitch.enableDevices(mHardwareReceiverService);
        Log.e(TAG, "transferback, received watch disconnect sensors confirmed");
        sendAction(MessageType.DISCONNECT_CONFIRMED);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void transferDone() {
        transferringToPhone = false;
        transferringToWatch = false;
        sendAction(MessageType.WATCH_CONNECTIONS_DONE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void inRide() {
        transferringToWatch = false;
        transferringToPhone = false;
        Prefs.setWatchMode(true);
        sendAction(MessageType.TRANSFER_IN_RIDE);
    }

    private void sendAction(MessageType messageType) {
        Log.e(TAG, "sendAction " + messageType.name());
        if (watchMessageCallback != null) {
            Log.e(TAG, "Callback: sendAction");
            watchMessageCallback.performAction(messageType);
        }
    }

    public static boolean isTransferringToWatch() {
        return transferringToWatch;
    }

    public static boolean isTransferringToPhone() {
        return transferringToPhone;
    }

    public static void setTransferringToPhone(boolean transfer) {
        transferringToPhone = transfer;
        if (transfer) {
            transferringToWatch = false;
        }
    }
}
