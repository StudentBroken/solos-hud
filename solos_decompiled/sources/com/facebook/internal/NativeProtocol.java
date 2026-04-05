package com.facebook.internal;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookSdk;
import com.facebook.login.DefaultAudience;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes41.dex */
public final class NativeProtocol {
    public static final String ACTION_APPINVITE_DIALOG = "com.facebook.platform.action.request.APPINVITES_DIALOG";
    public static final String ACTION_FEED_DIALOG = "com.facebook.platform.action.request.FEED_DIALOG";
    public static final String ACTION_LIKE_DIALOG = "com.facebook.platform.action.request.LIKE_DIALOG";
    public static final String ACTION_MESSAGE_DIALOG = "com.facebook.platform.action.request.MESSAGE_DIALOG";
    public static final String ACTION_OGACTIONPUBLISH_DIALOG = "com.facebook.platform.action.request.OGACTIONPUBLISH_DIALOG";
    public static final String ACTION_OGMESSAGEPUBLISH_DIALOG = "com.facebook.platform.action.request.OGMESSAGEPUBLISH_DIALOG";
    public static final String AUDIENCE_EVERYONE = "everyone";
    public static final String AUDIENCE_FRIENDS = "friends";
    public static final String AUDIENCE_ME = "only_me";
    public static final String BRIDGE_ARG_ACTION_ID_STRING = "action_id";
    public static final String BRIDGE_ARG_APP_NAME_STRING = "app_name";
    public static final String BRIDGE_ARG_ERROR_BUNDLE = "error";
    public static final String BRIDGE_ARG_ERROR_CODE = "error_code";
    public static final String BRIDGE_ARG_ERROR_DESCRIPTION = "error_description";
    public static final String BRIDGE_ARG_ERROR_JSON = "error_json";
    public static final String BRIDGE_ARG_ERROR_SUBCODE = "error_subcode";
    public static final String BRIDGE_ARG_ERROR_TYPE = "error_type";
    private static final String CONTENT_SCHEME = "content://";
    public static final String ERROR_APPLICATION_ERROR = "ApplicationError";
    public static final String ERROR_NETWORK_ERROR = "NetworkError";
    public static final String ERROR_PERMISSION_DENIED = "PermissionDenied";
    public static final String ERROR_PROTOCOL_ERROR = "ProtocolError";
    public static final String ERROR_SERVICE_DISABLED = "ServiceDisabled";
    public static final String ERROR_UNKNOWN_ERROR = "UnknownError";
    public static final String ERROR_USER_CANCELED = "UserCanceled";
    public static final String EXTRA_ACCESS_TOKEN = "com.facebook.platform.extra.ACCESS_TOKEN";
    public static final String EXTRA_APPLICATION_ID = "com.facebook.platform.extra.APPLICATION_ID";
    public static final String EXTRA_APPLICATION_NAME = "com.facebook.platform.extra.APPLICATION_NAME";
    public static final String EXTRA_DIALOG_COMPLETE_KEY = "com.facebook.platform.extra.DID_COMPLETE";
    public static final String EXTRA_DIALOG_COMPLETION_GESTURE_KEY = "com.facebook.platform.extra.COMPLETION_GESTURE";
    public static final String EXTRA_EXPIRES_SECONDS_SINCE_EPOCH = "com.facebook.platform.extra.EXPIRES_SECONDS_SINCE_EPOCH";
    public static final String EXTRA_GET_INSTALL_DATA_PACKAGE = "com.facebook.platform.extra.INSTALLDATA_PACKAGE";
    public static final String EXTRA_PERMISSIONS = "com.facebook.platform.extra.PERMISSIONS";
    public static final String EXTRA_PROTOCOL_ACTION = "com.facebook.platform.protocol.PROTOCOL_ACTION";
    public static final String EXTRA_PROTOCOL_BRIDGE_ARGS = "com.facebook.platform.protocol.BRIDGE_ARGS";
    public static final String EXTRA_PROTOCOL_CALL_ID = "com.facebook.platform.protocol.CALL_ID";
    public static final String EXTRA_PROTOCOL_METHOD_ARGS = "com.facebook.platform.protocol.METHOD_ARGS";
    public static final String EXTRA_PROTOCOL_METHOD_RESULTS = "com.facebook.platform.protocol.RESULT_ARGS";
    public static final String EXTRA_PROTOCOL_VERSION = "com.facebook.platform.protocol.PROTOCOL_VERSION";
    static final String EXTRA_PROTOCOL_VERSIONS = "com.facebook.platform.extra.PROTOCOL_VERSIONS";
    public static final String EXTRA_USER_ID = "com.facebook.platform.extra.USER_ID";
    private static final String FACEBOOK_PROXY_AUTH_ACTIVITY = "com.facebook.katana.ProxyAuth";
    public static final String FACEBOOK_PROXY_AUTH_APP_ID_KEY = "client_id";
    public static final String FACEBOOK_PROXY_AUTH_E2E_KEY = "e2e";
    public static final String FACEBOOK_PROXY_AUTH_PERMISSIONS_KEY = "scope";
    private static final String FACEBOOK_TOKEN_REFRESH_ACTIVITY = "com.facebook.katana.platform.TokenRefreshService";
    public static final String IMAGE_URL_KEY = "url";
    public static final String IMAGE_USER_GENERATED_KEY = "user_generated";
    static final String INTENT_ACTION_PLATFORM_ACTIVITY = "com.facebook.platform.PLATFORM_ACTIVITY";
    static final String INTENT_ACTION_PLATFORM_SERVICE = "com.facebook.platform.PLATFORM_SERVICE";
    public static final int MESSAGE_GET_ACCESS_TOKEN_REPLY = 65537;
    public static final int MESSAGE_GET_ACCESS_TOKEN_REQUEST = 65536;
    public static final int MESSAGE_GET_INSTALL_DATA_REPLY = 65541;
    public static final int MESSAGE_GET_INSTALL_DATA_REQUEST = 65540;
    public static final int MESSAGE_GET_LIKE_STATUS_REPLY = 65543;
    public static final int MESSAGE_GET_LIKE_STATUS_REQUEST = 65542;
    static final int MESSAGE_GET_PROTOCOL_VERSIONS_REPLY = 65539;
    static final int MESSAGE_GET_PROTOCOL_VERSIONS_REQUEST = 65538;
    public static final int NO_PROTOCOL_AVAILABLE = -1;
    public static final String OPEN_GRAPH_CREATE_OBJECT_KEY = "fbsdk:create_object";
    private static final String PLATFORM_PROVIDER = ".provider.PlatformProvider";
    private static final String PLATFORM_PROVIDER_VERSIONS = ".provider.PlatformProvider/versions";
    private static final String PLATFORM_PROVIDER_VERSION_COLUMN = "version";
    public static final String RESULT_ARGS_ACCESS_TOKEN = "access_token";
    public static final String RESULT_ARGS_DIALOG_COMPLETE_KEY = "didComplete";
    public static final String RESULT_ARGS_DIALOG_COMPLETION_GESTURE_KEY = "completionGesture";
    public static final String RESULT_ARGS_EXPIRES_SECONDS_SINCE_EPOCH = "expires_seconds_since_epoch";
    public static final String RESULT_ARGS_PERMISSIONS = "permissions";
    public static final String STATUS_ERROR_CODE = "com.facebook.platform.status.ERROR_CODE";
    public static final String STATUS_ERROR_DESCRIPTION = "com.facebook.platform.status.ERROR_DESCRIPTION";
    public static final String STATUS_ERROR_JSON = "com.facebook.platform.status.ERROR_JSON";
    public static final String STATUS_ERROR_SUBCODE = "com.facebook.platform.status.ERROR_SUBCODE";
    public static final String STATUS_ERROR_TYPE = "com.facebook.platform.status.ERROR_TYPE";
    public static final String WEB_DIALOG_ACTION = "action";
    public static final String WEB_DIALOG_IS_FALLBACK = "is_fallback";
    public static final String WEB_DIALOG_PARAMS = "params";
    public static final String WEB_DIALOG_URL = "url";
    private static final String TAG = NativeProtocol.class.getName();
    private static List<NativeAppInfo> facebookAppInfoList = buildFacebookAppList();
    private static Map<String, List<NativeAppInfo>> actionToAppInfoMap = buildActionToAppInfoMap();
    private static AtomicBoolean protocolVersionsAsyncUpdating = new AtomicBoolean(false);
    public static final int PROTOCOL_VERSION_20160327 = 20160327;
    public static final int PROTOCOL_VERSION_20141218 = 20141218;
    public static final int PROTOCOL_VERSION_20141107 = 20141107;
    public static final int PROTOCOL_VERSION_20141028 = 20141028;
    public static final int PROTOCOL_VERSION_20141001 = 20141001;
    public static final int PROTOCOL_VERSION_20140701 = 20140701;
    public static final int PROTOCOL_VERSION_20140324 = 20140324;
    public static final int PROTOCOL_VERSION_20140204 = 20140204;
    public static final int PROTOCOL_VERSION_20131107 = 20131107;
    public static final int PROTOCOL_VERSION_20130618 = 20130618;
    public static final int PROTOCOL_VERSION_20130502 = 20130502;
    public static final int PROTOCOL_VERSION_20121101 = 20121101;
    private static final List<Integer> KNOWN_PROTOCOL_VERSIONS = Arrays.asList(Integer.valueOf(PROTOCOL_VERSION_20160327), Integer.valueOf(PROTOCOL_VERSION_20141218), Integer.valueOf(PROTOCOL_VERSION_20141107), Integer.valueOf(PROTOCOL_VERSION_20141028), Integer.valueOf(PROTOCOL_VERSION_20141001), Integer.valueOf(PROTOCOL_VERSION_20140701), Integer.valueOf(PROTOCOL_VERSION_20140324), Integer.valueOf(PROTOCOL_VERSION_20140204), Integer.valueOf(PROTOCOL_VERSION_20131107), Integer.valueOf(PROTOCOL_VERSION_20130618), Integer.valueOf(PROTOCOL_VERSION_20130502), Integer.valueOf(PROTOCOL_VERSION_20121101));

