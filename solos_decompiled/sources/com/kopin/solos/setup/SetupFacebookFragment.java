package com.kopin.solos.setup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.kopin.solos.FacebookBaseFragment;
import com.kopin.solos.R;
import com.kopin.solos.SetupActivity;

/* JADX INFO: loaded from: classes24.dex */
public class SetupFacebookFragment extends FacebookBaseFragment {
    TextView txtStepHeader;
    TextView txtWelcome;
    boolean wasLoggedInFacebook = false;

    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);
        inflater.inflate(R.layout.layout_setup_facebook, (ViewGroup) this.mContainerLayout, true);
        this.mBtnStep.setText(R.string.btn_setup_manual);
        this.mStepHeader.setText(R.string.btn_setup_profile);
        this.mStepHeader.setVisibility(0);
        this.mStepSubHeader.setVisibility(8);
        this.txtWelcome = (TextView) view.findViewById(R.id.txtWelcome);
        this.wasLoggedInFacebook = isLoggedInFacebook();
        initFacebookLoginButton(view);
        updateView();
        return view;
    }

    @Override // com.kopin.solos.FacebookBaseFragment
    public void updateView() {
        if (getActivity() != null && !getActivity().isFinishing()) {
            this.txtWelcome.setText(isLoggedInFacebook() ? R.string.facebook_setup_intro_logged_in : R.string.facebook_setup_intro);
            skipFacebookIfLoggedIn();
        }
    }

    private void skipFacebookIfLoggedIn() {
        if (!this.wasLoggedInFacebook && isLoggedInFacebook()) {
            ((SetupActivity) getActivity()).skipFacebook();
        }
    }
}
