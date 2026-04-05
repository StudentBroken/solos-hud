package com.opentok.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;

/* JADX INFO: loaded from: classes15.dex */
public class VideoRenderFactory {
    private static String TAG = VideoCaptureFactory.class.getSimpleName();
    private static boolean useTextureViews = false;

    @SuppressLint({"LogNotTimber"})
    public static BaseVideoRenderer constructRenderer(Context context) {
        if (useTextureViews && Build.VERSION.SDK_INT >= 19) {
            Log.i(TAG, "Using TextureViews for render");
            return new TextureViewRenderer(context);
        }
        Log.i(TAG, "Using GLSurfaceViews for render");
        return new DefaultVideoRenderer(context);
    }

    public static void useTextureViews(boolean useTextureViews2) {
        useTextureViews = useTextureViews2;
    }
}
