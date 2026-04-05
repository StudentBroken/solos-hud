package com.kopin.pupil;

import android.bluetooth.BluetoothDevice;
import android.support.wearable.watchface.WatchFaceService;
import android.util.Log;
import com.kopin.accessory.AudioCodec;
import com.kopin.accessory.base.Connection;
import com.kopin.accessory.base.Packet;
import com.kopin.accessory.base.PacketReceivedListener;
import com.kopin.accessory.base.PacketType;
import com.kopin.accessory.packets.ActionType;
import com.kopin.accessory.packets.CapabilityPacketContent;
import com.kopin.accessory.packets.CapabilityType;
import com.kopin.accessory.packets.StatusPacketContent;
import com.kopin.accessory.packets.StatusType;
import com.kopin.pupil.PupilConnector;
import com.kopin.pupil.bluetooth.BluetoothConnector;
import com.kopin.pupil.bluetooth.DeviceLister;
import com.kopin.pupil.util.PhoneUtils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/* JADX INFO: loaded from: classes25.dex */
public class PupilDevice {
    private static PupilConnector mConnector;
    private static PupilConnector mMirrorConnector;
    private static String TAG = "PupilDevice";
    private static DeviceStatus mDeviceStatus = new DeviceStatus();
    private static boolean mDeviceOnPower = false;
    private static DeviceInfo mDeviceInfo = new DeviceInfo();
    static boolean mLowColorScreen = false;

    static {
        PhoneUtils.init();
    }

    public static void tryConnect(BluetoothDevice device, DeviceLister.ConnectionListener listener, boolean retry) {
        disconnect();
        try {
            mConnector = new BluetoothConnector(device, listener);
            mConnector.setRetry(retry);
            listener.onAttemptConnection(device);
            mConnector.start();
        } catch (IOException e) {
            Log.e("tryConnect", "Couldn't create connection. is BT enabled?");
        }
    }

    public static BluetoothDevice getDevice() {
        if (mConnector instanceof BluetoothConnector) {
            return ((BluetoothConnector) mConnector).getDevice();
        }
        return null;
    }

    public static boolean isConnected() {
        return mConnector != null && mConnector.isConnected();
    }

    public static boolean isMirrorConnected() {
        return mMirrorConnector != null && mMirrorConnector.isConnected();
    }

    public static boolean isReconnecting() {
        return mConnector != null && mConnector.isReconnecting();
    }

    public static DeviceInfo currentDeviceInfo() {
        if (mDeviceInfo.isEmpty() && mConnector != null) {
            mConnector.getDefaultCaps();
        }
        return mDeviceInfo;
    }

    public static DeviceStatus currentDeviceStatus() {
        return mDeviceStatus;
    }

    public static void sendDeviceStatus(DeviceStatus status) {
        if (mConnector != null) {
            mConnector.setStatus(status);
        }
    }

    public static void setName(String name) {
        if (mConnector != null) {
            mConnector.sendDeviceName(name);
        }
    }

    public static void disconnect() {
        if (mConnector != null) {
            mConnector.kill();
            mConnector = null;
            mDeviceInfo = new DeviceInfo();
            mDeviceStatus = new DeviceStatus();
        }
    }

    public static void disconnectMirror() {
        if (mMirrorConnector != null) {
            mMirrorConnector.kill();
            mMirrorConnector = null;
        }
    }

    public static void wakeUp() {
        if (mConnector != null) {
            mConnector.sendWake();
        }
        if (mMirrorConnector != null) {
            mMirrorConnector.sendWake();
        }
    }

    public static void gotoSleep() {
        if (mConnector != null) {
            mConnector.sendSleep();
        }
        if (mMirrorConnector != null) {
            mMirrorConnector.sendSleep();
        }
    }

    public static void setLowColorMode(boolean onOrOff) {
        mLowColorScreen = onOrOff;
        if (mConnector != null) {
            mConnector.sendDisplayPowerMode(onOrOff);
        }
    }

    public static boolean isLowColorMode() {
        return mLowColorScreen;
    }

    public static void enableMics() {
        enableMics(false);
    }

    public static void enableMics(boolean stereo) {
        if (mConnector != null) {
            mConnector.enableAudio(stereo ? 2 : 1);
        }
    }

