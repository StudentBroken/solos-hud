package com.kopin.pupil.aria;

import android.content.Context;
import android.media.AudioTrack;
import com.kopin.accessory.AudioCodec;
import com.kopin.pupil.AriaDevice;
import com.kopin.pupil.PupilDevice;
import com.kopin.pupil.aria.tts.AriaTTS;
import com.kopin.pupil.tts.IAudioCallback;

/* JADX INFO: loaded from: classes43.dex */
public class AudioPlayer {
    private static final IAudioCallback mAudioCallback = new IAudioCallback() { // from class: com.kopin.pupil.aria.AudioPlayer.1
        private AudioTrack mLocalPlayer;

        @Override // com.kopin.pupil.tts.IAudioCallback
        public void onAudioEnd(int arg0) {
            if (this.mLocalPlayer != null) {
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException e) {
                }
                this.mLocalPlayer.release();
                this.mLocalPlayer = null;
                AudioRecorder.unmuteLocal();
                AriaTTS.finishedSaying(arg0);
                return;
            }
            if (AriaDevice.isConnected()) {
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException e2) {
                }
                AriaDevice.sendAudioEnd(arg0);
            }
        }

        @Override // com.kopin.pupil.tts.IAudioCallback
        public void onAudioData(byte[] data, int len, AudioCodec codec, int sampleRate) {
            if (this.mLocalPlayer != null) {
                this.mLocalPlayer.write(data, 0, len);
            } else if (PupilDevice.isConnected()) {
                AriaDevice.sendAudioData(data, len, sampleRate);
            }
        }

        @Override // com.kopin.pupil.tts.IAudioCallback
        public void onAudioStart(int startID) {
            if (!PupilDevice.isConnected()) {
                if (Config.ALLOW_LOCAL_PLAYBACK || !Config.IS_PROVISIONED) {
                    AudioRecorder.muteLocal();
                    this.mLocalPlayer = new AudioTrack(3, 16000, 1, 2, 16000, 1);
                    if (this.mLocalPlayer != null) {
                        this.mLocalPlayer.play();
                        return;
                    }
                    return;
                }
                return;
            }
            AudioRecorder.muteRemote();
        }
    };

    public static void init(Context context) {
        AriaTTS.init(context, mAudioCallback);
    }
}
