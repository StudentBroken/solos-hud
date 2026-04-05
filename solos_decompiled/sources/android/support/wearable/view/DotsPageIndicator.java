package android.support.wearable.view;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.support.wearable.R;
import android.support.wearable.view.GridViewPager;
import android.util.AttributeSet;
import android.view.View;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(20)
@Deprecated
public class DotsPageIndicator extends View implements GridViewPager.OnPageChangeListener, GridViewPager.OnAdapterChangeListener {
    private static final String TAG = "Dots";
    private GridPagerAdapter mAdapter;
    private GridViewPager.OnAdapterChangeListener mAdapterChangeListener;
    private int mColumnCount;
    private int mCurrentState;
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
    private GridViewPager.OnPageChangeListener mPageChangeListener;
    private GridViewPager mPager;
    private int mSelectedColumn;
    private int mSelectedRow;
    private boolean mVisible;

    public DotsPageIndicator(Context context) {
        this(context, null);
    }

    public DotsPageIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotsPageIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DotsPageIndicator, 0, R.style.DotsPageIndicatorStyle);
        this.mDotSpacing = a.getDimensionPixelOffset(R.styleable.DotsPageIndicator_dotSpacing, 0);
        this.mDotRadius = a.getDimension(R.styleable.DotsPageIndicator_dotRadius, 0.0f);
        this.mDotRadiusSelected = a.getDimension(R.styleable.DotsPageIndicator_dotRadiusSelected, 0.0f);
        this.mDotColor = a.getColor(R.styleable.DotsPageIndicator_dotColor, 0);
        this.mDotColorSelected = a.getColor(R.styleable.DotsPageIndicator_dotColorSelected, 0);
        this.mDotFadeOutDelay = a.getInt(R.styleable.DotsPageIndicator_dotFadeOutDelay, 0);
        this.mDotFadeOutDuration = a.getInt(R.styleable.DotsPageIndicator_dotFadeOutDuration, 0);
        this.mDotFadeInDuration = a.getInt(R.styleable.DotsPageIndicator_dotFadeInDuration, 0);
        this.mDotFadeWhenIdle = a.getBoolean(R.styleable.DotsPageIndicator_dotFadeWhenIdle, false);
        this.mDotShadowDx = a.getDimension(R.styleable.DotsPageIndicator_dotShadowDx, 0.0f);
        this.mDotShadowDy = a.getDimension(R.styleable.DotsPageIndicator_dotShadowDy, 0.0f);
        this.mDotShadowRadius = a.getDimension(R.styleable.DotsPageIndicator_dotShadowRadius, 0.0f);
        this.mDotShadowColor = a.getColor(R.styleable.DotsPageIndicator_dotShadowColor, 0);
        a.recycle();
        this.mDotPaint = new Paint(1);
        this.mDotPaint.setColor(this.mDotColor);
        this.mDotPaint.setStyle(Paint.Style.FILL);
        this.mDotPaintSelected = new Paint(1);
        this.mDotPaintSelected.setColor(this.mDotColorSelected);
        this.mDotPaintSelected.setStyle(Paint.Style.FILL);
        this.mDotPaintShadow = new Paint(1);
        this.mDotPaintShadowSelected = new Paint(1);
        this.mCurrentState = 0;
        if (isInEditMode()) {
            this.mColumnCount = 5;
            this.mSelectedColumn = 2;
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

    public void setPager(GridViewPager pager) {
        if (this.mPager != pager) {
            if (this.mPager != null) {
                this.mPager.setOnPageChangeListener(null);
                this.mPager.setOnAdapterChangeListener(null);
                this.mPager = null;
            }
            this.mPager = pager;
            if (this.mPager != null) {
                this.mPager.setOnPageChangeListener(this);
                this.mPager.setOnAdapterChangeListener(this);
                this.mAdapter = this.mPager.getAdapter();
            }
        }
        if (this.mAdapter != null && this.mAdapter.getRowCount() > 0) {
            rowChanged(0, 0);
        }
    }

    public void setOnPageChangeListener(GridViewPager.OnPageChangeListener listener) {
        this.mPageChangeListener = listener;
    }

    public void setOnAdapterChangeListener(GridViewPager.OnAdapterChangeListener listener) {
        this.mAdapterChangeListener = listener;
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

    private void columnChanged(int column) {
        this.mSelectedColumn = column;
        invalidate();
    }

    private void rowChanged(int row, int column) {
        this.mSelectedRow = row;
        int count = this.mAdapter.getColumnCount(row);
        if (count != this.mColumnCount) {
            this.mColumnCount = count;
            this.mSelectedColumn = column;
            requestLayout();
        } else if (column != this.mSelectedColumn) {
            this.mSelectedColumn = column;
            invalidate();
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
        animate().alpha(1.0f).setStartDelay(0L).setDuration(this.mDotFadeInDuration).setListener(new SimpleAnimatorListener() { // from class: android.support.wearable.view.DotsPageIndicator.1
            @Override // android.support.wearable.view.SimpleAnimatorListener
            public void onAnimationComplete(Animator animator) {
                DotsPageIndicator.this.mVisible = false;
                DotsPageIndicator.this.animate().alpha(0.0f).setListener(null).setStartDelay(DotsPageIndicator.this.mDotFadeOutDelay).setDuration(DotsPageIndicator.this.mDotFadeOutDuration).start();
            }
        }).start();
    }

    @Override // android.support.wearable.view.GridViewPager.OnPageChangeListener
    public void onPageScrollStateChanged(int state) {
        if (this.mCurrentState != state) {
            this.mCurrentState = state;
            if (this.mDotFadeWhenIdle && state == 0) {
                if (this.mVisible) {
                    fadeOut(this.mDotFadeOutDelay);
                } else {
                    fadeInOut();
                }
            }
        }
        if (this.mPageChangeListener != null) {
            this.mPageChangeListener.onPageScrollStateChanged(state);
        }
    }

    @Override // android.support.wearable.view.GridViewPager.OnPageChangeListener
    public void onPageScrolled(int row, int column, float rowOffset, float columnOffset, int rowOffsetPixels, int columnOffsetPixels) {
        if (this.mDotFadeWhenIdle && this.mCurrentState == 1) {
            if (columnOffset != 0.0f) {
                if (!this.mVisible) {
                    fadeIn();
                }
            } else if (this.mVisible) {
                fadeOut(0L);
            }
        }
        if (this.mPageChangeListener != null) {
            this.mPageChangeListener.onPageScrolled(row, column, rowOffset, columnOffset, rowOffsetPixels, columnOffsetPixels);
        }
    }

    @Override // android.support.wearable.view.GridViewPager.OnPageChangeListener
    public void onPageSelected(int row, int column) {
        if (row != this.mSelectedRow) {
            rowChanged(row, column);
        } else if (column != this.mSelectedColumn) {
            columnChanged(column);
        }
        if (this.mPageChangeListener != null) {
            this.mPageChangeListener.onPageSelected(row, column);
        }
    }

    @Override // android.support.wearable.view.GridViewPager.OnAdapterChangeListener
    public void onAdapterChanged(GridPagerAdapter oldAdapter, GridPagerAdapter newAdapter) {
        this.mAdapter = newAdapter;
        if (this.mAdapter != null) {
            rowChanged(0, 0);
            if (this.mDotFadeWhenIdle) {
                fadeInOut();
            }
        }
        if (this.mAdapterChangeListener != null) {
            this.mAdapterChangeListener.onAdapterChanged(oldAdapter, newAdapter);
        }
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int totalWidth;
        int totalHeight;
        if (View.MeasureSpec.getMode(widthMeasureSpec) == 1073741824) {
            totalWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        } else {
            int contentWidth = this.mColumnCount * this.mDotSpacing;
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
        if (this.mColumnCount > 1) {
            float dotCenterLeft = getPaddingLeft() + (this.mDotSpacing / 2.0f);
            float dotCenterTop = getHeight() / 2.0f;
            canvas.save();
            canvas.translate(dotCenterLeft, dotCenterTop);
            for (int i = 0; i < this.mColumnCount; i++) {
                if (i == this.mSelectedColumn) {
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

    @Override // android.support.wearable.view.GridViewPager.OnAdapterChangeListener
    public void onDataSetChanged() {
        if (this.mAdapter != null && this.mAdapter.getRowCount() > 0) {
            rowChanged(0, 0);
        }
        if (this.mAdapterChangeListener != null) {
            this.mAdapterChangeListener.onDataSetChanged();
        }
    }
}
