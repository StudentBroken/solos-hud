package android.support.wearable.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.support.wearable.R;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.WindowInsets;
import android.widget.FrameLayout;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(20)
@Deprecated
public class WatchViewStub extends FrameLayout {
    private static final String TAG = "WatchViewStub";
    private boolean mInflateNeeded;
    private boolean mLastKnownRound;
    private OnLayoutInflatedListener mListener;
    private int mRectLayout;
    private int mRoundLayout;
    private boolean mWindowInsetsApplied;
    private boolean mWindowOverscan;

    public interface OnLayoutInflatedListener {
        void onLayoutInflated(WatchViewStub watchViewStub);
    }

    public WatchViewStub(Context context) {
        this(context, null);
    }

    public WatchViewStub(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WatchViewStub(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WatchViewStub, 0, 0);
        this.mRectLayout = a.getResourceId(R.styleable.WatchViewStub_rectLayout, 0);
        this.mRoundLayout = a.getResourceId(R.styleable.WatchViewStub_roundLayout, 0);
        this.mInflateNeeded = true;
        a.recycle();
    }

    public void setOnLayoutInflatedListener(OnLayoutInflatedListener listener) {
        this.mListener = listener;
    }

    public void setRectLayout(@LayoutRes int resId) {
        if (!this.mInflateNeeded) {
            Log.w(TAG, "Views have already been inflated. setRectLayout will have no effect.");
        } else {
            this.mRectLayout = resId;
        }
    }

    public void setRoundLayout(@LayoutRes int resId) {
        if (!this.mInflateNeeded) {
            Log.w(TAG, "Views have already been inflated. setRoundLayout will have no effect.");
        } else {
            this.mRoundLayout = resId;
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mWindowOverscan = Func.getWindowOverscan(this);
        this.mWindowInsetsApplied = false;
        requestApplyInsets();
    }

    @Override // android.widget.FrameLayout, android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mInflateNeeded && !this.mWindowOverscan) {
            inflate();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override // android.view.View
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        this.mWindowInsetsApplied = true;
        boolean round = insets.isRound();
        if (round != this.mLastKnownRound) {
            this.mLastKnownRound = round;
            this.mInflateNeeded = true;
        }
        if (this.mInflateNeeded) {
            inflate();
        }
        return insets;
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (this.mWindowOverscan && !this.mWindowInsetsApplied) {
            Log.w(TAG, "onApplyWindowInsets was not called. WatchViewStub should be the the root of your layout. If an OnApplyWindowInsetsListener was attached to this view, it must forward the insets on by calling view.onApplyWindowInsets.");
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    private void inflate() {
        removeAllViews();
        if (this.mRoundLayout == 0 && !isInEditMode()) {
            throw new IllegalStateException("You must supply a roundLayout resource");
        }
        if (this.mRectLayout == 0 && !isInEditMode()) {
            throw new IllegalStateException("You must supply a rectLayout resource");
        }
        int layout = this.mLastKnownRound ? this.mRoundLayout : this.mRectLayout;
        LayoutInflater.from(getContext()).inflate(layout, this);
        this.mInflateNeeded = false;
        if (this.mListener != null) {
            this.mListener.onLayoutInflated(this);
        }
    }
}
