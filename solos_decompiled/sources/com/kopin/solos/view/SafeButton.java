package com.kopin.solos.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/* JADX INFO: loaded from: classes48.dex */
public class SafeButton extends Button {
    private volatile boolean handlingClick;

    public SafeButton(Context context) {
        super(context);
        this.handlingClick = false;
    }

    public SafeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.handlingClick = false;
    }

    public SafeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.handlingClick = false;
    }

    @Override // android.view.View
    public boolean performClick() {
        if (this.handlingClick || getContext() == null || ((getContext() instanceof Activity) && ((Activity) getContext()).isFinishing())) {
            return false;
        }
        this.handlingClick = true;
        boolean zPerformClick = super.performClick();
        this.handlingClick = false;
        return zPerformClick;
    }

    public void reset() {
        this.handlingClick = false;
    }

    @Override // android.widget.TextView, android.view.View
    public void setEnabled(boolean enabled) {
        reset();
        super.setEnabled(enabled);
    }
}
