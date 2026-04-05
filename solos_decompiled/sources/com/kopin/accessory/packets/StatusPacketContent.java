package com.kopin.accessory.packets;

import com.kopin.accessory.packets.base.FlagDescriptor;
import com.kopin.accessory.packets.base.FlagPacketContent;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes14.dex */
public class StatusPacketContent extends FlagPacketContent {
    private static FlagDescriptor[] DESCRIPTOR = {StatusType.AMBIENT_LIGHT.getDescriptor(), StatusType.DEVICE_ORIENTATION.getDescriptor(), StatusType.DISPLAY_BRIGHTNESS.getDescriptor(), StatusType.SPEAKER_VOLUME.getDescriptor(), StatusType.AMBIENT_NOISE.getDescriptor(), StatusType.LEFT_MICROPHONE_LEVEL.getDescriptor(), StatusType.RIGHT_MICROPHONE_LEVEL.getDescriptor(), StatusType.BATTERY_LEVEL.getDescriptor(), StatusType.POWER_AVAILABLE.getDescriptor()};

    public StatusPacketContent() {
    }

    public StatusPacketContent(ByteBuffer byteBuffer) {
        super(byteBuffer);
    }

    @Override // com.kopin.accessory.packets.base.FlagPacketContent
    protected FlagDescriptor[] getDescriptors() {
        return DESCRIPTOR;
    }
}
