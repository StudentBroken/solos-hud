package com.kopin.pupil.aria.dialler;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import com.facebook.share.internal.ShareConstants;
import com.kopin.pupil.PupilDevice;
import com.kopin.pupil.aria.app.AppState;
import com.kopin.pupil.aria.phone.R;
import com.kopin.pupil.aria.tts.ConversationPoint;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes20.dex */
public class IncomingCall extends AppState {
    private static final String CONFIRM_ID = "IncomingCall";
    private final String[] ANSWER_CALL;
    private final String[] BUSY_TEXT;
    private final ConversationPoint INCOMING_CALL;
    private ContentResolver mContentResolver;
    private Context mContext;
    private SmsManager mSmsManager;

    public IncomingCall(Context context) {
        super(CONFIRM_ID, Dialler.RINGING_GRAMMAR);
        this.mContext = context;
        this.INCOMING_CALL = ConversationPoint.build(context, CONFIRM_ID, 0, R.array.tts_dialler_incoming_silent, R.array.tts_dialler_incoming_once, R.array.tts_dialler_incoming_terse, R.array.tts_dialler_incoming_verbose, R.array.tts_dialler_incoming_verbose);
        this.ANSWER_CALL = context.getResources().getStringArray(R.array.dialler_take_call);
        this.BUSY_TEXT = context.getResources().getStringArray(R.array.dialler_busy_text);
        this.mContentResolver = context.getContentResolver();
        this.mSmsManager = SmsManager.getDefault();
    }

    @Override // com.kopin.pupil.aria.app.AppState
    protected ConversationPoint getOnEnterSpeech() {
        return this.INCOMING_CALL;
    }

    @Override // com.kopin.pupil.aria.app.AppState
    protected void onEnter() {
        super.onEnter();
    }

    @Override // com.kopin.pupil.aria.app.AppState
    public boolean onCommandResult(String cmd) {
        for (String str : this.ANSWER_CALL) {
            if (cmd.contentEquals(str)) {
                PupilDevice.sendAnswerCallPacket();
                setAppState("incall");
                return true;
            }
        }
        String[] strArr = this.BUSY_TEXT;
        int length = strArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            String t = strArr[i];
            if (!cmd.contentEquals(t)) {
                i++;
            } else {
                sendSMS(Dialler.NUMBER_CALLING, t);
                break;
            }
        }
        PupilDevice.sendEndCallPacket();
        return false;
    }

    private boolean sendSMS(String to, String message) {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this.mContext, "android.permission.SEND_SMS") != 0) {
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
        return true;
    }
}
