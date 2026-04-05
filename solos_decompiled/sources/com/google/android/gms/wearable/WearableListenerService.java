package com.google.android.gms.wearable;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.common.util.zzy;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.ChannelApi;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.internal.zzaa;
import com.google.android.gms.wearable.internal.zzai;
import com.google.android.gms.wearable.internal.zzdl;
import com.google.android.gms.wearable.internal.zzdx;
import com.google.android.gms.wearable.internal.zzeg;
import com.google.android.gms.wearable.internal.zzgh;
import java.util.List;

/* JADX INFO: loaded from: classes6.dex */
public class WearableListenerService extends Service implements CapabilityApi.CapabilityListener, ChannelApi.ChannelListener, DataApi.DataListener, MessageApi.MessageListener, NodeApi.NodeListener {
    public static final String BIND_LISTENER_INTENT_ACTION = "com.google.android.gms.wearable.BIND_LISTENER";
    private IBinder zzaHl;
    private ComponentName zzbRs;
    private zzb zzbRt;
    private Intent zzbRu;
    private Looper zzbRv;
    private final Object zzbRw = new Object();
    private boolean zzbRx;

    class zza implements ServiceConnection {
        private zza(WearableListenerService wearableListenerService) {
        }

        @Override // android.content.ServiceConnection
        public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        }

