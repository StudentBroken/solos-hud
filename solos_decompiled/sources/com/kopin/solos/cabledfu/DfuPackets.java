package com.kopin.solos.cabledfu;

import com.kopin.accessory.utility.CallHelper;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* JADX INFO: loaded from: classes38.dex */
public class DfuPackets {

    public static abstract class DfuPacket {
        public abstract String debugName();

        public abstract byte[] toValue();
    }

    public enum DfuOpCode {
        NONE((byte) 0),
        START_DFU((byte) 1),
        INIT_PARAMS((byte) 2),
        UPLOAD_FIRMWARE((byte) 3),
        VERIFY_FIRMWARE((byte) 4),
        ACTIVATE_AND_RESET((byte) 5),
        RESET((byte) 6),
        REQUEST_PACKET_RECEIPT((byte) 8),
        RESPONSE(CallHelper.CallState.FLAG_HFP_STATE),
        PACKET_REPSONSE((byte) 17),
        UNKNOWN((byte) -1);

        private final byte mOpCode;

        DfuOpCode(byte opcode) {
            this.mOpCode = opcode;
        }

        static DfuOpCode fromValue(byte val) {
            for (DfuOpCode op : values()) {
                if (op.mOpCode == val) {
                    return op;
                }
            }
            return UNKNOWN;
        }
    }

    public enum DfuResponseCode {
        UNKNOWN((byte) 0),
        OPERATION_SUCCESSFUL((byte) 1),
        OPERATION_INVALID((byte) 2),
        OPERATION_NOT_SUPPORTED((byte) 3),
        DATA_SIZE_EXCEEDS_LIMIT((byte) 4),
        CRC_ERROR((byte) 5),
        OPERATION_FAILED((byte) 6);

        private final byte mStatusCode;

        DfuResponseCode(byte opcode) {
            this.mStatusCode = opcode;
        }

        static DfuResponseCode fromValue(byte val) {
            for (DfuResponseCode op : values()) {
                if (op.mStatusCode == val) {
                    return op;
                }
            }
            return UNKNOWN;
        }
    }

    public enum InitPacketParam {
        START_INIT_PACKET((byte) 0),
        END_INIT_PACKET((byte) 1);

        private final byte mCode;

        InitPacketParam(byte hex) {
            this.mCode = hex;
        }
    }

    public enum DfuFirmwareType {
        SOFTDEVICE((byte) 1),
        BOOTLOADER((byte) 2),
        SOFTDEVICE_AND_BOOTLOADER((byte) 3),
        APPLICATION((byte) 4);

        private final byte mCode;

        DfuFirmwareType(byte code) {
            this.mCode = code;
        }
    }

    private static class DfuControlNoArgsPacket extends DfuPacket {
        private final DfuOpCode mDfuOp;

        DfuControlNoArgsPacket(DfuOpCode op) {
            this.mDfuOp = op;
        }

        @Override // com.kopin.solos.cabledfu.DfuPackets.DfuPacket
        public String debugName() {
            return this.mDfuOp.name();
        }

        @Override // com.kopin.solos.cabledfu.DfuPackets.DfuPacket
        public byte[] toValue() {
            return new byte[]{this.mDfuOp.mOpCode};
        }
    }

    private static class DfuControlStartArgsPacket extends DfuPacket {
        private final DfuOpCode mDfuOp;
        private final DfuFirmwareType mFWType;

        DfuControlStartArgsPacket(DfuOpCode op, DfuFirmwareType type) {
            this.mDfuOp = op;
            this.mFWType = type;
        }

        @Override // com.kopin.solos.cabledfu.DfuPackets.DfuPacket
        public String debugName() {
            return this.mDfuOp.name();
        }

        @Override // com.kopin.solos.cabledfu.DfuPackets.DfuPacket
        public byte[] toValue() {
            return new byte[]{this.mDfuOp.mOpCode, this.mFWType.mCode};
        }
    }

    static DfuPacket startDfu() {
        return new DfuControlStartArgsPacket(DfuOpCode.START_DFU, DfuFirmwareType.APPLICATION);
    }

