package com.kopin.solos.settings;

import android.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import com.kopin.solos.common.DialogUtils;
import com.kopin.solos.storage.settings.TargetPreference;
import com.kopin.solos.storage.util.Utility;

/* JADX INFO: loaded from: classes24.dex */
public class TargetDialog {
    private final int def;
    private final int len;
    private AlertDialog mDialog;
    private final EditText mEditText;
    private final TextWatcher mEditWatcher;
    private final TargetChangedListener mObs;
    private final DialogInterface.OnClickListener mOnOKCLick;
    private final TargetPreference mTargetType;
    private final int max;
    private final int min;
    private final String prefKey;

    interface TargetChangedListener {
        void onTargetValueChanged(String str, long j);

        void onTargetValueChanged(String str, String str2);
    }

    private TargetDialog(Context context, String title, String value, TargetPreference type, TargetChangedListener obs) {
        this(context, title, value, type, obs, false);
    }

    private TargetDialog(Context context, String str, String str2, TargetPreference targetPreference, TargetChangedListener targetChangedListener, boolean z) {
        this.mEditWatcher = new TextWatcher() { // from class: com.kopin.solos.settings.TargetDialog.1
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
                boolean valid = true;
                if (!s.toString().trim().isEmpty()) {
                    double nr = Utility.doubleFromString(s.toString().trim(), -1.0d);
                    valid = nr >= ((double) TargetDialog.this.min) && (TargetDialog.this.max <= 0 || (TargetDialog.this.max > 0 && nr <= ((double) TargetDialog.this.max))) && (TargetDialog.this.len <= 0 || s.toString().length() <= TargetDialog.this.len);
                }
                Button okButton = TargetDialog.this.mDialog.getButton(-1);
                okButton.setEnabled(valid);
                if (!valid && TargetDialog.this.mEditText != null) {
                    TargetDialog.this.mEditText.setError("Enter valid value");
                }
            }
        };
        this.mOnOKCLick = new DialogInterface.OnClickListener() { // from class: com.kopin.solos.settings.TargetDialog.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int whichButton) {
                String val = TargetDialog.this.mEditText.getText().toString().trim();
                if (!val.isEmpty()) {
                    TargetDialog.this.mObs.onTargetValueChanged(TargetDialog.this.prefKey, Utility.roundToTwoDecimalPlaces(val));
                }
            }
        };
        this.mObs = targetChangedListener;
        this.prefKey = targetPreference.getKey();
        this.min = targetPreference.getResMin() <= 0 ? 0 : context.getResources().getInteger(targetPreference.getResMin());
        this.max = targetPreference.getResMax() <= 0 ? 0 : context.getResources().getInteger(targetPreference.getResMax());
        this.len = targetPreference.getResLen() <= 0 ? 0 : context.getResources().getInteger(targetPreference.getResLen());
        this.def = targetPreference.getResDefault() <= 0 ? 0 : context.getResources().getInteger(targetPreference.getResDefault());
        this.mEditText = new EditText(context);
        this.mEditText.setHint(Utility.trimEmptyDecimal("" + (str2.isEmpty() ? Integer.valueOf(this.def) : str2)));
        this.mEditText.setInputType(z ? 8194 : 2);
        if (this.len > 0) {
            this.mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(this.len)});
        }
        this.mEditText.addTextChangedListener(this.mEditWatcher);
        this.mDialog = new AlertDialog.Builder(context).setTitle(str).setView(this.mEditText).setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null).setPositiveButton(R.string.ok, this.mOnOKCLick).create();
        this.mTargetType = targetPreference;
    }

    private void show() {
        this.mDialog.getWindow().setSoftInputMode(4);
        this.mDialog.setCanceledOnTouchOutside(true);
        this.mDialog.show();
        DialogUtils.setDialogTitleDivider(this.mDialog);
    }

    public static void show(Context context, String title, String value, TargetPreference type, TargetChangedListener changedListener, boolean allowDecimals) {
        TargetDialog self = new TargetDialog(context, title, value, type, changedListener, allowDecimals);
        self.show();
    }
}
