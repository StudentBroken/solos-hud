package com.google.android.gms.analytics.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes49.dex */
public interface IAnalyticsService extends IInterface {
    void clearHits() throws RemoteException;

    void sendHit(Map map, long j, String str, List<Command> list) throws RemoteException;

    public static abstract class Stub extends Binder implements IAnalyticsService {
        private static final String DESCRIPTOR = "com.google.android.gms.analytics.internal.IAnalyticsService";
        static final int TRANSACTION_clearHits = 2;
        static final int TRANSACTION_sendHit = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAnalyticsService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IAnalyticsService)) {
                return (IAnalyticsService) iin;
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
                    ClassLoader cl = getClass().getClassLoader();
                    Map _arg0 = data.readHashMap(cl);
                    long _arg1 = data.readLong();
                    String _arg2 = data.readString();
                    List<Command> _arg3 = data.createTypedArrayList(Command.CREATOR);
                    sendHit(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    clearHits();
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements IAnalyticsService {
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

            @Override // com.google.android.gms.analytics.internal.IAnalyticsService
            public void sendHit(Map wireParams, long hitTimeInMilliseconds, String path, List<Command> commands) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeMap(wireParams);
                    _data.writeLong(hitTimeInMilliseconds);
                    _data.writeString(path);
                    _data.writeTypedList(commands);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.google.android.gms.analytics.internal.IAnalyticsService
            public void clearHits() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
