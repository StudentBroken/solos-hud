package com.kopin.pupil.aria.contacts;

import android.content.Context;
import com.kopin.aria.app.R;
import com.kopin.pupil.aria.app.AppState;
import com.kopin.pupil.aria.app.CommonGrammar;
import com.kopin.pupil.aria.app.CommonTTS;
import com.kopin.pupil.aria.tts.ConversationPoint;

/* JADX INFO: loaded from: classes43.dex */
public class CheckWho extends AppState {
    private static final String TAG = "CheckWho";
    private final ConversationPoint TTS_NO_NUMBER;

    public CheckWho(Context context) {
        super(TAG, CommonGrammar.YES_NO_OK_CANCEL);
        this.TTS_NO_NUMBER = ConversationPoint.build(context, TAG, 0, R.array.tts_nonumber_silent, R.array.tts_nonumber_once, R.array.tts_nonumber_terse, R.array.tts_nonumber_verbose, R.array.tts_nonumber_verbose);
    }

    @Override // com.kopin.pupil.aria.app.AppState
    protected ConversationPoint getOnEnterSpeech() {
        return CommonTTS.SORRY_DIDNT_UNDERSTAND;
    }

    @Override // com.kopin.pupil.aria.app.AppState
    public boolean onCommandResult(String cmd) {
        sayText(this.TTS_NO_NUMBER);
        return false;
    }
}
