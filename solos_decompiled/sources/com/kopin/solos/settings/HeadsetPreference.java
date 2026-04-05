package com.kopin.solos.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.Preference;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.kopin.pupil.PupilDevice;
import com.kopin.solos.R;
import com.kopin.solos.common.DialogUtils;

/* JADX INFO: loaded from: classes24.dex */
public class HeadsetPreference extends Preference {
    private static final int MAX_HEADSET_NAME_LENGTH = 30;
    private final View.OnClickListener mSetDeviceName;
    private View viewRoot;

    public HeadsetPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mSetDeviceName = new View.OnClickListener() { // from class: com.kopin.solos.settings.HeadsetPreference.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                final EditText edHeadsetName = new EditText(HeadsetPreference.this.getContext());
                PupilDevice.DeviceInfo currentInfo = PupilDevice.currentDeviceInfo();
                edHeadsetName.setHint(currentInfo != null ? currentInfo.mName : "");
                edHeadsetName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
                edHeadsetName.setMaxLines(1);
                edHeadsetName.setSingleLine();
                edHeadsetName.setInputType(33);
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() { // from class: com.kopin.solos.settings.HeadsetPreference.1.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case -1:
                                String text = edHeadsetName.getText().toString();
                                if (text != null && !text.trim().isEmpty()) {
                                    PupilDevice.setName(text);
                                }
                                HeadsetPreference.this.updateInfo();
                                break;
                        }
                    }
                };
                AlertDialog.Builder alert = new AlertDialog.Builder(HeadsetPreference.this.getContext());
                alert.setTitle(R.string.pref_headset_rename_title).setView(edHeadsetName).setNegativeButton(R.string.dialog_button_cancel, dialogClickListener).setPositiveButton(R.string.dialog_button_save, dialogClickListener);
                AlertDialog dialog = alert.create();
                dialog.getWindow().setSoftInputMode(4);
                dialog.show();
                DialogUtils.setDialogTitleDivider(dialog);
                final Button btnOk = dialog.getButton(-1);
                edHeadsetName.addTextChangedListener(new TextWatcher() { // from class: com.kopin.solos.settings.HeadsetPreference.1.2
                    @Override // android.text.TextWatcher
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override // android.text.TextWatcher
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override // android.text.TextWatcher
                    public void afterTextChanged(Editable s) {
                        boolean enable = !s.toString().trim().isEmpty();
                        if (btnOk != null) {
                            btnOk.setEnabled(enable);
                        }
                        if (!enable) {
                            edHeadsetName.setError(HeadsetPreference.this.getContext().getString(R.string.pref_headset_name_warning));
                        }
                    }
                });
            }
        };
    }

    public HeadsetPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mSetDeviceName = new View.OnClickListener() { // from class: com.kopin.solos.settings.HeadsetPreference.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                final EditText edHeadsetName = new EditText(HeadsetPreference.this.getContext());
                PupilDevice.DeviceInfo currentInfo = PupilDevice.currentDeviceInfo();
                edHeadsetName.setHint(currentInfo != null ? currentInfo.mName : "");
                edHeadsetName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
                edHeadsetName.setMaxLines(1);
                edHeadsetName.setSingleLine();
                edHeadsetName.setInputType(33);
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() { // from class: com.kopin.solos.settings.HeadsetPreference.1.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case -1:
                                String text = edHeadsetName.getText().toString();
                                if (text != null && !text.trim().isEmpty()) {
                                    PupilDevice.setName(text);
                                }
                                HeadsetPreference.this.updateInfo();
                                break;
                        }
                    }
                };
                AlertDialog.Builder alert = new AlertDialog.Builder(HeadsetPreference.this.getContext());
                alert.setTitle(R.string.pref_headset_rename_title).setView(edHeadsetName).setNegativeButton(R.string.dialog_button_cancel, dialogClickListener).setPositiveButton(R.string.dialog_button_save, dialogClickListener);
                AlertDialog dialog = alert.create();
                dialog.getWindow().setSoftInputMode(4);
                dialog.show();
                DialogUtils.setDialogTitleDivider(dialog);
                final Button btnOk = dialog.getButton(-1);
                edHeadsetName.addTextChangedListener(new TextWatcher() { // from class: com.kopin.solos.settings.HeadsetPreference.1.2
                    @Override // android.text.TextWatcher
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override // android.text.TextWatcher
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override // android.text.TextWatcher
                    public void afterTextChanged(Editable s) {
                        boolean enable = !s.toString().trim().isEmpty();
                        if (btnOk != null) {
                            btnOk.setEnabled(enable);
                        }
                        if (!enable) {
                            edHeadsetName.setError(HeadsetPreference.this.getContext().getString(R.string.pref_headset_name_warning));
                        }
                    }
                });
            }
        };
    }

    public HeadsetPreference(Context context) {
        super(context);
        this.mSetDeviceName = new View.OnClickListener() { // from class: com.kopin.solos.settings.HeadsetPreference.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                final EditText edHeadsetName = new EditText(HeadsetPreference.this.getContext());
                PupilDevice.DeviceInfo currentInfo = PupilDevice.currentDeviceInfo();
                edHeadsetName.setHint(currentInfo != null ? currentInfo.mName : "");
                edHeadsetName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
                edHeadsetName.setMaxLines(1);
                edHeadsetName.setSingleLine();
                edHeadsetName.setInputType(33);
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() { // from class: com.kopin.solos.settings.HeadsetPreference.1.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case -1:
                                String text = edHeadsetName.getText().toString();
                                if (text != null && !text.trim().isEmpty()) {
                                    PupilDevice.setName(text);
                                }
                                HeadsetPreference.this.updateInfo();
                                break;
                        }
                    }
                };
                AlertDialog.Builder alert = new AlertDialog.Builder(HeadsetPreference.this.getContext());
                alert.setTitle(R.string.pref_headset_rename_title).setView(edHeadsetName).setNegativeButton(R.string.dialog_button_cancel, dialogClickListener).setPositiveButton(R.string.dialog_button_save, dialogClickListener);
                AlertDialog dialog = alert.create();
                dialog.getWindow().setSoftInputMode(4);
                dialog.show();
                DialogUtils.setDialogTitleDivider(dialog);
                final Button btnOk = dialog.getButton(-1);
                edHeadsetName.addTextChangedListener(new TextWatcher() { // from class: com.kopin.solos.settings.HeadsetPreference.1.2
                    @Override // android.text.TextWatcher
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override // android.text.TextWatcher
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override // android.text.TextWatcher
                    public void afterTextChanged(Editable s) {
                        boolean enable = !s.toString().trim().isEmpty();
                        if (btnOk != null) {
                            btnOk.setEnabled(enable);
                        }
                        if (!enable) {
                            edHeadsetName.setError(HeadsetPreference.this.getContext().getString(R.string.pref_headset_name_warning));
                        }
                    }
                });
            }
        };
    }

    private void setTextField(int id, String text) {
        TextView label = (TextView) this.viewRoot.findViewById(id);
        if (label != null) {
            label.setText(text);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateInfo() {
        if (this.viewRoot != null) {
            if (PupilDevice.isConnected()) {
                this.viewRoot.findViewById(R.id.infoPanel).setVisibility(0);
                this.viewRoot.findViewById(R.id.infoNone).setVisibility(8);
                PupilDevice.DeviceInfo currentInfo = PupilDevice.currentDeviceInfo();
                setTextField(R.id.textDeviceName, currentInfo != null ? currentInfo.mName : "---");
                setTextField(R.id.textDeviceModel, currentInfo != null ? currentInfo.mModel : "---");
                setTextField(R.id.textDeviceVersion, currentInfo != null ? currentInfo.mVersion : "---");
                setTextField(R.id.txtHeadsetSerial, currentInfo != null ? currentInfo.mSerial : "---");
                if (currentInfo.mAntBridgeId > 0) {
                    this.viewRoot.findViewById(R.id.divAnt).setVisibility(0);
                    this.viewRoot.findViewById(R.id.labelAnt).setVisibility(0);
                    this.viewRoot.findViewById(R.id.textAntVersion).setVisibility(0);
                    setTextField(R.id.textAntVersion, currentInfo != null ? currentInfo.mAntBridgeVersion : "---");
                } else {
                    this.viewRoot.findViewById(R.id.divAnt).setVisibility(8);
                    this.viewRoot.findViewById(R.id.labelAnt).setVisibility(8);
                    this.viewRoot.findViewById(R.id.textAntVersion).setVisibility(8);
                }
                PupilDevice.DeviceStatus currentStatus = PupilDevice.currentDeviceStatus();
                setTextField(R.id.textBattery, currentStatus != null ? String.format("%d %%", Byte.valueOf(currentStatus.getBattery())) : "---");
                return;
            }
            this.viewRoot.findViewById(R.id.infoPanel).setVisibility(8);
            this.viewRoot.findViewById(R.id.infoNone).setVisibility(0);
        }
    }

    @Override // android.preference.Preference
    protected View onCreateView(ViewGroup parent) {
        super.onCreateView(parent);
        this.viewRoot = View.inflate(getContext(), R.layout.layout_headset, null);
        updateInfo();
        this.viewRoot.findViewById(R.id.layoutHeadsetName).setOnClickListener(this.mSetDeviceName);
        return this.viewRoot;
    }

    public void refresh() {
        updateInfo();
    }
}
