package com.kopin.pupil;

import android.os.Handler;
import android.util.Log;
import com.kopin.accessory.base.Connection;
import com.kopin.accessory.base.Packet;
import com.kopin.accessory.base.PacketReceivedListener;
import com.kopin.accessory.base.PacketType;
import com.kopin.accessory.packets.AntResponseContent;
import com.kopin.accessory.packets.CapabilityPacketContent;
import com.kopin.accessory.packets.CapabilityType;
import com.kopin.accessory.packets.base.BytePacketContent;
import com.kopin.pupil.PupilDevice;
import com.kopin.solos.cabledfu.CableFlash;
import com.kopin.solos.sensors.SensorsConnector;
import com.kopin.solos.storage.settings.Prefs;
import java.util.Timer;

/* JADX INFO: loaded from: classes37.dex */
public class SolosDevice extends PupilDevice {
    private static final byte CMD_ANT_BRIDGE_HUP = 3;
    private static final byte CMD_ANT_BRIDGE_OFF = 2;
    private static final byte CMD_ANT_BRIDGE_ON = 1;
    private static final String TAG = "SolosDevice";
    private static Handler mReconnectHandler;
    private static Product CURRENT_PRODUCT = Product.NONE;
    private static Timer mInfoTimeout = null;
    private static final PacketReceivedListener mInfoResponseListener = new PacketReceivedListener() { // from class: com.kopin.pupil.SolosDevice.1
        @Override // com.kopin.accessory.base.PacketReceivedListener
        public void onPacketReceived(Connection connection, Packet packet) {
            CapabilityPacketContent capsPacket = (CapabilityPacketContent) packet.getContent();
            if (capsPacket.hasEntry(CapabilityType.ANT_MODULE.getFlag())) {
                byte[] info = capsPacket.getByteArray(CapabilityType.ANT_MODULE.getFlag());
                int antBridgeId = info[0] == 1 ? ((info[4] & 255) << 16) | ((info[3] & 255) << 8) | (info[2] & 255) : 0;
                Log.d(SolosDevice.TAG, String.format("CABLE ID: %02x : %02x %02x %02x -> %d", Byte.valueOf(info[0]), Byte.valueOf(info[4]), Byte.valueOf(info[3]), Byte.valueOf(info[2]), Integer.valueOf(antBridgeId)));
            } else {
                Log.e(SolosDevice.TAG, "Capabilities didn't Have a CABLE ID field");
            }
            synchronized (this) {
                notify();
            }
        }
    };
    private static boolean isListening = false;
    private static final PacketReceivedListener mAntResponseListener = new PacketReceivedListener() { // from class: com.kopin.pupil.SolosDevice.2
        @Override // com.kopin.accessory.base.PacketReceivedListener
        public void onPacketReceived(Connection connection, Packet packet) {
            AntResponseContent resp = (AntResponseContent) packet.getContent();
            switch (AnonymousClass5.$SwitchMap$com$kopin$accessory$packets$AntResponseContent$ResponseType[resp.getType().ordinal()]) {
                case 1:
                    Log.d(SolosDevice.TAG, "ANT+ OK");
                    PupilDevice.DeviceInfo info = PupilDevice.currentDeviceInfo();
                    if (!SensorsConnector.isAntBridgeActive(info.mAntBridgeId)) {
                        SensorsConnector.connectAntBridge(info.mAntBridgeId);
                    }
                    break;
                case 2:
                    Log.d(SolosDevice.TAG, "ANT+ STARTING");
                    break;
                case 3:
                    Log.d(SolosDevice.TAG, "ANT+ ADVERTISING");
                    SolosDevice.checkForAntBridge();
                    break;
                case 4:
                    Log.d(SolosDevice.TAG, "ANT+ CONNECT " + resp.getDeviceId());
                    break;
                case 5:
                    Log.d(SolosDevice.TAG, "ANT+ DISCONNECT " + resp.getDeviceId());
                    break;
                case 6:
                    Log.d(SolosDevice.TAG, "ANT+ POWERING DOWN");
                    SolosDevice.wakeAntBridge();
                    break;
                default:
                    Log.d(SolosDevice.TAG, "ANT+ response: " + resp.getType());
                    break;
            }
        }
    };
    private static boolean isListeningForButtons = false;
    private static AntButtonResponseListener mButtonsCallback = null;
    private static final PacketReceivedListener mAntButtonResponseListener = new PacketReceivedListener() { // from class: com.kopin.pupil.SolosDevice.3
        @Override // com.kopin.accessory.base.PacketReceivedListener
        public void onPacketReceived(Connection connection, Packet packet) {
            AntResponseContent resp = (AntResponseContent) packet.getContent();
            switch (AnonymousClass5.$SwitchMap$com$kopin$accessory$packets$AntResponseContent$ResponseType[resp.getType().ordinal()]) {
                case 7:
                    Log.d(SolosDevice.TAG, "ANT+ Buttons Enabled");
                    break;
                case 8:
                    Log.d(SolosDevice.TAG, "ANT+ Buttons Disabled");
                    break;
                case 9:
                    Log.d(SolosDevice.TAG, "ANT+ Button Command");
                    AntResponseContent.AntCommand cmd = resp.getCommand();
                    if (cmd != null && SolosDevice.mButtonsCallback != null) {
                        SolosDevice.mButtonsCallback.onCommand(cmd.Sequence, cmd.Command);
                        break;
                    }
                    break;
            }
        }
    };
    private static final Runnable mReconnectCable = new Runnable() { // from class: com.kopin.pupil.SolosDevice.4
        @Override // java.lang.Runnable
        public void run() {
        }
    };

