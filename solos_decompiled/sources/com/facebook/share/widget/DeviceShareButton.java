package com.facebook.share.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.facebook.CallbackManager;
import com.facebook.FacebookButtonBase;
import com.facebook.FacebookCallback;
import com.facebook.FacebookSdk;
import com.facebook.R;
import com.facebook.internal.AnalyticsEvents;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.share.DeviceShareDialog;
import com.facebook.share.model.ShareContent;

/* JADX INFO: loaded from: classes41.dex */
public final class DeviceShareButton extends FacebookButtonBase {
    private DeviceShareDialog dialog;
    private boolean enabledExplicitlySet;
    private int requestCode;
    private ShareContent shareContent;

    public DeviceShareButton(Context context) {
        this(context, null, 0);
    }

    public DeviceShareButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private DeviceShareButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, 0, AnalyticsEvents.EVENT_DEVICE_SHARE_BUTTON_CREATE, AnalyticsEvents.EVENT_DEVICE_SHARE_BUTTON_DID_TAP);
        this.requestCode = 0;
        this.enabledExplicitlySet = false;
        this.dialog = null;
        this.requestCode = isInEditMode() ? 0 : getDefaultRequestCode();
        internalSetEnabled(false);
    }

    public ShareContent getShareContent() {
        return this.shareContent;
    }

    public void setShareContent(ShareContent shareContent) {
        this.shareContent = shareContent;
        if (!this.enabledExplicitlySet) {
            internalSetEnabled(canShare());
        }
    }

    @Override // android.widget.TextView, android.view.View
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.enabledExplicitlySet = true;
    }

    @Override // com.facebook.FacebookButtonBase
    public int getRequestCode() {
        return this.requestCode;
    }

    public void registerCallback(CallbackManager callbackManager, FacebookCallback<DeviceShareDialog.Result> callback) {
        getDialog().registerCallback(callbackManager, callback);
    }

    public void registerCallback(CallbackManager callbackManager, FacebookCallback<DeviceShareDialog.Result> callback, int requestCode) {
        setRequestCode(requestCode);
        getDialog().registerCallback(callbackManager, callback, requestCode);
    }

    @Override // com.facebook.FacebookButtonBase
    protected void configureButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super.configureButton(context, attrs, defStyleAttr, defStyleRes);
        setInternalOnClickListener(getShareOnClickListener());
    }

    @Override // com.facebook.FacebookButtonBase
    protected int getDefaultStyleResource() {
        return R.style.com_facebook_button_share;
    }

    @Override // com.facebook.FacebookButtonBase
    protected int getDefaultRequestCode() {
        return CallbackManagerImpl.RequestCodeOffset.Share.toRequestCode();
    }

    protected View.OnClickListener getShareOnClickListener() {
        return new View.OnClickListener() { // from class: com.facebook.share.widget.DeviceShareButton.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                DeviceShareButton.this.callExternalOnClickListener(v);
                DeviceShareButton.this.getDialog().show(DeviceShareButton.this.getShareContent());
            }
        };
    }

    private void internalSetEnabled(boolean enabled) {
        setEnabled(enabled);
        this.enabledExplicitlySet = false;
    }

    private void setRequestCode(int requestCode) {
        if (FacebookSdk.isFacebookRequestCode(requestCode)) {
            throw new IllegalArgumentException("Request code " + requestCode + " cannot be within the range reserved by the Facebook SDK.");
        }
        this.requestCode = requestCode;
    }

    private boolean canShare() {
        return new DeviceShareDialog(getActivity()).canShow(getShareContent());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public DeviceShareDialog getDialog() {
        if (this.dialog != null) {
            return this.dialog;
        }
        if (getFragment() != null) {
            this.dialog = new DeviceShareDialog(getFragment());
        } else if (getNativeFragment() != null) {
            this.dialog = new DeviceShareDialog(getNativeFragment());
        } else {
            this.dialog = new DeviceShareDialog(getActivity());
        }
        return this.dialog;
    }
}
