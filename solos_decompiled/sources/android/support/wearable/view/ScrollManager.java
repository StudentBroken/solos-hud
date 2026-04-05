package android.support.wearable.view;

import android.annotation.TargetApi;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.MotionEvent;
import android.view.VelocityTracker;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(23)
class ScrollManager {
    private static final float FLING_EDGE_RATIO = 1.5f;
    private static final int ONE_SEC_IN_MS = 1000;
    private static final float VELOCITY_MULTIPLIER = 1.5f;
    private boolean mDown;
    private float mLastAngleRadians;
    private RecyclerView mRecyclerView;
    private float mScreenRadiusPx;
    private float mScreenRadiusPxSquared;
    private float mScrollPixelsPerRadian;
    private boolean mScrolling;
    VelocityTracker mVelocityTracker;
    private float mMinRadiusFraction = 0.0f;
    private float mMinRadiusFractionSquared = this.mMinRadiusFraction * this.mMinRadiusFraction;
    private float mScrollDegreesPerScreen = 180.0f;
    private float mScrollRadiansPerScreen = (float) Math.toRadians(this.mScrollDegreesPerScreen);

    ScrollManager() {
    }

    void setRecyclerView(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
        Point displaySize = new Point();
        Display display = this.mRecyclerView.getDisplay();
        display.getSize(displaySize);
        this.mScreenRadiusPx = Math.max(displaySize.x, displaySize.y) / 2.0f;
        this.mScreenRadiusPxSquared = this.mScreenRadiusPx * this.mScreenRadiusPx;
        this.mScrollPixelsPerRadian = displaySize.y / this.mScrollRadiansPerScreen;
        this.mVelocityTracker = VelocityTracker.obtain();
    }

    void clearRecyclerView() {
        this.mRecyclerView = null;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    boolean onTouchEvent(MotionEvent event) {
        float deltaX = event.getRawX() - this.mScreenRadiusPx;
        float deltaY = event.getRawY() - this.mScreenRadiusPx;
        float radiusSquared = (deltaX * deltaX) + (deltaY * deltaY);
        MotionEvent vtev = MotionEvent.obtain(event);
        this.mVelocityTracker.addMovement(vtev);
        vtev.recycle();
        switch (event.getActionMasked()) {
            case 0:
                if (radiusSquared / this.mScreenRadiusPxSquared > this.mMinRadiusFractionSquared) {
                    this.mDown = true;
                    return true;
                }
                return false;
            case 1:
                this.mDown = false;
                this.mScrolling = false;
                this.mVelocityTracker.computeCurrentVelocity(1000, this.mRecyclerView.getMaxFlingVelocity());
                int velocityY = (int) this.mVelocityTracker.getYVelocity();
                if (event.getX() < 1.5f * this.mScreenRadiusPx) {
                    velocityY = -velocityY;
                }
                this.mVelocityTracker.clear();
                if (Math.abs(velocityY) > this.mRecyclerView.getMinFlingVelocity()) {
                    return this.mRecyclerView.fling(0, (int) (1.5f * velocityY));
                }
                return false;
            case 2:
                if (this.mScrolling) {
                    float angleRadians = (float) Math.atan2(deltaY, deltaX);
                    float deltaRadians = angleRadians - this.mLastAngleRadians;
                    int scrollPixels = Math.round(this.mScrollPixelsPerRadian * normalizeAngleRadians(deltaRadians));
                    if (scrollPixels != 0) {
                        this.mRecyclerView.scrollBy(0, scrollPixels);
                        float deltaRadians2 = scrollPixels / this.mScrollPixelsPerRadian;
                        this.mLastAngleRadians += deltaRadians2;
                        this.mLastAngleRadians = normalizeAngleRadians(this.mLastAngleRadians);
                    }
                    return true;
                }
                if (this.mDown) {
                    float deltaXFromCenter = event.getRawX() - this.mScreenRadiusPx;
                    float deltaYFromCenter = event.getRawY() - this.mScreenRadiusPx;
                    float distFromCenter = (float) Math.hypot(deltaXFromCenter, deltaYFromCenter);
                    this.mScrolling = true;
                    this.mRecyclerView.invalidate();
                    this.mLastAngleRadians = (float) Math.atan2(deltaYFromCenter / distFromCenter, deltaXFromCenter / distFromCenter);
                    return true;
                }
                if (radiusSquared / this.mScreenRadiusPxSquared > this.mMinRadiusFractionSquared) {
                    this.mDown = true;
                    return true;
                }
                return false;
            case 3:
                if (this.mDown) {
                    this.mDown = false;
                    this.mScrolling = false;
                    this.mRecyclerView.invalidate();
                    return true;
                }
                return false;
            default:
                return false;
        }
    }

    private static float normalizeAngleRadians(float angleRadians) {
        if (angleRadians < -3.141592653589793d) {
            angleRadians = (float) (((double) angleRadians) + 6.283185307179586d);
        }
        if (angleRadians > 3.141592653589793d) {
            return (float) (((double) angleRadians) - 6.283185307179586d);
        }
        return angleRadians;
    }

    public void setScrollDegreesPerScreen(float degreesPerScreen) {
        this.mScrollDegreesPerScreen = degreesPerScreen;
        this.mScrollRadiansPerScreen = (float) Math.toRadians(this.mScrollDegreesPerScreen);
    }

    public void setBezelWidth(float fraction) {
        this.mMinRadiusFraction = 1.0f - fraction;
        this.mMinRadiusFractionSquared = this.mMinRadiusFraction * this.mMinRadiusFraction;
    }

    public float getScrollDegreesPerScreen() {
        return this.mScrollDegreesPerScreen;
    }

    public float getBezelWidth() {
        return 1.0f - this.mMinRadiusFraction;
    }
}
