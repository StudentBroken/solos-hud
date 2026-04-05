package com.facebook.login.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookButtonBase;
import com.facebook.FacebookCallback;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.R;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.internal.AnalyticsEvents;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.internal.LoginAuthorizationType;
import com.facebook.internal.Utility;
import com.facebook.login.DefaultAudience;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.ToolTipPopup;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes41.dex */
public class LoginButton extends FacebookButtonBase {
    private static final String TAG = LoginButton.class.getName();
    private AccessTokenTracker accessTokenTracker;
    private boolean confirmLogout;
    private String loginLogoutEventName;
    private LoginManager loginManager;
    private String loginText;
    private String logoutText;
    private LoginButtonProperties properties;
    private boolean toolTipChecked;
    private long toolTipDisplayTime;
    private ToolTipMode toolTipMode;
    private ToolTipPopup toolTipPopup;
    private ToolTipPopup.Style toolTipStyle;

    public enum ToolTipMode {
        AUTOMATIC(AnalyticsEvents.PARAMETER_SHARE_DIALOG_SHOW_AUTOMATIC, 0),
        DISPLAY_ALWAYS("display_always", 1),
        NEVER_DISPLAY("never_display", 2);

        public static ToolTipMode DEFAULT = AUTOMATIC;
        private int intValue;
        private String stringValue;

        public static ToolTipMode fromInt(int enumValue) {
            ToolTipMode[] arr$ = values();
            for (ToolTipMode mode : arr$) {
                if (mode.getValue() == enumValue) {
                    return mode;
                }
            }
            return null;
        }

        ToolTipMode(String stringValue, int value) {
            this.stringValue = stringValue;
            this.intValue = value;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.stringValue;
        }

        public int getValue() {
            return this.intValue;
        }
    }

    static class LoginButtonProperties {
        private DefaultAudience defaultAudience = DefaultAudience.FRIENDS;
        private List<String> permissions = Collections.emptyList();
        private LoginAuthorizationType authorizationType = null;
        private LoginBehavior loginBehavior = LoginBehavior.NATIVE_WITH_FALLBACK;

        LoginButtonProperties() {
        }

        public void setDefaultAudience(DefaultAudience defaultAudience) {
            this.defaultAudience = defaultAudience;
        }

        public DefaultAudience getDefaultAudience() {
            return this.defaultAudience;
        }

        public void setReadPermissions(List<String> permissions) {
            if (LoginAuthorizationType.PUBLISH.equals(this.authorizationType)) {
                throw new UnsupportedOperationException("Cannot call setReadPermissions after setPublishPermissions has been called.");
            }
            this.permissions = permissions;
            this.authorizationType = LoginAuthorizationType.READ;
        }

        public void setPublishPermissions(List<String> permissions) {
            if (LoginAuthorizationType.READ.equals(this.authorizationType)) {
                throw new UnsupportedOperationException("Cannot call setPublishPermissions after setReadPermissions has been called.");
            }
            if (Utility.isNullOrEmpty(permissions)) {
                throw new IllegalArgumentException("Permissions for publish actions cannot be null or empty.");
            }
            this.permissions = permissions;
            this.authorizationType = LoginAuthorizationType.PUBLISH;
        }

        List<String> getPermissions() {
            return this.permissions;
        }

        public void clearPermissions() {
            this.permissions = null;
            this.authorizationType = null;
        }

        public void setLoginBehavior(LoginBehavior loginBehavior) {
            this.loginBehavior = loginBehavior;
        }

        public LoginBehavior getLoginBehavior() {
            return this.loginBehavior;
        }
    }

    public LoginButton(Context context) {
        super(context, null, 0, 0, AnalyticsEvents.EVENT_LOGIN_BUTTON_CREATE, AnalyticsEvents.EVENT_LOGIN_BUTTON_DID_TAP);
        this.properties = new LoginButtonProperties();
        this.loginLogoutEventName = AnalyticsEvents.EVENT_LOGIN_VIEW_USAGE;
        this.toolTipStyle = ToolTipPopup.Style.BLUE;
        this.toolTipDisplayTime = ToolTipPopup.DEFAULT_POPUP_DISPLAY_TIME;
    }

