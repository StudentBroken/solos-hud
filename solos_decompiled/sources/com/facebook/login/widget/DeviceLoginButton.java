package com.facebook.login.widget;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import com.facebook.login.DeviceLoginManager;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;

/* JADX INFO: loaded from: classes41.dex */
public class DeviceLoginButton extends LoginButton {
    private Uri deviceRedirectUri;

    public DeviceLoginButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public DeviceLoginButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DeviceLoginButton(Context context) {
        super(context);
    }

    public void setDeviceRedirectUri(Uri uri) {
        this.deviceRedirectUri = uri;
    }

    public Uri getDeviceRedirectUri() {
        return this.deviceRedirectUri;
    }

    @Override // com.facebook.login.widget.LoginButton
    protected LoginButton.LoginClickListener getNewLoginClickListener() {
        return new DeviceLoginClickListener();
    }

    private class DeviceLoginClickListener extends LoginButton.LoginClickListener {
        private DeviceLoginClickListener() {
            super();
        }

        @Override // com.facebook.login.widget.LoginButton.LoginClickListener
        protected LoginManager getLoginManager() {
            DeviceLoginManager manager = DeviceLoginManager.getInstance();
            manager.setDefaultAudience(DeviceLoginButton.this.getDefaultAudience());
            manager.setLoginBehavior(LoginBehavior.DEVICE_AUTH);
            manager.setDeviceRedirectUri(DeviceLoginButton.this.getDeviceRedirectUri());
            return manager;
        }
    }
}
