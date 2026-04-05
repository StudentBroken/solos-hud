package com.kopin.solos.Fragments;

import com.kopin.solos.HardwareReceiverService;
import com.kopin.solos.common.BaseFragment;

/* JADX INFO: loaded from: classes24.dex */
public class BaseServiceFragment extends BaseFragment {
    protected HardwareReceiverService mHardwareService;
    protected boolean mServiceBound;

    public void onServiceConnected(HardwareReceiverService service) {
        this.mHardwareService = service;
        this.mServiceBound = true;
    }

    public void onServiceDisconnected() {
        this.mServiceBound = false;
    }
}
