package com.digits.sdk.android;

import com.facebook.internal.ServerProtocol;
import com.google.gson.annotations.SerializedName;

/* JADX INFO: loaded from: classes18.dex */
public class DeviceRegistrationResponse {

    @SerializedName("config")
    public AuthConfig authConfig;

    @SerializedName("device_id")
    public String deviceId;

    @SerializedName(DigitsClient.EXTRA_PHONE)
    public String normalizedPhoneNumber;

    @SerializedName(ServerProtocol.DIALOG_PARAM_STATE)
    public String state;
}
