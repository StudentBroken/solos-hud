package com.kopin.solos.util;

import android.content.Context;
import com.kopin.solos.core.R;

/* JADX INFO: loaded from: classes37.dex */
public class BikeTypeHelper {
    public static String getBikeTypeName(Context context, int bikeType) {
        String[] bikes = context.getResources().getStringArray(R.array.bike_types);
        return (bikes == null || bikes.length <= bikeType || bikeType < 0) ? context.getResources().getString(R.string.selection_none) : bikes[bikeType];
    }
}
