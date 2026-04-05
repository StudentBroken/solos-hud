package com.kopin.solos.cabledfu;

import android.util.Log;
import com.kopin.pupil.update.util.FirmwareFlash;
import com.kopin.solos.cabledfu.CableFlash;
import com.kopin.solos.cabledfu.DfuPackets;

/* JADX INFO: loaded from: classes38.dex */
public class LegacyFirmwareUpdater extends CableFlash.FlashUpdater {
    private static final String TAG = "CableLegacyUpdater";
    int bytesToFlash;
    DfuChunker chunker;
    int crc16;
    byte[] crcArgs;
    boolean isComplete;
    private DfuCommands[] mCommandSequence;
    private int mCurCommand;

    private enum DfuCommands {
        CONNECT_CABLE,
        ENSURE_INACTIVE,
        DFU_MODE,
        CONNECT_DFU,
        ENSURE_ACTIVE,
        START_DFU,
        SET_PARAMS,
        SET_SIZE,
        SET_CRC,
        END_PARAMS,
        REQ_RECEIPT,
        BEGIN_UPDATE,
        SEND_CHUNK,
        VERIFY,
        RESET_AND_ACTIVATE,
        RESET,
        CHECK_INACTIVE,
        CHECK_VERSIONS,
        STORE_UPDATE
    }

    public static CableFlash.FlashUpdater createFirmwareUpdater(DfuFile file, int cableId) {
        DfuCommands[] dfuCommands;
        String name;
        if (file != null) {
            dfuCommands = new DfuCommands[]{DfuCommands.CONNECT_CABLE, DfuCommands.CHECK_INACTIVE, DfuCommands.DFU_MODE, DfuCommands.CONNECT_DFU, DfuCommands.ENSURE_ACTIVE, DfuCommands.START_DFU, DfuCommands.SET_SIZE, DfuCommands.SET_PARAMS, DfuCommands.SET_CRC, DfuCommands.END_PARAMS, DfuCommands.REQ_RECEIPT, DfuCommands.BEGIN_UPDATE, DfuCommands.SEND_CHUNK, DfuCommands.VERIFY, DfuCommands.RESET_AND_ACTIVATE, DfuCommands.ENSURE_INACTIVE, DfuCommands.CHECK_VERSIONS};
            name = "Legacy DFU Update";
        } else {
            dfuCommands = new DfuCommands[]{DfuCommands.RESET, DfuCommands.ENSURE_INACTIVE, DfuCommands.CHECK_VERSIONS};
            name = "Legacy DFU Reset";
        }
        return new LegacyFirmwareUpdater(name, file, dfuCommands, cableId);
    }

    public LegacyFirmwareUpdater(String name, DfuFile dfuFile, DfuCommands[] sequence, int cableId) {
        super(name, cableId);
        this.mCommandSequence = sequence;
        if (dfuFile != null) {
            this.chunker = dfuFile.openBinary();
            this.bytesToFlash = this.chunker.getBinarySize();
            this.crc16 = dfuFile.getCRC16();
            this.crcArgs = dfuFile.getFlashArgs();
        }
        this.mBytesWritten = 0;
    }

