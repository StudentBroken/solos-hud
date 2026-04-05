package com.google.android.gms.iid;

import android.os.Build;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import com.google.android.gms.common.internal.ReflectedParcelable;

/* JADX INFO: loaded from: classes56.dex */
public class MessengerCompat implements ReflectedParcelable {
    public static final Parcelable.Creator<MessengerCompat> CREATOR = new zzd();
    private Messenger zzbhb;
    private zzb zzbhc;

    public MessengerCompat(IBinder iBinder) {
        zzb zzcVar;
        if (Build.VERSION.SDK_INT >= 21) {
            this.zzbhb = new Messenger(iBinder);
            return;
        }
        if (iBinder == null) {
            zzcVar = null;
        } else {
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.iid.IMessengerCompat");
            zzcVar = iInterfaceQueryLocalInterface instanceof zzb ? (zzb) iInterfaceQueryLocalInterface : new zzc(iBinder);
        }
        this.zzbhc = zzcVar;
    }

    private final IBinder getBinder() {
        return this.zzbhb != null ? this.zzbhb.getBinder() : this.zzbhc.asBinder();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            return getBinder().equals(((MessengerCompat) obj).getBinder());
        } catch (ClassCastException e) {
            return false;
        }
    }

    public int hashCode() {
        return getBinder().hashCode();
    }

    public final void send(Message message) throws RemoteException {
        if (this.zzbhb != null) {
            this.zzbhb.send(message);
        } else {
            this.zzbhc.send(message);
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        if (this.zzbhb != null) {
            parcel.writeStrongBinder(this.zzbhb.getBinder());
        } else {
            parcel.writeStrongBinder(this.zzbhc.asBinder());
        }
    }
}
