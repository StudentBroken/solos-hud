package com.kopin.solos.cabledfu;

import android.util.Log;
import com.kopin.pupil.update.util.FirmwareFlash;
import com.kopin.solos.cabledfu.CableFlash;
import com.kopin.solos.cabledfu.DfuPackets;
import java.util.ArrayList;
import java.util.zip.CRC32;

/* JADX INFO: loaded from: classes38.dex */
public class SecureFirmwareUpdater extends CableFlash.FlashUpdater {
    private static final int MAX_PAYLOAD_SIZE = 1000;
    private static final String TAG = "CableSecureUpdater";
    byte[] initPackets;
    boolean isComplete;
    private long mCRC;
    private SecureDFUCommand mCurCommand;
    int mCurrentRangeIndex;
    byte[] mFirmwareData;
    ArrayList<DfuRange> mFirmwareRanges;
    private boolean mFirmwareSent;
    private boolean mInitPacketSent;
    private int mMaxLen;
    private int mOffset;
    private short mPRNValue;

    private enum SecureDFUCommand {
        ENSURE_READY,
        CONNECT_CABLE,
        ENSURE_INACTIVE,
        CHECK_INACTIVE,
        SET_DFU_DEVICE_NAME,
        ENTER_DFU,
        ENSURE_DISCONNECTED,
        CONNECT_SECURE_DFU,
        ENSURE_ACTIVE,
        READ_COMMAND_OBJECT_INFO,
        CREATE_COMMAND_OBJECT,
        READ_DATA_OBJECT_INFO,
        CREATE_DATA_OBJECT,
        SET_PRN,
        EXECUTE_OBJECT,
        SEND_INIT_PACKET,
        SEND_CALC_CHECKSUM,
        SEND_DATA_OBJECT,
        RESET,
        RESET_ON_ERROR
    }

    public static CableFlash.FlashUpdater createFirmwareUpdater(DfuFile dfuFile, int cableId) {
        return new SecureFirmwareUpdater("Secure Firmware Updater", dfuFile, cableId);
    }

    public SecureFirmwareUpdater(String name, DfuFile dfuFile, int cableId) {
        super(name, cableId);
        this.mFirmwareSent = false;
        this.mInitPacketSent = false;
        this.mMaxLen = 0;
        this.mOffset = 0;
        this.mCRC = 0L;
        this.mPRNValue = (short) 0;
        if (dfuFile != null) {
            this.mFirmwareData = dfuFile.readBytes();
            this.initPackets = dfuFile.getFlashArgs();
            Log.d(TAG, "Firmware size: " + this.mFirmwareData.length);
            this.mCurCommand = SecureDFUCommand.ENSURE_READY;
            return;
        }
        this.mCurCommand = SecureDFUCommand.RESET_ON_ERROR;
    }

    private void waitForCommand(SecureDFUCommand command) {
        this.flashError = FirmwareFlash.FlashError.UNKNOWN;
        long waitTime = 30000;
        switch (command) {
            case ENSURE_READY:
                this.flashError = FirmwareFlash.FlashError.NONE;
                waitTime = 1000;
                break;
            case CHECK_INACTIVE:
                this.flashError = FirmwareFlash.FlashError.NONE;
                waitTime = 3000;
                break;
            case CONNECT_CABLE:
                waitTime = 90000;
                break;
            case CONNECT_SECURE_DFU:
                waitTime = 60000;
                break;
        }
        Log.d(TAG, " Wait for response to command: " + command.name());
        try {
            wait(waitTime);
            Log.d(TAG, "   Got response with error: " + this.flashError.name());
        } catch (InterruptedException e) {
            e.printStackTrace();
            this.flashError = FirmwareFlash.FlashError.UNKNOWN;
        }
    }

