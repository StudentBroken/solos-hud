package com.kopin.solos.share.strava;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.kopin.solos.common.SportType;
import com.kopin.solos.share.Platforms;
import com.kopin.solos.share.R;
import com.kopin.solos.share.ShareHelper;
import com.kopin.solos.share.TCXGenerator;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.SavedRide;
import com.kopin.solos.storage.SavedRun;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.storage.Shared;
import com.kopin.solos.storage.util.Utility;
import com.ua.sdk.datapoint.BaseDataTypes;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jstrava.authenticator.AuthResponse;
import org.jstrava.authenticator.StravaAuthenticator;
import org.jstrava.connector.JStravaV3;
import org.jstrava.entities.VCTrakFile;
import org.jstrava.entities.activity.Activity;
import org.jstrava.entities.activity.UploadStatus;
import org.jstrava.entities.athlete.Athlete;
import org.jstrava.entities.stream.Stream;
import org.jstrava.utility.MultipartUtility;

/* JADX INFO: loaded from: classes4.dex */
public class StravaHelper {
    private static final String CODE = "code=";
    private static final int MIN_NUM_STRAVA_ITEMS_SHOWN = 16;
    private static final int NUM_STRAVA_RIDES_PER_PAGE = 64;
    private static final String SECRET = "7caf384c32921aa14daf61007e8b71b4c48b5c82";
    private static final String STRAVA_AUTH = "strava_auth";
    private static final String STRAVA_CODE = "strava_code";
    private static final String STRAVA_RESPONSE = "strava_athlete";
    private static final String STRAVA_TOKEN = "strava_token";
    private static final String TAG = "StravaHelper";
    private static final long UPLOAD_STATUS_INTERVAL = 4000;
    private static Handler handler;
    public static JStravaV3 jStravaV3;
    private static Context mContext;
    private static AuthResponse mResponse;
    private static long threadId;
    private static final int CLIENT_ID = 14376;
    private static final String REDIRECT_URI = "http://solos-wearables.com";
    private static StravaAuthenticator authenticator = new StravaAuthenticator(CLIENT_ID, REDIRECT_URI);

    public static class StravaActivitySet {
        public List<Activity> activities = new ArrayList();
        public int page;
    }

    public enum ActivityType {
        Ride(SportType.RIDE),
        VirtualRide(SportType.RIDE),
        Run(SportType.RUN),
        Swim,
        Workout;

        private SportType mSportType;

        ActivityType(SportType sportType) {
            this.mSportType = sportType;
        }

        static List<String> getSportTypes(SportType sportType) {
            List<String> list = new ArrayList<>();
            for (ActivityType activityType : values()) {
                if (activityType.mSportType == sportType) {
                    list.add(activityType.name().toUpperCase());
                }
            }
            return list;
        }
    }

