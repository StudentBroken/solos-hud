package com.facebook.share.widget;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.facebook.FacebookException;
import com.facebook.R;
import com.facebook.internal.AnalyticsEvents;
import com.facebook.internal.FragmentWrapper;
import com.facebook.internal.NativeProtocol;
import com.facebook.internal.Utility;
import com.facebook.share.internal.LikeActionController;
import com.facebook.share.internal.LikeBoxCountView;
import com.facebook.share.internal.LikeButton;
import com.kopin.pupil.ui.PageHelper;

/* JADX INFO: loaded from: classes41.dex */
public class LikeView extends FrameLayout {
    private static final int NO_FOREGROUND_COLOR = -1;
    private AuxiliaryViewPosition auxiliaryViewPosition;
    private BroadcastReceiver broadcastReceiver;
    private LinearLayout containerView;
    private LikeActionControllerCreationCallback creationCallback;
    private int edgePadding;
    private boolean explicitlyDisabled;
    private int foregroundColor;
    private HorizontalAlignment horizontalAlignment;
    private int internalPadding;
    private LikeActionController likeActionController;
    private LikeBoxCountView likeBoxCountView;
    private LikeButton likeButton;
    private Style likeViewStyle;
    private String objectId;
    private ObjectType objectType;
    private OnErrorListener onErrorListener;
    private FragmentWrapper parentFragment;
    private TextView socialSentenceView;

    public interface OnErrorListener {
        void onError(FacebookException facebookException);
    }

    public enum ObjectType {
        UNKNOWN("unknown", 0),
        OPEN_GRAPH(AnalyticsEvents.PARAMETER_SHARE_DIALOG_CONTENT_OPENGRAPH, 1),
        PAGE("page", 2);

        private int intValue;
        private String stringValue;
        public static ObjectType DEFAULT = UNKNOWN;

        public static ObjectType fromInt(int enumValue) {
            ObjectType[] arr$ = values();
            for (ObjectType objectType : arr$) {
                if (objectType.getValue() == enumValue) {
                    return objectType;
                }
            }
            return null;
        }

