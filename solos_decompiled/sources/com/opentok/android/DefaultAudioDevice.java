package com.opentok.android;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.NoiseSuppressor;
import android.os.Build;
import android.os.Process;
import com.opentok.android.BaseAudioDevice;
import com.opentok.android.OtLog;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/* JADX INFO: loaded from: classes15.dex */
class DefaultAudioDevice extends BaseAudioDevice {
    private static final int DEFAULT_BUFFER_SIZE = 1760;
    private static final int DEFAULT_SAMPLES_PER_BUFFER = 440;
    private static final int DEFAULT_SAMPLE_RATE = 44100;
    private static final String HEADSET_PLUG_STATE_KEY = "state";
    private static final int NUM_CHANNELS_CAPTURING = 1;
    private static final int NUM_CHANNELS_RENDERING = 1;
    private static final int SAMPLE_SIZE_IN_BYTES = 2;
    private static final int STEREO_CHANNELS = 2;
    private static final OtLog.LogToken log = new OtLog.LogToken();
    private AudioManager audioManager;
    private AudioRecord audioRecord;
    private AudioTrack audioTrack;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothProfile bluetoothProfile;
    private BluetoothState bluetoothState;
    private BaseAudioDevice.AudioSettings captureSettings;
    private Context context;
    private AcousticEchoCanceler echoCanceler;
    private NoiseSuppressor noiseSuppressor;
    private int outputSamplingRate;
    private ByteBuffer playBuffer;
    private ByteBuffer recBuffer;
    private boolean receiverRegistered;
    private BaseAudioDevice.AudioSettings rendererSettings;
    private int samplesPerBuffer;
    private boolean scoReceiverRegistered;
    private byte[] tempBufPlay;
    private byte[] tempBufRec;
    private final ReentrantLock rendererLock = new ReentrantLock(true);
    private final Condition renderEvent = this.rendererLock.newCondition();
    private volatile boolean isRendering = false;
    private volatile boolean shutdownRenderThread = false;
    private final ReentrantLock captureLock = new ReentrantLock(true);
    private final Condition captureEvent = this.captureLock.newCondition();
    private volatile boolean isCapturing = false;
    private volatile boolean shutdownCaptureThread = false;
    private OutputType audioOutput = OutputType.PHONE_SPEAKERS;
    private int estimatedCaptureDelay = 0;
    private int bufferedPlaySamples = 0;
    private int playPosition = 0;
    private int estimatedRenderDelay = 0;
    private AudioManagerMode audioManagerMode = new AudioManagerMode();
    private int captureSamplingRate = DEFAULT_SAMPLE_RATE;
    private Object bluetoothLock = new Object();
    private final BroadcastReceiver btStatusReceiver = new BroadcastReceiver() { // from class: com.opentok.android.DefaultAudioDevice.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && action.equals("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED")) {
                int state = intent.getIntExtra("android.bluetooth.profile.extra.STATE", -1);
                switch (state) {
                    case 0:
                        DefaultAudioDevice.log.d("BroadcastReceiver: STATE_DISCONNECTED", new Object[0]);
                        synchronized (DefaultAudioDevice.this.bluetoothLock) {
                            if (BluetoothState.CONNECTED == DefaultAudioDevice.this.bluetoothState) {
                                DefaultAudioDevice.log.d("Bluetooth Headset: Disconnecting SCO", new Object[0]);
                                DefaultAudioDevice.this.bluetoothState = BluetoothState.DISCONNECTED;
                                DefaultAudioDevice.this.audioManager.setBluetoothScoOn(false);
                                DefaultAudioDevice.this.stopBluetoothSco();
                                if (DefaultAudioDevice.this.audioManager.isWiredHeadsetOn()) {
                                    DefaultAudioDevice.this.setOutputType(OutputType.HEADPHONES);
                                    DefaultAudioDevice.this.audioManager.setSpeakerphoneOn(false);
                                } else {
                                    DefaultAudioDevice.this.setOutputType(OutputType.PHONE_SPEAKERS);
                                    DefaultAudioDevice.this.audioManager.setSpeakerphoneOn(DefaultAudioDevice.this.getOutputMode() == BaseAudioDevice.OutputMode.SpeakerPhone);
                                }
                            }
                            break;
                        }
                        return;
                    case 1:
                    default:
                        return;
                    case 2:
                        DefaultAudioDevice.log.d("BroadcastReceiver: STATE_CONNECTED", new Object[0]);
                        synchronized (DefaultAudioDevice.this.bluetoothLock) {
                            if (BluetoothState.DISCONNECTED == DefaultAudioDevice.this.bluetoothState) {
                                DefaultAudioDevice.log.d("Bluetooth Headset: Connecting SCO", new Object[0]);
                                DefaultAudioDevice.this.bluetoothState = BluetoothState.CONNECTED;
                                DefaultAudioDevice.this.setOutputType(OutputType.BLUETOOTH);
                                DefaultAudioDevice.this.audioManager.setMode(3);
                                DefaultAudioDevice.this.audioManager.setBluetoothScoOn(true);
                                DefaultAudioDevice.this.startBluetoothSco();
                                DefaultAudioDevice.this.audioManager.setSpeakerphoneOn(false);
                            }
                            break;
                        }
                        return;
                }
            }
        }
    };
    private final BluetoothProfile.ServiceListener bluetoothProfileListener = new BluetoothProfile.ServiceListener() { // from class: com.opentok.android.DefaultAudioDevice.2
        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceConnected(int type, BluetoothProfile profile) {
            if (1 == type) {
                DefaultAudioDevice.this.bluetoothProfile = profile;
                List<BluetoothDevice> devices = profile.getConnectedDevices();
                DefaultAudioDevice.log.d("Service Proxy Connected", new Object[0]);
                if (!devices.isEmpty() && 2 == profile.getConnectionState(devices.get(0))) {
                    Intent intent = new Intent("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED");
                    intent.putExtra("android.bluetooth.profile.extra.STATE", 2);
                    DefaultAudioDevice.this.btStatusReceiver.onReceive(DefaultAudioDevice.this.context, intent);
                }
            }
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceDisconnected(int type) {
            DefaultAudioDevice.log.d("Service Proxy Disconnected", new Object[0]);
        }
    };
    Runnable captureThread = new Runnable() { // from class: com.opentok.android.DefaultAudioDevice.3
        @Override // java.lang.Runnable
        public void run() {
            int samplesToRec = DefaultAudioDevice.this.captureSamplingRate / 100;
            try {
                Process.setThreadPriority(-19);
            } catch (Exception e) {
                e.printStackTrace();
            }
            while (!DefaultAudioDevice.this.shutdownCaptureThread) {
                DefaultAudioDevice.this.captureLock.lock();
                try {
                    if (!DefaultAudioDevice.this.isCapturing) {
                        DefaultAudioDevice.this.captureEvent.await();
                        DefaultAudioDevice.this.captureLock.unlock();
                    } else if (DefaultAudioDevice.this.audioRecord != null) {
                        int lengthInBytes = (samplesToRec << 1) * 1;
                        int readBytes = DefaultAudioDevice.this.audioRecord.read(DefaultAudioDevice.this.tempBufRec, 0, lengthInBytes);
                        if (readBytes < 0) {
                            switch (readBytes) {
                                case -3:
                                    throw new RuntimeException("Audio Capture Error: Invalid Operation (-3)");
                                case -2:
                                    throw new RuntimeException("Audio Capture Error: Bad Value (-2)");
                                default:
                                    throw new RuntimeException("Audio Capture Error(-1)");
                            }
                        } else {
                            DefaultAudioDevice.this.recBuffer.rewind();
                            DefaultAudioDevice.this.recBuffer.put(DefaultAudioDevice.this.tempBufRec);
                            int samplesRead = (readBytes >> 1) / 1;
                            DefaultAudioDevice.this.captureLock.unlock();
                            DefaultAudioDevice.this.getAudioBus().writeCaptureData(DefaultAudioDevice.this.recBuffer, samplesRead);
                            DefaultAudioDevice.this.estimatedCaptureDelay = (samplesRead * 1000) / DefaultAudioDevice.this.captureSamplingRate;
                        }
                    }
                } catch (Exception e2) {
                    BaseAudioDevice.publisherError(e2);
                    return;
                } finally {
                    DefaultAudioDevice.this.captureLock.unlock();
                }
            }
        }
    };
    Runnable renderThread = new Runnable() { // from class: com.opentok.android.DefaultAudioDevice.4
        @Override // java.lang.Runnable
        public void run() {
            int samplesToPlay = DefaultAudioDevice.this.samplesPerBuffer;
            try {
                Process.setThreadPriority(-19);
            } catch (Exception e) {
                e.printStackTrace();
            }
            while (!DefaultAudioDevice.this.shutdownRenderThread) {
                DefaultAudioDevice.this.rendererLock.lock();
                try {
                    if (DefaultAudioDevice.this.isRendering) {
                        DefaultAudioDevice.this.rendererLock.unlock();
                        DefaultAudioDevice.this.playBuffer.clear();
                        int samplesRead = DefaultAudioDevice.this.getAudioBus().readRenderData(DefaultAudioDevice.this.playBuffer, samplesToPlay);
                        DefaultAudioDevice.this.rendererLock.lock();
                        if (DefaultAudioDevice.this.audioTrack != null && DefaultAudioDevice.this.isRendering) {
                            int bytesRead = (samplesRead << 1) * 1;
                            DefaultAudioDevice.this.playBuffer.get(DefaultAudioDevice.this.tempBufPlay, 0, bytesRead);
                            int bytesWritten = DefaultAudioDevice.this.audioTrack.write(DefaultAudioDevice.this.tempBufPlay, 0, bytesRead);
                            if (bytesWritten <= 0) {
                                switch (bytesWritten) {
                                    case -3:
                                        throw new RuntimeException("Audio Renderer Error: Invalid Operation (-3)");
                                    case -2:
                                        throw new RuntimeException("Audio Renderer Error: Bad Value (-2)");
                                    default:
                                        throw new RuntimeException("Audio Renderer Error(-1)");
                                }
                            }
                            DefaultAudioDevice.this.bufferedPlaySamples += (bytesWritten >> 1) / 1;
                            int pos = DefaultAudioDevice.this.audioTrack.getPlaybackHeadPosition();
                            if (pos < DefaultAudioDevice.this.playPosition) {
                                DefaultAudioDevice.this.playPosition = 0;
                            }
                            DefaultAudioDevice.this.bufferedPlaySamples -= pos - DefaultAudioDevice.this.playPosition;
                            DefaultAudioDevice.this.playPosition = pos;
                            DefaultAudioDevice.this.estimatedRenderDelay = (DefaultAudioDevice.this.bufferedPlaySamples * 1000) / DefaultAudioDevice.this.outputSamplingRate;
                            DefaultAudioDevice.this.rendererLock.unlock();
                        }
                    } else {
                        DefaultAudioDevice.this.renderEvent.await();
                        DefaultAudioDevice.this.rendererLock.unlock();
                    }
                } catch (Exception e2) {
                    BaseAudioDevice.publisherError(e2);
                    return;
                } finally {
                    DefaultAudioDevice.this.rendererLock.unlock();
                }
            }
        }
    };
    private BroadcastReceiver headsetReceiver = new BroadcastReceiver() { // from class: com.opentok.android.DefaultAudioDevice.5
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.HEADSET_PLUG")) {
                if (intent.getIntExtra("state", 0) == 1) {
                    DefaultAudioDevice.log.d("Headphones connected", new Object[0]);
                    DefaultAudioDevice.this.setOutputType(OutputType.HEADPHONES);
                    DefaultAudioDevice.this.audioManager.setSpeakerphoneOn(false);
                    DefaultAudioDevice.this.audioManager.setBluetoothScoOn(false);
                    return;
                }
                DefaultAudioDevice.log.d("Headphones disconnected", new Object[0]);
                if (BluetoothState.CONNECTED == DefaultAudioDevice.this.bluetoothState) {
                    DefaultAudioDevice.this.audioManager.setBluetoothScoOn(true);
                    DefaultAudioDevice.this.setOutputType(OutputType.BLUETOOTH);
                } else {
                    DefaultAudioDevice.this.audioManager.setSpeakerphoneOn(DefaultAudioDevice.this.getOutputMode() == BaseAudioDevice.OutputMode.SpeakerPhone);
                    DefaultAudioDevice.this.setOutputType(OutputType.PHONE_SPEAKERS);
                }
            }
        }
    };

    private enum BluetoothState {
        DISCONNECTED,
        CONNECTED
    }

    private enum OutputType {
        PHONE_SPEAKERS,
        HEADPHONES,
        BLUETOOTH
    }

    private static class AudioManagerMode {
        private int oldMode = 0;
        private int naquire = 0;

        public void acquireMode(AudioManager audioManager) {
            int i = this.naquire;
            this.naquire = i + 1;
            if (i == 0) {
                this.oldMode = audioManager.getMode();
                audioManager.setMode(3);
            }
        }

        public void releaseMode(AudioManager audioManager) {
            int i = this.naquire - 1;
            this.naquire = i;
            if (i == 0) {
                audioManager.setMode(this.oldMode);
            }
        }
    }

    /* JADX WARN: Finally extract failed */
    public DefaultAudioDevice(Context context) {
        this.outputSamplingRate = DEFAULT_SAMPLE_RATE;
        this.samplesPerBuffer = DEFAULT_SAMPLES_PER_BUFFER;
        this.context = context;
        try {
            this.recBuffer = ByteBuffer.allocateDirect(DEFAULT_BUFFER_SIZE);
        } catch (Exception e) {
            log.e(e.getMessage(), new Object[0]);
        }
        this.tempBufRec = new byte[DEFAULT_BUFFER_SIZE];
        this.audioManager = (AudioManager) context.getSystemService("audio");
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.bluetoothProfile = null;
        int outputBufferSize = DEFAULT_BUFFER_SIZE;
        if (Build.VERSION.SDK_INT > 16) {
            try {
                this.outputSamplingRate = Integer.parseInt(this.audioManager.getProperty("android.media.property.OUTPUT_SAMPLE_RATE"));
                if (this.outputSamplingRate == 0) {
                    this.outputSamplingRate = DEFAULT_SAMPLE_RATE;
                }
                try {
                    this.samplesPerBuffer = Integer.parseInt(this.audioManager.getProperty("android.media.property.OUTPUT_FRAMES_PER_BUFFER"));
                    outputBufferSize = this.samplesPerBuffer * 2 * 1;
                } finally {
                    if (DEFAULT_BUFFER_SIZE == 0) {
                        this.samplesPerBuffer = DEFAULT_SAMPLES_PER_BUFFER;
                    }
                }
            } catch (Throwable th) {
                if (this.outputSamplingRate == 0) {
                    this.outputSamplingRate = DEFAULT_SAMPLE_RATE;
                }
                throw th;
            }
        }
        try {
            this.playBuffer = ByteBuffer.allocateDirect(outputBufferSize);
        } catch (Exception e2) {
            log.e(e2.getMessage(), new Object[0]);
        }
        this.tempBufPlay = new byte[outputBufferSize];
        this.captureSettings = new BaseAudioDevice.AudioSettings(this.captureSamplingRate, 1);
        this.rendererSettings = new BaseAudioDevice.AudioSettings(this.outputSamplingRate, 1);
    }

    @Override // com.opentok.android.BaseAudioDevice
    public boolean initCapturer() {
        this.audioManagerMode.acquireMode(this.audioManager);
        int minRecBufSize = AudioRecord.getMinBufferSize(this.captureSettings.getSampleRate(), 16, 2);
        int recBufSize = minRecBufSize * 2;
        if (this.noiseSuppressor != null) {
            this.noiseSuppressor.release();
            this.noiseSuppressor = null;
        }
        if (this.echoCanceler != null) {
            this.echoCanceler.release();
            this.echoCanceler = null;
        }
        if (this.audioRecord != null) {
            this.audioRecord.release();
            this.audioRecord = null;
        }
        try {
            this.audioRecord = new AudioRecord(7, this.captureSettings.getSampleRate(), 16, 2, recBufSize);
            if (NoiseSuppressor.isAvailable()) {
                this.noiseSuppressor = NoiseSuppressor.create(this.audioRecord.getAudioSessionId());
            }
            if (AcousticEchoCanceler.isAvailable()) {
                this.echoCanceler = AcousticEchoCanceler.create(this.audioRecord.getAudioSessionId());
            }
            if (this.audioRecord.getState() != 1) {
                throw new RuntimeException("Audio capture is not initialized " + this.captureSettings.getSampleRate());
            }
            this.shutdownCaptureThread = false;
            new Thread(this.captureThread).start();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override // com.opentok.android.BaseAudioDevice
    public boolean destroyCapturer() {
        this.captureLock.lock();
        if (this.echoCanceler != null) {
            this.echoCanceler.release();
            this.echoCanceler = null;
        }
        if (this.noiseSuppressor != null) {
            this.noiseSuppressor.release();
            this.noiseSuppressor = null;
        }
        this.audioRecord.release();
        this.audioRecord = null;
        this.shutdownCaptureThread = true;
        this.captureEvent.signal();
        this.captureLock.unlock();
        this.audioManagerMode.releaseMode(this.audioManager);
        return true;
    }

    @Override // com.opentok.android.BaseAudioDevice
    public int getEstimatedCaptureDelay() {
        return this.estimatedCaptureDelay;
    }

    @Override // com.opentok.android.BaseAudioDevice
    public boolean startCapturer() {
        try {
            this.audioRecord.startRecording();
            this.captureLock.lock();
            this.isCapturing = true;
            this.captureEvent.signal();
            this.captureLock.unlock();
            return true;
        } catch (IllegalStateException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override // com.opentok.android.BaseAudioDevice
    public boolean stopCapturer() {
        this.captureLock.lock();
        try {
            try {
                if (this.audioRecord.getRecordingState() == 3) {
                    this.audioRecord.stop();
                }
                this.isCapturing = false;
                this.captureLock.unlock();
                return true;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        } catch (Throwable th) {
            this.isCapturing = false;
            this.captureLock.unlock();
            throw th;
        }
    }

    @Override // com.opentok.android.BaseAudioDevice
    public boolean initRenderer() {
        this.bluetoothState = BluetoothState.DISCONNECTED;
        this.audioManagerMode.acquireMode(this.audioManager);
        setOutputMode(getOutputMode());
        enableBluetoothEvents();
        int minPlayBufSize = AudioTrack.getMinBufferSize(this.rendererSettings.getSampleRate(), 4, 2);
        if (this.audioTrack != null) {
            this.audioTrack.release();
            this.audioTrack = null;
        }
        try {
            this.audioTrack = new AudioTrack(0, this.rendererSettings.getSampleRate(), 4, 2, minPlayBufSize >= 6000 ? minPlayBufSize : minPlayBufSize * 2, 1);
            if (this.audioTrack.getState() != 1) {
                throw new RuntimeException("Audio renderer not initialized " + this.rendererSettings.getSampleRate());
            }
            this.bufferedPlaySamples = 0;
            this.shutdownRenderThread = false;
            new Thread(this.renderThread).start();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void destroyAudioTrack() {
        this.rendererLock.lock();
        this.audioTrack.release();
        this.audioTrack = null;
        this.shutdownRenderThread = true;
        this.renderEvent.signal();
        this.rendererLock.unlock();
    }

    @Override // com.opentok.android.BaseAudioDevice
    public boolean destroyRenderer() {
        destroyAudioTrack();
        disableBluetoothEvents();
        unregisterHeadsetReceiver();
        this.audioManager.setSpeakerphoneOn(false);
        this.audioManagerMode.releaseMode(this.audioManager);
        return true;
    }

    @Override // com.opentok.android.BaseAudioDevice
    public int getEstimatedRenderDelay() {
        return this.estimatedRenderDelay;
    }

    @Override // com.opentok.android.BaseAudioDevice
    public boolean startRenderer() {
        synchronized (this.bluetoothLock) {
            if (BluetoothState.CONNECTED != this.bluetoothState) {
                if (this.audioManager.isWiredHeadsetOn()) {
                    log.d("Turn off Speaker phone", new Object[0]);
                    this.audioManager.setSpeakerphoneOn(false);
                } else {
                    log.d("Turn on Speaker phone", new Object[0]);
                    this.audioManager.setSpeakerphoneOn(true);
                }
            }
        }
        try {
            this.audioTrack.play();
            this.rendererLock.lock();
            this.isRendering = true;
            this.renderEvent.signal();
            this.rendererLock.unlock();
            registerHeadsetReceiver();
            return true;
        } catch (IllegalStateException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override // com.opentok.android.BaseAudioDevice
    public boolean stopRenderer() {
        this.rendererLock.lock();
        try {
            try {
                if (this.audioTrack.getPlayState() == 3) {
                    this.audioTrack.stop();
                }
                this.audioTrack.flush();
                this.isRendering = false;
                this.rendererLock.unlock();
                unregisterHeadsetReceiver();
                return true;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        } catch (Throwable th) {
            this.isRendering = false;
            this.rendererLock.unlock();
            throw th;
        }
    }

    @Override // com.opentok.android.BaseAudioDevice
    public BaseAudioDevice.AudioSettings getCaptureSettings() {
        return this.captureSettings;
    }

    @Override // com.opentok.android.BaseAudioDevice
    public BaseAudioDevice.AudioSettings getRenderSettings() {
        return this.rendererSettings;
    }

    @Override // com.opentok.android.BaseAudioDevice
    public boolean setOutputMode(BaseAudioDevice.OutputMode mode) {
        super.setOutputMode(mode);
        if (this.audioOutput == OutputType.BLUETOOTH || this.audioOutput == OutputType.HEADPHONES) {
            return false;
        }
        this.audioManager.setSpeakerphoneOn(mode == BaseAudioDevice.OutputMode.SpeakerPhone);
        return true;
    }

    private void registerHeadsetReceiver() {
        if (!this.receiverRegistered) {
            this.context.registerReceiver(this.headsetReceiver, new IntentFilter("android.intent.action.HEADSET_PLUG"));
            this.receiverRegistered = true;
        }
    }

    private void unregisterHeadsetReceiver() {
        if (this.receiverRegistered) {
            this.context.unregisterReceiver(this.headsetReceiver);
            this.receiverRegistered = false;
        }
    }

    private void registerBtReceiver() {
        if (!this.scoReceiverRegistered) {
            this.context.registerReceiver(this.btStatusReceiver, new IntentFilter("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED"));
            this.scoReceiverRegistered = true;
        }
    }

    private void unregisterBtReceiver() {
        if (this.scoReceiverRegistered) {
            this.context.unregisterReceiver(this.btStatusReceiver);
            this.scoReceiverRegistered = false;
        }
    }

    @Override // com.opentok.android.BaseAudioDevice
    public synchronized void onPause() {
        if (this.isRendering && getOutputMode() == BaseAudioDevice.OutputMode.SpeakerPhone) {
            unregisterHeadsetReceiver();
        }
    }

    @Override // com.opentok.android.BaseAudioDevice
    public synchronized void onResume() {
        if (this.isRendering && getOutputMode() == BaseAudioDevice.OutputMode.SpeakerPhone) {
            registerHeadsetReceiver();
            if (!this.audioManager.isWiredHeadsetOn()) {
                this.audioManager.setSpeakerphoneOn(true);
            }
        }
        synchronized (this.bluetoothLock) {
            if (this.audioOutput == OutputType.BLUETOOTH) {
                this.bluetoothState = BluetoothState.DISCONNECTED;
                if (this.bluetoothAdapter != null) {
                    this.bluetoothAdapter.getProfileProxy(this.context, this.bluetoothProfileListener, 1);
                }
            }
        }
    }

    private void enableBluetoothEvents() {
        if (this.audioManager.isBluetoothScoAvailableOffCall()) {
            registerBtReceiver();
            if (this.bluetoothAdapter != null) {
                this.bluetoothAdapter.getProfileProxy(this.context, this.bluetoothProfileListener, 1);
            }
        }
    }

    private void disableBluetoothEvents() {
        if (this.bluetoothProfile != null && this.bluetoothAdapter != null) {
            this.bluetoothAdapter.closeProfileProxy(1, this.bluetoothProfile);
        }
        unregisterBtReceiver();
        Intent intent = new Intent("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED");
        intent.putExtra("android.bluetooth.profile.extra.STATE", 0);
        this.btStatusReceiver.onReceive(this.context, intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startBluetoothSco() {
        try {
            this.audioManager.startBluetoothSco();
        } catch (NullPointerException e) {
            log.d("Failed to start the BT SCO. In Android 5.0 calling [start|stop]BluetoothSco produces a NPE in some devices", new Object[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopBluetoothSco() {
        try {
            this.audioManager.stopBluetoothSco();
        } catch (NullPointerException e) {
            log.d("Failed to start the BT SCO. In Android 5.0 calling [start|stop]BluetoothSco produces a NPE in some devices", new Object[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setOutputType(OutputType type) {
        this.audioOutput = type;
    }
}
