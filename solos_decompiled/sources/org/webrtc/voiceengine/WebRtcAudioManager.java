package org.webrtc.voiceengine;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.Build;
import com.kopin.pupil.aria.app.TimedAppState;
import java.util.Timer;
import java.util.TimerTask;
import org.webrtc.Logging;

/* JADX INFO: loaded from: classes57.dex */
public class WebRtcAudioManager {
    private static final int BITS_PER_SAMPLE = 16;
    private static final int CHANNELS = 1;
    private static final boolean DEBUG = false;
    private static final int DEFAULT_FRAME_PER_BUFFER = 256;
    private static final String TAG = "WebRtcAudioManager";
    private final AudioManager audioManager;
    private int channels;
    private final Context context;
    private boolean hardwareAEC;
    private boolean hardwareAGC;
    private boolean hardwareNS;
    private boolean initialized = false;
    private int inputBufferSize;
    private boolean lowLatencyInput;
    private boolean lowLatencyOutput;
    private final long nativeAudioManager;
    private int nativeChannels;
    private int nativeSampleRate;
    private int outputBufferSize;
    private boolean proAudio;
    private int sampleRate;
    private final VolumeLogger volumeLogger;
    private static boolean blacklistDeviceForOpenSLESUsage = false;
    private static boolean blacklistDeviceForOpenSLESUsageIsOverridden = false;
    private static final String[] AUDIO_MODES = {"MODE_NORMAL", "MODE_RINGTONE", "MODE_IN_CALL", "MODE_IN_COMMUNICATION"};

    private native void nativeCacheAudioParameters(int i, int i2, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, int i3, int i4, long j);

    public static synchronized void setBlacklistDeviceForOpenSLESUsage(boolean enable) {
        blacklistDeviceForOpenSLESUsageIsOverridden = true;
        blacklistDeviceForOpenSLESUsage = enable;
    }

    private static class VolumeLogger {
        private static final String THREAD_NAME = "WebRtcVolumeLevelLoggerThread";
        private static final int TIMER_PERIOD_IN_SECONDS = 10;
        private final AudioManager audioManager;
        private Timer timer;

        public VolumeLogger(AudioManager audioManager) {
            this.audioManager = audioManager;
        }

        public void start() {
            this.timer = new Timer(THREAD_NAME);
            this.timer.schedule(new LogVolumeTask(this.audioManager.getStreamMaxVolume(2), this.audioManager.getStreamMaxVolume(0)), 0L, TimedAppState.DEFAULT_CONFIRM_TIMEOUT);
        }

        private class LogVolumeTask extends TimerTask {
            private final int maxRingVolume;
            private final int maxVoiceCallVolume;

