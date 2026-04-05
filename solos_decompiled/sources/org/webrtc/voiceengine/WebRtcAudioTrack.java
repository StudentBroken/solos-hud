package org.webrtc.voiceengine;

import android.content.Context;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Process;
import java.nio.ByteBuffer;
import org.webrtc.Logging;

/* JADX INFO: loaded from: classes57.dex */
public class WebRtcAudioTrack {
    private static final int BITS_PER_SAMPLE = 16;
    private static final int BUFFERS_PER_SECOND = 100;
    private static final int CALLBACK_BUFFER_SIZE_MS = 10;
    private static final boolean DEBUG = false;
    private static final String TAG = "WebRtcAudioTrack";
    private static volatile boolean speakerMute = false;
    private final AudioManager audioManager;
    private ByteBuffer byteBuffer;
    private final Context context;
    private byte[] emptyBytes;
    private final long nativeAudioTrack;
    private AudioTrack audioTrack = null;
    private AudioTrackThread audioThread = null;

    private native void nativeCacheDirectBufferAddress(ByteBuffer byteBuffer, long j);

    /* JADX INFO: Access modifiers changed from: private */
    public native void nativeGetPlayoutData(int i, long j);

    private class AudioTrackThread extends Thread {
        private volatile boolean keepAlive;

        public AudioTrackThread(String name) {
            super(name);
            this.keepAlive = true;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Process.setThreadPriority(-19);
            Logging.d(WebRtcAudioTrack.TAG, "AudioTrackThread" + WebRtcAudioUtils.getThreadInfo());
            try {
                WebRtcAudioTrack.this.audioTrack.play();
                WebRtcAudioTrack.assertTrue(WebRtcAudioTrack.this.audioTrack.getPlayState() == 3);
                int sizeInBytes = WebRtcAudioTrack.this.byteBuffer.capacity();
                while (this.keepAlive) {
                    WebRtcAudioTrack.this.nativeGetPlayoutData(sizeInBytes, WebRtcAudioTrack.this.nativeAudioTrack);
                    WebRtcAudioTrack.assertTrue(sizeInBytes <= WebRtcAudioTrack.this.byteBuffer.remaining());
                    if (WebRtcAudioTrack.speakerMute) {
                        WebRtcAudioTrack.this.byteBuffer.clear();
                        WebRtcAudioTrack.this.byteBuffer.put(WebRtcAudioTrack.this.emptyBytes);
                        WebRtcAudioTrack.this.byteBuffer.position(0);
                    }
                    int bytesWritten = WebRtcAudioUtils.runningOnLollipopOrHigher() ? writeOnLollipop(WebRtcAudioTrack.this.audioTrack, WebRtcAudioTrack.this.byteBuffer, sizeInBytes) : writePreLollipop(WebRtcAudioTrack.this.audioTrack, WebRtcAudioTrack.this.byteBuffer, sizeInBytes);
                    if (bytesWritten != sizeInBytes) {
                        Logging.e(WebRtcAudioTrack.TAG, "AudioTrack.write failed: " + bytesWritten);
                        if (bytesWritten == -3) {
                            this.keepAlive = false;
                        }
                    }
                    WebRtcAudioTrack.this.byteBuffer.rewind();
                }
                try {
                    WebRtcAudioTrack.this.audioTrack.stop();
                } catch (IllegalStateException e) {
                    Logging.e(WebRtcAudioTrack.TAG, "AudioTrack.stop failed: " + e.getMessage());
                }
                WebRtcAudioTrack.assertTrue(WebRtcAudioTrack.this.audioTrack.getPlayState() == 1);
                WebRtcAudioTrack.this.audioTrack.flush();
            } catch (IllegalStateException e2) {
                Logging.e(WebRtcAudioTrack.TAG, "AudioTrack.play failed: " + e2.getMessage());
            }
        }

        private int writeOnLollipop(AudioTrack audioTrack, ByteBuffer byteBuffer, int sizeInBytes) {
            return audioTrack.write(byteBuffer, sizeInBytes, 0);
        }

        private int writePreLollipop(AudioTrack audioTrack, ByteBuffer byteBuffer, int sizeInBytes) {
            return audioTrack.write(byteBuffer.array(), byteBuffer.arrayOffset(), sizeInBytes);
        }

        public void joinThread() {
            this.keepAlive = false;
            while (isAlive()) {
                try {
                    join();
                } catch (InterruptedException e) {
                }
            }
        }
    }

    WebRtcAudioTrack(Context context, long nativeAudioTrack) {
        Logging.d(TAG, "ctor" + WebRtcAudioUtils.getThreadInfo());
        this.context = context;
        this.nativeAudioTrack = nativeAudioTrack;
        this.audioManager = (AudioManager) context.getSystemService("audio");
    }

