package com.digits.sdk.android;

import com.digits.sdk.android.DigitsScribeConstants;
import com.twitter.sdk.android.core.internal.scribe.EventNamespace;

/* JADX INFO: loaded from: classes18.dex */
class ContactsScribeService implements DigitsScribeService {
    static final String CONTACTS_COMPONENT = "contacts";
    private final DigitsScribeClient scribeClient;

    ContactsScribeService(DigitsScribeClient scribeClient) {
        if (scribeClient == null) {
            throw new NullPointerException("scribeClient must not be null");
        }
        this.scribeClient = scribeClient;
    }

    @Override // com.digits.sdk.android.DigitsScribeService
    public void impression() {
        EventNamespace ns = DigitsScribeConstants.DIGITS_EVENT_BUILDER.setComponent("contacts").setElement("").setAction("impression").builder();
        this.scribeClient.scribe(ns);
    }

    @Override // com.digits.sdk.android.DigitsScribeService
    public void failure() {
    }

    @Override // com.digits.sdk.android.DigitsScribeService
    public void click(DigitsScribeConstants.Element element) {
        EventNamespace ns = DigitsScribeConstants.DIGITS_EVENT_BUILDER.setComponent("contacts").setElement(element.toString()).setAction("click").builder();
        this.scribeClient.scribe(ns);
    }

    @Override // com.digits.sdk.android.DigitsScribeService
    public void success() {
    }

    @Override // com.digits.sdk.android.DigitsScribeService
    public void error(DigitsException exception) {
        EventNamespace ns = DigitsScribeConstants.DIGITS_EVENT_BUILDER.setComponent("contacts").setElement("").setAction("error").builder();
        this.scribeClient.scribe(ns);
    }
}
