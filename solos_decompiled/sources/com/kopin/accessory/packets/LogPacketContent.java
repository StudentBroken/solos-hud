package com.kopin.accessory.packets;

import com.kopin.accessory.base.Packet;
import com.kopin.accessory.base.PacketContent;
import com.kopin.accessory.base.PacketType;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes14.dex */
public final class LogPacketContent extends PacketContent {
    public static final int END_OF_LOG = -1;
    private final int mCount;
    private final int mLineNum;
    private final String mMsg;
    private final int mTimeStamp;
    private final PacketType mType;

    public LogPacketContent(ByteBuffer byteBuffer) {
        super(byteBuffer);
        int len = byteBuffer.limit();
        if (len == 8) {
            this.mType = PacketType.LOG_QUERY;
        } else {
            this.mType = PacketType.LOG_RESPONSE;
        }
        this.mLineNum = byteBuffer.getInt();
        if (this.mType == PacketType.LOG_QUERY) {
            this.mCount = byteBuffer.getInt();
            this.mTimeStamp = -1;
            this.mMsg = null;
            return;
        }
        if (this.mLineNum != -1) {
            this.mCount = -1;
            this.mTimeStamp = byteBuffer.getInt();
            byte[] chars = new byte[len - 8];
            String msg = null;
            if (len - 8 > 0) {
                byteBuffer.get(chars);
                try {
                    msg = new String(chars, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            this.mMsg = msg;
            return;
        }
        this.mCount = -1;
        this.mTimeStamp = -1;
        this.mMsg = null;
    }

    private LogPacketContent(int from, int count) {
        super(null);
        this.mType = PacketType.LOG_QUERY;
        this.mLineNum = from;
        this.mCount = count;
        this.mTimeStamp = -1;
        this.mMsg = null;
    }

    public static Packet newLogQuery() {
        return newLogQuery(0, -1);
    }

    public static Packet newLogQuery(int from, int count) {
        LogPacketContent payload = new LogPacketContent(from, count);
        return new Packet(PacketType.LOG_QUERY, payload);
    }

    @Override // com.kopin.accessory.base.PacketContent
    public void write(ByteBuffer byteBuffer) {
        byteBuffer.putInt(this.mLineNum);
        if (this.mType == PacketType.LOG_QUERY) {
            byteBuffer.putInt(this.mCount);
            return;
        }
        if (this.mLineNum != -1) {
            byteBuffer.putInt(this.mTimeStamp);
            try {
                byte[] bytes = this.mMsg.getBytes("UTF-8");
                if (bytes != null && bytes.length > 0) {
                    byteBuffer.put(bytes);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.kopin.accessory.base.PacketContent
    public int getSize() {
        if (this.mType == PacketType.LOG_QUERY) {
            return 8;
        }
        if (this.mLineNum == -1) {
            return 4;
        }
        if (this.mMsg != null) {
            return this.mMsg.length() + 8;
        }
        return 0;
    }

    public int getLineNumber() {
        return this.mLineNum;
    }

    public int getTimestamp() {
        return this.mTimeStamp;
    }

    public String getMsg() {
        return this.mMsg;
    }

    public boolean isEndOfLog() {
        return this.mType == PacketType.LOG_RESPONSE && this.mLineNum == -1;
    }

    @Override // com.kopin.accessory.base.PacketContent
    public String toString() {
        return this.mType == PacketType.LOG_QUERY ? String.format("Log Request: %d + %d", Integer.valueOf(this.mLineNum), Integer.valueOf(this.mCount)) : String.format("%d: @%d - %s", Integer.valueOf(this.mLineNum), Integer.valueOf(this.mTimeStamp), this.mMsg);
    }
}
