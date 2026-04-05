package com.kopin.accessory.packets.base;

import com.kopin.accessory.base.PacketContent;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes14.dex */
public final class MessagePacketContent extends PacketContent {
    public MessagePacketContent(ByteBuffer byteBuffer) {
        super(byteBuffer);
    }

    @Override // com.kopin.accessory.base.PacketContent
    public void write(ByteBuffer byteBuffer) {
    }

    @Override // com.kopin.accessory.base.PacketContent
    public int getSize() {
        return 0;
    }
}
