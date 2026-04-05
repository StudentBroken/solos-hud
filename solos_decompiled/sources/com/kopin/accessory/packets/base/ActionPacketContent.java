package com.kopin.accessory.packets.base;

import com.kopin.accessory.base.PacketContent;
import com.kopin.accessory.packets.ActionType;
import com.kopin.accessory.packets.ButtonType;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes14.dex */
public final class ActionPacketContent extends PacketContent {
    private final ActionType action;
    private final ButtonPacketContent button;

    public ActionPacketContent(ActionType action) {
        super(null);
        this.action = action;
        this.button = null;
    }

    public ActionPacketContent(ActionType action, ButtonType button, short holdTime) {
        super(null);
        this.action = action;
        this.button = new ButtonPacketContent(button, holdTime);
    }

    public ActionPacketContent(ByteBuffer byteBuffer) {
        super(byteBuffer);
        this.action = ActionType.fromInt(byteBuffer.getInt());
        switch (this.action) {
            case BUTTON_SHORT:
            case BUTTON_LONG:
            case BUTTON_BREAK:
            case BUTTON_MAKE:
            case BUTTON_REPEAT:
                this.button = new ButtonPacketContent(byteBuffer);
                break;
            default:
                this.button = null;
                break;
        }
    }

    @Override // com.kopin.accessory.base.PacketContent
    public void write(ByteBuffer byteBuffer) {
        byteBuffer.putInt(this.action.getValue());
    }

    @Override // com.kopin.accessory.base.PacketContent
    public int getSize() {
        return 4;
    }

    public ActionType getAction() {
        return this.action;
    }

    public ButtonPacketContent getButtonPress() {
        return this.button;
    }
}
