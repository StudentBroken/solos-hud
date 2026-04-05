package com.facebook.internal;

import android.content.Context;
import com.facebook.LoggingBehavior;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes41.dex */
public class AppEventsLoggerUtility {
    private static final Map<GraphAPIActivityType, String> API_ACTIVITY_TYPE_TO_STRING = new HashMap<GraphAPIActivityType, String>() { // from class: com.facebook.internal.AppEventsLoggerUtility.1
        {
            put(GraphAPIActivityType.MOBILE_INSTALL_EVENT, "MOBILE_APP_INSTALL");
            put(GraphAPIActivityType.CUSTOM_APP_EVENTS, "CUSTOM_APP_EVENTS");
        }
    };

    public enum GraphAPIActivityType {
        MOBILE_INSTALL_EVENT,
        CUSTOM_APP_EVENTS
    }

    public static JSONObject getJSONObjectForGraphAPICall(GraphAPIActivityType activityType, AttributionIdentifiers attributionIdentifiers, String anonymousAppDeviceGUID, boolean limitEventUsage, Context context) throws JSONException {
        JSONObject publishParams = new JSONObject();
        publishParams.put("event", API_ACTIVITY_TYPE_TO_STRING.get(activityType));
        Utility.setAppEventAttributionParameters(publishParams, attributionIdentifiers, anonymousAppDeviceGUID, limitEventUsage);
        try {
            Utility.setAppEventExtendedDeviceInfoParameters(publishParams, context);
        } catch (Exception e) {
            Logger.log(LoggingBehavior.APP_EVENTS, "AppEvents", "Fetching extended device info parameters failed: '%s'", e.toString());
        }
        publishParams.put("application_package_name", context.getPackageName());
        return publishParams;
    }
}
