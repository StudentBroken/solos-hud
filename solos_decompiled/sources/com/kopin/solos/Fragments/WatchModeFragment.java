package com.kopin.solos.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.android.gms.wearable.Node;
import com.kopin.solos.R;
import com.kopin.solos.common.SafeFragment;
import com.kopin.solos.wear.WearMessenger;
import java.util.List;

/* JADX INFO: loaded from: classes24.dex */
public class WatchModeFragment extends SafeFragment {
    @Override // android.app.Fragment
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_watch_mode, container, false);
        final View warningView = view.findViewById(R.id.viewWarning);
        final Button btnSwitch = (Button) view.findViewById(R.id.btnSwitchToPhoneMode);
        btnSwitch.setEnabled(false);
        final View progressBar = view.findViewById(R.id.progressBar);
        WearMessenger.getConnectedNodes(new WearMessenger.DeviceCallback() { // from class: com.kopin.solos.Fragments.WatchModeFragment.1
            @Override // com.kopin.solos.wear.WearMessenger.DeviceCallback
            public void getConnectedWatches(final List<Node> nodes) {
                WatchModeFragment.this.getActivity().runOnUiThread(new Runnable() { // from class: com.kopin.solos.Fragments.WatchModeFragment.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (WatchModeFragment.this.isAdded()) {
                            boolean isWatchInRange = nodes != null && nodes.size() > 0;
                            if (!isWatchInRange) {
                                warningView.setVisibility(0);
                            }
                            progressBar.setVisibility(8);
                            btnSwitch.setEnabled(true);
                        }
                    }
                });
            }
        });
        return view;
    }
}
