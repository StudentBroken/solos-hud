package com.kopin.accessory;

import com.kopin.accessory.utility.EnumConverter;
import com.kopin.accessory.utility.ReverseEnumMap;

/* JADX INFO: loaded from: classes14.dex */
public enum AudioCodec implements EnumConverter<Byte> {
    NULL((byte) 0),
    PCM((byte) 1),
    SBC((byte) 3),
    MSBC((byte) 4);

    private static final ReverseEnumMap<Byte, AudioCodec> map = new ReverseEnumMap<>(AudioCodec.class);
    private final byte value;

    public static AudioCodec fromType(byte type) {
        return (AudioCodec) map.get(Byte.valueOf(type));
    }

    AudioCodec(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return this.value;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.kopin.accessory.utility.EnumConverter
    public Byte convert() {
        return Byte.valueOf(this.value);
    }
}