    private static class DfuControlInitParamsArgsPacket extends DfuPacket {
        private final boolean isStart;
        private final DfuOpCode mDfuOp;

        DfuControlInitParamsArgsPacket(DfuOpCode op, boolean start) {
            this.mDfuOp = op;
            this.isStart = start;
        }

        @Override // com.kopin.solos.cabledfu.DfuPackets.DfuPacket
        public String debugName() {
            return this.mDfuOp.name();
        }

        @Override // com.kopin.solos.cabledfu.DfuPackets.DfuPacket
        public byte[] toValue() {
            byte[] bArr = new byte[2];
            bArr[0] = this.mDfuOp.mOpCode;
            bArr[1] = (this.isStart ? InitPacketParam.START_INIT_PACKET : InitPacketParam.END_INIT_PACKET).mCode;
            return bArr;
        }
    }

    static DfuPacket initParams(boolean start) {
        return new DfuControlInitParamsArgsPacket(DfuOpCode.INIT_PARAMS, start);
    }

    private static class DfuControlPacketReceiptsArgsPacket extends DfuPacket {
        private final byte mCount;
        private final DfuOpCode mDfuOp;

        DfuControlPacketReceiptsArgsPacket(DfuOpCode op, int packetCount) {
            this.mDfuOp = op;
            this.mCount = (byte) packetCount;
        }

        @Override // com.kopin.solos.cabledfu.DfuPackets.DfuPacket
        public String debugName() {
            return this.mDfuOp.name();
        }

        @Override // com.kopin.solos.cabledfu.DfuPackets.DfuPacket
        public byte[] toValue() {
            return new byte[]{this.mDfuOp.mOpCode, this.mCount, 0};
        }
    }

    static DfuPacket requestNotifcations(int packetCount) {
        return new DfuControlPacketReceiptsArgsPacket(DfuOpCode.REQUEST_PACKET_RECEIPT, packetCount);
    }

    static DfuPacket startUpload() {
        return new DfuControlNoArgsPacket(DfuOpCode.UPLOAD_FIRMWARE);
    }

    static DfuPacket verifyUpload() {
        return new DfuControlNoArgsPacket(DfuOpCode.VERIFY_FIRMWARE);
    }

    static DfuPacket activateAndReset() {
        return new DfuControlNoArgsPacket(DfuOpCode.ACTIVATE_AND_RESET);
    }

    static DfuPacket discardAndReset() {
        return new DfuControlNoArgsPacket(DfuOpCode.RESET);
    }

    static class DfuSizeParamsPacket extends DfuDataPacket {
        private final int mAppSize;
        private final int mBootSize;
        private final int mSoftDevSize;

        /* JADX WARN: Multi-variable type inference failed */
        private DfuSizeParamsPacket(int i) {
            super(null);
            this.mSoftDevSize = 0;
            this.mBootSize = 0;
            this.mAppSize = i;
        }

        @Override // com.kopin.solos.cabledfu.DfuPackets.DfuDataPacket, com.kopin.solos.cabledfu.DfuPackets.DfuPacket
        public String debugName() {
            return "InitParams: SoftDev: " + this.mSoftDevSize + ", Boot: " + this.mBootSize + ", AppImage: " + this.mAppSize + " bytes";
        }

        @Override // com.kopin.solos.cabledfu.DfuPackets.DfuDataPacket, com.kopin.solos.cabledfu.DfuPackets.DfuPacket
        public byte[] toValue() {
            ByteBuffer bb = ByteBuffer.allocate(12);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            bb.putInt(this.mSoftDevSize);
            bb.putInt(this.mBootSize);
            bb.putInt(this.mAppSize);
            return bb.array();
        }
    }

    static DfuPacket sizePacket(int appSize) {
        return new DfuSizeParamsPacket(appSize);
    }

    static class DfuCRCParamsPacket extends DfuDataPacket {
        private final byte[] crc;

        /* JADX WARN: Multi-variable type inference failed */
        private DfuCRCParamsPacket(int i) {
            super(null);
            ByteBuffer byteBufferAllocate = ByteBuffer.allocate(2);
            byteBufferAllocate.order(ByteOrder.LITTLE_ENDIAN);
            byteBufferAllocate.putShort((short) i);
            this.crc = byteBufferAllocate.array();
        }

