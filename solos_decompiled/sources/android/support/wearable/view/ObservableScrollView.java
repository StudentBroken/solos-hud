package android.support.wearable.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/* JADX INFO: loaded from: classes33.dex */
public class ObservableScrollView extends ScrollView {
    private OnScrollListener mOnScrollListener;

    public interface OnScrollListener {
        void onScroll(float f);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override // android.view.View
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (this.mOnScrollListener != null) {
            this.mOnScrollListener.onScroll(oldt - t);
        }
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.mOnScrollListener = onScrollListener;
    }
}
