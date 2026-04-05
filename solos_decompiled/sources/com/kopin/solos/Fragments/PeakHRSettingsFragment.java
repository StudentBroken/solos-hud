package com.kopin.solos.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.kopin.solos.MainActivity;
import com.kopin.solos.R;
import com.kopin.solos.common.DialogUtils;
import com.kopin.solos.common.SafeFragment;
import com.kopin.solos.share.Sync;
import com.kopin.solos.storage.FTP;
import com.kopin.solos.storage.FTPHelper;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.settings.UserProfile;
import com.kopin.solos.storage.util.HeartRateZones;
import com.kopin.solos.storage.util.Utility;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes24.dex */
public class PeakHRSettingsFragment extends SafeFragment {
    private static final String ARG_PARAM1 = "param1";
    private View boxHrzResult;
    private String mParam1;
    private TextView txtPeakHR;
    private View zonesPreview;

    public static PeakHRSettingsFragment newInstance() {
        PeakHRSettingsFragment fragment = new PeakHRSettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override // com.kopin.solos.common.SafeFragment, android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override // android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_peak_heartrate_settings, container, false);
        this.txtPeakHR = (TextView) view.findViewById(R.id.txtHrzScore);
        this.boxHrzResult = view.findViewById(R.id.boxHrzResult);
        view.findViewById(R.id.layoutManualHrz).setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.Fragments.PeakHRSettingsFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                PeakHRSettingsFragment.this.manualPeakHREntry();
            }
        });
        getActivity().getActionBar().setNavigationMode(0);
        getActivity().getActionBar().setTitle(R.string.effective_peak_heartrate);
        getActivity().getActionBar().setDisplayShowTitleEnabled(true);
        this.zonesPreview = view.findViewById(R.id.layoutZonesPreview);
        updatePeakHR();
        return view;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePeakHR() {
        resetNavigation();
        FTP phr = FTPHelper.getPeakHR();
        if (phr != null && phr.mValue > 0.0d) {
            this.txtPeakHR.setText(Integer.toString((int) phr.mValue));
            HeartRateZones.setMaxHR((int) phr.mValue);
        } else {
            int peak = HeartRateZones.setFromAge(UserProfile.getAge());
            this.txtPeakHR.setText(Integer.toString(peak));
        }
        this.boxHrzResult.setVisibility(0);
        List<String> zones = new ArrayList<>();
        List<Integer> hrZoneVals = HeartRateZones.getHeartRateZones();
        zones.add(String.valueOf(hrZoneVals.get(0)));
        for (int i = 1; i < hrZoneVals.size(); i++) {
            zones.add(String.valueOf(hrZoneVals.get(i)));
            zones.add(String.valueOf(hrZoneVals.get(i).intValue() + 1));
        }
        fillZonesCategories(zones);
    }

    private void fillZonesCategories(List<String> zones) {
        ((TextView) this.zonesPreview.findViewById(R.id.hr_zone1_bpm_min)).setText(zones.get(0));
        ((TextView) this.zonesPreview.findViewById(R.id.hr_zone1_bpm_max)).setText(zones.get(1));
        ((TextView) this.zonesPreview.findViewById(R.id.hr_zone2_bpm_min)).setText(zones.get(2));
        ((TextView) this.zonesPreview.findViewById(R.id.hr_zone2_bpm_max)).setText(zones.get(3));
        ((TextView) this.zonesPreview.findViewById(R.id.hr_zone3_bpm_min)).setText(zones.get(4));
        ((TextView) this.zonesPreview.findViewById(R.id.hr_zone3_bpm_max)).setText(zones.get(5));
        ((TextView) this.zonesPreview.findViewById(R.id.hr_zone4_bpm_min)).setText(zones.get(6));
        ((TextView) this.zonesPreview.findViewById(R.id.hr_zone4_bpm_max)).setText(zones.get(7));
        ((TextView) this.zonesPreview.findViewById(R.id.hr_zone5_bpm_min)).setText(zones.get(8));
        ((TextView) this.zonesPreview.findViewById(R.id.hr_zone5_bpm_max)).setText(zones.get(9));
        ((TextView) this.zonesPreview.findViewById(R.id.hr_zone6_bpm_min)).setText(zones.get(10));
        ((TextView) this.zonesPreview.findViewById(R.id.hr_zone6_bpm_max)).setText("+");
    }

    private void resetNavigation() {
        getActivity().getActionBar().setDisplayShowHomeEnabled(true);
        getActivity().getActionBar().setDisplayUseLogoEnabled(true);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override // com.kopin.solos.common.SafeFragment, android.app.Fragment
    public void onResume() {
        super.onResume();
        if (LiveRide.isActiveFtp()) {
            ((MainActivity) getActivity()).showFunctionalThresholdPower();
        } else {
            updatePeakHR();
        }
    }

    @Override // android.app.Fragment
    public void onDetach() {
        super.onDetach();
        getActivity().getActionBar().setDisplayUseLogoEnabled(false);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void manualPeakHREntry() {
        final EditText edManualPeakHR = new EditText(getActivity());
        edManualPeakHR.setInputType(8194);
        int hrzLen = getResources().getInteger(R.integer.target_heartrate_len);
        if (hrzLen > 0) {
            edManualPeakHR.setFilters(new InputFilter[]{new InputFilter.LengthFilter(hrzLen)});
        }
        FTP phr = FTPHelper.getPeakHR();
        if (phr != null && phr.mValue > 25.0d) {
            String currentFtp = Integer.toString((int) phr.mValue);
            edManualPeakHR.setHint(currentFtp);
        } else {
            edManualPeakHR.setHint(Integer.toString(220 - UserProfile.getAge()));
        }
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() { // from class: com.kopin.solos.Fragments.PeakHRSettingsFragment.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case -1:
                        String newPeakHR = edManualPeakHR.getText().toString();
                        if (newPeakHR != null && !newPeakHR.isEmpty()) {
                            try {
                                int value = Integer.parseInt(newPeakHR);
                                if (value >= 0 && value <= PeakHRSettingsFragment.this.getResources().getInteger(R.integer.target_heartrate_max)) {
                                    Sync.addFTP(FTPHelper.setPeakHR(System.currentTimeMillis(), value));
                                    PeakHRSettingsFragment.this.updatePeakHR();
                                    break;
                                }
                            } catch (NumberFormatException e) {
                                return;
                            }
                        }
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(edManualPeakHR);
        builder.setTitle(R.string.manual_hrz_dialog_title).setPositiveButton(R.string.dialog_button_ok, dialogClickListener).setNegativeButton(R.string.dialog_button_cancel, dialogClickListener);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(4);
        dialog.show();
        DialogUtils.setDialogTitleDivider(dialog);
        final Button btnOk = dialog.getButton(-1);
        edManualPeakHR.addTextChangedListener(new TextWatcher() { // from class: com.kopin.solos.Fragments.PeakHRSettingsFragment.3
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
                String text = edManualPeakHR.getText() == null ? "" : edManualPeakHR.getText().toString();
                String hint = edManualPeakHR.getHint() == null ? "" : edManualPeakHR.getHint().toString();
                boolean enable = PeakHRSettingsFragment.this.onCheckPeakHRValue(text, hint);
                if (btnOk != null) {
                    btnOk.setEnabled(enable);
                }
                if (!enable) {
                    edManualPeakHR.setError("Enter valid value");
                }
            }
        });
    }

    protected boolean onCheckPeakHRValue(String value, String hint) {
        if ((value == null || value.trim().isEmpty()) && ((value = hint) == null || value.trim().isEmpty())) {
            return false;
        }
        double nr = Utility.doubleFromString(value, -1.0d);
        return nr >= 0.0d && nr <= ((double) getResources().getInteger(R.integer.target_heartrate_max));
    }
}
