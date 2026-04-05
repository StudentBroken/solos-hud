package com.kopin.solos.share;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Looper;
import android.preference.PreferenceManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.kopin.peloton.PelotonPrefs;
import com.kopin.solos.share.facebook.FacebookSharingHelper;
import com.kopin.solos.share.peloton.PelotonActivity;
import com.kopin.solos.share.strava.StravaHelper;
import com.kopin.solos.share.trainingpeaks.TPHelper;
import com.kopin.solos.share.underarmour.MapMyHelper;
import com.kopin.solos.storage.SavedWorkout;
import io.fabric.sdk.android.services.events.EventsFilesManager;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/* JADX INFO: loaded from: classes4.dex */
public class ShareHelper {
    private static ShareTask shareTask;
    private static final HashMap<String, ShareTask> TASKS = new HashMap<>();
    private static final HashMap<String, HashSet<UploadListener>> PENDING_LISTENERS = new HashMap<>();

    public interface AuthListener {
        void onResult(Platforms platforms, boolean z, String str);
    }

    public static abstract class BaseShareHelper {
    }

    public interface ShareProgressListener {
        void onProgress(Platforms platforms, ShareProgress shareProgress);
    }

    public enum Status {
        INIT,
        AUTH,
        AUTH_FAIL,
        ALREADY_SHARED,
        PREPARING,
        UPLOADING,
        PROCESSING,
        UNKNOWN,
        DONE,
        NETWORK_ERROR,
        CANCELED
    }

    public static boolean hasAutoShare(String which, Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getBoolean(which, false);
    }

    public static void upload(Context context, SavedWorkout workout, UploadListener listener, Platforms platform) {
        if (listener != null) {
            listener.enable();
        }
        String key = getKey(workout.getId(), platform.name());
        shareTask = TASKS.get(key);
        if (shareTask == null) {
            shareTask = getShareTask(platform, key, context);
            shareTask.setListeners(getPendingListeners(key));
            shareTask.uploadRide(workout);
            TASKS.put(key, shareTask);
        }
        if (listener != null) {
            shareTask.addListener(listener);
        }
    }

    public static void cancelTasks() {
        if (shareTask != null) {
            shareTask.cancel(true);
        }
        for (ShareTask task : TASKS.values()) {
            if (task != null) {
                task.cancel(true);
            }
        }
    }

    public static void registerListener(long rideId, UploadListener listener) {
        for (Platforms p : Platforms.values()) {
            registerListener(rideId, listener, p);
        }
    }

    public static void registerListener(long rideId, UploadListener listener, Platforms platform) {
        listener.enable();
        String key = getKey(rideId, platform.name());
        ShareTask task = TASKS.get(key);
        if (task != null) {
            task.addListener(listener);
        }
        addPendingListener(key, listener);
    }

    public static void unregisterListener(long rideId, UploadListener listener) {
        for (Platforms p : Platforms.values()) {
            unregisterListener(rideId, listener, p);
        }
    }

    public static void unregisterListener(long rideId, UploadListener listener, Platforms platform) {
        String key = getKey(rideId, platform.name());
        ShareTask task = TASKS.get(key);
        if (task != null) {
            task.removeListener(listener);
        }
        removePendingListener(key, listener);
        listener.remove();
    }

    private static void addPendingListener(String key, UploadListener listener) {
        HashSet<UploadListener> listeners = PENDING_LISTENERS.get(key);
        if (listeners == null) {
            listeners = new HashSet<>();
            PENDING_LISTENERS.put(key, listeners);
        }
        listeners.add(listener);
    }

    private static HashSet<UploadListener> getPendingListeners(String key) {
        HashSet<UploadListener> listeners = PENDING_LISTENERS.get(key);
        return listeners != null ? new HashSet<>(listeners) : new HashSet<>();
    }

    private static void removePendingListener(String key, UploadListener listener) {
        HashSet<UploadListener> listeners = PENDING_LISTENERS.get(key);
        if (listeners != null) {
            listeners.remove(listener);
            if (listeners.isEmpty()) {
                PENDING_LISTENERS.remove(key);
            }
        }
    }

    private static String getKey(long rideId, String service) {
        return rideId + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + service;
    }

    public static void login(Activity activity, Platforms which) {
        switch (which) {
            case Strava:
                StravaHelper.login(activity, which.getRequestCode());
                break;
            case TrainingPeaks:
                TPHelper.login(activity, which.getRequestCode());
                break;
            case UnderArmour:
                MapMyHelper.login(activity, which.getRequestCode());
                break;
            case Peloton:
                activity.startActivityForResult(new Intent(activity, (Class<?>) PelotonActivity.class), which.getRequestCode());
                break;
        }
    }

    public static void login(Fragment frag, Platforms which) {
        switch (which) {
            case Strava:
                StravaHelper.login(frag, which.getRequestCode());
                break;
            case TrainingPeaks:
                TPHelper.login(frag, which.getRequestCode());
                break;
            case UnderArmour:
                MapMyHelper.login(frag, which.getRequestCode());
                break;
            case Peloton:
                frag.startActivityForResult(new Intent(frag.getActivity(), (Class<?>) PelotonActivity.class), which.getRequestCode());
                break;
        }
    }

