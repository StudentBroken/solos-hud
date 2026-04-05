package com.facebook.share.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.facebook.CallbackManager;
import com.facebook.FacebookButtonBase;
import com.facebook.FacebookCallback;
import com.facebook.FacebookSdk;
import com.facebook.internal.FacebookDialogBase;
import com.facebook.share.Sharer;
import com.facebook.share.internal.ShareInternalUtility;
import com.facebook.share.model.ShareContent;

/* JADX INFO: loaded from: classes41.dex */
public abstract class ShareButtonBase extends FacebookButtonBase {
    private boolean enabledExplicitlySet;
    private int requestCode;
    private ShareContent shareContent;

    protected abstract FacebookDialogBase<ShareContent, Sharer.Result> getDialog();

    protected ShareButtonBase(Context context, AttributeSet attrs, int defStyleAttr, String analyticsButtonCreatedEventName, String analyticsButtonTappedEventName) {
        super(context, attrs, defStyleAttr, 0, analyticsButtonCreatedEventName, analyticsButtonTappedEventName);
        this.requestCode = 0;
        this.enabledExplicitlySet = false;
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

    protected void setRequestCode(int requestCode) {
        if (FacebookSdk.isFacebookRequestCode(requestCode)) {
            throw new IllegalArgumentException("Request code " + requestCode + " cannot be within the range reserved by the Facebook SDK.");
        }
        this.requestCode = requestCode;
    }

    public void registerCallback(CallbackManager callbackManager, FacebookCallback<Sharer.Result> callback) {
        ShareInternalUtility.registerSharerCallback(getRequestCode(), callbackManager, callback);
    }

    public void registerCallback(CallbackManager callbackManager, FacebookCallback<Sharer.Result> callback, int requestCode) {
        setRequestCode(requestCode);
        registerCallback(callbackManager, callback);
    }

    @Override // com.facebook.FacebookButtonBase
    protected void configureButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super.configureButton(context, attrs, defStyleAttr, defStyleRes);
        setInternalOnClickListener(getShareOnClickListener());
    }

    protected boolean canShare() {
        return getDialog().canShow(getShareContent());
    }

    protected View.OnClickListener getShareOnClickListener() {
        return new View.OnClickListener() { // from class: com.facebook.share.widget.ShareButtonBase.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ShareButtonBase.this.callExternalOnClickListener(v);
                ShareButtonBase.this.getDialog().show(ShareButtonBase.this.getShareContent());
            }
        };
    }

    private void internalSetEnabled(boolean enabled) {
        setEnabled(enabled);
        this.enabledExplicitlySet = false;
    }
}
