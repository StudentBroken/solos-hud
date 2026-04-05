package com.kopin.accessory.packets.base;

import com.kopin.accessory.base.PacketContent;
import com.kopin.accessory.base.PacketSubHeader;
import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes14.dex */
public class DataPacketContent<T extends PacketSubHeader> extends PacketContent {
    private Class<T> clazz;
    private final byte[] data;
    private T header;
    private final int length;

    public DataPacketContent(T header, Class<T> clazz, byte[] data, int length) {
        super(null);
        this.clazz = clazz;
        this.header = header;
        this.data = data;
        this.length = length;
    }

    public DataPacketContent(Class<T> clazz, ByteBuffer byteBuffer) {
        super(byteBuffer);
        this.clazz = clazz;
        try {
            Constructor<T> constructor = clazz.getConstructor(ByteBuffer.class);
            this.header = constructor.newInstance(byteBuffer);
        } catch (Exception e) {
        }
        this.length = byteBuffer.limit() - byteBuffer.position();
        this.data = new byte[this.length];
        byteBuffer.get(this.data);
    }

    @Override // com.kopin.accessory.base.PacketContent
    public void write(ByteBuffer byteBuffer) {
        this.header.write(byteBuffer);
        if (this.data != null) {
            byteBuffer.put(this.data, 0, this.length);
        }
    }

    public T getHeader() {
        return this.header;
    }

    public final byte[] getData() {
        return this.data;
    }

    public int getDataSize() {
        return this.length;
    }

    @Override // com.kopin.accessory.base.PacketContent
    public int getSize() {
        return this.header.getSize() + this.length;
    }
}
