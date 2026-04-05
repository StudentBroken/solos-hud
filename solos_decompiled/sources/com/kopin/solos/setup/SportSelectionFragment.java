package com.kopin.solos.setup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import com.kopin.solos.Fragments.BaseServiceFragment;
import com.kopin.solos.R;

/* JADX INFO: loaded from: classes24.dex */
public class SportSelectionFragment extends BaseServiceFragment {
    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);
        inflater.inflate(R.layout.fragment_sport_selection, (ViewGroup) this.mContainerLayout, true);
        this.mStepHeader.setVisibility(8);
        this.mBtnStep.setVisibility(8);
        return view;
    }

    @Override // android.app.Fragment
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menuSkip).setVisible(false);
    }
}
