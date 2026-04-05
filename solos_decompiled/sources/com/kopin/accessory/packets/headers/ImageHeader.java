package com.kopin.accessory.packets.headers;

import com.kopin.accessory.ImageCodec;
import com.kopin.accessory.base.PacketSubHeader;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes14.dex */
public class ImageHeader extends PacketSubHeader {
    public static final int BYTES = 14;
    private ImageCodec format;
    private short height;
    private short index;
    private short total;
    private short width;
    private byte window;
    private short x;
    private short y;

    public ImageHeader(ImageCodec format, byte window, short index, short total, short x, short y, short width, short height) {
        this.format = format;
        this.window = window;
        this.index = index;
        this.total = total;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public ImageHeader(ByteBuffer byteBuffer) {
        super(byteBuffer);
        this.format = ImageCodec.fromType(byteBuffer.get());
        this.window = byteBuffer.get();
        this.index = byteBuffer.getShort();
        this.total = byteBuffer.getShort();
        this.x = byteBuffer.getShort();
        this.y = byteBuffer.getShort();
        this.width = byteBuffer.getShort();
        this.height = byteBuffer.getShort();
    }

    @Override // com.kopin.accessory.base.PacketSubHeader
    public void write(ByteBuffer byteBuffer) {
        byteBuffer.put(this.format.convert().byteValue());
        byteBuffer.put(this.window);
        byteBuffer.putShort(this.index);
        byteBuffer.putShort(this.total);
        byteBuffer.putShort(this.x);
        byteBuffer.putShort(this.y);
        byteBuffer.putShort(this.width);
        byteBuffer.putShort(this.height);
    }

    @Override // com.kopin.accessory.base.PacketSubHeader
    public int getSize() {
        return 14;
    }

    public ImageCodec getFormat() {
        return this.format;
    }

    public byte getWindow() {
        return this.window;
    }

    public short getIndex() {
        return this.index;
    }

    public short getTotal() {
        return this.total;
    }

    public short getX() {
        return this.x;
    }

    public short getY() {
        return this.y;
    }

    public short getWidth() {
        return this.width;
    }

    public short getHeight() {
        return this.height;
    }

    public String toString() {
        return String.format("%dx%d - %dx%d, %s", Short.valueOf(this.x), Short.valueOf(this.y), Short.valueOf(this.width), Short.valueOf(this.height), this.format.toString());
    }
}
