package com.kopin.solos.share.underarmour;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.webkit.CookieManager;
import com.kopin.solos.share.Platforms;
import com.kopin.solos.share.R;
import com.kopin.solos.share.ShareHelper;
import com.kopin.solos.storage.Record;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.storage.Shared;
import com.ua.sdk.CreateCallback;
import com.ua.sdk.Ua;
import com.ua.sdk.UaException;
import com.ua.sdk.activitytype.ActivityTypeRef;
import com.ua.sdk.internal.UaProviderImpl;
import com.ua.sdk.internal.net.v7.UrlBuilderImpl;
import com.ua.sdk.privacy.Privacy;
import com.ua.sdk.user.User;
import com.ua.sdk.workout.Workout;
import com.ua.sdk.workout.WorkoutBuilder;
import java.util.Date;
import java.util.TimeZone;

/* JADX INFO: loaded from: classes4.dex */
public class MapMyHelper {
    private static final String CODE = "code=";
    private static final String KEY = "s38gyxapzrc8x6djb3f22qudd4vx2bh6";
    private static final String REDIRECT_URI = "uasdk://mmf.oauth/authorization_callback/";
    private static final String SECRET = "76AMvqteJJFFvpwwMYWQaTW2b37SkyXvZXrHZ8smp6s";
    private static final String TAG = "MapMyHelper";
    private static final String UA_CODE = "oauth_code";
    private static final String UA_PREFS = "ua_prefs";
    private static final String UA_USER = "user";
    private static SharedPreferences mPrefs;
    private static Ua mUA;
    private static final UrlBuilderImpl urlBuilder = new UrlBuilderImpl();

    public static void init(Context context) {
        mUA = Ua.getBuilder().setContext(context).setClientId(KEY).setClientSecret(SECRET).setProvider(new UaProviderImpl(KEY, SECRET, context, true) { // from class: com.kopin.solos.share.underarmour.MapMyHelper.1
            @Override // com.ua.sdk.internal.UaProviderImpl
            public UrlBuilderImpl getUrlBuilder() {
                return MapMyHelper.urlBuilder;
            }
        }).setDebug(true).build();
        mPrefs = context.getSharedPreferences(UA_PREFS, 0);
    }

    public static void login(Activity activity, int requestCode) {
        activity.startActivityForResult(getLoginIntent(activity), requestCode);
    }

    public static void login(Fragment fragment, int requestCode) {
        fragment.startActivityForResult(getLoginIntent(fragment.getActivity()), requestCode);
    }

    private static Intent getLoginIntent(Context context) {
        Intent i = new Intent(context, (Class<?>) MapMyActivity.class);
        i.setData(Uri.parse(mUA.getUserAuthorizationUrl(REDIRECT_URI)));
        i.putExtra(MapMyActivity.EXTRA_CALLBACK_URL, REDIRECT_URI);
        return i;
    }

    public static void onActivityResult(Intent data, final ShareHelper.AuthListener listener) {
        if (data == null) {
            listener.onResult(Platforms.UnderArmour, false, "");
            return;
        }
        String uri = data.getData().toString();
        int index = uri.indexOf(CODE);
        if (index > 0) {
            uri = uri.substring(CODE.length() + index);
        }
        if (uri.contains("&")) {
            uri = uri.substring(0, uri.indexOf("&"));
        }
        String code = uri;
        if (code.isEmpty()) {
            listener.onResult(Platforms.UnderArmour, false, "No auth token");
        } else {
            mPrefs.edit().putString(UA_CODE, code).commit();
            mUA.login(code, new Ua.LoginCallback() { // from class: com.kopin.solos.share.underarmour.MapMyHelper.2
                @Override // com.ua.sdk.Ua.LoginCallback
                public void onLogin(User arg0, UaException arg1) {
                    MapMyHelper.mPrefs.edit().putString("user", arg0.getEmail()).commit();
                    listener.onResult(Platforms.UnderArmour, true, "Logged in");
                }
            });
        }
    }

    public static boolean isLoggedIn() {
        return mUA.isAuthenticated();
    }

