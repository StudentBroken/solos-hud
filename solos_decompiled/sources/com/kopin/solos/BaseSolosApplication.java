package com.kopin.solos;

import android.os.Process;
import android.support.multidex.MultiDexApplication;
import com.kopin.solos.common.config.Config;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.settings.ConfigMetrics;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.settings.UserProfile;
import java.io.IOException;

/* JADX INFO: loaded from: classes37.dex */
public abstract class BaseSolosApplication extends MultiDexApplication {
    HardwareReceiverService mService;

    abstract boolean isDefaultModeActive();

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        if (Config.DEBUG) {
            int pid = Process.myPid();
            String whiteList = "logcat -P '" + pid + "'";
            try {
                Runtime.getRuntime().exec(whiteList).waitFor();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
        }
        Prefs.init(getApplicationContext());
        ConfigMetrics.init();
        SQLHelper.init(getApplicationContext());
        UserProfile.init(getApplicationContext());
        Prefs.initSingleMetrics();
    }
}
