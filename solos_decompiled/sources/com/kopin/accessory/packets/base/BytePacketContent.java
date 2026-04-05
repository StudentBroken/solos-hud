package com.kopin.accessory.packets.base;

import com.kopin.accessory.base.PacketContent;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes14.dex */
public final class BytePacketContent extends PacketContent {
    private final byte value;

    public BytePacketContent(byte value) {
        super(null);
        this.value = value;
    }

    public BytePacketContent(ByteBuffer byteBuffer) {
        super(byteBuffer);
        this.value = byteBuffer.get();
    }

    @Override // com.kopin.accessory.base.PacketContent
    public void write(ByteBuffer byteBuffer) {
        byteBuffer.put(this.value);
    }

    @Override // com.kopin.accessory.base.PacketContent
    public int getSize() {
        return 1;
    }

    public byte getValue() {
        return this.value;
    }
}
