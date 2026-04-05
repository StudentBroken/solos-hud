package com.kopin.solos.settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.kopin.solos.R;
import com.kopin.solos.common.DialogUtils;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.Utility;
import java.text.DecimalFormat;

/* JADX INFO: loaded from: classes24.dex */
public class ValidatedEditTextPreference extends EditTextPreference {
    private String defaultValueTxt;
    private String mKey;
    private int mMaxLength;
    private double mMaxValue;
    private double mMaxValueImperial;
    private double mMinValue;
    private double mMinValueImperial;
    private UnitSystemChangeListener mUnitSystemChangeListener;
    private EditTextWatcher mWatcher;

    public ValidatedEditTextPreference(Context ctx, AttributeSet attrs, int defStyle) {
        super(ctx, attrs, defStyle);
        this.mMinValue = 1.0d;
        this.mMaxValue = 6.0d;
        this.mMaxValueImperial = 0.0d;
        this.mMinValueImperial = 0.0d;
        this.mMaxLength = 1024;
        this.mWatcher = new EditTextWatcher();
        this.mKey = "";
        parseAttributes(ctx, attrs);
    }

    public ValidatedEditTextPreference(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        this.mMinValue = 1.0d;
        this.mMaxValue = 6.0d;
        this.mMaxValueImperial = 0.0d;
        this.mMinValueImperial = 0.0d;
        this.mMaxLength = 1024;
        this.mWatcher = new EditTextWatcher();
        this.mKey = "";
        parseAttributes(ctx, attrs);
    }

