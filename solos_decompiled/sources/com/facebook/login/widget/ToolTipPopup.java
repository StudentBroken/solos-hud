package com.facebook.login.widget;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.facebook.R;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes41.dex */
public class ToolTipPopup {
    public static final long DEFAULT_POPUP_DISPLAY_TIME = 6000;
    private final WeakReference<View> mAnchorViewRef;
    private final Context mContext;
    private PopupContentView mPopupContent;
    private PopupWindow mPopupWindow;
    private final String mText;
    private Style mStyle = Style.BLUE;
    private long mNuxDisplayTime = DEFAULT_POPUP_DISPLAY_TIME;
    private final ViewTreeObserver.OnScrollChangedListener mScrollListener = new ViewTreeObserver.OnScrollChangedListener() { // from class: com.facebook.login.widget.ToolTipPopup.1
        @Override // android.view.ViewTreeObserver.OnScrollChangedListener
        public void onScrollChanged() {
            if (ToolTipPopup.this.mAnchorViewRef.get() != null && ToolTipPopup.this.mPopupWindow != null && ToolTipPopup.this.mPopupWindow.isShowing()) {
                if (ToolTipPopup.this.mPopupWindow.isAboveAnchor()) {
                    ToolTipPopup.this.mPopupContent.showBottomArrow();
                } else {
                    ToolTipPopup.this.mPopupContent.showTopArrow();
                }
            }
        }
    };

    public enum Style {
        BLUE,
        BLACK
    }

    public ToolTipPopup(String text, View anchor) {
        this.mText = text;
        this.mAnchorViewRef = new WeakReference<>(anchor);
        this.mContext = anchor.getContext();
    }

    public void setStyle(Style mStyle) {
        this.mStyle = mStyle;
    }

    public void show() {
        if (this.mAnchorViewRef.get() != null) {
            this.mPopupContent = new PopupContentView(this.mContext);
            TextView body = (TextView) this.mPopupContent.findViewById(R.id.com_facebook_tooltip_bubble_view_text_body);
            body.setText(this.mText);
            if (this.mStyle == Style.BLUE) {
                this.mPopupContent.bodyFrame.setBackgroundResource(R.drawable.com_facebook_tooltip_blue_background);
                this.mPopupContent.bottomArrow.setImageResource(R.drawable.com_facebook_tooltip_blue_bottomnub);
                this.mPopupContent.topArrow.setImageResource(R.drawable.com_facebook_tooltip_blue_topnub);
                this.mPopupContent.xOut.setImageResource(R.drawable.com_facebook_tooltip_blue_xout);
            } else {
                this.mPopupContent.bodyFrame.setBackgroundResource(R.drawable.com_facebook_tooltip_black_background);
                this.mPopupContent.bottomArrow.setImageResource(R.drawable.com_facebook_tooltip_black_bottomnub);
                this.mPopupContent.topArrow.setImageResource(R.drawable.com_facebook_tooltip_black_topnub);
                this.mPopupContent.xOut.setImageResource(R.drawable.com_facebook_tooltip_black_xout);
            }
            Window window = ((Activity) this.mContext).getWindow();
            View decorView = window.getDecorView();
            int decorWidth = decorView.getWidth();
            int decorHeight = decorView.getHeight();
            registerObserver();
            this.mPopupContent.measure(View.MeasureSpec.makeMeasureSpec(decorWidth, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(decorHeight, Integer.MIN_VALUE));
            this.mPopupWindow = new PopupWindow(this.mPopupContent, this.mPopupContent.getMeasuredWidth(), this.mPopupContent.getMeasuredHeight());
            this.mPopupWindow.showAsDropDown(this.mAnchorViewRef.get());
            updateArrows();
            if (this.mNuxDisplayTime > 0) {
                this.mPopupContent.postDelayed(new Runnable() { // from class: com.facebook.login.widget.ToolTipPopup.2
                    @Override // java.lang.Runnable
                    public void run() {
                        ToolTipPopup.this.dismiss();
                    }
                }, this.mNuxDisplayTime);
            }
            this.mPopupWindow.setTouchable(true);
            this.mPopupContent.setOnClickListener(new View.OnClickListener() { // from class: com.facebook.login.widget.ToolTipPopup.3
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    ToolTipPopup.this.dismiss();
                }
            });
        }
    }

    public void setNuxDisplayTime(long displayTime) {
        this.mNuxDisplayTime = displayTime;
    }

    private void updateArrows() {
        if (this.mPopupWindow != null && this.mPopupWindow.isShowing()) {
            if (this.mPopupWindow.isAboveAnchor()) {
                this.mPopupContent.showBottomArrow();
            } else {
                this.mPopupContent.showTopArrow();
            }
        }
    }

    public void dismiss() {
        unregisterObserver();
        if (this.mPopupWindow != null) {
            this.mPopupWindow.dismiss();
        }
    }

    private void registerObserver() {
        unregisterObserver();
        if (this.mAnchorViewRef.get() != null) {
            this.mAnchorViewRef.get().getViewTreeObserver().addOnScrollChangedListener(this.mScrollListener);
        }
    }

    private void unregisterObserver() {
        if (this.mAnchorViewRef.get() != null) {
            this.mAnchorViewRef.get().getViewTreeObserver().removeOnScrollChangedListener(this.mScrollListener);
        }
    }

    private class PopupContentView extends FrameLayout {
        private View bodyFrame;
        private ImageView bottomArrow;
        private ImageView topArrow;
        private ImageView xOut;

        public PopupContentView(Context context) {
            super(context);
            init();
        }

        private void init() {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            inflater.inflate(R.layout.com_facebook_tooltip_bubble, this);
            this.topArrow = (ImageView) findViewById(R.id.com_facebook_tooltip_bubble_view_top_pointer);
            this.bottomArrow = (ImageView) findViewById(R.id.com_facebook_tooltip_bubble_view_bottom_pointer);
            this.bodyFrame = findViewById(R.id.com_facebook_body_frame);
            this.xOut = (ImageView) findViewById(R.id.com_facebook_button_xout);
        }

        public void showTopArrow() {
            this.topArrow.setVisibility(0);
            this.bottomArrow.setVisibility(4);
        }

        public void showBottomArrow() {
            this.topArrow.setVisibility(4);
            this.bottomArrow.setVisibility(0);
        }
    }
}
