package android.support.v4.media;

import android.media.browse.MediaBrowser;
import android.support.annotation.RequiresApi;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/* JADX INFO: loaded from: classes58.dex */
@RequiresApi(21)
class ParceledListSliceAdapterApi21 {
    private static Constructor sConstructor;

    ParceledListSliceAdapterApi21() {
    }

    static {
        try {
            sConstructor = Class.forName("android.content.pm.ParceledListSlice").getConstructor(List.class);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    static Object newInstance(List<MediaBrowser.MediaItem> itemList) {
        try {
            Object result = sConstructor.newInstance(itemList);
            return result;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}
