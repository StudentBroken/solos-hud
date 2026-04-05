package com.twitter.sdk.android.core.models;

import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.models.MediaEntity;
import java.util.List;

/* JADX INFO: loaded from: classes62.dex */
public class Configuration {

    @SerializedName("dm_text_character_limit")
    public final int dmTextCharacterLimit;

    @SerializedName("non_username_paths")
    public final List<String> nonUsernamePaths;

    @SerializedName("photo_size_limit")
    public final long photoSizeLimit;

    @SerializedName("photo_sizes")
    public final MediaEntity.Sizes photoSizes;

    @SerializedName("short_url_length_https")
    public final int shortUrlLengthHttps;

    public Configuration(int dmTextCharacterLimit, List<String> nonUsernamePaths, long photoSizeLimit, MediaEntity.Sizes photoSizes, int shortUrlLengthHttps) {
        this.dmTextCharacterLimit = dmTextCharacterLimit;
        this.nonUsernamePaths = nonUsernamePaths;
        this.photoSizeLimit = photoSizeLimit;
        this.photoSizes = photoSizes;
        this.shortUrlLengthHttps = shortUrlLengthHttps;
    }
}
