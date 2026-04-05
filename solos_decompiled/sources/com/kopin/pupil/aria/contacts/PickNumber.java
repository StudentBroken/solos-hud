package com.kopin.pupil.aria.contacts;

import android.content.Context;
import com.kopin.pupil.aria.R;
import com.kopin.pupil.aria.app.AppState;
import com.kopin.pupil.aria.app.CommonGrammar;
import com.kopin.pupil.aria.tts.AriaTTS;
import com.kopin.pupil.aria.tts.ConversationPoint;

/* JADX INFO: loaded from: classes43.dex */
public class PickNumber extends AppState {
    private static final String TAG = "PickNumber";
    private final ConversationPoint TTS_NO_MORE;
    private final ConversationPoint TTS_THIS_NUMBER;
    private final ConversationPoint TTS_TOO_MANY;

    public PickNumber(Context context) {
        super(TAG, CommonGrammar.YES_NO_OK_CANCEL);
        this.TTS_THIS_NUMBER = ConversationPoint.build(context, TAG, 0, R.array.tts_pick_number_silent, R.array.tts_pick_number_once, R.array.tts_pick_number_terse, R.array.tts_pick_number_verbose, R.array.tts_pick_number_verbose);
        this.TTS_TOO_MANY = ConversationPoint.build(context, TAG, 0, R.array.tts_too_many_numbers_silent, R.array.tts_too_many_numbers_once, R.array.tts_too_many_numbers_terse, R.array.tts_too_many_numbers_verbose, R.array.tts_too_many_numbers_verbose);
        this.TTS_NO_MORE = ConversationPoint.build(context, TAG, 0, R.array.tts_no_more_numbers_silent, R.array.tts_no_more_numbers_once, R.array.tts_no_more_numbers_terse, R.array.tts_no_more_numbers_verbose, R.array.tts_no_more_numbers_verbose);
    }

    @Override // com.kopin.pupil.aria.app.AppState
    protected ConversationPoint getOnEnterSpeech() {
        return Contacts.CHOSEN_ALTERNATE < 3 ? this.TTS_THIS_NUMBER : this.TTS_TOO_MANY;
    }

    @Override // com.kopin.pupil.aria.app.AppState
    public boolean onCommandResult(String cmd) {
        for (String t : CommonGrammar.YES_OK) {
            if (cmd.contentEquals(t)) {
                Contacts.RESOLVED_CONTACT.get(0).selectAlternate(Contacts.CHOSEN_ALTERNATE);
                appFinished();
                return false;
            }
        }
        Contacts.CHOSEN_ALTERNATE++;
        if (Contacts.CHOSEN_ALTERNATE < Contacts.RESOLVED_CONTACT.get(0).getAlternates().length) {
            setAppState("pick");
            return true;
        }
        sayText(AriaTTS.SayPriority.VERBOSE, this.TTS_NO_MORE);
        appFinished();
        return false;
    }
}
