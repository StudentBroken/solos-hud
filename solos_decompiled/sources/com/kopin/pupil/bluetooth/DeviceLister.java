package com.kopin.pupil.bluetooth;

import android.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.kopin.pupil.PupilConnector;
import com.kopin.pupil.PupilDevice;
import com.kopin.pupil.bluetooth.BluetoothUtilities;

/* JADX INFO: loaded from: classes25.dex */
public class DeviceLister extends Dialog implements BluetoothUtilities.DiscoveryListener, AdapterView.OnItemClickListener {
    private final BluetoothDeviceAdapter mAdapter;
    private volatile boolean mConnecting;
    private final Handler mHandler;
    private final ListView mListView;
    private final ConnectionListener mListener;
    private final BluetoothUtilities mUtilities;

    public interface ConnectionListener {
        void onAttemptConnection(BluetoothDevice bluetoothDevice);

        void onConnection(PupilConnector pupilConnector);

        void onConnectionFailed();
    }

    public DeviceLister(Context context, ConnectionListener listener) {
        this(context, listener, R.layout.list_content);
    }

    public DeviceLister(Context context, ConnectionListener listener, int layoutResId) {
        this(context, listener, layoutResId, 0);
    }

    public DeviceLister(Context context, ConnectionListener listener, int layoutResId, int cancelButtonResId) {
        super(context);
        setCancelable(true);
        requestWindowFeature(1);
        setContentView(layoutResId);
        this.mListView = (ListView) findViewById(R.id.list);
        View btnCancel = cancelButtonResId <= 0 ? null : findViewById(cancelButtonResId);
        if (btnCancel != null) {
            btnCancel.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.pupil.bluetooth.DeviceLister.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    DeviceLister.this.dismiss();
                }
            });
        }
        this.mUtilities = new BluetoothUtilities(context, this);
        ListView listView = this.mListView;
        BluetoothDeviceAdapter bluetoothDeviceAdapter = new BluetoothDeviceAdapter(context);
        this.mAdapter = bluetoothDeviceAdapter;
        listView.setAdapter((ListAdapter) bluetoothDeviceAdapter);
        this.mListView.setOnItemClickListener(this);
        this.mListener = listener;
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    @Override // android.app.Dialog
    protected void onStart() {
        super.onStart();
        this.mUtilities.register();
    }

    @Override // android.app.Dialog
    protected void onStop() {
        super.onStop();
        this.mUtilities.stop();
        this.mUtilities.unregister();
    }

    @Override // android.app.Dialog
    public void show() {
        if (this.mUtilities.isBluetoothEnabled()) {
            super.show();
            this.mAdapter.clear();
            this.mUtilities.start();
            setTitle("Searching");
            return;
        }
        showNoBluetoothDialog(getContext());
    }

    @Override // com.kopin.pupil.bluetooth.BluetoothUtilities.DiscoveryListener
    public void onDeviceDiscovered(BluetoothDevice device) {
        this.mAdapter.add(device);
    }

    @Override // com.kopin.pupil.bluetooth.BluetoothUtilities.DiscoveryListener
    public void onDeviceStateChanged(BluetoothDevice device, boolean doConnect) {
        this.mAdapter.notifyDataSetChanged();
        if (doConnect) {
            connectToDevice(device);
        }
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BluetoothDevice device = this.mAdapter.get(position);
        if (device.getBondState() == 10) {
            BluetoothUtilities.pairDevice(device, true);
        } else {
            connectToDevice(device);
        }
    }

    private void connectToDevice(BluetoothDevice device) {
        if (!this.mConnecting) {
            this.mConnecting = true;
            this.mUtilities.stop();
            this.mListView.setEnabled(false);
            setTitle("Connecting");
            PupilDevice.tryConnect(device, new ConnectionListener() { // from class: com.kopin.pupil.bluetooth.DeviceLister.2
                @Override // com.kopin.pupil.bluetooth.DeviceLister.ConnectionListener
                public void onAttemptConnection(BluetoothDevice device2) {
                    DeviceLister.this.mListener.onAttemptConnection(device2);
                }

                @Override // com.kopin.pupil.bluetooth.DeviceLister.ConnectionListener
                public void onConnection(PupilConnector connector) {
                    DeviceLister.this.mListener.onConnection(connector);
                    DeviceLister.this.mConnecting = false;
                    DeviceLister.this.mHandler.post(new Runnable() { // from class: com.kopin.pupil.bluetooth.DeviceLister.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            DeviceLister.this.mListView.setEnabled(true);
                            DeviceLister.this.setTitle("Connected");
                        }
                    });
                }

                @Override // com.kopin.pupil.bluetooth.DeviceLister.ConnectionListener
                public void onConnectionFailed() {
                    DeviceLister.this.mConnecting = false;
                    DeviceLister.this.mListener.onConnectionFailed();
                    DeviceLister.this.mHandler.post(new Runnable() { // from class: com.kopin.pupil.bluetooth.DeviceLister.2.2
                        @Override // java.lang.Runnable
                        public void run() {
                            DeviceLister.this.mListView.setEnabled(true);
                            DeviceLister.this.setTitle("Connection failed");
                        }
                    });
                }
            }, false);
        }
    }

    private static void showNoBluetoothDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.bt_enable).setTitle(R.string.bt_not_available);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() { // from class: com.kopin.pupil.bluetooth.DeviceLister.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
