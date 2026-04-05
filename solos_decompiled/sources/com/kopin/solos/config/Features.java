package com.kopin.solos.config;

import com.kopin.pupil.SolosDevice;
import com.kopin.solos.common.config.Config;
import com.kopin.solos.storage.settings.Prefs;

/* JADX INFO: loaded from: classes37.dex */
public class Features {
    public static boolean IS_NAVIGATION_ENABLED = false;
    public static boolean IS_GROUPCHAT_ENABLED = false;
    public static boolean IS_WEARAPP_ENABLED = false;
    public static boolean IS_SPORT_CHANGE_ENABLED = false;
    public static boolean IS_TRAINING_ENABLED = false;

    public static void updateForProduct(SolosDevice.Product product, boolean isConnected, boolean isWear) {
        switch (product) {
            case HDK:
                IS_NAVIGATION_ENABLED = false;
                IS_GROUPCHAT_ENABLED = false;
                IS_SPORT_CHANGE_ENABLED = false;
                IS_WEARAPP_ENABLED = true;
                break;
            case SOLOS:
                Config.VOCON_ENABLED = Config.DEBUG;
                IS_NAVIGATION_ENABLED = Config.DEBUG;
                IS_GROUPCHAT_ENABLED = Config.DEBUG && isConnected;
                IS_SPORT_CHANGE_ENABLED = Config.MULTI_SPORT_ENABLED || Prefs.isMultiSportSupported();
                IS_WEARAPP_ENABLED = Config.DEBUG;
                IS_TRAINING_ENABLED = Config.DEBUG || Prefs.isMultiSportSupported();
                break;
            case SOLOS2:
                Config.VOCON_ENABLED = Config.DEBUG || (isConnected && !isWear);
                IS_NAVIGATION_ENABLED = isConnected || Config.DEBUG;
                if ((isConnected || Config.DEBUG) && !isWear) {
                    z = true;
                }
                IS_GROUPCHAT_ENABLED = z;
                Prefs.setMultiSportSupported(true);
                IS_SPORT_CHANGE_ENABLED = true;
                IS_WEARAPP_ENABLED = true;
                IS_TRAINING_ENABLED = true;
                break;
            default:
                Config.VOCON_ENABLED = Config.DEBUG;
                IS_NAVIGATION_ENABLED = Config.DEBUG;
                IS_GROUPCHAT_ENABLED = false;
                IS_SPORT_CHANGE_ENABLED = Config.MULTI_SPORT_ENABLED || Prefs.isMultiSportSupported();
                IS_WEARAPP_ENABLED = false;
                IS_TRAINING_ENABLED = Config.DEBUG || Prefs.isMultiSportSupported();
                break;
        }
    }
}