    private void parseAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ValidatedEditTextPreference);
        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case 0:
                    setMinValue(a.getFloat(attr, 1.0f));
                    break;
                case 1:
                    setMaxValue(a.getFloat(attr, 6.0f));
                    break;
                case 2:
                    setMaxLength(a.getInteger(attr, 1024));
                    break;
                case 3:
                    setMaxValueImperial(a.getFloat(attr, 0.0f));
                    break;
                case 4:
                    setMinValueImperial(a.getFloat(attr, 1.0f));
                    break;
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

    private class EditTextWatcher implements TextWatcher {
        private EditTextWatcher() {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable s) {
            ValidatedEditTextPreference.this.onEditTextChanged();
        }
    }

    public boolean onCheckValue(String value) {
        return onCheckValue(value, "");
    }

    protected boolean onCheckValue(String value, String hint) {
        if ((value == null || value.trim().isEmpty()) && ((value = hint) == null || value.trim().isEmpty())) {
            return false;
        }
        switch (getEditText().getInputType()) {
            case 2:
            case 8192:
            case 8194:
                double nr = Utility.doubleFromString(value, -1.0d);
                if (this.mUnitSystemChangeListener != null) {
                    nr = this.mUnitSystemChangeListener.onValueCheck(nr);
                }
                double min = (this.mMinValueImperial <= 0.0d || Prefs.isMetric()) ? this.mMinValue : this.mMinValueImperial;
                double max = (this.mMaxValueImperial <= 0.0d || Prefs.isMetric()) ? this.mMaxValue : this.mMaxValueImperial;
                return nr >= min && nr <= max && value.length() <= this.mMaxLength;
            default:
                return true;
        }
    }

    protected void onEditTextChanged() {
        boolean enable = onCheckValue((getEditText() == null || getEditText().getText() == null) ? "" : getEditText().getText().toString(), (getEditText() == null || getEditText().getHint() == null) ? "" : getEditText().getHint().toString());
        Dialog dlg = getDialog();
        if (dlg instanceof AlertDialog) {
            AlertDialog alertDlg = (AlertDialog) dlg;
            Button btn = alertDlg.getButton(-1);
            btn.setEnabled(enable);
            EditText editText = getEditText();
            if (!enable && editText != null) {
                editText.setError("Enter valid value");
            }
        }
    }

    public void setMinValue(double minValue) {
        this.mMinValue = minValue;
    }

    public void setMaxValue(double maxValue) {
        this.mMaxValue = maxValue;
    }

    public void setMaxValueImperial(double maxValue) {
        this.mMaxValueImperial = maxValue;
    }

    public void setMinValueImperial(double minValue) {
        this.mMinValueImperial = minValue;
    }

    public void setMaxLength(int maxLength) {
        this.mMaxLength = maxLength;
    }

    @Override // android.preference.Preference
    protected void onBindView(View view) {
        super.onBindView(view);
        if (view.getId() == R.id.layoutTargetPref) {
            view.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.settings.ValidatedEditTextPreference.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    ValidatedEditTextPreference.this.onClick();
                }
            });
        }
    }

    @Override // android.preference.EditTextPreference, android.preference.DialogPreference
    protected void showDialog(Bundle state) {
        String text;
        super.showDialog(state);
        DialogUtils.setDialogTitleDivider(getDialog());
        EditText editText = getEditText();
        editText.removeTextChangedListener(this.mWatcher);
        editText.addTextChangedListener(this.mWatcher);
        editText.setMaxLines(1);
        editText.setSingleLine(true);
        if (this.mMaxLength > 0) {
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(this.mMaxLength)});
        }
        String val = getText();
        try {
            String val2 = Utility.roundToTwoDecimalPlaces(val);
            String hint = (val2 == null || val2.length() <= 0) ? this.defaultValueTxt : val2;
            editText.setHint(Utility.trimEmptyDecimal(hint));
        } catch (NumberFormatException e) {
            String hint2 = (val == null || val.length() <= 0) ? this.defaultValueTxt : val;
            editText.setHint(Utility.trimEmptyDecimal(hint2));
        } catch (Throwable th) {
            String hint3 = (val == null || val.length() <= 0) ? this.defaultValueTxt : val;
            editText.setHint(Utility.trimEmptyDecimal(hint3));
            throw th;
        }
        if (this.mUnitSystemChangeListener != null && (text = this.mUnitSystemChangeListener.onValueFetched(getText())) != null && text.length() > 0 && !text.equals("0")) {
            editText.setHint(Utility.trimEmptyDecimal(text));
        }
        editText.setText("");
        onEditTextChanged();
    }

    @Override // android.preference.EditTextPreference, android.preference.DialogPreference
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            String value = getEditText().getText().toString();
            if (value.length() == 0) {
                value = getEditText().getHint().toString();
            }
            if (this.mKey.matches(getContext().getString(R.string.pref_key_target_average_speed))) {
                value = String.valueOf(Utility.roundToLong(value));
            }
            if (callChangeListener(value)) {
                setText(value);
            }
        }
    }

    @Override // android.preference.EditTextPreference
    public void setText(String text) {
        if (this.mUnitSystemChangeListener != null) {
            text = this.mUnitSystemChangeListener.onValueEntered(text);
        }
        super.setText(text);
    }

    public void setUnitSystemChangeListener(UnitSystemChangeListener listener) {
        this.mUnitSystemChangeListener = listener;
    }

    public void refresh() {
        String s;
        if (this.mKey != null && !this.mKey.isEmpty() && (s = getSharedPreferences().getString(this.mKey, this.defaultValueTxt)) != null && !s.isEmpty()) {
            setText(s);
        }
    }

    public void refresh(String form, DecimalFormat decimalFormat) {
        refresh(form, decimalFormat, null);
    }

    public void refresh(String form, DecimalFormat decimalFormat, String unit) {
        String str;
        refresh();
        String s = getText();
        if (s != null && !s.isEmpty()) {
            if (unit != null) {
                Object[] objArr = new Object[2];
                objArr[0] = decimalFormat.format(s.contains(".") ? Double.parseDouble(s) : Long.parseLong(s));
                objArr[1] = unit;
                str = String.format(form, objArr);
            } else {
                Object[] objArr2 = new Object[1];
                objArr2[0] = decimalFormat.format(s.contains(".") ? Double.parseDouble(s) : Long.parseLong(s));
                str = String.format(form, objArr2);
            }
            setSummary(str);
        }
    }

    public static abstract class UnitSystemChangeListener {
        public String onValueEntered(String text) {
            return text;
        }

        public String onValueFetched(String text) {
            return text;
        }

        public double onValueCheck(double value) {
            return value;
        }
    }
}
