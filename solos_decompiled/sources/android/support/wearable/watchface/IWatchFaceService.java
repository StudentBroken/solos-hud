package android.support.wearable.watchface;

import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes33.dex */
public interface IWatchFaceService extends IInterface {
    void setActiveComplications(int[] iArr, boolean z) throws RemoteException;

    void setDefaultComplicationProvider(int i, ComponentName componentName, int i2) throws RemoteException;

    void setDefaultSystemComplicationProvider(int i, int i2, int i3) throws RemoteException;

    void setStyle(WatchFaceStyle watchFaceStyle) throws RemoteException;

    public static abstract class Stub extends Binder implements IWatchFaceService {
        private static final String DESCRIPTOR = "android.support.wearable.watchface.IWatchFaceService";
        static final int TRANSACTION_setActiveComplications = 2;
        static final int TRANSACTION_setDefaultComplicationProvider = 3;
        static final int TRANSACTION_setDefaultSystemComplicationProvider = 4;
        static final int TRANSACTION_setStyle = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IWatchFaceService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IWatchFaceService)) {
                return (IWatchFaceService) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            ComponentName _arg1;
            WatchFaceStyle _arg0;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = WatchFaceStyle.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    setStyle(_arg0);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    int[] _arg02 = data.createIntArray();
                    boolean _arg12 = data.readInt() != 0;
                    setActiveComplications(_arg02, _arg12);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg1 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    int _arg2 = data.readInt();
                    setDefaultComplicationProvider(_arg03, _arg1, _arg2);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg04 = data.readInt();
                    int _arg13 = data.readInt();
                    int _arg22 = data.readInt();
                    setDefaultSystemComplicationProvider(_arg04, _arg13, _arg22);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements IWatchFaceService {
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

            @Override // android.support.wearable.watchface.IWatchFaceService
            public void setStyle(WatchFaceStyle style) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (style != null) {
                        _data.writeInt(1);
                        style.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.support.wearable.watchface.IWatchFaceService
            public void setActiveComplications(int[] ids, boolean updateAll) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(ids);
                    _data.writeInt(updateAll ? 1 : 0);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.support.wearable.watchface.IWatchFaceService
            public void setDefaultComplicationProvider(int watchFaceComplicationId, ComponentName provider, int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(watchFaceComplicationId);
                    if (provider != null) {
                        _data.writeInt(1);
                        provider.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(type);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.support.wearable.watchface.IWatchFaceService
            public void setDefaultSystemComplicationProvider(int watchFaceComplicationId, int systemProvider, int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(watchFaceComplicationId);
                    _data.writeInt(systemProvider);
                    _data.writeInt(type);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
