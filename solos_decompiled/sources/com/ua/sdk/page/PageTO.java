package com.ua.sdk.page;

import com.facebook.share.internal.ShareConstants;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.UaException;
import com.ua.sdk.UaLog;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.ImageUrlImpl;
import com.ua.sdk.location.LocationImpl;
import java.net.URI;
import java.net.URISyntaxException;

/* JADX INFO: loaded from: classes65.dex */
public class PageTO extends ApiTransferObject {

    @SerializedName("alias")
    String alias;

    @SerializedName("cover_photo")
    Photo coverPhoto;

    @SerializedName("description")
    String description;

    @SerializedName("headline")
    String headline;

    @SerializedName("location")
    LocationImpl location;

    @SerializedName("profile_photo")
    Photo profilePhoto;

    @SerializedName("settings")
    PageSettingImpl setting;

    @SerializedName("title")
    String title;

    @SerializedName("url")
    String url;

    @SerializedName("website")
    String website;

    public static PageImpl toPageImpl(PageTO obj) throws UaException {
        String coverTemplate;
        String profileTemplate;
        if (obj == null) {
            return null;
        }
        PageImpl page = new PageImpl();
        if (obj.website != null) {
            try {
                page.setWebsite(new URI(obj.website));
            } catch (URISyntaxException e) {
                UaLog.error("Website could not put into URI Object: " + obj.website, (Throwable) e);
            }
        }
        page.setPageDescription(obj.description);
        page.setTitle(obj.title);
        if (obj.url != null) {
            try {
                page.setUrl(new URI(obj.url));
            } catch (URISyntaxException e2) {
                UaLog.error("Url could not put into URI Object: " + obj.url, (Throwable) e2);
            }
        }
        page.setAlias(obj.alias);
        page.setLocation(obj.location);
        if (obj.profilePhoto != null && (profileTemplate = obj.profilePhoto.template) != null) {
            page.setProfilePhoto(ImageUrlImpl.getBuilder().setUri(obj.profilePhoto.uri).setTemplate(profileTemplate).build());
        }
        if (obj.coverPhoto != null && (coverTemplate = obj.coverPhoto.template) != null) {
            page.setCoverPhoto(ImageUrlImpl.getBuilder().setUri(obj.coverPhoto.uri).setTemplate(coverTemplate).build());
        }
        page.setHeadline(obj.headline);
        page.setPageSetting(obj.setting);
        for (String key : obj.getLinkKeys()) {
            page.setLinksForRelation(key, obj.getLinks(key));
        }
        return page;
    }

    static class Photo {

        @SerializedName("template")
        String template;

        @SerializedName(ShareConstants.MEDIA_URI)
        String uri;

        Photo() {
        }
    }
}