    public interface AntButtonResponseListener {
        void onCommand(int i, int i2);
    }

    public enum Product {
        NONE("none"),
        HDK("soloshdk"),
        SOLOS("solos"),
        SOLOS2("solos2");

        private final String mName;

        Product(String name) {
            this.mName = name;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static Product fromModelString(String model) {
            if (model != null) {
                if (model.startsWith("Solos HDK")) {
                    return HDK;
                }
                if (model.startsWith("Solos2")) {
                    return SOLOS2;
                }
                if (model.startsWith("Solos")) {
                    return SOLOS;
                }
                Log.e(SolosDevice.TAG, "Unknown Hardware model: " + model);
            } else {
                Log.e(SolosDevice.TAG, "Hardware didn't report a model");
            }
            return NONE;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.mName;
        }
    }

    public static Product getHeadsetModel() {
        String model = PupilDevice.currentDeviceInfo().mModel;
        Prefs.setModel(model);
        CURRENT_PRODUCT = Product.fromModelString(model);
        return CURRENT_PRODUCT;
    }

    public static Product getLastHeadsetModel() {
        String model = Prefs.getLastModel();
        Prefs.setModel(model);
        CURRENT_PRODUCT = Product.fromModelString(model);
        return CURRENT_PRODUCT;
    }

    public static String getCableProductName() {
        switch (CURRENT_PRODUCT) {
            case SOLOS:
                return "Cable";
            case SOLOS2:
                return "Cable2";
            default:
                return "none";
        }
    }

    public static void forgetHeadset() {
        closeAntBridge();
        PupilDevice.disconnect();
        Prefs.removedPairedHeadset();
        Prefs.setModel(null);
        CURRENT_PRODUCT = Product.NONE;
    }

    public static int getAntBridgeId() {
        if (isConnected()) {
            return currentDeviceInfo().mAntBridgeId;
        }
        return 0;
    }

    public static void wakeAntBridge(boolean isSolosConnected) {
        if (isSolosConnected) {
            if (!isListening) {
                registerForPackets(PacketType.ANT_RESPONSE, mAntResponseListener);
                isListening = true;
            }
            Log.d(TAG, "Wake ANT Bridge / CABLE");
            BytePacketContent cmd = new BytePacketContent((byte) 3);
            sendPacket(new Packet(PacketType.ANT_COMMAND, cmd));
            BytePacketContent cmd2 = new BytePacketContent((byte) 1);
            sendPacket(new Packet(PacketType.ANT_COMMAND, cmd2));
        }
    }

