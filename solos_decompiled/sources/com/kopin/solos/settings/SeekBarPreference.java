package com.kopin.solos.settings;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import com.kopin.solos.R;

/* JADX INFO: loaded from: classes24.dex */
public class SeekBarPreference extends Preference implements SeekBar.OnSeekBarChangeListener {
    private Drawable mBackgroundResource;
    private Context mContext;
    private Handler mHandler;
    private int mMaxValue;
    private String mMaxValueLabel;
    private int mMinValue;
    private String mMinValueLabel;
    private int mProgress;
    private SeekBar mSeekBar;
    private boolean mShowSeekBarValue;
    private String mTextSuffix;
    private TextView mtextSeekBarValue;

    public SeekBarPreference(Context context) {
        super(context);
        this.mMinValueLabel = "";
        this.mMaxValueLabel = "";
        this.mMinValue = 0;
        this.mMaxValue = 10;
        this.mTextSuffix = "%";
        this.mBackgroundResource = null;
        this.mHandler = new Handler();
        this.mShowSeekBarValue = false;
        this.mContext = context;
    }

    public SeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mMinValueLabel = "";
        this.mMaxValueLabel = "";
        this.mMinValue = 0;
        this.mMaxValue = 10;
        this.mTextSuffix = "%";
        this.mBackgroundResource = null;
        this.mHandler = new Handler();
        this.mShowSeekBarValue = false;
        this.mContext = context;
        parseAttributes(context, attrs);
    }

    public SeekBarPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mMinValueLabel = "";
        this.mMaxValueLabel = "";
        this.mMinValue = 0;
        this.mMaxValue = 10;
        this.mTextSuffix = "%";
        this.mBackgroundResource = null;
        this.mHandler = new Handler();
        this.mShowSeekBarValue = false;
        this.mContext = context;
        parseAttributes(context, attrs);
    }

    public void setTextSuffix(String text) {
        this.mTextSuffix = text;
    }

    private void parseAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SeekBarPreference);
        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case 0:
                    this.mProgress = a.getInt(attr, 0);
                    break;
                case 1:
                    this.mMinValueLabel = a.getString(attr);
                    break;
                case 2:
                    this.mMaxValueLabel = a.getString(attr);
                    break;
                case 3:
                    this.mMinValue = a.getInt(attr, 0);
                    break;
                case 4:
                    this.mMaxValue = a.getInt(attr, 10);
                    break;
                case 5:
                    this.mShowSeekBarValue = a.getBoolean(attr, false);
                    break;
                case 6:
                    this.mBackgroundResource = a.getDrawable(attr);
                    break;
            }
        }
        this.mProgress -= this.mMinValue;
        a.recycle();
    }

    @Override // android.preference.Preference
    protected View onCreateView(ViewGroup parent) {
        super.onCreateView(parent);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService("layout_inflater");
        View view = inflater.inflate(R.layout.preference_seekbar, parent, false);
        this.mSeekBar = (SeekBar) view.findViewById(R.id.seekbar);
        this.mSeekBar.setProgress(this.mProgress);
        this.mSeekBar.setOnSeekBarChangeListener(this);
        this.mSeekBar.setMax(this.mMaxValue - this.mMinValue);
        if (this.mBackgroundResource != null) {
            this.mSeekBar.setBackground(this.mBackgroundResource);
        }
        TextView txtMin = (TextView) view.findViewById(R.id.txtMin);
        TextView txtMax = (TextView) view.findViewById(R.id.txtMax);
        txtMin.setText(this.mMinValueLabel);
        txtMax.setText(this.mMaxValueLabel);
        this.mtextSeekBarValue = (TextView) view.findViewById(R.id.textSeekBarValue);
        this.mtextSeekBarValue.setVisibility(this.mShowSeekBarValue ? 0 : 8);
        return view;
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onProgressChanged(final SeekBar seekBar, final int progress, boolean fromUser) {
        this.mHandler.post(new Runnable() { // from class: com.kopin.solos.settings.SeekBarPreference.1
            @Override // java.lang.Runnable
            public void run() {
                if (seekBar != null && SeekBarPreference.this.mtextSeekBarValue.getVisibility() == 0) {
                    int val = (progress * (seekBar.getWidth() - (seekBar.getThumbOffset() * 2))) / seekBar.getMax();
                    if (SeekBarPreference.this.mtextSeekBarValue != null) {
                        SeekBarPreference.this.mtextSeekBarValue.setText("" + (SeekBarPreference.this.mMinValue + progress) + SeekBarPreference.this.mTextSuffix);
                        float x = seekBar.getX() + val + (seekBar.getThumbOffset() / 2);
                        SeekBarPreference.this.mtextSeekBarValue.setX(x);
                    }
                }
            }
        });
        if (fromUser) {
            setValue(progress);
        }
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override // android.preference.Preference
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        setValue(restoreValue ? getPersistedInt(this.mProgress) - this.mMinValue : ((Integer) defaultValue).intValue());
    }

    public void setValue(int value) {
        if (shouldPersist()) {
            persistInt(this.mMinValue + value);
        }
        if (value != this.mProgress) {
            this.mProgress = value;
        }
    }
}