        /* JADX WARN: Multi-variable type inference failed */
        private DfuCRCParamsPacket(byte[] bArr) {
            super(null);
            this.crc = bArr;
        }

        @Override // com.kopin.solos.cabledfu.DfuPackets.DfuDataPacket, com.kopin.solos.cabledfu.DfuPackets.DfuPacket
        public String debugName() {
            return "InitParams: CRC";
        }

        @Override // com.kopin.solos.cabledfu.DfuPackets.DfuDataPacket, com.kopin.solos.cabledfu.DfuPackets.DfuPacket
        public byte[] toValue() {
            return this.crc;
        }
    }

    static DfuPacket crcPacket(int crc16) {
        return new DfuCRCParamsPacket(crc16);
    }

    static DfuPacket crcPacket(byte[] crc) {
        return new DfuCRCParamsPacket(crc);
    }

    static class DfuDataPacket extends DfuPacket {
        private final byte[] mBytes;

        private DfuDataPacket(byte[] bytes) {
            this.mBytes = bytes;
        }

        @Override // com.kopin.solos.cabledfu.DfuPackets.DfuPacket
        public String debugName() {
            return "DATA: " + this.mBytes.length + " bytes";
        }

        @Override // com.kopin.solos.cabledfu.DfuPackets.DfuPacket
        public byte[] toValue() {
            return this.mBytes;
        }
    }

    static DfuPacket writeData(byte[] bytes) {
        return new DfuDataPacket(bytes);
    }

    static class DfuControlResponsePacket extends DfuPacket {
        private final DfuOpCode mDfuOp;
        private final DfuResponseCode mDfuStatus;
        private final boolean mSuccess;

        private DfuControlResponsePacket(boolean ok, DfuOpCode op) {
            this.mSuccess = ok;
            this.mDfuOp = op;
            this.mDfuStatus = DfuResponseCode.UNKNOWN;
        }

        private DfuControlResponsePacket(DfuResponseCode status, DfuOpCode op) {
            this.mDfuStatus = status;
            this.mSuccess = status == DfuResponseCode.OPERATION_SUCCESSFUL;
            this.mDfuOp = op;
        }

        public boolean isSuccess() {
            return this.mSuccess;
        }

        public DfuOpCode getOpCode() {
            return this.mDfuOp;
        }

        @Override // com.kopin.solos.cabledfu.DfuPackets.DfuPacket
        public String debugName() {
            return "RESPONSE: " + this.mDfuOp.name() + (this.mSuccess ? " OK" : " FAIL") + " (" + this.mDfuStatus.name() + ")";
        }

        @Override // com.kopin.solos.cabledfu.DfuPackets.DfuPacket
        public byte[] toValue() {
            return new byte[]{DfuOpCode.RESPONSE.mOpCode, this.mDfuOp.mOpCode};
        }
    }

    static DfuPacket fromValue(byte[] value) {
        boolean z = true;
        if (value == null || value.length <= 0) {
            return null;
        }
        DfuOpCode op = DfuOpCode.fromValue(value[0]);
        switch (op) {
            case RESPONSE:
                if (value.length > 2) {
                    return new DfuControlResponsePacket(DfuResponseCode.fromValue(value[2]), DfuOpCode.fromValue(value[1]));
                }
                if (value.length > 1) {
                    return new DfuControlResponsePacket(z, DfuOpCode.fromValue(value[1]));
                }
                return new DfuControlResponsePacket(z, DfuOpCode.NONE);
            case PACKET_REPSONSE:
                return new DfuControlResponsePacket(z, DfuOpCode.REQUEST_PACKET_RECEIPT);
            case START_DFU:
            case INIT_PARAMS:
            case UPLOAD_FIRMWARE:
            case VERIFY_FIRMWARE:
            case ACTIVATE_AND_RESET:
                return new DfuControlNoArgsPacket(op);
            default:
                return null;
        }
    }

