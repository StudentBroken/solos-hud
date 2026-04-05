package com.digits.sdk.android;

import android.support.annotation.NonNull;
import com.twitter.sdk.android.core.TwitterApiException;
import com.twitter.sdk.android.core.TwitterException;
import retrofit.RetrofitError;

/* JADX INFO: loaded from: classes18.dex */
public class DigitsException extends RuntimeException {
    private final AuthConfig config;
    private final int errorCode;

    DigitsException(String message) {
        this(message, -1, new AuthConfig());
    }

    DigitsException(String message, int errorCode, @NonNull AuthConfig config) {
        super(message);
        this.errorCode = errorCode;
        this.config = config;
    }

    static DigitsException create(ErrorCodes errors, TwitterException exception) {
        if (exception instanceof TwitterApiException) {
            TwitterApiException apiException = (TwitterApiException) exception;
            String message = getMessageForApiError(errors, apiException);
            return createException(apiException.getErrorCode(), message, (AuthConfig) apiException.getRetrofitError().getBodyAs(AuthConfig.class));
        }
        String message2 = errors.getDefaultMessage();
        return new DigitsException(message2);
    }

    private static DigitsException createException(int error, String message, AuthConfig config) {
        if (error == 32) {
            return new CouldNotAuthenticateException(message, error, config);
        }
        if (error == 286) {
            return new OperatorUnsupportedException(message, error, config);
        }
        if (isUnrecoverable(error)) {
            return new UnrecoverableException(message, error, config);
        }
        return new DigitsException(message, error, config);
    }

    private static boolean isUnrecoverable(int error) {
        return error == 269 || error == 235 || error == 237 || error == 299 || error == 284;
    }

    private static String getMessageForApiError(ErrorCodes errors, TwitterApiException apiException) {
        RetrofitError error = apiException.getRetrofitError();
        if (error.isNetworkError()) {
            String errorCodeMessage = errors.getNetworkError();
            return errorCodeMessage;
        }
        String errorCodeMessage2 = errors.getMessage(apiException.getErrorCode());
        return errorCodeMessage2;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    @NonNull
    public AuthConfig getConfig() {
        return this.config;
    }
}
