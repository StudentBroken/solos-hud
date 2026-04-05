package com.kopin.accessory.packets;

import com.kopin.accessory.packets.base.FlagDescriptor;
import com.kopin.accessory.packets.base.FlagPacketEnum;

/* JADX INFO: loaded from: classes14.dex */
public enum CapabilityType implements FlagPacketEnum {
    PROTOCOL_VERSION(new FlagDescriptor(1, Byte.TYPE, 4)),
    FIRMWARE_VERSION(new FlagDescriptor(16, Byte.TYPE, 4)),
    BIT_DEPTH(new FlagDescriptor(2, Byte.TYPE, 1)),
    SUPPORTED_IMAGE_FORMATS(new FlagDescriptor(4, Byte.TYPE, 2)),
    HEADSET_SERIAL_NUMBER(new FlagDescriptor(8, Byte.TYPE, 16)),
    MANUFACTURER(new FlagDescriptor(32, String.class, 30)),
    MODEL(new FlagDescriptor(64, String.class, 30)),
    DEVICE_NAME(new FlagDescriptor(128, String.class, 30)),
    DISPLAY_RESOLUTION(new FlagDescriptor(256, Short.TYPE, 2)),
    BUTTONS(new FlagDescriptor(512, Byte.TYPE, 3)),
    ANT_MODULE(new FlagDescriptor(1024, Byte.TYPE, 8)),
    ANT_MODULE_VERSION(new FlagDescriptor(262144, Byte.TYPE, 4)),
    FIRMWARE_BANKS(new FlagDescriptor(2048, Byte.TYPE, 2)),
    MAX_IMAGE_SIZE(new FlagDescriptor(524288, Short.TYPE, 2)),
    ENABLE_HFP_CONNECTION(new FlagDescriptor(1048576, Byte.TYPE, 1));

    private final FlagDescriptor descriptor;

    CapabilityType(FlagDescriptor descriptor) {
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
