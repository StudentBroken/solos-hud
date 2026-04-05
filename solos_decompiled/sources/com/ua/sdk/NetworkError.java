package com.ua.sdk;

import com.ua.sdk.UaException;
import com.ua.sdk.util.Streams;
import java.net.HttpURLConnection;

/* JADX INFO: loaded from: classes65.dex */
public class NetworkError extends UaException {
    String message;
    final int responseCode;

    public NetworkError(int responseCode, HttpURLConnection conn) {
        super(getErrorCode(responseCode));
        this.message = "";
        this.responseCode = responseCode;
        try {
            this.message = Streams.readFully(conn.getErrorStream());
        } catch (Throwable th) {
            this.message = "";
        }
    }

    public NetworkError(int responseCode) {
        this(responseCode, (Throwable) null);
    }

    public NetworkError(int responseCode, Throwable cause) {
        super(getErrorCode(responseCode), cause);
        this.message = "";
        this.responseCode = responseCode;
    }

    @Override // java.lang.Throwable
    public String toString() {
        return "Response Code " + this.responseCode + " " + this.message;
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    public String getHttpResponse() {
        return this.message;
    }

    public static final UaException.Code getErrorCode(int responseCode) {
        return UaException.Code.NETWORK;
    }
}
