package com.opentok.client;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import java.util.UUID;

/* JADX INFO: loaded from: classes15.dex */
public class DeviceInfo {
    private Context context;

    public DeviceInfo(Context applicationContext) {
        this.context = applicationContext;
    }

    public Context getApplicationContext() {
        return this.context;
    }

    public void setApplicationContext(Context context) {
        this.context = context;
    }

    public String getApplicationIdentifier() {
        return this.context.getPackageName();
    }

    public String getApplicationVersion() {
        try {
            return this.context.getPackageManager().getPackageInfo(this.context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "unknown";
        }
    }

    public String getSystemVersion() {
        return Integer.toString(Build.VERSION.SDK_INT);
    }

    public String getSystemName() {
        return "Android OS";
    }

    public String getDeviceModel() {
        return String.format("mfr=%s,model=%s,abi=%s", Build.MANUFACTURER, Build.MODEL, Build.CPU_ABI);
    }

    public String getSdkVersion() {
        return String.format(".%s-android", "e0064a9d1509d474ce90b86000204feaab6bfd1a".substring(0, 8));
    }

    public String getCarrierName() {
        return Build.BRAND;
    }

    public String getNetworkStatus() {
        return Build.VERSION.SDK_INT >= 14 ? Build.getRadioVersion() : Build.RADIO;
    }

    public String getOpenTokDeviceIdentifier() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("opentok", 0);
        if (prefs.getString("guid", null) == null) {
            prefs.edit().putString("guid", UUID.randomUUID().toString()).apply();
        }
        return prefs.getString("guid", null);
    }
}
