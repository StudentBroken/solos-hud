package com.kopin.accessory.packets;

import com.kopin.accessory.packets.base.DataPacketContent;
import com.kopin.accessory.packets.headers.ImageHeader;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes14.dex */
public class ImagePacketContent extends DataPacketContent<ImageHeader> {
    public ImagePacketContent(ImageHeader header, byte[] data, int length) {
        super(header, ImageHeader.class, data, length);
    }

    public ImagePacketContent(ImageHeader header, byte[] data) {
        super(header, ImageHeader.class, data, data.length);
    }

    public ImagePacketContent(ByteBuffer byteBuffer) {
        super(ImageHeader.class, byteBuffer);
    }

    @Override // com.kopin.accessory.base.PacketContent
    public String toString() {
        return getHeader().toString();
    }
}
