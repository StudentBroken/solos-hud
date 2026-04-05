package android.support.wearable.view.drawer;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.wearable.R;
import android.support.wearable.view.SimpleAnimatorListener;
import android.util.AttributeSet;
import android.view.View;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(23)
public class PageIndicatorView extends View implements ViewPager.OnPageChangeListener {
    private static final String TAG = "Dots";
    private PagerAdapter mAdapter;
    private int mCurrentViewPagerState;
    private int mDotColor;
    private int mDotColorSelected;
    private int mDotFadeInDuration;
    private int mDotFadeOutDelay;
    private int mDotFadeOutDuration;
    private boolean mDotFadeWhenIdle;
    private final Paint mDotPaint;
    private final Paint mDotPaintSelected;
    private final Paint mDotPaintShadow;
    private final Paint mDotPaintShadowSelected;
    private float mDotRadius;
    private float mDotRadiusSelected;
    private int mDotShadowColor;
    private float mDotShadowDx;
    private float mDotShadowDy;
    private float mDotShadowRadius;
    private int mDotSpacing;
    private int mNumberOfPositions;
    private int mSelectedPosition;
    private boolean mVisible;

    public PageIndicatorView(Context context) {
        this(context, null);
    }

    public PageIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PageIndicatorView, defStyleAttr, R.style.PageIndicatorViewStyle);
        this.mDotSpacing = a.getDimensionPixelOffset(R.styleable.PageIndicatorView_pageIndicatorDotSpacing, 0);
        this.mDotRadius = a.getDimension(R.styleable.PageIndicatorView_pageIndicatorDotRadius, 0.0f);
        this.mDotRadiusSelected = a.getDimension(R.styleable.PageIndicatorView_pageIndicatorDotRadiusSelected, 0.0f);
        this.mDotColor = a.getColor(R.styleable.PageIndicatorView_pageIndicatorDotColor, 0);
        this.mDotColorSelected = a.getColor(R.styleable.PageIndicatorView_pageIndicatorDotColorSelected, 0);
        this.mDotFadeOutDelay = a.getInt(R.styleable.PageIndicatorView_pageIndicatorDotFadeOutDelay, 0);
        this.mDotFadeOutDuration = a.getInt(R.styleable.PageIndicatorView_pageIndicatorDotFadeOutDuration, 0);
        this.mDotFadeInDuration = a.getInt(R.styleable.PageIndicatorView_pageIndicatorDotFadeInDuration, 0);
        this.mDotFadeWhenIdle = a.getBoolean(R.styleable.PageIndicatorView_pageIndicatorDotFadeWhenIdle, false);
        this.mDotShadowDx = a.getDimension(R.styleable.PageIndicatorView_pageIndicatorDotShadowDx, 0.0f);
        this.mDotShadowDy = a.getDimension(R.styleable.PageIndicatorView_pageIndicatorDotShadowDy, 0.0f);
        this.mDotShadowRadius = a.getDimension(R.styleable.PageIndicatorView_pageIndicatorDotShadowRadius, 0.0f);
        this.mDotShadowColor = a.getColor(R.styleable.PageIndicatorView_pageIndicatorDotShadowColor, 0);
        a.recycle();
        this.mDotPaint = new Paint(1);
        this.mDotPaint.setColor(this.mDotColor);
        this.mDotPaint.setStyle(Paint.Style.FILL);
        this.mDotPaintSelected = new Paint(1);
        this.mDotPaintSelected.setColor(this.mDotColorSelected);
        this.mDotPaintSelected.setStyle(Paint.Style.FILL);
        this.mDotPaintShadow = new Paint(1);
        this.mDotPaintShadowSelected = new Paint(1);
        this.mCurrentViewPagerState = 0;
        if (isInEditMode()) {
            this.mNumberOfPositions = 5;
            this.mSelectedPosition = 2;
            this.mDotFadeWhenIdle = false;
        }
        if (this.mDotFadeWhenIdle) {
            this.mVisible = false;
            animate().alpha(0.0f).setStartDelay(2000L).setDuration(this.mDotFadeOutDuration).start();
        } else {
            animate().cancel();
            setAlpha(1.0f);
        }
        updateShadows();
    }

    private void updateShadows() {
        updateDotPaint(this.mDotPaint, this.mDotPaintShadow, this.mDotRadius, this.mDotShadowRadius, this.mDotColor, this.mDotShadowColor);
        updateDotPaint(this.mDotPaintSelected, this.mDotPaintShadowSelected, this.mDotRadiusSelected, this.mDotShadowRadius, this.mDotColorSelected, this.mDotShadowColor);
    }

    private void updateDotPaint(Paint dotPaint, Paint shadowPaint, float baseRadius, float shadowRadius, int color, int shadowColor) {
        float radius = baseRadius + shadowRadius;
        float shadowStart = baseRadius / radius;
        Shader gradient = new RadialGradient(0.0f, 0.0f, radius, new int[]{shadowColor, shadowColor, 0}, new float[]{0.0f, shadowStart, 1.0f}, Shader.TileMode.CLAMP);
        shadowPaint.setShader(gradient);
        dotPaint.setColor(color);
        dotPaint.setStyle(Paint.Style.FILL);
    }

    public void setPager(ViewPager pager) {
        pager.addOnPageChangeListener(this);
        setPagerAdapter(pager.getAdapter());
        this.mAdapter = pager.getAdapter();
        if (this.mAdapter != null && this.mAdapter.getCount() > 0) {
            positionChanged(0);
        }
    }

    public float getDotSpacing() {
        return this.mDotSpacing;
    }

    public void setDotSpacing(int spacing) {
        if (this.mDotSpacing != spacing) {
            this.mDotSpacing = spacing;
            requestLayout();
        }
    }

    public float getDotRadius() {
        return this.mDotRadius;
    }

    public void setDotRadius(int radius) {
        if (this.mDotRadius != radius) {
            this.mDotRadius = radius;
            updateShadows();
            invalidate();
        }
    }

    public float getDotRadiusSelected() {
        return this.mDotRadiusSelected;
    }

    public void setDotRadiusSelected(int radius) {
        if (this.mDotRadiusSelected != radius) {
            this.mDotRadiusSelected = radius;
            updateShadows();
            invalidate();
        }
    }

    public int getDotColor() {
        return this.mDotColor;
    }

    public void setDotColor(int color) {
        if (this.mDotColor != color) {
            this.mDotColor = color;
            invalidate();
        }
    }

    public int getDotColorSelected() {
        return this.mDotColorSelected;
    }

    public void setDotColorSelected(int color) {
        if (this.mDotColorSelected != color) {
            this.mDotColorSelected = color;
            invalidate();
        }
    }

    public boolean getDotFadeWhenIdle() {
        return this.mDotFadeWhenIdle;
    }

    public void setDotFadeWhenIdle(boolean fade) {
        this.mDotFadeWhenIdle = fade;
        if (!fade) {
            fadeIn();
        }
    }

    public int getDotFadeOutDuration() {
        return this.mDotFadeOutDuration;
    }

    public void setDotFadeOutDuration(int duration, TimeUnit unit) {
        this.mDotFadeOutDuration = (int) TimeUnit.MILLISECONDS.convert(duration, unit);
    }

    public int getDotFadeInDuration() {
        return this.mDotFadeInDuration;
    }

    public void setDotFadeInDuration(int duration, TimeUnit unit) {
        this.mDotFadeInDuration = (int) TimeUnit.MILLISECONDS.convert(duration, unit);
    }

    public int getDotFadeOutDelay() {
        return this.mDotFadeOutDelay;
    }

    public void setDotFadeOutDelay(int delay) {
        this.mDotFadeOutDelay = delay;
    }

    public float getDotShadowRadius() {
        return this.mDotShadowRadius;
    }

    public void setDotShadowRadius(float radius) {
        if (this.mDotShadowRadius != radius) {
            this.mDotShadowRadius = radius;
            updateShadows();
            invalidate();
        }
    }

    public float getDotShadowDx() {
        return this.mDotShadowDx;
    }

    public void setDotShadowDx(float dx) {
        this.mDotShadowDx = dx;
        invalidate();
    }

    public float getDotShadowDy() {
        return this.mDotShadowDy;
    }

    public void setDotShadowDy(float dy) {
        this.mDotShadowDy = dy;
        invalidate();
    }

    public int getDotShadowColor() {
        return this.mDotShadowColor;
    }

    public void setDotShadowColor(int color) {
        this.mDotShadowColor = color;
        updateShadows();
        invalidate();
    }

    private void positionChanged(int position) {
        this.mSelectedPosition = position;
        invalidate();
    }

    private void updateNumberOfPositions() {
        int count = this.mAdapter.getCount();
        if (count != this.mNumberOfPositions) {
            this.mNumberOfPositions = count;
            requestLayout();
        }
    }

    private void fadeIn() {
        this.mVisible = true;
        animate().cancel();
        animate().alpha(1.0f).setStartDelay(0L).setDuration(this.mDotFadeInDuration).start();
    }

    private void fadeOut(long delayMillis) {
        this.mVisible = false;
        animate().cancel();
        animate().alpha(0.0f).setStartDelay(delayMillis).setDuration(this.mDotFadeOutDuration).start();
    }

    private void fadeInOut() {
        this.mVisible = true;
        animate().cancel();
        animate().alpha(1.0f).setStartDelay(0L).setDuration(this.mDotFadeInDuration).setListener(new SimpleAnimatorListener() { // from class: android.support.wearable.view.drawer.PageIndicatorView.1
            @Override // android.support.wearable.view.SimpleAnimatorListener
            public void onAnimationComplete(Animator animator) {
                PageIndicatorView.this.mVisible = false;
                PageIndicatorView.this.animate().alpha(0.0f).setListener(null).setStartDelay(PageIndicatorView.this.mDotFadeOutDelay).setDuration(PageIndicatorView.this.mDotFadeOutDuration).start();
            }
        }).start();
    }

    @Override // android.support.v4.view.ViewPager.OnPageChangeListener
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (this.mDotFadeWhenIdle && this.mCurrentViewPagerState == 1) {
            if (positionOffset != 0.0f) {
                if (!this.mVisible) {
                    fadeIn();
                }
            } else if (this.mVisible) {
                fadeOut(0L);
            }
        }
    }

    @Override // android.support.v4.view.ViewPager.OnPageChangeListener
    public void onPageSelected(int position) {
        if (position != this.mSelectedPosition) {
            positionChanged(position);
        }
    }

    @Override // android.support.v4.view.ViewPager.OnPageChangeListener
    public void onPageScrollStateChanged(int state) {
        if (this.mCurrentViewPagerState != state) {
            this.mCurrentViewPagerState = state;
            if (this.mDotFadeWhenIdle && state == 0) {
                if (this.mVisible) {
                    fadeOut(this.mDotFadeOutDelay);
                } else {
                    fadeInOut();
                }
            }
        }
    }

    public void setPagerAdapter(PagerAdapter adapter) {
        this.mAdapter = adapter;
        if (this.mAdapter != null) {
            updateNumberOfPositions();
            if (this.mDotFadeWhenIdle) {
                fadeInOut();
            }
        }
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int totalWidth;
        int totalHeight;
        if (View.MeasureSpec.getMode(widthMeasureSpec) == 1073741824) {
            totalWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        } else {
            int contentWidth = this.mNumberOfPositions * this.mDotSpacing;
            totalWidth = getPaddingLeft() + contentWidth + getPaddingRight();
        }
        if (View.MeasureSpec.getMode(heightMeasureSpec) == 1073741824) {
            totalHeight = View.MeasureSpec.getSize(heightMeasureSpec);
        } else {
            float maxRadius = Math.max(this.mDotRadius + this.mDotShadowRadius, this.mDotRadiusSelected + this.mDotShadowRadius);
            int contentHeight = (int) Math.ceil(2.0f * maxRadius);
            totalHeight = getPaddingTop() + ((int) (contentHeight + this.mDotShadowDy)) + getPaddingBottom();
        }
        setMeasuredDimension(resolveSizeAndState(totalWidth, widthMeasureSpec, 0), resolveSizeAndState(totalHeight, heightMeasureSpec, 0));
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mNumberOfPositions > 1) {
            float dotCenterLeft = getPaddingLeft() + (this.mDotSpacing / 2.0f);
            float dotCenterTop = getHeight() / 2.0f;
            canvas.save();
            canvas.translate(dotCenterLeft, dotCenterTop);
            for (int i = 0; i < this.mNumberOfPositions; i++) {
                if (i == this.mSelectedPosition) {
                    float radius = this.mDotRadiusSelected + this.mDotShadowRadius;
                    canvas.drawCircle(this.mDotShadowDx, this.mDotShadowDy, radius, this.mDotPaintShadowSelected);
                    canvas.drawCircle(0.0f, 0.0f, this.mDotRadiusSelected, this.mDotPaintSelected);
                } else {
                    float radius2 = this.mDotRadius + this.mDotShadowRadius;
                    canvas.drawCircle(this.mDotShadowDx, this.mDotShadowDy, radius2, this.mDotPaintShadow);
                    canvas.drawCircle(0.0f, 0.0f, this.mDotRadius, this.mDotPaint);
                }
                canvas.translate(this.mDotSpacing, 0.0f);
            }
            canvas.restore();
        }
    }

    public void notifyDataSetChanged() {
        if (this.mAdapter != null && this.mAdapter.getCount() > 0) {
            updateNumberOfPositions();
        }
    }
}
