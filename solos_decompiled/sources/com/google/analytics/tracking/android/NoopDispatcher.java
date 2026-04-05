package com.google.analytics.tracking.android;

import android.text.TextUtils;
import java.util.List;

/* JADX INFO: loaded from: classes49.dex */
class NoopDispatcher implements Dispatcher {
    NoopDispatcher() {
    }

    @Override // com.google.analytics.tracking.android.Dispatcher
    public boolean okToDispatch() {
        return true;
    }

    @Override // com.google.analytics.tracking.android.Dispatcher
    public int dispatchHits(List<Hit> hits) {
        String logMessage;
        if (hits == null) {
            return 0;
        }
        int maxHits = Math.min(hits.size(), 40);
        if (Log.isVerbose()) {
            Log.v("Hits not actually being sent as dispatch is false...");
            for (int i = 0; i < maxHits; i++) {
                String hitString = hits.get(i).getHitParams();
                String modifiedHit = TextUtils.isEmpty(hitString) ? "" : HitBuilder.postProcessHit(hits.get(i), System.currentTimeMillis());
                if (TextUtils.isEmpty(modifiedHit)) {
                    logMessage = "Hit couldn't be read, wouldn't be sent:";
                } else if (modifiedHit.length() <= 2036) {
                    logMessage = "GET would be sent:";
                } else if (modifiedHit.length() > 8192) {
                    logMessage = "Would be too big:";
                } else {
                    logMessage = "POST would be sent:";
                }
                Log.v(logMessage + modifiedHit);
            }
            return maxHits;
        }
        return maxHits;
    }

    @Override // com.google.analytics.tracking.android.Dispatcher
    public void overrideHostUrl(String hostOverride) {
    }

    @Override // com.google.analytics.tracking.android.Dispatcher
    public void close() {
    }
}
