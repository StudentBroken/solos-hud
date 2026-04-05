package com.kopin.solos.sensors.btle;

import android.util.Log;
import com.facebook.internal.AnalyticsEvents;
import com.kopin.solos.sensors.btle.BTLESensorConfig;
import com.kopin.solos.sensors.btle.CableAnt;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* JADX INFO: loaded from: classes28.dex */
public class CableControl {

    enum Opcode {
        NOOP(0),
        SELECT_ACTIVE_SET(20),
        GET_ACTIVE_SET_INDEX(21),
        REQ_ACTIVE_DEV_SETTINGS(16),
        SET_DEV_SETTINGS(17),
        SAVE_DEV_SETTINGS(19),
        REQ_WILD_CARD_SCAN(18),
        CANCEL_WILD_CARD_SCAN(34),
        REQ_ANT_DISCOVERY(27),
        END_ANT_DISCOVERY(28),
        ANT_DISCOVERY_DATA(-86),
        REQ_SWITCH_OFF(22),
        REOPEN_CHANNELS(52),
        KEEP_ALIVE(53),
        OPEN_CHANNEL(56),
        CLOSE_CHANNEL(57),
        REQ_ANT_CALIBRATION(29),
        RESP_ANT_CALIBRATION(61),
        INVALID_PARAMETERS(253),
        UNKNOWN_COMMAND(254);

        private final byte mOpCode;

        Opcode(int opcode) {
            this.mOpCode = (byte) opcode;
        }

        public byte getOpCode() {
            return this.mOpCode;
        }

        public static Opcode fromByte(byte op) {
            for (Opcode o : values()) {
                if (o.mOpCode == op) {
                    return o;
                }
            }
            return NOOP;
        }
    }

    static abstract class Request extends BTLESensorConfig.ConfigControlRequest {
        private final byte mArg1;
        private final byte mArg2;
        protected final Opcode mReq;

        protected Request(Opcode req, byte arg1, byte arg2) {
            this.mReq = req;
            this.mArg1 = arg1;
            this.mArg2 = arg2;
        }

        @Override // com.kopin.solos.sensors.btle.BTLESensorConfig.ConfigControlRequest
        public String debugName() {
            return this.mReq.name() + " " + ((int) this.mArg1);
        }

        @Override // com.kopin.solos.sensors.btle.BTLESensorConfig.ConfigControlRequest
        public byte[] toValue() {
            return new byte[]{this.mReq.getOpCode(), this.mArg1, this.mArg2};
        }
    }

    static class RequestActiveSet extends Request {
        RequestActiveSet() {
            super(Opcode.GET_ACTIVE_SET_INDEX, (byte) 0, (byte) 0);
        }
    }

    static class SelectActiveSet extends Request {
        SelectActiveSet(int set) {
            super(Opcode.SELECT_ACTIVE_SET, (byte) set, (byte) 0);
        }
    }

    static class RequestSettings extends Request {
        RequestSettings(int channel, int set) {
            super(Opcode.REQ_ACTIVE_DEV_SETTINGS, (byte) set, (byte) channel);
        }
    }

    static class RequestScan extends Request {
        RequestScan(boolean startOrCancel, int channel) {
            super(startOrCancel ? Opcode.REQ_WILD_CARD_SCAN : Opcode.CANCEL_WILD_CARD_SCAN, (byte) channel, (byte) 7);
        }
    }

    static class ReopenChannels extends Request {
        ReopenChannels() {
            super(Opcode.REOPEN_CHANNELS, (byte) 0, (byte) 0);
        }
    }

    static class AntDiscovery extends Request {
        AntDiscovery(int duration) {
            super(Opcode.REQ_ANT_DISCOVERY, (byte) duration, (byte) 0);
        }
    }

    static class EndDiscovery extends Request {
        EndDiscovery() {
            super(Opcode.END_ANT_DISCOVERY, (byte) 0, (byte) 0);
        }
    }

    static class SaveSettings extends Request {
        SaveSettings() {
            super(Opcode.SAVE_DEV_SETTINGS, (byte) 0, (byte) 0);
        }
    }

    static class OpenChannel extends Request {
        OpenChannel(int slot) {
            super(Opcode.OPEN_CHANNEL, (byte) slot, (byte) 0);
        }
    }

    static class CloseChannel extends Request {
        CloseChannel(int slot) {
            super(Opcode.CLOSE_CHANNEL, (byte) slot, (byte) 0);
        }
    }

    static class SetDevSettings extends Request {
        private final int mDevId;
        private final byte mSet;
        private final byte mSlot;
        private final byte mTrans;
        private final CableAnt.DeviceType mType;

