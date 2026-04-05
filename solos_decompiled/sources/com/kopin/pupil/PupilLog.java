package com.kopin.pupil;

import com.kopin.accessory.base.Connection;
import com.kopin.accessory.base.Packet;
import com.kopin.accessory.base.PacketContent;
import com.kopin.accessory.base.PacketReceivedListener;
import com.kopin.accessory.base.PacketType;
import com.kopin.accessory.packets.LogPacketContent;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes25.dex */
public class PupilLog extends PupilDevice {
    private static ArrayList<LogListener> mListeners = new ArrayList<>();
    private static final PacketReceivedListener mLogResponse = new PacketReceivedListener() { // from class: com.kopin.pupil.PupilLog.1
        @Override // com.kopin.accessory.base.PacketReceivedListener
        public void onPacketReceived(Connection connection, Packet packet) {
            LogPacketContent line = (LogPacketContent) packet.getContent();
            if (line != null) {
                for (LogListener cb : PupilLog.mListeners) {
                    cb.onLogLine(line);
                }
            }
        }
    };

    public interface LogListener {
        void onLogLine(LogPacketContent logPacketContent);
    }

    public static void startLogging(LogListener cb) {
        if (cb != null && !mListeners.contains(cb)) {
            if (mListeners.isEmpty()) {
                registerForPackets(PacketType.LOG_RESPONSE, mLogResponse);
            }
            mListeners.add(cb);
        }
    }

    public static void stopLogging(LogListener cb) {
        if (cb != null) {
            if (mListeners.contains(cb)) {
                mListeners.remove(cb);
            }
            if (mListeners.isEmpty()) {
                unregisterForPackets(PacketType.LOG_RESPONSE, mLogResponse);
            }
        }
    }

    public static void requestLines(int from, int to) {
        sendPacket(LogPacketContent.newLogQuery(from, to));
    }

    public static void requestClear() {
        sendPacket(new Packet(PacketType.LOG_CLEAR, PacketContent.Empty.noContent()));
    }
}
