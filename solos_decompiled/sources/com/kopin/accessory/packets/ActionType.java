package com.kopin.accessory.packets;

import com.kopin.accessory.packets.base.IntPacketContent;

/* JADX INFO: loaded from: classes14.dex */
public enum ActionType {
    NONE(0),
    BUTTON_SHORT(1),
    BUTTON_LONG(2),
    BUTTON_MAKE(11),
    BUTTON_BREAK(12),
    BUTTON_REPEAT(13),
    POWER_CHARGING(4),
    POWER_DISCHARGING(5),
    PLAY_CHIRP(6),
    PLAY_LISTENING_CHIME(7),
    PLAY_PROCESSING_CHIME(8),
    AUDIO_BUFFER_FULL(9),
    AUDIO_BUFFER_EMPTY(10),
    ALS_EVENT(3),
    PROXIMITY_EVENT(14),
    TAP_EVENT(15),
    VAD_SILENCE_DETECT(18),
    VAD_VOICE_PRESENT(19),
    MAX(255);

    private final int mValue;

    ActionType(int val) {
        this.mValue = val;
    }

    public int getValue() {
        return this.mValue;
    }

    public static ActionType fromInt(int val) {
        for (ActionType a : values()) {
            if (a.mValue == val) {
                return a;
            }
        }
        return NONE;
    }

    public static ActionType fromIntPacket(IntPacketContent content) {
        return fromInt(content.getValue());
    }
}
