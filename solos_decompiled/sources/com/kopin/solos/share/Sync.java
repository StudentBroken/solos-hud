package com.kopin.solos.share;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import com.kopin.peloton.Failure;
import com.kopin.peloton.Peloton;
import com.kopin.peloton.PelotonPrefs;
import com.kopin.solos.common.Device;
import com.kopin.solos.common.SportType;
import com.kopin.solos.share.peloton.PelotonHelper;
import com.kopin.solos.storage.Bike;
import com.kopin.solos.storage.Coordinate;
import com.kopin.solos.storage.FTP;
import com.kopin.solos.storage.Rider;
import com.kopin.solos.storage.Route;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.SavedRide;
import com.kopin.solos.storage.SavedRides;
import com.kopin.solos.storage.SavedRun;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.storage.ShareMap;
import com.kopin.solos.storage.Shared;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.ICancellable;
import com.kopin.solos.storage.util.Utility;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/* JADX INFO: loaded from: classes4.dex */
public class Sync {
    public static final String KEY_REAUTHENTICATE = "reauthenticate";
    public static final int LOGIN_ACTIVITY_CALLBACK = 77;
    public static final String PROFILE_ID = "profile_id";
    private static final long RETRY_DELAY = 10000;
    private static final String TAG = "Sync";
    private static ConnectionListener mConnectionListener;
    private static Context mContext;
    private static UIListener mProfileCompleteUIListener;
    private static SyncUpdateListener mSyncUpdateListener;
    private static UnSyncedTask resetTask;
    private static UnSyncedTask testTask;
    private static UnSyncedTask uploadTask;
    private static String username = "";
    private static boolean syncDoneForCurrentSession = false;
    private static SyncPolicy syncPolicy = SyncPolicy.FULL;
    private static boolean retrying = false;
    private static final Runnable retryUploadRunnable = new Runnable() { // from class: com.kopin.solos.share.Sync.1
        @Override // java.lang.Runnable
        public void run() {
            Log.i(Sync.TAG, "retryUploadRunnable uploadAll()");
            boolean unused = Sync.retrying = false;
            if (Sync.mEnabled) {
                Sync.uploadAll();
            }
        }
    };
    private static final Runnable retrySyncRunnable = new Runnable() { // from class: com.kopin.solos.share.Sync.2
        @Override // java.lang.Runnable
        public void run() {
            boolean unused = Sync.retrying = false;
            if (Sync.mEnabled) {
                Sync.sync();
            }
        }
    };
    private static final Runnable retryDownloadRunnable = new Runnable() { // from class: com.kopin.solos.share.Sync.3
        @Override // java.lang.Runnable
        public void run() {
            Log.i(Sync.TAG, "retryDownloadRunnable download");
            boolean unused = Sync.retrying = false;
            if (Sync.mEnabled) {
                Sync.download();
            }
        }
    };
    private static int numSyncedItems = 0;
    private static int numUnsyncedItems = 0;
    private static String deviceId = "";
    private static boolean mEnabled = true;
    private static Handler mHandler = new Handler();

    public interface CompleteListener {
        void onComplete();
    }

    public interface ConnectionListener {
        void onConnectionChange(boolean z);
    }

    public interface SyncListener {
        void onFailure();

        void onSuccess();
    }

    public enum SyncPolicy {
        FULL,
        OVERVIEW
    }

    public interface SyncUpdateListener {
        void onComplete();

        void onProgress(int i, int i2);

        void onStart();
    }

    public interface UIListener extends ConnectionListener {
        void onComplete();

        void onStart();
    }

    public static void init(Context context, Device.DeviceType deviceType, Peloton.AuthenticationListener authenticationListener) {
        mContext = context;
        syncPolicy = deviceType == Device.DeviceType.MOBILE ? SyncPolicy.FULL : SyncPolicy.OVERVIEW;
        deviceId = Device.getDeviceId(context, deviceType);
        Log.i(TAG, "Device id = " + deviceId);
        Peloton.init(context.getApplicationContext(), deviceId, authenticationListener);
        Peloton.configure(Config.CLOUD_LIVE, Config.CLOUD_HTTPS);
        syncDoneForCurrentSession = false;
        Log.i(TAG, "init for " + PelotonPrefs.getEmail());
        setUsername(PelotonPrefs.getEmail());
    }

