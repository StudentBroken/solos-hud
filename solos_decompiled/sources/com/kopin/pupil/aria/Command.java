package com.kopin.pupil.aria;

import com.kopin.pupil.PupilSpeechRecognizer;
import com.kopin.pupil.vocon.Grammar;

/* JADX INFO: loaded from: classes43.dex */
public class Command extends Grammar implements PupilSpeechRecognizer.ResultListener {
    public Command(String cmd) {
        super(cmd, new String[]{cmd});
    }

    public Command(String id, String cmd) {
        super(id, new String[]{cmd});
    }

    public Command(String id, String[] cmds) {
        super(id, cmds);
    }

    @Override // com.kopin.pupil.PupilSpeechRecognizer.ResultListener
    public void onListening() {
    }

    @Override // com.kopin.pupil.PupilSpeechRecognizer.ResultListener
    public boolean onResult(String cmd, PupilSpeechRecognizer.SpeechConfig recognizer) {
        return false;
    }

    @Override // com.kopin.pupil.PupilSpeechRecognizer.ResultListener
    public void onWarning(PupilSpeechRecognizer.ASRWarnings code) {
    }

    @Override // com.kopin.pupil.PupilSpeechRecognizer.ResultListener
    public void onSilence() {
    }

    @Override // com.kopin.pupil.PupilSpeechRecognizer.ResultListener
    public void onError(PupilSpeechRecognizer.ASRErrors code) {
    }
}
