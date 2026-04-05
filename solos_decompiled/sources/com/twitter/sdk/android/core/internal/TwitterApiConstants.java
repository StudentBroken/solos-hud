package com.twitter.sdk.android.core.internal;

/* JADX INFO: loaded from: classes62.dex */
public class TwitterApiConstants {
    public static final int MAX_TWEET_CHARS = 140;

    public static class Base {
        public static final String FIELD_ID = "id";
        public static final String PARAM_ID = "id";
    }

    public static class Errors extends Base {
        public static final int ALREADY_FAVORITED = 139;
        public static final int ALREADY_UNFAVORITED = 144;
        public static final int APP_AUTH_ERROR_CODE = 89;
        public static final String ERRORS = "errors";
        public static final int GUEST_AUTH_ERROR_CODE = 239;
        public static final int LEGACY_ERROR = 0;
    }
}