        ObjectType(String stringValue, int value) {
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

    public enum Style {
        STANDARD("standard", 0),
        BUTTON("button", 1),
        BOX_COUNT("box_count", 2);

        private int intValue;
        private String stringValue;
        static Style DEFAULT = STANDARD;

        static Style fromInt(int enumValue) {
            Style[] arr$ = values();
            for (Style style : arr$) {
                if (style.getValue() == enumValue) {
                    return style;
                }
            }
            return null;
        }

        Style(String stringValue, int value) {
            this.stringValue = stringValue;
            this.intValue = value;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.stringValue;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getValue() {
            return this.intValue;
        }
    }

    public enum HorizontalAlignment {
        CENTER("center", 0),
        LEFT("left", 1),
        RIGHT("right", 2);

        static HorizontalAlignment DEFAULT = CENTER;
        private int intValue;
        private String stringValue;

        static HorizontalAlignment fromInt(int enumValue) {
            HorizontalAlignment[] arr$ = values();
            for (HorizontalAlignment horizontalAlignment : arr$) {
                if (horizontalAlignment.getValue() == enumValue) {
                    return horizontalAlignment;
                }
            }
            return null;
        }

        HorizontalAlignment(String stringValue, int value) {
            this.stringValue = stringValue;
            this.intValue = value;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.stringValue;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getValue() {
            return this.intValue;
        }
    }

    public enum AuxiliaryViewPosition {
        BOTTOM(PageHelper.ALIGNMENT_BOTTOM, 0),
        INLINE("inline", 1),
        TOP(PageHelper.ALIGNMENT_TOP, 2);

        static AuxiliaryViewPosition DEFAULT = BOTTOM;
        private int intValue;
        private String stringValue;

        static AuxiliaryViewPosition fromInt(int enumValue) {
            AuxiliaryViewPosition[] arr$ = values();
            for (AuxiliaryViewPosition auxViewPosition : arr$) {
                if (auxViewPosition.getValue() == enumValue) {
                    return auxViewPosition;
                }
            }
            return null;
        }

        AuxiliaryViewPosition(String stringValue, int value) {
            this.stringValue = stringValue;
            this.intValue = value;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.stringValue;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getValue() {
            return this.intValue;
        }
    }

    public LikeView(Context context) {
        super(context);
        this.likeViewStyle = Style.DEFAULT;
        this.horizontalAlignment = HorizontalAlignment.DEFAULT;
        this.auxiliaryViewPosition = AuxiliaryViewPosition.DEFAULT;
        this.foregroundColor = -1;
        initialize(context);
    }

    public LikeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.likeViewStyle = Style.DEFAULT;
        this.horizontalAlignment = HorizontalAlignment.DEFAULT;
        this.auxiliaryViewPosition = AuxiliaryViewPosition.DEFAULT;
        this.foregroundColor = -1;
        parseAttributes(attrs);
        initialize(context);
    }

    public void setObjectIdAndType(String objectId, ObjectType objectType) {
        String objectId2 = Utility.coerceValueIfNullOrEmpty(objectId, null);
        if (objectType == null) {
            objectType = ObjectType.DEFAULT;
        }
        if (!Utility.areObjectsEqual(objectId2, this.objectId) || objectType != this.objectType) {
            setObjectIdAndTypeForced(objectId2, objectType);
            updateLikeStateAndLayout();
        }
    }

    public void setLikeViewStyle(Style likeViewStyle) {
        if (likeViewStyle == null) {
            likeViewStyle = Style.DEFAULT;
        }
        if (this.likeViewStyle != likeViewStyle) {
            this.likeViewStyle = likeViewStyle;
            updateLayout();
        }
    }

    public void setAuxiliaryViewPosition(AuxiliaryViewPosition auxiliaryViewPosition) {
        if (auxiliaryViewPosition == null) {
            auxiliaryViewPosition = AuxiliaryViewPosition.DEFAULT;
        }
        if (this.auxiliaryViewPosition != auxiliaryViewPosition) {
            this.auxiliaryViewPosition = auxiliaryViewPosition;
            updateLayout();
        }
    }

    public void setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
        if (horizontalAlignment == null) {
            horizontalAlignment = HorizontalAlignment.DEFAULT;
        }
        if (this.horizontalAlignment != horizontalAlignment) {
            this.horizontalAlignment = horizontalAlignment;
            updateLayout();
        }
    }

    public void setForegroundColor(int foregroundColor) {
        if (this.foregroundColor != foregroundColor) {
            this.socialSentenceView.setTextColor(foregroundColor);
        }
    }

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    public OnErrorListener getOnErrorListener() {
        return this.onErrorListener;
    }

    public void setFragment(Fragment fragment) {
        this.parentFragment = new FragmentWrapper(fragment);
    }

    public void setFragment(android.app.Fragment fragment) {
        this.parentFragment = new FragmentWrapper(fragment);
    }

    @Override // android.view.View
    public void setEnabled(boolean enabled) {
        this.explicitlyDisabled = !enabled;
        updateLikeStateAndLayout();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        setObjectIdAndType(null, ObjectType.UNKNOWN);
        super.onDetachedFromWindow();
    }

    private void parseAttributes(AttributeSet attrs) {
        TypedArray a;
        if (attrs != null && getContext() != null && (a = getContext().obtainStyledAttributes(attrs, R.styleable.com_facebook_like_view)) != null) {
            this.objectId = Utility.coerceValueIfNullOrEmpty(a.getString(R.styleable.com_facebook_like_view_com_facebook_object_id), null);
            this.objectType = ObjectType.fromInt(a.getInt(R.styleable.com_facebook_like_view_com_facebook_object_type, ObjectType.DEFAULT.getValue()));
            this.likeViewStyle = Style.fromInt(a.getInt(R.styleable.com_facebook_like_view_com_facebook_style, Style.DEFAULT.getValue()));
            if (this.likeViewStyle != null) {
                this.auxiliaryViewPosition = AuxiliaryViewPosition.fromInt(a.getInt(R.styleable.com_facebook_like_view_com_facebook_auxiliary_view_position, AuxiliaryViewPosition.DEFAULT.getValue()));
                if (this.auxiliaryViewPosition != null) {
                    this.horizontalAlignment = HorizontalAlignment.fromInt(a.getInt(R.styleable.com_facebook_like_view_com_facebook_horizontal_alignment, HorizontalAlignment.DEFAULT.getValue()));
                    if (this.horizontalAlignment == null) {
                        throw new IllegalArgumentException("Unsupported value for LikeView 'horizontal_alignment'");
                    }
                    this.foregroundColor = a.getColor(R.styleable.com_facebook_like_view_com_facebook_foreground_color, -1);
                    a.recycle();
                    return;
                }
                throw new IllegalArgumentException("Unsupported value for LikeView 'auxiliary_view_position'");
            }
            throw new IllegalArgumentException("Unsupported value for LikeView 'style'");
        }
    }

    private void initialize(Context context) {
        this.edgePadding = getResources().getDimensionPixelSize(R.dimen.com_facebook_likeview_edge_padding);
        this.internalPadding = getResources().getDimensionPixelSize(R.dimen.com_facebook_likeview_internal_padding);
        if (this.foregroundColor == -1) {
            this.foregroundColor = getResources().getColor(R.color.com_facebook_likeview_text_color);
        }
        setBackgroundColor(0);
        this.containerView = new LinearLayout(context);
        FrameLayout.LayoutParams containerViewLayoutParams = new FrameLayout.LayoutParams(-2, -2);
        this.containerView.setLayoutParams(containerViewLayoutParams);
        initializeLikeButton(context);
        initializeSocialSentenceView(context);
        initializeLikeCountView(context);
        this.containerView.addView(this.likeButton);
        this.containerView.addView(this.socialSentenceView);
        this.containerView.addView(this.likeBoxCountView);
        addView(this.containerView);
        setObjectIdAndTypeForced(this.objectId, this.objectType);
        updateLikeStateAndLayout();
    }

    private void initializeLikeButton(Context context) {
        this.likeButton = new LikeButton(context, this.likeActionController != null && this.likeActionController.isObjectLiked());
        this.likeButton.setOnClickListener(new View.OnClickListener() { // from class: com.facebook.share.widget.LikeView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                LikeView.this.toggleLike();
            }
        });
        LinearLayout.LayoutParams buttonLayout = new LinearLayout.LayoutParams(-2, -2);
        this.likeButton.setLayoutParams(buttonLayout);
    }

    private void initializeSocialSentenceView(Context context) {
        this.socialSentenceView = new TextView(context);
        this.socialSentenceView.setTextSize(0, getResources().getDimension(R.dimen.com_facebook_likeview_text_size));
        this.socialSentenceView.setMaxLines(2);
        this.socialSentenceView.setTextColor(this.foregroundColor);
        this.socialSentenceView.setGravity(17);
        LinearLayout.LayoutParams socialSentenceViewLayout = new LinearLayout.LayoutParams(-2, -1);
        this.socialSentenceView.setLayoutParams(socialSentenceViewLayout);
    }

    private void initializeLikeCountView(Context context) {
        this.likeBoxCountView = new LikeBoxCountView(context);
        LinearLayout.LayoutParams likeCountViewLayout = new LinearLayout.LayoutParams(-1, -1);
        this.likeBoxCountView.setLayoutParams(likeCountViewLayout);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toggleLike() {
        if (this.likeActionController != null) {
            Activity activity = null;
            if (this.parentFragment == null) {
                activity = getActivity();
            }
            this.likeActionController.toggleLike(activity, this.parentFragment, getAnalyticsParameters());
        }
    }

    private Activity getActivity() {
        Context context = getContext();
        while (!(context instanceof Activity) && (context instanceof ContextWrapper)) {
            context = ((ContextWrapper) context).getBaseContext();
        }
        if (context instanceof Activity) {
            return (Activity) context;
        }
        throw new FacebookException("Unable to get Activity.");
    }

    private Bundle getAnalyticsParameters() {
        Bundle params = new Bundle();
        params.putString(AnalyticsEvents.PARAMETER_LIKE_VIEW_STYLE, this.likeViewStyle.toString());
        params.putString(AnalyticsEvents.PARAMETER_LIKE_VIEW_AUXILIARY_POSITION, this.auxiliaryViewPosition.toString());
        params.putString(AnalyticsEvents.PARAMETER_LIKE_VIEW_HORIZONTAL_ALIGNMENT, this.horizontalAlignment.toString());
        params.putString("object_id", Utility.coerceValueIfNullOrEmpty(this.objectId, ""));
        params.putString("object_type", this.objectType.toString());
        return params;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setObjectIdAndTypeForced(String newObjectId, ObjectType newObjectType) {
        tearDownObjectAssociations();
        this.objectId = newObjectId;
        this.objectType = newObjectType;
        if (!Utility.isNullOrEmpty(newObjectId)) {
            this.creationCallback = new LikeActionControllerCreationCallback();
            if (!isInEditMode()) {
                LikeActionController.getControllerForObjectId(newObjectId, newObjectType, this.creationCallback);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void associateWithLikeActionController(LikeActionController likeActionController) {
        this.likeActionController = likeActionController;
        this.broadcastReceiver = new LikeControllerBroadcastReceiver();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        IntentFilter filter = new IntentFilter();
        filter.addAction(LikeActionController.ACTION_LIKE_ACTION_CONTROLLER_UPDATED);
        filter.addAction(LikeActionController.ACTION_LIKE_ACTION_CONTROLLER_DID_ERROR);
        filter.addAction(LikeActionController.ACTION_LIKE_ACTION_CONTROLLER_DID_RESET);
        localBroadcastManager.registerReceiver(this.broadcastReceiver, filter);
    }

    private void tearDownObjectAssociations() {
        if (this.broadcastReceiver != null) {
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
            localBroadcastManager.unregisterReceiver(this.broadcastReceiver);
            this.broadcastReceiver = null;
        }
        if (this.creationCallback != null) {
            this.creationCallback.cancel();
            this.creationCallback = null;
        }
        this.likeActionController = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateLikeStateAndLayout() {
        boolean enabled = !this.explicitlyDisabled;
        if (this.likeActionController == null) {
            this.likeButton.setSelected(false);
            this.socialSentenceView.setText((CharSequence) null);
            this.likeBoxCountView.setText(null);
        } else {
            this.likeButton.setSelected(this.likeActionController.isObjectLiked());
            this.socialSentenceView.setText(this.likeActionController.getSocialSentence());
            this.likeBoxCountView.setText(this.likeActionController.getLikeCountString());
            enabled &= this.likeActionController.shouldEnableView();
        }
        super.setEnabled(enabled);
        this.likeButton.setEnabled(enabled);
        updateLayout();
    }

    private void updateLayout() {
        int viewGravity;
        View auxView;
        FrameLayout.LayoutParams containerViewLayoutParams = (FrameLayout.LayoutParams) this.containerView.getLayoutParams();
        LinearLayout.LayoutParams buttonLayoutParams = (LinearLayout.LayoutParams) this.likeButton.getLayoutParams();
        if (this.horizontalAlignment == HorizontalAlignment.LEFT) {
            viewGravity = 3;
        } else {
            viewGravity = this.horizontalAlignment == HorizontalAlignment.CENTER ? 1 : 5;
        }
        containerViewLayoutParams.gravity = viewGravity | 48;
        buttonLayoutParams.gravity = viewGravity;
        this.socialSentenceView.setVisibility(8);
        this.likeBoxCountView.setVisibility(8);
        if (this.likeViewStyle == Style.STANDARD && this.likeActionController != null && !Utility.isNullOrEmpty(this.likeActionController.getSocialSentence())) {
            auxView = this.socialSentenceView;
        } else if (this.likeViewStyle == Style.BOX_COUNT && this.likeActionController != null && !Utility.isNullOrEmpty(this.likeActionController.getLikeCountString())) {
            updateBoxCountCaretPosition();
            auxView = this.likeBoxCountView;
        } else {
            return;
        }
        auxView.setVisibility(0);
        LinearLayout.LayoutParams auxViewLayoutParams = (LinearLayout.LayoutParams) auxView.getLayoutParams();
        auxViewLayoutParams.gravity = viewGravity;
        this.containerView.setOrientation(this.auxiliaryViewPosition == AuxiliaryViewPosition.INLINE ? 0 : 1);
        if (this.auxiliaryViewPosition == AuxiliaryViewPosition.TOP || (this.auxiliaryViewPosition == AuxiliaryViewPosition.INLINE && this.horizontalAlignment == HorizontalAlignment.RIGHT)) {
            this.containerView.removeView(this.likeButton);
            this.containerView.addView(this.likeButton);
        } else {
            this.containerView.removeView(auxView);
            this.containerView.addView(auxView);
        }
        switch (this.auxiliaryViewPosition) {
            case TOP:
                auxView.setPadding(this.edgePadding, this.edgePadding, this.edgePadding, this.internalPadding);
                break;
            case BOTTOM:
                auxView.setPadding(this.edgePadding, this.internalPadding, this.edgePadding, this.edgePadding);
                break;
            case INLINE:
                if (this.horizontalAlignment == HorizontalAlignment.RIGHT) {
                    auxView.setPadding(this.edgePadding, this.edgePadding, this.internalPadding, this.edgePadding);
                } else {
                    auxView.setPadding(this.internalPadding, this.edgePadding, this.edgePadding, this.edgePadding);
                }
                break;
        }
    }

    private void updateBoxCountCaretPosition() {
        switch (this.auxiliaryViewPosition) {
            case TOP:
                this.likeBoxCountView.setCaretPosition(LikeBoxCountView.LikeBoxCountViewCaretPosition.BOTTOM);
                break;
            case BOTTOM:
                this.likeBoxCountView.setCaretPosition(LikeBoxCountView.LikeBoxCountViewCaretPosition.TOP);
                break;
            case INLINE:
                this.likeBoxCountView.setCaretPosition(this.horizontalAlignment == HorizontalAlignment.RIGHT ? LikeBoxCountView.LikeBoxCountViewCaretPosition.RIGHT : LikeBoxCountView.LikeBoxCountViewCaretPosition.LEFT);
                break;
        }
    }

    private class LikeControllerBroadcastReceiver extends BroadcastReceiver {
        private LikeControllerBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String intentAction = intent.getAction();
            Bundle extras = intent.getExtras();
            boolean shouldRespond = true;
            if (extras != null) {
                String broadcastObjectId = extras.getString(LikeActionController.ACTION_OBJECT_ID_KEY);
                shouldRespond = Utility.isNullOrEmpty(broadcastObjectId) || Utility.areObjectsEqual(LikeView.this.objectId, broadcastObjectId);
            }
            if (shouldRespond) {
                if (LikeActionController.ACTION_LIKE_ACTION_CONTROLLER_UPDATED.equals(intentAction)) {
                    LikeView.this.updateLikeStateAndLayout();
                    return;
                }
                if (LikeActionController.ACTION_LIKE_ACTION_CONTROLLER_DID_ERROR.equals(intentAction)) {
                    if (LikeView.this.onErrorListener != null) {
                        LikeView.this.onErrorListener.onError(NativeProtocol.getExceptionFromErrorData(extras));
                    }
                } else if (LikeActionController.ACTION_LIKE_ACTION_CONTROLLER_DID_RESET.equals(intentAction)) {
                    LikeView.this.setObjectIdAndTypeForced(LikeView.this.objectId, LikeView.this.objectType);
                    LikeView.this.updateLikeStateAndLayout();
                }
            }
        }
    }

    private class LikeActionControllerCreationCallback implements LikeActionController.CreationCallback {
        private boolean isCancelled;

        private LikeActionControllerCreationCallback() {
        }

        public void cancel() {
            this.isCancelled = true;
        }

        @Override // com.facebook.share.internal.LikeActionController.CreationCallback
        public void onComplete(LikeActionController likeActionController, FacebookException error) {
            if (!this.isCancelled) {
                if (likeActionController != null) {
                    if (!likeActionController.shouldEnableView()) {
                        error = new FacebookException("Cannot use LikeView. The device may not be supported.");
                    }
                    LikeView.this.associateWithLikeActionController(likeActionController);
                    LikeView.this.updateLikeStateAndLayout();
                }
                if (error != null && LikeView.this.onErrorListener != null) {
                    LikeView.this.onErrorListener.onError(error);
                }
                LikeView.this.creationCallback = null;
            }
        }
    }
}
