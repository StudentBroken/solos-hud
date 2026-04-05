package com.kopin.pupil.aria.messages;

import android.content.Context;
import com.kopin.pupil.PupilSpeechRecognizer;
import com.kopin.pupil.aria.app.AppState;
import com.kopin.pupil.aria.app.CommonGrammar;
import com.kopin.pupil.aria.app.CommonTTS;
import com.kopin.pupil.aria.tts.ConversationPoint;

/* JADX INFO: loaded from: classes55.dex */
public class GetMessage extends AppState {
    private static final String GET_MESSAGE_ID = "GetMessage";
    private final ConversationPoint TTS_ASK_MESSAGE;
    private final ConversationPoint TTS_DICTATE_MESSAGE;
    private Context mContext;
    private boolean tryDictation;

    public GetMessage(Context context, String[] canned) {
        super(GET_MESSAGE_ID, canned);
        this.mContext = context;
        this.TTS_ASK_MESSAGE = ConversationPoint.build(context, GET_MESSAGE_ID, 0, R.array.tts_messages_getmessage_silent, R.array.tts_messages_getmessage_once, R.array.tts_messages_getmessage_terse, R.array.tts_messages_getmessage_verbose, R.array.tts_messages_getmessage_verbose);
        this.TTS_DICTATE_MESSAGE = ConversationPoint.build(context, GET_MESSAGE_ID, 0, R.array.tts_messages_saymessage_silent, R.array.tts_messages_saymessage_once, R.array.tts_messages_saymessage_terse, R.array.tts_messages_saymessage_verbose, R.array.tts_messages_saymessage_verbose);
    }

    @Override // com.kopin.pupil.aria.app.AppState
    protected ConversationPoint getOnEnterSpeech() {
        return this.tryDictation ? this.TTS_DICTATE_MESSAGE : this.TTS_ASK_MESSAGE;
    }

    @Override // com.kopin.pupil.aria.app.AppState
    protected void onEnter() {
        this.tryDictation = isDictationAvailable();
        super.onEnter();
    }

    @Override // com.kopin.pupil.vocon.Grammar
    public boolean needsDictation() {
        return this.tryDictation;
    }

    @Override // com.kopin.pupil.aria.app.AppState
    public boolean holdIdle() {
        return this.tryDictation;
    }

    @Override // com.kopin.pupil.aria.app.AppState
    public boolean onCommandResult(String cmd) {
        for (String t : CommonGrammar.NO_CANCEL) {
            if (cmd.contentEquals(t)) {
                return false;
            }
        }
        Messages.SEND_MESSAGE = cmd;
        if (Messages.SEND_MESSAGE == null || Messages.SEND_MESSAGE.isEmpty()) {
            sayText(CommonTTS.SORRY_DIDNT_UNDERSTAND);
            return true;
        }
        setAppState(this.tryDictation ? "confirm_msg" : "confirm");
        return true;
    }

    @Override // com.kopin.pupil.aria.app.AppState
    public boolean onASRError(PupilSpeechRecognizer.ASRErrors code) {
        if (code != PupilSpeechRecognizer.ASRErrors.NO_CONNECTIVITY) {
            return false;
        }
        this.tryDictation = false;
        return true;
    }
}
