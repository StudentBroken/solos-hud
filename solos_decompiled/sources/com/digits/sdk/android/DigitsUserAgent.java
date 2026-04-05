package com.digits.sdk.android;

/* JADX INFO: loaded from: classes18.dex */
class DigitsUserAgent {
    private final String androidVersion;
    private final String digitsVersion;

    DigitsUserAgent(String digitsVersion, String androidVersion) {
        this.digitsVersion = digitsVersion;
        this.androidVersion = androidVersion;
    }

    public String toString() {
        return "Digits/" + this.digitsVersion + " (Android " + this.androidVersion + ")";
    }
}
