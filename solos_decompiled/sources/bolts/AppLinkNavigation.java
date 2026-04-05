package bolts;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseArray;
import bolts.AppLink;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.applinks.AppLinkData;
import com.facebook.internal.AnalyticsEvents;
import com.facebook.share.internal.ShareConstants;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes46.dex */
public class AppLinkNavigation {
    private static final String KEY_NAME_REFERER_APP_LINK = "referer_app_link";
    private static final String KEY_NAME_REFERER_APP_LINK_APP_NAME = "app_name";
    private static final String KEY_NAME_REFERER_APP_LINK_PACKAGE = "package";
    private static final String KEY_NAME_USER_AGENT = "user_agent";
    private static final String KEY_NAME_VERSION = "version";
    private static final String VERSION = "1.0";
    private static AppLinkResolver defaultResolver;
    private final AppLink appLink;
    private final Bundle appLinkData;
    private final Bundle extras;

    public enum NavigationResult {
        FAILED("failed", false),
        WEB(AnalyticsEvents.PARAMETER_SHARE_DIALOG_SHOW_WEB, true),
        APP(SettingsJsonConstants.APP_KEY, true);

        private String code;
        private boolean succeeded;

        public String getCode() {
            return this.code;
        }

        public boolean isSucceeded() {
            return this.succeeded;
        }

        NavigationResult(String code, boolean success) {
            this.code = code;
            this.succeeded = success;
        }
    }

    public AppLinkNavigation(AppLink appLink, Bundle extras, Bundle appLinkData) {
        if (appLink == null) {
            throw new IllegalArgumentException("appLink must not be null.");
        }
        extras = extras == null ? new Bundle() : extras;
        appLinkData = appLinkData == null ? new Bundle() : appLinkData;
        this.appLink = appLink;
        this.extras = extras;
        this.appLinkData = appLinkData;
    }

    public AppLink getAppLink() {
        return this.appLink;
    }

    public Bundle getAppLinkData() {
        return this.appLinkData;
    }

    public Bundle getExtras() {
        return this.extras;
    }

    private Bundle buildAppLinkDataForNavigation(Context context) {
        String refererAppName;
        Bundle data = new Bundle();
        Bundle refererAppLinkData = new Bundle();
        if (context != null) {
            String refererAppPackage = context.getPackageName();
            if (refererAppPackage != null) {
                refererAppLinkData.putString(KEY_NAME_REFERER_APP_LINK_PACKAGE, refererAppPackage);
            }
            ApplicationInfo appInfo = context.getApplicationInfo();
            if (appInfo != null && (refererAppName = context.getString(appInfo.labelRes)) != null) {
                refererAppLinkData.putString("app_name", refererAppName);
            }
        }
        data.putAll(getAppLinkData());
        data.putString("target_url", getAppLink().getSourceUrl().toString());
        data.putString("version", "1.0");
        data.putString(KEY_NAME_USER_AGENT, "Bolts Android 1.4.0");
        data.putBundle(KEY_NAME_REFERER_APP_LINK, refererAppLinkData);
        data.putBundle(AppLinkData.ARGUMENTS_EXTRAS_KEY, getExtras());
        return data;
    }

