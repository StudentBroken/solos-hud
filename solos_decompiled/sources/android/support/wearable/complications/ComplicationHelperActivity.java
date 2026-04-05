package android.support.wearable.complications;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(24)
public class ComplicationHelperActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final String ACTION_PERMISSION_REQUEST_ONLY = "android.support.wearable.complications.ACTION_PERMISSION_REQUEST_ONLY";
    public static final String ACTION_REQUEST_UPDATE_ALL_ACTIVE = "android.support.wearable.complications.ACTION_REQUEST_UPDATE_ALL_ACTIVE";
    private static final String ACTION_START_PROVIDER_CHOOSER = "android.support.wearable.complications.ACTION_START_PROVIDER_CHOOSER";
    private static final String COMPLICATIONS_PERMISSION = "com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA";
    private static final String COMPLICATIONS_PERMISSION_PRIVILEGED = "com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA_PRIVILEGED";
    public static final String EXTRA_WATCH_FACE_COMPONENT = "android.support.wearable.complications.EXTRA_WATCH_FACE_COMPONENT";
    private static final int PERMISSION_REQUEST_CODE_PROVIDER_CHOOSER = 1;
    private static final int PERMISSION_REQUEST_CODE_REQUEST_ONLY = 2;
    private static final int START_REQUEST_CODE_PROVIDER_CHOOSER = 1;
    private static final String UPDATE_REQUEST_RECEIVER_PACKAGE = "com.google.android.wearable.app";
    private int[] mTypes;
    private ComponentName mWatchFace;
    private int mWfComplicationId;

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent;
        setTheme(16973840);
        super.onCreate(savedInstanceState);
        intent = getIntent();
        switch (intent.getAction()) {
            case "android.support.wearable.complications.ACTION_START_PROVIDER_CHOOSER":
                this.mWatchFace = (ComponentName) intent.getParcelableExtra(ProviderChooserIntent.EXTRA_WATCH_FACE_COMPONENT_NAME);
                this.mWfComplicationId = intent.getIntExtra("android.support.wearable.complications.EXTRA_COMPLICATION_ID", 0);
                this.mTypes = intent.getIntArrayExtra(ProviderChooserIntent.EXTRA_SUPPORTED_TYPES);
                if (checkPermission()) {
                    startProviderChooser();
                    return;
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{COMPLICATIONS_PERMISSION}, 1);
                    return;
                }
            case "android.support.wearable.complications.ACTION_PERMISSION_REQUEST_ONLY":
                this.mWatchFace = (ComponentName) intent.getParcelableExtra(ProviderChooserIntent.EXTRA_WATCH_FACE_COMPONENT_NAME);
                if (checkPermission()) {
                    finish();
                    return;
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{COMPLICATIONS_PERMISSION}, 2);
                    return;
                }
            default:
                throw new IllegalStateException("Unrecognised intent action.");
        }
    }

    @Override // android.app.Activity, android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length != 0) {
            if (grantResults[0] == 0) {
                if (requestCode == 1) {
                    startProviderChooser();
                } else {
                    finish();
                }
                requestUpdateAll(this.mWatchFace);
                return;
            }
            finish();
        }
    }

    @Override // android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            setResult(resultCode, data);
            finish();
        }
    }

    private boolean checkPermission() {
        return ActivityCompat.checkSelfPermission(this, COMPLICATIONS_PERMISSION_PRIVILEGED) == 0 || ActivityCompat.checkSelfPermission(this, COMPLICATIONS_PERMISSION) == 0;
    }

    public static Intent createProviderChooserHelperIntent(Context context, ComponentName watchFace, int watchFaceComplicationId, int... supportedTypes) {
        Intent intent = new Intent(context, (Class<?>) ComplicationHelperActivity.class);
        intent.setAction(ACTION_START_PROVIDER_CHOOSER);
        intent.putExtra(ProviderChooserIntent.EXTRA_WATCH_FACE_COMPONENT_NAME, watchFace);
        intent.putExtra("android.support.wearable.complications.EXTRA_COMPLICATION_ID", watchFaceComplicationId);
        intent.putExtra(ProviderChooserIntent.EXTRA_SUPPORTED_TYPES, supportedTypes);
        return intent;
    }

    public static Intent createPermissionRequestHelperIntent(Context context, ComponentName watchFace) {
        Intent intent = new Intent(context, (Class<?>) ComplicationHelperActivity.class);
        intent.setAction(ACTION_PERMISSION_REQUEST_ONLY);
        intent.putExtra(ProviderChooserIntent.EXTRA_WATCH_FACE_COMPONENT_NAME, watchFace);
        return intent;
    }

    private void startProviderChooser() {
        startActivityForResult(ProviderChooserIntent.createProviderChooserIntent(this.mWatchFace, this.mWfComplicationId, this.mTypes), 1);
    }

    private void requestUpdateAll(ComponentName watchFaceComponent) {
        Intent intent = new Intent(ACTION_REQUEST_UPDATE_ALL_ACTIVE);
        intent.setPackage(UPDATE_REQUEST_RECEIVER_PACKAGE);
        intent.putExtra(EXTRA_WATCH_FACE_COMPONENT, watchFaceComponent);
        intent.putExtra("android.support.wearable.complications.EXTRA_PENDING_INTENT", PendingIntent.getActivity(this, 0, new Intent(""), 0));
        sendBroadcast(intent);
    }
}
