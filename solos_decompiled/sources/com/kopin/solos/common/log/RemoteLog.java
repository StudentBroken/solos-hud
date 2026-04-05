package com.kopin.solos.common.log;

import android.app.Application;
import com.kopin.solos.common.config.Config;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes52.dex */
public class RemoteLog {
    private static final String DeviceId = "deviceId";
    private static final String UserId = "userId";
    private static String mDeviceId;
    private static String mUserId;

    public static void init(String deviceId, String userId) {
        mDeviceId = deviceId;
        mUserId = userId;
    }

    public static void register(Application application) {
        if (Config.REMOTE_CRASH_LOG_ENABLED) {
        }
        if (Config.REMOTE_EVENT_LOG_ENABLED) {
        }
    }

    public static void track(LogEvent event, String value) {
        if (Config.REMOTE_EVENT_LOG_ENABLED) {
            Map<String, String> properties = new HashMap<>();
            if (mDeviceId != null) {
                properties.put(DeviceId, mDeviceId);
            }
            if (mUserId != null) {
                properties.put(UserId, mUserId);
            }
        }
    }
}
