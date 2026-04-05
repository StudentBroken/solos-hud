package com.kopin.peloton;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import com.digits.sdk.vcard.VCardBuilder;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import com.kopin.peloton.Peloton;
import com.kopin.peloton.debug.LogEvent;
import com.kopin.peloton.groupcom.ChatGroup;
import com.kopin.peloton.groupcom.SessionInfo;
import com.kopin.peloton.ride.Lap;
import com.kopin.peloton.ride.LapStats;
import com.kopin.peloton.ride.RideRecord;
import com.kopin.peloton.ride.Route;
import com.kopin.peloton.training.TrainingWorkout;
import com.kopin.pupil.pagerenderer.Theme;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* JADX INFO: loaded from: classes61.dex */
public class Cloud {
    private static final String CLIENT_ID = "ST3ljqo5YN7dNog+rcK4+FgLMBwIGoMAncfTFECgG3M=";
    private static final String CLIENT_ID2 = "099153c2625149bc8ecb3e85e03f0022";
    private static final String CONTENT_FORM = "application/x-www-form-urlencoded";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final boolean ENFORCE_ZIP = false;
    private static final String HOST_DEV = "kopinsolosapi-dev.azurewebsites.net/";
    private static final String HTTP = "http://";
    private static final boolean HTTP_RETRY = true;
    private static final String KEY_ACCEPT = "Accept";
    private static final String KEY_AUTHORISATION = "Authorization";
    private static final String KEY_CLIENT_ID = "client_id";
    private static final String KEY_COMPRESSION = "Accept-Encoding";
    private static final String KEY_CONTENT_TYPE = "Content-Type";
    private static final String KEY_DEVICE_ID = "DeviceId";
    private static final String KEY_GRANTTYPE = "grant_type";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_REFRESH = "refresh_token";
    private static final String KEY_RIDE_DATA = "ridedata";
    private static final String KEY_RUN_DATA = "rundata";
    private static final String KEY_TRAINING_DATA = "trainingdata";
    private static final String KEY_USERNAME = "username";
    private static final String RIDE_DATA_JSON_ARRAY_EVENTS = "EventLog";
    private static final String RIDE_DATA_JSON_ARRAY_LAPS = "Laps";
    private static final String RIDE_DATA_JSON_ARRAY_RECORDS = "Records";
    private static final String RIDE_DATA_JSON_OVERALL_STATS = "OverallStats";
    private static final String TAG = "PelotonCloud";
    private static final String URL_ADD_BIKE = "api/bike";
    private static final String URL_ADD_FTP = "api/FunctionalThresholdPower";
    private static final String URL_ADD_GEAR = "api/Gear";
    private static final String URL_ADD_PHR = "api/Phr";
    private static final String URL_ADD_RFTP = "api/Rtp";
    private static final String URL_ADD_RIDE = "api/ride/";
    private static final String URL_ADD_RIDE_DATA = "api/RideData?rideId=";
    private static final String URL_ADD_ROUTE = "api/route";
    private static final String URL_ADD_RUN = "api/run/";
    private static final String URL_ADD_RUN_DATA = "api/RunData?runId=";
    private static final String URL_ADD_TRAINING = "api/Training/";
    private static final String URL_FORGOT_PASSWORD = "PasswordRecovery";
    private static final String URL_GET_TRAININGS = "api/Training/GetMyWorkouts";
    private static final String URL_GROUP_MANAGEMENT = "WebChat/Authenticate";
    private static final String URL_JOIN_CHAT_SESSION = "api/Chat/Join";
    private static final String URL_LEAVE_CHAT_SESSION = "api/Chat/Leave";
    private static final String URL_LOGIN = "OAUth2/token";
    private static final String URL_MY_BIKES = "api/bike/GetMyBikes";
    private static final String URL_MY_CHAT_GROUPS = "api/Chat/MyGroups";
    private static final String URL_MY_FTP = "api/FunctionalThresholdPower/GetMyPower";
    private static final String URL_MY_GEAR = "api/gear/GetMyGear";
    private static final String URL_MY_PHR = "api/Phr/GetMyPower";
    private static final String URL_MY_PROFILE = "api/userprofile/GetMyProfile";
    private static final String URL_MY_RFTP = "api/Rtp/GetMyPower";
    private static final String URL_MY_RIDES = "api/ride/GetMyRides";
    private static final String URL_MY_ROUTES = "api/route/GetAllRoutes";
    private static final String URL_MY_RUNS = "api/run/GetMyRuns";
    private static final String URL_PROFILE = "api/userprofile/";
    private static final String URL_REGISTER = "api/register";
    private static final String URL_REGISTER_EXTERNAL = "api/RegisterExternal";
    private static final String URL_REMOVE_BIKE = "api/bike/Delete?bikeId=";
    private static final String URL_REMOVE_GEAR = "api/gear/Delete?gearId=";
    private static final String URL_REMOVE_RIDE = "api/ride/Delete?rideId=";
    private static final String URL_REMOVE_ROUTE = "api/route/Delete?routeId=";
    private static final String URL_REMOVE_RUN = "api/run/Delete?runId=";
    private static final String URL_REMOVE_TRAINING = "api/Training/Delete?trainingId=";
    private static final String URL_RIDE_DATA = "api/RideData?rideId=";
    private static final String URL_RUN_DATA = "api/RunData?runId=";
    private static final String URL_TRAINING_DATA = "api/TrainingData?trainingId=";
    private static final String URL_UPDATE_BIKE = "api/bike/";
    private static final String URL_UPDATE_GEAR = "api/Gear/";
    private static final String URL_UPDATE_RIDE = "api/ride/UpdateRide";
    private static final String URL_UPDATE_RUN = "api/run/UpdateRun";
    private static final String URL_USER_DATA = "api/userprofile/GetExtendedProfile";
    private static final String VALUE_AUTHORISATION_BEARER = "Bearer ";
    private static final String VALUE_COMPRESSION = "gzip";
    private static Peloton.AuthenticationListener mAuthenticationListener;
    private static Context mContext;
    private static String mDeviceId;
    private static final String HTTPS = "https://";
    private static String PROTOCOL = HTTPS;
    private static final String HOST_LIVE = "api.solos-wearables.com/";
    private static String HOST = HOST_LIVE;
    private static String CHARSET_UTF8 = "UTF-8";
    private static String authorisation = null;
    private static String refreshToken = "";

    static void setHostDev() {
        HOST = HOST_DEV;
    }

    static void setHostLive() {
        HOST = HOST_LIVE;
    }

    static void setHttps(boolean https) {
        PROTOCOL = https ? HTTPS : HTTP;
    }

    static boolean init(Context context, String deviceId, Peloton.AuthenticationListener authenticationListener) {
        if (context == null) {
            return false;
        }
        mAuthenticationListener = authenticationListener;
        mContext = context;
        mDeviceId = deviceId;
        PelotonPrefs.init(context);
        authorisation = PelotonPrefs.getToken();
        refreshToken = PelotonPrefs.getRefreshToken();
        return true;
    }

    static PelotonResponse registerWork(UserInfo userInfo) {
        PelotonResponse response = new PelotonResponse();
        if (userInfo == null) {
            throw new InvalidParameterException("Invalid parameter, user info is null");
        }
        try {
            String data = userInfo.toJson();
            HttpURLConnection connection = preparePostConnection(PROTOCOL + HOST + URL_REGISTER, data);
            response = getResponse(connection);
            processToken(response, userInfo.Email);
            if (response.isServerSuccess()) {
                setProfileWork(new Profile(userInfo.Name, userInfo.DOB, userInfo.Weight));
            }
        } catch (IOException ioe) {
            Log.e(TAG, "register error " + ioe.getMessage());
        }
        return response;
    }

    static PelotonResponse registerExternalWork(UserInfo userInfo) {
        PelotonResponse response = new PelotonResponse();
        if (userInfo == null) {
            throw new InvalidParameterException("Invalid parameter, user info is null");
        }
        try {
            String data = userInfo.toJson();
            HttpURLConnection connection = preparePostConnection(PROTOCOL + HOST + URL_REGISTER_EXTERNAL, data);
            response = getResponse(connection);
            processToken(response, userInfo.Email);
            return response;
        } catch (IOException ioe) {
            Log.e(TAG, "register error " + ioe.getMessage());
            return response;
        }
    }

    static PelotonResponse signInWork(String email, String password) {
        PelotonResponse response = new PelotonResponse();
        if (email == null) {
            throw new InvalidParameterException("Invalid parameter, user email is null");
        }
        if (password == null) {
            throw new InvalidParameterException("Invalid parameter, user password is null");
        }
        try {
            StringBuilder builder = new StringBuilder();
            builder.append(KEY_USERNAME).append("=").append(email).append("&").append(KEY_PASSWORD).append("=").append(password).append("&").append("grant_type").append("=password&").append("client_id").append("=").append(CLIENT_ID2);
            String data = builder.toString();
            HttpURLConnection connection = preparePostConnectionForm(PROTOCOL + HOST + URL_LOGIN, data);
            response = getResponse(connection);
            processToken(response, email);
            return response;
        } catch (IOException ioe) {
            Log.e(TAG, "register error " + ioe.getMessage());
            return response;
        }
    }

    private static void processToken(PelotonResponse response) {
        processToken(response, null);
    }

