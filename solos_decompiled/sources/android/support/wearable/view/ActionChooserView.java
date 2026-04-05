package android.support.wearable.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.wearable.R;
import android.util.AttributeSet;
import android.util.Property;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.kopin.pupil.ui.PageHelper;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(20)
@Deprecated
public class ActionChooserView extends View {
    private static final int ANIMATION_STATE_DISABLED = 2;
    private static final int ANIMATION_STATE_DISABLING = 1;
    private static final int ANIMATION_STATE_ENABLED = 0;
    public static final int OPTION_END = 2;
    public static final int OPTION_START = 1;
    private final float mAnimMaxOffset;
    private int mAnimationState;
    private final float mBaseRadiusPercentage;
    private final int mBounceAnimationDuration;
    private final int mBounceDelay;
    private final ObjectAnimator mCenterAnimator;
    private final Paint mCirclePaint;
    private final int mConfirmationDelay;
    private final ObjectAnimator mExpandAnimator;
    private final boolean mExpandSelected;
    private final long mExpandToFullMillis;
    private final GestureDetector mGestureDetector;
    private final float mIconHeightPercentage;
    private final float mIdleAnimationSpeed;
    private final AnimatorSet mIdleAnimatorSet;
    private float mLastTouchOffset;
    private float mLastTouchX;
    private ArrayList<ActionChooserListener> mListeners;
    private final float mMaxRadiusPercentage;
    private final float mMinDragSelectPercent;
    private final float mMinSwipeSelectPercent;
    private float mOffset;
    private final SparseArray<Option> mOptions;
    private final ObjectAnimator mReturnAnimator;
    private final Runnable mSelectOptionRunnable;
    private float mSelectedMultiplier;
    private Integer mSelectedOption;
    private float mSelectedPercent;
    private float mSpeed;
    private final boolean mSymmetricalDimens;
    private boolean mTouchedEnabled;
    private static final Property<ActionChooserView, Float> OFFSET = new Property<ActionChooserView, Float>(Float.class, PageHelper.OFFSET_ATTRIBUTE) { // from class: android.support.wearable.view.ActionChooserView.6
        @Override // android.util.Property
        public Float get(ActionChooserView view) {
            return Float.valueOf(view.getOffset());
        }

        @Override // android.util.Property
        public void set(ActionChooserView view, Float value) {
            view.setAnimationOffset(value.floatValue());
        }
    };
    private static final Property<ActionChooserView, Float> SELECTED_MULTIPLIER = new Property<ActionChooserView, Float>(Float.class, "selected_multiplier") { // from class: android.support.wearable.view.ActionChooserView.7
        @Override // android.util.Property
        public Float get(ActionChooserView view) {
            return Float.valueOf(view.getSelectedMultiplier());
        }

        @Override // android.util.Property
        public void set(ActionChooserView view, Float value) {
            view.setSelectedMultiplier(value.floatValue());
        }
    };

    @Deprecated
    public interface ActionChooserListener {
        void onOptionChosen(int i);

        void onOptionProgress(float f);
    }

    public ActionChooserView(Context context) {
        this(context, null);
    }

