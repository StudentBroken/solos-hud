package android.support.v7.widget;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/* JADX INFO: loaded from: classes19.dex */
public class CardView extends FrameLayout {
    private static final int[] COLOR_BACKGROUND_ATTR = {R.attr.colorBackground};
    private static final CardViewImpl IMPL;
    private final CardViewDelegate mCardViewDelegate;
    private boolean mCompatPadding;
    private final Rect mContentPadding;
    private boolean mPreventCornerOverlap;
    private final Rect mShadowBounds;
    private int mUserSetMinHeight;
    private int mUserSetMinWidth;

    static {
        if (Build.VERSION.SDK_INT >= 21) {
            IMPL = new CardViewApi21();
        } else if (Build.VERSION.SDK_INT >= 17) {
            IMPL = new CardViewJellybeanMr1();
        } else {
            IMPL = new CardViewEclairMr1();
        }
        IMPL.initStatic();
    }

    public CardView(Context context) {
        super(context);
        this.mContentPadding = new Rect();
        this.mShadowBounds = new Rect();
        this.mCardViewDelegate = new CardViewDelegate() { // from class: android.support.v7.widget.CardView.1
            private Drawable mCardBackground;

            @Override // android.support.v7.widget.CardViewDelegate
            public void setCardBackground(Drawable drawable) {
                this.mCardBackground = drawable;
                CardView.this.setBackgroundDrawable(drawable);
            }

            @Override // android.support.v7.widget.CardViewDelegate
            public boolean getUseCompatPadding() {
                return CardView.this.getUseCompatPadding();
            }

            @Override // android.support.v7.widget.CardViewDelegate
            public boolean getPreventCornerOverlap() {
                return CardView.this.getPreventCornerOverlap();
            }

            @Override // android.support.v7.widget.CardViewDelegate
            public void setShadowPadding(int left, int top, int right, int bottom) {
                CardView.this.mShadowBounds.set(left, top, right, bottom);
                CardView.super.setPadding(CardView.this.mContentPadding.left + left, CardView.this.mContentPadding.top + top, CardView.this.mContentPadding.right + right, CardView.this.mContentPadding.bottom + bottom);
            }

            @Override // android.support.v7.widget.CardViewDelegate
            public void setMinWidthHeightInternal(int width, int height) {
                if (width > CardView.this.mUserSetMinWidth) {
                    CardView.super.setMinimumWidth(width);
                }
                if (height > CardView.this.mUserSetMinHeight) {
                    CardView.super.setMinimumHeight(height);
                }
            }

            @Override // android.support.v7.widget.CardViewDelegate
            public Drawable getCardBackground() {
                return this.mCardBackground;
            }

            @Override // android.support.v7.widget.CardViewDelegate
            public View getCardView() {
                return CardView.this;
            }
        };
        initialize(context, null, 0);
    }

