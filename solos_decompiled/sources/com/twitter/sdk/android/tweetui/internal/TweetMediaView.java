package com.twitter.sdk.android.tweetui.internal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.twitter.sdk.android.core.internal.util.AspectRatioImageView;

/* JADX INFO: loaded from: classes9.dex */
public class TweetMediaView extends AspectRatioImageView {
    Overlay overlay;

    public TweetMediaView(Context context) {
        super(context);
        this.overlay = new Overlay(null);
    }

    public TweetMediaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.overlay = new Overlay(null);
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.overlay.draw(canvas);
    }

    @Override // android.widget.ImageView, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        this.overlay.setDrawableState(getDrawableState());
    }

    @Override // com.twitter.sdk.android.core.internal.util.AspectRatioImageView, android.widget.ImageView, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.overlay.setDrawableBounds(getMeasuredWidth(), getMeasuredHeight());
    }

    @Override // android.view.View
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        this.overlay.setDrawableBounds(width, height);
    }

    @Override // android.widget.ImageView, android.view.View, android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable drawable) {
        if (drawable == this.overlay.drawable) {
            invalidate();
        } else {
            super.invalidateDrawable(drawable);
        }
    }

    public void setOverlayDrawable(Drawable drawable) {
        this.overlay.cleanupDrawable(this);
        if (drawable != null) {
            drawable.setCallback(this);
        }
        this.overlay = new Overlay(drawable);
        this.overlay.setDrawableState(getDrawableState());
        requestLayout();
    }

    protected static class Overlay {
        final Drawable drawable;

        Overlay(Drawable drawable) {
            this.drawable = drawable;
        }

        protected void cleanupDrawable(ImageView imageView) {
            if (this.drawable != null) {
                this.drawable.setCallback(null);
                imageView.unscheduleDrawable(this.drawable);
            }
        }

        protected void setDrawableBounds(int width, int height) {
            if (this.drawable != null) {
                this.drawable.setBounds(0, 0, width, height);
            }
        }

        protected void setDrawableState(int[] state) {
            if (this.drawable != null && this.drawable.isStateful()) {
                this.drawable.setState(state);
            }
        }

        protected void draw(Canvas canvas) {
            if (this.drawable != null) {
                this.drawable.draw(canvas);
            }
        }
    }
}
