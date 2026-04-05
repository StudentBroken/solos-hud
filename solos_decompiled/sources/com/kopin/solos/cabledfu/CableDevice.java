package com.kopin.solos.cabledfu;

import android.content.Context;
import android.os.Handler;
import com.kopin.solos.cabledfu.CableDfu;
import com.kopin.solos.cabledfu.CableScanner;
import com.kopin.solos.cabledfu.DfuPackets;

/* JADX INFO: loaded from: classes38.dex */
public class CableDevice {
    private static CableConnectionListener mConnectionCallback;
    private static Context mContext;
    private static CableDfu mDfu;
    private static CableDfu.DfuResponseListener mDfuCallback;
    private static CableScanner.DiscoveryListener mDiscoveryListener;
    private static Handler mReconnectHandler;
    private static CableScanner mScanner;
    private static final CableDfu.DfuResponseListener mCableListener = new CableDfu.DfuResponseListener() { // from class: com.kopin.solos.cabledfu.CableDevice.3
        @Override // com.kopin.solos.cabledfu.CableDfu.DfuResponseListener
        public void onConnected(boolean dfuMode) {
            if (CableDevice.mConnectionCallback != null) {
                CableDevice.mConnectionCallback.onConnected(dfuMode);
            }
        }

        @Override // com.kopin.solos.cabledfu.CableDfu.DfuResponseListener
        public void onReady() {
            if (CableDevice.mConnectionCallback != null) {
                CableDevice.mConnectionCallback.onReady();
            }
        }

        @Override // com.kopin.solos.cabledfu.CableDfu.DfuResponseListener
        public void onResponse(DfuPackets.DfuPacket pkt) {
        }

        @Override // com.kopin.solos.cabledfu.CableDfu.DfuResponseListener
        public void onDisconnected(boolean wasLoss) {
            if (CableDevice.mConnectionCallback != null) {
                CableDevice.mConnectionCallback.onDisconnected(wasLoss);
            }
        }
    };
    private static final CableDfu.DfuResponseListener mCableDFUListener = new CableDfu.DfuResponseListener() { // from class: com.kopin.solos.cabledfu.CableDevice.4
        @Override // com.kopin.solos.cabledfu.CableDfu.DfuResponseListener
        public void onConnected(boolean dfuMode) {
        }

        @Override // com.kopin.solos.cabledfu.CableDfu.DfuResponseListener
        public void onReady() {
            if (CableDevice.mDfuCallback != null) {
                CableDevice.mDfuCallback.onConnected(CableDevice.mDfu.isDfuMode());
            }
        }

        @Override // com.kopin.solos.cabledfu.CableDfu.DfuResponseListener
        public void onResponse(DfuPackets.DfuPacket pkt) {
            if (CableDevice.mDfuCallback != null) {
                CableDevice.mDfuCallback.onResponse(pkt);
            }
        }

        @Override // com.kopin.solos.cabledfu.CableDfu.DfuResponseListener
        public void onDisconnected(boolean wasLoss) {
            if (CableDevice.mDfuCallback != null) {
                CableDevice.mDfuCallback.onDisconnected(wasLoss);
            }
        }
    };
    private static char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public interface CableConnectionListener {
        void onConnected(boolean z);

        void onDisconnected(boolean z);

        void onReady();
    }

    public static void init(Context ctx) {
        mContext = ctx;
        mScanner = new CableScanner(ctx);
        mReconnectHandler = new Handler();
    }

    public static void startScan(CableScanner.DiscoveryListener cb) {
        mScanner.start(cb);
    }

    public static void stopScan() {
        mScanner.stop();
    }

    public static void connectTo(int cableId) {
        mScanner.find(cableId, false, new CableScanner.DiscoveryListener() { // from class: com.kopin.solos.cabledfu.CableDevice.1
            @Override // com.kopin.solos.cabledfu.CableScanner.DiscoveryListener
            public void onDeviceDiscovered(CableScanner.CableDeviceStub device) {
                CableDfu unused = CableDevice.mDfu = new CableDfu(CableDevice.mContext, device);
                CableDevice.mDfu.setCallback(CableDevice.mCableDFUListener);
                CableDevice.mDfu.connect();
            }
        });
    }

