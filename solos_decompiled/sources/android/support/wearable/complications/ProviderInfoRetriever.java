package android.support.wearable.complications;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.support.wearable.complications.IProviderInfoService;
import android.util.Log;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(24)
public class ProviderInfoRetriever {
    public static final String ACTION_GET_COMPLICATION_CONFIG = "android.support.wearable.complications.ACTION_GET_COMPLICATION_CONFIG";
    private static final String PROVIDER_INFO_SERVICE_CLASS = "com.google.android.clockwork.home.complications.ProviderInfoService";
    private static final String PROVIDER_INFO_SERVICE_PACKAGE = "com.google.android.wearable.app";
    private static final String TAG = "ProviderInfoRetriever";
    private static final long TIMEOUT_MILLIS = 5000;
    private final Context mContext;
    private final Executor mExecutor;
    private IProviderInfoService mService;
    private final Handler mMainThreadHandler = new Handler(Looper.getMainLooper());
    private final CountDownLatch mLatch = new CountDownLatch(1);
    private final ServiceConnection mConn = new ProviderInfoServiceConnection();
    private final Object mServiceLock = new Object();

    public static abstract class OnProviderInfoReceivedCallback {
        public abstract void onProviderInfoReceived(int i, @Nullable ComplicationProviderInfo complicationProviderInfo);

        public void onRetrievalFailed() {
        }
    }

    private final class ProviderInfoServiceConnection implements ServiceConnection {
        private ProviderInfoServiceConnection() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName name, IBinder service) {
            synchronized (ProviderInfoRetriever.this.mServiceLock) {
                ProviderInfoRetriever.this.mService = IProviderInfoService.Stub.asInterface(service);
            }
            ProviderInfoRetriever.this.mLatch.countDown();
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName name) {
            synchronized (ProviderInfoRetriever.this.mServiceLock) {
                ProviderInfoRetriever.this.mService = null;
            }
        }
    }

    public ProviderInfoRetriever(Context context, Executor executor) {
        this.mContext = context;
        this.mExecutor = executor;
    }

    public void init() {
        Intent intent = new Intent(ACTION_GET_COMPLICATION_CONFIG);
        intent.setClassName(PROVIDER_INFO_SERVICE_PACKAGE, PROVIDER_INFO_SERVICE_CLASS);
        this.mContext.bindService(intent, this.mConn, 1);
    }

    public void retrieveProviderInfo(final OnProviderInfoReceivedCallback callback, final ComponentName watchFaceComponent, final int... watchFaceComplicationIds) {
        this.mExecutor.execute(new Runnable() { // from class: android.support.wearable.complications.ProviderInfoRetriever.1
            @Override // java.lang.Runnable
            public void run() {
                ComplicationProviderInfo[] infos = ProviderInfoRetriever.this.doRetrieveInfo(watchFaceComponent, watchFaceComplicationIds);
                if (infos == null) {
                    ProviderInfoRetriever.this.mMainThreadHandler.post(new Runnable() { // from class: android.support.wearable.complications.ProviderInfoRetriever.1.2
                        @Override // java.lang.Runnable
                        public void run() {
                            callback.onRetrievalFailed();
                        }
                    });
                    return;
                }
                for (int i = 0; i < infos.length; i++) {
                    final int watchFaceComplicationId = watchFaceComplicationIds[i];
                    final ComplicationProviderInfo info = infos[i];
                    ProviderInfoRetriever.this.mMainThreadHandler.post(new Runnable() { // from class: android.support.wearable.complications.ProviderInfoRetriever.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            callback.onProviderInfoReceived(watchFaceComplicationId, info);
                        }
                    });
                }
            }
        });
    }

    public void release() {
        this.mContext.unbindService(this.mConn);
        synchronized (this.mServiceLock) {
            this.mService = null;
        }
        this.mLatch.countDown();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @WorkerThread
    @Nullable
    public ComplicationProviderInfo[] doRetrieveInfo(ComponentName watchFaceComponent, int... ids) {
        ComplicationProviderInfo[] providerInfos = null;
        try {
            if (!this.mLatch.await(5000L, TimeUnit.MILLISECONDS)) {
                Log.w(TAG, "Timeout while waiting for service binding.");
            } else {
                synchronized (this.mServiceLock) {
                    if (this.mService != null) {
                        try {
                            providerInfos = this.mService.getProviderInfos(watchFaceComponent, ids);
                        } catch (RemoteException e) {
                            Log.w(TAG, "RemoteException from ProviderInfoService.", e);
                        }
                    }
                }
            }
        } catch (InterruptedException e2) {
            Log.w(TAG, "Interrupted while waiting for service binding.", e2);
            Thread.currentThread().interrupt();
        }
        return providerInfos;
    }
}
