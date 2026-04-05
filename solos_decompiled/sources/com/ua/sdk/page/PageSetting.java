package com.ua.sdk.page;

import android.os.Parcelable;

/* JADX INFO: loaded from: classes65.dex */
public interface PageSetting extends Parcelable {
    String getCtaLink();

    String getCtaTarget();

    String getCtaText();

    Boolean getFeaturedGalleryEnabled();

    Boolean getQsGraphEnabled();

    String getTemplate();

    boolean isFeaturedGalleryEnabled();

    boolean isQsGraphEnabled();

    void setCtaLink(String str);

    void setCtaTarget(String str);

    void setCtaText(String str);

    void setFeaturedGalleryEnabled(Boolean bool);

    void setQsGraphEnabled(Boolean bool);

    void setTemplate(String str);
}
