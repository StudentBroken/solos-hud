package com.kopin.solos.settings;

import android.os.Bundle;
import android.os.Handler;
import android.preference.ListPreference;
import android.preference.Preference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import com.kopin.pupil.PupilDevice;
import com.kopin.solos.R;
import com.kopin.solos.WearMessageListenerService;
import com.kopin.solos.common.BaseFragment;
import com.kopin.solos.common.SportType;
import com.kopin.solos.common.config.Config;
import com.kopin.solos.config.Features;
import com.kopin.solos.settings.CustomSwitchPreference;
import com.kopin.solos.settings.SettingsFragment;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.wear.MessageType;
import com.kopin.solos.wear.WatchMessageCallback;
import com.kopin.solos.wear.WatchTransferState;
import java.text.DecimalFormat;

/* JADX INFO: loaded from: classes24.dex */
public class QuickSettingsFragment extends BaseFragment {
    private static final long RETRY_TIME = 3500;
    private Button mBtnWatchMode;
    private ProgressBar mProgressBarWatch;
    private TextView mTextWatchModeError;

    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_quick);
    }

    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quick_settings, container, false);
        CustomSwitchPreference autoStartSwitch = (CustomSwitchPreference) findPreference("pref_auto_start_switch");
        autoStartSwitch.setBindViewCallback(new CustomSwitchPreference.BindViewCallback() { // from class: com.kopin.solos.settings.QuickSettingsFragment.1
            @Override // com.kopin.solos.settings.CustomSwitchPreference.BindViewCallback
            public void onBindView(Switch switchView) {
                switchView.setChecked(Prefs.isAuto());
            }
        });
        autoStartSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: com.kopin.solos.settings.QuickSettingsFragment.2
            @Override // android.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Prefs.setAutoStart(((Boolean) newValue).booleanValue());
                return true;
            }
        });
        final DecimalFormat decimalFormat = new DecimalFormat("#.##");
        double value = Conversion.speedForLocale(Prefs.getAutoPauseSpeedMPS(), Prefs.getUnitSystem());
        final ValidatedEditTextPreference autoPauseSpeedPref = (ValidatedEditTextPreference) findPreference(Prefs.AUTO_PAUSE_SPEED_KEY);
        final String unitOfSpeed = Conversion.getUnitOfSpeed(getActivity());
        final String title = getString(R.string.pref_title_pause_speed, new Object[]{unitOfSpeed});
        String summary = (value <= 0.0d || !Prefs.isAutoPauseEnabled()) ? title : String.format("%s %s", decimalFormat.format(value), unitOfSpeed);
        autoPauseSpeedPref.setDialogTitle(getString(R.string.pref_title_pause_speed, new Object[]{unitOfSpeed}));
        autoPauseSpeedPref.setSummary(summary);
        SettingsFragment.configureTextPreferenceKeyboard(autoPauseSpeedPref);
        autoPauseSpeedPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: com.kopin.solos.settings.QuickSettingsFragment.3
            @Override // android.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                double speedLocale = Utility.doubleFromString(newValue.toString(), 0.0d);
                double speed = Conversion.speedFromLocale(speedLocale);
                Prefs.setAutoPauseSpeedMPS(speed);
                preference.setSummary(String.format("%s %s", decimalFormat.format(speedLocale), Conversion.getUnitOfSpeed(QuickSettingsFragment.this.getActivity())));
                return true;
            }
        });
        if (LiveRide.getCurrentSport() == SportType.RUN) {
            getPreferenceScreen().removePreference(findPreference(Prefs.AUTO_PAUSE_SPEED_KEY));
        } else {
            findPreference(Prefs.AUTO_PAUSE_KEY).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: com.kopin.solos.settings.QuickSettingsFragment.4
                @Override // android.preference.Preference.OnPreferenceChangeListener
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if (((Boolean) newValue).booleanValue()) {
                        autoPauseSpeedPref.setSummary(String.format("%s %s", decimalFormat.format(Conversion.speedForLocale(Prefs.getAutoPauseSpeedMPS())), unitOfSpeed));
                    } else {
                        autoPauseSpeedPref.setSummary(title);
                    }
                    return true;
                }
            });
        }
        ListPreference prefVocon = (ListPreference) findPreference(com.kopin.pupil.aria.Prefs.VOCON_KEY);
        if (!Config.VOCON_ENABLED) {
            getPreferenceScreen().removePreference(prefVocon);
            view.findViewById(R.id.dividerWatchPref).setVisibility(8);
        } else {
            prefVocon.setSummary(prefVocon.getEntry());
            prefVocon.setOnPreferenceChangeListener(new SettingsFragment.OnListPreferenceChangeListener(R.array.setting_vocon_entries, R.array.setting_vocon_values));
        }
        if (Features.IS_WEARAPP_ENABLED) {
            this.mProgressBarWatch = (ProgressBar) view.findViewById(R.id.progressBarWatch);
            this.mTextWatchModeError = (TextView) view.findViewById(R.id.textError);
            this.mBtnWatchMode = (Button) view.findViewById(R.id.btnWatchMode);
            checkWatch();
        } else {
            view.findViewById(R.id.layoutWatch).setVisibility(8);
        }
        return view;
    }

    @Override // com.kopin.solos.common.BaseFragment, android.app.Fragment
    public void onResume() {
        super.onResume();
        getView().setVisibility(0);
    }

    @Override // com.kopin.solos.common.BaseFragment, android.app.Fragment
    public void onPause() {
        super.onPause();
        getView().setVisibility(4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateErrorText(final int errorRes) {
        if (isAdded()) {
            getActivity().runOnUiThread(new Runnable() { // from class: com.kopin.solos.settings.QuickSettingsFragment.5
                @Override // java.lang.Runnable
                public void run() {
                    QuickSettingsFragment.this.mProgressBarWatch.setVisibility(8);
                    if (errorRes > 0) {
                        QuickSettingsFragment.this.mTextWatchModeError.setVisibility(0);
                        QuickSettingsFragment.this.mTextWatchModeError.setText(errorRes);
                        QuickSettingsFragment.this.mBtnWatchMode.setEnabled(false);
                    } else {
                        QuickSettingsFragment.this.mTextWatchModeError.setVisibility(8);
                        QuickSettingsFragment.this.mBtnWatchMode.setEnabled(true);
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkWatch() {
        boolean isHeadsetConnected = false;
        this.mBtnWatchMode.setEnabled(false);
        if (PupilDevice.isConnected() && PupilDevice.getDevice() != null && PupilDevice.getDevice().getAddress() != null) {
            isHeadsetConnected = true;
        }
        if (isHeadsetConnected) {
            WearMessageListenerService.setCallback(new WatchMessageCallback() { // from class: com.kopin.solos.settings.QuickSettingsFragment.6
                @Override // com.kopin.solos.wear.WatchMessageCallback
                public void performAction(MessageType messageType) {
                }

                @Override // com.kopin.solos.wear.WatchMessageCallback
                public void onGetWatchState(WatchTransferState watchTransferState) {
                    int msg;
                    switch (AnonymousClass8.$SwitchMap$com$kopin$solos$wear$WatchTransferState[watchTransferState.ordinal()]) {
                        case 1:
                            msg = R.string.watch_not_in_range;
                            break;
                        case 2:
                            msg = R.string.wear_app_not_installed;
                            break;
                        default:
                            msg = 0;
                            break;
                    }
                    QuickSettingsFragment.this.updateErrorText(msg);
                    if (msg > 0) {
                        QuickSettingsFragment.this.doRetry();
                    }
                }
            });
            WearMessageListenerService.getWatchState();
        } else {
            updateErrorText(R.string.label_no_headset);
            doRetry();
        }
    }

    /* JADX INFO: renamed from: com.kopin.solos.settings.QuickSettingsFragment$8, reason: invalid class name */
    static /* synthetic */ class AnonymousClass8 {
        static final /* synthetic */ int[] $SwitchMap$com$kopin$solos$wear$WatchTransferState = new int[WatchTransferState.values().length];

        static {
            try {
                $SwitchMap$com$kopin$solos$wear$WatchTransferState[WatchTransferState.WATCH_NOT_AVAILABLE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$kopin$solos$wear$WatchTransferState[WatchTransferState.WATCH_SOLOS_NOT_INSTALLED.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doRetry() {
        new Handler().postDelayed(new Runnable() { // from class: com.kopin.solos.settings.QuickSettingsFragment.7
            @Override // java.lang.Runnable
            public void run() {
                if (!QuickSettingsFragment.this.isDetached() && QuickSettingsFragment.this.getActivity() != null && !QuickSettingsFragment.this.getActivity().isFinishing()) {
                    QuickSettingsFragment.this.checkWatch();
                }
            }
        }, RETRY_TIME);
    }
}
