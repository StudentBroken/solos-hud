package android.support.wearable.notifications;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.wearable.notifications.IBridgingManagerService;
import android.util.Log;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(24)
public class BridgingManager {
    private static final String ACTION_BIND_BRIDGING_MANAGER = "android.support.wearable.notifications.action.BIND_BRIDGING_MANAGER";
    private static final String BRIDGING_CONFIG_SERVICE_PACKAGE = "com.google.android.wearable.app";
    private static final String TAG = "BridgingManager";
    private final Context mContext;

    private BridgingManager(Context context) {
        this.mContext = context;
    }

    public static BridgingManager fromContext(Context context) {
        return new BridgingManager(context);
    }

    public void setConfig(BridgingConfig bridgingConfig) {
        if (!isWearableDevice(this.mContext)) {
            throw new IllegalStateException("API only supported on wearable devices");
        }
        BridgingConfigServiceConnection connection = new BridgingConfigServiceConnection(this.mContext, bridgingConfig);
        Intent intent = new Intent(ACTION_BIND_BRIDGING_MANAGER);
        intent.setPackage(BRIDGING_CONFIG_SERVICE_PACKAGE);
        if (!this.mContext.bindService(intent, connection, 1)) {
            Log.e(TAG, "Failed to bind");
            this.mContext.unbindService(connection);
        }
    }

    private static class BridgingConfigServiceConnection implements ServiceConnection {
        private final Bundle mBundle;
        private final Context mContext;

        BridgingConfigServiceConnection(Context context, BridgingConfig bridgingConfig) {
            this.mContext = context;
            this.mBundle = bridgingConfig.toBundle(this.mContext);
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName className, IBinder binder) {
            IBridgingManagerService service = IBridgingManagerService.Stub.asInterface(binder);
            try {
                service.setBridgingConfig(this.mBundle);
            } catch (RemoteException e) {
                Log.e(BridgingManager.TAG, "Failed to call method", e);
            }
            this.mContext.unbindService(this);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName arg0) {
            this.mContext.unbindService(this);
        }
    }

    private static boolean isWearableDevice(Context context) {
        return context.getPackageManager().hasSystemFeature("android.hardware.type.watch");
    }
}
