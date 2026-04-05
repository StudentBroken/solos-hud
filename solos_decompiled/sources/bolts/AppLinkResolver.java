package bolts;

import android.net.Uri;

/* JADX INFO: loaded from: classes46.dex */
public interface AppLinkResolver {
    Task<AppLink> getAppLinkFromUrlInBackground(Uri uri);
}