    public CardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContentPadding = new Rect();
        this.mShadowBounds = new Rect();
        this.mCardViewDelegate = new CardViewDelegate() { // from class: android.support.v7.widget.CardView.1
            private Drawable mCardBackground;

            @Override // android.support.v7.widget.CardViewDelegate
            public void setCardBackground(Drawable drawable) {
                this.mCardBackground = drawable;
                CardView.this.setBackgroundDrawable(drawable);
            }

            @Override // android.support.v7.widget.CardViewDelegate
            public boolean getUseCompatPadding() {
                return CardView.this.getUseCompatPadding();
            }

            @Override // android.support.v7.widget.CardViewDelegate
            public boolean getPreventCornerOverlap() {
                return CardView.this.getPreventCornerOverlap();
            }

            @Override // android.support.v7.widget.CardViewDelegate
            public void setShadowPadding(int left, int top, int right, int bottom) {
                CardView.this.mShadowBounds.set(left, top, right, bottom);
                CardView.super.setPadding(CardView.this.mContentPadding.left + left, CardView.this.mContentPadding.top + top, CardView.this.mContentPadding.right + right, CardView.this.mContentPadding.bottom + bottom);
            }

            @Override // android.support.v7.widget.CardViewDelegate
            public void setMinWidthHeightInternal(int width, int height) {
                if (width > CardView.this.mUserSetMinWidth) {
                    CardView.super.setMinimumWidth(width);
                }
                if (height > CardView.this.mUserSetMinHeight) {
                    CardView.super.setMinimumHeight(height);
                }
            }

            @Override // android.support.v7.widget.CardViewDelegate
            public Drawable getCardBackground() {
                return this.mCardBackground;
            }

            @Override // android.support.v7.widget.CardViewDelegate
            public View getCardView() {
                return CardView.this;
            }
        };
        initialize(context, attrs, 0);
    }

    public CardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContentPadding = new Rect();
        this.mShadowBounds = new Rect();
        this.mCardViewDelegate = new CardViewDelegate() { // from class: android.support.v7.widget.CardView.1
            private Drawable mCardBackground;

            @Override // android.support.v7.widget.CardViewDelegate
            public void setCardBackground(Drawable drawable) {
                this.mCardBackground = drawable;
                CardView.this.setBackgroundDrawable(drawable);
            }

            @Override // android.support.v7.widget.CardViewDelegate
            public boolean getUseCompatPadding() {
                return CardView.this.getUseCompatPadding();
            }

            @Override // android.support.v7.widget.CardViewDelegate
            public boolean getPreventCornerOverlap() {
                return CardView.this.getPreventCornerOverlap();
            }

            @Override // android.support.v7.widget.CardViewDelegate
            public void setShadowPadding(int left, int top, int right, int bottom) {
                CardView.this.mShadowBounds.set(left, top, right, bottom);
                CardView.super.setPadding(CardView.this.mContentPadding.left + left, CardView.this.mContentPadding.top + top, CardView.this.mContentPadding.right + right, CardView.this.mContentPadding.bottom + bottom);
            }

            @Override // android.support.v7.widget.CardViewDelegate
            public void setMinWidthHeightInternal(int width, int height) {
                if (width > CardView.this.mUserSetMinWidth) {
                    CardView.super.setMinimumWidth(width);
                }
                if (height > CardView.this.mUserSetMinHeight) {
                    CardView.super.setMinimumHeight(height);
                }
            }

            @Override // android.support.v7.widget.CardViewDelegate
            public Drawable getCardBackground() {
                return this.mCardBackground;
            }

            @Override // android.support.v7.widget.CardViewDelegate
            public View getCardView() {
                return CardView.this;
            }
        };
        initialize(context, attrs, defStyleAttr);
    }

    @Override // android.view.View
    public void setPadding(int left, int top, int right, int bottom) {
    }

    @Override // android.view.View
    public void setPaddingRelative(int start, int top, int end, int bottom) {
    }

    public boolean getUseCompatPadding() {
        return this.mCompatPadding;
    }

    public void setUseCompatPadding(boolean useCompatPadding) {
        if (this.mCompatPadding != useCompatPadding) {
            this.mCompatPadding = useCompatPadding;
            IMPL.onCompatPaddingChanged(this.mCardViewDelegate);
        }
    }

    public void setContentPadding(int left, int top, int right, int bottom) {
        this.mContentPadding.set(left, top, right, bottom);
        IMPL.updatePadding(this.mCardViewDelegate);
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!(IMPL instanceof CardViewApi21)) {
            int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
            switch (widthMode) {
                case Integer.MIN_VALUE:
                case 1073741824:
                    int minWidth = (int) Math.ceil(IMPL.getMinWidth(this.mCardViewDelegate));
                    widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(Math.max(minWidth, View.MeasureSpec.getSize(widthMeasureSpec)), widthMode);
                    break;
            }
            int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
            switch (heightMode) {
                case Integer.MIN_VALUE:
                case 1073741824:
                    int minHeight = (int) Math.ceil(IMPL.getMinHeight(this.mCardViewDelegate));
                    heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(Math.max(minHeight, View.MeasureSpec.getSize(heightMeasureSpec)), heightMode);
                    break;
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        int backgroundColor;
        TypedArray a = context.obtainStyledAttributes(attrs, android.support.v7.cardview.R.styleable.CardView, defStyleAttr, android.support.v7.cardview.R.style.CardView);
        if (a.hasValue(android.support.v7.cardview.R.styleable.CardView_cardBackgroundColor)) {
            backgroundColor = a.getColor(android.support.v7.cardview.R.styleable.CardView_cardBackgroundColor, 0);
        } else {
            TypedArray aa = getContext().obtainStyledAttributes(COLOR_BACKGROUND_ATTR);
            int themeColorBackground = aa.getColor(0, 0);
            aa.recycle();
            float[] hsv = new float[3];
            Color.colorToHSV(themeColorBackground, hsv);
            backgroundColor = hsv[2] > 0.5f ? getResources().getColor(android.support.v7.cardview.R.color.cardview_light_background) : getResources().getColor(android.support.v7.cardview.R.color.cardview_dark_background);
        }
        float radius = a.getDimension(android.support.v7.cardview.R.styleable.CardView_cardCornerRadius, 0.0f);
        float elevation = a.getDimension(android.support.v7.cardview.R.styleable.CardView_cardElevation, 0.0f);
        float maxElevation = a.getDimension(android.support.v7.cardview.R.styleable.CardView_cardMaxElevation, 0.0f);
        this.mCompatPadding = a.getBoolean(android.support.v7.cardview.R.styleable.CardView_cardUseCompatPadding, false);
        this.mPreventCornerOverlap = a.getBoolean(android.support.v7.cardview.R.styleable.CardView_cardPreventCornerOverlap, true);
        int defaultPadding = a.getDimensionPixelSize(android.support.v7.cardview.R.styleable.CardView_contentPadding, 0);
        this.mContentPadding.left = a.getDimensionPixelSize(android.support.v7.cardview.R.styleable.CardView_contentPaddingLeft, defaultPadding);
        this.mContentPadding.top = a.getDimensionPixelSize(android.support.v7.cardview.R.styleable.CardView_contentPaddingTop, defaultPadding);
        this.mContentPadding.right = a.getDimensionPixelSize(android.support.v7.cardview.R.styleable.CardView_contentPaddingRight, defaultPadding);
        this.mContentPadding.bottom = a.getDimensionPixelSize(android.support.v7.cardview.R.styleable.CardView_contentPaddingBottom, defaultPadding);
        if (elevation > maxElevation) {
            maxElevation = elevation;
        }
        this.mUserSetMinWidth = a.getDimensionPixelSize(android.support.v7.cardview.R.styleable.CardView_android_minWidth, 0);
        this.mUserSetMinHeight = a.getDimensionPixelSize(android.support.v7.cardview.R.styleable.CardView_android_minHeight, 0);
        a.recycle();
        IMPL.initialize(this.mCardViewDelegate, context, backgroundColor, radius, elevation, maxElevation);
    }

    @Override // android.view.View
    public void setMinimumWidth(int minWidth) {
        this.mUserSetMinWidth = minWidth;
        super.setMinimumWidth(minWidth);
    }

    @Override // android.view.View
    public void setMinimumHeight(int minHeight) {
        this.mUserSetMinHeight = minHeight;
        super.setMinimumHeight(minHeight);
    }

    public void setCardBackgroundColor(int color) {
        IMPL.setBackgroundColor(this.mCardViewDelegate, color);
    }

    public int getContentPaddingLeft() {
        return this.mContentPadding.left;
    }

    public int getContentPaddingRight() {
        return this.mContentPadding.right;
    }

    public int getContentPaddingTop() {
        return this.mContentPadding.top;
    }

    public int getContentPaddingBottom() {
        return this.mContentPadding.bottom;
    }

    public void setRadius(float radius) {
        IMPL.setRadius(this.mCardViewDelegate, radius);
    }

    public float getRadius() {
        return IMPL.getRadius(this.mCardViewDelegate);
    }

    public void setCardElevation(float elevation) {
        IMPL.setElevation(this.mCardViewDelegate, elevation);
    }

    public float getCardElevation() {
        return IMPL.getElevation(this.mCardViewDelegate);
    }

    public void setMaxCardElevation(float maxElevation) {
        IMPL.setMaxElevation(this.mCardViewDelegate, maxElevation);
    }

    public float getMaxCardElevation() {
        return IMPL.getMaxElevation(this.mCardViewDelegate);
    }

    public boolean getPreventCornerOverlap() {
        return this.mPreventCornerOverlap;
    }

    public void setPreventCornerOverlap(boolean preventCornerOverlap) {
        if (preventCornerOverlap != this.mPreventCornerOverlap) {
            this.mPreventCornerOverlap = preventCornerOverlap;
            IMPL.onPreventCornerOverlapChanged(this.mCardViewDelegate);
        }
    }
}
