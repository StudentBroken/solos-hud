package com.kopin.pupil.aria.app;

import android.content.Context;
import com.kopin.aria.app.R;

/* JADX INFO: loaded from: classes21.dex */
public class CommonGrammar {
    public static String[] NO_CANCEL;
    public static String[] NO_CANCEL_RETRY;
    public static String[] OK_CANCEL_RETRY;
    public static String[] RETRY_TRY_AGAIN;
    public static String[] YES_NO_OK_CANCEL;
    public static String[] YES_OK;

    static void init(Context context) {
        YES_NO_OK_CANCEL = context.getResources().getStringArray(R.array.common_confirm_or_cancel);
        YES_OK = context.getResources().getStringArray(R.array.common_confirm);
        NO_CANCEL = context.getResources().getStringArray(R.array.common_cancel);
        NO_CANCEL_RETRY = context.getResources().getStringArray(R.array.common_cancel_or_retry);
        RETRY_TRY_AGAIN = context.getResources().getStringArray(R.array.common_retry);
        OK_CANCEL_RETRY = context.getResources().getStringArray(R.array.common_confirm_or_retry);
    }
}
