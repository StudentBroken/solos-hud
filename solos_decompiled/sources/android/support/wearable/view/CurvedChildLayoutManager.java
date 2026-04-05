package android.support.wearable.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.VisibleForTesting;
import android.support.wearable.R;
import android.support.wearable.view.WearableRecyclerView;
import android.view.View;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(23)
public class CurvedChildLayoutManager extends WearableRecyclerView.ChildLayoutManager {
    private static final float EPSILON = 0.001f;
    private final float[] mAnchorOffsetXY;
    private float mCurveBottom;
    private final Path mCurvePath;
    private int mCurvePathHeight;
    private float mCurveTop;
    private boolean mIsScreenRound;
    private int mLayoutHeight;
    private int mLayoutWidth;
    private float mLineGradient;
    private WearableRecyclerView mParentView;
    private float mPathLength;
    private final PathMeasure mPathMeasure;
    private final float[] mPathPoints;
    private final float[] mPathTangent;
    private int mXCurveOffset;

    public CurvedChildLayoutManager(Context context) {
        super(context);
        this.mPathPoints = new float[2];
        this.mPathTangent = new float[2];
        this.mAnchorOffsetXY = new float[2];
        this.mCurvePath = new Path();
        this.mPathMeasure = new PathMeasure();
        this.mIsScreenRound = context.getResources().getConfiguration().isScreenRound();
        this.mXCurveOffset = context.getResources().getDimensionPixelSize(R.dimen.wrv_curve_default_x_offset);
    }

    @VisibleForTesting
    void setRound(boolean isScreenRound) {
        this.mIsScreenRound = isScreenRound;
    }

    @VisibleForTesting
    void setOffset(int offset) {
        this.mXCurveOffset = offset;
    }

    @Override // android.support.wearable.view.WearableRecyclerView.ChildLayoutManager
    public void updateChild(View child, WearableRecyclerView parent) {
        if (this.mParentView != parent) {
            this.mParentView = parent;
            this.mLayoutWidth = this.mParentView.getWidth();
            this.mLayoutHeight = this.mParentView.getHeight();
        }
        if (this.mIsScreenRound) {
            maybeSetUpCircularInitialLayout(this.mLayoutWidth, this.mLayoutHeight);
            this.mAnchorOffsetXY[0] = this.mXCurveOffset;
            this.mAnchorOffsetXY[1] = child.getHeight() / 2.0f;
            adjustAnchorOffsetXY(child, this.mAnchorOffsetXY);
            float minCenter = (-child.getHeight()) / 2.0f;
            float maxCenter = this.mLayoutHeight + (child.getHeight() / 2.0f);
            float range = maxCenter - minCenter;
            float verticalAnchor = child.getTop() + this.mAnchorOffsetXY[1];
            float mYScrollProgress = (Math.abs(minCenter) + verticalAnchor) / range;
            this.mPathMeasure.getPosTan(this.mPathLength * mYScrollProgress, this.mPathPoints, this.mPathTangent);
            boolean topClusterRisk = Math.abs(this.mPathPoints[1] - this.mCurveBottom) < EPSILON && minCenter < this.mPathPoints[1];
            boolean bottomClusterRisk = Math.abs(this.mPathPoints[1] - this.mCurveTop) < EPSILON && maxCenter > this.mPathPoints[1];
            if (topClusterRisk || bottomClusterRisk) {
                this.mPathPoints[1] = verticalAnchor;
                this.mPathPoints[0] = Math.abs(verticalAnchor) * this.mLineGradient;
            }
            int newLeft = (int) (this.mPathPoints[0] - this.mAnchorOffsetXY[0]);
            child.offsetLeftAndRight(newLeft - child.getLeft());
            float verticalTranslation = this.mPathPoints[1] - verticalAnchor;
            child.setTranslationY(verticalTranslation);
        }
    }

    public void adjustAnchorOffsetXY(View child, float[] anchorOffsetXY) {
    }

    private void maybeSetUpCircularInitialLayout(int width, int height) {
        if (this.mCurvePathHeight != height) {
            this.mCurvePathHeight = height;
            this.mCurveBottom = (-0.048f) * height;
            this.mCurveTop = 1.048f * height;
            this.mLineGradient = 10.416667f;
            this.mCurvePath.reset();
            this.mCurvePath.moveTo(0.5f * width, this.mCurveBottom);
            this.mCurvePath.lineTo(width * 0.34f, 0.075f * height);
            this.mCurvePath.cubicTo(width * 0.22f, 0.17f * height, width * 0.13f, 0.32f * height, width * 0.13f, height / 2);
            this.mCurvePath.cubicTo(width * 0.13f, 0.68f * height, width * 0.22f, 0.83f * height, width * 0.34f, 0.925f * height);
            this.mCurvePath.lineTo(width / 2, this.mCurveTop);
            this.mPathMeasure.setPath(this.mCurvePath, false);
            this.mPathLength = this.mPathMeasure.getLength();
        }
    }
}