    public static void connectTo(CableScanner.CableDeviceStub dev, CableConnectionListener cb) {
        if (isConnected()) {
            mConnectionCallback = null;
            mDfu.disconnect();
        }
        if (dev != null) {
            mDfu = new CableDfu(mContext, dev);
            mConnectionCallback = cb;
            mDfu.setCallback(mCableListener);
            mDfu.connect();
            return;
        }
        mDfu = null;
    }

    public static boolean isConnected() {
        return mDfu != null && mDfu.isConnected();
    }

    public static void disconnect() {
        if (isConnected()) {
            if (isDfuMode()) {
                mDfu.writeReq(DfuPackets.discardAndReset(), true);
                mDfu.disconnect(true);
            } else {
                mDfu.disconnect();
            }
            mDfu = null;
        }
    }

    public static String getName() {
        return isConnected() ? mDfu.getName() : "";
    }

    public static String getVersion() {
        return isConnected() ? mDfu.getFirmwareVersion() : "";
    }

    public static String getProductName() {
        if (!isConnected()) {
            return "";
        }
        String ver = mDfu.getHardwareVersion();
        if (ver != null && ver.startsWith("2")) {
            return "Cable2";
        }
        return "Cable";
    }

    public static boolean isCableName(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }
        return name.startsWith("Solos") || name.startsWith("cable") || name.contains("Dfu");
    }

    static int idFromName(String name) {
        String[] s = name.split("#");
        if (s.length <= 1) {
            return 0;
        }
        int id = Integer.parseInt(s[1]);
        return id;
    }

    public static void setResponseListener(CableDfu.DfuResponseListener cb) {
        mDfuCallback = cb;
    }

    public static boolean isDfuMode() {
        return isConnected() && mDfu.isDfuMode();
    }

    static void beginDfu() {
        if (isConnected() && !mDfu.isDfuMode()) {
            mDfu.getCableId();
            mDfu.setCallback(mCableDFUListener);
            mDfu.expectDisconnection(true);
            mDfu.writeReq(DfuPackets.startDfu(), true);
        }
    }

    static int getCableId() {
        if (mDfu != null) {
            return mDfu.getCableId();
        }
        return 0;
    }

    static void endDfu(boolean acceptUpdate) {
        if (isConnected() && mDfu.isDfuMode()) {
            mDfu.getCableId();
            mDfu.expectDisconnection(true);
            mDfu.writeReq(acceptUpdate ? DfuPackets.activateAndReset() : DfuPackets.discardAndReset(), false);
        }
    }

    static void startDfu() {
        if (isConnected()) {
            mDfu.writeReq(DfuPackets.startDfu());
        }
    }

    static void setImageSize(int appSize) {
        if (isConnected()) {
            mDfu.writeReq(DfuPackets.sizePacket(appSize));
        }
    }

    static void initParams() {
        if (isConnected()) {
            mDfu.writeReq(DfuPackets.initParams(true));
        }
    }

    static void setCRC(int crc16) {
        if (isConnected()) {
            mDfu.writeReq(DfuPackets.crcPacket(crc16));
        }
    }

    static void setCRC(byte[] args) {
        if (isConnected()) {
            mDfu.writeReq(DfuPackets.crcPacket(args));
        }
    }

    static void endParams() {
        if (isConnected()) {
            mDfu.writeReq(DfuPackets.initParams(false));
        }
    }

    static void requestPacketReceipts(int numberOfPackets) {
        if (isConnected()) {
            mDfu.writeReq(DfuPackets.requestNotifcations(numberOfPackets));
        }
    }

    static void beginUpdate() {
        if (isConnected()) {
            mDfu.writeReq(DfuPackets.startUpload());
        }
    }

    static void flashBytes(byte[] bytes, boolean andWait) {
        if (isConnected() && bytes != null) {
            int offs = 0;
            while (offs < bytes.length) {
                int len = bytes.length - offs;
                if (len > 20) {
                    len = 20;
                }
                byte[] b = new byte[len];
                System.arraycopy(bytes, offs, b, 0, len);
                mDfu.writeReq(DfuPackets.writeData(b), andWait);
                offs += len;
            }
        }
    }

    static void verifyUpdate() {
        if (isConnected()) {
            mDfu.writeReq(DfuPackets.verifyUpload());
        }
    }

    static void setName() {
        if (isConnected() && !mDfu.isDfuMode()) {
            int cableId = mDfu.getCableId();
            mDfu.setCallback(mCableDFUListener);
            mDfu.writeReq(DfuPackets.setName(cableId));
        }
    }

    static void enterSecureDFu() {
        if (isConnected() && !mDfu.isDfuMode()) {
            mDfu.setCallback(mCableDFUListener);
            mDfu.expectDisconnection(true);
            mDfu.writeReq(DfuPackets.startDfu(), false);
        }
    }

    static void connectInDFUMode() {
        int cableId = mDfu.getCableId();
        mDfu.setCallback(null);
        mDfu = null;
        mScanner.find(cableId, true, new CableScanner.DiscoveryListener() { // from class: com.kopin.solos.cabledfu.CableDevice.2
            @Override // com.kopin.solos.cabledfu.CableScanner.DiscoveryListener
            public void onDeviceDiscovered(CableScanner.CableDeviceStub device) {
                CableDfu unused = CableDevice.mDfu = new CableDfu(CableDevice.mContext, device);
                CableDevice.mDfu.setCallback(CableDevice.mCableDFUListener);
                CableDevice.mReconnectHandler.postDelayed(new Runnable() { // from class: com.kopin.solos.cabledfu.CableDevice.2.1
                    @Override // java.lang.Runnable
                    public void run() {
                        CableDevice.mDfu.connect();
                    }
                }, 2000L);
            }
        });
    }

    static void exitSecureDfu() {
        if (isConnected() && mDfu.isDfuMode()) {
            mDfu.getCableId();
            mDfu.setCallback(null);
            mDfu.disconnect(false);
            mDfu = null;
        }
    }

    static void exitDfuOnError() {
        if (isConnected()) {
            mDfu.setCallback(null);
            mDfu.disconnect(false);
            mDfu = null;
        }
    }

    static void readCommandObjectInfo() {
        if (isConnected()) {
            mDfu.writeReq(DfuPackets.readCommandObjectInfo());
        }
    }

    static void createCommandObject(int size) {
        if (isConnected()) {
            mDfu.writeReq(DfuPackets.createCommandObject(size));
        }
    }

    static void readDataObjectInfo() {
        if (isConnected()) {
            mDfu.writeReq(DfuPackets.readDataObjectInfo());
        }
    }

    static void createDataObject(int size) {
        if (isConnected()) {
            mDfu.writeReq(DfuPackets.createDataObject(size));
        }
    }

    static void setPacketReceiptNotification(short value) {
        if (isConnected()) {
            mDfu.writeReq(DfuPackets.setPacketReceiptNotification(value));
        }
    }

    static void sendExecuteCommand() {
        if (isConnected()) {
            mDfu.writeReq(DfuPackets.executeCommand());
        }
    }

    static void sendInitPacket(byte[] data) {
        flashBytes(data, true);
    }

    static void sendCalculateChecksum() {
        if (isConnected()) {
            mDfu.writeReq(DfuPackets.calculateChecksum());
        }
    }

    public static String getDfuMacAddress(String mac) {
        return addToMacAddress(mac, 1);
    }

    private static String addToMacAddress(String mac, int number) {
        String hexStr = mac.replace(":", "");
        long macNumber = Long.parseLong(hexStr, 16);
        String newMacHex = String.format("%06x", Long.valueOf(macNumber + ((long) number)));
        StringBuilder sb = new StringBuilder(newMacHex.substring(0, 2));
        for (int i = 2; i <= 10; i += 2) {
            sb.append(":").append(newMacHex.substring(i, i + 2));
        }
        return sb.toString();
    }

    public static String bytesToString(byte[] data) {
        StringBuilder sb = new StringBuilder("[");
        boolean init = false;
        if (data != null) {
            for (byte b : data) {
                if (init) {
                    sb.append(", ");
                }
                sb.append('\"');
                sb.append(HEX_CHARS[(b >> 4) & 15]);
                sb.append(HEX_CHARS[b & 15]);
                sb.append('\"');
                init = true;
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
