package com.facebook;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import com.digits.sdk.vcard.VCardBuilder;
import com.digits.sdk.vcard.VCardConfig;
import com.facebook.GraphRequestBatch;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.internal.AttributionIdentifiers;
import com.facebook.internal.InternalSettings;
import com.facebook.internal.Logger;
import com.facebook.internal.NativeProtocol;
import com.facebook.internal.ServerProtocol;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import com.facebook.share.internal.OpenGraphJSONUtility;
import com.facebook.share.internal.ShareConstants;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.model.SharePhoto;
import com.ua.sdk.datapoint.BaseDataTypes;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes41.dex */
public class GraphRequest {
    private static final String ACCEPT_LANGUAGE_HEADER = "Accept-Language";
    public static final String ACCESS_TOKEN_PARAM = "access_token";
    private static final String ATTACHED_FILES_PARAM = "attached_files";
    private static final String ATTACHMENT_FILENAME_PREFIX = "file";
    private static final String BATCH_APP_ID_PARAM = "batch_app_id";
    private static final String BATCH_BODY_PARAM = "body";
    private static final String BATCH_ENTRY_DEPENDS_ON_PARAM = "depends_on";
    private static final String BATCH_ENTRY_NAME_PARAM = "name";
    private static final String BATCH_ENTRY_OMIT_RESPONSE_ON_SUCCESS_PARAM = "omit_response_on_success";
    private static final String BATCH_METHOD_PARAM = "method";
    private static final String BATCH_PARAM = "batch";
    private static final String BATCH_RELATIVE_URL_PARAM = "relative_url";
    private static final String CAPTION_PARAM = "caption";
    private static final String CONTENT_ENCODING_HEADER = "Content-Encoding";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String DEBUG_KEY = "__debug__";
    private static final String DEBUG_MESSAGES_KEY = "messages";
    private static final String DEBUG_MESSAGE_KEY = "message";
    private static final String DEBUG_MESSAGE_LINK_KEY = "link";
    private static final String DEBUG_MESSAGE_TYPE_KEY = "type";
    private static final String DEBUG_PARAM = "debug";
    private static final String DEBUG_SEVERITY_INFO = "info";
    private static final String DEBUG_SEVERITY_WARNING = "warning";
    public static final String FIELDS_PARAM = "fields";
    private static final String FORMAT_JSON = "json";
    private static final String FORMAT_PARAM = "format";
    private static final String GRAPH_PATH_FORMAT = "%s/%s";
    private static final String ISO_8601_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final int MAXIMUM_BATCH_SIZE = 50;
    private static final String ME = "me";
    private static final String MIME_BOUNDARY = "3i2ndDfv2rTHiSisAbouNdArYfORhtTPEefj3q2f";
    private static final String MY_FRIENDS = "me/friends";
    private static final String MY_PHOTOS = "me/photos";
    private static final String PICTURE_PARAM = "picture";
    private static final String SDK_ANDROID = "android";
    private static final String SDK_PARAM = "sdk";
    private static final String SEARCH = "search";
    private static final String USER_AGENT_BASE = "FBAndroidSDK";
    private static final String USER_AGENT_HEADER = "User-Agent";
    private static final String VIDEOS_SUFFIX = "/videos";
    private static String defaultBatchApplicationId;
    private static volatile String userAgent;
    private AccessToken accessToken;
    private String batchEntryDependsOn;
    private String batchEntryName;
    private boolean batchEntryOmitResultOnSuccess;
    private Callback callback;
    private JSONObject graphObject;
    private String graphPath;
    private HttpMethod httpMethod;
    private String overriddenURL;
    private Bundle parameters;
    private boolean skipClientToken;
    private Object tag;
    private String version;
    public static final String TAG = GraphRequest.class.getSimpleName();
    private static Pattern versionPattern = Pattern.compile("^/?v\\d+\\.\\d+/(.*)");

    public interface Callback {
        void onCompleted(GraphResponse graphResponse);
    }

    public interface GraphJSONArrayCallback {
        void onCompleted(JSONArray jSONArray, GraphResponse graphResponse);
    }

    public interface GraphJSONObjectCallback {
        void onCompleted(JSONObject jSONObject, GraphResponse graphResponse);
    }

    private interface KeyValueSerializer {
        void writeString(String str, String str2) throws IOException;
    }

    public interface OnProgressCallback extends Callback {
        void onProgress(long j, long j2);
    }

    public GraphRequest() {
        this(null, null, null, null, null);
    }

    public GraphRequest(AccessToken accessToken, String graphPath) {
        this(accessToken, graphPath, null, null, null);
    }

    public GraphRequest(AccessToken accessToken, String graphPath, Bundle parameters, HttpMethod httpMethod) {
        this(accessToken, graphPath, parameters, httpMethod, null);
    }

    public GraphRequest(AccessToken accessToken, String graphPath, Bundle parameters, HttpMethod httpMethod, Callback callback) {
        this(accessToken, graphPath, parameters, httpMethod, callback, null);
    }

    public GraphRequest(AccessToken accessToken, String graphPath, Bundle parameters, HttpMethod httpMethod, Callback callback, String version) {
        this.batchEntryOmitResultOnSuccess = true;
        this.skipClientToken = false;
        this.accessToken = accessToken;
        this.graphPath = graphPath;
        this.version = version;
        setCallback(callback);
        setHttpMethod(httpMethod);
        if (parameters != null) {
            this.parameters = new Bundle(parameters);
        } else {
            this.parameters = new Bundle();
        }
        if (this.version == null) {
            this.version = ServerProtocol.getAPIVersion();
        }
    }

    GraphRequest(AccessToken accessToken, URL overriddenURL) {
        this.batchEntryOmitResultOnSuccess = true;
        this.skipClientToken = false;
        this.accessToken = accessToken;
        this.overriddenURL = overriddenURL.toString();
        setHttpMethod(HttpMethod.GET);
        this.parameters = new Bundle();
    }

    public static GraphRequest newDeleteObjectRequest(AccessToken accessToken, String id, Callback callback) {
        return new GraphRequest(accessToken, id, null, HttpMethod.DELETE, callback);
    }

    public static GraphRequest newMeRequest(AccessToken accessToken, final GraphJSONObjectCallback callback) {
        Callback wrapper = new Callback() { // from class: com.facebook.GraphRequest.1
            @Override // com.facebook.GraphRequest.Callback
            public void onCompleted(GraphResponse response) {
                if (callback != null) {
                    callback.onCompleted(response.getJSONObject(), response);
                }
            }
        };
        return new GraphRequest(accessToken, ME, null, null, wrapper);
    }

