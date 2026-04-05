package com.kopin.solos.setup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.kopin.solos.Fragments.BaseServiceFragment;
import com.kopin.solos.R;
import com.kopin.solos.common.DialogUtils;

/* JADX INFO: loaded from: classes24.dex */
public class BaseProfileFragment extends BaseServiceFragment implements View.OnClickListener {
    DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() { // from class: com.kopin.solos.setup.BaseProfileFragment.2
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            if (which == -1) {
                Editable text = BaseProfileFragment.this.mEditText.getText();
                String value = text.toString();
                if (value != null && !value.isEmpty()) {
                    BaseProfileFragment.this.onEditDialogClosed(value);
                } else {
                    BaseProfileFragment.this.onEditDialogClosed(BaseProfileFragment.this.mEditText.getHint().toString());
                }
            }
        }
    };
    EditText mEditText;
    View mProfileLayout1;
    View mProfileLayout2;
    View mProfileLayout3;
    View mProfileLayout4;
    int mSelectedViewResId;

    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);
        inflater.inflate(R.layout.layout_setup_profile, (ViewGroup) this.mContainerLayout, true);
        this.mProfileLayout1 = view.findViewById(R.id.layoutProfile1);
        this.mProfileLayout2 = view.findViewById(R.id.layoutProfile2);
        this.mProfileLayout3 = view.findViewById(R.id.layoutProfile3);
        this.mProfileLayout4 = view.findViewById(R.id.layoutProfile4);
        this.mProfileLayout1.setOnClickListener(this);
        this.mProfileLayout2.setOnClickListener(this);
        this.mProfileLayout3.setOnClickListener(this);
        this.mProfileLayout4.setOnClickListener(this);
        this.mStepHeader.setText(R.string.pref_profile_title);
        return view;
    }

    protected void showEditTextDialog(String title, String defaultValue, int inputType, final double minValue, final double maxValue, final int maxLength) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_custom_edittext, (ViewGroup) null);
        this.mEditText = (EditText) view.findViewById(android.R.id.edit);
        this.mEditText.setInputType(inputType);
        this.mEditText.setHint(defaultValue);
        if (maxLength > 0) {
            this.mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setTitle(title).setView(view).setPositiveButton(android.R.string.ok, this.clickListener).setNegativeButton(android.R.string.cancel, this.clickListener);
        final AlertDialog dialog = builder.create();
        this.mEditText.addTextChangedListener(new TextWatcher() { // from class: com.kopin.solos.setup.BaseProfileFragment.1
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
                if (BaseProfileFragment.this.onCheckValue(s.toString(), minValue, maxValue, maxLength)) {
                    dialog.getButton(-1).setEnabled(true);
                } else {
                    BaseProfileFragment.this.mEditText.setError("Enter valid value");
                    dialog.getButton(-1).setEnabled(false);
                }
            }
        });
        dialog.getWindow().setSoftInputMode(4);
        dialog.show();
        DialogUtils.setDialogTitleDivider(dialog);
    }

    protected void showEditTextDialog(int titleResId, String defaultValue, int inputType, double minValue, double maxValue, int maxLength) {
        showEditTextDialog(getString(titleResId), defaultValue, inputType, minValue, maxValue, maxLength);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        this.mSelectedViewResId = view.getId();
    }

    void onEditDialogClosed(String value) {
    }

    protected boolean onCheckValue(String value, double minValue, double maxValue, int maxLength) {
        if (value.trim().isEmpty()) {
            return true;
        }
        double doubleVal = 0.0d;
        try {
            doubleVal = Double.parseDouble(value);
        } catch (NumberFormatException e) {
        }
        boolean validMinVal = minValue == -1.0d || doubleVal >= minValue;
        boolean validMaxVal = maxValue == -1.0d || doubleVal <= maxValue;
        boolean validLength = maxLength == -1 || value.length() <= maxLength;
        return validMinVal && validMaxVal && validLength;
    }
}
