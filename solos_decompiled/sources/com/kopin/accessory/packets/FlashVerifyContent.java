package com.kopin.accessory.packets;

import com.kopin.accessory.packets.base.DataPacketContent;
import com.kopin.accessory.packets.headers.FlashBaseHeader;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes14.dex */
public class FlashVerifyContent extends DataPacketContent<FlashBaseHeader> {
    public FlashVerifyContent(FlashBaseHeader header, byte[] data) {
        super(header, FlashBaseHeader.class, data, data.length);
    }

    public FlashVerifyContent(FlashBaseHeader header) {
        super(header, FlashBaseHeader.class, null, 0);
    }

    public FlashVerifyContent(ByteBuffer byteBuffer) {
        super(FlashBaseHeader.class, byteBuffer);
    }

    @Override // com.kopin.accessory.packets.base.DataPacketContent, com.kopin.accessory.base.PacketContent
    public void write(ByteBuffer byteBuffer) {
        super.write(byteBuffer);
    }
}
