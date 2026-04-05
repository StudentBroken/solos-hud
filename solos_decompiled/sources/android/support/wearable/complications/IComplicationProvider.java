package android.support.wearable.complications;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes33.dex */
public interface IComplicationProvider extends IInterface {
    void onComplicationActivated(int i, int i2, IBinder iBinder) throws RemoteException;

    void onComplicationDeactivated(int i) throws RemoteException;

    void onUpdate(int i, int i2, IBinder iBinder) throws RemoteException;

    public static abstract class Stub extends Binder implements IComplicationProvider {
        private static final String DESCRIPTOR = "android.support.wearable.complications.IComplicationProvider";
        static final int TRANSACTION_onComplicationActivated = 3;
        static final int TRANSACTION_onComplicationDeactivated = 2;
        static final int TRANSACTION_onUpdate = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IComplicationProvider asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IComplicationProvider)) {
                return (IComplicationProvider) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0 = data.readInt();
                    int _arg1 = data.readInt();
                    IBinder _arg2 = data.readStrongBinder();
                    onUpdate(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg02 = data.readInt();
                    onComplicationDeactivated(_arg02);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    int _arg12 = data.readInt();
                    IBinder _arg22 = data.readStrongBinder();
                    onComplicationActivated(_arg03, _arg12, _arg22);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements IComplicationProvider {
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

            @Override // android.support.wearable.complications.IComplicationProvider
            public void onUpdate(int complicationId, int type, IBinder manager) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(complicationId);
                    _data.writeInt(type);
                    _data.writeStrongBinder(manager);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.support.wearable.complications.IComplicationProvider
            public void onComplicationDeactivated(int complicationId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(complicationId);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.support.wearable.complications.IComplicationProvider
            public void onComplicationActivated(int complicationId, int type, IBinder manager) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(complicationId);
                    _data.writeInt(type);
                    _data.writeStrongBinder(manager);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
