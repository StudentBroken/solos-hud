package com.kopin.solos.sensors.btle;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import com.kopin.accessory.utility.CallHelper;
import com.kopin.solos.sensors.R;
import com.kopin.solos.sensors.Sensor;
import com.kopin.solos.sensors.SensorData;
import com.kopin.solos.sensors.btle.CableSensor;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* JADX INFO: loaded from: classes28.dex */
public class BTLESensorConfig {

    private enum PowerConfigRows {
        ROW_MANUAL_CALIBRATION,
        ROW_AUTO_ZERO_ON,
        ROW_AUTO_ZERO_OFF,
        ROW_GET_CRANK_LENGTH,
        ROW_SET_CRANK_LENGTH,
        ROW_MAX
    }

    static Dialog configurePowerSensor(Context context, final BTLESensor sensor) {
        BikePowerControlAdapter configureBikePower = new BikePowerControlAdapter(context, sensor);
        Dialog dlg = new AlertDialog.Builder(context).setTitle(sensor.getName()).setIcon(R.drawable.ic_sensor_config).setCancelable(true).setAdapter(configureBikePower, configureBikePower).setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.kopin.solos.sensors.btle.BTLESensorConfig.1
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialog) {
                sensor.setConfigObserver(null);
            }
        }).create();
        configureBikePower.setRootDialog(dlg);
        return dlg;
    }

    private static class BikePowerControlAdapter extends BaseAdapter implements Sensor.SensorConfigObserver, DialogInterface.OnClickListener, TextWatcher, View.OnFocusChangeListener, TextView.OnEditorActionListener {
        private final Context mContext;
        private Dialog mDialog;
        private Handler mHandler;
        private final BTLESensor mSensor;
        private boolean modified;
        private final View.OnClickListener mManualCalibrationClick = new View.OnClickListener() { // from class: com.kopin.solos.sensors.btle.BTLESensorConfig.BikePowerControlAdapter.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (BikePowerControlAdapter.this.mSensor instanceof CableSensor.CableSensorStub) {
                    ((CableSensor.CableSensorStub) BikePowerControlAdapter.this.mSensor).requestAntCalibration(true, false);
                } else {
                    BikePowerControlAdapter.this.mSensor.writeReq(BTLESensorConfig.requestManualCalibration());
                }
            }
        };
        private final View.OnClickListener mAutoCalibrationClick = new View.OnClickListener() { // from class: com.kopin.solos.sensors.btle.BTLESensorConfig.BikePowerControlAdapter.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (BikePowerControlAdapter.this.mSensor instanceof CableSensor.CableSensorStub) {
                    ((CableSensor.CableSensorStub) BikePowerControlAdapter.this.mSensor).requestAntCalibration(false, true);
                } else {
                    BikePowerControlAdapter.this.mSensor.writeReq(BTLESensorConfig.requestAutoCalibration());
                }
            }
        };
        private final View.OnClickListener mAutoCalibrationOffClick = new View.OnClickListener() { // from class: com.kopin.solos.sensors.btle.BTLESensorConfig.BikePowerControlAdapter.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (BikePowerControlAdapter.this.mSensor instanceof CableSensor.CableSensorStub) {
                    ((CableSensor.CableSensorStub) BikePowerControlAdapter.this.mSensor).requestAntCalibration(false, false);
                }
            }
        };
        private final View.OnClickListener mGetCrankLengthClick = new View.OnClickListener() { // from class: com.kopin.solos.sensors.btle.BTLESensorConfig.BikePowerControlAdapter.5
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                BikePowerControlAdapter.this.mSensor.writeReq(BTLESensorConfig.requestGetCrankLength());
            }
        };

        BikePowerControlAdapter(Context context, BTLESensor sensor) {
            this.mContext = context;
            this.mSensor = sensor;
            sensor.setConfigObserver(this);
            this.mHandler = new Handler();
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return PowerConfigRows.ROW_MAX.ordinal();
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
            boolean hasConfigured = false;
            SensorData config = this.mSensor.getLastConfig();
            switch (PowerConfigRows.values()[position]) {
                case ROW_MANUAL_CALIBRATION:
                    View view = View.inflate(this.mContext, R.layout.row_config_button, null);
                    TextView textView = (TextView) view.findViewById(R.id.textLabel);
                    textView.setText(R.string.sensor_config_power_manual);
                    if ((this.mSensor instanceof CableSensor.CableSensorStub) || ((config instanceof CrankPowerConfig) && ((CrankPowerConfig) config).hasCompensationAdjust())) {
                        hasConfigured = true;
                    }
                    textView.setFocusable(hasConfigured);
                    textView.setEnabled(hasConfigured);
                    textView.setOnClickListener(this.mManualCalibrationClick);
                    return view;
                case ROW_AUTO_ZERO_ON:
                    View view2 = View.inflate(this.mContext, R.layout.row_config_button, null);
                    TextView textView2 = (TextView) view2.findViewById(R.id.textLabel);
                    textView2.setText(R.string.sensor_config_power_auto);
                    boolean hasConfigured2 = this.mSensor instanceof CableSensor.CableSensorStub;
                    textView2.setFocusable(hasConfigured2);
                    textView2.setEnabled(hasConfigured2);
                    textView2.setOnClickListener(this.mAutoCalibrationClick);
                    return view2;
                case ROW_AUTO_ZERO_OFF:
                    View view3 = View.inflate(this.mContext, R.layout.row_config_button, null);
                    TextView textView3 = (TextView) view3.findViewById(R.id.textLabel);
                    textView3.setText(R.string.sensor_config_power_auto_off);
                    boolean hasConfigured3 = this.mSensor instanceof CableSensor.CableSensorStub;
                    textView3.setFocusable(hasConfigured3);
                    textView3.setEnabled(hasConfigured3);
                    textView3.setOnClickListener(this.mAutoCalibrationOffClick);
                    return view3;
                case ROW_GET_CRANK_LENGTH:
                    View view4 = View.inflate(this.mContext, R.layout.row_config_button, null);
                    TextView textView4 = (TextView) view4.findViewById(R.id.textLabel);
                    textView4.setText(R.string.sensor_config_get_crank_legnth);
                    if ((config instanceof CrankPowerConfig) && ((CrankPowerConfig) config).hasCrankAdjust()) {
                        hasConfigured = true;
                    }
                    textView4.setFocusable(hasConfigured);
                    textView4.setEnabled(hasConfigured);
                    textView4.setOnClickListener(this.mGetCrankLengthClick);
                    return view4;
                case ROW_SET_CRANK_LENGTH:
                    View view5 = View.inflate(this.mContext, R.layout.row_config_input, null);
                    TextView title = (TextView) view5.findViewById(R.id.textLabel);
                    title.setText(R.string.sensor_config_set_crank_legnth);
                    if (config instanceof CrankPowerConfig) {
                        EditText mEditView = (EditText) view5.findViewById(R.id.textValue);
                        mEditView.setText(Double.toString(((CrankPowerConfig) config).getCrankLength()));
                        mEditView.setHint(R.string.sensor_config_hint_crank_length);
                        mEditView.setOnFocusChangeListener(this);
                        mEditView.addTextChangedListener(this);
                        mEditView.setOnEditorActionListener(this);
                        mEditView.setInputType(8194);
                        boolean hasConfig = ((CrankPowerConfig) this.mSensor.getLastConfig()).hasCrankAdjust();
                        mEditView.setEnabled(hasConfig);
                        title.setEnabled(hasConfig);
                        mEditView.setFilters(new InputFilter[]{new InputFilter() { // from class: com.kopin.solos.sensors.btle.BTLESensorConfig.BikePowerControlAdapter.1
                            @Override // android.text.InputFilter
                            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                                if (end > start) {
                                    String destStr = dest.toString();
                                    String resultStr = destStr.substring(0, dstart) + ((Object) source.subSequence(start, end)) + destStr.substring(dend);
                                    int maxLength = resultStr.contains(".") ? 5 : 3;
                                    try {
                                        double number = Double.valueOf(resultStr).doubleValue();
                                        if (resultStr.length() <= maxLength) {
                                            if ((2.0d * number) % 1.0d == 0.0d) {
                                                return null;
                                            }
                                        }
                                        return "";
                                    } catch (NumberFormatException e) {
                                        return "";
                                    }
                                }
                                return null;
                            }
                        }});
                        return view5;
                    }
                    return view5;
                default:
                    return null;
            }
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            switch (PowerConfigRows.values()[which]) {
                case ROW_MANUAL_CALIBRATION:
                    this.mManualCalibrationClick.onClick(null);
                    break;
                case ROW_AUTO_ZERO_ON:
                    this.mAutoCalibrationClick.onClick(null);
                    break;
                case ROW_AUTO_ZERO_OFF:
                    this.mAutoCalibrationOffClick.onClick(null);
                    break;
                case ROW_GET_CRANK_LENGTH:
                    this.mGetCrankLengthClick.onClick(null);
                    break;
            }
        }

        private void setCrankLength(EditText mEditView) {
            if (this.modified && mEditView != null) {
                try {
                    String length = mEditView.getText().toString();
                    double len = Double.parseDouble(length);
                    this.mSensor.writeReq(BTLESensorConfig.requestSetCrankLength(len));
                    SensorData config = this.mSensor.getLastConfig();
                    if (config instanceof CrankPowerConfig) {
                        ((CrankPowerConfig) config).setCrankLength(len);
                    }
                } catch (NumberFormatException e) {
                }
                this.modified = false;
            }
        }

        void setRootDialog(Dialog dlg) {
            this.mDialog = dlg;
        }

        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                this.mDialog.getWindow().clearFlags(131080);
                this.mDialog.getWindow().setSoftInputMode(5);
            } else if (v instanceof EditText) {
                setCrankLength((EditText) v);
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

        @Override // android.widget.TextView.OnEditorActionListener
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (((event != null && event.getKeyCode() == 66) || actionId == 6) && (v instanceof EditText)) {
                setCrankLength((EditText) v);
                return false;
            }
            return false;
        }

        @Override // com.kopin.solos.sensors.Sensor.SensorConfigObserver
        public void onSensorConfigChanged(Sensor sensor) {
            this.mHandler.post(new Runnable() { // from class: com.kopin.solos.sensors.btle.BTLESensorConfig.BikePowerControlAdapter.6
                @Override // java.lang.Runnable
                public void run() {
                    BikePowerControlAdapter.this.notifyDataSetInvalidated();
                }
            });
        }

        @Override // com.kopin.solos.sensors.Sensor.SensorConfigObserver
        public void onSensorDisconnected(Sensor sensor) {
            Log.d("SensorConfig", "Connection to sensor being configured lost, closing dialog");
            this.mDialog.dismiss();
        }
    }

    static abstract class ConfigControlRequest {
        public abstract String debugName();

        public abstract byte[] toValue();

        ConfigControlRequest() {
        }
    }

    static ConfigControlRequest requestGetCrankLength() {
        return new ConfigControlRequest() { // from class: com.kopin.solos.sensors.btle.BTLESensorConfig.2
            @Override // com.kopin.solos.sensors.btle.BTLESensorConfig.ConfigControlRequest
            public String debugName() {
                return "Request Crank Length";
            }

            @Override // com.kopin.solos.sensors.btle.BTLESensorConfig.ConfigControlRequest
            public byte[] toValue() {
                return new byte[]{5};
            }
        };
    }

    static ConfigControlRequest requestSetCrankLength(final double length) {
        return new ConfigControlRequest() { // from class: com.kopin.solos.sensors.btle.BTLESensorConfig.3
            @Override // com.kopin.solos.sensors.btle.BTLESensorConfig.ConfigControlRequest
            public String debugName() {
                return "Set Crank Length: " + length;
            }

            @Override // com.kopin.solos.sensors.btle.BTLESensorConfig.ConfigControlRequest
            public byte[] toValue() {
                int len = (int) (length * 2.0d);
                return new byte[]{4, (byte) (len & 255), (byte) (len >> 8)};
            }
        };
    }

    static ConfigControlRequest requestManualCalibration() {
        return new ConfigControlRequest() { // from class: com.kopin.solos.sensors.btle.BTLESensorConfig.4
            @Override // com.kopin.solos.sensors.btle.BTLESensorConfig.ConfigControlRequest
            public String debugName() {
                return "Request Manual Calibration";
            }

            @Override // com.kopin.solos.sensors.btle.BTLESensorConfig.ConfigControlRequest
            public byte[] toValue() {
                return new byte[]{CallHelper.CallState.SCO_DISCONNECTED};
            }
        };
    }

    static ConfigControlRequest requestAutoCalibration() {
        return new ConfigControlRequest() { // from class: com.kopin.solos.sensors.btle.BTLESensorConfig.5
            @Override // com.kopin.solos.sensors.btle.BTLESensorConfig.ConfigControlRequest
            public String debugName() {
                return "Request Auto Zero";
            }

            @Override // com.kopin.solos.sensors.btle.BTLESensorConfig.ConfigControlRequest
            public byte[] toValue() {
                return new byte[]{CallHelper.CallState.FLAG_HFP_STATE};
            }
        };
    }

    public static class CrankPowerResponse {
        public String mMessage;
        public boolean mSuccess;

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        static CrankPowerResponse fromValue(byte stat, byte opcode) {
            String op;
            CrankPowerResponse self = new CrankPowerResponse();
            if (stat == 1) {
                self.mSuccess = true;
                switch (opcode) {
                    case 4:
                        self.mMessage = "Set Crank Length OK";
                        break;
                    case 5:
                        self.mMessage = "Got Crank Length";
                        break;
                    case 12:
                        self.mMessage = "Requested Manual Calibration";
                        break;
                    case 16:
                        self.mMessage = "Requested Auto Zero";
                        break;
                }
            } else {
                switch (opcode) {
                    case 4:
                        op = "Set Crank Length";
                        break;
                    case 5:
                        op = "Get Crank Length";
                        break;
                    case 12:
                        op = "Calibration";
                        break;
                    case 16:
                        op = "Auto Zero";
                        break;
                    default:
                        op = Integer.toHexString(opcode);
                        break;
                }
                self.mSuccess = false;
                if (stat == 2) {
                    self.mMessage = "Operation '" + op + "' not supported";
                } else if (stat == 3) {
                    self.mMessage = "Invalid parameter";
                } else if (stat == 4) {
                    self.mMessage = "Operation '" + op + "' failed";
                } else {
                    self.mMessage = "Unknown Error " + ((int) stat) + " for request '" + op + "'";
                }
            }
            return self;
        }
    }

    public static class CrankPowerConfig extends SensorData {
        private static final int FLAG_CRANK_LENGTH = 4096;
        private static final int FLAG_HAS_CRANK_REVS = 8;
        private static final int FLAG_HAS_WHEEL_REVS = 4;
        private static final int FLAG_OFFSET_COMPENSATION = 512;
        private static final int FLAG_OFFSET_INDICATOR = 256;
        private static final String TAG = "BTLESensorPowerConfig";
        private int mCalibration;
        private int mCrankLength;
        private int mFlags;

        void flagsFromValue(byte[] value) {
            if (value != null && value.length != 0) {
                ByteBuffer buffer = ByteBuffer.wrap(value);
                buffer.order(ByteOrder.LITTLE_ENDIAN);
                try {
                    if (buffer.limit() < 4) {
                        this.mFlags = buffer.getShort() & 65535;
                    } else {
                        this.mFlags = buffer.getInt();
                    }
                } catch (BufferUnderflowException bue) {
                    bue.printStackTrace();
                }
                Log.d(TAG, "Power Configuration: " + (hasSpeed() ? "HAS SPEED, " : "") + (hasCadence() ? "HAS CADENCE" : ""));
                Log.d(TAG, "Power Configuration: " + (hasCrankAdjust() ? "HAS CRANK ADJUST, " : "") + (hasCompensationAdjust() ? "HAS OFFSET COMPENSATION" : ""));
            }
        }

        CrankPowerResponse responseFromValue(byte[] value) {
            byte tmp;
            Log.d(TAG, "Got indication: " + value);
            ByteBuffer buffer = ByteBuffer.wrap(value);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            try {
                tmp = buffer.get();
            } catch (BufferUnderflowException bue) {
                bue.printStackTrace();
            }
            if (tmp == 32) {
                Log.d(TAG, "  Response msg");
                byte op = buffer.get();
                byte stat = buffer.get();
                Log.d(TAG, String.format("   Opcode: %02x, Status: %02x", Byte.valueOf(op), Byte.valueOf(stat)));
                CrankPowerResponse resp = CrankPowerResponse.fromValue(stat, op);
                if (stat == 1) {
                    switch (op) {
                        case 4:
                            Log.d(TAG, "    Set crank length OK");
                            break;
                        case 5:
                            this.mCrankLength = buffer.getShort() & 65535;
                            Log.d(TAG, "    Crank Length: " + (this.mCrankLength / 2));
                            resp.mMessage = "Crank Length: " + (this.mCrankLength / 2);
                            break;
                        case 12:
                            this.mCalibration = buffer.getShort() & 65535;
                            Log.d(TAG, "    Manual Calibration OK: " + this.mCalibration);
                            resp.mMessage = "Manual Calibration OK: " + this.mCalibration;
                            break;
                        case 16:
                            this.mCalibration = buffer.getShort() & 65535;
                            Log.d(TAG, "    Auto Zero OK: " + this.mCalibration);
                            resp.mMessage = "Auto Zero OK: " + this.mCalibration;
                            break;
                    }
                    return resp;
                }
                Log.d(TAG, "   Operation failed?");
                return resp;
            }
            Log.d(TAG, "  Unexpected msg: " + ((int) tmp));
            return null;
        }

        public boolean hasCompensationAdjust() {
            return (this.mFlags & 512) != 0;
        }

        public boolean hasCrankAdjust() {
            return (this.mFlags & 4096) != 0;
        }

        void setCrankLength(double length) {
            this.mCrankLength = (int) (2.0d * length);
        }

        public double getCrankLength() {
            return ((double) this.mCrankLength) / 2.0d;
        }

        public int getCalibrationOffset() {
            return this.mCalibration;
        }

        boolean hasSpeed() {
            return (this.mFlags & 4) != 0;
        }

        boolean hasCadence() {
            return (this.mFlags & 8) != 0;
        }
    }
}
