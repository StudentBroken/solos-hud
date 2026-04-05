package com.kopin.accessory.packets;

import com.kopin.accessory.packets.base.HeaderPacketContent;
import com.kopin.accessory.packets.headers.AudioHeader;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes14.dex */
public class AudioEnablePacketContent extends HeaderPacketContent<AudioHeader> {
    public AudioEnablePacketContent(AudioHeader header, byte[] data) {
        super(header, AudioHeader.class, data);
    }

    public AudioEnablePacketContent(ByteBuffer byteBuffer) {
        super(AudioHeader.class, byteBuffer);
    }
}