    public static void wakeAntBridge() {
        wakeAntBridge(isConnected());
    }

    public static void closeAntBridge() {
        if (isConnected()) {
            if (isListening) {
                unregisterForPackets(PacketType.ANT_RESPONSE, mAntResponseListener);
                isListening = false;
            }
            Log.d(TAG, "Close ANT Bridge / CABLE");
            BytePacketContent cmd = new BytePacketContent((byte) 2);
            sendPacket(new Packet(PacketType.ANT_COMMAND, cmd));
        }
    }

    public static void resetAntBridge() {
        if (isConnected()) {
            Log.d(TAG, "Reset ANT Bridge / CABLE");
            BytePacketContent cmd = new BytePacketContent((byte) 3);
            sendPacket(new Packet(PacketType.ANT_COMMAND, cmd));
        }
    }

    /* JADX INFO: renamed from: com.kopin.pupil.SolosDevice$5, reason: invalid class name */
    static /* synthetic */ class AnonymousClass5 {
        static final /* synthetic */ int[] $SwitchMap$com$kopin$accessory$packets$AntResponseContent$ResponseType = new int[AntResponseContent.ResponseType.values().length];

        static {
            try {
                $SwitchMap$com$kopin$accessory$packets$AntResponseContent$ResponseType[AntResponseContent.ResponseType.OK.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$kopin$accessory$packets$AntResponseContent$ResponseType[AntResponseContent.ResponseType.STARTING.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$kopin$accessory$packets$AntResponseContent$ResponseType[AntResponseContent.ResponseType.ADVERTISING.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$kopin$accessory$packets$AntResponseContent$ResponseType[AntResponseContent.ResponseType.CONNECT.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$kopin$accessory$packets$AntResponseContent$ResponseType[AntResponseContent.ResponseType.DISCONNECT.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$kopin$accessory$packets$AntResponseContent$ResponseType[AntResponseContent.ResponseType.SHUTDOWN.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$kopin$accessory$packets$AntResponseContent$ResponseType[AntResponseContent.ResponseType.GCD_ENABLED.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$kopin$accessory$packets$AntResponseContent$ResponseType[AntResponseContent.ResponseType.GCD_DISABLED.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$kopin$accessory$packets$AntResponseContent$ResponseType[AntResponseContent.ResponseType.GCD_COMMAND.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            $SwitchMap$com$kopin$pupil$SolosDevice$Product = new int[Product.values().length];
            try {
                $SwitchMap$com$kopin$pupil$SolosDevice$Product[Product.SOLOS.ordinal()] = 1;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$kopin$pupil$SolosDevice$Product[Product.SOLOS2.ordinal()] = 2;
            } catch (NoSuchFieldError e11) {
            }
        }
    }

    public static void listenForAntButtons(AntButtonResponseListener cb) {
        if (cb != null) {
            if (!isListeningForButtons) {
                registerForPackets(PacketType.ANT_RESPONSE, mAntButtonResponseListener);
                isListeningForButtons = true;
            }
        } else if (isListeningForButtons) {
            unregisterForPackets(PacketType.ANT_RESPONSE, mAntButtonResponseListener);
            isListeningForButtons = false;
        }
        mButtonsCallback = cb;
    }

    public static void onConnected() {
    }

    public static void onDisconnected() {
    }

    public static void checkForAntBridge() {
        int cableId = getAntBridgeId();
        checkForAntBridge(cableId);
    }

    public static void checkForAntBridge(int cableId) {
        if (!SensorsConnector.isAntBridgeActive(cableId) && !CableFlash.isActive()) {
            SensorsConnector.connectAntBridge(cableId);
        }
    }
}