    public LoginButton(Context context, AttributeSet attrs) {
        super(context, attrs, 0, 0, AnalyticsEvents.EVENT_LOGIN_BUTTON_CREATE, AnalyticsEvents.EVENT_LOGIN_BUTTON_DID_TAP);
        this.properties = new LoginButtonProperties();
        this.loginLogoutEventName = AnalyticsEvents.EVENT_LOGIN_VIEW_USAGE;
        this.toolTipStyle = ToolTipPopup.Style.BLUE;
        this.toolTipDisplayTime = ToolTipPopup.DEFAULT_POPUP_DISPLAY_TIME;
    }

    public LoginButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle, 0, AnalyticsEvents.EVENT_LOGIN_BUTTON_CREATE, AnalyticsEvents.EVENT_LOGIN_BUTTON_DID_TAP);
        this.properties = new LoginButtonProperties();
        this.loginLogoutEventName = AnalyticsEvents.EVENT_LOGIN_VIEW_USAGE;
        this.toolTipStyle = ToolTipPopup.Style.BLUE;
        this.toolTipDisplayTime = ToolTipPopup.DEFAULT_POPUP_DISPLAY_TIME;
    }

    public void setDefaultAudience(DefaultAudience defaultAudience) {
        this.properties.setDefaultAudience(defaultAudience);
    }

    public DefaultAudience getDefaultAudience() {
        return this.properties.getDefaultAudience();
    }

    public void setReadPermissions(List<String> permissions) {
        this.properties.setReadPermissions(permissions);
    }

    public void setReadPermissions(String... permissions) {
        this.properties.setReadPermissions(Arrays.asList(permissions));
    }

    public void setPublishPermissions(List<String> permissions) {
        this.properties.setPublishPermissions(permissions);
    }

    public void setPublishPermissions(String... permissions) {
        this.properties.setPublishPermissions(Arrays.asList(permissions));
    }

    public void clearPermissions() {
        this.properties.clearPermissions();
    }

    public void setLoginBehavior(LoginBehavior loginBehavior) {
        this.properties.setLoginBehavior(loginBehavior);
    }

    public LoginBehavior getLoginBehavior() {
        return this.properties.getLoginBehavior();
    }

    public void setToolTipStyle(ToolTipPopup.Style toolTipStyle) {
        this.toolTipStyle = toolTipStyle;
    }

    public void setToolTipMode(ToolTipMode toolTipMode) {
        this.toolTipMode = toolTipMode;
    }

    public ToolTipMode getToolTipMode() {
        return this.toolTipMode;
    }

    public void setToolTipDisplayTime(long displayTime) {
        this.toolTipDisplayTime = displayTime;
    }

    public long getToolTipDisplayTime() {
        return this.toolTipDisplayTime;
    }

    public void dismissToolTip() {
        if (this.toolTipPopup != null) {
            this.toolTipPopup.dismiss();
            this.toolTipPopup = null;
        }
    }

    public void registerCallback(CallbackManager callbackManager, FacebookCallback<LoginResult> callback) {
        getLoginManager().registerCallback(callbackManager, callback);
    }

    @Override // com.facebook.FacebookButtonBase, android.widget.TextView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.accessTokenTracker != null && !this.accessTokenTracker.isTracking()) {
            this.accessTokenTracker.startTracking();
            setButtonText();
        }
    }

    @Override // com.facebook.FacebookButtonBase, android.widget.TextView, android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!this.toolTipChecked && !isInEditMode()) {
            this.toolTipChecked = true;
            checkToolTipSettings();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showToolTipPerSettings(Utility.FetchedAppSettings settings) {
        if (settings != null && settings.getNuxEnabled() && getVisibility() == 0) {
            String toolTipString = settings.getNuxContent();
            displayToolTip(toolTipString);
        }
    }

    private void displayToolTip(String toolTipString) {
        this.toolTipPopup = new ToolTipPopup(toolTipString, this);
        this.toolTipPopup.setStyle(this.toolTipStyle);
        this.toolTipPopup.setNuxDisplayTime(this.toolTipDisplayTime);
        this.toolTipPopup.show();
    }

    private void checkToolTipSettings() {
        switch (this.toolTipMode) {
            case AUTOMATIC:
                final String appId = Utility.getMetadataApplicationId(getContext());
                FacebookSdk.getExecutor().execute(new Runnable() { // from class: com.facebook.login.widget.LoginButton.1
                    @Override // java.lang.Runnable
                    public void run() {
                        final Utility.FetchedAppSettings settings = Utility.queryAppSettings(appId, false);
                        LoginButton.this.getActivity().runOnUiThread(new Runnable() { // from class: com.facebook.login.widget.LoginButton.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                LoginButton.this.showToolTipPerSettings(settings);
                            }
                        });
                    }
                });
                break;
            case DISPLAY_ALWAYS:
                String toolTipString = getResources().getString(R.string.com_facebook_tooltip_default);
                displayToolTip(toolTipString);
                break;
        }
    }

    @Override // android.widget.TextView, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        setButtonText();
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.accessTokenTracker != null) {
            this.accessTokenTracker.stopTracking();
        }
        dismissToolTip();
    }

    @Override // android.widget.TextView, android.view.View
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility != 0) {
            dismissToolTip();
        }
    }

    List<String> getPermissions() {
        return this.properties.getPermissions();
    }

    void setProperties(LoginButtonProperties properties) {
        this.properties = properties;
    }

    @Override // com.facebook.FacebookButtonBase
    protected void configureButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super.configureButton(context, attrs, defStyleAttr, defStyleRes);
        setInternalOnClickListener(getNewLoginClickListener());
        parseLoginButtonAttributes(context, attrs, defStyleAttr, defStyleRes);
        if (isInEditMode()) {
            setBackgroundColor(getResources().getColor(R.color.com_facebook_blue));
            this.loginText = "Log in with Facebook";
        } else {
            this.accessTokenTracker = new AccessTokenTracker() { // from class: com.facebook.login.widget.LoginButton.2
                @Override // com.facebook.AccessTokenTracker
                protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                    LoginButton.this.setButtonText();
                }
            };
        }
        setButtonText();
    }

    protected LoginClickListener getNewLoginClickListener() {
        return new LoginClickListener();
    }

    @Override // com.facebook.FacebookButtonBase
    protected int getDefaultStyleResource() {
        return R.style.com_facebook_loginview_default_style;
    }

    private void parseLoginButtonAttributes(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this.toolTipMode = ToolTipMode.DEFAULT;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.com_facebook_login_view, defStyleAttr, defStyleRes);
        try {
            this.confirmLogout = a.getBoolean(R.styleable.com_facebook_login_view_com_facebook_confirm_logout, true);
            this.loginText = a.getString(R.styleable.com_facebook_login_view_com_facebook_login_text);
            this.logoutText = a.getString(R.styleable.com_facebook_login_view_com_facebook_logout_text);
            this.toolTipMode = ToolTipMode.fromInt(a.getInt(R.styleable.com_facebook_login_view_com_facebook_tooltip_mode, ToolTipMode.DEFAULT.getValue()));
        } finally {
            a.recycle();
        }
    }

    @Override // android.widget.TextView, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();
        int height = getCompoundPaddingTop() + ((int) Math.ceil(Math.abs(fontMetrics.top) + Math.abs(fontMetrics.bottom))) + getCompoundPaddingBottom();
        Resources resources = getResources();
        String text = this.loginText;
        if (text == null) {
            text = resources.getString(R.string.com_facebook_loginview_log_in_button_long);
            int logInWidth = measureButtonWidth(text);
            int width = resolveSize(logInWidth, widthMeasureSpec);
            if (width < logInWidth) {
                text = resources.getString(R.string.com_facebook_loginview_log_in_button);
            }
        }
        int logInWidth2 = measureButtonWidth(text);
        String text2 = this.logoutText;
        if (text2 == null) {
            text2 = resources.getString(R.string.com_facebook_loginview_log_out_button);
        }
        int logOutWidth = measureButtonWidth(text2);
        int width2 = resolveSize(Math.max(logInWidth2, logOutWidth), widthMeasureSpec);
        setMeasuredDimension(width2, height);
    }

    private int measureButtonWidth(String text) {
        int textWidth = measureTextWidth(text);
        int width = getCompoundPaddingLeft() + getCompoundDrawablePadding() + textWidth + getCompoundPaddingRight();
        return width;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setButtonText() {
        Resources resources = getResources();
        if (!isInEditMode() && AccessToken.getCurrentAccessToken() != null) {
            setText(this.logoutText != null ? this.logoutText : resources.getString(R.string.com_facebook_loginview_log_out_button));
            return;
        }
        if (this.loginText != null) {
            setText(this.loginText);
            return;
        }
        String text = resources.getString(R.string.com_facebook_loginview_log_in_button_long);
        int width = getWidth();
        if (width != 0) {
            int measuredWidth = measureButtonWidth(text);
            if (measuredWidth > width) {
                text = resources.getString(R.string.com_facebook_loginview_log_in_button);
            }
        }
        setText(text);
    }

    @Override // com.facebook.FacebookButtonBase
    protected int getDefaultRequestCode() {
        return CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode();
    }

    LoginManager getLoginManager() {
        if (this.loginManager == null) {
            this.loginManager = LoginManager.getInstance();
        }
        return this.loginManager;
    }

    void setLoginManager(LoginManager loginManager) {
        this.loginManager = loginManager;
    }

    protected class LoginClickListener implements View.OnClickListener {
        protected LoginClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            LoginButton.this.callExternalOnClickListener(v);
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            if (accessToken != null) {
                performLogout(LoginButton.this.getContext());
            } else {
                performLogin();
            }
            AppEventsLogger logger = AppEventsLogger.newLogger(LoginButton.this.getContext());
            Bundle parameters = new Bundle();
            parameters.putInt("logging_in", accessToken != null ? 0 : 1);
            logger.logSdkEvent(LoginButton.this.loginLogoutEventName, null, parameters);
        }

        protected void performLogin() {
            LoginManager loginManager = getLoginManager();
            if (LoginAuthorizationType.PUBLISH.equals(LoginButton.this.properties.authorizationType)) {
                if (LoginButton.this.getFragment() != null) {
                    loginManager.logInWithPublishPermissions(LoginButton.this.getFragment(), LoginButton.this.properties.permissions);
                    return;
                } else if (LoginButton.this.getNativeFragment() != null) {
                    loginManager.logInWithPublishPermissions(LoginButton.this.getNativeFragment(), LoginButton.this.properties.permissions);
                    return;
                } else {
                    loginManager.logInWithPublishPermissions(LoginButton.this.getActivity(), LoginButton.this.properties.permissions);
                    return;
                }
            }
            if (LoginButton.this.getFragment() != null) {
                loginManager.logInWithReadPermissions(LoginButton.this.getFragment(), LoginButton.this.properties.permissions);
            } else if (LoginButton.this.getNativeFragment() != null) {
                loginManager.logInWithReadPermissions(LoginButton.this.getNativeFragment(), LoginButton.this.properties.permissions);
            } else {
                loginManager.logInWithReadPermissions(LoginButton.this.getActivity(), LoginButton.this.properties.permissions);
            }
        }

        protected void performLogout(Context context) {
            String message;
            final LoginManager loginManager = getLoginManager();
            if (LoginButton.this.confirmLogout) {
                String logout = LoginButton.this.getResources().getString(R.string.com_facebook_loginview_log_out_action);
                String cancel = LoginButton.this.getResources().getString(R.string.com_facebook_loginview_cancel_action);
                Profile profile = Profile.getCurrentProfile();
                if (profile != null && profile.getName() != null) {
                    message = String.format(LoginButton.this.getResources().getString(R.string.com_facebook_loginview_logged_in_as), profile.getName());
                } else {
                    message = LoginButton.this.getResources().getString(R.string.com_facebook_loginview_logged_in_using_facebook);
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(message).setCancelable(true).setPositiveButton(logout, new DialogInterface.OnClickListener() { // from class: com.facebook.login.widget.LoginButton.LoginClickListener.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        loginManager.logOut();
                    }
                }).setNegativeButton(cancel, (DialogInterface.OnClickListener) null);
                builder.create().show();
                return;
            }
            loginManager.logOut();
        }

        protected LoginManager getLoginManager() {
            LoginManager manager = LoginManager.getInstance();
            manager.setDefaultAudience(LoginButton.this.getDefaultAudience());
            manager.setLoginBehavior(LoginButton.this.getLoginBehavior());
            return manager;
        }
    }
}