    public static void enableMics(boolean stereo, AudioCodec codec) {
        if (mConnector != null) {
            mConnector.enableAudio(stereo ? 2 : 1, codec);
        }
    }

    public static void disableMics() {
        if (mConnector != null) {
            mConnector.disableAudio();
        }
    }

    static void enableDecodeAudio(boolean onOrOff) {
        if (mConnector != null) {
            mConnector.setEnableDecoder(onOrOff);
        }
    }

    static boolean hasConnector() {
        return mConnector != null;
    }

    static boolean isPrimaryConnector(BluetoothConnector test) {
        return mConnector != null && test == mConnector;
    }

    static void sendPacket(Packet packet) {
        if (mConnector != null) {
            mConnector.mConnection.enqueuePacket(packet);
        }
    }

    static void connected(PupilConnector connector) {
        if (mConnector != connector) {
            disconnect();
            mConnector = connector;
        }
    }

    static void connectedMirror(PupilConnector connector) {
        mMirrorConnector = connector;
    }

    static void setResponseListener(PupilConnector.ResponseListener listener) {
        if (mConnector != null) {
            mConnector.setResponseListener(listener);
        }
    }

    static void registerForPackets(PacketType type, PacketReceivedListener cb) {
        if (mConnector != null) {
            mConnector.listenForPackets(type, cb);
        }
    }

    static void unregisterForPackets(PacketType type, PacketReceivedListener cb) {
        if (mConnector != null) {
            mConnector.unlistenForPackets(type, cb);
        }
    }

    static void sendAudio(byte[] data, int length, AudioCodec codec, int sampleRate) {
        if (mConnector != null) {
            mConnector.sendAudio(data, length, codec, sampleRate);
        }
    }

    static void sendAudioEnd(int endId) {
        if (mConnector != null) {
            mConnector.sendAudioEnd(endId);
        }
    }

    static void stopAudioSending() {
        if (mConnector != null) {
            mConnector.stopAudioSending();
        }
    }

    static void sendRLE565(short x, short y, short width, short height, byte[] data, int length) {
        if (mConnector != null) {
            mConnector.sendRLE565(x, y, width, height, data, length);
        }
        if (mMirrorConnector != null) {
            mMirrorConnector.sendRLE565(x, y, width, height, data, length);
        }
    }

    static void sendRGB111(short x, short y, short width, short height, byte[] data, int length) {
        if (mConnector != null) {
            mConnector.sendRGB111(x, y, width, height, data, length);
        }
        if (mMirrorConnector != null) {
            mMirrorConnector.sendRGB111(x, y, width, height, data, length);
        }
    }

    public static void requestHFPStatus() {
        if (mConnector != null) {
            mConnector.sendHFPQuery();
        }
    }

    public static void sendDialPacket(String numberToDial) {
        if (mConnector != null) {
            mConnector.sendDialNumberPacket(numberToDial);
        }
    }

    public static void sendAnswerCallPacket() {
        if (mConnector != null) {
            mConnector.sendAnswerCallPacket();
        }
    }

    public static void sendEndCallPacket() {
        if (mConnector != null) {
            mConnector.sendEndCallPacket();
        }
    }

    public static void sendAction(ActionType action) {
        if (mConnector != null) {
            mConnector.sendAction(action.getValue());
        }
    }

    public static void requestCapabilities(CapabilityType... caps) {
        if (mConnector != null) {
            mConnector.getCaps(caps);
        }
    }

    public static void requestStatus() {
        if (mConnector != null) {
            mConnector.getStatus();
        }
    }

    public static void requestStatus(StatusType... types) {
        if (mConnector != null) {
            mConnector.getStatus(types);
        }
    }

    static void setPowerStatus(boolean onCharge) {
        mDeviceOnPower = onCharge;
        mDeviceStatus.onCharge = onCharge;
    }

    public static void setDebugPacketWatcher(Connection.PacketWatcher packetWatcher) {
        if (mConnector != null) {
            mConnector.setPacketWatcher(packetWatcher);
        }
    }

    public static void enableVAD(boolean enable) {
        if (mConnector != null) {
            mConnector.enableVAD(enable);
        }
    }

