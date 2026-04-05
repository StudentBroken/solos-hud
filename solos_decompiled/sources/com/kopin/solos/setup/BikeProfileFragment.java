package com.kopin.solos.setup;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.kopin.solos.R;
import com.kopin.solos.SetupActivity;
import com.kopin.solos.common.DialogUtils;
import com.kopin.solos.share.Sync;
import com.kopin.solos.storage.Bike;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.Utility;
import java.text.DecimalFormat;
import java.util.Locale;

/* JADX INFO: loaded from: classes24.dex */
public class BikeProfileFragment extends BaseProfileFragment {
    private static final int MAX_BIKE_NAME_LENGTH = 25;
    private static final String TAG = "BikeProfileFrag";
    private Bike bike;
    private boolean mInitialSetup;
    private TextView mTextTitle;
    private TextView mTextType;
    private TextView mTextWeight;
    private TextView mTextWheelSize;

    public static BikeProfileFragment getInstance(Bike bike, boolean initialSetup) {
        BikeProfileFragment fragment = new BikeProfileFragment();
        fragment.bike = bike;
        fragment.mInitialSetup = initialSetup;
        return fragment;
    }

    @Override // com.kopin.solos.setup.BaseProfileFragment, com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);
        this.mStepSubHeader.setText(R.string.sub_header_bike_profile);
        this.mProfileLayout3.setVisibility(0);
        this.mProfileLayout4.setVisibility(0);
        view.findViewById(R.id.divider2).setVisibility(0);
        view.findViewById(R.id.divider3).setVisibility(0);
        ((TextView) this.mProfileLayout1.findViewById(R.id.txtProfileLabel)).setText(R.string.bike_name);
        ((TextView) this.mProfileLayout2.findViewById(R.id.txtProfileLabel)).setText(R.string.label_bike_type);
        ((TextView) this.mProfileLayout3.findViewById(R.id.txtProfileLabel)).setText(R.string.setting_size);
        ((TextView) this.mProfileLayout4.findViewById(R.id.txtProfileLabel)).setText(R.string.label_bike_weight);
        TextView txtStepMsg = (TextView) view.findViewById(R.id.txtStepMsg);
        txtStepMsg.setText(R.string.msg_setup_profile_bike);
        ImageView img = (ImageView) view.findViewById(R.id.imgHeart);
        img.setImageResource(R.drawable.ic_circle_bike);
        view.findViewById(R.id.txtStepMsg2).setVisibility(4);
        view.findViewById(R.id.imgCalories).setVisibility(4);
        this.mTextTitle = (TextView) this.mProfileLayout1.findViewById(R.id.txtProfileValue);
        this.mTextType = (TextView) this.mProfileLayout2.findViewById(R.id.txtProfileValue);
        this.mTextWheelSize = (TextView) this.mProfileLayout3.findViewById(R.id.txtProfileValue);
        this.mTextWeight = (TextView) this.mProfileLayout4.findViewById(R.id.txtProfileValue);
        setTextValue(R.id.layoutProfile1);
        setTextValue(R.id.layoutProfile2);
        setTextValue(R.id.layoutProfile3);
        setTextValue(R.id.layoutProfile4);
        this.mBtnStep.setOnClickListener(this);
        return view;
    }

    @Override // android.preference.PreferenceFragment, android.app.Fragment
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!this.mInitialSetup) {
            view.setBackgroundResource(R.drawable.solos_back);
            view.findViewById(R.id.txtStepHeader).setVisibility(4);
        }
    }

    @Override // android.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (this.bike == null) {
            this.bike = Bike.getDefaultBike(activity);
        }
    }

    @Override // com.kopin.solos.common.BaseFragment, android.app.Fragment
    public void onResume() {
        super.onResume();
        if (!this.mInitialSetup) {
            getActivity().getActionBar().setDisplayShowTitleEnabled(true);
            getActivity().getActionBar().setDisplayUseLogoEnabled(false);
            getActivity().getActionBar().setTitle(R.string.add_bike);
        }
    }

    @Override // com.kopin.solos.setup.BaseProfileFragment, android.view.View.OnClickListener
    public void onClick(View view) {
        super.onClick(view);
        Resources resources = getResources();
        switch (this.mSelectedViewResId) {
            case R.id.btnStepSetup /* 2131952171 */:
                addBikeToDB();
                break;
            case R.id.layoutProfile1 /* 2131952324 */:
                showEditTextDialog(R.string.hint_bike_name, this.bike.getName(), 1, -1.0d, -1.0d, 25);
                break;
            case R.id.layoutProfile2 /* 2131952325 */:
                showListDialog(R.string.finish_ride_bike_type_msg);
                break;
            case R.id.layoutProfile3 /* 2131952326 */:
                showEditTextDialog(R.string.hint_wheel_circumference, String.valueOf(this.bike.getWheelSize()), 2, resources.getInteger(R.integer.bike_wheel_size_min), getResources().getInteger(R.integer.bike_wheel_size_max), 4);
                break;
            case R.id.layoutProfile4 /* 2131952328 */:
                showEditTextDialog(getString(R.string.hint_bike_weight, new Object[]{Utility.getUnitOfWeight(getActivity())}), Prefs.isMetric() ? String.valueOf(this.bike.getWeight()) : Utility.roundToTwoDecimalPlaces(Utility.kilogramsToPounds(this.bike.getWeight())), 12290, Prefs.isMetric() ? resources.getInteger(R.integer.bike_weight_min) : ((double) resources.getInteger(R.integer.bike_weight_min_imperial)) / 100.0d, Prefs.isMetric() ? resources.getInteger(R.integer.bike_weight_max) : ((double) resources.getInteger(R.integer.bike_weight_max_imperial)) / 100.0d, 5);
                break;
        }
    }

    @Override // com.kopin.solos.setup.BaseProfileFragment
    void onEditDialogClosed(String value) {
        super.onEditDialogClosed(value);
        switch (this.mSelectedViewResId) {
            case R.id.layoutProfile1 /* 2131952324 */:
                this.bike.setName(value);
                break;
            case R.id.layoutProfile2 /* 2131952325 */:
            case R.id.divider3 /* 2131952327 */:
            default:
                return;
            case R.id.layoutProfile3 /* 2131952326 */:
                this.bike.setWheelSize(Integer.parseInt(value));
                break;
            case R.id.layoutProfile4 /* 2131952328 */:
                if (Prefs.isMetric()) {
                    this.bike.setWeight(Double.parseDouble(value));
                } else {
                    this.bike.setWeight(Utility.poundsToKilograms(Double.parseDouble(value)));
                }
                break;
        }
        setTextValue(this.mSelectedViewResId);
    }

    void showListDialog(int titleResId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(titleResId).setItems(Bike.BikeType.toStringArray(getActivity()), new DialogInterface.OnClickListener() { // from class: com.kopin.solos.setup.BikeProfileFragment.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                Bike.BikeType bikeType = Bike.BikeType.VALUES[i];
                BikeProfileFragment.this.bike.setType(bikeType);
                BikeProfileFragment.this.bike.setWheelSize(bikeType.defaultWheelSize());
                BikeProfileFragment.this.bike.setWeight(bikeType.defaultWeight());
                BikeProfileFragment.this.setTextValue(R.id.layoutProfile2);
                BikeProfileFragment.this.setTextValue(R.id.layoutProfile3);
                BikeProfileFragment.this.setTextValue(R.id.layoutProfile4);
            }
        });
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        DialogUtils.setDialogTitleDivider(dialog);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setTextValue(int profileResId) {
        switch (profileResId) {
            case R.id.layoutProfile1 /* 2131952324 */:
                this.mTextTitle.setText(this.bike.getName());
                break;
            case R.id.layoutProfile2 /* 2131952325 */:
                this.mTextType.setText(this.bike.getType().toString(getActivity()));
                break;
            case R.id.layoutProfile3 /* 2131952326 */:
                this.mTextWheelSize.setText(String.format(Locale.US, "%d mm", Integer.valueOf(this.bike.getWheelSize())));
                break;
            case R.id.layoutProfile4 /* 2131952328 */:
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                this.mTextWeight.setText(Prefs.isMetric() ? String.format("%s kg", decimalFormat.format(this.bike.getWeight())) : Utility.roundToTwoDecimalPlaces(Utility.kilogramsToPounds(this.bike.getWeight())) + " " + Utility.getUnitOfWeight(getActivity()));
                break;
        }
    }

    private void addBikeToDB() {
        if (this.mInitialSetup) {
            ((SetupActivity) getActivity()).setDefaultBike(this.bike);
        } else {
            Sync.addBike(this.bike);
        }
        ((SetupActivity) getActivity()).onStepBtnClick(this.mBtnStep);
    }
}
