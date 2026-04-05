package android.support.wearable.watchface;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.service.wallpaper.WallpaperService;
import android.support.annotation.CallSuper;
import android.support.annotation.RequiresPermission;
import android.support.wearable.complications.ComplicationData;
import android.support.wearable.watchface.IWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Objects;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(21)
public abstract class WatchFaceService extends WallpaperService {
    public static final String ACTION_REQUEST_STATE = "com.google.android.wearable.watchfaces.action.REQUEST_STATE";
    public static final String COMMAND_AMBIENT_UPDATE = "com.google.android.wearable.action.AMBIENT_UPDATE";
    public static final String COMMAND_BACKGROUND_ACTION = "com.google.android.wearable.action.BACKGROUND_ACTION";
    public static final String COMMAND_COMPLICATION_DATA = "com.google.android.wearable.action.COMPLICATION_DATA";
    public static final String COMMAND_REQUEST_STYLE = "com.google.android.wearable.action.REQUEST_STYLE";
    public static final String COMMAND_SET_BINDER = "com.google.android.wearable.action.SET_BINDER";
    public static final String COMMAND_SET_PROPERTIES = "com.google.android.wearable.action.SET_PROPERTIES";
    public static final String COMMAND_TAP = "android.wallpaper.tap";
    public static final String COMMAND_TOUCH = "android.wallpaper.touch";
    public static final String COMMAND_TOUCH_CANCEL = "android.wallpaper.touch_cancel";
    public static final String EXTRA_AMBIENT_MODE = "ambient_mode";
    public static final String EXTRA_BINDER = "binder";
    public static final String EXTRA_CARD_LOCATION = "card_location";
    public static final String EXTRA_COMPLICATION_DATA = "complication_data";
    public static final String EXTRA_COMPLICATION_ID = "complication_id";
    public static final String EXTRA_INDICATOR_STATUS = "indicator_status";
    public static final String EXTRA_INTERRUPTION_FILTER = "interruption_filter";
    public static final String EXTRA_NOTIFICATION_COUNT = "notification_count";
    public static final String EXTRA_TAP_TIME = "tap_time";
    public static final String EXTRA_UNREAD_COUNT = "unread_count";
    public static final String EXTRA_WATCH_FACE_VISIBLE = "watch_face_visible";
    public static final int INTERRUPTION_FILTER_ALARMS = 4;
    public static final int INTERRUPTION_FILTER_ALL = 1;
    public static final int INTERRUPTION_FILTER_NONE = 3;
    public static final int INTERRUPTION_FILTER_PRIORITY = 2;
    public static final int INTERRUPTION_FILTER_UNKNOWN = 0;
    public static final String PROPERTY_BURN_IN_PROTECTION = "burn_in_protection";
    public static final String PROPERTY_IN_RETAIL_MODE = "in_retail_mode";
    public static final String PROPERTY_LOW_BIT_AMBIENT = "low_bit_ambient";
    public static final String STATUS_INTERRUPTION_FILTER = "interruption_filter";
    private static final long SURFACE_DRAW_TIMEOUT_MS = 100;
    private static final String TAG = "WatchFaceService";
    public static final int TAP_TYPE_TAP = 2;
    public static final int TAP_TYPE_TOUCH = 0;
    public static final int TAP_TYPE_TOUCH_CANCEL = 1;
    public static final String STATUS_CHARGING = "charging";
    public static final String STATUS_AIRPLANE_MODE = "airplane_mode";
    public static final String STATUS_CONNECTED = "connected";
    public static final String STATUS_THEATER_MODE = "theater_mode";
    public static final String STATUS_GPS_ACTIVE = "gps_active";
    private static final String[] STATUS_KEYS = {STATUS_CHARGING, STATUS_AIRPLANE_MODE, STATUS_CONNECTED, STATUS_THEATER_MODE, STATUS_GPS_ACTIVE, "interruption_filter"};

    @Retention(RetentionPolicy.SOURCE)
    public @interface TapType {
    }

    @Override // android.service.wallpaper.WallpaperService
    public abstract Engine onCreateEngine();

    public abstract class Engine extends WallpaperService.Engine {
        private int[] mActiveComplicationsPending;
        private final IntentFilter mAmbientTimeTickFilter;
        private PowerManager.WakeLock mAmbientUpdateWakelock;
        private boolean mComplicationsActivated;
        private final SparseArray<ProviderConfig> mDefaultProviderConfigsPending;
        private boolean mInAmbientMode;
        private final IntentFilter mInteractiveTimeTickFilter;
        private int mInterruptionFilter;
        private int[] mLastActiveComplicationsPending;
        private Bundle mLastStatusBundle;
        private WatchFaceStyle mLastWatchFaceStyle;
        private int mNotificationCount;
        private final Rect mPeekCardPosition;
        private final BroadcastReceiver mTimeTickReceiver;
        private boolean mTimeTickRegistered;
        private int mUnreadCount;
        private IWatchFaceService mWatchFaceService;
        private WatchFaceStyle mWatchFaceStyle;

