package com.digits.sdk.android;

import android.app.Activity;

/* JADX INFO: loaded from: classes18.dex */
public interface ActivityClassManager {
    Class<? extends Activity> getConfirmationActivity();

    Class<? extends Activity> getContactsActivity();

    Class<? extends Activity> getEmailRequestActivity();

    Class<? extends Activity> getFailureActivity();

    Class<? extends Activity> getLoginCodeActivity();

    Class<? extends Activity> getPhoneNumberActivity();

    Class<? extends Activity> getPinCodeActivity();
}