        SetDevSettings(int set, int slot, int devId, CableAnt.DeviceType type, int trans) {
            super(Opcode.SET_DEV_SETTINGS, type.deviceType(), (byte) 0);
            this.mSet = (byte) set;
            this.mSlot = (byte) slot;
            this.mDevId = devId;
            this.mType = type;
            this.mTrans = (byte) trans;
        }

        SetDevSettings(ParamsAntDiscovery scanData, ParamsDevSettings slotParams) {
            super(Opcode.SET_DEV_SETTINGS, scanData.mDevType.deviceType(), (byte) 0);
            this.mSet = (byte) slotParams.mSet;
            this.mSlot = (byte) slotParams.mChannel;
            this.mDevId = scanData.mDevId;
            this.mType = scanData.mDevType;
            this.mTrans = scanData.mTrans;
        }

        @Override // com.kopin.solos.sensors.btle.CableControl.Request, com.kopin.solos.sensors.btle.BTLESensorConfig.ConfigControlRequest
        public byte[] toValue() {
            ByteBuffer buffer = ByteBuffer.allocate(13);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            buffer.put(Opcode.SET_DEV_SETTINGS.getOpCode());
            buffer.put(this.mSet);
            buffer.put(this.mSlot);
            buffer.put((byte) 53);
            buffer.put(this.mType.deviceType());
            buffer.put((byte) 0);
            buffer.put(this.mTrans);
            buffer.putInt(this.mDevId);
            buffer.put((byte) (this.mSlot * 2));
            buffer.put((byte) 0);
            return buffer.array();
        }
    }

    static class RequestAntCalibration extends Request {
        private final boolean mManual;
        private final boolean mOnOrOff;
        private final int mSlot;

        RequestAntCalibration(int slot, boolean manual, boolean onOrOff) {
            super(Opcode.REQ_ANT_CALIBRATION, (byte) slot, (byte) 0);
            this.mSlot = slot;
            this.mManual = manual;
            this.mOnOrOff = onOrOff;
        }

        @Override // com.kopin.solos.sensors.btle.CableControl.Request, com.kopin.solos.sensors.btle.BTLESensorConfig.ConfigControlRequest
        public byte[] toValue() {
            if (this.mManual) {
                return new byte[]{this.mReq.getOpCode(), (byte) this.mSlot, -86};
            }
            byte[] bArr = new byte[4];
            bArr[0] = this.mReq.getOpCode();
            bArr[1] = (byte) this.mSlot;
            bArr[2] = -85;
            bArr[3] = (byte) (this.mOnOrOff ? 1 : 0);
            return bArr;
        }
    }

    static class KeepAlive extends Request {
        KeepAlive() {
            super(Opcode.KEEP_ALIVE, (byte) 0, (byte) 0);
        }
    }

    static class Rx {
        private byte mNumber;
        private short mNumberLE;
        Opcode mOpcode;
        Params mParams;

        private Rx() {
        }

        static Rx fromValue(byte[] value) {
            Log.v("CABLE", "<< " + BTLESensor.bytesToString(value));
            Rx self = new Rx();
            ByteBuffer buffer = ByteBuffer.wrap(value);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            try {
                self.mOpcode = Opcode.fromByte(buffer.get());
                switch (self.mOpcode) {
                    case SELECT_ACTIVE_SET:
                        self.mParams = ParamsActiveSet.fromValue(buffer);
                        break;
                    case REQ_ACTIVE_DEV_SETTINGS:
                        self.mParams = ParamsDevSettings.fromValue(buffer);
                        break;
                    case ANT_DISCOVERY_DATA:
                        self.mParams = ParamsAntDiscovery.fromValue(buffer);
                        break;
                    case RESP_ANT_CALIBRATION:
                        self.mParams = ParamsAntCalibration.fromValue(buffer);
                        break;
                }
            } catch (BufferUnderflowException bue) {
                bue.printStackTrace();
            }
            return self;
        }
    }

    static abstract class Params {
        Params() {
        }
    }

    static class ParamsActiveSet extends Params {
        int mSet;

        ParamsActiveSet() {
        }

        static ParamsActiveSet fromValue(ByteBuffer buffer) {
            ParamsActiveSet self = new ParamsActiveSet();
            self.mSet = buffer.get() & 255;
            return self;
        }
    }

    static class ParamsDevSettings extends Params {
        private static final byte CONTROL_ANT_RELAY_MASK = 64;
        private static final byte CONTROL_ANT_SIM_MASK = -128;
        private static final byte CONTROL_BLE_MASK = 32;
        private static final byte CONTROL_PROXIMITY_MASK = 15;
        private static final byte CONTROL_SLOT_MASK = 16;
        byte mAntChan;
        private byte mAntEvent;
        int mChannel;
        byte mCtrl;
        int mDevId;
        CableAnt.DeviceType mDevType;
        CableAnt.ChannelStatus mRxStatus;
        int mSet;
        int mTxStatus;

