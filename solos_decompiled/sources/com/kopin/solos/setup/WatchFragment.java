package com.kopin.solos.setup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import com.kopin.solos.Fragments.BaseServiceFragment;
import com.kopin.solos.R;

/* JADX INFO: loaded from: classes24.dex */
public class WatchFragment extends BaseServiceFragment {
    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);
        inflater.inflate(R.layout.fragment_watch, (ViewGroup) this.mContainerLayout, true);
        if (!isFinishing()) {
            this.mStepSubHeader.setVisibility(8);
            this.mStepHeader.setVisibility(8);
            setHasOptionsMenu(true);
        }
        return view;
    }

    @Override // android.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.menuSkip).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
