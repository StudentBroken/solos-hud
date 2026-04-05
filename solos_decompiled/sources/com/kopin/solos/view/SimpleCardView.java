package com.kopin.solos.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.kopin.solos.view.util.Utility;

/* JADX INFO: loaded from: classes48.dex */
public class SimpleCardView extends FrameLayout {
    private ImageView mImage;
    private ViewGroup mImageContainer;
    private ViewGroup mMainContainer;
    private TextView mMetric;
    private TextView mSubtitle;
    private TextView mValue;

    public SimpleCardView(Context context) {
        super(context);
        init();
    }

    public SimpleCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SimpleCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.card_template_simple, (ViewGroup) this, true);
        this.mMainContainer = (ViewGroup) view.findViewById(R.id.card_container_simple);
        this.mImageContainer = (ViewGroup) this.mMainContainer.findViewById(R.id.card_image_container_simple);
        this.mImage = (ImageView) this.mImageContainer.findViewById(R.id.card_image_simple);
        this.mValue = (TextView) this.mMainContainer.findViewById(R.id.card_value_simple);
        this.mMetric = (TextView) this.mMainContainer.findViewById(R.id.card_metric_simple);
        this.mSubtitle = (TextView) this.mMainContainer.findViewById(R.id.card_subtitle);
    }

    public void setTheme(boolean light) {
        Drawable background1;
        Drawable background2;
        int tint;
        if (light) {
            background1 = getResources().getDrawable(R.color.element_background_lighter2);
            background2 = getResources().getDrawable(R.color.element_background_lighter2);
            tint = getResources().getColor(R.color.element_background_light1);
        } else {
            background1 = getResources().getDrawable(R.color.element_background_dark2);
            background2 = getResources().getDrawable(R.color.element_background_dark2);
            tint = getResources().getColor(R.color.element_background_light1);
        }
        this.mImageContainer.setBackground(background2);
        this.mMainContainer.setBackground(background1);
        this.mImage.setColorFilter(tint);
        Utility.setTextColor((ViewGroup) this, tint);
    }

    public void setImage(int image) {
        this.mImage.setImageResource(image);
    }

    public void setValue(String value) {
        this.mValue.setText(value);
    }

    public void setValue(int value) {
        this.mValue.setText(value);
    }

    public void setMetric(String metric) {
        this.mMetric.setText(metric);
    }

    public void setMetric(int metric) {
        this.mMetric.setText(metric);
    }

    public void setSubtitle(String subtitle) {
        this.mSubtitle.setText(subtitle);
        this.mSubtitle.setVisibility(0);
    }

    public void setSubtitle(int subtitle) {
        this.mSubtitle.setText(subtitle);
        this.mSubtitle.setVisibility(0);
    }
}