        ParamsDevSettings() {
        }

        static ParamsDevSettings fromValue(ByteBuffer buffer) {
            ParamsDevSettings self = new ParamsDevSettings();
            self.mSet = buffer.get() & 255;
            self.mChannel = buffer.get() & 255;
            self.mCtrl = buffer.get();
            self.mDevType = CableAnt.DeviceType.fromByte(buffer.get());
            self.mRxStatus = CableAnt.ChannelStatus.fromByte(buffer.get());
            self.mTxStatus = buffer.get();
            self.mDevId = buffer.getInt();
            self.mAntChan = buffer.get();
            self.mAntEvent = buffer.get();
            return self;
        }

        byte getPairingProximity() {
            return (byte) (this.mCtrl & 15);
        }

        boolean isSlotEnabled() {
            return (this.mCtrl & 16) != 0;
        }

        boolean isBleEnabled() {
            return (this.mCtrl & 32) != 0;
        }
    }

    static class ParamsAntDiscovery extends Params {
        int mDevId;
        CableAnt.DeviceType mDevType;
        private byte mFlags;
        private byte mPage;
        byte mRssi;
        byte mTrans;

        ParamsAntDiscovery() {
        }

        static ParamsAntDiscovery fromValue(ByteBuffer buffer) {
            ParamsAntDiscovery self = new ParamsAntDiscovery();
            self.mPage = buffer.get(4);
            self.mFlags = buffer.get(12);
            self.mDevId = buffer.getShort(13) & 65535;
            self.mDevType = CableAnt.DeviceType.fromByte(buffer.get(15));
            byte trans = buffer.get(16);
            self.mRssi = buffer.get(18);
            self.mDevId |= ((trans >> 4) & 15) << 16;
            self.mTrans = (byte) (trans & 15);
            return self;
        }
    }

    static class ParamsAntCalibration extends Params {
        private int mOffset;
        private OpType mOp;
        boolean mSuccess;

        private enum OpType {
            UNKNOWN,
            CALIBRATION,
            SET_SLOPE,
            SET_SERIAL
        }

        ParamsAntCalibration() {
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        static ParamsAntCalibration fromValue(ByteBuffer buffer) {
            ParamsAntCalibration self = new ParamsAntCalibration();
            byte op = buffer.get(1);
            switch (op) {
                case -84:
                    self.mOp = OpType.CALIBRATION;
                    self.mSuccess = true;
                    self.mOffset = buffer.getShort(6) & 65535;
                    return self;
                case -81:
                    self.mOp = OpType.CALIBRATION;
                    self.mOffset = 0;
                    self.mSuccess = false;
                    return self;
                case 16:
                    byte type = buffer.get(3);
                    switch (type) {
                        case 2:
                            self.mOp = OpType.SET_SLOPE;
                            break;
                        case 3:
                            self.mOp = OpType.SET_SERIAL;
                            break;
                        default:
                            self.mOp = OpType.UNKNOWN;
                            break;
                    }
                    byte result = buffer.get(2);
                    switch (result) {
                        case -84:
                            self.mOffset = 0;
                            self.mSuccess = true;
                            break;
                        case -81:
                            self.mOffset = 0;
                            self.mSuccess = false;
                            break;
                        case 1:
                            self.mSuccess = true;
                            self.mOffset = buffer.getShort(6) & 65535;
                            break;
                    }
                    break;
                default:
                    return self;
            }
        }

        public String toString() {
            if (this.mOp != null) {
                switch (this.mOp) {
                    case CALIBRATION:
                        if (this.mSuccess) {
                            return "Calibration Success, Offset " + this.mOffset;
                        }
                        return "Calibration Failed";
                    case SET_SLOPE:
                        return "Set Slope " + (this.mSuccess ? "Success" : AnalyticsEvents.PARAMETER_DIALOG_OUTCOME_VALUE_FAILED);
                    case SET_SERIAL:
                        return "Set Serial " + (this.mSuccess ? "Success" : AnalyticsEvents.PARAMETER_DIALOG_OUTCOME_VALUE_FAILED);
                    case UNKNOWN:
                        return "Operation " + (this.mSuccess ? "Success" : AnalyticsEvents.PARAMETER_DIALOG_OUTCOME_VALUE_FAILED);
                }
            }
            return this.mSuccess ? "Success" : AnalyticsEvents.PARAMETER_DIALOG_OUTCOME_VALUE_FAILED;
        }
    }
}
