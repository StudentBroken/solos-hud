package com.kopin.solos.settings;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import com.kopin.solos.R;
import com.kopin.solos.common.DialogUtils;
import com.kopin.solos.storage.settings.UserProfile;
import java.util.Calendar;

/* JADX INFO: loaded from: classes24.dex */
public class DatePickerPreference extends EditTextPreference {
    private static final int DEFAULT_DAY = 1;
    private static final int DEFAULT_MONTH = 0;
    private String defaultValueTxt;
    private DatePickerDialog dialog;
    private DatePickerDialog.OnDateSetListener externalDateSetListener;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private String mKey;

    public DatePickerPreference(Context ctx, AttributeSet attrs, int defStyle) {
        super(ctx, attrs, defStyle);
        this.mKey = "";
        this.mDateSetListener = new DatePickerDialog.OnDateSetListener() { // from class: com.kopin.solos.settings.DatePickerPreference.3
            @Override // android.app.DatePickerDialog.OnDateSetListener
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                DatePickerPreference.this.setText(String.format("%d,%d,%d", Integer.valueOf(year), Integer.valueOf(monthOfYear), Integer.valueOf(dayOfMonth)));
                DatePickerPreference.this.setSummary(DatePickerPreference.this.formatDate(year, monthOfYear, dayOfMonth));
                if (DatePickerPreference.this.externalDateSetListener != null) {
                    DatePickerPreference.this.externalDateSetListener.onDateSet(view, year, monthOfYear, dayOfMonth);
                }
            }
        };
        parseAttributes(ctx, attrs);
        setSummary(formatDate(getYear(), getMonth(), getDay()));
    }

    public DatePickerPreference(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        this.mKey = "";
        this.mDateSetListener = new DatePickerDialog.OnDateSetListener() { // from class: com.kopin.solos.settings.DatePickerPreference.3
            @Override // android.app.DatePickerDialog.OnDateSetListener
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                DatePickerPreference.this.setText(String.format("%d,%d,%d", Integer.valueOf(year), Integer.valueOf(monthOfYear), Integer.valueOf(dayOfMonth)));
                DatePickerPreference.this.setSummary(DatePickerPreference.this.formatDate(year, monthOfYear, dayOfMonth));
                if (DatePickerPreference.this.externalDateSetListener != null) {
                    DatePickerPreference.this.externalDateSetListener.onDateSet(view, year, monthOfYear, dayOfMonth);
                }
            }
        };
        parseAttributes(ctx, attrs);
        setSummary(formatDate(getYear(), getMonth(), getDay()));
    }

    private void parseAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ValidatedEditTextPreference);
        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case 5:
                    this.mKey = a.getString(attr);
                    break;
            }
        }
        a.recycle();
    }

    @Override // android.preference.EditTextPreference, android.preference.Preference
    protected Object onGetDefaultValue(TypedArray a, int index) {
        Object defaultValue = super.onGetDefaultValue(a, index);
        this.defaultValueTxt = defaultValue.toString();
        return defaultValue;
    }

    @Override // android.preference.Preference
    protected void onBindView(View view) {
        super.onBindView(view);
        view.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.settings.DatePickerPreference.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                DatePickerPreference.this.onClick();
            }
        });
    }

    @Override // android.preference.EditTextPreference, android.preference.Preference
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        super.onSetInitialValue(restorePersistedValue, defaultValue);
        setSummary(formatDate(getYear(), getMonth(), getDay()));
    }

    private int getYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(1, -25);
        return UserProfile.getDatePart(getText(), 0, calendar.get(1));
    }

    private int getMonth() {
        return UserProfile.getDatePart(getText(), 1, 0);
    }

    private int getDay() {
        return UserProfile.getDatePart(getText(), 2, 1);
    }

    @Override // android.preference.EditTextPreference, android.preference.DialogPreference
    protected void showDialog(Bundle state) {
        this.dialog = new DatePickerDialog(getContext(), this.mDateSetListener, getYear(), getMonth(), getDay()) { // from class: com.kopin.solos.settings.DatePickerPreference.2
            @Override // android.app.DatePickerDialog, android.widget.DatePicker.OnDateChangedListener
            public void onDateChanged(DatePicker view, int year, int month, int day) {
                view.init(year, month, day, this);
            }
        };
        UserProfile.prepareDateOfBirthDialog(this.dialog);
        this.dialog.setTitle(getDialogTitle());
        this.dialog.show();
        DialogUtils.setDialogTitleDivider(this.dialog);
        DialogUtils.setDialogSeparatorDivider(this.dialog);
    }

    public void setOnDateSetListener(DatePickerDialog.OnDateSetListener dateSetListener) {
        this.externalDateSetListener = dateSetListener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String formatDate(int year, int month, int day) {
        return String.format("%02d-%02d-%02d", Integer.valueOf(year), Integer.valueOf(month + 1), Integer.valueOf(day));
    }
}
