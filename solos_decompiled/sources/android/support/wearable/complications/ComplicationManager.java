package android.support.wearable.complications;

import android.annotation.TargetApi;
import android.os.RemoteException;
import android.util.Log;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(24)
public class ComplicationManager {
    private static final String TAG = "ComplicationManager";
    private final IComplicationManager mService;

    public ComplicationManager(IComplicationManager service) {
        this.mService = service;
    }

    public void updateComplicationData(int complicationId, ComplicationData data) {
        if (data.getType() == 1 || data.getType() == 2) {
            throw new IllegalArgumentException("Cannot send data of TYPE_NOT_CONFIGURED or TYPE_EMPTY. Use TYPE_NO_DATA instead.");
        }
        try {
            this.mService.updateComplicationData(complicationId, data);
        } catch (RemoteException e) {
            Log.w(TAG, "Failed to send complication data.", e);
        }
    }

    public void noUpdateRequired(int complicationId) {
        try {
            this.mService.updateComplicationData(complicationId, null);
        } catch (RemoteException e) {
            Log.w(TAG, "Failed to send complication data.", e);
        }
    }
}
