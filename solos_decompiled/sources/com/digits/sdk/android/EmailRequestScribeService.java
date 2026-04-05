package com.digits.sdk.android;

import com.digits.sdk.android.DigitsScribeConstants;
import com.facebook.GraphResponse;
import com.twitter.sdk.android.core.internal.scribe.EventNamespace;

/* JADX INFO: loaded from: classes18.dex */
class EmailRequestScribeService implements DigitsScribeService {
    static final String EMAIL_COMPONENT = "email";
    private final DigitsScribeClient scribeClient;

    public EmailRequestScribeService(DigitsScribeClient scribeClient) {
        if (scribeClient == null) {
            throw new NullPointerException("scribeClient must not be null");
        }
        this.scribeClient = scribeClient;
    }

    @Override // com.digits.sdk.android.DigitsScribeService
    public void impression() {
        EventNamespace ns = DigitsScribeConstants.DIGITS_EVENT_BUILDER.setComponent("email").setElement("").setAction("impression").builder();
        this.scribeClient.scribe(ns);
    }

    @Override // com.digits.sdk.android.DigitsScribeService
    public void failure() {
        EventNamespace ns = DigitsScribeConstants.DIGITS_EVENT_BUILDER.setComponent("email").setElement("").setAction("failure").builder();
        this.scribeClient.scribe(ns);
    }

    @Override // com.digits.sdk.android.DigitsScribeService
    public void click(DigitsScribeConstants.Element element) {
        EventNamespace ns = DigitsScribeConstants.DIGITS_EVENT_BUILDER.setComponent("email").setElement(element.toString()).setAction("click").builder();
        this.scribeClient.scribe(ns);
    }

    @Override // com.digits.sdk.android.DigitsScribeService
    public void success() {
        EventNamespace ns = DigitsScribeConstants.DIGITS_EVENT_BUILDER.setComponent("email").setElement("").setAction(GraphResponse.SUCCESS_KEY).builder();
        this.scribeClient.scribe(ns);
    }

    @Override // com.digits.sdk.android.DigitsScribeService
    public void error(DigitsException exception) {
        EventNamespace ns = DigitsScribeConstants.DIGITS_EVENT_BUILDER.setComponent("email").setElement("").setAction("error").builder();
        this.scribeClient.scribe(ns);
    }
}
