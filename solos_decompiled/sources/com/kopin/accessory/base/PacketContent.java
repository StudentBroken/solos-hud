package com.kopin.accessory.base;

import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes14.dex */
public abstract class PacketContent {
    public abstract int getSize();

    public abstract void write(ByteBuffer byteBuffer);

    public PacketContent(ByteBuffer byteBuffer) {
    }

    public String toString() {
        return "DATA " + getSize() + " bytes";
    }

    public static PacketContent noContent() {
        return new Empty(null);
    }

    public static class Empty extends PacketContent {
        public Empty(ByteBuffer byteBuffer) {
            super(byteBuffer);
        }

        @Override // com.kopin.accessory.base.PacketContent
        public void write(ByteBuffer byteBuffer) {
        }

        @Override // com.kopin.accessory.base.PacketContent
        public int getSize() {
            return 0;
        }
    }
}
