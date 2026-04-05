package com.kopin.accessory.packets;

import com.kopin.accessory.base.PacketContent;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes14.dex */
public final class FlashChecksumContent extends PacketContent {
    private final byte[] checksum;

    public FlashChecksumContent(byte[] check) {
        super(null);
        this.checksum = check;
    }

    public FlashChecksumContent(ByteBuffer byteBuffer) {
        super(byteBuffer);
        this.checksum = new byte[64];
        byteBuffer.get(this.checksum);
    }

    @Override // com.kopin.accessory.base.PacketContent
    public void write(ByteBuffer byteBuffer) {
        byteBuffer.put(this.checksum);
    }

    @Override // com.kopin.accessory.base.PacketContent
    public int getSize() {
        if (this.checksum == null) {
            return 0;
        }
        return this.checksum.length;
    }

    public byte[] getChecksum() {
        return this.checksum;
    }
}
