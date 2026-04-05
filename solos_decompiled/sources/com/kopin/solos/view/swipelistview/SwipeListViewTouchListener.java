package com.kopin.solos.view.swipelistview;

import android.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes48.dex */
public class SwipeListViewTouchListener implements View.OnTouchListener {
    private static final int DISPLACE_CHOICE = 80;
    private long animationTime;
    private View backView;
    private long configShortAnimationTime;
    private int downPosition;
    private float downX;
    private View frontView;
    private boolean listViewMoving;
    private int maxFlingVelocity;
    private int minFlingVelocity;
    private int oldSwipeActionLeft;
    private int oldSwipeActionRight;
    private View parentView;
    private boolean paused;
    private int slop;
    private int swipeBackView;
    private int swipeFrontView;
    private SwipeListView swipeListView;
    private boolean swiping;
    private boolean swipingRight;
    private VelocityTracker velocityTracker;
    private int swipeMode = 1;
    private boolean swipeOpenOnLongPress = true;
    private boolean swipeClosesAllItemsWhenListMoves = true;
    private Rect rect = new Rect();
    private float leftOffset = 0.0f;
    private float rightOffset = 0.0f;
    private int swipeDrawableChecked = 0;
    private int swipeDrawableUnchecked = 0;
    private int viewWidth = 1;
    private List<PendingDismissData> pendingDismisses = new ArrayList();
    private int dismissAnimationRefCount = 0;
    private int swipeCurrentAction = 3;
    private int swipeActionLeft = 0;
    private int swipeActionRight = 0;
    private List<Boolean> opened = new ArrayList();
    private List<Boolean> openedRight = new ArrayList();
    private List<Boolean> checked = new ArrayList();

    static /* synthetic */ int access$906(SwipeListViewTouchListener x0) {
        int i = x0.dismissAnimationRefCount - 1;
        x0.dismissAnimationRefCount = i;
        return i;
    }

    public SwipeListViewTouchListener(SwipeListView swipeListView, int swipeFrontView, int swipeBackView) {
        this.swipeFrontView = 0;
        this.swipeBackView = 0;
        this.swipeFrontView = swipeFrontView;
        this.swipeBackView = swipeBackView;
        ViewConfiguration vc = ViewConfiguration.get(swipeListView.getContext());
        this.slop = vc.getScaledTouchSlop();
        this.minFlingVelocity = vc.getScaledMinimumFlingVelocity();
        this.maxFlingVelocity = vc.getScaledMaximumFlingVelocity();
        this.configShortAnimationTime = swipeListView.getContext().getResources().getInteger(R.integer.config_shortAnimTime);
        this.animationTime = this.configShortAnimationTime;
        this.swipeListView = swipeListView;
    }

    private void setParentView(View parentView) {
        this.parentView = parentView;
    }

