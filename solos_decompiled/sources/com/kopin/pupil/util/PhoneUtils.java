package com.kopin.pupil.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.kopin.accessory.base.Packet;
import com.kopin.accessory.packets.PhoneNumberPacketContent;
import com.kopin.accessory.packets.base.BytePacketContent;
import com.kopin.accessory.utility.CallHelper;
import com.kopin.solos.wear.WatchModeActivity;
import java.util.HashSet;

/* JADX INFO: loaded from: classes25.dex */
public class PhoneUtils {
    private static final String TAG = "PhoneUtils";
    private static final String[] CALL_STATE_NAMES = {"NONE", "INCOMING_CALL", "INCOMING_CALL_ANSWERED", "INCOMING_CALL_TERMINATED", "OUTGOING_CALL_RINGING", "OUTGOING_CALL_ANSWERED", "OUTGOING_CALL_TERMINATED", "HFP_ENABLED", "HFP_DISABLED", "UNKNOWN_0x09", "OUTGOING_CALL_CONNECTING", "SCO_CONNECTED", "SCO_DISCONNECTED", "CALL_IN_PROGRESS", "CALL_ENDED", "HFP_DROPPED"};
    private static byte CALL_STATE = 0;
    private static boolean newState = false;
    private static boolean HFP_ACTIVE = false;
    private static boolean SCO_ACTIVE = false;
    private static String CALL_NUMBER = null;
    private static final HashSet<PhoneActionListener> mListeners = new HashSet<>();
    private static BroadcastReceiver mSMSReceiver = new BroadcastReceiver() { // from class: com.kopin.pupil.util.PhoneUtils.1
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
                        Log.i(PhoneUtils.TAG, "New message from: " + phoneNumber + "; " + ((Object) message));
                        PhoneUtils.onNewMessage(context, phoneNumber, message.toString());
                        message = new StringBuilder();
                    }
                    phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    message.append(currentMessage.getDisplayMessageBody());
                }
                if (phoneNumber != null && message.length() != 0) {
                    Log.i(PhoneUtils.TAG, "New message from: " + phoneNumber + "; " + ((Object) message));
                    PhoneUtils.onNewMessage(context, phoneNumber, message.toString());
                }
            }
        }
    };
    private static final PhoneStateListener mPhoneListener = new PhoneStateListener() { // from class: com.kopin.pupil.util.PhoneUtils.2
        private String NUMBER_CALLING;

        @Override // android.telephony.PhoneStateListener
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case 0:
                    if (this.NUMBER_CALLING != null) {
                        PhoneUtils.onCallTerminated(this.NUMBER_CALLING);
                    }
                    this.NUMBER_CALLING = null;
                    break;
                case 1:
                    this.NUMBER_CALLING = incomingNumber;
                    PhoneUtils.onCallReceived(this.NUMBER_CALLING);
                    break;
                case 2:
                    PhoneUtils.onCallAnswer(this.NUMBER_CALLING);
                    break;
            }
        }
    };
    private static final BroadcastReceiver mOutgoingCallsReceiver = new BroadcastReceiver() { // from class: com.kopin.pupil.util.PhoneUtils.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String phoneNumber = getResultData();
            if (phoneNumber == null) {
                phoneNumber = intent.getStringExtra("android.intent.extra.PHONE_NUMBER");
            }
            Log.d(PhoneUtils.TAG, "Dialling number: " + phoneNumber);
            PhoneUtils.onCallInitiated(phoneNumber);
        }
    };

    public interface PhoneActionListener {
        void onCallAnswer(String str);

        void onCallInitiated(String str);

        void onCallReceived(String str);

        void onCallTerminated(String str);

        void onMessageReceived(String str, String str2);

        void onPhoneStatus(int i, boolean z, boolean z2);
    }

    public static void init() {
    }

    public static void addListener(PhoneActionListener listener) {
        synchronized (mListeners) {
            if (!mListeners.contains(listener)) {
                mListeners.add(listener);
            }
        }
    }

    public static void removeListener(PhoneActionListener listener) {
        synchronized (mListeners) {
            mListeners.remove(listener);
        }
    }

    public static void register(Context context) {
        context.registerReceiver(mSMSReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
        context.registerReceiver(mOutgoingCallsReceiver, new IntentFilter("android.intent.action.NEW_OUTGOING_CALL"));
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(WatchModeActivity.MODE_PHONE);
        telephonyManager.listen(mPhoneListener, 32);
    }

    public static void unregister(Context context) {
        context.unregisterReceiver(mSMSReceiver);
        context.unregisterReceiver(mOutgoingCallsReceiver);
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(WatchModeActivity.MODE_PHONE);
        telephonyManager.listen(mPhoneListener, 0);
    }

    public static void onDiallingNumber(String number) {
        CALL_NUMBER = number;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void onNewMessage(Context context, String phoneNumber, String message) {
        String name = resolveName(context, phoneNumber);
        synchronized (mListeners) {
            for (PhoneActionListener listener : mListeners) {
                listener.onMessageReceived(name, message);
            }
        }
    }

    private static void onPhoneStatus(byte state, boolean hfpConnected, boolean scoActive) {
        synchronized (mListeners) {
            for (PhoneActionListener listener : mListeners) {
                listener.onPhoneStatus(state, hfpConnected, scoActive);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void onCallReceived(String phoneNumber) {
        synchronized (mListeners) {
            for (PhoneActionListener listener : mListeners) {
                listener.onCallReceived(phoneNumber);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void onCallInitiated(String phoneNumber) {
        synchronized (mListeners) {
            for (PhoneActionListener listener : mListeners) {
                listener.onCallInitiated(phoneNumber);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void onCallAnswer(String phoneNumber) {
        synchronized (mListeners) {
            for (PhoneActionListener listener : mListeners) {
                listener.onCallAnswer(phoneNumber);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void onCallTerminated(String phoneNumber) {
        synchronized (mListeners) {
            for (PhoneActionListener listener : mListeners) {
                listener.onCallTerminated(phoneNumber);
            }
        }
    }

    public static Packet onCallStateReceived(Packet packet) {
        boolean clearNumber = false;
        Packet reply = null;
        BytePacketContent response = (BytePacketContent) packet.getContent();
        byte callStatus = response.getValue();
        HFP_ACTIVE = (callStatus & CallHelper.CallState.FLAG_HFP_STATE) == 16;
        SCO_ACTIVE = (callStatus & CallHelper.CallState.FLAG_SCO_STATE) == 32;
        byte callStatus2 = (byte) (callStatus & 15);
        boolean ignore = false;
        Log.d("PhoneUtil", "call state: " + CALL_STATE_NAMES[callStatus2] + " (" + ((int) callStatus2) + "), HFP: " + (HFP_ACTIVE ? "ACTIVE" : "IDLE") + ", SCO: " + (SCO_ACTIVE ? "ACTIVE" : "IDLE"));
        switch (callStatus2) {
            case 1:
            case 2:
            case 5:
                if (CALL_NUMBER == null) {
                    reply = CallHelper.requestNumber();
                    newState = true;
                } else if (callStatus2 == 1) {
                    onCallReceived(CALL_NUMBER);
                } else {
                    onCallAnswer(CALL_NUMBER);
                }
                break;
            case 3:
            case 6:
                onCallTerminated(CALL_NUMBER);
                break;
            case 4:
            case 10:
                onCallInitiated(CALL_NUMBER);
                break;
            case 7:
                reply = CallHelper.requestState();
                break;
            case 8:
            case 15:
                SCO_ACTIVE = false;
                HFP_ACTIVE = false;
                clearNumber = true;
                break;
            case 11:
                ignore = true;
                SCO_ACTIVE = true;
                break;
            case 12:
                ignore = true;
            case 14:
                SCO_ACTIVE = false;
                break;
        }
        if (!ignore) {
            CALL_STATE = callStatus2;
        }
        onPhoneStatus(CALL_STATE, HFP_ACTIVE, SCO_ACTIVE);
        if (clearNumber) {
            CALL_NUMBER = null;
        }
        return reply;
    }

    public static void onCallNumberReceived(Packet packet) {
        PhoneNumberPacketContent info = (PhoneNumberPacketContent) packet.getContent();
        String PHONE_NUMBER = info.getNumber();
        Log.d("PhoneUtil", "call number: '" + PHONE_NUMBER + "'");
        if (CALL_NUMBER == null && PHONE_NUMBER != null && !PHONE_NUMBER.isEmpty()) {
            CALL_NUMBER = PHONE_NUMBER;
        }
        if (newState) {
            switch (CALL_STATE) {
                case 1:
                    onCallReceived(CALL_NUMBER);
                    break;
                case 2:
                    onCallAnswer(CALL_NUMBER);
                    break;
                case 3:
                    onCallTerminated(CALL_NUMBER);
                    CALL_NUMBER = null;
                    break;
                case 4:
                    onCallInitiated(CALL_NUMBER);
                    break;
                case 5:
                    onCallAnswer(CALL_NUMBER);
                    break;
                case 6:
                    onCallTerminated(CALL_NUMBER);
                    CALL_NUMBER = null;
                    break;
                case 10:
                    onCallInitiated(CALL_NUMBER);
                    break;
            }
        }
        newState = false;
    }

    public static String resolveName(Context context, String phoneNumber) {
        String name = phoneNumber;
        try {
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor.moveToNext()) {
                try {
                    name = cursor.getString(cursor.getColumnIndex("display_name"));
                } catch (Exception e) {
                }
            }
            cursor.close();
        } catch (Exception e2) {
        }
        return name;
    }
}