    @SuppressLint({"NewApi"})
    public static void logOut() {
        mPrefs.edit().clear().commit();
        mUA.logout(null);
        if (Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().removeAllCookies(null);
        } else {
            CookieManager.getInstance().removeAllCookie();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getUsername() {
        return mPrefs.getString("user", null);
    }

    public static boolean hasAutoShare(Context context) {
        return ShareHelper.hasAutoShare(Platforms.UnderArmour.getAutoSharePrefKey(), context);
    }

    public static boolean isShared(long rideId) {
        return SQLHelper.isRideShared(rideId, Platforms.UnderArmour.getSharedKey(), getUsername());
    }

    public static ShareHelper.ShareTask getShareTask(String id, Context context) {
        return new UAWorkoutShareTask(id, context);
    }

    private static class UAWorkoutShareTask extends ShareHelper.ShareTask {
        ShareHelper.ShareProgress mShareResult;

        protected UAWorkoutShareTask(String id, Context context) {
            super(Platforms.UnderArmour, id, context);
            this.mShareResult = null;
        }

        @Override // com.kopin.solos.share.ShareHelper.ShareTask
        public ShareHelper.ShareProgress doTaskInBackground(final SavedWorkout ride) {
            Context context = getContext();
            sendUpdate(new ShareHelper.ShareProgress(ShareHelper.Status.AUTH, "Preparing"));
            final String username = MapMyHelper.getUsername();
            if (username != null && MapMyHelper.isShared(ride.getId())) {
                return new ShareHelper.ShareProgress(ShareHelper.Status.ALREADY_SHARED, context.getString(R.string.share_mmr_message_already));
            }
            if (!MapMyHelper.isLoggedIn()) {
                return new ShareHelper.ShareProgress(ShareHelper.Status.AUTH_FAIL, context.getString(R.string.share_strava_auth_fail));
            }
            sendUpdate(new ShareHelper.ShareProgress(ShareHelper.Status.PREPARING, context.getString(R.string.share_please_wait)));
            Date startTime = new Date(ride.getActualStartTime());
            sendUpdate(new ShareHelper.ShareProgress(ShareHelper.Status.UPLOADING, context.getString(R.string.share_please_wait)));
            final WorkoutBuilder activity = MapMyHelper.mUA.getWorkoutManager().getWorkoutBuilderCreate().setName(ride.getOrGenerateTitle()).setActivityType(ActivityTypeRef.getBuilder().setActivityTypeId(MapMyHelper.getActivityId(ride)).build()).setCreateTime(startTime).setStartTime(startTime).setTimeZone(TimeZone.getDefault()).setPrivacy(Privacy.Level.FRIENDS).setTotalTime(Double.valueOf(ride.getDuration() / 1000), Double.valueOf(ride.getDuration() / 1000)).setTotalDistance(Double.valueOf(ride.getDistance()));
            ride.foreachRecord(new SavedWorkout.foreachRecordCallback() { // from class: com.kopin.solos.share.underarmour.MapMyHelper.UAWorkoutShareTask.1
                @Override // com.kopin.solos.storage.SavedWorkout.foreachRecordCallback
                public boolean onRecord(Record record) {
                    double stamp = record.getTimestamp() / 1000;
                    if (record.hasSpeed()) {
                        activity.addSpeedEvent(stamp, record.getSpeed());
                    }
                    if (record.hasCadence()) {
                        activity.addCadenceEvent(stamp, (int) record.getCadence());
                    }
                    if (record.hasHeartrate()) {
                        activity.addHeartRateEvent(stamp, record.getHeartrate());
                    }
                    if (record.hasPower()) {
                        activity.addPowerEvent(stamp, record.getPower());
                    }
                    if (record.hasAltitude() && record.hasLocation()) {
                        activity.addPositionEvent(stamp, Double.valueOf(record.getAltitude()), Double.valueOf(record.getLatitude()), Double.valueOf(record.getLongitude()));
                        return true;
                    }
                    return true;
                }
            });
            sendUpdate(new ShareHelper.ShareProgress(ShareHelper.Status.PROCESSING, context.getString(R.string.share_please_wait)));
            final Workout file = activity.build();
            Log.d("result", " ; " + file + ";");
            MapMyHelper.mUA.getWorkoutManager().createWorkout(file, new CreateCallback<Workout>() { // from class: com.kopin.solos.share.underarmour.MapMyHelper.UAWorkoutShareTask.2
                @Override // com.ua.sdk.CreateCallback
                public void onCreated(Workout newWorkout, UaException error) {
                    if (error != null) {
                        UAWorkoutShareTask.this.mShareResult = new ShareHelper.ShareProgress(ShareHelper.Status.NETWORK_ERROR, error.getMessage());
                    } else {
                        Shared shared = Shared.newShare(ride.getId(), Platforms.UnderArmour.getSharedKey(), username, "", ride.getSportType());
                        SQLHelper.addShare(shared);
                        UAWorkoutShareTask.this.mShareResult = new ShareHelper.ShareProgress(ShareHelper.Status.DONE, "Activity successfully uploaded.");
                    }
                    synchronized (file) {
                        file.notify();
                    }
                }
            });
            synchronized (file) {
                try {
                    file.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return this.mShareResult;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getActivityId(SavedWorkout workout) {
        switch (workout.getSportType()) {
            case RUN:
                return "16";
            default:
                return "11";
        }
    }
}
