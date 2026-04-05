package android.support.wearable.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.wearable.R;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(20)
public class DelayedConfirmationView extends CircledImageView {
    private static final int DEFAULT_UPDATE_INTERVAL_MS = 33;
    private long mCurrentTimeMs;
    private DelayedConfirmationListener mListener;
    private long mStartTimeMs;
    private final Handler mTimerHandler;
    private long mTotalTimeMs;
    private long mUpdateIntervalMs;

    public interface DelayedConfirmationListener {
        void onTimerFinished(View view);

        void onTimerSelected(View view);
    }

    public DelayedConfirmationView(Context context) {
        this(context, null);
    }

    public DelayedConfirmationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DelayedConfirmationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mUpdateIntervalMs = 0L;
        this.mTimerHandler = new Handler() { // from class: android.support.wearable.view.DelayedConfirmationView.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                DelayedConfirmationView.this.mCurrentTimeMs = SystemClock.elapsedRealtime();
                DelayedConfirmationView.this.invalidate();
                if (DelayedConfirmationView.this.mCurrentTimeMs - DelayedConfirmationView.this.mStartTimeMs < DelayedConfirmationView.this.mTotalTimeMs) {
                    DelayedConfirmationView.this.mTimerHandler.sendEmptyMessageDelayed(0, DelayedConfirmationView.this.mUpdateIntervalMs);
                } else if (DelayedConfirmationView.this.mStartTimeMs > 0 && DelayedConfirmationView.this.mListener != null) {
                    DelayedConfirmationView.this.mListener.onTimerFinished(DelayedConfirmationView.this);
                }
            }
        };
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DelayedConfirmationView, defStyle, 0);
        this.mUpdateIntervalMs = a.getInteger(R.styleable.DelayedConfirmationView_update_interval, 33);
        setProgress(0.0f);
        a.recycle();
    }

    public void setStartTimeMs(long time) {
        this.mStartTimeMs = time;
        invalidate();
    }

    public void setTotalTimeMs(long time) {
        this.mTotalTimeMs = time;
    }

    public void setListener(DelayedConfirmationListener listener) {
        this.mListener = listener;
    }

    public void start() {
        this.mStartTimeMs = SystemClock.elapsedRealtime();
        this.mCurrentTimeMs = SystemClock.elapsedRealtime();
        this.mTimerHandler.sendEmptyMessageDelayed(0, this.mUpdateIntervalMs);
    }

    public void reset() {
        this.mStartTimeMs = 0L;
        setProgress(0.0f);
        invalidate();
    }

    @Override // android.support.wearable.view.CircledImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.mStartTimeMs > 0) {
            setProgress((this.mCurrentTimeMs - this.mStartTimeMs) / this.mTotalTimeMs);
        }
        super.onDraw(canvas);
    }

    @Override // android.view.View
    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case 0:
            case 2:
                if (this.mListener != null) {
                    this.mListener.onTimerSelected(this);
                }
                break;
        }
        return false;
    }
}
