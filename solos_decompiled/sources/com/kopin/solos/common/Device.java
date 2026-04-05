package com.kopin.solos.common;

import android.content.Context;
import android.provider.Settings;
import io.fabric.sdk.android.services.events.EventsFilesManager;

/* JADX INFO: loaded from: classes52.dex */
public class Device {
    private static final String MOBILE_PREFIX = "android_";
    private static final String WEAR_PREFIX = "watch_android_";

    public enum DeviceType {
        MOBILE,
        WEAR
    }

    public static String getDeviceId(Context context, DeviceType deviceType) {
        return (DeviceType.WEAR == deviceType ? WEAR_PREFIX : MOBILE_PREFIX) + "1.0" + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + Settings.Secure.getString(context.getContentResolver(), "android_id");
    }
}
