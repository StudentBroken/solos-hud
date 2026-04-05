package com.kopin.accessory.base;

import android.support.v4.view.MotionEventCompat;
import com.kopin.pupil.aria.app.TimedAppState;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/* JADX INFO: loaded from: classes14.dex */
public class Connection {
    private static final long AUDIO_BURST_DELAY = 1000;
    private static final int AUDIO_BURST_LEN = 2000;
    private int audioBurstSize;
    private volatile boolean audioChannelOpen;
    private volatile boolean connectionClosed;
    private volatile boolean connectionWasLost;
    private boolean connectionWatchdog;
    protected PacketWatcher debugPacketWatcher;
    private final String deviceAddress;
    private final String deviceName;
    private Set<DisconnectedListener> disconnectedListeners;
    private InputStream inputStream;
    private long lastAudioBurst;
    private long lastRxStamp;
    private long lastTxStamp;
    private OutputStream outputStream;
    protected Map<PacketType, List<PacketReceivedListener>> packetListeners;
    private final Queue<Packet> packetTransmitQueue;
    private final Queue<Packet> packetTransmitQueueAudio;
    private ReceiveThread receiveThread;
    private final Object transmitLock;
    private TransmitThread transmitThread;
    private static final String TAG = Connection.class.getCanonicalName();
    private static char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public interface DisconnectedListener {
        void onDisconnected(Connection connection, boolean z);
    }

    public interface PacketWatcher extends PacketReceivedListener {
        void onPacketSent(Connection connection, Packet packet);
    }

    public Connection(InputStream inputStream, OutputStream outputStream) throws IOException {
        this("", "", inputStream, outputStream);
    }

    public Connection(String name, String address, InputStream input, OutputStream output) throws IOException {
        this.transmitLock = new Object();
        this.connectionWatchdog = false;
        this.deviceName = name;
        this.deviceAddress = address;
        this.packetTransmitQueue = new LinkedList();
        this.packetTransmitQueueAudio = new LinkedList();
        this.audioChannelOpen = true;
        this.packetListeners = new HashMap();
        this.disconnectedListeners = new HashSet();
        this.connectionWasLost = false;
        this.connectionClosed = true;
        this.inputStream = input;
        this.outputStream = output;
    }

    public void open() {
        this.connectionClosed = false;
        this.receiveThread = new ReceiveThread(this, String.format("RECEIVE: %s(%s)", this.deviceName, this.deviceAddress));
        this.receiveThread.start();
        this.transmitThread = new TransmitThread(this, String.format("TRANSMIT: %s(%s)", this.deviceName, this.deviceAddress));
        this.transmitThread.start();
    }

    public void reopen(InputStream input, OutputStream output) {
        if (this.connectionClosed) {
            this.connectionClosed = false;
            this.audioChannelOpen = true;
            this.inputStream = input;
            this.outputStream = output;
            open();
        }
    }

    public void close() {
        if (!this.connectionClosed) {
            this.connectionClosed = true;
            if (this.transmitThread.isAlive()) {
                try {
                    this.transmitThread.interrupt();
                    this.transmitThread.join();
                } catch (InterruptedException e) {
                }
            }
            if (this.receiveThread.isAlive()) {
                try {
                    this.receiveThread.interrupt();
                    this.receiveThread.join();
                } catch (InterruptedException e2) {
                }
            }
            this.lastRxStamp = 0L;
            this.lastTxStamp = 0L;
            onDisconnected(this.connectionWasLost);
        }
    }

    private void onDisconnected(boolean wasLoss) {
        synchronized (this.disconnectedListeners) {
            for (DisconnectedListener listener : this.disconnectedListeners) {
                listener.onDisconnected(this, wasLoss);
            }
        }
    }

    public void registerForPacket(PacketType type, PacketReceivedListener listener) {
        if (listener != null) {
            synchronized (this.packetListeners) {
                if (!this.packetListeners.containsKey(type)) {
                    this.packetListeners.put(type, new ArrayList());
                }
                List<PacketReceivedListener> listeners = this.packetListeners.get(type);
                listeners.add(listener);
            }
        }
    }

    public void unregisterForPacket(PacketType type, PacketReceivedListener listener) {
        synchronized (this.packetListeners) {
            if (this.packetListeners.containsKey(type)) {
                List<PacketReceivedListener> listeners = this.packetListeners.get(type);
                if (listener == null) {
                    listeners.clear();
                } else {
                    listeners.remove(listener);
                }
            }
        }
    }

    public void setPacketWatcher(PacketWatcher packetWatcher) {
        this.debugPacketWatcher = packetWatcher;
    }

    public void registerForDisconnect(DisconnectedListener listener) {
        synchronized (this.disconnectedListeners) {
            this.disconnectedListeners.add(listener);
        }
    }

    public void unregisterForDisconnect(DisconnectedListener listener) {
        synchronized (this.disconnectedListeners) {
            if (listener == null) {
                this.disconnectedListeners.clear();
            } else {
                this.disconnectedListeners.remove(listener);
            }
        }
    }