        @Override // android.content.ServiceConnection
        public final void onServiceDisconnected(ComponentName componentName) {
        }
    }

    final class zzb extends Handler {
        private boolean started;
        private final zza zzbRy;

        zzb(Looper looper) {
            super(looper);
            this.zzbRy = new zza();
        }

        @SuppressLint({"UntrackedBindService"})
        private final synchronized void zzDT() {
            if (!this.started) {
                if (Log.isLoggable("WearableLS", 2)) {
                    String strValueOf = String.valueOf(WearableListenerService.this.zzbRs);
                    Log.v("WearableLS", new StringBuilder(String.valueOf(strValueOf).length() + 13).append("bindService: ").append(strValueOf).toString());
                }
                WearableListenerService.this.bindService(WearableListenerService.this.zzbRu, this.zzbRy, 1);
                this.started = true;
            }
        }

        @SuppressLint({"UntrackedBindService"})
        private final synchronized void zzgm(String str) {
            if (this.started) {
                if (Log.isLoggable("WearableLS", 2)) {
                    String strValueOf = String.valueOf(WearableListenerService.this.zzbRs);
                    Log.v("WearableLS", new StringBuilder(String.valueOf(str).length() + 17 + String.valueOf(strValueOf).length()).append("unbindService: ").append(str).append(", ").append(strValueOf).toString());
                }
                try {
                    WearableListenerService.this.unbindService(this.zzbRy);
                } catch (RuntimeException e) {
                    Log.e("WearableLS", "Exception when unbinding from local service", e);
                }
                this.started = false;
            }
        }

        @Override // android.os.Handler
        public final void dispatchMessage(Message message) {
            zzDT();
            try {
                super.dispatchMessage(message);
            } finally {
                if (!hasMessages(0)) {
                    zzgm("dispatch");
                }
            }
        }

        final void quit() {
            getLooper().quit();
            zzgm("quit");
        }
    }

    final class zzc extends zzdl {
        private volatile int zzbRA;

        private zzc() {
            this.zzbRA = -1;
        }

        private final boolean zza(Runnable runnable, String str, Object obj) {
            boolean z;
            boolean z2 = false;
            if (Log.isLoggable("WearableLS", 3)) {
                Log.d("WearableLS", String.format("%s: %s %s", str, WearableListenerService.this.zzbRs.toString(), obj));
            }
            int callingUid = Binder.getCallingUid();
            if (callingUid == this.zzbRA) {
                z = true;
            } else if ((zzgh.zzbz(WearableListenerService.this).zzgo("com.google.android.wearable.app.cn") && zzy.zzb(WearableListenerService.this, callingUid, "com.google.android.wearable.app.cn")) || zzy.zzf(WearableListenerService.this, callingUid)) {
                this.zzbRA = callingUid;
                z = true;
            } else {
                Log.e("WearableLS", new StringBuilder(57).append("Caller is not GooglePlayServices; caller UID: ").append(callingUid).toString());
                z = false;
            }
            if (z) {
                synchronized (WearableListenerService.this.zzbRw) {
                    if (!WearableListenerService.this.zzbRx) {
                        WearableListenerService.this.zzbRt.post(runnable);
                        z2 = true;
                    }
                }
            }
            return z2;
        }

        @Override // com.google.android.gms.wearable.internal.zzdk
        public final void onConnectedNodes(List<zzeg> list) {
            zza(new zzp(this, list), "onConnectedNodes", list);
        }

        @Override // com.google.android.gms.wearable.internal.zzdk
        public final void zzS(DataHolder dataHolder) {
            zzl zzlVar = new zzl(this, dataHolder);
            try {
                String strValueOf = String.valueOf(dataHolder);
                if (zza(zzlVar, "onDataItemChanged", new StringBuilder(String.valueOf(strValueOf).length() + 18).append(strValueOf).append(", rows=").append(dataHolder.getCount()).toString())) {
                }
            } finally {
                dataHolder.close();
            }
        }

        @Override // com.google.android.gms.wearable.internal.zzdk
        public final void zza(zzaa zzaaVar) {
            zza(new zzq(this, zzaaVar), "onConnectedCapabilityChanged", zzaaVar);
        }

        @Override // com.google.android.gms.wearable.internal.zzdk
        public final void zza(zzai zzaiVar) {
            zza(new zzt(this, zzaiVar), "onChannelEvent", zzaiVar);
        }

        @Override // com.google.android.gms.wearable.internal.zzdk
        public final void zza(zzdx zzdxVar) {
            zza(new zzm(this, zzdxVar), "onMessageReceived", zzdxVar);
        }

        @Override // com.google.android.gms.wearable.internal.zzdk
        public final void zza(zzeg zzegVar) {
            zza(new zzn(this, zzegVar), "onPeerConnected", zzegVar);
        }

        @Override // com.google.android.gms.wearable.internal.zzdk
        public final void zza(com.google.android.gms.wearable.internal.zzi zziVar) {
            zza(new zzs(this, zziVar), "onEntityUpdate", zziVar);
        }

        @Override // com.google.android.gms.wearable.internal.zzdk
        public final void zza(com.google.android.gms.wearable.internal.zzl zzlVar) {
            zza(new zzr(this, zzlVar), "onNotificationReceived", zzlVar);
        }

        @Override // com.google.android.gms.wearable.internal.zzdk
        public final void zzb(zzeg zzegVar) {
            zza(new zzo(this, zzegVar), "onPeerDisconnected", zzegVar);
        }
    }

    public Looper getLooper() {
        if (this.zzbRv == null) {
            HandlerThread handlerThread = new HandlerThread("WearableListenerService");
            handlerThread.start();
            this.zzbRv = handlerThread.getLooper();
        }
        return this.zzbRv;
    }

    @Override // android.app.Service
    public final IBinder onBind(Intent intent) {
        if (BIND_LISTENER_INTENT_ACTION.equals(intent.getAction())) {
            return this.zzaHl;
        }
        return null;
    }

    @Override // com.google.android.gms.wearable.CapabilityApi.CapabilityListener
    public void onCapabilityChanged(CapabilityInfo capabilityInfo) {
    }

    @Override // com.google.android.gms.wearable.ChannelApi.ChannelListener
    public void onChannelClosed(Channel channel, int i, int i2) {
    }

    @Override // com.google.android.gms.wearable.ChannelApi.ChannelListener
    public void onChannelOpened(Channel channel) {
    }

    public void onConnectedNodes(List<Node> list) {
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.zzbRs = new ComponentName(this, getClass().getName());
        if (Log.isLoggable("WearableLS", 3)) {
            String strValueOf = String.valueOf(this.zzbRs);
            Log.d("WearableLS", new StringBuilder(String.valueOf(strValueOf).length() + 10).append("onCreate: ").append(strValueOf).toString());
        }
        this.zzbRt = new zzb(getLooper());
        this.zzbRu = new Intent(BIND_LISTENER_INTENT_ACTION);
        this.zzbRu.setComponent(this.zzbRs);
        this.zzaHl = new zzc();
    }

    @Override // com.google.android.gms.wearable.DataApi.DataListener
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
    }

    @Override // android.app.Service
    public void onDestroy() {
        if (Log.isLoggable("WearableLS", 3)) {
            String strValueOf = String.valueOf(this.zzbRs);
            Log.d("WearableLS", new StringBuilder(String.valueOf(strValueOf).length() + 11).append("onDestroy: ").append(strValueOf).toString());
        }
        synchronized (this.zzbRw) {
            this.zzbRx = true;
            if (this.zzbRt == null) {
                String strValueOf2 = String.valueOf(this.zzbRs);
                throw new IllegalStateException(new StringBuilder(String.valueOf(strValueOf2).length() + 111).append("onDestroy: mServiceHandler not set, did you override onCreate() but forget to call super.onCreate()? component=").append(strValueOf2).toString());
            }
            this.zzbRt.quit();
        }
        super.onDestroy();
    }

    public void onEntityUpdate(com.google.android.gms.wearable.zzb zzbVar) {
    }

    @Override // com.google.android.gms.wearable.ChannelApi.ChannelListener
    public void onInputClosed(Channel channel, int i, int i2) {
    }

    @Override // com.google.android.gms.wearable.MessageApi.MessageListener
    public void onMessageReceived(MessageEvent messageEvent) {
    }

    public void onNotificationReceived(zzd zzdVar) {
    }

    @Override // com.google.android.gms.wearable.ChannelApi.ChannelListener
    public void onOutputClosed(Channel channel, int i, int i2) {
    }

    @Override // com.google.android.gms.wearable.NodeApi.NodeListener
    public void onPeerConnected(Node node) {
    }

    @Override // com.google.android.gms.wearable.NodeApi.NodeListener
    public void onPeerDisconnected(Node node) {
    }
}
