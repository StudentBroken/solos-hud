package com.kopin.accessory.packets.base;

import com.kopin.accessory.base.PacketContent;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes14.dex */
public final class IntPacketContent extends PacketContent {
    private final int value;

    public IntPacketContent(int value) {
        super(null);
        this.value = value;
    }

    public IntPacketContent(ByteBuffer byteBuffer) {
        super(byteBuffer);
        this.value = byteBuffer.getInt();
    }

    @Override // com.kopin.accessory.base.PacketContent
    public void write(ByteBuffer byteBuffer) {
        byteBuffer.putInt(this.value);
    }

    @Override // com.kopin.accessory.base.PacketContent
    public int getSize() {
        return 4;
    }

    public int getValue() {
        return this.value;
    }
}
