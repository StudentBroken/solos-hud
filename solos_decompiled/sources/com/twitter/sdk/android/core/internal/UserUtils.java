package com.twitter.sdk.android.core.internal;

import android.text.TextUtils;
import com.twitter.sdk.android.core.models.User;

/* JADX INFO: loaded from: classes62.dex */
public final class UserUtils {
    private UserUtils() {
    }

    public enum AvatarSize {
        NORMAL("_normal"),
        BIGGER("_bigger"),
        MINI("_mini"),
        ORIGINAL("_original"),
        REASONABLY_SMALL("_reasonably_small");

        private final String suffix;

        AvatarSize(String suffix) {
            this.suffix = suffix;
        }

        String getSuffix() {
            return this.suffix;
        }
    }

    public static String getProfileImageUrlHttps(User user, AvatarSize size) {
        if (user != null && user.profileImageUrlHttps != null) {
            String url = user.profileImageUrlHttps;
            if (size == null || url == null) {
                return url;
            }
            switch (size) {
            }
            return url;
        }
        return null;
    }

    public static CharSequence formatScreenName(CharSequence screenName) {
        if (TextUtils.isEmpty(screenName)) {
            return "";
        }
        return screenName.charAt(0) != '@' ? "@" + ((Object) screenName) : screenName;
    }
}
