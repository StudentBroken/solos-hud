package com.kopin.solos.navigation;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.kopin.solos.R;
import java.util.List;

/* JADX INFO: loaded from: classes24.dex */
public class RouteCardView extends FrameLayout {
    private static final long ANIMATION_DURATION = 300;
    private ImageView mChevron;
    private final TextView mDistance;
    private final TextView mDistanceUnit;
    private ViewGroup mFrontContainer;
    private ViewGroup mMainContainer;
    private final TextView mTitle;

    public RouteCardView(Context context) {
        this(context, null);
    }

    public RouteCardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RouteCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addView(View.inflate(getContext(), R.layout.routes_result_list_item, null));
        this.mMainContainer = (ViewGroup) findViewById(R.id.card_container);
        this.mFrontContainer = (ViewGroup) findViewById(R.id.card_front);
        this.mChevron = (ImageView) findViewById(R.id.card_chevron);
        this.mTitle = (TextView) findViewById(R.id.route_name);
        this.mDistance = (TextView) findViewById(R.id.route_distance);
        this.mDistanceUnit = (TextView) findViewById(R.id.route_distance_unit);
    }

    public void setTheme(boolean light) {
        Drawable background1;
        int tint;
        if (light) {
            background1 = getResources().getDrawable(R.color.element_background_lighter2);
            tint = getResources().getColor(R.color.navigation_orange);
        } else {
            background1 = getResources().getDrawable(R.color.element_background_dark2);
            tint = getResources().getColor(R.color.navigation_white);
        }
        this.mMainContainer.setBackground(background1);
        this.mFrontContainer.setBackground(background1);
        this.mChevron.setColorFilter(tint);
    }

    public void setTitle(String title) {
        this.mTitle.setText(title);
    }

    public void setDistance(List<String> distanceUnits) {
        this.mDistance.setText(distanceUnits.get(0));
        this.mDistanceUnit.setText(distanceUnits.get(1).toUpperCase());
    }

    public void disableOnClick() {
        this.mMainContainer.setClickable(false);
        this.mMainContainer.setFocusable(false);
    }
}
