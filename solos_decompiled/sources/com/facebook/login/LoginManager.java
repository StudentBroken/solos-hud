package com.facebook.login;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;
import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.internal.FragmentWrapper;
import com.facebook.internal.Validate;
import com.facebook.login.LoginClient;
import com.facebook.share.internal.ShareConstants;
import com.kopin.solos.storage.Ride;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/* JADX INFO: loaded from: classes41.dex */
public class LoginManager {
    private static final String MANAGE_PERMISSION_PREFIX = "manage";
    private static final Set<String> OTHER_PUBLISH_PERMISSIONS = getOtherPublishPermissions();
    private static final String PUBLISH_PERMISSION_PREFIX = "publish";
    private static volatile LoginManager instance;
    private LoginBehavior loginBehavior = LoginBehavior.NATIVE_WITH_FALLBACK;
    private DefaultAudience defaultAudience = DefaultAudience.FRIENDS;

    LoginManager() {
        Validate.sdkInitialized();
    }

    public static LoginManager getInstance() {
        if (instance == null) {
            synchronized (LoginManager.class) {
                if (instance == null) {
                    instance = new LoginManager();
                }
            }
        }
        return instance;
    }

    public void resolveError(Activity activity, GraphResponse response) {
        startLogin(new ActivityStartActivityDelegate(activity), createLoginRequestFromResponse(response));
    }

    public void resolveError(Fragment fragment, GraphResponse response) {
        resolveError(new FragmentWrapper(fragment), response);
    }

    public void resolveError(android.app.Fragment fragment, GraphResponse response) {
        resolveError(new FragmentWrapper(fragment), response);
    }

    private void resolveError(FragmentWrapper fragment, GraphResponse response) {
        startLogin(new FragmentStartActivityDelegate(fragment), createLoginRequestFromResponse(response));
    }

    private LoginClient.Request createLoginRequestFromResponse(GraphResponse response) {
        Validate.notNull(response, "response");
        AccessToken failedToken = response.getRequest().getAccessToken();
        return createLoginRequest(failedToken != null ? failedToken.getPermissions() : null);
    }

    public void registerCallback(CallbackManager callbackManager, final FacebookCallback<LoginResult> callback) {
        if (!(callbackManager instanceof CallbackManagerImpl)) {
            throw new FacebookException("Unexpected CallbackManager, please use the provided Factory.");
        }
        ((CallbackManagerImpl) callbackManager).registerCallback(CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode(), new CallbackManagerImpl.Callback() { // from class: com.facebook.login.LoginManager.1
            @Override // com.facebook.internal.CallbackManagerImpl.Callback
            public boolean onActivityResult(int resultCode, Intent data) {
                return LoginManager.this.onActivityResult(resultCode, data, callback);
            }
        });
    }

    boolean onActivityResult(int resultCode, Intent data) {
        return onActivityResult(resultCode, data, null);
    }

    boolean onActivityResult(int resultCode, Intent data, FacebookCallback<LoginResult> callback) {
        FacebookException exception = null;
        AccessToken newToken = null;
        LoginClient.Result.Code code = LoginClient.Result.Code.ERROR;
        Map<String, String> loggingExtras = null;
        LoginClient.Request originalRequest = null;
        boolean isCanceled = false;
        if (data != null) {
            LoginClient.Result result = (LoginClient.Result) data.getParcelableExtra("com.facebook.LoginFragment:Result");
            if (result != null) {
                originalRequest = result.request;
                code = result.code;
                if (resultCode == -1) {
                    if (result.code == LoginClient.Result.Code.SUCCESS) {
                        newToken = result.token;
                    } else {
                        exception = new FacebookAuthorizationException(result.errorMessage);
                    }
                } else if (resultCode == 0) {
                    isCanceled = true;
                }
                loggingExtras = result.loggingExtras;
            }
        } else if (resultCode == 0) {
            isCanceled = true;
            code = LoginClient.Result.Code.CANCEL;
        }
        if (exception == null && newToken == null && !isCanceled) {
            exception = new FacebookException("Unexpected call to LoginManager.onActivityResult");
        }
        logCompleteLogin(null, code, loggingExtras, exception, true, originalRequest);
        finishLogin(newToken, originalRequest, exception, isCanceled, callback);
        return true;
    }

