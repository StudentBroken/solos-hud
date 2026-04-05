package com.kopin.accessory.packets.base;

import com.kopin.accessory.base.PacketContent;
import com.kopin.accessory.base.PacketSubHeader;
import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes14.dex */
public class HeaderPacketContent<T extends PacketSubHeader> extends PacketContent {
    private Class<T> clazz;
    private T header;

    public HeaderPacketContent(T header, Class<T> clazz, byte[] data) {
        super(null);
        this.clazz = clazz;
        this.header = header;
    }

    public HeaderPacketContent(Class<T> clazz, ByteBuffer byteBuffer) {
        super(byteBuffer);
        this.clazz = clazz;
        try {
            Constructor<T> constructor = clazz.getConstructor(ByteBuffer.class);
            this.header = constructor.newInstance(byteBuffer);
        } catch (Exception e) {
        }
    }

    @Override // com.kopin.accessory.base.PacketContent
    public void write(ByteBuffer byteBuffer) {
        this.header.write(byteBuffer);
    }

    public T getHeader() {
        return this.header;
    }

    @Override // com.kopin.accessory.base.PacketContent
    public int getSize() {
        return this.header.getSize();
    }
}
