package com.kopin.pupil.aria.messages;

import android.content.Context;
import com.kopin.pupil.aria.app.CommonGrammar;
import com.kopin.pupil.aria.app.TimedAppState;
import com.kopin.pupil.aria.tts.ConversationPoint;

/* JADX INFO: loaded from: classes55.dex */
public class ConfirmMessage extends TimedAppState {
    private static final String CONFIRM_ID = "MessageConfirm";
    private final ConversationPoint TTS;

    public ConfirmMessage(Context context) {
        super(CONFIRM_ID, CommonGrammar.OK_CANCEL_RETRY, null);
        this.TTS = ConversationPoint.build(context, CONFIRM_ID, 0, R.array.tts_messages_confirm_msg_silent, R.array.tts_messages_confirm_msg_once, R.array.tts_messages_confirm_msg_terse, R.array.tts_messages_confirm_msg_verbose, R.array.tts_messages_confirm_msg_verbose);
    }

    @Override // com.kopin.pupil.aria.app.AppState
    protected ConversationPoint getOnEnterSpeech() {
        return this.TTS;
    }

    @Override // com.kopin.pupil.aria.app.AppState
    public boolean onCommandResult(String cmd) {
        for (String t : CommonGrammar.NO_CANCEL) {
            if (cmd.contentEquals(t)) {
                return false;
            }
        }
        for (String t2 : CommonGrammar.YES_OK) {
            if (cmd.contentEquals(t2)) {
                setAppState("confirm");
                return true;
            }
        }
        setAppState("message");
        return true;
    }
}
