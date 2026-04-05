package com.kopin.solos.view;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import com.kopin.pupil.ui.PageHelper;
import com.kopin.solos.view.ExpandableCardView;

/* JADX INFO: loaded from: classes48.dex */
public class ExpandableCardLayout extends LinearLayout {
    private int mAnimationCount;
    private ExpandableCardView.OnExpandingListener mExpandingListener;
    private final Object mLock;

    static /* synthetic */ int access$208(ExpandableCardLayout x0) {
        int i = x0.mAnimationCount;
        x0.mAnimationCount = i + 1;
        return i;
    }

    static /* synthetic */ int access$210(ExpandableCardLayout x0) {
        int i = x0.mAnimationCount;
        x0.mAnimationCount = i - 1;
        return i;
    }

    public ExpandableCardLayout(Context context) {
        this(context, null);
    }

    public ExpandableCardLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableCardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mLock = new Object();
        this.mExpandingListener = new ExpandableCardView.OnExpandingListener() { // from class: com.kopin.solos.view.ExpandableCardLayout.4
            @Override // com.kopin.solos.view.ExpandableCardView.OnExpandingListener
            public void onExpanding() {
                synchronized (ExpandableCardLayout.this.mLock) {
                    ExpandableCardLayout.access$208(ExpandableCardLayout.this);
                }
            }

            @Override // com.kopin.solos.view.ExpandableCardView.OnExpandingListener
            public void onExpanded() {
                synchronized (ExpandableCardLayout.this.mLock) {
                    ExpandableCardLayout.access$210(ExpandableCardLayout.this);
                }
            }

            @Override // com.kopin.solos.view.ExpandableCardView.OnExpandingListener
            public void onCollapsing() {
                synchronized (ExpandableCardLayout.this.mLock) {
                    ExpandableCardLayout.access$208(ExpandableCardLayout.this);
                }
            }

            @Override // com.kopin.solos.view.ExpandableCardView.OnExpandingListener
            public void onCollapsed() {
                synchronized (ExpandableCardLayout.this.mLock) {
                    ExpandableCardLayout.access$210(ExpandableCardLayout.this);
                }
            }
        };
        setOrientation(1);
        setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() { // from class: com.kopin.solos.view.ExpandableCardLayout.1
            @Override // android.view.ViewGroup.OnHierarchyChangeListener
            public void onChildViewAdded(View parent, View child) {
                if (child instanceof ExpandableCardView) {
                    ((ExpandableCardView) child).setParent(ExpandableCardLayout.this, ExpandableCardLayout.this.mExpandingListener);
                }
            }

            @Override // android.view.ViewGroup.OnHierarchyChangeListener
            public void onChildViewRemoved(View parent, View child) {
                if (child instanceof ExpandableCardView) {
                    ((ExpandableCardView) child).setParent(null, null);
                }
            }
        });
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int index = 0;
        ExpandableCardView foundExpandingView = null;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if ((child instanceof ExpandableCardView) && (((ExpandableCardView) child).isExpanding() || ((ExpandableCardView) child).isCollapsing())) {
                index = i;
                foundExpandingView = (ExpandableCardView) child;
                break;
            }
        }
        if (foundExpandingView != null) {
            final ExpandableCardView expandingView = foundExpandingView;
            final int height = expandingView.getHeight();
            for (int i2 = index + 1; i2 < getChildCount(); i2++) {
                final View child2 = getChildAt(i2);
                if (expandingView.isExpanding()) {
                    final ViewTreeObserver observer = getViewTreeObserver();
                    observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: com.kopin.solos.view.ExpandableCardLayout.2
                        @Override // android.view.ViewTreeObserver.OnPreDrawListener
                        public boolean onPreDraw() {
                            int difference = height - expandingView.getOriginalHeight();
                            ObjectAnimator animator = ObjectAnimator.ofFloat(child2, (Property<View, Float>) View.Y, child2.getTop() - difference, child2.getTop());
                            animator.start();
                            observer.removeOnPreDrawListener(this);
                            return true;
                        }
                    });
                } else {
                    final ViewTreeObserver observer2 = getViewTreeObserver();
                    observer2.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: com.kopin.solos.view.ExpandableCardLayout.3
                        @Override // android.view.ViewTreeObserver.OnPreDrawListener
                        public boolean onPreDraw() {
                            int difference = height - expandingView.getOriginalHeight();
                            int oldTop = child2.getTop();
                            int newTop = oldTop - difference;
                            int oldBottom = child2.getBottom();
                            int newBottom = oldBottom - difference;
                            PropertyValuesHolder top2 = PropertyValuesHolder.ofInt(PageHelper.ALIGNMENT_TOP, oldTop, newTop);
                            PropertyValuesHolder bottom2 = PropertyValuesHolder.ofInt(PageHelper.ALIGNMENT_BOTTOM, oldBottom, newBottom);
                            ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(child2, top2, bottom2);
                            animator.start();
                            observer2.removeOnPreDrawListener(this);
                            return true;
                        }
                    });
                }
            }
        }
    }

    public boolean isAnimating() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mAnimationCount != 0;
        }
        return z;
    }

    public void recycle() {
        int len = getChildCount();
        for (int i = 0; i < len; i++) {
            View child = getChildAt(i);
            if (child != null && (child instanceof ExpandableCardView)) {
                ((ExpandableCardView) child).recycle();
            }
        }
    }
}
