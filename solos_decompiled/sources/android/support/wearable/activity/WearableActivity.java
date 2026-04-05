package android.support.wearable.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.wearable.activity.WearableActivityDelegate;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.PrintWriter;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(21)
public abstract class WearableActivity extends Activity {
    public static final String EXTRA_BURN_IN_PROTECTION = "com.google.android.wearable.compat.extra.BURN_IN_PROTECTION";
    public static final String EXTRA_LOWBIT_AMBIENT = "com.google.android.wearable.compat.extra.LOWBIT_AMBIENT";
    private static final String TAG = "WearableActivity";
    private final WearableActivityDelegate.AmbientCallback callback = new WearableActivityDelegate.AmbientCallback() { // from class: android.support.wearable.activity.WearableActivity.1
        @Override // android.support.wearable.activity.WearableActivityDelegate.AmbientCallback
        public void onEnterAmbient(Bundle ambientDetails) {
            WearableActivity.this.mSuperCalled = false;
            WearableActivity.this.onEnterAmbient(ambientDetails);
            if (!WearableActivity.this.mSuperCalled) {
                String strValueOf = String.valueOf(WearableActivity.this);
                Log.w(WearableActivity.TAG, new StringBuilder(String.valueOf(strValueOf).length() + 56).append("Activity ").append(strValueOf).append(" did not call through to super.onEnterAmbient()").toString());
            }
        }

        @Override // android.support.wearable.activity.WearableActivityDelegate.AmbientCallback
        public void onExitAmbient() {
            WearableActivity.this.mSuperCalled = false;
            WearableActivity.this.onExitAmbient();
            if (!WearableActivity.this.mSuperCalled) {
                String strValueOf = String.valueOf(WearableActivity.this);
                Log.w(WearableActivity.TAG, new StringBuilder(String.valueOf(strValueOf).length() + 55).append("Activity ").append(strValueOf).append(" did not call through to super.onExitAmbient()").toString());
            }
        }

        @Override // android.support.wearable.activity.WearableActivityDelegate.AmbientCallback
        public void onUpdateAmbient() {
            WearableActivity.this.mSuperCalled = false;
            WearableActivity.this.onUpdateAmbient();
            if (!WearableActivity.this.mSuperCalled) {
                String strValueOf = String.valueOf(WearableActivity.this);
                Log.w(WearableActivity.TAG, new StringBuilder(String.valueOf(strValueOf).length() + 57).append("Activity ").append(strValueOf).append(" did not call through to super.onUpdateAmbient()").toString());
            }
        }
    };
    private final WearableActivityDelegate mDelegate = new WearableActivityDelegate(this.callback);
    private boolean mSuperCalled;

    @Override // android.app.Activity
    @CallSuper
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mDelegate.onCreate(this);
    }

    @Override // android.app.Activity
    @CallSuper
    protected void onResume() {
        super.onResume();
        this.mDelegate.onResume();
    }

    @Override // android.app.Activity
    @CallSuper
    protected void onPause() {
        this.mDelegate.onPause();
        super.onPause();
    }

    @Override // android.app.Activity
    @CallSuper
    protected void onStop() {
        this.mDelegate.onStop();
        super.onStop();
    }

    @Override // android.app.Activity
    @CallSuper
    protected void onDestroy() {
        this.mDelegate.onDestroy();
        super.onDestroy();
    }

    public final void setAmbientEnabled() {
        this.mDelegate.setAmbientEnabled();
    }

    public final void setAutoResumeEnabled(boolean enabled) {
        this.mDelegate.setAutoResumeEnabled(enabled);
    }

    public final boolean isAmbient() {
        return this.mDelegate.isAmbient();
    }

    @Override // android.app.Activity
    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        this.mDelegate.dump(prefix, fd, writer, args);
    }

    @CallSuper
    public void onEnterAmbient(Bundle ambientDetails) {
        this.mSuperCalled = true;
    }

    @CallSuper
    public void onUpdateAmbient() {
        this.mSuperCalled = true;
    }

    @CallSuper
    public void onExitAmbient() {
        this.mSuperCalled = true;
    }
}