        @RequiresPermission("android.permission.WAKE_LOCK")
        public Engine() {
            super(WatchFaceService.this);
            this.mTimeTickReceiver = new BroadcastReceiver() { // from class: android.support.wearable.watchface.WatchFaceService.Engine.1
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    if (Log.isLoggable(WatchFaceService.TAG, 3)) {
                        String strValueOf = String.valueOf(intent);
                        Log.d(WatchFaceService.TAG, new StringBuilder(String.valueOf(strValueOf).length() + 46).append("Received intent that triggers onTimeTick for: ").append(strValueOf).toString());
                    }
                    Engine.this.onTimeTick();
                }
            };
            this.mTimeTickRegistered = false;
            this.mDefaultProviderConfigsPending = new SparseArray<>();
            this.mComplicationsActivated = false;
            this.mPeekCardPosition = new Rect(0, 0, 0, 0);
            this.mAmbientTimeTickFilter = new IntentFilter();
            this.mAmbientTimeTickFilter.addAction("android.intent.action.DATE_CHANGED");
            this.mAmbientTimeTickFilter.addAction("android.intent.action.TIME_SET");
            this.mAmbientTimeTickFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
            this.mInteractiveTimeTickFilter = new IntentFilter(this.mAmbientTimeTickFilter);
            this.mInteractiveTimeTickFilter.addAction("android.intent.action.TIME_TICK");
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        @CallSuper
        public Bundle onCommand(String action, int x, int y, int z, Bundle extras, boolean resultRequested) {
            if (Log.isLoggable(WatchFaceService.TAG, 3)) {
                String strValueOf = String.valueOf(action);
                Log.d(WatchFaceService.TAG, strValueOf.length() != 0 ? "received command: ".concat(strValueOf) : new String("received command: "));
            }
            if (WatchFaceService.COMMAND_BACKGROUND_ACTION.equals(action)) {
                maybeUpdateAmbientMode(extras);
                maybeUpdateInterruptionFilter(extras);
                maybeUpdatePeekCardPosition(extras);
                maybeUpdateUnreadCount(extras);
                maybeUpdateNotificationCount(extras);
                maybeUpdateStatus(extras);
                return null;
            }
            if (WatchFaceService.COMMAND_AMBIENT_UPDATE.equals(action)) {
                if (this.mInAmbientMode) {
                    if (Log.isLoggable(WatchFaceService.TAG, 3)) {
                        Log.d(WatchFaceService.TAG, "ambient mode update");
                    }
                    this.mAmbientUpdateWakelock.acquire();
                    onTimeTick();
                    this.mAmbientUpdateWakelock.acquire(WatchFaceService.SURFACE_DRAW_TIMEOUT_MS);
                    return null;
                }
                return null;
            }
            if (WatchFaceService.COMMAND_SET_PROPERTIES.equals(action)) {
                onPropertiesChanged(extras);
                return null;
            }
            if (WatchFaceService.COMMAND_SET_BINDER.equals(action)) {
                onSetBinder(extras);
                return null;
            }
            if (WatchFaceService.COMMAND_REQUEST_STYLE.equals(action)) {
                if (this.mLastWatchFaceStyle != null) {
                    setWatchFaceStyle(this.mLastWatchFaceStyle);
                } else if (Log.isLoggable(WatchFaceService.TAG, 3)) {
                    Log.d(WatchFaceService.TAG, "Last watch face style is null.");
                }
                if (this.mLastActiveComplicationsPending != null) {
                    setActiveComplications(this.mLastActiveComplicationsPending);
                    return null;
                }
                return null;
            }
            if (WatchFaceService.COMMAND_TOUCH.equals(action) || WatchFaceService.COMMAND_TOUCH_CANCEL.equals(action) || WatchFaceService.COMMAND_TAP.equals(action)) {
                long tapTime = extras.getLong(WatchFaceService.EXTRA_TAP_TIME);
                int tapType = 0;
                if (WatchFaceService.COMMAND_TOUCH_CANCEL.equals(action)) {
                    tapType = 1;
                } else if (WatchFaceService.COMMAND_TAP.equals(action)) {
                    tapType = 2;
                }
                onTapCommand(tapType, x, y, tapTime);
                return null;
            }
            if (WatchFaceService.COMMAND_COMPLICATION_DATA.equals(action)) {
                extras.setClassLoader(ComplicationData.class.getClassLoader());
                onComplicationDataUpdate(extras.getInt(WatchFaceService.EXTRA_COMPLICATION_ID), (ComplicationData) extras.getParcelable(WatchFaceService.EXTRA_COMPLICATION_DATA));
                return null;
            }
            return null;
        }

