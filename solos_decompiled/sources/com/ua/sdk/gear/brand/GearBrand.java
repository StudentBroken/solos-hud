package com.ua.sdk.gear.brand;

import android.os.Parcelable;
import com.ua.sdk.Entity;

/* JADX INFO: loaded from: classes65.dex */
public interface GearBrand extends Entity, Parcelable {
    String getBrandName();

    String getGearTypeId();

    Boolean isPopular();
}
