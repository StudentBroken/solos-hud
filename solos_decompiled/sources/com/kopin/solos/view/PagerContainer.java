package com.kopin.solos.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

/* JADX INFO: loaded from: classes48.dex */
public class PagerContainer extends FrameLayout implements ViewPager.OnPageChangeListener {
    private static final float DEFAULT_PAGE_PROPORTION_WIDTH = 0.6f;
    public int currentPageId;
    private int defaultId;
    private final GestureDetector gestureDetector;
    private boolean hasSetupPager;
    private Point mCenter;
    private boolean mEnabled;
    private Point mInitialTouch;
    boolean mNeedsRedraw;
    private ViewPager mPager;
    private PagerInitialised mPagerInitCallback;
    private double mWidthProportion;
    private boolean persist;
    private String prefKey;
    private SharedPreferences preferences;

    public interface PagerInitialised {
        void onEnabled(PagerContainer pagerContainer, boolean z);

        void onInitialised();
    }

    public PagerContainer(Context context) {
        super(context);
        this.mWidthProportion = 0.6000000238418579d;
        this.mNeedsRedraw = false;
        this.mCenter = new Point();
        this.mInitialTouch = new Point();
        this.hasSetupPager = false;
        this.mEnabled = false;
        this.currentPageId = -1;
        this.defaultId = 0;
        this.persist = true;
        this.gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() { // from class: com.kopin.solos.view.PagerContainer.3
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public void onLongPress(MotionEvent e) {
                PagerContainer.this.setEnabled(!PagerContainer.this.isEnabled());
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onSingleTapUp(MotionEvent e) {
                PagerContainer.this.setEnabled(false);
                return true;
            }
        });
        init(context);
    }

