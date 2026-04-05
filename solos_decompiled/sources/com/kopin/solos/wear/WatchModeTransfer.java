package com.kopin.solos.wear;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;
import com.kopin.peloton.PelotonPrefs;
import com.kopin.pupil.PupilDevice;
import com.kopin.solos.sensors.SensorList;
import com.kopin.solos.storage.settings.Prefs;

/* JADX INFO: loaded from: classes24.dex */
public class WatchModeTransfer {
    private static final String TAG = "WatchModeTransfer";

    public interface WatchTransferStateCallback {
        void result(WatchTransferState watchTransferState);
    }

    public static void prepareData(WearData wearData, Context context) {
        String token = PelotonPrefs.getToken();
        String refreshToken = PelotonPrefs.getRefreshToken();
        String email = PelotonPrefs.getEmail();
        if (token != null && email != null) {
            Log.i(TAG, "prepare tokens");
            wearData.cloudToken = token;
            wearData.cloudRefreshToken = refreshToken;
            wearData.userEmail = email;
        }
        if (PupilDevice.getDevice() != null && PupilDevice.getDevice().getAddress() != null) {
            wearData.headsetMacAddress = PupilDevice.getDevice().getAddress();
            Log.e(TAG, "head mac = " + PupilDevice.getDevice().getAddress());
        } else {
            Log.e(TAG, "*** No head mac");
        }
        wearData.sensors.clear();
        wearData.sensors.addAll(SensorList.getSavedList());
        wearData.setFromPrefs(PreferenceManager.getDefaultSharedPreferences(context).getAll());
        wearData.metricMode = Boolean.valueOf(Prefs.isMetric());
        Log.i(TAG, "for transfer: wear data");
        Log.i(TAG, wearData.toString());
    }
}
