package com.kopin.solos.setup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import com.kopin.solos.Fragments.BaseServiceFragment;
import com.kopin.solos.R;
import com.kopin.solos.storage.settings.Prefs;

/* JADX INFO: loaded from: classes24.dex */
public class SetupBeginFragment extends BaseServiceFragment {
    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);
        inflater.inflate(R.layout.layout_setup_start_finish, (ViewGroup) this.mContainerLayout, true);
        this.mBtnStep.setText(R.string.btn_setup_start);
        this.mStepSubHeader.setVisibility(8);
        view.findViewById(R.id.txtStepHeader).setVisibility(8);
        Prefs.initSingleMetrics();
        setHasOptionsMenu(true);
        return view;
    }

    @Override // android.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.menuSkip).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
