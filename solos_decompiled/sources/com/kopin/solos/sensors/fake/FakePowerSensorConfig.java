package com.kopin.solos.sensors.fake;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import com.kopin.solos.sensors.R;

/* JADX INFO: loaded from: classes28.dex */
public class FakePowerSensorConfig {
    static Dialog configurePowerSensor(Context context, FakeSensor sensor) {
        BikePowerControlAdapter configureBikePower = new BikePowerControlAdapter(context, sensor);
        Dialog dlg = new AlertDialog.Builder(context).setTitle(sensor.getName()).setIcon(R.drawable.ic_sensor_config).setCancelable(true).setAdapter(configureBikePower, configureBikePower).create();
        configureBikePower.setRootDialog(dlg);
        return dlg;
    }

    private static class BikePowerControlAdapter extends BaseAdapter implements DialogInterface.OnClickListener, TextWatcher, View.OnFocusChangeListener {
        private final Context mContext;
        private Dialog mDialog;
        private final FakeSensor mSensor;
        private boolean modified;

        BikePowerControlAdapter(Context context, FakeSensor sensor) {
            this.mContext = context;
            this.mSensor = sensor;
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return 3;
        }

        @Override // android.widget.Adapter
        public Object getItem(int position) {
            return null;
        }

        @Override // android.widget.Adapter
        public long getItemId(int position) {
            return position;
        }

        @Override // android.widget.Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            switch (position) {
                case 0:
                    View view = View.inflate(this.mContext, R.layout.row_config_button, null);
                    ((TextView) view.findViewById(R.id.textLabel)).setText("Request Manual Calibration");
                    ((TextView) view.findViewById(R.id.textLabel)).setFocusable(true);
                    ((TextView) view.findViewById(R.id.textLabel)).setEnabled(false);
                    return view;
                case 1:
                    View view2 = View.inflate(this.mContext, R.layout.row_config_button, null);
                    ((TextView) view2.findViewById(R.id.textLabel)).setText("Request Auto Calibration");
                    return view2;
                case 2:
                    View view3 = View.inflate(this.mContext, R.layout.row_config_input, null);
                    ((TextView) view3.findViewById(R.id.textLabel)).setText("Set Crank Length (mm)");
                    EditText edit = (EditText) view3.findViewById(R.id.textValue);
                    String crankLength = this.mSensor.readConfig("crank");
                    edit.setText(crankLength);
                    edit.setOnFocusChangeListener(this);
                    edit.addTextChangedListener(this);
                    return view3;
                default:
                    return null;
            }
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case 0:
                    onManualCalibrationClick();
                    break;
                case 1:
                    onAutoCalibrationClick();
                    break;
            }
        }

        private void onManualCalibrationClick() {
        }

        private void onAutoCalibrationClick() {
        }

        void setRootDialog(Dialog dlg) {
            this.mDialog = dlg;
        }

        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                this.mDialog.getWindow().clearFlags(131080);
                this.mDialog.getWindow().setSoftInputMode(5);
            } else if (this.modified) {
                this.mSensor.writeConfig("crank", ((EditText) v).getText().toString());
                this.modified = false;
            }
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            this.modified = true;
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable s) {
        }
    }
}
