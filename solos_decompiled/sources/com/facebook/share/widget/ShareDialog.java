package com.facebook.share.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.facebook.FacebookCallback;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.internal.AnalyticsEvents;
import com.facebook.internal.AppCall;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.internal.DialogFeature;
import com.facebook.internal.DialogPresenter;
import com.facebook.internal.FacebookDialogBase;
import com.facebook.internal.FragmentWrapper;
import com.facebook.internal.Utility;
import com.facebook.share.Sharer;
import com.facebook.share.internal.LegacyNativeDialogParameters;
import com.facebook.share.internal.NativeDialogParameters;
import com.facebook.share.internal.OpenGraphActionDialogFeature;
import com.facebook.share.internal.ShareContentValidation;
import com.facebook.share.internal.ShareDialogFeature;
import com.facebook.share.internal.ShareFeedContent;
import com.facebook.share.internal.ShareInternalUtility;
import com.facebook.share.internal.WebDialogParameters;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareMediaContent;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideoContent;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes41.dex */
public final class ShareDialog extends FacebookDialogBase<ShareContent, Sharer.Result> implements Sharer {
    private static final int DEFAULT_REQUEST_CODE = CallbackManagerImpl.RequestCodeOffset.Share.toRequestCode();
    private static final String FEED_DIALOG = "feed";
    private static final String WEB_OG_SHARE_DIALOG = "share_open_graph";
    private static final String WEB_SHARE_DIALOG = "share";
    private boolean isAutomaticMode;
    private boolean shouldFailOnDataError;

    public enum Mode {
        AUTOMATIC,
        NATIVE,
        WEB,
        FEED
    }

    public static void show(Activity activity, ShareContent shareContent) {
        new ShareDialog(activity).show(shareContent);
    }

    public static void show(Fragment fragment, ShareContent shareContent) {
        show(new FragmentWrapper(fragment), shareContent);
    }

    public static void show(android.app.Fragment fragment, ShareContent shareContent) {
        show(new FragmentWrapper(fragment), shareContent);
    }

    private static void show(FragmentWrapper fragmentWrapper, ShareContent shareContent) {
        new ShareDialog(fragmentWrapper).show(shareContent);
    }

