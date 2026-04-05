package com.kopin.accessory.packets.headers;

import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes14.dex */
public class FlashProgramHeader extends FlashBaseHeader {
    public static final int PAYLOAD_LENGTH = 11;
    private final short checksum;
    private final byte options;

    public FlashProgramHeader(int start, int len, short check, byte opts) {
        super(start, len);
        this.checksum = check;
        this.options = opts;
    }

    public FlashProgramHeader(ByteBuffer byteBuffer) {
        super(byteBuffer);
        this.options = byteBuffer.get();
        this.checksum = byteBuffer.getShort();
    }

    @Override // com.kopin.accessory.packets.headers.FlashBaseHeader, com.kopin.accessory.base.PacketSubHeader
    public void write(ByteBuffer byteBuffer) {
        super.write(byteBuffer);
        byteBuffer.put(this.options);
        byteBuffer.putShort(this.checksum);
    }

    @Override // com.kopin.accessory.packets.headers.FlashBaseHeader, com.kopin.accessory.base.PacketSubHeader
    public int getSize() {
        return 11;
    }

    public short getChecksum() {
        return this.checksum;
    }

    public byte getOptions() {
        return this.options;
    }
}
