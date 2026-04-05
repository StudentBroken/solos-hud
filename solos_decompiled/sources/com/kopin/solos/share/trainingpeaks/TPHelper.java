package com.kopin.solos.share.trainingpeaks;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.webkit.CookieManager;
import com.facebook.internal.ServerProtocol;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kopin.peloton.PelotonPrefs;
import com.kopin.peloton.PelotonResponse;
import com.kopin.solos.common.SportType;
import com.kopin.solos.share.Config;
import com.kopin.solos.share.PWXGenerator;
import com.kopin.solos.share.Platforms;
import com.kopin.solos.share.R;
import com.kopin.solos.share.Share;
import com.kopin.solos.share.ShareHelper;
import com.kopin.solos.share.TrainingCache;
import com.kopin.solos.storage.Bike;
import com.kopin.solos.storage.Run;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.SavedTraining;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.storage.ShareMap;
import com.kopin.solos.storage.Shared;
import com.kopin.solos.storage.TrainingSegment;
import com.kopin.solos.storage.TrainingStep;
import com.kopin.solos.storage.TrainingTarget;
import com.kopin.solos.storage.settings.UserProfile;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.MetricType;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.jstrava.entities.VCTrakFile;

/* JADX INFO: loaded from: classes4.dex */
public class TPHelper {
    private static final String ACCESS_TOKEN_GRANT_TYPE_AUTH_CODE = "authorization_code";
    private static final String ACCESS_TOKEN_GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
    private static final String AUTH_URL = "https://oauth.trainingpeaks.com/oauth/authorize";
    private static final String CLIENT_ID = "solos";
    private static final String CLIENT_SECRET = "g3FGjpiFniHN01YFy1Al23CL0GYWren6nBml8VTRZkQ";
    private static final String FILE_UPLOAD_URL = "https://api.trainingpeaks.com/v1/file";
    private static final String HOST = "https://api.trainingpeaks.com/v1/workouts/wod/";
    private static final String PREFS_AUTH = "tp_auth_data";
    private static final String PREF_ACCESS_TOKEN = "access_token";
    private static final String PREF_REFRESH_TOKEN = "refresh_token";
    private static final String REDIRECT_URI = "http://solos-wearables.com";
    private static final String TAG = "TPHelper";
    private static final String TOKEN_URL = "https://oauth.trainingpeaks.com/oauth/token";
    private static final String UNIVERSAL_USER_ID = "TrainingPeaks";

    public static void login(Activity activity, int requestCode) {
        activity.startActivityForResult(getIntent(activity), requestCode);
    }

    public static void login(Fragment fragment, int requestCode) {
        fragment.startActivityForResult(getIntent(fragment.getActivity()), requestCode);
    }

    private static Intent getIntent(Context context) {
        Uri urv = getAuthorizationUri();
        Log.i(TAG, urv.toString());
        Intent intent = new Intent(context, (Class<?>) TPActivity.class);
        intent.setData(urv);
        intent.putExtra(ServerProtocol.DIALOG_PARAM_REDIRECT_URI, REDIRECT_URI);
        return intent;
    }

    public static void logout(Context context) {
        storeTokens(context, "", "");
        if (Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().removeAllCookies(null);
        } else {
            CookieManager.getInstance().removeAllCookie();
        }
    }

    public static boolean isLoggedIn(Context context) {
        return (context == null || getStoredRefreshToken(context).trim().isEmpty()) ? false : true;
    }

    public static boolean hasAutoShare(Context context) {
        return ShareHelper.hasAutoShare(Platforms.TrainingPeaks.getAutoSharePrefKey(), context);
    }

    public static boolean isShared(long rideId) {
        return SQLHelper.isRideShared(rideId, Platforms.TrainingPeaks.getSharedKey(), UNIVERSAL_USER_ID);
    }

    public static ShareHelper.ShareTask getShareTask(String id, Context context) {
        return new TPShareTask(id, context);
    }