    public ActionChooserView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ActionChooserView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mSelectOptionRunnable = new Runnable() { // from class: android.support.wearable.view.ActionChooserView.1
            @Override // java.lang.Runnable
            public void run() {
                if (ActionChooserView.this.mListeners != null) {
                    for (ActionChooserListener listener : ActionChooserView.this.mListeners) {
                        listener.onOptionChosen(ActionChooserView.this.mSelectedOption.intValue());
                    }
                }
            }
        };
        this.mSelectedMultiplier = 1.0f;
        this.mTouchedEnabled = true;
        this.mAnimationState = 0;
        this.mCirclePaint = new Paint();
        this.mCirclePaint.setAntiAlias(true);
        this.mCirclePaint.setStyle(Paint.Style.FILL);
        TypedValue val = new TypedValue();
        getResources().getValue(R.dimen.action_chooser_bounce_in_percent, val, true);
        this.mAnimMaxOffset = val.getFloat();
        getResources().getValue(R.dimen.action_chooser_base_radius_percent, val, true);
        this.mBaseRadiusPercentage = val.getFloat();
        getResources().getValue(R.dimen.action_chooser_max_radius_percent, val, true);
        this.mMaxRadiusPercentage = val.getFloat();
        getResources().getValue(R.dimen.action_chooser_icon_height_percent, val, true);
        this.mIconHeightPercentage = val.getFloat();
        getResources().getValue(R.dimen.action_chooser_min_drag_select_percent, val, true);
        this.mMinDragSelectPercent = val.getFloat();
        getResources().getValue(R.dimen.action_chooser_min_swipe_select_percent, val, true);
        this.mMinSwipeSelectPercent = val.getFloat();
        this.mBounceAnimationDuration = getResources().getInteger(R.integer.action_chooser_anim_duration);
        this.mBounceDelay = getResources().getInteger(R.integer.action_chooser_bounce_delay);
        this.mIdleAnimationSpeed = this.mMaxRadiusPercentage / this.mBounceAnimationDuration;
        this.mConfirmationDelay = getResources().getInteger(R.integer.action_chooser_confirmation_duration);
        this.mExpandSelected = getResources().getBoolean(R.bool.action_choose_expand_selected);
        this.mSymmetricalDimens = getResources().getBoolean(R.bool.action_choose_symmetrical_dimen);
        this.mExpandToFullMillis = getResources().getInteger(R.integer.action_choose_expand_full_duration);
        this.mOptions = new SparseArray<>();
        ArrayList<Animator> bounceAnimators = new ArrayList<>();
        bounceAnimators.addAll(generateOptionAnimation(1));
        bounceAnimators.addAll(generateOptionAnimation(2));
        this.mIdleAnimatorSet = new AnimatorSet();
        this.mIdleAnimatorSet.playSequentially(bounceAnimators);
        this.mIdleAnimatorSet.addListener(new Animator.AnimatorListener() { // from class: android.support.wearable.view.ActionChooserView.2
            private boolean mCancelled;

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                this.mCancelled = false;
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (!this.mCancelled && ActionChooserView.this.mAnimationState == 0) {
                    ActionChooserView.this.mIdleAnimatorSet.start();
                }
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                this.mCancelled = true;
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
            }
        });
        this.mReturnAnimator = ObjectAnimator.ofFloat(this, OFFSET, 0.0f);
        this.mReturnAnimator.addListener(new Animator.AnimatorListener() { // from class: android.support.wearable.view.ActionChooserView.3
            private boolean mCancelled;

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                this.mCancelled = false;
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (!this.mCancelled && ActionChooserView.this.mAnimationState == 0) {
                    ActionChooserView.this.mIdleAnimatorSet.start();
                }
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                this.mCancelled = true;
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
            }
        });
        this.mCenterAnimator = ObjectAnimator.ofFloat(this, OFFSET, 0.0f);
        this.mExpandAnimator = ObjectAnimator.ofFloat(this, SELECTED_MULTIPLIER, 1.0f, (float) Math.sqrt(2.0d));
        this.mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() { // from class: android.support.wearable.view.ActionChooserView.4
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (Math.abs(e2.getX() - e1.getX()) >= ActionChooserView.this.getMeasuredWidth() * ActionChooserView.this.mMinSwipeSelectPercent) {
                    int option = velocityX < 0.0f ? 2 : 1;
                    ActionChooserView.this.selectOption(option);
                    ActionChooserView.this.enableAnimations(true);
                    return true;
                }
                return false;
            }
        });
    }

    private ArrayList<Animator> generateOptionAnimation(int option) {
        ArrayList<Animator> returnList = new ArrayList<>();
        int direction = option == 1 ? 1 : -1;
        ObjectAnimator bounceIn = ObjectAnimator.ofFloat(this, OFFSET, 0.0f, direction * this.mAnimMaxOffset);
        bounceIn.setDuration(this.mBounceAnimationDuration);
        bounceIn.setStartDelay(this.mBounceDelay);
        returnList.add(bounceIn);
        ObjectAnimator bounceout = ObjectAnimator.ofFloat(this, OFFSET, direction * this.mAnimMaxOffset, 0.0f);
        bounceIn.setDuration(this.mBounceAnimationDuration);
        bounceIn.setStartDelay(this.mBounceDelay);
        returnList.add(bounceout);
        return returnList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void selectOption(int option) {
        this.mSelectedOption = Integer.valueOf(option);
        this.mTouchedEnabled = false;
    }

    @Override // android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mIdleAnimatorSet.start();
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        this.mIdleAnimatorSet.cancel();
        super.onDetachedFromWindow();
    }

    private boolean validateOption(int option) {
        return option == 1 || option == 2;
    }

    public void addListener(ActionChooserListener listener) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList<>();
        }
        if (!this.mListeners.contains(listener)) {
            this.mListeners.add(listener);
        }
    }

    public void removeListener(ActionChooserListener listener) {
        if (this.mListeners != null) {
            this.mListeners.remove(listener);
        }
    }

    public void setOption(int option, Drawable drawable, int color) {
        if (!validateOption(option)) {
            throw new IllegalArgumentException("unrecognized option");
        }
        this.mOptions.put(option, new Option(color, drawable));
        invalidate();
    }

    public void performSelectOption(int option) {
        if (!validateOption(option)) {
            throw new IllegalArgumentException("unrecognized option");
        }
        if (option == 1) {
            selectOption(1);
            enableAnimations(true);
        } else if (option == 2) {
            selectOption(2);
            enableAnimations(true);
        }
    }

    @Override // android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        layoutOption(this.mOptions.get(1));
        layoutOption(this.mOptions.get(2));
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = canvas.getWidth();
        int height = this.mSymmetricalDimens ? Math.max(width, canvas.getHeight()) : canvas.getHeight();
        int shift = Math.round(width * this.mOffset);
        int baseRadius = Math.round(height * this.mBaseRadiusPercentage);
        float maxRadius = height * this.mMaxRadiusPercentage;
        drawOption(canvas, this.mOptions.get(1), shift - baseRadius, height / 2, getCircleRadius(baseRadius, this.mOffset, maxRadius, isSelected(1), this.mSelectedMultiplier));
        drawOption(canvas, this.mOptions.get(2), shift + width + baseRadius, height / 2, getCircleRadius(baseRadius, -this.mOffset, maxRadius, isSelected(2), this.mSelectedMultiplier));
    }

    private boolean isSelected(int option) {
        return this.mSelectedOption != null && this.mSelectedOption.intValue() == option;
    }

    private float getCircleRadius(int baseRadius, float offset, float maxRadius, boolean selected, float selectedMultiplier) {
        float interpolatedPercent = (offset - this.mAnimMaxOffset) / (getMaxOffset() - this.mAnimMaxOffset);
        float maxDelta = maxRadius - baseRadius;
        float radius = baseRadius + Math.max(0.0f, interpolatedPercent * maxDelta);
        if (!selected) {
            selectedMultiplier = 1.0f;
        }
        return radius * selectedMultiplier;
    }

    private void layoutOption(Option option) {
        if (option != null) {
            Rect bounds = option.icon.getBounds();
            int maxIconDimen = Math.max(option.icon.getIntrinsicHeight(), option.icon.getIntrinsicHeight());
            float maxContainerDimen = this.mIconHeightPercentage * 2.0f * this.mBaseRadiusPercentage * getMeasuredHeight();
            float scale = maxContainerDimen / maxIconDimen;
            bounds.left = 0;
            bounds.top = 0;
            bounds.right = Math.round(option.icon.getIntrinsicWidth() * scale);
            bounds.bottom = Math.round(option.icon.getIntrinsicHeight() * scale);
        }
    }

    private void drawOption(Canvas canvas, Option option, int cX, int cY, float radius) {
        if (option != null) {
            this.mCirclePaint.setColor(option.color);
            canvas.drawCircle(cX, cY, radius, this.mCirclePaint);
            if (option.icon != null) {
                Rect bounds = option.icon.getBounds();
                bounds.offsetTo(cX - (bounds.width() / 2), cY - (bounds.height() / 2));
                option.icon.setBounds(bounds);
                option.icon.draw(canvas);
            }
        }
    }

    @Override // android.view.View
    public boolean canScrollHorizontally(int direction) {
        return true;
    }

    @Override // android.view.View
    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent event) {
        if (!this.mTouchedEnabled) {
            return false;
        }
        if (this.mGestureDetector.onTouchEvent(event)) {
            return true;
        }
        switch (event.getAction() & 255) {
            case 0:
                enableAnimations(false);
                this.mLastTouchX = event.getX();
                this.mLastTouchOffset = getOffset();
                return true;
            case 1:
            case 3:
                if (Math.abs(event.getX() - this.mLastTouchX) >= getMeasuredWidth() * this.mMinDragSelectPercent) {
                    selectOption(event.getX() < this.mLastTouchX ? 2 : 1);
                }
                enableAnimations(true);
                return true;
            case 2:
                float delta = event.getX() - this.mLastTouchX;
                this.mSpeed = Math.abs(((delta / getWidth()) - this.mLastTouchOffset) / (event.getEventTime() - event.getDownTime()));
                float offset = this.mLastTouchOffset + (delta / getWidth());
                setOffset(offset);
                return true;
            default:
                return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getOffset() {
        return this.mOffset;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getSelectedMultiplier() {
        return this.mSelectedMultiplier;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSelectedMultiplier(float percent) {
        this.mSelectedMultiplier = percent;
        invalidate();
    }

    private float getMaxOffset() {
        return 0.5f + this.mBaseRadiusPercentage;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAnimationOffset(float offset) {
        if (this.mAnimationState != 2) {
            setOffset(offset);
        }
    }

    private void setOffset(float offset) {
        int direction = offset < 0.0f ? -1 : 1;
        if (this.mAnimationState == 1 && Math.abs(offset) == 0.0f) {
            enableAnimations(false, true);
            setOffsetAndNotify(0.0f);
            invalidate();
            return;
        }
        setOffsetAndNotify(direction * Math.min(Math.abs(offset), getMaxOffset()));
        if (Math.abs(this.mOffset) >= getMaxOffset()) {
            this.mSelectedOption = Integer.valueOf(direction < 0 ? 2 : 1);
            if (this.mOptions.indexOfKey(this.mSelectedOption.intValue()) > -1) {
                this.mTouchedEnabled = false;
                enableAnimations(false, true);
                if (this.mExpandSelected) {
                    this.mExpandAnimator.setDuration(this.mExpandToFullMillis);
                    this.mExpandAnimator.addListener(new Animator.AnimatorListener() { // from class: android.support.wearable.view.ActionChooserView.5
                        @Override // android.animation.Animator.AnimatorListener
                        public void onAnimationStart(Animator animator) {
                        }

                        @Override // android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            ActionChooserView.this.removeCallbacks(ActionChooserView.this.mSelectOptionRunnable);
                            ActionChooserView.this.postDelayed(ActionChooserView.this.mSelectOptionRunnable, ActionChooserView.this.mConfirmationDelay);
                        }

                        @Override // android.animation.Animator.AnimatorListener
                        public void onAnimationCancel(Animator animator) {
                        }

                        @Override // android.animation.Animator.AnimatorListener
                        public void onAnimationRepeat(Animator animator) {
                        }
                    });
                    this.mExpandAnimator.start();
                } else {
                    removeCallbacks(this.mSelectOptionRunnable);
                    postDelayed(this.mSelectOptionRunnable, this.mConfirmationDelay);
                }
            }
        }
        invalidate();
    }

    private void setOffsetAndNotify(float newOffset) {
        if (newOffset != this.mOffset) {
            this.mOffset = newOffset;
            float percent = Math.max(0.0f, (Math.abs(newOffset) - this.mAnimMaxOffset) / (getMaxOffset() - this.mAnimMaxOffset));
            if (this.mSelectedPercent != percent) {
                this.mSelectedPercent = percent;
                for (ActionChooserListener listener : this.mListeners) {
                    listener.onOptionProgress(this.mSelectedPercent);
                }
            }
        }
    }

    @Override // android.view.View
    public void setEnabled(boolean enabled) {
        boolean oldEnabled = isEnabled();
        super.setEnabled(enabled);
        if (oldEnabled != enabled) {
            this.mTouchedEnabled = enabled;
            enableAnimations(enabled, enabled);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enableAnimations(boolean enabled) {
        enableAnimations(enabled, true);
    }

    private void enableAnimations(boolean enabled, boolean immediate) {
        if (enabled) {
            this.mAnimationState = 0;
            if (this.mSelectedOption == null) {
                if (this.mOffset == 0.0f) {
                    this.mIdleAnimatorSet.start();
                    return;
                }
                float currentOffset = getOffset();
                this.mReturnAnimator.setFloatValues(currentOffset, 0.0f);
                this.mReturnAnimator.setDuration(Math.round(Math.abs(currentOffset / this.mIdleAnimationSpeed)));
                this.mReturnAnimator.start();
                return;
            }
            this.mIdleAnimatorSet.cancel();
            this.mCenterAnimator.cancel();
            this.mReturnAnimator.cancel();
            ObjectAnimator objectAnimator = this.mCenterAnimator;
            float[] fArr = new float[2];
            fArr[0] = getOffset();
            fArr[1] = (this.mSelectedOption.intValue() == 2 ? -1 : 1) * getMaxOffset();
            objectAnimator.setFloatValues(fArr);
            this.mCenterAnimator.setDuration(Math.round((Math.abs(getMaxOffset()) - Math.abs(getOffset())) / Math.max(this.mIdleAnimationSpeed, this.mSpeed)));
            this.mCenterAnimator.start();
            return;
        }
        if (immediate) {
            this.mAnimationState = 2;
            this.mIdleAnimatorSet.cancel();
            this.mCenterAnimator.cancel();
            this.mReturnAnimator.cancel();
            return;
        }
        this.mAnimationState = 1;
    }

    private static class Option {
        public int color;
        public Drawable icon;

        public Option(int color, Drawable icon) {
            this.color = color;
            this.icon = icon;
        }
    }
}
