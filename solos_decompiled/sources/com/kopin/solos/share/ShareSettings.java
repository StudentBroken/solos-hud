package com.kopin.solos.share;

import android.content.Context;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import com.kopin.solos.settings.CustomSwitchPreference;

/* JADX INFO: loaded from: classes4.dex */
public class ShareSettings {

    public interface EnabledListener {
        void onEnabled(Preference preference, CustomSwitchPreference customSwitchPreference, boolean z);
    }

    public static void init(PreferenceScreen screen) {
        screen.findPreference(null);
    }

    public static Preference createLoginPref(Context context, Platforms which) {
        Preference ret = new Preference(context);
        ret.setKey(which.getLoginPrefKey());
        ret.setTitle(R.string.pref_login);
        ret.setWidgetLayoutResource(R.layout.layout_right_arrow);
        return ret;
    }

    public static CustomSwitchPreference createAutoPref(Context context, Platforms which) {
        CustomSwitchPreference ret = new CustomSwitchPreference(context);
        ret.setTitle(R.string.pref_auto_share);
        ret.setEnabled(false);
        ret.setDefaultValue(Boolean.FALSE);
        ret.setKey(which.getAutoSharePrefKey());
        ret.setWidgetLayoutResource(R.layout.switch_preference);
        return ret;
    }

    public static void refreshPrefs(Context context, PreferenceScreen screen, EnabledListener cb) {
        for (Platforms platform : Platforms.values()) {
            if (platform.getPrefCategory() != 0) {
                cb.onEnabled(screen.findPreference(platform.getLoginPrefKey()), (CustomSwitchPreference) screen.findPreference(platform.getAutoSharePrefKey()), platform.isLoggedIn(context));
            }
        }
    }

    public static boolean onLoginActivityResult() {
        return false;
    }
}
