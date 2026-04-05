package com.kopin.solos.settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.preference.TwoStatePreference;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.kopin.accessory.packets.ActionType;
import com.kopin.pupil.ConnectionManager;
import com.kopin.pupil.PupilDevice;
import com.kopin.pupil.SolosDevice;
import com.kopin.pupil.VCApplication;
import com.kopin.pupil.remote.RemoteScreenLister;
import com.kopin.pupil.update.util.FirmwareFlash;
import com.kopin.pupil.util.VolumeHelper;
import com.kopin.solos.AppService;
import com.kopin.solos.HardwareReceiverService;
import com.kopin.solos.R;
import com.kopin.solos.SetupActivity;
import com.kopin.solos.cabledfu.CableFlash;
import com.kopin.solos.common.BaseFragment;
import com.kopin.solos.common.DialogUtils;
import com.kopin.solos.common.SportType;
import com.kopin.solos.common.config.MetricDataType;
import com.kopin.solos.common.permission.Permission;
import com.kopin.solos.common.permission.PermissionUtil;
import com.kopin.solos.config.Features;
import com.kopin.solos.metrics.TemplateManager;
import com.kopin.solos.sensors.Sensor;
import com.kopin.solos.sensors.SensorsConnector;
import com.kopin.solos.settings.SeekBarDialog;
import com.kopin.solos.settings.ValidatedEditTextPreference;
import com.kopin.solos.share.Config;
import com.kopin.solos.share.Platforms;
import com.kopin.solos.share.ShareHelper;
import com.kopin.solos.share.ShareSettings;
import com.kopin.solos.share.peloton.TermsActivity;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.settings.ConfigMetrics;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.settings.TargetPreference;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.SplitHelper;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.update.CableFlashActivity;
import com.kopin.solos.update.FlashActivity;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* JADX INFO: loaded from: classes24.dex */
public class SettingsFragment extends BaseFragment {
    public static final String HEADSET_CABLE_UPDATE = "headset_cable_update";
    public static final String HEADSET_FORGET = "headset_forget";
    public static final String HEADSET_INFO = "headset_info";
    public static final String HEADSET_MIRROR = "headset_mirror";
    public static final String HEADSET_ROLLING_AVG = "pref_metrics_display_settings";
    public static final String HEADSET_SCREEN = "headset_preference_screen";
    public static final String HEADSET_UPDATE = "headset_update";
    public static final String MANAGE_REMOTE = "manage_remote";
    public static final String PARENT_SCREEN = "parent_screen";
    public static final String PREF_ABOUT = "pref_screen_about_vctrack";
    public static final String PREF_DEVICES_INFO = "devices_info";
    public static final String PREF_MANAGE_SENSORS = "manage_sensors";
    public static final String PREF_PRIVACY_POLICY = "pref_privacy_policy";
    public static final String PREF_RIDE = "ride_settings";
    public static final String PREF_TERMS = "pref_screen_terms";
    public static final String PREF_VIRTUAL_COACH = "pref_virtual_coach";
    public static final String PREF_VOICE_FEEDBACK = "pref_voice_feedback";
    public static final String RESULT_MESSAGE = "login_result_msg";
    public static final String RESULT_TITLE = "login_result_t";
    private static final String ROLLING_AVG_SETTINGS = "pref_metrics_display_settings";
    public static final String SHARE_SCREEN = "share_settings";
    private static final String TAG = "SettingsFragment";
    public static final String USER_INFO = "user_info";
    private static final String mValueUnitFmtStr = "%s %s";
    private SwitchPreference autoPause;
    private TimePickerPreference countdownPref;
    private PreferenceScreen mActiveMetrics;
    private PreferenceScreen mActiveMetricsMultiple;
    private AppService mAppService;
    private ValidatedEditTextPreference mAutoPauseSpeedPref;
    private SwitchPreference mAutoShareFacebook;
    private SwitchPreference mAutoShareStrava;
    private SwitchPreference mAutoShareTP;
    private SwitchPreference mAutoShareUA;
    private PreferenceScreen mBikeInforPref;
    private Preference mCableUpdatePref;
    private SwitchPreference mGPSToggle;
    private HeadsetPreference mHeadsetPref;
    private Preference mHeadsetUpdatePref;
    private Preference mLoginFacebookPref;
    private Preference mLoginStravaPref;
    private Preference mLoginTPPref;
    private Preference mLoginUAPref;
    DualRadioPreference mMetricSwitchPref;
    private ListPreference mModePref;
    private SwitchPreference mNavigateDisplayCompass;
    private SwitchPreference mNavigateFullScreen;
    private PreferenceScreen mProfile;
    private PreferenceScreen mRideSettingsScreen;
    private HardwareReceiverService mService;
    private ValidatedEditTextPreference mSplitDistance;
    private TimePickerPreference mSplitTime;
    private ValidatedEditTextPreference mTargetAverageCadencePref;
    private ValidatedEditTextPreference mTargetAverageHeartratePref;
    private ValidatedEditTextPreference mTargetAverageKickPref;
    private TimePickerPreference mTargetAveragePacePref;
    private ValidatedEditTextPreference mTargetAveragePowerPref;
    private ValidatedEditTextPreference mTargetAverageSpeedPref;
    private ValidatedEditTextPreference mTargetAverageStepPref;
    private ValidatedEditTextPreference mTargetDistancePref;
    private ValidatedEditTextPreference mWeightPref;
    private DualCustomListPreference multiMetrics1;
    private DualCustomListPreference multiMetrics2;
    private DualCustomListPreference multiMetrics3;
    private CustomSwitchPreference multiScreen1;
    private CustomSwitchPreference multiScreen2;
    private CustomSwitchPreference multiScreen3;
    ListPreference splitMode;
    private ListPreference unitPref;
    private boolean mAppBound = false;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private long mThreadId = Looper.getMainLooper().getThread().getId();
    private DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private DecimalFormat decimalFormatSingle = new DecimalFormat("#.#");
    private DecimalFormat decimalFormatNoPlaces = new DecimalFormat("#");
    Preference.OnPreferenceChangeListener mSingleMetricsListener = new Preference.OnPreferenceChangeListener() { // from class: com.kopin.solos.settings.SettingsFragment.13
        @Override // android.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            SettingsFragment.this.readSingleMetrics();
            return true;
        }
    };
    private final ShareSettings.EnabledListener mSetSharingElementsEnabled = new ShareSettings.EnabledListener() { // from class: com.kopin.solos.settings.SettingsFragment.16
        @Override // com.kopin.solos.share.ShareSettings.EnabledListener
        public void onEnabled(Preference loginPref, CustomSwitchPreference sharePref, boolean enabled) {
            SettingsFragment.this.setSharingElementsEnabled(loginPref, sharePref, enabled);
        }
    };
    private final Preference.OnPreferenceClickListener mForgetHeadset = new Preference.OnPreferenceClickListener() { // from class: com.kopin.solos.settings.SettingsFragment.21
        @Override // android.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(final Preference preference) {
            AlertDialog dialog = new AlertDialog.Builder(SettingsFragment.this.getActivity()).setTitle(R.string.label_forget_headset).setMessage(R.string.dialog_forget_headset_message).setNegativeButton(R.string.dialog_button_no, new DialogInterface.OnClickListener() { // from class: com.kopin.solos.settings.SettingsFragment.21.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog2, int which) {
                    dialog2.dismiss();
                }
            }).setPositiveButton(R.string.dialog_button_yes, new DialogInterface.OnClickListener() { // from class: com.kopin.solos.settings.SettingsFragment.21.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog2, int which) {
                    preference.getEditor().remove(ConnectionManager.HEADSET_PAIRED).commit();
                    SensorsConnector.connectAntBridge(0);
                    SolosDevice.forgetHeadset();
                    Features.updateForProduct(SolosDevice.Product.NONE, false, false);
                    SettingsFragment.this.refreshForgetButton();
                    SettingsFragment.this.mHeadsetPref.refresh();
                    dialog2.dismiss();
                }
            }).show();
            DialogUtils.setDialogTitleDivider(dialog);
            return false;
        }
    };
    private final Preference.OnPreferenceClickListener mConnectHeadset = new Preference.OnPreferenceClickListener() { // from class: com.kopin.solos.settings.SettingsFragment.22
        @Override // android.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            Intent intent = new Intent(SettingsFragment.this.getActivity(), (Class<?>) SetupActivity.class);
            intent.putExtra(SetupActivity.SETUP_INTENT_EXTRA_KEY, 4);
            SettingsFragment.this.startActivity(intent);
            return true;
        }
    };
    private final SeekBarDialog.OnValueChangedListener mUpdateHeadsetBrightness = new SeekBarDialog.OnValueChangedListener() { // from class: com.kopin.solos.settings.SettingsFragment.24
        @Override // com.kopin.solos.settings.SeekBarDialog.OnValueChangedListener
        public void onValueChanged(int val) {
            if (SettingsFragment.this.mAppBound) {
                if (SettingsFragment.this.mAppBound && SettingsFragment.this.mAppService.isConnected()) {
                    PupilDevice.wakeUp();
                    SettingsFragment.this.checkDeviceStatus(false);
                    PupilDevice.DeviceStatus deviceStatus = new PupilDevice.DeviceStatus();
                    deviceStatus.setBrightness((byte) val);
                    PupilDevice.currentDeviceStatus().setBrightness((byte) val);
                    PupilDevice.sendDeviceStatus(deviceStatus);
                    Prefs.setHeadsetBrightness(val);
                    return;
                }
                Toast.makeText(SettingsFragment.this.getActivity(), R.string.err_no_headset, 0).show();
            }
        }

        @Override // com.kopin.solos.settings.SeekBarDialog.OnValueChangedListener
        public void onChecked(boolean check, SeekBarDialog dialog) {
            Prefs.setHeadsetAutoBrightness(check);
        }
    };
    private final SeekBarDialog.OnValueChangedListener mUpdateHeadsetVolume = new SeekBarDialog.OnValueChangedListener() { // from class: com.kopin.solos.settings.SettingsFragment.26
        private long lastChirp = 0;

        @Override // com.kopin.solos.settings.SeekBarDialog.OnValueChangedListener
        public void onValueChanged(int val) {
            if (SettingsFragment.this.mAppBound) {
                SettingsFragment.this.checkDeviceStatus(false);
                PupilDevice.DeviceStatus deviceStatus = new PupilDevice.DeviceStatus();
                byte actual = VolumeHelper.friendlyToHeadset((byte) val);
                deviceStatus.setVolume(actual);
                PupilDevice.sendDeviceStatus(deviceStatus);
                PupilDevice.currentDeviceStatus().setVolume(actual);
                Prefs.setHeadsetVolume(val);
                if (Utility.getTimeMilliseconds() > this.lastChirp + 500) {
                    PupilDevice.sendAction(ActionType.PLAY_CHIRP);
                    this.lastChirp = Utility.getTimeMilliseconds();
                }
            }
        }

        @Override // com.kopin.solos.settings.SeekBarDialog.OnValueChangedListener
        public void onChecked(boolean isChecked, SeekBarDialog dialog) {
        }
    };
    private Preference.OnPreferenceChangeListener mTTSSwitched = new Preference.OnPreferenceChangeListener() { // from class: com.kopin.solos.settings.SettingsFragment.32
        @Override // android.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (SettingsFragment.this.mAppBound) {
                SettingsFragment.this.mAppService.checkTTSSettings();
                return true;
            }
            return true;
        }
    };
    private Preference.OnPreferenceChangeListener mTTSTimerUpdated = new Preference.OnPreferenceChangeListener() { // from class: com.kopin.solos.settings.SettingsFragment.33
        @Override // android.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String[] entries = SettingsFragment.this.getResources().getStringArray(R.array.setting_tts_entries);
            String[] values = SettingsFragment.this.getResources().getStringArray(R.array.setting_tts_values);
            int pos = Utility.search(values, newValue.toString());
            if (pos >= 0 && pos < entries.length) {
                preference.setSummary(entries[pos]);
            }
            if (SettingsFragment.this.mAppBound) {
                SettingsFragment.this.mAppService.checkTTSSettings();
                return true;
            }
            return true;
        }
    };
    private Preference.OnPreferenceChangeListener mScreensUpdated = new Preference.OnPreferenceChangeListener() { // from class: com.kopin.solos.settings.SettingsFragment.34
        @Override // android.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (!(newValue instanceof Set)) {
                return false;
            }
            if (preference instanceof CustomMultiSelectPreference) {
                preference.setSummary(((CustomMultiSelectPreference) preference).createSummary((Set) newValue));
            } else {
                Log.w(SettingsFragment.TAG, "(mScreensUpdated) OnPreferenceChangeListener was not assigned to the right Preference", new Exception("stack trace"));
            }
            if (SettingsFragment.this.mAppBound) {
                SettingsFragment.this.mAppService.refreshPagesAvailability();
            }
            return true;
        }
    };
    Preference.OnPreferenceChangeListener mPageSettingUpdated = new Preference.OnPreferenceChangeListener() { // from class: com.kopin.solos.settings.SettingsFragment.35
        @Override // android.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if ((preference instanceof CustomSwitchPreference) && !((CustomSwitchPreference) preference).allowToggle()) {
                return false;
            }
            if ((newValue instanceof Boolean) && !((Boolean) newValue).booleanValue()) {
                int enabled = SettingsFragment.this.multiScreen1.isChecked() ? 0 + 1 : 0;
                if (SettingsFragment.this.multiScreen2.isChecked()) {
                    enabled++;
                }
                if (SettingsFragment.this.multiScreen3.isChecked()) {
                    enabled++;
                }
                if (enabled < 2) {
                    return false;
                }
            }
            if (SettingsFragment.this.mAppBound) {
                SettingsFragment.this.mAppService.refreshDoublePages();
            }
            if (SettingsFragment.this.mMetricSwitchPref != null && !SettingsFragment.this.mMetricSwitchPref.isChecked()) {
                Context context = SettingsFragment.this.getActivity();
                SettingsFragment.this.mTargetAverageCadencePref.refresh(SettingsFragment.this.getString(R.string.format_cadence), SettingsFragment.this.decimalFormatNoPlaces);
                SettingsFragment.this.mTargetAverageStepPref.refresh(SettingsFragment.this.getString(R.string.format_cadence_run), SettingsFragment.this.decimalFormatNoPlaces);
                SettingsFragment.this.mTargetAverageHeartratePref.refresh(SettingsFragment.this.getString(R.string.format_heartrate), SettingsFragment.this.decimalFormatNoPlaces);
                SettingsFragment.this.mTargetAveragePowerPref.refresh(SettingsFragment.this.getString(R.string.format_power), SettingsFragment.this.decimalFormat);
                SettingsFragment.this.mTargetAverageSpeedPref.refresh(SettingsFragment.mValueUnitFmtStr, SettingsFragment.this.decimalFormatNoPlaces, Conversion.getUnitOfSpeed(context));
                SettingsFragment.this.mTargetAveragePacePref.refresh();
                SettingsFragment.this.mTargetDistancePref.refresh(SettingsFragment.mValueUnitFmtStr, SettingsFragment.this.decimalFormat, Conversion.getUnitOfDistance(context));
                SettingsFragment.this.countdownPref.refresh();
                SettingsFragment.this.mAutoPauseSpeedPref.refresh(SettingsFragment.mValueUnitFmtStr, SettingsFragment.this.decimalFormatSingle, Conversion.getUnitOfDistance(context));
                SettingsFragment.this.multiMetrics1.refresh();
                SettingsFragment.this.multiMetrics2.refresh();
                SettingsFragment.this.multiMetrics3.refresh();
            }
            return true;
        }
    };
    private OnTextPreferenceChangeListener mTargetDistanceChanged = new OnTextPreferenceChangeListener(this, 0, new Action() { // from class: com.kopin.solos.settings.SettingsFragment.36
        @Override // com.kopin.solos.settings.SettingsFragment.Action
        public void onAction(Preference preference, Object object) {
            String unit = Conversion.getUnitOfDistance(SettingsFragment.this.getActivity());
            double value = Utility.doubleFromString(object.toString(), 0.0d);
            preference.setSummary(String.format(SettingsFragment.mValueUnitFmtStr, SettingsFragment.this.decimalFormat.format(value), unit));
            SettingsFragment.this.mPageSettingUpdated.onPreferenceChange(preference, object);
        }
    });
    private OnTextPreferenceChangeListener mTargetAverageSpeedPrefChanged = new OnTextPreferenceChangeListener(this, R.string.format_speed, new Action() { // from class: com.kopin.solos.settings.SettingsFragment.37
        @Override // com.kopin.solos.settings.SettingsFragment.Action
        public void onAction(Preference preference, Object object) {
            String unit = Conversion.getUnitOfSpeed(SettingsFragment.this.getActivity());
            double value = Utility.doubleFromString(object.toString(), 0.0d);
            preference.setSummary(String.format(SettingsFragment.mValueUnitFmtStr, SettingsFragment.this.decimalFormat.format(value), unit));
            SettingsFragment.this.mPageSettingUpdated.onPreferenceChange(preference, object);
            Prefs.setTargetAverageSpeedValue(Conversion.speedFromLocale(value));
        }
    });
    private OnTextPreferenceChangeListener mTargetAveragePacePrefChanged = new OnTextPreferenceChangeListener(this, R.string.format_speed, new Action() { // from class: com.kopin.solos.settings.SettingsFragment.38
        @Override // com.kopin.solos.settings.SettingsFragment.Action
        public void onAction(Preference preference, Object object) {
            ((TimePickerPreference) preference).refresh();
            SettingsFragment.this.mPageSettingUpdated.onPreferenceChange(preference, object);
            int value = Utility.intFromString(object.toString(), 0);
            Prefs.setTargetAveragePaceValue(Conversion.millisPerKmToSecondsPerMetre(value));
        }
    });
    private OnTextPreferenceChangeListener mTargetAverageHeartratePrefChanged = new OnTextPreferenceChangeListener(this, R.string.format_heartrate, new Action() { // from class: com.kopin.solos.settings.SettingsFragment.39
        @Override // com.kopin.solos.settings.SettingsFragment.Action
        public void onAction(Preference preference, Object object) {
            int value = Utility.intFromString(object.toString(), 0);
            preference.setSummary(SettingsFragment.this.getString(R.string.format_heartrate, new Object[]{SettingsFragment.this.decimalFormat.format(value)}));
            SettingsFragment.this.mPageSettingUpdated.onPreferenceChange(preference, object);
        }
    });
    private OnTextPreferenceChangeListener mTargetAveragePowerPrefChanged = new OnTextPreferenceChangeListener(this, R.string.format_power, new Action() { // from class: com.kopin.solos.settings.SettingsFragment.40
        @Override // com.kopin.solos.settings.SettingsFragment.Action
        public void onAction(Preference preference, Object object) {
            int value = Utility.intFromString(object.toString(), 0);
            preference.setSummary(SettingsFragment.this.getString(R.string.format_power, new Object[]{SettingsFragment.this.decimalFormat.format(value)}));
            SettingsFragment.this.mPageSettingUpdated.onPreferenceChange(preference, object);
        }
    });
    private OnTextPreferenceChangeListener mTargetAverageKickPrefChanged = new OnTextPreferenceChangeListener(this, R.string.format_power, new Action() { // from class: com.kopin.solos.settings.SettingsFragment.41
        @Override // com.kopin.solos.settings.SettingsFragment.Action
        public void onAction(Preference preference, Object object) {
            int value = Utility.intFromString(object.toString(), 0);
            preference.setSummary(SettingsFragment.this.getString(R.string.format_power, new Object[]{SettingsFragment.this.decimalFormat.format(value)}));
            SettingsFragment.this.mPageSettingUpdated.onPreferenceChange(preference, object);
        }
    });
    private OnTextPreferenceChangeListener mTargetAverageCadencePrefChanged = new OnTextPreferenceChangeListener(this, R.string.format_cadence, new Action() { // from class: com.kopin.solos.settings.SettingsFragment.42
        @Override // com.kopin.solos.settings.SettingsFragment.Action
        public void onAction(Preference preference, Object object) {
            int value = Utility.intFromString(object.toString(), 0);
            preference.setSummary(SettingsFragment.this.getString(R.string.format_cadence, new Object[]{SettingsFragment.this.decimalFormat.format(value)}));
            SettingsFragment.this.mPageSettingUpdated.onPreferenceChange(preference, object);
        }
    });
    private OnTextPreferenceChangeListener mTargetAverageStepPrefChanged = new OnTextPreferenceChangeListener(this, R.string.format_cadence_run, new Action() { // from class: com.kopin.solos.settings.SettingsFragment.43
        @Override // com.kopin.solos.settings.SettingsFragment.Action
        public void onAction(Preference preference, Object object) {
            int value = Utility.intFromString(object.toString(), 0);
            preference.setSummary(SettingsFragment.this.getString(R.string.format_cadence_run, new Object[]{SettingsFragment.this.decimalFormat.format(value)}));
            SettingsFragment.this.mPageSettingUpdated.onPreferenceChange(preference, object);
        }
    });
    private OnTextPreferenceChangeListener mNavigateReminderPrefChanged = new OnTextPreferenceChangeListener(this, R.string.format_time_in_seconds, new Action() { // from class: com.kopin.solos.settings.SettingsFragment.44
        @Override // com.kopin.solos.settings.SettingsFragment.Action
        public void onAction(Preference preference, Object object) {
            int value = Utility.intFromString(object.toString(), 0);
            preference.setSummary(SettingsFragment.this.getString(R.string.format_time_in_seconds, new Object[]{SettingsFragment.this.decimalFormat.format(value)}));
            SettingsFragment.this.mPageSettingUpdated.onPreferenceChange(preference, object);
        }
    });
    private Preference.OnPreferenceClickListener mManageSensorsClickListener = new Preference.OnPreferenceClickListener() { // from class: com.kopin.solos.settings.SettingsFragment.45
        @Override // android.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            Intent intent = new Intent(SettingsFragment.this.getActivity(), (Class<?>) SetupActivity.class);
            intent.putExtra(SetupActivity.SETUP_INTENT_EXTRA_KEY, 8);
            SettingsFragment.this.startActivity(intent);
            return true;
        }
    };
    private Preference.OnPreferenceClickListener mTermsClickListener = new Preference.OnPreferenceClickListener() { // from class: com.kopin.solos.settings.SettingsFragment.46
        @Override // android.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            Intent intent = new Intent(SettingsFragment.this.getActivity(), (Class<?>) TermsActivity.class);
            if (preference.getKey().contentEquals(SettingsFragment.PREF_TERMS)) {
                intent.putExtra(TermsActivity.LOAD_URL, TermsActivity.URL_TERMS).putExtra(TermsActivity.PAGE_TITLE_RESOURCE, R.string.title_terms);
            } else {
                intent.putExtra(TermsActivity.LOAD_URL, TermsActivity.URL_PRIVACY_POLICY).putExtra(TermsActivity.PAGE_TITLE_RESOURCE, R.string.title_privacy);
            }
            SettingsFragment.this.startActivity(intent);
            return true;
        }
    };
    Preference.OnPreferenceClickListener mBackPress = new Preference.OnPreferenceClickListener() { // from class: com.kopin.solos.settings.SettingsFragment.47
        @Override // android.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            if (preference instanceof PreferenceScreen) {
                final Dialog dialog = ((PreferenceScreen) preference).getDialog();
                dialog.getActionBar().setDisplayHomeAsUpEnabled(true);
                dialog.getActionBar().setDisplayShowTitleEnabled(true);
                View homeBtn = dialog.findViewById(android.R.id.home);
                View lv = dialog.findViewById(android.R.id.list);
                if (lv != null) {
                    lv.setPadding(0, 0, 0, 0);
                }
                if (homeBtn != null) {
                    View.OnClickListener dismissDialogClickListener = new View.OnClickListener() { // from class: com.kopin.solos.settings.SettingsFragment.47.1
                        @Override // android.view.View.OnClickListener
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    };
                    ViewParent homeBtnContainer = homeBtn.getParent();
                    if (homeBtnContainer instanceof FrameLayout) {
                        ViewGroup containerParent = (ViewGroup) homeBtnContainer.getParent();
                        if (containerParent instanceof LinearLayout) {
                            containerParent.setOnClickListener(dismissDialogClickListener);
                        } else {
                            ((FrameLayout) homeBtnContainer).setOnClickListener(dismissDialogClickListener);
                        }
                    } else {
                        homeBtn.setOnClickListener(dismissDialogClickListener);
                    }
                }
            }
            return false;
        }
    };
    private ServiceConnection mAppConnection = new ServiceConnection() { // from class: com.kopin.solos.settings.SettingsFragment.48
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName className, IBinder service) {
            HardwareReceiverService.HardwareBinder binder = (HardwareReceiverService.HardwareBinder) service;
            SettingsFragment.this.mService = binder.getService();
            SettingsFragment.this.mAppService = SettingsFragment.this.mService.getAppService();
            SettingsFragment.this.mAppService.setStatusListener(SettingsFragment.this.mStatusListener);
            PupilDevice.requestStatus();
            SettingsFragment.this.mAppBound = true;
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName className) {
            SettingsFragment.this.mAppBound = false;
            SettingsFragment.this.mAppService.removeStatusListener(SettingsFragment.this.mStatusListener);
            SettingsFragment.this.mStatusListener = null;
        }
    };
    private VCApplication.StatusListener mStatusListener = new VCApplication.StatusListener() { // from class: com.kopin.solos.settings.SettingsFragment.49
        @Override // com.kopin.pupil.VCApplication.StatusListener
        public void onStatus(PupilDevice.DeviceStatus deviceStatus) {
            if (SettingsFragment.this.mHeadsetPref != null) {
                SettingsFragment.this.runOnMainThread(new Runnable() { // from class: com.kopin.solos.settings.SettingsFragment.49.1
                    @Override // java.lang.Runnable
                    public void run() {
                        SettingsFragment.this.refreshForgetButton();
                    }
                });
            }
        }
    };

    public interface Action {
        void onAction(Preference preference, Object obj);
    }

    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_listview, (ViewGroup) null);
    }

    @Override // android.preference.PreferenceFragment
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference.getKey().matches(HEADSET_SCREEN)) {
            refreshForgetButton();
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
        if (!Config.DEBUG) {
            PreferenceScreen screen = (PreferenceScreen) findPreference(Prefs.PREF_NAVIGATE);
            Preference screenPrefNavLong = findPreference(Prefs.DEBUG_NAVIGATE_LONG_REMINDER);
            Preference screenPrefNavShort = findPreference(Prefs.DEBUG_NAVIGATE_SHORT_REMINDER);
            if (screen != null && screenPrefNavLong != null && screenPrefNavShort != null) {
                screen.removePreference(screenPrefNavLong);
                screen.removePreference(screenPrefNavShort);
            }
            PreferenceScreen screen2 = (PreferenceScreen) findPreference(HEADSET_SCREEN);
            Preference screenMirror = findPreference(HEADSET_MIRROR);
            if (screen2 != null && screenMirror != null) {
                screen2.removePreference(screenMirror);
            }
        }
        SplitHelper.splitInit(getActivity());
        refreshSplitSettings();
        this.multiMetrics1 = (DualCustomListPreference) findPreference("pref_dual_list");
        setSupportedEntries(this.multiMetrics1, R.array.multi_display_entries, R.array.multi_display_values);
        this.multiMetrics1.setOnPreferenceChangeListener(this.mPageSettingUpdated);
        if (Config.FORCE_MULTI_TIME_ELAPSED) {
            this.multiMetrics1.allowToggle(false);
        }
        this.multiMetrics2 = (DualCustomListPreference) findPreference("pref_dual_list2");
        setSupportedEntries(this.multiMetrics2, R.array.multi_display_entries, R.array.multi_display_values);
        this.multiMetrics2.setOnPreferenceChangeListener(this.mPageSettingUpdated);
        this.multiMetrics3 = (DualCustomListPreference) findPreference("pref_dual_list3");
        setSupportedEntries(this.multiMetrics3, R.array.multi_display_entries, R.array.multi_display_values);
        this.multiMetrics3.setOnPreferenceChangeListener(this.mPageSettingUpdated);
        ListPreference timerPref = (ListPreference) findPreference(Prefs.TIMER_KEY);
        if (timerPref.findIndexOfValue(timerPref.getValue()) == -1) {
            timerPref.setValueIndex(timerPref.findIndexOfValue(getString(R.string.setting_timer_default)));
        }
        timerPref.setSummary(timerPref.getEntry());
        timerPref.setOnPreferenceChangeListener(new OnListPreferenceChangeListener(R.array.setting_timer_entries, R.array.setting_timer_values));
        SwitchPreference ttsSwitch = (SwitchPreference) findPreference(Prefs.TTS_KEY);
        ttsSwitch.setOnPreferenceChangeListener(this.mTTSSwitched);
        ListPreference TTSTimer = (ListPreference) findPreference(Prefs.TTS_TIMER_KEY);
        TTSTimer.setSummary(TTSTimer.getEntry());
        TTSTimer.setOnPreferenceChangeListener(this.mTTSTimerUpdated);
        this.mModePref = (ListPreference) findPreference(Prefs.MODE_KEY);
        this.mModePref.setSummary(this.mModePref.getEntry());
        this.mModePref.setOnPreferenceChangeListener(new OnListPreferenceChangeListener(R.array.setting_mode_entries, R.array.setting_mode_values) { // from class: com.kopin.solos.settings.SettingsFragment.1
            @Override // com.kopin.solos.settings.SettingsFragment.OnListPreferenceChangeListener, android.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean ret = super.onPreferenceChange(preference, newValue);
                String[] modes = SettingsFragment.this.getResources().getStringArray(R.array.setting_mode_values);
                SettingsFragment.this.mAppService.refreshOnStartModeChange(newValue.equals(modes[0]));
                return ret;
            }
        });
        this.mTargetAveragePacePref = (TimePickerPreference) findPreference(Prefs.TARGET_AVERAGE_PACE_KEY);
        this.mTargetAveragePacePref.refresh();
        this.multiScreen1 = (CustomSwitchPreference) findPreference(Prefs.getMultiScreenKey(1));
        this.multiScreen1.setOnPreferenceChangeListener(this.mPageSettingUpdated);
        if (Config.FORCE_MULTI_TIME_ELAPSED) {
            this.multiScreen1.allowToggle(false);
        }
        this.multiScreen2 = (CustomSwitchPreference) findPreference(Prefs.getMultiScreenKey(2));
        this.multiScreen2.setOnPreferenceChangeListener(this.mPageSettingUpdated);
        this.multiScreen3 = (CustomSwitchPreference) findPreference(Prefs.getMultiScreenKey(3));
        this.multiScreen3.setOnPreferenceChangeListener(this.mPageSettingUpdated);
        this.splitMode = (ListPreference) findPreference(Prefs.LAP_MODE_SETTING);
        this.splitMode.setSummary(this.splitMode.getEntry());
        this.splitMode.setOnPreferenceChangeListener(new OnListPreferenceChangeListener(R.array.lap_mode_entries, R.array.lap_mode_values, new ValueAction() { // from class: com.kopin.solos.settings.SettingsFragment.2
            @Override // com.kopin.solos.settings.SettingsFragment.ValueAction
            public void onAction(Object object) {
                SettingsFragment.this.setSplitMetricEnabled(SplitHelper.SplitType.fromPreference(object.toString()));
                SettingsFragment.this.refreshSplitSettings();
            }
        }));
        this.mSplitTime = (TimePickerPreference) findPreference(Prefs.LAP_TIME);
        this.mSplitTime.setOnPreferenceChangeListener(new OnTextPreferenceChangeListener(this, 0, new SimpleAction() { // from class: com.kopin.solos.settings.SettingsFragment.3
            @Override // com.kopin.solos.settings.SettingsFragment.SimpleAction
            public void onAction() {
                SettingsFragment.this.refreshSplitSettings();
            }
        }));
        findPreference(PREF_RIDE).setOnPreferenceClickListener(this.mBackPress);
        findPreference(Prefs.PREF_NAVIGATE).setOnPreferenceClickListener(this.mBackPress);
        findPreference(PREF_VOICE_FEEDBACK).setOnPreferenceClickListener(this.mBackPress);
        findPreference(PREF_DEVICES_INFO).setOnPreferenceClickListener(this.mBackPress);
        findPreference(PREF_VIRTUAL_COACH).setOnPreferenceClickListener(this.mBackPress);
        findPreference("pref_metrics_display_settings").setOnPreferenceClickListener(this.mBackPress);
        handlePrefsForUnitSystem();
        this.mTargetAverageHeartratePref = (ValidatedEditTextPreference) findPreference(Prefs.TARGET_AVERAGE_HEARTRATE_KEY);
        int value = Utility.intFromString(this.mTargetAverageHeartratePref.getText(), getResources().getInteger(R.integer.target_heartrate_default));
        String dialogTitle = getString(R.string.pref_target_summary_format, new Object[]{getString(R.string.caps_bpm).toLowerCase()});
        String summary = getString(R.string.format_heartrate, new Object[]{this.decimalFormat.format(value)});
        this.mTargetAverageHeartratePref.setSummary(summary);
        this.mTargetAverageHeartratePref.setDialogTitle(dialogTitle);
        configureTextPreferenceKeyboard(this.mTargetAverageHeartratePref);
        this.mTargetAverageHeartratePref.setOnPreferenceChangeListener(this.mTargetAverageHeartratePrefChanged);
        this.mTargetAveragePowerPref = (ValidatedEditTextPreference) findPreference(Prefs.TARGET_AVERAGE_POWER_KEY);
        String dialogTitle2 = getString(R.string.pref_target_summary_format, new Object[]{getString(R.string.lower_watts)});
        int value2 = Utility.intFromString(this.mTargetAveragePowerPref.getText(), getResources().getInteger(R.integer.target_power_default));
        String summary2 = getString(R.string.format_power, new Object[]{this.decimalFormat.format(value2)});
        this.mTargetAveragePowerPref.setSummary(summary2);
        this.mTargetAveragePowerPref.setDialogTitle(dialogTitle2);
        configureTextPreferenceKeyboard(this.mTargetAveragePowerPref);
        this.mTargetAveragePowerPref.setOnPreferenceChangeListener(this.mTargetAveragePowerPrefChanged);
        this.mTargetAverageCadencePref = (ValidatedEditTextPreference) findPreference(Prefs.TARGET_AVERAGE_CADENCE_KEY);
        String dialogTitle3 = getString(R.string.pref_target_summary_format, new Object[]{getString(R.string.caps_rpm).toLowerCase()});
        int value3 = Utility.intFromString(this.mTargetAverageCadencePref.getText(), getResources().getInteger(R.integer.target_cadence_default));
        String summary3 = getString(R.string.format_cadence, new Object[]{this.decimalFormat.format(value3)});
        this.mTargetAverageCadencePref.setDialogTitle(dialogTitle3);
        this.mTargetAverageCadencePref.setSummary(summary3);
        configureTextPreferenceKeyboard(this.mTargetAverageCadencePref);
        this.mTargetAverageCadencePref.setOnPreferenceChangeListener(this.mTargetAverageCadencePrefChanged);
        this.mTargetAverageKickPref = (ValidatedEditTextPreference) findPreference(Prefs.TARGET_AVERAGE_KICK_KEY);
        String dialogTitle4 = getString(R.string.pref_target_summary_format, new Object[]{getString(R.string.lower_watts)});
        int value4 = Utility.intFromString(this.mTargetAverageKickPref.getText(), getResources().getInteger(R.integer.target_running_power_default));
        String summary4 = getString(R.string.format_power, new Object[]{this.decimalFormat.format(value4)});
        this.mTargetAverageKickPref.setSummary(summary4);
        this.mTargetAverageKickPref.setDialogTitle(dialogTitle4);
        configureTextPreferenceKeyboard(this.mTargetAverageKickPref);
        this.mTargetAverageKickPref.setOnPreferenceChangeListener(this.mTargetAverageKickPrefChanged);
        long targetTime = Prefs.getTargetTime();
        Prefs.setTargetTime(targetTime);
        this.countdownPref = (TimePickerPreference) findPreference(getString(R.string.pref_key_target_time));
        findPreference(PREF_MANAGE_SENSORS).setOnPreferenceClickListener(this.mManageSensorsClickListener);
        findPreference(PREF_ABOUT).setOnPreferenceClickListener(this.mBackPress);
        findPreference(PREF_TERMS).setOnPreferenceClickListener(this.mTermsClickListener);
        findPreference(PREF_PRIVACY_POLICY).setOnPreferenceClickListener(this.mTermsClickListener);
        findPreference(HEADSET_SCREEN).setOnPreferenceClickListener(this.mBackPress);
        setupHeadsetInfo();
        setupHeadsetBrightness();
        setupHeadsetVolume();
        ListPreference headsetSleepPref = (ListPreference) findPreference(Prefs.HEADSET_SLEEP);
        headsetSleepPref.setSummary(headsetSleepPref.getEntry());
        headsetSleepPref.setOnPreferenceChangeListener(new OnListPreferenceChangeListener(R.array.headset_sleep_entries, R.array.headset_sleep_values, new ValueAction() { // from class: com.kopin.solos.settings.SettingsFragment.4
            @Override // com.kopin.solos.settings.SettingsFragment.ValueAction
            public void onAction(Object object) {
                SettingsFragment.this.mAppService.sendWakeUp();
            }
        }));
        headsetSleepPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.kopin.solos.settings.SettingsFragment.5
            @Override // android.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                if (SettingsFragment.this.mAppBound && SettingsFragment.this.mAppService.isConnected()) {
                    return false;
                }
                ((ListPreference) preference).getDialog().dismiss();
                Toast.makeText(SettingsFragment.this.getActivity(), R.string.err_no_headset, 0).show();
                return true;
            }
        });
        PreferenceScreen sharing = (PreferenceScreen) findPreference(SHARE_SCREEN);
        sharing.setOnPreferenceClickListener(this.mBackPress);
        PreferenceCategory strava = new PreferenceCategory(getActivity());
        strava.setTitle(Platforms.Strava.getPrefCategory());
        sharing.addPreference(strava);
        this.mLoginStravaPref = ShareSettings.createLoginPref(getActivity(), Platforms.Strava);
        this.mAutoShareStrava = ShareSettings.createAutoPref(getActivity(), Platforms.Strava);
        strava.addPreference(this.mLoginStravaPref);
        strava.addPreference(this.mAutoShareStrava);
        PreferenceCategory tp = new PreferenceCategory(getActivity());
        tp.setTitle(Platforms.TrainingPeaks.getPrefCategory());
        sharing.addPreference(tp);
        this.mLoginTPPref = ShareSettings.createLoginPref(getActivity(), Platforms.TrainingPeaks);
        this.mAutoShareTP = ShareSettings.createAutoPref(getActivity(), Platforms.TrainingPeaks);
        tp.addPreference(this.mLoginTPPref);
        tp.addPreference(this.mAutoShareTP);
        PreferenceCategory mapmy = new PreferenceCategory(getActivity());
        mapmy.setTitle(Platforms.UnderArmour.getPrefCategory());
        sharing.addPreference(mapmy);
        this.mLoginUAPref = ShareSettings.createLoginPref(getActivity(), Platforms.UnderArmour);
        this.mAutoShareUA = ShareSettings.createAutoPref(getActivity(), Platforms.UnderArmour);
        mapmy.addPreference(this.mLoginUAPref);
        mapmy.addPreference(this.mAutoShareUA);
        PreferenceCategory facebookPrefCategory = new PreferenceCategory(getActivity());
        facebookPrefCategory.setTitle(Platforms.Facebook.getPrefCategory());
        this.mLoginFacebookPref = ShareSettings.createLoginPref(getActivity(), Platforms.Facebook);
        this.mAutoShareFacebook = ShareSettings.createAutoPref(getActivity(), Platforms.Facebook);
        this.mLoginStravaPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.kopin.solos.settings.SettingsFragment.6
            @Override // android.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                if (!Platforms.Strava.isLoggedIn(SettingsFragment.this.getActivity())) {
                    SettingsFragment.this.loginToShare(Platforms.Strava);
                    return true;
                }
                Platforms.Strava.logout(SettingsFragment.this.getActivity());
                SettingsFragment.this.mAutoShareStrava.setChecked(false);
                SettingsFragment.this.setSharingElementsEnabled(SettingsFragment.this.mLoginStravaPref, SettingsFragment.this.mAutoShareStrava, false);
                return true;
            }
        });
        this.mLoginTPPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.kopin.solos.settings.SettingsFragment.7
            @Override // android.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                if (!Platforms.TrainingPeaks.isLoggedIn(SettingsFragment.this.getActivity())) {
                    SettingsFragment.this.loginToShare(Platforms.TrainingPeaks);
                    return true;
                }
                Platforms.TrainingPeaks.logout(SettingsFragment.this.getActivity());
                SettingsFragment.this.mAutoShareTP.setChecked(false);
                SettingsFragment.this.setSharingElementsEnabled(SettingsFragment.this.mLoginTPPref, SettingsFragment.this.mAutoShareTP, false);
                return true;
            }
        });
        this.mLoginUAPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.kopin.solos.settings.SettingsFragment.8
            @Override // android.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                if (!Platforms.UnderArmour.isLoggedIn(SettingsFragment.this.getActivity())) {
                    SettingsFragment.this.loginToShare(Platforms.UnderArmour);
                    return true;
                }
                Platforms.UnderArmour.logout(SettingsFragment.this.getActivity());
                SettingsFragment.this.mAutoShareUA.setChecked(false);
                SettingsFragment.this.setSharingElementsEnabled(SettingsFragment.this.mLoginUAPref, SettingsFragment.this.mAutoShareUA, false);
                return true;
            }
        });
        this.mGPSToggle = (SwitchPreference) findPreference(getString(R.string.pref_key_gps));
        this.mGPSToggle.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: com.kopin.solos.settings.SettingsFragment.9
            @Override // android.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (SettingsFragment.this.mAppBound && (newValue instanceof Boolean)) {
                    if (!((Boolean) newValue).booleanValue()) {
                        SettingsFragment.this.mService.stopLocation();
                        Prefs.setGPSAllowed(false);
                    } else {
                        PermissionUtil.askPermission(SettingsFragment.this.getActivity(), Permission.ACCESS_FINE_LOCATION);
                        SettingsFragment.this.mService.prepareLocation();
                    }
                }
                return true;
            }
        });
        this.unitPref = (ListPreference) findPreference(getResources().getString(R.string.pref_key_unit));
        int i = 0;
        CharSequence[] entryValues = this.unitPref.getEntryValues();
        int length = entryValues.length;
        int i2 = 0;
        while (true) {
            if (i2 >= length) {
                break;
            }
            CharSequence val = entryValues[i2];
            if (Utility.isMetric() == Prefs.UnitSystem.METRIC.name().equalsIgnoreCase(val.toString())) {
                this.unitPref.setValueIndex(i);
                break;
            } else {
                i++;
                i2++;
            }
        }
        this.unitPref.setSummary(this.unitPref.getEntry());
        this.unitPref.setOnPreferenceChangeListener(new OnListPreferenceChangeListener(R.array.pref_unit_system_entries, R.array.pref_unit_system_values, new ValueAction() { // from class: com.kopin.solos.settings.SettingsFragment.10
            @Override // com.kopin.solos.settings.SettingsFragment.ValueAction
            public void onAction(Object object) {
                Prefs.UnitSystem unitSystem = Prefs.UnitSystem.getUnit(object.toString());
                if (unitSystem != Prefs.UnitSystem.IMPERIAL) {
                    unitSystem = Prefs.UnitSystem.METRIC;
                }
                SettingsFragment.this.onUnitSystemChanged(unitSystem);
                SettingsFragment.this.refreshUnitSystem(unitSystem);
            }
        }));
        PreferenceScreen rollingAvgs = (PreferenceScreen) findPreference("pref_metrics_display_settings");
        if (com.kopin.solos.common.config.Config.DEBUG) {
            String[] rollingAvgKeys = getResources().getStringArray(R.array.rolling_avg_keys);
            int length2 = rollingAvgKeys.length;
            int i3 = 0;
            while (true) {
                int i4 = i3;
                if (i4 >= length2) {
                    break;
                }
                String key = rollingAvgKeys[i4];
                final Sensor.DataType dataType = Sensor.DataType.guessFromString(key);
                Preference pref = rollingAvgs.findPreference(key);
                if (pref != null) {
                    if (!SensorsConnector.isAllowedType(dataType)) {
                        rollingAvgs.removePreference(pref);
                    } else {
                        ((CustomListPreference) pref).updateSummary();
                        pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: com.kopin.solos.settings.SettingsFragment.11
                            @Override // android.preference.Preference.OnPreferenceChangeListener
                            public boolean onPreferenceChange(Preference preference, Object newValue) {
                                if (SettingsFragment.this.mService != null) {
                                    SettingsFragment.this.mService.configureDataHolder(dataType, (String) newValue);
                                }
                                ((CustomListPreference) preference).updateSummary((String) newValue);
                                return true;
                            }
                        });
                    }
                }
                i3 = i4 + 1;
            }
        } else {
            PreferenceScreen root = (PreferenceScreen) findPreference(PARENT_SCREEN);
            if (root != null) {
                root.removePreference(rollingAvgs);
            }
        }
        this.mRideSettingsScreen = (PreferenceScreen) findPreference(PREF_RIDE);
        this.mActiveMetrics = (PreferenceScreen) findPreference("active_metrics");
        this.mActiveMetrics.setOnPreferenceClickListener(this.mBackPress);
        this.mActiveMetricsMultiple = (PreferenceScreen) findPreference("active_metrics_multiple");
        this.mActiveMetricsMultiple.setOnPreferenceClickListener(this.mBackPress);
        this.mMetricSwitchPref = (DualRadioPreference) findPreference(Prefs.PAGE_DISPLAY_MODE_KEY);
        this.mRideSettingsScreen.removePreference(this.mActiveMetrics);
        this.mRideSettingsScreen.removePreference(this.mActiveMetricsMultiple);
        this.mRideSettingsScreen.addPreference(this.mMetricSwitchPref.isChecked() ? this.mActiveMetrics : this.mActiveMetricsMultiple);
        this.mMetricSwitchPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: com.kopin.solos.settings.SettingsFragment.12
            @Override // android.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean choiceSingleMetric = ((Boolean) newValue).booleanValue();
                SettingsFragment.this.mRideSettingsScreen.removePreference(choiceSingleMetric ? SettingsFragment.this.mActiveMetricsMultiple : SettingsFragment.this.mActiveMetrics);
                SettingsFragment.this.mRideSettingsScreen.addPreference(choiceSingleMetric ? SettingsFragment.this.mActiveMetrics : SettingsFragment.this.mActiveMetricsMultiple);
                SettingsFragment.this.initMetricPreferences(choiceSingleMetric);
                SettingsFragment.this.mAppService.refreshDoublePages();
                return true;
            }
        });
        initMetricPreferences(this.mMetricSwitchPref.isChecked());
        if (Config.DEBUG) {
            ValidatedEditTextPreference mDebugNavigateLongReminder = (ValidatedEditTextPreference) findPreference(Prefs.DEBUG_NAVIGATE_LONG_REMINDER);
            int value5 = Utility.intFromString(mDebugNavigateLongReminder.getText(), getResources().getInteger(R.integer.navigate_long_reminder_default));
            String summary5 = getString(R.string.format_time_in_seconds, new Object[]{this.decimalFormatNoPlaces.format(value5)});
            mDebugNavigateLongReminder.setSummary(summary5);
            configureTextPreferenceKeyboard(mDebugNavigateLongReminder);
            mDebugNavigateLongReminder.setOnPreferenceChangeListener(this.mNavigateReminderPrefChanged);
            ValidatedEditTextPreference mDebugNavigateShortReminder = (ValidatedEditTextPreference) findPreference(Prefs.DEBUG_NAVIGATE_SHORT_REMINDER);
            int value6 = Utility.intFromString(mDebugNavigateShortReminder.getText(), getResources().getInteger(R.integer.navigate_short_reminder_default));
            String summary6 = getString(R.string.format_time_in_seconds, new Object[]{this.decimalFormatNoPlaces.format(value6)});
            mDebugNavigateShortReminder.setSummary(summary6);
            configureTextPreferenceKeyboard(mDebugNavigateShortReminder);
            mDebugNavigateShortReminder.setOnPreferenceChangeListener(this.mNavigateReminderPrefChanged);
        }
        this.mNavigateDisplayCompass = (SwitchPreference) findPreference(Prefs.NAVIGATE_DISPLAY_COMPASS);
        this.mNavigateFullScreen = (SwitchPreference) findPreference(Prefs.NAVIGATE_FULL_SCREEN);
        CustomMultiSelectPreference mGrScreensPref = (CustomMultiSelectPreference) findPreference(Prefs.getGhostMetricKey());
        setSupportedEntries(mGrScreensPref, R.array.gr_setting_screens_entries, R.array.gr_setting_screens_values);
        mGrScreensPref.setEnabled(true);
        mGrScreensPref.setSummary(mGrScreensPref.createSummary(mGrScreensPref.getValues()));
        mGrScreensPref.setOnPreferenceChangeListener(this.mScreensUpdated);
        if (LiveRide.getCurrentSport() == SportType.RUN) {
            this.mRideSettingsScreen.removePreference(findPreference(Prefs.AUTO_PAUSE_SPEED_KEY));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initMetricPreferences(boolean choiceSingleMetric) {
        if (choiceSingleMetric) {
            initialiseSingleMetrics(getResources().getStringArray(R.array.switch_pref_single_metrics_values), this.mSingleMetricsListener);
            initialiseSingleMetrics(getResources().getStringArray(R.array.dual_pref_single_metrics_values), this.mSingleMetricsListener);
            initialiseSingleMetrics(getResources().getStringArray(R.array.switch_pref_compound_metrics_values), this.mSingleMetricsListener);
            initialiseSingleMetrics(getResources().getStringArray(R.array.target_single_metrics_values), this.mSingleMetricsListener);
        }
    }

    private void initialiseSingleMetrics(String[] keys, Preference.OnPreferenceChangeListener listener) {
        for (String key : keys) {
            TwoStatePreference pref = (TwoStatePreference) findPreference(key);
            if (pref != null) {
                pref.setOnPreferenceChangeListener(listener);
            }
        }
        readSingleMetrics();
    }

    private void setSupportedEntries(Preference prefList, int entsId, int valsId) {
        CharSequence[] ents = getResources().getStringArray(entsId);
        CharSequence[] vals = getResources().getStringArray(valsId);
        if (ents == null || vals == null || ents.length != vals.length) {
            throw new IllegalArgumentException("Arrays don't match for list preference");
        }
        CharSequence[] supportedEnts = null;
        CharSequence[] supportedVals = null;
        Set<String> supportedValues = new HashSet<>(ents.length);
        while (true) {
            int count = 0;
            for (int i = 0; i < vals.length; i++) {
                CharSequence val = vals[i];
                TemplateManager.DataType dataType = TemplateManager.DataType.fromString(val.toString());
                MetricDataType metricDataType = MetricDataType.fromString(val.toString());
                if (dataType.isSupported() && (metricDataType == null || ConfigMetrics.isHeadsetMetricEnabled(metricDataType))) {
                    if (supportedEnts != null) {
                        supportedEnts[count] = ents[i];
                        supportedVals[count] = val;
                        supportedValues.add(val.toString());
                    }
                    count++;
                }
            }
            if (count == 0 || supportedEnts != null) {
                break;
            }
            supportedEnts = new CharSequence[count];
            supportedVals = new CharSequence[count];
        }
        if (supportedEnts != null && supportedVals != null) {
            if (prefList instanceof ListPreference) {
                ((ListPreference) prefList).setEntries(supportedEnts);
                ((ListPreference) prefList).setEntryValues(supportedVals);
            } else if (prefList instanceof MultiSelectListPreference) {
                ((MultiSelectListPreference) prefList).setEntries(supportedEnts);
                ((MultiSelectListPreference) prefList).setEntryValues(supportedVals);
                Set<String> values = ((MultiSelectListPreference) prefList).getValues();
                if (!values.isEmpty()) {
                    supportedValues.retainAll(values);
                }
                ((MultiSelectListPreference) prefList).setValues(supportedValues);
            }
        }
    }

    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(getActivity(), (Class<?>) HardwareReceiverService.class);
        getActivity().bindService(intent, this.mAppConnection, 1);
    }

    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public void onStop() {
        super.onStop();
        if (this.mAppBound) {
            getActivity().unbindService(this.mAppConnection);
            this.mAppBound = false;
        }
    }

    @Override // com.kopin.solos.common.BaseFragment, android.app.Fragment
    public void onPause() {
        super.onPause();
        readSingleMetrics();
        onScreensUpdated(true);
    }

    @Override // com.kopin.solos.common.BaseFragment, android.app.Fragment
    public void onResume() {
        super.onResume();
        boolean inRide = Prefs.isReady();
        this.mModePref.setEnabled(!inRide);
        this.splitMode.setEnabled(!inRide);
        setSplitMetricEnabled(SplitHelper.getSplitMode(getActivity()));
        Preference ghostLaps = findPreference(getString(R.string.pref_key_ghost_laps));
        if (ghostLaps != null) {
            ghostLaps.setEnabled(!inRide);
        }
        this.mMetricSwitchPref.setEnabled(!inRide);
        this.mActiveMetrics.setEnabled(!inRide);
        this.mActiveMetricsMultiple.setEnabled(!inRide);
        this.unitPref.setEnabled(!inRide);
        CustomMultiSelectPreference mGrScreensPref = (CustomMultiSelectPreference) findPreference(Prefs.getGhostMetricKey());
        mGrScreensPref.setEnabled(!inRide);
        ShareSettings.refreshPrefs(getActivity(), (PreferenceScreen) findPreference(SHARE_SCREEN), this.mSetSharingElementsEnabled);
        if (this.mMetricSwitchPref.isChecked()) {
            this.mTargetDistancePref.setEnabled(!inRide);
            findPreference(getString(R.string.pref_key_target_time)).setEnabled(!inRide);
            this.mTargetAverageSpeedPref.setEnabled(!inRide);
            this.mTargetAverageHeartratePref.setEnabled(!inRide);
            this.mTargetAveragePowerPref.setEnabled(!inRide);
            this.mTargetAverageCadencePref.setEnabled(!inRide);
            this.mTargetAveragePacePref.setEnabled(!inRide);
            this.mTargetAverageStepPref.setEnabled(!inRide);
            this.mTargetAverageKickPref.setEnabled(inRide ? false : true);
        }
        readSingleMetrics();
        refreshForgetButton();
        this.mGPSToggle.setChecked(Prefs.isGPSEnabled());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void readSingleMetrics() {
        String depend;
        PreferenceScreen singleScreen = (PreferenceScreen) findPreference("active_metrics");
        List<TwoStatePreference> prefs = new ArrayList<>();
        for (String key : Prefs.getSingleMetricPrefKeys()) {
            TwoStatePreference twoStatePreference = (TwoStatePreference) findPreference(key);
            if (twoStatePreference != null) {
                TemplateManager.DataType dataType = TemplateManager.DataType.fromString(key);
                MetricDataType metricType = MetricDataType.fromString(key);
                if (!dataType.isSupported() || (metricType != null && !ConfigMetrics.isHeadsetMetricEnabled(metricType))) {
                    if (singleScreen != null) {
                        Log.d(TAG, "removing pref " + key);
                        if (singleScreen.removePreference(twoStatePreference)) {
                            String[] singleMetricsWithsDependencies = getResources().getStringArray(R.array.switch_pref_single_metrics_all_dependencies);
                            for (String otherKey : singleMetricsWithsDependencies) {
                                Preference preference = findPreference(otherKey);
                                if (preference != null && (depend = preference.getDependency()) != null && depend.length() > 0 && key.equals(depend)) {
                                    Log.d(TAG, "removing pref dependency " + depend);
                                    singleScreen.removePreference(preference);
                                }
                            }
                        }
                    }
                } else {
                    prefs.add(twoStatePreference);
                }
            }
        }
        Prefs.setSingleMetricChoices(prefs);
        Prefs.setSingleMetricGhostChoices();
    }

    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle tempextras = null;
        if (data != null) {
            tempextras = data.getExtras();
        }
        final Bundle extras = tempextras;
        if (!ShareHelper.onLoginResult(getActivity(), requestCode, resultCode, data, new ShareHelper.AuthListener() { // from class: com.kopin.solos.settings.SettingsFragment.14
            @Override // com.kopin.solos.share.ShareHelper.AuthListener
            public void onResult(Platforms which, boolean success, String message) {
                if (success) {
                    ShareSettings.refreshPrefs(SettingsFragment.this.getActivity(), SettingsFragment.this.getPreferenceScreen(), SettingsFragment.this.mSetSharingElementsEnabled);
                    return;
                }
                String errorTitle = SettingsFragment.this.getString(R.string.share_please_login_title);
                String errorMessage = SettingsFragment.this.getString(R.string.share_please_login_message, new Object[]{SettingsFragment.this.getString(which.getNameId())});
                if (extras != null) {
                    errorTitle = extras.getString(SettingsFragment.RESULT_TITLE, SettingsFragment.this.getString(R.string.share_please_login_title));
                    errorMessage = extras.getString(SettingsFragment.RESULT_MESSAGE, SettingsFragment.this.getString(R.string.share_please_login_message, new Object[]{SettingsFragment.this.getString(which.getNameId())}));
                }
                AlertDialog dialog = DialogUtils.createDialog(SettingsFragment.this.getActivity(), "", "", SettingsFragment.this.getString(android.R.string.ok), (Runnable) null);
                dialog.setTitle(errorTitle);
                dialog.setMessage(errorMessage);
                dialog.show();
                DialogUtils.setDialogTitleDivider(dialog);
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public static void configureTextPreferenceKeyboard(final EditTextPreference mEditTextPreference) {
        mEditTextPreference.getEditText().setImeOptions(6);
        mEditTextPreference.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.kopin.solos.settings.SettingsFragment.15
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId != 6) {
                    return false;
                }
                if ((mEditTextPreference instanceof ValidatedEditTextPreference) && !((ValidatedEditTextPreference) mEditTextPreference).onCheckValue(v.getText().toString())) {
                    return false;
                }
                mEditTextPreference.onClick(mEditTextPreference.getDialog(), -1);
                mEditTextPreference.getDialog().dismiss();
                return true;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSharingElementsEnabled(final Preference loginPref, final SwitchPreference sharePref, final boolean loggedIn) {
        if (Thread.currentThread().getId() != this.mThreadId) {
            this.mHandler.post(new Runnable() { // from class: com.kopin.solos.settings.SettingsFragment.17
                @Override // java.lang.Runnable
                public void run() {
                    SettingsFragment.this.setSharingElementsEnabled(loginPref, sharePref, loggedIn);
                }
            });
            return;
        }
        if (sharePref != null) {
            sharePref.setEnabled(loggedIn);
        }
        if (loginPref != null) {
            loginPref.setTitle(loggedIn ? R.string.pref_logout : R.string.pref_login);
        }
    }

    private void setupHeadsetInfo() {
        CustomSwitchPreference mirrorPref;
        this.mHeadsetPref = (HeadsetPreference) findPreference(HEADSET_INFO);
        this.mHeadsetUpdatePref = findPreference(HEADSET_UPDATE);
        this.mHeadsetUpdatePref.setEnabled(PupilDevice.isConnected() && FirmwareFlash.updateAvailable());
        this.mHeadsetUpdatePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.kopin.solos.settings.SettingsFragment.18
            @Override // android.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(SettingsFragment.this.getActivity(), (Class<?>) FlashActivity.class);
                SettingsFragment.this.startActivity(intent);
                return true;
            }
        });
        this.mCableUpdatePref = findPreference(HEADSET_CABLE_UPDATE);
        this.mCableUpdatePref.setEnabled(SensorsConnector.isAntBridgeConnected() && CableFlash.updateAvailable());
        this.mCableUpdatePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.kopin.solos.settings.SettingsFragment.19
            @Override // android.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(SettingsFragment.this.getActivity(), (Class<?>) CableFlashActivity.class);
                SettingsFragment.this.startActivity(intent);
                return true;
            }
        });
        if (Config.DEBUG && (mirrorPref = (CustomSwitchPreference) findPreference(HEADSET_MIRROR)) != null) {
            mirrorPref.setChecked(PupilDevice.isMirrorConnected());
            mirrorPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: com.kopin.solos.settings.SettingsFragment.20
                @Override // android.preference.Preference.OnPreferenceChangeListener
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if (!((Boolean) newValue).booleanValue()) {
                        PupilDevice.disconnectMirror();
                        return true;
                    }
                    new RemoteScreenLister(SettingsFragment.this.getActivity(), new RemoteScreenLister.OnServiceChosenListener() { // from class: com.kopin.solos.settings.SettingsFragment.20.1
                        @Override // com.kopin.pupil.remote.RemoteScreenLister.OnServiceChosenListener
                        public void onServiceChosen(NsdServiceInfo device) {
                            String addr = device.getHost().getHostAddress();
                            try {
                                InetAddress[] addrs = InetAddress.getAllByName(addr);
                                if (addrs != null && addrs.length == 1 && SettingsFragment.this.mAppBound) {
                                    SettingsFragment.this.mAppService.connectToMirrorService(addrs[0]);
                                }
                            } catch (UnknownHostException e) {
                                e.printStackTrace();
                            }
                        }
                    }).show();
                    return true;
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshForgetButton() {
        boolean allowUpdate = false;
        Preference forgetButton = findPreference(HEADSET_FORGET);
        String paired = forgetButton.getSharedPreferences().getString(ConnectionManager.HEADSET_PAIRED, null);
        if (paired == null || !PupilDevice.isConnected()) {
            forgetButton.setOnPreferenceClickListener(this.mConnectHeadset);
            forgetButton.setTitle(R.string.label_connect_headset);
        } else {
            forgetButton.setTitle(R.string.label_forget_headset);
            forgetButton.setOnPreferenceClickListener(this.mForgetHeadset);
            allowUpdate = true;
        }
        this.mHeadsetPref.refresh();
        boolean allowFW = allowUpdate;
        if (allowFW && (!FirmwareFlash.updateAvailable() || !FirmwareFlash.isUpdateHigherVersion(PupilDevice.currentDeviceInfo().mVersion))) {
            allowFW = false;
        }
        this.mHeadsetUpdatePref.setEnabled(allowFW || FirmwareFlash.isActive() || CableFlash.isActive());
        this.mHeadsetUpdatePref.setSummary(FirmwareFlash.updateVersion());
        boolean allowCABLE = allowUpdate;
        if (allowCABLE && (!CableFlash.updateAvailable() || !CableFlash.isUpdateHigherVersion(PupilDevice.currentDeviceInfo().mAntBridgeVersion))) {
            allowCABLE = false;
        }
        this.mCableUpdatePref.setEnabled(allowCABLE || FirmwareFlash.isActive() || CableFlash.isActive());
        this.mCableUpdatePref.setSummary(CableFlash.updateVersion());
    }

    private void setupHeadsetBrightness() {
        Preference headsetBrightnessPreference = findPreference(Prefs.HEADSET_BRIGHTNESS);
        headsetBrightnessPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.kopin.solos.settings.SettingsFragment.23
            @Override // android.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                if (SettingsFragment.this.mAppBound && SettingsFragment.this.mAppService.isConnected()) {
                    SettingsFragment.this.checkDeviceStatus(true);
                    SeekBarDialog.show(SettingsFragment.this.getActivity(), new int[]{R.drawable.ic_brightness_empty, R.drawable.ic_brightness_half, R.drawable.ic_brightness_full}, PupilDevice.currentDeviceStatus().getBrightness() & 255, 255, Prefs.hasHeadsetAutoBrightness(), SettingsFragment.this.mUpdateHeadsetBrightness);
                } else {
                    Toast.makeText(SettingsFragment.this.getActivity(), R.string.err_no_headset, 0).show();
                }
                return false;
            }
        });
    }

    private void setupHeadsetVolume() {
        Preference volumePreference = findPreference(Prefs.HEADSET_VOLUME);
        volumePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.kopin.solos.settings.SettingsFragment.25
            @Override // android.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                if (SettingsFragment.this.mAppBound && SettingsFragment.this.mAppService.isConnected()) {
                    PupilDevice.wakeUp();
                    SettingsFragment.this.checkDeviceStatus(true);
                    byte actual = PupilDevice.currentDeviceStatus().getVolume();
                    byte val = VolumeHelper.headsetToFriendly(actual);
                    SeekBarDialog.show(SettingsFragment.this.getActivity(), new int[]{R.drawable.ic_volume_empty, R.drawable.ic_volume_half, R.drawable.ic_volume_full}, val, 10, SettingsFragment.this.mUpdateHeadsetVolume);
                    PupilDevice.sendAction(ActionType.PLAY_CHIRP);
                } else {
                    Toast.makeText(SettingsFragment.this.getActivity(), R.string.err_no_headset, 0).show();
                }
                return false;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkDeviceStatus(boolean andRefresh) {
        if (andRefresh && this.mAppBound) {
            PupilDevice.requestStatus();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSplitMetricEnabled(SplitHelper.SplitType splitMode) {
        boolean z = false;
        boolean inRide = Prefs.isReady();
        TimePickerPreference timePickerPreference = this.mSplitTime;
        boolean z2 = !inRide && splitMode == SplitHelper.SplitType.TIME;
        timePickerPreference.setEnabled(z2);
        ValidatedEditTextPreference validatedEditTextPreference = this.mSplitDistance;
        if (!inRide && splitMode == SplitHelper.SplitType.DISTANCE) {
            z = true;
        }
        validatedEditTextPreference.setEnabled(z);
    }

    private void handlePrefsForUnitSystem() {
        String string;
        final Context context = getActivity();
        ValidatedEditTextPreference.UnitSystemChangeListener distanceUnitChangeListener = new ValidatedEditTextPreference.UnitSystemChangeListener() { // from class: com.kopin.solos.settings.SettingsFragment.27
            @Override // com.kopin.solos.settings.ValidatedEditTextPreference.UnitSystemChangeListener
            public String onValueEntered(String text) {
                return text;
            }

            @Override // com.kopin.solos.settings.ValidatedEditTextPreference.UnitSystemChangeListener
            public String onValueFetched(String text) {
                return SettingsFragment.this.decimalFormat.format(Double.parseDouble(text));
            }
        };
        String unit = Conversion.getUnitOfDistance(context);
        this.mTargetDistancePref = (ValidatedEditTextPreference) findPreference(Prefs.TARGET_DISTANCE);
        this.mTargetDistancePref.setDialogTitle(getString(R.string.pref_target_summary_format, new Object[]{unit}));
        double value = Prefs.getTarget(TargetPreference.DISTANCE);
        String summary = String.format(mValueUnitFmtStr, this.decimalFormat.format(value), unit);
        this.mTargetDistancePref.setSummary(summary);
        this.mTargetDistancePref.setOnPreferenceChangeListener(this.mTargetDistanceChanged);
        configureTextPreferenceKeyboard(this.mTargetDistancePref);
        String unit2 = Conversion.getUnitOfDistance(context);
        this.mSplitDistance = (ValidatedEditTextPreference) findPreference(getString(R.string.pref_key_lap_distance));
        String title = getString(R.string.lap_distance_title, new Object[]{unit2});
        this.mSplitDistance.setDialogTitle(title);
        this.mSplitDistance.setTitle(title);
        double value2 = Double.parseDouble(this.mSplitDistance.getText());
        String summary2 = String.format(mValueUnitFmtStr, this.decimalFormat.format(value2), unit2);
        this.mSplitDistance.setSummary(summary2);
        configureTextPreferenceKeyboard(this.mSplitDistance);
        this.mSplitDistance.setOnPreferenceChangeListener(new OnTextPreferenceChangeListener(0, new SimpleAction() { // from class: com.kopin.solos.settings.SettingsFragment.28
            @Override // com.kopin.solos.settings.SettingsFragment.SimpleAction
            public void onAction() {
                SettingsFragment.this.refreshSplitSettings();
            }
        }) { // from class: com.kopin.solos.settings.SettingsFragment.29
            @Override // com.kopin.solos.settings.SettingsFragment.OnTextPreferenceChangeListener, android.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean retVal = super.onPreferenceChange(preference, newValue);
                Double splitDistance = Double.valueOf(Double.parseDouble(newValue.toString()));
                preference.setSummary(String.format(SettingsFragment.mValueUnitFmtStr, SettingsFragment.this.decimalFormat.format(splitDistance), Conversion.getUnitOfDistance(context)));
                return retVal;
            }
        });
        this.mSplitDistance.setUnitSystemChangeListener(distanceUnitChangeListener);
        String unit3 = Conversion.getUnitOfSpeed(context);
        this.mTargetAverageSpeedPref = (ValidatedEditTextPreference) findPreference(Prefs.TARGET_AVERAGE_SPEED_KEY);
        this.mTargetAverageSpeedPref.setDialogTitle(getString(R.string.pref_target_summary_format, new Object[]{unit3}));
        double value3 = Utility.roundToLong(Conversion.speedForLocale(Prefs.getTargetAverageSpeedValue()));
        String summary3 = String.format(mValueUnitFmtStr, Long.valueOf(Utility.roundToLong(value3)), unit3);
        this.mTargetAverageSpeedPref.setSummary(summary3);
        this.mTargetAverageSpeedPref.setOnPreferenceChangeListener(this.mTargetAverageSpeedPrefChanged);
        configureTextPreferenceKeyboard(this.mTargetAverageSpeedPref);
        Conversion.getUnitOfPace(context);
        this.mTargetAveragePacePref.setOnPreferenceChangeListener(this.mTargetAveragePacePrefChanged);
        this.mTargetAveragePacePref.refresh();
        String unit4 = getString(R.string.unit_cadence_run_short);
        this.mTargetAverageStepPref = (ValidatedEditTextPreference) findPreference(Prefs.TARGET_AVERAGE_STEP_KEY);
        this.mTargetAverageStepPref.setDialogTitle(getString(R.string.pref_target_summary_format, new Object[]{unit4}));
        double value4 = Prefs.getTarget(TargetPreference.AVERAGE_STEP);
        String summary4 = String.format(mValueUnitFmtStr, Double.valueOf(Utility.roundToOneDecimalPlaces(value4)), unit4);
        this.mTargetAverageStepPref.setSummary(summary4);
        this.mTargetAverageStepPref.setOnPreferenceChangeListener(this.mTargetAverageStepPrefChanged);
        findPreference(Prefs.AUTO_PAUSE_KEY).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: com.kopin.solos.settings.SettingsFragment.30
            @Override // android.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object o) {
                String unit5 = Conversion.getUnitOfSpeed(SettingsFragment.this.getActivity());
                if (((Boolean) o).booleanValue()) {
                    SettingsFragment.this.mAutoPauseSpeedPref.setSummary(String.format(SettingsFragment.mValueUnitFmtStr, SettingsFragment.this.decimalFormat.format(Conversion.speedForLocale(Prefs.getAutoPauseSpeedMPS())), unit5));
                } else {
                    String title2 = SettingsFragment.this.getString(R.string.pref_title_pause_speed, new Object[]{unit5});
                    SettingsFragment.this.mAutoPauseSpeedPref.setSummary(title2);
                }
                return true;
            }
        });
        String unit5 = Conversion.getUnitOfSpeed(getActivity());
        String title2 = getString(R.string.pref_title_pause_speed, new Object[]{unit5});
        this.mAutoPauseSpeedPref = (ValidatedEditTextPreference) findPreference(Prefs.AUTO_PAUSE_SPEED_KEY);
        this.mAutoPauseSpeedPref.setDialogTitle(title2);
        ValidatedEditTextPreference validatedEditTextPreference = this.mAutoPauseSpeedPref;
        if (Prefs.isAutoPauseEnabled()) {
            string = String.format(mValueUnitFmtStr, this.decimalFormat.format(Conversion.speedForLocale(Prefs.getAutoPauseSpeedMPS())), unit5);
        } else {
            string = getString(R.string.pref_title_pause_speed, new Object[]{unit5});
        }
        validatedEditTextPreference.setSummary(string);
        configureTextPreferenceKeyboard(this.mAutoPauseSpeedPref);
        this.mAutoPauseSpeedPref.setOnPreferenceChangeListener(new OnTextPreferenceChangeListener(0) { // from class: com.kopin.solos.settings.SettingsFragment.31
            @Override // com.kopin.solos.settings.SettingsFragment.OnTextPreferenceChangeListener, android.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                double speedLocale = Utility.doubleFromString(newValue.toString(), 0.0d);
                double speed = Conversion.speedFromLocale(speedLocale);
                Prefs.setAutoPauseSpeedMPS(speed);
                preference.setSummary(String.format(SettingsFragment.mValueUnitFmtStr, SettingsFragment.this.decimalFormat.format(speedLocale), Conversion.getUnitOfSpeed(context)));
                return super.onPreferenceChange(preference, newValue);
            }
        });
        this.multiMetrics1.setUnitSystemChangeListener(distanceUnitChangeListener);
        this.multiMetrics2.setUnitSystemChangeListener(distanceUnitChangeListener);
        this.multiMetrics3.setUnitSystemChangeListener(distanceUnitChangeListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUnitSystemChanged(Prefs.UnitSystem unitSystem) {
        String summary;
        Utility.setUnitSystem(unitSystem);
        Prefs.convertTargets(unitSystem == Prefs.UnitSystem.METRIC);
        String unit = Conversion.getUnitOfDistance(getActivity());
        Double value = Double.valueOf(Prefs.getTarget(TargetPreference.DISTANCE));
        this.mTargetDistancePref.setSummary(String.format(mValueUnitFmtStr, this.decimalFormat.format(value), unit));
        String title = getString(R.string.pref_target_distance_title) + " " + unit;
        this.mTargetDistancePref.setText(value.toString());
        this.mTargetDistancePref.setDialogTitle(title);
        Double value2 = Double.valueOf(Prefs.getLapDistance());
        this.mSplitDistance.setSummary(String.format(mValueUnitFmtStr, this.decimalFormat.format(value2), unit));
        String title2 = getString(R.string.lap_distance_title, new Object[]{unit});
        this.mSplitDistance.setText(value2.toString());
        this.mSplitDistance.setTitle(title2);
        this.mSplitDistance.setDialogTitle(title2);
        long speedInLocale = Utility.roundToLong(Conversion.speedForLocale(Prefs.getTargetAverageSpeedValue(), unitSystem));
        Object unit2 = Conversion.getUnitOfSpeed(getActivity());
        String title3 = getString(R.string.pref_target_summary_format, new Object[]{unit2});
        String summary2 = String.format(mValueUnitFmtStr, Long.valueOf(speedInLocale), unit2);
        this.mTargetAverageSpeedPref.setText(String.valueOf(speedInLocale));
        this.mTargetAverageSpeedPref.setSummary(summary2);
        this.mTargetAverageSpeedPref.setDialogTitle(title3);
        this.mTargetAveragePacePref.refresh();
        Double value3 = Double.valueOf(Conversion.speedForLocale(Double.valueOf(Prefs.getAutoPauseSpeedMPS()).doubleValue(), unitSystem));
        boolean autoPaused = Prefs.isAutoPauseEnabled();
        this.mAutoPauseSpeedPref.setText(value3.toString());
        String title4 = getString(R.string.pref_title_pause_speed, new Object[]{unit2});
        if (autoPaused && value3.doubleValue() > 0.0d) {
            summary = String.format(mValueUnitFmtStr, this.decimalFormat.format(value3), unit2);
        } else {
            summary = title4;
        }
        this.mAutoPauseSpeedPref.setSummary(summary);
        this.mAutoPauseSpeedPref.setDialogTitle(title4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshUnitSystem(Prefs.UnitSystem unitSystem) {
        if (this.mAppBound) {
            this.mAppService.refreshUnitSystem(unitSystem);
        }
    }

    private void onScreensUpdated(boolean singleMetric) {
        if (this.mAppBound) {
            if (singleMetric) {
                this.mAppService.refreshPagesAvailability();
            } else {
                this.mAppService.refreshDoublePages();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshSplitSettings() {
        if (this.mAppBound) {
            this.mAppService.refreshSplitSettings();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loginToShare(Platforms platform) {
        if (!Utility.isNetworkAvailable(getActivity())) {
            DialogUtils.showNoNetworkDialog(getActivity());
        } else {
            ShareHelper.login(this, platform);
        }
    }

    public void runOnMainThread(Runnable runnable) {
        if (Thread.currentThread().getId() != this.mThreadId) {
            this.mHandler.post(runnable);
        } else {
            runnable.run();
        }
    }

    private abstract class ValueAction implements Action {
        public abstract void onAction(Object obj);

        private ValueAction() {
        }

        @Override // com.kopin.solos.settings.SettingsFragment.Action
        public void onAction(Preference preference, Object object) {
            onAction(object);
        }
    }

    private abstract class SimpleAction implements Action {
        public abstract void onAction();

        private SimpleAction() {
        }

        @Override // com.kopin.solos.settings.SettingsFragment.Action
        public void onAction(Preference preference, Object object) {
            onAction();
        }
    }

    private class OnTextPreferenceChangeListener implements Preference.OnPreferenceChangeListener {
        private Action mAction;
        private int mTextFormatter;

        public OnTextPreferenceChangeListener(int textFormatter) {
            this.mTextFormatter = textFormatter;
        }

        public OnTextPreferenceChangeListener(SettingsFragment settingsFragment, int textFormatter, Action action) {
            this(textFormatter);
            this.mAction = action;
        }

        @Override // android.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (this.mTextFormatter != 0) {
                preference.setSummary(SettingsFragment.this.getString(this.mTextFormatter, new Object[]{newValue.toString()}));
            }
            if (this.mAction != null) {
                this.mAction.onAction(preference, newValue);
            }
            return true;
        }
    }

    protected static class OnListPreferenceChangeListener implements Preference.OnPreferenceChangeListener {
        protected Action mAction;
        protected int mEntries;
        protected int mValues;

        public OnListPreferenceChangeListener(int entriesId, int valueId) {
            this.mEntries = entriesId;
            this.mValues = valueId;
        }

        public OnListPreferenceChangeListener(int entriesId, int valueId, Action action) {
            this(entriesId, valueId);
            this.mAction = action;
        }

        @Override // android.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String[] entries = preference.getContext().getResources().getStringArray(this.mEntries);
            String[] values = preference.getContext().getResources().getStringArray(this.mValues);
            int pos = Utility.search(values, newValue.toString());
            if (pos >= 0 && pos < entries.length) {
                preference.setSummary(entries[pos]);
            }
            if (this.mAction != null) {
                this.mAction.onAction(preference, newValue);
                return true;
            }
            return true;
        }
    }
}
