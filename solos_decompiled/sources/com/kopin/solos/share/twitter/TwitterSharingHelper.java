package com.kopin.solos.share.twitter;

import com.kopin.solos.share.Platforms;
import com.kopin.solos.storage.SQLHelper;

/* JADX INFO: loaded from: classes4.dex */
public class TwitterSharingHelper {
    private static String mTwitterId = null;

    public static String getTwitterId() {
        return mTwitterId;
    }

    public static void setTwitterId(String id) {
        mTwitterId = id;
    }

    public static boolean isShared(long rideId) {
        return SQLHelper.isRideShared(rideId, Platforms.Twitter.getSharedKey(), mTwitterId);
    }
}
