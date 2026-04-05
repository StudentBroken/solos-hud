package com.kopin.accessory.packets;

import com.kopin.accessory.packets.base.FlagDescriptor;
import com.kopin.accessory.packets.base.FlagPacketContent;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes14.dex */
public class CapabilityPacketContent extends FlagPacketContent {
    private static FlagDescriptor[] DESCRIPTOR = {CapabilityType.PROTOCOL_VERSION.getDescriptor(), CapabilityType.FIRMWARE_VERSION.getDescriptor(), CapabilityType.BIT_DEPTH.getDescriptor(), CapabilityType.SUPPORTED_IMAGE_FORMATS.getDescriptor(), CapabilityType.HEADSET_SERIAL_NUMBER.getDescriptor(), CapabilityType.MANUFACTURER.getDescriptor(), CapabilityType.MODEL.getDescriptor(), CapabilityType.DEVICE_NAME.getDescriptor(), CapabilityType.DISPLAY_RESOLUTION.getDescriptor(), CapabilityType.BUTTONS.getDescriptor(), CapabilityType.ANT_MODULE.getDescriptor(), CapabilityType.MAX_IMAGE_SIZE.getDescriptor(), CapabilityType.ENABLE_HFP_CONNECTION.getDescriptor(), CapabilityType.ANT_MODULE_VERSION.getDescriptor()};

    public CapabilityPacketContent() {
    }

    public CapabilityPacketContent(ByteBuffer byteBuffer) {
        super(byteBuffer);
    }

    @Override // com.kopin.accessory.packets.base.FlagPacketContent
    protected FlagDescriptor[] getDescriptors() {
        return DESCRIPTOR;
    }

    public static CapabilityPacketContent createSetDeviceName(String name) {
        CapabilityPacketContent self = new CapabilityPacketContent();
        self.putString(CapabilityType.DEVICE_NAME, name);
        return self;
    }
}
