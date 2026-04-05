package com.kopin.accessory.packets.headers;

import com.kopin.accessory.AudioCodec;
import com.kopin.accessory.base.PacketSubHeader;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes14.dex */
public class AudioHeader extends PacketSubHeader {
    public static final int BYTES = 8;
    private final AudioCodec audioCodec;
    private final byte bitRate;
    private final byte channels;
    private final byte reserved;
    private final int sampleRate;

    public AudioHeader(AudioCodec codec, byte reserved, byte channels, byte bitRate, int sampleRate) {
        super(null);
        this.audioCodec = codec;
        this.reserved = reserved;
        this.channels = channels;
        this.bitRate = bitRate;
        this.sampleRate = sampleRate;
    }

    public AudioHeader(ByteBuffer byteBuffer) {
        super(byteBuffer);
        this.audioCodec = AudioCodec.fromType(byteBuffer.get());
        this.reserved = byteBuffer.get();
        this.channels = byteBuffer.get();
        this.bitRate = byteBuffer.get();
        this.sampleRate = byteBuffer.getInt();
    }

    @Override // com.kopin.accessory.base.PacketSubHeader
    public void write(ByteBuffer byteBuffer) {
        byteBuffer.put(this.audioCodec.getValue());
        byteBuffer.put(this.reserved);
        byteBuffer.put(this.channels);
        byteBuffer.put(this.bitRate);
        byteBuffer.putInt(this.sampleRate);
    }

    @Override // com.kopin.accessory.base.PacketSubHeader
    public int getSize() {
        return 8;
    }

    public AudioCodec getAudioCodec() {
        return this.audioCodec;
    }

    public byte getChannels() {
        return this.channels;
    }

    public byte getBitRate() {
        return this.bitRate;
    }

    public int getSampleRate() {
        return this.sampleRate;
    }
}
