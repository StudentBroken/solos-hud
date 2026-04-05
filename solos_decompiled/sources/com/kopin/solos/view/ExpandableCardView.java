package com.kopin.solos.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.kopin.pupil.ui.PageHelper;
import com.kopin.solos.view.util.Utility;

/* JADX INFO: loaded from: classes48.dex */
public class ExpandableCardView extends FrameLayout {
    private static final long ANIMATION_DURATION = 300;
    private ImageView imgGhost;
    private ImageView imgLaps;
    private View layoutCardImage;
    private CalendarView mCalendar;
    private TextView mCardName;
    private TextView mCardUm;
    private ImageView mChevron;
    private ViewGroup mCollapsedContainer;
    private View mCollapsedView;
    private OnExpandingListener mDefaultListener;
    private MessageView mExpandableViewText;
    private boolean mFirstPreDraw;
    private ViewGroup mFrontContainer;
    private ImageView mImage;
    private ViewGroup mImageContainer;
    private boolean mIsExpanded;
    private boolean mIsExpanding;
    private boolean mLightTheme;
    private LoadingView mLoadingView;
    private ViewGroup mMainContainer;
    private TextView mMetric1;
    private TextView mMetric2;
    private int mOldHeight;
    private ExpandableCardLayout mParent;
    private OnExpandingListener mParentListener;
    private View mProgressBar;
    private TextView mTitle;
    private TextView mTopTitle;
    private TextView mValue1;
    private TextView mValue2;

    public interface LoadingView {
        View getView();

        boolean isReady();

        void onScreen();

        void setParent(ExpandableCardView expandableCardView);

        void setTextColor(int i);
    }

    public interface MessageView {
        View getView();

        void setText(String str);

        void setTextColor(int i);
    }

    public interface OnExpandingListener {
        void onCollapsed();

        void onCollapsing();

        void onExpanded();

        void onExpanding();
    }

    public ExpandableCardView(Context context) {
        this(context, null);
    }

