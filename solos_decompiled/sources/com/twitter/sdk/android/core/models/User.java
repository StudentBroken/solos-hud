package com.twitter.sdk.android.core.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/* JADX INFO: loaded from: classes62.dex */
public class User implements Serializable, Identifiable {
    public static final long INVALID_ID = -1;
    private static final long serialVersionUID = 4663450696842173958L;

    @SerializedName("contributors_enabled")
    public final boolean contributorsEnabled;

    @SerializedName("created_at")
    public final String createdAt;

    @SerializedName("default_profile")
    public final boolean defaultProfile;

    @SerializedName("default_profile_image")
    public final boolean defaultProfileImage;

    @SerializedName("description")
    public final String description;

    @SerializedName("email")
    public final String email;

    @SerializedName("entities")
    public final UserEntities entities;

    @SerializedName("favourites_count")
    public final int favouritesCount;

    @SerializedName("follow_request_sent")
    public final boolean followRequestSent;

    @SerializedName("followers_count")
    public final int followersCount;

    @SerializedName("friends_count")
    public final int friendsCount;

    @SerializedName("geo_enabled")
    public final boolean geoEnabled;

    @SerializedName("id")
    public final long id;

    @SerializedName("id_str")
    public final String idStr;

    @SerializedName("is_translator")
    public final boolean isTranslator;

    @SerializedName("lang")
    public final String lang;

    @SerializedName("listed_count")
    public final int listedCount;

    @SerializedName("location")
    public final String location;

    @SerializedName("name")
    public final String name;

    @SerializedName("profile_background_color")
    public final String profileBackgroundColor;

    @SerializedName("profile_background_image_url")
    public final String profileBackgroundImageUrl;

    @SerializedName("profile_background_image_url_https")
    public final String profileBackgroundImageUrlHttps;

    @SerializedName("profile_background_tile")
    public final boolean profileBackgroundTile;

    @SerializedName("profile_banner_url")
    public final String profileBannerUrl;

    @SerializedName("profile_image_url")
    public final String profileImageUrl;

    @SerializedName("profile_image_url_https")
    public final String profileImageUrlHttps;

    @SerializedName("profile_link_color")
    public final String profileLinkColor;

    @SerializedName("profile_sidebar_border_color")
    public final String profileSidebarBorderColor;

    @SerializedName("profile_sidebar_fill_color")
    public final String profileSidebarFillColor;

    @SerializedName("profile_text_color")
    public final String profileTextColor;

    @SerializedName("profile_use_background_image")
    public final boolean profileUseBackgroundImage;

    @SerializedName("protected")
    public final boolean protectedUser;

    @SerializedName("screen_name")
    public final String screenName;

    @SerializedName("show_all_inline_media")
    public final boolean showAllInlineMedia;

    @SerializedName("status")
    public final Tweet status;

    @SerializedName("statuses_count")
    public final int statusesCount;

    @SerializedName("time_zone")
    public final String timeZone;

    @SerializedName("url")
    public final String url;

    @SerializedName("utc_offset")
    public final int utcOffset;

    @SerializedName("verified")
    public final boolean verified;

    @SerializedName("withheld_in_countries")
    public final String withheldInCountries;

    @SerializedName("withheld_scope")
    public final String withheldScope;

    public User(boolean contributorsEnabled, String createdAt, boolean defaultProfile, boolean defaultProfileImage, String description, String emailAddress, UserEntities entities, int favouritesCount, boolean followRequestSent, int followersCount, int friendsCount, boolean geoEnabled, long id, String idStr, boolean isTranslator, String lang, int listedCount, String location, String name, String profileBackgroundColor, String profileBackgroundImageUrl, String profileBackgroundImageUrlHttps, boolean profileBackgroundTile, String profileBannerUrl, String profileImageUrl, String profileImageUrlHttps, String profileLinkColor, String profileSidebarBorderColor, String profileSidebarFillColor, String profileTextColor, boolean profileUseBackgroundImage, boolean protectedUser, String screenName, boolean showAllInlineMedia, Tweet status, int statusesCount, String timeZone, String url, int utcOffset, boolean verified, String withheldInCountries, String withheldScope) {
        this.contributorsEnabled = contributorsEnabled;
        this.createdAt = createdAt;
        this.defaultProfile = defaultProfile;
        this.defaultProfileImage = defaultProfileImage;
        this.description = description;
        this.email = emailAddress;
        this.entities = entities;
        this.favouritesCount = favouritesCount;
        this.followRequestSent = followRequestSent;
        this.followersCount = followersCount;
        this.friendsCount = friendsCount;
        this.geoEnabled = geoEnabled;
        this.id = id;
        this.idStr = idStr;
        this.isTranslator = isTranslator;
        this.lang = lang;
        this.listedCount = listedCount;
        this.location = location;
        this.name = name;
        this.profileBackgroundColor = profileBackgroundColor;
        this.profileBackgroundImageUrl = profileBackgroundImageUrl;
        this.profileBackgroundImageUrlHttps = profileBackgroundImageUrlHttps;
        this.profileBackgroundTile = profileBackgroundTile;
        this.profileBannerUrl = profileBannerUrl;
        this.profileImageUrl = profileImageUrl;
        this.profileImageUrlHttps = profileImageUrlHttps;
        this.profileLinkColor = profileLinkColor;
        this.profileSidebarBorderColor = profileSidebarBorderColor;
        this.profileSidebarFillColor = profileSidebarFillColor;
        this.profileTextColor = profileTextColor;
        this.profileUseBackgroundImage = profileUseBackgroundImage;
        this.protectedUser = protectedUser;
        this.screenName = screenName;
        this.showAllInlineMedia = showAllInlineMedia;
        this.status = status;
        this.statusesCount = statusesCount;
        this.timeZone = timeZone;
        this.url = url;
        this.utcOffset = utcOffset;
        this.verified = verified;
        this.withheldInCountries = withheldInCountries;
        this.withheldScope = withheldScope;
    }

    @Override // com.twitter.sdk.android.core.models.Identifiable
    public long getId() {
        return this.id;
    }
}
