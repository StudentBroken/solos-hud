package com.kopin.pupil.aria.dialler;

import android.content.Context;
import com.kopin.pupil.PupilDevice;
import com.kopin.pupil.aria.app.CommonGrammar;
import com.kopin.pupil.aria.app.TimedAppState;
import com.kopin.pupil.aria.phone.R;
import com.kopin.pupil.aria.tts.ConversationPoint;

/* JADX INFO: loaded from: classes20.dex */
public class Confirm extends TimedAppState {
    private static final String CONFIRM_ID = "CallConfirm";
    private final ConversationPoint PERMISSION_CALL;
    private final ConversationPoint TTS;
    private Context mContext;

    public Confirm(Context context) {
        super(CONFIRM_ID, CommonGrammar.YES_NO_OK_CANCEL, null);
        this.mContext = context;
        this.TTS = ConversationPoint.build(context, CONFIRM_ID, 0, R.array.tts_dialler_confirm_silent, R.array.tts_dialler_confirm_once, R.array.tts_dialler_confirm_terse, R.array.tts_dialler_confirm_verbose, R.array.tts_dialler_confirm_verbose);
        this.PERMISSION_CALL = ConversationPoint.build(context, CONFIRM_ID, 0, R.array.tts_dialler_permission, R.array.tts_dialler_permission, R.array.tts_dialler_permission, R.array.tts_dialler_permission, R.array.tts_dialler_permission);
    }

    @Override // com.kopin.pupil.aria.app.AppState
    protected ConversationPoint getOnEnterSpeech() {
        return this.TTS;
    }

    @Override // com.kopin.pupil.aria.app.AppState
    public boolean onCommandResult(String cmd) {
        for (String t : CommonGrammar.YES_OK) {
            if (cmd.contentEquals(t)) {
                PupilDevice.sendDialPacket(Dialler.NUMBER_TO_CALL);
                setAppState("incall");
                return true;
            }
        }
        return false;
    }
}