    public enum SecureDFUOpCode {
        NONE((byte) 0),
        CREATE_OBJECT((byte) 1),
        SET_PRN_VALUE((byte) 2),
        CALCULATE_CHECKSUM((byte) 3),
        EXECUTE((byte) 4),
        READ_OBJECT_INFO((byte) 6),
        RESPONSE((byte) 96);

        private final byte mOpCode;

        SecureDFUOpCode(byte opcode) {
            this.mOpCode = opcode;
        }

        static SecureDFUOpCode fromValue(byte val) {
            for (SecureDFUOpCode op : values()) {
                if (op.mOpCode == val) {
                    return op;
                }
            }
            return NONE;
        }
    }

    public enum SecureDFUProcedureType {
        UNKNOWN((byte) 0),
        COMMAND((byte) 1),
        DATA((byte) 2);

        private final byte mProcedureType;

        SecureDFUProcedureType(byte opcode) {
            this.mProcedureType = opcode;
        }

        static SecureDFUProcedureType fromValue(byte val) {
            for (SecureDFUProcedureType op : values()) {
                if (op.mProcedureType == val) {
                    return op;
                }
            }
            return UNKNOWN;
        }
    }

    public enum SecureDFUResultCode {
        INVALID((byte) 0),
        SUCCESS((byte) 1),
        OPCODE_NOT_SUPPORTED((byte) 2),
        INVALID_PARAMETER((byte) 3),
        INSUFFICIENT_RESOURCES((byte) 4),
        INVALID_OBJECT((byte) 5),
        SIGNATURE_MISMATCH((byte) 6),
        UNSUPPORTED_TYPE((byte) 7),
        OPERATION_NOT_PERMITTED((byte) 8),
        OPERATION_FAILED((byte) 10),
        EXTENDED_ERROR(CallHelper.CallState.SCO_CONNECTED);

        private final byte mResultCode;

        SecureDFUResultCode(byte resultCode) {
            this.mResultCode = resultCode;
        }

        static SecureDFUResultCode fromValue(byte val) {
            for (SecureDFUResultCode op : values()) {
                if (op.mResultCode == val) {
                    return op;
                }
            }
            return INVALID;
        }
    }

    public enum SecureDFUExtendedErrorCode {
        NOERROR((byte) 0),
        WRONG_COMMAND_FORMAT((byte) 2),
        UNKNOWN_COMMAND((byte) 3),
        INIT_COMMAND_INVALID((byte) 4),
        FW_VERSION_FAILURE((byte) 5),
        HW_VERSION_FAILURE((byte) 6),
        SD_VERSION_FAILURE((byte) 7),
        SIGNATURE_MISSING((byte) 8),
        WRONG_HASH_TYPE((byte) 9),
        HASH_FAILED((byte) 10),
        WRONG_SIGNATURE_TYPE(CallHelper.CallState.SCO_CONNECTED),
        VERIFICATION_FAILED(CallHelper.CallState.SCO_DISCONNECTED),
        INSUFFICIENT_SPACE(CallHelper.CallState.CALL_IN_PROGRESS);

        private final byte mErrorCode;

        SecureDFUExtendedErrorCode(byte error) {
            this.mErrorCode = error;
        }

        static SecureDFUExtendedErrorCode fromValue(byte val) {
            for (SecureDFUExtendedErrorCode op : values()) {
                if (op.mErrorCode == val) {
                    return op;
                }
            }
            return NOERROR;
        }
    }

    public enum ButtonlessDfuCode {
        NONE((byte) 0),
        ENTER_DFU((byte) 1),
        SET_NAME((byte) 2),
        RESPONSE(CallHelper.CallState.FLAG_SCO_STATE);

        private final byte mOpCode;

        ButtonlessDfuCode(byte opcode) {
            this.mOpCode = opcode;
        }

        static ButtonlessDfuCode fromValue(byte val) {
            for (ButtonlessDfuCode op : values()) {
                if (op.mOpCode == val) {
                    return op;
                }
            }
            return NONE;
        }
    }