    private void setFrontView(View frontView, final int childPosition) {
        this.frontView = frontView;
        frontView.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.view.swipelistview.SwipeListViewTouchListener.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SwipeListViewTouchListener.this.swipeListView.onClickFrontView(SwipeListViewTouchListener.this.downPosition);
            }
        });
        frontView.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.kopin.solos.view.swipelistview.SwipeListViewTouchListener.2
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View v) {
                if (SwipeListViewTouchListener.this.swipeOpenOnLongPress) {
                    if (SwipeListViewTouchListener.this.downPosition >= 0) {
                        SwipeListViewTouchListener.this.openAnimate(childPosition);
                        return false;
                    }
                    return false;
                }
                SwipeListViewTouchListener.this.swapChoiceState(childPosition);
                return false;
            }
        });
    }

    private void setBackView(View backView) {
        this.backView = backView;
        backView.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.view.swipelistview.SwipeListViewTouchListener.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SwipeListViewTouchListener.this.swipeListView.onClickBackView(SwipeListViewTouchListener.this.downPosition);
            }
        });
    }

    public boolean isListViewMoving() {
        return this.listViewMoving;
    }

    public void setAnimationTime(long animationTime) {
        if (animationTime > 0) {
            this.animationTime = animationTime;
        } else {
            this.animationTime = this.configShortAnimationTime;
        }
    }

    public void setRightOffset(float rightOffset) {
        this.rightOffset = rightOffset;
    }

    public void setLeftOffset(float leftOffset) {
        this.leftOffset = leftOffset;
    }

    public void setSwipeClosesAllItemsWhenListMoves(boolean swipeClosesAllItemsWhenListMoves) {
        this.swipeClosesAllItemsWhenListMoves = swipeClosesAllItemsWhenListMoves;
    }

    public void setSwipeOpenOnLongPress(boolean swipeOpenOnLongPress) {
        this.swipeOpenOnLongPress = swipeOpenOnLongPress;
    }

    public void setSwipeMode(int swipeMode) {
        this.swipeMode = swipeMode;
    }

    protected boolean isSwipeEnabled() {
        return this.swipeMode != 0;
    }

    public int getSwipeActionLeft() {
        return this.swipeActionLeft;
    }

    public void setSwipeActionLeft(int swipeActionLeft) {
        this.swipeActionLeft = swipeActionLeft;
    }

    public int getSwipeActionRight() {
        return this.swipeActionRight;
    }

    public void setSwipeActionRight(int swipeActionRight) {
        this.swipeActionRight = swipeActionRight;
    }

    protected void setSwipeDrawableChecked(int swipeDrawableChecked) {
        this.swipeDrawableChecked = swipeDrawableChecked;
    }

    protected void setSwipeDrawableUnchecked(int swipeDrawableUnchecked) {
        this.swipeDrawableUnchecked = swipeDrawableUnchecked;
    }

    public void resetItems() {
        if (this.swipeListView.getAdapter() != null) {
            int count = this.swipeListView.getAdapter().getCount();
            for (int i = this.opened.size(); i <= count; i++) {
                this.opened.add(false);
                this.openedRight.add(false);
                this.checked.add(false);
            }
        }
    }

    protected void openAnimate(int position) {
        View child = this.swipeListView.getChildAt(position - this.swipeListView.getFirstVisiblePosition()).findViewById(this.swipeFrontView);
        if (child != null) {
            openAnimate(child, position);
        }
    }

    protected void closeAnimate(int position) {
        View child;
        if (this.swipeListView != null) {
            int firstVisibleChildPosition = this.swipeListView.getFirstVisiblePosition();
            View childContainer = this.swipeListView.getChildAt(position - firstVisibleChildPosition);
            if (childContainer != null && (child = childContainer.findViewById(this.swipeFrontView)) != null) {
                closeAnimate(child, position);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void swapChoiceState(int position) {
        int lastCount = getCountSelected();
        boolean lastChecked = this.checked.get(position).booleanValue();
        this.checked.set(position, Boolean.valueOf(!lastChecked));
        int count = lastChecked ? lastCount - 1 : lastCount + 1;
        if (lastCount == 0 && count == 1) {
            this.swipeListView.onChoiceStarted();
            closeOpenedItems();
            setActionsTo(2);
        }
        if (lastCount == 1 && count == 0) {
            this.swipeListView.onChoiceEnded();
            returnOldActions();
        }
        if (Build.VERSION.SDK_INT >= 11) {
            this.swipeListView.setItemChecked(position, !lastChecked);
        }
        this.swipeListView.onChoiceChanged(position, lastChecked ? false : true);
        reloadChoiceStateInView(this.frontView, position);
    }

    protected void unselectedChoiceStates() {
        int start = this.swipeListView.getFirstVisiblePosition();
        int end = this.swipeListView.getLastVisiblePosition();
        for (int i = 0; i < this.checked.size(); i++) {
            if (this.checked.get(i).booleanValue() && i >= start && i <= end) {
                reloadChoiceStateInView(this.swipeListView.getChildAt(i - start).findViewById(this.swipeFrontView), i);
            }
            this.checked.set(i, false);
        }
        this.swipeListView.onChoiceEnded();
        returnOldActions();
    }

    protected int dismiss(int position) {
        this.opened.remove(position);
        this.checked.remove(position);
        int start = this.swipeListView.getFirstVisiblePosition();
        int end = this.swipeListView.getLastVisiblePosition();
        View view = this.swipeListView.getChildAt(position - start);
        this.dismissAnimationRefCount++;
        if (position >= start && position <= end) {
            performDismiss(view, position, false);
            return view.getHeight();
        }
        this.pendingDismisses.add(new PendingDismissData(position, null));
        return 0;
    }

    protected void reloadChoiceStateInView(View frontView, int position) {
        if (isChecked(position)) {
            if (this.swipeDrawableChecked > 0) {
                frontView.setBackgroundResource(this.swipeDrawableChecked);
            }
        } else if (this.swipeDrawableUnchecked > 0) {
            frontView.setBackgroundResource(this.swipeDrawableUnchecked);
        }
    }

    protected void reloadSwipeStateInView(View frontView, int position) {
        if (!this.opened.get(position).booleanValue()) {
            frontView.setTranslationX(0.0f);
        } else if (this.openedRight.get(position).booleanValue()) {
            frontView.setTranslationX(this.swipeListView.getWidth());
        } else {
            frontView.setTranslationX(-this.swipeListView.getWidth());
        }
    }

    protected boolean isChecked(int position) {
        return position < this.checked.size() && this.checked.get(position).booleanValue();
    }

    protected int getCountSelected() {
        int count = 0;
        for (int i = 0; i < this.checked.size(); i++) {
            if (this.checked.get(i).booleanValue()) {
                count++;
            }
        }
        return count;
    }

    protected List<Integer> getPositionsSelected() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < this.checked.size(); i++) {
            if (this.checked.get(i).booleanValue()) {
                list.add(Integer.valueOf(i));
            }
        }
        return list;
    }

    private void openAnimate(View view, int position) {
        if (!this.opened.get(position).booleanValue()) {
            generateRevealAnimate(view, true, false, position);
        }
    }

    private void closeAnimate(View view, int position) {
        if (this.opened.get(position).booleanValue()) {
            generateRevealAnimate(view, true, false, position);
        }
    }

    private void generateAnimate(View view, boolean swap, boolean swapRight, int position) {
        if (this.swipeCurrentAction == 0) {
            generateRevealAnimate(view, swap, swapRight, position);
        }
        if (this.swipeCurrentAction == 1) {
            generateDismissAnimate(this.parentView, swap, swapRight, position);
        }
        if (this.swipeCurrentAction == 2) {
            generateChoiceAnimate(view, position);
        }
    }

    private void generateChoiceAnimate(View view, int position) {
        view.animate().translationX(0.0f).setDuration(this.animationTime).setListener(new AnimatorListenerAdapter() { // from class: com.kopin.solos.view.swipelistview.SwipeListViewTouchListener.4
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                SwipeListViewTouchListener.this.swipeListView.resetScrolling();
                SwipeListViewTouchListener.this.resetCell();
            }
        });
    }

    private void generateDismissAnimate(final View view, final boolean swap, boolean swapRight, final int position) {
        int moveTo = 0;
        if (this.opened.get(position).booleanValue()) {
            if (!swap) {
                moveTo = this.openedRight.get(position).booleanValue() ? (int) (this.viewWidth - this.rightOffset) : (int) ((-this.viewWidth) + this.leftOffset);
            }
        } else if (swap) {
            moveTo = swapRight ? (int) (this.viewWidth - this.rightOffset) : (int) ((-this.viewWidth) + this.leftOffset);
        }
        int alpha = 1;
        if (swap) {
            this.dismissAnimationRefCount++;
            alpha = 0;
        }
        view.animate().translationX(moveTo).alpha(alpha).setDuration(this.animationTime).setListener(new AnimatorListenerAdapter() { // from class: com.kopin.solos.view.swipelistview.SwipeListViewTouchListener.5
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                if (swap) {
                    SwipeListViewTouchListener.this.closeOpenedItems();
                    SwipeListViewTouchListener.this.performDismiss(view, position, true);
                }
                SwipeListViewTouchListener.this.resetCell();
            }
        });
    }

    private void generateRevealAnimate(View view, final boolean swap, final boolean swapRight, final int position) {
        int moveTo = 0;
        if (this.opened.get(position).booleanValue()) {
            if (!swap) {
                moveTo = this.openedRight.get(position).booleanValue() ? (int) (this.viewWidth - this.rightOffset) : (int) ((-this.viewWidth) + this.leftOffset);
            }
        } else if (swap) {
            moveTo = swapRight ? (int) (this.viewWidth - this.rightOffset) : (int) ((-this.viewWidth) + this.leftOffset);
        }
        view.animate().translationX(moveTo).setDuration(this.animationTime).setListener(new AnimatorListenerAdapter() { // from class: com.kopin.solos.view.swipelistview.SwipeListViewTouchListener.6
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                SwipeListViewTouchListener.this.swipeListView.resetScrolling();
                if (swap) {
                    boolean aux = !((Boolean) SwipeListViewTouchListener.this.opened.get(position)).booleanValue();
                    SwipeListViewTouchListener.this.opened.set(position, Boolean.valueOf(aux));
                    if (aux) {
                        SwipeListViewTouchListener.this.swipeListView.onOpened(position, swapRight);
                        SwipeListViewTouchListener.this.openedRight.set(position, Boolean.valueOf(swapRight));
                    } else {
                        SwipeListViewTouchListener.this.swipeListView.onClosed(position, ((Boolean) SwipeListViewTouchListener.this.openedRight.get(position)).booleanValue());
                    }
                }
                SwipeListViewTouchListener.this.resetCell();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetCell() {
        if (this.downPosition != -1) {
            if (this.swipeCurrentAction == 2) {
                this.backView.setVisibility(0);
            }
            this.frontView.setClickable(this.opened.get(this.downPosition).booleanValue());
            this.frontView.setLongClickable(this.opened.get(this.downPosition).booleanValue());
            this.frontView = null;
            this.backView = null;
            this.downPosition = -1;
        }
    }

    public void setEnabled(boolean enabled) {
        this.paused = !enabled;
    }

    public AbsListView.OnScrollListener makeScrollListener() {
        return new AbsListView.OnScrollListener() { // from class: com.kopin.solos.view.swipelistview.SwipeListViewTouchListener.7
            private boolean isFirstItem = false;
            private boolean isLastItem = false;

            @Override // android.widget.AbsListView.OnScrollListener
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                SwipeListViewTouchListener.this.setEnabled(scrollState != 1);
                if (SwipeListViewTouchListener.this.swipeClosesAllItemsWhenListMoves && scrollState == 1) {
                    SwipeListViewTouchListener.this.closeOpenedItems();
                }
                if (scrollState == 1) {
                    SwipeListViewTouchListener.this.listViewMoving = true;
                    SwipeListViewTouchListener.this.setEnabled(false);
                }
                if (scrollState != 2 && scrollState != 1) {
                    SwipeListViewTouchListener.this.listViewMoving = false;
                    SwipeListViewTouchListener.this.downPosition = -1;
                    SwipeListViewTouchListener.this.swipeListView.resetScrolling();
                    new Handler().postDelayed(new Runnable() { // from class: com.kopin.solos.view.swipelistview.SwipeListViewTouchListener.7.1
                        @Override // java.lang.Runnable
                        public void run() {
                            SwipeListViewTouchListener.this.setEnabled(true);
                        }
                    }, 500L);
                }
            }

            @Override // android.widget.AbsListView.OnScrollListener
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (this.isFirstItem) {
                    boolean onSecondItemList = firstVisibleItem == 1;
                    if (onSecondItemList) {
                        this.isFirstItem = false;
                    }
                } else {
                    boolean onFirstItemList = firstVisibleItem == 0;
                    if (onFirstItemList) {
                        this.isFirstItem = true;
                        SwipeListViewTouchListener.this.swipeListView.onFirstListItem();
                    }
                }
                if (this.isLastItem) {
                    boolean onBeforeLastItemList = firstVisibleItem + visibleItemCount == totalItemCount + (-1);
                    if (onBeforeLastItemList) {
                        this.isLastItem = false;
                        return;
                    }
                    return;
                }
                boolean onLastItemList = firstVisibleItem + visibleItemCount >= totalItemCount;
                if (onLastItemList) {
                    this.isLastItem = true;
                    SwipeListViewTouchListener.this.swipeListView.onLastListItem();
                }
            }
        };
    }

    void closeOpenedItems() {
        if (this.opened != null) {
            int start = this.swipeListView.getFirstVisiblePosition();
            int end = this.swipeListView.getLastVisiblePosition();
            for (int i = start; i <= end; i++) {
                if (this.opened.get(i).booleanValue()) {
                    closeAnimate(this.swipeListView.getChildAt(i - start).findViewById(this.swipeFrontView), i);
                }
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (!isSwipeEnabled()) {
            return false;
        }
        if (this.viewWidth < 2) {
            this.viewWidth = this.swipeListView.getWidth();
        }
        switch (MotionEventCompat.getActionMasked(motionEvent)) {
            case 0:
                if (this.paused && this.downPosition != -1) {
                    return false;
                }
                this.swipeCurrentAction = 3;
                int childCount = this.swipeListView.getChildCount();
                int[] listViewCoords = new int[2];
                this.swipeListView.getLocationOnScreen(listViewCoords);
                int x = ((int) motionEvent.getRawX()) - listViewCoords[0];
                int y = ((int) motionEvent.getRawY()) - listViewCoords[1];
                int i = 0;
                while (true) {
                    if (i < childCount) {
                        View child = this.swipeListView.getChildAt(i);
                        child.getHitRect(this.rect);
                        int childPosition = this.swipeListView.getPositionForView(child);
                        boolean allowSwipe = this.swipeListView.getAdapter().isEnabled(childPosition) && this.swipeListView.getAdapter().getItemViewType(childPosition) >= 0;
                        if (!allowSwipe || !this.rect.contains(x, y)) {
                            i++;
                        } else {
                            setParentView(child);
                            setFrontView(child.findViewById(this.swipeFrontView), childPosition);
                            this.downX = motionEvent.getRawX();
                            this.downPosition = childPosition;
                            this.frontView.setClickable(!this.opened.get(this.downPosition).booleanValue());
                            this.frontView.setLongClickable(!this.opened.get(this.downPosition).booleanValue());
                            this.velocityTracker = VelocityTracker.obtain();
                            this.velocityTracker.addMovement(motionEvent);
                            if (this.swipeBackView > 0) {
                                setBackView(child.findViewById(this.swipeBackView));
                            }
                        }
                    }
                }
                view.onTouchEvent(motionEvent);
                return true;
            case 1:
                if (this.velocityTracker != null && this.swiping && this.downPosition != -1) {
                    float deltaX = motionEvent.getRawX() - this.downX;
                    this.velocityTracker.addMovement(motionEvent);
                    this.velocityTracker.computeCurrentVelocity(1000);
                    float velocityX = Math.abs(this.velocityTracker.getXVelocity());
                    if (!this.opened.get(this.downPosition).booleanValue()) {
                        if (this.swipeMode == 3 && this.velocityTracker.getXVelocity() > 0.0f) {
                            velocityX = 0.0f;
                        }
                        if (this.swipeMode == 2 && this.velocityTracker.getXVelocity() < 0.0f) {
                            velocityX = 0.0f;
                        }
                    }
                    float velocityY = Math.abs(this.velocityTracker.getYVelocity());
                    boolean swap = false;
                    boolean swapRight = false;
                    if (this.minFlingVelocity <= velocityX && velocityX <= this.maxFlingVelocity && 2.0f * velocityY < velocityX) {
                        swapRight = this.velocityTracker.getXVelocity() > 0.0f;
                        swap = (swapRight == this.swipingRight || this.swipeActionLeft == this.swipeActionRight) ? (this.opened.get(this.downPosition).booleanValue() && this.openedRight.get(this.downPosition).booleanValue() && swapRight) ? false : !this.opened.get(this.downPosition).booleanValue() || this.openedRight.get(this.downPosition).booleanValue() || swapRight : false;
                    } else if (Math.abs(deltaX) > this.viewWidth / 2) {
                        swap = true;
                        swapRight = deltaX > 0.0f;
                    }
                    generateAnimate(this.frontView, swap, swapRight, this.downPosition);
                    if (this.swipeCurrentAction == 2) {
                        swapChoiceState(this.downPosition);
                    }
                    this.velocityTracker.recycle();
                    this.velocityTracker = null;
                    this.downX = 0.0f;
                    this.swiping = false;
                }
                return false;
            case 2:
                if (this.velocityTracker != null && !this.paused && this.downPosition != -1) {
                    this.velocityTracker.addMovement(motionEvent);
                    this.velocityTracker.computeCurrentVelocity(1000);
                    float velocityX2 = Math.abs(this.velocityTracker.getXVelocity());
                    float velocityY2 = Math.abs(this.velocityTracker.getYVelocity());
                    float deltaX2 = motionEvent.getRawX() - this.downX;
                    float deltaMode = Math.abs(deltaX2);
                    int swipeMode = this.swipeMode;
                    int changeSwipeMode = this.swipeListView.changeSwipeMode(this.downPosition);
                    if (changeSwipeMode >= 0) {
                        swipeMode = changeSwipeMode;
                    }
                    if (swipeMode == 0) {
                        deltaMode = 0.0f;
                    } else if (swipeMode != 1) {
                        if (this.opened.get(this.downPosition).booleanValue()) {
                            if (swipeMode == 3 && deltaX2 < 0.0f) {
                                deltaMode = 0.0f;
                            } else if (swipeMode == 2 && deltaX2 > 0.0f) {
                                deltaMode = 0.0f;
                            }
                        } else if (swipeMode == 3 && deltaX2 > 0.0f) {
                            deltaMode = 0.0f;
                        } else if (swipeMode == 2 && deltaX2 < 0.0f) {
                            deltaMode = 0.0f;
                        }
                    }
                    if (deltaMode > this.slop && this.swipeCurrentAction == 3 && velocityY2 < velocityX2) {
                        this.swiping = true;
                        this.swipingRight = deltaX2 > 0.0f;
                        if (this.opened.get(this.downPosition).booleanValue()) {
                            this.swipeListView.onStartClose(this.downPosition, this.swipingRight);
                            this.swipeCurrentAction = 0;
                        } else {
                            if (this.swipingRight && this.swipeActionRight == 1) {
                                this.swipeCurrentAction = 1;
                            } else if (!this.swipingRight && this.swipeActionLeft == 1) {
                                this.swipeCurrentAction = 1;
                            } else if (this.swipingRight && this.swipeActionRight == 2) {
                                this.swipeCurrentAction = 2;
                            } else if (!this.swipingRight && this.swipeActionLeft == 2) {
                                this.swipeCurrentAction = 2;
                            } else {
                                this.swipeCurrentAction = 0;
                            }
                            this.swipeListView.onStartOpen(this.downPosition, this.swipeCurrentAction, this.swipingRight);
                        }
                        this.swipeListView.requestDisallowInterceptTouchEvent(true);
                        MotionEvent cancelEvent = MotionEvent.obtain(motionEvent);
                        cancelEvent.setAction((MotionEventCompat.getActionIndex(motionEvent) << 8) | 3);
                        this.swipeListView.onTouchEvent(cancelEvent);
                        if (this.swipeCurrentAction == 2) {
                            this.backView.setVisibility(8);
                        }
                    }
                    if (this.swiping && this.downPosition != -1) {
                        if (this.opened.get(this.downPosition).booleanValue()) {
                            deltaX2 += this.openedRight.get(this.downPosition).booleanValue() ? this.viewWidth - this.rightOffset : (-this.viewWidth) + this.leftOffset;
                        }
                        move(deltaX2);
                        return true;
                    }
                }
                return false;
            default:
                return false;
        }
    }

    private void setActionsTo(int action) {
        this.oldSwipeActionRight = this.swipeActionRight;
        this.oldSwipeActionLeft = this.swipeActionLeft;
        this.swipeActionRight = action;
        this.swipeActionLeft = action;
    }

    protected void returnOldActions() {
        this.swipeActionRight = this.oldSwipeActionRight;
        this.swipeActionLeft = this.oldSwipeActionLeft;
    }

    public void move(float deltaX) {
        this.swipeListView.onMove(this.downPosition, deltaX);
        float posX = this.frontView.getX();
        if (this.opened.get(this.downPosition).booleanValue()) {
            posX += this.openedRight.get(this.downPosition).booleanValue() ? (-this.viewWidth) + this.rightOffset : this.viewWidth - this.leftOffset;
        }
        if (posX > 0.0f && !this.swipingRight) {
            this.swipingRight = !this.swipingRight;
            this.swipeCurrentAction = this.swipeActionRight;
            if (this.swipeCurrentAction == 2) {
                this.backView.setVisibility(8);
            } else {
                this.backView.setVisibility(0);
            }
        }
        if (posX < 0.0f && this.swipingRight) {
            this.swipingRight = !this.swipingRight;
            this.swipeCurrentAction = this.swipeActionLeft;
            if (this.swipeCurrentAction == 2) {
                this.backView.setVisibility(8);
            } else {
                this.backView.setVisibility(0);
            }
        }
        if (this.swipeCurrentAction == 1) {
            this.parentView.setTranslationX(deltaX);
            this.parentView.setAlpha(Math.max(0.0f, Math.min(1.0f, 1.0f - ((2.0f * Math.abs(deltaX)) / this.viewWidth))));
            return;
        }
        if (this.swipeCurrentAction == 2) {
            if ((this.swipingRight && deltaX > 0.0f && posX < 80.0f) || ((!this.swipingRight && deltaX < 0.0f && posX > -80.0f) || ((this.swipingRight && deltaX < 80.0f) || (!this.swipingRight && deltaX > -80.0f)))) {
                this.frontView.setTranslationX(deltaX);
                return;
            }
            return;
        }
        this.frontView.setTranslationX(deltaX);
    }

    class PendingDismissData implements Comparable<PendingDismissData> {
        public int position;
        public View view;

        public PendingDismissData(int position, View view) {
            this.position = position;
            this.view = view;
        }

        @Override // java.lang.Comparable
        public int compareTo(PendingDismissData other) {
            return other.position - this.position;
        }
    }

    protected void performDismiss(final View dismissView, int dismissPosition, boolean doPendingDismiss) {
        enableDisableViewGroup((ViewGroup) dismissView, false);
        final ViewGroup.LayoutParams lp = dismissView.getLayoutParams();
        final int originalHeight = dismissView.getHeight();
        ValueAnimator animator = ValueAnimator.ofInt(originalHeight, 1).setDuration(this.animationTime);
        if (doPendingDismiss) {
            animator.addListener(new AnimatorListenerAdapter() { // from class: com.kopin.solos.view.swipelistview.SwipeListViewTouchListener.8
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animation) {
                    SwipeListViewTouchListener.access$906(SwipeListViewTouchListener.this);
                    if (SwipeListViewTouchListener.this.dismissAnimationRefCount == 0) {
                        SwipeListViewTouchListener.this.removePendingDismisses(originalHeight);
                    }
                }
            });
        }
        animator.addListener(new AnimatorListenerAdapter() { // from class: com.kopin.solos.view.swipelistview.SwipeListViewTouchListener.9
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                SwipeListViewTouchListener.enableDisableViewGroup((ViewGroup) dismissView, true);
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.kopin.solos.view.swipelistview.SwipeListViewTouchListener.10
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                lp.height = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                dismissView.setLayoutParams(lp);
            }
        });
        this.pendingDismisses.add(new PendingDismissData(dismissPosition, dismissView));
        animator.start();
    }

    protected void resetPendingDismisses() {
        this.pendingDismisses.clear();
    }

    protected void handlerPendingDismisses(final int originalHeight) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() { // from class: com.kopin.solos.view.swipelistview.SwipeListViewTouchListener.11
            @Override // java.lang.Runnable
            public void run() {
                SwipeListViewTouchListener.this.removePendingDismisses(originalHeight);
            }
        }, this.animationTime + 100);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removePendingDismisses(int originalHeight) {
        Collections.sort(this.pendingDismisses);
        int[] dismissPositions = new int[this.pendingDismisses.size()];
        for (int i = this.pendingDismisses.size() - 1; i >= 0; i--) {
            dismissPositions[i] = this.pendingDismisses.get(i).position;
        }
        this.swipeListView.onDismiss(dismissPositions);
        for (PendingDismissData pendingDismiss : this.pendingDismisses) {
            if (pendingDismiss.view != null) {
                pendingDismiss.view.setAlpha(1.0f);
                pendingDismiss.view.setTranslationX(0.0f);
                ViewGroup.LayoutParams lp = pendingDismiss.view.getLayoutParams();
                lp.height = originalHeight;
                pendingDismiss.view.setLayoutParams(lp);
            }
        }
        resetPendingDismisses();
    }

    public static void enableDisableViewGroup(ViewGroup viewGroup, boolean enabled) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = viewGroup.getChildAt(i);
            view.setEnabled(enabled);
            if (view instanceof ViewGroup) {
                enableDisableViewGroup((ViewGroup) view, enabled);
            }
        }
    }
}
