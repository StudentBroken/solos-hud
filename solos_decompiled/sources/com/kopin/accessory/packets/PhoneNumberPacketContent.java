package com.kopin.accessory.packets;

import com.kopin.accessory.base.Packet;
import com.kopin.accessory.base.PacketContent;
import com.kopin.accessory.base.PacketType;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes14.dex */
public final class PhoneNumberPacketContent extends PacketContent {
    private static final int DEFAULT_STRING_SIZE_BYTES = 30;
    private String mNumber;

    public PhoneNumberPacketContent(ByteBuffer byteBuffer) {
        super(byteBuffer);
        this.mNumber = null;
        int len = byteBuffer.limit();
        byte[] chars = new byte[len];
        byteBuffer.get(chars);
        try {
            this.mNumber = new String(chars, "UTF-8");
            this.mNumber = this.mNumber.substring(0, this.mNumber.indexOf(0));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private PhoneNumberPacketContent(String number) {
        super(null);
        this.mNumber = null;
        this.mNumber = number;
    }

    public static Packet newPhoneNumberPacket(String number) {
        PhoneNumberPacketContent payload = new PhoneNumberPacketContent(number);
        return new Packet(PacketType.DEBUG_VOICE_COMMAND, payload);
    }

    @Override // com.kopin.accessory.base.PacketContent
    public void write(ByteBuffer byteBuffer) {
        byte[] bytes = new byte[30];
        int remain = 30;
        try {
            byte[] encoded = this.mNumber.getBytes("UTF-8");
            if (encoded.length <= 30) {
                byteBuffer.put(encoded);
                remain = 30 - encoded.length;
            } else {
                byteBuffer.put(encoded, 0, 30);
                remain = 0;
            }
        } catch (UnsupportedEncodingException e) {
        }
        if (remain > 0) {
            byteBuffer.put(bytes, 0, remain);
        }
    }

    @Override // com.kopin.accessory.base.PacketContent
    public int getSize() {
        return 30;
    }

    public String getNumber() {
        return this.mNumber;
    }
}
