package com.digits.sdk.android;

import android.content.Context;
import android.os.ResultReceiver;
import android.text.TextWatcher;

/* JADX INFO: loaded from: classes18.dex */
interface DigitsController {
    void clearError();

    void executeRequest(Context context);

    int getErrorCount();

    ErrorCodes getErrors();

    TextWatcher getTextWatcher();

    void handleError(Context context, DigitsException digitsException);

    void onResume();

    void showTOS(Context context);

    void startFallback(Context context, ResultReceiver resultReceiver, DigitsException digitsException);

    boolean validateInput(CharSequence charSequence);
}
