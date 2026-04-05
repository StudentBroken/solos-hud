package com.kopin.accessory.base;

import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes14.dex */
public abstract class PacketSubHeader {
    public abstract int getSize();

    public abstract void write(ByteBuffer byteBuffer);

    public PacketSubHeader() {
    }

    public PacketSubHeader(ByteBuffer byteBuffer) {
    }
}
