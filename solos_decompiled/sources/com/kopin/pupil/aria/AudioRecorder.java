package com.kopin.pupil.aria;

import android.media.AudioRecord;
import android.util.Log;
import com.kopin.accessory.AudioCodec;
import com.kopin.pupil.PupilDevice;

/* JADX INFO: loaded from: classes43.dex */
public class AudioRecorder {
    private static boolean micsOpen = false;
    private static boolean isMuted = false;
    private static AudioRecordThread mRecorderThread = null;

    public interface AudioSink {
        void onAudioReceived(byte[] bArr);
    }

    public static void start(AudioSink cb) {
        if (PupilDevice.isConnected()) {
            openMics();
        } else {
            startLocalRecording(cb);
        }
    }

    public static void stop() {
        if (PupilDevice.isConnected()) {
            closeMics();
        } else {
            stopLocalRecording();
        }
    }

    public static void muteLocal() {
        Log.d("SpeechRecorder", "Mute local");
        if (mRecorderThread == null) {
            return;
        }
        mRecorderThread.muted = true;
    }

    public static void unmuteLocal() {
        Log.d("SpeechRecorder", "Unmute local");
        if (mRecorderThread == null) {
            return;
        }
        mRecorderThread.muted = false;
    }

    public static void muteRemote() {
        Log.d("SpeechRecorder", "Mute remote, mics open: " + micsOpen);
        if (micsOpen) {
            PupilDevice.disableMics();
        }
    }

    public static void unmuteRemote() {
        Log.d("SpeechRecorder", "Unmute remote, mics open: " + micsOpen);
        if (micsOpen) {
            PupilDevice.enableMics(false, AudioCodec.MSBC);
        }
    }

    private static void openMics() {
        Log.d("SpeechRecorder", "Open mics, muted: " + isMuted);
        micsOpen = true;
        if (!isMuted) {
            PupilDevice.enableMics(false, AudioCodec.MSBC);
        }
    }

    private static void closeMics() {
        Log.d("SpeechRecorder", "Close mics, muted: " + isMuted);
        micsOpen = false;
        if (!isMuted) {
            PupilDevice.disableMics();
        }
    }

    private static void startLocalRecording(AudioSink cb) {
        if (mRecorderThread == null && Config.ALLOW_LOCAL_RECORD) {
            mRecorderThread = new AudioRecordThread(cb);
            mRecorderThread.start();
        }
    }

    private static void stopLocalRecording() {
        if (mRecorderThread != null) {
            mRecorderThread.interrupt();
            mRecorderThread = null;
        }
    }

    private static class AudioRecordThread extends Thread {
        private AudioRecord mRecorder;
        private AudioSink mResponseListener;
        private boolean muted;

        public AudioRecordThread(AudioSink cb) {
            super("AudioRecorder");
            this.mRecorder = null;
            this.mRecorder = new AudioRecord(0, 16000, 1, 2, 8000);
            this.mResponseListener = cb;
            this.muted = false;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            this.mRecorder.startRecording();
            byte[] buf = new byte[8000];
            while (!isInterrupted()) {
                try {
                    sleep(100L);
                    int len = this.mRecorder.read(buf, 0, 8000);
                    if (len > 0 && !this.muted) {
                        this.mResponseListener.onAudioReceived(buf);
                    }
                } catch (InterruptedException e) {
                }
            }
            this.mRecorder.stop();
            this.mRecorder.release();
        }
    }
}
