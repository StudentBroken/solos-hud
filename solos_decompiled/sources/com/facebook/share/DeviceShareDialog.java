package com.facebook.share;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import com.facebook.FacebookActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.FacebookSdk;
import com.facebook.internal.AppCall;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.internal.FacebookDialogBase;
import com.facebook.internal.FragmentWrapper;
import com.facebook.share.internal.DeviceShareDialogFragment;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareOpenGraphContent;
import java.util.List;

/* JADX INFO: loaded from: classes41.dex */
public class DeviceShareDialog extends FacebookDialogBase<ShareContent, Result> {
    private static final int DEFAULT_REQUEST_CODE = CallbackManagerImpl.RequestCodeOffset.DeviceShare.toRequestCode();

    public static class Result {
    }

    public DeviceShareDialog(Activity activity) {
        super(activity, DEFAULT_REQUEST_CODE);
    }

    public DeviceShareDialog(Fragment fragment) {
        super(new FragmentWrapper(fragment), DEFAULT_REQUEST_CODE);
    }

    public DeviceShareDialog(android.support.v4.app.Fragment fragment) {
        super(new FragmentWrapper(fragment), DEFAULT_REQUEST_CODE);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.internal.FacebookDialogBase
    public boolean canShowImpl(ShareContent content, Object mode) {
        return (content instanceof ShareLinkContent) || (content instanceof ShareOpenGraphContent);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.internal.FacebookDialogBase
    public void showImpl(ShareContent content, Object mode) {
        if (content == null) {
            throw new FacebookException("Must provide non-null content to share");
        }
        if (!(content instanceof ShareLinkContent) && !(content instanceof ShareOpenGraphContent)) {
            throw new FacebookException(getClass().getSimpleName() + " only supports ShareLinkContent or ShareOpenGraphContent");
        }
        Intent intent = new Intent();
        intent.setClass(FacebookSdk.getApplicationContext(), FacebookActivity.class);
        intent.setAction(DeviceShareDialogFragment.TAG);
        intent.putExtra("content", content);
        startActivityForResult(intent, getRequestCode());
    }

    @Override // com.facebook.internal.FacebookDialogBase
    protected List<FacebookDialogBase<ShareContent, Result>.ModeHandler> getOrderedModeHandlers() {
        return null;
    }

    @Override // com.facebook.internal.FacebookDialogBase
    protected AppCall createBaseAppCall() {
        return null;
    }

    @Override // com.facebook.internal.FacebookDialogBase
    protected void registerCallbackImpl(CallbackManagerImpl callbackManager, final FacebookCallback<Result> callback) {
        callbackManager.registerCallback(getRequestCode(), new CallbackManagerImpl.Callback() { // from class: com.facebook.share.DeviceShareDialog.1
            @Override // com.facebook.internal.CallbackManagerImpl.Callback
            public boolean onActivityResult(int resultCode, Intent data) {
                data.getExtras();
                if (data.hasExtra("error")) {
                    FacebookRequestError error = (FacebookRequestError) data.getParcelableExtra("error");
                    callback.onError(error.getException());
                } else {
                    callback.onSuccess(new Result());
                }
                return true;
            }
        });
    }
}