    private boolean initPlayout(int sampleRate, int channels) {
        Logging.d(TAG, "initPlayout(sampleRate=" + sampleRate + ", channels=" + channels + ")");
        int bytesPerFrame = channels * 2;
        ByteBuffer byteBuffer = this.byteBuffer;
        this.byteBuffer = ByteBuffer.allocateDirect((sampleRate / 100) * bytesPerFrame);
        Logging.d(TAG, "byteBuffer.capacity: " + this.byteBuffer.capacity());
        this.emptyBytes = new byte[this.byteBuffer.capacity()];
        nativeCacheDirectBufferAddress(this.byteBuffer, this.nativeAudioTrack);
        int minBufferSizeInBytes = AudioTrack.getMinBufferSize(sampleRate, 4, 2);
        Logging.d(TAG, "AudioTrack.getMinBufferSize: " + minBufferSizeInBytes);
        if (minBufferSizeInBytes < this.byteBuffer.capacity()) {
            Logging.e(TAG, "AudioTrack.getMinBufferSize returns an invalid value.");
            return false;
        }
        if (this.audioTrack != null) {
            Logging.e(TAG, "Conflict with existing AudioTrack.");
            return false;
        }
        try {
            this.audioTrack = new AudioTrack(0, sampleRate, 4, 2, minBufferSizeInBytes, 1);
            if (this.audioTrack.getState() != 1) {
                Logging.e(TAG, "Initialization of audio track failed.");
                return false;
            }
            if (!areParametersValid(sampleRate, channels)) {
                Logging.e(TAG, "At least one audio track parameter is invalid.");
                return false;
            }
            logMainParameters();
            logMainParametersExtended();
            return true;
        } catch (IllegalArgumentException e) {
            Logging.d(TAG, e.getMessage());
            return false;
        }
    }

    private boolean startPlayout() {
        Logging.d(TAG, "startPlayout");
        assertTrue(this.audioTrack != null);
        assertTrue(this.audioThread == null);
        if (this.audioTrack.getState() != 1) {
            Logging.e(TAG, "Audio track is not successfully initialized.");
            return false;
        }
        this.audioThread = new AudioTrackThread("AudioTrackJavaThread");
        this.audioThread.start();
        return true;
    }

    private boolean stopPlayout() {
        Logging.d(TAG, "stopPlayout");
        assertTrue(this.audioThread != null);
        logUnderrunCount();
        this.audioThread.joinThread();
        this.audioThread = null;
        if (this.audioTrack != null) {
            this.audioTrack.release();
            this.audioTrack = null;
        }
        return true;
    }

    private int getStreamMaxVolume() {
        Logging.d(TAG, "getStreamMaxVolume");
        assertTrue(this.audioManager != null);
        return this.audioManager.getStreamMaxVolume(0);
    }

    private boolean setStreamVolume(int volume) {
        Logging.d(TAG, "setStreamVolume(" + volume + ")");
        assertTrue(this.audioManager != null);
        if (isVolumeFixed()) {
            Logging.e(TAG, "The device implements a fixed volume policy.");
            return false;
        }
        this.audioManager.setStreamVolume(0, volume, 0);
        return true;
    }

    private boolean isVolumeFixed() {
        if (WebRtcAudioUtils.runningOnLollipopOrHigher()) {
            return this.audioManager.isVolumeFixed();
        }
        return false;
    }

    private int getStreamVolume() {
        Logging.d(TAG, "getStreamVolume");
        assertTrue(this.audioManager != null);
        return this.audioManager.getStreamVolume(0);
    }

    private boolean areParametersValid(int sampleRate, int channels) {
        int streamType = this.audioTrack.getStreamType();
        if (this.audioTrack.getAudioFormat() == 2 && this.audioTrack.getChannelConfiguration() == 4 && streamType == 0 && this.audioTrack.getSampleRate() == sampleRate) {
            AudioTrack audioTrack = this.audioTrack;
            if (sampleRate == AudioTrack.getNativeOutputSampleRate(streamType) && this.audioTrack.getChannelCount() == channels) {
                return true;
            }
        }
        return false;
    }

    private void logMainParameters() {
        StringBuilder sbAppend = new StringBuilder().append("AudioTrack: session ID: ").append(this.audioTrack.getAudioSessionId()).append(", ").append("channels: ").append(this.audioTrack.getChannelCount()).append(", ").append("sample rate: ").append(this.audioTrack.getSampleRate()).append(", ").append("max gain: ");
        AudioTrack audioTrack = this.audioTrack;
        Logging.d(TAG, sbAppend.append(AudioTrack.getMaxVolume()).toString());
    }

    private void logMainParametersExtended() {
        if (WebRtcAudioUtils.runningOnMarshmallowOrHigher()) {
            Logging.d(TAG, "AudioTrack: buffer size in frames: " + this.audioTrack.getBufferSizeInFrames());
        }
        if (WebRtcAudioUtils.runningOnNougatOrHigher()) {
            Logging.d(TAG, "AudioTrack: buffer capacity in frames: " + this.audioTrack.getBufferCapacityInFrames());
        }
    }

    private void logUnderrunCount() {
        if (WebRtcAudioUtils.runningOnNougatOrHigher()) {
            Logging.d(TAG, "underrun count: " + this.audioTrack.getUnderrunCount());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Expected condition to be true");
        }
    }

    public static void setSpeakerMute(boolean mute) {
        Logging.w(TAG, "setSpeakerMute(" + mute + ")");
        speakerMute = mute;
    }
}