    public enum ButtonlessDfuResponseCode {
        INVALID_CODE((byte) 0),
        SUCCESS((byte) 1),
        OPERATION_NOT_SUPPORTED((byte) 2),
        OPERATION_FAILED((byte) 4),
        INVALID_ADVERTISEMENT_NAME((byte) 5),
        BUSY((byte) 6),
        NOT_BONDED((byte) 7);

        private final byte mStatusCode;

        ButtonlessDfuResponseCode(byte status) {
            this.mStatusCode = status;
        }

        static ButtonlessDfuResponseCode fromValue(byte val) {
            for (ButtonlessDfuResponseCode op : values()) {
                if (op.mStatusCode == val) {
                    return op;
                }
            }
            return INVALID_CODE;
        }
    }

    private static class DfuSetNamePacket extends DfuPacket {
        private final ButtonlessDfuCode mDfuOp;
        private final String mName;

        DfuSetNamePacket(ButtonlessDfuCode opCode, int cableId) {
            this.mDfuOp = opCode;
            this.mName = "DFu#" + cableId;
        }

        @Override // com.kopin.solos.cabledfu.DfuPackets.DfuPacket
        public String debugName() {
            return this.mDfuOp.name();
        }

        @Override // com.kopin.solos.cabledfu.DfuPackets.DfuPacket
        public byte[] toValue() {
            byte[] name = new byte[0];
            try {
                name = this.mName.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            ByteBuffer byteBuffer = ByteBuffer.allocate(name.length + 2);
            byteBuffer.put(this.mDfuOp.mOpCode);
            byteBuffer.put((byte) name.length);
            byteBuffer.put(name);
            return byteBuffer.array();
        }
    }

    static DfuPacket setName(int cableId) {
        return new DfuSetNamePacket(ButtonlessDfuCode.SET_NAME, cableId);
    }

    private static class ReadCommandObjectInfoPacket extends DfuPacket {
        private ReadCommandObjectInfoPacket() {
        }

        @Override // com.kopin.solos.cabledfu.DfuPackets.DfuPacket
        public String debugName() {
            return SecureDFUOpCode.READ_OBJECT_INFO.name();
        }

        @Override // com.kopin.solos.cabledfu.DfuPackets.DfuPacket
        public byte[] toValue() {
            return new byte[]{SecureDFUOpCode.READ_OBJECT_INFO.mOpCode, SecureDFUProcedureType.COMMAND.mProcedureType};
        }
    }

    static DfuPacket readCommandObjectInfo() {
        return new ReadCommandObjectInfoPacket();
    }

    private static class CreateCommandObjectPacket extends DfuPacket {
        private final int mSize;

        CreateCommandObjectPacket(int size) {
            this.mSize = size;
        }

        @Override // com.kopin.solos.cabledfu.DfuPackets.DfuPacket
        public String debugName() {
            return SecureDFUOpCode.CREATE_OBJECT.name();
        }

        @Override // com.kopin.solos.cabledfu.DfuPackets.DfuPacket
        public byte[] toValue() {
            ByteBuffer byteBuffer = ByteBuffer.allocate(6);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            byteBuffer.put(SecureDFUOpCode.CREATE_OBJECT.mOpCode);
            byteBuffer.put(SecureDFUProcedureType.COMMAND.mProcedureType);
            byteBuffer.putInt(this.mSize);
            return byteBuffer.array();
        }
    }

    static DfuPacket createCommandObject(int size) {
        return new CreateCommandObjectPacket(size);
    }

    private static class ReadDataObjectInfoPacket extends DfuPacket {
        private ReadDataObjectInfoPacket() {
        }

        @Override // com.kopin.solos.cabledfu.DfuPackets.DfuPacket
        public String debugName() {
            return SecureDFUOpCode.READ_OBJECT_INFO.name();
        }

        @Override // com.kopin.solos.cabledfu.DfuPackets.DfuPacket
        public byte[] toValue() {
            return new byte[]{SecureDFUOpCode.READ_OBJECT_INFO.mOpCode, SecureDFUProcedureType.DATA.mProcedureType};
        }
    }

    static DfuPacket readDataObjectInfo() {
        return new ReadDataObjectInfoPacket();
    }

    private static class CreateDataObjectPacket extends DfuPacket {
        private final int mSize;

