package com.kopin.pupil.aria.app;

import android.content.Context;
import com.kopin.aria.app.R;
import com.kopin.pupil.aria.tts.ConversationPoint;

/* JADX INFO: loaded from: classes21.dex */
public class CommonTTS {
    public static ConversationPoint OK;
    public static ConversationPoint SORRY_DIDNT_UNDERSTAND;

    static void init(Context context) {
        SORRY_DIDNT_UNDERSTAND = ConversationPoint.build(context, "Sorry", 0, R.array.tts_common_silent, R.array.tts_common_once, R.array.tts_common_terse, R.array.tts_common_verbose, R.array.tts_common_verbose);
        OK = ConversationPoint.build(context, "OK", 0, 0, 0, 0, R.array.tts_ok_hint, R.array.tts_ok_verbose);
    }
}
