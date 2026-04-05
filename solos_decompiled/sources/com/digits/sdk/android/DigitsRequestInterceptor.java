package com.digits.sdk.android;

import retrofit.RequestInterceptor;

/* JADX INFO: loaded from: classes18.dex */
class DigitsRequestInterceptor implements RequestInterceptor {
    static final String USER_AGENT_KEY = "User-Agent";
    private final DigitsUserAgent userAgent;

    public DigitsRequestInterceptor(DigitsUserAgent userAgent) {
        this.userAgent = userAgent;
    }

    @Override // retrofit.RequestInterceptor
    public void intercept(RequestInterceptor.RequestFacade request) {
        request.addHeader("User-Agent", this.userAgent.toString());
    }
}
