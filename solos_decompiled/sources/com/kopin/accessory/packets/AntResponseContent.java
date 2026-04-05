package com.kopin.accessory.packets;

import com.kopin.accessory.base.PacketContent;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes14.dex */
public final class AntResponseContent extends PacketContent {
    private final int deviceId;
    private final AntCommand gcd;
    private final String msg;
    private final ResponseType type;

    public enum ResponseType {
        MESSAGE(0),
        OK(1),
        STARTING(2),
        ADVERTISING(3),
        CONNECT(4),
        DISCONNECT(5),
        SHUTDOWN(6),
        DFU_START(7),
        DFU_WAIT(8),
        DFU_ACTIVE(9),
        GCD_ENABLED(10),
        GCD_DISABLED(11),
        GCD_COMMAND(12),
        NONE(255);

        private int value;

        ResponseType(int val) {
            this.value = val;
        }

        public static ResponseType fromValue(int val) {
            for (ResponseType r : values()) {
                if (r.value == val) {
                    return r;
                }
            }
            return NONE;
        }
    }

    public static class AntCommand {
        public int Command;
        public int ManufacturerId;
        public int Sequence;
        public int SerialNumber;

        private AntCommand() {
        }

        static AntCommand fromBuffer(ByteBuffer buffer) {
            AntCommand self = new AntCommand();
            self.SerialNumber = buffer.getShort() & 65535;
            self.ManufacturerId = buffer.getShort() & 65535;
            self.Sequence = buffer.get() & 255;
            self.Command = buffer.getShort() & 65535;
            return self;
        }
    }

    public AntResponseContent(ByteBuffer byteBuffer) {
        super(byteBuffer);
        this.type = ResponseType.fromValue(byteBuffer.get());
        switch (this.type) {
            case MESSAGE:
                this.deviceId = -1;
                this.gcd = null;
                byte[] buf = new byte[byteBuffer.limit()];
                String tmp = null;
                try {
                    String tmp2 = new String(buf, "utf-8");
                    tmp = tmp2;
                } catch (UnsupportedEncodingException e) {
                }
                this.msg = tmp;
                break;
            case CONNECT:
            case DISCONNECT:
            case DFU_START:
                this.deviceId = byteBuffer.getInt();
                this.msg = null;
                this.gcd = null;
                break;
            case GCD_COMMAND:
                this.deviceId = -1;
                this.msg = null;
                this.gcd = AntCommand.fromBuffer(byteBuffer);
                break;
            default:
                this.deviceId = -1;
                this.msg = null;
                this.gcd = null;
                break;
        }
    }

    @Override // com.kopin.accessory.base.PacketContent
    public void write(ByteBuffer byteBuffer) {
        throw new IllegalArgumentException("Client side support not implemented for this packet type!");
    }

    @Override // com.kopin.accessory.base.PacketContent
    public int getSize() {
        return 4;
    }

    public ResponseType getType() {
        return this.type;
    }

    public String getMessage() {
        return this.msg;
    }

    public int getDeviceId() {
        return this.deviceId;
    }

    public AntCommand getCommand() {
        return this.gcd;
    }
}
