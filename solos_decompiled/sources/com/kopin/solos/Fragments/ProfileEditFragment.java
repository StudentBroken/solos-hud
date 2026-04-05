package com.kopin.solos.Fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import com.kopin.solos.FacebookBaseFragment;
import com.kopin.solos.R;
import com.kopin.solos.settings.DatePickerPreference;
import com.kopin.solos.settings.SettingsFragment;
import com.kopin.solos.settings.ValidatedEditTextPreference;
import com.kopin.solos.share.Platforms;
import com.kopin.solos.share.Sync;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.settings.UserProfile;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.view.RoundedImageView;
import java.text.DecimalFormat;

/* JADX INFO: loaded from: classes24.dex */
public class ProfileEditFragment extends FacebookBaseFragment {
    private static final String mValueUnitFmtStr = "%s %s";
    private RoundedImageView imgProfileUser;
    private ImageView imgProfileUserDefault;
    private String mName;
    private ValidatedEditTextPreference mNamePref;
    private View mView;
    private ValidatedEditTextPreference mWeightPref;
    private TextView txtAddProfilePhoto;
    private DecimalFormat decimalFormatNoPlaces = new DecimalFormat("#");
    private boolean mRideActive = false;
    private boolean edited = false;
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() { // from class: com.kopin.solos.Fragments.ProfileEditFragment.7
        @Override // android.app.DatePickerDialog.OnDateSetListener
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            ProfileEditFragment.this.edited = true;
        }
    };

    @Override // com.kopin.solos.FacebookBaseFragment, com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null && args.containsKey("rideActive")) {
            this.mRideActive = args.getBoolean("rideActive");
        }
        addPreferencesFromResource(R.xml.preference_profile);
    }

    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int i = 0;
        this.mView = inflater.inflate(R.layout.fragment_profile_edit, container, false);
        this.imgProfileUser = (RoundedImageView) this.mView.findViewById(R.id.imgProfileUser);
        this.imgProfileUser.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.Fragments.ProfileEditFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ProfileEditFragment.this.addProfilePhoto(FacebookBaseFragment.REQUEST_IMAGE_CAPTURE, FacebookBaseFragment.RESULT_LOAD_IMG);
            }
        });
        this.imgProfileUserDefault = (ImageView) this.mView.findViewById(R.id.imgProfileUserDefault);
        this.imgProfileUserDefault.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.Fragments.ProfileEditFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ProfileEditFragment.this.addProfilePhoto(FacebookBaseFragment.REQUEST_IMAGE_CAPTURE, FacebookBaseFragment.RESULT_LOAD_IMG);
            }
        });
        this.txtAddProfilePhoto = (TextView) this.mView.findViewById(R.id.txtAddProfilePhoto);
        this.txtAddProfilePhoto.setPaintFlags(this.txtAddProfilePhoto.getPaintFlags() | 8);
        this.txtAddProfilePhoto.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.Fragments.ProfileEditFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ProfileEditFragment.this.addProfilePhoto(FacebookBaseFragment.REQUEST_IMAGE_CAPTURE, FacebookBaseFragment.RESULT_LOAD_IMG);
            }
        });
        this.mNamePref = (ValidatedEditTextPreference) findPreference(getActivity().getString(R.string.pref_key_user_name));
        this.mNamePref.setMaxLength(getActivity().getResources().getInteger(R.integer.name_len));
        this.mName = UserProfile.getName();
        if (this.mName == null || this.mName.isEmpty()) {
            this.mNamePref.setSummary(R.string.profile_edit_name_default);
        } else {
            this.mNamePref.setSummary(this.mName);
            this.mNamePref.setDefaultValue(this.mName);
        }
        this.mNamePref.setOnPreferenceChangeListener(new OnTextPreferenceChangeListener(i) { // from class: com.kopin.solos.Fragments.ProfileEditFragment.4
            @Override // com.kopin.solos.Fragments.ProfileEditFragment.OnTextPreferenceChangeListener, android.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                ProfileEditFragment.this.edited = true;
                boolean retVal = super.onPreferenceChange(preference, newValue);
                String name = newValue.toString();
                if (name != null && !name.isEmpty()) {
                    preference.setSummary(name);
                } else {
                    preference.setSummary(R.string.profile_edit_name_default);
                }
                return retVal;
            }
        });
        this.mWeightPref = (ValidatedEditTextPreference) findPreference(getActivity().getString(R.string.pref_key_user_weight));
        this.mWeightPref.setMaxLength(getActivity().getResources().getInteger(R.integer.weight_len));
        this.mWeightPref.setDefaultValue(String.format(mValueUnitFmtStr, this.decimalFormatNoPlaces.format(UserProfile.getWeightKG()), Utility.getUnitOfWeight(getActivity())));
        double value = Utility.weightInUnitSystem("" + UserProfile.getWeightKG(), Prefs.getUnitSystem(), getResources().getInteger(R.integer.weight_default));
        this.mWeightPref.setSummary(String.format(mValueUnitFmtStr, this.decimalFormatNoPlaces.format(value), Utility.getUnitOfWeight(getActivity())));
        SettingsFragment.configureTextPreferenceKeyboard(this.mWeightPref);
        this.mWeightPref.setOnPreferenceChangeListener(new OnTextPreferenceChangeListener(i) { // from class: com.kopin.solos.Fragments.ProfileEditFragment.5
            @Override // com.kopin.solos.Fragments.ProfileEditFragment.OnTextPreferenceChangeListener, android.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                ProfileEditFragment.this.edited = true;
                boolean retVal = super.onPreferenceChange(preference, newValue);
                if (UserProfile.isValidWeight(newValue.toString())) {
                    Double weight = Double.valueOf(Double.parseDouble(newValue.toString()));
                    preference.setSummary(String.format(ProfileEditFragment.mValueUnitFmtStr, ProfileEditFragment.this.decimalFormatNoPlaces.format(weight), Utility.getUnitOfWeight(ProfileEditFragment.this.getActivity())));
                } else {
                    preference.setSummary(String.format(ProfileEditFragment.mValueUnitFmtStr, ProfileEditFragment.this.decimalFormatNoPlaces.format(UserProfile.getWeightKG()), Utility.getUnitOfWeight(ProfileEditFragment.this.getActivity())));
                }
                return retVal;
            }
        });
        this.mWeightPref.setUnitSystemChangeListener(new ValidatedEditTextPreference.UnitSystemChangeListener() { // from class: com.kopin.solos.Fragments.ProfileEditFragment.6
            @Override // com.kopin.solos.settings.ValidatedEditTextPreference.UnitSystemChangeListener
            public String onValueEntered(String text) {
                return Utility.convertToKg(ProfileEditFragment.this.getActivity(), text);
            }

            @Override // com.kopin.solos.settings.ValidatedEditTextPreference.UnitSystemChangeListener
            public String onValueFetched(String text) {
                String value2 = "" + Utility.weightInUnitSystem(text, Prefs.getUnitSystem(), ProfileEditFragment.this.getActivity().getResources().getInteger(R.integer.weight_default));
                return ProfileEditFragment.this.decimalFormatNoPlaces.format(Double.parseDouble(value2));
            }

            @Override // com.kopin.solos.settings.ValidatedEditTextPreference.UnitSystemChangeListener
            public double onValueCheck(double value2) {
                return Prefs.getUnitSystem() == Prefs.UnitSystem.METRIC ? value2 : Utility.poundsToKilograms(value2);
            }
        });
        DatePickerPreference datePickerPreference = (DatePickerPreference) findPreference(getActivity().getString(R.string.pref_key_user_dob));
        datePickerPreference.setOnDateSetListener(this.mDateSetListener);
        loadFacebookProfileImage();
        updateView();
        return this.mView;
    }

    @Override // com.kopin.solos.FacebookBaseFragment
    protected void updateView() {
        if (getActivity() != null && !getActivity().isFinishing()) {
            this.mName = UserProfile.getName();
            if (this.mBmpProfile == null) {
                this.imgProfileUser.setVisibility(8);
                this.imgProfileUserDefault.setVisibility(0);
            } else {
                this.imgProfileUserDefault.setVisibility(8);
                this.imgProfileUser.setImageBitmap(this.mBmpProfile);
                this.imgProfileUser.setVisibility(0);
            }
            this.txtAddProfilePhoto.setText(this.mBmpProfile == null ? R.string.profile_edit_add_picture : R.string.profile_edit_change_picture);
        }
    }

    @Override // com.kopin.solos.FacebookBaseFragment, com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class OnTextPreferenceChangeListener implements Preference.OnPreferenceChangeListener {
        private SettingsFragment.Action mAction;
        private int mTextFormatter;

        public OnTextPreferenceChangeListener(int textFormatter) {
            this.mTextFormatter = textFormatter;
        }

        public OnTextPreferenceChangeListener(ProfileEditFragment profileEditFragment, int textFormatter, SettingsFragment.Action action) {
            this(textFormatter);
            this.mAction = action;
        }

        @Override // android.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            ProfileEditFragment.this.edited = true;
            if (this.mTextFormatter != 0) {
                preference.setSummary(ProfileEditFragment.this.getString(this.mTextFormatter, new Object[]{newValue.toString()}));
            }
            if (this.mAction != null) {
                this.mAction.onAction(preference, newValue);
            }
            return true;
        }
    }

    @Override // android.app.Fragment
    public void onDetach() {
        super.onDetach();
        if (this.edited) {
            Sync.setProfile(Platforms.Peloton.getSharedKey(), UserProfile.createRider(), System.currentTimeMillis());
        }
    }
}
