package com.kopin.accessory.packets;

import com.kopin.accessory.base.PacketContent;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes14.dex */
public class TimeStampPacketContent extends PacketContent {
    private final long mTime;

    public TimeStampPacketContent(long time) {
        super(null);
        this.mTime = time;
    }

    public TimeStampPacketContent(ByteBuffer byteBuffer) {
        super(byteBuffer);
        this.mTime = byteBuffer.getLong();
        byteBuffer.getInt();
        byteBuffer.getInt();
    }

    @Override // com.kopin.accessory.base.PacketContent
    public void write(ByteBuffer byteBuffer) {
        byteBuffer.putLong(this.mTime);
        byteBuffer.putInt(0);
        byteBuffer.putInt(0);
    }

    @Override // com.kopin.accessory.base.PacketContent
    public int getSize() {
        return 16;
    }

    public long getTime() {
        return this.mTime;
    }
}
