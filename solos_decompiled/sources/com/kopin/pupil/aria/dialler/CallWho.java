package com.kopin.pupil.aria.dialler;

import android.content.Context;
import com.kopin.pupil.aria.contacts.ContactResolver;
import com.kopin.pupil.aria.contacts.GetToWho;
import com.kopin.pupil.aria.phone.R;
import com.kopin.pupil.aria.tts.ConversationPoint;

/* JADX INFO: loaded from: classes20.dex */
public class CallWho extends GetToWho {
    private static final String CALLWHO_ID = "CallWho";
    private final ConversationPoint TTS;

    public CallWho(Context context) {
        super(context, CALLWHO_ID);
        this.TTS = ConversationPoint.build(context, CALLWHO_ID, 0, R.array.tts_dialler_callwho_silent, R.array.tts_dialler_callwho_once, R.array.tts_dialler_callwho_terse, R.array.tts_dialler_callwho_verbose, R.array.tts_dialler_callwho_verbose);
    }

    @Override // com.kopin.pupil.aria.app.AppState
    protected ConversationPoint getOnEnterSpeech() {
        return this.TTS;
    }

    @Override // com.kopin.pupil.aria.contacts.ContactResolver.OnResolvedListener
    public void onNumberResolved(ContactResolver.ResolvedContact contact) {
        if (contact == null) {
            appFinished();
            return;
        }
        Dialler.NAME_TO_CALL = contact.getDisplayName();
        Dialler.NUMBER_TO_CALL = contact.getNumberToUse();
        setAppState("confirm");
    }
}
