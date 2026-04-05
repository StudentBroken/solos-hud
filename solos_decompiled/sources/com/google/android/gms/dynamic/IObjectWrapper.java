package com.google.android.gms.dynamic;

import android.os.IBinder;
import android.os.IInterface;
import com.google.android.gms.internal.zzee;

/* JADX INFO: loaded from: classes67.dex */
public interface IObjectWrapper extends IInterface {

    public static abstract class zza extends zzee implements IObjectWrapper {
        public zza() {
            attachInterface(this, "com.google.android.gms.dynamic.IObjectWrapper");
        }

        public static IObjectWrapper zzM(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.dynamic.IObjectWrapper");
            return iInterfaceQueryLocalInterface instanceof IObjectWrapper ? (IObjectWrapper) iInterfaceQueryLocalInterface : new zzm(iBinder);
        }
    }
}
