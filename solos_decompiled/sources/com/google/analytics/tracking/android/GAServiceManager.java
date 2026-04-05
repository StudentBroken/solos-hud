package com.google.analytics.tracking.android;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.google.analytics.tracking.android.GAUsage;
import com.google.android.gms.common.util.VisibleForTesting;

/* JADX INFO: loaded from: classes49.dex */
public class GAServiceManager extends ServiceManager {
    private static final int MSG_KEY = 1;
    private static final Object MSG_OBJECT = new Object();
    private static GAServiceManager instance;
    private boolean connected;
    private Context ctx;
    private int dispatchPeriodInSeconds;
    private Handler handler;
    private boolean listenForNetwork;
    private AnalyticsStoreStateListener listener;
    private GANetworkReceiver networkReceiver;
    private boolean pendingDispatch;
    private boolean pendingForceLocalDispatch;
    private String pendingHostOverride;
    private AnalyticsStore store;
    private boolean storeIsEmpty;
    private volatile AnalyticsThread thread;

    public static GAServiceManager getInstance() {
        if (instance == null) {
            instance = new GAServiceManager();
        }
        return instance;
    }

    private GAServiceManager() {
        this.dispatchPeriodInSeconds = 1800;
        this.pendingDispatch = true;
        this.connected = true;
        this.listenForNetwork = true;
        this.listener = new AnalyticsStoreStateListener() { // from class: com.google.analytics.tracking.android.GAServiceManager.1
            @Override // com.google.analytics.tracking.android.AnalyticsStoreStateListener
            public void reportStoreIsEmpty(boolean isEmpty) {
                GAServiceManager.this.updatePowerSaveMode(isEmpty, GAServiceManager.this.connected);
            }
        };
        this.storeIsEmpty = false;
    }

    @VisibleForTesting
    static void clearInstance() {
        instance = null;
    }

    @VisibleForTesting
    GAServiceManager(Context ctx, AnalyticsThread thread, AnalyticsStore store, boolean listenForNetwork) {
        this.dispatchPeriodInSeconds = 1800;
        this.pendingDispatch = true;
        this.connected = true;
        this.listenForNetwork = true;
        this.listener = new AnalyticsStoreStateListener() { // from class: com.google.analytics.tracking.android.GAServiceManager.1
            @Override // com.google.analytics.tracking.android.AnalyticsStoreStateListener
            public void reportStoreIsEmpty(boolean isEmpty) {
                GAServiceManager.this.updatePowerSaveMode(isEmpty, GAServiceManager.this.connected);
            }
        };
        this.storeIsEmpty = false;
        this.store = store;
        this.thread = thread;
        this.listenForNetwork = listenForNetwork;
        initialize(ctx, thread);
    }

    private void initializeNetworkReceiver() {
        this.networkReceiver = new GANetworkReceiver(this);
        this.networkReceiver.register(this.ctx);
    }

    private void initializeHandler() {
        this.handler = new Handler(this.ctx.getMainLooper(), new Handler.Callback() { // from class: com.google.analytics.tracking.android.GAServiceManager.2
            @Override // android.os.Handler.Callback
            public boolean handleMessage(Message msg) {
                if (1 == msg.what && GAServiceManager.MSG_OBJECT.equals(msg.obj)) {
                    GAUsage.getInstance().setDisableUsage(true);
                    GAServiceManager.this.dispatchLocalHits();
                    GAUsage.getInstance().setDisableUsage(false);
                    if (GAServiceManager.this.dispatchPeriodInSeconds > 0 && !GAServiceManager.this.storeIsEmpty) {
                        GAServiceManager.this.handler.sendMessageDelayed(GAServiceManager.this.handler.obtainMessage(1, GAServiceManager.MSG_OBJECT), GAServiceManager.this.dispatchPeriodInSeconds * 1000);
                    }
                }
                return true;
            }
        });
        if (this.dispatchPeriodInSeconds > 0) {
            this.handler.sendMessageDelayed(this.handler.obtainMessage(1, MSG_OBJECT), this.dispatchPeriodInSeconds * 1000);
        }
    }

    synchronized void initialize(Context ctx, AnalyticsThread thread) {
        if (this.ctx == null) {
            this.ctx = ctx.getApplicationContext();
            if (this.thread == null) {
                this.thread = thread;
                if (this.pendingDispatch) {
                    dispatchLocalHits();
                    this.pendingDispatch = false;
                }
                if (this.pendingForceLocalDispatch) {
                    setForceLocalDispatch();
                    this.pendingForceLocalDispatch = false;
                }
            }
        }
    }

