package android.support.wearable.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.wearable.internal.SharedLibraryVersion;
import android.util.Log;
import com.google.android.wearable.compat.WearableActivityController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(21)
public final class WearableActivityDelegate {
    private static volatile boolean sAmbientCallbacksVerifiedPresent;
    private static boolean sHasAutoResumeEnabledMethod;
    private static boolean sInitAutoResumeEnabledMethod;
    private final AmbientCallback mCallback;
    private WearableActivityController mWearableController;

    public interface AmbientCallback {
        void onEnterAmbient(Bundle bundle);

        void onExitAmbient();

        void onUpdateAmbient();
    }

    public WearableActivityDelegate(AmbientCallback callback) {
        this.mCallback = callback;
    }

    public void onCreate(Activity activity) {
        initAmbientSupport(activity);
        if (this.mWearableController != null) {
            this.mWearableController.onCreate();
        }
    }

    public void onResume() {
        if (this.mWearableController != null) {
            this.mWearableController.onResume();
        }
    }

    public void onPause() {
        if (this.mWearableController != null) {
            this.mWearableController.onPause();
        }
    }

    public void onStop() {
        if (this.mWearableController != null) {
            this.mWearableController.onStop();
        }
    }

    public void onDestroy() {
        if (this.mWearableController != null) {
            this.mWearableController.onDestroy();
        }
    }

    public void setAmbientEnabled() {
        if (this.mWearableController != null) {
            this.mWearableController.setAmbientEnabled();
        }
    }

    public void setAutoResumeEnabled(boolean enabled) {
        if (this.mWearableController != null && hasSetAutoResumeEnabledMethod()) {
            this.mWearableController.setAutoResumeEnabled(enabled);
        }
    }

    public boolean isAmbient() {
        if (this.mWearableController != null) {
            return this.mWearableController.isAmbient();
        }
        return false;
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        if (this.mWearableController != null) {
            this.mWearableController.dump(prefix, fd, writer, args);
        }
    }

    private void initAmbientSupport(Activity activity) {
        if (Build.VERSION.SDK_INT > 21) {
            SharedLibraryVersion.verifySharedLibraryPresent();
            WearableActivityController.AmbientCallback callbackBridge = new WearableActivityController.AmbientCallback() { // from class: android.support.wearable.activity.WearableActivityDelegate.1
                public void onEnterAmbient(Bundle ambientDetails) {
                    WearableActivityDelegate.this.mCallback.onEnterAmbient(ambientDetails);
                }

                public void onUpdateAmbient() {
                    WearableActivityDelegate.this.mCallback.onUpdateAmbient();
                }

                public void onExitAmbient() {
                    WearableActivityDelegate.this.mCallback.onExitAmbient();
                }
            };
            String strValueOf = String.valueOf(getClass().getSimpleName());
            String tagStart = strValueOf.length() != 0 ? "WearActivity[".concat(strValueOf) : new String("WearActivity[");
            String tag = String.valueOf(tagStart.substring(0, Math.min(tagStart.length(), 22))).concat("]");
            this.mWearableController = new WearableActivityController(tag, activity, callbackBridge);
            verifyAmbientCallbacksPresent();
        }
    }

    private static void verifyAmbientCallbacksPresent() {
        if (!sAmbientCallbacksVerifiedPresent) {
            try {
                Method method = WearableActivityController.AmbientCallback.class.getDeclaredMethod("onEnterAmbient", Bundle.class);
                String strValueOf = String.valueOf(method.getName());
                if (!".onEnterAmbient".equals(strValueOf.length() != 0 ? ".".concat(strValueOf) : new String("."))) {
                    throw new NoSuchMethodException();
                }
                sAmbientCallbacksVerifiedPresent = true;
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException("Could not find a required method for ambient support, likely due to proguard optimization. Please add com.google.android.wearable:wearable jar to the list of library jars for your project");
            }
        }
    }

    private boolean hasSetAutoResumeEnabledMethod() {
        if (!sInitAutoResumeEnabledMethod) {
            sInitAutoResumeEnabledMethod = true;
            try {
                Method method = WearableActivityController.class.getDeclaredMethod("setAutoResumeEnabled", Boolean.TYPE);
                String strValueOf = String.valueOf(method.getName());
                if (!".setAutoResumeEnabled".equals(strValueOf.length() != 0 ? ".".concat(strValueOf) : new String("."))) {
                    throw new NoSuchMethodException();
                }
                sHasAutoResumeEnabledMethod = true;
            } catch (NoSuchMethodException e) {
                Log.w("WearableActivity", "Could not find a required method for auto-resume support, likely due to proguard optimization. Please add com.google.android.wearable:wearable jar to the list of library jars for your project");
                sHasAutoResumeEnabledMethod = false;
            }
        }
        return sHasAutoResumeEnabledMethod;
    }
}