        private void onSetBinder(Bundle extras) {
            IBinder binder = extras.getBinder(WatchFaceService.EXTRA_BINDER);
            if (binder != null) {
                this.mWatchFaceService = IWatchFaceService.Stub.asInterface(binder);
                if (this.mWatchFaceStyle != null) {
                    try {
                        this.mWatchFaceService.setStyle(this.mWatchFaceStyle);
                        this.mWatchFaceStyle = null;
                    } catch (RemoteException e) {
                        Log.w(WatchFaceService.TAG, "Failed to set WatchFaceStyle", e);
                    }
                }
                if (this.mActiveComplicationsPending != null) {
                    doSetActiveComplications(this.mActiveComplicationsPending);
                    if (Log.isLoggable(WatchFaceService.TAG, 3)) {
                        String strValueOf = String.valueOf(Arrays.toString(this.mActiveComplicationsPending));
                        Log.d(WatchFaceService.TAG, strValueOf.length() != 0 ? "onSetBinder set active complications to ".concat(strValueOf) : new String("onSetBinder set active complications to "));
                    }
                    this.mActiveComplicationsPending = null;
                }
                doSetPendingDefaultComplicationProviders();
                return;
            }
            Log.w(WatchFaceService.TAG, "Binder is null.");
        }

        public void setWatchFaceStyle(WatchFaceStyle watchFaceStyle) {
            if (Log.isLoggable(WatchFaceService.TAG, 3)) {
                String strValueOf = String.valueOf(watchFaceStyle);
                Log.d(WatchFaceService.TAG, new StringBuilder(String.valueOf(strValueOf).length() + 18).append("setWatchFaceStyle ").append(strValueOf).toString());
            }
            this.mWatchFaceStyle = watchFaceStyle;
            this.mLastWatchFaceStyle = watchFaceStyle;
            if (this.mWatchFaceService != null) {
                try {
                    this.mWatchFaceService.setStyle(watchFaceStyle);
                    this.mWatchFaceStyle = null;
                } catch (RemoteException e) {
                    Log.e(WatchFaceService.TAG, "Failed to set WatchFaceStyle: ", e);
                }
            }
        }

        public void setActiveComplications(int... watchFaceComplicationIds) {
            if (Log.isLoggable(WatchFaceService.TAG, 3)) {
                String strValueOf = String.valueOf(Arrays.toString(watchFaceComplicationIds));
                Log.d(WatchFaceService.TAG, strValueOf.length() != 0 ? "setActiveComplications ".concat(strValueOf) : new String("setActiveComplications "));
            }
            this.mActiveComplicationsPending = watchFaceComplicationIds;
            this.mLastActiveComplicationsPending = watchFaceComplicationIds;
            if (this.mWatchFaceService != null) {
                doSetActiveComplications(watchFaceComplicationIds);
                this.mActiveComplicationsPending = null;
            } else if (Log.isLoggable(WatchFaceService.TAG, 3)) {
                Log.d(WatchFaceService.TAG, "Could not set active complications as mWatchFaceService is null.");
            }
        }

        public void setDefaultSystemComplicationProvider(int watchFaceComplicationId, int systemProvider, int type) {
            if (Log.isLoggable(WatchFaceService.TAG, 3)) {
                Log.d(WatchFaceService.TAG, new StringBuilder(72).append("setDefaultSystemComplicationProvider ").append(watchFaceComplicationId).append(",").append(systemProvider).append(",").append(type).toString());
            }
            this.mDefaultProviderConfigsPending.put(watchFaceComplicationId, new ProviderConfig(systemProvider, type));
            if (this.mWatchFaceService != null) {
                doSetPendingDefaultComplicationProviders();
            } else if (Log.isLoggable(WatchFaceService.TAG, 3)) {
                Log.d(WatchFaceService.TAG, "Could not set default provider as mWatchFaceService is null.");
            }
        }

