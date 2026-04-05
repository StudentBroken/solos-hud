package com.twitter.sdk.android.core.models;

import com.google.gson.annotations.SerializedName;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes62.dex */
public class UserEntities {

    @SerializedName("description")
    public final UrlEntities description;

    @SerializedName("url")
    public final UrlEntities url;

    public UserEntities(UrlEntities url, UrlEntities description) {
        this.url = url;
        this.description = description;
    }

    public static class UrlEntities {

        @SerializedName("urls")
        public final List<UrlEntity> urls;

        public UrlEntities(List<UrlEntity> urls) {
            this.urls = getSafeList(urls);
        }

        private <T> List<T> getSafeList(List<T> entities) {
            return entities == null ? Collections.EMPTY_LIST : Collections.unmodifiableList(entities);
        }
    }
}
