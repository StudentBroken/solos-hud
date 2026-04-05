package com.digits.sdk.android;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.ImageView;

/* JADX INFO: loaded from: classes18.dex */
public class AccentImageView extends ImageView {
    public AccentImageView(Context context) {
        super(context);
        init(context);
    }

    public AccentImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AccentImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        int accentColor = ThemeUtils.getAccentColor(getResources(), context.getTheme());
        setColorFilter(accentColor, PorterDuff.Mode.SRC_IN);
    }
}