    public PagerContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mWidthProportion = 0.6000000238418579d;
        this.mNeedsRedraw = false;
        this.mCenter = new Point();
        this.mInitialTouch = new Point();
        this.hasSetupPager = false;
        this.mEnabled = false;
        this.currentPageId = -1;
        this.defaultId = 0;
        this.persist = true;
        this.gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() { // from class: com.kopin.solos.view.PagerContainer.3
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public void onLongPress(MotionEvent e) {
                PagerContainer.this.setEnabled(!PagerContainer.this.isEnabled());
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onSingleTapUp(MotionEvent e) {
                PagerContainer.this.setEnabled(false);
                return true;
            }
        });
        init(context);
    }

    public PagerContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mWidthProportion = 0.6000000238418579d;
        this.mNeedsRedraw = false;
        this.mCenter = new Point();
        this.mInitialTouch = new Point();
        this.hasSetupPager = false;
        this.mEnabled = false;
        this.currentPageId = -1;
        this.defaultId = 0;
        this.persist = true;
        this.gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() { // from class: com.kopin.solos.view.PagerContainer.3
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public void onLongPress(MotionEvent e) {
                PagerContainer.this.setEnabled(!PagerContainer.this.isEnabled());
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onSingleTapUp(MotionEvent e) {
                PagerContainer.this.setEnabled(false);
                return true;
            }
        });
        init(context);
    }

    private void init(Context context) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
        setClipChildren(false);
        setLayerType(1, null);
        this.gestureDetector.setIsLongpressEnabled(true);
    }

    public void setLongPressEnabled(boolean longPressEnabled) {
        this.gestureDetector.setIsLongpressEnabled(longPressEnabled);
        if (!longPressEnabled && isEnabled()) {
            setEnabled(false);
        }
    }

    public void setPrefKey(String key, int defaultIndex) {
        this.prefKey = key;
        this.defaultId = defaultIndex;
        this.currentPageId = getSelectedId();
    }

    public int getSelectedId() {
        return this.preferences.getInt(this.prefKey, this.defaultId);
    }

    public void setSelectedId(int id) {
        if (this.persist) {
            this.preferences.edit().putInt(this.prefKey, id).apply();
        }
    }

    public void overrideSelection(int id) {
        this.persist = false;
        this.currentPageId = id;
        int i = getViewPager().getAdapter().getItemPosition(Integer.valueOf(id));
        getViewPager().setCurrentItem(resetPosition(i), false);
    }

    public void resetSelection() {
        this.currentPageId = getSelectedId();
        int pos = getViewPager().getAdapter().getItemPosition(Integer.valueOf(this.currentPageId));
        getViewPager().setCurrentItem(pos, false);
        this.persist = true;
    }

    public int resetPosition(int position) {
        return resetPosition(position, getViewPager().getAdapter().getCount());
    }

    public int resetPosition(int position, int numItems) {
        if (position <= 1) {
            return position + numItems;
        }
        if (position >= numItems + 2) {
            return 2;
        }
        return position;
    }

    public void setPagerInitCallback(PagerInitialised initCallback) {
        this.mPagerInitCallback = initCallback;
    }

    public void setPageWidthProportion(double widthProportion) {
        this.mWidthProportion = widthProportion;
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.kopin.solos.view.PagerContainer.1
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                PagerContainer.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                PagerContainer.this.setPagerWidth();
                PagerContainer.this.mPagerInitCallback.onInitialised();
            }
        });
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        try {
            this.mPager = (ViewPager) getChildAt(0);
            this.mPager.setOnPageChangeListener(this);
            this.mPager.setOnTouchListener(new View.OnTouchListener() { // from class: com.kopin.solos.view.PagerContainer.2
                @Override // android.view.View.OnTouchListener
                public boolean onTouch(View v, MotionEvent event) {
                    if (PagerContainer.this.gestureDetector.onTouchEvent(event)) {
                        return true;
                    }
                    if (PagerContainer.this.isEnabled()) {
                        return !PagerContainer.this.mEnabled;
                    }
                    PagerContainer.this.mPagerInitCallback.onEnabled(PagerContainer.this, false);
                    return true;
                }
            });
            super.onFinishInflate();
        } catch (Exception e) {
            throw new IllegalStateException("The root child of PagerContainer must be a ViewPager");
        }
    }

    public ViewPager getViewPager() {
        if (!this.hasSetupPager) {
            this.hasSetupPager = true;
            setPagerWidth();
        }
        return this.mPager;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setPagerWidth() {
        ViewGroup.LayoutParams params = this.mPager.getLayoutParams();
        params.width = (int) (((double) getWidth()) * this.mWidthProportion);
        this.mPager.setLayoutParams(params);
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.mCenter.x = w / 2;
        this.mCenter.y = h / 2;
    }

    @Override // android.view.View
    public void setEnabled(boolean enabled) {
        this.mEnabled = enabled;
        this.mPagerInitCallback.onEnabled(this, enabled);
        setPagerWidth();
        invalidate();
    }

    public void refresh() {
        setPagerWidth();
        invalidate();
        getViewPager().invalidate();
    }

    @Override // android.view.View
    public boolean isEnabled() {
        return this.mPager.getAdapter().getCount() > 1;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case 0:
                this.mInitialTouch.x = (int) ev.getX();
                this.mInitialTouch.y = (int) ev.getY();
                if (!isEnabled()) {
                    this.mPagerInitCallback.onEnabled(this, false);
                }
                break;
        }
        ev.offsetLocation(this.mCenter.x - this.mInitialTouch.x, this.mCenter.y - this.mInitialTouch.y);
        return this.mPager.dispatchTouchEvent(ev);
    }

    @Override // android.support.v4.view.ViewPager.OnPageChangeListener
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (this.mNeedsRedraw) {
            invalidate();
        }
    }

    @Override // android.support.v4.view.ViewPager.OnPageChangeListener
    public void onPageSelected(int position) {
    }

    @Override // android.support.v4.view.ViewPager.OnPageChangeListener
    public void onPageScrollStateChanged(int state) {
        this.mNeedsRedraw = state != 0;
    }
}
