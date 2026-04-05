package com.facebook;

import com.facebook.internal.Logger;
import com.facebook.internal.Utility;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/* JADX INFO: loaded from: classes41.dex */
public class GraphResponse {
    private static final String BODY_KEY = "body";
    private static final String CODE_KEY = "code";
    public static final String NON_JSON_RESPONSE_PROPERTY = "FACEBOOK_NON_JSON_RESULT";
    private static final String RESPONSE_LOG_TAG = "Response";
    public static final String SUCCESS_KEY = "success";
    private final HttpURLConnection connection;
    private final FacebookRequestError error;
    private final JSONObject graphObject;
    private final JSONArray graphObjectArray;
    private final String rawResponse;
    private final GraphRequest request;

    public enum PagingDirection {
        NEXT,
        PREVIOUS
    }

    GraphResponse(GraphRequest request, HttpURLConnection connection, String rawResponse, JSONObject graphObject) {
        this(request, connection, rawResponse, graphObject, null, null);
    }

    GraphResponse(GraphRequest request, HttpURLConnection connection, String rawResponse, JSONArray graphObjects) {
        this(request, connection, rawResponse, null, graphObjects, null);
    }

    GraphResponse(GraphRequest request, HttpURLConnection connection, FacebookRequestError error) {
        this(request, connection, null, null, null, error);
    }

    GraphResponse(GraphRequest request, HttpURLConnection connection, String rawResponse, JSONObject graphObject, JSONArray graphObjects, FacebookRequestError error) {
        this.request = request;
        this.connection = connection;
        this.rawResponse = rawResponse;
        this.graphObject = graphObject;
        this.graphObjectArray = graphObjects;
        this.error = error;
    }

    public final FacebookRequestError getError() {
        return this.error;
    }

    public final JSONObject getJSONObject() {
        return this.graphObject;
    }

    public final JSONArray getJSONArray() {
        return this.graphObjectArray;
    }

    public final HttpURLConnection getConnection() {
        return this.connection;
    }

    public GraphRequest getRequest() {
        return this.request;
    }

    public String getRawResponse() {
        return this.rawResponse;
    }

