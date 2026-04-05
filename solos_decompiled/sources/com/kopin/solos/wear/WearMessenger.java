package com.kopin.solos.wear;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import java.util.List;

/* JADX INFO: loaded from: classes59.dex */
public class WearMessenger implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    static final String TAG = "WearMessgr";
    private static GoogleApiClient mApiClient;
    private static MessageApi.MessageListener messageListener;
    private static WearMessenger self = null;

    public interface DeviceCallback {
        void getConnectedWatches(List<Node> list);
    }

    public static void init(Context context) {
        self = new WearMessenger();
        mApiClient = new GoogleApiClient.Builder(context.getApplicationContext()).addApi(Wearable.API).addConnectionCallbacks(self).addOnConnectionFailedListener(self).build();
        Log.i(TAG, "init now connect");
        connect();
    }

    public static void connect() {
        Log.i(TAG, "connect");
        if (mApiClient != null && !mApiClient.isConnected() && !mApiClient.isConnecting()) {
            Log.i(TAG, "connecting now...");
            mApiClient.connect();
        }
        if (mApiClient != null && mApiClient.isConnected()) {
            Log.i(TAG, "api client is connected");
        }
    }

    public static void disconnect() {
        Log.i(TAG, "disconnect msgr");
        if (mApiClient != null) {
            Log.i(TAG, "disconnect msgr not null");
            if (messageListener != null) {
                Wearable.MessageApi.removeListener(mApiClient, messageListener);
            }
            if (mApiClient.isConnected()) {
                mApiClient.disconnect();
            }
            mApiClient.unregisterConnectionCallbacks(self);
            mApiClient.unregisterConnectionFailedListener(self);
        }
    }

    public static void setMessageListener(MessageApi.MessageListener listener) {
        Log.i(TAG, "setMessageListener");
        messageListener = listener;
        if (messageListener != null) {
            Log.i(TAG, "setMessageListener NOT NULL");
            Wearable.MessageApi.addListener(mApiClient, messageListener);
        }
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "onConnected");
        if (mApiClient != null && messageListener != null) {
            Log.i(TAG, "onConnected set listener not null");
            Wearable.MessageApi.addListener(mApiClient, messageListener);
        }
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "onConnectionSuspended");
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed ****");
    }

    public static void startApp() {
        Log.i(TAG, "startApp on other device");
        sendAsyncCommand(MessageType.START_APP);
    }

    public static void sendAsyncMessage(final MessageType messageType, final byte[] message, final ResultCallback<MessageApi.SendMessageResult> callback) {
        Log.i(TAG, "sendAsyncMessage: " + messageType.name());
        Wearable.NodeApi.getConnectedNodes(mApiClient).setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() { // from class: com.kopin.solos.wear.WearMessenger.1
            @Override // com.google.android.gms.common.api.ResultCallback
            public void onResult(NodeApi.GetConnectedNodesResult nodes) {
                Log.i(WearMessenger.TAG, "sendAsyncMessage to nodes");
                for (Node node : nodes.getNodes()) {
                    Log.i(WearMessenger.TAG, "sendAsyncMessage to node " + node.getDisplayName());
                    if (callback == null) {
                        Wearable.MessageApi.sendMessage(WearMessenger.mApiClient, node.getId(), messageType.name(), message);
                    } else {
                        Wearable.MessageApi.sendMessage(WearMessenger.mApiClient, node.getId(), messageType.name(), message).setResultCallback(callback);
                    }
                }
            }
        });
    }

    public static void sendAsyncCommand(final MessageType messageType) {
        Log.i(TAG, "sendAsyncCommand: " + messageType.name());
        Wearable.NodeApi.getConnectedNodes(mApiClient).setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() { // from class: com.kopin.solos.wear.WearMessenger.2
            @Override // com.google.android.gms.common.api.ResultCallback
            public void onResult(NodeApi.GetConnectedNodesResult nodes) {
                Log.i(WearMessenger.TAG, "sendAsyncMessage to nodes");
                WearMessenger.sendCommand(messageType, nodes.getNodes());
            }
        });
    }

    public static void sendCommand(MessageType messageType, List<Node> nodes) {
        Log.i(TAG, "sendCommand: " + messageType.name());
        for (Node node : nodes) {
            Log.i(TAG, "sendAsyncMessage to node " + node.getDisplayName());
            Wearable.MessageApi.sendMessage(mApiClient, node.getId(), messageType.name(), null);
        }
    }

    public static void sendCommand(MessageType messageType, List<Node> nodes, ResultCallback<MessageApi.SendMessageResult> callback) {
        Log.i(TAG, "sendAsyncMessage: " + messageType.name());
        for (Node node : nodes) {
            Log.i(TAG, "sendAsyncMessage to node " + node.getDisplayName());
            if (callback != null) {
                Wearable.MessageApi.sendMessage(mApiClient, node.getId(), messageType.name(), null).setResultCallback(callback);
            } else {
                Wearable.MessageApi.sendMessage(mApiClient, node.getId(), messageType.name(), null);
            }
        }
    }

    public static void getConnectedNodes(final DeviceCallback deviceCallback) {
        Wearable.NodeApi.getConnectedNodes(mApiClient).setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() { // from class: com.kopin.solos.wear.WearMessenger.3
            @Override // com.google.android.gms.common.api.ResultCallback
            public void onResult(NodeApi.GetConnectedNodesResult nodes) {
                if (deviceCallback != null) {
                    deviceCallback.getConnectedWatches(nodes.getNodes());
                }
            }
        });
    }
}
