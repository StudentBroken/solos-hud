package com.digits.sdk.android;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.kopin.solos.wear.WatchModeActivity;
import io.fabric.sdk.android.services.common.CommonUtils;
import java.util.Locale;

/* JADX INFO: loaded from: classes18.dex */
class SimManager {
    private final boolean canReadPhoneState;
    private final TelephonyManager telephonyManager;

    public static SimManager createSimManager(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(WatchModeActivity.MODE_PHONE);
        return new SimManager(telephonyManager, CommonUtils.checkPermission(context, "android.permission.READ_PHONE_STATE"));
    }

    protected SimManager(TelephonyManager telephonyManager, boolean canReadPhoneState) {
        this.telephonyManager = telephonyManager;
        this.canReadPhoneState = canReadPhoneState;
    }

    protected String getRawPhoneNumber() {
        return (this.telephonyManager == null || !this.canReadPhoneState) ? "" : normalizeNumber(this.telephonyManager.getLine1Number());
    }

    private String normalizeNumber(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int len = phoneNumber.length();
        for (int i = 0; i < len; i++) {
            char c = phoneNumber.charAt(i);
            int digit = Character.digit(c, 10);
            if (digit != -1) {
                sb.append(digit);
            } else if (i == 0 && c == '+') {
                sb.append(c);
            } else if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                return normalizeNumber(android.telephony.PhoneNumberUtils.convertKeypadLettersToDigits(phoneNumber));
            }
        }
        return sb.toString();
    }

    protected String getCountryIso() {
        if (this.telephonyManager != null) {
            String simCountry = this.telephonyManager.getSimCountryIso();
            if (isIso2(simCountry)) {
                return simCountry.toUpperCase(Locale.getDefault());
            }
            if (!isCdma()) {
                String networkCountry = this.telephonyManager.getNetworkCountryIso();
                if (isIso2(networkCountry)) {
                    return networkCountry.toUpperCase(Locale.getDefault());
                }
            }
        }
        return "";
    }

    private boolean isIso2(String simCountry) {
        return simCountry != null && simCountry.length() == 2;
    }

    private boolean isCdma() {
        return this.telephonyManager.getPhoneType() == 2;
    }
}