            LogVolumeTask(int maxRingVolume, int maxVoiceCallVolume) {
                this.maxRingVolume = maxRingVolume;
                this.maxVoiceCallVolume = maxVoiceCallVolume;
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                int mode = VolumeLogger.this.audioManager.getMode();
                if (mode == 1) {
                    Logging.d(WebRtcAudioManager.TAG, "STREAM_RING stream volume: " + VolumeLogger.this.audioManager.getStreamVolume(2) + " (max=" + this.maxRingVolume + ")");
                } else if (mode == 3) {
                    Logging.d(WebRtcAudioManager.TAG, "VOICE_CALL stream volume: " + VolumeLogger.this.audioManager.getStreamVolume(0) + " (max=" + this.maxVoiceCallVolume + ")");
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void stop() {
            if (this.timer != null) {
                this.timer.cancel();
                this.timer = null;
            }
        }
    }

    WebRtcAudioManager(Context context, long nativeAudioManager) {
        Logging.d(TAG, "ctor" + WebRtcAudioUtils.getThreadInfo());
        this.context = context;
        this.nativeAudioManager = nativeAudioManager;
        this.audioManager = (AudioManager) context.getSystemService("audio");
        this.volumeLogger = new VolumeLogger(this.audioManager);
        storeAudioParameters();
        nativeCacheAudioParameters(this.sampleRate, this.channels, this.hardwareAEC, this.hardwareAGC, this.hardwareNS, this.lowLatencyOutput, this.lowLatencyInput, this.proAudio, this.outputBufferSize, this.inputBufferSize, nativeAudioManager);
    }

    private boolean init() {
        Logging.d(TAG, "init" + WebRtcAudioUtils.getThreadInfo());
        if (!this.initialized) {
            Logging.d(TAG, "audio mode is: " + AUDIO_MODES[this.audioManager.getMode()]);
            this.initialized = true;
            this.volumeLogger.start();
        }
        return true;
    }

    private void dispose() {
        Logging.d(TAG, "dispose" + WebRtcAudioUtils.getThreadInfo());
        if (this.initialized) {
            this.volumeLogger.stop();
        }
    }

    private boolean isCommunicationModeEnabled() {
        return this.audioManager.getMode() == 3;
    }

    private boolean isDeviceBlacklistedForOpenSLESUsage() {
        boolean blacklisted = blacklistDeviceForOpenSLESUsageIsOverridden ? blacklistDeviceForOpenSLESUsage : WebRtcAudioUtils.deviceIsBlacklistedForOpenSLESUsage();
        if (blacklisted) {
            Logging.e(TAG, Build.MODEL + " is blacklisted for OpenSL ES usage!");
        }
        return blacklisted;
    }

    private void storeAudioParameters() {
        this.channels = 1;
        this.sampleRate = getNativeOutputSampleRate();
        this.hardwareAEC = isAcousticEchoCancelerSupported();
        this.hardwareAGC = false;
        this.hardwareNS = isNoiseSuppressorSupported();
        this.lowLatencyOutput = isLowLatencyOutputSupported();
        this.lowLatencyInput = isLowLatencyInputSupported();
        this.proAudio = isProAudioSupported();
        this.outputBufferSize = this.lowLatencyOutput ? getLowLatencyOutputFramesPerBuffer() : getMinOutputFrameSize(this.sampleRate, this.channels);
        this.inputBufferSize = this.lowLatencyInput ? getLowLatencyInputFramesPerBuffer() : getMinInputFrameSize(this.sampleRate, this.channels);
    }

    private boolean hasEarpiece() {
        return this.context.getPackageManager().hasSystemFeature("android.hardware.telephony");
    }

    private boolean isLowLatencyOutputSupported() {
        return isOpenSLESSupported() && this.context.getPackageManager().hasSystemFeature("android.hardware.audio.low_latency");
    }

    public boolean isLowLatencyInputSupported() {
        return WebRtcAudioUtils.runningOnLollipopOrHigher() && isLowLatencyOutputSupported();
    }

    private boolean isProAudioSupported() {
        return WebRtcAudioUtils.runningOnMarshmallowOrHigher() && this.context.getPackageManager().hasSystemFeature("android.hardware.audio.pro");
    }

    private int getNativeOutputSampleRate() {
        int sampleRateHz;
        if (WebRtcAudioUtils.runningOnEmulator()) {
            Logging.d(TAG, "Running emulator, overriding sample rate to 8 kHz.");
            return 8000;
        }
        if (WebRtcAudioUtils.isDefaultSampleRateOverridden()) {
            Logging.d(TAG, "Default sample rate is overriden to " + WebRtcAudioUtils.getDefaultSampleRateHz() + " Hz");
            return WebRtcAudioUtils.getDefaultSampleRateHz();
        }
        if (WebRtcAudioUtils.runningOnJellyBeanMR1OrHigher()) {
            sampleRateHz = getSampleRateOnJellyBeanMR10OrHigher();
        } else {
            sampleRateHz = WebRtcAudioUtils.getDefaultSampleRateHz();
        }
        Logging.d(TAG, "Sample rate is set to " + sampleRateHz + " Hz");
        return sampleRateHz;
    }

    @TargetApi(17)
    private int getSampleRateOnJellyBeanMR10OrHigher() {
        String sampleRateString = this.audioManager.getProperty("android.media.property.OUTPUT_SAMPLE_RATE");
        return sampleRateString == null ? WebRtcAudioUtils.getDefaultSampleRateHz() : Integer.parseInt(sampleRateString);
    }

    @TargetApi(17)
    private int getLowLatencyOutputFramesPerBuffer() {
        String framesPerBuffer;
        assertTrue(isLowLatencyOutputSupported());
        if (WebRtcAudioUtils.runningOnJellyBeanMR1OrHigher() && (framesPerBuffer = this.audioManager.getProperty("android.media.property.OUTPUT_FRAMES_PER_BUFFER")) != null) {
            return Integer.parseInt(framesPerBuffer);
        }
        return 256;
    }

    private static boolean isAcousticEchoCancelerSupported() {
        return WebRtcAudioEffects.canUseAcousticEchoCanceler();
    }

    private static boolean isNoiseSuppressorSupported() {
        return WebRtcAudioEffects.canUseNoiseSuppressor();
    }

    private static int getMinOutputFrameSize(int sampleRateInHz, int numChannels) {
        int channelConfig;
        int bytesPerFrame = numChannels * 2;
        if (numChannels == 1) {
            channelConfig = 4;
        } else if (numChannels == 2) {
            channelConfig = 12;
        } else {
            return -1;
        }
        return AudioTrack.getMinBufferSize(sampleRateInHz, channelConfig, 2) / bytesPerFrame;
    }

    private int getLowLatencyInputFramesPerBuffer() {
        assertTrue(isLowLatencyInputSupported());
        return getLowLatencyOutputFramesPerBuffer();
    }

    private static int getMinInputFrameSize(int sampleRateInHz, int numChannels) {
        int bytesPerFrame = numChannels * 2;
        assertTrue(numChannels == 1);
        return AudioRecord.getMinBufferSize(sampleRateInHz, 16, 2) / bytesPerFrame;
    }

    private static boolean isOpenSLESSupported() {
        return WebRtcAudioUtils.runningOnGingerBreadOrHigher();
    }

    private static void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Expected condition to be true");
        }
    }
}