    public static void logOut(Context context, Platforms which) {
        switch (which) {
            case Strava:
                StravaHelper.logOut(context);
                break;
            case TrainingPeaks:
                TPHelper.logout(context);
                break;
            case UnderArmour:
                MapMyHelper.logOut();
                break;
            case Peloton:
                PelotonPrefs.clearSession(context);
                break;
            case Facebook:
                FacebookSdk.sdkInitialize(context);
                LoginManager.getInstance().logOut();
                break;
        }
    }

    public static void logoutAll(Context context) {
        for (Platforms platform : Platforms.values()) {
            logOut(context, platform);
        }
    }

    public static void logoutAll(Context context, Platforms exceptPlatform) {
        for (Platforms platform : Platforms.values()) {
            if (!platform.equals(exceptPlatform)) {
                logOut(context, platform);
            }
        }
    }

    public static boolean onLoginResult(Context context, int req, int result, Intent data, AuthListener cb) {
        for (Platforms p : Platforms.values()) {
            if (req == p.getRequestCode()) {
                if (result == -1) {
                    p.onActivityResult(context, data, cb);
                } else {
                    cb.onResult(p, false, "Please login to be able to share.");
                }
                return true;
            }
        }
        return false;
    }

    private static ShareTask getShareTask(Platforms platform, String id, Context context) {
        switch (platform) {
            case Strava:
                return StravaHelper.getShareTask(id, context);
            case TrainingPeaks:
                return TPHelper.getShareTask(id, context);
            case UnderArmour:
                return MapMyHelper.getShareTask(id, context);
            case Peloton:
            case FileExport:
            default:
                return null;
            case Facebook:
                return FacebookSharingHelper.getShareTask(id, context);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void removeTask(String id) {
        TASKS.remove(id);
    }

    public static abstract class UploadListener implements ShareProgressListener {
        private boolean mRemoved = false;

        @Override // com.kopin.solos.share.ShareHelper.ShareProgressListener
        public abstract void onProgress(Platforms platforms, ShareProgress shareProgress);

        public boolean isRemoved() {
            return this.mRemoved;
        }

        public void remove() {
            this.mRemoved = true;
        }

        public void enable() {
            this.mRemoved = false;
        }
    }

    public static class ShareProgress {
        public String message;
        public Status status;

        public ShareProgress(Status status, String message) {
            this.status = status;
            this.message = message;
        }

        public String toString() {
            return "ShareProgress {" + this.status + ", " + this.message + "}";
        }
    }

    public static abstract class ShareTask extends AsyncTask<SavedWorkout, ShareProgress, ShareProgress> {
        private Context mContext;
        private String mId;
        private ShareProgress mLastProgress;
        private HashSet<UploadListener> mListeners = new HashSet<>();
        private final Platforms mWhich;

        public abstract ShareProgress doTaskInBackground(SavedWorkout savedWorkout);

        protected ShareTask(Platforms which, String id, Context context) {
            this.mWhich = which;
            this.mId = id;
            this.mContext = context;
            if (context == null) {
                throw new NullPointerException("null context");
            }
        }

        public Context getContext() {
            return this.mContext;
        }

        public final void addListener(UploadListener listener) {
            if (Thread.currentThread().getId() != Looper.getMainLooper().getThread().getId()) {
                throw new RuntimeException("Use main thread to add listeners");
            }
            if (this.mLastProgress != null) {
                listener.onProgress(this.mWhich, this.mLastProgress);
            }
            this.mListeners.add(listener);
        }

        public final void setListeners(HashSet<UploadListener> listeners) {
            if (listeners == null) {
                return;
            }
            if (Thread.currentThread().getId() != Looper.getMainLooper().getThread().getId()) {
                throw new RuntimeException("Use main thread to add listeners");
            }
            this.mListeners = listeners;
        }

        public final void removeListener(UploadListener listener) {
            if (Thread.currentThread().getId() != Looper.getMainLooper().getThread().getId()) {
                throw new RuntimeException("Use main thread to remove listeners");
            }
            this.mListeners.remove(listener);
        }

        public final void sendUpdate(ShareProgress progress) {
            publishProgress(progress);
        }

        public final void uploadRide(SavedWorkout ride) {
            execute(ride);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public final ShareProgress doInBackground(SavedWorkout... params) {
            if (params.length != 1) {
                throw new RuntimeException("Use uploadRide to load the ride");
            }
            return doTaskInBackground(params[0]);
        }

        @Override // android.os.AsyncTask
        protected final void onPreExecute() {
            sendUpdate(new ShareProgress(Status.INIT, ""));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public final void onPostExecute(ShareProgress shareProgress) {
            ShareHelper.removeTask(this.mId);
            pushUpdate(shareProgress);
            this.mListeners.clear();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public final void onProgressUpdate(ShareProgress... values) {
            if (values.length != 1) {
                throw new RuntimeException("Use sendUpdate to post updates to listeners!");
            }
            pushUpdate(values[0]);
        }

        private void pushUpdate(ShareProgress progress) {
            this.mLastProgress = progress;
            Iterator<UploadListener> iterator = this.mListeners.iterator();
            while (iterator.hasNext()) {
                UploadListener listener = iterator.next();
                if (!listener.isRemoved()) {
                    listener.onProgress(this.mWhich, progress);
                } else {
                    iterator.remove();
                }
            }
        }
    }
}