    public static GraphRequest newPostRequest(AccessToken accessToken, String graphPath, JSONObject graphObject, Callback callback) {
        GraphRequest request = new GraphRequest(accessToken, graphPath, null, HttpMethod.POST, callback);
        request.setGraphObject(graphObject);
        return request;
    }

    public static GraphRequest newMyFriendsRequest(AccessToken accessToken, final GraphJSONArrayCallback callback) {
        Callback wrapper = new Callback() { // from class: com.facebook.GraphRequest.2
            @Override // com.facebook.GraphRequest.Callback
            public void onCompleted(GraphResponse response) {
                if (callback != null) {
                    JSONObject result = response.getJSONObject();
                    JSONArray data = result != null ? result.optJSONArray(ShareConstants.WEB_DIALOG_PARAM_DATA) : null;
                    callback.onCompleted(data, response);
                }
            }
        };
        return new GraphRequest(accessToken, MY_FRIENDS, null, null, wrapper);
    }

    public static GraphRequest newGraphPathRequest(AccessToken accessToken, String graphPath, Callback callback) {
        return new GraphRequest(accessToken, graphPath, null, null, callback);
    }

    public static GraphRequest newPlacesSearchRequest(AccessToken accessToken, Location location, int radiusInMeters, int resultsLimit, String searchText, final GraphJSONArrayCallback callback) {
        if (location == null && Utility.isNullOrEmpty(searchText)) {
            throw new FacebookException("Either location or searchText must be specified.");
        }
        Bundle parameters = new Bundle(5);
        parameters.putString("type", "place");
        parameters.putInt("limit", resultsLimit);
        if (location != null) {
            parameters.putString("center", String.format(Locale.US, "%f,%f", Double.valueOf(location.getLatitude()), Double.valueOf(location.getLongitude())));
            parameters.putInt(BaseDataTypes.ID_DISTANCE, radiusInMeters);
        }
        if (!Utility.isNullOrEmpty(searchText)) {
            parameters.putString("q", searchText);
        }
        Callback wrapper = new Callback() { // from class: com.facebook.GraphRequest.3
            @Override // com.facebook.GraphRequest.Callback
            public void onCompleted(GraphResponse response) {
                if (callback != null) {
                    JSONObject result = response.getJSONObject();
                    JSONArray data = result != null ? result.optJSONArray(ShareConstants.WEB_DIALOG_PARAM_DATA) : null;
                    callback.onCompleted(data, response);
                }
            }
        };
        return new GraphRequest(accessToken, "search", parameters, HttpMethod.GET, wrapper);
    }

    public static GraphRequest newUploadPhotoRequest(AccessToken accessToken, String graphPath, Bitmap image, String caption, Bundle params, Callback callback) {
        String graphPath2 = getDefaultPhotoPathIfNull(graphPath);
        Bundle parameters = new Bundle();
        if (params != null) {
            parameters.putAll(params);
        }
        parameters.putParcelable("picture", image);
        if (caption != null && !caption.isEmpty()) {
            parameters.putString("caption", caption);
        }
        return new GraphRequest(accessToken, graphPath2, parameters, HttpMethod.POST, callback);
    }

    public static GraphRequest newUploadPhotoRequest(AccessToken accessToken, String graphPath, File file, String caption, Bundle params, Callback callback) throws FileNotFoundException {
        String graphPath2 = getDefaultPhotoPathIfNull(graphPath);
        ParcelFileDescriptor descriptor = ParcelFileDescriptor.open(file, VCardConfig.FLAG_REFRAIN_QP_TO_NAME_PROPERTIES);
        Bundle parameters = new Bundle();
        if (params != null) {
            parameters.putAll(params);
        }
        parameters.putParcelable("picture", descriptor);
        if (caption != null && !caption.isEmpty()) {
            parameters.putString("caption", caption);
        }
        return new GraphRequest(accessToken, graphPath2, parameters, HttpMethod.POST, callback);
    }

    public static GraphRequest newUploadPhotoRequest(AccessToken accessToken, String graphPath, Uri photoUri, String caption, Bundle params, Callback callback) throws FileNotFoundException {
        String graphPath2 = getDefaultPhotoPathIfNull(graphPath);
        if (Utility.isFileUri(photoUri)) {
            return newUploadPhotoRequest(accessToken, graphPath2, new File(photoUri.getPath()), caption, params, callback);
        }
        if (!Utility.isContentUri(photoUri)) {
            throw new FacebookException("The photo Uri must be either a file:// or content:// Uri");
        }
        Bundle parameters = new Bundle();
        if (params != null) {
            parameters.putAll(params);
        }
        parameters.putParcelable("picture", photoUri);
        return new GraphRequest(accessToken, graphPath2, parameters, HttpMethod.POST, callback);
    }

    public static GraphRequest newCustomAudienceThirdPartyIdRequest(AccessToken accessToken, Context context, String applicationId, Callback callback) {
        if (applicationId == null && accessToken != null) {
            applicationId = accessToken.getApplicationId();
        }
        if (applicationId == null) {
            applicationId = Utility.getMetadataApplicationId(context);
        }
        if (applicationId == null) {
            throw new FacebookException("Facebook App ID cannot be determined");
        }
        String endpoint = applicationId + "/custom_audience_third_party_id";
        AttributionIdentifiers attributionIdentifiers = AttributionIdentifiers.getAttributionIdentifiers(context);
        Bundle parameters = new Bundle();
        if (accessToken == null) {
            if (attributionIdentifiers == null) {
                throw new FacebookException("There is no access token and attribution identifiers could not be retrieved");
            }
            String udid = attributionIdentifiers.getAttributionId() != null ? attributionIdentifiers.getAttributionId() : attributionIdentifiers.getAndroidAdvertiserId();
            if (attributionIdentifiers.getAttributionId() != null) {
                parameters.putString("udid", udid);
            }
        }
        if (FacebookSdk.getLimitEventAndDataUsage(context) || (attributionIdentifiers != null && attributionIdentifiers.isTrackingLimited())) {
            parameters.putString("limit_event_usage", AppEventsConstants.EVENT_PARAM_VALUE_YES);
        }
        return new GraphRequest(accessToken, endpoint, parameters, HttpMethod.GET, callback);
    }

    public static GraphRequest newCustomAudienceThirdPartyIdRequest(AccessToken accessToken, Context context, Callback callback) {
        return newCustomAudienceThirdPartyIdRequest(accessToken, context, null, callback);
    }

    public final JSONObject getGraphObject() {
        return this.graphObject;
    }

    public final void setGraphObject(JSONObject graphObject) {
        this.graphObject = graphObject;
    }

