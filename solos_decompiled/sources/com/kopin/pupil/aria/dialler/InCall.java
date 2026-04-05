package com.kopin.pupil.aria.dialler;

import android.content.Context;
import com.kopin.pupil.PupilDevice;
import com.kopin.pupil.aria.app.AppState;

/* JADX INFO: loaded from: classes20.dex */
public class InCall extends AppState {
    private static final String CONFIRM_ID = "InCall";

    public InCall(Context context) {
        super(CONFIRM_ID, Dialler.INCALL_GRAMMAR);
    }

    @Override // com.kopin.pupil.aria.app.AppState
    protected void onEnter() {
        super.onEnter();
    }

    @Override // com.kopin.pupil.aria.app.AppState
    public boolean onCommandResult(String cmd) {
        PupilDevice.sendEndCallPacket();
        return false;
    }
}
