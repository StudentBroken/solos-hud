package com.facebook.share.internal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import com.facebook.FacebookCallback;
import com.facebook.internal.AppCall;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.internal.DialogFeature;
import com.facebook.internal.DialogPresenter;
import com.facebook.internal.FacebookDialogBase;
import com.facebook.internal.FragmentWrapper;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes41.dex */
public class LikeDialog extends FacebookDialogBase<LikeContent, Result> {
    private static final int DEFAULT_REQUEST_CODE = CallbackManagerImpl.RequestCodeOffset.Like.toRequestCode();
    private static final String TAG = "LikeDialog";

    public static final class Result {
        private final Bundle bundle;

        public Result(Bundle bundle) {
            this.bundle = bundle;
        }

        public Bundle getData() {
            return this.bundle;
        }
    }

    public static boolean canShowNativeDialog() {
        return DialogPresenter.canPresentNativeDialogWithFeature(getFeature());
    }

    public static boolean canShowWebFallback() {
        return DialogPresenter.canPresentWebFallbackDialogWithFeature(getFeature());
    }

    public LikeDialog(Activity activity) {
        super(activity, DEFAULT_REQUEST_CODE);
    }

    public LikeDialog(Fragment fragment) {
        this(new FragmentWrapper(fragment));
    }

    public LikeDialog(android.app.Fragment fragment) {
        this(new FragmentWrapper(fragment));
    }

    public LikeDialog(FragmentWrapper fragmentWrapper) {
        super(fragmentWrapper, DEFAULT_REQUEST_CODE);
    }

    @Override // com.facebook.internal.FacebookDialogBase
    protected AppCall createBaseAppCall() {
        return new AppCall(getRequestCode());
    }

    @Override // com.facebook.internal.FacebookDialogBase
    protected List<FacebookDialogBase<LikeContent, Result>.ModeHandler> getOrderedModeHandlers() {
        ArrayList<FacebookDialogBase<LikeContent, Result>.ModeHandler> handlers = new ArrayList<>();
        handlers.add(new NativeHandler());
        handlers.add(new WebFallbackHandler());
        return handlers;
    }

    @Override // com.facebook.internal.FacebookDialogBase
    protected void registerCallbackImpl(CallbackManagerImpl callbackManager, final FacebookCallback<Result> callback) {
        final ResultProcessor resultProcessor = callback == null ? null : new ResultProcessor(callback) { // from class: com.facebook.share.internal.LikeDialog.1
            @Override // com.facebook.share.internal.ResultProcessor
            public void onSuccess(AppCall appCall, Bundle results) {
                callback.onSuccess(new Result(results));
            }
        };
        CallbackManagerImpl.Callback callbackManagerCallback = new CallbackManagerImpl.Callback() { // from class: com.facebook.share.internal.LikeDialog.2
            @Override // com.facebook.internal.CallbackManagerImpl.Callback
            public boolean onActivityResult(int resultCode, Intent data) {
                return ShareInternalUtility.handleActivityResult(LikeDialog.this.getRequestCode(), resultCode, data, resultProcessor);
            }
        };
        callbackManager.registerCallback(getRequestCode(), callbackManagerCallback);
    }

    private class NativeHandler extends FacebookDialogBase<LikeContent, Result>.ModeHandler {
        private NativeHandler() {
            super();
        }

        @Override // com.facebook.internal.FacebookDialogBase.ModeHandler
        public boolean canShow(LikeContent content, boolean isBestEffort) {
            return content != null && LikeDialog.canShowNativeDialog();
        }

        @Override // com.facebook.internal.FacebookDialogBase.ModeHandler
        public AppCall createAppCall(final LikeContent content) {
            AppCall appCall = LikeDialog.this.createBaseAppCall();
            DialogPresenter.setupAppCallForNativeDialog(appCall, new DialogPresenter.ParameterProvider() { // from class: com.facebook.share.internal.LikeDialog.NativeHandler.1
                @Override // com.facebook.internal.DialogPresenter.ParameterProvider
                public Bundle getParameters() {
                    return LikeDialog.createParameters(content);
                }

                @Override // com.facebook.internal.DialogPresenter.ParameterProvider
                public Bundle getLegacyParameters() {
                    Log.e(LikeDialog.TAG, "Attempting to present the Like Dialog with an outdated Facebook app on the device");
                    return new Bundle();
                }
            }, LikeDialog.getFeature());
            return appCall;
        }
    }

    private class WebFallbackHandler extends FacebookDialogBase<LikeContent, Result>.ModeHandler {
        private WebFallbackHandler() {
            super();
        }

        @Override // com.facebook.internal.FacebookDialogBase.ModeHandler
        public boolean canShow(LikeContent content, boolean isBestEffort) {
            return content != null && LikeDialog.canShowWebFallback();
        }

        @Override // com.facebook.internal.FacebookDialogBase.ModeHandler
        public AppCall createAppCall(LikeContent content) {
            AppCall appCall = LikeDialog.this.createBaseAppCall();
            DialogPresenter.setupAppCallForWebFallbackDialog(appCall, LikeDialog.createParameters(content), LikeDialog.getFeature());
            return appCall;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static DialogFeature getFeature() {
        return LikeDialogFeature.LIKE_DIALOG;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Bundle createParameters(LikeContent likeContent) {
        Bundle params = new Bundle();
        params.putString("object_id", likeContent.getObjectId());
        params.putString("object_type", likeContent.getObjectType());
        return params;
    }
}
