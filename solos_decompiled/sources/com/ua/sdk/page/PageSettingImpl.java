package com.ua.sdk.page;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

/* JADX INFO: loaded from: classes65.dex */
public class PageSettingImpl implements PageSetting {
    public static Parcelable.Creator<PageSettingImpl> CREATOR = new Parcelable.Creator<PageSettingImpl>() { // from class: com.ua.sdk.page.PageSettingImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PageSettingImpl createFromParcel(Parcel source) {
            return new PageSettingImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PageSettingImpl[] newArray(int size) {
            return new PageSettingImpl[size];
        }
    };

    @SerializedName("cta_link")
    String ctaLink;

    @SerializedName("cta_target")
    String ctaTarget;

    @SerializedName("cta_text")
    String ctaText;

    @SerializedName("featured_gallery_enabled")
    Boolean featuredGalleryEnabled;

    @SerializedName("qs_graph_enabled")
    Boolean qsGraphEnabled;

    @SerializedName("template")
    String template;

    public PageSettingImpl() {
    }

    public PageSettingImpl(Boolean featuredGalleryEnabled, Boolean qsGraphEnabled, String ctaText, String ctaLink, String ctaTarget, String template) {
        this.featuredGalleryEnabled = featuredGalleryEnabled;
        this.qsGraphEnabled = qsGraphEnabled;
        this.ctaText = ctaText;
        this.ctaLink = ctaLink;
        this.ctaTarget = ctaTarget;
        this.template = template;
    }

    @Override // com.ua.sdk.page.PageSetting
    public boolean isFeaturedGalleryEnabled() {
        if (this.featuredGalleryEnabled == null) {
            return false;
        }
        return this.featuredGalleryEnabled.booleanValue();
    }

    @Override // com.ua.sdk.page.PageSetting
    public Boolean getFeaturedGalleryEnabled() {
        return this.featuredGalleryEnabled;
    }

    @Override // com.ua.sdk.page.PageSetting
    public void setFeaturedGalleryEnabled(Boolean enabled) {
        this.featuredGalleryEnabled = enabled;
    }

    @Override // com.ua.sdk.page.PageSetting
    public boolean isQsGraphEnabled() {
        if (this.qsGraphEnabled == null) {
            return false;
        }
        return this.qsGraphEnabled.booleanValue();
    }

    @Override // com.ua.sdk.page.PageSetting
    public Boolean getQsGraphEnabled() {
        return this.featuredGalleryEnabled;
    }

    @Override // com.ua.sdk.page.PageSetting
    public void setQsGraphEnabled(Boolean enabled) {
        this.qsGraphEnabled = enabled;
    }

    @Override // com.ua.sdk.page.PageSetting
    public String getCtaText() {
        return this.ctaText;
    }

    @Override // com.ua.sdk.page.PageSetting
    public void setCtaText(String ctaText) {
        this.ctaText = ctaText;
    }

    @Override // com.ua.sdk.page.PageSetting
    public String getCtaLink() {
        return this.ctaLink;
    }

    @Override // com.ua.sdk.page.PageSetting
    public void setCtaLink(String ctaLink) {
        this.ctaLink = ctaLink;
    }

    @Override // com.ua.sdk.page.PageSetting
    public String getCtaTarget() {
        return this.ctaTarget;
    }

    @Override // com.ua.sdk.page.PageSetting
    public void setCtaTarget(String ctaTarget) {
        this.ctaTarget = ctaTarget;
    }

    @Override // com.ua.sdk.page.PageSetting
    public String getTemplate() {
        return this.template;
    }

    @Override // com.ua.sdk.page.PageSetting
    public void setTemplate(String template) {
        this.template = template;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.featuredGalleryEnabled);
        dest.writeValue(this.qsGraphEnabled);
        dest.writeString(this.ctaText);
        dest.writeString(this.ctaLink);
        dest.writeString(this.ctaTarget);
        dest.writeString(this.template);
    }

    private PageSettingImpl(Parcel in) {
        this.featuredGalleryEnabled = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.qsGraphEnabled = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.ctaText = in.readString();
        this.ctaLink = in.readString();
        this.ctaTarget = in.readString();
        this.template = in.readString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PageSettingImpl)) {
            return false;
        }
        PageSettingImpl pageSetting = (PageSettingImpl) o;
        if (this.featuredGalleryEnabled == null ? pageSetting.featuredGalleryEnabled != null : !this.featuredGalleryEnabled.equals(pageSetting.featuredGalleryEnabled)) {
            return false;
        }
        if (this.qsGraphEnabled == null ? pageSetting.qsGraphEnabled != null : !this.qsGraphEnabled.equals(pageSetting.qsGraphEnabled)) {
            return false;
        }
        if (this.ctaText == null ? pageSetting.ctaText != null : !this.ctaText.equals(pageSetting.ctaText)) {
            return false;
        }
        if (this.ctaLink == null ? pageSetting.ctaLink != null : !this.ctaLink.equals(pageSetting.ctaLink)) {
            return false;
        }
        if (this.ctaTarget == null ? pageSetting.ctaTarget != null : !this.ctaTarget.equals(pageSetting.ctaTarget)) {
            return false;
        }
        if (this.template != null) {
            if (this.template.equals(pageSetting.template)) {
                return true;
            }
        } else if (pageSetting.template == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = this.featuredGalleryEnabled != null ? this.featuredGalleryEnabled.hashCode() : 0;
        return (((((((((result * 31) + (this.qsGraphEnabled != null ? this.qsGraphEnabled.hashCode() : 0)) * 31) + (this.ctaText != null ? this.ctaText.hashCode() : 0)) * 31) + (this.ctaLink != null ? this.ctaLink.hashCode() : 0)) * 31) + (this.ctaTarget != null ? this.ctaTarget.hashCode() : 0)) * 31) + (this.template != null ? this.template.hashCode() : 0);
    }
}
