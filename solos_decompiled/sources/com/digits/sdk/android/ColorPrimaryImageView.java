package com.digits.sdk.android;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.ImageView;

/* JADX INFO: loaded from: classes18.dex */
public class ColorPrimaryImageView extends ImageView {
    public ColorPrimaryImageView(Context context) {
        super(context);
        init(context);
    }

    public ColorPrimaryImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorPrimaryImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setColorFilter(getTextColorPrimary(context), PorterDuff.Mode.SRC_IN);
    }

    private int getTextColorPrimary(Context context) {
        TypedArray array = context.getTheme().obtainStyledAttributes(new int[]{android.R.attr.textColorPrimary});
        int color = array.getColor(0, getResources().getColor(R.color.dgts__default_logo_name));
        array.recycle();
        return color;
    }
}
