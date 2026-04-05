package com.kopin.pupil.aria.messages;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import com.facebook.share.internal.ShareConstants;
import com.kopin.pupil.aria.app.CommonGrammar;
import com.kopin.pupil.aria.app.TimedAppState;
import com.kopin.pupil.aria.tts.ConversationPoint;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes55.dex */
public class Confirm extends TimedAppState {
    private static final String CONFIRM_ID = "SendConfirm";
    private final ConversationPoint TTS;
    private final ConversationPoint TTS_SENT;
    private final ConversationPoint TTS_SMS_PERMISSION;
    private ContentResolver mContentResolver;
    private final Context mContext;
    private SmsManager mSmsManager;

    public Confirm(Context context) {
        super(CONFIRM_ID, CommonGrammar.YES_NO_OK_CANCEL, null);
        this.mContext = context;
        this.mContentResolver = context.getContentResolver();
        this.mSmsManager = SmsManager.getDefault();
        this.TTS = ConversationPoint.build(context, CONFIRM_ID, 0, R.array.tts_messages_confirm_silent, R.array.tts_messages_confirm_once, R.array.tts_messages_confirm_terse, R.array.tts_messages_confirm_verbose, R.array.tts_messages_confirm_verbose);
        this.TTS_SENT = ConversationPoint.build(context, CONFIRM_ID, 0, 0, 0, 0, R.array.tts_messages_sent_verbose, R.array.tts_messages_sent_verbose);
        this.TTS_SMS_PERMISSION = ConversationPoint.build(context, CONFIRM_ID, 0, 0, 0, 0, R.array.tts_messages_send_permission, R.array.tts_messages_send_permission);
    }

    @Override // com.kopin.pupil.aria.app.AppState
    protected ConversationPoint getOnEnterSpeech() {
        return this.TTS;
    }

    @Override // com.kopin.pupil.aria.app.AppState
    public boolean onCommandResult(String cmd) {
        for (String t : CommonGrammar.YES_OK) {
            if (cmd.contentEquals(t)) {
                sendSMS(Messages.SEND_NUMBER, Messages.SEND_MESSAGE);
            }
        }
        return false;
    }

    private boolean sendSMS(String to, String message) {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this.mContext, "android.permission.SEND_SMS") != 0) {
            sayText(this.TTS_SMS_PERMISSION);
            return false;
        }
        ContentValues values = new ContentValues(3);
        values.put("address", to);
        values.put("date", String.valueOf(System.currentTimeMillis()));
        values.put("read", (Integer) 1);
        values.put("status", (Integer) (-1));
        values.put(ShareConstants.MEDIA_TYPE, (Integer) 2);
        values.put("body", message);
        Uri uri = Uri.parse("content://sms/outbox");
        this.mContentResolver.insert(uri, values);
        if (message.length() > 140) {
            ArrayList<String> messageParts = this.mSmsManager.divideMessage(message);
            this.mSmsManager.sendMultipartTextMessage(to, null, messageParts, null, null);
        } else {
            this.mSmsManager.sendTextMessage(to, null, message, null, null);
        }
        sayText(this.TTS_SENT);
        return true;
    }
}
