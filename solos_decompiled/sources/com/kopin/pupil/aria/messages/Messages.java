package com.kopin.pupil.aria.messages;

import android.content.Context;
import android.content.SharedPreferences;
import com.kopin.pupil.PupilSpeechRecognizer;
import com.kopin.pupil.aria.app.BaseAriaApp;
import com.kopin.pupil.aria.app.CommonTTS;
import com.kopin.pupil.aria.app.HeadCommand;
import com.kopin.pupil.aria.contacts.ContactResolver;
import com.kopin.pupil.aria.contacts.ContactsTTS;
import com.kopin.pupil.aria.tts.AriaTTS;

/* JADX INFO: loaded from: classes55.dex */
public class Messages extends BaseAriaApp {
    public static final String APP_NAME = "messages";
    static String[] INBOX_GRAMMAR = null;
    private static String[] MESSAGES_CANNED = null;
    private static final String MESSAGES_ID = "NewMessage";
    public static final String NOTIFY_PACKAGE = "com.android.mms";
    private static String[] PREFIXES = null;
    private static final String PREF_QUEUE_OR_IGNORE = "notify";
    static String SEND_MESSAGE;
    static String SEND_NUMBER;
    static String SEND_WHO;
    static NotifyAction mNotification = NotifyAction.PLAY;
    private static BaseAriaApp.AriaAppHost mParent;
    private static SharedPreferences mPrefs;
    private final HeadCommand HEAD_COMMAND;
    private final Inbox INBOX;
    private final String[] MESSAGES_COMMAND;
    private final String[] MESSAGES_KEYWORDS;

    public enum NotifyAction {
        PLAY,
        QUEUE,
        IGNORE
    }

    public Messages(Context context, BaseAriaApp.AriaAppHost appHost, PupilSpeechRecognizer.SpeechConfig speechHost) {
        super(MESSAGES_ID, context.getResources().getStringArray(R.array.messages_grammar), appHost, speechHost);
        this.HEAD_COMMAND = new HeadCommand(context.getString(R.string.messages_head));
        this.MESSAGES_KEYWORDS = context.getResources().getStringArray(R.array.messages_keywords);
        this.MESSAGES_COMMAND = context.getResources().getStringArray(R.array.messages_start);
        PREFIXES = context.getResources().getStringArray(R.array.messages_prefixes);
        MESSAGES_CANNED = context.getResources().getStringArray(R.array.messages_canned);
        INBOX_GRAMMAR = context.getResources().getStringArray(R.array.inbox_grammar);
        mPrefs = context.getSharedPreferences(MESSAGES_ID, 0);
        refreshPrefs();
        mParent = appHost;
        addState(ContactResolver.CONTACTS_GET_TO_WHO, new SendToWho(context));
        addState("message", new GetMessage(context, MESSAGES_CANNED));
        addState("confirm", new Confirm(context));
        addState("confirm_msg", new ConfirmMessage(context));
        this.INBOX = new Inbox(context);
        addState("inbox", this.INBOX);
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp
    public HeadCommand getHeadCommand() {
        return this.HEAD_COMMAND;
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp
    public String[] getKeywords() {
        return this.MESSAGES_KEYWORDS;
    }

    public static void setNotificationPreference(NotifyAction action) {
        if (mPrefs != null) {
            mPrefs.edit().putInt(PREF_QUEUE_OR_IGNORE, action.ordinal()).commit();
        }
        mNotification = action;
    }

    public static NotifyAction getNotificationPreference() {
        if (mPrefs == null) {
            return mNotification;
        }
        int ord = mPrefs.getInt(PREF_QUEUE_OR_IGNORE, 0);
        if (ord < NotifyAction.values().length) {
            return NotifyAction.values()[ord];
        }
        return NotifyAction.PLAY;
    }

    private void refreshPrefs() {
        mNotification = getNotificationPreference();
    }

    static boolean shouldIgnore() {
        return mNotification == NotifyAction.IGNORE;
    }

    static boolean shouldQueue() {
        return mNotification == NotifyAction.QUEUE && !mParent.isIdle();
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp
    public boolean needsToWho() {
        return SEND_NUMBER == null;
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp
    public void onIdle() {
        refreshPrefs();
        if (this.INBOX.hasQueuedMessages()) {
            this.INBOX.playQueuedMessages();
        }
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp
    public void onStart() {
        super.onStart();
        SEND_MESSAGE = null;
        SEND_NUMBER = null;
        SEND_WHO = null;
        setAppState(ContactResolver.CONTACTS_GET_TO_WHO);
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp, com.kopin.pupil.aria.tts.AriaTTS.TTSListener
    public String getSpeechSubstitution(String tag, String subs) {
        if (subs.contentEquals("NAME")) {
            return SEND_WHO;
        }
        if (subs.contentEquals("NUMBER")) {
            return SEND_NUMBER;
        }
        if (subs.contentEquals("MESSAGE")) {
            return SEND_MESSAGE;
        }
        if (subs.contentEquals("COUNT")) {
            return Integer.toString(this.INBOX.msgCount());
        }
        return super.getSpeechSubstitution(tag, subs);
    }

    static String findMessage(String text) {
        for (String msg : MESSAGES_CANNED) {
            if (text.endsWith(msg)) {
                SEND_MESSAGE = msg;
                text = text.substring(0, text.length() - msg.length()).trim();
            }
        }
        return text;
    }

    static void findContact(String text) {
        for (String key : PREFIXES) {
            if (text.startsWith(key)) {
                SEND_WHO = text.substring(key.length()).trim();
            }
        }
        if (SEND_WHO == null) {
            SEND_WHO = text;
        }
        SEND_NUMBER = ContactResolver.getNumberFor(SEND_WHO);
    }

    static void findNumber(String number) {
        SEND_NUMBER = number;
        SEND_WHO = ContactResolver.getNameFor(number);
        if (SEND_WHO == null) {
            SEND_WHO = SEND_NUMBER;
        }
    }

    @Override // com.kopin.pupil.aria.app.BaseAriaApp, com.kopin.pupil.aria.app.AppState
    public boolean onCommandResult(String cmd) {
        for (String c : this.MESSAGES_COMMAND) {
            if (c.contentEquals(cmd)) {
                setAppState(ContactResolver.CONTACTS_GET_TO_WHO);
                return true;
            }
        }
        findContact(findMessage(cmd));
        if (SEND_NUMBER == null) {
            sayText(AriaTTS.SayPriority.VERBOSE, ContactsTTS.TTS_NO_NUMBER);
            return false;
        }
        if (SEND_MESSAGE == null) {
            setAppState("message");
            return true;
        }
        if (SEND_NUMBER != null) {
            setAppState("confirm");
            return true;
        }
        sayText(AriaTTS.SayPriority.VERBOSE, CommonTTS.SORRY_DIDNT_UNDERSTAND);
        return false;
    }
}
