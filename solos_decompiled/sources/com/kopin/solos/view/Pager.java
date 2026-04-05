package com.kopin.solos.view;

import android.content.Context;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/* JADX INFO: loaded from: classes48.dex */
public class Pager extends HorizontalScrollView {
    private static final boolean CONTINUOUS_SCROLL = true;
    private static final double PAGE_PROPORTION = 0.6d;
    private static final double PAGE_SPACER_PROPORTION = 0.2d;
    private static final boolean SHOW_ONE_PAGE_PER_VIEW = false;
    private final LinearLayout contents;
    private final List<Page> excludedPages;
    private final GestureDetector gestureDetector;
    private final List<OnPagerListener> listeners;
    private boolean mEnabled;
    private int mId;
    private WeakReference<View> mLeftEar;
    private WeakReference<View> mRightEar;
    private final SortedSet<Page> pages;
    private boolean showOnePagePerView;

    public interface OnPagerListener {
        void onEnabled(Pager pager);

        void onPageChange(Pager pager, int i);
    }

    public Pager(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        this.showOnePagePerView = true;
        this.listeners = new ArrayList();
        this.mEnabled = true;
        this.pages = new TreeSet();
        this.excludedPages = new ArrayList();
        this.mId = -1;
        this.gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() { // from class: com.kopin.solos.view.Pager.1
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public void onLongPress(MotionEvent e) {
                Pager.this.setEnabled(!Pager.this.isEnabled());
            }
        });
        this.contents = new LinearLayout(ctx);
        this.contents.setLayoutParams(new FrameLayout.LayoutParams(-2, -1));
        addView(this.contents);
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
    }

    @Override // android.widget.HorizontalScrollView, android.widget.FrameLayout, android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int len = this.contents.getChildCount();
        for (int i = 0; i < len; i++) {
            View child = this.contents.getChildAt(i);
            if (child.getLayoutParams().width != specSize) {
                child.setLayoutParams(new LinearLayout.LayoutParams(getPageWidth(specSize), -2));
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override // android.widget.HorizontalScrollView, android.view.View
    protected float getLeftFadingEdgeStrength() {
        return 0.0f;
    }

    @Override // android.widget.HorizontalScrollView, android.view.View
    protected float getRightFadingEdgeStrength() {
        return 0.0f;
    }

    public void addPage(View child) {
        if (this.contents.getChildCount() == 0) {
            this.mId = 0;
        }
        child.setTag(Integer.valueOf(this.contents.getChildCount()));
        this.pages.add(new Page(this.contents.getChildCount(), child));
        child.setLayoutParams(new FrameLayout.LayoutParams(getPageWidth(), -1));
        this.contents.addView(child);
        this.contents.requestLayout();
    }

    public void addPages(List<View> children) {
        int i = 0;
        for (View child : children) {
            child.setTag(Integer.valueOf(i));
            child.setLayoutParams(new FrameLayout.LayoutParams(getPageWidth(), -1));
            this.pages.add(new Page(i, child));
            this.contents.addView(child);
            i++;
        }
        this.mId = 0;
        this.contents.scrollBy(children.size() * getPageWidth(), 0);
        this.contents.requestLayout();
    }

    private int getPageWidth() {
        return getPageWidth(getWidth());
    }

    private int getPageWidth(int viewWidth) {
        return this.showOnePagePerView ? viewWidth : (int) (((double) viewWidth) * PAGE_PROPORTION);
    }

    private int getPageOffset() {
        if (this.showOnePagePerView) {
            return 0;
        }
        return (int) (((double) getWidth()) * PAGE_SPACER_PROPORTION);
    }

    private class Page implements Comparable<Page> {
        public Integer id;
        public View view;

        public Page(int i, View v) {
            this.id = -1;
            this.id = Integer.valueOf(i);
            this.view = v;
        }

        @Override // java.lang.Comparable
        public int compareTo(Page p) {
            return this.id.compareTo(p.id);
        }
    }

    public void hidePages(List<Integer> hiddenIds) {
        List<Page> _pages = new ArrayList<>();
        _pages.addAll(this.pages);
        synchronized (this.excludedPages) {
            for (Page page : _pages) {
                if (hiddenIds.contains(page.id)) {
                    this.excludedPages.add(page);
                    this.pages.remove(page);
                    this.contents.removeView(page.view);
                }
            }
            _pages.clear();
            _pages.addAll(this.excludedPages);
            for (Page page2 : _pages) {
                if (!hiddenIds.contains(page2.id)) {
                    this.excludedPages.remove(page2);
                    this.pages.add(page2);
                    this.contents.addView(page2.view, getPageIndex(page2.id.intValue()));
                }
            }
        }
    }

    private int getPageIndex(int visiblePageId) {
        for (Page page : this.pages) {
            if (page.id.intValue() == visiblePageId) {
                return this.contents.indexOfChild(page.view);
            }
        }
        Log.e("Pager", "Could not find visible page id: " + visiblePageId);
        return -1;
    }

    @Override // android.widget.HorizontalScrollView, android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        if (!this.gestureDetector.onTouchEvent(event) && this.mEnabled) {
            boolean zOnTouchEvent = super.onTouchEvent(event);
            if (event.getAction() == 1) {
                int width = getPageWidth();
                int index = (getScrollX() + (width / 2)) / width;
                updatePage(scroll(index, true), true);
                return zOnTouchEvent;
            }
            return zOnTouchEvent;
        }
        return true;
    }

    private int scroll(int index, boolean animate) {
        this.mId = ((Integer) this.contents.getChildAt(index).getTag()).intValue();
        if (index <= 1) {
            View child = this.contents.getChildAt(this.contents.getChildCount() - 1);
            this.contents.removeView(child);
            this.contents.addView(child, 0);
            View child2 = this.contents.getChildAt(this.contents.getChildCount() - 1);
            this.contents.removeView(child2);
            this.contents.addView(child2, 0);
            index += 2;
        } else if (index > this.contents.getChildCount() - 3) {
            View child3 = this.contents.getChildAt(0);
            this.contents.removeView(child3);
            this.contents.addView(child3);
            View child4 = this.contents.getChildAt(0);
            this.contents.removeView(child4);
            this.contents.addView(child4);
            index -= 2;
        }
        int width = getWidth();
        int pos = index * getPageWidth(width);
        if (!this.showOnePagePerView && index > 0) {
            pos -= getPageOffset();
        }
        if (animate) {
            smoothScrollTo(pos, 0);
        } else {
            scrollTo(pos, 0);
        }
        return index;
    }

    public void moveNext() {
        int index = getCurrentPageIndex() + 1;
        updatePage(scroll(index, true), true);
    }

    public void movePrevious() {
        int index = getCurrentPageIndex() - 1;
        if (index >= 0) {
            updatePage(scroll(index, true), true);
        }
    }

    public void setPage(int index, boolean notify) {
        updatePage(scroll(index, false), notify);
    }

    public boolean hasPage(View v) {
        return this.contents.indexOfChild(v) != -1;
    }

    public void removePage(View v) {
        this.contents.removeView(v);
    }

    public int pageIndex(View v) {
        return this.contents.indexOfChild(v);
    }

    public int getCurrentPageIndex() {
        for (Page page : this.pages) {
            if (page.id.intValue() == this.mId) {
                return this.contents.indexOfChild(page.view);
            }
        }
        return -1;
    }

    public int getCurrentPageId() {
        Log.d("Pager", "getCurrentPageId " + this.mId);
        return this.mId;
    }

    private int getPageId(int index) {
        int i = 0;
        for (Page page : this.pages) {
            if (i == index) {
                Log.e("Pager", "getPageId " + page.id);
                return page.id.intValue();
            }
            i++;
        }
        Log.e("Pager", "getPageId not found ");
        return -1;
    }

    public int getPageCount() {
        return this.contents.getChildCount();
    }

    public void removeAllPages() {
        this.contents.removeAllViews();
    }

    public void addOnPageChangedListener(OnPagerListener onPageChangedListener) {
        if (onPageChangedListener != null) {
            this.listeners.add(onPageChangedListener);
        }
    }

    public void removeOnPageChangedListener(OnPagerListener onPageChangedListener) {
        if (onPageChangedListener != null) {
            this.listeners.remove(onPageChangedListener);
        }
    }

    public void setEars(View leftEar, View rightEar) {
        this.mLeftEar = new WeakReference<>(leftEar);
        this.mRightEar = new WeakReference<>(rightEar);
    }

    private void updatePage(int index, boolean notify) {
    }

    @Override // android.view.View
    public void setEnabled(boolean enabled) {
        if (this.mEnabled != enabled) {
            this.mEnabled = enabled;
            this.showOnePagePerView = !this.mEnabled;
            if (this.mEnabled) {
                Vibrator vib = (Vibrator) getContext().getSystemService("vibrator");
                if (vib != null) {
                    vib.vibrate(300L);
                }
                for (OnPagerListener listener : this.listeners) {
                    listener.onEnabled(this);
                }
            }
            this.contents.requestLayout();
            scroll(getCurrentPageIndex(), false);
        }
    }

    @Override // android.view.View
    public boolean isEnabled() {
        return this.mEnabled;
    }
}
