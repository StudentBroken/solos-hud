package com.kopin.solos.setup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.kopin.solos.R;
import com.kopin.solos.ThemeUtil;
import com.kopin.solos.common.BaseFragment;

/* JADX INFO: loaded from: classes24.dex */
public class HeadsetSetupFragment extends BaseFragment {
    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);
        inflater.inflate(R.layout.layout_setup_start_finish, (ViewGroup) this.mContainerLayout, true);
        ((TextView) view.findViewById(R.id.txtStepHeader)).setText(R.string.msg_connect_headset);
        ImageView stepLogo = (ImageView) view.findViewById(R.id.imgStepLogo);
        stepLogo.setImageResource(R.drawable.set_up_solos_logo);
        TextView textStepMsg = (TextView) view.findViewById(R.id.txtStepMsg);
        textStepMsg.setText(ThemeUtil.getResourceId(getActivity(), R.attr.strPairHeadsetMsg));
        return view;
    }
}
