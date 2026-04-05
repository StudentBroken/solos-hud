package com.facebook.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import com.facebook.AccessToken;
import com.facebook.FacebookException;
import com.facebook.GraphResponse;
import com.facebook.R;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.internal.NativeProtocol;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes41.dex */
class LoginClient implements Parcelable {
    public static final Parcelable.Creator<LoginClient> CREATOR = new Parcelable.Creator() { // from class: com.facebook.login.LoginClient.1
        @Override // android.os.Parcelable.Creator
        public LoginClient createFromParcel(Parcel source) {
            return new LoginClient(source);
        }

        @Override // android.os.Parcelable.Creator
        public LoginClient[] newArray(int size) {
            return new LoginClient[size];
        }
    };
    BackgroundProcessingListener backgroundProcessingListener;
    boolean checkedInternetPermission;
    int currentHandler;
    Fragment fragment;
    LoginMethodHandler[] handlersToTry;
    Map<String, String> loggingExtras;
    private LoginLogger loginLogger;
    OnCompletedListener onCompletedListener;
    Request pendingRequest;

    interface BackgroundProcessingListener {
        void onBackgroundProcessingStarted();

        void onBackgroundProcessingStopped();
    }

    public interface OnCompletedListener {
        void onCompleted(Result result);
    }

    public LoginClient(Fragment fragment) {
        this.currentHandler = -1;
        this.fragment = fragment;
    }

    public Fragment getFragment() {
        return this.fragment;
    }

    void setFragment(Fragment fragment) {
        if (this.fragment != null) {
            throw new FacebookException("Can't set fragment once it is already set.");
        }
        this.fragment = fragment;
    }

    FragmentActivity getActivity() {
        return this.fragment.getActivity();
    }

    public Request getPendingRequest() {
        return this.pendingRequest;
    }

    public static int getLoginRequestCode() {
        return CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode();
    }

    void startOrContinueAuth(Request request) {
        if (!getInProgress()) {
            authorize(request);
        }
    }

    void authorize(Request request) {
        if (request != null) {
            if (this.pendingRequest != null) {
                throw new FacebookException("Attempted to authorize while a request is pending.");
            }
            if (AccessToken.getCurrentAccessToken() == null || checkInternetPermission()) {
                this.pendingRequest = request;
                this.handlersToTry = getHandlersToTry(request);
                tryNextHandler();
            }
        }
    }

    boolean getInProgress() {
        return this.pendingRequest != null && this.currentHandler >= 0;
    }

    void cancelCurrentHandler() {
        if (this.currentHandler >= 0) {
            getCurrentHandler().cancel();
        }
    }

    LoginMethodHandler getCurrentHandler() {
        if (this.currentHandler >= 0) {
            return this.handlersToTry[this.currentHandler];
        }
        return null;
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (this.pendingRequest != null) {
            return getCurrentHandler().onActivityResult(requestCode, resultCode, data);
        }
        return false;
    }

    private LoginMethodHandler[] getHandlersToTry(Request request) {
        ArrayList<LoginMethodHandler> handlers = new ArrayList<>();
        LoginBehavior behavior = request.getLoginBehavior();
        if (behavior.allowsKatanaAuth()) {
            handlers.add(new GetTokenLoginMethodHandler(this));
            handlers.add(new KatanaProxyLoginMethodHandler(this));
        }
        if (behavior.allowsFacebookLiteAuth()) {
            handlers.add(new FacebookLiteLoginMethodHandler(this));
        }
        if (behavior.allowsCustomTabAuth()) {
            handlers.add(new CustomTabLoginMethodHandler(this));
        }
        if (behavior.allowsWebViewAuth()) {
            handlers.add(new WebViewLoginMethodHandler(this));
        }
        if (behavior.allowsDeviceAuth()) {
            handlers.add(new DeviceAuthMethodHandler(this));
        }
        LoginMethodHandler[] result = new LoginMethodHandler[handlers.size()];
        handlers.toArray(result);
        return result;
    }

    boolean checkInternetPermission() {
        if (this.checkedInternetPermission) {
            return true;
        }
        int permissionCheck = checkPermission("android.permission.INTERNET");
        if (permissionCheck != 0) {
            Activity activity = getActivity();
            String errorType = activity.getString(R.string.com_facebook_internet_permission_error_title);
            String errorDescription = activity.getString(R.string.com_facebook_internet_permission_error_message);
            complete(Result.createErrorResult(this.pendingRequest, errorType, errorDescription));
            return false;
        }
        this.checkedInternetPermission = true;
        return true;
    }