    @VisibleForTesting
    AnalyticsStoreStateListener getListener() {
        return this.listener;
    }

    synchronized AnalyticsStore getStore() {
        if (this.store == null) {
            if (this.ctx == null) {
                throw new IllegalStateException("Cant get a store unless we have a context");
            }
            this.store = new PersistentAnalyticsStore(this.listener, this.ctx);
            if (this.pendingHostOverride != null) {
                this.store.getDispatcher().overrideHostUrl(this.pendingHostOverride);
                this.pendingHostOverride = null;
            }
        }
        if (this.handler == null) {
            initializeHandler();
        }
        if (this.networkReceiver == null && this.listenForNetwork) {
            initializeNetworkReceiver();
        }
        return this.store;
    }

    @VisibleForTesting
    synchronized void overrideHostUrl(String hostOverride) {
        if (this.store == null) {
            this.pendingHostOverride = hostOverride;
        } else {
            this.store.getDispatcher().overrideHostUrl(hostOverride);
        }
    }

    @Override // com.google.analytics.tracking.android.ServiceManager
    @Deprecated
    public synchronized void dispatchLocalHits() {
        if (this.thread == null) {
            Log.v("Dispatch call queued. Dispatch will run once initialization is complete.");
            this.pendingDispatch = true;
        } else {
            GAUsage.getInstance().setUsage(GAUsage.Field.DISPATCH);
            this.thread.dispatch();
        }
    }

    @Override // com.google.analytics.tracking.android.ServiceManager
    @Deprecated
    public synchronized void setLocalDispatchPeriod(int dispatchPeriodInSeconds) {
        if (this.handler == null) {
            Log.v("Dispatch period set with null handler. Dispatch will run once initialization is complete.");
            this.dispatchPeriodInSeconds = dispatchPeriodInSeconds;
        } else {
            GAUsage.getInstance().setUsage(GAUsage.Field.SET_DISPATCH_PERIOD);
            if (!this.storeIsEmpty && this.connected && this.dispatchPeriodInSeconds > 0) {
                this.handler.removeMessages(1, MSG_OBJECT);
            }
            this.dispatchPeriodInSeconds = dispatchPeriodInSeconds;
            if (dispatchPeriodInSeconds > 0 && !this.storeIsEmpty && this.connected) {
                this.handler.sendMessageDelayed(this.handler.obtainMessage(1, MSG_OBJECT), dispatchPeriodInSeconds * 1000);
            }
        }
    }

    @Override // com.google.analytics.tracking.android.ServiceManager
    @Deprecated
    public void setForceLocalDispatch() {
        if (this.thread == null) {
            Log.v("setForceLocalDispatch() queued. It will be called once initialization is complete.");
            this.pendingForceLocalDispatch = true;
        } else {
            GAUsage.getInstance().setUsage(GAUsage.Field.SET_FORCE_LOCAL_DISPATCH);
            this.thread.setForceLocalDispatch();
        }
    }

    @VisibleForTesting
    synchronized void updatePowerSaveMode(boolean storeIsEmpty, boolean connected) {
        if (this.storeIsEmpty != storeIsEmpty || this.connected != connected) {
            if ((storeIsEmpty || !connected) && this.dispatchPeriodInSeconds > 0) {
                this.handler.removeMessages(1, MSG_OBJECT);
            }
            if (!storeIsEmpty && connected && this.dispatchPeriodInSeconds > 0) {
                this.handler.sendMessageDelayed(this.handler.obtainMessage(1, MSG_OBJECT), this.dispatchPeriodInSeconds * 1000);
            }
            Log.v("PowerSaveMode " + ((storeIsEmpty || !connected) ? "initiated." : "terminated."));
            this.storeIsEmpty = storeIsEmpty;
            this.connected = connected;
        }
    }

    @Override // com.google.analytics.tracking.android.ServiceManager
    synchronized void updateConnectivityStatus(boolean connected) {
        updatePowerSaveMode(this.storeIsEmpty, connected);
    }

    @Override // com.google.analytics.tracking.android.ServiceManager
    synchronized void onRadioPowered() {
        if (!this.storeIsEmpty && this.connected && this.dispatchPeriodInSeconds > 0) {
            this.handler.removeMessages(1, MSG_OBJECT);
            this.handler.sendMessage(this.handler.obtainMessage(1, MSG_OBJECT));
        }
    }
}
