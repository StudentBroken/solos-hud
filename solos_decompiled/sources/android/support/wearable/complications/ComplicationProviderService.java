package android.support.wearable.complications;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.wearable.complications.IComplicationManager;
import android.support.wearable.complications.IComplicationProvider;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(24)
public abstract class ComplicationProviderService extends Service {
    public static final String ACTION_COMPLICATION_UPDATE_REQUEST = "android.support.wearable.complications.ACTION_COMPLICATION_UPDATE_REQUEST";
    public static final String CATEGORY_PROVIDER_CONFIG_ACTION = "android.support.wearable.complications.category.PROVIDER_CONFIG";
    public static final String EXTRA_COMPLICATION_DEACTIVATED = "android.support.wearable.complications.EXTRA_COMPLICATION_DEACTIVATED";
    public static final String EXTRA_COMPLICATION_ID = "android.support.wearable.complications.EXTRA_COMPLICATION_ID";
    public static final String EXTRA_COMPLICATION_MANAGER_BINDER = "android.support.wearable.complications.EXTRA_COMPLICATION_MANAGER_BINDER";
    public static final String EXTRA_COMPLICATION_TYPE = "android.support.wearable.complications.EXTRA_COMPLICATION_TYPE";
    public static final String EXTRA_CONFIG_COMPLICATION_ID = "android.support.wearable.complications.EXTRA_CONFIG_COMPLICATION_ID";
    public static final String EXTRA_CONFIG_COMPLICATION_TYPE = "android.support.wearable.complications.EXTRA_CONFIG_COMPLICATION_TYPE";
    public static final String EXTRA_CONFIG_PROVIDER_COMPONENT = "android.support.wearable.complications.EXTRA_CONFIG_PROVIDER_COMPONENT";
    public static final String METADATA_KEY_PROVIDER_CONFIG_ACTION = "android.support.wearable.complications.PROVIDER_CONFIG_ACTION";
    public static final String METADATA_KEY_SAFE_WATCH_FACES = "android.support.wearable.complications.SAFE_WATCH_FACES";
    public static final String METADATA_KEY_SUPPORTED_TYPES = "android.support.wearable.complications.SUPPORTED_TYPES";
    public static final String METADATA_KEY_UPDATE_PERIOD_SECONDS = "android.support.wearable.complications.UPDATE_PERIOD_SECONDS";
    private final Handler mMainThreadHandler = new Handler(Looper.getMainLooper());
    private IComplicationProviderWrapper mWrapper;

    public abstract void onComplicationUpdate(int i, int i2, ComplicationManager complicationManager);

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        if (!ACTION_COMPLICATION_UPDATE_REQUEST.equals(intent.getAction())) {
            return null;
        }
        if (this.mWrapper == null) {
            this.mWrapper = new IComplicationProviderWrapper();
        }
        return this.mWrapper;
    }

    public void onComplicationActivated(int complicationId, int type, ComplicationManager manager) {
    }

    public void onComplicationDeactivated(int complicationId) {
    }

    private class IComplicationProviderWrapper extends IComplicationProvider.Stub {
        private IComplicationProviderWrapper() {
        }

        @Override // android.support.wearable.complications.IComplicationProvider
        public void onUpdate(final int complicationId, final int type, IBinder manager) {
            final ComplicationManager complicationManager = new ComplicationManager(IComplicationManager.Stub.asInterface(manager));
            ComplicationProviderService.this.mMainThreadHandler.post(new Runnable() { // from class: android.support.wearable.complications.ComplicationProviderService.IComplicationProviderWrapper.1
                @Override // java.lang.Runnable
                public void run() {
                    ComplicationProviderService.this.onComplicationUpdate(complicationId, type, complicationManager);
                }
            });
        }

        @Override // android.support.wearable.complications.IComplicationProvider
        public void onComplicationDeactivated(final int complicationId) {
            ComplicationProviderService.this.mMainThreadHandler.post(new Runnable() { // from class: android.support.wearable.complications.ComplicationProviderService.IComplicationProviderWrapper.2
                @Override // java.lang.Runnable
                public void run() {
                    ComplicationProviderService.this.onComplicationDeactivated(complicationId);
                }
            });
        }

        @Override // android.support.wearable.complications.IComplicationProvider
        public void onComplicationActivated(final int complicationId, final int type, IBinder manager) {
            final ComplicationManager complicationManager = new ComplicationManager(IComplicationManager.Stub.asInterface(manager));
            ComplicationProviderService.this.mMainThreadHandler.post(new Runnable() { // from class: android.support.wearable.complications.ComplicationProviderService.IComplicationProviderWrapper.3
                @Override // java.lang.Runnable
                public void run() {
                    ComplicationProviderService.this.onComplicationActivated(complicationId, type, complicationManager);
                }
            });
        }
    }
}
