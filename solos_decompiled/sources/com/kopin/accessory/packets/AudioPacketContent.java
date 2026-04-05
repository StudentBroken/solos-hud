package com.kopin.accessory.packets;

import com.kopin.accessory.packets.base.DataPacketContent;
import com.kopin.accessory.packets.headers.AudioHeader;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes14.dex */
public class AudioPacketContent extends DataPacketContent<AudioHeader> {
    public AudioPacketContent(AudioHeader header, byte[] data, int length) {
        super(header, AudioHeader.class, data, length);
    }

    public AudioPacketContent(AudioHeader header, byte[] data) {
        super(header, AudioHeader.class, data, data.length);
    }

    public AudioPacketContent(ByteBuffer byteBuffer) {
        super(AudioHeader.class, byteBuffer);
    }
}
