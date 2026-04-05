package com.google.tagmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import com.facebook.internal.ServerProtocol;
import com.google.analytics.containertag.common.FunctionType;
import com.google.analytics.containertag.common.Key;
import com.google.analytics.midtier.proto.containertag.TypeSystem;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes49.dex */
class ArbitraryPixelTag extends TrackingTag {
    private final Context mContext;
    private final HitSenderProvider mHitSenderProvider;
    private static final String ID = FunctionType.ARBITRARY_PIXEL.toString();
    private static final String URL = Key.URL.toString();
    private static final String ADDITIONAL_PARAMS = Key.ADDITIONAL_PARAMS.toString();
    private static final String UNREPEATABLE = Key.UNREPEATABLE.toString();
    static final String ARBITRARY_PIXEL_UNREPEATABLE = "gtm_" + ID + "_unrepeatable";
    private static final Set<String> unrepeatableIds = new HashSet();

    public interface HitSenderProvider {
        HitSender get();
    }

    public static String getFunctionId() {
        return ID;
    }

    public ArbitraryPixelTag(final Context context) {
        this(context, new HitSenderProvider() { // from class: com.google.tagmanager.ArbitraryPixelTag.1
            @Override // com.google.tagmanager.ArbitraryPixelTag.HitSenderProvider
            public HitSender get() {
                return DelayedHitSender.getInstance(context);
            }
        });
    }

    @VisibleForTesting
    ArbitraryPixelTag(Context context, HitSenderProvider hitSenderProvider) {
        super(ID, URL);
        this.mHitSenderProvider = hitSenderProvider;
        this.mContext = context;
    }

    @Override // com.google.tagmanager.TrackingTag
    public void evaluateTrackingTag(Map<String, TypeSystem.Value> tag) {
        String unrepeatableId = tag.get(UNREPEATABLE) != null ? Types.valueToString(tag.get(UNREPEATABLE)) : null;
        if (unrepeatableId == null || !idProcessed(unrepeatableId)) {
            String url = Types.valueToString(tag.get(URL));
            Uri initialUri = Uri.parse(url);
            Uri.Builder uriBuilder = initialUri.buildUpon();
            TypeSystem.Value additionalParamsList = tag.get(ADDITIONAL_PARAMS);
            if (additionalParamsList != null) {
                Object l = Types.valueToObject(additionalParamsList);
                if (!(l instanceof List)) {
                    Log.e("ArbitraryPixel: additional params not a list: not sending partial hit: " + uriBuilder.build().toString());
                    return;
                }
                List<Object> list = (List) l;
                for (Object m : list) {
                    if (!(m instanceof Map)) {
                        Log.e("ArbitraryPixel: additional params contains non-map: not sending partial hit: " + uriBuilder.build().toString());
                        return;
                    }
                    Map<Object, Object> map = (Map) m;
                    for (Map.Entry<Object, Object> entry : map.entrySet()) {
                        uriBuilder.appendQueryParameter(entry.getKey().toString(), entry.getValue().toString());
                    }
                }
            }
            String uri = uriBuilder.build().toString();
            this.mHitSenderProvider.get().sendHit(uri);
            Log.v("ArbitraryPixel: url = " + uri);
            if (unrepeatableId != null) {
                synchronized (ArbitraryPixelTag.class) {
                    unrepeatableIds.add(unrepeatableId);
                    SharedPreferencesUtil.saveAsync(this.mContext, ARBITRARY_PIXEL_UNREPEATABLE, unrepeatableId, ServerProtocol.DIALOG_RETURN_SCOPES_TRUE);
                }
            }
        }
    }

    private synchronized boolean idProcessed(String unrepeatableId) {
        boolean z = true;
        synchronized (this) {
            if (!idInCache(unrepeatableId)) {
                if (idInSharedPreferences(unrepeatableId)) {
                    unrepeatableIds.add(unrepeatableId);
                } else {
                    z = false;
                }
            }
        }
        return z;
    }

    @VisibleForTesting
    boolean idInSharedPreferences(String unrepeatableId) {
        SharedPreferences settings = this.mContext.getSharedPreferences(ARBITRARY_PIXEL_UNREPEATABLE, 0);
        return settings.contains(unrepeatableId);
    }

    @VisibleForTesting
    void clearCache() {
        unrepeatableIds.clear();
    }

    @VisibleForTesting
    boolean idInCache(String unrepeatableId) {
        return unrepeatableIds.contains(unrepeatableId);
    }
}