    private static abstract class NativeAppInfo {
        private static final String FBI_HASH = "a4b7452e2ed8f5f191058ca7bbfd26b0d3214bfc";
        private static final String FBL_HASH = "5e8f16062ea3cd2c4a0d547876baa6f38cabf625";
        private static final String FBR_HASH = "8a3c4b262d721acd49a4bf97d5213199c86fa2b9";
        private static final HashSet<String> validAppSignatureHashes = buildAppSignatureHashes();
        private TreeSet<Integer> availableVersions;

        protected abstract String getLoginActivity();

        protected abstract String getPackage();

        private NativeAppInfo() {
        }

        private static HashSet<String> buildAppSignatureHashes() {
            HashSet<String> set = new HashSet<>();
            set.add(FBR_HASH);
            set.add(FBI_HASH);
            set.add(FBL_HASH);
            return set;
        }

        public boolean validateSignature(Context context, String packageName) {
            String brand = Build.BRAND;
            int applicationFlags = context.getApplicationInfo().flags;
            if (brand.startsWith("generic") && (applicationFlags & 2) != 0) {
                return true;
            }
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 64);
                if (packageInfo.signatures == null || packageInfo.signatures.length <= 0) {
                    return false;
                }
                Signature[] arr$ = packageInfo.signatures;
                for (Signature signature : arr$) {
                    String hashedSignature = Utility.sha1hash(signature.toByteArray());
                    if (!validAppSignatureHashes.contains(hashedSignature)) {
                        return false;
                    }
                }
                return true;
            } catch (PackageManager.NameNotFoundException e) {
                return false;
            }
        }

        public TreeSet<Integer> getAvailableVersions() {
            if (this.availableVersions == null) {
                fetchAvailableVersions(false);
            }
            return this.availableVersions;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void fetchAvailableVersions(boolean force) {
            if (!force) {
                if (this.availableVersions == null) {
                }
            }
            this.availableVersions = NativeProtocol.fetchAllAvailableProtocolVersionsForAppInfo(this);
        }
    }

    private static class KatanaAppInfo extends NativeAppInfo {
        static final String KATANA_PACKAGE = "com.facebook.katana";

        private KatanaAppInfo() {
            super();
        }

        @Override // com.facebook.internal.NativeProtocol.NativeAppInfo
        protected String getPackage() {
            return KATANA_PACKAGE;
        }

        @Override // com.facebook.internal.NativeProtocol.NativeAppInfo
        protected String getLoginActivity() {
            return NativeProtocol.FACEBOOK_PROXY_AUTH_ACTIVITY;
        }
    }

    private static class MessengerAppInfo extends NativeAppInfo {
        static final String MESSENGER_PACKAGE = "com.facebook.orca";

        private MessengerAppInfo() {
            super();
        }

        @Override // com.facebook.internal.NativeProtocol.NativeAppInfo
        protected String getPackage() {
            return "com.facebook.orca";
        }

        @Override // com.facebook.internal.NativeProtocol.NativeAppInfo
        protected String getLoginActivity() {
            return null;
        }
    }

    private static class WakizashiAppInfo extends NativeAppInfo {
        static final String WAKIZASHI_PACKAGE = "com.facebook.wakizashi";

        private WakizashiAppInfo() {
            super();
        }

        @Override // com.facebook.internal.NativeProtocol.NativeAppInfo
        protected String getPackage() {
            return WAKIZASHI_PACKAGE;
        }

        @Override // com.facebook.internal.NativeProtocol.NativeAppInfo
        protected String getLoginActivity() {
            return NativeProtocol.FACEBOOK_PROXY_AUTH_ACTIVITY;
        }
    }

    private static class FBLiteAppInfo extends NativeAppInfo {
        static final String FACEBOOK_LITE_ACTIVITY = "com.facebook.lite.platform.LoginGDPDialogActivity";
        static final String FBLITE_PACKAGE = "com.facebook.lite";

        private FBLiteAppInfo() {
            super();
        }

        @Override // com.facebook.internal.NativeProtocol.NativeAppInfo
        protected String getPackage() {
            return FBLITE_PACKAGE;
        }

        @Override // com.facebook.internal.NativeProtocol.NativeAppInfo
        protected String getLoginActivity() {
            return FACEBOOK_LITE_ACTIVITY;
        }
    }

    private static List<NativeAppInfo> buildFacebookAppList() {
        List<NativeAppInfo> list = new ArrayList<>();
        list.add(new KatanaAppInfo());
        list.add(new WakizashiAppInfo());
        return list;
    }

    private static Map<String, List<NativeAppInfo>> buildActionToAppInfoMap() {
        Map<String, List<NativeAppInfo>> map = new HashMap<>();
        ArrayList<NativeAppInfo> messengerAppInfoList = new ArrayList<>();
        messengerAppInfoList.add(new MessengerAppInfo());
        map.put(ACTION_OGACTIONPUBLISH_DIALOG, facebookAppInfoList);
        map.put(ACTION_FEED_DIALOG, facebookAppInfoList);
        map.put(ACTION_LIKE_DIALOG, facebookAppInfoList);
        map.put(ACTION_APPINVITE_DIALOG, facebookAppInfoList);
        map.put(ACTION_MESSAGE_DIALOG, messengerAppInfoList);
        map.put(ACTION_OGMESSAGEPUBLISH_DIALOG, messengerAppInfoList);
        return map;
    }

    static Intent validateActivityIntent(Context context, Intent intent, NativeAppInfo appInfo) {
        ResolveInfo resolveInfo;
        if (intent == null || (resolveInfo = context.getPackageManager().resolveActivity(intent, 0)) == null || !appInfo.validateSignature(context, resolveInfo.activityInfo.packageName)) {
            return null;
        }
        return intent;
    }

    static Intent validateServiceIntent(Context context, Intent intent, NativeAppInfo appInfo) {
        ResolveInfo resolveInfo;
        if (intent == null || (resolveInfo = context.getPackageManager().resolveService(intent, 0)) == null || !appInfo.validateSignature(context, resolveInfo.serviceInfo.packageName)) {
            return null;
        }
        return intent;
    }

    public static Intent createFacebookLiteIntent(Context context, String applicationId, Collection<String> permissions, String e2e, boolean isRerequest, boolean isForPublish, DefaultAudience defaultAudience, String clientState) {
        NativeAppInfo appInfo = new FBLiteAppInfo();
        Intent intent = createNativeAppIntent(appInfo, applicationId, permissions, e2e, isRerequest, isForPublish, defaultAudience, clientState);
        return validateActivityIntent(context, intent, appInfo);
    }

    private static Intent createNativeAppIntent(NativeAppInfo appInfo, String applicationId, Collection<String> permissions, String e2e, boolean isRerequest, boolean isForPublish, DefaultAudience defaultAudience, String clientState) {
        String activityName = appInfo.getLoginActivity();
        if (activityName == null) {
            return null;
        }
        Intent intent = new Intent().setClassName(appInfo.getPackage(), activityName).putExtra("client_id", applicationId);
        if (!Utility.isNullOrEmpty(permissions)) {
            intent.putExtra("scope", TextUtils.join(",", permissions));
        }
        if (!Utility.isNullOrEmpty(e2e)) {
            intent.putExtra("e2e", e2e);
        }
        intent.putExtra(ServerProtocol.DIALOG_PARAM_STATE, clientState);
        intent.putExtra(ServerProtocol.DIALOG_PARAM_RESPONSE_TYPE, ServerProtocol.DIALOG_RESPONSE_TYPE_TOKEN_AND_SIGNED_REQUEST);
        intent.putExtra(ServerProtocol.DIALOG_PARAM_RETURN_SCOPES, ServerProtocol.DIALOG_RETURN_SCOPES_TRUE);
        if (isForPublish) {
            intent.putExtra(ServerProtocol.DIALOG_PARAM_DEFAULT_AUDIENCE, defaultAudience.getNativeProtocolAudience());
        }
        intent.putExtra(ServerProtocol.DIALOG_PARAM_LEGACY_OVERRIDE, ServerProtocol.GRAPH_API_VERSION);
        intent.putExtra(ServerProtocol.DIALOG_PARAM_AUTH_TYPE, ServerProtocol.DIALOG_REREQUEST_AUTH_TYPE);
        return intent;
    }

    public static Intent createProxyAuthIntent(Context context, String applicationId, Collection<String> permissions, String e2e, boolean isRerequest, boolean isForPublish, DefaultAudience defaultAudience, String clientState) {
        for (NativeAppInfo appInfo : facebookAppInfoList) {
            Intent intent = validateActivityIntent(context, createNativeAppIntent(appInfo, applicationId, permissions, e2e, isRerequest, isForPublish, defaultAudience, clientState), appInfo);
            if (intent != null) {
                return intent;
            }
        }
        return null;
    }

    public static Intent createTokenRefreshIntent(Context context) {
        for (NativeAppInfo appInfo : facebookAppInfoList) {
            Intent intent = validateServiceIntent(context, new Intent().setClassName(appInfo.getPackage(), FACEBOOK_TOKEN_REFRESH_ACTIVITY), appInfo);
            if (intent != null) {
                return intent;
            }
        }
        return null;
    }

    public static final int getLatestKnownVersion() {
        return KNOWN_PROTOCOL_VERSIONS.get(0).intValue();
    }

    private static Intent findActivityIntent(Context context, String activityAction, String internalAction) {
        List<NativeAppInfo> list = actionToAppInfoMap.get(internalAction);
        if (list == null) {
            return null;
        }
        Intent intent = null;
        for (NativeAppInfo appInfo : list) {
            Intent intent2 = new Intent().setAction(activityAction).setPackage(appInfo.getPackage()).addCategory("android.intent.category.DEFAULT");
            intent = validateActivityIntent(context, intent2, appInfo);
            if (intent != null) {
                return intent;
            }
        }
        return intent;
    }

    public static boolean isVersionCompatibleWithBucketedIntent(int version) {
        return KNOWN_PROTOCOL_VERSIONS.contains(Integer.valueOf(version)) && version >= 20140701;
    }

    public static Intent createPlatformActivityIntent(Context context, String callId, String action, int version, Bundle extras) {
        Intent intent = findActivityIntent(context, INTENT_ACTION_PLATFORM_ACTIVITY, action);
        if (intent == null) {
            return null;
        }
        setupProtocolRequestIntent(intent, callId, action, version, extras);
        return intent;
    }

    public static void setupProtocolRequestIntent(Intent intent, String callId, String action, int version, Bundle params) {
        String applicationId = FacebookSdk.getApplicationId();
        String applicationName = FacebookSdk.getApplicationName();
        intent.putExtra(EXTRA_PROTOCOL_VERSION, version).putExtra(EXTRA_PROTOCOL_ACTION, action).putExtra(EXTRA_APPLICATION_ID, applicationId);
        if (isVersionCompatibleWithBucketedIntent(version)) {
            Bundle bridgeArguments = new Bundle();
            bridgeArguments.putString("action_id", callId);
            Utility.putNonEmptyString(bridgeArguments, BRIDGE_ARG_APP_NAME_STRING, applicationName);
            intent.putExtra(EXTRA_PROTOCOL_BRIDGE_ARGS, bridgeArguments);
            Bundle methodArguments = params == null ? new Bundle() : params;
            intent.putExtra(EXTRA_PROTOCOL_METHOD_ARGS, methodArguments);
            return;
        }
        intent.putExtra(EXTRA_PROTOCOL_CALL_ID, callId);
        if (!Utility.isNullOrEmpty(applicationName)) {
            intent.putExtra(EXTRA_APPLICATION_NAME, applicationName);
        }
        intent.putExtras(params);
    }

    public static Intent createProtocolResultIntent(Intent requestIntent, Bundle results, FacebookException error) {
        UUID callId = getCallIdFromIntent(requestIntent);
        if (callId == null) {
            return null;
        }
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_PROTOCOL_VERSION, getProtocolVersionFromIntent(requestIntent));
        Bundle bridgeArguments = new Bundle();
        bridgeArguments.putString("action_id", callId.toString());
        if (error != null) {
            bridgeArguments.putBundle("error", createBundleForException(error));
        }
        resultIntent.putExtra(EXTRA_PROTOCOL_BRIDGE_ARGS, bridgeArguments);
        if (results != null) {
            resultIntent.putExtra(EXTRA_PROTOCOL_METHOD_RESULTS, results);
            return resultIntent;
        }
        return resultIntent;
    }

    public static Intent createPlatformServiceIntent(Context context) {
        for (NativeAppInfo appInfo : facebookAppInfoList) {
            Intent intent = validateServiceIntent(context, new Intent(INTENT_ACTION_PLATFORM_SERVICE).setPackage(appInfo.getPackage()).addCategory("android.intent.category.DEFAULT"), appInfo);
            if (intent != null) {
                return intent;
            }
        }
        return null;
    }

    public static int getProtocolVersionFromIntent(Intent intent) {
        return intent.getIntExtra(EXTRA_PROTOCOL_VERSION, 0);
    }

    public static UUID getCallIdFromIntent(Intent intent) {
        if (intent == null) {
            return null;
        }
        int version = getProtocolVersionFromIntent(intent);
        String callIdString = null;
        if (isVersionCompatibleWithBucketedIntent(version)) {
            Bundle bridgeArgs = intent.getBundleExtra(EXTRA_PROTOCOL_BRIDGE_ARGS);
            if (bridgeArgs != null) {
                callIdString = bridgeArgs.getString("action_id");
            }
        } else {
            callIdString = intent.getStringExtra(EXTRA_PROTOCOL_CALL_ID);
        }
        if (callIdString == null) {
            return null;
        }
        try {
            UUID callId = UUID.fromString(callIdString);
            return callId;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static Bundle getBridgeArgumentsFromIntent(Intent intent) {
        int version = getProtocolVersionFromIntent(intent);
        if (isVersionCompatibleWithBucketedIntent(version)) {
            return intent.getBundleExtra(EXTRA_PROTOCOL_BRIDGE_ARGS);
        }
        return null;
    }

    public static Bundle getMethodArgumentsFromIntent(Intent intent) {
        int version = getProtocolVersionFromIntent(intent);
        return !isVersionCompatibleWithBucketedIntent(version) ? intent.getExtras() : intent.getBundleExtra(EXTRA_PROTOCOL_METHOD_ARGS);
    }

    public static Bundle getSuccessResultsFromIntent(Intent resultIntent) {
        int version = getProtocolVersionFromIntent(resultIntent);
        Bundle extras = resultIntent.getExtras();
        return (!isVersionCompatibleWithBucketedIntent(version) || extras == null) ? extras : extras.getBundle(EXTRA_PROTOCOL_METHOD_RESULTS);
    }

    public static boolean isErrorResult(Intent resultIntent) {
        Bundle bridgeArgs = getBridgeArgumentsFromIntent(resultIntent);
        return bridgeArgs != null ? bridgeArgs.containsKey("error") : resultIntent.hasExtra(STATUS_ERROR_TYPE);
    }

    public static Bundle getErrorDataFromResultIntent(Intent resultIntent) {
        if (!isErrorResult(resultIntent)) {
            return null;
        }
        Bundle bridgeArgs = getBridgeArgumentsFromIntent(resultIntent);
        if (bridgeArgs != null) {
            return bridgeArgs.getBundle("error");
        }
        return resultIntent.getExtras();
    }

    public static FacebookException getExceptionFromErrorData(Bundle errorData) {
        if (errorData == null) {
            return null;
        }
        String type = errorData.getString(BRIDGE_ARG_ERROR_TYPE);
        if (type == null) {
            type = errorData.getString(STATUS_ERROR_TYPE);
        }
        String description = errorData.getString(BRIDGE_ARG_ERROR_DESCRIPTION);
        if (description == null) {
            description = errorData.getString(STATUS_ERROR_DESCRIPTION);
        }
        if (type != null && type.equalsIgnoreCase(ERROR_USER_CANCELED)) {
            return new FacebookOperationCanceledException(description);
        }
        return new FacebookException(description);
    }

    public static Bundle createBundleForException(FacebookException e) {
        if (e == null) {
            return null;
        }
        Bundle errorBundle = new Bundle();
        errorBundle.putString(BRIDGE_ARG_ERROR_DESCRIPTION, e.toString());
        if (e instanceof FacebookOperationCanceledException) {
            errorBundle.putString(BRIDGE_ARG_ERROR_TYPE, ERROR_USER_CANCELED);
            return errorBundle;
        }
        return errorBundle;
    }

    public static int getLatestAvailableProtocolVersionForService(int minimumVersion) {
        return getLatestAvailableProtocolVersionForAppInfoList(facebookAppInfoList, new int[]{minimumVersion});
    }

    public static int getLatestAvailableProtocolVersionForAction(String action, int[] versionSpec) {
        List<NativeAppInfo> appInfoList = actionToAppInfoMap.get(action);
        return getLatestAvailableProtocolVersionForAppInfoList(appInfoList, versionSpec);
    }

    private static int getLatestAvailableProtocolVersionForAppInfoList(List<NativeAppInfo> appInfoList, int[] versionSpec) {
        updateAllAvailableProtocolVersionsAsync();
        if (appInfoList == null) {
            return -1;
        }
        for (NativeAppInfo appInfo : appInfoList) {
            int protocolVersion = computeLatestAvailableVersionFromVersionSpec(appInfo.getAvailableVersions(), getLatestKnownVersion(), versionSpec);
            if (protocolVersion != -1) {
                return protocolVersion;
            }
        }
        return -1;
    }

    public static void updateAllAvailableProtocolVersionsAsync() {
        if (protocolVersionsAsyncUpdating.compareAndSet(false, true)) {
            FacebookSdk.getExecutor().execute(new Runnable() { // from class: com.facebook.internal.NativeProtocol.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        for (NativeAppInfo appInfo : NativeProtocol.facebookAppInfoList) {
                            appInfo.fetchAvailableVersions(true);
                        }
                    } finally {
                        NativeProtocol.protocolVersionsAsyncUpdating.set(false);
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static TreeSet<Integer> fetchAllAvailableProtocolVersionsForAppInfo(NativeAppInfo appInfo) {
        TreeSet<Integer> allAvailableVersions = new TreeSet<>();
        Context appContext = FacebookSdk.getApplicationContext();
        ContentResolver contentResolver = appContext.getContentResolver();
        String[] projection = {"version"};
        Uri uri = buildPlatformProviderVersionURI(appInfo);
        Cursor c = null;
        try {
            PackageManager pm = FacebookSdk.getApplicationContext().getPackageManager();
            String contentProviderName = appInfo.getPackage() + PLATFORM_PROVIDER;
            ProviderInfo pInfo = pm.resolveContentProvider(contentProviderName, 0);
            if (pInfo != null) {
                try {
                    c = contentResolver.query(uri, projection, null, null, null);
                } catch (NullPointerException | SecurityException e) {
                    Log.e(TAG, "Failed to query content resolver.");
                    c = null;
                }
                if (c != null) {
                    while (c.moveToNext()) {
                        int version = c.getInt(c.getColumnIndex("version"));
                        allAvailableVersions.add(Integer.valueOf(version));
                    }
                }
            }
            return allAvailableVersions;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    public static int computeLatestAvailableVersionFromVersionSpec(TreeSet<Integer> allAvailableFacebookAppVersions, int latestSdkVersion, int[] versionSpec) {
        int versionSpecIndex = versionSpec.length - 1;
        Iterator<Integer> fbAppVersionsIterator = allAvailableFacebookAppVersions.descendingIterator();
        int latestFacebookAppVersion = -1;
        while (fbAppVersionsIterator.hasNext()) {
            int fbAppVersion = fbAppVersionsIterator.next().intValue();
            latestFacebookAppVersion = Math.max(latestFacebookAppVersion, fbAppVersion);
            while (versionSpecIndex >= 0 && versionSpec[versionSpecIndex] > fbAppVersion) {
                versionSpecIndex--;
            }
            if (versionSpecIndex < 0) {
                return -1;
            }
            if (versionSpec[versionSpecIndex] == fbAppVersion) {
                return versionSpecIndex % 2 == 0 ? Math.min(latestFacebookAppVersion, latestSdkVersion) : -1;
            }
        }
        return -1;
    }

    private static Uri buildPlatformProviderVersionURI(NativeAppInfo appInfo) {
        return Uri.parse(CONTENT_SCHEME + appInfo.getPackage() + PLATFORM_PROVIDER_VERSIONS);
    }
}