    public static void onActivityResult(Context context, Intent data, ShareHelper.AuthListener listener) {
        if (data == null) {
            if (listener != null) {
                listener.onResult(Platforms.TrainingPeaks, false, "");
            }
        } else {
            String uri = data.getData().toString();
            parseResultUri(context, uri, listener);
        }
    }

    /* JADX WARN: Type inference failed for: r3v2, types: [com.kopin.solos.share.trainingpeaks.TPHelper$1] */
    public static void parseResultUri(final Context context, String uri, final ShareHelper.AuthListener listener) {
        int index = uri.indexOf("code=");
        if (index > 0) {
            uri = uri.substring("code=".length() + index);
        }
        if (uri.contains("&")) {
            uri = uri.substring(0, uri.indexOf("&"));
        }
        final String code = uri;
        new AsyncTask<Void, Void, AuthToken>() { // from class: com.kopin.solos.share.trainingpeaks.TPHelper.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public AuthToken doInBackground(Void... params) {
                return TPHelper.getNewAccessTokenFromAuthCode(context, code);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public void onPostExecute(AuthToken authToken) {
                super.onPostExecute(authToken);
                listener.onResult(Platforms.TrainingPeaks, authToken != null, "");
            }
        }.execute(new Void[0]);
    }

    public static Uri getAuthorizationUri() {
        return Uri.parse("https://oauth.trainingpeaks.com/oauth/authorize?client_id=solos&response_type=code&redirect_uri=http://solos-wearables.com&scope=file:write%20workouts:wod");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getStoredAccessToken(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_AUTH, 0);
        return sharedPref.getString("access_token", "");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getStoredRefreshToken(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_AUTH, 0);
        return sharedPref.getString("refresh_token", "");
    }

