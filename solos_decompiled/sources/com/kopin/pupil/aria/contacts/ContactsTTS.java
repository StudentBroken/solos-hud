package com.kopin.pupil.aria.contacts;

import android.content.Context;
import com.kopin.aria.app.R;
import com.kopin.pupil.aria.tts.ConversationPoint;

/* JADX INFO: loaded from: classes21.dex */
public class ContactsTTS {
    public static ConversationPoint TTS_NO_NUMBER;

    static void init(Context context) {
        TTS_NO_NUMBER = ConversationPoint.build(context, "NoNumber", 0, R.array.tts_nonumber_silent, R.array.tts_nonumber_once, R.array.tts_nonumber_terse, R.array.tts_nonumber_verbose, R.array.tts_nonumber_verbose);
    }
}
