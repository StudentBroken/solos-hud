package com.digits.sdk.android;

import com.digits.sdk.android.DigitsScribeConstants;
import com.twitter.sdk.android.core.internal.scribe.EventNamespace;

/* JADX INFO: loaded from: classes18.dex */
class AuthScribeService implements DigitsScribeService {
    static final String LOGGED_IN_ACTION = "logged_in";
    private final DigitsScribeClient scribeClient;

    AuthScribeService(DigitsScribeClient scribeClient) {
        if (scribeClient == null) {
            throw new NullPointerException("scribeClient must not be null");
        }
        this.scribeClient = scribeClient;
    }

    @Override // com.digits.sdk.android.DigitsScribeService
    public void impression() {
        EventNamespace ns = DigitsScribeConstants.DIGITS_EVENT_BUILDER.setComponent("").setElement("").setAction("impression").builder();
        this.scribeClient.scribe(ns);
    }

    @Override // com.digits.sdk.android.DigitsScribeService
    public void failure() {
        EventNamespace ns = DigitsScribeConstants.DIGITS_EVENT_BUILDER.setComponent("").setElement("").setAction("failure").builder();
        this.scribeClient.scribe(ns);
    }

    @Override // com.digits.sdk.android.DigitsScribeService
    public void click(DigitsScribeConstants.Element element) {
    }

    @Override // com.digits.sdk.android.DigitsScribeService
    public void success() {
        EventNamespace ns = DigitsScribeConstants.DIGITS_EVENT_BUILDER.setComponent("").setElement("").setAction(LOGGED_IN_ACTION).builder();
        this.scribeClient.scribe(ns);
    }

    @Override // com.digits.sdk.android.DigitsScribeService
    public void error(DigitsException exception) {
    }
}