    public static SyncPolicy getSyncPolicy() {
        return syncPolicy;
    }

    public static void setUsername(String usernameEmail) {
        Log.i(TAG, "setUsername " + usernameEmail);
        username = usernameEmail;
    }

    public static String getUsername() {
        return username;
    }

    public static void clear() {
        username = "";
    }

    public static void setSyncUpdateListener(SyncUpdateListener syncUpdateListener) {
        mSyncUpdateListener = syncUpdateListener;
        switch (Config.SYNC_PROVIDER) {
            case Peloton:
                PelotonHelper.setSyncUpdateListener(mSyncUpdateListener);
                break;
        }
    }

    public static void setConnected(boolean connected) {
        if (mProfileCompleteUIListener != null) {
            mProfileCompleteUIListener.onConnectionChange(connected);
        }
        if (mConnectionListener != null) {
            mConnectionListener.onConnectionChange(connected);
        }
    }

    public static void setConnectionListener(ConnectionListener connectionListener) {
        mConnectionListener = connectionListener;
    }

    public static void setProfileUIListener(UIListener completeListener) {
        mProfileCompleteUIListener = completeListener;
        switch (Config.SYNC_PROVIDER) {
            case Peloton:
                PelotonHelper.setProfileUIListener(mProfileCompleteUIListener);
                break;
        }
    }

    public static void setSyncDoneForCurrentSession() {
        syncDoneForCurrentSession = true;
    }

    public static void retryUploads() {
        if (!retrying && mEnabled) {
            retrying = true;
            mHandler.postDelayed(retryUploadRunnable, 10000L);
        }
    }

    public static void retryDownload() {
        if (!retrying && mEnabled) {
            retrying = true;
            mHandler.postDelayed(retryDownloadRunnable, 10000L);
        }
    }

    public static void retrySync() {
        if (!retrying && mEnabled) {
            Log.e(TAG, "sync retry");
            retrying = true;
            mHandler.postDelayed(retrySyncRunnable, 10000L);
        }
    }

    public static void addBike(final Bike solosBike) {
        Log.i(TAG, "Adding Bike " + solosBike.getName());
        SQLHelper.addBike(solosBike, new SQLHelper.AddListener() { // from class: com.kopin.solos.share.Sync.4
            @Override // com.kopin.solos.storage.SQLHelper.AddListener
            public void onAdded(long rowId) {
                Log.i(Sync.TAG, "Added Bike locally " + solosBike.getName() + ", local id = " + rowId);
                if (Utility.isNetworkAvailable(Sync.mContext)) {
                    Sync.uploadAll(true);
                } else {
                    Sync.retryUploads();
                }
            }
        });
    }

    public static void removeBike(Bike solosBike) {
        SQLHelper.hideBike(solosBike);
        if (Utility.isNetworkAvailable(mContext)) {
            switch (Config.SYNC_PROVIDER) {
                case Peloton:
                    PelotonHelper.removeBike(solosBike, new Peloton.RemoveListener() { // from class: com.kopin.solos.share.Sync.5
                        @Override // com.kopin.peloton.Peloton.RemoveListener
                        public void onRemoved() {
                        }

                        @Override // com.kopin.peloton.Peloton.CloudListener
                        public void onFailure(Failure failure, int i, String s) {
                            Prefs.setForceSync(true);
                        }
                    });
                    break;
            }
            return;
        }
        Prefs.setForceSync(true);
    }

    public static void addRoute(Route.Saved solosRoute, List<Coordinate> coords) {
        if (Utility.isNetworkAvailable(mContext)) {
            switch (Config.SYNC_PROVIDER) {
                case Peloton:
                    PelotonHelper.addRoute(solosRoute, coords);
                    break;
            }
        }
    }

    public static void removeRoute(long routeId) {
        removeRoute(routeId, null);
    }

    public static void removeRoute(long routeId, String cloudId) {
        SQLHelper.unSaveRoute(routeId);
        String id = cloudId != null ? cloudId : SQLHelper.getExternalId(routeId, Platforms.Peloton.getSharedKey(), username, Shared.ShareType.ROUTE);
        if (Utility.isNetworkAvailable(mContext)) {
            switch (Config.SYNC_PROVIDER) {
                case Peloton:
                    Peloton.removeRoute(id, new Peloton.RemoveListener() { // from class: com.kopin.solos.share.Sync.6
                        @Override // com.kopin.peloton.Peloton.RemoveListener
                        public void onRemoved() {
                        }

                        @Override // com.kopin.peloton.Peloton.CloudListener
                        public void onFailure(Failure failure, int i, String s) {
                            Prefs.setForceSync(true);
                        }
                    });
                    break;
            }
            return;
        }
        Prefs.setForceSync(true);
    }

