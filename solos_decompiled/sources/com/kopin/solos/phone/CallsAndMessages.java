package com.kopin.solos.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.kopin.accessory.packets.ButtonType;
import com.kopin.pupil.PupilDevice;
import com.kopin.pupil.aria.Prefs;
import com.kopin.pupil.aria.contacts.ContactResolver;
import com.kopin.pupil.ui.VCNotification;
import com.kopin.pupil.util.PhoneUtils;
import com.kopin.solos.AppService;
import com.kopin.solos.core.R;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.wear.WatchModeActivity;
import java.util.List;

/* JADX INFO: loaded from: classes37.dex */
public class CallsAndMessages implements PhoneUtils.PhoneActionListener {
    public static final String CALL_INCOMING_ICON = "incoming_icon";
    public static final String CALL_OUTGOING_ICON = "outgoing_icon";
    public static final String CALL_TERMINATE_ICON = "terminate_icon";
    public static final String MESSAGE_ICON = "message_icon";
    private static final int SMS_MESSAGE_PARTS_HEADSET_TIME_MILLIS_SMS_PART = 4000;
    private static final int SMS_VIEW_HEADSET_NOTIFICATION_TIME_MILLIS = 10000;
    private static final String TAG = "PhoneUtils";
    private VCNotification.TextPart mCallMessage;
    private VCNotification.TextPart mCallName;
    private TelephonyManager mTelMan;
    private final AppService mVCApp;
    private boolean HFPConnected = false;
    private long mCallStartTime = 0;
    private boolean mCallReceived = false;
    private Runnable mEndCall = new Runnable() { // from class: com.kopin.solos.phone.CallsAndMessages.1
        @Override // java.lang.Runnable
        public void run() {
            CallsAndMessages.this.mVCApp.sendEndCall();
        }
    };
    private final PhoneStateListener mPhoneListener = new PhoneStateListener() { // from class: com.kopin.solos.phone.CallsAndMessages.4
        private String NAME_CALLING;
        private String NUMBER_CALLING;

        @Override // android.telephony.PhoneStateListener
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case 0:
                    if (this.NUMBER_CALLING != null) {
                        CallsAndMessages.this.onCallTerminated(this.NUMBER_CALLING, this.NAME_CALLING);
                    }
                    this.NUMBER_CALLING = null;
                    this.NAME_CALLING = null;
                    break;
                case 1:
                    this.NUMBER_CALLING = incomingNumber;
                    this.NAME_CALLING = ContactResolver.getNameFor(incomingNumber);
                    if (this.NAME_CALLING == null) {
                        this.NAME_CALLING = incomingNumber;
                    }
                    CallsAndMessages.this.onCallReceived(this.NUMBER_CALLING, this.NAME_CALLING);
                    break;
                case 2:
                    if (this.NUMBER_CALLING == null || this.NUMBER_CALLING.isEmpty()) {
                        this.NUMBER_CALLING = incomingNumber;
                    }
                    if (this.NAME_CALLING == null || this.NAME_CALLING.isEmpty()) {
                        this.NAME_CALLING = ContactResolver.getNameFor(incomingNumber);
                    }
                    CallsAndMessages.this.onCallAnswer(this.NUMBER_CALLING, this.NAME_CALLING);
                    break;
                case 3:
                    this.NUMBER_CALLING = incomingNumber;
                    this.NAME_CALLING = ContactResolver.getNameFor(incomingNumber);
                    if (this.NAME_CALLING == null) {
                        this.NAME_CALLING = incomingNumber;
                    }
                    CallsAndMessages.this.onCallInitiated(this.NUMBER_CALLING, this.NAME_CALLING);
                    break;
            }
        }
    };
    private final BroadcastReceiver mOutgoingCallsReceiver = new BroadcastReceiver() { // from class: com.kopin.solos.phone.CallsAndMessages.5
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String phoneNumber = getResultData();
            if (phoneNumber == null) {
                phoneNumber = intent.getStringExtra("android.intent.extra.PHONE_NUMBER");
            }
            Log.d(CallsAndMessages.TAG, "Dialling number: " + phoneNumber);
            CallsAndMessages.this.mPhoneListener.onCallStateChanged(3, phoneNumber);
        }
    };
    private final BroadcastReceiver mSmsReceiver = new BroadcastReceiver() { // from class: com.kopin.solos.phone.CallsAndMessages.6
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            Bundle extras;
            if (intent != null && (extras = intent.getExtras()) != null) {
                Object[] pdus = (Object[]) extras.get("pdus");
                StringBuilder message = new StringBuilder();
                String phoneNumber = null;
                for (Object obj : pdus) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) obj);
                    if (phoneNumber != null && !currentMessage.getDisplayOriginatingAddress().contentEquals(phoneNumber) && message.length() != 0) {
                        Log.i(CallsAndMessages.TAG, "New message from: " + phoneNumber + "; " + ((Object) message));
                        CallsAndMessages.this.onMessageReceived(CallsAndMessages.this.resolveNumber(phoneNumber), message.toString());
                        message = new StringBuilder();
                    }
                    phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    message.append(currentMessage.getDisplayMessageBody());
                }
                if (phoneNumber != null && message.length() != 0) {
                    Log.i(CallsAndMessages.TAG, "New message from: " + phoneNumber + "; " + ((Object) message));
                    CallsAndMessages.this.onMessageReceived(CallsAndMessages.this.resolveNumber(phoneNumber), message.toString());
                }
            }
        }
    };

    public CallsAndMessages(AppService vcapp) {
        this.mVCApp = vcapp;
        this.mTelMan = (TelephonyManager) vcapp.getSystemService(WatchModeActivity.MODE_PHONE);
    }

    public void onConnected() {
        PhoneUtils.addListener(this);
        PupilDevice.requestHFPStatus();
    }

    public void onDisconnected() {
        PhoneUtils.removeListener(this);
        this.mCallStartTime = 0L;
    }

    public void refresh() {
    }

    public boolean isOnCall() {
        return this.mCallStartTime > 0;
    }

    public String getCallTime() {
        if (this.mCallStartTime <= 0) {
            return "00:00";
        }
        long diff = (Utility.getTimeMilliseconds() - this.mCallStartTime) / 1000;
        long sec = diff % 60;
        long diff2 = diff / 60;
        long min = diff2 % 60;
        long hour = diff2 / 60;
        if (hour > 0) {
            return String.format("%02d:%02d:%02d", Long.valueOf(hour), Long.valueOf(min), Long.valueOf(sec));
        }
        return String.format("%02d:%02d", Long.valueOf(min), Long.valueOf(sec));
    }

    private void speakText(int priority, int msg) {
        speakText(priority, this.mVCApp.getString(msg));
    }

    private void speakText(int priority, int msg, String extra) {
        speakText(priority, String.format(this.mVCApp.getString(msg), extra));
    }

    private void speakText(int priority, String msg) {
        this.mVCApp.speakText(priority, msg);
    }

    private VCNotification.TextPart createNotificationText(int resId) {
        return createNotificationText(this.mVCApp.getString(resId));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public VCNotification.TextPart createNotificationText(String text) {
        return new VCNotification.TextPart(text);
    }

    private VCNotification.TextPart createNotificationText(int resId, int textSize) {
        return createNotificationText(this.mVCApp.getString(resId), textSize);
    }

    private VCNotification.TextPart createNotificationText(String text, int textSize) {
        return new VCNotification.TextPart(text, textSize);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String resolveNumber(String phoneNumber) {
        String name = ContactResolver.getNameFor(phoneNumber);
        return name == null ? phoneNumber : name;
    }

    public boolean onButtonPress(ButtonType button, boolean longPress) {
        switch (button) {
            case MAIN:
                if (longPress) {
                    if (this.mTelMan.getCallState() != 1) {
                        return false;
                    }
                    PupilDevice.sendEndCallPacket();
                    return true;
                }
                if (this.mTelMan.getCallState() != 2) {
                    return false;
                }
                PupilDevice.sendEndCallPacket();
                return true;
            case FRONT:
                if (!isOnCall()) {
                    return false;
                }
                this.mVCApp.changeVolume(false);
                return true;
            case BACK:
                if (!isOnCall()) {
                    return false;
                }
                this.mVCApp.changeVolume(true);
                return true;
            default:
                return false;
        }
    }

    @Override // com.kopin.pupil.util.PhoneUtils.PhoneActionListener
    public void onMessageReceived(String name, final String message) {
        this.mVCApp.sendWakeUp();
        if (Prefs.isVoconEnabled()) {
        }
        Runnable viewMessage = new Runnable() { // from class: com.kopin.solos.phone.CallsAndMessages.2
            @Override // java.lang.Runnable
            public void run() {
                boolean first = true;
                VCNotification.TextPart messagePart = CallsAndMessages.this.createNotificationText(message);
                List<VCNotification.TextPart> parts = CallsAndMessages.this.mVCApp.getSplitNotificationText(messagePart);
                for (VCNotification.TextPart part : parts) {
                    CallsAndMessages.this.mVCApp.sendDropDownNotification(CallsAndMessages.MESSAGE_ICON, 4000, false, (Runnable) null, first ? VCNotification.Priority.MESSAGE_FIRST_VIEW : VCNotification.Priority.MESSAGE, part);
                    first = false;
                }
                CallsAndMessages.this.mVCApp.speakText(AppService.NOTIFICATION_TTS, messagePart.getText());
            }
        };
        VCNotification.TextPart namePart = createNotificationText(name);
        VCNotification.TextPart messagePart = createNotificationText(R.string.notification_new_message, 16);
        speakText(AppService.NOTIFICATION_TTS, R.string.tts_notification_new_message, name);
        this.mVCApp.sendDropDownNotification(MESSAGE_ICON, 10000, false, viewMessage, VCNotification.Priority.MESSAGE, namePart, messagePart);
    }

    @Override // com.kopin.pupil.util.PhoneUtils.PhoneActionListener
    public void onPhoneStatus(int state, boolean hfpActive, boolean scoActive) {
        Log.d(TAG, "onPhoneStatus: " + state);
        switch (state) {
            case 7:
                this.HFPConnected = true;
                break;
            case 8:
            case 15:
                this.HFPConnected = false;
                break;
        }
    }

    @Override // com.kopin.pupil.util.PhoneUtils.PhoneActionListener
    public void onCallReceived(String phoneNumber) {
        onCallReceived(phoneNumber, resolveNumber(phoneNumber));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCallReceived(String phoneNumber, String name) {
        this.mCallStartTime = 0L;
        this.mVCApp.sendWakeUp();
        Runnable answerCall = new Runnable() { // from class: com.kopin.solos.phone.CallsAndMessages.3
            @Override // java.lang.Runnable
            public void run() {
                CallsAndMessages.this.mVCApp.sendAnswerCall();
            }
        };
        this.mCallName = createNotificationText(name);
        this.mCallMessage = createNotificationText(R.string.notification_incoming_call, 16);
        this.mVCApp.sendDropDownNotification(CALL_INCOMING_ICON, -1, false, answerCall, VCNotification.Priority.CALL, this.mCallName, this.mCallMessage);
        if (!Prefs.isVoconEnabled()) {
            speakText(AppService.NOTIFICATION_TTS, R.string.tts_notification_incoming_call, name);
        }
        this.mCallReceived = true;
    }

    @Override // com.kopin.pupil.util.PhoneUtils.PhoneActionListener
    public void onCallInitiated(String phoneNumber) {
        onCallInitiated(phoneNumber, resolveNumber(phoneNumber));
    }

    public void onCallInitiated(String phoneNumber, String name) {
        Log.d(TAG, "onCallInitiated: " + phoneNumber + " - " + name, new Exception("CallNotif"));
        this.mCallStartTime = 0L;
        this.mVCApp.sendWakeUp();
        this.mCallName = createNotificationText(name);
        this.mCallMessage = createNotificationText(R.string.notification_in_call, 16);
        this.mVCApp.sendDropDownNotification(CALL_OUTGOING_ICON, -1, false, this.mEndCall, VCNotification.Priority.CALL, this.mCallName, this.mCallMessage);
        if (!Prefs.isVoconEnabled()) {
            speakText(AppService.NOTIFICATION_TTS, R.string.tts_notification_in_call);
        }
        this.mCallReceived = false;
    }

    @Override // com.kopin.pupil.util.PhoneUtils.PhoneActionListener
    public void onCallAnswer(String phoneNumber) {
        onCallAnswer(phoneNumber, resolveNumber(phoneNumber));
    }

    public void onCallAnswer(String phoneNumber, String name) {
        Log.d(TAG, "onCallAnswered: " + phoneNumber + " - " + name, new Exception("CallNotif"));
        this.mVCApp.sendWakeUp();
        this.mCallStartTime = Utility.getTimeMilliseconds();
        createNotificationText(getCallTime());
        this.mCallName = createNotificationText(name);
        this.mCallMessage = createNotificationText(R.string.notification_in_call, 16);
        String icon = this.mCallReceived ? CALL_INCOMING_ICON : CALL_OUTGOING_ICON;
        this.mVCApp.sendDropDownNotification(icon, -1, false, this.mEndCall, VCNotification.Priority.CALL, this.mCallName, this.mCallMessage);
        if (!Prefs.isVoconEnabled()) {
            speakText(AppService.NOTIFICATION_TTS, R.string.tts_notification_in_call);
        }
    }

    @Override // com.kopin.pupil.util.PhoneUtils.PhoneActionListener
    public void onCallTerminated(String phoneNumber) {
        onCallTerminated(phoneNumber, resolveNumber(phoneNumber));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCallTerminated(String phoneNumber, String name) {
        Log.d(TAG, "onCallTerminated: " + phoneNumber + " - " + name, new Exception("CallNotif"));
        this.mVCApp.sendWakeUp();
        VCNotification.TextPart time = createNotificationText(getCallTime());
        this.mCallName = createNotificationText(name);
        this.mCallMessage = createNotificationText(R.string.notification_call_ended);
        this.mCallStartTime = 0L;
        this.mVCApp.sendDropDownNotification(CALL_TERMINATE_ICON, 2000, false, (Runnable) null, VCNotification.Priority.CALL, time, this.mCallName, this.mCallMessage);
        if (!Prefs.isVoconEnabled()) {
            speakText(AppService.NOTIFICATION_TTS, R.string.tts_notification_call_ended);
        }
        this.mVCApp.checkTTSSettings();
    }
}
