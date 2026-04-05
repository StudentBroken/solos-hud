package com.kopin.solos.vocon;

import android.content.Context;
import android.content.res.Resources;
import com.kopin.pupil.aria.app.AppState;
import com.kopin.pupil.aria.app.CommonGrammar;
import com.kopin.pupil.aria.tts.ConversationPoint;
import com.kopin.solos.RideControl;
import com.kopin.solos.core.R;
import com.kopin.solos.storage.LiveRide;

/* JADX INFO: loaded from: classes37.dex */
public class SaveRide extends AppState {
    private static final String TAG = "SaveRide";
    private final ConversationPoint TTS_CONFIRM_RIDE;
    private final ConversationPoint TTS_CONFIRM_RUN;

    public SaveRide(Context context, Resources resources) {
        super(TAG, CommonGrammar.YES_NO_OK_CANCEL);
        this.TTS_CONFIRM_RIDE = ConversationPoint.build(context, TAG, 0, 0, 0, 0, R.array.tts_confirm_save_verbose, R.array.tts_confirm_save_verbose);
        this.TTS_CONFIRM_RUN = ConversationPoint.build(context, TAG, 0, 0, 0, 0, R.array.tts_confirm_save_verbose_run, R.array.tts_confirm_save_verbose_run);
    }

    @Override // com.kopin.pupil.aria.app.AppState
    protected ConversationPoint getOnEnterSpeech() {
        switch (LiveRide.getCurrentSport()) {
            case RIDE:
                return this.TTS_CONFIRM_RIDE;
            case RUN:
                return this.TTS_CONFIRM_RUN;
            default:
                return super.getOnEnterSpeech();
        }
    }

    @Override // com.kopin.pupil.aria.app.AppState
    public boolean onCommandResult(String cmd) {
        for (String t : CommonGrammar.NO_CANCEL) {
            if (cmd.contains(t)) {
                RideControl.discardWorkout();
            }
        }
        for (String t2 : CommonGrammar.YES_OK) {
            if (cmd.contains(t2)) {
                RideControl.saveWorkout(true);
            }
        }
        RideControl.reset();
        setAppState(null);
        return false;
    }
}