    private boolean shouldGotoNextCommand() {
        switch (this.mCurCommand) {
            case ENSURE_READY:
            case ENSURE_DISCONNECTED:
                this.flashError = FirmwareFlash.FlashError.NONE;
            case CHECK_INACTIVE:
            case CONNECT_CABLE:
            case CONNECT_SECURE_DFU:
            default:
                return true;
            case RESET:
            case RESET_ON_ERROR:
                return false;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:59:0x011d  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x003f A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0033 A[Catch: all -> 0x0051, TryCatch #0 {, blocks: (B:6:0x0006, B:7:0x002e, B:9:0x0033, B:10:0x0038, B:15:0x0043, B:18:0x004b, B:23:0x0055, B:25:0x005b, B:26:0x005d, B:27:0x0060, B:28:0x0063, B:30:0x0069, B:31:0x0070, B:32:0x0075, B:33:0x0079, B:35:0x007f, B:37:0x008c, B:38:0x0090, B:39:0x0094, B:40:0x009b, B:41:0x00bb, B:42:0x00c0, B:43:0x00d3, B:45:0x00d9, B:46:0x00dd, B:48:0x00e0, B:49:0x00e7, B:50:0x00f5, B:51:0x00fa, B:52:0x0107, B:53:0x010d, B:55:0x0114, B:56:0x0116, B:57:0x011a), top: B:60:0x0006 }] */
    @Override // com.kopin.solos.cabledfu.CableFlash.FlashUpdater, java.lang.Thread, java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void run() {
        /*
            Method dump skipped, instruction units count: 336
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.kopin.solos.cabledfu.SecureFirmwareUpdater.run():void");
    }

    @Override // com.kopin.solos.cabledfu.CableFlash.FlashUpdater
    public void notify(DfuPackets.DfuPacket packet) {
        super.notify(packet);
        if (packet instanceof DfuPackets.ButtonlessDfuControlResponsePacket) {
            this.flashError = ((DfuPackets.ButtonlessDfuControlResponsePacket) packet).isSuccess() ? FirmwareFlash.FlashError.NONE : FirmwareFlash.FlashError.UNKNOWN;
            notify();
        } else if (packet instanceof DfuPackets.SecureDFUResponse) {
            DfuPackets.SecureDFUResponse response = (DfuPackets.SecureDFUResponse) packet;
            boolean success = response.isSuccess();
            if (success) {
                this.mOffset = response.mOffset;
                this.mCRC = response.mCRC;
                this.mMaxLen = response.mMaxSize;
            }
            this.flashError = success ? FirmwareFlash.FlashError.NONE : FirmwareFlash.FlashError.UNKNOWN;
            notify();
        }
    }

    @Override // com.kopin.solos.cabledfu.CableFlash.FlashUpdater
    public int progressAsPercent() {
        double pc = ((double) this.mOffset) / ((double) this.mFirmwareData.length);
        return (int) Math.round(100.0d * pc);
    }

    private SecureDFUCommand getNextCommand() {
        if (this.flashError != FirmwareFlash.FlashError.NONE) {
            return SecureDFUCommand.RESET_ON_ERROR;
        }
        switch (this.mCurCommand) {
            case ENSURE_READY:
                return SecureDFUCommand.CONNECT_CABLE;
            case CHECK_INACTIVE:
                return SecureDFUCommand.ENTER_DFU;
            case CONNECT_CABLE:
                return SecureDFUCommand.ENSURE_INACTIVE;
            case CONNECT_SECURE_DFU:
                return SecureDFUCommand.ENSURE_ACTIVE;
            case RESET:
            case RESET_ON_ERROR:
            case SET_DFU_DEVICE_NAME:
            default:
                return SecureDFUCommand.RESET;
            case ENSURE_DISCONNECTED:
                return SecureDFUCommand.CONNECT_SECURE_DFU;
            case ENSURE_INACTIVE:
                return SecureDFUCommand.CHECK_INACTIVE;
            case ENTER_DFU:
                return SecureDFUCommand.ENSURE_DISCONNECTED;
            case READ_COMMAND_OBJECT_INFO:
                return getNxtCmdAfterReadCommandObjectInfo();
            case CREATE_COMMAND_OBJECT:
                this.mPRNValue = (short) 0;
                return SecureDFUCommand.SET_PRN;
            case SEND_INIT_PACKET:
                return SecureDFUCommand.SEND_CALC_CHECKSUM;
            case READ_DATA_OBJECT_INFO:
                return getNxtCmdAfterReadDataObjectInfo();
            case CREATE_DATA_OBJECT:
                return SecureDFUCommand.SEND_DATA_OBJECT;
            case SEND_DATA_OBJECT:
                return SecureDFUCommand.SEND_DATA_OBJECT;
            case SET_PRN:
                return getNxtCmdAfterSetPRN();
            case EXECUTE_OBJECT:
                return getNXtCmdAfterExecuteObject();
            case SEND_CALC_CHECKSUM:
                return getNxtCmdAfterChecksum();
            case ENSURE_ACTIVE:
                return SecureDFUCommand.READ_COMMAND_OBJECT_INFO;
        }
    }

    private boolean sendNextPayloadInRange() {
        int lastRangeSize = this.mFirmwareRanges.get(0).getSize();
        int currentRangeSize = this.mFirmwareRanges.get(this.mCurrentRangeIndex).getSize();
        int rangeOffset = this.mOffset % lastRangeSize;
        int bytesToFlashInRange = currentRangeSize - rangeOffset;
        boolean willBeMorePayloadInRange = bytesToFlashInRange > 1000;
        Log.d(TAG, "Current range " + this.mCurrentRangeIndex + " Payload Sent: " + bytesToFlashInRange + " Has more: " + willBeMorePayloadInRange);
        if (willBeMorePayloadInRange) {
            sendDataObject(this.mCurrentRangeIndex, rangeOffset, 1000, false);
        } else {
            sendDataObject(this.mCurrentRangeIndex, rangeOffset, bytesToFlashInRange, true);
        }
        return willBeMorePayloadInRange;
    }

    private void sendDataObject(int rangeIdx, int rangeOffset, int dateLen, boolean waitForResult) {
        int srcOffset = this.mFirmwareRanges.get(rangeIdx).mLower + rangeOffset;
        byte[] payload = new byte[dateLen];
        System.arraycopy(this.mFirmwareData, srcOffset, payload, 0, dateLen);
        CableDevice.flashBytes(payload, waitForResult);
    }

    private SecureDFUCommand getNxtCmdAfterReadCommandObjectInfo() {
        if (this.mOffset > 0) {
            boolean crcMatched = verifyCRC(this.initPackets, this.mOffset, this.mCRC);
            Log.d(TAG, " InitPacket CRC matched: " + crcMatched);
            if (crcMatched) {
                if (this.mOffset < this.initPackets.length) {
                    this.mPRNValue = (short) 0;
                    return SecureDFUCommand.SET_PRN;
                }
                return SecureDFUCommand.EXECUTE_OBJECT;
            }
            this.mOffset = 0;
            this.mCRC = 0L;
            return SecureDFUCommand.CREATE_COMMAND_OBJECT;
        }
        return SecureDFUCommand.CREATE_COMMAND_OBJECT;
    }

    private SecureDFUCommand getNxtCmdAfterSetPRN() {
        return !this.mInitPacketSent ? SecureDFUCommand.SEND_INIT_PACKET : SecureDFUCommand.CREATE_DATA_OBJECT;
    }

    private SecureDFUCommand getNxtCmdAfterChecksum() {
        if (this.mInitPacketSent) {
            boolean crcMatched = verifyCRC(this.mFirmwareData, this.mOffset, this.mCRC);
            Log.d(TAG, " Firmware chunk CRC matched: " + crcMatched);
            if (crcMatched) {
                this.mFirmwareSent = this.mOffset == this.mFirmwareData.length;
                return SecureDFUCommand.EXECUTE_OBJECT;
            }
            return getNxtCommandOnError();
        }
        boolean crcMatched2 = verifyCRC(this.initPackets, this.mOffset, this.mCRC);
        Log.d(TAG, " InitPacket CRC matched: " + crcMatched2);
        if (crcMatched2) {
            return SecureDFUCommand.EXECUTE_OBJECT;
        }
        return getNxtCommandOnError();
    }

    private SecureDFUCommand getNxtCommandOnError() {
        this.flashError = FirmwareFlash.FlashError.UNKNOWN;
        return SecureDFUCommand.RESET_ON_ERROR;
    }

    private SecureDFUCommand getNXtCmdAfterExecuteObject() {
        if (this.mInitPacketSent) {
            if (this.mFirmwareSent) {
                return SecureDFUCommand.RESET;
            }
            this.mCurrentRangeIndex++;
            return SecureDFUCommand.CREATE_DATA_OBJECT;
        }
        this.mInitPacketSent = true;
        return SecureDFUCommand.READ_DATA_OBJECT_INFO;
    }

    private SecureDFUCommand getNxtCmdAfterReadDataObjectInfo() {
        if (this.mFirmwareRanges == null) {
            this.mFirmwareRanges = calculateRanges(this.mFirmwareData.length, this.mMaxLen);
        }
        if (this.mOffset > 0) {
            this.mCurrentRangeIndex = 0;
            for (DfuRange dfuRange : this.mFirmwareRanges) {
                if (dfuRange.contains(this.mOffset)) {
                    break;
                }
                this.mCurrentRangeIndex++;
            }
            boolean crcMatched = verifyCRC(this.mFirmwareData, this.mOffset, this.mCRC);
            Log.d(TAG, " Firmware CRC matched: " + crcMatched);
            if (crcMatched) {
                if (this.mOffset == this.mFirmwareData.length) {
                    this.mFirmwareSent = true;
                    return SecureDFUCommand.EXECUTE_OBJECT;
                }
                this.mPRNValue = (short) 50;
                if (this.mPRNValue > 0) {
                    return SecureDFUCommand.SET_PRN;
                }
                return SecureDFUCommand.CREATE_DATA_OBJECT;
            }
            if (this.mOffset % this.mMaxLen == 0) {
            }
            return getNxtCommandOnError();
        }
        this.mPRNValue = (short) 50;
        if (this.mPRNValue > 0) {
            return SecureDFUCommand.SET_PRN;
        }
        return SecureDFUCommand.CREATE_DATA_OBJECT;
    }

    private static boolean verifyCRC(byte[] data, long dataLen, long crc) {
        if (dataLen > data.length) {
            return false;
        }
        CRC32 crc32 = new CRC32();
        crc32.update(data, 0, (int) dataLen);
        long calculatedCRC = crc32.getValue();
        return calculatedCRC == crc;
    }

    private static ArrayList<DfuRange> calculateRanges(int dataLen, int maxRangeLen) {
        DfuRange range;
        ArrayList<DfuRange> ranges = new ArrayList<>();
        int totalLength = dataLen;
        int rangeIdx = 0;
        while (totalLength > 0) {
            int lowerBound = rangeIdx * maxRangeLen;
            if (totalLength > maxRangeLen) {
                totalLength -= maxRangeLen;
                range = new DfuRange(lowerBound, (maxRangeLen + lowerBound) - 1);
            } else {
                range = new DfuRange(lowerBound, (totalLength + lowerBound) - 1);
                totalLength = 0;
            }
            ranges.add(range);
            rangeIdx++;
        }
        return ranges;
    }
}
