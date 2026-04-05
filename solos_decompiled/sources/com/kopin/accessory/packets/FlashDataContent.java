package com.kopin.accessory.packets;

import com.kopin.accessory.packets.base.DataPacketContent;
import com.kopin.accessory.packets.headers.FlashProgramHeader;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes14.dex */
public class FlashDataContent extends DataPacketContent<FlashProgramHeader> {
    public FlashDataContent(FlashProgramHeader header, byte[] data) {
        super(header, FlashProgramHeader.class, data, data.length);
    }

    public FlashDataContent(FlashProgramHeader header) {
        super(header, FlashProgramHeader.class, null, 0);
    }

    public FlashDataContent(ByteBuffer byteBuffer) {
        super(FlashProgramHeader.class, byteBuffer);
    }

    @Override // com.kopin.accessory.packets.base.DataPacketContent, com.kopin.accessory.base.PacketContent
    public void write(ByteBuffer byteBuffer) {
        super.write(byteBuffer);
    }
}
