package com.digits.sdk.android;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/* JADX INFO: loaded from: classes18.dex */
public class AccentButton extends Button {
    public AccentButton(Context context) {
        this(context, null);
    }

    public AccentButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.buttonStyle);
    }

    public AccentButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        int accentColor = ThemeUtils.getAccentColor(getResources(), getContext().getTheme());
        ButtonThemer buttonThemer = new ButtonThemer(getResources());
        buttonThemer.disableDropShadow(this);
        buttonThemer.setBackgroundAccentColor(this, accentColor);
        buttonThemer.setTextAccentColor(this, accentColor);
    }
}