        CreateDataObjectPacket(int size) {
            this.mSize = size;
        }

        @Override // com.kopin.solos.cabledfu.DfuPackets.DfuPacket
        public String debugName() {
            return SecureDFUOpCode.CREATE_OBJECT.name() + " Size: " + this.mSize;
        }

        @Override // com.kopin.solos.cabledfu.DfuPackets.DfuPacket
        public byte[] toValue() {
            ByteBuffer byteBuffer = ByteBuffer.allocate(6);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            byteBuffer.put(SecureDFUOpCode.CREATE_OBJECT.mOpCode);
            byteBuffer.put(SecureDFUProcedureType.DATA.mProcedureType);
            byteBuffer.putInt(this.mSize);
            return byteBuffer.array();
        }
    }

    static DfuPacket createDataObject(int size) {
        return new CreateDataObjectPacket(size);
    }

    private static class SetPRNPacket extends DfuPacket {
        private final short mValue;

        SetPRNPacket(short value) {
            this.mValue = value;
        }

        @Override // com.kopin.solos.cabledfu.DfuPackets.DfuPacket
        public String debugName() {
            return SecureDFUOpCode.SET_PRN_VALUE.name();
        }

        @Override // com.kopin.solos.cabledfu.DfuPackets.DfuPacket
        public byte[] toValue() {
            ByteBuffer byteBuffer = ByteBuffer.allocate(3);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            byteBuffer.put(SecureDFUOpCode.SET_PRN_VALUE.mOpCode);
            byteBuffer.putShort(this.mValue);
            return byteBuffer.array();
        }
    }

    static DfuPacket setPacketReceiptNotification(short value) {
        return new SetPRNPacket(value);
    }

    static DfuPacket calculateChecksum() {
        return new DfuPacket() { // from class: com.kopin.solos.cabledfu.DfuPackets.1
            @Override // com.kopin.solos.cabledfu.DfuPackets.DfuPacket
            public String debugName() {
                return SecureDFUOpCode.CALCULATE_CHECKSUM.name();
            }

            @Override // com.kopin.solos.cabledfu.DfuPackets.DfuPacket
            public byte[] toValue() {
                return new byte[]{SecureDFUOpCode.CALCULATE_CHECKSUM.mOpCode};
            }
        };
    }

    static DfuPacket executeCommand() {
        return new DfuPacket() { // from class: com.kopin.solos.cabledfu.DfuPackets.2
            @Override // com.kopin.solos.cabledfu.DfuPackets.DfuPacket
            public String debugName() {
                return SecureDFUOpCode.EXECUTE.name();
            }

            @Override // com.kopin.solos.cabledfu.DfuPackets.DfuPacket
            public byte[] toValue() {
                return new byte[]{SecureDFUOpCode.EXECUTE.mOpCode};
            }
        };
    }

    static DfuDataPacket sendInitPacket(final byte[] data) {
        return new DfuDataPacket(data) { // from class: com.kopin.solos.cabledfu.DfuPackets.3
            @Override // com.kopin.solos.cabledfu.DfuPackets.DfuDataPacket, com.kopin.solos.cabledfu.DfuPackets.DfuPacket
            public String debugName() {
                return "SendInitPacket: " + data.length + " bytes";
            }
        };
    }

    static class ButtonlessDfuControlResponsePacket extends DfuPacket {
        private final ButtonlessDfuCode mDfuOp;
        private final ButtonlessDfuResponseCode mDfuStatus;
        private final boolean mSuccess;

        private ButtonlessDfuControlResponsePacket(boolean ok, ButtonlessDfuCode op) {
            this.mSuccess = ok;
            this.mDfuOp = op;
            this.mDfuStatus = ButtonlessDfuResponseCode.INVALID_CODE;
        }

        private ButtonlessDfuControlResponsePacket(ButtonlessDfuResponseCode status, ButtonlessDfuCode op) {
            this.mDfuStatus = status;
            this.mSuccess = status == ButtonlessDfuResponseCode.SUCCESS;
            this.mDfuOp = op;
        }

        public boolean isSuccess() {
            return this.mSuccess;
        }