    public static DeviceStatus processStatusPacket(StatusPacketContent statusPacket) {
        if (statusPacket.hasEntry(StatusType.POWER_AVAILABLE.getFlag())) {
            byte chargeStatus = statusPacket.getByte(StatusType.POWER_AVAILABLE.getFlag());
            setPowerStatus((chargeStatus & 1) == 1);
        }
        DeviceStatus deviceStatus = new DeviceStatus(mDeviceOnPower);
        if (statusPacket.hasEntry(StatusType.BATTERY_LEVEL.getFlag())) {
            byte batteryLevel = statusPacket.getByte(StatusType.BATTERY_LEVEL.getFlag());
            deviceStatus.setBattery(batteryLevel);
            mDeviceStatus.setBattery(batteryLevel);
        }
        if (statusPacket.hasEntry(StatusType.DISPLAY_BRIGHTNESS.getFlag())) {
            byte brightness = statusPacket.getByte(StatusType.DISPLAY_BRIGHTNESS.getFlag());
            deviceStatus.setBrightness(brightness);
            mDeviceStatus.setBrightness(brightness);
        }
        if (statusPacket.hasEntry(StatusType.LEFT_MICROPHONE_LEVEL.getFlag())) {
            byte leftMic = statusPacket.getByte(StatusType.LEFT_MICROPHONE_LEVEL.getFlag());
            deviceStatus.setLeftMic(leftMic);
            mDeviceStatus.setLeftMic(leftMic);
        }
        if (statusPacket.hasEntry(StatusType.RIGHT_MICROPHONE_LEVEL.getFlag())) {
            byte rightMic = statusPacket.getByte(StatusType.RIGHT_MICROPHONE_LEVEL.getFlag());
            deviceStatus.setRightMic(rightMic);
            mDeviceStatus.setRightMic(rightMic);
        }
        if (statusPacket.hasEntry(StatusType.SPEAKER_VOLUME.getFlag())) {
            byte value = statusPacket.getByte(StatusType.SPEAKER_VOLUME.getFlag());
            deviceStatus.setVolume(value);
            mDeviceStatus.setVolume(value);
        }
        if (statusPacket.hasEntry(StatusType.AMBIENT_LIGHT.getFlag())) {
            int value2 = statusPacket.getInt(StatusType.AMBIENT_LIGHT.getFlag());
            deviceStatus.setLux(value2);
            mDeviceStatus.setLux(value2);
        }
        return deviceStatus;
    }

    public static DeviceInfo processCapabilityPacket(CapabilityPacketContent capsPacket) {
        if (capsPacket.hasEntry(CapabilityType.DEVICE_NAME.getFlag())) {
            mDeviceInfo.mName = capsPacket.getString(CapabilityType.DEVICE_NAME.getFlag());
        }
        if (capsPacket.hasEntry(CapabilityType.MODEL.getFlag())) {
            mDeviceInfo.mModel = capsPacket.getString(CapabilityType.MODEL.getFlag());
        }
        if (capsPacket.hasEntry(CapabilityType.FIRMWARE_VERSION.getFlag())) {
            byte[] version = capsPacket.getByteArray(CapabilityType.FIRMWARE_VERSION.getFlag());
            mDeviceInfo.mVersion = String.format("%d.%d.%d", Byte.valueOf(version[3]), Byte.valueOf(version[2]), Byte.valueOf(version[1]));
        }
        if (capsPacket.hasEntry(CapabilityType.HEADSET_SERIAL_NUMBER.getFlag())) {
            mDeviceInfo.mSerial = "--";
            try {
                byte[] serialBytes = capsPacket.getByteArray(CapabilityType.HEADSET_SERIAL_NUMBER.getFlag());
                if (serialBytes != null) {
                    mDeviceInfo.mSerial = new String(serialBytes, "UTF-8");
                }
            } catch (UnsupportedEncodingException e) {
            }
        }
        if (capsPacket.hasEntry(CapabilityType.ANT_MODULE.getFlag())) {
            byte[] info = capsPacket.getByteArray(CapabilityType.ANT_MODULE.getFlag());
            if (info[0] == 1) {
                mDeviceInfo.mAntBridgeId = ((info[4] & 255) << 16) | ((info[3] & 255) << 8) | (info[2] & 255);
            } else {
                mDeviceInfo.mAntBridgeId = 0;
            }
            if (capsPacket.hasEntry(CapabilityType.ANT_MODULE_VERSION.getFlag())) {
                byte[] version2 = capsPacket.getByteArray(CapabilityType.ANT_MODULE_VERSION.getFlag());
                mDeviceInfo.mAntBridgeVersion = String.format("%d.%d.%d", Byte.valueOf(version2[3]), Byte.valueOf(version2[2]), Byte.valueOf(version2[1]));
            }
        }
        if (capsPacket.hasEntry(CapabilityType.ENABLE_HFP_CONNECTION.getFlag())) {
            byte hfp = capsPacket.getByte(CapabilityType.ENABLE_HFP_CONNECTION.getFlag());
            Log.d("PupilDevice", "hfp: " + ((int) hfp));
            if (mConnector != null) {
                mConnector.sendHFPQuery();
            } else {
                Log.e("PupilDevice", "Received CAPS packet from a dead connection?");
            }
        }
        return mDeviceInfo;
    }

