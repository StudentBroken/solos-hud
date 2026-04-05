package android.support.wearable.complications;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.digits.sdk.vcard.VCardConfig;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(24)
public class ProviderChooserIntent {
    public static final String ACTION_CHOOSE_PROVIDER = "com.google.android.clockwork.home.complications.ACTION_CHOOSE_PROVIDER";
    public static final String EXTRA_COMPLICATION_ID = "android.support.wearable.complications.EXTRA_COMPLICATION_ID";
    public static final String EXTRA_PENDING_INTENT = "android.support.wearable.complications.EXTRA_PENDING_INTENT";
    public static final String EXTRA_PROVIDER_INFO = "android.support.wearable.complications.EXTRA_PROVIDER_INFO";
    public static final String EXTRA_SUPPORTED_TYPES = "android.support.wearable.complications.EXTRA_SUPPORTED_TYPES";
    public static final String EXTRA_WATCH_FACE_COMPONENT_NAME = "android.support.wearable.complications.EXTRA_WATCH_FACE_COMPONENT_NAME";

    public static Intent createProviderChooserIntent(ComponentName watchFace, int watchFaceComplicationId, int... supportedTypes) {
        Intent intent = new Intent(ACTION_CHOOSE_PROVIDER);
        intent.putExtra(EXTRA_WATCH_FACE_COMPONENT_NAME, watchFace);
        intent.putExtra("android.support.wearable.complications.EXTRA_COMPLICATION_ID", watchFaceComplicationId);
        intent.putExtra(EXTRA_SUPPORTED_TYPES, supportedTypes);
        return intent;
    }

    public static void startProviderChooserActivity(Context context, ComponentName watchFace, int watchFaceComplicationId, int... supportedTypes) {
        Intent intent = createProviderChooserIntent(watchFace, watchFaceComplicationId, supportedTypes);
        intent.putExtra("android.support.wearable.complications.EXTRA_PENDING_INTENT", PendingIntent.getActivity(context, 0, new Intent(""), 0));
        intent.setFlags(VCardConfig.FLAG_REFRAIN_QP_TO_NAME_PROPERTIES);
        context.startActivity(intent);
    }

    private ProviderChooserIntent() {
    }
}
