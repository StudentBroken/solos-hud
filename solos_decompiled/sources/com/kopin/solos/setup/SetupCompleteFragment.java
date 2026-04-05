package com.kopin.solos.setup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.kopin.solos.Fragments.BaseServiceFragment;
import com.kopin.solos.R;

/* JADX INFO: loaded from: classes24.dex */
public class SetupCompleteFragment extends BaseServiceFragment {
    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);
        inflater.inflate(R.layout.layout_setup_start_finish, (ViewGroup) this.mContainerLayout, true);
        if (!isFinishing()) {
            ((TextView) view.findViewById(R.id.txtStepMsg)).setText(R.string.msg_setup_complete);
            view.findViewById(R.id.txtWelcome).setVisibility(4);
            this.mStepSubHeader.setVisibility(8);
            this.mStepHeader.setVisibility(8);
            this.mBtnStep.setText(R.string.caption_finish);
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
