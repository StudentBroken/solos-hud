package com.kopin.pupil;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import com.kopin.accessory.AudioCodec;
import com.kopin.accessory.ImageCodec;
import com.kopin.accessory.base.Connection;
import com.kopin.accessory.base.Packet;
import com.kopin.accessory.base.PacketContent;
import com.kopin.accessory.base.PacketHeader;
import com.kopin.accessory.base.PacketReceivedListener;
import com.kopin.accessory.base.PacketType;
import com.kopin.accessory.packets.AudioEnablePacketContent;
import com.kopin.accessory.packets.AudioPacketContent;
import com.kopin.accessory.packets.ButtonType;
import com.kopin.accessory.packets.CapabilityPacketContent;
import com.kopin.accessory.packets.CapabilityType;
import com.kopin.accessory.packets.DebugPacketContent;
import com.kopin.accessory.packets.ImagePacketContent;
import com.kopin.accessory.packets.StatusPacketContent;
import com.kopin.accessory.packets.StatusType;
import com.kopin.accessory.packets.TimeStampPacketContent;
import com.kopin.accessory.packets.base.ActionPacketContent;
import com.kopin.accessory.packets.base.BytePacketContent;
import com.kopin.accessory.packets.base.IntPacketContent;
import com.kopin.accessory.packets.headers.AudioHeader;
import com.kopin.accessory.packets.headers.ImageHeader;
import com.kopin.accessory.utility.CallHelper;
import com.kopin.accessory.utility.Flash;
import com.kopin.core.Core;
import com.kopin.core.MSBCDecode;
import com.kopin.core.MSBCEncode;
import com.kopin.pupil.PupilDevice;
import com.kopin.pupil.bluetooth.BluetoothConnector;
import com.kopin.pupil.bluetooth.DeviceLister;
import com.kopin.pupil.util.PhoneUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashSet;

/* JADX INFO: loaded from: classes25.dex */
public abstract class PupilConnector extends Thread implements MSBCEncode.IEncodeCallback, MSBCDecode.IDecodeCallback {
    private static final int AUDIO_BUFFER_SIZE = 10000;
    private static final byte BIT_RATE = 16;
    private static final float MS_SAMPLE_SIZE = 32.0f;
    private static final long RECONNECT_INITIAL_PERIOD = 5000;
    private static final long RECONNECT_PERIOD = 60000;
    private static final int SAMPLE_RATE = 16000;
    public static final int SCREEN_SIZE_HEIGHT = 240;
    public static final int SCREEN_SIZE_WIDTH = 428;
    private static String TAG;
    protected boolean isConnected;
    private boolean isDead;
    protected boolean isReconnecting;
    protected long lastConnectedTime;
    private PacketReceivedListener mActionListener;
    private PacketReceivedListener mAudioEndListener;
    private Handler mAudioHandler;
    private PacketReceivedListener mAudioListener;
    private HandlerThread mAudioTransmitThread;
    private PacketReceivedListener mCallResponseListener;
    private PacketReceivedListener mCapsListener;
    protected Connection mConnection;
    protected final DeviceLister.ConnectionListener mConnectionListener;
    private PacketReceivedListener mDebugPacketListener;
    private final MSBCDecode mDecode;
    private boolean mDecodeInternally;
    private Connection.DisconnectedListener mDisconnectedListener;
    private final MSBCEncode mEncode;
    private long mLastBatteryRefresh;
    private int mMSBCSampleRate;
    protected ResponseListener mResponseListener;
    private PacketReceivedListener mStatusListener;
    private final HashSet<Integer> mTTSEndIds;
    private final PacketReceivedListener mVADListener;
    private boolean retryConnect;

    public interface BaseResponseListener {
        void onDeviceInfo(PupilDevice.DeviceInfo deviceInfo);

        void onDeviceStatus(PupilDevice.DeviceStatus deviceStatus);

        void onDisconnected(boolean z);

        void onReconnection();
    }

    public interface MaintenanceResponseListener extends BaseResponseListener {
        void onFlashProgess();

        void onReconnection(boolean z);
    }

    public interface ResponseListener extends BaseResponseListener {
        void onAudioEnd(int i);