    private Object getJSONValue(Object value) throws JSONException {
        if (value instanceof Bundle) {
            return getJSONForBundle((Bundle) value);
        }
        if (value instanceof CharSequence) {
            return value.toString();
        }
        if (value instanceof List) {
            JSONArray array = new JSONArray();
            for (Object listValue : (List) value) {
                array.put(getJSONValue(listValue));
            }
            return array;
        }
        if (value instanceof SparseArray) {
            JSONArray array2 = new JSONArray();
            SparseArray<?> sparseValue = (SparseArray) value;
            for (int i = 0; i < sparseValue.size(); i++) {
                array2.put(sparseValue.keyAt(i), getJSONValue(sparseValue.valueAt(i)));
            }
            return array2;
        }
        if (value instanceof Character) {
            return value.toString();
        }
        if (value instanceof Boolean) {
            return value;
        }
        if (value instanceof Number) {
            if ((value instanceof Double) || (value instanceof Float)) {
                return Double.valueOf(((Number) value).doubleValue());
            }
            return Long.valueOf(((Number) value).longValue());
        }
        if (value instanceof boolean[]) {
            JSONArray array3 = new JSONArray();
            boolean[] arr$ = (boolean[]) value;
            for (boolean arrValue : arr$) {
                array3.put(getJSONValue(Boolean.valueOf(arrValue)));
            }
            return array3;
        }
        if (value instanceof char[]) {
            JSONArray array4 = new JSONArray();
            char[] arr$2 = (char[]) value;
            for (char arrValue2 : arr$2) {
                array4.put(getJSONValue(Character.valueOf(arrValue2)));
            }
            return array4;
        }
        if (value instanceof CharSequence[]) {
            JSONArray array5 = new JSONArray();
            CharSequence[] arr$3 = (CharSequence[]) value;
            for (CharSequence arrValue3 : arr$3) {
                array5.put(getJSONValue(arrValue3));
            }
            return array5;
        }
        if (value instanceof double[]) {
            JSONArray array6 = new JSONArray();
            double[] arr$4 = (double[]) value;
            for (double arrValue4 : arr$4) {
                array6.put(getJSONValue(Double.valueOf(arrValue4)));
            }
            return array6;
        }
        if (value instanceof float[]) {
            JSONArray array7 = new JSONArray();
            float[] arr$5 = (float[]) value;
            for (float arrValue5 : arr$5) {
                array7.put(getJSONValue(Float.valueOf(arrValue5)));
            }
            return array7;
        }
        if (value instanceof int[]) {
            JSONArray array8 = new JSONArray();
            int[] arr$6 = (int[]) value;
            for (int arrValue6 : arr$6) {
                array8.put(getJSONValue(Integer.valueOf(arrValue6)));
            }
            return array8;
        }
        if (value instanceof long[]) {
            JSONArray array9 = new JSONArray();
            long[] arr$7 = (long[]) value;
            for (long arrValue7 : arr$7) {
                array9.put(getJSONValue(Long.valueOf(arrValue7)));
            }
            return array9;
        }
        if (value instanceof short[]) {
            JSONArray array10 = new JSONArray();
            short[] arr$8 = (short[]) value;
            for (short arrValue8 : arr$8) {
                array10.put(getJSONValue(Short.valueOf(arrValue8)));
            }
            return array10;
        }
        if (value instanceof String[]) {
            JSONArray array11 = new JSONArray();
            String[] arr$9 = (String[]) value;
            for (String arrValue9 : arr$9) {
                array11.put(getJSONValue(arrValue9));
            }
            return array11;
        }
        return null;
    }

    private JSONObject getJSONForBundle(Bundle bundle) throws JSONException {
        JSONObject root = new JSONObject();
        for (String key : bundle.keySet()) {
            root.put(key, getJSONValue(bundle.get(key)));
        }
        return root;
    }

