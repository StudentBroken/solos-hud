package android.support.wearable.complications;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(24)
public class ProviderUpdateRequester {
    public static final String ACTION_REQUEST_UPDATE = "android.support.wearable.complications.ACTION_REQUEST_UPDATE";
    public static final String ACTION_REQUEST_UPDATE_ALL = "android.support.wearable.complications.ACTION_REQUEST_UPDATE_ALL";
    public static final String EXTRA_COMPLICATION_IDS = "android.support.wearable.complications.EXTRA_COMPLICATION_IDS";
    public static final String EXTRA_PENDING_INTENT = "android.support.wearable.complications.EXTRA_PENDING_INTENT";
    public static final String EXTRA_PROVIDER_COMPONENT = "android.support.wearable.complications.EXTRA_PROVIDER_COMPONENT";
    private static final String UPDATE_REQUEST_RECEIVER_PACKAGE = "com.google.android.wearable.app";
    private final Context mContext;
    private final ComponentName mProviderComponent;

    public ProviderUpdateRequester(Context context, ComponentName providerComponent) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        if (providerComponent == null) {
            throw new IllegalArgumentException("ProviderComponent cannot be null");
        }
        this.mContext = context;
        this.mProviderComponent = providerComponent;
    }

    public void requestUpdateAll() {
        Intent intent = new Intent(ACTION_REQUEST_UPDATE_ALL);
        intent.setPackage(UPDATE_REQUEST_RECEIVER_PACKAGE);
        intent.putExtra(EXTRA_PROVIDER_COMPONENT, this.mProviderComponent);
        intent.putExtra("android.support.wearable.complications.EXTRA_PENDING_INTENT", PendingIntent.getActivity(this.mContext, 0, new Intent(""), 0));
        this.mContext.sendBroadcast(intent);
    }

    public void requestUpdate(int... complicationIds) {
        Intent intent = new Intent(ACTION_REQUEST_UPDATE);
        intent.setPackage(UPDATE_REQUEST_RECEIVER_PACKAGE);
        intent.putExtra(EXTRA_PROVIDER_COMPONENT, this.mProviderComponent);
        intent.putExtra(EXTRA_COMPLICATION_IDS, complicationIds);
        intent.putExtra("android.support.wearable.complications.EXTRA_PENDING_INTENT", PendingIntent.getActivity(this.mContext, 0, new Intent(""), 0));
        this.mContext.sendBroadcast(intent);
    }
}
