package com.facebook.applinks;

import android.net.Uri;
import android.os.Bundle;
import bolts.AppLink;
import bolts.AppLinkResolver;
import bolts.Continuation;
import bolts.Task;
import com.facebook.AccessToken;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes41.dex */
public class FacebookAppLinkResolver implements AppLinkResolver {
    private static final String APP_LINK_ANDROID_TARGET_KEY = "android";
    private static final String APP_LINK_KEY = "app_links";
    private static final String APP_LINK_TARGET_APP_NAME_KEY = "app_name";
    private static final String APP_LINK_TARGET_CLASS_KEY = "class";
    private static final String APP_LINK_TARGET_PACKAGE_KEY = "package";
    private static final String APP_LINK_TARGET_SHOULD_FALLBACK_KEY = "should_fallback";
    private static final String APP_LINK_TARGET_URL_KEY = "url";
    private static final String APP_LINK_WEB_TARGET_KEY = "web";
    private final HashMap<Uri, AppLink> cachedAppLinks = new HashMap<>();

    @Override // bolts.AppLinkResolver
    public Task<AppLink> getAppLinkFromUrlInBackground(final Uri uri) {
        ArrayList<Uri> uris = new ArrayList<>();
        uris.add(uri);
        Task<Map<Uri, AppLink>> resolveTask = getAppLinkFromUrlsInBackground(uris);
        return resolveTask.onSuccess(new Continuation<Map<Uri, AppLink>, AppLink>() { // from class: com.facebook.applinks.FacebookAppLinkResolver.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // bolts.Continuation
            public AppLink then(Task<Map<Uri, AppLink>> resolveUrisTask) throws Exception {
                return resolveUrisTask.getResult().get(uri);
            }
        });
    }

    public Task<Map<Uri, AppLink>> getAppLinkFromUrlsInBackground(List<Uri> uris) {
        AppLink appLink;
        final Map<Uri, AppLink> appLinkResults = new HashMap<>();
        final HashSet<Uri> urisToRequest = new HashSet<>();
        StringBuilder graphRequestFields = new StringBuilder();
        for (Uri uri : uris) {
            synchronized (this.cachedAppLinks) {
                appLink = this.cachedAppLinks.get(uri);
            }
            if (appLink != null) {
                appLinkResults.put(uri, appLink);
            } else {
                if (!urisToRequest.isEmpty()) {
                    graphRequestFields.append(',');
                }
                graphRequestFields.append(uri.toString());
                urisToRequest.add(uri);
            }
        }
        if (urisToRequest.isEmpty()) {
            return Task.forResult(appLinkResults);
        }
        final Task<Map<Uri, AppLink>>.TaskCompletionSource taskCompletionSource = Task.create();
        Bundle appLinkRequestParameters = new Bundle();
        appLinkRequestParameters.putString("ids", graphRequestFields.toString());
        appLinkRequestParameters.putString(GraphRequest.FIELDS_PARAM, String.format("%s.fields(%s,%s)", APP_LINK_KEY, "android", "web"));
        GraphRequest appLinkRequest = new GraphRequest(AccessToken.getCurrentAccessToken(), "", appLinkRequestParameters, null, new GraphRequest.Callback() { // from class: com.facebook.applinks.FacebookAppLinkResolver.2
            @Override // com.facebook.GraphRequest.Callback
            public void onCompleted(GraphResponse response) {
                FacebookRequestError error = response.getError();
                if (error != null) {
                    taskCompletionSource.setError(error.getException());
                    return;
                }
                JSONObject responseJson = response.getJSONObject();
                if (responseJson == null) {
                    taskCompletionSource.setResult(appLinkResults);
                    return;
                }
                for (Uri uri2 : urisToRequest) {
                    String uriString = uri2.toString();
                    if (responseJson.has(uriString)) {
                        try {
                            JSONObject urlData = responseJson.getJSONObject(uri2.toString());
                            JSONObject appLinkData = urlData.getJSONObject(FacebookAppLinkResolver.APP_LINK_KEY);
                            JSONArray rawTargets = appLinkData.getJSONArray("android");
                            int targetsCount = rawTargets.length();
                            List<AppLink.Target> targets = new ArrayList<>(targetsCount);
                            for (int i = 0; i < targetsCount; i++) {
                                AppLink.Target target = FacebookAppLinkResolver.getAndroidTargetFromJson(rawTargets.getJSONObject(i));
                                if (target != null) {
                                    targets.add(target);
                                }
                            }
                            Uri webFallbackUrl = FacebookAppLinkResolver.getWebFallbackUriFromJson(uri2, appLinkData);
                            AppLink appLink2 = new AppLink(uri2, targets, webFallbackUrl);
                            appLinkResults.put(uri2, appLink2);
                            synchronized (FacebookAppLinkResolver.this.cachedAppLinks) {
                                FacebookAppLinkResolver.this.cachedAppLinks.put(uri2, appLink2);
                            }
                        } catch (JSONException e) {
                        }
                    }
                }
                taskCompletionSource.setResult(appLinkResults);
            }
        });
        appLinkRequest.executeAsync();
        return taskCompletionSource.getTask();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static AppLink.Target getAndroidTargetFromJson(JSONObject targetJson) {
        String packageName = tryGetStringFromJson(targetJson, APP_LINK_TARGET_PACKAGE_KEY, null);
        if (packageName == null) {
            return null;
        }
        String className = tryGetStringFromJson(targetJson, APP_LINK_TARGET_CLASS_KEY, null);
        String appName = tryGetStringFromJson(targetJson, "app_name", null);
        String targetUrlString = tryGetStringFromJson(targetJson, "url", null);
        Uri targetUri = null;
        if (targetUrlString != null) {
            targetUri = Uri.parse(targetUrlString);
        }
        return new AppLink.Target(packageName, className, targetUri, appName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Uri getWebFallbackUriFromJson(Uri sourceUrl, JSONObject urlData) {
        try {
            JSONObject webTarget = urlData.getJSONObject("web");
            boolean shouldFallback = tryGetBooleanFromJson(webTarget, APP_LINK_TARGET_SHOULD_FALLBACK_KEY, true);
            if (!shouldFallback) {
                return null;
            }
            String webTargetUrlString = tryGetStringFromJson(webTarget, "url", null);
            Uri webUri = null;
            if (webTargetUrlString != null) {
                webUri = Uri.parse(webTargetUrlString);
            }
            return webUri == null ? sourceUrl : webUri;
        } catch (JSONException e) {
            return sourceUrl;
        }
    }

    private static String tryGetStringFromJson(JSONObject json, String propertyName, String defaultValue) {
        try {
            String defaultValue2 = json.getString(propertyName);
            return defaultValue2;
        } catch (JSONException e) {
            return defaultValue;
        }
    }

    private static boolean tryGetBooleanFromJson(JSONObject json, String propertyName, boolean defaultValue) {
        try {
            boolean defaultValue2 = json.getBoolean(propertyName);
            return defaultValue2;
        } catch (JSONException e) {
            return defaultValue;
        }
    }
}