        public void setDefaultComplicationProvider(int watchFaceComplicationId, ComponentName provider, int type) {
            if (Log.isLoggable(WatchFaceService.TAG, 3)) {
                String strValueOf = String.valueOf(provider);
                Log.d(WatchFaceService.TAG, new StringBuilder(String.valueOf(strValueOf).length() + 55).append("setDefaultComplicationProvider ").append(watchFaceComplicationId).append(",").append(strValueOf).append(",").append(type).toString());
            }
            this.mDefaultProviderConfigsPending.put(watchFaceComplicationId, new ProviderConfig(provider, type));
            if (this.mWatchFaceService != null) {
                doSetPendingDefaultComplicationProviders();
            } else if (Log.isLoggable(WatchFaceService.TAG, 3)) {
                Log.d(WatchFaceService.TAG, "Could not set default provider as mWatchFaceService is null.");
            }
        }

        public void onAmbientModeChanged(boolean inAmbientMode) {
        }

        public void onInterruptionFilterChanged(int interruptionFilter) {
        }

        @Deprecated
        public void onPeekCardPositionUpdate(Rect rect) {
        }

        public void onUnreadCountChanged(int count) {
        }

        public void onNotificationCountChanged(int count) {
        }

        public void onPropertiesChanged(Bundle properties) {
        }

        public void onStatusChanged(Bundle status) {
        }

        public void onTimeTick() {
        }

        public void onTapCommand(int tapType, int x, int y, long eventTime) {
        }

        public void onComplicationDataUpdate(int watchFaceComplicationId, ComplicationData data) {
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        @CallSuper
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);
            this.mWatchFaceStyle = new WatchFaceStyle.Builder(WatchFaceService.this).build();
            this.mAmbientUpdateWakelock = ((PowerManager) WatchFaceService.this.getSystemService("power")).newWakeLock(1, "WatchFaceService[AmbientUpdate]");
            this.mAmbientUpdateWakelock.setReferenceCounted(false);
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        @CallSuper
        public void onDestroy() {
            if (this.mTimeTickRegistered) {
                this.mTimeTickRegistered = false;
                WatchFaceService.this.unregisterReceiver(this.mTimeTickReceiver);
            }
            super.onDestroy();
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        @CallSuper
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if (Log.isLoggable(WatchFaceService.TAG, 3)) {
                Log.d(WatchFaceService.TAG, new StringBuilder(26).append("onVisibilityChanged: ").append(visible).toString());
            }
            Intent intent = new Intent(WatchFaceService.ACTION_REQUEST_STATE);
            intent.putExtra(WatchFaceService.EXTRA_WATCH_FACE_VISIBLE, visible);
            WatchFaceService.this.sendBroadcast(intent);
            updateTimeTickReceiver();
        }

        public final boolean isInAmbientMode() {
            return this.mInAmbientMode;
        }

        public final int getInterruptionFilter() {
            return this.mInterruptionFilter;
        }

        public final int getUnreadCount() {
            return this.mUnreadCount;
        }

        public final int getNotificationCount() {
            return this.mNotificationCount;
        }

        @Deprecated
        public final Rect getPeekCardPosition() {
            return this.mPeekCardPosition;
        }

        private void maybeUpdateInterruptionFilter(Bundle bundle) {
            int interruptionFilter;
            if (bundle.containsKey("interruption_filter") && (interruptionFilter = bundle.getInt("interruption_filter", 1)) != this.mInterruptionFilter) {
                this.mInterruptionFilter = interruptionFilter;
                onInterruptionFilterChanged(interruptionFilter);
            }
        }

        private void maybeUpdatePeekCardPosition(Bundle bundle) {
            if (bundle.containsKey(WatchFaceService.EXTRA_CARD_LOCATION)) {
                Rect rect = Rect.unflattenFromString(bundle.getString(WatchFaceService.EXTRA_CARD_LOCATION));
                if (!rect.equals(this.mPeekCardPosition)) {
                    this.mPeekCardPosition.set(rect);
                    onPeekCardPositionUpdate(rect);
                }
            }
        }

        private void maybeUpdateAmbientMode(Bundle bundle) {
            boolean inAmbientMode;
            if (bundle.containsKey(WatchFaceService.EXTRA_AMBIENT_MODE) && this.mInAmbientMode != (inAmbientMode = bundle.getBoolean(WatchFaceService.EXTRA_AMBIENT_MODE, false))) {
                this.mInAmbientMode = inAmbientMode;
                dispatchAmbientModeChanged();
            }
        }

        private void dispatchAmbientModeChanged() {
            if (Log.isLoggable(WatchFaceService.TAG, 3)) {
                Log.d(WatchFaceService.TAG, new StringBuilder(33).append("dispatchAmbientModeChanged: ").append(this.mInAmbientMode).toString());
            }
            onAmbientModeChanged(this.mInAmbientMode);
            updateTimeTickReceiver();
        }

