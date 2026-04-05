package com.kopin.solos.setup;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.kopin.pupil.PupilConnector;
import com.kopin.pupil.PupilDevice;
import com.kopin.pupil.bluetooth.BluetoothConnector;
import com.kopin.pupil.bluetooth.BluetoothUtilities;
import com.kopin.pupil.bluetooth.DeviceLister;
import com.kopin.solos.Fragments.BaseServiceFragment;
import com.kopin.solos.R;
import com.kopin.solos.SetupActivity;

/* JADX INFO: loaded from: classes24.dex */
public class HeadsetConnectFragment extends BaseServiceFragment implements BluetoothUtilities.DiscoveryListener, View.OnClickListener {
    BluetoothDevice mBluetoothDevice;
    BluetoothUtilities mBluetoothUtilities;
    View mButtonBar;
    private volatile boolean mConnecting;
    TextView mDeviceStatusText;
    View mLayoutStepMsg;
    ProgressBar mProgressBar;

    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mBluetoothDevice = (BluetoothDevice) getArguments().getParcelable(SetupActivity.BT_DEVICE_KEY);
        this.mBluetoothUtilities = new BluetoothUtilities(getActivity(), this);
        ViewGroup view = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);
        inflater.inflate(R.layout.fragment_headset_connect, (ViewGroup) this.mContainerLayout, true);
        this.mStepHeader.setText(R.string.msg_connect_headset);
        this.mLayoutStepMsg = view.findViewById(R.id.layoutStepMsg);
        this.mProgressBar = (ProgressBar) view.findViewById(R.id.progressSpinner);
        this.mButtonBar = view.findViewById(R.id.btnBar);
        this.mButtonBar.setVisibility(4);
        this.mBtnStep.setEnabled(false);
        this.mDeviceStatusText = (TextView) view.findViewById(R.id.txtDeviceStatus);
        TextView deviceName = (TextView) view.findViewById(R.id.txtDeviceTitle);
        deviceName.setText(this.mBluetoothDevice.getName());
        view.findViewById(R.id.btnBack).setOnClickListener(this);
        view.findViewById(R.id.btnRetry).setOnClickListener(this);
        return view;
    }

    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public void onStart() {
        super.onStart();
        this.mBluetoothUtilities.register();
    }

    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public void onStop() {
        super.onStop();
        this.mBluetoothUtilities.unregister();
    }

    @Override // com.kopin.solos.common.BaseFragment, android.app.Fragment
    public void onResume() {
        super.onResume();
        connectToDevice(this.mBluetoothDevice);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btnBack) {
            getActivity().onBackPressed();
        } else {
            connectToDevice(this.mBluetoothDevice);
        }
    }

    private void updateUIonConnecting() {
        if (isAdded()) {
            getActivity().runOnUiThread(new Runnable() { // from class: com.kopin.solos.setup.HeadsetConnectFragment.1
                @Override // java.lang.Runnable
                public void run() {
                    HeadsetConnectFragment.this.mProgressBar.setVisibility(0);
                    HeadsetConnectFragment.this.mButtonBar.setVisibility(8);
                    HeadsetConnectFragment.this.mLayoutStepMsg.setVisibility(0);
                    HeadsetConnectFragment.this.updateDeviceStatus(R.string.msg_connecting);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateUIonConnected() {
        if (isAdded()) {
            getActivity().runOnUiThread(new Runnable() { // from class: com.kopin.solos.setup.HeadsetConnectFragment.2
                @Override // java.lang.Runnable
                public void run() {
                    HeadsetConnectFragment.this.mConnecting = false;
                    HeadsetConnectFragment.this.mProgressBar.setVisibility(4);
                    HeadsetConnectFragment.this.mButtonBar.setVisibility(8);
                    HeadsetConnectFragment.this.mBtnStep.setEnabled(true);
                    HeadsetConnectFragment.this.updateDeviceStatus(R.string.msg_connected);
                    HeadsetConnectFragment.this.mLayoutStepMsg.setVisibility(4);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateUIonNotConnected() {
        if (isAdded()) {
            getActivity().runOnUiThread(new Runnable() { // from class: com.kopin.solos.setup.HeadsetConnectFragment.3
                @Override // java.lang.Runnable
                public void run() {
                    HeadsetConnectFragment.this.mConnecting = false;
                    HeadsetConnectFragment.this.mProgressBar.setVisibility(4);
                    HeadsetConnectFragment.this.mButtonBar.setVisibility(0);
                    HeadsetConnectFragment.this.updateDeviceStatus(R.string.msg_connection_failed);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDeviceStatus(final int status) {
        if (isAdded()) {
            getActivity().runOnUiThread(new Runnable() { // from class: com.kopin.solos.setup.HeadsetConnectFragment.4
                @Override // java.lang.Runnable
                public void run() {
                    int color;
                    switch (status) {
                        case R.string.msg_connected /* 2131362366 */:
                            color = R.color.solos_orange;
                            break;
                        case R.string.msg_connecting /* 2131362367 */:
                        default:
                            color = R.color.white;
                            break;
                        case R.string.msg_connection_failed /* 2131362368 */:
                            color = android.R.color.holo_red_dark;
                            break;
                    }
                    HeadsetConnectFragment.this.mDeviceStatusText.setTextColor(HeadsetConnectFragment.this.getResources().getColor(color));
                    HeadsetConnectFragment.this.mDeviceStatusText.setText(status);
                }
            });
        }
    }

    private void connectToDevice(BluetoothDevice device) {
        if (!this.mConnecting) {
            BluetoothDevice connectedDevice = PupilDevice.getDevice();
            if (connectedDevice != null && PupilDevice.isConnected() && connectedDevice.getAddress().equals(device.getAddress())) {
                updateUIonConnected();
                return;
            }
            updateUIonConnecting();
            this.mConnecting = true;
            this.mHardwareService.initiateConnectionToVC();
            try {
                BluetoothConnector bluetoothConnector = new BluetoothConnector(device, new DeviceLister.ConnectionListener() { // from class: com.kopin.solos.setup.HeadsetConnectFragment.5
                    @Override // com.kopin.pupil.bluetooth.DeviceLister.ConnectionListener
                    public void onAttemptConnection(BluetoothDevice dev) {
                    }

                    @Override // com.kopin.pupil.bluetooth.DeviceLister.ConnectionListener
                    public void onConnection(PupilConnector connector) {
                        DeviceLister.ConnectionListener vcListener = HeadsetConnectFragment.this.mHardwareService.getVCConnectionListener();
                        vcListener.onConnection(connector);
                        HeadsetConnectFragment.this.updateUIonConnected();
                    }

                    @Override // com.kopin.pupil.bluetooth.DeviceLister.ConnectionListener
                    public void onConnectionFailed() {
                        HeadsetConnectFragment.this.updateUIonNotConnected();
                    }
                });
                bluetoothConnector.start();
            } catch (Exception ioe) {
                Log.e("HeadsetConnect", "IO error starting bluetooth conn", ioe);
            }
        }
    }

    @Override // com.kopin.pupil.bluetooth.BluetoothUtilities.DiscoveryListener
    public void onDeviceDiscovered(BluetoothDevice device) {
    }

    @Override // com.kopin.pupil.bluetooth.BluetoothUtilities.DiscoveryListener
    public void onDeviceStateChanged(BluetoothDevice device, boolean doConnect) {
        if (device != null && device.getAddress().contentEquals(this.mBluetoothDevice.getAddress())) {
            switch (device.getBondState()) {
                case 10:
                    updateDeviceStatus(R.string.msg_not_paired);
                    break;
                case 11:
                    updateDeviceStatus(R.string.msg_pairing);
                    break;
                case 12:
                    updateDeviceStatus(R.string.msg_paired);
                    break;
                default:
                    updateDeviceStatus(R.string.msg_not_paired);
                    break;
            }
        }
    }
}
