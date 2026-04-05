package com.digits.sdk.android;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import com.digits.sdk.android.DigitsAuthConfig;

/* JADX INFO: loaded from: classes18.dex */
public class DigitsAuthButton extends Button implements View.OnClickListener {
    private DigitsAuthConfig.Builder digitsAuthConfigBuilder;
    volatile DigitsClient digitsClient;
    private View.OnClickListener onClickListener;

    public DigitsAuthButton(Context context) {
        this(context, null);
    }

    public DigitsAuthButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.buttonStyle);
    }

    public DigitsAuthButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setUpButton();
        this.digitsAuthConfigBuilder = new DigitsAuthConfig.Builder();
        super.setOnClickListener(this);
    }

    private void setUpButton() {
        Resources res = getResources();
        setCompoundDrawablePadding(res.getDimensionPixelSize(R.dimen.tw__login_btn_drawable_padding));
        setText(R.string.dgts__login_digits_text);
        setTextColor(res.getColor(R.color.tw__solid_white));
        setTextSize(0, res.getDimensionPixelSize(R.dimen.tw__login_btn_text_size));
        setTypeface(Typeface.DEFAULT_BOLD);
        setPadding(res.getDimensionPixelSize(R.dimen.tw__login_btn_right_padding), 0, res.getDimensionPixelSize(R.dimen.tw__login_btn_right_padding), 0);
        setBackgroundResource(R.drawable.dgts__digits_btn);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        DigitsAuthConfig digitsAuthConfig = this.digitsAuthConfigBuilder.build();
        getDigitsClient().startSignUp(digitsAuthConfig);
        if (this.onClickListener != null) {
            this.onClickListener.onClick(v);
        }
    }

    public void setCallback(AuthCallback callback) {
        this.digitsAuthConfigBuilder.withAuthCallBack(callback);
    }

    public void setAuthTheme(int themeResId) {
        getDigits().setTheme(themeResId);
    }

    @Override // android.view.View
    public void setOnClickListener(View.OnClickListener l) {
        this.onClickListener = l;
    }

    protected DigitsClient getDigitsClient() {
        if (this.digitsClient == null) {
            synchronized (DigitsClient.class) {
                if (this.digitsClient == null) {
                    this.digitsClient = getDigits().getDigitsClient();
                }
            }
        }
        return this.digitsClient;
    }

    protected Digits getDigits() {
        return Digits.getInstance();
    }
}
