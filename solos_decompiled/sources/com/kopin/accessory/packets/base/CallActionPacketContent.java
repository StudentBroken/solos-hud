package com.kopin.accessory.packets.base;

import com.kopin.accessory.base.PacketContent;
import java.nio.ByteBuffer;
import java.util.Arrays;

/* JADX INFO: loaded from: classes14.dex */
public final class CallActionPacketContent extends PacketContent {
    private byte[] mExtraData;
    private byte mType;
    private final byte[] value;

    public CallActionPacketContent(byte[] value) {
        super(null);
        this.value = value;
    }

    public CallActionPacketContent(ByteBuffer byteBuffer) {
        super(byteBuffer);
        this.value = new byte[byteBuffer.limit() - byteBuffer.position()];
        byteBuffer.get(this.value);
        this.mType = this.value[0];
        int length = this.value.length;
        if (this.mType == 5) {
            this.mExtraData = new byte[this.value.length - 1];
            this.mExtraData = Arrays.copyOfRange(this.value, 1, this.value.length - 1);
        }
    }

    @Override // com.kopin.accessory.base.PacketContent
    public void write(ByteBuffer byteBuffer) {
        byteBuffer.put(this.value);
    }

    @Override // com.kopin.accessory.base.PacketContent
    public int getSize() {
        return this.value.length;
    }

    public byte[] getValue() {
        return this.value;
    }

    public byte getType() {
        return this.mType;
    }

    public byte[] getExtraData() {
        return this.mExtraData;
    }
}
