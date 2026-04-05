package android.support.wearable.notifications;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes33.dex */
public interface IBridgingManagerService extends IInterface {
    void setBridgingConfig(Bundle bundle) throws RemoteException;

    public static abstract class Stub extends Binder implements IBridgingManagerService {
        private static final String DESCRIPTOR = "android.support.wearable.notifications.IBridgingManagerService";
        static final int TRANSACTION_setBridgingConfig = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IBridgingManagerService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IBridgingManagerService)) {
                return (IBridgingManagerService) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Bundle _arg0;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    setBridgingConfig(_arg0);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements IBridgingManagerService {
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

            @Override // android.support.wearable.notifications.IBridgingManagerService
            public void setBridgingConfig(Bundle bridgingConfig) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bridgingConfig != null) {
                        _data.writeInt(1);
                        bridgingConfig.writeToParcel(_data, 0);
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
        }
    }
}