    public static class DeviceInfo {
        public int mAntBridgeId;
        public String mAntBridgeVersion;
        public String mModel;
        public String mName;
        public String mSerial;
        public String mVersion;

        boolean isEmpty() {
            return this.mName == null || this.mModel == null || this.mVersion == null;
        }
    }

    public static class DeviceStatus {
        private boolean hasBattery;
        private boolean hasBrightness;
        private boolean hasLeftMic;
        private boolean hasLux;
        private boolean hasRightMic;
        private boolean hasVolume;
        private byte mBattery;
        private byte mBrightness;
        private byte mLeftMic;
        private int mLux;
        private byte mRightMic;
        private byte mVolume;
        private boolean onCharge;

        public DeviceStatus() {
        }

        private DeviceStatus(boolean charging) {
            this.onCharge = charging;
        }

        public byte getBattery() {
            return this.mBattery;
        }

        public boolean isOnCharge() {
            return this.onCharge;
        }

        public byte getBrightness() {
            return this.mBrightness;
        }

        public byte getLeftMic() {
            return this.mLeftMic;
        }

        public byte getRightMic() {
            return this.mRightMic;
        }

        public byte getVolume() {
            return this.mVolume;
        }

        public int getLux() {
            return this.mLux;
        }

        public boolean hasBattery() {
            return this.hasBattery;
        }

        public boolean hasBrightness() {
            return this.hasBrightness;
        }

        public boolean hasLeftMic() {
            return this.hasLeftMic;
        }

        public boolean hasRightMic() {
            return this.hasRightMic;
        }

        public boolean hasVolume() {
            return this.hasVolume;
        }

        public boolean hasLux() {
            return this.hasLux;
        }

        public void setBattery(byte mBattery) {
            this.mBattery = mBattery;
            this.hasBattery = true;
        }

        public void setBrightness(byte mBrightness) {
            this.mBrightness = mBrightness;
            this.hasBrightness = true;
        }

        public void setLeftMic(byte mLeftMic) {
            this.mLeftMic = mLeftMic;
            this.hasLeftMic = true;
        }

        public void setRightMic(byte mRightMic) {
            this.mRightMic = mRightMic;
            this.hasRightMic = true;
        }

        public void setVolume(byte mVolume) {
            this.mVolume = mVolume;
            this.hasVolume = true;
        }

        public void setLux(int lux) {
            this.mLux = lux;
            this.hasLux = true;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("DeviceStatus { ");
            if (this.hasBattery) {
                sb.append("battery: ").append(this.mBattery & 255).append(" (").append(this.onCharge ? WatchFaceService.STATUS_CHARGING : "discharging").append("), ");
            } else {
                sb.append("no battery, ");
            }
            if (this.hasBrightness) {
                sb.append("brightness: ").append(this.mBrightness & 255).append(", ");
            } else {
                sb.append("no brightness, ");
            }
            if (this.hasVolume) {
                sb.append("volume: ").append(this.mVolume & 255).append(", ");
            } else {
                sb.append("no volume, ");
            }
            if (this.hasLux) {
                sb.append("ambient light: ").append(this.mLux).append(" }");
            } else {
                sb.append("no ambient light }");
            }
            return sb.toString();
        }
    }

    public static void sendDebugTTS(String tts) {
        if (mMirrorConnector != null) {
            mMirrorConnector.sendDebugTTS(tts);
        }
    }
}