    private static void processToken(PelotonResponse response, String email) {
        if (response.isServerSuccess()) {
            try {
                Token token = (Token) new Gson().fromJson(response.rawResponse, Token.class);
                if (token != null) {
                    response.result = token;
                    if (mContext != null) {
                        PelotonPrefs.setToken(mContext, "");
                        authorisation = null;
                        if (token.access_token != null && !token.access_token.isEmpty()) {
                            PelotonPrefs.setToken(mContext, token.access_token);
                            authorisation = token.access_token;
                            if (email != null && email.contains("@")) {
                                PelotonPrefs.setEmail(mContext, email);
                            }
                        }
                        if (token.refresh_token != null && !token.refresh_token.isEmpty()) {
                            PelotonPrefs.setRefreshToken(mContext, token.refresh_token);
                            refreshToken = token.refresh_token;
                        }
                    }
                }
            } catch (JsonSyntaxException e) {
            }
        }
    }

    private static void processServerErrorResponse(PelotonResponse response) {
        if (response.rawResponse != null && response.rawResponse.length() > 0) {
            try {
                Failure failure = (Failure) new Gson().fromJson(response.rawResponse, Failure.class);
                response.serverFailure = failure;
                Log.i(TAG, "process rawResponse success = " + failure.Success + ", " + failure.ErrorMessage);
            } catch (JsonSyntaxException e) {
                Log.d(TAG, "process rawResponse json error ");
            }
        }
    }

