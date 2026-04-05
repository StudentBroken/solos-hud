package com.kopin.pupil.bluetooth;

import android.R;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.facebook.internal.AnalyticsEvents;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes25.dex */
public class BluetoothDeviceAdapter extends BaseAdapter {
    private static final String TAG = "BluetoothDeviceAdapter";
    private final ArrayList<BluetoothDevice> mDevices = new ArrayList<>();
    private LayoutInflater mInflater;

    public BluetoothDeviceAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    public void add(BluetoothDevice device) {
        if (!this.mDevices.contains(device)) {
            BluetoothClass bluetoothClass = device.getBluetoothClass();
            if (bluetoothClass.getDeviceClass() == 1028) {
                this.mDevices.add(device);
                notifyDataSetChanged();
                return;
            }
            if (bluetoothClass.getDeviceClass() == 268 || bluetoothClass.getDeviceClass() == 260) {
                if (bluetoothClass.hasService(2097152) && bluetoothClass.hasService(262144) && bluetoothClass.hasService(4194304)) {
                    this.mDevices.add(device);
                    notifyDataSetChanged();
                    return;
                }
                return;
            }
            if (bluetoothClass.toString().contentEquals("7e0100")) {
                this.mDevices.add(device);
                notifyDataSetChanged();
            } else {
                Log.v(TAG, "Device ignored: " + device.getName() + " ; " + device.getAddress());
            }
        }
    }

    public BluetoothDevice get(int index) {
        return this.mDevices.get(index);
    }

    public void clear() {
        this.mDevices.clear();
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.mDevices.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int index) {
        return get(index);
    }

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return 0L;
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = this.mInflater.inflate(R.layout.simple_list_item_1, viewGroup, false);
        }
        TextView textView = (TextView) view.findViewById(R.id.text1);
        if (textView != null) {
            BluetoothDevice device = (BluetoothDevice) getItem(i);
            String name = device.getName();
            if (name == null || name.isEmpty()) {
                name = device.getAddress();
            }
            textView.setText(name + " - " + getBondState(device));
        }
        return view;
    }

    public static String getBondState(BluetoothDevice device) {
        switch (device.getBondState()) {
            case 10:
                return "Not bonded";
            case 11:
                return "Bonding";
            case 12:
                return "Bonded";
            default:
                return AnalyticsEvents.PARAMETER_DIALOG_OUTCOME_VALUE_UNKNOWN;
        }
    }
}
