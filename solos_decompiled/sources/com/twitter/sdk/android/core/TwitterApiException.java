package com.twitter.sdk.android.core;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.twitter.sdk.android.core.internal.TwitterApiConstants;
import com.twitter.sdk.android.core.models.ApiError;
import io.fabric.sdk.android.Fabric;
import java.io.UnsupportedEncodingException;
import retrofit.RetrofitError;
import retrofit.mime.TypedByteArray;

/* JADX INFO: loaded from: classes62.dex */
public class TwitterApiException extends TwitterException {
    public static final int DEFAULT_ERROR_CODE = 0;
    private final ApiError apiError;
    private final RetrofitError retrofitError;
    private final TwitterRateLimit twitterRateLimit;

    TwitterApiException(ApiError apiError, TwitterRateLimit twitterRateLimit, RetrofitError retrofitError) {
        super(retrofitError.getMessage());
        this.retrofitError = retrofitError;
        this.apiError = apiError;
        this.twitterRateLimit = twitterRateLimit;
    }

    TwitterApiException(RetrofitError retrofitError) {
        super(createExceptionMessage(retrofitError));
        setStackTrace(retrofitError.getStackTrace());
        this.retrofitError = retrofitError;
        this.twitterRateLimit = createRateLimit(retrofitError);
        this.apiError = readApiError(retrofitError);
    }

    private static String createExceptionMessage(RetrofitError retrofitError) {
        if (retrofitError.getMessage() != null) {
            return retrofitError.getMessage();
        }
        if (retrofitError.getResponse() != null) {
            return "Status: " + retrofitError.getResponse().getStatus();
        }
        return "unknown error";
    }

    private static TwitterRateLimit createRateLimit(RetrofitError retrofitError) {
        if (retrofitError.getResponse() != null) {
            return new TwitterRateLimit(retrofitError.getResponse().getHeaders());
        }
        return null;
    }

    public int getErrorCode() {
        if (this.apiError == null) {
            return 0;
        }
        return this.apiError.getCode();
    }

    public String getErrorMessage() {
        if (this.apiError == null) {
            return null;
        }
        return this.apiError.getMessage();
    }

    public boolean canRetry() {
        int status = this.retrofitError.getResponse().getStatus();
        return status < 400 || status > 499;
    }

    public RetrofitError getRetrofitError() {
        return this.retrofitError;
    }

    public TwitterRateLimit getTwitterRateLimit() {
        return this.twitterRateLimit;
    }

    public static final TwitterApiException convert(RetrofitError retrofitError) {
        return new TwitterApiException(retrofitError);
    }

    public static ApiError readApiError(RetrofitError retrofitError) {
        if (retrofitError == null || retrofitError.getResponse() == null || retrofitError.getResponse().getBody() == null) {
            return null;
        }
        byte[] responseBytes = ((TypedByteArray) retrofitError.getResponse().getBody()).getBytes();
        if (responseBytes == null) {
            return null;
        }
        try {
            String response = new String(responseBytes, "UTF-8");
            return parseApiError(response);
        } catch (UnsupportedEncodingException e) {
            Fabric.getLogger().e(TwitterCore.TAG, "Failed to convert to string", e);
            return null;
        }
    }

    static ApiError parseApiError(String response) {
        Gson gson = new Gson();
        try {
            JsonObject responseObj = new JsonParser().parse(response).getAsJsonObject();
            ApiError[] apiErrors = (ApiError[]) gson.fromJson(responseObj.get(TwitterApiConstants.Errors.ERRORS), ApiError[].class);
            if (apiErrors.length == 0) {
                return null;
            }
            return apiErrors[0];
        } catch (JsonSyntaxException e) {
            Fabric.getLogger().e(TwitterCore.TAG, "Invalid json: " + response, e);
            return null;
        } catch (Exception e2) {
            Fabric.getLogger().e(TwitterCore.TAG, "Unexpected response: " + response, e2);
            return null;
        }
    }
}
