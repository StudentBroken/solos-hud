package android.support.customtabs;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.customtabs.ICustomTabsService;
import android.support.v4.util.ArrayMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/* JADX INFO: loaded from: classes31.dex */
public abstract class CustomTabsService extends Service {
    public static final String ACTION_CUSTOM_TABS_CONNECTION = "android.support.customtabs.action.CustomTabsService";
    public static final String KEY_URL = "android.support.customtabs.otherurls.URL";
    private final Map<IBinder, IBinder.DeathRecipient> mDeathRecipientMap = new ArrayMap();
    private ICustomTabsService.Stub mBinder = new ICustomTabsService.Stub() { // from class: android.support.customtabs.CustomTabsService.1
        @Override // android.support.customtabs.ICustomTabsService
        public boolean warmup(long flags) {
            return CustomTabsService.this.warmup(flags);
        }

        @Override // android.support.customtabs.ICustomTabsService
        public boolean newSession(ICustomTabsCallback callback) {
            final CustomTabsSessionToken sessionToken = new CustomTabsSessionToken(callback);
            try {
                IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() { // from class: android.support.customtabs.CustomTabsService.1.1
                    @Override // android.os.IBinder.DeathRecipient
                    public void binderDied() {
                        CustomTabsService.this.cleanUpSession(sessionToken);
                    }
                };
                synchronized (CustomTabsService.this.mDeathRecipientMap) {
                    callback.asBinder().linkToDeath(deathRecipient, 0);
                    CustomTabsService.this.mDeathRecipientMap.put(callback.asBinder(), deathRecipient);
                }
                return CustomTabsService.this.newSession(sessionToken);
            } catch (RemoteException e) {
                return false;
            }
        }

        @Override // android.support.customtabs.ICustomTabsService
        public boolean mayLaunchUrl(ICustomTabsCallback callback, Uri url, Bundle extras, List<Bundle> otherLikelyBundles) {
            return CustomTabsService.this.mayLaunchUrl(new CustomTabsSessionToken(callback), url, extras, otherLikelyBundles);
        }

        @Override // android.support.customtabs.ICustomTabsService
        public Bundle extraCommand(String commandName, Bundle args) {
            return CustomTabsService.this.extraCommand(commandName, args);
        }

        @Override // android.support.customtabs.ICustomTabsService
        public boolean updateVisuals(ICustomTabsCallback callback, Bundle bundle) {
            return CustomTabsService.this.updateVisuals(new CustomTabsSessionToken(callback), bundle);
        }
    };

    protected abstract Bundle extraCommand(String str, Bundle bundle);

    protected abstract boolean mayLaunchUrl(CustomTabsSessionToken customTabsSessionToken, Uri uri, Bundle bundle, List<Bundle> list);

    protected abstract boolean newSession(CustomTabsSessionToken customTabsSessionToken);

    protected abstract boolean updateVisuals(CustomTabsSessionToken customTabsSessionToken, Bundle bundle);

    protected abstract boolean warmup(long j);

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    protected boolean cleanUpSession(CustomTabsSessionToken sessionToken) {
        try {
            synchronized (this.mDeathRecipientMap) {
                IBinder binder = sessionToken.getCallbackBinder();
                IBinder.DeathRecipient deathRecipient = this.mDeathRecipientMap.get(binder);
                binder.unlinkToDeath(deathRecipient, 0);
                this.mDeathRecipientMap.remove(binder);
            }
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}
