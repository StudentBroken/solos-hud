package com.kopin.accessory.packets.headers;

import com.kopin.accessory.base.PacketSubHeader;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes14.dex */
public class FlashBaseHeader extends PacketSubHeader {
    public static final int PAYLOAD_LENGTH = 8;
    private final int numBytes;
    private final int startAddr;

    public FlashBaseHeader(int start, int len) {
        super(null);
        this.startAddr = start;
        this.numBytes = len;
    }

    public FlashBaseHeader(ByteBuffer byteBuffer) {
        super(byteBuffer);
        this.startAddr = byteBuffer.getInt();
        this.numBytes = byteBuffer.getInt();
    }

    @Override // com.kopin.accessory.base.PacketSubHeader
    public void write(ByteBuffer byteBuffer) {
        byteBuffer.putInt(this.startAddr);
        byteBuffer.putInt(this.numBytes);
    }

    @Override // com.kopin.accessory.base.PacketSubHeader
    public int getSize() {
        return 8;
    }

    public int getStartAddress() {
        return this.startAddr;
    }

    public int getNumberOfBytes() {
        return this.numBytes;
    }
}
