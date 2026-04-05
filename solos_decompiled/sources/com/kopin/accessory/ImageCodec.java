package com.kopin.accessory;

import com.kopin.accessory.utility.CallHelper;
import com.kopin.accessory.utility.EnumConverter;
import com.kopin.accessory.utility.ReverseEnumMap;

/* JADX INFO: loaded from: classes14.dex */
public enum ImageCodec implements EnumConverter<Byte> {
    RGB565((byte) 1),
    RLE565((byte) 2),
    GIF((byte) 4),
    JPEG((byte) 8),
    COLOUR8(CallHelper.CallState.FLAG_HFP_STATE),
    COLOR8(CallHelper.CallState.FLAG_HFP_STATE),
    PNG(CallHelper.CallState.FLAG_SCO_STATE),
    SINGLE_COLOUR(CallHelper.CallState.FLAG_CALL_SETUP),
    SINGLE_COLOR(CallHelper.CallState.FLAG_CALL_SETUP),
    PSRLE16(CallHelper.CallState.FLAG_CALL_ACTIVE);

    private static final ReverseEnumMap<Byte, ImageCodec> map = new ReverseEnumMap<>(ImageCodec.class);
    private final byte value;

    ImageCodec(byte value) {
        this.value = value;
    }

    public static ImageCodec fromType(byte type) {
        return (ImageCodec) map.get(Byte.valueOf(type));
    }

    ImageCodec(byte value, Class cls) {
        this.value = value;
    }

    public short getValue() {
        return this.value;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.kopin.accessory.utility.EnumConverter
    public Byte convert() {
        return Byte.valueOf(this.value);
    }
}