    static {
        authenticator.setSecrete(SECRET);
        HandlerThread ht = new HandlerThread(TAG);
        ht.start();
        threadId = ht.getLooper().getThread().getId();
        handler = new Handler(ht.getLooper());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getStravaActivityType(SavedWorkout workout) {
        if (workout instanceof SavedRide) {
            return ActivityType.Ride.name();
        }
        if (workout instanceof SavedRun) {
            return ActivityType.Run.name();
        }
        return ActivityType.Workout.name();
    }

    public static void init(Context context) {
        mContext = context;
        if (Build.VERSION.SDK_INT < 21) {
            CookieSyncManager.createInstance(mContext);
        }
        readSavedResponse(context);
    }

    public static void login(android.app.Activity activity, int requestCode) {
        activity.startActivityForResult(getLoginIntent(activity), requestCode);
    }

    public static void login(Fragment fragment, int requestCode) {
        fragment.startActivityForResult(getLoginIntent(fragment.getActivity()), requestCode);
    }

    private static Intent getLoginIntent(Context context) {
        Uri urv = Uri.parse(authenticator.getRequestAccessUrl("auto", true, true, "start"));
        Log.i(TAG, urv.toString());
        Intent intent = new Intent(context, (Class<?>) StravaWebView.class);
        intent.setData(urv);
        intent.putExtra(StravaWebView.CALLBACK, REDIRECT_URI);
        return intent;
    }

    public static void onActivityResult(Context context, Intent data, ShareHelper.AuthListener listener) {
        if (data == null) {
            if (listener != null) {
                listener.onResult(Platforms.Strava, false, "");
            }
        } else {
            String uri = data.getData().toString();
            parseWebResult(context, uri, listener);
        }
    }

    public static void parseWebResult(final Context context, String uri, final ShareHelper.AuthListener listener) {
        int index = uri.indexOf(CODE);
        if (index > 0) {
            uri = uri.substring(CODE.length() + index);
        }
        if (uri.contains("&")) {
            uri = uri.substring(0, uri.indexOf("&"));
        }
        final String code = uri;
        setCode(context, code);
        handler.post(new Runnable() { // from class: com.kopin.solos.share.strava.StravaHelper.1
            @Override // java.lang.Runnable
            public void run() {
                StravaHelper.refreshAuthResponse(context, code, listener);
            }
        });
    }

    public static void refreshAuthResponse(final Context context, final ShareHelper.AuthListener listener) {
        String code = getCode(context);
        if (code == null || code.trim().isEmpty()) {
            if (listener != null) {
                listener.onResult(Platforms.Strava, false, "Not authenticated");
            }
        } else if (Thread.currentThread().getId() != threadId) {
            handler.post(new Runnable() { // from class: com.kopin.solos.share.strava.StravaHelper.2
                @Override // java.lang.Runnable
                public void run() {
                    StravaHelper.refreshAuthResponse(context, StravaHelper.getCode(context), listener);
                }
            });
        } else {
            refreshAuthResponse(context, getCode(context), listener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean refreshAuthResponse(Context context, String code, ShareHelper.AuthListener listener) {
        try {
            mResponse = authenticator.getToken(code);
            if (mResponse != null) {
                setToken(context, mResponse.getAccess_token());
                writeSavedResponse(context);
            }
            if (listener != null) {
                listener.onResult(Platforms.Strava, mResponse != null, null);
            }
            return mResponse != null;
        } catch (Exception e) {
            Log.e(TAG, "", e);
            if (listener != null) {
                listener.onResult(Platforms.Strava, false, e.getMessage());
            }
            return false;
        }
    }

    private static void removeResponse(Context context) {
        mResponse = null;
        SharedPreferences sharedPref = context.getSharedPreferences(STRAVA_AUTH, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(STRAVA_RESPONSE, "");
        editor.apply();
    }

    private static void readSavedResponse(Context context) {
        if (mResponse == null) {
            SharedPreferences sharedPref = context.getSharedPreferences(STRAVA_AUTH, 0);
            String response = sharedPref.getString(STRAVA_RESPONSE, "");
            if (response != null && !response.trim().isEmpty()) {
                Gson gson = new Gson();
                try {
                    mResponse = (AuthResponse) gson.fromJson(response, AuthResponse.class);
                    if (Thread.currentThread().getId() != threadId) {
                        handler.post(new Runnable() { // from class: com.kopin.solos.share.strava.StravaHelper.3
                            @Override // java.lang.Runnable
                            public void run() {
                                StravaHelper.createJStrava(false);
                            }
                        });
                    } else {
                        createJStrava(false);
                    }
                } catch (JsonSyntaxException e) {
                }
            }
        }
    }

    private static void writeSavedResponse(Context context) {
        if (mResponse != null) {
            Gson gson = new Gson();
            SharedPreferences sharedPref = context.getSharedPreferences(STRAVA_AUTH, 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(STRAVA_RESPONSE, gson.toJson(mResponse));
            editor.apply();
            createJStrava(true);
        }
    }

    public static boolean isLoggedIn() {
        return getAthlete() != null;
    }

    @SuppressLint({"NewApi"})
    public static void logOut(Context context) {
        setCode(context, "");
        setToken(context, "");
        removeResponse(context);
        if (Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().removeAllCookies(null);
        } else {
            CookieManager.getInstance().removeAllCookie();
        }
    }

    public static void logoutIfAccessDenied(Context context, Exception e) {
        if ((e instanceof RuntimeException) && e.getMessage() != null && e.getMessage().contains("Failed : HTTP error code : 401") && context != null) {
            logOut(context);
        }
    }

    public static Athlete getAthlete() {
        if (mResponse == null) {
            return null;
        }
        return mResponse.getAthlete();
    }

    public static String getEmail() {
        Athlete athlete = getAthlete();
        if (athlete == null) {
            return null;
        }
        return athlete.getEmail();
    }

    public static String getCode(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(STRAVA_AUTH, 0);
        return sharedPref.getString(STRAVA_CODE, "");
    }

    private static void setCode(Context context, String code) {
        SharedPreferences sharedPref = context.getSharedPreferences(STRAVA_AUTH, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(STRAVA_CODE, code);
        editor.apply();
    }

    public static String getToken(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(STRAVA_AUTH, 0);
        return sharedPref.getString(STRAVA_TOKEN, "");
    }

    private static void setToken(Context context, String token) {
        SharedPreferences sharedPref = context.getSharedPreferences(STRAVA_AUTH, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(STRAVA_TOKEN, token);
        editor.apply();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean createJStrava(boolean force) {
        if (mResponse == null) {
            return false;
        }
        if (jStravaV3 != null && !force) {
            return true;
        }
        try {
            jStravaV3 = new JStravaV3(mResponse.getAccess_token());
            return true;
        } catch (RuntimeException e) {
            e.printStackTrace();
            jStravaV3 = null;
            logoutIfAccessDenied(mContext, e);
            return false;
        }
    }

    public static boolean hasAutoShare(Context context) {
        return ShareHelper.hasAutoShare(Platforms.Strava.getAutoSharePrefKey(), context);
    }

    public static boolean isShared(long rideId) {
        Athlete athlete = getAthlete();
        String username = athlete != null ? athlete.getEmail() : "";
        return SQLHelper.isRideShared(rideId, Platforms.Strava.getSharedKey(), username);
    }

    public static ShareHelper.ShareTask getShareTask(String id, Context context) {
        return new StravaShareTask(id, context);
    }

    private static class StravaShareTask extends ShareHelper.ShareTask {
        protected StravaShareTask(String id, Context context) {
            super(Platforms.Strava, id, context);
        }

        @Override // com.kopin.solos.share.ShareHelper.ShareTask
        public ShareHelper.ShareProgress doTaskInBackground(SavedWorkout ride) {
            Context context = getContext();
            try {
                sendUpdate(new ShareHelper.ShareProgress(ShareHelper.Status.AUTH, "Preparing"));
                if (StravaHelper.getEmail() == null || !StravaHelper.isShared(ride.getId())) {
                    if (StravaHelper.mResponse != null || StravaHelper.refreshAuthResponse(context, StravaHelper.getCode(context), null)) {
                        if (!StravaHelper.createJStrava(false)) {
                            return new ShareHelper.ShareProgress(ShareHelper.Status.AUTH_FAIL, context.getString(R.string.share_strava_init_fail));
                        }
                        if (StravaHelper.isShared(ride.getId())) {
                            return new ShareHelper.ShareProgress(ShareHelper.Status.ALREADY_SHARED, context.getString(R.string.share_strava_message_already));
                        }
                        sendUpdate(new ShareHelper.ShareProgress(ShareHelper.Status.PREPARING, context.getString(R.string.share_please_wait)));
                        TCXGenerator generator = new TCXGenerator(ride);
                        VCTrakFile file = new VCTrakFile("tcx", ride.getOrGenerateTitle(), generator);
                        String startTime = Utility.formatDateAndTimeUpload(ride.getActualStartTime(), true);
                        sendUpdate(new ShareHelper.ShareProgress(ShareHelper.Status.UPLOADING, context.getString(R.string.share_please_wait)));
                        Activity activity = StravaHelper.jStravaV3.createActivity(ride.getOrGenerateTitle(), StravaHelper.getStravaActivityType(ride), startTime, (int) (ride.getDuration() / 1000), (float) ride.getDistance(), file);
                        sendUpdate(new ShareHelper.ShareProgress(ShareHelper.Status.PROCESSING, context.getString(R.string.share_please_wait)));
                        Log.d("result", " ; " + activity + ";");
                        if (activity == null || activity.getResponseType() == MultipartUtility.ResponseType.UNKNOWN) {
                            return new ShareHelper.ShareProgress(ShareHelper.Status.NETWORK_ERROR, context.getString(R.string.share_message_unknown_response));
                        }
                        if (activity.getResponseType() == MultipartUtility.ResponseType.BAD_REQUEST) {
                            Shared shared = new Shared(ride.getId(), Platforms.Strava.getSharedKey(), StravaHelper.getEmail());
                            SQLHelper.addShare(shared);
                            return new ShareHelper.ShareProgress(ShareHelper.Status.ALREADY_SHARED, context.getString(R.string.share_strava_message_already));
                        }
                        if (activity.getResponseType() == MultipartUtility.ResponseType.ACCESS_DENIED) {
                            return new ShareHelper.ShareProgress(ShareHelper.Status.AUTH_FAIL, context.getString(R.string.share_strava_auth_fail));
                        }
                        while (true) {
                            try {
                                Thread.sleep(StravaHelper.UPLOAD_STATUS_INTERVAL);
                            } catch (InterruptedException e) {
                                Log.i("Status checker thread", "This should not be interrupted", e);
                            }
                            UploadStatus status = StravaHelper.jStravaV3.checkUploadStatus(activity.getId());
                            if (status == null) {
                                return new ShareHelper.ShareProgress(ShareHelper.Status.UNKNOWN, context.getString(R.string.share_strava_progress_problem));
                            }
                            Log.d("status", status.json);
                            if (status.getError() != null && !status.getError().trim().isEmpty()) {
                                return new ShareHelper.ShareProgress(ShareHelper.Status.NETWORK_ERROR, status.getError());
                            }
                            if (status.getStatus().startsWith("Your activity is ready")) {
                                Shared shared2 = Shared.newShare(ride.getId(), Platforms.Strava.getSharedKey(), StravaHelper.getEmail(), String.valueOf(activity.getId()), ride.getSportType());
                                SQLHelper.addShare(shared2);
                                return new ShareHelper.ShareProgress(ShareHelper.Status.DONE, "Activity successfully uploaded.");
                            }
                            sendUpdate(new ShareHelper.ShareProgress(ShareHelper.Status.PROCESSING, status.getStatus()));
                        }
                    } else {
                        return new ShareHelper.ShareProgress(ShareHelper.Status.AUTH_FAIL, context.getString(R.string.share_strava_auth_fail));
                    }
                } else {
                    return new ShareHelper.ShareProgress(ShareHelper.Status.ALREADY_SHARED, context.getString(R.string.share_strava_message_already));
                }
            } catch (RuntimeException e2) {
                e2.printStackTrace();
                StravaHelper.logoutIfAccessDenied(StravaHelper.mContext, e2);
                return new ShareHelper.ShareProgress(ShareHelper.Status.UNKNOWN, context.getString(R.string.share_strava_progress_problem));
            }
        }
    }

    public static abstract class StravaListener implements ShareHelper.AuthListener {
        public abstract void onResult(boolean z, String str);

        @Override // com.kopin.solos.share.ShareHelper.AuthListener
        public void onResult(Platforms which, boolean success, String message) {
            if (which == Platforms.Strava) {
                onResult(success, message);
            }
        }
    }

    public static class GetListActivitiesTask extends AsyncTask<Void, Void, StravaActivitySet> {
        private final SportType mSportType;
        List<String> stravaSportTypes;
        StravaActivitySet stravaSet = new StravaActivitySet();
        int numRecursions = 0;

        public GetListActivitiesTask(int page, SportType sportType) {
            this.mSportType = sportType;
            this.stravaSportTypes = ActivityType.getSportTypes(this.mSportType);
            this.stravaSet.page = page;
        }

        @Override // android.os.AsyncTask
        public StravaActivitySet doInBackground(Void... voids) {
            if (StravaHelper.createJStrava(false)) {
                try {
                    Log.i(StravaHelper.TAG, "jstrava page size 64");
                    addFilteredActivities();
                    return this.stravaSet;
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    StravaHelper.logoutIfAccessDenied(StravaHelper.mContext, e);
                }
            }
            return null;
        }

        private void addFilteredActivities() {
            List<Activity> activities = StravaHelper.jStravaV3.getCurrentAthleteActivities(this.stravaSet.page, 64);
            Log.d(StravaHelper.TAG, "strava addFilteredActivities - page num = " + this.stravaSet.page);
            Log.d(StravaHelper.TAG, "strava addFilteredActivities - full batch size = " + activities.size());
            int i = 0;
            for (Activity activity : activities) {
                if (this.stravaSportTypes.contains(activity.getType().toUpperCase())) {
                    this.stravaSet.activities.add(activity);
                    i++;
                }
            }
            Log.d(StravaHelper.TAG, "filtered by sport = " + i);
            if (activities.size() < 64) {
                Log.d(StravaHelper.TAG, "last page from strava");
                this.stravaSet.page = -1;
                return;
            }
            if (this.stravaSet.activities.size() < 16) {
                Log.d(StravaHelper.TAG, "too few filtered items, get another page");
                this.stravaSet.page++;
                this.numRecursions++;
                int minItems = this.stravaSet.page < 2 ? 8 : 1;
                if (this.stravaSet.activities.size() < minItems || this.numRecursions <= 3) {
                    addFilteredActivities();
                    return;
                }
                return;
            }
            Log.d(StravaHelper.TAG, "enough filtered items, another page to collect in future");
            this.stravaSet.page++;
        }
    }

    public static class GetActivityStreamTask extends AsyncTask<Integer, Void, List<Stream>> {
        @Override // android.os.AsyncTask
        public List<Stream> doInBackground(Integer... id) {
            Log.e("Strava Helper", "getting activities");
            if (StravaHelper.createJStrava(false)) {
                try {
                    return StravaHelper.jStravaV3.findActivityStreams(id[0].intValue(), new String[]{"time", "latlng", BaseDataTypes.ID_DISTANCE, "altitude", "velocity_smooth", "heartrate", "cadence", "watts", "grade_smooth"});
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    StravaHelper.logoutIfAccessDenied(StravaHelper.mContext, e);
                }
            }
            Log.e("Strava Helper", "auth prob");
            return null;
        }
    }

    public static Long getStravaDate(String dateTimeStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            Date date = format.parse(dateTimeStr);
            return Long.valueOf(date.getTime());
        } catch (ParseException e) {
            Log.e(TAG, "Could not parse Strava ride/run start: " + dateTimeStr);
            return null;
        }
    }
}