    public ExpandableCardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mIsExpanded = false;
        this.mIsExpanding = false;
        addView(View.inflate(getContext(), R.layout.card_template, null));
        this.mCollapsedContainer = (ViewGroup) findViewById(R.id.card_collapsed_container);
        this.mImageContainer = (ViewGroup) findViewById(R.id.card_image_container);
        this.mMainContainer = (ViewGroup) findViewById(R.id.card_container);
        this.mFrontContainer = (ViewGroup) findViewById(R.id.card_front);
        this.mChevron = (ImageView) findViewById(R.id.card_chevron);
        this.mImage = (ImageView) findViewById(R.id.card_image);
        this.layoutCardImage = findViewById(R.id.layoutCardImage);
        this.mCalendar = (CalendarView) findViewById(R.id.card_calendar);
        this.mCardName = (TextView) findViewById(R.id.card_name);
        this.mCardUm = (TextView) findViewById(R.id.card_um);
        this.mValue1 = (TextView) findViewById(R.id.card_value1);
        this.mValue2 = (TextView) findViewById(R.id.card_value2);
        this.mMetric1 = (TextView) findViewById(R.id.card_metric1);
        this.mMetric2 = (TextView) findViewById(R.id.card_metric2);
        this.mTitle = (TextView) findViewById(R.id.title);
        this.mTopTitle = (TextView) findViewById(R.id.top_title);
        this.imgGhost = (ImageView) findViewById(R.id.imgGhost);
        this.imgLaps = (ImageView) findViewById(R.id.imgLaps);
        this.mProgressBar = findViewById(R.id.progressBar);
        this.mMainContainer.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.view.ExpandableCardView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (ExpandableCardView.this.mParent == null || !ExpandableCardView.this.mParent.isAnimating()) {
                    if (ExpandableCardView.this.isExpanded()) {
                        ExpandableCardView.this.collapse(ExpandableCardView.this.mDefaultListener);
                    } else {
                        ExpandableCardView.this.expand(ExpandableCardView.this.mDefaultListener);
                    }
                }
            }
        });
    }

    public boolean isExpanded() {
        return this.mIsExpanded;
    }

    public void expand(OnExpandingListener listener) {
        if (!this.mIsExpanded && !this.mIsExpanding) {
            this.mIsExpanding = true;
            this.mFirstPreDraw = true;
            if (this.mParentListener != null) {
                this.mParentListener.onExpanding();
            }
            this.mOldHeight = getHeight();
            ViewTreeObserver observer = getViewTreeObserver();
            observer.addOnPreDrawListener(new AnonymousClass2(listener, observer));
            invalidate();
        }
    }

    /* JADX INFO: renamed from: com.kopin.solos.view.ExpandableCardView$2, reason: invalid class name */
    class AnonymousClass2 implements ViewTreeObserver.OnPreDrawListener {
        final /* synthetic */ OnExpandingListener val$listener;
        final /* synthetic */ ViewTreeObserver val$observer;

        AnonymousClass2(OnExpandingListener onExpandingListener, ViewTreeObserver viewTreeObserver) {
            this.val$listener = onExpandingListener;
            this.val$observer = viewTreeObserver;
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            if (ExpandableCardView.this.mFirstPreDraw) {
                ExpandableCardView.this.mFirstPreDraw = false;
                ExpandableCardView.this.mCollapsedContainer.setVisibility(0);
                ExpandableCardView.this.mCollapsedContainer.setAlpha(0.0f);
                ExpandableCardView.this.requestLayout();
                return false;
            }
            if (this.val$listener != null) {
                this.val$listener.onExpanding();
            }
            int newBottom = ExpandableCardView.this.getBottom();
            PropertyValuesHolder top = PropertyValuesHolder.ofInt(PageHelper.ALIGNMENT_TOP, ExpandableCardView.this.getTop(), ExpandableCardView.this.getTop());
            PropertyValuesHolder bottom = PropertyValuesHolder.ofInt(PageHelper.ALIGNMENT_BOTTOM, ExpandableCardView.this.getTop() + ExpandableCardView.this.mOldHeight, newBottom);
            ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(ExpandableCardView.this, top, bottom);
            animator.addListener(new AnimatorListenerAdapter() { // from class: com.kopin.solos.view.ExpandableCardView.2.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animation) {
                    ExpandableCardView.this.mCollapsedContainer.setAlpha(1.0f);
                    ExpandableCardView.this.invalidate();
                    ExpandableCardView.this.mIsExpanded = true;
                    ExpandableCardView.this.mIsExpanding = false;
                    if (ExpandableCardView.this.mCollapsedView != null) {
                        ExpandableCardView.this.mCollapsedView.setFocusable(true);
                        ExpandableCardView.this.mCollapsedView.setFocusableInTouchMode(true);
                        ExpandableCardView.this.mCollapsedView.requestFocus();
                    }
                    if (ExpandableCardView.this.mLoadingView != null && !ExpandableCardView.this.mLoadingView.isReady() && ExpandableCardView.this.setExpandableView(ExpandableCardView.this.mLoadingView.getView())) {
                        ExpandableCardView.this.post(new Runnable() { // from class: com.kopin.solos.view.ExpandableCardView.2.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                ExpandableCardView.this.mLoadingView.onScreen();
                            }
                        });
                    }
                    if (AnonymousClass2.this.val$listener != null) {
                        AnonymousClass2.this.val$listener.onExpanded();
                    }
                    if (ExpandableCardView.this.mParentListener != null) {
                        ExpandableCardView.this.mParentListener.onExpanded();
                    }
                }
            });
            ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(ExpandableCardView.this.mCollapsedContainer, (Property<ViewGroup, Float>) View.ALPHA, 1.0f);
            AnimatorSet set = new AnimatorSet();
            set.playTogether(animator, alphaAnimator);
            set.start();
            RotateAnimation rotateAnim = new RotateAnimation(0.0f, 90.0f, 1, 0.5f, 1, 0.5f);
            rotateAnim.setDuration(ExpandableCardView.ANIMATION_DURATION);
            rotateAnim.setFillAfter(true);
            ExpandableCardView.this.findViewById(R.id.card_chevron).startAnimation(rotateAnim);
            this.val$observer.removeOnPreDrawListener(this);
            return true;
        }
    }

    public void collapse(final OnExpandingListener listener) {
        if (this.mIsExpanded && !this.mIsExpanding) {
            this.mIsExpanding = true;
            this.mFirstPreDraw = true;
            if (this.mParentListener != null) {
                this.mParentListener.onCollapsing();
            }
            final int currentBottom = getBottom();
            final ViewTreeObserver observer = getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: com.kopin.solos.view.ExpandableCardView.3
                @Override // android.view.ViewTreeObserver.OnPreDrawListener
                public boolean onPreDraw() {
                    if (ExpandableCardView.this.mFirstPreDraw) {
                        ExpandableCardView.this.mFirstPreDraw = false;
                        ExpandableCardView.this.mCollapsedContainer.setAlpha(1.0f);
                        ExpandableCardView.this.requestLayout();
                        return false;
                    }
                    if (listener != null) {
                        listener.onCollapsing();
                    }
                    PropertyValuesHolder top = PropertyValuesHolder.ofInt(PageHelper.ALIGNMENT_TOP, ExpandableCardView.this.getTop(), ExpandableCardView.this.getTop());
                    PropertyValuesHolder bottom = PropertyValuesHolder.ofInt(PageHelper.ALIGNMENT_BOTTOM, currentBottom, ExpandableCardView.this.getTop() + ExpandableCardView.this.mOldHeight);
                    ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(ExpandableCardView.this, top, bottom);
                    animator.addListener(new AnimatorListenerAdapter() { // from class: com.kopin.solos.view.ExpandableCardView.3.1
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animation) {
                            ExpandableCardView.this.mCollapsedContainer.setVisibility(8);
                            ExpandableCardView.this.mCollapsedContainer.setAlpha(0.0f);
                            ExpandableCardView.this.requestLayout();
                            ExpandableCardView.this.mIsExpanded = false;
                            ExpandableCardView.this.mIsExpanding = false;
                            if (listener != null) {
                                listener.onCollapsed();
                            }
                            if (ExpandableCardView.this.mParentListener != null) {
                                ExpandableCardView.this.mParentListener.onCollapsed();
                            }
                        }
                    });
                    ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(ExpandableCardView.this.mCollapsedContainer, (Property<ViewGroup, Float>) View.ALPHA, 0.0f);
                    AnimatorSet set = new AnimatorSet();
                    set.playTogether(animator, alphaAnimator);
                    set.start();
                    RotateAnimation rotateAnim = new RotateAnimation(90.0f, 0.0f, 1, 0.5f, 1, 0.5f);
                    rotateAnim.setDuration(ExpandableCardView.ANIMATION_DURATION);
                    rotateAnim.setFillAfter(true);
                    ExpandableCardView.this.findViewById(R.id.card_chevron).startAnimation(rotateAnim);
                    observer.removeOnPreDrawListener(this);
                    return true;
                }
            });
            invalidate();
        }
    }

    public boolean isExpanding() {
        return this.mIsExpanding && !this.mIsExpanded;
    }

    public boolean isCollapsing() {
        return this.mIsExpanding && this.mIsExpanded;
    }

    public int getOriginalHeight() {
        return this.mOldHeight;
    }

    public void setParent(ExpandableCardLayout parent, OnExpandingListener parentListener) {
        this.mParent = parent;
        this.mParentListener = parentListener;
    }

    public void setTheme(boolean light) {
        Drawable background1;
        Drawable background2;
        int tint;
        this.mLightTheme = light;
        if (light) {
            background1 = getResources().getDrawable(R.color.element_background_lighter2);
            background2 = getResources().getDrawable(R.color.element_background_lighter2);
            tint = getResources().getColor(R.color.element_background_light1);
        } else {
            background1 = getResources().getDrawable(R.color.element_background_dark2);
            background2 = getResources().getDrawable(R.color.element_background_dark2);
            tint = getResources().getColor(R.color.element_background_light1);
        }
        this.mImageContainer.setBackground(background2);
        this.mMainContainer.setBackground(background1);
        this.mFrontContainer.setBackground(background1);
        this.mChevron.setColorFilter(tint);
        this.mImage.setColorFilter(tint);
        this.mCalendar.setTint(tint);
        Utility.setTextColor((ViewGroup) this, tint);
        if (this.mLoadingView != null) {
            this.mLoadingView.setTextColor(tint);
        }
        if (this.mExpandableViewText != null) {
            this.mExpandableViewText.setTextColor(tint);
        }
    }

    public void setTextColor(int colorRes) {
        this.mValue1.setTextColor(getResources().getColor(colorRes));
        this.mValue2.setTextColor(getResources().getColor(colorRes));
        this.mMetric1.setTextColor(getResources().getColor(colorRes));
        this.mMetric2.setTextColor(getResources().getColor(colorRes));
        this.mCardName.setTextColor(getResources().getColor(colorRes));
        this.mCardUm.setTextColor(getResources().getColor(colorRes));
        this.mTitle.setTextColor(getResources().getColor(colorRes));
        this.mTopTitle.setTextColor(getResources().getColor(colorRes));
    }

    public void setCollapsedContainerMaxHeight() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
        this.mCollapsedContainer.setLayoutParams(layoutParams);
        invalidate();
    }

    public void setCalendarView(boolean visible) {
        this.mCalendar.setVisibility(visible ? 0 : 8);
        this.layoutCardImage.setVisibility(visible ? 8 : 0);
        this.mImage.setVisibility(visible ? 8 : 0);
    }

    public void setTopTitle(String title) {
        this.mTopTitle.setVisibility(title.isEmpty() ? 8 : 0);
        this.mTopTitle.setText(title);
    }

    public void setCardName(int name) {
        setCardName(getContext().getString(name));
    }

    public void setCardName(int name, int suffix) {
        setCardName(getContext().getString(name), getContext().getString(suffix));
    }

    public void setCardName(String name, String suffix) {
        setCardName(name + " " + suffix);
    }

    public void setCardName(String name) {
        if (name.trim().isEmpty()) {
            this.mCardName.setVisibility(8);
        } else {
            this.mCardName.setVisibility(0);
            this.mCardName.setText(name);
        }
    }

    public void setCardUm(String um) {
        if (um == null || um.length() == 0) {
            this.mCardUm.setVisibility(8);
        } else {
            this.mCardUm.setVisibility(0);
            this.mCardUm.setText(um);
        }
    }

    public void setCardChevronVisible(int visibility) {
        this.mChevron.setVisibility(visibility);
    }

    public void setDate(long date) {
        this.mCalendar.setDate(date);
    }

    public void setImage(int image) {
        this.mImage.setImageResource(0);
        this.mImage.setImageBitmap(null);
        this.mImage.setImageResource(image);
        this.mImage.invalidate();
    }

    public void setImageTint(int colourResource) {
        this.mImage.setColorFilter(getResources().getColor(colourResource), PorterDuff.Mode.MULTIPLY);
    }

    public void setProgressBar(boolean visible) {
        this.mProgressBar.setVisibility(visible ? 0 : 8);
    }

    public void setValue1(String value) {
        this.mValue1.setText(value);
    }

    public void setValue1Size(int size) {
        this.mValue1.setTextSize(2, size);
    }

    public void setValue2(String value) {
        this.mValue2.setText(value);
    }

    public void setValue2Size(int size) {
        this.mValue2.setTextSize(2, size);
    }

    public void setMetric1(String metric) {
        this.mMetric1.setText(metric);
    }

    public void setMetric2(String metric) {
        this.mMetric2.setText(metric);
    }

    public void setMetric1(int metric) {
        this.mMetric1.setText(metric);
    }

    public void setMetric1Size(int size) {
        this.mMetric1.setTextSize(2, size);
    }

    public void setMetric2(int metric) {
        this.mMetric2.setText(metric);
    }

    public void setMetric2Size(int size) {
        this.mMetric2.setTextSize(2, size);
    }

    public void setTitle(String title) {
        this.mTitle.setText(title);
    }

    public boolean setExpandableView(View view) {
        if (this.mCollapsedView == view) {
            return false;
        }
        this.mCollapsedView = view;
        this.mCollapsedContainer.removeAllViews();
        this.mCollapsedContainer.addView(view);
        return true;
    }

    public void setLoadingView(LoadingView loadingView) {
        this.mLoadingView = loadingView;
        this.mLoadingView.setParent(this);
        loadingView.setTextColor(getResources().getColor(R.color.element_background_light1));
    }

    public LoadingView getLoadingView() {
        return this.mLoadingView;
    }

    public void setExpandableViewToLoading() {
        setExpandableViewMessage(getResources().getString(R.string.loading));
    }

    public void setDefaultListener(OnExpandingListener defaultListener) {
        this.mDefaultListener = defaultListener;
    }

    public void disableOnClick() {
        this.mMainContainer.setClickable(false);
        this.mMainContainer.setFocusable(false);
    }

    public void disableOnLongClick() {
        this.mMainContainer.setLongClickable(false);
        this.mMainContainer.setFocusable(false);
    }

    public void onExpandableViewReady() {
        if (this.mIsExpanded && setExpandableView(this.mLoadingView.getView())) {
            post(new Runnable() { // from class: com.kopin.solos.view.ExpandableCardView.4
                @Override // java.lang.Runnable
                public void run() {
                    ExpandableCardView.this.mLoadingView.onScreen();
                }
            });
        }
    }

    public void setExpandableViewMessage(String message) {
        if (this.mExpandableViewText == null) {
            this.mExpandableViewText = new DefaultMessageView(getContext());
        }
        this.mExpandableViewText.setText(message);
        setExpandableView(this.mExpandableViewText.getView());
    }

    public void setExpandableViewMessageView(MessageView messageView) {
        this.mExpandableViewText = messageView;
        setExpandableView(this.mExpandableViewText.getView());
        this.mExpandableViewText.setTextColor(getResources().getColor(R.color.element_background_light1));
    }

    public void setGhostRide(boolean ghost) {
        this.imgGhost.setVisibility(ghost ? 0 : 8);
    }

    public void setWorkoutModeImage(int imageRes) {
        if (imageRes != 0) {
            this.imgGhost.setImageResource(imageRes);
            this.imgGhost.setVisibility(0);
        } else {
            this.imgGhost.setVisibility(8);
        }
    }

    public void recycle() {
    }

    public void setLaps(boolean hasLaps) {
        this.imgLaps.setVisibility(hasLaps ? 0 : 8);
    }

    public static class DefaultMessageView implements MessageView {
        private TextView mTextView;

        public DefaultMessageView(Context context) {
            LayoutInflater inflater = LayoutInflater.from(context);
            this.mTextView = (TextView) inflater.inflate(R.layout.simple_text_view, (ViewGroup) null);
        }

        @Override // com.kopin.solos.view.ExpandableCardView.MessageView
        public View getView() {
            return this.mTextView;
        }

        @Override // com.kopin.solos.view.ExpandableCardView.MessageView
        public void setText(String text) {
            this.mTextView.setText(text);
        }

        @Override // com.kopin.solos.view.ExpandableCardView.MessageView
        public void setTextColor(int color) {
            this.mTextView.setTextColor(color);
        }
    }
}
