package com.kopin.accessory.packets;

import com.kopin.accessory.base.Packet;
import com.kopin.accessory.base.PacketContent;
import com.kopin.accessory.base.PacketType;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import org.apache.commons.lang3.CharEncoding;

/* JADX INFO: loaded from: classes14.dex */
public final class DebugPacketContent extends PacketContent {
    private byte[] mCmd;
    private String[] mList;

    public DebugPacketContent(ByteBuffer byteBuffer) {
        super(byteBuffer);
        this.mCmd = null;
        int len = byteBuffer.limit();
        byte[] chars = new byte[len];
        byteBuffer.get(chars);
        try {
            String commands = new String(chars, CharEncoding.UTF_16LE);
            this.mList = commands.split("\\|\\|");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private DebugPacketContent(String cmd) {
        super(null);
        this.mCmd = null;
        try {
            this.mCmd = cmd.getBytes(CharEncoding.UTF_16LE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static Packet newCommandPacket(String cmd) {
        DebugPacketContent payload = new DebugPacketContent(cmd);
        return new Packet(PacketType.DEBUG_VOICE_COMMAND, payload);
    }

    @Override // com.kopin.accessory.base.PacketContent
    public void write(ByteBuffer byteBuffer) {
        if (this.mCmd != null) {
            byteBuffer.put(this.mCmd);
        }
    }

    @Override // com.kopin.accessory.base.PacketContent
    public int getSize() {
        if (this.mCmd != null) {
            return this.mCmd.length;
        }
        return 0;
    }

    public boolean isVoconList() {
        return this.mList != null;
    }

    public String[] getVoconList() {
        return this.mList;
    }
}
