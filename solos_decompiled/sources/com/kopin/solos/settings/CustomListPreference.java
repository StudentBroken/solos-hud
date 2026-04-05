package com.kopin.solos.settings;

import android.content.Context;
import android.os.Bundle;
import android.preference.ListPreference;
import android.util.AttributeSet;
import com.kopin.solos.common.DialogUtils;

/* JADX INFO: loaded from: classes24.dex */
public class CustomListPreference extends ListPreference {
    public CustomListPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CustomListPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomListPreference(Context context) {
        super(context);
    }

    @Override // android.preference.DialogPreference
    protected void showDialog(Bundle state) {
        super.showDialog(state);
        DialogUtils.setDialogTitleDivider(getDialog());
    }

    public void updateSummary() {
        updateSummary(getValue());
    }

    public void updateSummary(String val) {
        CharSequence[] vals = getEntryValues();
        for (int i = 0; i < vals.length; i++) {
            if (val.contentEquals(vals[i])) {
                setSummary(getEntries()[i]);
                return;
            }
        }
    }
}