    public NavigationResult navigate(Context context) {
        PackageManager pm = context.getPackageManager();
        Bundle finalAppLinkData = buildAppLinkDataForNavigation(context);
        Intent eligibleTargetIntent = null;
        Iterator<AppLink.Target> it = getAppLink().getTargets().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            AppLink.Target target = it.next();
            Intent targetIntent = new Intent("android.intent.action.VIEW");
            if (target.getUrl() != null) {
                targetIntent.setData(target.getUrl());
            } else {
                targetIntent.setData(this.appLink.getSourceUrl());
            }
            targetIntent.setPackage(target.getPackageName());
            if (target.getClassName() != null) {
                targetIntent.setClassName(target.getPackageName(), target.getClassName());
            }
            targetIntent.putExtra("al_applink_data", finalAppLinkData);
            ResolveInfo resolved = pm.resolveActivity(targetIntent, 65536);
            if (resolved != null) {
                eligibleTargetIntent = targetIntent;
                break;
            }
        }
        Intent outIntent = null;
        NavigationResult result = NavigationResult.FAILED;
        if (eligibleTargetIntent != null) {
            outIntent = eligibleTargetIntent;
            result = NavigationResult.APP;
        } else {
            Uri webUrl = getAppLink().getWebUrl();
            if (webUrl != null) {
                try {
                    JSONObject appLinkDataJson = getJSONForBundle(finalAppLinkData);
                    outIntent = new Intent("android.intent.action.VIEW", webUrl.buildUpon().appendQueryParameter("al_applink_data", appLinkDataJson.toString()).build());
                    result = NavigationResult.WEB;
                } catch (JSONException e) {
                    sendAppLinkNavigateEventBroadcast(context, eligibleTargetIntent, NavigationResult.FAILED, e);
                    throw new RuntimeException(e);
                }
            }
        }
        sendAppLinkNavigateEventBroadcast(context, outIntent, result, null);
        if (outIntent != null) {
            context.startActivity(outIntent);
        }
        return result;
    }

    private void sendAppLinkNavigateEventBroadcast(Context context, Intent intent, NavigationResult type, JSONException e) {
        Map<String, String> extraLoggingData = new HashMap<>();
        if (e != null) {
            extraLoggingData.put("error", e.getLocalizedMessage());
        }
        extraLoggingData.put(GraphResponse.SUCCESS_KEY, type.isSucceeded() ? AppEventsConstants.EVENT_PARAM_VALUE_YES : "0");
        extraLoggingData.put(ShareConstants.MEDIA_TYPE, type.getCode());
        MeasurementEvent.sendBroadcastEvent(context, MeasurementEvent.APP_LINK_NAVIGATE_OUT_EVENT_NAME, intent, extraLoggingData);
    }

    public static void setDefaultResolver(AppLinkResolver resolver) {
        defaultResolver = resolver;
    }

    public static AppLinkResolver getDefaultResolver() {
        return defaultResolver;
    }

    private static AppLinkResolver getResolver(Context context) {
        return getDefaultResolver() != null ? getDefaultResolver() : new WebViewAppLinkResolver(context);
    }

    public static NavigationResult navigate(Context context, AppLink appLink) {
        return new AppLinkNavigation(appLink, null, null).navigate(context);
    }

    public static Task<NavigationResult> navigateInBackground(final Context context, Uri destination, AppLinkResolver resolver) {
        return resolver.getAppLinkFromUrlInBackground(destination).onSuccess((Continuation<AppLink, TContinuationResult>) new Continuation<AppLink, NavigationResult>() { // from class: bolts.AppLinkNavigation.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // bolts.Continuation
            public NavigationResult then(Task<AppLink> task) throws Exception {
                return AppLinkNavigation.navigate(context, task.getResult());
            }
        }, Task.UI_THREAD_EXECUTOR);
    }

    public static Task<NavigationResult> navigateInBackground(Context context, URL destination, AppLinkResolver resolver) {
        return navigateInBackground(context, Uri.parse(destination.toString()), resolver);
    }

    public static Task<NavigationResult> navigateInBackground(Context context, String destinationUrl, AppLinkResolver resolver) {
        return navigateInBackground(context, Uri.parse(destinationUrl), resolver);
    }

    public static Task<NavigationResult> navigateInBackground(Context context, Uri destination) {
        return navigateInBackground(context, destination, getResolver(context));
    }

    public static Task<NavigationResult> navigateInBackground(Context context, URL destination) {
        return navigateInBackground(context, destination, getResolver(context));
    }

    public static Task<NavigationResult> navigateInBackground(Context context, String destinationUrl) {
        return navigateInBackground(context, destinationUrl, getResolver(context));
    }
}
