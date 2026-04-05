package android.support.wearable.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.CallSuper;
import android.support.wearable.R;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.ObservableScrollView;
import android.text.TextUtils;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.TextView;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(21)
public class WearableDialogActivity extends WearableActivity implements Handler.Callback, View.OnLayoutChangeListener, ObservableScrollView.OnScrollListener, View.OnClickListener, View.OnApplyWindowInsetsListener {
    private static final long ANIM_DURATION = 500;
    private static final long HIDE_ANIM_DELAY = 1500;
    private static final int MSG_HIDE_BUTTON_BAR = 1001;
    private ViewGroup mAnimatedWrapperContainer;
    private Button mButtonNegative;
    private Button mButtonNeutral;
    private ViewGroup mButtonPanel;
    private ObjectAnimator mButtonPanelAnimator;
    private float mButtonPanelFloatHeight;
    private int mButtonPanelShadeHeight;
    private Button mButtonPositive;
    private Handler mHandler;
    private boolean mHiddenBefore;
    private Interpolator mInterpolator;
    private boolean mIsLowBitAmbient;
    private TextView mMessageView;
    private ObservableScrollView mParentPanel;
    private TextView mTitleView;
    private PropertyValuesHolder mTranslationValuesHolder;

    @Override // android.support.wearable.activity.WearableActivity, android.app.Activity
    @CallSuper
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_WearDiag);
        setContentView(R.layout.alert_dialog_wearable);
        this.mAnimatedWrapperContainer = (ViewGroup) findViewById(R.id.animatedWrapperContainer);
        this.mTitleView = (TextView) this.mAnimatedWrapperContainer.findViewById(R.id.alertTitle);
        this.mMessageView = (TextView) this.mAnimatedWrapperContainer.findViewById(android.R.id.message);
        this.mButtonPanel = (ViewGroup) this.mAnimatedWrapperContainer.findViewById(R.id.buttonPanel);
        this.mButtonPositive = (Button) this.mButtonPanel.findViewById(android.R.id.button1);
        this.mButtonPositive.setOnClickListener(this);
        this.mButtonNegative = (Button) this.mButtonPanel.findViewById(android.R.id.button2);
        this.mButtonNegative.setOnClickListener(this);
        this.mButtonNeutral = (Button) this.mButtonPanel.findViewById(android.R.id.button3);
        this.mButtonNeutral.setOnClickListener(this);
        setupLayout();
        this.mHandler = new Handler(this);
        this.mInterpolator = AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in);
        this.mButtonPanelFloatHeight = getResources().getDimension(R.dimen.diag_floating_height);
        this.mParentPanel = (ObservableScrollView) findViewById(R.id.parentPanel);
        this.mParentPanel.addOnLayoutChangeListener(this);
        this.mParentPanel.setOnScrollListener(this);
        this.mParentPanel.setOnApplyWindowInsetsListener(this);
    }

    @Override // android.view.View.OnApplyWindowInsetsListener
    public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
        Resources res = getResources();
        if (insets.isRound()) {
            this.mButtonPanelShadeHeight = res.getDimensionPixelSize(R.dimen.diag_shade_height_round);
            this.mTitleView.setPadding(res.getDimensionPixelSize(R.dimen.diag_content_side_padding_round), res.getDimensionPixelSize(R.dimen.diag_content_top_padding_round), res.getDimensionPixelSize(R.dimen.diag_content_side_padding_round), 0);
            this.mTitleView.setGravity(17);
            this.mMessageView.setPadding(res.getDimensionPixelSize(R.dimen.diag_content_side_padding_round), 0, res.getDimensionPixelSize(R.dimen.diag_content_side_padding_round), res.getDimensionPixelSize(R.dimen.diag_content_bottom_padding));
            this.mMessageView.setGravity(17);
            this.mButtonPanel.setPadding(res.getDimensionPixelSize(R.dimen.diag_content_side_padding_round), 0, res.getDimensionPixelSize(R.dimen.diag_button_side_padding_right_round), res.getDimensionPixelSize(R.dimen.diag_button_bottom_padding_round));
        } else {
            this.mButtonPanelShadeHeight = getResources().getDimensionPixelSize(R.dimen.diag_shade_height_rect);
        }
        return v.onApplyWindowInsets(insets);
    }

    protected void setupLayout() {
        CharSequence title = getAlertTitle();
        if (TextUtils.isEmpty(title)) {
            this.mTitleView.setVisibility(8);
        } else {
            this.mMessageView.setVisibility(0);
            this.mTitleView.setText(title);
        }
        CharSequence message = getMessage();
        if (TextUtils.isEmpty(message)) {
            this.mMessageView.setVisibility(8);
        } else {
            this.mMessageView.setVisibility(0);
            this.mMessageView.setText(message);
        }
        boolean hasButtons = setButton(this.mButtonPositive, getPositiveButtonText(), getPositiveButtonDrawable());
        boolean hasButtons2 = setButton(this.mButtonNegative, getNegativeButtonText(), getNegativeButtonDrawable()) || hasButtons;
        boolean hasButtons3 = setButton(this.mButtonNeutral, getNeutralButtonText(), getNeutralButtonDrawable()) || hasButtons2;
        this.mButtonPanel.setVisibility(hasButtons3 ? 0 : 8);
    }

    private boolean setButton(Button button, CharSequence text, Drawable drawable) {
        if (TextUtils.isEmpty(text)) {
            button.setVisibility(8);
            return false;
        }
        button.setText(text);
        if (drawable != null) {
            button.setCompoundDrawablesWithIntrinsicBounds(drawable, (Drawable) null, (Drawable) null, (Drawable) null);
        }
        button.setVisibility(0);
        return true;
    }

    public CharSequence getAlertTitle() {
        return null;
    }

    public CharSequence getMessage() {
        return null;
    }

    public CharSequence getPositiveButtonText() {
        return null;
    }

    public Drawable getPositiveButtonDrawable() {
        return null;
    }

    public CharSequence getNegativeButtonText() {
        return null;
    }

    public Drawable getNegativeButtonDrawable() {
        return null;
    }

    public CharSequence getNeutralButtonText() {
        return null;
    }

    public Drawable getNeutralButtonDrawable() {
        return null;
    }

    public void onPositiveButtonClick() {
        finish();
    }

    public void onNeutralButtonClick() {
        finish();
    }

    public void onNegativeButtonClick() {
        finish();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        switch (v.getId()) {
            case android.R.id.button1:
                onPositiveButtonClick();
                break;
            case android.R.id.button2:
                onNegativeButtonClick();
                break;
            case android.R.id.button3:
                onNeutralButtonClick();
                break;
        }
    }

    @Override // android.support.wearable.view.ObservableScrollView.OnScrollListener
    public void onScroll(float deltaY) {
        this.mHandler.removeMessages(1001);
        hideButtonBar();
    }

    @Override // android.view.View.OnLayoutChangeListener
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (this.mButtonPanelAnimator != null) {
            this.mButtonPanelAnimator.cancel();
        }
        this.mHandler.removeMessages(1001);
        this.mHiddenBefore = false;
        if (this.mAnimatedWrapperContainer.getHeight() > this.mParentPanel.getHeight()) {
            this.mButtonPanel.setTranslationZ(this.mButtonPanelFloatHeight);
            this.mHandler.sendEmptyMessageDelayed(1001, HIDE_ANIM_DELAY);
            this.mButtonPanelAnimator = ObjectAnimator.ofPropertyValuesHolder(this.mButtonPanel, PropertyValuesHolder.ofFloat((Property<?, Float>) View.TRANSLATION_Y, getButtonBarFloatingBottomTranslation(), getButtonBarFloatingTopTranslation()), PropertyValuesHolder.ofFloat((Property<?, Float>) View.TRANSLATION_Z, 0.0f, this.mButtonPanelFloatHeight));
            this.mButtonPanelAnimator.setDuration(ANIM_DURATION);
            this.mButtonPanelAnimator.setInterpolator(this.mInterpolator);
            this.mButtonPanelAnimator.start();
            return;
        }
        this.mButtonPanel.setTranslationY(0.0f);
        this.mButtonPanel.setTranslationZ(0.0f);
        this.mButtonPanel.offsetTopAndBottom(this.mParentPanel.getHeight() - this.mAnimatedWrapperContainer.getHeight());
        this.mAnimatedWrapperContainer.setBottom(this.mParentPanel.getHeight());
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 1001:
                hideButtonBar();
                return true;
            default:
                return false;
        }
    }

    private int getButtonBarFloatingTopTranslation() {
        return getButtonBarOffsetFromBottom() - Math.min(this.mButtonPanel.getHeight(), this.mButtonPanelShadeHeight);
    }

    private int getButtonBarFloatingBottomTranslation() {
        return Math.min(getButtonBarOffsetFromBottom(), 0);
    }

    private int getButtonBarOffsetFromBottom() {
        return (-this.mButtonPanel.getTop()) + Math.max(this.mParentPanel.getScrollY(), 0) + this.mParentPanel.getHeight();
    }

    private void hideButtonBar() {
        if (!this.mHiddenBefore || this.mButtonPanelAnimator == null) {
            if (this.mButtonPanelAnimator != null) {
                this.mButtonPanelAnimator.cancel();
            }
            this.mTranslationValuesHolder = PropertyValuesHolder.ofFloat((Property<?, Float>) View.TRANSLATION_Y, getButtonBarFloatingTopTranslation(), getButtonBarFloatingBottomTranslation());
            this.mButtonPanelAnimator = ObjectAnimator.ofPropertyValuesHolder(this.mButtonPanel, this.mTranslationValuesHolder, PropertyValuesHolder.ofFloat((Property<?, Float>) View.TRANSLATION_Z, this.mButtonPanelFloatHeight, 0.0f));
            this.mButtonPanelAnimator.addListener(new AnimatorListenerAdapter() { // from class: android.support.wearable.view.WearableDialogActivity.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animation) {
                    WearableDialogActivity.this.mParentPanel.setOnScrollListener(null);
                    WearableDialogActivity.this.mButtonPanel.setTranslationY(0.0f);
                    WearableDialogActivity.this.mButtonPanel.setTranslationZ(0.0f);
                }
            });
            this.mButtonPanelAnimator.setDuration(ANIM_DURATION);
            this.mButtonPanelAnimator.setInterpolator(this.mInterpolator);
            this.mButtonPanelAnimator.start();
        } else if (this.mButtonPanelAnimator.isRunning()) {
            int start = getButtonBarFloatingTopTranslation();
            int end = getButtonBarFloatingBottomTranslation();
            if (start < end) {
                this.mTranslationValuesHolder.setFloatValues(start, end);
                if (this.mButtonPanel.getTranslationY() < start) {
                    this.mButtonPanel.setTranslationY(start);
                }
            } else {
                this.mButtonPanelAnimator.cancel();
                this.mButtonPanel.setTranslationY(0.0f);
                this.mButtonPanel.setTranslationZ(0.0f);
            }
        } else {
            this.mButtonPanel.setTranslationY(0.0f);
            this.mButtonPanel.setTranslationZ(0.0f);
        }
        this.mHiddenBefore = true;
    }

    @Override // android.support.wearable.activity.WearableActivity
    @CallSuper
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        this.mIsLowBitAmbient = ambientDetails.getBoolean(WearableActivity.EXTRA_LOWBIT_AMBIENT);
        this.mButtonPanel.setVisibility(8);
        if (this.mIsLowBitAmbient) {
            setAntiAlias(this.mTitleView, false);
            setAntiAlias(this.mMessageView, false);
        }
    }

    @Override // android.support.wearable.activity.WearableActivity
    @CallSuper
    public void onExitAmbient() {
        super.onExitAmbient();
        this.mButtonPanel.setVisibility(0);
        if (this.mIsLowBitAmbient) {
            setAntiAlias(this.mTitleView, true);
            setAntiAlias(this.mMessageView, true);
        }
    }

    private void setAntiAlias(TextView textView, boolean antiAlias) {
        textView.getPaint().setAntiAlias(antiAlias);
        textView.invalidate();
    }
}
