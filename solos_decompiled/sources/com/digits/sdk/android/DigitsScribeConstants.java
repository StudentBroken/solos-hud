package com.digits.sdk.android;

import android.support.v4.app.NotificationCompat;
import com.twitter.sdk.android.core.internal.scribe.EventNamespace;

/* JADX INFO: loaded from: classes18.dex */
class DigitsScribeConstants {
    static final String CLICK_ACTION = "click";
    static final EventNamespace.Builder DIGITS_EVENT_BUILDER = new EventNamespace.Builder().setClient("tfw").setPage("android").setSection("digits");
    static final String EMPTY_SCRIBE_COMPONENT = "";
    static final String EMPTY_SCRIBE_ELEMENT = "";
    static final String ERROR_ACTION = "error";
    static final String FAILURE_ACTION = "failure";
    static final String IMPRESSION_ACTION = "impression";
    static final String SCRIBE_CLIENT = "tfw";
    static final String SCRIBE_PAGE = "android";
    static final String SCRIBE_SECTION = "digits";
    static final String SUCCESS_ACTION = "success";

    DigitsScribeConstants() {
    }

    enum Element {
        COUNTRY_CODE("country_code"),
        SUBMIT("submit"),
        RETRY("retry"),
        CALL(NotificationCompat.CATEGORY_CALL),
        CANCEL("cancel"),
        RESEND("resend"),
        DISMISS("dismiss");

        private final String element;

        Element(String element) {
            this.element = element;
        }

        public String getElement() {
            return this.element;
        }
    }
}