    static PelotonResponse getUserDataWork() {
        PelotonResponse response = new PelotonResponse();
        try {
            String url = PROTOCOL + HOST + URL_USER_DATA;
            HttpURLConnection connection = prepareGetConnection(url);
            response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = prepareGetConnection(url);
                response = getResponse(connection2);
            }
            if (response.isServerSuccess()) {
                try {
                    UserData userData = (UserData) new Gson().fromJson(response.rawResponse, UserData.class);
                    if (userData != null) {
                        response.result = userData;
                    }
                } catch (JsonSyntaxException e) {
                }
            }
        } catch (IOException ioe) {
            Log.e(TAG, "get UserData error " + ioe.getMessage());
        }
        return response;
    }

    public static PelotonResponse getProfileWork() {
        PelotonResponse response = new PelotonResponse();
        try {
            String url = PROTOCOL + HOST + URL_MY_PROFILE;
            HttpURLConnection connection = prepareGetConnection(url);
            response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = prepareGetConnection(url);
                response = getResponse(connection2);
                authenticationCallback(response);
            }
            if (response.isServerSuccess()) {
                try {
                    Profile profile = (Profile) new Gson().fromJson(response.rawResponse, Profile.class);
                    if (profile != null) {
                        response.result = profile;
                    }
                } catch (JsonSyntaxException e) {
                }
            }
        } catch (IOException ioe) {
            Log.e(TAG, "get Profile error " + ioe.getMessage());
        }
        return response;
    }

    static PelotonResponse setProfileWork(Profile profile) {
        PelotonResponse response = new PelotonResponse();
        if (profile == null) {
            throw new InvalidParameterException("Invalid parameter, profile is null");
        }
        try {
            String body = new Gson().toJson(profile);
            String url = PROTOCOL + HOST + URL_PROFILE;
            HttpURLConnection connection = preparePutConnection(url, body);
            response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = preparePutConnection(url, body);
                response = getResponse(connection2);
                authenticationCallback(response);
            }
            if (response.isServerSuccess()) {
                try {
                    Profile updatedProfile = (Profile) new Gson().fromJson(response.rawResponse, Profile.class);
                    if (updatedProfile != null) {
                        response.result = updatedProfile;
                    }
                } catch (JsonSyntaxException e) {
                }
            }
        } catch (IOException ioe) {
            Log.e(TAG, "get Profile error " + ioe.getMessage());
        }
        return response;
    }

    static PelotonResponse getBikesWork() {
        PelotonResponse response = new PelotonResponse();
        try {
            String url = PROTOCOL + HOST + URL_MY_BIKES;
            HttpURLConnection connection = prepareGetConnection(url);
            response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = prepareGetConnection(url);
                response = getResponse(connection2);
                authenticationCallback(response);
            }
            if (response.isServerSuccess()) {
                try {
                    Type typeList = new TypeToken<List<Bike>>() { // from class: com.kopin.peloton.Cloud.1
                    }.getType();
                    List<Bike> bikes = (List) new Gson().fromJson(response.rawResponse, typeList);
                    if (bikes != null) {
                        response.result = bikes;
                    }
                } catch (JsonSyntaxException jse) {
                    Log.e(TAG, "get bikes Json error " + jse.getMessage());
                }
            }
        } catch (IOException ioe) {
            Log.e(TAG, "get bikes error " + ioe.getMessage());
        }
        return response;
    }

    public static PelotonResponse addBikeWork(Bike bike) {
        PelotonResponse response = new PelotonResponse();
        if (bike == null) {
            throw new InvalidParameterException("Invalid parameter, bike is null");
        }
        try {
            String body = new Gson().toJson(bike);
            String url = PROTOCOL + HOST + URL_ADD_BIKE;
            HttpURLConnection connection = preparePostConnection(url, body);
            response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = preparePostConnection(url, body);
                response = getResponse(connection2);
                authenticationCallback(response);
            }
            if (response.isServerSuccess()) {
                try {
                    Bike updatedBike = (Bike) new Gson().fromJson(response.rawResponse, Bike.class);
                    if (updatedBike != null) {
                        response.result = updatedBike;
                    }
                } catch (JsonSyntaxException e) {
                }
            }
        } catch (IOException ioe) {
            Log.e(TAG, "add bike error " + ioe.getMessage());
        }
        return response;
    }

    static PelotonResponse updateBikeWork(Bike bike) {
        PelotonResponse response = new PelotonResponse();
        if (bike == null) {
            throw new InvalidParameterException("Invalid parameter, bike is null");
        }
        try {
            String body = new Gson().toJson(bike);
            String url = PROTOCOL + HOST + URL_UPDATE_BIKE;
            HttpURLConnection connection = preparePutConnection(url, body);
            response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = preparePutConnection(url, body);
                response = getResponse(connection2);
                authenticationCallback(response);
            }
            if (response.isServerSuccess()) {
                try {
                    Bike updatedBike = (Bike) new Gson().fromJson(response.rawResponse, Bike.class);
                    if (updatedBike != null) {
                        response.result = updatedBike;
                    }
                } catch (JsonSyntaxException e) {
                }
            }
        } catch (IOException ioe) {
            Log.e(TAG, "add bike error " + ioe.getMessage());
        }
        return response;
    }

    static PelotonResponse removeBikeWork(String bikeId) {
        PelotonResponse response = new PelotonResponse();
        try {
            String url = PROTOCOL + HOST + URL_REMOVE_BIKE + bikeId;
            HttpURLConnection connection = prepareDeleteConnection(url);
            response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = prepareDeleteConnection(url);
                response = getResponse(connection2);
                authenticationCallback(response);
            }
            if (response.isServerSuccess()) {
                try {
                    Bike updatedBike = (Bike) new Gson().fromJson(response.rawResponse, Bike.class);
                    if (updatedBike != null) {
                        response.result = updatedBike;
                    }
                } catch (JsonSyntaxException e) {
                }
            }
        } catch (IOException ioe) {
            Log.e(TAG, "removed bike error " + ioe.getMessage());
        }
        return response;
    }

    static PelotonResponse getGearWork() {
        PelotonResponse response = new PelotonResponse();
        try {
            String url = PROTOCOL + HOST + URL_MY_GEAR;
            HttpURLConnection connection = prepareGetConnection(url);
            response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = prepareGetConnection(url);
                response = getResponse(connection2);
                authenticationCallback(response);
            }
            if (response.isServerSuccess()) {
                try {
                    Type typeList = new TypeToken<List<Gear>>() { // from class: com.kopin.peloton.Cloud.2
                    }.getType();
                    List<Gear> gear = (List) new Gson().fromJson(response.rawResponse, typeList);
                    if (gear != null) {
                        response.result = gear;
                    }
                } catch (JsonSyntaxException jse) {
                    Log.e(TAG, "get gear Json error " + jse.getMessage());
                }
            }
        } catch (IOException ioe) {
            Log.e(TAG, "get gear error " + ioe.getMessage());
        }
        return response;
    }

    public static PelotonResponse addGearWork(Gear gear) {
        PelotonResponse response = new PelotonResponse();
        if (gear == null) {
            throw new InvalidParameterException("Invalid parameter, gear is null");
        }
        try {
            String body = new Gson().toJson(gear);
            String url = PROTOCOL + HOST + URL_ADD_GEAR;
            HttpURLConnection connection = preparePostConnection(url, body);
            response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = preparePostConnection(url, body);
                response = getResponse(connection2);
                authenticationCallback(response);
            }
            if (response.isServerSuccess()) {
                try {
                    Gear updatedGear = (Gear) new Gson().fromJson(response.rawResponse, Gear.class);
                    if (updatedGear != null) {
                        response.result = updatedGear;
                    }
                } catch (JsonSyntaxException e) {
                }
            }
        } catch (IOException ioe) {
            Log.e(TAG, "add gear error " + ioe.getMessage());
        }
        return response;
    }

    static PelotonResponse updateGearWork(Gear gear) {
        PelotonResponse response = new PelotonResponse();
        if (gear == null) {
            throw new InvalidParameterException("Invalid parameter, gear is null");
        }
        try {
            String body = new Gson().toJson(gear);
            String url = PROTOCOL + HOST + URL_UPDATE_GEAR;
            HttpURLConnection connection = preparePutConnection(url, body);
            response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = preparePutConnection(url, body);
                response = getResponse(connection2);
                authenticationCallback(response);
            }
            if (response.isServerSuccess()) {
                try {
                    Gear updatedGear = (Gear) new Gson().fromJson(response.rawResponse, Gear.class);
                    if (updatedGear != null) {
                        response.result = updatedGear;
                    }
                } catch (JsonSyntaxException e) {
                }
            }
        } catch (IOException ioe) {
            Log.e(TAG, "add gear error " + ioe.getMessage());
        }
        return response;
    }

    static PelotonResponse removeGearWork(String gearId) {
        PelotonResponse response = new PelotonResponse();
        try {
            String url = PROTOCOL + HOST + URL_REMOVE_GEAR + gearId;
            HttpURLConnection connection = prepareDeleteConnection(url);
            response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = prepareDeleteConnection(url);
                response = getResponse(connection2);
                authenticationCallback(response);
            }
            if (response.isServerSuccess()) {
                try {
                    Gear updatedGear = (Gear) new Gson().fromJson(response.rawResponse, Gear.class);
                    if (updatedGear != null) {
                        response.result = updatedGear;
                    }
                } catch (JsonSyntaxException e) {
                }
            }
        } catch (IOException ioe) {
            Log.e(TAG, "removed gear error " + ioe.getMessage());
        }
        return response;
    }

    static PelotonResponse getRoutesWork() {
        PelotonResponse response = new PelotonResponse();
        try {
            String url = PROTOCOL + HOST + URL_MY_ROUTES;
            HttpURLConnection connection = prepareGetConnection(url);
            response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = prepareGetConnection(url);
                response = getResponse(connection2);
                authenticationCallback(response);
            }
            if (response.isServerSuccess()) {
                try {
                    Type typeList = new TypeToken<List<Route>>() { // from class: com.kopin.peloton.Cloud.3
                    }.getType();
                    List<Route> routes = (List) new Gson().fromJson(response.rawResponse, typeList);
                    if (routes != null) {
                        response.result = routes;
                    }
                } catch (JsonSyntaxException jse) {
                    Log.e(TAG, "get routes Json error " + jse.getMessage());
                }
            }
        } catch (IOException ioe) {
            Log.e(TAG, "get routes error " + ioe.getMessage());
        }
        return response;
    }

    public static PelotonResponse addRouteWork(Route route) {
        PelotonResponse response = new PelotonResponse();
        if (route == null) {
            throw new InvalidParameterException("Invalid parameter, route is null");
        }
        try {
            String body = new Gson().toJson(route);
            String url = PROTOCOL + HOST + URL_ADD_ROUTE;
            HttpURLConnection connection = preparePostConnection(url, body);
            response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = preparePostConnection(url, body);
                response = getResponse(connection2);
                authenticationCallback(response);
            }
            if (response.isServerSuccess()) {
                try {
                    Route updatedRoute = (Route) new Gson().fromJson(response.rawResponse, Route.class);
                    if (updatedRoute != null) {
                        response.result = updatedRoute;
                    }
                } catch (JsonSyntaxException e) {
                }
            }
        } catch (IOException ioe) {
            Log.e(TAG, "add route error " + ioe.getMessage());
        }
        return response;
    }

    static PelotonResponse removeRouteWork(String routeId) {
        PelotonResponse response = new PelotonResponse();
        try {
            String url = PROTOCOL + HOST + URL_REMOVE_ROUTE + routeId;
            HttpURLConnection connection = prepareDeleteConnection(url);
            response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = prepareDeleteConnection(url);
                response = getResponse(connection2);
                authenticationCallback(response);
            }
            if (response.isServerSuccess()) {
                try {
                    Route updatedRoute = (Route) new Gson().fromJson(response.rawResponse, Route.class);
                    if (updatedRoute != null) {
                        response.result = updatedRoute;
                    }
                } catch (JsonSyntaxException e) {
                }
            }
        } catch (IOException ioe) {
            Log.e(TAG, "removed route error " + ioe.getMessage());
        }
        return response;
    }

    static PelotonResponse getFTPWork() {
        PelotonResponse response = new PelotonResponse();
        try {
            String url = PROTOCOL + HOST + URL_MY_FTP;
            HttpURLConnection connection = prepareGetConnection(url);
            response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = prepareGetConnection(url);
                response = getResponse(connection2);
                authenticationCallback(response);
            }
            if (response.isServerSuccess()) {
                try {
                    Type typeList = new TypeToken<List<FTP>>() { // from class: com.kopin.peloton.Cloud.4
                    }.getType();
                    List<FTP> ftps = (List) new Gson().fromJson(response.rawResponse, typeList);
                    if (ftps != null) {
                        response.result = ftps;
                    }
                } catch (JsonSyntaxException jse) {
                    Log.e(TAG, "get ftp Json error " + jse.getMessage());
                }
            }
        } catch (IOException ioe) {
            Log.e(TAG, "get ftp error " + ioe.getMessage());
        }
        return response;
    }

    static PelotonResponse getRFTPWork() {
        PelotonResponse response = new PelotonResponse();
        try {
            String url = PROTOCOL + HOST + URL_MY_RFTP;
            HttpURLConnection connection = prepareGetConnection(url);
            response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = prepareGetConnection(url);
                response = getResponse(connection2);
                authenticationCallback(response);
            }
            if (response.isServerSuccess()) {
                try {
                    Type typeList = new TypeToken<List<RFTP>>() { // from class: com.kopin.peloton.Cloud.5
                    }.getType();
                    List<RFTP> ftps = (List) new Gson().fromJson(response.rawResponse, typeList);
                    if (ftps != null) {
                        response.result = ftps;
                    }
                } catch (JsonSyntaxException jse) {
                    Log.e(TAG, "get ftp Json error " + jse.getMessage());
                }
            }
        } catch (IOException ioe) {
            Log.e(TAG, "get ftp error " + ioe.getMessage());
        }
        return response;
    }

    static PelotonResponse getPHRWork() {
        PelotonResponse response = new PelotonResponse();
        try {
            String url = PROTOCOL + HOST + URL_MY_PHR;
            HttpURLConnection connection = prepareGetConnection(url);
            response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = prepareGetConnection(url);
                response = getResponse(connection2);
                authenticationCallback(response);
            }
            if (response.isServerSuccess()) {
                try {
                    Type typeList = new TypeToken<List<PHR>>() { // from class: com.kopin.peloton.Cloud.6
                    }.getType();
                    List<PHR> ftps = (List) new Gson().fromJson(response.rawResponse, typeList);
                    if (ftps != null) {
                        response.result = ftps;
                    }
                } catch (JsonSyntaxException jse) {
                    Log.e(TAG, "get ftp Json error " + jse.getMessage());
                }
            }
        } catch (IOException ioe) {
            Log.e(TAG, "get ftp error " + ioe.getMessage());
        }
        return response;
    }

    public static PelotonResponse addFTPWork(FTP ftp) {
        PelotonResponse response = new PelotonResponse();
        if (ftp == null) {
            throw new InvalidParameterException("Invalid parameter, ftp is null");
        }
        try {
            String body = new Gson().toJson(ftp);
            String url = PROTOCOL + HOST + URL_ADD_FTP;
            HttpURLConnection connection = preparePostConnection(url, body);
            response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = preparePostConnection(url, body);
                response = getResponse(connection2);
                authenticationCallback(response);
            }
            if (response.isServerSuccess()) {
                try {
                    FTP updatedFTP = (FTP) new Gson().fromJson(response.rawResponse, FTP.class);
                    if (updatedFTP != null) {
                        response.result = updatedFTP;
                    }
                } catch (JsonSyntaxException e) {
                }
            }
        } catch (IOException ioe) {
            Log.e(TAG, "add ftp error " + ioe.getMessage());
        }
        return response;
    }

    public static PelotonResponse addRFTPWork(RFTP ftp) {
        PelotonResponse response = new PelotonResponse();
        if (ftp == null) {
            throw new InvalidParameterException("Invalid parameter, ftp is null");
        }
        try {
            String body = new Gson().toJson(ftp);
            String url = PROTOCOL + HOST + URL_ADD_RFTP;
            HttpURLConnection connection = preparePostConnection(url, body);
            response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = preparePostConnection(url, body);
                response = getResponse(connection2);
                authenticationCallback(response);
            }
            if (response.isServerSuccess()) {
                try {
                    RFTP updatedFTP = (RFTP) new Gson().fromJson(response.rawResponse, RFTP.class);
                    if (updatedFTP != null) {
                        response.result = updatedFTP;
                    }
                } catch (JsonSyntaxException e) {
                }
            }
        } catch (IOException ioe) {
            Log.e(TAG, "add ftp error " + ioe.getMessage());
        }
        return response;
    }

    public static PelotonResponse addPHRWork(PHR ftp) {
        PelotonResponse response = new PelotonResponse();
        if (ftp == null) {
            throw new InvalidParameterException("Invalid parameter, ftp is null");
        }
        try {
            String body = new Gson().toJson(ftp);
            String url = PROTOCOL + HOST + URL_ADD_PHR;
            HttpURLConnection connection = preparePostConnection(url, body);
            response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = preparePostConnection(url, body);
                response = getResponse(connection2);
                authenticationCallback(response);
            }
            if (response.isServerSuccess()) {
                try {
                    PHR updatedFTP = (PHR) new Gson().fromJson(response.rawResponse, PHR.class);
                    if (updatedFTP != null) {
                        response.result = updatedFTP;
                    }
                } catch (JsonSyntaxException e) {
                }
            }
        } catch (IOException ioe) {
            Log.e(TAG, "add ftp error " + ioe.getMessage());
        }
        return response;
    }

    static PelotonResponse getRunsWork() {
        PelotonResponse response = new PelotonResponse();
        try {
            String url = PROTOCOL + HOST + URL_MY_RUNS;
            HttpURLConnection connection = prepareGetConnection(url);
            response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = prepareGetConnection(url);
                response = getResponse(connection2);
                authenticationCallback(response);
            }
            if (response.isServerSuccess()) {
                try {
                    Type typeList = new TypeToken<List<Run>>() { // from class: com.kopin.peloton.Cloud.7
                    }.getType();
                    List<Run> runs = (List) new Gson().fromJson(response.rawResponse, typeList);
                    if (runs != null) {
                        response.result = runs;
                    }
                } catch (JsonSyntaxException jse) {
                    Log.e(TAG, "get runs Json error " + jse.getMessage());
                }
            }
        } catch (IOException ioe) {
            Log.e(TAG, "get runs error " + ioe.getMessage());
        }
        return response;
    }

    static PelotonResponse getRidesWork() {
        PelotonResponse response = new PelotonResponse();
        try {
            String url = PROTOCOL + HOST + URL_MY_RIDES;
            HttpURLConnection connection = prepareGetConnection(url);
            response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = prepareGetConnection(url);
                response = getResponse(connection2);
                authenticationCallback(response);
            }
            if (response.isServerSuccess()) {
                try {
                    Type typeList = new TypeToken<List<Ride>>() { // from class: com.kopin.peloton.Cloud.8
                    }.getType();
                    List<Ride> rides = (List) new Gson().fromJson(response.rawResponse, typeList);
                    if (rides != null) {
                        response.result = rides;
                    }
                } catch (JsonSyntaxException jse) {
                    Log.e(TAG, "get rides Json error " + jse.getMessage());
                }
            }
        } catch (IOException ioe) {
            Log.e(TAG, "get rides error " + ioe.getMessage());
        }
        return response;
    }

    private static void handleDefaultGear(Run run) {
        Gear gear;
        List<Gear> gears;
        if (run.GearId == null || run.GearId.isEmpty()) {
            PelotonResponse gearResponse = getGearWork();
            if (gearResponse.result != null && (gears = (List) gearResponse.result) != null && gears.size() > 0) {
                run.GearId = gears.get(0).GearId;
            }
            if (run.GearId == null || run.GearId.isEmpty()) {
                Gear gear2 = new Gear();
                gear2.Name = Theme.TEXT_SIZE_DEFAULT;
                PelotonResponse gearResponse2 = addGearWork(gear2);
                if (gearResponse2 != null && gearResponse2.result != null && (gear = (Gear) gearResponse2.result) != null) {
                    run.GearId = gear.GearId;
                }
            }
        }
    }

    public static PelotonResponse addRunHeaderWork(Run run) {
        handleDefaultGear(run);
        PelotonResponse response = new PelotonResponse();
        List<Lap> laps = new ArrayList<>(run.Laps);
        List<RideRecord> records = new ArrayList<>(run.Records);
        try {
            run.Laps.clear();
            run.Records.clear();
            run.Laps = null;
            run.Records = null;
            String body = new Gson().toJson(run);
            String url = PROTOCOL + HOST + URL_ADD_RUN;
            HttpURLConnection connection = preparePostConnection(url, body);
            response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = preparePostConnection(url, body);
                response = getResponse(connection2);
                authenticationCallback(response);
            }
            if (response.isServerSuccess()) {
                try {
                    Run updatedRun = (Run) new Gson().fromJson(response.rawResponse, Run.class);
                    if (updatedRun != null && updatedRun.RunId != null && !updatedRun.RunId.isEmpty()) {
                        response.result = updatedRun;
                    }
                } catch (JsonSyntaxException jse) {
                    Log.e(TAG, "add run Json error " + jse.getMessage());
                }
            }
        } catch (IOException ioe) {
            Log.e(TAG, "add run error " + ioe.getMessage());
        }
        run.Laps = new ArrayList<>();
        run.Records = new ArrayList<>();
        run.Laps.addAll(laps);
        run.Records.addAll(records);
        return response;
    }

    public static PelotonResponse addRideHeaderWork(Ride ride) {
        PelotonResponse response = new PelotonResponse();
        List<Lap> laps = new ArrayList<>(ride.Laps);
        List<RideRecord> records = new ArrayList<>(ride.Records);
        try {
            ride.Laps.clear();
            ride.Records.clear();
            ride.Laps = null;
            ride.Records = null;
            String body = new Gson().toJson(ride);
            String url = PROTOCOL + HOST + URL_ADD_RIDE;
            HttpURLConnection connection = preparePostConnection(url, body);
            response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = preparePostConnection(url, body);
                response = getResponse(connection2);
                authenticationCallback(response);
            }
            if (response.isServerSuccess()) {
                try {
                    Ride updatedRide = (Ride) new Gson().fromJson(response.rawResponse, Ride.class);
                    if (updatedRide != null && updatedRide.RideId != null && !updatedRide.RideId.isEmpty()) {
                        response.result = updatedRide;
                    }
                } catch (JsonSyntaxException jse) {
                    Log.e(TAG, "add ride Json error " + jse.getMessage());
                }
            }
        } catch (IOException ioe) {
            Log.e(TAG, "add ride error " + ioe.getMessage());
        }
        ride.Laps = new ArrayList<>();
        ride.Records = new ArrayList<>();
        ride.Laps.addAll(laps);
        ride.Records.addAll(records);
        return response;
    }

    @Deprecated
    static List<PelotonResponse> addRideWork(Ride ride) {
        List<PelotonResponse> responses = new ArrayList<>();
        try {
            List<Lap> laps = new ArrayList<>(ride.Laps);
            List<RideRecord> records = new ArrayList<>(ride.Records);
            ride.Laps.clear();
            ride.Records.clear();
            ride.Laps = null;
            ride.Records = null;
            String body = new Gson().toJson(ride);
            String url = PROTOCOL + HOST + URL_ADD_RIDE;
            HttpURLConnection connection = preparePostConnection(url, body);
            PelotonResponse response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = preparePostConnection(url, body);
                response = getResponse(connection2);
                authenticationCallback(response);
            }
            if (response.isServerSuccess()) {
                try {
                    Ride updatedRide = (Ride) new Gson().fromJson(response.rawResponse, Ride.class);
                    if (updatedRide != null && updatedRide.RideId != null && !updatedRide.RideId.isEmpty()) {
                        response.result = updatedRide;
                        responses.add(response);
                        ride.Laps = new ArrayList<>();
                        ride.Records = new ArrayList<>();
                        ride.Laps.addAll(laps);
                        ride.Records.addAll(records);
                        if (!ride.Laps.isEmpty() && !ride.Records.isEmpty()) {
                            PelotonResponse responseData = setRideDataWork(ride, updatedRide.RideId, false);
                            responses.add(responseData);
                        } else {
                            Log.w(TAG, "Could not upload ride data, removing header " + updatedRide.RideId);
                            removeRideWork(updatedRide.RideId);
                        }
                    }
                } catch (JsonSyntaxException jse) {
                    Log.e(TAG, "add ride Json error " + jse.getMessage());
                }
            }
        } catch (IOException ioe) {
            Log.e(TAG, "add ride error " + ioe.getMessage());
        }
        return responses;
    }

    static List<PelotonResponse> addRunWork(Run run) {
        handleDefaultGear(run);
        List<PelotonResponse> responses = new ArrayList<>();
        try {
            List<Lap> laps = new ArrayList<>(run.Laps);
            List<RideRecord> records = new ArrayList<>(run.Records);
            run.Laps.clear();
            run.Records.clear();
            run.Laps = null;
            run.Records = null;
            String body = new Gson().toJson(run);
            String url = PROTOCOL + HOST + URL_ADD_RUN;
            HttpURLConnection connection = preparePostConnection(url, body);
            PelotonResponse response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = preparePostConnection(url, body);
                response = getResponse(connection2);
                authenticationCallback(response);
            }
            if (response.isServerSuccess()) {
                try {
                    Run updatedRun = (Run) new Gson().fromJson(response.rawResponse, Run.class);
                    if (updatedRun != null && updatedRun.RunId != null && !updatedRun.RunId.isEmpty()) {
                        response.result = updatedRun;
                        responses.add(response);
                        run.Laps = new ArrayList<>();
                        run.Records = new ArrayList<>();
                        run.Laps.addAll(laps);
                        run.Records.addAll(records);
                        if (!run.Laps.isEmpty() && !run.Records.isEmpty()) {
                            PelotonResponse responseData = setRunDataWork(run, updatedRun.RunId, false);
                            responses.add(responseData);
                        } else {
                            Log.w(TAG, "Could not upload run data, removing header " + updatedRun.RunId);
                            removeRunWork(updatedRun.RunId);
                        }
                    }
                } catch (JsonSyntaxException jse) {
                    Log.e(TAG, "add run Json error " + jse.getMessage());
                }
            }
        } catch (IOException ioe) {
            Log.e(TAG, "add run error " + ioe.getMessage());
        }
        return responses;
    }

    static PelotonResponse updateRunDataWork(Run run) {
        PelotonResponse response = new PelotonResponse();
        if (run == null) {
            throw new InvalidParameterException("Invalid parameter, run is null");
        }
        try {
            ArrayList<Lap> laps = run.Laps;
            ArrayList<RideRecord> records = run.Records;
            run.Laps = null;
            run.Records = null;
            String body = new Gson().toJson(run);
            String url = PROTOCOL + HOST + URL_UPDATE_RUN;
            HttpURLConnection connection = preparePutConnection(url, body);
            PelotonResponse response2 = getResponse(connection);
            if (reauthenticationAndRetryCheck(response2)) {
                HttpURLConnection connection2 = preparePutConnection(url, body);
                response2 = getResponse(connection2);
                authenticationCallback(response2);
            }
            if (response2.isServerSuccess()) {
                run.Laps = laps;
                run.Records = records;
                if (run.Laps != null && !run.Laps.isEmpty() && run.Records != null && !run.Records.isEmpty()) {
                    PelotonResponse response3 = setRunDataWork(run, run.RunId, true);
                    return response3;
                }
                return response2;
            }
            return response2;
        } catch (Exception ioe) {
            Log.e(TAG, "update run error " + ioe.getMessage());
            return response;
        }
    }

    static PelotonResponse setRide(Ride ride) {
        PelotonResponse response = new PelotonResponse();
        if (ride == null) {
            throw new InvalidParameterException("Invalid parameter, ride is null");
        }
        try {
            ArrayList<Lap> laps = ride.Laps;
            ArrayList<RideRecord> records = ride.Records;
            ride.Laps = null;
            ride.Records = null;
            String body = new Gson().toJson(ride);
            String url = PROTOCOL + HOST + URL_ADD_RIDE;
            HttpURLConnection connection = preparePostConnection(url, body);
            response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = preparePostConnection(url, body);
                response = getResponse(connection2);
                authenticationCallback(response);
            }
            if (response.isServerSuccess()) {
                try {
                    Ride updatedRide = (Ride) new Gson().fromJson(response.rawResponse, Ride.class);
                    if (updatedRide != null && updatedRide.RideId != null && !updatedRide.RideId.isEmpty()) {
                        response.result = updatedRide;
                        ride.Laps = new ArrayList<>();
                        ride.Records = new ArrayList<>();
                        ride.Laps.addAll(laps);
                        ride.Records.addAll(records);
                        if (!ride.Laps.isEmpty() && !ride.Records.isEmpty()) {
                            PelotonResponse responseData = setRideDataWork(ride, updatedRide.RideId, false);
                            if (responseData == null || !responseData.isServerSuccess()) {
                                response = responseData;
                            }
                        } else {
                            Log.w(TAG, "Could not upload ride data, removing header " + updatedRide.RideId);
                            removeRideWork(updatedRide.RideId);
                        }
                    }
                } catch (JsonSyntaxException jse) {
                    Log.e(TAG, "add ride Json error " + jse.getMessage());
                }
            }
        } catch (Exception ioe) {
            Log.e(TAG, "update ride error " + ioe.getMessage());
        }
        return response;
    }

    static PelotonResponse setRun(Run run) {
        PelotonResponse response = new PelotonResponse();
        if (run == null) {
            throw new InvalidParameterException("Invalid parameter, run is null");
        }
        try {
            ArrayList<Lap> laps = run.Laps;
            ArrayList<RideRecord> records = run.Records;
            run.Laps = null;
            run.Records = null;
            String body = new Gson().toJson(run);
            String url = PROTOCOL + HOST + URL_ADD_RUN;
            HttpURLConnection connection = preparePostConnection(url, body);
            response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = preparePostConnection(url, body);
                response = getResponse(connection2);
                authenticationCallback(response);
            }
            if (response.isServerSuccess()) {
                try {
                    Run updatedRun = (Run) new Gson().fromJson(response.rawResponse, Run.class);
                    if (updatedRun != null && updatedRun.RunId != null && !updatedRun.RunId.isEmpty()) {
                        response.result = updatedRun;
                        run.Laps = new ArrayList<>();
                        run.Records = new ArrayList<>();
                        run.Laps.addAll(laps);
                        run.Records.addAll(records);
                        if (!run.Laps.isEmpty() && !run.Records.isEmpty()) {
                            PelotonResponse responseData = setRunDataWork(run, updatedRun.RunId, false);
                            if (responseData == null || !responseData.isServerSuccess()) {
                                response = responseData;
                            }
                        } else {
                            Log.w(TAG, "Could not upload run data, removing header " + updatedRun.RunId);
                            removeRunWork(updatedRun.RunId);
                        }
                    }
                } catch (JsonSyntaxException jse) {
                    Log.e(TAG, "add run Json error " + jse.getMessage());
                }
            }
        } catch (Exception ioe) {
            Log.e(TAG, "update run error " + ioe.getMessage());
        }
        return response;
    }

    static PelotonResponse updateRideDataWork(Ride ride) {
        PelotonResponse response = new PelotonResponse();
        if (ride == null) {
            throw new InvalidParameterException("Invalid parameter, ride is null");
        }
        try {
            ArrayList<Lap> laps = ride.Laps;
            ArrayList<RideRecord> records = ride.Records;
            ride.Laps = null;
            ride.Records = null;
            String body = new Gson().toJson(ride);
            String url = PROTOCOL + HOST + URL_UPDATE_RIDE;
            HttpURLConnection connection = preparePutConnection(url, body);
            PelotonResponse response2 = getResponse(connection);
            if (reauthenticationAndRetryCheck(response2)) {
                HttpURLConnection connection2 = preparePutConnection(url, body);
                response2 = getResponse(connection2);
                authenticationCallback(response2);
            }
            if (response2.isServerSuccess()) {
                ride.Laps = laps;
                ride.Records = records;
                if (ride.Laps != null && !ride.Laps.isEmpty() && ride.Records != null && !ride.Records.isEmpty()) {
                    PelotonResponse response3 = setRideDataWork(ride, ride.RideId, true);
                    return response3;
                }
                return response2;
            }
            return response2;
        } catch (Exception ioe) {
            Log.e(TAG, "update ride error " + ioe.getMessage());
            return response;
        }
    }

    static PelotonResponse removeRunWork(String runId) {
        PelotonResponse response = new PelotonResponse();
        try {
            String url = PROTOCOL + HOST + URL_REMOVE_RUN + runId;
            HttpURLConnection connection = prepareDeleteConnection(url);
            response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = prepareDeleteConnection(url);
                response = getResponse(connection2);
                authenticationCallback(response);
            }
            if (response.isServerSuccess()) {
                try {
                    Run updatedRun = (Run) new Gson().fromJson(response.rawResponse, Run.class);
                    if (updatedRun != null) {
                        response.result = updatedRun;
                    }
                } catch (JsonSyntaxException e) {
                }
            }
        } catch (IOException ioe) {
            Log.e(TAG, "removed run error " + ioe.getMessage());
        }
        return response;
    }

    static PelotonResponse removeRideWork(String rideId) {
        PelotonResponse response = new PelotonResponse();
        try {
            String url = PROTOCOL + HOST + URL_REMOVE_RIDE + rideId;
            HttpURLConnection connection = prepareDeleteConnection(url);
            response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = prepareDeleteConnection(url);
                response = getResponse(connection2);
                authenticationCallback(response);
            }
            if (response.isServerSuccess()) {
                try {
                    Ride updatedRide = (Ride) new Gson().fromJson(response.rawResponse, Ride.class);
                    if (updatedRide != null) {
                        response.result = updatedRide;
                    }
                } catch (JsonSyntaxException e) {
                }
            }
        } catch (IOException ioe) {
            Log.e(TAG, "removed ride error " + ioe.getMessage());
        }
        return response;
    }

    public static PelotonResponse getRunDataWork(String runId) {
        PelotonResponse response = new PelotonResponse();
        try {
            String url = PROTOCOL + HOST + "api/RunData?runId=" + runId;
            HttpURLConnection connection = prepareGetConnection(url);
            response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = prepareGetConnection(url);
                response = getResponse(connection2);
                authenticationCallback(response);
            }
            if (response.isServerSuccess()) {
                try {
                    Run runData = (Run) new Gson().fromJson(response.rawResponse, Run.class);
                    if (runData != null) {
                        response.result = runData;
                    }
                } catch (JsonSyntaxException jse) {
                    Log.e(TAG, "get run data Json error " + jse.getMessage());
                }
            }
        } catch (IOException ioe) {
            Log.e(TAG, "get run data error " + ioe.getMessage());
        }
        return response;
    }

    public static PelotonResponse getRideDataWork(String rideId) {
        PelotonResponse response = new PelotonResponse();
        try {
            String url = PROTOCOL + HOST + "api/RideData?rideId=" + rideId;
            HttpURLConnection connection = prepareGetConnection(url);
            response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = prepareGetConnection(url);
                response = getResponse(connection2);
                authenticationCallback(response);
            }
            if (response.isServerSuccess()) {
                try {
                    Ride rideData = (Ride) new Gson().fromJson(response.rawResponse, Ride.class);
                    if (rideData != null) {
                        response.result = rideData;
                    }
                } catch (JsonSyntaxException jse) {
                    Log.e(TAG, "get ride data Json error " + jse.getMessage());
                }
            }
        } catch (IOException ioe) {
            Log.e(TAG, "get ride data error " + ioe.getMessage());
        }
        return response;
    }

    public static PelotonResponse setRunDataWork(Run runData, String runId, boolean includeOverallStats) {
        String url = PROTOCOL + HOST + "api/RunData?runId=" + runId;
        Log.i(TAG, "uploading set run data work. " + url);
        Log.i(TAG, "uploading run gear id " + runData.GearId);
        return upload(url, runData, includeOverallStats, runId);
    }

    public static PelotonResponse setRideDataWork(Ride rideData, String rideId, boolean includeOverallStats) {
        String url = PROTOCOL + HOST + "api/RideData?rideId=" + rideId;
        Log.i(TAG, "uploading set ride data work. " + url);
        return upload(url, rideData, includeOverallStats, rideId);
    }

    private static PelotonResponse upload(String path, Ride rideData, boolean includeOverallStats, String rideId) {
        PelotonResponse response = uploadRide(path, rideData, includeOverallStats, true, false, rideId);
        if (!response.isServerSuccess()) {
            return uploadRide(path, rideData, includeOverallStats, true, false, rideId);
        }
        return response;
    }

    private static PelotonResponse upload(String path, Run runData, boolean includeOverallStats, String runId) {
        PelotonResponse response = uploadRun(path, runData, includeOverallStats, true, false, runId);
        if (!response.isServerSuccess()) {
            return uploadRun(path, runData, includeOverallStats, true, false, runId);
        }
        return response;
    }

    private static PelotonResponse uploadRun(String path, Run runData, boolean includeOverallStats, boolean jsonStreamed, boolean debugFile, String id) {
        DataOutputStream request;
        PelotonResponse pelotonResponse = new PelotonResponse();
        if (runData != null) {
            HttpURLConnection httpUrlConnection = null;
            try {
                try {
                    URL url = new URL(path);
                    httpUrlConnection = (HttpURLConnection) url.openConnection();
                    httpUrlConnection.setUseCaches(false);
                    httpUrlConnection.setDoOutput(true);
                    httpUrlConnection.setRequestMethod(HttpRequest.METHOD_POST);
                    httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
                    httpUrlConnection.setRequestProperty(HttpRequest.HEADER_CACHE_CONTROL, "no-cache");
                    httpUrlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=*****");
                    setHeader(httpUrlConnection);
                    if (debugFile) {
                        File sdCard = Environment.getExternalStorageDirectory();
                        request = new DataOutputStream(new FileOutputStream(new File(sdCard, jsonStreamed ? "data_stream.txt" : "data_string.txt")));
                    } else {
                        request = new DataOutputStream(httpUrlConnection.getOutputStream());
                    }
                    request.writeBytes("--*****" + VCardBuilder.VCARD_END_OF_LINE);
                    request.writeBytes("Content-Disposition: form-data; name=\"" + KEY_RUN_DATA + "\";filename=\"runData\"" + VCardBuilder.VCARD_END_OF_LINE);
                    request.writeBytes(VCardBuilder.VCARD_END_OF_LINE);
                    try {
                        Gson gson = new Gson();
                        if (jsonStreamed) {
                            JsonWriter writer = new JsonWriter(new OutputStreamWriter(request, CHARSET_UTF8));
                            writer.beginObject();
                            if (includeOverallStats) {
                                writer.name(RIDE_DATA_JSON_OVERALL_STATS);
                                gson.toJson(runData.OverallStats, LapStats.class, writer);
                            }
                            writer.name(RIDE_DATA_JSON_ARRAY_LAPS);
                            writer.beginArray();
                            for (Lap lap : runData.Laps) {
                                gson.toJson(lap, Lap.class, writer);
                            }
                            writer.endArray();
                            writer.name(RIDE_DATA_JSON_ARRAY_RECORDS);
                            writer.beginArray();
                            int numRecs = 0;
                            for (RideRecord record : runData.Records) {
                                gson.toJson(record, RideRecord.class, writer);
                                numRecs++;
                            }
                            Log.i(TAG, "Ride/run data uploading, num records = " + numRecs);
                            writer.endArray();
                            writer.name(RIDE_DATA_JSON_ARRAY_EVENTS);
                            writer.beginArray();
                            for (LogEvent event : runData.EventLog) {
                                gson.toJson(event, LogEvent.class, writer);
                            }
                            writer.endArray();
                            writer.endObject();
                            writer.flush();
                        } else {
                            String body = gson.toJson(runData, Run.class);
                            request.write(body.getBytes(CHARSET_UTF8));
                        }
                    } catch (IOException ioe) {
                        Log.w(TAG, "Ride upload error with ride data json " + ioe.getMessage());
                    }
                    request.writeBytes(VCardBuilder.VCARD_END_OF_LINE);
                    request.writeBytes("--*****--" + VCardBuilder.VCARD_END_OF_LINE);
                    request.flush();
                    request.close();
                    int httpCode = httpUrlConnection.getResponseCode();
                    Log.i(TAG, "Ride data uploaded data, http " + httpCode);
                    pelotonResponse.httpCode = httpCode;
                    InputStream responseStream = new BufferedInputStream((httpCode < 200 || httpCode >= 300) ? httpUrlConnection.getErrorStream() : httpUrlConnection.getInputStream());
                    BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
                    if (httpCode != 200) {
                        Log.e(TAG, "ride/run data upload failed " + httpCode);
                        StringBuilder stringBuilder = new StringBuilder();
                        while (true) {
                            String line = responseStreamReader.readLine();
                            if (line == null) {
                                break;
                            }
                            stringBuilder.append(line).append("\n");
                        }
                        Log.e(TAG, stringBuilder.toString());
                        pelotonResponse.rawResponse = stringBuilder.toString();
                        processServerErrorResponse(pelotonResponse);
                    } else {
                        pelotonResponse.rawResponse = "{}";
                        pelotonResponse.result = new Run(id);
                    }
                    responseStreamReader.close();
                    responseStream.close();
                    if (httpUrlConnection != null) {
                        httpUrlConnection.disconnect();
                    }
                } catch (Exception ex) {
                    Log.e(TAG, "ride upload error " + ex.getMessage());
                    ex.printStackTrace();
                    if (httpUrlConnection != null) {
                        httpUrlConnection.disconnect();
                    }
                }
            } catch (Throwable th) {
                if (httpUrlConnection != null) {
                    httpUrlConnection.disconnect();
                }
                throw th;
            }
        }
        return pelotonResponse;
    }

    private static PelotonResponse uploadRide(String path, Ride rideData, boolean includeOverallStats, boolean jsonStreamed, boolean debugFile, String id) {
        DataOutputStream request;
        PelotonResponse pelotonResponse = new PelotonResponse();
        if (rideData != null) {
            HttpURLConnection httpUrlConnection = null;
            try {
                try {
                    URL url = new URL(path);
                    httpUrlConnection = (HttpURLConnection) url.openConnection();
                    httpUrlConnection.setUseCaches(false);
                    httpUrlConnection.setDoOutput(true);
                    httpUrlConnection.setRequestMethod(HttpRequest.METHOD_POST);
                    httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
                    httpUrlConnection.setRequestProperty(HttpRequest.HEADER_CACHE_CONTROL, "no-cache");
                    httpUrlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=*****");
                    setHeader(httpUrlConnection);
                    if (debugFile) {
                        File sdCard = Environment.getExternalStorageDirectory();
                        request = new DataOutputStream(new FileOutputStream(new File(sdCard, jsonStreamed ? "data_stream.txt" : "data_string.txt")));
                    } else {
                        request = new DataOutputStream(httpUrlConnection.getOutputStream());
                    }
                    request.writeBytes("--*****" + VCardBuilder.VCARD_END_OF_LINE);
                    request.writeBytes("Content-Disposition: form-data; name=\"" + KEY_RIDE_DATA + "\";filename=\"rideData\"" + VCardBuilder.VCARD_END_OF_LINE);
                    request.writeBytes(VCardBuilder.VCARD_END_OF_LINE);
                    try {
                        Gson gson = new Gson();
                        if (jsonStreamed) {
                            JsonWriter writer = new JsonWriter(new OutputStreamWriter(request, CHARSET_UTF8));
                            writer.beginObject();
                            if (includeOverallStats) {
                                writer.name(RIDE_DATA_JSON_OVERALL_STATS);
                                gson.toJson(rideData.OverallStats, LapStats.class, writer);
                            }
                            writer.name(RIDE_DATA_JSON_ARRAY_LAPS);
                            writer.beginArray();
                            for (Lap lap : rideData.Laps) {
                                gson.toJson(lap, Lap.class, writer);
                            }
                            writer.endArray();
                            writer.name(RIDE_DATA_JSON_ARRAY_RECORDS);
                            writer.beginArray();
                            int numRecs = 0;
                            for (RideRecord record : rideData.Records) {
                                gson.toJson(record, RideRecord.class, writer);
                                numRecs++;
                            }
                            Log.i(TAG, "Ride/run data uploading, num records = " + numRecs);
                            writer.endArray();
                            writer.name(RIDE_DATA_JSON_ARRAY_EVENTS);
                            writer.beginArray();
                            for (LogEvent event : rideData.EventLog) {
                                gson.toJson(event, LogEvent.class, writer);
                            }
                            writer.endArray();
                            writer.endObject();
                            writer.flush();
                        } else {
                            String body = gson.toJson(rideData, Ride.class);
                            request.write(body.getBytes(CHARSET_UTF8));
                        }
                    } catch (IOException ioe) {
                        Log.w(TAG, "Ride upload error with ride data json " + ioe.getMessage());
                    }
                    request.writeBytes(VCardBuilder.VCARD_END_OF_LINE);
                    request.writeBytes("--*****--" + VCardBuilder.VCARD_END_OF_LINE);
                    request.flush();
                    request.close();
                    int httpCode = httpUrlConnection.getResponseCode();
                    Log.i(TAG, "Ride data uploaded data, http " + httpCode);
                    pelotonResponse.httpCode = httpCode;
                    InputStream responseStream = new BufferedInputStream((httpCode < 200 || httpCode >= 300) ? httpUrlConnection.getErrorStream() : httpUrlConnection.getInputStream());
                    BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
                    if (httpCode != 200) {
                        Log.e(TAG, "ride/run data upload failed " + httpCode);
                        StringBuilder stringBuilder = new StringBuilder();
                        while (true) {
                            String line = responseStreamReader.readLine();
                            if (line == null) {
                                break;
                            }
                            stringBuilder.append(line).append("\n");
                        }
                        Log.e(TAG, stringBuilder.toString());
                        pelotonResponse.rawResponse = stringBuilder.toString();
                        processServerErrorResponse(pelotonResponse);
                    } else {
                        pelotonResponse.rawResponse = "{}";
                        pelotonResponse.result = rideData != null ? new Ride(id) : new Run(id);
                    }
                    responseStreamReader.close();
                    responseStream.close();
                    if (httpUrlConnection != null) {
                        httpUrlConnection.disconnect();
                    }
                } catch (Exception ex) {
                    Log.e(TAG, "ride upload error " + ex.getMessage());
                    ex.printStackTrace();
                    if (httpUrlConnection != null) {
                        httpUrlConnection.disconnect();
                    }
                }
            } catch (Throwable th) {
                if (httpUrlConnection != null) {
                    httpUrlConnection.disconnect();
                }
                throw th;
            }
        }
        return pelotonResponse;
    }

    public static PelotonResponse fetchMyChatGroups(int timeout) {
        PelotonResponse response = new PelotonResponse();
        try {
            String url = PROTOCOL + HOST + URL_MY_CHAT_GROUPS;
            HttpURLConnection connection = prepareGetConnection(url, timeout);
            response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = prepareGetConnection(url, timeout);
                response = getResponse(connection2);
                authenticationCallback(response);
            }
            if (response.isServerSuccess()) {
                try {
                    Type typeList = new TypeToken<List<ChatGroup>>() { // from class: com.kopin.peloton.Cloud.9
                    }.getType();
                    List<ChatGroup> chatGroups = (List) new Gson().fromJson(response.rawResponse, typeList);
                    if (chatGroups != null) {
                        response.result = chatGroups;
                    }
                } catch (JsonSyntaxException jse) {
                    Log.e(TAG, "get chat groups error " + jse.getMessage());
                }
            }
        } catch (IOException ioe) {
            Log.e(TAG, "get chat groups error " + ioe.getMessage());
        }
        return response;
    }

    public static PelotonResponse joinChatSession(String groupId, int timeout) {
        PelotonResponse response = new PelotonResponse();
        try {
            String url = PROTOCOL + HOST + URL_JOIN_CHAT_SESSION + "?groupId=" + groupId;
            HttpURLConnection connection = preparePostConnection(url, "", timeout);
            response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = preparePostConnection(url, "");
                response = getResponse(connection2);
                authenticationCallback(response);
            }
            if (response.isServerSuccess()) {
                try {
                    Type typeList = new TypeToken<SessionInfo>() { // from class: com.kopin.peloton.Cloud.10
                    }.getType();
                    SessionInfo sessionInfo = (SessionInfo) new Gson().fromJson(response.rawResponse, typeList);
                    if (sessionInfo != null) {
                        response.result = sessionInfo;
                    }
                } catch (JsonSyntaxException jse) {
                    Log.e(TAG, "get session info " + jse.getMessage());
                }
            }
        } catch (IOException ioe) {
            Log.e(TAG, "get session error " + ioe.getMessage());
        }
        return response;
    }

    public static PelotonResponse leaveChatSession(String groupId, int timeout) {
        PelotonResponse response = new PelotonResponse();
        try {
            String url = PROTOCOL + HOST + URL_LEAVE_CHAT_SESSION + "?groupId=" + groupId;
            HttpURLConnection connection = preparePostConnection(url, "", timeout);
            PelotonResponse response2 = getResponse(connection);
            if (reauthenticationAndRetryCheck(response2)) {
                HttpURLConnection connection2 = preparePostConnection(url, "");
                response = getResponse(connection2);
                authenticationCallback(response);
                return response;
            }
            return response2;
        } catch (IOException ioe) {
            Log.e(TAG, "get session error " + ioe.getMessage());
            return response;
        }
    }

    private static HttpURLConnection prepareGetConnection(String urlString) throws IOException {
        return prepareGetConnection(urlString, 0);
    }

    private static HttpURLConnection prepareGetConnection(String urlString, int timeoutMillis) throws IOException {
        Log.i(TAG, "url = " + urlString);
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(HttpRequest.METHOD_GET);
        if (timeoutMillis > 0) {
            connection.setConnectTimeout(timeoutMillis);
        }
        setHeader(connection);
        return connection;
    }

    private static HttpURLConnection prepareDeleteConnection(String urlString) throws IOException {
        Log.i(TAG, "url = " + urlString);
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(HttpRequest.METHOD_DELETE);
        setHeader(connection);
        return connection;
    }

    private static HttpURLConnection preparePostConnectionForm(String urlString, String body) throws IOException {
        return prepareConnection(urlString, body, true, false, 0);
    }

    private static HttpURLConnection preparePostConnection(String urlString, String body) throws IOException {
        return prepareConnection(urlString, body, false, false, 0);
    }

    private static HttpURLConnection preparePostConnection(String urlString, String body, int timeoutInMills) throws IOException {
        return prepareConnection(urlString, body, false, false, timeoutInMills);
    }

    private static HttpURLConnection preparePutConnection(String urlString, String body) throws IOException {
        return prepareConnection(urlString, body, false, true, 0);
    }

    private static HttpURLConnection prepareConnection(String urlString, String body, boolean form, boolean put, int timeoutMillis) throws IOException {
        Log.i(TAG, "url = " + urlString);
        Log.i(TAG, "body = " + body);
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(put ? HttpRequest.METHOD_PUT : HttpRequest.METHOD_POST);
        connection.setRequestProperty("Content-Type", form ? "application/x-www-form-urlencoded" : "application/json");
        connection.setRequestProperty("client_id", CLIENT_ID);
        connection.setRequestProperty("Accept", "application/json");
        setHeader(connection, !form);
        connection.setRequestProperty(HttpRequest.HEADER_CONTENT_LENGTH, Integer.toString(body.getBytes().length));
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        if (timeoutMillis > 0) {
            connection.setConnectTimeout(timeoutMillis);
        }
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(body.getBytes(CHARSET_UTF8));
        outputStream.flush();
        return connection;
    }

    private static void setHeader(HttpURLConnection connection) {
        setHeader(connection, true);
    }

    private static void setHeader(HttpURLConnection connection, boolean setAuthorisation) {
        if (connection != null) {
            if (authorisation != null && setAuthorisation) {
                connection.setRequestProperty("Authorization", VALUE_AUTHORISATION_BEARER + authorisation);
            }
            if (mDeviceId != null) {
                connection.setRequestProperty(KEY_DEVICE_ID, mDeviceId);
            }
        }
    }

    private static PelotonResponse getResponse(HttpURLConnection connection) {
        BufferedReader reader;
        StringBuilder sb;
        PelotonResponse response = new PelotonResponse();
        try {
            Log.w(TAG, "URL " + connection.getURL());
            int httpCode = connection.getResponseCode();
            response.httpCode = httpCode;
            Log.w(TAG, "HTTP code " + httpCode);
            reader = new BufferedReader(new InputStreamReader((httpCode < 200 || httpCode >= 300) ? connection.getErrorStream() : connection.getInputStream()));
            sb = new StringBuilder();
        } catch (IOException ioe) {
            Log.e(TAG, "reading stream error " + ioe.getMessage());
        }
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            sb.append(line + "\n");
            Log.w(TAG, "getResponse " + sb.toString());
            return response;
        }
        response.rawResponse = sb.toString();
        processServerErrorResponse(response);
        return response;
    }

    private static boolean reauthenticationAndRetryCheck(PelotonResponse response) {
        if (!response.isAuthorised()) {
            if (mContext != null) {
                PelotonPrefs.setToken(mContext, "");
            }
            authorisation = null;
            try {
                StringBuilder builder = new StringBuilder();
                builder.append(KEY_REFRESH).append("=").append(refreshToken).append("&").append("grant_type").append("=password&").append("client_id").append("=").append(CLIENT_ID2);
                String data = builder.toString();
                HttpURLConnection connection = preparePostConnectionForm(PROTOCOL + HOST + URL_LOGIN, data);
                PelotonResponse authResponse = getResponse(connection);
                processToken(authResponse);
                return authResponse.isAuthorised();
            } catch (IOException ioe) {
                Log.e(TAG, "register error " + ioe.getMessage());
            }
        } else if (!response.isServerSuccess()) {
            return true;
        }
        return false;
    }

    private static void authenticationCallback(PelotonResponse response) {
        if (mAuthenticationListener != null && response != null && !response.isAuthorised()) {
            mAuthenticationListener.onFailed(response);
        }
    }

    public static String getHost() {
        return PROTOCOL + HOST;
    }

    static String getPasswordRecoveryUrl() {
        return PROTOCOL + HOST + URL_FORGOT_PASSWORD;
    }

    public static String getGroupManagementUrl() {
        return PROTOCOL + HOST + URL_GROUP_MANAGEMENT;
    }

    public static HashMap<String, String> getGroupManagementHttpHeaders() {
        HashMap<String, String> map = new HashMap<>();
        map.put("Authorization", VALUE_AUTHORISATION_BEARER + authorisation);
        map.put("client_id", CLIENT_ID);
        return map;
    }

    public static Intent getGroupManagementOpenUrlIntent() {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(PROTOCOL + HOST + URL_GROUP_MANAGEMENT));
        Bundle bundle = new Bundle();
        bundle.putString("Authorization", VALUE_AUTHORISATION_BEARER + authorisation);
        bundle.putString("client_id", CLIENT_ID);
        intent.putExtra("com.android.browser.headers", bundle);
        return intent;
    }

    static PelotonResponse getTrainingHeaders() {
        PelotonResponse response = new PelotonResponse();
        try {
            String url = PROTOCOL + HOST + URL_GET_TRAININGS;
            HttpURLConnection connection = prepareGetConnection(url);
            response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = prepareGetConnection(url);
                response = getResponse(connection2);
                authenticationCallback(response);
            }
            if (response.isServerSuccess()) {
                try {
                    Type typeList = new TypeToken<List<TrainingWorkout>>() { // from class: com.kopin.peloton.Cloud.11
                    }.getType();
                    List<TrainingWorkout> trainingHeaders = (List) new Gson().fromJson(response.rawResponse, typeList);
                    if (trainingHeaders != null) {
                        response.result = trainingHeaders;
                    }
                } catch (JsonSyntaxException jse) {
                    Log.e(TAG, "get runs Json error " + jse.getMessage());
                }
            }
        } catch (IOException ioe) {
            Log.e(TAG, "get runs error " + ioe.getMessage());
        }
        return response;
    }

    static PelotonResponse getTrainingData(String trainingId) {
        PelotonResponse response = new PelotonResponse();
        try {
            String url = PROTOCOL + HOST + URL_TRAINING_DATA + trainingId;
            HttpURLConnection connection = prepareGetConnection(url);
            response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = prepareGetConnection(url);
                response = getResponse(connection2);
                authenticationCallback(response);
            }
            if (response.isServerSuccess()) {
                try {
                    Type typeList = new TypeToken<TrainingWorkout>() { // from class: com.kopin.peloton.Cloud.12
                    }.getType();
                    TrainingWorkout training = (TrainingWorkout) new Gson().fromJson(response.rawResponse, typeList);
                    if (training != null) {
                        response.result = training;
                    }
                } catch (JsonSyntaxException jse) {
                    Log.e(TAG, "get runs Json error " + jse.getMessage());
                }
            }
        } catch (IOException ioe) {
            Log.e(TAG, "get runs error " + ioe.getMessage());
        }
        return response;
    }

    private static Gson getGsonForTrainingHeader() {
        ExclusionStrategy exclusionStrategy = new ExclusionStrategy() { // from class: com.kopin.peloton.Cloud.13
            @Override // com.google.gson.ExclusionStrategy
            public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                return fieldAttributes.getName().equals("Segments");
            }

            @Override // com.google.gson.ExclusionStrategy
            public boolean shouldSkipClass(Class<?> aClass) {
                return false;
            }
        };
        return new GsonBuilder().addSerializationExclusionStrategy(exclusionStrategy).create();
    }

    public static PelotonResponse addTrainingHeader(TrainingWorkout training) {
        PelotonResponse response = new PelotonResponse();
        try {
            String body = getGsonForTrainingHeader().toJson(training);
            String url = PROTOCOL + HOST + URL_ADD_TRAINING;
            HttpURLConnection connection = preparePostConnection(url, body);
            response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = preparePostConnection(url, body);
                response = getResponse(connection2);
                authenticationCallback(response);
            }
            if (response.isServerSuccess()) {
                try {
                    TrainingWorkout responseTraining = (TrainingWorkout) new Gson().fromJson(response.rawResponse, TrainingWorkout.class);
                    if (responseTraining != null && responseTraining.TrainingId != null && !responseTraining.TrainingId.isEmpty()) {
                        response.result = responseTraining;
                    }
                } catch (JsonSyntaxException jse) {
                    Log.e(TAG, "add training Json error " + jse.getMessage());
                }
            }
        } catch (IOException ioe) {
            Log.e(TAG, "add training error " + ioe.getMessage());
        }
        return response;
    }

    static PelotonResponse addTrainingData(TrainingWorkout training, boolean writeToDebugFile) {
        DataOutputStream request;
        PelotonResponse pelotonResponse = new PelotonResponse();
        if (training != null) {
            HttpURLConnection httpUrlConnection = null;
            try {
                try {
                    URL url = new URL(PROTOCOL + HOST + URL_TRAINING_DATA + training.TrainingId);
                    HttpURLConnection httpUrlConnection2 = (HttpURLConnection) url.openConnection();
                    httpUrlConnection2.setUseCaches(false);
                    httpUrlConnection2.setDoOutput(true);
                    httpUrlConnection2.setRequestMethod(HttpRequest.METHOD_POST);
                    httpUrlConnection2.setRequestProperty("Connection", "Keep-Alive");
                    httpUrlConnection2.setRequestProperty(HttpRequest.HEADER_CACHE_CONTROL, "no-cache");
                    httpUrlConnection2.setRequestProperty("Content-Type", "multipart/form-data;boundary=*****");
                    setHeader(httpUrlConnection2);
                    if (writeToDebugFile) {
                        File sdCard = Environment.getExternalStorageDirectory();
                        request = new DataOutputStream(new FileOutputStream(new File(sdCard, "data_string.txt")));
                    } else {
                        request = new DataOutputStream(httpUrlConnection2.getOutputStream());
                    }
                    request.writeBytes("--*****" + VCardBuilder.VCARD_END_OF_LINE);
                    request.writeBytes("Content-Disposition: form-data; name=\"" + KEY_TRAINING_DATA + "\";filename=\"trainingData\"" + VCardBuilder.VCARD_END_OF_LINE);
                    request.writeBytes(VCardBuilder.VCARD_END_OF_LINE);
                    try {
                        Gson gson = new Gson();
                        String body = gson.toJson(training, TrainingWorkout.class);
                        request.write(body.getBytes(CHARSET_UTF8));
                    } catch (IOException ioe) {
                        Log.w(TAG, "Training upload error with training data json " + ioe.getMessage());
                    }
                    request.writeBytes(VCardBuilder.VCARD_END_OF_LINE);
                    request.writeBytes("--*****--" + VCardBuilder.VCARD_END_OF_LINE);
                    request.flush();
                    request.close();
                    int httpCode = httpUrlConnection2.getResponseCode();
                    Log.i(TAG, "Training data uploaded data, http " + httpCode);
                    pelotonResponse.httpCode = httpCode;
                    if (httpCode != 200) {
                        InputStream responseStream = new BufferedInputStream((httpCode < 200 || httpCode >= 300) ? httpUrlConnection2.getErrorStream() : httpUrlConnection2.getInputStream());
                        BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
                        StringBuilder stringBuilder = new StringBuilder();
                        while (true) {
                            String line = responseStreamReader.readLine();
                            if (line == null) {
                                break;
                            }
                            stringBuilder.append(line).append("\n");
                        }
                        Log.e(TAG, "Training data upload failed " + httpCode);
                        Log.e(TAG, stringBuilder.toString());
                        pelotonResponse.rawResponse = stringBuilder.toString();
                        processServerErrorResponse(pelotonResponse);
                        responseStreamReader.close();
                        responseStream.close();
                    } else {
                        TrainingWorkout workout = new TrainingWorkout();
                        pelotonResponse.rawResponse = "{}";
                        workout.TrainingId = training.TrainingId;
                        pelotonResponse.result = workout;
                    }
                    if (httpUrlConnection2 != null) {
                        httpUrlConnection2.disconnect();
                    }
                } catch (Exception ex) {
                    Log.e(TAG, "Training upload error " + ex.getMessage());
                    ex.printStackTrace();
                    if (0 != 0) {
                        httpUrlConnection.disconnect();
                    }
                }
            } catch (Throwable th) {
                if (0 != 0) {
                    httpUrlConnection.disconnect();
                }
                throw th;
            }
        }
        return pelotonResponse;
    }

    static PelotonResponse removeTraining(String trainingId) {
        PelotonResponse response = new PelotonResponse();
        try {
            String url = PROTOCOL + HOST + URL_REMOVE_TRAINING + trainingId;
            HttpURLConnection connection = prepareDeleteConnection(url);
            response = getResponse(connection);
            if (reauthenticationAndRetryCheck(response)) {
                HttpURLConnection connection2 = prepareDeleteConnection(url);
                response = getResponse(connection2);
                authenticationCallback(response);
            }
            if (response.isServerSuccess()) {
                try {
                    TrainingWorkout removedRun = (TrainingWorkout) new Gson().fromJson(response.rawResponse, TrainingWorkout.class);
                    if (removedRun != null) {
                        response.result = removedRun;
                    }
                } catch (JsonSyntaxException e) {
                }
            }
        } catch (IOException ioe) {
            Log.e(TAG, "removed Training error " + ioe.getMessage());
        }
        return response;
    }
}
