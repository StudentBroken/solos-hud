package com.kopin.accessory.packets;

import android.support.v4.view.ViewCompat;
import com.kopin.accessory.base.PacketContent;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes14.dex */
public final class FlashResponseContent extends PacketContent {
    public static final int BOOTBANK_APPLICATION = 1;
    public static final int BOOTBANK_MAINTENANCE = 2;
    private final int val;

    public enum FlashErrorCodes {
        NONE,
        ERASE_INVALID_ARGUMENT,
        ERASE_ERROR,
        PROGRAM_INVALID_ARGUMENT,
        PROGRAM_ERROR,
        UNKNOWN
    }

    public enum FlashVerifyCodes {
        SIGNATURE_OK,
        SIGNATURE_MISMATCH,
        CHECKSUM_OK,
        CHECKSUM_MISMATCH,
        UNKNOWN
    }

    public enum ResponseType {
        NONE(0),
        ERASE_PROGRESS(1),
        ERASE_COMPLETE(2),
        PROGRAM_PROGRESS(3),
        PROGRAM_COMPLETE(4),
        READ_PROGRESS(5),
        READ_COMPLETE(6),
        VERIFY_RESULT(7),
        BOOT_BANK(8),
        ERROR(240);

        private int value;

        ResponseType(int val) {
            this.value = val;
        }

        public static ResponseType fromValue(int val) {
            for (ResponseType r : values()) {
                if (r.value == val) {
                    return r;
                }
            }
            return ERROR;
        }
    }

    public FlashResponseContent(ResponseType type, int extra) {
        super(null);
        this.val = (extra << 8) | (type.value & 255);
    }

    public FlashResponseContent(ByteBuffer byteBuffer) {
        super(byteBuffer);
        this.val = byteBuffer.getInt();
    }

    @Override // com.kopin.accessory.base.PacketContent
    public void write(ByteBuffer byteBuffer) {
        byteBuffer.putInt(this.val);
    }

    @Override // com.kopin.accessory.base.PacketContent
    public int getSize() {
        return 4;
    }

    public int getRawValue() {
        return this.val;
    }

    public ResponseType getType() {
        return ResponseType.fromValue(this.val & 255);
    }

    public int getData() {
        return (this.val >> 8) & ViewCompat.MEASURED_SIZE_MASK;
    }

    public FlashErrorCodes getErrorCode() {
        if (getType() != ResponseType.ERROR) {
            return FlashErrorCodes.NONE;
        }
        int code = getData() & 255;
        if (code < FlashErrorCodes.UNKNOWN.ordinal()) {
            return FlashErrorCodes.values()[code];
        }
        return FlashErrorCodes.UNKNOWN;
    }

    public FlashVerifyCodes getVerifyCode() {
        if (getType() != ResponseType.VERIFY_RESULT) {
            return FlashVerifyCodes.UNKNOWN;
        }
        int code = getData() & 255;
        if (code < FlashVerifyCodes.UNKNOWN.ordinal()) {
            return FlashVerifyCodes.values()[code];
        }
        return FlashVerifyCodes.UNKNOWN;
    }
}
