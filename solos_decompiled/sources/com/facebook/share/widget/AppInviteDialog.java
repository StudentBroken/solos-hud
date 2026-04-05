package com.facebook.share.widget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import com.facebook.FacebookCallback;
import com.facebook.internal.AppCall;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.internal.DialogFeature;
import com.facebook.internal.DialogPresenter;
import com.facebook.internal.FacebookDialogBase;
import com.facebook.internal.FragmentWrapper;
import com.facebook.share.internal.AppInviteDialogFeature;
import com.facebook.share.internal.ResultProcessor;
import com.facebook.share.internal.ShareConstants;
import com.facebook.share.internal.ShareInternalUtility;
import com.facebook.share.model.AppInviteContent;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes41.dex */
public class AppInviteDialog extends FacebookDialogBase<AppInviteContent, Result> {
    private static final int DEFAULT_REQUEST_CODE = CallbackManagerImpl.RequestCodeOffset.AppInvite.toRequestCode();
    private static final String TAG = "AppInviteDialog";

    public static final class Result {
        private final Bundle bundle;

        public Result(Bundle bundle) {
            this.bundle = bundle;
        }

        public Bundle getData() {
            return this.bundle;
        }
    }

    public static boolean canShow() {
        return canShowNativeDialog() || canShowWebFallback();
    }

    public static void show(Activity activity, AppInviteContent appInviteContent) {
        new AppInviteDialog(activity).show(appInviteContent);
    }

    public static void show(Fragment fragment, AppInviteContent appInviteContent) {
        show(new FragmentWrapper(fragment), appInviteContent);
    }

    public static void show(android.app.Fragment fragment, AppInviteContent appInviteContent) {
        show(new FragmentWrapper(fragment), appInviteContent);
    }

