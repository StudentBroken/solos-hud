package com.kopin.pupil.aria.tts;

import android.content.Context;
import com.kopin.pupil.aria.tts.AriaTTS;
import java.util.HashMap;

/* JADX INFO: loaded from: classes32.dex */
public class ConversationPoint {
    private boolean beenSaid;
    private int countSaid;
    private long lastSaid;
    private final HashMap<AriaTTS.SayPriority, String[]> mLines = new HashMap<>();
    private final String mTag;

    private ConversationPoint(String tag) {
        this.mTag = tag;
    }

    public String getTag() {
        return this.mTag;
    }

    public boolean shouldSay(AriaTTS.SayPriority forPriority) {
        return false;
    }

    public String selectText(AriaTTS.SayPriority forPriority) {
        String[] choices = this.mLines.get(forPriority);
        if (choices == null || choices.length <= 0) {
            return null;
        }
        int idx = (int) Math.floor(Math.random() * ((double) choices.length));
        return choices[idx];
    }

    public static ConversationPoint build(Context context, String tag, int... resIds) throws CloneNotSupportedException {
        String[] text;
        ConversationPoint self = new ConversationPoint(tag);
        for (AriaTTS.SayPriority pri : AriaTTS.SayPriority.values()) {
            int ord = pri.ordinal();
            if (resIds.length <= ord) {
                break;
            }
            if (resIds[ord] != 0 && (text = context.getResources().getStringArray(resIds[ord])) != null) {
                self.mLines.put(pri, text);
            }
        }
        return self;
    }
}
