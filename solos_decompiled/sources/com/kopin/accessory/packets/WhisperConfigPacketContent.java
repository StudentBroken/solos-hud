package com.kopin.accessory.packets;

import com.kopin.accessory.base.PacketContent;
import com.kopin.accessory.utility.WhisperConfig;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes14.dex */
public class WhisperConfigPacketContent extends PacketContent {
    private final WhisperConfig mConfig;

    public WhisperConfigPacketContent(WhisperConfig config) {
        super(null);
        this.mConfig = config;
    }

    public WhisperConfigPacketContent(ByteBuffer buffer) {
        super(buffer);
        this.mConfig = new WhisperConfig();
        this.mConfig.isPersisted = buffer.get() != 0;
        this.mConfig.isOutputEqualized = buffer.get() != 0;
        this.mConfig.isAVADEnabled = buffer.get() != 0;
        this.mConfig.vadMode = buffer.get();
        this.mConfig.micGain[0] = buffer.getInt();
        this.mConfig.micGain[1] = buffer.getInt();
        this.mConfig.micGain[2] = buffer.getInt();
        this.mConfig.micGain[3] = buffer.getInt();
        this.mConfig.sensitivityVAD = buffer.getInt();
        this.mConfig.AVADThreshold[0] = buffer.getInt();
        this.mConfig.AVADThreshold[1] = buffer.getInt();
        this.mConfig.AVADThreshold[2] = buffer.getInt();
        this.mConfig.AVADValues[0] = buffer.getInt();
        this.mConfig.AVADValues[1] = buffer.getInt();
        this.mConfig.AVADValues[2] = buffer.getInt();
        this.mConfig.AVADValues[3] = buffer.getInt();
        this.mConfig.outputConfig[0] = buffer.getInt();
        this.mConfig.outputConfig[1] = buffer.getInt();
        this.mConfig.outputConfig[2] = buffer.getInt();
    }

    @Override // com.kopin.accessory.base.PacketContent
    public void write(ByteBuffer byteBuffer) {
        byteBuffer.put((byte) (this.mConfig.isPersisted ? 1 : 0));
        byteBuffer.put((byte) (this.mConfig.isOutputEqualized ? 1 : 0));
        byteBuffer.put((byte) (this.mConfig.isAVADEnabled ? 1 : 0));
        byteBuffer.put(this.mConfig.vadMode);
        byteBuffer.putInt(this.mConfig.micGain[0]);
        byteBuffer.putInt(this.mConfig.micGain[1]);
        byteBuffer.putInt(this.mConfig.micGain[2]);
        byteBuffer.putInt(this.mConfig.micGain[3]);
        byteBuffer.putInt(this.mConfig.sensitivityVAD);
        byteBuffer.putInt(this.mConfig.AVADThreshold[0]);
        byteBuffer.putInt(this.mConfig.AVADThreshold[1]);
        byteBuffer.putInt(this.mConfig.AVADThreshold[2]);
        byteBuffer.putInt(this.mConfig.AVADValues[0]);
        byteBuffer.putInt(this.mConfig.AVADValues[1]);
        byteBuffer.putInt(this.mConfig.AVADValues[2]);
        byteBuffer.putInt(this.mConfig.AVADValues[3]);
        byteBuffer.putInt(this.mConfig.outputConfig[0]);
        byteBuffer.putInt(this.mConfig.outputConfig[1]);
        byteBuffer.putInt(this.mConfig.outputConfig[2]);
    }

    @Override // com.kopin.accessory.base.PacketContent
    public int getSize() {
        return 64;
    }

    public WhisperConfig getData() {
        return this.mConfig;
    }
}
