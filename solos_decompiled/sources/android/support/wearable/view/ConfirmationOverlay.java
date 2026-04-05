package android.support.wearable.view;

import android.R;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(21)
public class ConfirmationOverlay implements View.OnTouchListener {
    private static final int ANIMATION_DURATION_MS = 1000;
    public static final int FAILURE_ANIMATION = 1;
    public static final int OPEN_ON_PHONE_ANIMATION = 2;
    public static final int SUCCESS_ANIMATION = 0;
    private FinishedAnimationListener mListener;
    private String mMessage;
    private Drawable mOverlayDrawable;
    private View mOverlayView;
    private int mType = 0;
    private boolean mIsShowing = false;
    private final Handler mMainThreadHandler = new Handler(Looper.getMainLooper());
    private final Runnable mHideRunnable = new Runnable() { // from class: android.support.wearable.view.ConfirmationOverlay.1
        @Override // java.lang.Runnable
        public void run() {
            ConfirmationOverlay.this.hide();
        }
    };

    public interface FinishedAnimationListener {
        void onAnimationFinished();
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface OverlayType {
    }

    @Override // android.view.View.OnTouchListener
    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    public ConfirmationOverlay setMessage(String message) {
        this.mMessage = message;
        return this;
    }

    public ConfirmationOverlay setType(int type) {
        this.mType = type;
        return this;
    }

    public ConfirmationOverlay setFinishedAnimationListener(@Nullable FinishedAnimationListener listener) {
        this.mListener = listener;
        return this;
    }

    @MainThread
    public void showAbove(View view) {
        if (!this.mIsShowing) {
            this.mIsShowing = true;
            updateOverlayView(view.getContext());
            ((ViewGroup) view.getRootView()).addView(this.mOverlayView);
            animateAndHideAfterDelay();
        }
    }

    @MainThread
    public void showOn(Activity activity) {
        if (!this.mIsShowing) {
            this.mIsShowing = true;
            updateOverlayView(activity);
            activity.getWindow().addContentView(this.mOverlayView, this.mOverlayView.getLayoutParams());
            animateAndHideAfterDelay();
        }
    }

    @MainThread
    private void animateAndHideAfterDelay() {
        if (this.mOverlayDrawable instanceof Animatable) {
            Animatable animatable = (Animatable) this.mOverlayDrawable;
            animatable.start();
        }
        this.mMainThreadHandler.postDelayed(this.mHideRunnable, 1000L);
    }

    @VisibleForTesting
    @MainThread
    public void hide() {
        Animation fadeOut = AnimationUtils.loadAnimation(this.mOverlayView.getContext(), R.anim.fade_out);
        fadeOut.setAnimationListener(new Animation.AnimationListener() { // from class: android.support.wearable.view.ConfirmationOverlay.2
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                ((ViewGroup) ConfirmationOverlay.this.mOverlayView.getParent()).removeView(ConfirmationOverlay.this.mOverlayView);
                ConfirmationOverlay.this.mIsShowing = false;
                if (ConfirmationOverlay.this.mListener != null) {
                    ConfirmationOverlay.this.mListener.onAnimationFinished();
                }
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }
        });
        this.mOverlayView.startAnimation(fadeOut);
    }

    @MainThread
    private void updateOverlayView(Context context) {
        if (this.mOverlayView == null) {
            this.mOverlayView = LayoutInflater.from(context).inflate(android.support.wearable.R.layout.overlay_confirmation, (ViewGroup) null);
        }
        this.mOverlayView.setOnTouchListener(this);
        this.mOverlayView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        updateImageView(context, this.mOverlayView);
        updateMessageView(context, this.mOverlayView);
    }

    @MainThread
    private void updateMessageView(Context context, View overlayView) {
        TextView messageView = (TextView) overlayView.findViewById(android.support.wearable.R.id.wearable_support_confirmation_overlay_message);
        if (this.mMessage != null) {
            int screenWidthPx = ResourcesUtil.getScreenWidthPx(context);
            int topMarginPx = ResourcesUtil.getFractionOfScreenPx(context, screenWidthPx, android.support.wearable.R.fraction.confirmation_overlay_margin_above_text);
            int sideMarginPx = ResourcesUtil.getFractionOfScreenPx(context, screenWidthPx, android.support.wearable.R.fraction.confirmation_overlay_margin_side);
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) messageView.getLayoutParams();
            layoutParams.topMargin = topMarginPx;
            layoutParams.leftMargin = sideMarginPx;
            layoutParams.rightMargin = sideMarginPx;
            messageView.setLayoutParams(layoutParams);
            messageView.setText(this.mMessage);
            messageView.setVisibility(0);
            return;
        }
        messageView.setVisibility(8);
    }

    @MainThread
    private void updateImageView(Context context, View overlayView) {
        switch (this.mType) {
            case 0:
                this.mOverlayDrawable = ContextCompat.getDrawable(context, android.support.wearable.R.drawable.generic_confirmation_animation);
                break;
            case 1:
                this.mOverlayDrawable = ContextCompat.getDrawable(context, android.support.wearable.R.drawable.ic_full_sad);
                break;
            case 2:
                this.mOverlayDrawable = ContextCompat.getDrawable(context, android.support.wearable.R.drawable.open_on_phone_animation);
                break;
            default:
                String errorMessage = String.format(Locale.US, "Invalid ConfirmationOverlay type [%d]", Integer.valueOf(this.mType));
                throw new IllegalStateException(errorMessage);
        }
        ImageView imageView = (ImageView) overlayView.findViewById(android.support.wearable.R.id.wearable_support_confirmation_overlay_image);
        imageView.setImageDrawable(this.mOverlayDrawable);
    }
}
