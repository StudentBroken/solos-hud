package com.facebook.share.widget;

import android.content.Context;
import android.util.AttributeSet;
import com.facebook.R;
import com.facebook.internal.AnalyticsEvents;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.internal.FacebookDialogBase;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareContent;

/* JADX INFO: loaded from: classes41.dex */
public final class SendButton extends ShareButtonBase {
    public SendButton(Context context) {
        super(context, null, 0, AnalyticsEvents.EVENT_SEND_BUTTON_CREATE, AnalyticsEvents.EVENT_SEND_BUTTON_DID_TAP);
    }

    public SendButton(Context context, AttributeSet attrs) {
        super(context, attrs, 0, AnalyticsEvents.EVENT_SEND_BUTTON_CREATE, AnalyticsEvents.EVENT_SEND_BUTTON_DID_TAP);
    }

    public SendButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, AnalyticsEvents.EVENT_SEND_BUTTON_CREATE, AnalyticsEvents.EVENT_SEND_BUTTON_DID_TAP);
    }

    @Override // com.facebook.FacebookButtonBase
    protected int getDefaultStyleResource() {
        return R.style.com_facebook_button_send;
    }

    @Override // com.facebook.FacebookButtonBase
    protected int getDefaultRequestCode() {
        return CallbackManagerImpl.RequestCodeOffset.Message.toRequestCode();
    }

    @Override // com.facebook.share.widget.ShareButtonBase
    protected FacebookDialogBase<ShareContent, Sharer.Result> getDialog() {
        if (getFragment() != null) {
            MessageDialog dialog = new MessageDialog(getFragment(), getRequestCode());
            return dialog;
        }
        if (getNativeFragment() != null) {
            MessageDialog dialog2 = new MessageDialog(getNativeFragment(), getRequestCode());
            return dialog2;
        }
        MessageDialog dialog3 = new MessageDialog(getActivity(), getRequestCode());
        return dialog3;
    }
}
