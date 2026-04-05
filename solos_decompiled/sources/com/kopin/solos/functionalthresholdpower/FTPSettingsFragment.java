package com.kopin.solos.functionalthresholdpower;

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
import com.kopin.solos.sensors.Sensor;
import com.kopin.solos.sensors.SensorList;
import com.kopin.solos.share.Config;
import com.kopin.solos.share.Sync;
import com.kopin.solos.storage.FTP;
import com.kopin.solos.storage.FTPHelper;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.util.Utility;

/* JADX INFO: loaded from: classes24.dex */
public class FTPSettingsFragment extends SafeFragment {
    private static final String ARG_PARAM1 = "param1";
    private View boxFtpResult;
    private TextView btnFunctionalThresholdPower;
    private String mParam1;
    private TextView txtFtpScore;

    public static FTPSettingsFragment newInstance() {
        FTPSettingsFragment fragment = new FTPSettingsFragment();
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
        View view = inflater.inflate(R.layout.fragment_functional_threshold_power_settings, container, false);
        this.txtFtpScore = (TextView) view.findViewById(R.id.txtFtpScore);
        this.boxFtpResult = view.findViewById(R.id.boxFtpResult);
        this.btnFunctionalThresholdPower = (TextView) view.findViewById(R.id.btnFunctionalThresholdPower);
        this.btnFunctionalThresholdPower.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.functionalthresholdpower.FTPSettingsFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                FTPSettingsFragment.this.startFTP();
            }
        });
        view.findViewById(R.id.layoutManualFtp).setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.functionalthresholdpower.FTPSettingsFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                FTPSettingsFragment.this.manualFtpEntry();
            }
        });
        getActivity().getActionBar().setNavigationMode(0);
        getActivity().getActionBar().setTitle(R.string.functional_threshold_power);
        getActivity().getActionBar().setDisplayShowTitleEnabled(true);
        updateFtp();
        return view;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFtp() {
        resetFTPNavigation();
        FTP ftp = FTPHelper.getFTP();
        this.txtFtpScore.setText(FTPHelper.getFormattedFTP());
        this.boxFtpResult.setVisibility((ftp == null || ftp.mValue <= 0.0d) ? 4 : 0);
        this.btnFunctionalThresholdPower.setText((ftp == null || ftp.mValue <= 0.0d) ? R.string.btn_ftp_test : R.string.btn_ftp_retake_test);
    }

    private void resetFTPNavigation() {
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
            updateFtp();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startFTP() {
        if (LiveRide.isStarted()) {
            displayErrorDialog(R.string.dialog_ftp_during_ride);
        } else if (!isPowerSensorConnected()) {
            displayErrorDialog(R.string.dialog_ftp_no_sensor);
        } else {
            ((MainActivity) getActivity()).showFunctionalThresholdPower();
        }
    }

    private void displayErrorDialog(int resId) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() { // from class: com.kopin.solos.functionalthresholdpower.FTPSettingsFragment.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(resId).setPositiveButton(R.string.dialog_button_ok, dialogClickListener).show();
    }

    private boolean isPowerSensorConnected() {
        return SensorList.isSensorConnected(Sensor.DataType.POWER) || SensorList.isSensorConnected(Sensor.DataType.KICK) || Config.FAKE_DATA;
    }

    @Override // android.app.Fragment
    public void onDetach() {
        super.onDetach();
        getActivity().getActionBar().setDisplayUseLogoEnabled(false);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void manualFtpEntry() {
        final EditText edManualFtp = new EditText(getActivity());
        edManualFtp.setInputType(8194);
        int ftpLen = getResources().getInteger(R.integer.ftp_len);
        if (ftpLen > 0) {
            edManualFtp.setFilters(new InputFilter[]{new InputFilter.LengthFilter(ftpLen)});
        }
        FTP ftp = FTPHelper.getFTP();
        if (ftp != null && ftp.mValue >= getResources().getInteger(R.integer.ftp_min)) {
            String currentFtp = FTPHelper.getFormattedFTP();
            edManualFtp.setHint(currentFtp);
        }
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() { // from class: com.kopin.solos.functionalthresholdpower.FTPSettingsFragment.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case -1:
                        String newFtp = edManualFtp.getText().toString();
                        if (newFtp != null && !newFtp.isEmpty()) {
                            try {
                                double value = Double.parseDouble(newFtp);
                                if (value >= 0.0d && value <= FTPSettingsFragment.this.getResources().getInteger(R.integer.ftp_max)) {
                                    if (value == 0.0d) {
                                        FTPHelper.reset();
                                    }
                                    Sync.addFTP(FTPHelper.setFtp(System.currentTimeMillis(), value));
                                    FTPSettingsFragment.this.updateFtp();
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
        builder.setView(edManualFtp);
        builder.setTitle(R.string.manual_ftp_dialog_title).setPositiveButton(R.string.dialog_button_ok, dialogClickListener).setNegativeButton(R.string.dialog_button_cancel, dialogClickListener);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(4);
        dialog.show();
        DialogUtils.setDialogTitleDivider(dialog);
        final Button btnOk = dialog.getButton(-1);
        edManualFtp.addTextChangedListener(new TextWatcher() { // from class: com.kopin.solos.functionalthresholdpower.FTPSettingsFragment.5
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
                String text = edManualFtp.getText() == null ? "" : edManualFtp.getText().toString();
                String hint = edManualFtp.getHint() == null ? "" : edManualFtp.getHint().toString();
                boolean enable = FTPSettingsFragment.this.onCheckFtpValue(text, hint);
                if (btnOk != null) {
                    btnOk.setEnabled(enable);
                }
                if (!enable) {
                    edManualFtp.setError("Enter valid value");
                }
            }
        });
    }

    protected boolean onCheckFtpValue(String value, String hint) {
        if ((value == null || value.trim().isEmpty()) && ((value = hint) == null || value.trim().isEmpty())) {
            return false;
        }
        double nr = Utility.doubleFromString(value, -1.0d);
        return nr >= 0.0d && nr <= ((double) getResources().getInteger(R.integer.ftp_max));
    }
}