        void onAudioReceived(byte[] bArr);

        void onButtonPress(boolean z);
    }

    public interface Solos2ResponseListener extends SolosResponseListener {
        void onSilenceDetected();

        void onTapEvent();

        void onVoiceDetected();
    }

    public interface SolosResponseListener extends ResponseListener {
        void onButtonPress(boolean z, ButtonType buttonType);
    }

    protected abstract void doClose();

    protected abstract Connection doConnect() throws IOException;

    protected abstract boolean doReconnect();

    public PupilConnector(String tag, DeviceLister.ConnectionListener listener) {
        super(tag);
        this.isConnected = false;
        this.isReconnecting = false;
        this.retryConnect = false;
        this.isDead = false;
        this.mDecodeInternally = true;
        this.mTTSEndIds = new HashSet<>();
        this.mMSBCSampleRate = 0;
        this.mLastBatteryRefresh = 0L;
        this.mStatusListener = new PacketReceivedListener() { // from class: com.kopin.pupil.PupilConnector.4
            @Override // com.kopin.accessory.base.PacketReceivedListener
            public void onPacketReceived(Connection connection, Packet packet) {
                PupilDevice.DeviceStatus deviceStatus = PupilDevice.processStatusPacket((StatusPacketContent) packet.getContent());
                if (PupilConnector.this.mResponseListener != null) {
                    PupilConnector.this.mResponseListener.onDeviceStatus(deviceStatus);
                }
            }
        };
        this.mCapsListener = new PacketReceivedListener() { // from class: com.kopin.pupil.PupilConnector.5
            @Override // com.kopin.accessory.base.PacketReceivedListener
            public void onPacketReceived(Connection connection, Packet packet) {
                PupilDevice.processCapabilityPacket((CapabilityPacketContent) packet.getContent());
                if (PupilConnector.this.mResponseListener != null) {
                    PupilConnector.this.mResponseListener.onDeviceInfo(PupilDevice.currentDeviceInfo());
                }
            }
        };
        this.mActionListener = new PacketReceivedListener() { // from class: com.kopin.pupil.PupilConnector.6
            @Override // com.kopin.accessory.base.PacketReceivedListener
            public void onPacketReceived(Connection connection, Packet packet) {
                ActionPacketContent intPacket = (ActionPacketContent) packet.getContent();
                if (PupilConnector.this.mResponseListener == null) {
                    Log.d(PupilConnector.TAG, "NULL mResponseListener: received an Action packet with an int: " + intPacket.getAction().getValue());
                }
                if (PupilConnector.this.mResponseListener instanceof Solos2ResponseListener) {
                    Log.d(PupilConnector.TAG, "Solos2ResponseListener");
                }
                switch (intPacket.getAction()) {
                    case BUTTON_SHORT:
                        if (PupilConnector.this.mResponseListener instanceof SolosResponseListener) {
                            ((SolosResponseListener) PupilConnector.this.mResponseListener).onButtonPress(true, intPacket.getButtonPress().getButton());
                        } else {
                            PupilConnector.this.mResponseListener.onButtonPress(true);
                        }
                        break;
                    case BUTTON_BREAK:
                        if (PupilConnector.this.mResponseListener instanceof SolosResponseListener) {
                            ((SolosResponseListener) PupilConnector.this.mResponseListener).onButtonPress(false, intPacket.getButtonPress().getButton());
                        } else {
                            PupilConnector.this.mResponseListener.onButtonPress(false);
                        }
                        break;
                    case AUDIO_BUFFER_FULL:
                        Log.d(PupilConnector.TAG, "Audio holdoff");
                        PupilConnector.this.mConnection.setAudioFlow(false);
                        break;
                    case AUDIO_BUFFER_EMPTY:
                        Log.d(PupilConnector.TAG, "Audio resume");
                        PupilConnector.this.mConnection.setAudioFlow(true);
                        break;
                    case POWER_CHARGING:
                        Log.d(PupilConnector.TAG, "Device is on charge");
                        PupilDevice.setPowerStatus(true);
                        PupilConnector.this.refreshBatteryStatus();
                        break;
                    case POWER_DISCHARGING:
                        Log.d(PupilConnector.TAG, "Device is off charge");
                        PupilDevice.setPowerStatus(false);
                        PupilConnector.this.refreshBatteryStatus();
                        break;
                    case TAP_EVENT:
                        Log.d(PupilConnector.TAG, "Tap Event");
                        if (PupilConnector.this.mResponseListener instanceof Solos2ResponseListener) {
                            ((Solos2ResponseListener) PupilConnector.this.mResponseListener).onTapEvent();
                        }
                        break;
                    case VAD_VOICE_PRESENT:
                        Log.d(PupilConnector.TAG, "VAD: Voice detected");
                        if (PupilConnector.this.mResponseListener instanceof Solos2ResponseListener) {
                            ((Solos2ResponseListener) PupilConnector.this.mResponseListener).onVoiceDetected();
                        }
                        break;
                    case VAD_SILENCE_DETECT:
                        Log.d(PupilConnector.TAG, "VAD: Silence detected");
                        if (PupilConnector.this.mResponseListener instanceof Solos2ResponseListener) {
                            ((Solos2ResponseListener) PupilConnector.this.mResponseListener).onSilenceDetected();
                        }
                        break;
                }
            }
        };
        this.mAudioEndListener = new PacketReceivedListener() { // from class: com.kopin.pupil.PupilConnector.7
            @Override // com.kopin.accessory.base.PacketReceivedListener
            public void onPacketReceived(Connection connection, Packet packet) {
                IntPacketContent intPacketContent = (IntPacketContent) packet.getContent();
                if (PupilConnector.this.mResponseListener != null) {
                    PupilConnector.this.mResponseListener.onAudioEnd(intPacketContent.getValue());
                }
            }
        };
        this.mCallResponseListener = new PacketReceivedListener() { // from class: com.kopin.pupil.PupilConnector.8
            @Override // com.kopin.accessory.base.PacketReceivedListener
            public void onPacketReceived(Connection connection, Packet packet) {
                switch (packet.getType()) {
                    case CALL_STATE_RESPONSE:
                        Packet requestExtra = PhoneUtils.onCallStateReceived(packet);
                        if (requestExtra != null) {
                            connection.enqueuePacket(requestExtra);
                        }
                        break;
                    case CALL_NUMBER_RESPONSE:
                        PhoneUtils.onCallNumberReceived(packet);
                        break;
                }
            }
        };
        this.mDebugPacketListener = new PacketReceivedListener() { // from class: com.kopin.pupil.PupilConnector.9
            @Override // com.kopin.accessory.base.PacketReceivedListener
            public void onPacketReceived(Connection connection, Packet packet) {
                ((IntPacketContent) packet.getContent()).getValue();
            }
        };
        this.mDisconnectedListener = new Connection.DisconnectedListener() { // from class: com.kopin.pupil.PupilConnector.10
            @Override // com.kopin.accessory.base.Connection.DisconnectedListener
            public void onDisconnected(Connection connection, boolean wasLoss) {
                if (wasLoss) {
                    PupilConnector.this.attemptReconnect();
                }
                PupilConnector.this.isConnected = false;
                if (PupilConnector.this.mResponseListener != null) {
                    PupilConnector.this.mResponseListener.onDisconnected(wasLoss);
                }
                PupilMaintenance.onDisconnect(wasLoss);
                PupilDevice.setPowerStatus(false);
            }
        };
        this.mAudioListener = new PacketReceivedListener() { // from class: com.kopin.pupil.PupilConnector.11
            @Override // com.kopin.accessory.base.PacketReceivedListener
            public void onPacketReceived(Connection connection, Packet packet) {
                AudioPacketContent audioPacket = (AudioPacketContent) packet.getContent();
                AudioHeader hdr = audioPacket.getHeader();
                byte[] data = audioPacket.getData();
                switch (AnonymousClass13.$SwitchMap$com$kopin$accessory$AudioCodec[hdr.getAudioCodec().ordinal()]) {
                    case 2:
                        if (PupilConnector.this.mDecodeInternally) {
                            PupilConnector.this.mDecode.decode(data, 0, data.length);
                            return;
                        }
                        break;
                }
                if (PupilConnector.this.mResponseListener != null) {
                    PupilConnector.this.mResponseListener.onAudioReceived(data);
                }
            }
        };
        this.mVADListener = new PacketReceivedListener() { // from class: com.kopin.pupil.PupilConnector.12
            @Override // com.kopin.accessory.base.PacketReceivedListener
            public void onPacketReceived(Connection connection, Packet packet) {
                Log.d(PupilConnector.TAG, "VAD is " + (((BytePacketContent) packet.getContent()).getValue() == 0 ? "OFF" : "ON"));
            }
        };
        TAG = tag;
        this.mEncode = new MSBCEncode(this);
        this.mDecode = new MSBCDecode(this);
        this.mConnectionListener = listener;
        startAudioThread();
    }

