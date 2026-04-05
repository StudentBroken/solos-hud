package com.kopin.pupil.aria.tts;

import android.content.Context;
import android.util.Log;
import com.kopin.accessory.AudioCodec;
import com.kopin.pupil.tts.IAudioCallback;
import com.kopin.pupil.tts.PupilVoice;
import com.kopin.pupil.tts.TTS;
import java.util.HashMap;

/* JADX INFO: loaded from: classes32.dex */
public class AriaTTS {
    private static final String TAG = "AriaTTS";
    private static IAudioCallback mAudioCallback;
    private static TTS mTTS;
    private static final char[] SUBS_IDENTIFIER_CHARS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private static int mLastId = 1024;
    private static SayPriority mCurTalkative = SayPriority.VERBOSE;
    private static HashMap<Integer, TTSRequest> mTTSReqs = new HashMap<>();
    private static final IAudioCallback mTTSCallbackProxy = new IAudioCallback() { // from class: com.kopin.pupil.aria.tts.AriaTTS.1
        @Override // com.kopin.pupil.tts.IAudioCallback
        public void onAudioStart(int startID) {
            if (AriaTTS.mAudioCallback != null) {
                AriaTTS.mAudioCallback.onAudioStart(startID);
            }
        }

        @Override // com.kopin.pupil.tts.IAudioCallback
        public void onAudioEnd(int endID) {
            if (AriaTTS.mAudioCallback != null) {
                AriaTTS.mAudioCallback.onAudioEnd(endID);
            }
        }

        @Override // com.kopin.pupil.tts.IAudioCallback
        public void onAudioData(byte[] data, int length, AudioCodec codec, int sampleRate) {
            if (AriaTTS.mAudioCallback != null) {
                AriaTTS.mAudioCallback.onAudioData(data, length, codec, sampleRate);
            }
        }
    };

    public interface AudioChannelListener {
        void onAudioEnd(int i);

        void onAudioStart(int i);
    }

    public enum SayPriority {
        OFF,
        SILENT,
        FIRST_TIME,
        TERSE,
        HINT,
        VERBOSE
    }

    public interface TTSListener {
        String getSpeechSubstitution(String str, String str2);

        void onFinishedSaying(int i, String str);

        void onStartSaying(int i, String str, String str2);
    }

    private static class TTSRequest {
        public final TTSListener mCb;
        public final String mTag;

        public TTSRequest(String tag, TTSListener cb) {
            this.mTag = tag;
            this.mCb = cb;
        }
    }

    public static void init(Context context, IAudioCallback cb) {
        mTTS = new TTS(context);
        mAudioCallback = cb;
        mTTS.setAudioCallback(mTTSCallbackProxy);
    }

    public static PupilVoice[] getVoiceList() {
        return mTTS.getVoiceList();
    }

    public static void selectVoice(PupilVoice voice) {
        mTTS.selectVoice(voice);
    }

    public static boolean isActive() {
        return !mTTSReqs.isEmpty();
    }

    public static void setTalkativeLevel(SayPriority priority) {
        mCurTalkative = priority;
    }

    private static boolean checkPriority(SayPriority priority) {
        int level = priority.ordinal();
        int effective = SayPriority.VERBOSE.ordinal() - mCurTalkative.ordinal();
        return level >= effective && level > effective;
    }

    private static boolean isSubsIndentifier(char test) {
        for (char t : SUBS_IDENTIFIER_CHARS) {
            if (t == test) {
                return true;
            }
        }
        return false;
    }

    private static String performSubstitutions(String tag, String text, TTSListener cb) {
        int idx = text.indexOf(36);
        while (idx != -1) {
            int end = idx + 1;
            while (end < text.length() && isSubsIndentifier(text.charAt(end))) {
                end++;
            }
            String subs = text.substring(idx + 1, end);
            String value = cb.getSpeechSubstitution(tag, subs);
            if (value == null) {
                value = "";
            }
            Log.d(TAG, "  substitute '" + subs + "' -> '" + value + "'");
            text = text.replace("$" + subs, value);
            idx = text.indexOf(36, idx);
        }
        return text;
    }

    public static int sayText(SayPriority priority, String tag, String text, TTSListener cb) {
        Log.d(TAG, "Saying " + tag + ": " + text);
        Log.d(TAG, "  request level: " + priority + " current tts level: " + mCurTalkative);
        String text2 = performSubstitutions(tag, text, cb);
        Log.d(TAG, "  after substitutions: " + text2);
        TTSRequest req = new TTSRequest(tag, cb);
        HashMap<Integer, TTSRequest> map = mTTSReqs;
        int i = mLastId + 1;
        mLastId = i;
        map.put(Integer.valueOf(i), req);
        if (cb != null) {
            cb.onStartSaying(mLastId, tag, text2);
        }
        mTTS.speak(mLastId, text2);
        return mLastId;
    }

    public static int sayText(ConversationPoint text, TTSListener cb) {
        return sayText(mCurTalkative, text.getTag(), text.selectText(mCurTalkative), cb);
    }

    public static int sayText(SayPriority priority, ConversationPoint text, TTSListener cb) {
        return sayText(priority, text.getTag(), text.selectText(priority), cb);
    }

    public static void finishedSaying(int endID) {
        TTSRequest req = mTTSReqs.remove(Integer.valueOf(endID));
        if (req != null && req.mCb != null) {
            req.mCb.onFinishedSaying(endID, req.mTag);
        }
    }

    public static void startAudio(String tag, AudioChannelListener cb) {
        TTSExternalListener shim = new TTSExternalListener(cb);
        TTSRequest req = new TTSRequest(tag, shim);
        HashMap<Integer, TTSRequest> map = mTTSReqs;
        int i = mLastId + 1;
        mLastId = i;
        map.put(Integer.valueOf(i), req);
        if (shim != null) {
            shim.onStartSaying(mLastId, tag, null);
        }
    }

    public static void endAudio(int id) {
        if (mAudioCallback != null) {
            mAudioCallback.onAudioEnd(id);
        }
    }

    private static class TTSExternalListener implements TTSListener {
        private final AudioChannelListener mCb;

        public TTSExternalListener(AudioChannelListener callback) {
            this.mCb = callback;
        }

        @Override // com.kopin.pupil.aria.tts.AriaTTS.TTSListener
        public String getSpeechSubstitution(String tag, String subs) {
            return null;
        }

        @Override // com.kopin.pupil.aria.tts.AriaTTS.TTSListener
        public void onStartSaying(int id, String tag, String finalText) {
            if (this.mCb != null) {
                this.mCb.onAudioStart(id);
            }
        }

        @Override // com.kopin.pupil.aria.tts.AriaTTS.TTSListener
        public void onFinishedSaying(int id, String tag) {
            if (this.mCb != null) {
                this.mCb.onAudioEnd(id);
            }
        }
    }
}
