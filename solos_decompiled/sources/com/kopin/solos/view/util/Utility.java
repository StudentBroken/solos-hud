package com.kopin.solos.view.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/* JADX INFO: loaded from: classes48.dex */
public class Utility {
    public static void setTextColor(View view, int color) {
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(color);
        } else if (view instanceof ViewGroup) {
            setTextColor((ViewGroup) view, color);
        }
    }

    public static void setTextColor(ViewGroup v, int color) {
        for (int i = 0; i < v.getChildCount(); i++) {
            View view = v.getChildAt(i);
            setTextColor(view, color);
        }
    }
}
