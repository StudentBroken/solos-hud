package com.kopin.pupil.aria.messages;

import android.content.Context;
import com.kopin.pupil.aria.contacts.ContactResolver;
import com.kopin.pupil.aria.contacts.GetToWho;
import com.kopin.pupil.aria.tts.ConversationPoint;

/* JADX INFO: loaded from: classes55.dex */
public class SendToWho extends GetToWho {
    private static final String TOWHO_ID = "SendToWho";
    private final ConversationPoint ASK_TO_WHO;

    public SendToWho(Context context) {
        super(context, TOWHO_ID);
        this.ASK_TO_WHO = ConversationPoint.build(context, TOWHO_ID, 0, R.array.tts_messages_sendtowho_silent, R.array.tts_messages_sendtowho_once, R.array.tts_messages_sendtowho_terse, R.array.tts_messages_sendtowho_verbose, R.array.tts_messages_sendtowho_verbose);
    }

    @Override // com.kopin.pupil.aria.app.AppState
    protected ConversationPoint getOnEnterSpeech() {
        return this.ASK_TO_WHO;
    }

    @Override // com.kopin.pupil.aria.contacts.ContactResolver.OnResolvedListener
    public void onNumberResolved(ContactResolver.ResolvedContact contact) {
        if (contact == null) {
            appFinished();
            return;
        }
        Messages.SEND_WHO = contact.getDisplayName();
        Messages.SEND_NUMBER = contact.getNumberToUse();
        if (Messages.SEND_MESSAGE == null) {
            setAppState("message");
        } else {
            setAppState("confirm");
        }
    }
}
