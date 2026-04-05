package com.digits.sdk.android;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

/* JADX INFO: loaded from: classes18.dex */
public class LinkTextView extends TextView {
    public LinkTextView(Context context) {
        super(context);
        init(context);
    }

    public LinkTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LinkTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setTextColor(getLinkColor(context));
    }

    private int getLinkColor(Context context) {
        TypedValue value = ThemeUtils.getTypedValueColor(context.getTheme(), android.R.attr.textColorLink);
        return value == null ? ThemeUtils.getAccentColor(context.getResources(), context.getTheme()) : value.data;
    }
}
