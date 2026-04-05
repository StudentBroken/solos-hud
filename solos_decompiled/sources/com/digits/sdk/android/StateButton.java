package com.digits.sdk.android;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/* JADX INFO: loaded from: classes18.dex */
public class StateButton extends RelativeLayout {
    int accentColor;
    ButtonThemer buttonThemer;
    CharSequence finishText;
    ImageView imageView;
    ProgressBar progressBar;
    CharSequence progressText;
    CharSequence startText;
    TextView textView;

    public StateButton(Context context) {
        this(context, null);
    }

    public StateButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.StateButton);
        init(array);
        array.recycle();
        this.accentColor = ThemeUtils.getAccentColor(getResources(), context.getTheme());
        this.buttonThemer = new ButtonThemer(getResources());
        this.buttonThemer.setBackgroundAccentColor(this, this.accentColor);
        this.buttonThemer.setTextAccentColor(this.textView, this.accentColor);
        setImageAccentColor();
        setSpinnerAccentColor();
    }

    void setImageAccentColor() {
        this.imageView.setColorFilter(getTextColor(), PorterDuff.Mode.SRC_IN);
    }

    void setSpinnerAccentColor() {
        this.progressBar.setIndeterminateDrawable(getProgressDrawable());
    }

    int getTextColor() {
        return this.buttonThemer.getTextColor(this.accentColor);
    }

    Drawable getProgressDrawable() {
        return ThemeUtils.isLightColor(this.accentColor) ? getResources().getDrawable(R.drawable.progress_dark) : getResources().getDrawable(R.drawable.progress_light);
    }

    public void setStatesText(int startResId, int progressResId, int finishResId) {
        Context context = getContext();
        this.startText = context.getString(startResId);
        this.progressText = context.getString(progressResId);
        this.finishText = context.getString(finishResId);
    }

    void init(TypedArray array) {
        this.startText = array.getText(R.styleable.StateButton_startStateText);
        this.progressText = array.getText(R.styleable.StateButton_progressStateText);
        this.finishText = array.getText(R.styleable.StateButton_finishStateText);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.dgts__state_button, this);
        this.textView = (TextView) findViewById(R.id.dgts__state_button);
        this.progressBar = (ProgressBar) findViewById(R.id.dgts__state_progress);
        this.imageView = (ImageView) findViewById(R.id.dgts__state_success);
        showStart();
    }

    public void showProgress() {
        setClickable(false);
        this.textView.setText(this.progressText);
        this.progressBar.setVisibility(0);
        this.imageView.setVisibility(8);
    }

    public void showFinish() {
        setClickable(false);
        this.textView.setText(this.finishText);
        this.progressBar.setVisibility(8);
        this.imageView.setVisibility(0);
    }

    public void showError() {
        showStart();
    }

    public void showStart() {
        setClickable(true);
        this.textView.setText(this.startText);
        this.progressBar.setVisibility(8);
        this.imageView.setVisibility(8);
    }
}