    public static boolean canShow(Class<? extends ShareContent> contentType) {
        return canShowWebTypeCheck(contentType) || canShowNative(contentType);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean canShowNative(Class<? extends ShareContent> contentType) {
        DialogFeature feature = getFeature(contentType);
        return feature != null && DialogPresenter.canPresentNativeDialogWithFeature(feature);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean canShowWebTypeCheck(Class<? extends ShareContent> contentType) {
        return ShareLinkContent.class.isAssignableFrom(contentType) || ShareOpenGraphContent.class.isAssignableFrom(contentType);
    }

    public ShareDialog(Activity activity) {
        super(activity, DEFAULT_REQUEST_CODE);
        this.shouldFailOnDataError = false;
        this.isAutomaticMode = true;
        ShareInternalUtility.registerStaticShareCallback(DEFAULT_REQUEST_CODE);
    }

    public ShareDialog(Fragment fragment) {
        this(new FragmentWrapper(fragment));
    }

    public ShareDialog(android.app.Fragment fragment) {
        this(new FragmentWrapper(fragment));
    }

    private ShareDialog(FragmentWrapper fragmentWrapper) {
        super(fragmentWrapper, DEFAULT_REQUEST_CODE);
        this.shouldFailOnDataError = false;
        this.isAutomaticMode = true;
        ShareInternalUtility.registerStaticShareCallback(DEFAULT_REQUEST_CODE);
    }

    ShareDialog(Activity activity, int requestCode) {
        super(activity, requestCode);
        this.shouldFailOnDataError = false;
        this.isAutomaticMode = true;
        ShareInternalUtility.registerStaticShareCallback(requestCode);
    }

    ShareDialog(Fragment fragment, int requestCode) {
        this(new FragmentWrapper(fragment), requestCode);
    }

    ShareDialog(android.app.Fragment fragment, int requestCode) {
        this(new FragmentWrapper(fragment), requestCode);
    }

    private ShareDialog(FragmentWrapper fragmentWrapper, int requestCode) {
        super(fragmentWrapper, requestCode);
        this.shouldFailOnDataError = false;
        this.isAutomaticMode = true;
        ShareInternalUtility.registerStaticShareCallback(requestCode);
    }

    @Override // com.facebook.internal.FacebookDialogBase
    protected void registerCallbackImpl(CallbackManagerImpl callbackManager, FacebookCallback<Sharer.Result> callback) {
        ShareInternalUtility.registerSharerCallback(getRequestCode(), callbackManager, callback);
    }

    @Override // com.facebook.share.Sharer
    public boolean getShouldFailOnDataError() {
        return this.shouldFailOnDataError;
    }

    @Override // com.facebook.share.Sharer
    public void setShouldFailOnDataError(boolean shouldFailOnDataError) {
        this.shouldFailOnDataError = shouldFailOnDataError;
    }

    public boolean canShow(ShareContent shareContent, Mode mode) {
        Object obj = mode;
        if (mode == Mode.AUTOMATIC) {
            obj = BASE_AUTOMATIC_MODE;
        }
        return canShowImpl(shareContent, obj);
    }

    public void show(ShareContent shareContent, Mode mode) {
        this.isAutomaticMode = mode == Mode.AUTOMATIC;
        Object obj = mode;
        if (this.isAutomaticMode) {
            obj = BASE_AUTOMATIC_MODE;
        }
        showImpl(shareContent, obj);
    }

    @Override // com.facebook.internal.FacebookDialogBase
    protected AppCall createBaseAppCall() {
        return new AppCall(getRequestCode());
    }

    @Override // com.facebook.internal.FacebookDialogBase
    protected List<FacebookDialogBase<ShareContent, Sharer.Result>.ModeHandler> getOrderedModeHandlers() {
        ArrayList<FacebookDialogBase<ShareContent, Sharer.Result>.ModeHandler> handlers = new ArrayList<>();
        handlers.add(new NativeHandler());
        handlers.add(new FeedHandler());
        handlers.add(new WebShareHandler());
        return handlers;
    }

    private class NativeHandler extends FacebookDialogBase<ShareContent, Sharer.Result>.ModeHandler {
        private NativeHandler() {
            super();
        }

        @Override // com.facebook.internal.FacebookDialogBase.ModeHandler
        public Object getMode() {
            return Mode.NATIVE;
        }

        @Override // com.facebook.internal.FacebookDialogBase.ModeHandler
        public boolean canShow(ShareContent content, boolean isBestEffort) {
            if (content == null) {
                return false;
            }
            boolean canShowResult = true;
            if (!isBestEffort) {
                if (content.getShareHashtag() != null) {
                    canShowResult = DialogPresenter.canPresentNativeDialogWithFeature(ShareDialogFeature.HASHTAG);
                }
                if ((content instanceof ShareLinkContent) && !Utility.isNullOrEmpty(((ShareLinkContent) content).getQuote())) {
                    canShowResult &= DialogPresenter.canPresentNativeDialogWithFeature(ShareDialogFeature.LINK_SHARE_QUOTES);
                }
            }
            return canShowResult && ShareDialog.canShowNative(content.getClass());
        }

        @Override // com.facebook.internal.FacebookDialogBase.ModeHandler
        public AppCall createAppCall(final ShareContent content) {
            ShareDialog.this.logDialogShare(ShareDialog.this.getActivityContext(), content, Mode.NATIVE);
            ShareContentValidation.validateForNativeShare(content);
            final AppCall appCall = ShareDialog.this.createBaseAppCall();
            final boolean shouldFailOnDataError = ShareDialog.this.getShouldFailOnDataError();
            DialogPresenter.setupAppCallForNativeDialog(appCall, new DialogPresenter.ParameterProvider() { // from class: com.facebook.share.widget.ShareDialog.NativeHandler.1
                @Override // com.facebook.internal.DialogPresenter.ParameterProvider
                public Bundle getParameters() {
                    return NativeDialogParameters.create(appCall.getCallId(), content, shouldFailOnDataError);
                }

                @Override // com.facebook.internal.DialogPresenter.ParameterProvider
                public Bundle getLegacyParameters() {
                    return LegacyNativeDialogParameters.create(appCall.getCallId(), content, shouldFailOnDataError);
                }
            }, ShareDialog.getFeature(content.getClass()));
            return appCall;
        }
    }

    private class WebShareHandler extends FacebookDialogBase<ShareContent, Sharer.Result>.ModeHandler {
        private WebShareHandler() {
            super();
        }

        @Override // com.facebook.internal.FacebookDialogBase.ModeHandler
        public Object getMode() {
            return Mode.WEB;
        }

        @Override // com.facebook.internal.FacebookDialogBase.ModeHandler
        public boolean canShow(ShareContent content, boolean isBestEffort) {
            return content != null && ShareDialog.canShowWebTypeCheck(content.getClass());
        }

        @Override // com.facebook.internal.FacebookDialogBase.ModeHandler
        public AppCall createAppCall(ShareContent content) {
            Bundle params;
            ShareDialog.this.logDialogShare(ShareDialog.this.getActivityContext(), content, Mode.WEB);
            AppCall appCall = ShareDialog.this.createBaseAppCall();
            ShareContentValidation.validateForWebShare(content);
            if (content instanceof ShareLinkContent) {
                params = WebDialogParameters.create((ShareLinkContent) content);
            } else {
                params = WebDialogParameters.create((ShareOpenGraphContent) content);
            }
            DialogPresenter.setupAppCallForWebDialog(appCall, getActionName(content), params);
            return appCall;
        }

        private String getActionName(ShareContent shareContent) {
            if (shareContent instanceof ShareLinkContent) {
                return "share";
            }
            if (shareContent instanceof ShareOpenGraphContent) {
                return ShareDialog.WEB_OG_SHARE_DIALOG;
            }
            return null;
        }
    }

    private class FeedHandler extends FacebookDialogBase<ShareContent, Sharer.Result>.ModeHandler {
        private FeedHandler() {
            super();
        }

        @Override // com.facebook.internal.FacebookDialogBase.ModeHandler
        public Object getMode() {
            return Mode.FEED;
        }

        @Override // com.facebook.internal.FacebookDialogBase.ModeHandler
        public boolean canShow(ShareContent content, boolean isBestEffort) {
            return (content instanceof ShareLinkContent) || (content instanceof ShareFeedContent);
        }

        @Override // com.facebook.internal.FacebookDialogBase.ModeHandler
        public AppCall createAppCall(ShareContent content) {
            Bundle params;
            ShareDialog.this.logDialogShare(ShareDialog.this.getActivityContext(), content, Mode.FEED);
            AppCall appCall = ShareDialog.this.createBaseAppCall();
            if (content instanceof ShareLinkContent) {
                ShareLinkContent linkContent = (ShareLinkContent) content;
                ShareContentValidation.validateForWebShare(linkContent);
                params = WebDialogParameters.createForFeed(linkContent);
            } else {
                ShareFeedContent feedContent = (ShareFeedContent) content;
                params = WebDialogParameters.createForFeed(feedContent);
            }
            DialogPresenter.setupAppCallForWebDialog(appCall, ShareDialog.FEED_DIALOG, params);
            return appCall;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static DialogFeature getFeature(Class<? extends ShareContent> contentType) {
        if (ShareLinkContent.class.isAssignableFrom(contentType)) {
            return ShareDialogFeature.SHARE_DIALOG;
        }
        if (SharePhotoContent.class.isAssignableFrom(contentType)) {
            return ShareDialogFeature.PHOTOS;
        }
        if (ShareVideoContent.class.isAssignableFrom(contentType)) {
            return ShareDialogFeature.VIDEO;
        }
        if (ShareOpenGraphContent.class.isAssignableFrom(contentType)) {
            return OpenGraphActionDialogFeature.OG_ACTION_DIALOG;
        }
        if (ShareMediaContent.class.isAssignableFrom(contentType)) {
            return ShareDialogFeature.MULTIMEDIA;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logDialogShare(Context context, ShareContent content, Mode mode) {
        String displayType;
        String contentType;
        if (this.isAutomaticMode) {
            mode = Mode.AUTOMATIC;
        }
        switch (mode) {
            case AUTOMATIC:
                displayType = AnalyticsEvents.PARAMETER_SHARE_DIALOG_SHOW_AUTOMATIC;
                break;
            case WEB:
                displayType = AnalyticsEvents.PARAMETER_SHARE_DIALOG_SHOW_WEB;
                break;
            case NATIVE:
                displayType = AnalyticsEvents.PARAMETER_SHARE_DIALOG_SHOW_NATIVE;
                break;
            default:
                displayType = "unknown";
                break;
        }
        DialogFeature dialogFeature = getFeature(content.getClass());
        if (dialogFeature == ShareDialogFeature.SHARE_DIALOG) {
            contentType = "status";
        } else if (dialogFeature == ShareDialogFeature.PHOTOS) {
            contentType = "photo";
        } else if (dialogFeature == ShareDialogFeature.VIDEO) {
            contentType = "video";
        } else if (dialogFeature == OpenGraphActionDialogFeature.OG_ACTION_DIALOG) {
            contentType = AnalyticsEvents.PARAMETER_SHARE_DIALOG_CONTENT_OPENGRAPH;
        } else {
            contentType = "unknown";
        }
        AppEventsLogger logger = AppEventsLogger.newLogger(context);
        Bundle parameters = new Bundle();
        parameters.putString("fb_share_dialog_show", displayType);
        parameters.putString(AnalyticsEvents.PARAMETER_SHARE_DIALOG_CONTENT_TYPE, contentType);
        logger.logSdkEvent("fb_share_dialog_show", null, parameters);
    }
}