    public void enqueuePacket(Packet packet) {
        if (packet.getType() == PacketType.AUDIO || packet.getType() == PacketType.AUDIO_END || packet.getType() == PacketType.AUDIO_ENABLE || packet.getType() == PacketType.AUDIO_DISABLE) {
            synchronized (this.packetTransmitQueueAudio) {
                this.packetTransmitQueueAudio.add(packet);
            }
        } else {
            synchronized (this.packetTransmitQueue) {
                this.packetTransmitQueue.add(packet);
            }
        }
        synchronized (this.transmitLock) {
            this.transmitLock.notify();
        }
    }

    public void clearQueuedPackets() {
        synchronized (this.packetTransmitQueue) {
            this.packetTransmitQueue.clear();
        }
        synchronized (this.packetTransmitQueueAudio) {
            this.packetTransmitQueueAudio.clear();
        }
    }

    public Queue<Packet> getQueuedPackets() {
        LinkedList linkedList;
        synchronized (this.packetTransmitQueue) {
            linkedList = new LinkedList(this.packetTransmitQueue);
        }
        return linkedList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Packet dequeuePacket() throws InterruptedException {
        boolean wait;
        while (!this.connectionClosed) {
            synchronized (this.packetTransmitQueue) {
                wait = this.packetTransmitQueue.isEmpty();
            }
            synchronized (this.packetTransmitQueueAudio) {
                if (this.audioChannelOpen && !this.packetTransmitQueueAudio.isEmpty()) {
                    wait = false;
                }
            }
            if (wait && !this.connectionClosed) {
                synchronized (this.transmitLock) {
                    this.transmitLock.wait();
                }
            }
            if (this.connectionClosed) {
                break;
            }
            synchronized (this.packetTransmitQueueAudio) {
                if (this.audioChannelOpen && !this.packetTransmitQueueAudio.isEmpty()) {
                    return this.packetTransmitQueueAudio.poll();
                }
                synchronized (this.packetTransmitQueue) {
                    if (!this.packetTransmitQueue.isEmpty()) {
                        return this.packetTransmitQueue.poll();
                    }
                }
            }
        }
        return null;
    }

    public void setAudioFlow(boolean onOrOff) {
        this.audioChannelOpen = onOrOff;
        if (this.audioChannelOpen) {
            this.lastAudioBurst = 0L;
        }
        synchronized (this.transmitLock) {
            this.transmitLock.notify();
        }
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public String getDeviceAddress() {
        return this.deviceAddress;
    }

    private static class TransmitThread extends Thread {
        private final Connection connection;
        private final OutputStream outputStream;
        private ByteBuffer packetBuffer;

        public TransmitThread(Connection connection, String identifier) {
            super(String.format("Transmit Thread %s", identifier));
            this.connection = connection;
            this.outputStream = connection.outputStream;
            this.packetBuffer = ByteBuffer.allocate(15000);
            this.packetBuffer.order(ByteOrder.LITTLE_ENDIAN);
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Packet packet;
            this.connection.lastTxStamp = this.connection.connectionWatchdog ? System.currentTimeMillis() : 0L;
            while (!isInterrupted() && (packet = this.connection.dequeuePacket()) != null) {
                try {
                    if (this.connection.lastRxStamp > 0 && System.currentTimeMillis() - this.connection.lastRxStamp > TimedAppState.DEFAULT_CONFIRM_TIMEOUT) {
                        this.connection.connectionWasLost = true;
                        interrupt();
                    }
                    if (packet.getSize() > this.packetBuffer.capacity()) {
                        allocateBuffer(packet.getSize());
                    }
                    this.packetBuffer.clear();
                    packet.write(this.packetBuffer);
                    try {
                        this.packetBuffer.flip();
                        this.outputStream.write(this.packetBuffer.array(), 0, this.packetBuffer.limit());
                        if (this.connection.packetTransmitQueue.isEmpty()) {
                            this.outputStream.flush();
                        }
                        if (this.connection.debugPacketWatcher != null) {
                            this.connection.debugPacketWatcher.onPacketSent(this.connection, packet);
                        }
                        this.connection.lastTxStamp = this.connection.connectionWatchdog ? System.currentTimeMillis() : 0L;
                    } catch (IOException e) {
                        this.connection.connectionWasLost = true;
                        interrupt();
                    }
                } catch (InterruptedException e2) {
                }
            }
            try {
                this.outputStream.close();
            } catch (IOException e3) {
            }
            this.connection.close();
        }

        private void allocateBuffer(int size) {
            this.packetBuffer = ByteBuffer.allocate(size);
            this.packetBuffer.order(ByteOrder.LITTLE_ENDIAN);
        }
    }

    private static class ReceiveThread extends Thread {
        private final Connection connection;
        private final InputStream inputStream;
        private ByteBuffer packetBuffer;

        public ReceiveThread(Connection connection, String identifier) {
            super(String.format("Transmit Thread %s", identifier));
            this.connection = connection;
            this.inputStream = connection.inputStream;
            this.packetBuffer = ByteBuffer.allocate(15000);
            this.packetBuffer.order(ByteOrder.LITTLE_ENDIAN);
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            this.connection.lastRxStamp = this.connection.connectionWatchdog ? System.currentTimeMillis() : 0L;
            while (!this.connection.connectionClosed && !isInterrupted()) {
                try {
                    this.packetBuffer.clear();
                    finalMagic();
                    readAmount(8);
                    this.connection.lastRxStamp = this.connection.connectionWatchdog ? System.currentTimeMillis() : 0L;
                    this.packetBuffer.flip();
                    PacketHeader header = new PacketHeader(this.packetBuffer);
                    if (header.getPacketType() == null) {
                        System.out.println("Unknown packet found, skipping " + header.getPayloadLength() + " bytes.");
                    } else {
                        prepareBuffer(header);
                        PacketContent content = parsePacketContent(header);
                        if (content != null) {
                            Packet packet = new Packet(header, content);
                            postPacket(packet);
                        }
                    }
                } catch (IOException e) {
                    this.connection.connectionWasLost = true;
                }
            }
            try {
                this.inputStream.close();
            } catch (IOException e2) {
            }
            this.connection.close();
        }

        private void postPacket(Packet packet) {
            if (this.connection.debugPacketWatcher != null) {
                this.connection.debugPacketWatcher.onPacketReceived(this.connection, packet);
            }
            synchronized (this.connection.packetListeners) {
                if (this.connection.packetListeners.containsKey(packet.getType())) {
                    List<PacketReceivedListener> listeners = this.connection.packetListeners.get(packet.getType());
                    for (PacketReceivedListener listener : listeners) {
                        listener.onPacketReceived(this.connection, packet);
                    }
                }
            }
        }

        private PacketContent parsePacketContent(PacketHeader header) throws IOException {
            readAmount(header.getPayloadLength());
            Constructor<?> constructor = null;
            Constructor<?>[] constructors = header.getPacketType().getPacketClass().getConstructors();
            int length = constructors.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                Constructor<?> constructor2 = constructors[i];
                if (constructor2.getParameterTypes().length != 1 || !constructor2.getParameterTypes()[0].equals(ByteBuffer.class)) {
                    i++;
                } else {
                    constructor = constructor2;
                    break;
                }
            }
            if (constructor == null) {
                System.out.println("Failed to find suitable constructor to construct packet contents.");
                return null;
            }
            this.packetBuffer.flip();
            try {
                return (PacketContent) constructor.newInstance(this.packetBuffer);
            } catch (Exception e) {
                System.out.println("Failed to construct packet contents, please refer to stacktrace for more details.");
                e.printStackTrace();
                return null;
            }
        }

        private void prepareBuffer(PacketHeader header) {
            this.packetBuffer.clear();
            if (this.packetBuffer.capacity() < header.getPayloadLength()) {
                this.packetBuffer = ByteBuffer.allocate((int) (header.getPayloadLength() * 1.5f));
                this.packetBuffer.order(ByteOrder.LITTLE_ENDIAN);
            }
        }

        private void finalMagic() throws IOException {
            short magicNumber;
            byte lo = 0;
            do {
                int b = this.inputStream.read();
                if (b < 0) {
                    throw new IOException("Connection closed");
                }
                byte hi = lo;
                lo = (byte) b;
                magicNumber = (short) ((hi & 255) | ((lo << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK));
            } while (magicNumber != 24605);
            this.packetBuffer.putShort(magicNumber);
        }

        private void allocateBuffer(int size) {
            this.packetBuffer = ByteBuffer.allocate(size);
            this.packetBuffer.order(ByteOrder.LITTLE_ENDIAN);
        }

        private void readAmount(int size) throws IOException {
            if (size != 0) {
                int totalRead = 0;
                do {
                    int read = this.inputStream.read(this.packetBuffer.array(), this.packetBuffer.position(), size - totalRead);
                    if (read > 0) {
                        totalRead += read;
                        this.packetBuffer.position(this.packetBuffer.position() + read);
                    } else {
                        return;
                    }
                } while (totalRead != size);
            }
        }
    }

    private static String bytesToString(byte[] data) {
        StringBuilder sb = new StringBuilder("[");
        boolean init = false;
        if (data != null) {
            for (byte b : data) {
                if (init) {
                    sb.append(", ");
                }
                sb.append(HEX_CHARS[(b >> 4) & 15]);
                sb.append(HEX_CHARS[b & 15]);
                init = true;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private static String bytesToString(byte[] data, int count) {
        StringBuilder sb = new StringBuilder("[");
        boolean init = false;
        if (data != null) {
            for (int i = 0; i < count; i++) {
                byte b = data[i];
                if (init) {
                    sb.append(", ");
                }
                sb.append(HEX_CHARS[(b >> 4) & 15]);
                sb.append(HEX_CHARS[b & 15]);
                init = true;
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
