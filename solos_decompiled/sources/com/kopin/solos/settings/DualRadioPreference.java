package com.kopin.solos.settings;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.CheckBoxPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.kopin.solos.R;

/* JADX INFO: loaded from: classes24.dex */
public class DualRadioPreference extends CheckBoxPreference {
    String mChoice1;
    String mChoice2;

    public DualRadioPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mChoice1 = "";
        this.mChoice2 = "";
        parseAttributes(context, attrs);
    }

    public DualRadioPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mChoice1 = "";
        this.mChoice2 = "";
        parseAttributes(context, attrs);
    }

    public DualRadioPreference(Context context) {
        super(context);
        this.mChoice1 = "";
        this.mChoice2 = "";
    }

    @Override // android.preference.CheckBoxPreference, android.preference.Preference
    protected void onBindView(View viewRoot) {
        super.onBindView(viewRoot);
        RadioButton radio1 = (RadioButton) viewRoot.findViewById(R.id.radio1);
        RadioButton radio2 = (RadioButton) viewRoot.findViewById(R.id.radio2);
        if (this.mChoice1 != null && !this.mChoice1.isEmpty()) {
            radio1.setText(this.mChoice1);
        }
        if (this.mChoice2 != null && !this.mChoice2.isEmpty()) {
            radio2.setText(this.mChoice2);
        }
        boolean b = isChecked();
        Log.d("rad", "onBindView " + b);
        if (b) {
            Log.d("rad", "set radio 1 checked");
            radio1.setChecked(true);
        } else {
            radio2.setChecked(true);
        }
        RadioGroup rg = (RadioGroup) viewRoot.findViewById(R.id.radioGroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.kopin.solos.settings.DualRadioPreference.1
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("rad", checkedId == R.id.radio1 ? "checked rad1" : "checked rad2");
                boolean newValue = checkedId == R.id.radio1;
                if (DualRadioPreference.this.callChangeListener(Boolean.valueOf(newValue))) {
                    DualRadioPreference.this.setChecked(newValue);
                }
            }
        });
    }

    @Override // android.preference.TwoStatePreference, android.preference.Preference
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        Log.d("rad", "setInitVale restore" + restoreValue + ", persis " + getPersistedBoolean(true));
        setChecked(restoreValue ? getPersistedBoolean(isChecked()) : ((Boolean) defaultValue).booleanValue());
    }

    private void parseAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DualRadioPreference);
        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case 0:
                    this.mChoice1 = a.getString(attr);
                    break;
                case 1:
                    this.mChoice2 = a.getString(attr);
                    break;
            }
        }
        a.recycle();
    }
}