    public GraphRequest getRequestForPagedResults(PagingDirection direction) {
        JSONObject pagingInfo;
        String link = null;
        if (this.graphObject != null && (pagingInfo = this.graphObject.optJSONObject("paging")) != null) {
            link = direction == PagingDirection.NEXT ? pagingInfo.optString("next") : pagingInfo.optString("previous");
        }
        if (Utility.isNullOrEmpty(link)) {
            return null;
        }
        if (link != null && link.equals(this.request.getUrlForSingleRequest())) {
            return null;
        }
        try {
            return new GraphRequest(this.request.getAccessToken(), new URL(link));
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public String toString() {
        String responseCode;
        try {
            Locale locale = Locale.US;
            Object[] objArr = new Object[1];
            objArr[0] = Integer.valueOf(this.connection != null ? this.connection.getResponseCode() : 200);
            responseCode = String.format(locale, "%d", objArr);
        } catch (IOException e) {
            responseCode = "unknown";
        }
        return "{Response:  responseCode: " + responseCode + ", graphObject: " + this.graphObject + ", error: " + this.error + "}";
    }

    static List<GraphResponse> fromHttpConnection(HttpURLConnection connection, GraphRequestBatch requests) {
        List<GraphResponse> listConstructErrorResponses;
        InputStream stream = null;
        try {
            if (connection.getResponseCode() >= 400) {
                stream = connection.getErrorStream();
            } else {
                stream = connection.getInputStream();
            }
            listConstructErrorResponses = createResponsesFromStream(stream, connection, requests);
        } catch (FacebookException facebookException) {
            Logger.log(LoggingBehavior.REQUESTS, RESPONSE_LOG_TAG, "Response <Error>: %s", facebookException);
            listConstructErrorResponses = constructErrorResponses(requests, connection, facebookException);
        } catch (Exception exception) {
            Logger.log(LoggingBehavior.REQUESTS, RESPONSE_LOG_TAG, "Response <Error>: %s", exception);
            listConstructErrorResponses = constructErrorResponses(requests, connection, new FacebookException(exception));
        } finally {
            Utility.closeQuietly(stream);
        }
        return listConstructErrorResponses;
    }

    static List<GraphResponse> createResponsesFromStream(InputStream stream, HttpURLConnection connection, GraphRequestBatch requests) throws Throwable {
        String responseString = Utility.readStreamToString(stream);
        Logger.log(LoggingBehavior.INCLUDE_RAW_RESPONSES, RESPONSE_LOG_TAG, "Response (raw)\n  Size: %d\n  Response:\n%s\n", Integer.valueOf(responseString.length()), responseString);
        return createResponsesFromString(responseString, connection, requests);
    }

    static List<GraphResponse> createResponsesFromString(String responseString, HttpURLConnection connection, GraphRequestBatch requests) throws JSONException, FacebookException, IOException {
        JSONTokener tokener = new JSONTokener(responseString);
        Object resultObject = tokener.nextValue();
        List<GraphResponse> responses = createResponsesFromObject(connection, requests, resultObject);
        Logger.log(LoggingBehavior.REQUESTS, RESPONSE_LOG_TAG, "Response\n  Id: %s\n  Size: %d\n  Responses:\n%s\n", requests.getId(), Integer.valueOf(responseString.length()), responses);
        return responses;
    }

    private static List<GraphResponse> createResponsesFromObject(HttpURLConnection connection, List<GraphRequest> requests, Object object) throws JSONException, FacebookException {
        int numRequests = requests.size();
        List<GraphResponse> responses = new ArrayList<>(numRequests);
        if (numRequests == 1) {
            GraphRequest request = requests.get(0);
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(BODY_KEY, object);
                int responseCode = connection != null ? connection.getResponseCode() : 200;
                jsonObject.put(CODE_KEY, responseCode);
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonObject);
                object = jsonArray;
            } catch (IOException e) {
                responses.add(new GraphResponse(request, connection, new FacebookRequestError(connection, e)));
            } catch (JSONException e2) {
                responses.add(new GraphResponse(request, connection, new FacebookRequestError(connection, e2)));
            }
        }
        if (!(object instanceof JSONArray) || ((JSONArray) object).length() != numRequests) {
            FacebookException exception = new FacebookException("Unexpected number of results");
            throw exception;
        }
        JSONArray jsonArray2 = (JSONArray) object;
        for (int i = 0; i < jsonArray2.length(); i++) {
            GraphRequest request2 = requests.get(i);
            try {
                Object obj = jsonArray2.get(i);
                responses.add(createResponseFromObject(request2, connection, obj, object));
            } catch (FacebookException e3) {
                responses.add(new GraphResponse(request2, connection, new FacebookRequestError(connection, e3)));
            } catch (JSONException e4) {
                responses.add(new GraphResponse(request2, connection, new FacebookRequestError(connection, e4)));
            }
        }
        return responses;
    }

    private static GraphResponse createResponseFromObject(GraphRequest request, HttpURLConnection connection, Object object, Object originalResult) throws JSONException {
        if (object instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) object;
            FacebookRequestError error = FacebookRequestError.checkResponseAndCreateError(jsonObject, originalResult, connection);
            if (error != null) {
                if (error.getErrorCode() == 190 && Utility.isCurrentAccessToken(request.getAccessToken())) {
                    AccessToken.setCurrentAccessToken(null);
                }
                return new GraphResponse(request, connection, error);
            }
            Object body = Utility.getStringPropertyAsJSON(jsonObject, BODY_KEY, NON_JSON_RESPONSE_PROPERTY);
            if (body instanceof JSONObject) {
                return new GraphResponse(request, connection, body.toString(), (JSONObject) body);
            }
            if (body instanceof JSONArray) {
                return new GraphResponse(request, connection, body.toString(), (JSONArray) body);
            }
            object = JSONObject.NULL;
        }
        if (object == JSONObject.NULL) {
            return new GraphResponse(request, connection, object.toString(), (JSONObject) null);
        }
        throw new FacebookException("Got unexpected object type in response, class: " + object.getClass().getSimpleName());
    }

    static List<GraphResponse> constructErrorResponses(List<GraphRequest> requests, HttpURLConnection connection, FacebookException error) {
        int count = requests.size();
        List<GraphResponse> responses = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            GraphResponse response = new GraphResponse(requests.get(i), connection, new FacebookRequestError(connection, error));
            responses.add(response);
        }
        return responses;
    }
}
