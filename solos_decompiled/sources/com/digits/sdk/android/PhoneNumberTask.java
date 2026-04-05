package com.digits.sdk.android;

import io.fabric.sdk.android.services.concurrency.AsyncTask;

/* JADX INFO: loaded from: classes18.dex */
class PhoneNumberTask extends AsyncTask<Void, Void, PhoneNumber> {
    private final Listener listener;
    private final PhoneNumberUtils phoneNumberUtils;
    private final String providedPhoneNumber;

    interface Listener {
        void onLoadComplete(PhoneNumber phoneNumber);
    }

    protected PhoneNumberTask(PhoneNumberUtils phoneNumberUtils, Listener listener) {
        if (phoneNumberUtils == null) {
            throw new NullPointerException("phoneNumberUtils can't be null");
        }
        this.listener = listener;
        this.phoneNumberUtils = phoneNumberUtils;
        this.providedPhoneNumber = "";
    }

    protected PhoneNumberTask(PhoneNumberUtils phoneNumberUtils, String providedPhoneNumber, Listener listener) {
        if (phoneNumberUtils == null) {
            throw new NullPointerException("phoneNumberUtils can't be null");
        }
        this.listener = listener;
        this.phoneNumberUtils = phoneNumberUtils;
        this.providedPhoneNumber = providedPhoneNumber;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.fabric.sdk.android.services.concurrency.AsyncTask
    public PhoneNumber doInBackground(Void... params) {
        return this.phoneNumberUtils.getPhoneNumber(this.providedPhoneNumber);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.fabric.sdk.android.services.concurrency.AsyncTask
    public void onPostExecute(PhoneNumber phoneNumber) {
        if (this.listener != null) {
            this.listener.onLoadComplete(phoneNumber);
        }
    }
}
