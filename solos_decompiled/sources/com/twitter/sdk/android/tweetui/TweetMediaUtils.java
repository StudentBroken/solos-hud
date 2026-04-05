package com.twitter.sdk.android.tweetui;

import android.os.Build;
import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.VideoInfo;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes9.dex */
final class TweetMediaUtils {
    private static final String CONTENT_TYPE_MP4 = "video/mp4";
    private static final String CONTENT_TYPE_WEBM = "video/webm";
    public static final String GIF_TYPE = "animated_gif";
    public static final String PHOTO_TYPE = "photo";
    public static final String VIDEO_TYPE = "video";

    private TweetMediaUtils() {
    }

    static MediaEntity getPhotoEntity(Tweet tweet) {
        List<MediaEntity> mediaEntityList = getAllMediaEntities(tweet);
        for (int i = mediaEntityList.size() - 1; i >= 0; i--) {
            MediaEntity entity = mediaEntityList.get(i);
            if (entity.type != null && isPhotoType(entity)) {
                return entity;
            }
        }
        return null;
    }

    static boolean hasPhoto(Tweet tweet) {
        return getPhotoEntity(tweet) != null;
    }

    static MediaEntity getVideoEntity(Tweet tweet) {
        for (MediaEntity mediaEntity : getAllMediaEntities(tweet)) {
            if (mediaEntity.type != null && isVideoType(mediaEntity)) {
                return mediaEntity;
            }
        }
        return null;
    }

    static boolean hasVideo(Tweet tweet) {
        return getVideoEntity(tweet) != null;
    }

    static boolean isPhotoType(MediaEntity mediaEntity) {
        return "photo".equals(mediaEntity.type);
    }

    static boolean isVideoType(MediaEntity mediaEntity) {
        return "video".equals(mediaEntity.type) || GIF_TYPE.equals(mediaEntity.type);
    }

    static VideoInfo.Variant getSupportedVariant(MediaEntity mediaEntity) {
        for (VideoInfo.Variant variant : mediaEntity.videoInfo.variants) {
            if (isVariantSupported(variant)) {
                return variant;
            }
        }
        return null;
    }

    static boolean isLooping(MediaEntity mediaEntity) {
        return GIF_TYPE.equals(mediaEntity.type);
    }

    static boolean isVariantSupported(VideoInfo.Variant variant) {
        if (CONTENT_TYPE_MP4.equals(variant.contentType)) {
            return true;
        }
        return Build.VERSION.SDK_INT >= 19 && CONTENT_TYPE_WEBM.equals(variant.contentType);
    }

    static List<MediaEntity> getAllMediaEntities(Tweet tweet) {
        List<MediaEntity> entities = new ArrayList<>();
        if (tweet.entities != null && tweet.entities.media != null) {
            entities.addAll(tweet.entities.media);
        }
        if (tweet.extendedEtities != null && tweet.extendedEtities.media != null) {
            entities.addAll(tweet.extendedEtities.media);
        }
        return entities;
    }
}
