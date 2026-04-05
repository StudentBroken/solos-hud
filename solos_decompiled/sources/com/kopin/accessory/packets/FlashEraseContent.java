package com.kopin.accessory.packets;

import com.kopin.accessory.base.PacketContent;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes14.dex */
public final class FlashEraseContent extends PacketContent {
    private final byte cmd;
    private final byte end;
    private final byte start;

    public enum EraseCommand {
        ERASE_SECTORS((byte) 0),
        ERASE_BANK1((byte) 1),
        ERASE_BANK2((byte) 2),
        UNKNOWN((byte) -1);

        private final byte value;

        EraseCommand(byte val) {
            this.value = val;
        }
    }

    private FlashEraseContent(EraseCommand c, byte s, byte e) {
        super(null);
        this.cmd = c.value;
        this.start = s;
        this.end = e;
    }

    public static FlashEraseContent eraseSectors(int start, int count) {
        return new FlashEraseContent(EraseCommand.ERASE_SECTORS, (byte) start, (byte) count);
    }

    public static FlashEraseContent eraseApplicationBank() {
        return new FlashEraseContent(EraseCommand.ERASE_BANK1, (byte) 0, (byte) 0);
    }

    public static FlashEraseContent eraseMaintenanceBank() {
        return new FlashEraseContent(EraseCommand.ERASE_BANK2, (byte) 0, (byte) 0);
    }

    public FlashEraseContent(ByteBuffer byteBuffer) {
        super(byteBuffer);
        this.cmd = byteBuffer.get();
        this.start = byteBuffer.get();
        this.end = byteBuffer.get();
        byteBuffer.get();
    }

    @Override // com.kopin.accessory.base.PacketContent
    public void write(ByteBuffer byteBuffer) {
        byteBuffer.put(this.cmd);
        byteBuffer.put(this.start);
        byteBuffer.put(this.end);
        byteBuffer.put((byte) 0);
    }

    @Override // com.kopin.accessory.base.PacketContent
    public int getSize() {
        return 4;
    }

    public EraseCommand getCommand() {
        switch (this.cmd) {
            case 0:
                return EraseCommand.ERASE_SECTORS;
            case 1:
                return EraseCommand.ERASE_BANK1;
            case 2:
                return EraseCommand.ERASE_BANK2;
            default:
                return EraseCommand.UNKNOWN;
        }
    }

    public int getStartSector() {
        return this.start;
    }

    public int getEndSector() {
        return this.end;
    }
}
