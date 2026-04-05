package com.digits.sdk.android;

import android.app.Activity;

/* JADX INFO: loaded from: classes18.dex */
class ActivityClassManagerImp implements ActivityClassManager {
    ActivityClassManagerImp() {
    }

    @Override // com.digits.sdk.android.ActivityClassManager
    public Class<? extends Activity> getPhoneNumberActivity() {
        return PhoneNumberActivity.class;
    }

    @Override // com.digits.sdk.android.ActivityClassManager
    public Class<? extends Activity> getConfirmationActivity() {
        return ConfirmationCodeActivity.class;
    }

    @Override // com.digits.sdk.android.ActivityClassManager
    public Class<? extends Activity> getLoginCodeActivity() {
        return LoginCodeActivity.class;
    }

    @Override // com.digits.sdk.android.ActivityClassManager
    public Class<? extends Activity> getFailureActivity() {
        return FailureActivity.class;
    }

    @Override // com.digits.sdk.android.ActivityClassManager
    public Class<? extends Activity> getContactsActivity() {
        return ContactsActivity.class;
    }

    @Override // com.digits.sdk.android.ActivityClassManager
    public Class<? extends Activity> getPinCodeActivity() {
        return PinCodeActivity.class;
    }

    @Override // com.digits.sdk.android.ActivityClassManager
    public Class<? extends Activity> getEmailRequestActivity() {
        return EmailRequestActivity.class;
    }
}
