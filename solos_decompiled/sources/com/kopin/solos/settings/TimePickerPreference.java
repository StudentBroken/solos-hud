package com.kopin.solos.settings;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.intoware.numberpicker.NumberPicker;
import com.kopin.solos.R;
import com.kopin.solos.common.DialogUtils;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.Utility;

/* JADX INFO: loaded from: classes24.dex */
public class TimePickerPreference extends DialogPreference {
    private static final int MAX_HOURS = 99;
    private static final int MAX_MINS = 59;
    private static final int MAX_SECONDS = 59;
    private View layoutNumberPickerHrs;
    private Integer mDefaultValue;
    private NumberPicker mHoursPicker;
    private NumberPicker mMinutesPicker;
    private NumberPicker mSecondsPicker;
    private boolean mShowTopDivider;
    private boolean mShowUnit;
    private Integer mStepSeconds;
    private String mUnitImperial;
    private String mUnitMetric;
    private int maxHours;
    private View viewTitle;

    public TimePickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.maxHours = MAX_HOURS;
        this.mShowTopDivider = false;
        this.mShowUnit = false;
        this.mUnitMetric = "";
        this.mUnitImperial = "";
        this.mStepSeconds = 1;
        parseAttributes(context, attrs);
    }

    @Override // android.preference.Preference
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        super.onSetInitialValue(restorePersistedValue, defaultValue);
        setCustomSummary();
    }

    @Override // android.preference.DialogPreference
    protected View onCreateDialogView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService("layout_inflater");
        View view = inflater.inflate(R.layout.layout_time_picker, (ViewGroup) null);
        this.layoutNumberPickerHrs = view.findViewById(R.id.layoutNumberPickerHrs);
        this.mHoursPicker = (NumberPicker) view.findViewById(R.id.numberPickerHrs);
        this.mMinutesPicker = (NumberPicker) view.findViewById(R.id.numberPickerMin);
        this.mSecondsPicker = (NumberPicker) view.findViewById(R.id.numberPickerSec);
        setInitialValues();
        return view;
    }

    private void parseAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TimePickerPreference);
        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            if (attr == 0) {
                this.mShowTopDivider = a.getBoolean(attr, false);
            }
            if (attr == 1) {
                this.mShowUnit = a.getBoolean(attr, false);
            }
            if (attr == 2) {
                this.mUnitMetric = a.getString(attr);
            }
            if (attr == 3) {
                this.mUnitImperial = a.getString(attr);
            }
            if (attr == 4) {
                this.mDefaultValue = Integer.valueOf(a.getInteger(attr, 0));
            }
            if (attr == 5) {
                this.maxHours = a.getInteger(attr, MAX_HOURS);
            }
            if (attr == 6) {
                this.mStepSeconds = Integer.valueOf(a.getInteger(attr, 1));
            }
        }
        a.recycle();
    }

    @Override // android.preference.Preference
    protected void onBindView(View view) {
        super.onBindView(view);
        if (view.getId() == R.id.layoutTargetPref) {
            if (this.mShowTopDivider) {
                view.findViewById(R.id.topDivider).setVisibility(0);
            }
            view.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.settings.TimePickerPreference.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    TimePickerPreference.this.onClick();
                }
            });
            this.viewTitle = view.findViewById(android.R.id.title);
        }
    }

    @Override // android.preference.DialogPreference
    protected void showDialog(Bundle state) {
        super.showDialog(state);
        DialogUtils.setDialogTitleDivider(getDialog());
    }

    @Override // android.preference.DialogPreference
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            persistTime();
        }
    }

    private void persistTime() {
        long hrsInMills = this.mHoursPicker.getSteppedValue();
        long minInMills = this.mMinutesPicker.getSteppedValue();
        long secInMills = this.mSecondsPicker.getSteppedValue();
        long time = (60 * hrsInMills * 60000) + (60000 * minInMills) + (1000 * secInMills);
        if (this.mShowUnit && !Utility.isMetric()) {
            time = round(Conversion.minutesPerMileToMinutesPerKm(time), 1000);
        }
        persistLong(time);
        setSummary(addUnit(this.maxHours > 0 ? Utility.toTime(hrsInMills, minInMills, secInMills) : Utility.toTime(minInMills, secInMills)));
        callChangeListener(Long.valueOf(time));
    }

    private void setInitialValues() {
        this.mHoursPicker.setMaxValue(this.maxHours);
        this.mHoursPicker.setMinValue(0);
        this.mMinutesPicker.setMaxValue(59);
        this.mMinutesPicker.setMinValue(0);
        if (this.mStepSeconds.intValue() > 1) {
            this.mSecondsPicker.setStep(this.mStepSeconds.intValue(), 0, 59);
        } else {
            this.mSecondsPicker.setMaxValue(59);
            this.mSecondsPicker.setMinValue(0);
        }
        long timeMillis = getPersistedLong(0L);
        if (this.mShowUnit && !Utility.isMetric()) {
            timeMillis = (long) Conversion.minutesPerKmToMinutesPerMile(timeMillis);
        }
        long timeInSeconds = round(timeMillis, this.mStepSeconds.intValue() * 1000) / 1000;
        int seconds = ((int) timeInSeconds) % 60;
        long timeInSeconds2 = timeInSeconds / 60;
        int minutes = ((int) timeInSeconds2) % 60;
        int hours = ((int) timeInSeconds2) / 60;
        this.mHoursPicker.setValue(hours);
        this.mMinutesPicker.setValue(minutes);
        this.mSecondsPicker.setValue(seconds);
        this.layoutNumberPickerHrs.setVisibility(this.maxHours <= 0 ? 8 : 0);
    }

    public void setCustomSummary() {
        long timeInMills = getPersistedLong(this.mDefaultValue == null ? 0L : this.mDefaultValue.intValue());
        if (timeInMills != 0) {
            persistLong(timeInMills);
            if (this.mShowUnit && !Utility.isMetric()) {
                timeInMills = (long) Conversion.minutesPerKmToMinutesPerMile(timeInMills);
            }
            if (this.mStepSeconds.intValue() > 1) {
                timeInMills = round(timeInMills, this.mStepSeconds.intValue() * 1000);
            }
            setSummary(addUnit(Utility.toTime(timeInMills, this.maxHours > 0)));
            return;
        }
        setSummary(addUnit(getContext().getString(R.string.txt_default_timer_time)));
    }

    private String addUnit(String value) {
        if (!this.mShowUnit) {
            return value;
        }
        Object[] objArr = new Object[2];
        objArr[0] = value;
        objArr[1] = Utility.isMetric() ? this.mUnitMetric : this.mUnitImperial;
        return String.format("%s %s", objArr);
    }

    public void refresh() {
        setCustomSummary();
    }

    @Override // android.preference.Preference
    public void setTitle(CharSequence title) {
        if (this.viewTitle != null) {
            this.viewTitle.setVisibility((title == null || title.length() <= 0) ? 8 : 0);
        }
        super.setTitle(title);
    }

    @Override // android.preference.Preference
    public void setTitle(int titleResId) {
        if (this.viewTitle != null) {
            this.viewTitle.setVisibility(titleResId > 0 ? 0 : 8);
        }
        super.setTitle(titleResId);
    }

    static long round(double value, int step) {
        return Math.round(value / ((double) step)) * ((long) step);
    }
}
