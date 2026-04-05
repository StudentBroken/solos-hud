package com.google.android.gms.common.internal;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.widget.Button;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbz extends Button {
    public zzbz(Context context) {
        this(context, null);
    }

    private zzbz(Context context, AttributeSet attributeSet) {
        super(context, null, R.attr.buttonStyle);
    }

    private static int zzf(int i, int i2, int i3, int i4) {
        switch (i) {
            case 0:
                return i2;
            case 1:
                return i3;
            case 2:
                return i4;
            default:
                throw new IllegalStateException(new StringBuilder(33).append("Unknown color scheme: ").append(i).toString());
        }
    }

    public final void zza(Resources resources, int i, int i2) {
        setTypeface(Typeface.DEFAULT_BOLD);
        setTextSize(14.0f);
        float f = resources.getDisplayMetrics().density;
        setMinHeight((int) ((f * 48.0f) + 0.5f));
        setMinWidth((int) ((f * 48.0f) + 0.5f));
        int iZzf = zzf(i2, com.google.android.gms.R.drawable.common_google_signin_btn_icon_dark, com.google.android.gms.R.drawable.common_google_signin_btn_icon_light, com.google.android.gms.R.drawable.common_google_signin_btn_icon_light);
        int iZzf2 = zzf(i2, com.google.android.gms.R.drawable.common_google_signin_btn_text_dark, com.google.android.gms.R.drawable.common_google_signin_btn_text_light, com.google.android.gms.R.drawable.common_google_signin_btn_text_light);
        switch (i) {
            case 0:
            case 1:
                break;
            case 2:
                iZzf2 = iZzf;
                break;
            default:
                throw new IllegalStateException(new StringBuilder(32).append("Unknown button size: ").append(i).toString());
        }
        Drawable drawableWrap = DrawableCompat.wrap(resources.getDrawable(iZzf2));
        DrawableCompat.setTintList(drawableWrap, resources.getColorStateList(com.google.android.gms.R.color.common_google_signin_btn_tint));
        DrawableCompat.setTintMode(drawableWrap, PorterDuff.Mode.SRC_ATOP);
        setBackgroundDrawable(drawableWrap);
        setTextColor((ColorStateList) zzbr.zzu(resources.getColorStateList(zzf(i2, com.google.android.gms.R.color.common_google_signin_btn_text_dark, com.google.android.gms.R.color.common_google_signin_btn_text_light, com.google.android.gms.R.color.common_google_signin_btn_text_light))));
        switch (i) {
            case 0:
                setText(resources.getString(com.google.android.gms.R.string.common_signin_button_text));
                break;
            case 1:
                setText(resources.getString(com.google.android.gms.R.string.common_signin_button_text_long));
                break;
            case 2:
                setText((CharSequence) null);
                break;
            default:
                throw new IllegalStateException(new StringBuilder(32).append("Unknown button size: ").append(i).toString());
        }
        setTransformationMethod(null);
        if (com.google.android.gms.common.util.zzk.zzaG(getContext())) {
            setGravity(19);
        }
    }
}
