package com.facebook.share.widget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.facebook.FacebookCallback;
import com.facebook.internal.AppCall;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.internal.DialogPresenter;
import com.facebook.internal.FacebookDialogBase;
import com.facebook.internal.FragmentWrapper;
import com.facebook.share.internal.GameRequestValidation;
import com.facebook.share.internal.ResultProcessor;
import com.facebook.share.internal.ShareConstants;
import com.facebook.share.internal.ShareInternalUtility;
import com.facebook.share.internal.WebDialogParameters;
import com.facebook.share.model.GameRequestContent;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes41.dex */
public class GameRequestDialog extends FacebookDialogBase<GameRequestContent, Result> {
    private static final int DEFAULT_REQUEST_CODE = CallbackManagerImpl.RequestCodeOffset.GameRequest.toRequestCode();
    private static final String GAME_REQUEST_DIALOG = "apprequests";

    public static final class Result {
        String requestId;
        List<String> to;

        private Result(Bundle results) {
            this.requestId = results.getString(ShareConstants.WEB_DIALOG_RESULT_PARAM_REQUEST_ID);
            this.to = new ArrayList();
            while (results.containsKey(String.format(ShareConstants.WEB_DIALOG_RESULT_PARAM_TO_ARRAY_MEMBER, Integer.valueOf(this.to.size())))) {
                this.to.add(results.getString(String.format(ShareConstants.WEB_DIALOG_RESULT_PARAM_TO_ARRAY_MEMBER, Integer.valueOf(this.to.size()))));
            }
        }

        public String getRequestId() {
            return this.requestId;
        }

        public List<String> getRequestRecipients() {
            return this.to;
        }
    }

    public static boolean canShow() {
        return true;
    }

    public static void show(Activity activity, GameRequestContent gameRequestContent) {
        new GameRequestDialog(activity).show(gameRequestContent);
    }

    public static void show(Fragment fragment, GameRequestContent gameRequestContent) {
        show(new FragmentWrapper(fragment), gameRequestContent);
    }

    public static void show(android.app.Fragment fragment, GameRequestContent gameRequestContent) {
        show(new FragmentWrapper(fragment), gameRequestContent);
    }

    private static void show(FragmentWrapper fragmentWrapper, GameRequestContent gameRequestContent) {
        new GameRequestDialog(fragmentWrapper).show(gameRequestContent);
    }

    public GameRequestDialog(Activity activity) {
        super(activity, DEFAULT_REQUEST_CODE);
    }

    public GameRequestDialog(Fragment fragment) {
        this(new FragmentWrapper(fragment));
    }

    public GameRequestDialog(android.app.Fragment fragment) {
        this(new FragmentWrapper(fragment));
    }

    private GameRequestDialog(FragmentWrapper fragmentWrapper) {
        super(fragmentWrapper, DEFAULT_REQUEST_CODE);
    }

    @Override // com.facebook.internal.FacebookDialogBase
    protected void registerCallbackImpl(CallbackManagerImpl callbackManager, final FacebookCallback<Result> callback) {
        final ResultProcessor resultProcessor = callback == null ? null : new ResultProcessor(callback) { // from class: com.facebook.share.widget.GameRequestDialog.1
            @Override // com.facebook.share.internal.ResultProcessor
            public void onSuccess(AppCall appCall, Bundle results) {
                if (results != null) {
                    callback.onSuccess(new Result(results));
                } else {
                    onCancel(appCall);
                }
            }
        };
        callbackManager.registerCallback(getRequestCode(), new CallbackManagerImpl.Callback() { // from class: com.facebook.share.widget.GameRequestDialog.2
            @Override // com.facebook.internal.CallbackManagerImpl.Callback
            public boolean onActivityResult(int resultCode, Intent data) {
                return ShareInternalUtility.handleActivityResult(GameRequestDialog.this.getRequestCode(), resultCode, data, resultProcessor);
            }
        });
    }

    @Override // com.facebook.internal.FacebookDialogBase
    protected AppCall createBaseAppCall() {
        return new AppCall(getRequestCode());
    }

    @Override // com.facebook.internal.FacebookDialogBase
    protected List<FacebookDialogBase<GameRequestContent, Result>.ModeHandler> getOrderedModeHandlers() {
        ArrayList<FacebookDialogBase<GameRequestContent, Result>.ModeHandler> handlers = new ArrayList<>();
        handlers.add(new WebHandler());
        return handlers;
    }

    private class WebHandler extends FacebookDialogBase<GameRequestContent, Result>.ModeHandler {
        private WebHandler() {
            super();
        }

        @Override // com.facebook.internal.FacebookDialogBase.ModeHandler
        public boolean canShow(GameRequestContent content, boolean isBestEffort) {
            return true;
        }

        @Override // com.facebook.internal.FacebookDialogBase.ModeHandler
        public AppCall createAppCall(GameRequestContent content) {
            GameRequestValidation.validate(content);
            AppCall appCall = GameRequestDialog.this.createBaseAppCall();
            DialogPresenter.setupAppCallForWebDialog(appCall, GameRequestDialog.GAME_REQUEST_DIALOG, WebDialogParameters.create(content));
            return appCall;
        }
    }
}