    void setPacketWatcher(Connection.PacketWatcher packetWatcher) {
        if (this.mConnection != null) {
            this.mConnection.setPacketWatcher(packetWatcher);
        }
    }

    private void startAudioThread() {
        this.mAudioTransmitThread = new HandlerThread("audio transmit thread");
        this.mAudioTransmitThread.start();
        this.mAudioHandler = new Handler(this.mAudioTransmitThread.getLooper());
    }

    private void stopAudioThread() {
        this.mEncode.cleanUp();
        this.mAudioTransmitThread.quit();
    }

    private void runOnAudioThread(Runnable action) {
        if (!this.mAudioTransmitThread.isAlive()) {
            startAudioThread();
        }
        if (this.mAudioTransmitThread.isAlive()) {
            this.mAudioHandler.post(action);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void attemptReconnect() {
        this.lastConnectedTime = System.currentTimeMillis();
        synchronized (this.mConnection) {
            this.mConnection.notify();
        }
    }

    public void setRetry(boolean retry) {
        this.retryConnect = retry;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        boolean connectOk;
        try {
            this.mConnection = new Connection(null, null);
            this.mConnection.registerForPacket(PacketType.ACTION, this.mActionListener);
            this.mConnection.registerForPacket(PacketType.STATUS_RESPONSE, this.mStatusListener);
            this.mConnection.registerForPacket(PacketType.CAPABILITIES_RESPONSE, this.mCapsListener);
            this.mConnection.registerForPacket(PacketType.AUDIO_END, this.mAudioEndListener);
            this.mConnection.registerForPacket(PacketType.CALL_STATE_RESPONSE, this.mCallResponseListener);
            this.mConnection.registerForPacket(PacketType.CALL_NUMBER_RESPONSE, this.mCallResponseListener);
            this.mConnection.registerForPacket(PacketType.DEBUG_GARMIN_REMOTE, this.mDebugPacketListener);
            this.mConnection.registerForPacket(PacketType.AUDIO, this.mAudioListener);
            this.mConnection.registerForPacket(PacketType.VAD_RESPONSE, this.mVADListener);
            this.mConnection.registerForDisconnect(this.mDisconnectedListener);
            PupilMaintenance.init(this);
            while (!this.isDead) {
                do {
                    long now = System.currentTimeMillis();
                    try {
                        if (this.lastConnectedTime != 0) {
                            Thread.sleep(now - this.lastConnectedTime > 60000 ? 60000L : 5000L);
                        } else {
                            this.lastConnectedTime = now;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if ((this instanceof BluetoothConnector) && PupilDevice.hasConnector() && !PupilDevice.isPrimaryConnector((BluetoothConnector) this)) {
                        throw new IOException("Killing zombie connector thread");
                    }
                    connectOk = doReconnect();
                    if (connectOk) {
                        TimeStampPacketContent requestTime = new TimeStampPacketContent(System.currentTimeMillis() / 1000);
                        this.mConnection.enqueuePacket(new Packet(PacketType.TIME_SET, requestTime));
                        getDefaultCaps();
                        getStatus();
                        this.mConnection.enqueuePacket(Flash.createBootBankQuery());
                    }
                    if (this.isDead || connectOk) {
                        break;
                    }
                } while (this.retryConnect);
                if (!connectOk) {
                    throw new IOException("Connect failed, and retry policy is off");
                }
                if (!this.isConnected) {
                    this.mConnectionListener.onConnection(this);
                    getDefaultCaps();
                    this.isConnected = true;
                    this.retryConnect = true;
                }
                synchronized (this.mConnection) {
                    try {
                        this.mConnection.wait();
                        this.isReconnecting = true;
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        } catch (IOException connectException) {
            Log.e(TAG, connectException.getMessage());
            this.mConnectionListener.onConnectionFailed();
            stopAudioThread();
        } finally {
            doClose();
            this.isConnected = false;
        }
    }

    public void setResponseListener(ResponseListener listener) {
        this.mResponseListener = listener;
    }

    void listenForPackets(PacketType type, PacketReceivedListener cb) {
        this.mConnection.registerForPacket(type, cb);
    }

    void unlistenForPackets(PacketType type, PacketReceivedListener cb) {
        this.mConnection.unregisterForPacket(type, cb);
    }

    public void kill() {
        this.isDead = true;
        synchronized (this.mConnection) {
            this.mConnection.notify();
        }
        stopAudioThread();
    }

    public void sendWake() {
        IntPacketContent content = new IntPacketContent(0);
        this.mConnection.enqueuePacket(new Packet(PacketType.WAKE_UP, content));
    }

    public void sendSleep() {
        IntPacketContent content = new IntPacketContent(0);
        this.mConnection.enqueuePacket(new Packet(PacketType.SLEEP, content));
    }

    public void sendDeviceName(String name) {
        CapabilityPacketContent nameContent = new CapabilityPacketContent();
        nameContent.putString(CapabilityType.DEVICE_NAME, name);
        PacketHeader header = new PacketHeader(PacketType.CAPABILITIES_SET, nameContent.getSize());
        this.mConnection.enqueuePacket(new Packet(header, nameContent));
        getCaps(CapabilityType.DEVICE_NAME);
    }

    public void sendEnableHFP(boolean onOrOff) {
        CapabilityPacketContent capsContent = new CapabilityPacketContent();
        CapabilityType capabilityType = CapabilityType.ENABLE_HFP_CONNECTION;
        byte[] bArr = new byte[1];
        bArr[0] = (byte) (onOrOff ? 1 : 0);
        capsContent.putBytes(capabilityType, bArr);
        PacketHeader header = new PacketHeader(PacketType.CAPABILITIES_SET, capsContent.getSize());
        this.mConnection.enqueuePacket(new Packet(header, capsContent));
        getCaps(CapabilityType.ENABLE_HFP_CONNECTION);
    }

    public void sendDisplayPowerMode(boolean lowPower) {
    }

    public void sendBitmapRLE565(Bitmap bitmap) {
        sendBitmapRLE565(bitmap, 0, 0);
    }

    public void sendBitmapRLE565(Bitmap bitmap, int x, int y) {
        Bitmap bitmap2 = scaleBitmap(bitmap, 428 - x, 240 - y);
        Bitmap rgb565 = bitmap2.copy(Bitmap.Config.RGB_565, false);
        byte[] result = new byte[bitmap2.getWidth() * bitmap2.getHeight() * 3];
        int length = Core.convertToRLE(rgb565, 0, 0, rgb565.getWidth(), rgb565.getHeight(), result, false);
        ImageHeader imageHeader = new ImageHeader(ImageCodec.RLE565, (byte) 0, (short) 0, (short) 1, (short) x, (short) y, (short) rgb565.getWidth(), (short) rgb565.getHeight());
        ImagePacketContent imagePacket = new ImagePacketContent(imageHeader, result, length);
        Packet packet = new Packet(PacketType.IMAGE, imagePacket);
        this.mConnection.enqueuePacket(packet);
    }

    public void sendBitmapRGB565(Bitmap rgb565) {
        sendBitmapRGB565(rgb565, 0, 0);
    }

    public void sendBitmapRGB565(Bitmap rgb565, int x, int y) {
        Bitmap rgb5652 = scaleBitmap(rgb565, 428 - x, 240 - y);
        ByteBuffer byteBuffer = ByteBuffer.allocate(rgb5652.getByteCount());
        rgb5652.copyPixelsToBuffer(byteBuffer);
        ImageHeader imageHeader = new ImageHeader(ImageCodec.RGB565, (byte) 0, (short) 0, (short) 1, (short) x, (short) y, (short) rgb5652.getWidth(), (short) rgb5652.getHeight());
        ImagePacketContent imagePacket = new ImagePacketContent(imageHeader, byteBuffer.array(), byteBuffer.limit());
        Packet packet = new Packet(PacketType.IMAGE, imagePacket);
        this.mConnection.enqueuePacket(packet);
    }

    public void sendRLE565(short x, short y, short width, short height, byte[] data, int length) {
        ImageHeader imageHeader = new ImageHeader(ImageCodec.RLE565, (byte) 0, (short) 0, (short) 1, x, y, width, height);
        ImagePacketContent imagePacket = new ImagePacketContent(imageHeader, data, length);
        Packet packet = new Packet(PacketType.IMAGE, imagePacket);
        this.mConnection.enqueuePacket(packet);
    }

    public void sendRGB111(short x, short y, short width, short height, byte[] data, int length) {
        ImageHeader imageHeader = new ImageHeader(ImageCodec.COLOR8, (byte) 0, (short) 0, (short) 1, x, y, width, height);
        ImagePacketContent imagePacket = new ImagePacketContent(imageHeader, data, length);
        Packet packet = new Packet(PacketType.IMAGE, imagePacket);
        this.mConnection.enqueuePacket(packet);
    }

    public synchronized void sendView(View view) {
        view.measure(428, 240);
        view.layout(0, 0, 428, 240);
        view.forceLayout();
        view.invalidate();
        Bitmap bitmap = Bitmap.createBitmap(428, 240, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        sendBitmapJPEG(bitmap);
    }

    public void sendBitmapJPEG(Bitmap bitmap) {
        Bitmap bitmap2 = scaleBitmap(bitmap);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        ImageHeader imageHeader = new ImageHeader(ImageCodec.JPEG, (byte) 0, (short) 0, (short) 1, (short) 0, (short) 0, (short) bitmap2.getWidth(), (short) bitmap2.getHeight());
        ImagePacketContent imagePacket = new ImagePacketContent(imageHeader, stream.toByteArray(), stream.size());
        Packet packet = new Packet(PacketType.IMAGE, imagePacket);
        this.mConnection.enqueuePacket(packet);
    }

    private Bitmap scaleBitmap(Bitmap bitmap) {
        return scaleBitmap(bitmap, 428, 240);
    }

    private Bitmap scaleBitmap(Bitmap bitmap, int width, int height) {
        if (width < bitmap.getWidth() || height < bitmap.getHeight()) {
            return Bitmap.createScaledBitmap(bitmap, width, height, false);
        }
        return bitmap;
    }

    @Override // com.kopin.core.MSBCEncode.IEncodeCallback
    public void onEncoded(byte[] data, int length) {
        sendAudioMSBC((byte[]) data.clone(), length, this.mMSBCSampleRate);
    }

    public void sendAudio(byte[] data, int length, AudioCodec codec, int sampleRate) {
        switch (codec) {
            case PCM:
                this.mMSBCSampleRate = sampleRate;
                this.mEncode.encode(data, 0, length);
                break;
            case MSBC:
                sendAudioMSBC(data, length, sampleRate);
                break;
        }
    }

    private void sendAudioPCM(final byte[] data, final int length, final int sampleRate) {
        runOnAudioThread(new Runnable() { // from class: com.kopin.pupil.PupilConnector.1
            @Override // java.lang.Runnable
            public void run() {
                byte[] buffer = new byte[10000];
                ByteArrayInputStream inputStream = new ByteArrayInputStream(data, 0, length);
                while (true) {
                    try {
                        int bytesWritten = inputStream.read(buffer);
                        if (bytesWritten > 0) {
                            AudioHeader header = new AudioHeader(AudioCodec.PCM, (byte) 0, (byte) 1, (byte) 16, sampleRate);
                            AudioPacketContent audioPacket = new AudioPacketContent(header, buffer, bytesWritten);
                            Packet packet = new Packet(PacketType.AUDIO, audioPacket);
                            PupilConnector.this.mConnection.enqueuePacket(packet);
                            try {
                                Thread.sleep((long) ((bytesWritten / PupilConnector.MS_SAMPLE_SIZE) * 0.75f));
                            } catch (InterruptedException e) {
                            }
                        } else {
                            inputStream.close();
                            return;
                        }
                    } catch (IOException e2) {
                        return;
                    }
                }
            }
        });
    }

    private void sendAudioMSBC(final byte[] data, final int length, final int sampleRate) {
        runOnAudioThread(new Runnable() { // from class: com.kopin.pupil.PupilConnector.2
            @Override // java.lang.Runnable
            public void run() {
                AudioHeader header = new AudioHeader(AudioCodec.MSBC, (byte) 0, (byte) 1, (byte) 16, sampleRate);
                AudioPacketContent audioPacket = new AudioPacketContent(header, data, length);
                Packet packet = new Packet(PacketType.AUDIO, audioPacket);
                PupilConnector.this.mConnection.enqueuePacket(packet);
                long sleepTime = (long) (length / 9.0f);
                if (sleepTime > 0) {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                    }
                }
            }
        });
    }

    public void sendAudioEnd(final int id) {
        synchronized (this.mTTSEndIds) {
            this.mTTSEndIds.add(Integer.valueOf(id));
        }
        runOnAudioThread(new Runnable() { // from class: com.kopin.pupil.PupilConnector.3
            @Override // java.lang.Runnable
            public void run() {
                synchronized (PupilConnector.this.mTTSEndIds) {
                    if (PupilConnector.this.mTTSEndIds.contains(Integer.valueOf(id))) {
                        PupilConnector.this.mTTSEndIds.remove(Integer.valueOf(id));
                        PupilConnector.this.sendAudioEndIntern(id);
                    }
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendAudioEndIntern(int id) {
        IntPacketContent intPacketContent = new IntPacketContent(id);
        Packet packet = new Packet(PacketType.AUDIO_END, intPacketContent);
        this.mConnection.enqueuePacket(packet);
    }

    public void stopAudioSending() {
        ArrayList<Integer> temp;
        this.mAudioHandler.removeCallbacksAndMessages(null);
        synchronized (this.mTTSEndIds) {
            temp = new ArrayList<>(this.mTTSEndIds);
            this.mTTSEndIds.clear();
        }
        for (Integer integer : temp) {
            sendAudioEndIntern(integer.intValue());
        }
    }

    public boolean isConnected() {
        return this.isConnected;
    }

    public boolean isReconnecting() {
        return this.isReconnecting;
    }

    public void getStatus() {
        getStatus(StatusType.BATTERY_LEVEL, StatusType.DISPLAY_BRIGHTNESS, StatusType.SPEAKER_VOLUME, StatusType.AMBIENT_LIGHT, StatusType.POWER_AVAILABLE);
    }

    public void getStatus(StatusType... statusTypes) {
        int flag = 0;
        for (StatusType statusType : statusTypes) {
            flag |= statusType.getFlag();
        }
        IntPacketContent getStatusPacket = new IntPacketContent(flag);
        this.mConnection.enqueuePacket(new Packet(PacketType.STATUS_GET, getStatusPacket));
    }

    public void setStatus(PupilDevice.DeviceStatus deviceStatus) {
        StatusPacketContent setStatusPacket = new StatusPacketContent();
        if (deviceStatus.hasBrightness()) {
            setStatusPacket.putBytes(StatusType.DISPLAY_BRIGHTNESS, deviceStatus.getBrightness());
        }
        if (deviceStatus.hasVolume()) {
            setStatusPacket.putBytes(StatusType.SPEAKER_VOLUME, deviceStatus.getVolume());
        }
        if (deviceStatus.hasLeftMic()) {
            setStatusPacket.putBytes(StatusType.LEFT_MICROPHONE_LEVEL, deviceStatus.getLeftMic());
        }
        if (deviceStatus.hasRightMic()) {
            setStatusPacket.putBytes(StatusType.RIGHT_MICROPHONE_LEVEL, deviceStatus.getRightMic());
        }
        this.mConnection.enqueuePacket(new Packet(PacketType.STATUS_SET, setStatusPacket));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshBatteryStatus() {
        long now = System.currentTimeMillis();
        if (now - this.mLastBatteryRefresh > 2000) {
            getStatus(StatusType.BATTERY_LEVEL, StatusType.POWER_AVAILABLE, StatusType.AMBIENT_LIGHT);
            this.mLastBatteryRefresh = now;
        }
    }

    public void sendHFPQuery() {
        this.mConnection.enqueuePacket(CallHelper.requestState());
    }

    public void sendDialNumberPacket(String numberToDial) {
        this.mConnection.enqueuePacket(CallHelper.dialNumber(numberToDial));
        PhoneUtils.onDiallingNumber(numberToDial);
    }

    public void sendAnswerCallPacket() {
        this.mConnection.enqueuePacket(CallHelper.answerCall());
    }

    public void sendEndCallPacket() {
        this.mConnection.enqueuePacket(CallHelper.endCall());
    }

    public void sendAction(int actionId) {
        this.mConnection.enqueuePacket(new Packet(PacketType.ACTION, new IntPacketContent(actionId)));
    }

    public void sendDebugTTS(String tts) {
        this.mConnection.enqueuePacket(DebugPacketContent.newCommandPacket(tts));
    }

    public void getDefaultCaps() {
        getCaps(CapabilityType.PROTOCOL_VERSION, CapabilityType.FIRMWARE_VERSION, CapabilityType.DEVICE_NAME, CapabilityType.MANUFACTURER, CapabilityType.MODEL, CapabilityType.ENABLE_HFP_CONNECTION, CapabilityType.BUTTONS, CapabilityType.ANT_MODULE, CapabilityType.ANT_MODULE_VERSION, CapabilityType.HEADSET_SERIAL_NUMBER);
    }

    public void getCaps(CapabilityType... capTypes) {
        int flag = 0;
        for (CapabilityType cap : capTypes) {
            flag |= cap.getFlag();
        }
        IntPacketContent getCapsPacket = new IntPacketContent(flag);
        this.mConnection.enqueuePacket(new Packet(PacketType.CAPABILITIES_GET, getCapsPacket));
    }

    @Override // com.kopin.core.MSBCDecode.IDecodeCallback
    public void onDecoded(byte[] data, int length) {
        if (this.mResponseListener != null) {
            this.mResponseListener.onAudioReceived(data);
        }
    }

    public void enableAudio() {
        enableAudio(1, AudioCodec.PCM, 16, SAMPLE_RATE);
    }

    public void enableAudio(int channels) {
        enableAudio(channels, AudioCodec.PCM, 16, SAMPLE_RATE);
    }

    public void enableAudio(int channels, AudioCodec codec) {
        enableAudio(channels, codec, 16, SAMPLE_RATE);
    }

    private void enableAudio(int channels, AudioCodec codec, int bitRate, int sampleRate) {
        AudioHeader audioHeader = new AudioHeader(codec, (byte) 0, (byte) channels, (byte) bitRate, sampleRate);
        AudioEnablePacketContent audioEnablePacket = new AudioEnablePacketContent(audioHeader, new byte[0]);
        Packet packet = new Packet(new PacketHeader(PacketType.AUDIO_ENABLE, audioEnablePacket.getSize()), audioEnablePacket);
        this.mConnection.enqueuePacket(packet);
    }

    public void disableAudio() {
        Packet packet = new Packet(new PacketHeader(PacketType.AUDIO_DISABLE, 0), PacketContent.noContent());
        this.mConnection.enqueuePacket(packet);
    }

    void setEnableDecoder(boolean onOrOff) {
        this.mDecodeInternally = onOrOff;
    }

    void enableVAD(boolean onOrOff) {
        BytePacketContent requestVAD = new BytePacketContent((byte) (onOrOff ? 1 : 0));
        this.mConnection.enqueuePacket(new Packet(PacketType.VAD_REQUEST, requestVAD));
    }
}
