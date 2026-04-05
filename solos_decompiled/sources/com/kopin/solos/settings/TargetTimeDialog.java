package com.kopin.solos.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.intoware.numberpicker.NumberPicker;
import com.kopin.solos.R;
import com.kopin.solos.common.DialogUtils;
import com.kopin.solos.settings.TargetDialog;
import com.kopin.solos.storage.settings.TargetPreference;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.Utility;

/* JADX INFO: loaded from: classes24.dex */
public class TargetTimeDialog {
    private View layoutNumberPickerHrs;
    private AlertDialog mDialog;
    private NumberPicker mHoursPicker;
    private NumberPicker mMinutesPicker;
    private final TargetDialog.TargetChangedListener mObs;
    private NumberPicker mSecondsPicker;
    private final TargetPreference mTargetType;
    private final String prefKey;
    private Integer mStepSeconds = 1;
    private final DialogInterface.OnClickListener mOnOKCLick = new DialogInterface.OnClickListener() { // from class: com.kopin.solos.settings.TargetTimeDialog.1
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialog, int whichButton) {
            long hrsInMills = TargetTimeDialog.this.mHoursPicker.getSteppedValue();
            long minInMills = TargetTimeDialog.this.mMinutesPicker.getSteppedValue();
            long secInMills = TargetTimeDialog.this.mSecondsPicker.getSteppedValue();
            long time = (60 * hrsInMills * 60000) + (60000 * minInMills) + (1000 * secInMills);
            if (TargetTimeDialog.this.mTargetType == TargetPreference.AVERAGE_PACE && !Utility.isMetric()) {
                time = (long) Conversion.minutesPerMileToMinutesPerKm(time);
            }
            TargetTimeDialog.this.mObs.onTargetValueChanged(TargetTimeDialog.this.prefKey, time);
        }
    };

    private TargetTimeDialog(Context context, TargetPreference type, TargetDialog.TargetChangedListener obs) {
        this.mObs = obs;
        this.mTargetType = type;
        this.prefKey = type.getKey();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        View view = inflater.inflate(R.layout.layout_time_picker, (ViewGroup) null);
        this.layoutNumberPickerHrs = view.findViewById(R.id.layoutNumberPickerHrs);
        this.mHoursPicker = (NumberPicker) view.findViewById(R.id.numberPickerHrs);
        this.mMinutesPicker = (NumberPicker) view.findViewById(R.id.numberPickerMin);
        this.mSecondsPicker = (NumberPicker) view.findViewById(R.id.numberPickerSec);
        this.mDialog = new AlertDialog.Builder(context).setView(view).setNegativeButton(android.R.string.cancel, (DialogInterface.OnClickListener) null).setPositiveButton(android.R.string.ok, this.mOnOKCLick).create();
    }

    private void setInitialValues(long timeInSeconds) {
        this.mHoursPicker.setMaxValue(100);
        this.mHoursPicker.setMinValue(0);
        this.mMinutesPicker.setMaxValue(59);
        this.mMinutesPicker.setMinValue(0);
        if (this.mStepSeconds.intValue() > 1) {
            this.mSecondsPicker.setStep(this.mStepSeconds.intValue(), 0, 59);
        } else {
            this.mSecondsPicker.setMaxValue(59);
            this.mSecondsPicker.setMinValue(0);
        }
        int seconds = ((int) timeInSeconds) % 60;
        long timeInSeconds2 = timeInSeconds / 60;
        int minutes = ((int) timeInSeconds2) % 60;
        int hours = ((int) timeInSeconds2) / 60;
        this.mHoursPicker.setValue(hours);
        this.mMinutesPicker.setValue(minutes);
        this.mSecondsPicker.setValue(seconds);
    }

    private void show(long initialVal) {
        setInitialValues(initialVal);
        this.mDialog.getWindow().setSoftInputMode(2);
        this.mDialog.setCanceledOnTouchOutside(true);
        this.mDialog.show();
        DialogUtils.setDialogTitleDivider(this.mDialog);
    }

    private void setStep(int step) {
        this.mStepSeconds = Integer.valueOf(step);
    }

    private void showHours(boolean show) {
        this.layoutNumberPickerHrs.setVisibility(show ? 0 : 8);
    }

    public static void show(Context context, TargetPreference type, long initialValMillis, TargetDialog.TargetChangedListener changeListener) {
        if (initialValMillis % 1000 >= 500) {
            initialValMillis += 1000;
        }
        long seconds = initialValMillis / 1000;
        TargetTimeDialog self = new TargetTimeDialog(context, type, changeListener);
        if (type == TargetPreference.AVERAGE_PACE) {
            if (!Utility.isMetric()) {
                seconds = (long) Conversion.minutesPerKmToMinutesPerMile(seconds);
            }
            seconds = TimePickerPreference.round(seconds, 5);
            self.setStep(5);
            self.showHours(false);
        }
        self.show(seconds);
    }
}
