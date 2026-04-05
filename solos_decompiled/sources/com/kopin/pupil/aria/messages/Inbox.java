package com.kopin.pupil.aria.messages;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import com.digits.sdk.vcard.VCardConstants;
import com.kopin.pupil.aria.app.AppState;
import com.kopin.pupil.aria.tts.ConversationPoint;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes55.dex */
public class Inbox extends AppState {
    private static final String INBOX_ID = "ReadMessage";
    private final String[] NEXT_COMMANDS;
    private final String[] READ_COMMANDS;
    private final String[] REPLY_COMMANDS;
    private final ConversationPoint TTS_COUNT;
    private final ConversationPoint TTS_NEW_MESSAGE;
    private final ConversationPoint TTS_NO_MORE;
    private final ConversationPoint TTS_READ_MESSAGE;
    private boolean mClearOnExit;
    private final ContentResolver mContentResolver;
    private final Context mContext;
    private final BroadcastReceiver mDebugReceiver;
    private boolean mListMode;
    private ArrayList<QueuedMessage> mQueuedMessages;
    private boolean mReadOrReply;
    private final BroadcastReceiver mSmsReceiver;

    public Inbox(Context context) {
        super(INBOX_ID, Messages.INBOX_GRAMMAR);
        this.mQueuedMessages = new ArrayList<>();
        this.mSmsReceiver = new BroadcastReceiver() { // from class: com.kopin.pupil.aria.messages.Inbox.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                Bundle extras;
                if (intent != null && (extras = intent.getExtras()) != null) {
                    Object[] pdus = (Object[]) extras.get("pdus");
                    StringBuilder message = new StringBuilder();
                    String phoneNumber = null;
                    for (Object obj : pdus) {
                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) obj);
                        if (phoneNumber != null && !currentMessage.getDisplayOriginatingAddress().contentEquals(phoneNumber) && message.length() != 0) {
                            Log.i(Inbox.INBOX_ID, "New message from: " + phoneNumber + "; " + ((Object) message));
                            Inbox.this.onNewMessage(phoneNumber, message.toString());
                            message = new StringBuilder();
                        }
                        phoneNumber = currentMessage.getDisplayOriginatingAddress();
                        message.append(currentMessage.getDisplayMessageBody());
                    }
                    if (phoneNumber != null && message.length() != 0) {
                        Log.i(Inbox.INBOX_ID, "New message from: " + phoneNumber + "; " + ((Object) message));
                        Inbox.this.onNewMessage(phoneNumber, message.toString());
                    }
                }
            }
        };
        this.mDebugReceiver = new BroadcastReceiver() { // from class: com.kopin.pupil.aria.messages.Inbox.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String from = intent.getStringExtra("FROM");
                String msg = intent.getStringExtra(VCardConstants.PARAM_TYPE_MSG);
                Inbox.this.onNewMessage(from, msg);
            }
        };
        this.mContext = context;
        this.mContentResolver = context.getContentResolver();
        this.TTS_NEW_MESSAGE = ConversationPoint.build(context, INBOX_ID, 0, R.array.tts_messages_inbox_silent, R.array.tts_messages_inbox_once, R.array.tts_messages_inbox_terse, R.array.tts_messages_inbox_verbose, R.array.tts_messages_inbox_verbose);
        this.TTS_COUNT = ConversationPoint.build(context, INBOX_ID, 0, 0, 0, 0, R.array.tts_messages_count_verbose, R.array.tts_messages_count_verbose);
        this.TTS_READ_MESSAGE = ConversationPoint.build(context, INBOX_ID, 0, R.array.tts_messages_read_silent, R.array.tts_messages_read_once, R.array.tts_messages_read_terse, R.array.tts_messages_read_verbose, R.array.tts_messages_read_verbose);
        this.TTS_NO_MORE = ConversationPoint.build(context, INBOX_ID, 0, 0, 0, 0, R.array.tts_messages_inbox_empty_verbose, R.array.tts_messages_inbox_empty_verbose);
        context.registerReceiver(this.mSmsReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
        context.registerReceiver(this.mDebugReceiver, new IntentFilter("aria.debug.sms"));
        this.READ_COMMANDS = context.getResources().getStringArray(R.array.inbox_read);
        this.REPLY_COMMANDS = context.getResources().getStringArray(R.array.inbox_reply);
        this.NEXT_COMMANDS = context.getResources().getStringArray(R.array.inbox_next);
    }

    protected void finalize() throws Throwable {
        this.mContext.unregisterReceiver(this.mSmsReceiver);
        super.finalize();
    }

    @Override // com.kopin.pupil.aria.app.AppState
    protected void onEnter() {
        super.onEnter();
        this.mClearOnExit = true;
    }

    @Override // com.kopin.pupil.aria.app.AppState
    protected void onExit() {
        super.onExit();
        if (this.mClearOnExit) {
            this.mQueuedMessages.clear();
        }
    }

    @Override // com.kopin.pupil.aria.app.AppState
    protected ConversationPoint getOnEnterSpeech() {
        return this.mListMode ? this.TTS_COUNT : this.TTS_NEW_MESSAGE;
    }

    private void nextMessage(QueuedMessage qm) {
        Messages.findNumber(qm.from);
        Messages.SEND_MESSAGE = qm.msg;
        this.mReadOrReply = true;
    }

    void nextMessage() {
        QueuedMessage qm = this.mQueuedMessages.remove(0);
        if (qm != null) {
            nextMessage(qm);
        } else {
            Messages.SEND_MESSAGE = null;
        }
        this.mListMode = false;
    }

    boolean hasQueuedMessages() {
        return !this.mQueuedMessages.isEmpty();
    }

    int msgCount() {
        return this.mQueuedMessages.size();
    }

    void playQueuedMessages() {
        Messages.SEND_MESSAGE = null;
        this.mListMode = this.mQueuedMessages.size() > 3;
        if (!this.mListMode) {
            nextMessage();
        }
        requestState("inbox");
    }

    @Override // com.kopin.pupil.aria.app.AppState
    public boolean onCommandResult(String cmd) {
        if (this.mReadOrReply) {
            for (String t : this.READ_COMMANDS) {
                if (t.contentEquals(cmd)) {
                    if (Messages.SEND_MESSAGE != null) {
                        sayText(this.TTS_READ_MESSAGE);
                        this.mReadOrReply = false;
                        return true;
                    }
                    nextMessage();
                    sayText(this.TTS_NEW_MESSAGE);
                    return true;
                }
            }
        }
        if (!this.mReadOrReply) {
            for (String t2 : this.REPLY_COMMANDS) {
                if (t2.contentEquals(cmd)) {
                    this.mClearOnExit = false;
                    setAppState("message");
                    return true;
                }
            }
        }
        for (String t3 : this.NEXT_COMMANDS) {
            if (t3.contentEquals(cmd)) {
                if (this.mQueuedMessages.isEmpty()) {
                    sayText(this.TTS_NO_MORE);
                    return false;
                }
                nextMessage();
                sayText(this.TTS_NEW_MESSAGE);
                return true;
            }
        }
        if (this.mListMode || !hasQueuedMessages()) {
            return false;
        }
        nextMessage();
        sayText(this.TTS_NEW_MESSAGE);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onNewMessage(String from, String msg) {
        if (!Messages.shouldIgnore()) {
            QueuedMessage qm = new QueuedMessage(from, msg);
            if (Messages.shouldQueue()) {
                this.mQueuedMessages.add(qm);
            } else {
                nextMessage(qm);
                requestState("inbox");
            }
        }
    }

    private static class QueuedMessage {
        String from;
        String msg;

        public QueuedMessage(String f, String m) {
            this.from = f;
            this.msg = m;
        }
    }
}
