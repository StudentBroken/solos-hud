package com.digits.sdk.android;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.StateSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

/* JADX INFO: loaded from: classes18.dex */
class ButtonThemer {
    private final Resources resources;

    public ButtonThemer(Resources resources) {
        this.resources = resources;
    }

    @TargetApi(16)
    void setBackgroundAccentColor(View view, int accentColor) {
        StateListDrawable background = new StateListDrawable();
        float radius = TypedValue.applyDimension(1, 5.0f, this.resources.getDisplayMetrics());
        GradientDrawable tmp = new GradientDrawable();
        tmp.setCornerRadius(radius);
        tmp.setColor(getPressedColor(accentColor));
        background.addState(new int[]{android.R.attr.state_focused, android.R.attr.state_pressed}, tmp);
        background.addState(new int[]{-16842908, android.R.attr.state_pressed}, tmp);
        background.addState(new int[]{android.R.attr.state_focused}, tmp);
        GradientDrawable tmp2 = new GradientDrawable();
        tmp2.setColor(accentColor);
        tmp2.setCornerRadius(radius);
        background.addState(StateSet.WILD_CARD, tmp2);
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }

    @TargetApi(16)
    void setBackgroundAccentColorInverse(View view, int accentColor) {
        StateListDrawable background = new StateListDrawable();
        float radius = TypedValue.applyDimension(1, 5.0f, this.resources.getDisplayMetrics());
        float strokeWidth = TypedValue.applyDimension(1, 2.0f, this.resources.getDisplayMetrics());
        GradientDrawable tmp = new GradientDrawable();
        tmp.setCornerRadius(radius);
        tmp.setStroke((int) strokeWidth, getPressedColor(accentColor));
        background.addState(new int[]{android.R.attr.state_focused, android.R.attr.state_pressed}, tmp);
        background.addState(new int[]{-16842908, android.R.attr.state_pressed}, tmp);
        background.addState(new int[]{android.R.attr.state_focused}, tmp);
        GradientDrawable tmp2 = new GradientDrawable();
        tmp2.setCornerRadius(radius);
        tmp2.setStroke((int) strokeWidth, accentColor);
        background.addState(StateSet.WILD_CARD, tmp2);
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }

    private int getPressedColor(int accentColor) {
        return ThemeUtils.isLightColor(accentColor) ? ThemeUtils.calculateOpacityTransform(0.2d, ViewCompat.MEASURED_STATE_MASK, accentColor) : ThemeUtils.calculateOpacityTransform(0.2d, -1, accentColor);
    }

    void setTextAccentColor(TextView view, int accentColor) {
        view.setTextColor(getTextColor(accentColor));
    }

    void setTextAccentColorInverse(TextView view, int accentColor) {
        int pressedColor = getPressedColor(accentColor);
        int[][] states = {new int[]{android.R.attr.state_focused, -16842919}, new int[]{android.R.attr.state_focused, android.R.attr.state_pressed}, new int[]{-16842908, android.R.attr.state_pressed}, StateSet.WILD_CARD};
        int[] colors = {accentColor, pressedColor, pressedColor, accentColor};
        ColorStateList stateList = new ColorStateList(states, colors);
        view.setTextColor(stateList);
    }

    int getTextColor(int accentColor) {
        return ThemeUtils.isLightColor(accentColor) ? this.resources.getColor(R.color.dgts__text_dark) : this.resources.getColor(R.color.dgts__text_light);
    }

    @TargetApi(21)
    void disableDropShadow(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            view.setStateListAnimator(null);
            view.setElevation(0.0f);
        }
    }
}
