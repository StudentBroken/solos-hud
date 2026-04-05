package android.support.wearable.complications;

import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes33.dex */
public interface IProviderInfoService extends IInterface {
    ComplicationProviderInfo[] getProviderInfos(ComponentName componentName, int[] iArr) throws RemoteException;

    public static abstract class Stub extends Binder implements IProviderInfoService {
        private static final String DESCRIPTOR = "android.support.wearable.complications.IProviderInfoService";
        static final int TRANSACTION_getProviderInfos = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IProviderInfoService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IProviderInfoService)) {
                return (IProviderInfoService) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            ComponentName _arg0;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    int[] _arg1 = data.createIntArray();
                    ComplicationProviderInfo[] _result = getProviderInfos(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeTypedArray(_result, 1);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements IProviderInfoService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // android.support.wearable.complications.IProviderInfoService
            public ComplicationProviderInfo[] getProviderInfos(ComponentName watchFaceComponent, int[] ids) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (watchFaceComponent != null) {
                        _data.writeInt(1);
                        watchFaceComponent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeIntArray(ids);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    ComplicationProviderInfo[] _result = (ComplicationProviderInfo[]) _reply.createTypedArray(ComplicationProviderInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