    public final String getGraphPath() {
        return this.graphPath;
    }

    public final void setGraphPath(String graphPath) {
        this.graphPath = graphPath;
    }

    public final HttpMethod getHttpMethod() {
        return this.httpMethod;
    }

    public final void setHttpMethod(HttpMethod httpMethod) {
        if (this.overriddenURL != null && httpMethod != HttpMethod.GET) {
            throw new FacebookException("Can't change HTTP method on request with overridden URL.");
        }
        if (httpMethod == null) {
            httpMethod = HttpMethod.GET;
        }
        this.httpMethod = httpMethod;
    }

    public final String getVersion() {
        return this.version;
    }

    public final void setVersion(String version) {
        this.version = version;
    }

    public final void setSkipClientToken(boolean skipClientToken) {
        this.skipClientToken = skipClientToken;
    }

    public final Bundle getParameters() {
        return this.parameters;
    }

    public final void setParameters(Bundle parameters) {
        this.parameters = parameters;
    }

    public final AccessToken getAccessToken() {
        return this.accessToken;
    }

    public final void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public final String getBatchEntryName() {
        return this.batchEntryName;
    }

    public final void setBatchEntryName(String batchEntryName) {
        this.batchEntryName = batchEntryName;
    }

    public final String getBatchEntryDependsOn() {
        return this.batchEntryDependsOn;
    }

    public final void setBatchEntryDependsOn(String batchEntryDependsOn) {
        this.batchEntryDependsOn = batchEntryDependsOn;
    }

    public final boolean getBatchEntryOmitResultOnSuccess() {
        return this.batchEntryOmitResultOnSuccess;
    }

    public final void setBatchEntryOmitResultOnSuccess(boolean batchEntryOmitResultOnSuccess) {
        this.batchEntryOmitResultOnSuccess = batchEntryOmitResultOnSuccess;
    }

    public static final String getDefaultBatchApplicationId() {
        return defaultBatchApplicationId;
    }

    public static final void setDefaultBatchApplicationId(String applicationId) {
        defaultBatchApplicationId = applicationId;
    }

    public final Callback getCallback() {
        return this.callback;
    }

    public final void setCallback(final Callback callback) {
        if (FacebookSdk.isLoggingBehaviorEnabled(LoggingBehavior.GRAPH_API_DEBUG_INFO) || FacebookSdk.isLoggingBehaviorEnabled(LoggingBehavior.GRAPH_API_DEBUG_WARNING)) {
            Callback wrapper = new Callback() { // from class: com.facebook.GraphRequest.4
                @Override // com.facebook.GraphRequest.Callback
                public void onCompleted(GraphResponse response) {
                    JSONObject responseObject = response.getJSONObject();
                    JSONObject debug = responseObject != null ? responseObject.optJSONObject(GraphRequest.DEBUG_KEY) : null;
                    JSONArray debugMessages = debug != null ? debug.optJSONArray("messages") : null;
                    if (debugMessages != null) {
                        for (int i = 0; i < debugMessages.length(); i++) {
                            JSONObject debugMessageObject = debugMessages.optJSONObject(i);
                            String debugMessage = debugMessageObject != null ? debugMessageObject.optString("message") : null;
                            String debugMessageType = debugMessageObject != null ? debugMessageObject.optString("type") : null;
                            String debugMessageLink = debugMessageObject != null ? debugMessageObject.optString("link") : null;
                            if (debugMessage != null && debugMessageType != null) {
                                LoggingBehavior behavior = LoggingBehavior.GRAPH_API_DEBUG_INFO;
                                if (debugMessageType.equals(GraphRequest.DEBUG_SEVERITY_WARNING)) {
                                    behavior = LoggingBehavior.GRAPH_API_DEBUG_WARNING;
                                }
                                if (!Utility.isNullOrEmpty(debugMessageLink)) {
                                    debugMessage = debugMessage + " Link: " + debugMessageLink;
                                }
                                Logger.log(behavior, GraphRequest.TAG, debugMessage);
                            }
                        }
                    }
                    if (callback != null) {
                        callback.onCompleted(response);
                    }
                }
            };
            this.callback = wrapper;
        } else {
            this.callback = callback;
        }
    }

    public final void setTag(Object tag) {
        this.tag = tag;
    }

    public final Object getTag() {
        return this.tag;
    }

    public final GraphResponse executeAndWait() {
        return executeAndWait(this);
    }

    public final GraphRequestAsyncTask executeAsync() {
        return executeBatchAsync(this);
    }

    public static HttpURLConnection toHttpConnection(GraphRequest... requests) {
        return toHttpConnection(Arrays.asList(requests));
    }

    public static HttpURLConnection toHttpConnection(Collection<GraphRequest> requests) {
        Validate.notEmptyAndContainsNoNulls(requests, "requests");
        return toHttpConnection(new GraphRequestBatch(requests));
    }

    public static HttpURLConnection toHttpConnection(GraphRequestBatch requests) {
        URL url;
        validateFieldsParamForGetRequests(requests);
        try {
            if (requests.size() == 1) {
                GraphRequest request = requests.get(0);
                url = new URL(request.getUrlForSingleRequest());
            } else {
                url = new URL(ServerProtocol.getGraphUrlBase());
            }
            HttpURLConnection connection = null;
            try {
                connection = createConnection(url);
                serializeToUrlConnection(requests, connection);
                return connection;
            } catch (IOException | JSONException e) {
                Utility.disconnectQuietly(connection);
                throw new FacebookException("could not construct request body", e);
            }
        } catch (MalformedURLException e2) {
            throw new FacebookException("could not construct URL for request", e2);
        }
    }

    public static GraphResponse executeAndWait(GraphRequest request) {
        List<GraphResponse> responses = executeBatchAndWait(request);
        if (responses == null || responses.size() != 1) {
            throw new FacebookException("invalid state: expected a single response");
        }
        return responses.get(0);
    }

    public static List<GraphResponse> executeBatchAndWait(GraphRequest... requests) {
        Validate.notNull(requests, "requests");
        return executeBatchAndWait(Arrays.asList(requests));
    }

    public static List<GraphResponse> executeBatchAndWait(Collection<GraphRequest> requests) {
        return executeBatchAndWait(new GraphRequestBatch(requests));
    }

