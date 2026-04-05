package android.support.customtabs;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.customtabs.ICustomTabsCallback;
import android.support.v4.app.BundleCompat;
import android.util.Log;

/* JADX INFO: loaded from: classes31.dex */
public class CustomTabsSessionToken {
    private static final String TAG = "CustomTabsSessionToken";
    private final CustomTabsCallback mCallback = new CustomTabsCallback() { // from class: android.support.customtabs.CustomTabsSessionToken.1
        @Override // android.support.customtabs.CustomTabsCallback
        public void onNavigationEvent(int navigationEvent, Bundle extras) {
            try {
                CustomTabsSessionToken.this.mCallbackBinder.onNavigationEvent(navigationEvent, extras);
            } catch (RemoteException e) {
                Log.e(CustomTabsSessionToken.TAG, "RemoteException during ICustomTabsCallback transaction");
            }
        }
    };
    private final ICustomTabsCallback mCallbackBinder;

    public static CustomTabsSessionToken getSessionTokenFromIntent(Intent intent) {
        Bundle b = intent.getExtras();
        IBinder binder = BundleCompat.getBinder(b, CustomTabsIntent.EXTRA_SESSION);
        if (binder == null) {
            return null;
        }
        return new CustomTabsSessionToken(ICustomTabsCallback.Stub.asInterface(binder));
    }

    CustomTabsSessionToken(ICustomTabsCallback callbackBinder) {
        this.mCallbackBinder = callbackBinder;
    }

    IBinder getCallbackBinder() {
        return this.mCallbackBinder.asBinder();
    }

    public int hashCode() {
        return getCallbackBinder().hashCode();
    }

    public boolean equals(Object o) {
        if (!(o instanceof CustomTabsSessionToken)) {
            return false;
        }
        CustomTabsSessionToken token = (CustomTabsSessionToken) o;
        return token.getCallbackBinder().equals(this.mCallbackBinder.asBinder());
    }

    public CustomTabsCallback getCallback() {
        return this.mCallback;
    }
}