    public static void addFTP(FTP ftp) {
        SQLHelper.addFTP(ftp.mValue, ftp.mThresholdType, new SQLHelper.AddListener() { // from class: com.kopin.solos.share.Sync.7
            @Override // com.kopin.solos.storage.SQLHelper.AddListener
            public void onAdded(long rowId) {
                if (Utility.isNetworkAvailable(Sync.mContext)) {
                    Sync.uploadAll(true);
                } else {
                    Sync.retryUploads();
                }
            }
        });
    }

    public static void setProfile(int platformId, Rider rider, long date) {
        SQLHelper.setProfile(platformId, username, PROFILE_ID, rider, date, new SQLHelper.AddListener() { // from class: com.kopin.solos.share.Sync.8
            @Override // com.kopin.solos.storage.SQLHelper.AddListener
            public void onAdded(long rowId) {
                if (Utility.isNetworkAvailable(Sync.mContext)) {
                    Sync.uploadAll(true);
                } else {
                    Sync.retryUploads();
                }
            }
        });
    }

    public static void addRide(SavedWorkout localRide) {
        if (Utility.isNetworkAvailable(mContext)) {
            uploadAll(true);
        }
    }

    public static void removeWorkout(final long localRideId, final SportType sportType) {
        if (Config.SYNC_PROVIDER == Platforms.None) {
            SavedRides.deleteWorkout(sportType, localRideId);
            return;
        }
        SavedRides.prepareDeleteWorkout(sportType, localRideId);
        if (Utility.isNetworkAvailable(mContext)) {
            Peloton.RemoveListener removeListener = new Peloton.RemoveListener() { // from class: com.kopin.solos.share.Sync.9
                @Override // com.kopin.peloton.Peloton.RemoveListener
                public void onRemoved() {
                    SavedRides.deleteWorkout(sportType, localRideId);
                }

                @Override // com.kopin.peloton.Peloton.CloudListener
                public void onFailure(Failure failure, int i, String s) {
                    Prefs.setForceSync(true);
                }
            };
            switch (Config.SYNC_PROVIDER) {
                case Peloton:
                    PelotonHelper.removeCloudWorkout(localRideId, sportType, removeListener);
                    break;
            }
            return;
        }
        Prefs.setForceSync(true);
    }

