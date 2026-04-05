package com.twitter.sdk.android.core.internal.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/* JADX INFO: loaded from: classes62.dex */
public class ObservableScrollView extends ScrollView {
    ScrollViewListener scrollViewListener;

    public interface ScrollViewListener {
        void onScrollChanged(int i);
    }

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override // android.view.View
    protected void onScrollChanged(int currentX, int currentY, int oldX, int oldY) {
        super.onScrollChanged(currentX, currentY, oldX, oldY);
        if (this.scrollViewListener != null) {
            this.scrollViewListener.onScrollChanged(currentY);
        }
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }
}