    public static List<GraphResponse> executeBatchAndWait(GraphRequestBatch requests) {
        List<GraphResponse> responses;
        Validate.notEmptyAndContainsNoNulls(requests, "requests");
        HttpURLConnection connection = null;
        try {
            connection = toHttpConnection(requests);
            responses = executeConnectionAndWait(connection, requests);
        } catch (Exception ex) {
            responses = GraphResponse.constructErrorResponses(requests.getRequests(), null, new FacebookException(ex));
            runCallbacks(requests, responses);
        } finally {
            Utility.disconnectQuietly(connection);
        }
        return responses;
    }

    public static GraphRequestAsyncTask executeBatchAsync(GraphRequest... requests) {
        Validate.notNull(requests, "requests");
        return executeBatchAsync(Arrays.asList(requests));
    }

    public static GraphRequestAsyncTask executeBatchAsync(Collection<GraphRequest> requests) {
        return executeBatchAsync(new GraphRequestBatch(requests));
    }

    public static GraphRequestAsyncTask executeBatchAsync(GraphRequestBatch requests) {
        Validate.notEmptyAndContainsNoNulls(requests, "requests");
        GraphRequestAsyncTask asyncTask = new GraphRequestAsyncTask(requests);
        asyncTask.executeOnExecutor(FacebookSdk.getExecutor(), new Void[0]);
        return asyncTask;
    }

    public static List<GraphResponse> executeConnectionAndWait(HttpURLConnection connection, Collection<GraphRequest> requests) {
        return executeConnectionAndWait(connection, new GraphRequestBatch(requests));
    }

    public static List<GraphResponse> executeConnectionAndWait(HttpURLConnection connection, GraphRequestBatch requests) {
        List<GraphResponse> responses = GraphResponse.fromHttpConnection(connection, requests);
        Utility.disconnectQuietly(connection);
        int numRequests = requests.size();
        if (numRequests != responses.size()) {
            throw new FacebookException(String.format(Locale.US, "Received %d responses while expecting %d", Integer.valueOf(responses.size()), Integer.valueOf(numRequests)));
        }
        runCallbacks(requests, responses);
        AccessTokenManager.getInstance().extendAccessTokenIfNeeded();
        return responses;
    }

    public static GraphRequestAsyncTask executeConnectionAsync(HttpURLConnection connection, GraphRequestBatch requests) {
        return executeConnectionAsync(null, connection, requests);
    }

    public static GraphRequestAsyncTask executeConnectionAsync(Handler callbackHandler, HttpURLConnection connection, GraphRequestBatch requests) {
        Validate.notNull(connection, "connection");
        GraphRequestAsyncTask asyncTask = new GraphRequestAsyncTask(connection, requests);
        requests.setCallbackHandler(callbackHandler);
        asyncTask.executeOnExecutor(FacebookSdk.getExecutor(), new Void[0]);
        return asyncTask;
    }

    public String toString() {
        return "{Request:  accessToken: " + (this.accessToken == null ? "null" : this.accessToken) + ", graphPath: " + this.graphPath + ", graphObject: " + this.graphObject + ", httpMethod: " + this.httpMethod + ", parameters: " + this.parameters + "}";
    }

    static void runCallbacks(final GraphRequestBatch requests, List<GraphResponse> responses) {
        int numRequests = requests.size();
        final ArrayList<Pair<Callback, GraphResponse>> callbacks = new ArrayList<>();
        for (int i = 0; i < numRequests; i++) {
            GraphRequest request = requests.get(i);
            if (request.callback != null) {
                callbacks.add(new Pair<>(request.callback, responses.get(i)));
            }
        }
        if (callbacks.size() > 0) {
            Runnable runnable = new Runnable() { // from class: com.facebook.GraphRequest.5
                @Override // java.lang.Runnable
                public void run() {
                    for (Pair<Callback, GraphResponse> pair : callbacks) {
                        ((Callback) pair.first).onCompleted((GraphResponse) pair.second);
                    }
                    List<GraphRequestBatch.Callback> batchCallbacks = requests.getCallbacks();
                    for (GraphRequestBatch.Callback batchCallback : batchCallbacks) {
                        batchCallback.onBatchCompleted(requests);
                    }
                }
            };
            Handler callbackHandler = requests.getCallbackHandler();
            if (callbackHandler == null) {
                runnable.run();
            } else {
                callbackHandler.post(runnable);
            }
        }
    }

    private static String getDefaultPhotoPathIfNull(String graphPath) {
        return graphPath == null ? "me/photos" : graphPath;
    }

