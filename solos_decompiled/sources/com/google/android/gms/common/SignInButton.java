package com.google.android.gms.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import com.google.android.gms.R;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.zzby;
import com.google.android.gms.common.internal.zzbz;
import com.google.android.gms.dynamic.zzq;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* JADX INFO: loaded from: classes3.dex */
public final class SignInButton extends FrameLayout implements View.OnClickListener {
    public static final int COLOR_AUTO = 2;
    public static final int COLOR_DARK = 0;
    public static final int COLOR_LIGHT = 1;
    public static final int SIZE_ICON_ONLY = 2;
    public static final int SIZE_STANDARD = 0;
    public static final int SIZE_WIDE = 1;
    private int mColor;
    private int mSize;
    private View zzaAx;
    private View.OnClickListener zzaAy;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ButtonSize {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ColorScheme {
    }

    public SignInButton(Context context) {
        this(context, null);
    }

    public SignInButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SignInButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.zzaAy = null;
        TypedArray typedArrayObtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.SignInButton, 0, 0);
        try {
            this.mSize = typedArrayObtainStyledAttributes.getInt(R.styleable.SignInButton_buttonSize, 0);
            this.mColor = typedArrayObtainStyledAttributes.getInt(R.styleable.SignInButton_colorScheme, 2);
            typedArrayObtainStyledAttributes.recycle();
            setStyle(this.mSize, this.mColor);
        } catch (Throwable th) {
            typedArrayObtainStyledAttributes.recycle();
            throw th;
        }
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        if (this.zzaAy == null || view != this.zzaAx) {
            return;
        }
        this.zzaAy.onClick(this);
    }

    public final void setColorScheme(int i) {
        setStyle(this.mSize, i);
    }

    @Override // android.view.View
    public final void setEnabled(boolean z) {
        super.setEnabled(z);
        this.zzaAx.setEnabled(z);
    }

    @Override // android.view.View
    public final void setOnClickListener(View.OnClickListener onClickListener) {
        this.zzaAy = onClickListener;
        if (this.zzaAx != null) {
            this.zzaAx.setOnClickListener(this);
        }
    }

    @Deprecated
    public final void setScopes(Scope[] scopeArr) {
        setStyle(this.mSize, this.mColor);
    }

    public final void setSize(int i) {
        setStyle(i, this.mColor);
    }

    public final void setStyle(int i, int i2) {
        this.mSize = i;
        this.mColor = i2;
        Context context = getContext();
        if (this.zzaAx != null) {
            removeView(this.zzaAx);
        }
        try {
            this.zzaAx = zzby.zzc(context, this.mSize, this.mColor);
        } catch (zzq e) {
            Log.w("SignInButton", "Sign in button not found, using placeholder instead");
            int i3 = this.mSize;
            int i4 = this.mColor;
            zzbz zzbzVar = new zzbz(context);
            zzbzVar.zza(context.getResources(), i3, i4);
            this.zzaAx = zzbzVar;
        }
        addView(this.zzaAx);
        this.zzaAx.setEnabled(isEnabled());
        this.zzaAx.setOnClickListener(this);
    }

    @Deprecated
    public final void setStyle(int i, int i2, Scope[] scopeArr) {
        setStyle(i, i2);
    }
}
