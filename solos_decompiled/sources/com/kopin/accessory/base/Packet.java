package com.kopin.accessory.base;

import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes14.dex */
public final class Packet {
    private final PacketContent content;
    private final PacketHeader header;

    public Packet(PacketHeader header, PacketContent content) {
        this.header = header;
        this.content = content;
    }

    public Packet(PacketType type, PacketContent content) {
        this(new PacketHeader(type, content.getSize()), content);
    }

    public PacketHeader getHeader() {
        return this.header;
    }

    public PacketType getType() {
        return this.header.getPacketType();
    }

    public PacketContent getContent() {
        return this.content;
    }

    public void write(ByteBuffer byteBuffer) {
        this.header.write(byteBuffer);
        this.content.write(byteBuffer);
    }

    public int getSize() {
        return this.content.getSize() + 10;
    }

    public String toString() {
        return getType() + " (" + (this.content != null ? this.content.toString() : this.header != null ? this.header.toString() : "EMPTY") + ")";
    }
}
