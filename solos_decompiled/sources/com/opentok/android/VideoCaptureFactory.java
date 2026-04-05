package com.opentok.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import com.opentok.android.Publisher;
import com.opentok.android.Session;

/* JADX INFO: loaded from: classes15.dex */
class VideoCaptureFactory {
    private static String TAG = VideoCaptureFactory.class.getSimpleName();
    private static boolean camera2Enabled;

    VideoCaptureFactory() {
    }

    static {
        boolean z = false;
        camera2Enabled = false;
        Session.SessionOptions options = new Session.SessionOptions() { // from class: com.opentok.android.VideoCaptureFactory.1
        };
        if (Build.VERSION.SDK_INT >= 21 && options.isCamera2Capable()) {
            z = true;
        }
        camera2Enabled = z;
    }

    @SuppressLint({"LogNotTimber"})
    public static BaseVideoCapturer constructCapturer(Context context, Publisher.CameraCaptureResolution resolution, Publisher.CameraCaptureFrameRate frameRate) {
        if (isCamera2Capable()) {
            Log.i(TAG, "Using Camera2 Capturer");
            return new Camera2VideoCapturer(context, resolution, frameRate);
        }
        Log.i(TAG, "Using Camera Capturer");
        return new DefaultVideoCapturer(context, resolution, frameRate);
    }

    public static BaseVideoCapturer constructCapturer(Context context) {
        return constructCapturer(context, Publisher.CameraCaptureResolution.defaultResolution(), Publisher.CameraCaptureFrameRate.defaultFrameRate());
    }

    public static void enableCamera2api(boolean enable) {
        camera2Enabled = enable;
    }

    private static boolean isCamera2Capable() {
        return camera2Enabled;
    }
}