    public LoginBehavior getLoginBehavior() {
        return this.loginBehavior;
    }

    public LoginManager setLoginBehavior(LoginBehavior loginBehavior) {
        this.loginBehavior = loginBehavior;
        return this;
    }

    public DefaultAudience getDefaultAudience() {
        return this.defaultAudience;
    }

    public LoginManager setDefaultAudience(DefaultAudience defaultAudience) {
        this.defaultAudience = defaultAudience;
        return this;
    }

    public void logOut() {
        AccessToken.setCurrentAccessToken(null);
        Profile.setCurrentProfile(null);
    }

    public void logInWithReadPermissions(Fragment fragment, Collection<String> permissions) {
        logInWithReadPermissions(new FragmentWrapper(fragment), permissions);
    }

    public void logInWithReadPermissions(android.app.Fragment fragment, Collection<String> permissions) {
        logInWithReadPermissions(new FragmentWrapper(fragment), permissions);
    }

    private void logInWithReadPermissions(FragmentWrapper fragment, Collection<String> permissions) {
        validateReadPermissions(permissions);
        LoginClient.Request loginRequest = createLoginRequest(permissions);
        startLogin(new FragmentStartActivityDelegate(fragment), loginRequest);
    }

    public void logInWithReadPermissions(Activity activity, Collection<String> permissions) {
        validateReadPermissions(permissions);
        LoginClient.Request loginRequest = createLoginRequest(permissions);
        startLogin(new ActivityStartActivityDelegate(activity), loginRequest);
    }

    public void logInWithPublishPermissions(Fragment fragment, Collection<String> permissions) {
        logInWithPublishPermissions(new FragmentWrapper(fragment), permissions);
    }

    public void logInWithPublishPermissions(android.app.Fragment fragment, Collection<String> permissions) {
        logInWithPublishPermissions(new FragmentWrapper(fragment), permissions);
    }

    private void logInWithPublishPermissions(FragmentWrapper fragment, Collection<String> permissions) {
        validatePublishPermissions(permissions);
        LoginClient.Request loginRequest = createLoginRequest(permissions);
        startLogin(new FragmentStartActivityDelegate(fragment), loginRequest);
    }

    public void logInWithPublishPermissions(Activity activity, Collection<String> permissions) {
        validatePublishPermissions(permissions);
        LoginClient.Request loginRequest = createLoginRequest(permissions);
        startLogin(new ActivityStartActivityDelegate(activity), loginRequest);
    }

