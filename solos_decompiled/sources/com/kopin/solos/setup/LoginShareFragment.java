package com.kopin.solos.setup;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.kopin.solos.Fragments.BaseServiceFragment;
import com.kopin.solos.R;
import com.kopin.solos.common.DialogUtils;
import com.kopin.solos.share.Platforms;
import com.kopin.solos.share.ShareHelper;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.Utility;

/* JADX INFO: loaded from: classes24.dex */
public class LoginShareFragment extends BaseServiceFragment {
    private View btnMapMyRide;
    private View btnStrava;
    private View btnTrainingPeaks;
    private TextView textMapMyRide;
    private TextView textStrava;
    private TextView textTrainingPeaks;

    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);
        inflater.inflate(R.layout.fragment_login_share, (ViewGroup) this.mContainerLayout, true);
        if (isStillRequired()) {
            this.mBtnStep.setText(R.string.btn_setup_continue);
            this.mStepSubHeader.setVisibility(8);
            view.findViewById(R.id.txtStepHeader).setVisibility(8);
            this.btnStrava = view.findViewById(R.id.layoutStrava);
            this.btnTrainingPeaks = view.findViewById(R.id.layoutTrainingPeaks);
            this.btnMapMyRide = view.findViewById(R.id.layoutMapMyRide);
            this.textStrava = (TextView) view.findViewById(R.id.textStrava);
            this.textTrainingPeaks = (TextView) view.findViewById(R.id.textTrainingPeaks);
            this.textMapMyRide = (TextView) view.findViewById(R.id.textMapMyRide);
            this.btnStrava.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.setup.LoginShareFragment.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    LoginShareFragment.this.login(Platforms.Strava);
                }
            });
            this.btnTrainingPeaks.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.setup.LoginShareFragment.2
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    LoginShareFragment.this.login(Platforms.TrainingPeaks);
                }
            });
            this.btnMapMyRide.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.setup.LoginShareFragment.3
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    LoginShareFragment.this.login(Platforms.UnderArmour);
                }
            });
        }
        return view;
    }

    @Override // com.kopin.solos.common.BaseFragment, android.app.Fragment
    public void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        if (isStillRequired()) {
            boolean logStrava = Platforms.Strava.isLoggedIn(getActivity().getApplicationContext());
            boolean logTP = Platforms.TrainingPeaks.isLoggedIn(getActivity().getApplicationContext());
            boolean logMapMyRide = Platforms.UnderArmour.isLoggedIn(getActivity().getApplicationContext());
            this.btnStrava.setBackgroundResource(logStrava ? R.drawable.button_strava : R.drawable.button_strava_connect);
            if (logStrava) {
                this.textStrava.setVisibility(0);
                this.textStrava.setText(R.string.setup_share_strava_logout);
            } else {
                this.textStrava.setVisibility(4);
            }
            this.textTrainingPeaks.setText(logTP ? R.string.setup_share_training_peaks_logout : R.string.setup_share_training_peaks_login);
            this.textMapMyRide.setText(logMapMyRide ? R.string.setup_share_map_my_ride_logout : R.string.setup_share_map_my_ride_login);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loggedIn(Platforms platform) {
        if (isStillRequired()) {
            Prefs.setAutoShare(platform.getAutoSharePrefKey(), true);
            if (platform == Platforms.Strava) {
                this.btnStrava.setBackgroundResource(R.drawable.button_strava);
                this.textStrava.setVisibility(0);
                this.textStrava.setText(R.string.setup_share_strava_logout);
            } else if (platform == Platforms.TrainingPeaks) {
                this.textTrainingPeaks.setText(R.string.setup_share_training_peaks_logout);
            } else if (platform == Platforms.UnderArmour) {
                this.textMapMyRide.setText(R.string.setup_share_map_my_ride_logout);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void login(Platforms platform) {
        if (isStillRequired()) {
            if (Utility.isNetworkAvailable(getActivity())) {
                if (!platform.isLoggedIn(getActivity())) {
                    ShareHelper.login(this, platform);
                    return;
                }
                return;
            }
            DialogUtils.showNoNetworkDialog(getActivity());
        }
    }

    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public void onActivityResult(final int requestCode, final int resultCode, Intent data) {
        if (isStillRequired() && !ShareHelper.onLoginResult(getActivity(), requestCode, resultCode, data, new ShareHelper.AuthListener() { // from class: com.kopin.solos.setup.LoginShareFragment.4
            @Override // com.kopin.solos.share.ShareHelper.AuthListener
            public void onResult(final Platforms which, final boolean success, String message) {
                if (LoginShareFragment.this.isStillRequired()) {
                    LoginShareFragment.this.getActivity().runOnUiThread(new Runnable() { // from class: com.kopin.solos.setup.LoginShareFragment.4.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (!success || !LoginShareFragment.this.isStillRequired()) {
                                if (LoginShareFragment.this.isStillRequired()) {
                                    AlertDialog dialog = DialogUtils.createDialog(LoginShareFragment.this.getActivity(), "", "", LoginShareFragment.this.getString(android.R.string.ok), (Runnable) null);
                                    dialog.setTitle(R.string.share_please_login_title);
                                    dialog.setMessage(LoginShareFragment.this.getString(R.string.share_please_login_message, new Object[]{LoginShareFragment.this.getString(which.getNameId())}));
                                    dialog.show();
                                    DialogUtils.setDialogTitleDivider(dialog);
                                    return;
                                }
                                return;
                            }
                            if (resultCode == -1) {
                                for (Platforms platform : Platforms.values()) {
                                    if (requestCode == platform.getRequestCode()) {
                                        LoginShareFragment.this.loggedIn(platform);
                                        return;
                                    }
                                }
                            }
                        }
                    });
                }
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
