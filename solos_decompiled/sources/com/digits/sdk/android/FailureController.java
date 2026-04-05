package com.digits.sdk.android;

import android.app.Activity;
import android.os.ResultReceiver;

/* JADX INFO: loaded from: classes18.dex */
interface FailureController {
    void sendFailure(ResultReceiver resultReceiver, Exception exc);

    void tryAnotherNumber(Activity activity, ResultReceiver resultReceiver);
}
