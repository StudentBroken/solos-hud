package android.support.v4.media.session;

import android.media.session.MediaSession;
import android.support.annotation.RequiresApi;

/* JADX INFO: loaded from: classes58.dex */
@RequiresApi(22)
class MediaSessionCompatApi22 {
    MediaSessionCompatApi22() {
    }

    public static void setRatingType(Object sessionObj, int type) {
        ((MediaSession) sessionObj).setRatingType(type);
    }
}
