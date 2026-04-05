package com.facebook;

/* JADX INFO: loaded from: classes41.dex */
public class FacebookServiceException extends FacebookException {
    private static final long serialVersionUID = 1;
    private final FacebookRequestError error;

    public FacebookServiceException(FacebookRequestError error, String errorMessage) {
        super(errorMessage);
        this.error = error;
    }

    public final FacebookRequestError getRequestError() {
        return this.error;
    }

    @Override // com.facebook.FacebookException, java.lang.Throwable
    public final String toString() {
        return "{FacebookServiceException: httpResponseCode: " + this.error.getRequestStatusCode() + ", facebookErrorCode: " + this.error.getErrorCode() + ", facebookErrorType: " + this.error.getErrorType() + ", message: " + this.error.getErrorMessage() + "}";
    }
}