    private static void show(FragmentWrapper fragmentWrapper, AppInviteContent appInviteContent) {
        new AppInviteDialog(fragmentWrapper).show(appInviteContent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean canShowNativeDialog() {
        return DialogPresenter.canPresentNativeDialogWithFeature(getFeature());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean canShowWebFallback() {
        return DialogPresenter.canPresentWebFallbackDialogWithFeature(getFeature());
    }

    public AppInviteDialog(Activity activity) {
        super(activity, DEFAULT_REQUEST_CODE);
    }

    public AppInviteDialog(Fragment fragment) {
        this(new FragmentWrapper(fragment));
    }

    public AppInviteDialog(android.app.Fragment fragment) {
        this(new FragmentWrapper(fragment));
    }

    private AppInviteDialog(FragmentWrapper fragment) {
        super(fragment, DEFAULT_REQUEST_CODE);
    }

    @Override // com.facebook.internal.FacebookDialogBase
    protected void registerCallbackImpl(CallbackManagerImpl callbackManager, final FacebookCallback<Result> callback) {
        final ResultProcessor resultProcessor = callback == null ? null : new ResultProcessor(callback) { // from class: com.facebook.share.widget.AppInviteDialog.1
            @Override // com.facebook.share.internal.ResultProcessor
            public void onSuccess(AppCall appCall, Bundle results) {
                String gesture = ShareInternalUtility.getNativeDialogCompletionGesture(results);
                if ("cancel".equalsIgnoreCase(gesture)) {
                    callback.onCancel();
                } else {
                    callback.onSuccess(new Result(results));
                }
            }
        };
        CallbackManagerImpl.Callback callbackManagerCallback = new CallbackManagerImpl.Callback() { // from class: com.facebook.share.widget.AppInviteDialog.2
            @Override // com.facebook.internal.CallbackManagerImpl.Callback
            public boolean onActivityResult(int resultCode, Intent data) {
                return ShareInternalUtility.handleActivityResult(AppInviteDialog.this.getRequestCode(), resultCode, data, resultProcessor);
            }
        };
        callbackManager.registerCallback(getRequestCode(), callbackManagerCallback);
    }

    @Override // com.facebook.internal.FacebookDialogBase
    protected AppCall createBaseAppCall() {
        return new AppCall(getRequestCode());
    }

    @Override // com.facebook.internal.FacebookDialogBase
    protected List<FacebookDialogBase<AppInviteContent, Result>.ModeHandler> getOrderedModeHandlers() {
        ArrayList<FacebookDialogBase<AppInviteContent, Result>.ModeHandler> handlers = new ArrayList<>();
        handlers.add(new NativeHandler());
        handlers.add(new WebFallbackHandler());
        return handlers;
    }

    private class NativeHandler extends FacebookDialogBase<AppInviteContent, Result>.ModeHandler {
        private NativeHandler() {
            super();
        }

        @Override // com.facebook.internal.FacebookDialogBase.ModeHandler
        public boolean canShow(AppInviteContent content, boolean isBestEffort) {
            return AppInviteDialog.canShowNativeDialog();
        }

        @Override // com.facebook.internal.FacebookDialogBase.ModeHandler
        public AppCall createAppCall(final AppInviteContent content) {
            AppCall appCall = AppInviteDialog.this.createBaseAppCall();
            DialogPresenter.setupAppCallForNativeDialog(appCall, new DialogPresenter.ParameterProvider() { // from class: com.facebook.share.widget.AppInviteDialog.NativeHandler.1
                @Override // com.facebook.internal.DialogPresenter.ParameterProvider
                public Bundle getParameters() {
                    return AppInviteDialog.createParameters(content);
                }

                @Override // com.facebook.internal.DialogPresenter.ParameterProvider
                public Bundle getLegacyParameters() {
                    Log.e(AppInviteDialog.TAG, "Attempting to present the AppInviteDialog with an outdated Facebook app on the device");
                    return new Bundle();
                }
            }, AppInviteDialog.getFeature());
            return appCall;
        }
    }

    private class WebFallbackHandler extends FacebookDialogBase<AppInviteContent, Result>.ModeHandler {
        private WebFallbackHandler() {
            super();
        }

        @Override // com.facebook.internal.FacebookDialogBase.ModeHandler
        public boolean canShow(AppInviteContent content, boolean isBestEffort) {
            return AppInviteDialog.canShowWebFallback();
        }

        @Override // com.facebook.internal.FacebookDialogBase.ModeHandler
        public AppCall createAppCall(AppInviteContent content) {
            AppCall appCall = AppInviteDialog.this.createBaseAppCall();
            DialogPresenter.setupAppCallForWebFallbackDialog(appCall, AppInviteDialog.createParameters(content), AppInviteDialog.getFeature());
            return appCall;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static DialogFeature getFeature() {
        return AppInviteDialogFeature.APP_INVITES_DIALOG;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Bundle createParameters(AppInviteContent content) {
        Bundle params = new Bundle();
        params.putString(ShareConstants.APPLINK_URL, content.getApplinkUrl());
        params.putString(ShareConstants.PREVIEW_IMAGE_URL, content.getPreviewImageUrl());
        params.putString("destination", content.getDestination().toString());
        String promoCode = content.getPromotionCode();
        if (promoCode == null) {
            promoCode = "";
        }
        String promoText = content.getPromotionText();
        if (!TextUtils.isEmpty(promoText)) {
            try {
                JSONObject deeplinkContent = new JSONObject();
                deeplinkContent.put(ShareConstants.PROMO_CODE, promoCode);
                deeplinkContent.put(ShareConstants.PROMO_TEXT, promoText);
                params.putString(ShareConstants.DEEPLINK_CONTEXT, deeplinkContent.toString());
                params.putString(ShareConstants.PROMO_CODE, promoCode);
                params.putString(ShareConstants.PROMO_TEXT, promoText);
            } catch (JSONException e) {
                Log.e(TAG, "Json Exception in creating deeplink context");
            }
        }
        return params;
    }
}
