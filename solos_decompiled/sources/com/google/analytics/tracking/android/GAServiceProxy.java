package com.google.analytics.tracking.android;

import android.content.Context;
import android.content.Intent;
import com.google.analytics.tracking.android.AnalyticsGmsCoreClient;
import com.google.android.gms.analytics.internal.Command;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

/* JADX INFO: loaded from: classes49.dex */
class GAServiceProxy implements ServiceProxy, AnalyticsGmsCoreClient.OnConnectedListener, AnalyticsGmsCoreClient.OnConnectionFailedListener {
    private static final long FAILED_CONNECT_WAIT_TIME = 3000;
    private static final int MAX_TRIES = 2;
    private static final long RECONNECT_WAIT_TIME = 5000;
    private static final long SERVICE_CONNECTION_TIMEOUT = 300000;
    private volatile AnalyticsClient client;
    private Clock clock;
    private volatile int connectTries;
    private final Context ctx;
    private volatile Timer disconnectCheckTimer;
    private volatile Timer failedConnectTimer;
    private boolean forceLocalDispatch;
    private final GoogleAnalytics gaInstance;
    private long idleTimeout;
    private volatile long lastRequestTime;
    private boolean pendingClearHits;
    private boolean pendingDispatch;
    private boolean pendingServiceDisconnect;
    private final Queue<HitParams> queue;
    private volatile Timer reConnectTimer;
    private volatile ConnectState state;
    private AnalyticsStore store;
    private AnalyticsStore testStore;
    private final AnalyticsThread thread;

    private enum ConnectState {
        CONNECTING,
        CONNECTED_SERVICE,
        CONNECTED_LOCAL,
        BLOCKED,
        PENDING_CONNECTION,
        PENDING_DISCONNECT,
        DISCONNECTED
    }

    @VisibleForTesting
    GAServiceProxy(Context ctx, AnalyticsThread thread, AnalyticsStore store, GoogleAnalytics gaInstance) {
        this.queue = new ConcurrentLinkedQueue();
        this.idleTimeout = SERVICE_CONNECTION_TIMEOUT;
        this.testStore = store;
        this.ctx = ctx;
        this.thread = thread;
        this.gaInstance = gaInstance;
        this.clock = new Clock() { // from class: com.google.analytics.tracking.android.GAServiceProxy.1
            @Override // com.google.analytics.tracking.android.Clock
            public long currentTimeMillis() {
                return System.currentTimeMillis();
            }
        };
        this.connectTries = 0;
        this.state = ConnectState.DISCONNECTED;
    }

    GAServiceProxy(Context ctx, AnalyticsThread thread) {
        this(ctx, thread, null, GoogleAnalytics.getInstance(ctx));
    }

    void setClock(Clock clock) {
        this.clock = clock;
    }

    @Override // com.google.analytics.tracking.android.ServiceProxy
    public void putHit(Map<String, String> wireFormatParams, long hitTimeInMilliseconds, String path, List<Command> commands) {
        Log.v("putHit called");
        this.queue.add(new HitParams(wireFormatParams, hitTimeInMilliseconds, path, commands));
        sendQueue();
    }

    @Override // com.google.analytics.tracking.android.ServiceProxy
    public void dispatch() {
        switch (this.state) {
            case CONNECTED_LOCAL:
                dispatchToStore();
                break;
            case CONNECTED_SERVICE:
                break;
            default:
                this.pendingDispatch = true;
                break;
        }
    }

    @Override // com.google.analytics.tracking.android.ServiceProxy
    public void clearHits() {
        Log.v("clearHits called");
        this.queue.clear();
        switch (this.state) {
            case CONNECTED_LOCAL:
                this.store.clearHits(0L);
                this.pendingClearHits = false;
                break;
            case CONNECTED_SERVICE:
                this.client.clearHits();
                this.pendingClearHits = false;
                break;
            default:
                this.pendingClearHits = true;
                break;
        }
    }

    @Override // com.google.analytics.tracking.android.ServiceProxy
    public synchronized void setForceLocalDispatch() {
        if (!this.forceLocalDispatch) {
            Log.v("setForceLocalDispatch called.");
            this.forceLocalDispatch = true;
            switch (this.state) {
                case CONNECTED_SERVICE:
                    disconnectFromService();
                    break;
                case CONNECTING:
                    this.pendingServiceDisconnect = true;
                    break;
            }
        }
    }

