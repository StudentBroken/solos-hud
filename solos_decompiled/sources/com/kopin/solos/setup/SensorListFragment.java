package com.kopin.solos.setup;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.InputDeviceCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.kopin.pupil.SolosDevice;
import com.kopin.solos.Fragments.BaseServiceFragment;
import com.kopin.solos.HardwareReceiverService;
import com.kopin.solos.R;
import com.kopin.solos.SetupActivity;
import com.kopin.solos.ThemeUtil;
import com.kopin.solos.common.config.Config;
import com.kopin.solos.common.permission.Permission;
import com.kopin.solos.common.permission.PermissionUtil;
import com.kopin.solos.sensors.Sensor;
import com.kopin.solos.sensors.SensorList;
import com.kopin.solos.sensors.SensorListAdapter;
import com.kopin.solos.sensors.SensorsConnector;
import java.text.MessageFormat;
import java.util.List;

/* JADX INFO: loaded from: classes24.dex */
public class SensorListFragment extends BaseServiceFragment implements HardwareReceiverService.InfoListener, AdapterView.OnItemClickListener {
    private SensorListAdapter mAdapter;
    private ImageView mAntIndicator;
    private Button mButtonText;
    private Handler mDiscoveryHandler;
    private ProgressBar mProgressBar;
    private View mStepMsgView;
    private TextView mTitle;
    private boolean haveShownBt = false;
    private boolean mClear = false;
    final long STEP_MESSAGE_SHOW_TIME = 3000;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.kopin.solos.setup.SensorListFragment.5
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("android.bluetooth.adapter.action.STATE_CHANGED")) {
                intent.getIntExtra("android.bluetooth.adapter.extra.STATE", Integer.MIN_VALUE);
                SensorListFragment.this.updateUIOnBTStateChange();
                SensorListFragment.this.updateAntIndicator();
            }
        }
    };

    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);
        inflater.inflate(R.layout.fragment_lister, (ViewGroup) this.mContainerLayout, true);
        this.mBtnStep.setEnabled(false);
        this.mAdapter = new SensorListAdapter(getActivity());
        ListView listView = (ListView) view.findViewById(android.R.id.list);
        listView.setAdapter((ListAdapter) this.mAdapter);
        TextView textHeader = (TextView) view.findViewById(R.id.txtStepHeader);
        textHeader.setText(R.string.header_pair_sensor);
        this.mProgressBar = (ProgressBar) view.findViewById(R.id.lister_progress);
        this.mTitle = (TextView) view.findViewById(R.id.lister_title);
        this.mButtonText = (Button) view.findViewById(R.id.lister_text_clear_saved);
        this.mButtonText.setVisibility(0);
        this.mAntIndicator = (ImageView) view.findViewById(R.id.antIndicator);
        this.mStepMsgView = view.findViewById(R.id.layoutStepMsg);
        ((TextView) view.findViewById(R.id.layoutStepMsg1).findViewById(R.id.txtStepMsg)).setText(ThemeUtil.getResourceId(getActivity(), R.attr.strPairSensorsMsg2));
        ((ImageView) view.findViewById(R.id.layoutStepMsg1).findViewById(R.id.imgStep)).setImageResource(R.drawable.ic_connect);
        ((TextView) view.findViewById(R.id.layoutStepMsg2).findViewById(R.id.txtStepMsg)).setText(R.string.hint_text_sensor2);
        ((ImageView) view.findViewById(R.id.layoutStepMsg2).findViewById(R.id.imgStep)).setImageResource(R.drawable.ic_circle_sensors);
        view.findViewById(R.id.layoutStepMsg3).setVisibility(0);
        ((TextView) view.findViewById(R.id.layoutStepMsg3).findViewById(R.id.txtStepMsg)).setText(R.string.hint_text_sensor3);
        ((ImageView) view.findViewById(R.id.layoutStepMsg3).findViewById(R.id.imgStep)).setImageResource(R.drawable.ic_info);
        this.mButtonText.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.setup.SensorListFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                    SensorListFragment.this.startDiscovery(true);
                } else {
                    SetupActivity.startBTSettingsActivity(SensorListFragment.this.getActivity());
                }
            }
        });
        this.mDiscoveryHandler = new Handler();
        return view;
    }

    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public void onStart() {
        super.onStart();
        if (this.mHardwareService != null) {
            this.mHardwareService.addCallback(this);
        }
        onListChanged();
        SolosDevice.checkForAntBridge();
        IntentFilter filter = new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED");
        getActivity().registerReceiver(this.mReceiver, filter);
        startDiscovery(false);
        this.mStepMsgView.setVisibility(0);
        showStepMessageForTime();
    }

    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public void onStop() {
        super.onStop();
        if (this.mHardwareService != null) {
            this.mHardwareService.removeCallback(this);
        }
        SensorsConnector.stopDiscovery();
        getActivity().unregisterReceiver(this.mReceiver);
    }

    @Override // com.kopin.solos.Fragments.BaseServiceFragment
    public void onServiceConnected(HardwareReceiverService service) {
        super.onServiceConnected(service);
        if (isStillRequired()) {
            this.mHardwareService.addCallback(this);
            onListChanged();
            startDiscovery(false);
        }
    }

    @Override // com.kopin.solos.Fragments.BaseServiceFragment
    public void onServiceDisconnected() {
        super.onServiceDisconnected();
        this.mHardwareService.removeCallback(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startDiscovery(boolean andClear) {
        boolean btOn = BluetoothAdapter.getDefaultAdapter().isEnabled();
        if (!btOn && !this.haveShownBt) {
            SetupActivity.startBTSettingsActivity(getActivity());
            this.haveShownBt = true;
            return;
        }
        this.mClear = andClear;
        if (this.mServiceBound && !PermissionUtil.askPermissionInfo(getActivity(), R.string.permission_gps_sensors_title, R.string.permission_gps_sensors_message, Permission.ACCESS_FINE_LOCATION)) {
            doDiscovery(andClear);
        }
    }

    private void doDiscovery(boolean andClear) {
        SensorsConnector.stopDiscovery();
        if (andClear) {
            SensorsConnector.reset();
        }
        SensorsConnector.startDiscovery();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateUIOnDeviceDiscovery() {
        String deviceFound = getString(R.string.device_found);
        int deviceCount = this.mAdapter.getCount();
        this.mButtonText.setVisibility(deviceCount > 0 ? 0 : 4);
        String fmtStr = MessageFormat.format(deviceFound, Integer.valueOf(deviceCount));
        this.mTitle.setText(fmtStr);
        showStepMessageForTime();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateUIOnBTStateChange() {
        if (isStillRequired()) {
            getActivity().runOnUiThread(new Runnable() { // from class: com.kopin.solos.setup.SensorListFragment.2
                @Override // java.lang.Runnable
                public void run() {
                    if (SensorListFragment.this.isVisible()) {
                        boolean btOn = BluetoothAdapter.getDefaultAdapter().isEnabled();
                        if (btOn) {
                            SensorListFragment.this.updateUIOnDeviceDiscovery();
                            SensorListFragment.this.mButtonText.setText(R.string.clear_list);
                            SensorListFragment.this.mProgressBar.setVisibility(0);
                        } else {
                            SensorListFragment.this.mAdapter.clear();
                            SensorListFragment.this.mButtonText.setVisibility(0);
                            SensorListFragment.this.mButtonText.setText(R.string.title_activity_settings);
                            SensorListFragment.this.mStepMsgView.setVisibility(0);
                            SensorListFragment.this.mTitle.setText(R.string.bt_enable);
                            SensorListFragment.this.mProgressBar.setVisibility(4);
                        }
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAntIndicator() {
        if (isStillRequired() && Config.DEBUG) {
            getActivity().runOnUiThread(new Runnable() { // from class: com.kopin.solos.setup.SensorListFragment.3
                @Override // java.lang.Runnable
                public void run() {
                    int cableId;
                    boolean visible = false;
                    int col = 0;
                    if (BluetoothAdapter.getDefaultAdapter().isEnabled() && (cableId = SolosDevice.getAntBridgeId()) != 0) {
                        visible = true;
                        col = SensorsConnector.isAntBridgeActive(cableId) ? SensorsConnector.isAntBridgeActive() ? SensorListFragment.this.getResources().getColor(R.color.status_connected) : InputDeviceCompat.SOURCE_ANY : SensorListFragment.this.getResources().getColor(R.color.status_disconnected);
                    }
                    SensorListFragment.this.mAntIndicator.setVisibility(visible ? 0 : 8);
                    SensorListFragment.this.mAntIndicator.setColorFilter(col);
                }
            });
        }
    }

    private void showStepMessageForTime() {
        int deviceCount = this.mAdapter.getCount();
        if (deviceCount > 0) {
            if (this.mStepMsgView.getVisibility() == 0) {
                this.mDiscoveryHandler.postDelayed(new Runnable() { // from class: com.kopin.solos.setup.SensorListFragment.4
                    @Override // java.lang.Runnable
                    public void run() {
                        SensorListFragment.this.mStepMsgView.setVisibility(8);
                    }
                }, 3000L);
                return;
            }
            return;
        }
        this.mStepMsgView.setVisibility(0);
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override // com.kopin.solos.HardwareReceiverService.InfoListener
    public void onListChanged() {
        if (isStillRequired()) {
            setList(SensorList.getList());
            this.mBtnStep.setEnabled(SensorList.hasConnectedDevices());
            updateUIOnBTStateChange();
            updateAntIndicator();
        }
    }

    private void setList(List<Sensor> list) {
        this.mAdapter.setList(list);
        if (!list.isEmpty()) {
            this.mButtonText.setVisibility(0);
        } else {
            this.mButtonText.setVisibility(8);
        }
    }

    @Override // com.kopin.solos.HardwareReceiverService.InfoListener
    public void onNotification(HardwareReceiverService.NotificationType type) {
    }

    @Override // android.app.Fragment
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionUtil.granted(Permission.ACCESS_FINE_LOCATION, permissions, grantResults)) {
            doDiscovery(this.mClear);
        }
    }
}
