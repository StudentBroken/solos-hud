package com.twitter.sdk.android.core.models;

import com.facebook.share.internal.ShareConstants;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/* JADX INFO: loaded from: classes62.dex */
public class MediaEntity extends UrlEntity {

    @SerializedName("id")
    public final long id;

    @SerializedName("id_str")
    public final String idStr;

    @SerializedName("media_url")
    public final String mediaUrl;

    @SerializedName("media_url_https")
    public final String mediaUrlHttps;

    @SerializedName("sizes")
    public final Sizes sizes;

    @SerializedName("source_status_id")
    public final long sourceStatusId;

    @SerializedName("source_status_id_str")
    public final String sourceStatusIdStr;

    @SerializedName(ShareConstants.MEDIA_TYPE)
    public final String type;

    @SerializedName("video_info")
    public final VideoInfo videoInfo;

    @Deprecated
    public MediaEntity(String url, String expandedUrl, String displayUrl, int start, int end, long id, String idStr, String mediaUrl, String mediaUrlHttps, Sizes sizes, long sourceStatusId, String sourceStatusIdStr, String type) {
        this(url, expandedUrl, displayUrl, start, end, id, idStr, mediaUrl, mediaUrlHttps, sizes, sourceStatusId, sourceStatusIdStr, type, null);
    }

    public MediaEntity(String url, String expandedUrl, String displayUrl, int start, int end, long id, String idStr, String mediaUrl, String mediaUrlHttps, Sizes sizes, long sourceStatusId, String sourceStatusIdStr, String type, VideoInfo videoInfo) {
        super(url, expandedUrl, displayUrl, start, end);
        this.id = id;
        this.idStr = idStr;
        this.mediaUrl = mediaUrl;
        this.mediaUrlHttps = mediaUrlHttps;
        this.sizes = sizes;
        this.sourceStatusId = sourceStatusId;
        this.sourceStatusIdStr = sourceStatusIdStr;
        this.type = type;
        this.videoInfo = videoInfo;
    }

    public static class Sizes implements Serializable {

        @SerializedName("large")
        public final Size large;

        @SerializedName("medium")
        public final Size medium;

        @SerializedName("small")
        public final Size small;

        @SerializedName("thumb")
        public final Size thumb;

        public Sizes(Size thumb, Size small, Size medium, Size large) {
            this.thumb = thumb;
            this.small = small;
            this.medium = medium;
            this.large = large;
        }
    }

    public static class Size implements Serializable {

        @SerializedName("h")
        public final int h;

        @SerializedName("resize")
        public final String resize;

        @SerializedName("w")
        public final int w;

        public Size(int w, int h, String resize) {
            this.w = w;
            this.h = h;
            this.resize = resize;
        }
    }
}