    @Override // com.kopin.solos.cabledfu.CableFlash.FlashUpdater, java.lang.Thread, java.lang.Runnable
    public void run() {
        this.mCurCommand = 0;
        while (!this.isComplete) {
            synchronized (this) {
                boolean andWait = true;
                Log.d(TAG, "Next Command: " + this.mCommandSequence[this.mCurCommand].name());
                switch (this.mCommandSequence[this.mCurCommand]) {
                    case CONNECT_CABLE:
                        if (CableDevice.isConnected()) {
                            andWait = false;
                        } else {
                            CableDevice.connectTo(this.mCableId);
                        }
                        break;
                    case ENSURE_INACTIVE:
                        andWait = false;
                        this.flashError = CableDevice.isDfuMode() ? FirmwareFlash.FlashError.APPLICATION_MODE_FAILED : FirmwareFlash.FlashError.NONE;
                        break;
                    case CHECK_INACTIVE:
                        andWait = false;
                        if (CableDevice.isDfuMode()) {
                            Log.d(TAG, "DFU Mode already active. Expect failure.");
                        }
                        this.flashError = FirmwareFlash.FlashError.NONE;
                        break;
                    case DFU_MODE:
                        if (!CableDevice.isDfuMode()) {
                            this.mCb.onSwitchMode(1, 2);
                            CableDevice.beginDfu();
                        } else {
                            andWait = false;
                        }
                        break;
                    case CONNECT_DFU:
                        CableDevice.connectInDFUMode();
                        break;
                    case START_DFU:
                        this.mCb.onProgressUpdate("Configuring update parameters");
                        CableDevice.startDfu();
                        andWait = false;
                        break;
                    case SET_SIZE:
                        CableDevice.setImageSize(this.bytesToFlash);
                        break;
                    case SET_PARAMS:
                        CableDevice.initParams();
                        andWait = false;
                        break;
                    case SET_CRC:
                        if (this.crcArgs != null) {
                            CableDevice.setCRC(this.crcArgs);
                        } else if (this.crc16 != 0) {
                            CableDevice.setCRC(this.crc16);
                        }
                        andWait = false;
                        break;
                    case END_PARAMS:
                        CableDevice.endParams();
                        break;
                    case REQ_RECEIPT:
                        CableDevice.requestPacketReceipts(50);
                        andWait = false;
                        break;
                    case BEGIN_UPDATE:
                        this.mCb.onProgress(0);
                        CableDevice.beginUpdate();
                        andWait = false;
                        break;
                    case SEND_CHUNK:
                        if (this.chunker != null) {
                            if (!this.chunker.hasMore()) {
                                this.chunker = null;
                                andWait = false;
                            } else {
                                byte[] bytes = this.chunker.nextSection();
                                Log.d(TAG, "Flashing next " + bytes.length + " bytes of " + this.bytesToFlash);
                                CableDevice.flashBytes(bytes, false);
                            }
                        }
                        break;
                    case VERIFY:
                        CableDevice.verifyUpdate();
                        break;
                    case RESET:
                        this.mCb.onSwitchMode(2, 1);
                        CableDevice.endDfu(false);
                        break;
                    case RESET_AND_ACTIVATE:
                        this.mCb.onSwitchMode(2, 1);
                        CableDevice.endDfu(true);
                        break;
                    case ENSURE_ACTIVE:
                        andWait = false;
                        this.flashError = CableDevice.isDfuMode() ? FirmwareFlash.FlashError.NONE : FirmwareFlash.FlashError.MAINTENANCE_MODE_FAILED;
                        break;
                }
                if (andWait) {
                    try {
                        this.flashError = FirmwareFlash.FlashError.UNKNOWN;
                        Log.d(TAG, " wait for response to command: " + this.mCommandSequence[this.mCurCommand].name());
                        wait(30000L);
                        if (this.flashError == FirmwareFlash.FlashError.UNKNOWN) {
                            Log.e(TAG, "   Timed out waiting for response!");
                            switch (this.mCommandSequence[this.mCurCommand]) {
                                case DFU_MODE:
                                    this.flashError = FirmwareFlash.FlashError.MAINTENANCE_MODE_FAILED;
                                    break;
                                case RESET:
                                case RESET_AND_ACTIVATE:
                                    this.flashError = FirmwareFlash.FlashError.NONE;
                                    break;
                            }
                        } else {
                            Log.d(TAG, "   got response: " + this.flashError.name());
                        }
                    } catch (InterruptedException e) {
                        this.flashError = FirmwareFlash.FlashError.UNKNOWN;
                    }
                }
            }
            if (this.flashError == FirmwareFlash.FlashError.NONE) {
                switch (this.mCommandSequence[this.mCurCommand]) {
                    case ENSURE_INACTIVE:
                        this.isComplete = true;
                        continue;
                    case SEND_CHUNK:
                        if (this.chunker != null) {
                            continue;
                        }
                        break;
                }
            }
            if (this.flashError == FirmwareFlash.FlashError.NONE) {
                this.mCurCommand++;
            } else {
                super.run();
            }
        }
        super.run();
    }

    @Override // com.kopin.solos.cabledfu.CableFlash.FlashUpdater
    public void notify(DfuPackets.DfuPacket packet) {
        super.notify(packet);
        if (packet instanceof DfuPackets.DfuControlResponsePacket) {
            DfuPackets.DfuControlResponsePacket response = (DfuPackets.DfuControlResponsePacket) packet;
            if (response.getOpCode() == DfuPackets.DfuOpCode.REQUEST_PACKET_RECEIPT) {
                this.mBytesWritten += 1000;
                this.mCb.onProgress(progressAsPercent());
            }
            this.flashError = !response.isSuccess() ? FirmwareFlash.FlashError.UNKNOWN : FirmwareFlash.FlashError.NONE;
            notify();
        }
    }

    @Override // com.kopin.solos.cabledfu.CableFlash.FlashUpdater
    public int progressAsPercent() {
        double pc = ((double) this.mBytesWritten) / ((double) this.bytesToFlash);
        return (int) Math.round(100.0d * pc);
    }
}
