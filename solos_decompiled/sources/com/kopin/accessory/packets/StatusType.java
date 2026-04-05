package com.kopin.accessory.packets;

import com.kopin.accessory.packets.base.FlagDescriptor;
import com.kopin.accessory.packets.base.FlagPacketEnum;

/* JADX INFO: loaded from: classes14.dex */
public enum StatusType implements FlagPacketEnum {
    AMBIENT_LIGHT(new FlagDescriptor(1, Integer.TYPE, 1)),
    DEVICE_ORIENTATION(new FlagDescriptor(2, Short.TYPE, 3)),
    DISPLAY_BRIGHTNESS(new FlagDescriptor(4, Byte.TYPE, 1)),
    SPEAKER_VOLUME(new FlagDescriptor(8, Byte.TYPE, 1)),
    AMBIENT_NOISE(new FlagDescriptor(16, Byte.TYPE, 16)),
    LEFT_MICROPHONE_LEVEL(new FlagDescriptor(32, Byte.TYPE, 1)),
    RIGHT_MICROPHONE_LEVEL(new FlagDescriptor(64, Byte.TYPE, 1)),
    BATTERY_LEVEL(new FlagDescriptor(256, Byte.TYPE, 1)),
    POWER_AVAILABLE(new FlagDescriptor(512, Byte.TYPE, 1));

    private final FlagDescriptor descriptor;

    StatusType(FlagDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    @Override // com.kopin.accessory.packets.base.FlagPacketEnum
    public int getFlag() {
        return this.descriptor.getFlag();
    }

    @Override // com.kopin.accessory.packets.base.FlagPacketEnum
    public FlagDescriptor getDescriptor() {
        return this.descriptor;
    }
}