    public static void updateRide(SavedWorkout localRide) {
        if (Utility.isNetworkAvailable(mContext)) {
            switch (Config.SYNC_PROVIDER) {
                case Peloton:
                    if (localRide instanceof SavedRide) {
                        PelotonHelper.updateRide((SavedRide) localRide);
                    } else if (localRide instanceof SavedRun) {
                        PelotonHelper.updateRun((SavedRun) localRide);
                    }
                    break;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void removeImportedRideIds(List<Long> rides) {
        List<Long> importedRides = new ArrayList<>();
        Iterator<Long> it = rides.iterator();
        while (it.hasNext()) {
            long rideId = it.next().longValue();
            if (SQLHelper.wasImportedExceptPlatform(rideId, Config.SYNC_PROVIDER.getSharedKey())) {
                importedRides.add(Long.valueOf(rideId));
            }
        }
        rides.removeAll(importedRides);
    }

    public static void removeImportedRides(List<SavedWorkout> rides) {
        List<SavedWorkout> importedRides = new ArrayList<>();
        for (SavedWorkout ride : rides) {
            if (SQLHelper.wasImportedExceptPlatform(ride.getId(), Config.SYNC_PROVIDER.getSharedKey())) {
                importedRides.add(ride);
            }
        }
        rides.removeAll(importedRides);
    }

    public static void uploadAll() {
        Log.i(TAG, "uploadAll()");
        uploadAll(false);
    }

    public static void uploadAll(boolean forceUpload) {
        if (Config.SYNC_PROVIDER != Platforms.None && mEnabled) {
            Log.i(TAG, "uploadAll force " + forceUpload);
            if (forceUpload || Prefs.updateCloudLastUploadTime(Config.CLOUD_SYNC_UPLOAD_PERIOD)) {
                uploadTask = new UnSyncedTask(true);
                uploadTask.execute(new Void[0]);
            } else {
                reset();
            }
        }
    }

    public static void reset() {
        Log.i(TAG, "Sync reset()");
        resetTask = new UnSyncedTask(false);
        resetTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    public static class UnSyncedTask extends AsyncTask<Void, Void, Void> implements ICancellable {
        private final CompleteListener mCompleteListener;
        private final boolean mDoUpload;

        public UnSyncedTask(boolean upload) {
            this.mDoUpload = upload;
            this.mCompleteListener = null;
        }

        public UnSyncedTask(boolean upload, CompleteListener completeListener) {
            this.mDoUpload = upload;
            this.mCompleteListener = completeListener;
        }

        @Override // com.kopin.solos.storage.util.ICancellable
        public boolean isActive() {
            return !isCancelled();
        }

        @Override // android.os.AsyncTask
        public Void doInBackground(Void... params) {
            Log.d(Sync.TAG, "UnSyncedTask doInBackground");
            List<Bike> unsyncedBikes = SQLHelper.getUnSyncedBikes(Platforms.Peloton.getSharedKey(), Sync.username, this);
            List<FTP> unsyncedFTPs = SQLHelper.getUnSyncedFTPs(Platforms.Peloton.getSharedKey(), Sync.username, this);
            List<Route.Saved> unsyncedRoutes = SQLHelper.getUnSyncedRoutes(Platforms.Peloton.getSharedKey(), Sync.username, this);
            List<Long> unsyncedTrainingIds = SQLHelper.getUnsyncedTrainingIds(Platforms.Peloton.getSharedKey(), Sync.username, this);
            if (!this.mDoUpload) {
                List<Long> unsyncedRideIds = ShareMap.getUnSyncedRideIds(Platforms.Peloton.getSharedKey(), Sync.username, this);
                Sync.removeImportedRideIds(unsyncedRideIds);
                int unused = Sync.numSyncedItems = 0;
                int unused2 = Sync.numUnsyncedItems = unsyncedBikes.size() + unsyncedFTPs.size() + unsyncedRideIds.size() + unsyncedRoutes.size() + unsyncedTrainingIds.size();
                if (this.mCompleteListener != null) {
                    this.mCompleteListener.onComplete();
                    return null;
                }
                return null;
            }
            List<SavedWorkout> unsyncedWorkouts = ShareMap.getUnSyncedRides(Platforms.Peloton.getSharedKey(), Sync.username, this);
            if (!isCancelled()) {
                Sync.removeImportedRides(unsyncedWorkouts);
                Log.i(Sync.TAG, "sync local bikes to be sent to cloud size " + unsyncedBikes.size());
                Log.i(Sync.TAG, "sync local FTPS to be sent to cloud " + unsyncedFTPs.size());
                Log.i(Sync.TAG, "sync local rides to be sent to cloud " + unsyncedWorkouts.size());
                Log.i(Sync.TAG, "sync local routes to be sent to cloud " + unsyncedRoutes.size());
                int unused3 = Sync.numSyncedItems = 0;
                int unused4 = Sync.numUnsyncedItems = unsyncedBikes.size() + unsyncedFTPs.size() + unsyncedWorkouts.size() + unsyncedRoutes.size() + unsyncedTrainingIds.size();
                if (isCancelled() || !Utility.isNetworkAvailable(Sync.mContext) || !Sync.mEnabled) {
                    if (Sync.mSyncUpdateListener != null) {
                        Sync.mSyncUpdateListener.onProgress(Sync.numSyncedItems, Sync.numUnsyncedItems);
                        return null;
                    }
                    return null;
                }
                switch (Config.SYNC_PROVIDER) {
                    case Peloton:
                        SparseArray<List> uploadItems = new SparseArray<>();
                        uploadItems.put(PelotonHelper.PelotonItem.Bike.ordinal(), unsyncedBikes);
                        uploadItems.put(PelotonHelper.PelotonItem.FTP.ordinal(), unsyncedFTPs);
                        uploadItems.put(PelotonHelper.PelotonItem.Route.ordinal(), unsyncedRoutes);
                        uploadItems.put(PelotonHelper.PelotonItem.Training.ordinal(), unsyncedTrainingIds);
                        uploadItems.put(PelotonHelper.PelotonItem.Workout.ordinal(), unsyncedWorkouts);
                        Log.d(Sync.TAG, "uploadAllItems, has network");
                        PelotonHelper.uploadItems(uploadItems, this.mCompleteListener);
                        break;
                }
                return null;
            }
            return null;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.AsyncTask
        public void onPostExecute(Void result) {
            if (Sync.mSyncUpdateListener != null && Sync.numUnsyncedItems > 0 && !this.mDoUpload) {
                Sync.mSyncUpdateListener.onProgress(Sync.numSyncedItems, Sync.numUnsyncedItems);
            }
        }
    }

    public static void sync() {
        sync(!syncDoneForCurrentSession || Prefs.isForceSync());
    }

    public static void sync(boolean forceSync) {
        sync(forceSync, false);
    }

    public static void sync(boolean forceSync, boolean forceUpload) {
        if (mEnabled) {
            if (Config.SYNC_PROVIDER != Platforms.None && Utility.isNetworkAvailable(mContext) && Prefs.updateCloudLastSyncTime(Config.CLOUD_SYNC_FULL_PERIOD, forceSync)) {
                Log.i(TAG, "sync FULL");
                Prefs.setForceSync(false);
                switch (Config.SYNC_PROVIDER) {
                    case Peloton:
                        PelotonHelper.sync();
                        break;
                }
            }
            Log.i(TAG, "sync uplOAD");
            if (mProfileCompleteUIListener != null) {
                mProfileCompleteUIListener.onComplete();
            }
            uploadAll(forceUpload);
        }
    }

    public static void download() {
        if (Config.SYNC_PROVIDER != Platforms.None) {
            PelotonHelper.download();
        }
    }

    public static int getNumUnsyncedItems() {
        return numUnsyncedItems + PelotonHelper.getNumDownloadingRides();
    }

    public static int getNumUnsyncedItems(boolean includeDownloads) {
        return includeDownloads ? numUnsyncedItems + PelotonHelper.getNumDownloadingRides() : numUnsyncedItems;
    }

    public static int getNumSyncedItems() {
        return numSyncedItems + PelotonHelper.getNumDownloadedRides();
    }

    public static int incSyncedItems() {
        int i = numSyncedItems + 1;
        numSyncedItems = i;
        return i + PelotonHelper.getNumDownloadedRides();
    }

    public static void cancelTasks() {
        Log.i(TAG, "cancelTasks, set sync enabled to false");
        mEnabled = false;
        mHandler.removeCallbacksAndMessages(null);
        if (uploadTask != null) {
            uploadTask.cancel(true);
        }
        if (resetTask != null) {
            resetTask.cancel(true);
        }
        if (testTask != null) {
            testTask.cancel(true);
        }
        ShareHelper.cancelTasks();
        switch (Config.SYNC_PROVIDER) {
            case Peloton:
                PelotonHelper.cancelTasks();
                Peloton.cancelTasks();
                break;
        }
    }

    public static boolean isEnabled() {
        return mEnabled;
    }

    public static void setEnabled(boolean enabled) {
        Log.i(TAG, "setEnabled " + enabled);
        mEnabled = enabled;
    }

    public static void test() {
        if (Config.DEBUG) {
            Random random = new Random();
            for (int i = 0; i < 10; i++) {
                int r = random.nextInt(5000) + 10;
                Bike bike = new Bike("random " + random.nextInt(r), Bike.BikeType.DEFAULT, r, r);
                SQLHelper.addBike(bike, null);
                Log.e(TAG, "bike " + i + ", " + r);
            }
            new Handler().postDelayed(new Runnable() { // from class: com.kopin.solos.share.Sync.10
                @Override // java.lang.Runnable
                public void run() {
                    UnSyncedTask unused = Sync.testTask = new UnSyncedTask(true);
                    Sync.testTask.execute(new Void[0]);
                }
            }, 1800L);
        }
    }

    static void setNumSyncedItems(int numSynced) {
        numSyncedItems = numSynced;
    }

    static void setNumUnsyncedItems(int numUnsynced) {
        numUnsyncedItems = numUnsynced;
    }
}
