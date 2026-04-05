package com.kopin.solos.setup;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
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
import com.kopin.pupil.bluetooth.BluetoothDeviceAdapter;
import com.kopin.pupil.bluetooth.BluetoothUtilities;
import com.kopin.solos.Fragments.BaseServiceFragment;
import com.kopin.solos.R;
import com.kopin.solos.SetupActivity;
import com.kopin.solos.common.permission.Permission;
import com.kopin.solos.common.permission.PermissionUtil;
import java.text.MessageFormat;

/* JADX INFO: loaded from: classes24.dex */
public class HeadsetListFragment extends BaseServiceFragment implements BluetoothUtilities.DiscoveryListener, AdapterView.OnItemClickListener {
    BluetoothDeviceAdapter mAdapter;
    BluetoothUtilities mBluetoothUtilities;
    private Button mButtonText;
    Handler mDiscoveryHandler;
    private TextView mListTitle;
    ListView mListView;
    ProgressBar mProgressBar;
    View mStepMsgView;
    final long STEP_MESSAGE_SHOW_TIME = 3000;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.kopin.solos.setup.HeadsetListFragment.6
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("android.bluetooth.adapter.action.STATE_CHANGED")) {
                intent.getIntExtra("android.bluetooth.adapter.extra.STATE", Integer.MIN_VALUE);
                HeadsetListFragment.this.updateUIOnBTStateChange();
            }
        }
    };

    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);
        inflater.inflate(R.layout.fragment_lister, (ViewGroup) this.mContainerLayout, true);
        this.mBtnStep.setEnabled(false);
        this.mStepHeader.setText(R.string.label_connect_headset);
        this.mStepSubHeader.setText("");
        this.mListView = (ListView) view.findViewById(android.R.id.list);
        this.mListTitle = (TextView) view.findViewById(R.id.lister_title);
        this.mProgressBar = (ProgressBar) view.findViewById(R.id.lister_progress);
        this.mButtonText = (Button) view.findViewById(R.id.lister_text_clear_saved);
        this.mButtonText.setText(R.string.title_activity_settings);
        this.mStepMsgView = view.findViewById(R.id.layoutStepMsg);
        ((TextView) view.findViewById(R.id.layoutStepMsg1).findViewById(R.id.txtStepMsg)).setText(R.string.hint_text_headset_1);
        ((ImageView) view.findViewById(R.id.layoutStepMsg1).findViewById(R.id.imgStep)).setImageResource(R.drawable.ic_headset);
        ((TextView) view.findViewById(R.id.layoutStepMsg2).findViewById(R.id.txtStepMsg)).setText(R.string.hint_text_headset_2);
        ((ImageView) view.findViewById(R.id.layoutStepMsg2).findViewById(R.id.imgStep)).setImageResource(R.drawable.ic_info);
        this.mButtonText.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.setup.HeadsetListFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                boolean btOn = BluetoothAdapter.getDefaultAdapter().isEnabled();
                if (!btOn) {
                    SetupActivity.startBTSettingsActivity(HeadsetListFragment.this.getActivity());
                }
            }
        });
        this.mDiscoveryHandler = new Handler();
        return view;
    }

    @Override // android.preference.PreferenceFragment, android.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mAdapter = new BluetoothDeviceAdapter(getActivity());
        this.mBluetoothUtilities = new BluetoothUtilities(getActivity(), this);
        this.mListView.setAdapter((ListAdapter) this.mAdapter);
        this.mListView.setOnItemClickListener(this);
    }

    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED");
        getActivity().registerReceiver(this.mReceiver, filter);
        this.mBluetoothUtilities.register();
        updateUIOnBTStateChange();
        startDiscovery();
    }

    @Override // com.kopin.solos.common.BaseFragment, android.app.Fragment
    public void onResume() {
        super.onResume();
        this.mStepMsgView.setVisibility(0);
        showStepMessageForTime();
    }

    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public void onStop() {
        super.onStop();
        stopDiscovery();
        this.mBluetoothUtilities.unregister();
        getActivity().unregisterReceiver(this.mReceiver);
    }

    @Override // com.kopin.pupil.bluetooth.BluetoothUtilities.DiscoveryListener
    public void onDeviceDiscovered(BluetoothDevice device) {
        this.mAdapter.add(device);
        updateUIOnBTStateChange();
    }

    @Override // com.kopin.pupil.bluetooth.BluetoothUtilities.DiscoveryListener
    public void onDeviceStateChanged(BluetoothDevice device, boolean doConnect) {
        this.mAdapter.notifyDataSetChanged();
    }

    private void startDiscovery() {
        this.mAdapter.clear();
        this.mAdapter.notifyDataSetChanged();
        doDiscovery();
        PermissionUtil.askPermissionInfo(getActivity(), R.string.permission_gps_sensors_title, R.string.permission_gps_sensors_message, Permission.ACCESS_FINE_LOCATION);
    }

    private void doDiscovery() {
        this.mBluetoothUtilities.start();
        updateUIonStartDiscovery();
    }

    private void stopDiscovery() {
        this.mBluetoothUtilities.stop();
        this.mDiscoveryHandler.removeCallbacksAndMessages(true);
        updateUIonStopDiscovery();
    }

    private void updateUIonStartDiscovery() {
        if (isAdded()) {
            getActivity().runOnUiThread(new Runnable() { // from class: com.kopin.solos.setup.HeadsetListFragment.2
                @Override // java.lang.Runnable
                public void run() {
                    HeadsetListFragment.this.updateUIOnBTStateChange();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateUIOnDeviceDiscovery() {
        String deviceFound = getString(R.string.device_found);
        int deviceCount = this.mAdapter.getCount();
        String fmtStr = MessageFormat.format(deviceFound, Integer.valueOf(deviceCount));
        this.mListTitle.setText(fmtStr);
        showStepMessageForTime();
    }

    private void updateUIonStopDiscovery() {
        if (isAdded()) {
            getActivity().runOnUiThread(new Runnable() { // from class: com.kopin.solos.setup.HeadsetListFragment.3
                @Override // java.lang.Runnable
                public void run() {
                    HeadsetListFragment.this.mProgressBar.setVisibility(4);
                    HeadsetListFragment.this.updateUIOnDeviceDiscovery();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateUIOnBTStateChange() {
        getActivity().runOnUiThread(new Runnable() { // from class: com.kopin.solos.setup.HeadsetListFragment.4
            @Override // java.lang.Runnable
            public void run() {
                boolean btOn = BluetoothAdapter.getDefaultAdapter().isEnabled();
                if (btOn) {
                    HeadsetListFragment.this.updateUIOnDeviceDiscovery();
                    HeadsetListFragment.this.mButtonText.setVisibility(4);
                    HeadsetListFragment.this.mProgressBar.setVisibility(0);
                } else {
                    HeadsetListFragment.this.mAdapter.clear();
                    HeadsetListFragment.this.mAdapter.notifyDataSetChanged();
                    HeadsetListFragment.this.mStepMsgView.setVisibility(0);
                    HeadsetListFragment.this.mListTitle.setText(R.string.bt_enable);
                    HeadsetListFragment.this.mButtonText.setVisibility(0);
                    HeadsetListFragment.this.mProgressBar.setVisibility(4);
                }
            }
        });
    }

    private void showStepMessageForTime() {
        int deviceCount = this.mAdapter.getCount();
        if (deviceCount > 0) {
            if (this.mStepMsgView.getVisibility() == 0) {
                this.mDiscoveryHandler.postDelayed(new Runnable() { // from class: com.kopin.solos.setup.HeadsetListFragment.5
                    @Override // java.lang.Runnable
                    public void run() {
                        HeadsetListFragment.this.mStepMsgView.setVisibility(8);
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
        goToHeadsetConnectionPage(this.mAdapter.get(position));
    }

    private void goToHeadsetConnectionPage(BluetoothDevice device) {
        Button btnStep = (Button) getActivity().findViewById(R.id.btnStepSetup);
        stopDiscovery();
        Bundle bundle = new Bundle();
        bundle.putParcelable(SetupActivity.BT_DEVICE_KEY, device);
        bundle.putInt(SetupActivity.FRAGMENT_NAME_KEY, 4);
        btnStep.setTag(bundle);
        btnStep.performClick();
    }

    @Override // android.app.Fragment
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionUtil.granted(Permission.ACCESS_FINE_LOCATION, permissions, grantResults)) {
            doDiscovery();
        }
    }
}
