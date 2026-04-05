package com.kopin.accessory.packets;

/* JADX INFO: loaded from: classes14.dex */
public enum ButtonType {
    MAIN(0),
    FRONT(1),
    BACK(2),
    UNKNOWN(-1);

    private final byte buttonCode;

    ButtonType(int code) {
        this.buttonCode = (byte) code;
    }

    public byte getCode() {
        return this.buttonCode;
    }

    public static ButtonType fromValue(byte val) {
        for (ButtonType b : values()) {
            if (b.buttonCode == val) {
                return b;
            }
        }
        return UNKNOWN;
    }
}
