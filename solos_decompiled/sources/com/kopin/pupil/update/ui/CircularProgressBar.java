package com.kopin.pupil.update.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.kopin.pupil.update.R;

/* JADX INFO: loaded from: classes13.dex */
public class CircularProgressBar extends View {
    private float mAngle;
    private String mBgColor;
    private Paint mBgPaint;
    private String mFgColor;
    private int mHeight;
    private int mMax;
    private final Paint mPaint;
    private int mProgress;
    private RectF mRect;
    private int mStartPoint;
    private StartPosition mStartPosition;
    private int mStrokeWidth;
    private TextView mTxtProgress;
    private float mUnit;
    private int mWidth;

    public enum StartPosition {
        TWELVE_OCLOCK,
        THREE_OCLOCK,
        SIX_OCLOCK,
        NINE_OCLOCK
    }

    public CircularProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mStartPosition = StartPosition.TWELVE_OCLOCK;
        this.mStartPoint = 270;
        this.mStrokeWidth = 20;
        this.mBgPaint = new Paint();
        this.mPaint = new Paint();
        this.mProgress = 0;
        this.mMax = 100;
        this.mFgColor = "#ffb700";
        this.mBgColor = null;
        this.mTxtProgress = null;
        this.mBgPaint = new Paint();
        this.mBgPaint.setAntiAlias(true);
        this.mBgPaint.setStyle(Paint.Style.STROKE);
        this.mBgPaint.setStrokeWidth(this.mStrokeWidth);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(this.mStrokeWidth);
        this.mPaint.setColor(Color.parseColor(this.mFgColor));
        this.mRect = new RectF(this.mStrokeWidth, this.mStrokeWidth, this.mStrokeWidth + 200, this.mStrokeWidth + 200);
        calcUnit();
        this.mAngle = 0.0f;
        loadAttributes(context, attrs);
    }

    private void loadAttributes(Context context, AttributeSet attrs) {
        TypedArray array;
        if (attrs != null && (array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircularProgressBar, 0, 0)) != null) {
            int num = array.getIndexCount();
            for (int idx = 0; idx < num; idx++) {
                int attr = array.getIndex(idx);
                if (attr == R.styleable.CircularProgressBar_bgColor) {
                    setBgColor(array.getString(attr));
                } else if (attr == R.styleable.CircularProgressBar_fgColor) {
                    setFgColor(array.getString(attr));
                } else if (attr == R.styleable.CircularProgressBar_strokeWidth) {
                    setStrokeWidth(Integer.parseInt(array.getString(attr)));
                } else if (attr == R.styleable.CircularProgressBar_max) {
                    setMax(Integer.parseInt(array.getString(attr)));
                } else if (attr == R.styleable.CircularProgressBar_progress) {
                    setProgress(Integer.parseInt(array.getString(attr)));
                } else if (attr == R.styleable.CircularProgressBar_startPosition) {
                    int position = Integer.parseInt(array.getString(attr));
                    if (position > 3) {
                        position = 0;
                    }
                    if (position < 0) {
                        position = 0;
                    }
                    setStartPosition(StartPosition.values()[position]);
                }
            }
            array.recycle();
        }
    }

    private void calcUnit() {
        this.mUnit = 360.0f / this.mMax;
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.mWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        this.mHeight = View.MeasureSpec.getSize(heightMeasureSpec);
        this.mRect = new RectF(this.mStrokeWidth, this.mStrokeWidth, this.mWidth - this.mStrokeWidth, this.mHeight - this.mStrokeWidth);
    }

    private void drawBackground(Canvas canvas) {
        canvas.drawCircle(this.mWidth / 2, this.mWidth / 2, (this.mWidth / 2) - this.mStrokeWidth, this.mBgPaint);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mBgColor != null) {
            drawBackground(canvas);
        }
        canvas.drawArc(this.mRect, this.mStartPoint, this.mAngle, false, this.mPaint);
    }

    private void refresh() {
        invalidate();
        requestLayout();
    }

    public void setProgress(int progress) {
        this.mProgress = progress;
        this.mAngle = this.mProgress * this.mUnit;
        setAngle(this.mAngle);
        if (this.mTxtProgress != null) {
            this.mTxtProgress.setText(progress + "%");
        }
    }

    public float getAngle() {
        return this.mAngle;
    }

    public void setAngle(float angle) {
        this.mAngle = angle;
        if (this.mAngle > 360.0f) {
            this.mAngle -= 360.0f;
        }
        refresh();
    }

    public void setBgColor(String color) {
        this.mBgPaint.setColor(Color.parseColor(color));
        this.mBgColor = color;
        refresh();
    }

    public void setFgColor(String color) {
        this.mPaint.setColor(Color.parseColor(color));
        this.mFgColor = color;
        refresh();
    }

    public void setTextView(TextView textView) {
        this.mTxtProgress = textView;
        if (this.mTxtProgress != null) {
            this.mTxtProgress.setText(this.mProgress + "%");
        }
    }

    public void setStrokeWidth(int strokeWidth) {
        this.mStrokeWidth = strokeWidth;
        this.mPaint.setStrokeWidth(this.mStrokeWidth);
        this.mBgPaint.setStrokeWidth(this.mStrokeWidth);
        refresh();
    }

    public void setStartPosition(StartPosition startPosition) {
        if (startPosition != this.mStartPosition) {
            this.mStartPosition = startPosition;
            int start = (this.mStartPosition.ordinal() * 90) + 270;
            if (start >= 360) {
                start -= 360;
            }
            this.mStartPoint = start;
            refresh();
        }
    }

    public void setMax(int max) {
        this.mMax = max;
        calcUnit();
        setProgress(this.mProgress);
    }

    @Override // android.view.View
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (this.mTxtProgress != null) {
            this.mTxtProgress.setVisibility(visibility);
        }
    }

    public void show() {
        setVisibility(0);
    }

    public void hide() {
        setVisibility(4);
    }
}
