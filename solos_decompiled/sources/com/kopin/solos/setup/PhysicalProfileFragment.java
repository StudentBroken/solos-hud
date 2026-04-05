package com.kopin.solos.setup;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import com.kopin.solos.FacebookBaseFragment;
import com.kopin.solos.R;
import com.kopin.solos.common.DialogUtils;
import com.kopin.solos.share.Platforms;
import com.kopin.solos.share.Sync;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.settings.UserProfile;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.view.RoundedImageView;
import java.util.Locale;

/* JADX INFO: loaded from: classes24.dex */
public class PhysicalProfileFragment extends FacebookBaseFragment implements View.OnClickListener {
    RoundedImageView imgProfileUser;
    View imgProfileUserDefault;
    View layoutPic;
    EditText mEditText;
    String mName;
    View mProfileLayout1;
    View mProfileLayout2;
    View mProfileLayout3;
    int mSelectedViewResId;
    TextView mTextViewAge;
    TextView mTextViewWeight;
    TextView mTxtName;
    String mWeight;
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() { // from class: com.kopin.solos.setup.PhysicalProfileFragment.4
        @Override // android.app.DatePickerDialog.OnDateSetListener
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            UserProfile.setDOB(year, monthOfYear, dayOfMonth, false);
            PhysicalProfileFragment.this.mTextViewAge.setText(String.valueOf(UserProfile.getAge()));
        }
    };
    DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() { // from class: com.kopin.solos.setup.PhysicalProfileFragment.6
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            if (which == -1) {
                Editable text = PhysicalProfileFragment.this.mEditText.getText();
                String value = text.toString();
                if (value != null && !value.isEmpty()) {
                    PhysicalProfileFragment.this.onEditDialogClosed(value);
                } else {
                    PhysicalProfileFragment.this.onEditDialogClosed(PhysicalProfileFragment.this.mEditText.getHint().toString());
                }
            }
        }
    };

    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);
        inflater.inflate(R.layout.layout_setup_profile, (ViewGroup) this.mContainerLayout, true);
        this.mProfileLayout1 = view.findViewById(R.id.layoutProfile1);
        this.mProfileLayout2 = view.findViewById(R.id.layoutProfile2);
        this.mProfileLayout3 = view.findViewById(R.id.layoutProfile3);
        this.mProfileLayout1.setOnClickListener(this);
        this.mProfileLayout2.setOnClickListener(this);
        this.mProfileLayout3.setOnClickListener(this);
        this.mStepSubHeader.setText("");
        this.mStepHeader.setText(R.string.btn_setup_profile);
        ((TextView) this.mProfileLayout1.findViewById(R.id.txtProfileLabel)).setText(R.string.profile_name);
        this.mTxtName = (TextView) this.mProfileLayout1.findViewById(R.id.txtProfileValue);
        this.mTxtName.setText(UserProfile.getName(R.string.profile_name_default));
        this.mName = UserProfile.getName();
        ((TextView) this.mProfileLayout2.findViewById(R.id.txtProfileLabel)).setText(R.string.profile_age);
        this.mTextViewAge = (TextView) this.mProfileLayout2.findViewById(R.id.txtProfileValue);
        this.mTextViewAge.setText(String.valueOf(UserProfile.getAge()));
        view.findViewById(R.id.divider2).setVisibility(0);
        this.mProfileLayout3.setVisibility(0);
        ((TextView) this.mProfileLayout3.findViewById(R.id.txtProfileLabel)).setText(R.string.profile_weight);
        this.mTextViewWeight = (TextView) this.mProfileLayout3.findViewById(R.id.txtProfileValue);
        this.mTextViewWeight.setText(formatWeight());
        this.layoutPic = view.findViewById(R.id.layoutPic);
        this.imgProfileUserDefault = view.findViewById(R.id.imgProfileUserDefault);
        this.imgProfileUser = (RoundedImageView) view.findViewById(R.id.imgProfileUser);
        this.imgProfileUser.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.setup.PhysicalProfileFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                PhysicalProfileFragment.this.addProfilePhoto(FacebookBaseFragment.REQUEST_IMAGE_CAPTURE, FacebookBaseFragment.RESULT_LOAD_IMG);
            }
        });
        this.imgProfileUserDefault.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.setup.PhysicalProfileFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                PhysicalProfileFragment.this.addProfilePhoto(FacebookBaseFragment.REQUEST_IMAGE_CAPTURE, FacebookBaseFragment.RESULT_LOAD_IMG);
            }
        });
        loadFacebookProfileImage();
        updateView();
        return view;
    }

    @Override // com.kopin.solos.FacebookBaseFragment
    public void updateView() {
        if (getActivity() != null && !getActivity().isFinishing()) {
            this.layoutPic.setVisibility(0);
            if (this.mBmpProfile == null) {
                this.imgProfileUser.setVisibility(8);
                this.imgProfileUserDefault.setVisibility(0);
            } else {
                this.imgProfileUserDefault.setVisibility(8);
                this.imgProfileUser.setImageBitmap(this.mBmpProfile);
                this.imgProfileUser.setVisibility(0);
            }
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        this.mSelectedViewResId = view.getId();
        if (this.mSelectedViewResId == R.id.layoutProfile1) {
            showEditTextDialog(R.string.hint_name, this.mName, getResources().getInteger(R.integer.name_len));
            return;
        }
        if (this.mSelectedViewResId == R.id.layoutProfile2) {
            String dob = UserProfile.getDOB();
            int year = UserProfile.getDatePart(dob, 0, 1992);
            int month = UserProfile.getDatePart(dob, 1, 0);
            int day = UserProfile.getDatePart(dob, 2, 1);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this.mDateSetListener, year, month, day) { // from class: com.kopin.solos.setup.PhysicalProfileFragment.3
                @Override // android.app.DatePickerDialog, android.widget.DatePicker.OnDateChangedListener
                public void onDateChanged(DatePicker view2, int year2, int month2, int day2) {
                    view2.init(year2, month2, day2, this);
                }
            };
            UserProfile.prepareDateOfBirthDialog(dialog);
            dialog.setTitle(getString(R.string.pref_title_dob));
            dialog.show();
            DialogUtils.setDialogTitleDivider(dialog);
            DialogUtils.setDialogSeparatorDivider(dialog);
            return;
        }
        int weight = (int) (Prefs.isMetric() ? UserProfile.getWeightKG() : UserProfile.getWeightPounds());
        showEditTextDialog(Prefs.isMetric() ? R.string.hint_weight : R.string.hint_weight_imperial, String.format(Locale.US, "%d", Integer.valueOf(weight)), 2, Integer.valueOf(getResources().getInteger(R.integer.weight_min)), Integer.valueOf(getResources().getInteger(R.integer.weight_max)), getResources().getInteger(R.integer.weight_len));
    }

    void onEditDialogClosed(String value) {
        if (this.mSelectedViewResId == R.id.layoutProfile1) {
            this.mTxtName.setText(value);
            this.mName = value;
        } else if (this.mSelectedViewResId == R.id.layoutProfile2) {
            this.mTextViewAge.setText(value);
        } else {
            this.mTextViewWeight.setText(formatWeight(value));
            this.mWeight = value;
        }
    }

    @Override // com.kopin.solos.common.BaseFragment, android.app.Fragment
    public void onPause() {
        super.onPause();
        savePhysicalProfile();
    }

    private void savePhysicalProfile() {
        if (this.mName != null && !this.mName.isEmpty() && !this.mName.equalsIgnoreCase(getString(R.string.profile_name_default))) {
            UserProfile.setName(this.mName);
        }
        UserProfile.setWeight(this.mWeight, Prefs.isMetric());
        Sync.setProfile(Platforms.Peloton.getSharedKey(), UserProfile.createRider(), System.currentTimeMillis());
    }

    private void showEditTextDialog(int titleResId, String defaultValue, int maxLength) {
        showEditTextDialog(titleResId, defaultValue, null, null, null, maxLength);
    }

    private void showEditTextDialog(int titleResId, String defaultValue, Integer inputType, final Integer minValue, final Integer maxValue, final int maxLength) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_custom_edittext, (ViewGroup) null);
        this.mEditText = (EditText) view.findViewById(android.R.id.edit);
        if (inputType != null) {
            this.mEditText.setInputType(inputType.intValue());
        }
        this.mEditText.setHint(defaultValue);
        if (maxLength > 0) {
            this.mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        }
        this.mEditText.setMaxLines(1);
        this.mEditText.setSingleLine(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setTitle(titleResId).setView(view).setPositiveButton(android.R.string.ok, this.clickListener).setNegativeButton(android.R.string.cancel, this.clickListener);
        final AlertDialog dialog = builder.create();
        if (minValue != null || maxValue != null) {
            this.mEditText.addTextChangedListener(new TextWatcher() { // from class: com.kopin.solos.setup.PhysicalProfileFragment.5
                @Override // android.text.TextWatcher
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override // android.text.TextWatcher
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override // android.text.TextWatcher
                public void afterTextChanged(Editable s) {
                    if (PhysicalProfileFragment.this.onCheckValue(s.toString(), minValue.intValue(), maxValue.intValue(), maxLength)) {
                        dialog.getButton(-1).setEnabled(true);
                    } else {
                        PhysicalProfileFragment.this.mEditText.setError(PhysicalProfileFragment.this.getString(R.string.profile_invalid_value));
                        dialog.getButton(-1).setEnabled(false);
                    }
                }
            });
        }
        dialog.getWindow().setSoftInputMode(4);
        dialog.show();
        DialogUtils.setDialogTitleDivider(dialog);
    }

    protected boolean onCheckValue(String value, double minValue, double maxValue, int maxLength) {
        if (value.trim().isEmpty()) {
            return true;
        }
        double doubleVal = 0.0d;
        try {
            doubleVal = Double.parseDouble(value);
            if (!Prefs.isMetric()) {
                doubleVal = Utility.poundsToKilograms(doubleVal);
            }
        } catch (NumberFormatException e) {
        }
        boolean validMinVal = minValue == -1.0d || doubleVal >= minValue;
        boolean validMaxVal = maxValue == -1.0d || doubleVal <= maxValue;
        boolean validLength = maxLength == -1 || value.length() <= maxLength;
        return validMinVal && validMaxVal && validLength;
    }

    @Override // com.kopin.solos.FacebookBaseFragment, com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) throws Throwable {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String formatWeight() {
        int weight = (int) (Prefs.isMetric() ? UserProfile.getWeightKG() : UserProfile.getWeightPounds());
        return formatWeight(String.valueOf(weight));
    }

    private String formatWeight(String value) {
        return String.format(Locale.US, "%s %s", value, Utility.getUnitOfWeight(getActivity()));
    }
}
