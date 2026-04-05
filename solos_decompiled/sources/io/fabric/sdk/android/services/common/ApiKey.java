package io.fabric.sdk.android.services.common;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.text.TextUtils;
import io.fabric.sdk.android.Fabric;

/* JADX INFO: loaded from: classes66.dex */
public class ApiKey {
    static final String CRASHLYTICS_API_KEY = "com.crashlytics.ApiKey";
    static final String FABRIC_API_KEY = "io.fabric.ApiKey";

    @Deprecated
    public static String getApiKey(Context context) {
        Fabric.getLogger().w(Fabric.TAG, "getApiKey(context) is deprecated, please upgrade kit(s) to the latest version.");
        return new ApiKey().getValue(context);
    }

    @Deprecated
    public static String getApiKey(Context context, boolean debug) {
        Fabric.getLogger().w(Fabric.TAG, "getApiKey(context, debug) is deprecated, please upgrade kit(s) to the latest version.");
        return new ApiKey().getValue(context);
    }

    public String getValue(Context context) {
        String apiKey = getApiKeyFromManifest(context);
        if (TextUtils.isEmpty(apiKey)) {
            apiKey = getApiKeyFromStrings(context);
        }
        if (TextUtils.isEmpty(apiKey)) {
            logErrorOrThrowException(context);
        }
        return apiKey;
    }

    protected String getApiKeyFromManifest(Context context) {
        try {
            String packageName = context.getPackageName();
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(packageName, 128);
            Bundle bundle = ai.metaData;
            if (bundle == null) {
                return null;
            }
            String apiKey = bundle.getString(FABRIC_API_KEY);
            if (apiKey == null) {
                Fabric.getLogger().d(Fabric.TAG, "Falling back to Crashlytics key lookup from Manifest");
                return bundle.getString(CRASHLYTICS_API_KEY);
            }
            return apiKey;
        } catch (Exception e) {
            Fabric.getLogger().d(Fabric.TAG, "Caught non-fatal exception while retrieving apiKey: " + e);
            return null;
        }
    }

    protected String getApiKeyFromStrings(Context context) {
        int id = CommonUtils.getResourcesIdentifier(context, FABRIC_API_KEY, "string");
        if (id == 0) {
            Fabric.getLogger().d(Fabric.TAG, "Falling back to Crashlytics key lookup from Strings");
            id = CommonUtils.getResourcesIdentifier(context, CRASHLYTICS_API_KEY, "string");
        }
        if (id == 0) {
            return null;
        }
        String apiKey = context.getResources().getString(id);
        return apiKey;
    }

    protected void logErrorOrThrowException(Context context) {
        if (Fabric.isDebuggable() || CommonUtils.isAppDebuggable(context)) {
            throw new IllegalArgumentException(buildApiKeyInstructions());
        }
        Fabric.getLogger().e(Fabric.TAG, buildApiKeyInstructions());
    }

    protected String buildApiKeyInstructions() {
        return "Fabric could not be initialized, API key missing from AndroidManifest.xml. Add the following tag to your Application element \n\t<meta-data android:name=\"io.fabric.ApiKey\" android:value=\"YOUR_API_KEY\"/>";
    }
}
