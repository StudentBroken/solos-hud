package com.digits.sdk.android;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;

/* JADX INFO: loaded from: classes18.dex */
class FailureControllerImpl implements FailureController {
    final ActivityClassManager classManager;

    public FailureControllerImpl() {
        this(Digits.getInstance().getActivityClassManager());
    }

    public FailureControllerImpl(ActivityClassManager classManager) {
        this.classManager = classManager;
    }

    @Override // com.digits.sdk.android.FailureController
    public void tryAnotherNumber(Activity activity, ResultReceiver resultReceiver) {
        Intent intent = new Intent(activity, this.classManager.getPhoneNumberActivity());
        Bundle bundle = new Bundle();
        bundle.putParcelable(DigitsClient.EXTRA_RESULT_RECEIVER, resultReceiver);
        intent.putExtras(bundle);
        intent.setFlags(getFlags());
        activity.startActivity(intent);
    }

    @Override // com.digits.sdk.android.FailureController
    public void sendFailure(ResultReceiver resultReceiver, Exception exception) {
        Bundle bundle = new Bundle();
        bundle.putString("login_error", exception.getLocalizedMessage());
        resultReceiver.send(400, bundle);
    }

    @TargetApi(11)
    int getFlags() {
        return Build.VERSION.SDK_INT >= 11 ? 268468224 : 335544320;
    }
}
