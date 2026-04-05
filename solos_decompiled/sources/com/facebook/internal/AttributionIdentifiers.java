package com.facebook.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import com.facebook.FacebookException;
import io.fabric.sdk.android.services.common.AdvertisingInfoServiceStrategy;
import java.lang.reflect.Method;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes41.dex */
public class AttributionIdentifiers {
    private static final String ANDROID_ID_COLUMN_NAME = "androidid";
    private static final String ATTRIBUTION_ID_COLUMN_NAME = "aid";
    private static final String ATTRIBUTION_ID_CONTENT_PROVIDER = "com.facebook.katana.provider.AttributionIdProvider";
    private static final String ATTRIBUTION_ID_CONTENT_PROVIDER_WAKIZASHI = "com.facebook.wakizashi.provider.AttributionIdProvider";
    private static final int CONNECTION_RESULT_SUCCESS = 0;
    private static final long IDENTIFIER_REFRESH_INTERVAL_MILLIS = 3600000;
    private static final String LIMIT_TRACKING_COLUMN_NAME = "limit_tracking";
    private static final String TAG = AttributionIdentifiers.class.getCanonicalName();
    private static AttributionIdentifiers recentlyFetchedIdentifiers;
    private String androidAdvertiserId;
    private String androidInstallerPackage;
    private String attributionId;
    private long fetchTime;
    private boolean limitTracking;

    private static AttributionIdentifiers getAndroidId(Context context) {
        AttributionIdentifiers identifiers = getAndroidIdViaReflection(context);
        if (identifiers == null) {
            AttributionIdentifiers identifiers2 = getAndroidIdViaService(context);
            if (identifiers2 == null) {
                return new AttributionIdentifiers();
            }
            return identifiers2;
        }
        return identifiers;
    }

