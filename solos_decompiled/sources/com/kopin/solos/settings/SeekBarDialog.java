package com.kopin.solos.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import com.kopin.solos.R;
import com.kopin.solos.storage.util.Utility;

/* JADX INFO: loaded from: classes24.dex */
class SeekBarDialog {
    private static final long AUTO_DISMISS_TIMEOUT = 5000;
    private CheckBox mAutoCheckbox;
    private final Runnable mAutoDismiss;
    private OnValueChangedListener mCb;
    private View mCheckboxContainer;
    private AlertDialog mDialog;
    private int[] mIconIds;
    private ImageView mImageView;
    private long mLastTouch;
    private SeekBar mSeekBar;

    interface OnValueChangedListener {
        void onChecked(boolean z, SeekBarDialog seekBarDialog);

        void onValueChanged(int i);
    }

    private SeekBarDialog(Context context, int[] ids, boolean isChecked, OnValueChangedListener cb) {
        this(context, ids, cb);
        this.mCheckboxContainer.setVisibility(0);
        this.mAutoCheckbox.setChecked(isChecked);
        this.mAutoCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.kopin.solos.settings.SeekBarDialog.1
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked2) {
                if (SeekBarDialog.this.mCb != null) {
                    SeekBarDialog.this.mCb.onChecked(isChecked2, SeekBarDialog.this);
                }
            }
        });
    }

    private SeekBarDialog(Context context, int[] ids, OnValueChangedListener cb) {
        this.mAutoDismiss = new Runnable() { // from class: com.kopin.solos.settings.SeekBarDialog.3
            @Override // java.lang.Runnable
            public void run() {
                if (SeekBarDialog.this.mDialog.isShowing()) {
                    long now = Utility.getTimeMilliseconds();
                    if (now - SeekBarDialog.this.mLastTouch > 5000) {
                        SeekBarDialog.this.mDialog.dismiss();
                    } else {
                        SeekBarDialog.this.mSeekBar.postDelayed(SeekBarDialog.this.mAutoDismiss, 1000L);
                    }
                }
            }
        };
        this.mIconIds = ids;
        this.mCb = cb;
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_seekbar, (ViewGroup) null);
        this.mImageView = (ImageView) view.findViewById(android.R.id.icon);
        this.mSeekBar = (SeekBar) view.findViewById(R.id.seek_bar);
        this.mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.kopin.solos.settings.SeekBarDialog.2
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float percent = progress / seekBar.getMax();
                SeekBarDialog.this.setProgressIcon(SeekBarDialog.this.mImageView, percent, SeekBarDialog.this.mIconIds);
                if (fromUser && SeekBarDialog.this.mCb != null) {
                    SeekBarDialog.this.mCb.onValueChanged(progress);
                }
                SeekBarDialog.this.mLastTouch = Utility.getTimeMilliseconds();
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (SeekBarDialog.this.mAutoCheckbox != null && SeekBarDialog.this.mAutoCheckbox.isChecked()) {
                    SeekBarDialog.this.mAutoCheckbox.setChecked(false);
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        this.mCheckboxContainer = view.findViewById(R.id.checkBoxContainer);
        this.mAutoCheckbox = (CheckBox) view.findViewById(R.id.checkboxAuto);
        this.mDialog = new AlertDialog.Builder(context).setInverseBackgroundForced(true).setView(view).create();
        this.mDialog.setCanceledOnTouchOutside(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setProgressIcon(ImageView imageView, float percent, int[] iconIds) {
        if (percent < 0.09d) {
            imageView.setImageResource(iconIds[0]);
        } else if (percent >= 0.91d) {
            imageView.setImageResource(iconIds[2]);
        } else {
            imageView.setImageResource(iconIds[1]);
        }
    }

    private void show(int curVal, int max) {
        this.mSeekBar.setMax(max);
        this.mSeekBar.setProgress(curVal);
        this.mDialog.show();
        this.mLastTouch = Utility.getTimeMilliseconds();
        this.mSeekBar.postDelayed(this.mAutoDismiss, 2000L);
    }

    public void setProgress(int progress) {
        this.mSeekBar.setProgress(progress);
    }

    public static void show(Context context, int[] iconIds, int current, int max, boolean isChecked, OnValueChangedListener cb) {
        SeekBarDialog self = new SeekBarDialog(context, iconIds, isChecked, cb);
        self.show(current, max);
    }

    public static void show(Context context, int[] iconIds, int current, int max, OnValueChangedListener cb) {
        SeekBarDialog self = new SeekBarDialog(context, iconIds, cb);
        self.show(current, max);
    }
}
