package com.kopin.pupil.aria.contacts;

import android.content.Context;
import com.kopin.pupil.aria.R;
import com.kopin.pupil.aria.app.AppState;
import com.kopin.pupil.aria.app.CommonGrammar;
import com.kopin.pupil.aria.contacts.ContactResolver;
import com.kopin.pupil.aria.tts.ConversationPoint;
import java.util.ArrayList;
import java.util.Iterator;

/* JADX INFO: loaded from: classes43.dex */
public class SelectWho extends AppState {
    private static final String TAG = "SelectWho";
    private final ConversationPoint TTS_2_NAMES;
    private final ConversationPoint TTS_3_NAMES;
    private final ConversationPoint TTS_NO_NAME;
    private final ConversationPoint TTS_TOO_MANY;

    public SelectWho(Context context) {
        super(TAG, new String[]{"<SelectWho>"});
        this.TTS_2_NAMES = ConversationPoint.build(context, TAG, 0, R.array.tts_clarify2_silent, R.array.tts_clarify2_once, R.array.tts_clarify2_terse, R.array.tts_clarify2_verbose, R.array.tts_clarify2_verbose);
        this.TTS_3_NAMES = ConversationPoint.build(context, TAG, 0, R.array.tts_clarify3_silent, R.array.tts_clarify3_once, R.array.tts_clarify3_terse, R.array.tts_clarify3_verbose, R.array.tts_clarify3_verbose);
        this.TTS_TOO_MANY = ConversationPoint.build(context, TAG, 0, R.array.tts_too_many_names_silent, R.array.tts_too_many_names_once, R.array.tts_too_many_names_terse, R.array.tts_too_many_names_verbose, R.array.tts_too_many_names_verbose);
        this.TTS_NO_NAME = ConversationPoint.build(context, TAG, 0, R.array.tts_no_name_matches_silent, R.array.tts_no_name_matches_once, R.array.tts_no_name_matches_terse, R.array.tts_no_name_matches_verbose, R.array.tts_no_name_matches_verbose);
    }

    @Override // com.kopin.pupil.aria.app.AppState
    protected void onEnter() {
        super.onEnter();
        if (!Contacts.RESOLVED_CONTACT.isEmpty()) {
            addDefinition("Name1", Contacts.RESOLVED_CONTACT.get(0).getUniqueNameParts());
        } else {
            addDefinition("Name1", new String[]{"<VOID>"});
        }
        if (Contacts.RESOLVED_CONTACT.size() > 1) {
            addDefinition("Name2", Contacts.RESOLVED_CONTACT.get(1).getUniqueNameParts());
        } else {
            addDefinition("Name2", new String[]{"<VOID>"});
        }
        if (Contacts.RESOLVED_CONTACT.size() > 2) {
            addDefinition("Name3", Contacts.RESOLVED_CONTACT.get(2).getUniqueNameParts());
        } else {
            addDefinition("Name3", new String[]{"<VOID>"});
        }
        addDefinition(TAG, new String[]{"<Name1>", "<Name2>", "<Name3>", "<CancelRetry>"});
        addDefinition("CancelRetry", CommonGrammar.NO_CANCEL_RETRY);
    }

    @Override // com.kopin.pupil.aria.app.AppState
    protected void onExit() {
        super.onExit();
        clearDefinitions();
    }

    @Override // com.kopin.pupil.aria.app.AppState
    protected ConversationPoint getOnEnterSpeech() {
        if (Contacts.RESOLVED_CONTACT.size() == 2) {
            return this.TTS_2_NAMES;
        }
        if (Contacts.RESOLVED_CONTACT.size() == 3) {
            return this.TTS_3_NAMES;
        }
        return this.TTS_TOO_MANY;
    }

    @Override // com.kopin.pupil.aria.app.AppState
    public boolean onCommandResult(String cmd) {
        for (String t : CommonGrammar.NO_CANCEL) {
            if (cmd.contentEquals(t)) {
                Contacts.RESOLVED_CONTACT.clear();
                appFinished();
                return false;
            }
        }
        for (String t2 : CommonGrammar.RETRY_TRY_AGAIN) {
            if (cmd.contentEquals(t2)) {
                Contacts.RESOLVED_CONTACT.clear();
                setAppState(ContactResolver.CONTACTS_GET_TO_WHO);
                return true;
            }
        }
        ArrayList<ContactResolver.ResolvedContact> removeList = new ArrayList<>();
        for (ContactResolver.ResolvedContact c : Contacts.RESOLVED_CONTACT) {
            boolean nomatch = true;
            for (String p : c.getUniqueNameParts()) {
                if (p.contentEquals(cmd)) {
                    nomatch = false;
                }
            }
            if (nomatch) {
                removeList.add(c);
            }
        }
        Iterator<ContactResolver.ResolvedContact> it = removeList.iterator();
        while (it.hasNext()) {
            Contacts.RESOLVED_CONTACT.remove(it.next());
        }
        if (Contacts.RESOLVED_CONTACT.isEmpty()) {
            sayText(this.TTS_NO_NAME);
            appFinished();
            return false;
        }
        if (Contacts.RESOLVED_CONTACT.size() > 1) {
            sayText(this.TTS_TOO_MANY);
            return false;
        }
        if (Contacts.RESOLVED_CONTACT.get(0).hasAlternates()) {
            Contacts.CHOSEN_ALTERNATE = 0;
            setAppState("pick");
            return true;
        }
        appFinished();
        return false;
    }
}
