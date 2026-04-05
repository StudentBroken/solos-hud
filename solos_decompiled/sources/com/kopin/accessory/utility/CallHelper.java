package com.kopin.accessory.utility;

import com.kopin.accessory.base.Packet;
import com.kopin.accessory.base.PacketContent;
import com.kopin.accessory.base.PacketType;
import com.kopin.accessory.packets.base.CallActionPacketContent;
import java.io.UnsupportedEncodingException;

/* JADX INFO: loaded from: classes14.dex */
public class CallHelper {

    public static class CallAction {
        public static final byte ANSWER = 1;
        public static final byte CLOSE_SCO = 7;
        public static final byte DIAL = 5;
        public static final byte HOLD = 3;
        public static final byte OPEN_SCO = 6;
        public static final byte TERMINATE = 2;
        public static final byte UNHOLD = 4;
    }

    public static class CallState {
        public static final byte CALL_ENDED = 14;
        public static final byte CALL_IN_PROGRESS = 13;
        public static final byte CALL_STATE_MASK = 15;
        public static final byte FLAG_CALL_ACTIVE = -128;
        public static final byte FLAG_CALL_IDLE = 0;
        public static final byte FLAG_CALL_MASK = -64;
        public static final byte FLAG_CALL_SETUP = 64;
        public static final byte FLAG_HFP_STATE = 16;
        public static final byte FLAG_SCO_STATE = 32;
        public static final byte HFP_DISABLED = 8;
        public static final byte HFP_DROPPED = 15;
        public static final byte HFP_ENABLED = 7;
        public static final byte INCOMING_CALL = 1;
        public static final byte INCOMING_CALL_ANSWERED = 2;
        public static final byte INCOMING_CALL_TERMINATED = 3;
        public static final byte NONE = 0;
        public static final byte OUTGOING_CALL_ANSWERED = 5;
        public static final byte OUTGOING_CALL_CONNECTING = 10;
        public static final byte OUTGOING_CALL_RINGING = 4;
        public static final byte OUTGOING_CALL_TERMINATED = 6;
        public static final byte SCO_CONNECTED = 11;
        public static final byte SCO_DISCONNECTED = 12;
    }

    public static Packet answerCall() {
        byte[] payload = {1};
        return new Packet(PacketType.CALL_ACTION, new CallActionPacketContent(payload));
    }

    public static Packet endCall() {
        byte[] payload = {2};
        return new Packet(PacketType.CALL_ACTION, new CallActionPacketContent(payload));
    }

    public static Packet openSCO() {
        byte[] payload = {6};
        return new Packet(PacketType.CALL_ACTION, new CallActionPacketContent(payload));
    }

    public static Packet dialNumber(String number) {
        byte[] payload = new byte[31];
        payload[0] = 5;
        try {
            byte[] numberBytes = number.replace("-", "").replace("(", "").replace(")", "").replace(" ", "").getBytes("ASCII");
            System.arraycopy(numberBytes, 0, payload, 1, Math.min(numberBytes.length, 30));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new Packet(PacketType.CALL_ACTION, new CallActionPacketContent(payload));
    }

    public static Packet requestState() {
        return new Packet(PacketType.CALL_STATE_QUERY, PacketContent.noContent());
    }

    public static Packet requestNumber() {
        return new Packet(PacketType.CALL_NUMBER_QUERY, PacketContent.noContent());
    }
}
