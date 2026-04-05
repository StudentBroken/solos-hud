package com.kopin.accessory.base;

import com.kopin.accessory.base.PacketContent;
import com.kopin.accessory.packets.AntResponseContent;
import com.kopin.accessory.packets.AudioEnablePacketContent;
import com.kopin.accessory.packets.AudioPacketContent;
import com.kopin.accessory.packets.CapabilityPacketContent;
import com.kopin.accessory.packets.DebugPacketContent;
import com.kopin.accessory.packets.DisplayModePacketContent;
import com.kopin.accessory.packets.FlashChecksumContent;
import com.kopin.accessory.packets.FlashDataContent;
import com.kopin.accessory.packets.FlashEraseContent;
import com.kopin.accessory.packets.FlashResponseContent;
import com.kopin.accessory.packets.ImagePacketContent;
import com.kopin.accessory.packets.LogPacketContent;
import com.kopin.accessory.packets.PhoneNumberPacketContent;
import com.kopin.accessory.packets.StatusPacketContent;
import com.kopin.accessory.packets.TimeStampPacketContent;
import com.kopin.accessory.packets.WhisperConfigPacketContent;
import com.kopin.accessory.packets.base.ActionPacketContent;
import com.kopin.accessory.packets.base.BytePacketContent;
import com.kopin.accessory.packets.base.CallActionPacketContent;
import com.kopin.accessory.packets.base.IntPacketContent;
import com.kopin.accessory.packets.base.MessagePacketContent;
import com.kopin.accessory.utility.EnumConverter;
import com.kopin.accessory.utility.ReverseEnumMap;

/* JADX INFO: loaded from: classes14.dex */
public enum PacketType implements EnumConverter<Short> {
    PING(1, MessagePacketContent.class),
    PONG(34, PacketContent.Empty.class),
    ACTION(2, ActionPacketContent.class),
    AUDIO_ENABLE(11, AudioEnablePacketContent.class),
    AUDIO_DISABLE(12, MessagePacketContent.class),
    AUDIO(3, AudioPacketContent.class),
    AUDIO_END(21, IntPacketContent.class),
    WAKE_UP(4, MessagePacketContent.class),
    SLEEP(22, MessagePacketContent.class),
    IMAGE(5, ImagePacketContent.class),
    IMAGE_REQUEST(6, MessagePacketContent.class),
    IMAGE_SYNC(38, IntPacketContent.class),
    CAPABILITIES_GET(7, IntPacketContent.class),
    CAPABILITIES_RESPONSE(8, CapabilityPacketContent.class),
    CAPABILITIES_SET(9, CapabilityPacketContent.class),
    CALL_STATE_QUERY(10, PacketContent.Empty.class),
    CALL_STATE_RESPONSE(13, BytePacketContent.class),
    CALL_NUMBER_QUERY(14, PacketContent.Empty.class),
    CALL_NUMBER_RESPONSE(15, PhoneNumberPacketContent.class),
    CALL_ACTION(16, CallActionPacketContent.class),
    STATUS_GET(18, IntPacketContent.class),
    STATUS_RESPONSE(19, StatusPacketContent.class),
    STATUS_SET(20, StatusPacketContent.class),
    CALL_RESTART_HFP(23, MessagePacketContent.class),
    FLASH_PROGRAM(24, FlashDataContent.class),
    FLASH_RESPONSE(25, FlashResponseContent.class),
    FLASH_CHECKSUM_QUERY(26, FlashDataContent.class),
    FLASH_CHECKSUM_RESPONSE(27, FlashChecksumContent.class),
    FLASH_ERASE(28, FlashEraseContent.class),
    QUERY_BOOT_BANK(29, PacketContent.Empty.class),
    SWITCH_BOOT_BANK(30, PacketContent.Empty.class),
    FLASH_VERIFY(31, FlashDataContent.class),
    SET_DISPLAY_MODE(33, DisplayModePacketContent.class),
    DISCONNECT_AND_FORGET(35, PacketContent.Empty.class),
    ANT_COMMAND(36, BytePacketContent.class),
    ANT_RESPONSE(37, AntResponseContent.class),
    LOG_QUERY(44, LogPacketContent.class),
    LOG_APPEND(45, DebugPacketContent.class),
    LOG_CLEAR(46, PacketContent.Empty.class),
    LOG_RESPONSE(47, LogPacketContent.class),
    VAD_REQUEST(49, BytePacketContent.class),
    VAD_RESPONSE(50, BytePacketContent.class),
    TIME_SET(56, TimeStampPacketContent.class),
    DEBUG_VOICE_COMMAND_LIST(4095, DebugPacketContent.class),
    DEBUG_GARMIN_REMOTE(3841, IntPacketContent.class),
    DEBUG_WHISPER_SET(4083, WhisperConfigPacketContent.class),
    DEBUG_WHISPER_QUERY(4084, BytePacketContent.class),
    DEBUG_WHISPER_RESPONSE(4085, WhisperConfigPacketContent.class),
    DEBUG_VOICE_COMMAND(4094, DebugPacketContent.class);

    private static final ReverseEnumMap<Short, PacketType> map = new ReverseEnumMap<>(PacketType.class);
    private final Class<?> packetClass;
    private final short value;

    public static PacketType fromType(short type) {
        PacketType ret = (PacketType) map.get(Short.valueOf(type));
        if (ret == null) {
            System.out.println("PacketType: Couldn't find a mapping for type = " + ((int) type));
        }
        return ret;
    }

    PacketType(short value, Class cls) {
        this.value = value;
        this.packetClass = cls;
    }

    public short getValue() {
        return this.value;
    }

    public Class<?> getPacketClass() {
        return this.packetClass;
    }

    @Override // com.kopin.accessory.utility.EnumConverter
    public Short convert() {
        return Short.valueOf(this.value);
    }
}
