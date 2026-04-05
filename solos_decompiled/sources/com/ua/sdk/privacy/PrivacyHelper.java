package com.ua.sdk.privacy;

import com.ua.sdk.UaLog;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.net.v7.UrlBuilderImpl;
import com.ua.sdk.privacy.Privacy;

/* JADX INFO: loaded from: classes65.dex */
public class PrivacyHelper {
    public static final String DESC_FRIENDS = "Friends. Share With All My Friend";
    public static final String DESC_PRIVATE = "Private. Do Not Share";
    public static final String DESC_PUBLIC = "Public. Share With Everyone";

    public static PrivacyImpl getPrivacy(Privacy.Level level) {
        switch (level) {
            case PRIVATE:
                return new PrivacyImpl(Privacy.Level.PRIVATE, DESC_PRIVATE);
            case FRIENDS:
                return new PrivacyImpl(Privacy.Level.FRIENDS, DESC_FRIENDS);
            case PUBLIC:
                return new PrivacyImpl(Privacy.Level.PUBLIC, DESC_PUBLIC);
            default:
                UaLog.error("This state is not supported.");
                return null;
        }
    }

    public static Privacy.Level getLevelFromId(int id) {
        switch (id) {
            case 0:
                return Privacy.Level.PRIVATE;
            case 1:
                return Privacy.Level.FRIENDS;
            case 2:
            default:
                UaLog.error("This ID is not supported.");
                return null;
            case 3:
                return Privacy.Level.PUBLIC;
        }
    }

    public static Privacy getPrivacyFromId(int id) {
        Privacy.Level state = getLevelFromId(id);
        if (state != null) {
            return getPrivacy(state);
        }
        return null;
    }

    public static Link toLink(Privacy.Level level, String settingName) {
        return new Link(String.format(UrlBuilderImpl.GET_PRIVACY_URL, Integer.valueOf(level.id)), String.valueOf(level.id), settingName);
    }

    public static Link toLink(Privacy.Level level) {
        return new Link(String.format(UrlBuilderImpl.GET_PRIVACY_URL, Integer.valueOf(level.id)), String.valueOf(level.id));
    }
}
