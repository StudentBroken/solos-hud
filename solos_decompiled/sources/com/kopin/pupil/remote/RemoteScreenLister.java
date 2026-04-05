package com.kopin.pupil.remote;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.kopin.pupil.bluetooth.R;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes25.dex */
@SuppressLint({"NewApi"})
public class RemoteScreenLister extends Dialog implements AdapterView.OnItemClickListener {
    private static final String REMOTE_SCREEN_SERVICE = "_vc_remote._tcp.";
    private final NsdManager.DiscoveryListener mDiscoveryListener;
    private final Handler mHandler;
    private boolean mIsDiscovering;
    private final ListView mListView;
    private final OnServiceChosenListener mListener;
    private ArrayAdapter<NsdServiceInfo> mNsdAdapter;
    private NsdManager mNsdManager;
    private final NsdManager.ResolveListener mResolveListener;

    public interface OnServiceChosenListener {
        void onServiceChosen(NsdServiceInfo nsdServiceInfo);
    }

    public RemoteScreenLister(Context context, OnServiceChosenListener listener) {
        super(context);
        this.mIsDiscovering = false;
        this.mDiscoveryListener = new NsdManager.DiscoveryListener() { // from class: com.kopin.pupil.remote.RemoteScreenLister.2
            @Override // android.net.nsd.NsdManager.DiscoveryListener
            public void onDiscoveryStarted(String serviceType) {
                RemoteScreenLister.this.mIsDiscovering = true;
            }

            @Override // android.net.nsd.NsdManager.DiscoveryListener
            public void onDiscoveryStopped(String serviceType) {
                RemoteScreenLister.this.mIsDiscovering = false;
            }

            @Override // android.net.nsd.NsdManager.DiscoveryListener
            public void onServiceFound(final NsdServiceInfo serviceInfo) {
                RemoteScreenLister.this.mHandler.post(new Runnable() { // from class: com.kopin.pupil.remote.RemoteScreenLister.2.1
                    @Override // java.lang.Runnable
                    public void run() {
                        RemoteScreenLister.this.mNsdAdapter.add(serviceInfo);
                    }
                });
            }

            @Override // android.net.nsd.NsdManager.DiscoveryListener
            public void onServiceLost(NsdServiceInfo serviceInfo) {
            }

            @Override // android.net.nsd.NsdManager.DiscoveryListener
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
            }

            @Override // android.net.nsd.NsdManager.DiscoveryListener
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
            }
        };
        this.mResolveListener = new NsdManager.ResolveListener() { // from class: com.kopin.pupil.remote.RemoteScreenLister.3
            @Override // android.net.nsd.NsdManager.ResolveListener
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.d(RemoteScreenLister.REMOTE_SCREEN_SERVICE, "Error: " + errorCode);
                Toast.makeText(RemoteScreenLister.this.getContext(), "Couldn't resolve host", 1).show();
                RemoteScreenLister.this.mHandler.post(new Runnable() { // from class: com.kopin.pupil.remote.RemoteScreenLister.3.1
                    @Override // java.lang.Runnable
                    public void run() {
                        RemoteScreenLister.this.mNsdAdapter.clear();
                    }
                });
                if (!RemoteScreenLister.this.mIsDiscovering) {
                    RemoteScreenLister.this.mNsdManager.discoverServices(RemoteScreenLister.REMOTE_SCREEN_SERVICE, 1, RemoteScreenLister.this.mDiscoveryListener);
                }
            }

            @Override // android.net.nsd.NsdManager.ResolveListener
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                if (RemoteScreenLister.this.mListener != null) {
                    RemoteScreenLister.this.mListener.onServiceChosen(serviceInfo);
                }
                RemoteScreenLister.this.dismiss();
            }
        };
        this.mNsdManager = (NsdManager) context.getSystemService("servicediscovery");
        setCancelable(true);
        requestWindowFeature(1);
        setContentView(R.layout.list_layout);
        this.mListView = (ListView) findViewById(android.R.id.list);
        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() { // from class: com.kopin.pupil.remote.RemoteScreenLister.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                RemoteScreenLister.this.dismiss();
            }
        });
        this.mNsdAdapter = new NsdAdapter(context);
        this.mListView.setAdapter((ListAdapter) this.mNsdAdapter);
        this.mListView.setOnItemClickListener(this);
        this.mListener = listener;
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    @Override // android.app.Dialog
    protected void onStart() {
        super.onStart();
        this.mNsdManager.discoverServices(REMOTE_SCREEN_SERVICE, 1, this.mDiscoveryListener);
    }

    @Override // android.app.Dialog
    protected void onStop() {
        super.onStop();
        if (this.mIsDiscovering) {
            this.mNsdManager.stopServiceDiscovery(this.mDiscoveryListener);
        }
    }

    @Override // android.app.Dialog
    public void show() {
        if (isWifiConnected()) {
            super.show();
            this.mNsdAdapter.clear();
            setTitle("Searching");
            return;
        }
        showNoWifiDialog(getContext());
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NsdServiceInfo device = this.mNsdAdapter.getItem(position);
        if (this.mIsDiscovering) {
            this.mNsdManager.stopServiceDiscovery(this.mDiscoveryListener);
        }
        this.mNsdManager.resolveService(device, this.mResolveListener);
    }

    private void showNoWifiDialog(Context ctx) {
    }

    private boolean isWifiConnected() {
        return true;
    }

    private class NsdAdapter extends ArrayAdapter<NsdServiceInfo> {
        public NsdAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_1, android.R.id.text1, new ArrayList());
        }

        @Override // android.widget.ArrayAdapter
        public void add(NsdServiceInfo object) {
            for (int i = 0; i < getCount(); i++) {
                if (getItem(i).getServiceName().contentEquals(object.getServiceName())) {
                    return;
                }
            }
            super.add(object);
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            NsdServiceInfo device = getItem(position);
            ((TextView) view.findViewById(android.R.id.text1)).setText(device.getServiceName());
            return view;
        }
    }
}
