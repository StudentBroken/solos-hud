package com.kopin.accessory.base;

import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes14.dex */
public final class PacketHeader {
    public static final short MAGIC_NUMBER = 24605;
    public static final int SIZE = 10;
    private final short magicNumber;
    private final PacketType packetType;
    private final int payloadLength;
    private final short reserved;

    public PacketHeader(PacketType type, int payloadLength) {
        this.magicNumber = MAGIC_NUMBER;
        this.packetType = type;
        this.reserved = (short) 0;
        this.payloadLength = payloadLength;
    }

    protected PacketHeader(short magic, PacketType type, short reserved, int payloadLength) {
        this(type, payloadLength);
    }

    public PacketHeader(ByteBuffer byteBuffer) {
        this.magicNumber = byteBuffer.getShort();
        this.packetType = PacketType.fromType(byteBuffer.getShort());
        this.reserved = byteBuffer.getShort();
        this.payloadLength = byteBuffer.getInt();
    }

    public boolean verify() {
        return this.magicNumber == 24605;
    }

    public void write(ByteBuffer byteBuffer) {
        byteBuffer.putShort(this.magicNumber);
        byteBuffer.putShort(this.packetType.getValue());
        byteBuffer.putShort(this.reserved);
        byteBuffer.putInt(this.payloadLength);
    }

    public short getMagicNumber() {
        return this.magicNumber;
    }

    public PacketType getPacketType() {
        return this.packetType;
    }

    public int getPayloadLength() {
        return this.payloadLength;
    }

    public long getPayloadLongLength() {
        return ((long) this.payloadLength) & (-1);
    }

    public String toString() {
        return "DATA " + this.payloadLength + " bytes";
    }
}