    private static HttpURLConnection createConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent", getUserAgent());
        connection.setRequestProperty(ACCEPT_LANGUAGE_HEADER, Locale.getDefault().toString());
        connection.setChunkedStreamingMode(0);
        return connection;
    }

    private void addCommonParameters() {
        if (this.accessToken != null) {
            if (!this.parameters.containsKey("access_token")) {
                String token = this.accessToken.getToken();
                Logger.registerAccessToken(token);
                this.parameters.putString("access_token", token);
            }
        } else if (!this.skipClientToken && !this.parameters.containsKey("access_token")) {
            String appID = FacebookSdk.getApplicationId();
            String clientToken = FacebookSdk.getClientToken();
            if (!Utility.isNullOrEmpty(appID) && !Utility.isNullOrEmpty(clientToken)) {
                String accessToken = appID + "|" + clientToken;
                this.parameters.putString("access_token", accessToken);
            } else {
                Log.d(TAG, "Warning: Request without access token missing application ID or client token.");
            }
        }
        this.parameters.putString("sdk", "android");
        this.parameters.putString(FORMAT_PARAM, FORMAT_JSON);
        if (FacebookSdk.isLoggingBehaviorEnabled(LoggingBehavior.GRAPH_API_DEBUG_INFO)) {
            this.parameters.putString("debug", DEBUG_SEVERITY_INFO);
        } else if (FacebookSdk.isLoggingBehaviorEnabled(LoggingBehavior.GRAPH_API_DEBUG_WARNING)) {
            this.parameters.putString("debug", DEBUG_SEVERITY_WARNING);
        }
    }

    private String appendParametersToBaseUrl(String baseUrl) {
        Uri.Builder uriBuilder = new Uri.Builder().encodedPath(baseUrl);
        Set<String> keys = this.parameters.keySet();
        for (String key : keys) {
            Object value = this.parameters.get(key);
            if (value == null) {
                value = "";
            }
            if (!isSupportedParameterType(value)) {
                if (this.httpMethod == HttpMethod.GET) {
                    throw new IllegalArgumentException(String.format(Locale.US, "Unsupported parameter type for GET request: %s", value.getClass().getSimpleName()));
                }
            } else {
                uriBuilder.appendQueryParameter(key, parameterToString(value).toString());
            }
        }
        return uriBuilder.toString();
    }

    final String getUrlForBatchedRequest() {
        if (this.overriddenURL != null) {
            throw new FacebookException("Can't override URL for a batch request");
        }
        String baseUrl = getGraphPathWithVersion();
        addCommonParameters();
        return appendParametersToBaseUrl(baseUrl);
    }

    final String getUrlForSingleRequest() {
        String graphBaseUrlBase;
        if (this.overriddenURL != null) {
            return this.overriddenURL.toString();
        }
        if (getHttpMethod() == HttpMethod.POST && this.graphPath != null && this.graphPath.endsWith(VIDEOS_SUFFIX)) {
            graphBaseUrlBase = ServerProtocol.getGraphVideoUrlBase();
        } else {
            graphBaseUrlBase = ServerProtocol.getGraphUrlBase();
        }
        String baseUrl = String.format(GRAPH_PATH_FORMAT, graphBaseUrlBase, getGraphPathWithVersion());
        addCommonParameters();
        return appendParametersToBaseUrl(baseUrl);
    }

    private String getGraphPathWithVersion() {
        Matcher matcher = versionPattern.matcher(this.graphPath);
        return matcher.matches() ? this.graphPath : String.format(GRAPH_PATH_FORMAT, this.version, this.graphPath);
    }

    private static class Attachment {
        private final GraphRequest request;
        private final Object value;

        public Attachment(GraphRequest request, Object value) {
            this.request = request;
            this.value = value;
        }

        public GraphRequest getRequest() {
            return this.request;
        }

        public Object getValue() {
            return this.value;
        }
    }

    private void serializeToBatch(JSONArray batch, Map<String, Attachment> attachments) throws JSONException, IOException {
        JSONObject batchEntry = new JSONObject();
        if (this.batchEntryName != null) {
            batchEntry.put("name", this.batchEntryName);
            batchEntry.put(BATCH_ENTRY_OMIT_RESPONSE_ON_SUCCESS_PARAM, this.batchEntryOmitResultOnSuccess);
        }
        if (this.batchEntryDependsOn != null) {
            batchEntry.put(BATCH_ENTRY_DEPENDS_ON_PARAM, this.batchEntryDependsOn);
        }
        String relativeURL = getUrlForBatchedRequest();
        batchEntry.put(BATCH_RELATIVE_URL_PARAM, relativeURL);
        batchEntry.put(BATCH_METHOD_PARAM, this.httpMethod);
        if (this.accessToken != null) {
            String token = this.accessToken.getToken();
            Logger.registerAccessToken(token);
        }
        ArrayList<String> attachmentNames = new ArrayList<>();
        Set<String> keys = this.parameters.keySet();
        for (String key : keys) {
            Object value = this.parameters.get(key);
            if (isSupportedAttachmentType(value)) {
                String name = String.format(Locale.ROOT, "%s%d", ATTACHMENT_FILENAME_PREFIX, Integer.valueOf(attachments.size()));
                attachmentNames.add(name);
                attachments.put(name, new Attachment(this, value));
            }
        }
        if (!attachmentNames.isEmpty()) {
            String attachmentNamesString = TextUtils.join(",", attachmentNames);
            batchEntry.put(ATTACHED_FILES_PARAM, attachmentNamesString);
        }
        if (this.graphObject != null) {
            final ArrayList<String> keysAndValues = new ArrayList<>();
            processGraphObject(this.graphObject, relativeURL, new KeyValueSerializer() { // from class: com.facebook.GraphRequest.6
                @Override // com.facebook.GraphRequest.KeyValueSerializer
                public void writeString(String key2, String value2) throws IOException {
                    keysAndValues.add(String.format(Locale.US, "%s=%s", key2, URLEncoder.encode(value2, "UTF-8")));
                }
            });
            String bodyValue = TextUtils.join("&", keysAndValues);
            batchEntry.put(BATCH_BODY_PARAM, bodyValue);
        }
        batch.put(batchEntry);
    }

    private static boolean hasOnProgressCallbacks(GraphRequestBatch requests) {
        for (GraphRequestBatch.Callback callback : requests.getCallbacks()) {
            if (callback instanceof GraphRequestBatch.OnProgressCallback) {
                return true;
            }
        }
        for (GraphRequest request : requests) {
            if (request.getCallback() instanceof OnProgressCallback) {
                return true;
            }
        }
        return false;
    }

    private static void setConnectionContentType(HttpURLConnection connection, boolean shouldUseGzip) {
        if (shouldUseGzip) {
            connection.setRequestProperty("Content-Type", HttpRequest.CONTENT_TYPE_FORM);
            connection.setRequestProperty("Content-Encoding", HttpRequest.ENCODING_GZIP);
        } else {
            connection.setRequestProperty("Content-Type", getMimeContentType());
        }
    }

    private static boolean isGzipCompressible(GraphRequestBatch requests) {
        for (GraphRequest request : requests) {
            for (String key : request.parameters.keySet()) {
                Object value = request.parameters.get(key);
                if (isSupportedAttachmentType(value)) {
                    return false;
                }
            }
        }
        return true;
    }

    static final boolean shouldWarnOnMissingFieldsParam(GraphRequest request) {
        boolean z = false;
        String version = request.getVersion();
        if (Utility.isNullOrEmpty(version)) {
            return true;
        }
        if (version.startsWith("v")) {
            version = version.substring(1);
        }
        String[] versionParts = version.split("\\.");
        if ((versionParts.length >= 2 && Integer.parseInt(versionParts[0]) > 2) || (Integer.parseInt(versionParts[0]) >= 2 && Integer.parseInt(versionParts[1]) >= 4)) {
            z = true;
        }
        return z;
    }

    static final void validateFieldsParamForGetRequests(GraphRequestBatch requests) {
        for (GraphRequest request : requests) {
            if (HttpMethod.GET.equals(request.getHttpMethod()) && shouldWarnOnMissingFieldsParam(request)) {
                Bundle params = request.getParameters();
                if (!params.containsKey(FIELDS_PARAM) || Utility.isNullOrEmpty(params.getString(FIELDS_PARAM))) {
                    Logger.log(LoggingBehavior.DEVELOPER_ERRORS, 5, "Request", "starting with Graph API v2.4, GET requests for /%s should contain an explicit \"fields\" parameter.", request.getGraphPath());
                }
            }
        }
    }

    static final void serializeToUrlConnection(GraphRequestBatch requests, HttpURLConnection connection) throws Throwable {
        Logger logger = new Logger(LoggingBehavior.REQUESTS, "Request");
        int numRequests = requests.size();
        boolean shouldUseGzip = isGzipCompressible(requests);
        HttpMethod connectionHttpMethod = numRequests == 1 ? requests.get(0).httpMethod : HttpMethod.POST;
        connection.setRequestMethod(connectionHttpMethod.name());
        setConnectionContentType(connection, shouldUseGzip);
        URL url = connection.getURL();
        logger.append("Request:\n");
        logger.appendKeyValue("Id", requests.getId());
        logger.appendKeyValue("URL", url);
        logger.appendKeyValue("Method", connection.getRequestMethod());
        logger.appendKeyValue("User-Agent", connection.getRequestProperty("User-Agent"));
        logger.appendKeyValue("Content-Type", connection.getRequestProperty("Content-Type"));
        connection.setConnectTimeout(requests.getTimeout());
        connection.setReadTimeout(requests.getTimeout());
        boolean isPost = connectionHttpMethod == HttpMethod.POST;
        if (!isPost) {
            logger.log();
            return;
        }
        connection.setDoOutput(true);
        OutputStream outputStream = null;
        try {
            OutputStream outputStream2 = new BufferedOutputStream(connection.getOutputStream());
            if (shouldUseGzip) {
                try {
                    outputStream2 = new GZIPOutputStream(outputStream2);
                } catch (Throwable th) {
                    th = th;
                    outputStream = outputStream2;
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    throw th;
                }
            }
            if (hasOnProgressCallbacks(requests)) {
                ProgressNoopOutputStream countingStream = new ProgressNoopOutputStream(requests.getCallbackHandler());
                processRequest(requests, null, numRequests, url, countingStream, shouldUseGzip);
                int max = countingStream.getMaxProgress();
                Map<GraphRequest, RequestProgress> progressMap = countingStream.getProgressMap();
                outputStream = new ProgressOutputStream(outputStream2, requests, progressMap, max);
            } else {
                outputStream = outputStream2;
            }
            processRequest(requests, logger, numRequests, url, outputStream, shouldUseGzip);
            if (outputStream != null) {
                outputStream.close();
            }
            logger.log();
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private static void processRequest(GraphRequestBatch requests, Logger logger, int numRequests, URL url, OutputStream outputStream, boolean shouldUseGzip) throws JSONException, IOException {
        Serializer serializer = new Serializer(outputStream, logger, shouldUseGzip);
        if (numRequests == 1) {
            GraphRequest request = requests.get(0);
            Map<String, Attachment> attachments = new HashMap<>();
            for (String key : request.parameters.keySet()) {
                Object value = request.parameters.get(key);
                if (isSupportedAttachmentType(value)) {
                    attachments.put(key, new Attachment(request, value));
                }
            }
            if (logger != null) {
                logger.append("  Parameters:\n");
            }
            serializeParameters(request.parameters, serializer, request);
            if (logger != null) {
                logger.append("  Attachments:\n");
            }
            serializeAttachments(attachments, serializer);
            if (request.graphObject != null) {
                processGraphObject(request.graphObject, url.getPath(), serializer);
                return;
            }
            return;
        }
        String batchAppID = getBatchAppId(requests);
        if (Utility.isNullOrEmpty(batchAppID)) {
            throw new FacebookException("App ID was not specified at the request or Settings.");
        }
        serializer.writeString(BATCH_APP_ID_PARAM, batchAppID);
        Map<String, Attachment> attachments2 = new HashMap<>();
        serializeRequestsAsJSON(serializer, requests, attachments2);
        if (logger != null) {
            logger.append("  Attachments:\n");
        }
        serializeAttachments(attachments2, serializer);
    }

    private static boolean isMeRequest(String path) {
        Matcher matcher = versionPattern.matcher(path);
        if (matcher.matches()) {
            path = matcher.group(1);
        }
        return path.startsWith("me/") || path.startsWith("/me/");
    }

    private static void processGraphObject(JSONObject graphObject, String path, KeyValueSerializer serializer) throws IOException {
        boolean isOGAction = false;
        if (isMeRequest(path)) {
            int colonLocation = path.indexOf(":");
            int questionMarkLocation = path.indexOf("?");
            isOGAction = colonLocation > 3 && (questionMarkLocation == -1 || colonLocation < questionMarkLocation);
        }
        Iterator<String> keyIterator = graphObject.keys();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            Object value = graphObject.opt(key);
            boolean passByValue = isOGAction && key.equalsIgnoreCase("image");
            processGraphObjectProperty(key, value, serializer, passByValue);
        }
    }

    public static GraphRequest createOpenGraphObject(ShareOpenGraphObject openGraphObject) throws FacebookException {
        String type = openGraphObject.getString("type");
        if (type == null) {
            type = openGraphObject.getString("og:type");
        }
        if (type == null) {
            throw new FacebookException("Open graph object type cannot be null");
        }
        try {
            JSONObject stagedObject = (JSONObject) OpenGraphJSONUtility.toJSONValue(openGraphObject, new OpenGraphJSONUtility.PhotoJSONProcessor() { // from class: com.facebook.GraphRequest.7
                @Override // com.facebook.share.internal.OpenGraphJSONUtility.PhotoJSONProcessor
                public JSONObject toJSONObject(SharePhoto photo) {
                    Uri photoUri = photo.getImageUrl();
                    JSONObject photoJSONObject = new JSONObject();
                    try {
                        photoJSONObject.put("url", photoUri.toString());
                        return photoJSONObject;
                    } catch (Exception e) {
                        throw new FacebookException("Unable to attach images", e);
                    }
                }
            });
            String ogType = type;
            Bundle parameters = new Bundle();
            parameters.putString("object", stagedObject.toString());
            String graphPath = String.format(Locale.ROOT, GRAPH_PATH_FORMAT, ME, "objects/" + ogType);
            return new GraphRequest(AccessToken.getCurrentAccessToken(), graphPath, parameters, HttpMethod.POST);
        } catch (JSONException e) {
            throw new FacebookException(e.getMessage());
        }
    }

    private static void processGraphObjectProperty(String key, Object value, KeyValueSerializer serializer, boolean passByValue) throws IOException {
        Class<?> valueClass = value.getClass();
        if (JSONObject.class.isAssignableFrom(valueClass)) {
            JSONObject jsonObject = (JSONObject) value;
            if (passByValue) {
                Iterator<String> keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String propertyName = keys.next();
                    String subKey = String.format("%s[%s]", key, propertyName);
                    processGraphObjectProperty(subKey, jsonObject.opt(propertyName), serializer, passByValue);
                }
                return;
            }
            if (jsonObject.has("id")) {
                processGraphObjectProperty(key, jsonObject.optString("id"), serializer, passByValue);
                return;
            } else if (jsonObject.has("url")) {
                processGraphObjectProperty(key, jsonObject.optString("url"), serializer, passByValue);
                return;
            } else {
                if (jsonObject.has(NativeProtocol.OPEN_GRAPH_CREATE_OBJECT_KEY)) {
                    processGraphObjectProperty(key, jsonObject.toString(), serializer, passByValue);
                    return;
                }
                return;
            }
        }
        if (JSONArray.class.isAssignableFrom(valueClass)) {
            JSONArray jsonArray = (JSONArray) value;
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                String subKey2 = String.format(Locale.ROOT, "%s[%d]", key, Integer.valueOf(i));
                processGraphObjectProperty(subKey2, jsonArray.opt(i), serializer, passByValue);
            }
            return;
        }
        if (String.class.isAssignableFrom(valueClass) || Number.class.isAssignableFrom(valueClass) || Boolean.class.isAssignableFrom(valueClass)) {
            serializer.writeString(key, value.toString());
        } else if (Date.class.isAssignableFrom(valueClass)) {
            Date date = (Date) value;
            SimpleDateFormat iso8601DateFormat = new SimpleDateFormat(ISO_8601_FORMAT_STRING, Locale.US);
            serializer.writeString(key, iso8601DateFormat.format(date));
        }
    }

    private static void serializeParameters(Bundle bundle, Serializer serializer, GraphRequest request) throws IOException {
        Set<String> keys = bundle.keySet();
        for (String key : keys) {
            Object value = bundle.get(key);
            if (isSupportedParameterType(value)) {
                serializer.writeObject(key, value, request);
            }
        }
    }

    private static void serializeAttachments(Map<String, Attachment> attachments, Serializer serializer) throws IOException {
        Set<String> keys = attachments.keySet();
        for (String key : keys) {
            Attachment attachment = attachments.get(key);
            if (isSupportedAttachmentType(attachment.getValue())) {
                serializer.writeObject(key, attachment.getValue(), attachment.getRequest());
            }
        }
    }

    private static void serializeRequestsAsJSON(Serializer serializer, Collection<GraphRequest> requests, Map<String, Attachment> attachments) throws JSONException, IOException {
        JSONArray batch = new JSONArray();
        for (GraphRequest request : requests) {
            request.serializeToBatch(batch, attachments);
        }
        serializer.writeRequestsAsJson(BATCH_PARAM, batch, requests);
    }

    private static String getMimeContentType() {
        return String.format("multipart/form-data; boundary=%s", MIME_BOUNDARY);
    }

    private static String getUserAgent() {
        if (userAgent == null) {
            userAgent = String.format("%s.%s", USER_AGENT_BASE, FacebookSdkVersion.BUILD);
            String customUserAgent = InternalSettings.getCustomUserAgent();
            if (!Utility.isNullOrEmpty(customUserAgent)) {
                userAgent = String.format(Locale.ROOT, GRAPH_PATH_FORMAT, userAgent, customUserAgent);
            }
        }
        return userAgent;
    }

    private static String getBatchAppId(GraphRequestBatch batch) {
        String applicationId;
        if (!Utility.isNullOrEmpty(batch.getBatchApplicationId())) {
            return batch.getBatchApplicationId();
        }
        for (GraphRequest request : batch) {
            AccessToken accessToken = request.accessToken;
            if (accessToken != null && (applicationId = accessToken.getApplicationId()) != null) {
                return applicationId;
            }
        }
        if (!Utility.isNullOrEmpty(defaultBatchApplicationId)) {
            return defaultBatchApplicationId;
        }
        return FacebookSdk.getApplicationId();
    }

    private static boolean isSupportedAttachmentType(Object value) {
        return (value instanceof Bitmap) || (value instanceof byte[]) || (value instanceof Uri) || (value instanceof ParcelFileDescriptor) || (value instanceof ParcelableResourceWithMimeType);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isSupportedParameterType(Object value) {
        return (value instanceof String) || (value instanceof Boolean) || (value instanceof Number) || (value instanceof Date);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String parameterToString(Object value) {
        if (value instanceof String) {
            return (String) value;
        }
        if ((value instanceof Boolean) || (value instanceof Number)) {
            return value.toString();
        }
        if (value instanceof Date) {
            SimpleDateFormat iso8601DateFormat = new SimpleDateFormat(ISO_8601_FORMAT_STRING, Locale.US);
            return iso8601DateFormat.format(value);
        }
        throw new IllegalArgumentException("Unsupported parameter type.");
    }

    private static class Serializer implements KeyValueSerializer {
        private boolean firstWrite = true;
        private final Logger logger;
        private final OutputStream outputStream;
        private boolean useUrlEncode;

        public Serializer(OutputStream outputStream, Logger logger, boolean useUrlEncode) {
            this.useUrlEncode = false;
            this.outputStream = outputStream;
            this.logger = logger;
            this.useUrlEncode = useUrlEncode;
        }

        public void writeObject(String key, Object value, GraphRequest request) throws IOException {
            if (this.outputStream instanceof RequestOutputStream) {
                ((RequestOutputStream) this.outputStream).setCurrentRequest(request);
            }
            if (GraphRequest.isSupportedParameterType(value)) {
                writeString(key, GraphRequest.parameterToString(value));
                return;
            }
            if (value instanceof Bitmap) {
                writeBitmap(key, (Bitmap) value);
                return;
            }
            if (value instanceof byte[]) {
                writeBytes(key, (byte[]) value);
                return;
            }
            if (value instanceof Uri) {
                writeContentUri(key, (Uri) value, null);
                return;
            }
            if (value instanceof ParcelFileDescriptor) {
                writeFile(key, (ParcelFileDescriptor) value, null);
                return;
            }
            if (value instanceof ParcelableResourceWithMimeType) {
                ParcelableResourceWithMimeType resourceWithMimeType = (ParcelableResourceWithMimeType) value;
                Parcelable resource = resourceWithMimeType.getResource();
                String mimeType = resourceWithMimeType.getMimeType();
                if (resource instanceof ParcelFileDescriptor) {
                    writeFile(key, (ParcelFileDescriptor) resource, mimeType);
                    return;
                } else {
                    if (resource instanceof Uri) {
                        writeContentUri(key, (Uri) resource, mimeType);
                        return;
                    }
                    throw getInvalidTypeError();
                }
            }
            throw getInvalidTypeError();
        }

        private RuntimeException getInvalidTypeError() {
            return new IllegalArgumentException("value is not a supported type.");
        }

        public void writeRequestsAsJson(String key, JSONArray requestJsonArray, Collection<GraphRequest> requests) throws JSONException, IOException {
            if (!(this.outputStream instanceof RequestOutputStream)) {
                writeString(key, requestJsonArray.toString());
                return;
            }
            RequestOutputStream requestOutputStream = (RequestOutputStream) this.outputStream;
            writeContentDisposition(key, null, null);
            write("[", new Object[0]);
            int i = 0;
            for (GraphRequest request : requests) {
                JSONObject requestJson = requestJsonArray.getJSONObject(i);
                requestOutputStream.setCurrentRequest(request);
                if (i > 0) {
                    write(",%s", requestJson.toString());
                } else {
                    write("%s", requestJson.toString());
                }
                i++;
            }
            write("]", new Object[0]);
            if (this.logger != null) {
                this.logger.appendKeyValue("    " + key, requestJsonArray.toString());
            }
        }

        @Override // com.facebook.GraphRequest.KeyValueSerializer
        public void writeString(String key, String value) throws IOException {
            writeContentDisposition(key, null, null);
            writeLine("%s", value);
            writeRecordBoundary();
            if (this.logger != null) {
                this.logger.appendKeyValue("    " + key, value);
            }
        }

        public void writeBitmap(String key, Bitmap bitmap) throws IOException {
            writeContentDisposition(key, key, "image/png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, this.outputStream);
            writeLine("", new Object[0]);
            writeRecordBoundary();
            if (this.logger != null) {
                this.logger.appendKeyValue("    " + key, "<Image>");
            }
        }

        public void writeBytes(String key, byte[] bytes) throws IOException {
            writeContentDisposition(key, key, "content/unknown");
            this.outputStream.write(bytes);
            writeLine("", new Object[0]);
            writeRecordBoundary();
            if (this.logger != null) {
                this.logger.appendKeyValue("    " + key, String.format(Locale.ROOT, "<Data: %d>", Integer.valueOf(bytes.length)));
            }
        }

        public void writeContentUri(String key, Uri contentUri, String mimeType) throws IOException {
            if (mimeType == null) {
                mimeType = "content/unknown";
            }
            writeContentDisposition(key, key, mimeType);
            int totalBytes = 0;
            if (this.outputStream instanceof ProgressNoopOutputStream) {
                long contentSize = Utility.getContentSize(contentUri);
                ((ProgressNoopOutputStream) this.outputStream).addProgress(contentSize);
            } else {
                InputStream inputStream = FacebookSdk.getApplicationContext().getContentResolver().openInputStream(contentUri);
                totalBytes = 0 + Utility.copyAndCloseInputStream(inputStream, this.outputStream);
            }
            writeLine("", new Object[0]);
            writeRecordBoundary();
            if (this.logger != null) {
                this.logger.appendKeyValue("    " + key, String.format(Locale.ROOT, "<Data: %d>", Integer.valueOf(totalBytes)));
            }
        }

        public void writeFile(String key, ParcelFileDescriptor descriptor, String mimeType) throws IOException {
            if (mimeType == null) {
                mimeType = "content/unknown";
            }
            writeContentDisposition(key, key, mimeType);
            int totalBytes = 0;
            if (this.outputStream instanceof ProgressNoopOutputStream) {
                ((ProgressNoopOutputStream) this.outputStream).addProgress(descriptor.getStatSize());
            } else {
                ParcelFileDescriptor.AutoCloseInputStream inputStream = new ParcelFileDescriptor.AutoCloseInputStream(descriptor);
                totalBytes = 0 + Utility.copyAndCloseInputStream(inputStream, this.outputStream);
            }
            writeLine("", new Object[0]);
            writeRecordBoundary();
            if (this.logger != null) {
                this.logger.appendKeyValue("    " + key, String.format(Locale.ROOT, "<Data: %d>", Integer.valueOf(totalBytes)));
            }
        }

        public void writeRecordBoundary() throws IOException {
            if (!this.useUrlEncode) {
                writeLine("--%s", GraphRequest.MIME_BOUNDARY);
            } else {
                this.outputStream.write("&".getBytes());
            }
        }

        public void writeContentDisposition(String name, String filename, String contentType) throws IOException {
            if (!this.useUrlEncode) {
                write("Content-Disposition: form-data; name=\"%s\"", name);
                if (filename != null) {
                    write("; filename=\"%s\"", filename);
                }
                writeLine("", new Object[0]);
                if (contentType != null) {
                    writeLine("%s: %s", "Content-Type", contentType);
                }
                writeLine("", new Object[0]);
                return;
            }
            this.outputStream.write(String.format("%s=", name).getBytes());
        }

        public void write(String format, Object... args) throws IOException {
            if (!this.useUrlEncode) {
                if (this.firstWrite) {
                    this.outputStream.write("--".getBytes());
                    this.outputStream.write(GraphRequest.MIME_BOUNDARY.getBytes());
                    this.outputStream.write(VCardBuilder.VCARD_END_OF_LINE.getBytes());
                    this.firstWrite = false;
                }
                this.outputStream.write(String.format(format, args).getBytes());
                return;
            }
            this.outputStream.write(URLEncoder.encode(String.format(Locale.US, format, args), "UTF-8").getBytes());
        }

        public void writeLine(String format, Object... args) throws IOException {
            write(format, args);
            if (!this.useUrlEncode) {
                write(VCardBuilder.VCARD_END_OF_LINE, new Object[0]);
            }
        }
    }

    public static class ParcelableResourceWithMimeType<RESOURCE extends Parcelable> implements Parcelable {
        public static final Parcelable.Creator<ParcelableResourceWithMimeType> CREATOR = new Parcelable.Creator<ParcelableResourceWithMimeType>() { // from class: com.facebook.GraphRequest.ParcelableResourceWithMimeType.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public ParcelableResourceWithMimeType createFromParcel(Parcel in) {
                return new ParcelableResourceWithMimeType(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public ParcelableResourceWithMimeType[] newArray(int size) {
                return new ParcelableResourceWithMimeType[size];
            }
        };
        private final String mimeType;
        private final RESOURCE resource;

        public String getMimeType() {
            return this.mimeType;
        }

        public RESOURCE getResource() {
            return this.resource;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 1;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            out.writeString(this.mimeType);
            out.writeParcelable(this.resource, flags);
        }

        public ParcelableResourceWithMimeType(RESOURCE resource, String mimeType) {
            this.mimeType = mimeType;
            this.resource = resource;
        }

        private ParcelableResourceWithMimeType(Parcel parcel) {
            this.mimeType = parcel.readString();
            this.resource = (RESOURCE) parcel.readParcelable(FacebookSdk.getApplicationContext().getClassLoader());
        }
    }
}
