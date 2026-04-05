package com.kopin.accessory.packets.base;

import com.kopin.accessory.base.PacketContent;
import com.kopin.accessory.packets.ButtonType;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes14.dex */
public final class ButtonPacketContent extends PacketContent {
    private final ButtonType button;
    private final short holdTime;

    public ButtonPacketContent(ButtonType button, short holdTime) {
        super(null);
        this.button = button;
        this.holdTime = holdTime;
    }

    public ButtonPacketContent(ByteBuffer byteBuffer) {
        super(byteBuffer);
        this.holdTime = byteBuffer.getShort();
        this.button = ButtonType.fromValue(byteBuffer.get());
    }

    @Override // com.kopin.accessory.base.PacketContent
    public void write(ByteBuffer byteBuffer) {
        byteBuffer.putShort(this.holdTime);
        byteBuffer.put(this.button.getCode());
    }

    @Override // com.kopin.accessory.base.PacketContent
    public int getSize() {
        return 7;
    }

    public ButtonType getButton() {
        return this.button;
    }

    public short getHoldTime() {
        return this.holdTime;
    }
}
