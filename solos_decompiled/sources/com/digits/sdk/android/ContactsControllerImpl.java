package com.digits.sdk.android;

import android.content.Context;
import android.content.Intent;

/* JADX INFO: loaded from: classes18.dex */
class ContactsControllerImpl implements ContactsController {
    ContactsControllerImpl() {
    }

    @Override // com.digits.sdk.android.ContactsController
    public void startUploadService(Context context) {
        context.startService(new Intent(context, (Class<?>) ContactsUploadService.class));
    }
}
