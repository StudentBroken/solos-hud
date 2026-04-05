package com.kopin.solos.vocon;

import android.content.res.Resources;
import com.kopin.pupil.aria.app.AppState;
import com.kopin.solos.RideControl;
import com.kopin.solos.common.SportType;
import com.kopin.solos.core.R;

/* JADX INFO: loaded from: classes37.dex */
public class PausedRide extends AppState {
    private static final String TAG = "PausedRide";
    private final String[] PAUSEDRIDE_GRAMMAR;
    private final String[] RESUME_PREFIXES;
    private final String[] STOP_PREFIXES;

    public PausedRide(Resources resources) {
        super(TAG, resources.getStringArray(R.array.inride_paused_grammar));
        this.PAUSEDRIDE_GRAMMAR = resources.getStringArray(R.array.inride_paused_grammar);
        this.RESUME_PREFIXES = resources.getStringArray(R.array.ride_resume_prefixes);
        this.STOP_PREFIXES = resources.getStringArray(R.array.ride_stop_prefixes);
    }

    @Override // com.kopin.pupil.aria.app.AppState
    protected void onEnter() {
        super.onEnter();
        clearDefinitions();
        addDefinition(TAG, this.PAUSEDRIDE_GRAMMAR);
        addDefinition(SportType.KEY, new String[]{SolosAriaApp.getSportName()});
    }

    @Override // com.kopin.pupil.aria.app.AppState
    public boolean holdIdle() {
        return false;
    }

    @Override // com.kopin.pupil.aria.app.AppState
    public boolean onCommandResult(String cmd) {
        for (String t : this.STOP_PREFIXES) {
            if (cmd.contains(t)) {
                setAppState("endride");
                return true;
            }
        }
        for (String t2 : this.RESUME_PREFIXES) {
            if (cmd.contains(t2)) {
                RideControl.requestResume();
                return true;
            }
        }
        return super.onCommandResult(cmd);
    }
}
