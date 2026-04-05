package com.google.tagmanager;

import android.content.Context;
import android.net.Uri;
import com.google.tagmanager.DataLayer;
import java.util.Map;

/* JADX INFO: loaded from: classes49.dex */
class AdwordsClickReferrerListener implements DataLayer.Listener {
    private final Context context;

    public AdwordsClickReferrerListener(Context context) {
        this.context = context;
    }

    @Override // com.google.tagmanager.DataLayer.Listener
    public void changed(Map<Object, Object> update) {
        Object gtm;
        Object url = update.get("gtm.url");
        if (url == null && (gtm = update.get("gtm")) != null && (gtm instanceof Map)) {
            url = ((Map) gtm).get("url");
        }
        if (url != null && (url instanceof String)) {
            Uri uri = Uri.parse((String) url);
            String referrer = uri.getQueryParameter("referrer");
            if (referrer != null) {
                InstallReferrerUtil.addClickReferrer(this.context, referrer);
            }
        }
    }
}
