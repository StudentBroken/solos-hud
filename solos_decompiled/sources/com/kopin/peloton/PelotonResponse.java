package com.kopin.peloton;

/* JADX INFO: loaded from: classes61.dex */
public class PelotonResponse {
    public static final int HTTP_SUCCESS = 200;
    public static final int HTTP_UNAUTHORISED = 401;
    public int httpCode;
    public String rawResponse;
    public Object result;
    public Failure serverFailure;

    public PelotonResponse() {
        this.httpCode = 0;
        this.rawResponse = "";
    }

    public PelotonResponse(int code) {
        this.httpCode = 0;
        this.rawResponse = "";
        this.httpCode = code;
    }

    public boolean isServerSuccess() {
        return this.httpCode == 200 && this.rawResponse != null && this.rawResponse.length() > 0 && (this.serverFailure == null || this.serverFailure.isSuccess());
    }

    public boolean isAuthorised() {
        return this.httpCode != 401;
    }
}
