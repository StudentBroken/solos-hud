package com.kopin.solos;

import com.kopin.groupcom.GroupCom;
import com.kopin.solos.config.ConfigHelper;
import com.kopin.solos.share.Config;
import com.kopin.solos.share.strava.StravaSync;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.wear.WearMessenger;

/* JADX INFO: loaded from: classes24.dex */
public class SolosApplication extends BaseSolosApplication {
    @Override // com.kopin.solos.BaseSolosApplication, android.app.Application
    public void onCreate() {
        Config.init(BuildConfig.BUILD_FLAVOUR);
        super.onCreate();
        getApplicationContext().setTheme(ThemeUtil.getTheme(Prefs.getSportType()));
        GroupCom.init(getApplicationContext());
        if (Config.DEBUG_ALLOW_CONFIG_OVERRIDE) {
            ConfigHelper.init(getApplicationContext());
        }
        StravaSync.init(getApplicationContext());
        WearMessenger.init(getApplicationContext());
        StethoUtil.initialise(this);
    }

    @Override // com.kopin.solos.BaseSolosApplication
    protected boolean isDefaultModeActive() {
        return !Prefs.isWatchMode();
    }
}