        private void maybeUpdateUnreadCount(Bundle bundle) {
            int unreadCount;
            if (bundle.containsKey(WatchFaceService.EXTRA_UNREAD_COUNT) && (unreadCount = bundle.getInt(WatchFaceService.EXTRA_UNREAD_COUNT, 0)) != this.mUnreadCount) {
                this.mUnreadCount = unreadCount;
                onUnreadCountChanged(this.mUnreadCount);
            }
        }

        private void maybeUpdateNotificationCount(Bundle bundle) {
            int notificationCount;
            if (bundle.containsKey(WatchFaceService.EXTRA_NOTIFICATION_COUNT) && (notificationCount = bundle.getInt(WatchFaceService.EXTRA_NOTIFICATION_COUNT, 0)) != this.mNotificationCount) {
                this.mNotificationCount = notificationCount;
                onNotificationCountChanged(this.mNotificationCount);
            }
        }

        private void maybeUpdateStatus(Bundle bundle) {
            Bundle statusBundle = bundle.getBundle(WatchFaceService.EXTRA_INDICATOR_STATUS);
            if (statusBundle != null) {
                if (this.mLastStatusBundle == null || !sameStatus(statusBundle, this.mLastStatusBundle)) {
                    this.mLastStatusBundle = new Bundle(statusBundle);
                    onStatusChanged(statusBundle);
                }
            }
        }

        private void updateTimeTickReceiver() {
            if (Log.isLoggable(WatchFaceService.TAG, 3)) {
                boolean z = this.mTimeTickRegistered;
                boolean zIsVisible = isVisible();
                Log.d(WatchFaceService.TAG, new StringBuilder(47).append("updateTimeTickReceiver: ").append(z).append(" -> (").append(zIsVisible).append(", ").append(this.mInAmbientMode).append(")").toString());
            }
            if (this.mTimeTickRegistered) {
                WatchFaceService.this.unregisterReceiver(this.mTimeTickReceiver);
                this.mTimeTickRegistered = false;
            }
            if (isVisible()) {
                if (this.mInAmbientMode) {
                    WatchFaceService.this.registerReceiver(this.mTimeTickReceiver, this.mAmbientTimeTickFilter);
                } else {
                    WatchFaceService.this.registerReceiver(this.mTimeTickReceiver, this.mInteractiveTimeTickFilter);
                }
                this.mTimeTickRegistered = true;
                onTimeTick();
            }
        }

        private boolean sameStatus(Bundle bundle0, Bundle bundle1) {
            for (String key : WatchFaceService.STATUS_KEYS) {
                if (!Objects.equals(bundle0.get(key), bundle1.get(key))) {
                    return false;
                }
            }
            return true;
        }

        private void doSetActiveComplications(int[] ids) {
            try {
                this.mWatchFaceService.setActiveComplications(ids, this.mComplicationsActivated ? false : true);
                this.mComplicationsActivated = true;
            } catch (RemoteException e) {
                Log.e(WatchFaceService.TAG, "Failed to set active complications: ", e);
            }
        }

        private void doSetPendingDefaultComplicationProviders() {
            if (Log.isLoggable(WatchFaceService.TAG, 3)) {
                Log.d(WatchFaceService.TAG, "doSetPendingDefaultComplicationProviders");
            }
            for (int i = 0; i < this.mDefaultProviderConfigsPending.size(); i++) {
                try {
                    int watchFaceComplicationId = this.mDefaultProviderConfigsPending.keyAt(i);
                    ProviderConfig config = this.mDefaultProviderConfigsPending.valueAt(i);
                    if (config.systemProvider == -1) {
                        this.mWatchFaceService.setDefaultComplicationProvider(watchFaceComplicationId, config.provider, config.type);
                    } else {
                        this.mWatchFaceService.setDefaultSystemComplicationProvider(watchFaceComplicationId, config.systemProvider, config.type);
                    }
                } catch (RemoteException e) {
                    Log.e(WatchFaceService.TAG, "Failed to set default complication providers: ", e);
                    return;
                }
            }
            this.mDefaultProviderConfigsPending.clear();
        }
    }

    private static class ProviderConfig {
        private static final int NONE = -1;
        public final ComponentName provider;
        public final int systemProvider;
        public final int type;

        public ProviderConfig(ComponentName provider, int type) {
            this.provider = provider;
            this.systemProvider = -1;
            this.type = type;
        }

        public ProviderConfig(int systemProvider, int type) {
            this.systemProvider = systemProvider;
            this.provider = null;
            this.type = type;
        }
    }
}
