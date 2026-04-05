package com.digits.sdk.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.widget.EditText;
import java.lang.ref.WeakReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes18.dex */
public class SmsBroadcastReceiver extends BroadcastReceiver {
    static final String PDU_EXTRA = "pdus";
    final WeakReference<EditText> editTextWeakReference;
    final Pattern patternConfirmationCode = Pattern.compile("\\s(\\d{6}).*Digits by Twitter");

    SmsBroadcastReceiver(EditText editText) {
        this.editTextWeakReference = new WeakReference<>(editText);
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        EditText editText;
        SmsMessage[] messages = getMessagesFromIntent(intent);
        String confirmationCode = getConfirmationCode(messages);
        if (confirmationCode != null && (editText = this.editTextWeakReference.get()) != null) {
            editText.setText(confirmationCode);
            editText.setSelection(confirmationCode.length());
        }
    }

    String getConfirmationCode(SmsMessage[] messages) {
        for (SmsMessage message : messages) {
            String result = getConfirmationCode(message);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    String getConfirmationCode(SmsMessage message) {
        String body = message.getDisplayMessageBody();
        if (body != null) {
            Matcher matcher = this.patternConfirmationCode.matcher(body);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        return null;
    }

    SmsMessage[] getMessagesFromIntent(Intent intent) {
        Object[] messages = (Object[]) intent.getSerializableExtra(PDU_EXTRA);
        int pduCount = messages.length;
        SmsMessage[] msgs = new SmsMessage[pduCount];
        for (int i = 0; i < pduCount; i++) {
            msgs[i] = SmsMessage.createFromPdu((byte[]) messages[i]);
        }
        return msgs;
    }
}