    void tryNextHandler() {
        if (this.currentHandler >= 0) {
            logAuthorizationMethodComplete(getCurrentHandler().getNameForLogging(), "skipped", null, null, getCurrentHandler().methodLoggingExtras);
        }
        while (this.handlersToTry != null && this.currentHandler < this.handlersToTry.length - 1) {
            this.currentHandler++;
            boolean started = tryCurrentHandler();
            if (started) {
                return;
            }
        }
        if (this.pendingRequest != null) {
            completeWithFailure();
        }
    }

    private void completeWithFailure() {
        complete(Result.createErrorResult(this.pendingRequest, "Login attempt failed.", null));
    }

    private void addLoggingExtra(String key, String value, boolean accumulate) {
        if (this.loggingExtras == null) {
            this.loggingExtras = new HashMap();
        }
        if (this.loggingExtras.containsKey(key) && accumulate) {
            value = this.loggingExtras.get(key) + "," + value;
        }
        this.loggingExtras.put(key, value);
    }

    boolean tryCurrentHandler() {
        boolean tried = false;
        LoginMethodHandler handler = getCurrentHandler();
        if (handler.needsInternetPermission() && !checkInternetPermission()) {
            addLoggingExtra("no_internet_permission", AppEventsConstants.EVENT_PARAM_VALUE_YES, false);
        } else {
            tried = handler.tryAuthorize(this.pendingRequest);
            if (tried) {
                getLogger().logAuthorizationMethodStart(this.pendingRequest.getAuthId(), handler.getNameForLogging());
            } else {
                addLoggingExtra("not_tried", handler.getNameForLogging(), true);
            }
        }
        return tried;
    }

    void completeAndValidate(Result outcome) {
        if (outcome.token != null && AccessToken.getCurrentAccessToken() != null) {
            validateSameFbidAndFinish(outcome);
        } else {
            complete(outcome);
        }
    }

    void complete(Result outcome) {
        LoginMethodHandler handler = getCurrentHandler();
        if (handler != null) {
            logAuthorizationMethodComplete(handler.getNameForLogging(), outcome, handler.methodLoggingExtras);
        }
        if (this.loggingExtras != null) {
            outcome.loggingExtras = this.loggingExtras;
        }
        this.handlersToTry = null;
        this.currentHandler = -1;
        this.pendingRequest = null;
        this.loggingExtras = null;
        notifyOnCompleteListener(outcome);
    }

    OnCompletedListener getOnCompletedListener() {
        return this.onCompletedListener;
    }

    void setOnCompletedListener(OnCompletedListener onCompletedListener) {
        this.onCompletedListener = onCompletedListener;
    }

    BackgroundProcessingListener getBackgroundProcessingListener() {
        return this.backgroundProcessingListener;
    }

    void setBackgroundProcessingListener(BackgroundProcessingListener backgroundProcessingListener) {
        this.backgroundProcessingListener = backgroundProcessingListener;
    }

