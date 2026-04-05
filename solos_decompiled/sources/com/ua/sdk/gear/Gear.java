package com.ua.sdk.gear;

import android.os.Parcelable;
import com.ua.sdk.Entity;

/* JADX INFO: loaded from: classes65.dex */
public interface Gear extends Entity, Parcelable {
    String getAgeGroup();

    Boolean getAvailable();

    String getBrand();

    String getCategory();

    String getColor();

    String getDepartment();

    String getDescription();

    String getDetailPhotoUrl();

    String getGender();

    String getKeywords();

    String getMidLevelProductType();

    String getModel();

    String getMsrp();

    String getPhotoUrl();

    String getPrice();

    String getProductType();

    String getProductUrl();

    String getPurchaseUrl();

    String getSize();

    String getSku();

    String getSource();

    String getStyleId();

    String getStyleNumber();

    String getThumbnailUrl();

    Long getUpc();
}
