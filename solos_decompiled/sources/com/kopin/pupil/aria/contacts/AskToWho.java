package com.kopin.pupil.aria.contacts;

import android.content.Context;
import android.util.Log;
import com.goldeni.audio.ASR_RESULTS;
import com.kopin.pupil.PupilSpeechRecognizer;
import com.kopin.pupil.aria.R;
import com.kopin.pupil.aria.app.AppState;
import com.kopin.pupil.aria.app.CommonGrammar;
import com.kopin.pupil.aria.contacts.ContactResolver;
import com.kopin.pupil.aria.tts.AriaTTS;
import com.kopin.pupil.aria.tts.ConversationPoint;
import java.util.ArrayList;
import java.util.Iterator;

/* JADX INFO: loaded from: classes43.dex */
public class AskToWho extends AppState {
    private static final String[] TOWHO_GRAMMAR = {"<ToWho>", "<Cancel>"};
    private static final String TOWHO_ID = "AskToWho";
    private final ConversationPoint TTS_NO_NAME;
    private final ConversationPoint TTS_TOO_MANY;

    public AskToWho(Context context) {
        super(TOWHO_ID, TOWHO_GRAMMAR);
        this.TTS_NO_NAME = ConversationPoint.build(context, TOWHO_ID, 0, R.array.tts_nonumber_silent, R.array.tts_nonumber_once, R.array.tts_nonumber_terse, R.array.tts_nonumber_verbose, R.array.tts_nonumber_verbose);
        this.TTS_TOO_MANY = ConversationPoint.build(context, TOWHO_ID, 0, R.array.tts_too_many_names_silent, R.array.tts_too_many_names_once, R.array.tts_too_many_names_terse, R.array.tts_too_many_names_verbose, R.array.tts_too_many_names_verbose);
        addDefinition("Cancel", CommonGrammar.NO_CANCEL);
    }

    @Override // com.kopin.pupil.aria.app.AppState
    protected void onEnter() {
        Contacts.RESOLVED_CONTACT.clear();
    }

    @Override // com.kopin.pupil.aria.app.AppState
    public boolean onASRWarn(PupilSpeechRecognizer.ASRWarnings code) {
        Log.d(TOWHO_ID, " onASRWarn");
        return super.onASRWarn(code);
    }

    @Override // com.kopin.pupil.aria.app.AppState
    public void onSilence() {
        Log.d(TOWHO_ID, " onSilence");
        super.onSilence();
    }

    @Override // com.kopin.pupil.aria.app.AppState
    public boolean onCommandResult(String cmd) {
        int numbers;
        Contacts.NAME_HEARD = cmd;
        for (String t : CommonGrammar.NO_CANCEL) {
            if (cmd.contentEquals(t)) {
                Contacts.RESOLVED_CONTACT.clear();
                appFinished();
                return false;
            }
        }
        ASR_RESULTS results = getFullResults();
        if (results != null) {
            String[] hits = results.getLocalPhrase();
            int[] conf = results.getLocalConfidence();
            int lastConf = conf[0];
            int i = 0;
            while (true) {
                if (i >= hits.length) {
                    break;
                }
                int maxDiff = lastConf / 6;
                if (conf[i] < lastConf - maxDiff) {
                    Log.d(TOWHO_ID, "ASR confidence below threshold, ignoring remaining results");
                    break;
                }
                ContactsCache.getNumbersFor(hits[i], Contacts.RESOLVED_CONTACT);
                lastConf = conf[i];
                i++;
            }
        } else {
            ContactsCache.getNumbersFor(cmd, Contacts.RESOLVED_CONTACT);
        }
        for (ContactResolver.ResolvedContact c : Contacts.RESOLVED_CONTACT) {
            StringBuilder sb = new StringBuilder("Contact: ");
            if (c.isNumberOnly()) {
                sb.append(c.getNumberToUse()).append(" (number)");
            } else if (!c.hasAlternates()) {
                sb.append(c.getDisplayName()).append(" - ").append(c.getNumberToUse());
            } else {
                sb.append(c.getDisplayName()).append(" - ");
                for (String alt : c.getAlternates()) {
                    sb.append(alt).append(",");
                }
            }
            Log.d(TOWHO_ID, sb.toString());
        }
        if (Contacts.RESOLVED_CONTACT.isEmpty()) {
            sayText(this.TTS_NO_NAME);
            appFinished();
            return false;
        }
        if (Contacts.RESOLVED_CONTACT.size() > 1) {
            if (!Contacts.RESOLVED_CONTACT.get(0).isNumberOnly()) {
                ArrayList<ContactResolver.ResolvedContact> removeList = new ArrayList<>();
                for (ContactResolver.ResolvedContact c2 : Contacts.RESOLVED_CONTACT) {
                    if (c2.isNumberOnly()) {
                        removeList.add(c2);
                    }
                }
                Iterator<ContactResolver.ResolvedContact> it = removeList.iterator();
                while (it.hasNext()) {
                    Contacts.RESOLVED_CONTACT.remove(it.next());
                }
            } else {
                int numbers2 = 1;
                if (Contacts.RESOLVED_CONTACT.size() > 1 && Contacts.RESOLVED_CONTACT.get(1).isNumberOnly()) {
                    numbers2 = 1 + 1;
                }
                if (Contacts.RESOLVED_CONTACT.size() > 2 && Contacts.RESOLVED_CONTACT.get(2).isNumberOnly()) {
                    numbers2++;
                }
                String[] altNumbers = new String[numbers2];
                int numbers3 = 0 + 1;
                altNumbers[0] = Contacts.RESOLVED_CONTACT.get(0).getNumberToUse();
                if (Contacts.RESOLVED_CONTACT.size() <= 1 || !Contacts.RESOLVED_CONTACT.get(1).isNumberOnly()) {
                    numbers = numbers3;
                } else {
                    numbers = numbers3 + 1;
                    altNumbers[numbers3] = Contacts.RESOLVED_CONTACT.get(1).getNumberToUse();
                }
                if (Contacts.RESOLVED_CONTACT.size() > 2 && Contacts.RESOLVED_CONTACT.get(2).isNumberOnly()) {
                    int i2 = numbers + 1;
                    altNumbers[numbers] = Contacts.RESOLVED_CONTACT.get(2).getNumberToUse();
                }
                Contacts.RESOLVED_CONTACT.clear();
                Contacts.RESOLVED_CONTACT.add(new ContactResolver.ResolvedContact(Contacts.NAME_HEARD, null, null, altNumbers));
            }
        }
        if (Contacts.RESOLVED_CONTACT.size() > 1) {
            if (Contacts.RESOLVED_CONTACT.size() < 4) {
                setAppState("which");
                return true;
            }
            sayText(AriaTTS.SayPriority.VERBOSE, this.TTS_TOO_MANY);
            Contacts.RESOLVED_CONTACT.clear();
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
