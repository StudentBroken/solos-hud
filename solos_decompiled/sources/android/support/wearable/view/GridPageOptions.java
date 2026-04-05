package android.support.wearable.view;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(20)
@Deprecated
public interface GridPageOptions {

    public interface BackgroundListener {
        void notifyBackgroundChanged();
    }

    Drawable getBackground();

    void setBackgroundListener(BackgroundListener backgroundListener);
}
