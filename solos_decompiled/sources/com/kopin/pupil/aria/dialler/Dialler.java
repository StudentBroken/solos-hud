package com.kopin.pupil.aria.dialler;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.kopin.pupil.PupilDevice;
import com.kopin.pupil.PupilSpeechRecognizer;
import com.kopin.pupil.aria.app.BaseAriaApp;
import com.kopin.pupil.aria.app.HeadCommand;
import com.kopin.pupil.aria.contacts.ContactResolver;
import com.kopin.pupil.aria.contacts.ContactsTTS;
import com.kopin.pupil.aria.phone.R;
import com.kopin.pupil.aria.tts.AriaTTS;
import com.kopin.solos.wear.WatchModeActivity;

/* JADX INFO: loaded from: classes20.dex */
public class Dialler extends BaseAriaApp {
    public static final String APP_NAME = "dialler";
    private static final String DIALLER_ID = "DialNumber";
    private static String[] DIALLER_KEYWORDS;
    private static String[] DIALLER_PREFIXES;
    static String[] INCALL_GRAMMAR;
    static String NAME_CALLING;
    static String NAME_TO_CALL;
    static String NUMBER_CALLING;
    static String NUMBER_TO_CALL;
    static String[] RINGING_GRAMMAR;
    private final HeadCommand HEAD_COMMAND;
    private final PhoneStateListener mPhoneListener;
    private TelephonyManager mTelMan;

    public Dialler(Context context, BaseAriaApp.AriaAppHost appHost, PupilSpeechRecognizer.SpeechConfig speechHost) {
        super(DIALLER_ID, context.getResources().getStringArray(R.array.dialler_grammar), appHost, speechHost);
        this.mPhoneListener = new PhoneStateListener() { // from class: com.kopin.pupil.aria.dialler.Dialler.1
            @Override // android.telephony.PhoneStateListener
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    case 0:
                        Dialler.NUMBER_CALLING = null;
                        Dialler.NAME_CALLING = null;
                        if (!Dialler.this.getStatus().contentEquals("idle")) {
                            Dialler.this.appFinished();
                        }
                        break;
                    case 1:
                        Dialler.NUMBER_CALLING = incomingNumber;
                        Dialler.NAME_CALLING = ContactResolver.getNameFor(incomingNumber);
                        if (Dialler.NUMBER_CALLING == null) {
                            Dialler.NUMBER_CALLING = incomingNumber;
                        }
                        Dialler.this.requestState("ringing");
                        break;
                }
            }
        };
        this.HEAD_COMMAND = new HeadCommand(context.getString(R.string.dialler_head));
        DIALLER_PREFIXES = context.getResources().getStringArray(R.array.dialler_prefix);
        DIALLER_KEYWORDS = context.getResources().getStringArray(R.array.dialler_keywords);
        INCALL_GRAMMAR = context.getResources().getStringArray(R.array.incall_grammar);
        RINGING_GRAMMAR = context.getResources().getStringArray(R.array.ringing_grammar);
        addState(ContactResolver.CONTACTS_GET_TO_WHO, new CallWho(context));
        addState("confirm", new Confirm(context));
        addState("incall", new InCall(context));
        addState("ringing", new IncomingCall(context));
        this.mTelMan = (TelephonyManager) context.getSystemService(WatchModeActivity.MODE_PHONE);
        this.mTelMan.listen(this.mPhoneListener, 32);
    }

    protected void finalize() throws Throwable {
        this.mTelMan.listen(this.mPhoneListener, 0);
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp, com.kopin.pupil.aria.app.AppState
    public boolean holdIdle() {
        return super.holdIdle() || this.mTelMan.getCallState() == 2;
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp
    public void onIdle() {
        if (this.mTelMan.getCallState() == 2) {
            requestState("incall");
        }
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp
    public boolean onButtonPress(boolean isShort) {
        if (this.mTelMan.getCallState() == 2) {
            Log.d(APP_NAME, "In a call, button press ends.");
            PupilDevice.sendEndCallPacket();
            appFinished();
        } else if (this.mTelMan.getCallState() == 1) {
            Log.d(APP_NAME, "Phone ringing, " + (isShort ? "short press answers." : "long press rejects."));
            if (isShort) {
                PupilDevice.sendAnswerCallPacket();
            } else {
                PupilDevice.sendEndCallPacket();
                appFinished();
            }
        } else {
            return super.onButtonPress(isShort);
        }
        return true;
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp
    public void onStart() {
        super.onStart();
        NAME_TO_CALL = null;
        NUMBER_TO_CALL = null;
        setAppState(ContactResolver.CONTACTS_GET_TO_WHO);
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp
    public HeadCommand getHeadCommand() {
        return this.HEAD_COMMAND;
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp
    public String[] getKeywords() {
        return DIALLER_KEYWORDS;
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp
    public boolean needsToWho() {
        return NUMBER_TO_CALL == null;
    }

    static void findContact(String text) {
        for (String key : DIALLER_KEYWORDS) {
            if (text.startsWith(key)) {
                NAME_TO_CALL = text.substring(key.length()).trim();
            }
        }
        if (NAME_TO_CALL == null) {
            NAME_TO_CALL = text;
        }
        NUMBER_TO_CALL = ContactResolver.getNumberFor(NAME_TO_CALL);
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp, com.kopin.pupil.aria.tts.AriaTTS.TTSListener
    public String getSpeechSubstitution(String tag, String subs) {
        if (subs.contentEquals("NAME")) {
            return NAME_TO_CALL;
        }
        if (subs.contentEquals("NUMBER")) {
            return NUMBER_TO_CALL;
        }
        if (subs.contentEquals("CALLER")) {
            return NAME_CALLING == null ? NUMBER_CALLING : NAME_CALLING;
        }
        return super.getSpeechSubstitution(tag, subs);
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp, com.kopin.pupil.aria.app.AppState
    public boolean onCommandResult(String cmd) {
        for (String t : DIALLER_PREFIXES) {
            if (cmd.contentEquals(t)) {
                setAppState(ContactResolver.CONTACTS_GET_TO_WHO);
                return true;
            }
        }
        findContact(cmd);
        if (NAME_TO_CALL == null) {
            sayText(AriaTTS.SayPriority.HINT, ContactsTTS.TTS_NO_NUMBER);
            setAppState(ContactResolver.CONTACTS_GET_TO_WHO);
            return true;
        }
        if (NUMBER_TO_CALL != null) {
            setAppState("confirm");
            return true;
        }
        sayText(AriaTTS.SayPriority.VERBOSE, ContactsTTS.TTS_NO_NUMBER);
        return false;
    }
}