    private Timer cancelTimer(Timer timer) {
        if (timer != null) {
            timer.cancel();
            return null;
        }
        return null;
    }

    private void clearAllTimers() {
        this.reConnectTimer = cancelTimer(this.reConnectTimer);
        this.failedConnectTimer = cancelTimer(this.failedConnectTimer);
        this.disconnectCheckTimer = cancelTimer(this.disconnectCheckTimer);
    }

    @Override // com.google.analytics.tracking.android.ServiceProxy
    public void createService() {
        if (this.client == null) {
            this.client = new AnalyticsGmsCoreClient(this.ctx, this, this);
            connectToService();
        }
    }

    void createService(AnalyticsClient client) {
        if (this.client == null) {
            this.client = client;
            connectToService();
        }
    }

    public void setIdleTimeout(long idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public synchronized void sendQueue() {
        if (!Thread.currentThread().equals(this.thread.getThread())) {
            this.thread.getQueue().add(new Runnable() { // from class: com.google.analytics.tracking.android.GAServiceProxy.2
                @Override // java.lang.Runnable
                public void run() {
                    GAServiceProxy.this.sendQueue();
                }
            });
        } else {
            if (this.pendingClearHits) {
                clearHits();
            }
            switch (this.state) {
                case CONNECTED_LOCAL:
                    while (!this.queue.isEmpty()) {
                        HitParams hitParams = this.queue.poll();
                        Log.v("Sending hit to store  " + hitParams);
                        this.store.putHit(hitParams.getWireFormatParams(), hitParams.getHitTimeInMilliseconds(), hitParams.getPath(), hitParams.getCommands());
                    }
                    if (this.pendingDispatch) {
                        dispatchToStore();
                    }
                    break;
                case CONNECTED_SERVICE:
                    while (!this.queue.isEmpty()) {
                        HitParams hitParams2 = this.queue.peek();
                        Log.v("Sending hit to service   " + hitParams2);
                        if (!this.gaInstance.isDryRunEnabled()) {
                            this.client.sendHit(hitParams2.getWireFormatParams(), hitParams2.getHitTimeInMilliseconds(), hitParams2.getPath(), hitParams2.getCommands());
                        } else {
                            Log.v("Dry run enabled. Hit not actually sent to service.");
                        }
                        this.queue.poll();
                    }
                    this.lastRequestTime = this.clock.currentTimeMillis();
                    break;
                case DISCONNECTED:
                    Log.v("Need to reconnect");
                    if (!this.queue.isEmpty()) {
                        connectToService();
                    }
                    break;
            }
        }
    }

    private void dispatchToStore() {
        this.store.dispatch();
        this.pendingDispatch = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void useStore() {
        if (this.state != ConnectState.CONNECTED_LOCAL) {
            clearAllTimers();
            Log.v("falling back to local store");
            if (this.testStore != null) {
                this.store = this.testStore;
            } else {
                GAServiceManager instance = GAServiceManager.getInstance();
                instance.initialize(this.ctx, this.thread);
                this.store = instance.getStore();
            }
            this.state = ConnectState.CONNECTED_LOCAL;
            sendQueue();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void connectToService() {
        if (!this.forceLocalDispatch && this.client != null && this.state != ConnectState.CONNECTED_LOCAL) {
            try {
                this.connectTries++;
                cancelTimer(this.failedConnectTimer);
                this.state = ConnectState.CONNECTING;
                this.failedConnectTimer = new Timer("Failed Connect");
                this.failedConnectTimer.schedule(new FailedConnectTask(), FAILED_CONNECT_WAIT_TIME);
                Log.v("connecting to Analytics service");
                this.client.connect();
            } catch (SecurityException e) {
                Log.w("security exception on connectToService");
                useStore();
            }
        } else {
            Log.w("client not initialized.");
            useStore();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void disconnectFromService() {
        if (this.client != null && this.state == ConnectState.CONNECTED_SERVICE) {
            this.state = ConnectState.PENDING_DISCONNECT;
            this.client.disconnect();
        }
    }

    @Override // com.google.analytics.tracking.android.AnalyticsGmsCoreClient.OnConnectedListener
    public synchronized void onConnected() {
        this.failedConnectTimer = cancelTimer(this.failedConnectTimer);
        this.connectTries = 0;
        Log.v("Connected to service");
        this.state = ConnectState.CONNECTED_SERVICE;
        if (this.pendingServiceDisconnect) {
            disconnectFromService();
            this.pendingServiceDisconnect = false;
        } else {
            sendQueue();
            this.disconnectCheckTimer = cancelTimer(this.disconnectCheckTimer);
            this.disconnectCheckTimer = new Timer("disconnect check");
            this.disconnectCheckTimer.schedule(new DisconnectCheckTask(), this.idleTimeout);
        }
    }

    @Override // com.google.analytics.tracking.android.AnalyticsGmsCoreClient.OnConnectedListener
    public synchronized void onDisconnected() {
        if (this.state == ConnectState.PENDING_DISCONNECT) {
            Log.v("Disconnected from service");
            clearAllTimers();
            this.state = ConnectState.DISCONNECTED;
        } else {
            Log.v("Unexpected disconnect.");
            this.state = ConnectState.PENDING_CONNECTION;
            if (this.connectTries < 2) {
                fireReconnectAttempt();
            } else {
                useStore();
            }
        }
    }

    @Override // com.google.analytics.tracking.android.AnalyticsGmsCoreClient.OnConnectionFailedListener
    public synchronized void onConnectionFailed(int errorCode, Intent resolution) {
        this.state = ConnectState.PENDING_CONNECTION;
        if (this.connectTries < 2) {
            Log.w("Service unavailable (code=" + errorCode + "), will retry.");
            fireReconnectAttempt();
        } else {
            Log.w("Service unavailable (code=" + errorCode + "), using local store.");
            useStore();
        }
    }

    private void fireReconnectAttempt() {
        this.reConnectTimer = cancelTimer(this.reConnectTimer);
        this.reConnectTimer = new Timer("Service Reconnect");
        this.reConnectTimer.schedule(new ReconnectTask(), 5000L);
    }

    private class FailedConnectTask extends TimerTask {
        private FailedConnectTask() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            if (GAServiceProxy.this.state == ConnectState.CONNECTING) {
                GAServiceProxy.this.useStore();
            }
        }
    }

    private class ReconnectTask extends TimerTask {
        private ReconnectTask() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            GAServiceProxy.this.connectToService();
        }
    }

    private class DisconnectCheckTask extends TimerTask {
        private DisconnectCheckTask() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            if (GAServiceProxy.this.state != ConnectState.CONNECTED_SERVICE || !GAServiceProxy.this.queue.isEmpty() || GAServiceProxy.this.lastRequestTime + GAServiceProxy.this.idleTimeout >= GAServiceProxy.this.clock.currentTimeMillis()) {
                GAServiceProxy.this.disconnectCheckTimer.schedule(GAServiceProxy.this.new DisconnectCheckTask(), GAServiceProxy.this.idleTimeout);
            } else {
                Log.v("Disconnecting due to inactivity");
                GAServiceProxy.this.disconnectFromService();
            }
        }
    }

    private static class HitParams {
        private final List<Command> commands;
        private final long hitTimeInMilliseconds;
        private final String path;
        private final Map<String, String> wireFormatParams;

        public HitParams(Map<String, String> wireFormatParams, long hitTimeInMilliseconds, String path, List<Command> commands) {
            this.wireFormatParams = wireFormatParams;
            this.hitTimeInMilliseconds = hitTimeInMilliseconds;
            this.path = path;
            this.commands = commands;
        }

        public Map<String, String> getWireFormatParams() {
            return this.wireFormatParams;
        }

        public long getHitTimeInMilliseconds() {
            return this.hitTimeInMilliseconds;
        }

        public String getPath() {
            return this.path;
        }

        public List<Command> getCommands() {
            return this.commands;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("PATH: ");
            sb.append(this.path);
            if (this.wireFormatParams != null) {
                sb.append("  PARAMS: ");
                for (Map.Entry<String, String> entry : this.wireFormatParams.entrySet()) {
                    sb.append(entry.getKey());
                    sb.append("=");
                    sb.append(entry.getValue());
                    sb.append(",  ");
                }
            }
            return sb.toString();
        }
    }
}