    private static void storeTokens(Context context, String accessToken, String refreshToken) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_AUTH, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("access_token", accessToken);
        editor.putString("refresh_token", refreshToken);
        editor.apply();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static AuthToken getNewAccessTokenFromRefreshToken(Context context, String refreshToken) {
        try {
            return getNewAccessToken(context, "refresh_token", refreshToken);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (URISyntaxException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static AuthToken getNewAccessTokenFromAuthCode(Context context, String authCode) {
        try {
            return getNewAccessToken(context, ACCESS_TOKEN_GRANT_TYPE_AUTH_CODE, authCode);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (URISyntaxException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static AuthToken getNewAccessToken(Context context, String grantType, String grantStr) throws URISyntaxException, IOException {
        URI uri = new URI(TOKEN_URL);
        URL url = uri.toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("client_id=").append(CLIENT_ID);
            sb.append("&client_secret=").append(CLIENT_SECRET);
            sb.append("&redirect_uri=").append(REDIRECT_URI);
            if (grantType.contentEquals(ACCESS_TOKEN_GRANT_TYPE_AUTH_CODE)) {
                sb.append("&grant_type=authorization_code");
                sb.append("&code=").append(grantStr);
            } else {
                sb.append("&grant_type=refresh_token");
                sb.append("&refresh_token=").append(grantStr);
            }
            conn.setRequestMethod(HttpRequest.METHOD_POST);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            OutputStream os = conn.getOutputStream();
            os.write(sb.toString().getBytes("UTF-8"));
            if (conn.getResponseCode() != 200) {
                for (Map.Entry<String, List<String>> entry : conn.getHeaderFields().entrySet()) {
                    System.out.println(entry.getKey() + " : " + entry.getValue());
                }
                return null;
            }
            Reader br = new InputStreamReader(conn.getInputStream());
            Gson gson = new Gson();
            AuthToken token = (AuthToken) gson.fromJson(br, AuthToken.class);
            storeTokens(context, token.access_token, token.refresh_token);
            return token;
        } finally {
            conn.disconnect();
        }
    }

    private static class AuthToken {
        String access_token;
        Integer expires_in;
        String refresh_token;

        private AuthToken() {
        }
    }

    private static class FileUploadData {
        String Data;
        String Filename;
        String UploadClient = "Solos-Android";

        FileUploadData(String filename, String data) {
            this.Data = data;
            this.Filename = filename;
        }
    }

    private static class TPShareTask extends ShareHelper.ShareTask {
        protected TPShareTask(String id, Context context) {
            super(Platforms.TrainingPeaks, id, context);
        }

        @Override // com.kopin.solos.share.ShareHelper.ShareTask
        public ShareHelper.ShareProgress doTaskInBackground(SavedWorkout ride) {
            Context context = getContext();
            if (!TPHelper.isLoggedIn(context)) {
                return new ShareHelper.ShareProgress(ShareHelper.Status.AUTH_FAIL, context.getString(R.string.share_tp_message_no_login_1));
            }
            if (TPHelper.isShared(ride.getId())) {
                return new ShareHelper.ShareProgress(ShareHelper.Status.ALREADY_SHARED, context.getString(R.string.share_tp_message_already));
            }
            try {
                if (ride instanceof SavedWorkout) {
                    PWXGenerator generator = new PWXGenerator(ride);
                    VCTrakFile file = new VCTrakFile("pwx", ride.getOrGenerateTitle(), generator);
                    String base64Data = file.base64Encoded();
                    FileUploadData fileUploadData = new FileUploadData(file.getFileName() + "." + file.getDataType(), base64Data);
                    String fileUploadJson = new Gson().toJson(fileUploadData);
                    URL url = new URL(TPHelper.FILE_UPLOAD_URL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setUseCaches(false);
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod(HttpRequest.METHOD_POST);
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setRequestProperty("Authorization", "Bearer " + TPHelper.getStoredAccessToken(context));
                    connection.setRequestProperty(HttpRequest.HEADER_CONTENT_LENGTH, Integer.toString(fileUploadJson.getBytes().length));
                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(fileUploadJson.getBytes("UTF-8"));
                    outputStream.flush();
                    int responseCode = connection.getResponseCode();
                    switch (responseCode) {
                        case 200:
                            Shared shared = Shared.newShare(ride.getId(), Platforms.TrainingPeaks.getSharedKey(), TPHelper.UNIVERSAL_USER_ID, "", ride.getSportType());
                            SQLHelper.addShare(shared);
                            return new ShareHelper.ShareProgress(ShareHelper.Status.DONE, context.getString(R.string.share_tp_message_successful));
                        case PelotonResponse.HTTP_UNAUTHORISED /* 401 */:
                            String refreshToken = TPHelper.getStoredRefreshToken(context);
                            TPHelper.getNewAccessTokenFromRefreshToken(context, refreshToken);
                            return doTaskInBackground(ride);
                        case 500:
                        case 503:
                            return new ShareHelper.ShareProgress(ShareHelper.Status.NETWORK_ERROR, context.getString(R.string.share_message_unknown_response));
                        default:
                            Log.d(TPHelper.TAG, "response code: " + connection.getResponseCode());
                            for (Map.Entry<String, List<String>> entry : connection.getHeaderFields().entrySet()) {
                                Log.d(TPHelper.TAG, "{ " + entry.getKey() + " : " + entry.getValue() + " }");
                                System.out.print("");
                            }
                            return new ShareHelper.ShareProgress(ShareHelper.Status.NETWORK_ERROR, context.getString(R.string.share_tp_message_no_response_code));
                    }
                }
            } catch (IOException e) {
                Log.e(TPHelper.TAG, "uploadFile", e);
            } catch (OutOfMemoryError e2) {
                Log.d(TPHelper.TAG, "uploadFile", e2);
            }
            return new ShareHelper.ShareProgress(ShareHelper.Status.NETWORK_ERROR, context.getString(R.string.share_tp_message_io_exception));
        }
    }

    private static void getWorkoutHeaders(Context context, Share.ShareListener listener) {
        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String query = dateStr + "?numberOfDays=5&includeDescription=true";
        new TPWorkoutHeadersTask(context, listener).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{query});
    }

    public static void importWorkouts(final Context context, final Share.ShareListener listener) {
        getWorkoutHeaders(context, new Share.ShareListener() { // from class: com.kopin.solos.share.trainingpeaks.TPHelper.2
            @Override // com.kopin.solos.share.Share.ShareListener
            public void onResponse(Share.ShareResponse response) {
                if (!response.isSuccess()) {
                    listener.onResponse(response);
                    return;
                }
                List<TPWorkoutHeader> headers = (List) response.serialisedResponse;
                ArrayList<TPWorkoutHeader> legitWorkoutHeaders = new ArrayList<>();
                for (TPWorkoutHeader header : headers) {
                    if (TPHelper.isValidWorkout(header)) {
                        legitWorkoutHeaders.add(header);
                    }
                }
                if (legitWorkoutHeaders.size() == 0) {
                    response.serialisedResponse = null;
                    TrainingCache.clearCache(Platforms.TrainingPeaks);
                    listener.onResponse(response);
                    return;
                }
                new TPWorkoutsImportTask(legitWorkoutHeaders, context, listener).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidWorkout(TPWorkoutHeader workoutHeader) {
        return (workoutHeader.WorkoutType.contentEquals(Bike.TABLE) || workoutHeader.WorkoutType.contentEquals(Run.TABLE_NAME)) && workoutHeader.WorkoutFileFormats != null && workoutHeader.WorkoutFileFormats.contains("json");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Share.ShareResponse requestHTTPResponse(String query, Context context) {
        BufferedReader reader;
        StringBuilder sb;
        Share.ShareResponse cloudResponse = new Share.ShareResponse();
        try {
            URL url = new URL(HOST + query);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(HttpRequest.METHOD_GET);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + getStoredAccessToken(context));
            cloudResponse.responseCode = connection.getResponseCode();
            reader = new BufferedReader(new InputStreamReader(cloudResponse.isSuccess() ? connection.getInputStream() : connection.getErrorStream()));
            sb = new StringBuilder();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            sb.append(line + "\n");
            Log.w(TAG, "getResponse " + sb.toString());
            return cloudResponse;
        }
        cloudResponse.rawResponse = sb.toString();
        return cloudResponse;
    }

    static abstract class TPCloudTask extends AsyncTask<String, Void, Share.ShareResponse> {
        Context context;
        Share.ShareListener listener;

        protected abstract void parseResponse(Share.ShareResponse shareResponse);

        public TPCloudTask(Context context, Share.ShareListener tpCloudListener) {
            this.listener = tpCloudListener;
            this.context = context;
        }

        Share.ShareResponse makeHttpRequest(String request) {
            if (TPHelper.isLoggedIn(this.context)) {
                Share.ShareResponse response = TPHelper.requestHTTPResponse(request, this.context);
                if (response.responseCode == 401) {
                    String refreshToken = TPHelper.getStoredRefreshToken(this.context);
                    TPHelper.getNewAccessTokenFromRefreshToken(this.context, refreshToken);
                    response = TPHelper.requestHTTPResponse(request, this.context);
                }
                if (response.isSuccess()) {
                    parseResponse(response);
                }
                return response;
            }
            Share.ShareResponse response2 = new Share.ShareResponse();
            response2.responseCode = 600;
            response2.rawResponse = this.context.getString(R.string.share_tp_message_no_login_1);
            return response2;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Share.ShareResponse doInBackground(String... params) {
            return makeHttpRequest(params[0]);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Share.ShareResponse response) {
            this.listener.onResponse(response);
        }
    }

    private static class TPWorkoutHeadersTask extends TPCloudTask {
        TPWorkoutHeadersTask(Context context, Share.ShareListener listener) {
            super(context, listener);
        }

        @Override // com.kopin.solos.share.trainingpeaks.TPHelper.TPCloudTask
        protected void parseResponse(Share.ShareResponse response) {
            Type typeList = new TypeToken<List<TPWorkoutHeader>>() { // from class: com.kopin.solos.share.trainingpeaks.TPHelper.TPWorkoutHeadersTask.1
            }.getType();
            List<TPWorkoutHeader> headers = (List) new Gson().fromJson(response.rawResponse, typeList);
            response.serialisedResponse = headers;
        }
    }

    private static class TPWorkoutTask extends TPCloudTask {
        TPWorkoutTask(Context context, Share.ShareListener listener) {
            super(context, listener);
        }

        @Override // com.kopin.solos.share.trainingpeaks.TPHelper.TPCloudTask
        protected void parseResponse(Share.ShareResponse response) {
            Type typeList = new TypeToken<TPWorkout>() { // from class: com.kopin.solos.share.trainingpeaks.TPHelper.TPWorkoutTask.1
            }.getType();
            TPWorkout workout = (TPWorkout) new Gson().fromJson(response.rawResponse, typeList);
            response.serialisedResponse = workout;
        }
    }

    private static class TPWorkoutsImportTask extends TPCloudTask {
        List<TPWorkoutHeader> headers;

        TPWorkoutsImportTask(List<TPWorkoutHeader> headers, Context context, Share.ShareListener listener) {
            super(context, listener);
            this.headers = headers;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.kopin.solos.share.trainingpeaks.TPHelper.TPCloudTask, android.os.AsyncTask
        public Share.ShareResponse doInBackground(String... params) {
            ArrayList<TPWorkout> workouts = new ArrayList<>();
            Share.ShareResponse response = new Share.ShareResponse();
            double defaultAvgSpeedRun = UserProfile.getAverageSpeed(SportType.RUN);
            double defaultAvgSpeedRide = UserProfile.getAverageSpeed(SportType.RIDE);
            for (TPWorkoutHeader header : this.headers) {
                String query = "file/" + header.Id + "/?format=json";
                response = makeHttpRequest(query);
                if (response.isSuccess() && (response.serialisedResponse instanceof TPWorkout)) {
                    TPWorkout workout = (TPWorkout) response.serialisedResponse;
                    workout.approxAvgSpeed = workout.Type.contentEquals(Run.TABLE_NAME) ? defaultAvgSpeedRun : defaultAvgSpeedRide;
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    format.setTimeZone(TimeZone.getTimeZone("UTC"));
                    try {
                        Date date = format.parse(header.WorkoutDay);
                        workout.workoutDay = date.getTime();
                        if (header.Description != null) {
                            workout.description = header.Description;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    workouts.add(workout);
                }
            }
            TrainingCache.clearCache(Platforms.TrainingPeaks);
            for (TPWorkout workout2 : workouts) {
                ArrayList<SavedTraining.Segment> segments = new ArrayList<>();
                int i = 0;
                for (TPStructure structure : workout2.Structure) {
                    ArrayList<SavedTraining.Step> steps = new ArrayList<>();
                    if (structure.Steps != null) {
                        for (TPStep step : structure.Steps) {
                            steps.add(createStep(workout2, step, i));
                            i++;
                        }
                    } else {
                        steps.add(createStep(workout2, structure, i));
                        i++;
                    }
                    segments.add(createSegment(workout2, structure, steps));
                }
                boolean isCompleted = ShareMap.isTrainingCompleted(PelotonPrefs.getEmail(), Platforms.TrainingPeaks.getSharedKey(), workout2.Id, workout2.workoutDay);
                if (!isCompleted || Config.DEBUG) {
                    SavedTraining training = createTraining(workout2, segments, isCompleted);
                    TrainingCache.add(Platforms.TrainingPeaks, training, workout2.workoutDay);
                }
            }
            response.serialisedResponse = TrainingCache.getTrainingSchedule(Platforms.TrainingPeaks);
            return response;
        }

        private SavedTraining createTraining(TPWorkout workout, List<SavedTraining.Segment> segments, boolean isCompleted) {
            MetricType primaryMetric;
            SportType sportType = SportType.RIDE;
            if (workout.Type.contentEquals(Run.TABLE_NAME)) {
                sportType = SportType.RUN;
            } else if (workout.Type.contentEquals(Bike.TABLE)) {
                sportType = SportType.RIDE;
            }
            if (workout.Ftp > 0.0d) {
                primaryMetric = sportType == SportType.RUN ? MetricType.AVERAGE_TARGET_KICK : MetricType.AVERAGE_TARGET_POWER;
            } else if (workout.MaxHr > 0 || workout.ThresholdHr > 0) {
                primaryMetric = MetricType.AVERAGE_TARGET_HEARTRATE;
            } else if (workout.ThresholdSpeed > 0.0d) {
                primaryMetric = sportType == SportType.RUN ? MetricType.AVERAGE_TARGET_PACE : MetricType.AVERAGE_TARGET_SPEED;
            } else {
                primaryMetric = MetricType.PERCEIVED_EXERTION_RATING;
            }
            return new SavedTraining(workout.Title, workout.description, workout.timePlanned, workout.distancePlanned, sportType, primaryMetric, workout.Id, workout.workoutDay, isCompleted, segments);
        }

        private SavedTraining.Segment createSegment(TPWorkout workout, TPStructure structure, List<SavedTraining.Step> steps) {
            TrainingStep.Trigger primaryTrigger;
            int stepCount = structure.Steps != null ? structure.Steps.size() : 1;
            TrainingSegment.SegmentType type = TrainingSegment.SegmentType.get(structure.Type);
            int loopCount = type == TrainingSegment.SegmentType.REPEAT ? structure.Length.Value.intValue() : 1;
            long duration = 0;
            double distance = 0.0d;
            int countDistTrigger = 0;
            int countTimeTrigger = 0;
            for (SavedTraining.Step step : steps) {
                switch (step.getSplitType()) {
                    case DISTANCE:
                        countDistTrigger += loopCount;
                        break;
                    case TIME:
                        countTimeTrigger += loopCount;
                        break;
                    default:
                        if (step.getDuration() > 0) {
                            countTimeTrigger += loopCount;
                        }
                        if (step.getDistance() > 0.0d) {
                            countDistTrigger += loopCount;
                        }
                        break;
                }
                long stepDuration = step.getDuration() > 0 ? step.getDuration() : (long) (step.getDistance() / workout.approxAvgSpeed);
                duration += ((long) loopCount) * stepDuration;
                double stepDistance = step.getDistance() > 0.0d ? step.getDistance() : step.getDuration() * workout.approxAvgSpeed;
                distance += ((double) loopCount) * stepDistance;
            }
            workout.distancePlanned += distance;
            workout.timePlanned += duration;
            if (countTimeTrigger >= countDistTrigger) {
                primaryTrigger = TrainingStep.Trigger.TIME;
            } else {
                primaryTrigger = TrainingStep.Trigger.DISTANCE;
            }
            return new SavedTraining.Segment(stepCount, loopCount, type, duration, distance, primaryTrigger, steps);
        }

        private SavedTraining.Step createStep(TPWorkout workout, TPStep step, long sequence) {
            ArrayList<SavedTraining.Target> targets = new ArrayList<>();
            SavedTraining.Target target = createTarget(workout, step.IntensityTarget);
            if (target != null) {
                targets.add(target);
            }
            SavedTraining.Target target2 = createTarget(workout, step.CadenceTarget);
            if (target2 != null) {
                targets.add(target2);
            }
            TrainingStep.Trigger trigger = TrainingStep.Trigger.TIME;
            long duration = 0;
            double distance = 0.0d;
            if (step.Length.Unit != null && step.Length.Unit.contentEquals("Meter")) {
                trigger = TrainingStep.Trigger.DISTANCE;
                distance = step.Length.Value.doubleValue();
            } else if (step.Length.Unit.contentEquals("Second")) {
                trigger = TrainingStep.Trigger.TIME;
                duration = step.Length.Value.longValue();
            }
            if (step.OpenDuration != null && step.OpenDuration.booleanValue()) {
                trigger = TrainingStep.Trigger.MANUAL;
            }
            TrainingStep.IntensityClass intensityClass = TrainingStep.IntensityClass.get(step.IntensityClass);
            return new SavedTraining.Step(step.Name, intensityClass, sequence, duration, distance, trigger, step.Notes, targets);
        }

        private SavedTraining.Target createTarget(TPWorkout workout, TPTarget target) {
            boolean isPaceTarget;
            boolean toToRounded;
            MetricType metricType;
            double multiplier;
            if (target == null) {
                return null;
            }
            String metric = target.Unit;
            TrainingTarget.TargetType targetType = TrainingTarget.TargetType.ABOVE;
            double minValue = -2.147483648E9d;
            double maxValue = -2.147483648E9d;
            double threshold = -2.147483648E9d;
            if (target.Value != null) {
                threshold = target.Value.doubleValue();
            }
            if (target.MinValue != null && target.MaxValue != null) {
                targetType = TrainingTarget.TargetType.RANGE;
                minValue = target.MinValue.doubleValue();
                maxValue = target.MaxValue.doubleValue();
            }
            isPaceTarget = false;
            toToRounded = false;
            switch (metric) {
                case "PercentOfMaxHr":
                    metricType = MetricType.AVERAGE_TARGET_HEARTRATE;
                    multiplier = ((double) workout.MaxHr) / 100.0d;
                    toToRounded = true;
                    break;
                case "PercentOfThresholdHr":
                    metricType = MetricType.AVERAGE_TARGET_HEARTRATE;
                    multiplier = ((double) workout.ThresholdHr) / 100.0d;
                    toToRounded = true;
                    break;
                case "PercentOfThresholdSpeed":
                    multiplier = workout.ThresholdSpeed / 100.0d;
                    if (workout.Type.contentEquals(Run.TABLE_NAME)) {
                        isPaceTarget = true;
                        metricType = MetricType.AVERAGE_TARGET_PACE;
                        break;
                    } else {
                        metricType = MetricType.AVERAGE_TARGET_SPEED;
                        break;
                    }
                    break;
                case "rpm":
                    toToRounded = true;
                    if (workout.Type.contentEquals(Run.TABLE_NAME)) {
                        metricType = MetricType.AVERAGE_TARGET_STEP;
                        multiplier = 2.0d;
                        break;
                    } else {
                        metricType = MetricType.AVERAGE_TARGET_CADENCE;
                        multiplier = 1.0d;
                        break;
                    }
                    break;
                case "Rpe":
                    metricType = MetricType.PERCEIVED_EXERTION_RATING;
                    multiplier = 1.0d;
                    break;
                case "PercentOfFtp":
                default:
                    if (workout.Type.contentEquals(Run.TABLE_NAME)) {
                        metricType = MetricType.AVERAGE_TARGET_KICK;
                    } else {
                        metricType = MetricType.AVERAGE_TARGET_POWER;
                    }
                    multiplier = workout.Ftp / 100.0d;
                    toToRounded = true;
                    break;
            }
            if (target.Value != null) {
                threshold *= multiplier;
                if (isPaceTarget) {
                    threshold = Conversion.speedToPace2(threshold);
                }
                if (toToRounded) {
                    threshold = Math.round(threshold);
                }
            }
            if (target.MinValue != null && target.MaxValue != null) {
                minValue *= multiplier;
                maxValue *= multiplier;
                if (isPaceTarget) {
                    minValue = Conversion.speedToPace2(maxValue);
                    maxValue = Conversion.speedToPace2(minValue);
                }
                if (toToRounded) {
                    minValue = Math.round(minValue);
                    maxValue = Math.round(maxValue);
                }
            }
            return new SavedTraining.Target(metricType, targetType, threshold, minValue, maxValue);
        }

        @Override // com.kopin.solos.share.trainingpeaks.TPHelper.TPCloudTask
        protected void parseResponse(Share.ShareResponse response) {
            Type typeList = new TypeToken<TPWorkout>() { // from class: com.kopin.solos.share.trainingpeaks.TPHelper.TPWorkoutsImportTask.1
            }.getType();
            TPWorkout workout = (TPWorkout) new Gson().fromJson(response.rawResponse, typeList);
            response.serialisedResponse = workout;
        }
    }
}
