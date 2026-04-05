package com.digits.sdk.android;

import android.text.TextUtils;

/* JADX INFO: loaded from: classes18.dex */
public class PhoneNumber {
    private static final PhoneNumber EMPTY_PHONE_NUMBER = new PhoneNumber("", "", "");
    private final String countryCode;
    private final String countryIso;
    private final String phoneNumber;

    public PhoneNumber(String phoneNumber, String countryIso, String countryCode) {
        this.phoneNumber = phoneNumber;
        this.countryIso = countryIso;
        this.countryCode = countryCode;
    }

    public static PhoneNumber emptyPhone() {
        return EMPTY_PHONE_NUMBER;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getCountryIso() {
        return this.countryIso;
    }

    protected static boolean isValid(PhoneNumber phoneNumber) {
        return (phoneNumber == null || EMPTY_PHONE_NUMBER.equals(phoneNumber) || TextUtils.isEmpty(phoneNumber.getPhoneNumber()) || TextUtils.isEmpty(phoneNumber.getCountryCode()) || TextUtils.isEmpty(phoneNumber.getCountryIso())) ? false : true;
    }

    protected static boolean isCountryValid(PhoneNumber phoneNumber) {
        return (phoneNumber == null || EMPTY_PHONE_NUMBER.equals(phoneNumber) || TextUtils.isEmpty(phoneNumber.getCountryCode()) || TextUtils.isEmpty(phoneNumber.getCountryIso())) ? false : true;
    }
}