    private static AttributionIdentifiers getAndroidIdViaReflection(Context context) {
        Object advertisingInfo;
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                throw new FacebookException("getAndroidId cannot be called on the main thread.");
            }
            Method isGooglePlayServicesAvailable = Utility.getMethodQuietly("com.google.android.gms.common.GooglePlayServicesUtil", "isGooglePlayServicesAvailable", (Class<?>[]) new Class[]{Context.class});
            if (isGooglePlayServicesAvailable == null) {
                return null;
            }
            Object connectionResult = Utility.invokeMethodQuietly(null, isGooglePlayServicesAvailable, context);
            if (!(connectionResult instanceof Integer) || ((Integer) connectionResult).intValue() != 0) {
                return null;
            }
            Method getAdvertisingIdInfo = Utility.getMethodQuietly("com.google.android.gms.ads.identifier.AdvertisingIdClient", "getAdvertisingIdInfo", (Class<?>[]) new Class[]{Context.class});
            if (getAdvertisingIdInfo != null && (advertisingInfo = Utility.invokeMethodQuietly(null, getAdvertisingIdInfo, context)) != null) {
                Method getId = Utility.getMethodQuietly(advertisingInfo.getClass(), "getId", (Class<?>[]) new Class[0]);
                Method isLimitAdTrackingEnabled = Utility.getMethodQuietly(advertisingInfo.getClass(), "isLimitAdTrackingEnabled", (Class<?>[]) new Class[0]);
                if (getId == null || isLimitAdTrackingEnabled == null) {
                    return null;
                }
                AttributionIdentifiers identifiers = new AttributionIdentifiers();
                identifiers.androidAdvertiserId = (String) Utility.invokeMethodQuietly(advertisingInfo, getId, new Object[0]);
                identifiers.limitTracking = ((Boolean) Utility.invokeMethodQuietly(advertisingInfo, isLimitAdTrackingEnabled, new Object[0])).booleanValue();
                return identifiers;
            }
            return null;
        } catch (Exception e) {
            Utility.logd("android_id", e);
            return null;
        }
    }

    private static AttributionIdentifiers getAndroidIdViaService(Context context) {
        GoogleAdServiceConnection connection = new GoogleAdServiceConnection();
        Intent intent = new Intent(AdvertisingInfoServiceStrategy.GOOGLE_PLAY_SERVICES_INTENT);
        intent.setPackage("com.google.android.gms");
        try {
            if (context.bindService(intent, connection, 1)) {
                GoogleAdInfo adInfo = new GoogleAdInfo(connection.getBinder());
                AttributionIdentifiers identifiers = new AttributionIdentifiers();
                identifiers.androidAdvertiserId = adInfo.getAdvertiserId();
                identifiers.limitTracking = adInfo.isTrackingLimited();
                return identifiers;
            }
        } catch (Exception exception) {
            Utility.logd("android_id", exception);
        } finally {
            context.unbindService(connection);
        }
        return null;
    }

    public static AttributionIdentifiers getAttributionIdentifiers(Context context) {
        if (recentlyFetchedIdentifiers != null && System.currentTimeMillis() - recentlyFetchedIdentifiers.fetchTime < 3600000) {
            return recentlyFetchedIdentifiers;
        }
        AttributionIdentifiers identifiers = getAndroidId(context);
        Cursor c = null;
        try {
            try {
                String[] projection = {ATTRIBUTION_ID_COLUMN_NAME, ANDROID_ID_COLUMN_NAME, LIMIT_TRACKING_COLUMN_NAME};
                Uri providerUri = null;
                if (context.getPackageManager().resolveContentProvider(ATTRIBUTION_ID_CONTENT_PROVIDER, 0) != null) {
                    providerUri = Uri.parse("content://com.facebook.katana.provider.AttributionIdProvider");
                } else if (context.getPackageManager().resolveContentProvider(ATTRIBUTION_ID_CONTENT_PROVIDER_WAKIZASHI, 0) != null) {
                    providerUri = Uri.parse("content://com.facebook.wakizashi.provider.AttributionIdProvider");
                }
                String installerPackageName = getInstallerPackageName(context);
                if (installerPackageName != null) {
                    identifiers.androidInstallerPackage = installerPackageName;
                }
                if (providerUri == null) {
                    AttributionIdentifiers attributionIdentifiersCacheAndReturnIdentifiers = cacheAndReturnIdentifiers(identifiers);
                    if (0 == 0) {
                        return attributionIdentifiersCacheAndReturnIdentifiers;
                    }
                    c.close();
                    return attributionIdentifiersCacheAndReturnIdentifiers;
                }
                Cursor c2 = context.getContentResolver().query(providerUri, projection, null, null, null);
                if (c2 == null || !c2.moveToFirst()) {
                    AttributionIdentifiers attributionIdentifiersCacheAndReturnIdentifiers2 = cacheAndReturnIdentifiers(identifiers);
                    if (c2 == null) {
                        return attributionIdentifiersCacheAndReturnIdentifiers2;
                    }
                    c2.close();
                    return attributionIdentifiersCacheAndReturnIdentifiers2;
                }
                int attributionColumnIndex = c2.getColumnIndex(ATTRIBUTION_ID_COLUMN_NAME);
                int androidIdColumnIndex = c2.getColumnIndex(ANDROID_ID_COLUMN_NAME);
                int limitTrackingColumnIndex = c2.getColumnIndex(LIMIT_TRACKING_COLUMN_NAME);
                identifiers.attributionId = c2.getString(attributionColumnIndex);
                if (androidIdColumnIndex > 0 && limitTrackingColumnIndex > 0 && identifiers.getAndroidAdvertiserId() == null) {
                    identifiers.androidAdvertiserId = c2.getString(androidIdColumnIndex);
                    identifiers.limitTracking = Boolean.parseBoolean(c2.getString(limitTrackingColumnIndex));
                }
                if (c2 != null) {
                    c2.close();
                }
                return cacheAndReturnIdentifiers(identifiers);
            } catch (Exception e) {
                Log.d(TAG, "Caught unexpected exception in getAttributionId(): " + e.toString());
                if (0 == 0) {
                    return null;
                }
                c.close();
                return null;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                c.close();
            }
            throw th;
        }
    }

    private static AttributionIdentifiers cacheAndReturnIdentifiers(AttributionIdentifiers identifiers) {
        identifiers.fetchTime = System.currentTimeMillis();
        recentlyFetchedIdentifiers = identifiers;
        return identifiers;
    }

    public String getAttributionId() {
        return this.attributionId;
    }

    public String getAndroidAdvertiserId() {
        return this.androidAdvertiserId;
    }

    public String getAndroidInstallerPackage() {
        return this.androidInstallerPackage;
    }

    public boolean isTrackingLimited() {
        return this.limitTracking;
    }

    @Nullable
    private static String getInstallerPackageName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        if (packageManager != null) {
            return packageManager.getInstallerPackageName(context.getPackageName());
        }
        return null;
    }

    private static final class GoogleAdServiceConnection implements ServiceConnection {
        private AtomicBoolean consumed;
        private final BlockingQueue<IBinder> queue;

        private GoogleAdServiceConnection() {
            this.consumed = new AtomicBoolean(false);
            this.queue = new LinkedBlockingDeque();
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                this.queue.put(service);
            } catch (InterruptedException e) {
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName name) {
        }

        public IBinder getBinder() throws InterruptedException {
            if (this.consumed.compareAndSet(true, true)) {
                throw new IllegalStateException("Binder already consumed");
            }
            return this.queue.take();
        }
    }

    private static final class GoogleAdInfo implements IInterface {
        private static final int FIRST_TRANSACTION_CODE = 1;
        private static final int SECOND_TRANSACTION_CODE = 2;
        private IBinder binder;

        GoogleAdInfo(IBinder binder) {
            this.binder = binder;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this.binder;
        }

        public String getAdvertiserId() throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try {
                data.writeInterfaceToken(AdvertisingInfoServiceStrategy.AdvertisingInterface.ADVERTISING_ID_SERVICE_INTERFACE_TOKEN);
                this.binder.transact(1, data, reply, 0);
                reply.readException();
                String id = reply.readString();
                return id;
            } finally {
                reply.recycle();
                data.recycle();
            }
        }

        public boolean isTrackingLimited() throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try {
                data.writeInterfaceToken(AdvertisingInfoServiceStrategy.AdvertisingInterface.ADVERTISING_ID_SERVICE_INTERFACE_TOKEN);
                data.writeInt(1);
                this.binder.transact(2, data, reply, 0);
                reply.readException();
                boolean limitAdTracking = reply.readInt() != 0;
                return limitAdTracking;
            } finally {
                reply.recycle();
                data.recycle();
            }
        }
    }
}
