package com.kopin.accessory.packets;

import com.kopin.accessory.base.PacketSubHeader;
import com.kopin.accessory.packets.base.DataPacketContent;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes14.dex */
public class DisplayModePacketContent extends DataPacketContent<DisplayModeFlags> {
    public static DisplayModePacketContent createPacket(boolean lowPower) {
        return new DisplayModePacketContent(new DisplayModeFlags(lowPower), new byte[0]);
    }

    public DisplayModePacketContent(DisplayModeFlags header, byte[] data, int length) {
        super(header, DisplayModeFlags.class, data, length);
    }

    public DisplayModePacketContent(DisplayModeFlags header, byte[] data) {
        super(header, DisplayModeFlags.class, data, data.length);
    }

    public DisplayModePacketContent(ByteBuffer byteBuffer) {
        super(DisplayModeFlags.class, byteBuffer);
    }

    @Override // com.kopin.accessory.base.PacketContent
    public String toString() {
        return getHeader().toString();
    }

    public static class DisplayModeFlags extends PacketSubHeader {
        public static final int BYTES = 2;
        private byte low_power;
        private byte preserve;

        public DisplayModeFlags(boolean lowPowerMode) {
            this.low_power = (byte) (lowPowerMode ? 255 : 0);
            this.preserve = (byte) 0;
        }

        public DisplayModeFlags(ByteBuffer byteBuffer) {
            super(byteBuffer);
            this.low_power = byteBuffer.get();
            this.preserve = byteBuffer.get();
        }

        @Override // com.kopin.accessory.base.PacketSubHeader
        public void write(ByteBuffer byteBuffer) {
            byteBuffer.put(this.low_power);
            byteBuffer.put(this.preserve);
        }

        public boolean isLowPowerEnabled() {
            return this.low_power != 0;
        }

        public boolean isFrameBufferPreserved() {
            return this.preserve != 0;
        }

        @Override // com.kopin.accessory.base.PacketSubHeader
        public int getSize() {
            return 2;
        }

        public String toString() {
            Object[] objArr = new Object[1];
            objArr[0] = this.low_power != 0 ? "Low power" : "Normal";
            return String.format("Set Display mode: %s", objArr);
        }
    }
}
