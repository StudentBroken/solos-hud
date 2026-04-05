package com.google.android.gms.common.internal;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.v4.view.MotionEventCompat;

/* JADX INFO: loaded from: classes67.dex */
public abstract class zzaz extends Binder implements zzay {
    public zzaz() {
        attachInterface(this, "com.google.android.gms.common.internal.IGmsServiceBroker");
    }

    @Override // android.os.IInterface
    public IBinder asBinder() {
        return this;
    }

    @Override // android.os.Binder
    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        zzav zzaxVar;
        if (i > 16777215) {
            return super.onTransact(i, parcel, parcel2, i2);
        }
        parcel.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
        IBinder strongBinder = parcel.readStrongBinder();
        if (strongBinder == null) {
            zzaxVar = null;
        } else {
            IInterface iInterfaceQueryLocalInterface = strongBinder.queryLocalInterface("com.google.android.gms.common.internal.IGmsCallbacks");
            zzaxVar = iInterfaceQueryLocalInterface instanceof zzav ? (zzav) iInterfaceQueryLocalInterface : new zzax(strongBinder);
        }
        if (i == 46) {
            zza(zzaxVar, parcel.readInt() != 0 ? zzy.CREATOR.createFromParcel(parcel) : null);
            parcel2.writeNoException();
            return true;
        }
        if (i == 47) {
            if (parcel.readInt() != 0) {
                zzcc.CREATOR.createFromParcel(parcel);
            }
            throw new UnsupportedOperationException();
        }
        parcel.readInt();
        if (i != 4) {
            parcel.readString();
        }
        switch (i) {
            case 1:
                parcel.readString();
                parcel.createStringArray();
                parcel.readString();
                if (parcel.readInt() != 0) {
                    Bundle.CREATOR.createFromParcel(parcel);
                }
                break;
            case 2:
            case 5:
            case 6:
            case 7:
            case 8:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 23:
            case 25:
            case 27:
            case MotionEventCompat.AXIS_GENERIC_6 /* 37 */:
            case MotionEventCompat.AXIS_GENERIC_7 /* 38 */:
            case MotionEventCompat.AXIS_GENERIC_10 /* 41 */:
            case MotionEventCompat.AXIS_GENERIC_12 /* 43 */:
                if (parcel.readInt() != 0) {
                    Bundle.CREATOR.createFromParcel(parcel);
                }
                break;
            case 9:
                parcel.readString();
                parcel.createStringArray();
                parcel.readString();
                parcel.readStrongBinder();
                parcel.readString();
                if (parcel.readInt() != 0) {
                    Bundle.CREATOR.createFromParcel(parcel);
                }
                break;
            case 10:
                parcel.readString();
                parcel.createStringArray();
                break;
            case 19:
                parcel.readStrongBinder();
                if (parcel.readInt() != 0) {
                    Bundle.CREATOR.createFromParcel(parcel);
                }
                break;
            case 20:
            case 30:
                parcel.createStringArray();
                parcel.readString();
                if (parcel.readInt() != 0) {
                    Bundle.CREATOR.createFromParcel(parcel);
                }
                break;
            case 34:
                parcel.readString();
                break;
        }
        throw new UnsupportedOperationException();
    }
}
