package com.google.android.gms.dynamic;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.internal.zzee;
import com.google.android.gms.internal.zzef;

/* JADX INFO: loaded from: classes67.dex */
public abstract class zzl extends zzee implements zzk {
    public zzl() {
        attachInterface(this, "com.google.android.gms.dynamic.IFragmentWrapper");
    }

    @Override // android.os.Binder
    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        IObjectWrapper zzmVar = null;
        if (zza(i, parcel, parcel2, i2)) {
            return true;
        }
        switch (i) {
            case 2:
                IObjectWrapper iObjectWrapperZztx = zztx();
                parcel2.writeNoException();
                zzef.zza(parcel2, iObjectWrapperZztx);
                break;
            case 3:
                Bundle arguments = getArguments();
                parcel2.writeNoException();
                zzef.zzb(parcel2, arguments);
                break;
            case 4:
                int id = getId();
                parcel2.writeNoException();
                parcel2.writeInt(id);
                break;
            case 5:
                zzk zzkVarZzty = zzty();
                parcel2.writeNoException();
                zzef.zza(parcel2, zzkVarZzty);
                break;
            case 6:
                IObjectWrapper iObjectWrapperZztz = zztz();
                parcel2.writeNoException();
                zzef.zza(parcel2, iObjectWrapperZztz);
                break;
            case 7:
                boolean retainInstance = getRetainInstance();
                parcel2.writeNoException();
                zzef.zza(parcel2, retainInstance);
                break;
            case 8:
                String tag = getTag();
                parcel2.writeNoException();
                parcel2.writeString(tag);
                break;
            case 9:
                zzk zzkVarZztA = zztA();
                parcel2.writeNoException();
                zzef.zza(parcel2, zzkVarZztA);
                break;
            case 10:
                int targetRequestCode = getTargetRequestCode();
                parcel2.writeNoException();
                parcel2.writeInt(targetRequestCode);
                break;
            case 11:
                boolean userVisibleHint = getUserVisibleHint();
                parcel2.writeNoException();
                zzef.zza(parcel2, userVisibleHint);
                break;
            case 12:
                IObjectWrapper view = getView();
                parcel2.writeNoException();
                zzef.zza(parcel2, view);
                break;
            case 13:
                boolean zIsAdded = isAdded();
                parcel2.writeNoException();
                zzef.zza(parcel2, zIsAdded);
                break;
            case 14:
                boolean zIsDetached = isDetached();
                parcel2.writeNoException();
                zzef.zza(parcel2, zIsDetached);
                break;
            case 15:
                boolean zIsHidden = isHidden();
                parcel2.writeNoException();
                zzef.zza(parcel2, zIsHidden);
                break;
            case 16:
                boolean zIsInLayout = isInLayout();
                parcel2.writeNoException();
                zzef.zza(parcel2, zIsInLayout);
                break;
            case 17:
                boolean zIsRemoving = isRemoving();
                parcel2.writeNoException();
                zzef.zza(parcel2, zIsRemoving);
                break;
            case 18:
                boolean zIsResumed = isResumed();
                parcel2.writeNoException();
                zzef.zza(parcel2, zIsResumed);
                break;
            case 19:
                boolean zIsVisible = isVisible();
                parcel2.writeNoException();
                zzef.zza(parcel2, zIsVisible);
                break;
            case 20:
                IBinder strongBinder = parcel.readStrongBinder();
                if (strongBinder != null) {
                    IInterface iInterfaceQueryLocalInterface = strongBinder.queryLocalInterface("com.google.android.gms.dynamic.IObjectWrapper");
                    zzmVar = iInterfaceQueryLocalInterface instanceof IObjectWrapper ? (IObjectWrapper) iInterfaceQueryLocalInterface : new zzm(strongBinder);
                }
                zzC(zzmVar);
                parcel2.writeNoException();
                break;
            case 21:
                setHasOptionsMenu(zzef.zza(parcel));
                parcel2.writeNoException();
                break;
            case 22:
                setMenuVisibility(zzef.zza(parcel));
                parcel2.writeNoException();
                break;
            case 23:
                setRetainInstance(zzef.zza(parcel));
                parcel2.writeNoException();
                break;
            case 24:
                setUserVisibleHint(zzef.zza(parcel));
                parcel2.writeNoException();
                break;
            case 25:
                startActivity((Intent) zzef.zza(parcel, Intent.CREATOR));
                parcel2.writeNoException();
                break;
            case 26:
                startActivityForResult((Intent) zzef.zza(parcel, Intent.CREATOR), parcel.readInt());
                parcel2.writeNoException();
                break;
            case 27:
                IBinder strongBinder2 = parcel.readStrongBinder();
                if (strongBinder2 != null) {
                    IInterface iInterfaceQueryLocalInterface2 = strongBinder2.queryLocalInterface("com.google.android.gms.dynamic.IObjectWrapper");
                    zzmVar = iInterfaceQueryLocalInterface2 instanceof IObjectWrapper ? (IObjectWrapper) iInterfaceQueryLocalInterface2 : new zzm(strongBinder2);
                }
                zzD(zzmVar);
                parcel2.writeNoException();
                break;
            default:
                return false;
        }
        return true;
    }
}
