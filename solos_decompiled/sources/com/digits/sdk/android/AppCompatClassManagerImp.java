package com.digits.sdk.android;

import android.app.Activity;

/* JADX INFO: loaded from: classes18.dex */
class AppCompatClassManagerImp implements ActivityClassManager {
    AppCompatClassManagerImp() {
    }

    @Override // com.digits.sdk.android.ActivityClassManager
    public Class<? extends Activity> getPhoneNumberActivity() {
        return PhoneNumberActionBarActivity.class;
    }

    @Override // com.digits.sdk.android.ActivityClassManager
    public Class<? extends Activity> getConfirmationActivity() {
        return ConfirmationCodeActionBarActivity.class;
    }

    @Override // com.digits.sdk.android.ActivityClassManager
    public Class<? extends Activity> getLoginCodeActivity() {
        return LoginCodeActionBarActivity.class;
    }

    @Override // com.digits.sdk.android.ActivityClassManager
    public Class<? extends Activity> getFailureActivity() {
        return FailureActionBarActivity.class;
    }

    @Override // com.digits.sdk.android.ActivityClassManager
    public Class<? extends Activity> getContactsActivity() {
        return ContactsActionBarActivity.class;
    }

    @Override // com.digits.sdk.android.ActivityClassManager
    public Class<? extends Activity> getPinCodeActivity() {
        return PinCodeActionBarActivity.class;
    }

    @Override // com.digits.sdk.android.ActivityClassManager
    public Class<? extends Activity> getEmailRequestActivity() {
        return EmailRequestActionBarActivity.class;
    }
}
