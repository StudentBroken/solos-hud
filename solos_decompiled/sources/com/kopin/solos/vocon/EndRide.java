package com.kopin.solos.vocon;

import android.content.Context;
import android.content.res.Resources;
import com.kopin.pupil.aria.app.CommonGrammar;
import com.kopin.pupil.aria.app.TimedAppState;
import com.kopin.pupil.aria.tts.ConversationPoint;
import com.kopin.solos.AppService;
import com.kopin.solos.RideControl;
import com.kopin.solos.core.R;
import com.kopin.solos.storage.LiveRide;

/* JADX INFO: loaded from: classes37.dex */
public class EndRide extends TimedAppState {
    private static final String TAG = "EndRide";
    private final ConversationPoint TTS_CONFIRM_RIDE;
    private final ConversationPoint TTS_CONFIRM_RUN;

    public EndRide(Context context, Resources resources) {
        super(TAG, CommonGrammar.YES_NO_OK_CANCEL, "inride");
        this.TTS_CONFIRM_RIDE = ConversationPoint.build(context, TAG, 0, 0, 0, 0, R.array.tts_confirm_end_verbose, R.array.tts_confirm_end_verbose);
        this.TTS_CONFIRM_RUN = ConversationPoint.build(context, TAG, 0, 0, 0, 0, R.array.tts_confirm_end_verbose_run, R.array.tts_confirm_end_verbose_run);
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

    @Override // com.kopin.pupil.aria.app.TimedAppState
    protected boolean onTimedOut() {
        if (LiveRide.isPaused()) {
            setAppState(AppService.PAUSED_PAGE);
            return true;
        }
        setAppState("inride");
        return true;
    }

    @Override // com.kopin.pupil.aria.app.AppState
    public boolean onCommandResult(String cmd) {
        for (String t : CommonGrammar.YES_OK) {
            if (cmd.contains(t)) {
                RideControl.stop(LiveRide.isActiveFtp());
                return false;
            }
        }
        if (LiveRide.isPaused()) {
            setAppState(AppService.PAUSED_PAGE);
        } else {
            setAppState("inride");
        }
        return true;
    }
}