    int checkPermission(String permission) {
        return getActivity().checkCallingOrSelfPermission(permission);
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0031 A[Catch: Exception -> 0x003b, TRY_LEAVE, TryCatch #0 {Exception -> 0x003b, blocks: (B:9:0x0017, B:11:0x0025, B:12:0x002d, B:14:0x0031), top: B:18:0x0017 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void validateSameFbidAndFinish(com.facebook.login.LoginClient.Result r8) {
        /*
            r7 = this;
            com.facebook.AccessToken r4 = r8.token
            if (r4 != 0) goto Lc
            com.facebook.FacebookException r4 = new com.facebook.FacebookException
            java.lang.String r5 = "Can't validate without a token"
            r4.<init>(r5)
            throw r4
        Lc:
            com.facebook.AccessToken r2 = com.facebook.AccessToken.getCurrentAccessToken()
            com.facebook.AccessToken r1 = r8.token
            r3 = 0
            if (r2 == 0) goto L31
            if (r1 == 0) goto L31
            java.lang.String r4 = r2.getUserId()     // Catch: java.lang.Exception -> L3b
            java.lang.String r5 = r1.getUserId()     // Catch: java.lang.Exception -> L3b
            boolean r4 = r4.equals(r5)     // Catch: java.lang.Exception -> L3b
            if (r4 == 0) goto L31
            com.facebook.login.LoginClient$Request r4 = r7.pendingRequest     // Catch: java.lang.Exception -> L3b
            com.facebook.AccessToken r5 = r8.token     // Catch: java.lang.Exception -> L3b
            com.facebook.login.LoginClient$Result r3 = com.facebook.login.LoginClient.Result.createTokenResult(r4, r5)     // Catch: java.lang.Exception -> L3b
        L2d:
            r7.complete(r3)     // Catch: java.lang.Exception -> L3b
        L30:
            return
        L31:
            com.facebook.login.LoginClient$Request r4 = r7.pendingRequest     // Catch: java.lang.Exception -> L3b
            java.lang.String r5 = "User logged in as different Facebook user."
            r6 = 0
            com.facebook.login.LoginClient$Result r3 = com.facebook.login.LoginClient.Result.createErrorResult(r4, r5, r6)     // Catch: java.lang.Exception -> L3b
            goto L2d
        L3b:
            r0 = move-exception
            com.facebook.login.LoginClient$Request r4 = r7.pendingRequest
            java.lang.String r5 = "Caught exception"
            java.lang.String r6 = r0.getMessage()
            com.facebook.login.LoginClient$Result r4 = com.facebook.login.LoginClient.Result.createErrorResult(r4, r5, r6)
            r7.complete(r4)
            goto L30
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.login.LoginClient.validateSameFbidAndFinish(com.facebook.login.LoginClient$Result):void");
    }

    private static AccessToken createFromTokenWithRefreshedPermissions(AccessToken token, Collection<String> grantedPermissions, Collection<String> declinedPermissions) {
        return new AccessToken(token.getToken(), token.getApplicationId(), token.getUserId(), grantedPermissions, declinedPermissions, token.getSource(), token.getExpires(), token.getLastRefresh());
    }

    private LoginLogger getLogger() {
        if (this.loginLogger == null || !this.loginLogger.getApplicationId().equals(this.pendingRequest.getApplicationId())) {
            this.loginLogger = new LoginLogger(getActivity(), this.pendingRequest.getApplicationId());
        }
        return this.loginLogger;
    }

    private void notifyOnCompleteListener(Result outcome) {
        if (this.onCompletedListener != null) {
            this.onCompletedListener.onCompleted(outcome);
        }
    }

    void notifyBackgroundProcessingStart() {
        if (this.backgroundProcessingListener != null) {
            this.backgroundProcessingListener.onBackgroundProcessingStarted();
        }
    }

    void notifyBackgroundProcessingStop() {
        if (this.backgroundProcessingListener != null) {
            this.backgroundProcessingListener.onBackgroundProcessingStopped();
        }
    }

    private void logAuthorizationMethodComplete(String method, Result result, Map<String, String> loggingExtras) {
        logAuthorizationMethodComplete(method, result.code.getLoggingValue(), result.errorMessage, result.errorCode, loggingExtras);
    }

    private void logAuthorizationMethodComplete(String method, String result, String errorMessage, String errorCode, Map<String, String> loggingExtras) {
        if (this.pendingRequest == null) {
            getLogger().logUnexpectedError("fb_mobile_login_method_complete", "Unexpected call to logCompleteLogin with null pendingAuthorizationRequest.", method);
        } else {
            getLogger().logAuthorizationMethodComplete(this.pendingRequest.getAuthId(), method, result, errorMessage, errorCode, loggingExtras);
        }
    }

    static String getE2E() {
        JSONObject e2e = new JSONObject();
        try {
            e2e.put("init", System.currentTimeMillis());
        } catch (JSONException e) {
        }
        return e2e.toString();
    }

    public static class Request implements Parcelable {
        public static final Parcelable.Creator<Request> CREATOR = new Parcelable.Creator() { // from class: com.facebook.login.LoginClient.Request.1
            @Override // android.os.Parcelable.Creator
            public Request createFromParcel(Parcel source) {
                return new Request(source);
            }

            @Override // android.os.Parcelable.Creator
            public Request[] newArray(int size) {
                return new Request[size];
            }
        };
        private final String applicationId;
        private final String authId;
        private final DefaultAudience defaultAudience;
        private String deviceRedirectUriString;
        private boolean isRerequest;
        private final LoginBehavior loginBehavior;
        private Set<String> permissions;

        Request(LoginBehavior loginBehavior, Set<String> permissions, DefaultAudience defaultAudience, String applicationId, String authId) {
            this.isRerequest = false;
            this.loginBehavior = loginBehavior;
            this.permissions = permissions == null ? new HashSet<>() : permissions;
            this.defaultAudience = defaultAudience;
            this.applicationId = applicationId;
            this.authId = authId;
        }

        Set<String> getPermissions() {
            return this.permissions;
        }

        void setPermissions(Set<String> permissions) {
            Validate.notNull(permissions, NativeProtocol.RESULT_ARGS_PERMISSIONS);
            this.permissions = permissions;
        }

        LoginBehavior getLoginBehavior() {
            return this.loginBehavior;
        }

        DefaultAudience getDefaultAudience() {
            return this.defaultAudience;
        }

        String getApplicationId() {
            return this.applicationId;
        }

        String getAuthId() {
            return this.authId;
        }

        boolean isRerequest() {
            return this.isRerequest;
        }

        void setRerequest(boolean isRerequest) {
            this.isRerequest = isRerequest;
        }

        String getDeviceRedirectUriString() {
            return this.deviceRedirectUriString;
        }

        void setDeviceRedirectUriString(String deviceRedirectUriString) {
            this.deviceRedirectUriString = deviceRedirectUriString;
        }

        boolean hasPublishPermission() {
            for (String permission : this.permissions) {
                if (LoginManager.isPublishPermission(permission)) {
                    return true;
                }
            }
            return false;
        }

        private Request(Parcel parcel) {
            this.isRerequest = false;
            String enumValue = parcel.readString();
            this.loginBehavior = enumValue != null ? LoginBehavior.valueOf(enumValue) : null;
            ArrayList<String> permissionsList = new ArrayList<>();
            parcel.readStringList(permissionsList);
            this.permissions = new HashSet(permissionsList);
            String enumValue2 = parcel.readString();
            this.defaultAudience = enumValue2 != null ? DefaultAudience.valueOf(enumValue2) : null;
            this.applicationId = parcel.readString();
            this.authId = parcel.readString();
            this.isRerequest = parcel.readByte() != 0;
            this.deviceRedirectUriString = parcel.readString();
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.loginBehavior != null ? this.loginBehavior.name() : null);
            dest.writeStringList(new ArrayList(this.permissions));
            dest.writeString(this.defaultAudience != null ? this.defaultAudience.name() : null);
            dest.writeString(this.applicationId);
            dest.writeString(this.authId);
            dest.writeByte((byte) (this.isRerequest ? 1 : 0));
            dest.writeString(this.deviceRedirectUriString);
        }
    }

    public static class Result implements Parcelable {
        public static final Parcelable.Creator<Result> CREATOR = new Parcelable.Creator() { // from class: com.facebook.login.LoginClient.Result.1
            @Override // android.os.Parcelable.Creator
            public Result createFromParcel(Parcel source) {
                return new Result(source);
            }

            @Override // android.os.Parcelable.Creator
            public Result[] newArray(int size) {
                return new Result[size];
            }
        };
        final Code code;
        final String errorCode;
        final String errorMessage;
        public Map<String, String> loggingExtras;
        final Request request;
        final AccessToken token;

        enum Code {
            SUCCESS(GraphResponse.SUCCESS_KEY),
            CANCEL("cancel"),
            ERROR("error");

            private final String loggingValue;

            Code(String loggingValue) {
                this.loggingValue = loggingValue;
            }

            String getLoggingValue() {
                return this.loggingValue;
            }
        }

        Result(Request request, Code code, AccessToken token, String errorMessage, String errorCode) {
            Validate.notNull(code, "code");
            this.request = request;
            this.token = token;
            this.errorMessage = errorMessage;
            this.code = code;
            this.errorCode = errorCode;
        }

        static Result createTokenResult(Request request, AccessToken token) {
            return new Result(request, Code.SUCCESS, token, null, null);
        }

        static Result createCancelResult(Request request, String message) {
            return new Result(request, Code.CANCEL, null, message, null);
        }

        static Result createErrorResult(Request request, String errorType, String errorDescription) {
            return createErrorResult(request, errorType, errorDescription, null);
        }

        static Result createErrorResult(Request request, String errorType, String errorDescription, String errorCode) {
            String message = TextUtils.join(": ", Utility.asListNoNulls(errorType, errorDescription));
            return new Result(request, Code.ERROR, null, message, errorCode);
        }

        private Result(Parcel parcel) {
            this.code = Code.valueOf(parcel.readString());
            this.token = (AccessToken) parcel.readParcelable(AccessToken.class.getClassLoader());
            this.errorMessage = parcel.readString();
            this.errorCode = parcel.readString();
            this.request = (Request) parcel.readParcelable(Request.class.getClassLoader());
            this.loggingExtras = Utility.readStringMapFromParcel(parcel);
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.code.name());
            dest.writeParcelable(this.token, flags);
            dest.writeString(this.errorMessage);
            dest.writeString(this.errorCode);
            dest.writeParcelable(this.request, flags);
            Utility.writeStringMapToParcel(dest, this.loggingExtras);
        }
    }

    public LoginClient(Parcel source) {
        this.currentHandler = -1;
        Object[] o = source.readParcelableArray(LoginMethodHandler.class.getClassLoader());
        this.handlersToTry = new LoginMethodHandler[o.length];
        for (int i = 0; i < o.length; i++) {
            this.handlersToTry[i] = (LoginMethodHandler) o[i];
            this.handlersToTry[i].setLoginClient(this);
        }
        this.currentHandler = source.readInt();
        this.pendingRequest = (Request) source.readParcelable(Request.class.getClassLoader());
        this.loggingExtras = Utility.readStringMapFromParcel(source);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelableArray(this.handlersToTry, flags);
        dest.writeInt(this.currentHandler);
        dest.writeParcelable(this.pendingRequest, flags);
        Utility.writeStringMapToParcel(dest, this.loggingExtras);
    }
}
