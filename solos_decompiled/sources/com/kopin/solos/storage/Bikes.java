package com.kopin.solos.storage;

import com.kopin.solos.storage.settings.Prefs;
import java.util.List;

/* JADX INFO: loaded from: classes54.dex */
public class Bikes {
    public static List<Bike> getActiveBikes() {
        return SQLHelper.getActiveBikes();
    }

    public static void selectBike(int position) {
        Bike bike = getActiveBikes().get(position);
        if (bike != null) {
            Prefs.setChosenBikeId(bike.getId());
            float wheelSizeInMtr = SQLHelper.getBike(bike.getId()).getWheelSize() / 1000.0f;
            Prefs.setWheelSize(wheelSizeInMtr);
        }
    }
}