    private void validateReadPermissions(Collection<String> permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (isPublishPermission(permission)) {
                    throw new FacebookException(String.format("Cannot pass a publish or manage permission (%s) to a request for read authorization", permission));
                }
            }
        }
    }

    private void validatePublishPermissions(Collection<String> permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (!isPublishPermission(permission)) {
                    throw new FacebookException(String.format("Cannot pass a read permission (%s) to a request for publish authorization", permission));
                }
            }
        }
    }

    static boolean isPublishPermission(String permission) {
        return permission != null && (permission.startsWith(PUBLISH_PERMISSION_PREFIX) || permission.startsWith(MANAGE_PERMISSION_PREFIX) || OTHER_PUBLISH_PERMISSIONS.contains(permission));
    }

    private static Set<String> getOtherPublishPermissions() {
        HashSet<String> set = new HashSet<String>() { // from class: com.facebook.login.LoginManager.2
            {
                add("ads_management");
                add("create_event");
                add("rsvp_event");
            }
        };
        return Collections.unmodifiableSet(set);
    }

    protected LoginClient.Request createLoginRequest(Collection<String> permissions) {
        LoginClient.Request request = new LoginClient.Request(this.loginBehavior, Collections.unmodifiableSet(permissions != null ? new HashSet(permissions) : new HashSet()), this.defaultAudience, FacebookSdk.getApplicationId(), UUID.randomUUID().toString());
        request.setRerequest(AccessToken.getCurrentAccessToken() != null);
        return request;
    }

    private void startLogin(StartActivityDelegate startActivityDelegate, LoginClient.Request request) throws FacebookException {
        logStartLogin(startActivityDelegate.getActivityContext(), request);
        CallbackManagerImpl.registerStaticCallback(CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode(), new CallbackManagerImpl.Callback() { // from class: com.facebook.login.LoginManager.3
            @Override // com.facebook.internal.CallbackManagerImpl.Callback
            public boolean onActivityResult(int resultCode, Intent data) {
                return LoginManager.this.onActivityResult(resultCode, data);
            }
        });
        boolean started = tryFacebookActivity(startActivityDelegate, request);
        if (!started) {
            FacebookException exception = new FacebookException("Log in attempt failed: FacebookActivity could not be started. Please make sure you added FacebookActivity to the AndroidManifest.");
            logCompleteLogin(startActivityDelegate.getActivityContext(), LoginClient.Result.Code.ERROR, null, exception, false, request);
            throw exception;
        }
    }

    private void logStartLogin(Context context, LoginClient.Request loginRequest) {
        LoginLogger loginLogger = LoginLoggerHolder.getLogger(context);
        if (loginLogger != null && loginRequest != null) {
            loginLogger.logStartLogin(loginRequest);
        }
    }

    private void logCompleteLogin(Context context, LoginClient.Result.Code result, Map<String, String> resultExtras, Exception exception, boolean wasLoginActivityTried, LoginClient.Request request) {
        LoginLogger loginLogger = LoginLoggerHolder.getLogger(context);
        if (loginLogger != null) {
            if (request == null) {
                loginLogger.logUnexpectedError("fb_mobile_login_complete", "Unexpected call to logCompleteLogin with null pendingAuthorizationRequest.");
                return;
            }
            HashMap<String, String> pendingLoggingExtras = new HashMap<>();
            pendingLoggingExtras.put("try_login_activity", wasLoginActivityTried ? AppEventsConstants.EVENT_PARAM_VALUE_YES : "0");
            loginLogger.logCompleteLogin(request.getAuthId(), pendingLoggingExtras, result, resultExtras, exception);
        }
    }

    private boolean tryFacebookActivity(StartActivityDelegate startActivityDelegate, LoginClient.Request request) {
        Intent intent = getFacebookActivityIntent(request);
        if (!resolveIntent(intent)) {
            return false;
        }
        try {
            startActivityDelegate.startActivityForResult(intent, LoginClient.getLoginRequestCode());
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

    private boolean resolveIntent(Intent intent) {
        ResolveInfo resolveInfo = FacebookSdk.getApplicationContext().getPackageManager().resolveActivity(intent, 0);
        return resolveInfo != null;
    }

    private Intent getFacebookActivityIntent(LoginClient.Request request) {
        Intent intent = new Intent();
        intent.setClass(FacebookSdk.getApplicationContext(), FacebookActivity.class);
        intent.setAction(request.getLoginBehavior().toString());
        Bundle extras = new Bundle();
        extras.putParcelable(ShareConstants.WEB_DIALOG_RESULT_PARAM_REQUEST_ID, request);
        intent.putExtra("com.facebook.LoginFragment:Request", extras);
        return intent;
    }

    static LoginResult computeLoginResult(LoginClient.Request request, AccessToken newToken) {
        Collection<?> requestedPermissions = request.getPermissions();
        Set<String> grantedPermissions = new HashSet<>(newToken.getPermissions());
        if (request.isRerequest()) {
            grantedPermissions.retainAll(requestedPermissions);
        }
        Set<String> deniedPermissions = new HashSet<>((Collection<? extends String>) requestedPermissions);
        deniedPermissions.removeAll(grantedPermissions);
        return new LoginResult(newToken, grantedPermissions, deniedPermissions);
    }

    private void finishLogin(AccessToken newToken, LoginClient.Request origRequest, FacebookException exception, boolean isCanceled, FacebookCallback<LoginResult> callback) {
        if (newToken != null) {
            AccessToken.setCurrentAccessToken(newToken);
            Profile.fetchProfileForCurrentAccessToken();
        }
        if (callback != null) {
            LoginResult loginResult = newToken != null ? computeLoginResult(origRequest, newToken) : null;
            if (isCanceled || (loginResult != null && loginResult.getRecentlyGrantedPermissions().size() == 0)) {
                callback.onCancel();
            } else if (exception != null) {
                callback.onError(exception);
            } else if (newToken != null) {
                callback.onSuccess(loginResult);
            }
        }
    }

    public static void setSuccessResult(Intent intent, Bundle values) {
        Bundle bundle = intent.getBundleExtra("com.facebook.LoginFragment:Request");
        LoginClient.Request request = (LoginClient.Request) bundle.getParcelable(ShareConstants.WEB_DIALOG_RESULT_PARAM_REQUEST_ID);
        AccessToken token = LoginMethodHandler.createAccessTokenFromWebBundle(request.getPermissions(), values, AccessTokenSource.CHROME_CUSTOM_TAB, request.getApplicationId());
        LoginClient.Result result = LoginClient.Result.createTokenResult(request, token);
        intent.putExtra("com.facebook.LoginFragment:Result", result);
    }

    private static class ActivityStartActivityDelegate implements StartActivityDelegate {
        private final Activity activity;

        ActivityStartActivityDelegate(Activity activity) {
            Validate.notNull(activity, Ride.ACTIVITY);
            this.activity = activity;
        }

        @Override // com.facebook.login.StartActivityDelegate
        public void startActivityForResult(Intent intent, int requestCode) {
            this.activity.startActivityForResult(intent, requestCode);
        }

        @Override // com.facebook.login.StartActivityDelegate
        public Activity getActivityContext() {
            return this.activity;
        }
    }

    private static class FragmentStartActivityDelegate implements StartActivityDelegate {
        private final FragmentWrapper fragment;

        FragmentStartActivityDelegate(FragmentWrapper fragment) {
            Validate.notNull(fragment, "fragment");
            this.fragment = fragment;
        }

        @Override // com.facebook.login.StartActivityDelegate
        public void startActivityForResult(Intent intent, int requestCode) {
            this.fragment.startActivityForResult(intent, requestCode);
        }

        @Override // com.facebook.login.StartActivityDelegate
        public Activity getActivityContext() {
            return this.fragment.getActivity();
        }
    }

    private static class LoginLoggerHolder {
        private static volatile LoginLogger logger;

        private LoginLoggerHolder() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Removed duplicated region for block: B:10:0x000f A[Catch: all -> 0x0021, TryCatch #0 {, blocks: (B:10:0x000f, B:12:0x0013, B:13:0x001e, B:9:0x000a), top: B:18:0x000a }] */
        /* JADX WARN: Removed duplicated region for block: B:6:0x0007  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public static synchronized com.facebook.login.LoginLogger getLogger(android.content.Context r3) {
            /*
                java.lang.Class<com.facebook.login.LoginManager$LoginLoggerHolder> r1 = com.facebook.login.LoginManager.LoginLoggerHolder.class
                monitor-enter(r1)
                if (r3 == 0) goto La
            L5:
                if (r3 != 0) goto Lf
                r0 = 0
            L8:
                monitor-exit(r1)
                return r0
            La:
                android.content.Context r3 = com.facebook.FacebookSdk.getApplicationContext()     // Catch: java.lang.Throwable -> L21
                goto L5
            Lf:
                com.facebook.login.LoginLogger r0 = com.facebook.login.LoginManager.LoginLoggerHolder.logger     // Catch: java.lang.Throwable -> L21
                if (r0 != 0) goto L1e
                com.facebook.login.LoginLogger r0 = new com.facebook.login.LoginLogger     // Catch: java.lang.Throwable -> L21
                java.lang.String r2 = com.facebook.FacebookSdk.getApplicationId()     // Catch: java.lang.Throwable -> L21
                r0.<init>(r3, r2)     // Catch: java.lang.Throwable -> L21
                com.facebook.login.LoginManager.LoginLoggerHolder.logger = r0     // Catch: java.lang.Throwable -> L21
            L1e:
                com.facebook.login.LoginLogger r0 = com.facebook.login.LoginManager.LoginLoggerHolder.logger     // Catch: java.lang.Throwable -> L21
                goto L8
            L21:
                r0 = move-exception
                monitor-exit(r1)
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.facebook.login.LoginManager.LoginLoggerHolder.getLogger(android.content.Context):com.facebook.login.LoginLogger");
        }
    }
}