        public ButtonlessDfuCode getOpCode() {
            return this.mDfuOp;
        }

        @Override // com.kopin.solos.cabledfu.DfuPackets.DfuPacket
        public String debugName() {
            return "RESPONSE: " + this.mDfuOp.name() + (this.mSuccess ? " OK" : " FAIL") + " (" + this.mDfuStatus.name() + ")";
        }

        @Override // com.kopin.solos.cabledfu.DfuPackets.DfuPacket
        public byte[] toValue() {
            return new byte[]{DfuOpCode.RESPONSE.mOpCode, this.mDfuOp.mOpCode};
        }
    }

    static DfuPacket fromButtonlessDfuValue(byte[] value) {
        boolean z = true;
        if (value == null || value.length <= 0) {
            return null;
        }
        ButtonlessDfuCode op = ButtonlessDfuCode.fromValue(value[0]);
        switch (op) {
            case RESPONSE:
                if (value.length > 2) {
                    return new ButtonlessDfuControlResponsePacket(ButtonlessDfuResponseCode.fromValue(value[2]), ButtonlessDfuCode.fromValue(value[1]));
                }
                return new ButtonlessDfuControlResponsePacket(z, ButtonlessDfuCode.NONE);
            default:
                return null;
        }
    }

    static class SecureDFUResponse extends DfuPacket {
        long mCRC;
        SecureDFUExtendedErrorCode mError;
        int mMaxSize;
        int mOffset;
        SecureDFUOpCode mOpCode;
        SecureDFUOpCode mRequestOpCode;
        SecureDFUResultCode mStatus;

        SecureDFUResponse() {
        }

        @Override // com.kopin.solos.cabledfu.DfuPackets.DfuPacket
        public String debugName() {
            switch (this.mStatus) {
                case SUCCESS:
                    return String.format("SecureDFUResponse: (%s, %s) MaxSize: %d, Offset: %d, CRC: %d", this.mRequestOpCode.name(), this.mStatus.name(), Integer.valueOf(this.mMaxSize), Integer.valueOf(this.mOffset), Long.valueOf(this.mCRC));
                case EXTENDED_ERROR:
                    return String.format("SecureDFUResponse: (%s, %s) Extended Error: %s", this.mRequestOpCode.name(), this.mStatus.name(), this.mError.name());
                default:
                    return String.format("SecureDFUResponse: (%s, %s)", this.mRequestOpCode.name(), this.mStatus.name());
            }
        }

        @Override // com.kopin.solos.cabledfu.DfuPackets.DfuPacket
        public byte[] toValue() {
            return new byte[0];
        }

        public boolean isSuccess() {
            return (this.mStatus != null) & (this.mStatus == SecureDFUResultCode.SUCCESS);
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        static SecureDFUResponse parseResponse(byte[] responseData) {
            SecureDFUResponse response = new SecureDFUResponse();
            response.mOpCode = SecureDFUOpCode.fromValue(responseData[0]);
            response.mRequestOpCode = SecureDFUOpCode.fromValue(responseData[1]);
            response.mStatus = SecureDFUResultCode.fromValue(responseData[2]);
            switch (response.mStatus) {
                case SUCCESS:
                    switch (response.mRequestOpCode) {
                        case READ_OBJECT_INFO:
                            response.mMaxSize = ByteBuffer.wrap(responseData, 3, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
                            response.mOffset = ByteBuffer.wrap(responseData, 7, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
                            response.mCRC = ((long) ByteBuffer.wrap(responseData, 11, 4).order(ByteOrder.LITTLE_ENDIAN).getInt()) & 4294967295L;
                            break;
                        case CALCULATE_CHECKSUM:
                            response.mOffset = ByteBuffer.wrap(responseData, 3, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
                            response.mCRC = ((long) ByteBuffer.wrap(responseData, 7, 4).order(ByteOrder.LITTLE_ENDIAN).getInt()) & 4294967295L;
                            break;
                    }
                    break;
                case EXTENDED_ERROR:
                    response.mError = SecureDFUExtendedErrorCode.fromValue(responseData[3]);
                    return response;
                default:
                    return response;
            }
        }
    }
}
